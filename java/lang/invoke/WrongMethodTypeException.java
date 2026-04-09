/* WrongMethodTypeException.java -- Exception for method type mismatches
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
 * Thrown to indicate that code has attempted to call a method handle
 * via the wrong method type. As with the bytecode representation of
 * normal Java method calls, this exception is thrown at compile time
 * if the call site types are mismatched.
 *
 * @since 1.7
 */
public class WrongMethodTypeException extends RuntimeException {
    private static final long serialVersionUID = 292L;

    /**
     * Constructs a WrongMethodTypeException with no message.
     */
    public WrongMethodTypeException() {
        super();
    }

    /**
     * Constructs a WrongMethodTypeException with a message.
     */
    public WrongMethodTypeException(String message) {
        super(message);
    }

    /**
     * Constructs a WrongMethodTypeException with a message and cause.
     */
    public WrongMethodTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a WrongMethodTypeException with a cause.
     */
    public WrongMethodTypeException(Throwable cause) {
        super(cause);
    }
}
