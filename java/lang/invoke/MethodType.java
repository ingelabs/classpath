/* MethodType.java -- Method type descriptor
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A method type represents the arguments and return type accepted and
 * returned by a method handle, or the arguments and return type passed
 * and expected by a method handle caller.
 *
 * @since 1.7
 */
public final class MethodType implements java.io.Serializable {
    private static final long serialVersionUID = 292L;

    private final Class<?> rtype;
    private final Class<?>[] ptypes;

    private MethodType(Class<?> rtype, Class<?>[] ptypes) {
        this.rtype = rtype;
        this.ptypes = ptypes;
    }

    /**
     * Returns a method type with the given return type and parameter types.
     */
    public static MethodType methodType(Class<?> rtype, Class<?>[] ptypes) {
        if (rtype == null)
            throw new NullPointerException("rtype");
        ptypes = ptypes.clone();
        for (Class<?> ptype : ptypes)
            if (ptype == null)
                throw new NullPointerException("ptype");
        return new MethodType(rtype, ptypes);
    }

    /**
     * Returns a method type with the given return type and parameter types.
     */
    public static MethodType methodType(Class<?> rtype, List<Class<?>> ptypes) {
        return methodType(rtype, ptypes.toArray(new Class<?>[0]));
    }

    /**
     * Returns a method type with the given return type and no parameters.
     */
    public static MethodType methodType(Class<?> rtype) {
        return methodType(rtype, new Class<?>[0]);
    }

    /**
     * Returns a method type with the given return type and single parameter.
     */
    public static MethodType methodType(Class<?> rtype, Class<?> ptype0) {
        return methodType(rtype, new Class<?>[] { ptype0 });
    }

    /**
     * Returns a method type with the given return type and two parameters.
     */
    public static MethodType methodType(Class<?> rtype, Class<?> ptype0, Class<?>... ptypes) {
        Class<?>[] allPtypes = new Class<?>[1 + ptypes.length];
        allPtypes[0] = ptype0;
        System.arraycopy(ptypes, 0, allPtypes, 1, ptypes.length);
        return methodType(rtype, allPtypes);
    }

    /**
     * Returns the return type of this method type.
     */
    public Class<?> returnType() {
        return rtype;
    }

    /**
     * Returns the parameter types of this method type.
     */
    public List<Class<?>> parameterList() {
        return Collections.unmodifiableList(Arrays.asList(ptypes));
    }

    /**
     * Returns the parameter types as an array.
     */
    public Class<?>[] parameterArray() {
        return ptypes.clone();
    }

    /**
     * Returns the number of parameter types.
     */
    public int parameterCount() {
        return ptypes.length;
    }

    /**
     * Returns the parameter type at the given index.
     */
    public Class<?> parameterType(int num) {
        return ptypes[num];
    }

    /**
     * Returns a method type with different return type.
     */
    public MethodType changeReturnType(Class<?> nrtype) {
        if (rtype == nrtype) return this;
        return methodType(nrtype, ptypes);
    }

    /**
     * Returns a method type with different parameter type at given index.
     */
    public MethodType changeParameterType(int num, Class<?> nptype) {
        if (ptypes[num] == nptype) return this;
        Class<?>[] newPtypes = ptypes.clone();
        newPtypes[num] = nptype;
        return methodType(rtype, newPtypes);
    }

    /**
     * Returns a method type with additional parameter types inserted.
     */
    public MethodType insertParameterTypes(int num, Class<?>... newPtypes) {
        Class<?>[] result = new Class<?>[ptypes.length + newPtypes.length];
        System.arraycopy(ptypes, 0, result, 0, num);
        System.arraycopy(newPtypes, 0, result, num, newPtypes.length);
        System.arraycopy(ptypes, num, result, num + newPtypes.length, ptypes.length - num);
        return methodType(rtype, result);
    }

    /**
     * Returns a method type with parameter types dropped.
     */
    public MethodType dropParameterTypes(int start, int end) {
        Class<?>[] result = new Class<?>[ptypes.length - (end - start)];
        System.arraycopy(ptypes, 0, result, 0, start);
        System.arraycopy(ptypes, end, result, start, ptypes.length - end);
        return methodType(rtype, result);
    }

    /**
     * Returns this method type's descriptor string.
     */
    public String toMethodDescriptorString() {
        StringBuilder sb = new StringBuilder("(");
        for (Class<?> ptype : ptypes) {
            sb.append(toDescriptor(ptype));
        }
        sb.append(")");
        sb.append(toDescriptor(rtype));
        return sb.toString();
    }

    private static String toDescriptor(Class<?> c) {
        if (c == void.class) return "V";
        if (c == boolean.class) return "Z";
        if (c == byte.class) return "B";
        if (c == char.class) return "C";
        if (c == short.class) return "S";
        if (c == int.class) return "I";
        if (c == long.class) return "J";
        if (c == float.class) return "F";
        if (c == double.class) return "D";
        if (c.isArray()) return c.getName().replace('.', '/');
        return "L" + c.getName().replace('.', '/') + ";";
    }

    /**
     * Returns a method type from a descriptor string.
     */
    public static MethodType fromMethodDescriptorString(String descriptor, ClassLoader loader)
            throws IllegalArgumentException, TypeNotPresentException {
        // Simplified parsing - full implementation would be more complex
        throw new UnsupportedOperationException("fromMethodDescriptorString not yet implemented");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MethodType)) return false;
        MethodType that = (MethodType) obj;
        return rtype == that.rtype && Arrays.equals(ptypes, that.ptypes);
    }

    @Override
    public int hashCode() {
        return rtype.hashCode() ^ Arrays.hashCode(ptypes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < ptypes.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(ptypes[i].getSimpleName());
        }
        sb.append(")");
        sb.append(rtype.getSimpleName());
        return sb.toString();
    }
}
