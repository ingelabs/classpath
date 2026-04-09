/* IntUnaryOperator.java -- Unary operator on int values
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
 * Represents an operation on a single int-valued operand that produces
 * an int-valued result.
 *
 * @since 1.8
 */
@FunctionalInterface
public interface IntUnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return the operator result
     */
    int applyAsInt(int operand);

    /**
     * Returns a composed operator that first applies the before operator to its input,
     * and then applies this operator to the result.
     */
    default IntUnaryOperator compose(IntUnaryOperator before) {
        if (before == null) throw new NullPointerException();
        return (int v) -> applyAsInt(before.applyAsInt(v));
    }

    /**
     * Returns a composed operator that first applies this operator to its input,
     * and then applies the after operator to the result.
     */
    default IntUnaryOperator andThen(IntUnaryOperator after) {
        if (after == null) throw new NullPointerException();
        return (int t) -> after.applyAsInt(applyAsInt(t));
    }

    /**
     * Returns a unary operator that always returns its input argument.
     */
    static IntUnaryOperator identity() {
        return t -> t;
    }
}
