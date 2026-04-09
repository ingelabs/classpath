/* LambdaMetafactory.java -- Bootstrap methods for lambda expressions
   Copyright (C) 2024 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA. */

package java.lang.invoke;

/**
 * Methods to facilitate the creation of simple function objects that
 * implement one or more interfaces by delegation to a provided MethodHandle.
 *
 * This is the bootstrap method that javac uses for lambda expressions.
 * The actual proxy class generation is delegated to native code in the VM.
 *
 * @since 1.8
 */
public class LambdaMetafactory {

    /** Flag for serializable lambdas */
    public static final int FLAG_SERIALIZABLE = 1 << 0;

    /** Flag for lambdas with additional marker interfaces */
    public static final int FLAG_MARKERS = 1 << 1;

    /** Flag for lambdas with additional bridges */
    public static final int FLAG_BRIDGES = 1 << 2;

    private LambdaMetafactory() {} // non-instantiable

    /**
     * Facilitates the creation of simple function objects.
     * This is the standard bootstrap method for lambda expressions.
     *
     * @param caller Stacked automatically by the VM; represents a lookup context
     * @param invokedName Stacked automatically by the VM; the name of the invoked method
     * @param invokedType Stacked automatically by the VM; the signature of the CallSite
     * @param samMethodType Method type of the functional interface method
     * @param implMethod A direct method handle for the implementation method
     * @param instantiatedMethodType The signature enforced dynamically at invocation time
     * @return a CallSite whose target can create suitable function objects
     * @throws LambdaConversionException if the lambda form is invalid
     */
    public static CallSite metafactory(MethodHandles.Lookup caller,
                                       String invokedName,
                                       MethodType invokedType,
                                       MethodType samMethodType,
                                       MethodHandle implMethod,
                                       MethodType instantiatedMethodType)
            throws LambdaConversionException {

        // Delegate to native implementation which generates the proxy class
        MethodHandle constructor = nativeMetafactory(
            caller.lookupClass(),
            invokedName,
            invokedType,
            samMethodType,
            implMethod,
            instantiatedMethodType,
            false,  // not serializable
            new Class<?>[0],  // no marker interfaces
            new MethodType[0]  // no bridges
        );

        return new ConstantCallSite(constructor);
    }

    /**
     * Facilitates the creation of function objects with additional features.
     * This is the alternate bootstrap method for lambda expressions.
     *
     * @param caller Stacked automatically by the VM
     * @param invokedName Stacked automatically by the VM
     * @param invokedType Stacked automatically by the VM
     * @param args An array containing samMethodType, implMethod, instantiatedMethodType,
     *             and optionally flags, marker interfaces, and bridge method types
     * @return a CallSite whose target can create suitable function objects
     * @throws LambdaConversionException if the lambda form is invalid
     */
    public static CallSite altMetafactory(MethodHandles.Lookup caller,
                                          String invokedName,
                                          MethodType invokedType,
                                          Object... args)
            throws LambdaConversionException {

        MethodType samMethodType = (MethodType) args[0];
        MethodHandle implMethod = (MethodHandle) args[1];
        MethodType instantiatedMethodType = (MethodType) args[2];
        int flags = (Integer) args[3];

        boolean serializable = (flags & FLAG_SERIALIZABLE) != 0;

        int argIndex = 4;

        // Extract marker interfaces if present
        Class<?>[] markerInterfaces;
        if ((flags & FLAG_MARKERS) != 0) {
            int markerCount = (Integer) args[argIndex++];
            markerInterfaces = new Class<?>[markerCount];
            for (int i = 0; i < markerCount; i++) {
                markerInterfaces[i] = (Class<?>) args[argIndex++];
            }
        } else {
            markerInterfaces = new Class<?>[0];
        }

        // Extract bridge method types if present
        MethodType[] bridges;
        if ((flags & FLAG_BRIDGES) != 0) {
            int bridgeCount = (Integer) args[argIndex++];
            bridges = new MethodType[bridgeCount];
            for (int i = 0; i < bridgeCount; i++) {
                bridges[i] = (MethodType) args[argIndex++];
            }
        } else {
            bridges = new MethodType[0];
        }

        MethodHandle constructor = nativeMetafactory(
            caller.lookupClass(),
            invokedName,
            invokedType,
            samMethodType,
            implMethod,
            instantiatedMethodType,
            serializable,
            markerInterfaces,
            bridges
        );

        return new ConstantCallSite(constructor);
    }

    /**
     * Native method that generates the lambda proxy class and returns
     * a method handle for its constructor.
     *
     * The generated class will:
     * - Implement the functional interface (return type of invokedType)
     * - Have fields for any captured values (parameter types of invokedType)
     * - Have a constructor that initializes these fields
     * - Have a method named invokedName with samMethodType that delegates to implMethod
     *
     * @param callerClass The class that contains the lambda
     * @param invokedName The name of the SAM method
     * @param invokedType Signature: (captured types) -> functional interface
     * @param samMethodType The erased signature of the SAM method
     * @param implMethod Handle to the implementation (lambda body)
     * @param instantiatedMethodType The actual (generic) signature of the SAM method
     * @param serializable Whether the lambda should be serializable
     * @param markerInterfaces Additional interfaces the proxy should implement
     * @param bridges Additional bridge methods to generate
     * @return A method handle for creating instances of the proxy class
     */
    private static native MethodHandle nativeMetafactory(
        Class<?> callerClass,
        String invokedName,
        MethodType invokedType,
        MethodType samMethodType,
        MethodHandle implMethod,
        MethodType instantiatedMethodType,
        boolean serializable,
        Class<?>[] markerInterfaces,
        MethodType[] bridges
    ) throws LambdaConversionException;
}
