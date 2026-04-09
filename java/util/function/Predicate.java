/* Predicate.java -- Functional interface for boolean-valued functions
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
 * Represents a predicate (boolean-valued function) of one argument.
 *
 * @param <T> the type of the input to the predicate
 * @since 1.8
 */
@FunctionalInterface
public interface Predicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return true if the input argument matches the predicate, otherwise false
     */
    boolean test(T t);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.
     *
     * @param other a predicate that will be logically-ANDed with this predicate
     * @return a composed predicate
     */
    default Predicate<T> and(Predicate<? super T> other) {
        if (other == null) throw new NullPointerException();
        return (t) -> test(t) && other.test(t);
    }

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that represents the logical negation of this predicate
     */
    default Predicate<T> negate() {
        return (t) -> !test(t);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.
     *
     * @param other a predicate that will be logically-ORed with this predicate
     * @return a composed predicate
     */
    default Predicate<T> or(Predicate<? super T> other) {
        if (other == null) throw new NullPointerException();
        return (t) -> test(t) || other.test(t);
    }

    /**
     * Returns a predicate that tests if two arguments are equal according
     * to Objects.equals(Object, Object).
     *
     * @param <T> the type of arguments to the predicate
     * @param targetRef the object reference with which to compare for equality
     * @return a predicate that tests if two arguments are equal
     */
    static <T> Predicate<T> isEqual(Object targetRef) {
        return (null == targetRef)
                ? obj -> obj == null
                : obj -> targetRef.equals(obj);
    }
}
