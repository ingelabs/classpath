/* java.math.BigInteger -- Arbitary precision integers
   Copyright (C) 1998, 1999, 2001 Free Software Foundation, Inc.

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

package java.math;

import java.util.Random;

public class BigInteger implements Comparable {
  final int native_state = System.identityHashCode(this);

  public static final BigInteger ZERO;
  public static final BigInteger ONE;

  static {
    System.loadLibrary("bigint");
    initNativeState();

    ZERO = new BigInteger();
    ONE = new BigInteger(1L);
  }

  public BigInteger(String val) {
    this(val, 10);
  }

  public BigInteger(String val, int radix) {
    if (!initFromString(forEachDigit(val, radix), radix))
      throw new NumberFormatException(val);
  }

  /**
   * Canonicalizes each char digit in str, keeping a leading minus
   * sign if it exists. 
   */
  static String forEachDigit(String str, int radix) {
    char buf[] = new char[str.length()];
    int i = 0;
    if (str.charAt(0) == '-')
      buf[i++] = '-';

    for ( ; i < buf.length; i++)
      if ((buf[i] = 
           Character.forDigit(Character.digit(str.charAt(i), radix), radix))
	  == '\u0000')
	throw new NumberFormatException(str + " not valid in radix " + radix);

    return new String(buf);
  }

  public BigInteger(int bitLength, int certainty, Random rnd) {
    throw new ArithmeticException("unimplemented");
  }

  public BigInteger(int numBits, Random rnd) {
    this(1, getRandomMagnitude(numBits, rnd));
  }

  private static byte[] getRandomMagnitude(int numBits, Random rnd) {
    int array_size = numBits / 8;
    int extra_bits = numBits % 8;
    if (extra_bits != 0)
      array_size++;

    byte[] data = new byte[array_size];
    rnd.nextBytes(data);
    if (extra_bits != 0)
      data[0] &= (1 << extra_bits) - 1; // mask off any extra bits

    return data;
  }

  public BigInteger(byte[] val) {
    if (val.length == 0)
      throw new NumberFormatException("val.length is 0");
    initFromTwosCompByteArray(val);
  }

  public BigInteger(int signum, byte[] magnitude) {
    switch (signum) {
    case 0:
      for (int i = 0; i < magnitude.length; i++)
	if (magnitude[i] != 0)
	  throw new NumberFormatException("magnitude["+i+"] is non zero");
      initZero();
      break;
    case 1:
    case -1:
      if (magnitude.length == 0)
	initZero();
      else
	initFromSignedMagnitudeByteArray(signum, magnitude);
      break;
    default:
      throw new NumberFormatException("invalid signum");
    }	
  }

  private BigInteger(long l) {
    initFromLong(l);
  }

  private BigInteger() { 
    initZero();
  }

  static public BigInteger valueOf(long l) {
    if (l == 0)
      return ZERO;
    if (l == 1)
      return ONE;
    return new BigInteger(l);
  }

  native public BigInteger abs();
  native public BigInteger add(BigInteger val);
  native public BigInteger subtact(BigInteger val);
  native public BigInteger multiply(BigInteger val);
  native public BigInteger divide(BigInteger val) 
    throws ArithmeticException;
  native public BigInteger remainder(BigInteger val)
    throws ArithmeticException;
  native public BigInteger gcd(BigInteger val);

  public BigInteger[] divideAndRemainder(BigInteger val) 
    throws ArithmeticException {
    BigInteger res[] = new BigInteger[2];
    res[0] = divide(val);
    res[1] = remainder(val);
    return res;
  }

  native public BigInteger pow(int exponent) 
    throws ArithmeticException;
  native public BigInteger modPow(BigInteger exponent,
				  BigInteger m) 
    throws ArithmeticException;
  native public BigInteger mod(BigInteger m)
    throws ArithmeticException;
  native public BigInteger modInverse(BigInteger m) 
    throws ArithmeticException;

  // bitwise operations
  native public BigInteger shiftLeft(int n);
  native public BigInteger shiftRight(int n);
  native public BigInteger and(BigInteger val);
  native public BigInteger or(BigInteger val);
  native public BigInteger xor(BigInteger val);
  native public BigInteger not();
  native public BigInteger andNot(BigInteger val);
  native public int getLowestSetBit();
  native public int bitLength();
  native public int bitCount();
  native public boolean testBit(int n);
  native public BigInteger setBit(int n);
  native public BigInteger clearBit(int n);
  native public BigInteger flipBit(int n);

  native public boolean isProbablePrime(int certainty);

  native public BigInteger negate();
  native public BigInteger subtract(BigInteger val);
  native public int compareTo(BigInteger val);
  public int compareTo(Object o) throws ClassCastException {
    return compareTo((BigInteger)o);
  }
  native public int signum();

  public boolean equals(Object o) {
    return (o instanceof BigInteger && nativeEquals((BigInteger)o));
  }

  public BigInteger min(BigInteger val) {
    switch (compareTo(val)) {
    case -1:
    case 0:
      return this;
    default:
      return val;
    }
  }

  public BigInteger max(BigInteger val) {
    switch (compareTo(val)) {
    case -1:
    case 0:
      return val;
    default:
      return this;
    }
  }

  public native int hashCode();

  static native void initNativeState();
  native boolean initFromString(String val, int radix);
  native void initFromLong(long l);
  native void initFromSignedMagnitudeByteArray(int signum, byte[] magnitude);
  native void initFromTwosCompByteArray(byte[] array);
  native void initZero();

  public native void print();
  native boolean nativeEquals(BigInteger val);

  public native long longValue();
  public int intValue() {
    return (int)longValue();
  }
  
  public native double doubleValue();
  public float floatValue() {
    return (float)doubleValue();
  }

  public native String toString(int radix);
    
  public String toString() {
    return toString(10);
  }

  public native byte[] toByteArray();

  protected void finalize() throws Throwable {
    nativeFinalize();
    super.finalize();
  }

  native void nativeFinalize();

  static public void main(String args[]) {
    BigInteger i = new BigInteger(-549755813888L);
    BigInteger i2 = new BigInteger ("5");
    BigInteger i3 = new BigInteger ("7");
    byte[] foo = new byte[2];
    foo[0] = 0;
    foo[1] = 0;
//      BigInteger i4 = new BigInteger(-1, foo);
//      System.out.println(i4);
    //    BigInteger i5 = new BigInteger(20, new Random(5));

    BigInteger i4 = new BigInteger(-300L);
    System.out.println (i4);
    byte[] bar = i4.toByteArray();
    for (int z = 0; z < bar.length; z++)
      System.out.println(z + ": " + bar[z]);

    BigInteger i5 = new BigInteger(bar);
    System.out.println (i5);

    //    System.out.println(i5);
    //    i.modPow(i2, i3).print();
    //    System.out.println(i.toString());

    //    i3 = i.modInverse(i2);
    //    i3.print();
    //    System.out.println(i.isProbablePrime(50));

    java.math.BigInteger bi = new java.math.BigInteger("11");
    java.math.BigInteger bi2 = new java.math.BigInteger("-8");
    java.math.BigInteger bi3 = new java.math.BigInteger("7");
//    System.out.println(bi.isProbablePrime(50));


//      BigInteger i = new BigInteger("3");
//      BigInteger i2 = new BigInteger ("4");
//      BigInteger i3 = new BigInteger ("7");
//      //    i.print();
//      i.modPow(i2, i3).print();

//      java.math.BigInteger bi = new java.math.BigInteger("3");
//      java.math.BigInteger bi2 = new java.math.BigInteger("4");
//      java.math.BigInteger bi3 = new java.math.BigInteger("7");
//      System.out.println(bi.modPow(bi2, bi3));
  }
}
