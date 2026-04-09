# JamVM Native Lambda Implementation Spec

This document describes the native methods that JamVM needs to implement
to support the `java.lang.invoke` classes in GNU Classpath.

## Overview

Lambda expressions compile to `invokedynamic` bytecode that calls
`LambdaMetafactory.metafactory()` as the bootstrap method. This returns
a `CallSite` containing a `MethodHandle` that constructs lambda proxy objects.

The proxy class is trivial:
- Implements the functional interface
- Has fields for captured values
- Constructor initializes fields
- SAM method delegates to the implementation method

## Native Methods to Implement

### 1. LambdaMetafactory.nativeMetafactory()

```c
// Java signature:
// private static native MethodHandle nativeMetafactory(
//     Class<?> callerClass,
//     String invokedName,          // SAM method name (e.g., "run", "apply")
//     MethodType invokedType,      // (captured...) -> FunctionalInterface
//     MethodType samMethodType,    // Erased SAM signature
//     MethodHandle implMethod,     // Handle to lambda body
//     MethodType instantiatedMethodType,  // Actual SAM signature
//     boolean serializable,
//     Class<?>[] markerInterfaces,
//     MethodType[] bridges
// ) throws LambdaConversionException;
```

**What it does:**
1. Generate a proxy class (see below)
2. Define it in the VM (use existing `defineClass` infrastructure)
3. Return a MethodHandle to the proxy's constructor

**Proxy class structure:**
```
class CallerClass$$Lambda$1 implements FunctionalInterface {
    // Fields for captured values (from invokedType parameter types)
    private final Foo cap$0;
    private final Bar cap$1;
    
    // Constructor
    CallerClass$$Lambda$1(Foo cap$0, Bar cap$1) {
        this.cap$0 = cap$0;
        this.cap$1 = cap$1;
    }
    
    // SAM method - delegates to implMethod
    public ReturnType samMethodName(Args...) {
        return ImplClass.lambdaBody(cap$0, cap$1, args...);
    }
}
```

**Bytecode generation approach:**
- JamVM already has class parsing/creation infrastructure
- Generate bytecode directly in C (it's simple, ~200-300 bytes per class)
- Or use `defineAnonymousClass` if available

### 2. MethodHandle.invokeExact() / invoke()

```c
// Java signature:
// public final native Object invokeExact(Object... args) throws Throwable;
// public final native Object invoke(Object... args) throws Throwable;
```

**What it does:**
- Extract target method from MethodHandle structure
- Invoke it with provided arguments
- `invokeExact`: types must match exactly
- `invoke`: perform type conversions as needed

**Implementation notes:**
- MethodHandle subtypes (DirectMethodHandle, BoundMethodHandle, etc.) have different invoke logic
- Check `MethodHandle.type` field for expected signature
- Use existing reflection/invoke infrastructure

### 3. MethodHandles.getCallerClass()

```c
// Java signature:
// private static native Class<?> getCallerClass();
```

**What it does:**
- Walk the stack
- Return the class of the caller (skip internal frames)

**Implementation:**
- Similar to existing `Reflection.getCallerClass()` if available
- Or use JamVM's stack walking APIs

### 4. MethodHandles.Lookup native methods

```c
// Find a method and return a MethodHandle
private native MethodHandle nativeFindMethod(
    Class<?> refc, String name, MethodType type, boolean isStatic);

private native MethodHandle nativeFindConstructor(
    Class<?> refc, MethodType type);

private native MethodHandle nativeFindGetter(
    Class<?> refc, String name, Class<?> type, boolean isStatic);

private native MethodHandle nativeFindSetter(
    Class<?> refc, String name, Class<?> type, boolean isStatic);

private native MethodHandle nativeUnreflect(Method m);
private native MethodHandle nativeUnreflectConstructor(Constructor<?> c);
private native MethodHandle nativeUnreflectGetter(Field f);
private native MethodHandle nativeUnreflectSetter(Field f);
```

**What they do:**
- Look up the member using reflection-like logic
- Check access permissions against `lookupClass` and `allowedModes`
- Create a DirectMethodHandle pointing to the member

**Implementation:**
- Reuse existing method/field resolution code
- Create MethodHandle object with appropriate type and target

### 5. MutableCallSite / VolatileCallSite native methods

```c
// private native MethodHandle makeDynamicInvoker();
// private static native void nativeSyncAll(MutableCallSite[] sites);
```

**What they do:**
- `makeDynamicInvoker`: Return a MethodHandle that reads target on each invoke
- `nativeSyncAll`: Memory barrier to ensure target updates are visible

## Proxy Class Bytecode Template

For a lambda `(String s) -> s.length()` targeting `Function<String, Integer>`:

```
// Class header
magic: 0xCAFEBABE
minor_version: 0
major_version: 52 (Java 8)
access_flags: ACC_FINAL | ACC_SYNTHETIC

// Constant pool entries needed:
// - Class refs: this class, Function, Object, String, Integer
// - Method refs: Object.<init>, String.length, Integer.valueOf
// - Field refs: captured fields
// - NameAndType entries
// - UTF8 strings

// Fields (for captures):
// private final <CapturedType> cap$N;

// Methods:
// <init>(captured types...) - calls super(), assigns fields
// apply(Object) -> Object - casts, calls impl, boxes result
```

## Recommended Implementation Order

1. **MethodType** - Already pure Java, nothing native needed
2. **MethodHandles.getCallerClass()** - Simple stack walk
3. **Lookup.nativeFindMethod/nativeUnreflect** - Reuse reflection
4. **MethodHandle.invoke()** - Core invocation
5. **LambdaMetafactory.nativeMetafactory()** - Proxy generation (most complex)

## Testing

Minimal test case:
```java
public class LambdaTest {
    public static void main(String[] args) {
        Runnable r = () -> System.out.println("Hello Lambda!");
        r.run();
    }
}
```

Compile with: `javac -source 8 -target 8 LambdaTest.java`

The `invokedynamic` instruction will call `LambdaMetafactory.metafactory()`
which delegates to `nativeMetafactory()`.

## Reference

- OpenJDK LambdaMetafactory: `jdk/src/share/classes/java/lang/invoke/`
- OpenJDK InnerClassLambdaMetafactory: Shows proxy class generation
- JamVM existing reflection code: Good starting point for method lookup
