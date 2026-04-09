/* LongUnaryOperator.java -- Unary operator on long values
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
 * Represents an operation on a single long-valued operand that produces
 * a long-valued result.
 *
 * @since 1.8
 */
@FunctionalInterface
public interface LongUnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return the operator result
     */
    long applyAsLong(long operand);

    /**
     * Returns a composed operator that first applies the before operator to its input,
     * and then applies this operator to the result.
     */
    default LongUnaryOperator compose(LongUnaryOperator before) {
        if (before == null) throw new NullPointerException();
        return (long v) -> applyAsLong(before.applyAsLong(v));
    }

    /**
     * Returns a composed operator that first applies this operator to its input,
     * and then applies the after operator to the result.
     */
    default LongUnaryOperator andThen(LongUnaryOperator after) {
        if (after == null) throw new NullPointerException();
        return (long t) -> after.applyAsLong(applyAsLong(t));
    }

    /**
     * Returns a unary operator that always returns its input argument.
     */
    static LongUnaryOperator identity() {
        return t -> t;
    }
}
