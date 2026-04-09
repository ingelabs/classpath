/* MethodHandles.java -- Factory methods for method handles
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Factory methods for creating method handles.
 *
 * @since 1.7
 */
public class MethodHandles {

    private MethodHandles() {} // non-instantiable

    /**
     * Returns a lookup object for the caller's class.
     */
    public static Lookup lookup() {
        // Get caller class - simplified, relies on VM support
        return new Lookup(getCallerClass());
    }

    /**
     * Returns a lookup object with full privileges.
     */
    public static Lookup publicLookup() {
        return new Lookup(Object.class, Lookup.PUBLIC);
    }

    // Native method to get caller class
    private static native Class<?> getCallerClass();

    /**
     * A lookup object is a factory for creating method handles.
     */
    public static final class Lookup {
        private final Class<?> lookupClass;
        private final int allowedModes;

        /** Access mode flag: public access */
        public static final int PUBLIC = 0x01;
        /** Access mode flag: private access */
        public static final int PRIVATE = 0x02;
        /** Access mode flag: protected access */
        public static final int PROTECTED = 0x04;
        /** Access mode flag: package access */
        public static final int PACKAGE = 0x08;

        Lookup(Class<?> lookupClass) {
            this(lookupClass, PUBLIC | PRIVATE | PROTECTED | PACKAGE);
        }

        Lookup(Class<?> lookupClass, int allowedModes) {
            this.lookupClass = lookupClass;
            this.allowedModes = allowedModes;
        }

        /**
         * Returns the lookup class.
         */
        public Class<?> lookupClass() {
            return lookupClass;
        }

        /**
         * Returns the lookup modes.
         */
        public int lookupModes() {
            return allowedModes;
        }

        /**
         * Produces a method handle for a virtual method.
         */
        public MethodHandle findVirtual(Class<?> refc, String name, MethodType type)
                throws NoSuchMethodException, IllegalAccessException {
            return findMethod(refc, name, type, false);
        }

        /**
         * Produces a method handle for a static method.
         */
        public MethodHandle findStatic(Class<?> refc, String name, MethodType type)
                throws NoSuchMethodException, IllegalAccessException {
            return findMethod(refc, name, type, true);
        }

        /**
         * Produces a method handle for a constructor.
         */
        public MethodHandle findConstructor(Class<?> refc, MethodType type)
                throws NoSuchMethodException, IllegalAccessException {
            // Native implementation needed
            return nativeFindConstructor(refc, type);
        }

        /**
         * Produces a method handle for a getter.
         */
        public MethodHandle findGetter(Class<?> refc, String name, Class<?> type)
                throws NoSuchFieldException, IllegalAccessException {
            return nativeFindGetter(refc, name, type, false);
        }

        /**
         * Produces a method handle for a static getter.
         */
        public MethodHandle findStaticGetter(Class<?> refc, String name, Class<?> type)
                throws NoSuchFieldException, IllegalAccessException {
            return nativeFindGetter(refc, name, type, true);
        }

        /**
         * Produces a method handle for a setter.
         */
        public MethodHandle findSetter(Class<?> refc, String name, Class<?> type)
                throws NoSuchFieldException, IllegalAccessException {
            return nativeFindSetter(refc, name, type, false);
        }

        /**
         * Produces a method handle for a static setter.
         */
        public MethodHandle findStaticSetter(Class<?> refc, String name, Class<?> type)
                throws NoSuchFieldException, IllegalAccessException {
            return nativeFindSetter(refc, name, type, true);
        }

        /**
         * Produces a method handle giving read access to a reflected field.
         */
        public MethodHandle unreflectGetter(Field f) throws IllegalAccessException {
            return nativeUnreflectGetter(f);
        }

        /**
         * Produces a method handle giving write access to a reflected field.
         */
        public MethodHandle unreflectSetter(Field f) throws IllegalAccessException {
            return nativeUnreflectSetter(f);
        }

        /**
         * Produces a method handle for a reflected method.
         */
        public MethodHandle unreflect(Method m) throws IllegalAccessException {
            return nativeUnreflect(m);
        }

        /**
         * Produces a method handle for a reflected constructor.
         */
        public MethodHandle unreflectConstructor(Constructor<?> c) throws IllegalAccessException {
            return nativeUnreflectConstructor(c);
        }

        private MethodHandle findMethod(Class<?> refc, String name, MethodType type, boolean isStatic)
                throws NoSuchMethodException, IllegalAccessException {
            return nativeFindMethod(refc, name, type, isStatic);
        }

        // Native methods for method handle creation
        private native MethodHandle nativeFindMethod(Class<?> refc, String name,
                MethodType type, boolean isStatic);
        private native MethodHandle nativeFindConstructor(Class<?> refc, MethodType type);
        private native MethodHandle nativeFindGetter(Class<?> refc, String name,
                Class<?> type, boolean isStatic);
        private native MethodHandle nativeFindSetter(Class<?> refc, String name,
                Class<?> type, boolean isStatic);
        private native MethodHandle nativeUnreflect(Method m);
        private native MethodHandle nativeUnreflectConstructor(Constructor<?> c);
        private native MethodHandle nativeUnreflectGetter(Field f);
        private native MethodHandle nativeUnreflectSetter(Field f);

        @Override
        public String toString() {
            return "Lookup[" + lookupClass.getName() + "]";
        }
    }
}
