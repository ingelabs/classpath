/* MutableCallSite.java -- Mutable call site
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
 * A MutableCallSite is a CallSite whose target variable behaves like an
 * ordinary field. An invokedynamic instruction linked to a MutableCallSite
 * delegates all calls to the site's current target.
 *
 * @since 1.7
 */
public class MutableCallSite extends CallSite {

    /**
     * Creates a blank call site object with the given method type.
     */
    public MutableCallSite(MethodType type) {
        super(type);
    }

    /**
     * Creates a call site object with an initial target method handle.
     */
    public MutableCallSite(MethodHandle target) {
        super(target.type());
        this.target = target;
    }

    /**
     * Returns the target method of this call site.
     */
    @Override
    public final MethodHandle getTarget() {
        return target;
    }

    /**
     * Updates the target of this call site.
     */
    @Override
    public void setTarget(MethodHandle newTarget) {
        if (!type.equals(newTarget.type())) {
            throw new WrongMethodTypeException(
                "expected " + type + " but got " + newTarget.type());
        }
        this.target = newTarget;
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

    /**
     * Performs a synchronization operation on each call site in the given array.
     */
    public static void syncAll(MutableCallSite[] sites) {
        // Ensure all updates are visible
        for (MutableCallSite site : sites) {
            if (site == null) {
                throw new NullPointerException();
            }
        }
        // Native implementation would do memory barrier
        nativeSyncAll(sites);
    }

    private static native void nativeSyncAll(MutableCallSite[] sites);
}
