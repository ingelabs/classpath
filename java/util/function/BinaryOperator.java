/* BinaryOperator.java -- Binary operator functional interface
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

import java.util.Comparator;

/**
 * Represents an operation upon two operands of the same type, producing a
 * result of the same type as the operands. This is a specialization of
 * BiFunction for the case where the operands and the result are all of
 * the same type.
 *
 * @param <T> the type of the operands and result
 * @since 1.8
 */
@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T, T, T> {

    /**
     * Returns a BinaryOperator which returns the lesser of two elements
     * according to the specified Comparator.
     *
     * @param <T> the type of the input arguments
     * @param comparator a Comparator for comparing the two values
     * @return a BinaryOperator which returns the lesser of its operands
     */
    static <T> BinaryOperator<T> minBy(Comparator<? super T> comparator) {
        if (comparator == null) throw new NullPointerException();
        return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
    }

    /**
     * Returns a BinaryOperator which returns the greater of two elements
     * according to the specified Comparator.
     *
     * @param <T> the type of the input arguments
     * @param comparator a Comparator for comparing the two values
     * @return a BinaryOperator which returns the greater of its operands
     */
    static <T> BinaryOperator<T> maxBy(Comparator<? super T> comparator) {
        if (comparator == null) throw new NullPointerException();
        return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
    }
}
