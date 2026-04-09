/* ConstantCallSite.java -- Immutable call site
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
 * A ConstantCallSite is a CallSite whose target is permanent, and can never
 * be changed. An invokedynamic instruction linked to a ConstantCallSite is
 * permanently bound to the call site's target.
 *
 * @since 1.7
 */
public class ConstantCallSite extends CallSite {

    private final boolean isFrozen;

    /**
     * Creates a call site with a permanent target.
     */
    public ConstantCallSite(MethodHandle target) {
        super(target.type());
        this.target = target;
        this.isFrozen = true;
    }

    /**
     * Creates a call site with a permanent target, possibly still to be determined.
     * Used for lazy initialization.
     */
    protected ConstantCallSite(MethodType targetType, MethodHandle createTargetHook)
            throws Throwable {
        super(targetType);
        this.target = (MethodHandle) createTargetHook.invoke(this);
        this.isFrozen = true;
    }

    /**
     * Returns the target method of this call site.
     */
    @Override
    public final MethodHandle getTarget() {
        return target;
    }

    /**
     * Always throws UnsupportedOperationException since target is permanent.
     */
    @Override
    public final void setTarget(MethodHandle ignore) {
        throw new UnsupportedOperationException("ConstantCallSite target is permanent");
    }

    /**
     * Returns the target, which is also the dynamic invoker for a constant site.
     */
    @Override
    public final MethodHandle dynamicInvoker() {
        return target;
    }
}
