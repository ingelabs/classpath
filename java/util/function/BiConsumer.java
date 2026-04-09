/* BiConsumer.java -- Functional interface for two-argument consumers
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
 * Represents an operation that accepts two input arguments and returns no
 * result. This is the two-arity specialization of Consumer.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @since 1.8
 */
@FunctionalInterface
public interface BiConsumer<T, U> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(T t, U u);

    /**
     * Returns a composed BiConsumer that performs, in sequence, this operation
     * followed by the after operation.
     *
     * @param after the operation to perform after this operation
     * @return a composed BiConsumer
     */
    default BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> after) {
        if (after == null) throw new NullPointerException();
        return (t, u) -> { accept(t, u); after.accept(t, u); };
    }
}
