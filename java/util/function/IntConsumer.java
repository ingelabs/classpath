/* IntConsumer.java -- Consumer accepting int arguments
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

package java.util.function;

/**
 * Represents an operation that accepts a single int-valued argument and
 * returns no result.
 *
 * @since 1.8
 */
@FunctionalInterface
public interface IntConsumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     */
    void accept(int value);

    /**
     * Returns a composed IntConsumer that performs, in sequence, this operation
     * followed by the after operation.
     *
     * @param after the operation to perform after this operation
     * @return a composed IntConsumer
     */
    default IntConsumer andThen(IntConsumer after) {
        if (after == null) throw new NullPointerException();
        return (int value) -> { accept(value); after.accept(value); };
    }
}
