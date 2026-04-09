/* MethodHandle.java -- Reference to a method
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
 * A method handle is a typed, directly executable reference to an underlying
 * method, constructor, field, or similar low-level operation.
 *
 * This is a minimal implementation to support lambda expressions.
 * Full MethodHandle functionality requires VM support.
 *
 * @since 1.7
 */
public abstract class MethodHandle {

    // Package-private fields for VM use
    final MethodType type;

    // Package-private constructor
    MethodHandle(MethodType type) {
        this.type = type;
    }

    /**
     * Returns the type of this method handle.
     */
    public MethodType type() {
        return type;
    }

    /**
     * Invokes the method handle with the given arguments.
     * The signature is polymorphic - actual arguments depend on the method type.
     */
    public final native Object invokeExact(Object... args) throws Throwable;

    /**
     * Invokes the method handle with argument conversion.
     */
    public final native Object invoke(Object... args) throws Throwable;

    /**
     * Produces a method handle with a different type that invokes the original.
     */
    public MethodHandle asType(MethodType newType) {
        if (type.equals(newType)) return this;
        return new AsTypeMethodHandle(this, newType);
    }

    /**
     * Binds a value to the first argument of this method handle.
     */
    public MethodHandle bindTo(Object x) {
        return new BoundMethodHandle(this, x);
    }

    @Override
    public String toString() {
        return "MethodHandle" + type;
    }

    // Internal implementation classes

    static class AsTypeMethodHandle extends MethodHandle {
        final MethodHandle target;

        AsTypeMethodHandle(MethodHandle target, MethodType newType) {
            super(newType);
            this.target = target;
        }
    }

    static class BoundMethodHandle extends MethodHandle {
        final MethodHandle target;
        final Object boundArg;

        BoundMethodHandle(MethodHandle target, Object boundArg) {
            super(target.type().dropParameterTypes(0, 1));
            this.target = target;
            this.boundArg = boundArg;
        }
    }

    /**
     * Direct method handle - points to a specific method/constructor/field.
     * Created by MethodHandles.Lookup.
     */
    static class DirectMethodHandle extends MethodHandle {
        final Class<?> declaringClass;
        final String name;
        final int referenceKind;

        DirectMethodHandle(MethodType type, Class<?> declaringClass,
                          String name, int referenceKind) {
            super(type);
            this.declaringClass = declaringClass;
            this.name = name;
            this.referenceKind = referenceKind;
        }
    }
}
