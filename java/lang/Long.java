package java.lang;

public final class Long extends Number {

  // compatible with JDK 1.0.2+
  static final long serialVersionUID = 4290774380558885855L;

  public static final long MIN_VALUE = 0x8000000000000000L;
  public static final long MAX_VALUE = 0x7fffffffffffffffL;
  public static final Class TYPE = null; /* XXX */

  private long value;
    
  private static final char digits[] = 
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z' };
    
  public Long(long value) {
    this.value = value;
  }

  public Long(String s) throws NumberFormatException {
    value = parseLong(s, 10);
  }

  public boolean equals(Object obj) {
    if (obj == null || (!(obj instanceof Long))) return false;
    return (value == ((Long)obj).longValue());
  }

  public int hashCode() {
    return (int)(value^(value>>>32));
  }

  public static Long getLong(String nm) {
    return getLong(nm, null);
  }

  public static Long getLong(String nm, long val) {
    Long result = getLong(nm, null);
    return (result == null) ? new Long(val) : result;
  }

  public static Long getLong(String nm, Long def) {
    String val = System.getProperty(nm);
    if (val == null) return def;
    try {
      return decode(nm);
    } catch (NumberFormatException e) {
      return def;
    }
  }

  private static String toUnsignedString(long value, int bits) {
    final char digits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                            'u', 'v', 'w', 'x', 'y', 'z' };


    StringBuffer bp = new StringBuffer(64 / bits);
    long hi = value >>> 32;
    if (hi != 0) {
      long lo = value & 0xffffffff;
      for (int cnt = 32 / bits; cnt > 0; --cnt) {
	bp.append(digits[(int)(lo & ((1L << bits) - 1))]);
	lo >>>= bits;
      }
    }
    else
      hi = value & 0xffffffff;
    
    do {
      bp.append(digits[(int)(hi & ((1 << bits) - 1))]);
      hi >>>= bits;
    } while (hi != 0);

    return bp.reverse().toString();
  }

  public static String toHexString(long i) {
    return toUnsignedString(i, 4);
  }

  public static String toOctalString(long i) {
    return toUnsignedString(i, 3);
  }

  public static String toBinaryString(long i) {
    return toUnsignedString(i, 1);
  }

  public static String toString(long i) {
    return toString(i, 10);
  }

  public String toString() {
    return toString(value, 10);
  }
  
  public static String toString(long i, int radix) {
    StringBuffer tmp = new StringBuffer();

    if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
      radix = 10;
    
    boolean negative = (i < 0);
    do
      tmp.append(digits[Math.abs((int)(i % radix))]);
    while ((i /= radix) != 0);
    if (negative) tmp.append('-');
    return tmp.reverse().toString();
  }
    
  public static Long valueOf(String s) throws NumberFormatException {
    return new Long(parseLong(s));
  }

  public static Long valueOf(String s, int radix) 
    throws NumberFormatException {
    return new Long(parseLong(s, radix));
  }

  public static long parseLong(String s) throws NumberFormatException {
    return parseLong(s, 10);
  }

  public static long parseLong(String s, int radix) 
  throws NumberFormatException {
    return parseLong(s, radix, false);
  }

  private static Long decode(String s) throws NumberFormatException {
    return new Long(parseLong(s, 10, true));
  }

  private static long parseLong(String s, int radix, boolean decode) 
    throws NumberFormatException {
    if (s == null || s.length() == 0)
      throw new NumberFormatException("string null or empty");

    if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
      throw new NumberFormatException("radix outside of range: " + radix);

    /* check for a negative value, and setup the initial index for digits */
    boolean negative = false;
    int i = 0;
    if (s.charAt(0) == '-') {
      if (s.length() == 1)
	throw new NumberFormatException("negative sign without value");
      negative = true;
      i++;
    }

    /* attempt to determine the base.  any previous value of radix is
       overwritten, if decoding succeeds. */
    if (decode && !negative) {
      if (s.charAt(i) == '0') {
	try {
	  if (Character.toUpperCase(s.charAt(i+1)) == 'X') {
	    i += 2;
	    radix = 16;
	    if (i >= s.length()) 
	      throw new NumberFormatException("string empty");
	  }
	  else
	    radix = 8;
	} catch (StringIndexOutOfBoundsException e) { }
      }
      else
	if (s.charAt(i) == '#') {
	  i++;
	  radix = 16;
	  if (i >= s.length()) 
	    throw new NumberFormatException("string empty");
	}
	else
	  radix = 10;
    }

    long cutoff = MAX_VALUE / radix;
    long cutlim = MAX_VALUE % radix;
    long result = 0;

    while (i < s.length()) {
      int c = Character.digit(s.charAt(i++), radix);
      if (c == -1) 
	throw new NumberFormatException("char at index " + i + 
					" is not of specified radix");
      if (result > cutoff || (result == cutoff && c > cutlim)) {
	// check to see if we have a MIN_VALUE, by forcing an overflow
	if (negative) {
	  result *= radix;
	  result += c;
	  if (result == MIN_VALUE && i == s.length()) return MIN_VALUE;
	}
	throw new NumberFormatException("overflow");
      }

      result *= radix;
      result += c;
    }
    return (negative) ? -result : result;
  }

  public byte byteValue() {
    return (byte) value;
  }

  public short shortValue() {
    return (short) value;
  }

  public int intValue() {
    return (int) value;
  }

  public long longValue() {
    return value;
  }

  public float floatValue() {
    return value;
  }

  public double doubleValue() {
    return value;
  }
}
