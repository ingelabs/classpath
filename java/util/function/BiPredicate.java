/* BiPredicate.java -- Functional interface for two-argument predicates
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
 * Represents a predicate (boolean-valued function) of two arguments.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @since 1.8
 */
@FunctionalInterface
public interface BiPredicate<T, U> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @return true if the input arguments match the predicate, otherwise false
     */
    boolean test(T t, U u);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.
     */
    default BiPredicate<T, U> and(BiPredicate<? super T, ? super U> other) {
        if (other == null) throw new NullPointerException();
        return (t, u) -> test(t, u) && other.test(t, u);
    }

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     */
    default BiPredicate<T, U> negate() {
        return (t, u) -> !test(t, u);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.
     */
    default BiPredicate<T, U> or(BiPredicate<? super T, ? super U> other) {
        if (other == null) throw new NullPointerException();
        return (t, u) -> test(t, u) || other.test(t, u);
    }
}
