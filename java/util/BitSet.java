/* java.util.BitSet
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.util;
import java.io.*;

/**
 * This class can be viewed under to aspects.  You can see it as a
 * vector of bits or as a set of nonnegative integers.  The name
 * <code>BitSet</code> is a bit misleading.
 *
 * It is implemented by a bit vector, but its equally possible to see
 * it as set of nonnegative integer; each integer in the set is
 * represented by a set bit at the corresponding index.  The size of
 * this structure is determined by the highest integer in the set.
 *
 * You can union, intersect and build (symmetric) remainders, by
 * invoking the logical operations and, or, andNot, resp. xor.
 *
 * This class is thread safe, in the sense, that every method acts as
 * if it were atomic.
 *
 * @author Jochen Hoenicke */
public class BitSet implements Cloneable, java.io.Serializable
{
  private final static int INDEX_BITS = 6;
  private final static int INDEX_MASK = (1 << INDEX_BITS) - 1;
  
  /**
   * This field contains the bits.  The k-th bit is set, iff
   * <pre>
   * k/64 < bits.length  && ((bits[k/64] & (1<<(k%64))) != 0)
   * </pre>
   * bits must not be null.
   * @serial
   */
  private long bits[];
  /*{ invariant { bits != null :: "bit field is null"; } }*/
  
  /**
   * Contains an approximation to the value to be returned by length().
   * This value is always greater or equal to the length, and smaller
   * then bits.length << INDEX_BITS;
   */
  private transient int currentLength = 0;
  /*{ invariant { currentLength > (bits.length << INDEX_BITS) :: 
                  "currentLength too big"; } }*/
  
  /* The serialized format is compatible to the the sun classes.
   */
  private static final long serialVersionUID = 7997698588986878753L;
  
  /**
   * Create a new empty bit set.
   */
  public BitSet()
    {
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
  public BitSet(int nbits)
    {
      bits = new long[nbits >> INDEX_BITS];
    }
  
  /**
   * Grows the bits array.  You should make sure, that the array is indeed
   * smaller than necessary.
   * @param toSize The minimum size we need.
   * @require toSize > bits.length
   */
  private void grow(int toSize) 
    {
      int nextSize = Math.max(bits.length*2, toSize);
      long[] newBits = new long[nextSize];
      System.arraycopy(bits, 0, newBits, 0, bits.length);
      bits = newBits;
    }
  
  /**
   * Returns the logical number of bits actually used by this bit
   * set.  It returns the index of the highest set bit plus one.
   * Note that this method doesn't return the number of set bits.
   * @return the index of the highest set bit plus one.  
   */
  public synchronized int length() 
    {
      /* set k to highest index that can contain non null value */
      int k = (currentLength-1) >> INDEX_BITS;

      /* check if currentLength is exact */
      if (currentLength == 0
	  || (bits[k] & (1 << (currentLength-1 & INDEX_MASK))) >= 0)
	return currentLength;

      /* Find the highest k with bits[k] != 0 */
      while(k >= 0 && bits[k] == 0)
	k--;

      /* if k < 0 all bits are cleared. */
      if (k < 0)
	currentLength = 0;
      else
	{
	  /* Now determine the exact length. */
	  long b = bits[k];
	  currentLength = (k+1) << INDEX_BITS;
	  /* b >= 0 checks if the highest bit is zero. */
	  while (b >= 0) 
	    {
	      currentLength--;
	      b <<= 1;
	    }
	}
      return currentLength;
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
  public synchronized void set(int bitIndex) 
    {
      int intNr = bitIndex >> INDEX_BITS;
      if (intNr >= bits.length)
	{
	  grow(intNr+1);
	  currentLength = bitIndex + 1;
	}
      else if (currentLength <= bitIndex)
	currentLength = bitIndex + 1;
      bits[intNr] |= 1L << (bitIndex & INDEX_MASK);
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
  public synchronized void clear(int bitIndex) 
    {
      int intNr = bitIndex >> INDEX_BITS;

      /* if the highest bit was cleared, the new length 
       * is smaller than bitIndex. */
      if (currentLength == bitIndex + 1)
	currentLength = bitIndex;

      /* if intNr >= bits.length the bit wasn't set, so this method
       * does nothing.
       */
      if (intNr < bits.length)
	bits[intNr] &= ~(1L << (bitIndex & INDEX_MASK));
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
  public boolean get(int bitIndex) 
    {
      /* No need to synchronize.  If the bit is changed just in
       * that moment, every return value is correct.
       * Note that this is only true, because the array can't shrink, 
       * otherwise we could get a wrong ArrayIndexOutOfBoundsException.
       */
      int intNr = bitIndex >> INDEX_BITS;
      if (intNr < bits.length)
	return (bits[intNr] & (1L << (bitIndex & INDEX_MASK))) != 0;
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
  public void and(BitSet set) 
    {
      BitSet first, second;
      /* To avoid deadlocks we have to order the monitors.
       * Note that identityHashCode is suboptimal, because there
       * may be a slight chance that they are the same.
       */
      if (System.identityHashCode(this) < System.identityHashCode(set)) 
	{
	  first = this;
	  second = set;
        }
      else 
	{
	  first = set;
	  second = this;
        }
      synchronized(first) 
	{
	  synchronized(second) 
	    {
	      /* The smallest of the both lengths is enough. */
	      int newLength = Math.min(currentLength, 
				       set.currentLength);
	      int length = (newLength + INDEX_MASK) >> INDEX_BITS;
	      int i;
	      for (i=0; i < length; i++)
		bits[i] &= set.bits[i];
	      /* clear the remaining bits. */
	      length = (currentLength + INDEX_MASK) >> INDEX_BITS;
	      for (; i < length; i++)
		bits[i] = 0;
	      currentLength = newLength;
            }
        }
    }
  
  /**
   * Performs the logical AND operation on this bit set and the
   * complement of the given <code>set</code>.  This means it
   * selects every element in the first set, that isn't in the
   * second set.  The result is stored into this bit set.  
   * @param set the second bit set.  
   * @require set != null
   * @since JDK1.2
   */
  public void andNot(BitSet set) 
    {
      BitSet first, second;
      /* To avoid deadlocks we have to order the monitors.
       * Note that identityHashCode is suboptimal, because there
       * may be a slight chance that they are the same.
       */
      if (System.identityHashCode(this) < System.identityHashCode(set)) 
	{
	  first = this;
	  second = set;
	} 
      else 
	{
	  first = set;
	  second = this;
        }
      synchronized(first) 
	{
	  synchronized(second) 
	    {
	      int length = (currentLength + INDEX_MASK) >> INDEX_BITS;
	      for (int i=0; i < length; i++)
		bits[i] &= ~set.bits[i];
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
  public void or(BitSet set) 
    {
      BitSet first, second;
      /* To avoid deadlocks we have to order the monitors.
       * Note that identityHashCode is suboptimal, because there
       * may be a slight chance that they are the same.
       */
      if (System.identityHashCode(this) < System.identityHashCode(set)) 
	{
	  first = this;
	  second = set;
	}
      else
	{
	  first = set;
	  second = this;
        }
      synchronized(first)
	{
	  synchronized(second)
	    {
	      /* We calculate a good approximation to set.currentLength, 
	       * since it may save us a growing call.
	       */
	      int max = (set.currentLength - 1) >> INDEX_BITS;
	      while (max >= 0 && set.bits[max] == 0)
		{
		  set.currentLength = max << INDEX_BITS;
		  max--;
		}
	      currentLength = Math.max(currentLength, set.currentLength);
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
  public synchronized void xor(BitSet set) 
    {
      BitSet first, second;
      /* To avoid deadlocks we have to order the monitors.
       * Note that identityHashCode is suboptimal, because there
       * may be a slight chance that they are the same.
       */
      if (System.identityHashCode(this) < System.identityHashCode(set)) 
	{
	  first = this;
	  second = set;
        } 
      else
	{
	  first = set;
	  second = this;
        }
      synchronized(first)
	{
	  synchronized(second)
	    {
	      /* We calculate a good approximation to set.currentLength, 
	       * since it may save us a growing call.
	       */
	      int max = (set.currentLength - 1) >> INDEX_BITS;
	      while (max >= 0 && set.bits[max] == 0)
		{
		  set.currentLength = max << INDEX_BITS;
		  max--;
		}
	      currentLength = Math.max(currentLength, set.currentLength);
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
   *     for (int i = bits.length-1; i>=0; i--) {
   *         h ^= bits[i] * (i + 1);
   *     }
   *     return (int)((h >> 32) ^ h);
   * }
   * </pre>
   *
   * Note that the hash code values changes, if the set is changed.
   * @return the hash code value for this bit set.
   */
  public synchronized int hashCode() 
    {
      long h = 1234;
      for (int i = (currentLength - 1) >> INDEX_BITS; i>=0; i--) 
	h ^= bits[i] * (i + 1);
      return (int)((h >> 32) ^ h);
    }

  /**
   * Returns the number of bits actually used by this bit set.  Note
   * that this method doesn't return the number of set bits.
   * @returns the number of bits currently used.  
   */
  public int size() 
    {
      return bits.length << INDEX_BITS;
    }
  

  /**
   * Returns true if the <code>obj</code> is a bit set that contains
   * exactly the same elements as this bit set, otherwise false.
   * @return true if obj equals this bit set.
   */
  public boolean equals(Object obj) 
    {
      if (!(obj instanceof BitSet))
	return false;
      if (this == obj)
	return true;
      BitSet first, second;
      /* To avoid deadlocks we have to order the monitors.
       * Note that identityHashCode is suboptimal, because there
       * may be a slight chance that they are the same (at least
       * on 64 bit architectures). But in 99.99999997 % it works.
       */
      if (System.identityHashCode(this) < System.identityHashCode(obj)) 
	{
	  first = this;
	  second = (BitSet) obj;
        }
      else
	{
	  first = (BitSet) obj;
	  second = this;
        }
      synchronized(first)
	{
	  int i;
	  synchronized(second)
	    {
	      int length = (Math.min(first.currentLength, second.currentLength)
			    + INDEX_MASK) >> INDEX_BITS;
	      for (i=0; i < length; i++)
		{
		  if (first.bits[i] != second.bits[i])
		    return false;
                }
	      length = (second.currentLength + INDEX_MASK) >> INDEX_BITS;
	      for (; i < length; i++)
		{
		  if (second.bits[i] != 0)
		    return false;
                }
            }
	  int length = (first.currentLength + INDEX_MASK) >> INDEX_BITS;
	  for (; i < length; i++)
	    {
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
  public synchronized Object clone() 
    {
      try
	{
	  BitSet clone = (BitSet) super.clone();
	  clone.bits = (long[]) bits.clone();
	  return clone;
	}
      catch (CloneNotSupportedException ex)
	{
	  return null;
	}
    }
  
  /**
   * Returns the string representation of this bit set.  This
   * consists of a comma separated list of the integers in this set
   * surrounded by curly braces.  There is a space after each comma.
   * @return the string representation.
   */
  public synchronized String toString()
    {
      StringBuffer result = new StringBuffer("{");
      String comma = "";
      for (int i=0; i<bits.length; i++)
	{
	  for (int j=0; j < (1 << INDEX_BITS); j++)
	    {
	      if ((bits[i] & (1L << j)) != 0)
		{
		  result.append(comma).append((i << INDEX_BITS)+j);
		  comma = ", ";
		}
	    }
	}
      result.append('}');
      return result.toString();
    }
    
  /**
   * This method does initialize the transient field to sane values
   * after reading the object from stream.
   */
  private void readObject(ObjectInputStream input) 
    throws IOException, ClassNotFoundException
    {
      input.defaultReadObject();
      /* currentLength is not known */
      currentLength = bits.length << INDEX_BITS;
    }
}
