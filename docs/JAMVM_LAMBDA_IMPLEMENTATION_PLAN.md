# JamVM Lambda Implementation Plan

## Architecture Decision

All java.lang.invoke classes will live in JamVM's classlib, not GNU Classpath.

### Rationale
1. **Tight coupling** - 5 of 12 classes need native methods
2. **Simpler** - no VM* intermediate classes needed
3. **Self-contained** - Java classes and native code together
4. **JamVM-specific anyway** - other VMs would need their own implementation

### Exception
These pure-Java classes could optionally stay in Classpath:
- `@FunctionalInterface` (annotation)
- `BootstrapMethodError` (error class)

## Classes to Implement

### Pure Java (no native methods)

| Class | Purpose |
|-------|---------|
| MethodType | Method signature descriptor |
| CallSite | Abstract base for call sites |
| ConstantCallSite | Immutable call site |
| LambdaConversionException | Exception |
| WrongMethodTypeException | Exception |

### Require Native Methods

| Class | Native Methods |
|-------|----------------|
| MethodHandle | `invoke()`, `invokeExact()` |
| MethodHandles | `getCallerClass()` |
| MethodHandles.Lookup | `nativeFindMethod()`, `nativeFindConstructor()`, `nativeFindGetter()`, `nativeFindSetter()`, `nativeUnreflect()`, `nativeUnreflectConstructor()`, `nativeUnreflectGetter()`, `nativeUnreflectSetter()` |
| MutableCallSite | `makeDynamicInvoker()`, `nativeSyncAll()` |
| VolatileCallSite | `makeDynamicInvoker()` |
| LambdaMetafactory | `nativeMetafactory()` |

## Native Implementation Details

### LambdaMetafactory.nativeMetafactory()

This is the core function. When invoked:

1. **Input**: caller class, method name, types, implementation handle
2. **Generate proxy class bytecode**:
   - Class implementing functional interface
   - Fields for captured values
   - Constructor initializing fields
   - SAM method delegating to implementation
3. **Define class** using VM's defineClass infrastructure
4. **Return** MethodHandle to constructor

### Proxy Class Structure

For: `Function<String, Integer> f = s -> s.length();`

```java
class Caller$$Lambda$1 implements Function {
    // No fields (non-capturing)
    
    public Object apply(Object arg) {
        return Caller.lambda$0((String)arg);
    }
}
```

For: `String prefix = ">"; Function<String,String> f = s -> prefix + s;`

```java
class Caller$$Lambda$2 implements Function {
    private final String cap$0;
    
    Caller$$Lambda$2(String cap$0) {
        this.cap$0 = cap$0;
    }
    
    public Object apply(Object arg) {
        return Caller.lambda$1(cap$0, (String)arg);
    }
}
```

### MethodHandle.invoke() / invokeExact()

1. Extract target method from MethodHandle structure
2. Handle different MethodHandle types (Direct, Bound, AsType)
3. Invoke using existing VM reflection infrastructure

### Implementation Order

1. **MethodType** - pure Java, foundation
2. **MethodHandle** - base class with native stubs
3. **MethodHandles.getCallerClass()** - stack walking
4. **MethodHandles.Lookup.nativeFindMethod()** family - reuse reflection
5. **MethodHandle.invoke()** - core invocation
6. **CallSite classes** - mostly pure Java
7. **LambdaMetafactory.nativeMetafactory()** - proxy generation

## Bytecode Generation

The proxy class bytecode is simple (~200-300 bytes). Structure:

```
ClassFile {
    magic: 0xCAFEBABE
    version: 52.0 (Java 8)
    access_flags: ACC_FINAL | ACC_SYNTHETIC
    
    constant_pool:
        - Class refs (this, interface, Object)
        - Method refs (Object.<init>, impl method)
        - Field refs (captured fields)
        - NameAndType, UTF8 entries
    
    fields:
        - private final <Type> cap$N  (for each capture)
    
    methods:
        - <init>(captured types...) 
        - SAM method delegating to impl
}
```

JamVM already has bytecode parsing infrastructure that can be leveraged
for bytecode generation.

## Testing

### Minimal Test
```java
public class LambdaTest {
    public static void main(String[] args) {
        Runnable r = () -> System.out.println("Hello Lambda!");
        r.run();
    }
}
```

### With Captures
```java
public class CaptureTest {
    public static void main(String[] args) {
        String msg = "Hello";
        Runnable r = () -> System.out.println(msg);
        r.run();
    }
}
```

### Method Reference
```java
public class MethodRefTest {
    public static void main(String[] args) {
        java.util.Arrays.asList("a", "b", "c").forEach(System.out::println);
    }
}
```

## Verification Steps

1. Build JamVM with new classes
2. Compile tests: `javac -source 8 -target 8 *.java`
3. Run tests: `jamvm LambdaTest`
4. Verify output matches expected behavior
