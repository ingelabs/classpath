/*
 * java.util.BitSet: part of the Java Class Libraries project.
 * Copyright (C) 1998 Jochen Hoenicke
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.util;

/**
 * This class implements a set of nonnegative integers, of arbitrary
 * size.  It is implemented by a bit vector: each integer in the set
 * is represented by a set bit.  The size of this structure is determined
 * by the highest integer in the set.
 *
 * You can union, intersect and build symmetric remainders, by invoking the
 * logical operations AND, OR ,resp. XOR. 
 *
 * This class is thread safe, in the sense, that every method acts as if it
 * were atomic.
 *
 * @author Jochen Hoenicke
 */
public class BitSet implements Cloneable,java.io.Serializable {
    /**
     * This field contains the bits.  The k-th bit is set, iff
     * <pre>
     * k/64 < bits.length  && ((bits[k/64] & (1<<(k%64))) != 0)
     * </pre>
     */
    private long bits[];
    /*{ invariant { bits != null :: "bit field is null"; } }*/

    /* The serialized format is compatible to the the sun classes.
     */
    private static final long serialVersionUID = 7997698588986878753L;

    /**
     * Create a new empty bit set.
     */
    public BitSet() {
        bits = new long[1];
    }

    /**
     * Create a new empty bit set, with a given size.  This
     * constructor reserves enough space to represent the integers
     * from <code>0</code> to <code>nbits-1</code>.  
     * @param nbits the initial size of the bit set.
     * @exception NegativeArraySizeException if the specified initial
     * size is negative.  
     * @require nbits >= 0
     */
    public BitSet(int nbits) {
        bits = new long[nbits>>6];
    }

    /**
     * Grows the bits array.  You should make sure, that the array is indeed
     * smaller than necessary.
     * @param toSize The minimum size we need.
     * @require toSize > bits.length
     */
    private void grow(int toSize) {
        int nextSize = Math.max(bits.length*2, toSize);
        long[] newBits = new long[nextSize];
        System.arraycopy(bits, 0, newBits, 0, bits.length);
        bits = newBits;
    }

    /**
     * Add the integer <code>bitIndex</code> to this set.  That is 
     * the corresponding bit is set to true.  If the index was already in
     * the set, this method does nothing.  The size of this structure
     * is automatically increased as necessary.
     * @param bitIndex a nonnegative integer.
     * @exception ArrayIndexOutOfBoundsException if the specified bit index
     * is negative.
     * @exception OutOfMemoryError if the specified bit index is too big.
     * @require bitIndex >= 0
     */
    public synchronized void set(int bitIndex) {
        int intNr = bitIndex >> 6;
        if (intNr >= bits.length)
            grow(intNr+1);
        bits[intNr] |= 1L << (bitIndex & 63);
    }
    
    /**
     * Removes the integer <code>bitIndex</code> from this set. That is
     * the corresponding bit is cleared.  If the index is not in the set,
     * this method does nothing.
     * @param bitIndex a nonnegative integer.
     * @exception ArrayIndexOutOfBoundsException if the specified bit index
     * is negative.
     * @require bitIndex >= 0
     */
    public synchronized void clear(int bitIndex) {
        int intNr = bitIndex >> 6;
        /* if intNr >= bits.length the bit wasn't set, so this method
         * does nothing.
         */
        if (intNr < bits.length)
            bits[intNr] &= ~(1L << (bitIndex & 63));
    }

    /**
     * Returns true if the integer <code>bitIndex</code> is in this bit
     * set, otherwise false.
     * @param bitIndex a nonnegative integer
     * @return the value of the bit at the specified index.
     * @exception ArrayIndexOutOfBoundsException if the specified bit index
     * is negative.
     * @require bitIndex >= 0
     */
    public boolean get(int bitIndex) {
        /* No need to synchronize.  If the bit is changed just in
         * that moment, every return value is correct.
         * Note that this is only true, because the array can't shrink, 
         * otherwise we could get a wrong ArrayIndexOutOfBoundsException.
         */
        int intNr = bitIndex >> 6;
        if (intNr < bits.length)
            return (bits[intNr] & (1L << (bitIndex & 63))) != 0;
        else
            return false;
    }

    /**
     * Performs the logical AND operation on this bit set and the
     * given <code>set</code>.  This means it builds the intersection
     * of the two sets.  The result is stored into this bit set.
     * @param set the second bit set.
     * @require set != null
     */
    public void and(BitSet set) {
        BitSet first, second;
        /* To avoid deadlocks we have to order the monitors.
         * Note that identityHashCode is suboptimal, because there
         * may be a slight chance that they are the same.
         */
        if (System.identityHashCode(this) < System.identityHashCode(set)) {
            first = this;
            second = set;
        } else {
            first = set;
            second = this;
        }
        synchronized(first) {
            synchronized(second) {
                /* The smallest of the both lengths is enough. */
                int length = Math.min(bits.length, set.bits.length);
                int i;
                for (i=0; i < length; i++)
                    bits[i] &= set.bits[i];
                /* clear the remaining bits. */
                for (; i<bits.length; i++)
                    bits[i] = 0;
            }
        }
    }

    /**
     * Performs the logical OR operation on this bit set and the
     * given <code>set</code>.  This means it builds the union
     * of the two sets.  The result is stored into this bit set, which
     * grows as necessary.
     * @param set the second bit set.
     * @exception OutOfMemoryError if the current set can't grow.
     * @require set != null
     */
    public void or(BitSet set) {
        BitSet first, second;
        /* To avoid deadlocks we have to order the monitors.
         * Note that identityHashCode is suboptimal, because there
         * may be a slight chance that they are the same.
         */
        if (System.identityHashCode(this) < System.identityHashCode(set)) {
            first = this;
            second = set;
        } else {
            first = set;
            second = this;
        }
        synchronized(first) {
            synchronized(second) {
                int max = set.bits.length-1;
                while (set.bits[max] == 0)
                    max--;
                if (max >= bits.length)
                    grow(max+1);
                for (int i=0; i<=max; i++)
                    bits[i] |= set.bits[i];
            }
        }
    }

    /**
     * Performs the logical XOR operation on this bit set and the
     * given <code>set</code>.  This means it builds the symmetric
     * remainder of the two sets (the elements that are in one set,
     * but not in the other).  The result is stored into this bit set,
     * which grows as necessary.  
     * @param set the second bit set.
     * @exception OutOfMemoryError if the current set can't grow.  
     * @require set != null
     */
    public synchronized void xor(BitSet set) {
        BitSet first, second;
        /* To avoid deadlocks we have to order the monitors.
         * Note that identityHashCode is suboptimal, because there
         * may be a slight chance that they are the same.
         */
        if (System.identityHashCode(this) < System.identityHashCode(set)) {
            first = this;
            second = set;
        } else {
            first = set;
            second = this;
        }
        synchronized(first) {
            synchronized(second) {
                int max = set.bits.length-1;
                while (set.bits[max] == 0)
                    max--;
                if (max >= bits.length)
                    grow(max+1);
                for (int i=0; i<=max; i++)
                    bits[i] ^= set.bits[i];
            }
        }
    }

    /**
     * Returns a hash code value for this bit set.  The hash code of 
     * two bit sets containing the same integers is identical.  The algorithm
     * used to compute it is as follows:
     *
     * Suppose the bits in the BitSet were to be stored in an array of
     * long integers called <code>bits</code>, in such a manner that
     * bit <code>k</code> is set in the BitSet (for nonnegative values
     * of <code>k</code>) if and only if
     *
     * <pre>
     * ((k/64) < bits.length) && ((bits[k/64] & (1L << (bit % 64))) != 0)
     * </pre>
     *
     * Then the following definition of the hashCode method
     * would be a correct implementation of the actual algorithm:
     *
     * <pre>
     * public synchronized int hashCode() {
     *     long h = 1234;
     *     for (int i = bits.length; i>=0; i--) {
     *         h ^= bits[i] * (i + 1);
     *     }
     *     return (int)((h >> 32) ^ h);
     * }
     * </pre>
     *
     * Note that the hash code values changes, if the set is changed.
     * @return the hash code value for this bit set.
     */
    public synchronized int hashCode() {
        long h = 1234;
        for (int i = bits.length; i>=0; i--) {
            h ^= bits[i] * (i + 1);
        }
        return (int)((h >> 32) ^ h);
    }

    /**
     * Returns the number of bits actually used by this bit set.  Note
     * that this method doesn't return the number of set bits.
     * @returns the number of bits currently used.  
     */
    public int size() {
        return bits.length * 64;
    }


    /**
     * Returns true if the <code>obj</code> is a bit set that contains
     * exactly the same elements as this bit set, otherwise false.
     * @return true if obj equals this bit set.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof BitSet))
            return false;
        BitSet first, second;
        /* To avoid deadlocks we have to order the monitors.
         * Note that identityHashCode is suboptimal, because there
         * may be a slight chance that they are the same (at least
         * on 64 bit architectures). But in 99.99999997 % it works.
         */
        if (System.identityHashCode(this) < System.identityHashCode(obj)) {
            first = this;
            second = (BitSet) obj;
        } else {
            first = (BitSet) obj;
            second = this;
        }
        synchronized(first) {
            int i;
            synchronized(second) {
                int length = Math.min(first.bits.length, second.bits.length);
                for (i=0; i<length; i++) {
                    if (first.bits[i] != second.bits[i])
                        return false;
                }
                for (; i<second.bits.length; i++) {
                    if (second.bits[i] != 0)
                        return false;
                }
            }
            for (; i<first.bits.length; i++) {
                if (first.bits[i] != 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Create a clone of this bit set, that is an instance of the same
     * class and contains the same elements.  But it doesn't change when
     * this bit set changes.
     * @return the clone of this object.
     */
    public synchronized Object clone() {
        try {
            BitSet clone = (BitSet) super.clone();
            clone.bits = new long[bits.length];
            System.arraycopy(bits, 0, clone.bits, 0, bits.length);
            return clone;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    /**
     * Returns the string representation of this bit set.  This
     * consists of a comma separated list of the integers in this set
     * surrounded by curly braces.  There is a space after each comma.
     * @return the string representation.
     */
    public synchronized String toString() {
        StringBuffer result = new StringBuffer("{");
        String comma = "";
        for (int i=0; i<bits.length; i++) {
            for (int j=0; j< 64; j++) {
                if ((bits[i] & (1L << j)) != 0) {
                    result.append(comma).append((i<<6)+j);
                    comma = ", ";
                }
            }
        }
        result.append('}');
        return result.toString();
    }
}

