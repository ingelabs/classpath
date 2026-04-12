# Lambda Performance Analysis: JamVM vs OpenJDK

## Executive Summary

Lambda performance on JamVM will be **correct but slower** than OpenJDK due to:
1. Interpreter vs JIT execution model
2. Missing MethodHandle optimizations
3. Missing non-capturing lambda singleton optimization

For typical applications, the difference is negligible. For hot loops with
millions of lambda invocations, the gap widens significantly.

---

## Part 1: Lambda vs Anonymous Inner Class (on JamVM)

### Cold Path (First Invocation)

| Phase | Anonymous Inner Class | Lambda |
|-------|----------------------|--------|
| Class exists at runtime | Yes (compiled into .class file) | No (generated at runtime) |
| Work on first use | `new` + constructor | Generate bytecode, define class, link CallSite |
| Relative cost | 1x | 10-50x |

**Lambda is slower** on first invocation due to runtime class generation.

### Warm Path (Subsequent Invocations)

| Phase | Anonymous Inner Class | Lambda |
|-------|----------------------|--------|
| Instance creation | `new` + `invokespecial` | `invokedynamic` → MethodHandle → constructor |
| Method call | `invokeinterface` | `invokeinterface` |
| Relative cost | 1x | 1.1-1.5x |

**Nearly equivalent** once CallSite is linked. Small overhead from MethodHandle dispatch.

### Non-Capturing Comparison

| Scenario | Anonymous Inner Class | Lambda (JamVM) |
|----------|----------------------|----------------|
| Non-capturing | New instance each call | New instance each call |
| Memory impact | N allocations | N allocations |

Both allocate on every invocation in our implementation.

**Optimization opportunity**: Lambda can cache singleton for non-capturing case (see below).

### Capturing Comparison

| Scenario | Anonymous Inner Class | Lambda |
|----------|----------------------|--------|
| Captures | Fields initialized in constructor | Fields initialized in constructor |
| Structure | Identical | Identical |

For capturing lambdas, the proxy class structure is essentially identical
to what an anonymous inner class would look like.

### Summary: JamVM Lambda vs Inner Class

| Aspect | Winner | Margin |
|--------|--------|--------|
| Cold start | Inner class | 10-50x |
| Warm invocation | Tie | ~1x |
| Code readability | Lambda | N/A |
| Bytecode size | Lambda | Smaller .class files |

---

## Part 2: JamVM Lambda vs OpenJDK Lambda

### Class Generation (Cold Path)

Isolating the implementation approach from VM execution:

| Aspect | JamVM (Native C) | OpenJDK (Java + ASM) |
|--------|-----------------|----------------------|
| Code generating bytecode | ~500 lines C | ~2000+ lines Java |
| Abstraction layers | 1 (C → VM) | 3+ (Java → ASM → bytes → VM) |
| Object allocations | Few (C malloc) | Many (ClassWriter, MethodVisitor, byte[]) |
| Cold path speed | **Faster** | Slower |

**For class generation alone, JamVM's native approach is leaner.**

On JamVM (interpreter), the ASM approach would be significantly slower
because all that Java code runs interpreted.

### MethodHandle Dispatch (Warm Path)

| Aspect | JamVM | OpenJDK |
|--------|-------|---------|
| MethodHandle.invoke() | Native call, dynamic dispatch | LambdaForm compiled bytecode |
| MethodHandle chains | Walk chain at runtime | Pre-compiled to single method |
| Bound arguments | Fetched from objects each call | Baked into generated code |

OpenJDK's `LambdaForm` system generates specialized bytecode for each
unique MethodHandle chain, avoiding interpretation overhead.

Example: `mh.bindTo(x).asType(t).invoke(args)`

| | JamVM | OpenJDK |
|-|-------|---------|
| Representation | 3 linked MethodHandle objects | Single compiled LambdaForm |
| Invocation cost | O(chain length) | O(1) |

### JIT Compilation Effects

| Scenario | JamVM | OpenJDK HotSpot |
|----------|-------|-----------------|
| Lambda call in loop | Interpreted dispatch every time | JIT inlines to direct call |
| MethodHandle.invoke() | Native function call | Completely inlined away |
| Escape analysis | None | Can eliminate allocation |

**HotSpot advantage**: After warmup, a lambda call can become as fast as
a direct method call through JIT inlining.

### Non-Capturing Singleton Optimization

| | JamVM (current) | OpenJDK |
|-|-----------------|---------|
| Non-capturing lambda | New instance per invocation | Singleton cached |
| Memory for `() -> foo()` in loop | N objects | 1 object |

OpenJDK detects `invokedType.parameterCount() == 0` and caches a single instance.

### Quantitative Performance Estimates

| Scenario | JamVM | OpenJDK | JamVM/OpenJDK Ratio |
|----------|-------|---------|---------------------|
| Class generation (cold) | ~0.5ms | ~2ms | 0.25x (JamVM faster) |
| Single invocation (warm) | ~1μs | ~0.5μs | 2x slower |
| Tight loop (1M calls) | ~500ms | ~5ms | 100x slower |
| With JIT steady state | ~500ms | ~1ms | 500x slower |

**Note**: The large gap in hot loops is the interpreter vs JIT difference,
not lambda-specific. Any interpreted code would show similar gaps.

---

## Part 3: Optimization Opportunities

### 1. Non-Capturing Singleton (Easy, High Impact)

```c
// In nativeMetafactory:
if (invokedType.parameterCount() == 0) {
    // Generate class
    Class proxyClass = defineProxyClass(...);
    // Create single instance
    Object instance = instantiate(proxyClass);
    // Return handle that always returns this instance
    return createConstantHandle(instance);
} else {
    // Return handle to constructor (normal path)
    return createConstructorHandle(proxyClass);
}
```

**Effort**: ~30 lines of code
**Impact**: Eliminates allocation for stateless lambdas (very common)

### 2. Proxy Class Caching (Medium, Medium Impact)

Cache generated proxy classes by their specification:
- Same functional interface
- Same captured types
- Same implementation method

Avoids regenerating identical classes in loops.

### 3. Inline Common Functional Interfaces (Medium, Low Impact)

Pre-generate proxy classes for common cases:
- `Runnable`
- `Callable<T>`
- `Consumer<T>`
- `Supplier<T>`

Saves generation time for most common lambdas.

### 4. MethodHandle Specialization (Hard, Medium Impact)

Generate specialized invoke stubs for common signatures:
- `()V` (Runnable.run)
- `(Object)Object` (Function.apply)
- `(Object)V` (Consumer.accept)

Avoids generic Object[] boxing/unboxing.

---

## Part 4: Conclusions

### What JamVM Lambda Will Be

| Attribute | Assessment |
|-----------|------------|
| Correctness | Full Java 8 lambda/method reference support |
| Compatibility | Runs all valid Java 8 lambda code |
| Cold performance | Good (native class generation is fast) |
| Warm performance | Acceptable (small MethodHandle overhead) |
| Hot performance | Limited by interpreter (not lambda-specific) |

### Recommendations

1. **Implement basic support first** - correctness over optimization
2. **Add singleton optimization early** - low effort, high impact
3. **Skip complex optimizations** - JIT gap dominates anyway
4. **Profile real workloads** - optimize based on actual usage patterns

### When Lambda Performance Matters

| Use Case | Lambda OK? |
|----------|------------|
| Application code, occasional lambdas | Yes |
| Streams-style processing (if added) | Yes, for small/medium data |
| Inner loops, millions of invocations | Consider inner classes |
| Real-time / low-latency | Avoid lambdas in hot paths |

### Final Assessment

JamVM's lambda implementation will be **correct and usable** for typical
Java 8 applications. The performance gap vs OpenJDK is dominated by the
interpreter vs JIT difference, not the lambda implementation itself.

For users choosing JamVM, they've already accepted interpreter-level
performance. Lambda support enables running modern Java code, which is
the primary goal.
