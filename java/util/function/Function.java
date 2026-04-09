/* Function.java -- Functional interface for one-argument functions
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
 * Represents a function that accepts one argument and produces a result.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @since 1.8
 */
@FunctionalInterface
public interface Function<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t);

    /**
     * Returns a composed function that first applies the before function
     * to its input, and then applies this function to the result.
     *
     * @param <V> the type of input to the before function
     * @param before the function to apply before this function
     * @return a composed function
     */
    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        if (before == null) throw new NullPointerException();
        return (V v) -> apply(before.apply(v));
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the after function to the result.
     *
     * @param <V> the type of output of the after function
     * @param after the function to apply after this function
     * @return a composed function
     */
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        if (after == null) throw new NullPointerException();
        return (T t) -> after.apply(apply(t));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects
     * @return a function that always returns its input argument
     */
    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
