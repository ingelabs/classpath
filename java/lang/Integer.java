package java.lang;

public final class Integer extends Number {

  // compatible with JDK 1.0.2+
  static final long serialVersionUID = 1360826667806852920L;

  public static final int MIN_VALUE = 0x80000000;
  public static final int MAX_VALUE = 0x7fffffff;
  public static final Class TYPE = null; /* XXX */

  private int value;
    
  private static final char digits[] = 
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z' };
    
  public Integer(int value) {
    this.value = value;
  }

  public Integer(String s) throws NumberFormatException {
    value = parseInt(s, 10);
  }

  public int hashCode() {
    return value;
  }

  public static Integer getInteger(String nm) {
    return getInteger(nm, null);
  }

  public static Integer getInteger(String nm, int val) {
    Integer result = getInteger(nm, null);
    return (result == null) ? new Integer(val) : result;
  }

  public static Integer getInteger(String nm, Integer def) {
    String val = System.getProperty(nm);
    if (val == null) return def;
    try {
      return decode(nm);
    } catch (NumberFormatException e) {
      return def;
    }
  }

  // when calling toUnsignedString(), (32 % bits) must be 0
  private static String toUnsignedString(int value, int bits) {
    StringBuffer bp = new StringBuffer(32 / bits);
    do {
      bp.append(digits[value & ((1 << bits) - 1)]);
      value >>>= bits;
    } while (value != 0);
    return bp.reverse().toString();
  }

  public static String toHexString(int i) {
    return toUnsignedString(i, 4);
  }

  public static String toOctalString(int i) {
    return toUnsignedString(i, 3);
  }

  public static String toBinaryString(int i) {
    return toUnsignedString(i, 1);
  }

  public static String toString(int i) {
    return toString(i, 10);
  }

  public String toString() {
    return toString(value, 10);
  }
  
  public static String toString(int i, int radix) {
    StringBuffer tmp = new StringBuffer();

    if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
      radix = 10;
    
    boolean negative = (i < 0);
    do
      tmp.append(digits[Math.abs(i % radix)]);
    while ((i /= radix) != 0);
    if (negative) tmp.append('-');
    return tmp.reverse().toString();
  }
    
  public static Integer valueOf(String s) throws NumberFormatException {
    return new Integer(Integer.parseInt(s));
  }

  public static Integer valueOf(String s, int radix) 
    throws NumberFormatException {
    return new Integer(Integer.parseInt(s, radix));
  }

  public static int parseInt(String s) throws NumberFormatException {
    return parseInt(s, 10);
  }

  public static int parseInt(String s, int radix) 
  throws NumberFormatException {
    return parseInt(s, radix, false);
  }

  public static Integer decode(String s) throws NumberFormatException {
    return new Integer(parseInt(s, 10, true));
  }

  private static int parseInt(String s, int radix, boolean decode) 
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

    int cutoff = MAX_VALUE / radix;
    int cutlim = MAX_VALUE % radix;
    int result = 0;

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
    return value;
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
