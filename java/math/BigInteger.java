public class BigInteger {

  final int native_state = System.identityHashCode(this);

  static {
    System.loadLibrary("gmp2");
    System.loadLibrary("bigint");
    initNativeState();
  }

  public BigInteger(String val) {
    initFromString(val);
  }

  BigInteger() { }

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
  native public BigInteger modPow(BigInteger exponent, // DOES NOT WORK
				  BigInteger m) 
    throws ArithmeticException;
  native public BigInteger mod(BigInteger m)
    throws ArithmeticException;
  native public BigInteger modInverse(BigInteger m) // DOES NOT WORK
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
  native public int compareTo(BigInteger val);
  native public int signum();

  public boolean equals(Object o) {
    if (o == null || (!(o instanceof BigInteger))) return false;
    BigInteger integ = (BigInteger) o;
    if (this == integ) return true;
    return nativeEquals(integ);
  }
    
  static native void initNativeState();
  native void initFromString(String val);
  public native void print();
  native boolean nativeEquals(BigInteger val);

  static public void main(String args[]) {
    BigInteger i = new BigInteger("11");
    BigInteger i2 = new BigInteger ("-8");
    BigInteger i3 = new BigInteger ("7");
    //    i.print();
    System.out.println(i.isProbablePrime(50));

    java.math.BigInteger bi = new java.math.BigInteger("11");
    java.math.BigInteger bi2 = new java.math.BigInteger("-8");
    java.math.BigInteger bi3 = new java.math.BigInteger("7");
    System.out.println(bi.isProbablePrime(50));


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
