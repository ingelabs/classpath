package java.lang;

public final class Short extends Number {

  static final long serialVersionUID = 7515723908773894738L;

  public static final short MIN_VALUE = -32678;
  public static final short MAX_VALUE =  32767;
  public static final Class TYPE = VMClassLoader.getPrimitiveClass("short");

  private short value;

  private static final char digits[] = 
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z' };


  public Short(short value) {
    this.value = value;
  }

  public Short(String s) throws NumberFormatException {
    value = parseShort(s, 10);
  }

  public int hashCode() {
    return value;
  }

  public boolean equals(Object obj) {
    if (obj == null || (!(obj instanceof Short))) return false;
    return (value == ((Short)obj).shortValue());
  }

  public static String toString(short i) {
    return toString(i, 10);
  }

  private static String toString(short i, int radix) {
    StringBuffer tmp = new StringBuffer();
    
    boolean negative = (i < 0);
    do
      tmp.append(digits[Math.abs(i % radix)]);
    while ((i /= radix) != 0);
    if (negative) tmp.append('-');
    return tmp.reverse().toString();
  }

  public static Short valueOf(String s) throws NumberFormatException {
    return new Short(parseShort(s));
  }

  public static Short valueOf(String s, int radix) 
    throws NumberFormatException {
    return new Short(parseShort(s, radix));
  }

  public static short parseShort(String s) throws NumberFormatException {
    return parseShort(s, 10);
  }

  public static short parseShort(String s, int radix) 
  throws NumberFormatException {
    return parseShort(s, radix, false);
  }

  public static Short decode(String s) throws NumberFormatException {
    return new Short(parseShort(s, 10, true));
  }

  private static short parseShort(String s, int radix, boolean decode) 
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

    short cutoff = (short) (MAX_VALUE / radix);
    short cutlim = (short) (MAX_VALUE % radix);
    short result = 0;

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
    return (short) ((negative) ? -result : result);
  }

  public byte byteValue() {
    return (byte) value;
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
