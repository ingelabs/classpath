/* java.math.VMBigInteger -- Arbitary precision integers using GMP
   Copyright (C) 2006 Free Software Foundation, Inc.

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
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package java.math;

import gnu.classpath.Pointer;

/**
 * Implement BigInteger using GMP
 */
final class VMBigInteger
{
  private Pointer native_ptr;
  private int refCount = 1;
  
  VMBigInteger()
  {
    super();
    
    natInitialize();
  }

  private synchronized void acquireRef()
  {
    refCount++;
  }
  
  private synchronized void releaseRef()
  {
    refCount--;
    if (refCount == 0)
      {
	natFinalize();
	native_ptr = null;
      }
  }
  
  protected void finalize()
  {
    releaseRef();
  }
  
  
  void fromByteArray(byte[] v)
  {
    this.acquireRef();
    natFromByteArray(v);
    this.releaseRef();
  }
  
  void fromBI(BigInteger x)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    natFromBI(x.mpz.native_ptr);
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void fromLong(long n)
  {
    this.acquireRef();
    natFromLong(n);
    this.releaseRef();
  }
  
  int fromString(String s, int rdx)
  {
    this.acquireRef();
    int result = natFromString(s, rdx);
    this.releaseRef();
    return result;
  }
  
  void fromSignedMagnitude(byte[] m, boolean isNegative)
  {
    this.acquireRef();
    natFromSignedMagnitude(m, isNegative);
    this.releaseRef();
  }
  
  String toString(int b)
  {
    this.acquireRef();
    String result = natToString(b);
    this.releaseRef();
    return result;
  }
  
  void toByteArray(byte[] r)
  {
    this.acquireRef();
    natToByteArray(r);
    this.releaseRef();
  }
  
  double doubleValue()
  {
    this.acquireRef();
    double result = natDoubleValue();
    this.releaseRef();
    return result;
  }
  
  int absIntValue()
  {
    this.acquireRef();
    int result = natAbsIntValue();
    this.releaseRef();
    return result;
  }
  
  int compare(BigInteger x)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    int result = natCompare(x.mpz.native_ptr);
    x.mpz.releaseRef();
    this.releaseRef();
    return result;
  }
  
  void add(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natAdd(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void subtract(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natSubtract(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void multiply(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natMultiply(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void quotient(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natQuotient(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void remainder(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natRemainder(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void quotientAndRemainder(BigInteger x, BigInteger q, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    q.mpz.acquireRef();
    r.mpz.acquireRef();
    natQuotientAndRemainder(x.mpz.native_ptr, q.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    q.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void modulo(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natModulo(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void pow(int n, BigInteger r)
  {
    this.acquireRef();
    r.mpz.acquireRef();
    natPow(n, r.mpz.native_ptr);
    r.mpz.releaseRef();
    this.releaseRef();
  }
  
  void modPow(BigInteger e, BigInteger m, BigInteger r)
  {
    this.acquireRef();
    e.mpz.acquireRef();
    m.mpz.acquireRef();
    r.mpz.acquireRef();
    natModPow(e.mpz.native_ptr, m.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    m.mpz.releaseRef();
    e.mpz.releaseRef();
    this.releaseRef();
  }
  
  void modInverse(BigInteger m, BigInteger r)
  {
    this.acquireRef();
    m.mpz.acquireRef();
    r.mpz.acquireRef();
    natModInverse(m.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    m.mpz.releaseRef();
    this.releaseRef();
  }

  void gcd(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natGCD(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void shiftLeft(int n, BigInteger r)
  {
    this.acquireRef();
    r.mpz.acquireRef();
    natShiftLeft(n, r.mpz.native_ptr);
    r.mpz.releaseRef();
    this.releaseRef();
  }
  
  void shiftRight(int n, BigInteger r)
  {
    this.acquireRef();
    r.mpz.acquireRef();
    natShiftRight(n, r.mpz.native_ptr);
    r.mpz.releaseRef();
    this.releaseRef();
  }
  
  void abs(BigInteger r)
  {
    this.acquireRef();
    r.mpz.acquireRef();
    natAbs(r.mpz.native_ptr);
    r.mpz.releaseRef();
    this.releaseRef();
  }
  
  void negate(BigInteger r)
  {
    this.acquireRef();
    r.mpz.acquireRef();
    natNegate(r.mpz.native_ptr);
    r.mpz.releaseRef();
    this.releaseRef();
  }
  
  int bitLength()
  {
    this.acquireRef();
    int result = natBitLength();
    this.releaseRef();
    return result;
  }
  
  int bitCount()
  {
    this.acquireRef();
    int result = natSetBitCount();
    this.releaseRef();
    return result;
  }
  
  void and(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natAnd(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void or(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natOr(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void xor(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natXor(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void andNot(BigInteger x, BigInteger r)
  {
    this.acquireRef();
    x.mpz.acquireRef();
    r.mpz.acquireRef();
    natAndNot(x.mpz.native_ptr, r.mpz.native_ptr);
    r.mpz.releaseRef();
    x.mpz.releaseRef();
    this.releaseRef();
  }
  
  void not(BigInteger r)
  {
    this.acquireRef();
    r.mpz.acquireRef();
    natNot(r.mpz.native_ptr);
    r.mpz.releaseRef();
    this.releaseRef();
  }
  
  void flipBit(int n, BigInteger r)
  {
    this.acquireRef();
    r.mpz.acquireRef();
    natFlipBit(n, r.mpz.native_ptr);
    r.mpz.releaseRef();
    this.releaseRef();
  }

  int testBit(int n)
  {
    this.acquireRef();
    int result = natTestBit(n);
    this.releaseRef();
    return result;
  }
  
  void setBit(int n, boolean setIt, BigInteger r)
  {
    this.acquireRef();
    r.mpz.acquireRef();
    natSetBit(n, setIt, r.mpz.native_ptr);
    r.mpz.releaseRef();
    this.releaseRef();
  }
  
  int testPrimality(int certainty)
  {
    this.acquireRef();
    int result = natTestPrimality(certainty);
    this.releaseRef();
    return result;
  }
  
  int lowestSetBit()
  {
    this.acquireRef();
    int result = natLowestSetBit();
    this.releaseRef();
    return result;
  }
  
  // Native methods .........................................................
  
  static native void natInitializeLibrary();
  
  native void natInitialize();
  native void natFinalize();
  
  native void natFromLong(long n);
  native void natFromBI(Pointer x);
  native void natFromByteArray(byte[] v);
  native int natFromString(String s, int rdx);
  native void natFromSignedMagnitude(byte[] m, boolean isNegative);
  
  native String natToString(int base);
  native void natToByteArray(byte[] r);
  native int natAbsIntValue();
  native double natDoubleValue();
  
  native int natCompare(Pointer y);
  native void natAdd(Pointer x, Pointer r);
  native void natSubtract(Pointer x, Pointer r);
  native void natMultiply(Pointer x, Pointer r);
  native void natQuotient(Pointer x, Pointer r);
  native void natRemainder(Pointer x, Pointer r);
  native void natQuotientAndRemainder(Pointer x, Pointer q, Pointer r);
  native void natModulo(Pointer m, Pointer r);
  native void natPow(int n, Pointer r);
  native void natModPow(Pointer e, Pointer m, Pointer r);
  native void natModInverse(Pointer x, Pointer r);
  native void natGCD(Pointer x, Pointer r);
  native int natTestPrimality(int c);
  native void natShiftLeft(int n, Pointer r);
  native void natShiftRight(int n, Pointer r);
  native int natLowestSetBit();
  native void natAbs(Pointer r);
  native void natNegate(Pointer r);
  native int natBitLength();
  native int natSetBitCount();
  native void natXor(Pointer x, Pointer r);
  native void natOr(Pointer x, Pointer r);
  native void natAnd(Pointer x, Pointer r);
  native void natAndNot(Pointer x, Pointer r);
  native void natFlipBit(int n, Pointer r);
  native int natTestBit(int n);
  native void natSetBit(int n, boolean setIt, Pointer r);
  native void natNot(Pointer r);
}
