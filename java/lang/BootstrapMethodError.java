/* BootstrapMethodError.java -- Error from bootstrap method invocation
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

package java.lang;

/**
 * Thrown to indicate that an invokedynamic instruction has failed to find
 * its bootstrap method, or the bootstrap method has failed to provide a
 * call site with a target of the correct method type.
 *
 * @since 1.7
 */
public class BootstrapMethodError extends LinkageError {
    private static final long serialVersionUID = 292L;

    /**
     * Constructs a BootstrapMethodError with no message.
     */
    public BootstrapMethodError() {
        super();
    }

    /**
     * Constructs a BootstrapMethodError with a message.
     */
    public BootstrapMethodError(String message) {
        super(message);
    }

    /**
     * Constructs a BootstrapMethodError with a message and cause.
     */
    public BootstrapMethodError(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }

    /**
     * Constructs a BootstrapMethodError with a cause.
     */
    public BootstrapMethodError(Throwable cause) {
        super(cause == null ? null : cause.toString());
        initCause(cause);
    }
}
