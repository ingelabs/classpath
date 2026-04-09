# Native Lambda Proxy Implementation Plan

## Overview

This document outlines how to implement native lambda proxy class generation in JamVM,
eliminating the need for the ASM bytecode library (~70 files, ~15K lines) in GNU Classpath.

## Current Architecture (OpenJDK way)

```
Java Compiler                    JVM Runtime
┌─────────────┐                 ┌──────────────────────────────────────────┐
│ Lambda      │                 │ invokedynamic                            │
│ Expression  │ ─compile──────► │     │                                    │
│             │                 │     ▼                                    │
│ name -> ... │                 │ LambdaMetafactory.metafactory()          │
└─────────────┘                 │     │                                    │
                                │     ▼                                    │
                                │ InnerClassLambdaMetafactory (Java)       │
                                │     │                                    │
                                │     ▼                                    │
                                │ ASM bytecode generation (Java, ~70 files)│
                                │     │                                    │
                                │     ▼                                    │
                                │ Unsafe.defineAnonymousClass (native)     │
                                │     │                                    │
                                │     ▼                                    │
                                │ Lambda$$1 proxy class                    │
                                └──────────────────────────────────────────┘
```

## Proposed Architecture (Native way)

```
Java Compiler                    JVM Runtime
┌─────────────┐                 ┌──────────────────────────────────────────┐
│ Lambda      │                 │ invokedynamic                            │
│ Expression  │ ─compile──────► │     │                                    │
│             │                 │     ▼                                    │
│ name -> ... │                 │ LambdaMetafactory.metafactory()          │
└─────────────┘                 │     │                                    │
                                │     ▼                                    │
                                │ native createLambdaProxyClass() in C     │
                                │     │   (generates bytecode directly)    │
                                │     ▼                                    │
                                │ Lambda$$1 proxy class                    │
                                └──────────────────────────────────────────┘
```

## Lambda Proxy Class Structure

A lambda proxy is trivially simple:

```java
// For: Function<String,String> f = name -> prefix + name;
// Where prefix is captured

final class HostClass$$Lambda$1 implements Function<String,String> {
    private final String arg$0;  // captured variable
    
    HostClass$$Lambda$1(String arg$0) {
        this.arg$0 = arg$0;
    }
    
    public String apply(String arg) {
        return HostClass.lambda$method$0(this.arg$0, arg);
    }
}
```

## Implementation Steps

### Phase 1: JamVM Native Implementation

**File: `jamvm/src/classlib/gnuclasspath/lambda.c`**

1. **Bytecode generation helpers**
   - `BytecodeBuffer` - dynamically growing byte buffer
   - `ConstantPoolBuilder` - builds constant pool entries
   - Type descriptor utilities

2. **`createLambdaProxyClass()` function**
   ```c
   Class *createLambdaProxyClass(
       Class *host_class,           // For naming and access
       Class *func_interface,       // Interface to implement
       MethodBlock *sam_method,     // SAM method signature
       MethodBlock *impl_method,    // Lambda body method
       Class **captured_types,      // Types of captured vars
       int captured_count           // Number of captured vars
   );
   ```

3. **Native method registration**
   - Register `nativeLambdaMetafactory` for Java to call

### Phase 2: Minimal Java LambdaMetafactory

**File: `classpath/java/lang/invoke/LambdaMetafactory.java`**

```java
public class LambdaMetafactory {
    public static CallSite metafactory(
            MethodHandles.Lookup caller,
            String invokedName,
            MethodType invokedType,
            MethodType samMethodType,
            MethodHandle implMethod,
            MethodType instantiatedMethodType) throws LambdaConversionException {
        
        // Call native implementation
        Class<?> proxyClass = nativeCreateLambdaProxy(
            caller.lookupClass(),
            invokedType.returnType(),  // functional interface
            invokedName,               // SAM method name
            samMethodType,
            implMethod,
            invokedType.parameterArray()  // captured types
        );
        
        // Create constructor method handle
        MethodHandle constructor = caller.findConstructor(proxyClass, 
            MethodType.methodType(void.class, invokedType.parameterArray()));
        
        return new ConstantCallSite(constructor.asType(invokedType));
    }
    
    private static native Class<?> nativeCreateLambdaProxy(
        Class<?> hostClass,
        Class<?> functionalInterface,
        String samMethodName,
        MethodType samMethodType,
        MethodHandle implMethod,
        Class<?>[] capturedTypes
    );
}
```

### Phase 3: Files to Remove from GNU Classpath

After native implementation works:

```
REMOVE: jdk/internal/org/objectweb/asm/**     (~67 files)
REMOVE: java/lang/invoke/InnerClassLambdaMetafactory.java
REMOVE: java/lang/invoke/InvokerBytecodeGenerator.java
REMOVE: java/lang/invoke/LambdaFormBuffer.java
REMOVE: java/lang/invoke/LambdaFormEditor.java
SIMPLIFY: java/lang/invoke/LambdaForm.java (much simpler)
```

## Bytecode to Generate

### Constant Pool (simplified)
```
#1  = Utf8        HostClass$$Lambda$1
#2  = Class       #1
#3  = Utf8        java/lang/Object
#4  = Class       #3
#5  = Utf8        java/util/function/Function
#6  = Class       #5
#7  = Utf8        arg$0
#8  = Utf8        Ljava/lang/Object;
#9  = Utf8        <init>
#10 = Utf8        (Ljava/lang/Object;)V
#11 = NameAndType #9:#10
#12 = Methodref   #4.#11     // Object.<init>
#13 = NameAndType #7:#8
#14 = Fieldref    #2.#13     // this.arg$0
#15 = Utf8        apply
#16 = Utf8        (Ljava/lang/Object;)Ljava/lang/Object;
#17 = Utf8        HostClass
#18 = Class       #17
#19 = Utf8        lambda$main$0
#20 = Utf8        (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
#21 = NameAndType #19:#20
#22 = Methodref   #18.#21    // HostClass.lambda$main$0
```

### Constructor Bytecode
```
aload_0              // this
invokespecial #12    // Object.<init>()V
aload_0              // this
aload_1              // arg0 (captured)
putfield #14         // this.arg$0 = arg0
return
```

### SAM Method Bytecode
```
aload_0              // this
getfield #14         // this.arg$0 (captured var)
aload_1              // method arg
invokestatic #22     // HostClass.lambda$main$0(captured, arg)
areturn
```

## Testing Plan

1. **Simple lambda** - `() -> 42`
2. **Lambda with arg** - `x -> x + 1`
3. **Lambda with capture** - `prefix -> (x -> prefix + x)`
4. **Method reference** - `String::length`
5. **Constructor reference** - `ArrayList::new`

## Comparison

| Aspect | ASM Approach | Native Approach |
|--------|-------------|-----------------|
| Java code | ~15K lines | ~200 lines |
| Native code | minimal | ~500 lines |
| Complexity | High | Low |
| Build time | Slower | Faster |
| Dependencies | ASM library | None |
| Flexibility | High | Limited to lambdas |

## Risks and Mitigations

1. **Edge cases**: Serializable lambdas, bridge methods
   - Mitigation: Implement only common cases first, fall back to error for edge cases

2. **Type erasure**: Need to handle generic types correctly
   - Mitigation: Use Object and casts like OpenJDK does

3. **Method handles**: Still need basic MethodHandle/MethodType support
   - Mitigation: Keep minimal java.lang.invoke classes

## Conclusion

The lambda proxy class is simple enough to generate directly in native code.
This eliminates a significant amount of complexity from GNU Classpath while
leveraging JamVM's existing class creation infrastructure.
