/* Consumer.java -- Functional interface for consuming values
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
 * Represents an operation that accepts a single input argument and returns no
 * result. Unlike most other functional interfaces, Consumer is expected to
 * operate via side-effects.
 *
 * @param <T> the type of the input to the operation
 * @since 1.8
 */
@FunctionalInterface
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);

    /**
     * Returns a composed Consumer that performs, in sequence, this operation
     * followed by the after operation.
     *
     * @param after the operation to perform after this operation
     * @return a composed Consumer
     */
    default Consumer<T> andThen(Consumer<? super T> after) {
        if (after == null) throw new NullPointerException();
        return (T t) -> { accept(t); after.accept(t); };
    }
}
