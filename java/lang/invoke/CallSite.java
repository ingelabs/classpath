/* CallSite.java -- Call site for invokedynamic
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
 * A CallSite is a holder for a variable MethodHandle, which is called its target.
 * An invokedynamic instruction linked to a CallSite delegates all calls to the
 * site's current target.
 *
 * @since 1.7
 */
public abstract class CallSite {

    final MethodType type;
    MethodHandle target;

    CallSite(MethodType type) {
        this.type = type;
    }

    /**
     * Returns the type of this call site's target.
     */
    public MethodType type() {
        return type;
    }

    /**
     * Returns the target method of this call site.
     */
    public abstract MethodHandle getTarget();

    /**
     * Updates the target of this call site.
     */
    public abstract void setTarget(MethodHandle newTarget);

    /**
     * Produces a method handle equivalent to an invokedynamic instruction
     * that has been linked to this call site.
     */
    public abstract MethodHandle dynamicInvoker();
}
