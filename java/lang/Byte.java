package java.lang;

public final class Byte extends Number {
  static final long serialVersionUID = -7183698231559129828L;

  public static final byte MIN_VALUE = -128;
  public static final byte MAX_VALUE = 127;
  public static final Class TYPE = null; /* XXX */

  private byte value;

  private static final char digits[] = 
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z' };
    
  public Byte(byte value) {
    this.value = value;
  }

  public Byte(String s) throws NumberFormatException {
    value = parseByte(s, 10);
  }

  public int hashCode() {
    return value;
  }

  public boolean equals(Object obj) {
    if (obj == null || (!(obj instanceof Byte))) return false;
    return (value == ((Byte)obj).byteValue());
  }

  public static String toString(byte i) {
    return toStringStatic(i);
  }

  public String toString() {
    return toStringStatic(value);
  }

  private static String toStringStatic(byte i) {
    StringBuffer tmp = new StringBuffer();
    
    boolean negative = (i < 0);
    do
      tmp.append(digits[Math.abs(i % 10)]);
    while ((i /= 10) != 0);
    if (negative) tmp.append('-');
    return tmp.reverse().toString();
  }
  
  public static Byte valueOf(String s) throws NumberFormatException {
    return new Byte(parseByte(s));
  }

  public static Byte valueOf(String s, int radix) 
    throws NumberFormatException {
    return new Byte(parseByte(s, radix));
  }

  public static byte parseByte(String s) throws NumberFormatException {
    return parseByte(s, 10);
  }

  public static byte parseByte(String s, int radix) 
  throws NumberFormatException {
    return parseByte(s, radix, false);
  }

  public static Byte decode(String s) throws NumberFormatException {
    return new Byte(parseByte(s, 10, true));
  }

  private static byte parseByte(String s, int radix, boolean decode) 
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

    byte cutoff = (byte) (MAX_VALUE / radix);
    byte cutlim = (byte) (MAX_VALUE % radix);
    byte result = 0;

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
    return (byte) ((negative) ? -result : result);
  }

  public byte byteValue() {
    return value;
  }

  public short shortValue() {
    return value;
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
