/* DoublePredicate.java -- Predicate for double values
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
 * Represents a predicate (boolean-valued function) of one double-valued argument.
 *
 * @since 1.8
 */
@FunctionalInterface
public interface DoublePredicate {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param value the input argument
     * @return true if the input argument matches the predicate, otherwise false
     */
    boolean test(double value);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.
     */
    default DoublePredicate and(DoublePredicate other) {
        if (other == null) throw new NullPointerException();
        return (value) -> test(value) && other.test(value);
    }

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     */
    default DoublePredicate negate() {
        return (value) -> !test(value);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.
     */
    default DoublePredicate or(DoublePredicate other) {
        if (other == null) throw new NullPointerException();
        return (value) -> test(value) || other.test(value);
    }
}
