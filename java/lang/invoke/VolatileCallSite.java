/* VolatileCallSite.java -- Call site with volatile target
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
 * A VolatileCallSite is a CallSite whose target acts like a volatile variable.
 * An invokedynamic instruction linked to a VolatileCallSite sees updates
 * to its target immediately, even if the update occurs in another thread.
 *
 * @since 1.7
 */
public class VolatileCallSite extends CallSite {

    /**
     * Creates a blank call site object with the given method type.
     */
    public VolatileCallSite(MethodType type) {
        super(type);
    }

    /**
     * Creates a call site object with an initial target method handle.
     */
    public VolatileCallSite(MethodHandle target) {
        super(target.type());
        this.target = target;
    }

    /**
     * Returns the target method of this call site, with volatile semantics.
     */
    @Override
    public final MethodHandle getTarget() {
        return target; // In Java, field access to target would be volatile
    }

    /**
     * Updates the target of this call site, with volatile semantics.
     */
    @Override
    public void setTarget(MethodHandle newTarget) {
        if (!type.equals(newTarget.type())) {
            throw new WrongMethodTypeException(
                "expected " + type + " but got " + newTarget.type());
        }
        this.target = newTarget; // Volatile write
    }

    /**
     * Produces a method handle equivalent to an invokedynamic instruction
     * that has been linked to this call site.
     */
    @Override
    public final MethodHandle dynamicInvoker() {
        return makeDynamicInvoker();
    }

    private native MethodHandle makeDynamicInvoker();
}
