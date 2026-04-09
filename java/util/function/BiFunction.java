/* BiFunction.java -- Functional interface for two-argument functions
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
 * Represents a function that accepts two arguments and produces a result.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <R> the type of the result
 * @since 1.8
 */
@FunctionalInterface
public interface BiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u);

    /**
     * Returns a composed function that first applies this function to its input,
     * and then applies the after function to the result.
     *
     * @param <V> the type of output of the after function
     * @param after the function to apply after this function
     * @return a composed function
     */
    default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after) {
        if (after == null) throw new NullPointerException();
        return (T t, U u) -> after.apply(apply(t, u));
    }
}
