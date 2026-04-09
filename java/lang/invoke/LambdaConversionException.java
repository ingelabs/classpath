/* LambdaConversionException.java -- Exception for lambda conversion errors
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
 * LambdaConversionException indicates an invalid lambda conversion.
 *
 * @since 1.8
 */
public class LambdaConversionException extends Exception {
    private static final long serialVersionUID = 292L + 8L;

    /**
     * Constructs a LambdaConversionException with no message.
     */
    public LambdaConversionException() {
    }

    /**
     * Constructs a LambdaConversionException with a message.
     */
    public LambdaConversionException(String message) {
        super(message);
    }

    /**
     * Constructs a LambdaConversionException with a message and cause.
     */
    public LambdaConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a LambdaConversionException with a cause.
     */
    public LambdaConversionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a LambdaConversionException with full control over message and stack trace.
     */
    public LambdaConversionException(String message, Throwable cause,
                                     boolean enableSuppression,
                                     boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
