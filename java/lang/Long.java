/* java.lang.Long
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


package java.lang;

/**
 * Instances of class <code>Double</code> represent primitive
 * <code>double</code> values.
 *
 * Additionally, this class provides various helper functions and variables
 * related to longs.
 *
 * @author Paul Fisher
 * @author John Keiser
 * @since JDK 1.0
 */
public final class Long extends Number {

  // compatible with JDK 1.0.2+
  static final long serialVersionUID = 4290774380558885855L;

    /**
     * The minimum value a <code>long</code> can represent is (someone calculate, please).
     * @XXX calculate the value for the docs
     */
  public static final long MIN_VALUE = 0x8000000000000000L;

    /**
     * The maximum value a <code>long</code> can represent is (someone calculate, please).
     * @XXX calculate the value for the docs
     */
  public static final long MAX_VALUE = 0x7fffffffffffffffL;

    /**
     * The primitive type <code>long</code> is represented by this 
     * <code>Class</code> object.
     */
  public static final Class TYPE = VMClassLoader.getPrimitiveClass("long");

  private long value;
    
  private static final char digits[] = 
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z' };
    
    /**
     * Create a <code>Long</code> object representing the value of the 
     * <code>long</code> argument.
     *
     * @param value the value to use
     */
  public Long(long value) {
    this.value = value;
  }

    /**
     * Create a <code>Long</code> object representing the value of the 
     * argument after conversion to a <code>long</code>.
     *
     * @param s the string to convert.
     */
  public Long(String s) throws NumberFormatException {
    value = parseLong(s, 10);
  }

    /**
     * If the <code>Object</code> is not <code>null</code>, is an
     * <code>instanceof</code> <code>Long</code>, and represents
     * the same primitive <code>long</code> value return 
     * <code>true</code>.  Otherwise <code>false</code> is returned.
     */
  public boolean equals(Object obj) {
	return (obj instanceof Long && ((Long)obj).value == value);
  }

    /**
     * Return a hashcode representing this Object.
     *
     * <code>Long</code>'s hash code is calculated by simply returning its
     * value.
     *
     * @return this Object's hash code.
     */
  public int hashCode() {
    return (int)(value^(value>>>32));
  }

    /**
     * Get the specified system property as a <code>Long</code>.
     *
     * A method similar to <code>Integer</code>'s <code>decode()</code> will be
     * used to interpret the value of the property.
     * 
     * @param nm the name of the system property
     * @return the system property as an <code>Long</code>, or
     *         <code>null</code> if the property is not found or cannot be
     *         decoded as a <code>Long</code>.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see java.lang.Integer#decode(int)
     */
  public static Long getLong(String nm) {
    return getLong(nm, null);
  }

    /**
     * Get the specified system property as an <code>Long</code>, or use a
     * default <code>long</code> value if the property is not found or is not
     * decodable.
     * 
     * A method similar to <code>Integer</code>'s <code>decode()</code> will be
     * used to interpret the value of the property.
     * 
     * @param nm the name of the system property
     * @param val the default value to use if the property is not found or not
     *        a number.
     * @return the system property as a <code>Long</code>, or the default
     *         value if the property is not found or cannot be decoded as a
     *         <code>Long</code>.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see java.lang.Integer#decode(int)
     * @see #getLong(java.lang.String,java.lang.Long)
     */
  public static Long getLong(String nm, long val) {
    Long result = getLong(nm, null);
    return (result == null) ? new Long(val) : result;
  }

    /**
     * Get the specified system property as an <code>Long</code>, or use a
     * default <code>Long</code> value if the property is not found or is
     * not decodable.
     * 
     * The <code>decode()</code> method will be used to interpret the value of
     * the property.
     *
     * @param nm the name of the system property
     * @param val the default value to use if the property is not found or not
     *        a number.
     * @return the system property as an <code>Long</code>, or the default
     *         value if the property is not found or cannot be decoded as an
     *         <code>Long</code>.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see java.lang.Integer#decode(int)
     * @see #getLong(java.lang.String,long)
     */
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

    /**
     * Converts the <code>long</code> to a <code>String</code> assuming it is
     * unsigned in base 16.
     * @param i the <code>long</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument.
     */
  public static String toHexString(long i) {
    return toUnsignedString(i, 4);
  }

    /**
     * Converts the <code>long</code> to a <code>String</code> assuming it is
     * unsigned in base 8.
     * @param i the <code>long</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument.
     */
  public static String toOctalString(long i) {
    return toUnsignedString(i, 3);
  }

    /**
     * Converts the <code>long</code> to a <code>String</code> assuming it is
     * unsigned in base 2.
     * @param i the <code>long</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument.
     */
  public static String toBinaryString(long i) {
    return toUnsignedString(i, 1);
  }

    /**
     * Converts the <code>long</code> to a <code>String</code> and assumes
     * a radix of 10.
     * @param i the <code>long</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument.
     */    
  public static String toString(long i) {
    return toString(i, 10);
  }

    /**
     * Converts the <code>Long</code> value to a <code>String</code> and
     * assumes a radix of 10.
     * @return the <code>String</code> representation of this <code>Long</code>.
     */    
  public String toString() {
    return toString(value, 10);
  }
  
    /**
     * Converts the <code>long</code> to a <code>String</code> using
     * the specified radix (base).
     * @param i the <code>long</code> to convert to <code>String</code>.
     * @param radix the radix (base) to use in the conversion.
     * @return the <code>String</code> representation of the argument.
     */
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
    
    /**
     * Creates a new <code>Long</code> object using the <code>String</code>,
     * assuming a radix of 10.
     * @param s the <code>String</code> to convert.
     * @return the new <code>Long</code>.
     * @see #Long(java.lang.String)
     * @see #parseLong(java.lang.String)
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>long</code>.
     */
  public static Long valueOf(String s) throws NumberFormatException {
    return new Long(parseLong(s));
  }

    /**
     * Creates a new <code>Long</code> object using the <code>String</code>
     * and specified radix (base).
     * @param s the <code>String</code> to convert.
     * @param radix the radix (base) to convert with.
     * @return the new <code>Long</code>.
     * @see #parseLong(java.lang.String,int)
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>long</code>.
     */
  public static Long valueOf(String s, int radix) 
    throws NumberFormatException {
    return new Long(parseLong(s, radix));
  }

    /**
     * Converts the specified <code>String</code> into a <code>long</code>.
     * This function assumes a radix of 10.
     *
     * @param s the <code>String</code> to convert
     * @return the <code>long</code> value of the <code>String</code>
     *         argument.
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>long</code>.
     */
  public static long parseLong(String s) throws NumberFormatException {
    return parseLong(s, 10);
  }

    /**
     * Converts the specified <code>String</code> into a <code>long</code>
     * using the specified radix (base).
     *
     * @param s the <code>String</code> to convert
     * @param radix the radix (base) to use in the conversion
     * @return the <code>String</code> argument converted to </code>long</code>.
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>long</code>.    
     */
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

  /** Return the value of this <code>Long</code> as an <code>short</code>.
   ** @return the value of this <code>Long</code> as an <code>short</code>.
   **/
  public byte byteValue() {
    return (byte) value;
  }

  /** Return the value of this <code>Long</code> as an <code>short</code>.
   ** @return the value of this <code>Long</code> as an <code>short</code>.
   **/
  public short shortValue() {
    return (short) value;
  }

  /** Return the value of this <code>Long</code> as an <code>int</code>.
   ** @return the value of this <code>Long</code> as an <code>int</code>.
   **/
  public int intValue() {
    return (int) value;
  }

  /** Return the value of this <code>Long</code> as a <code>long</code>.
   ** @return the value of this <code>Long</code> as a <code>long</code>.
   **/
  public long longValue() {
    return value;
  }

  /** Return the value of this <code>Long</code> as a <code>float</code>.
   ** @return the value of this <code>Long</code> as a <code>float</code>.
   **/
  public float floatValue() {
    return value;
  }

  /** Return the value of this <code>Long</code> as a <code>double</code>.
   ** @return the value of this <code>Long</code> as a <code>double</code>.
   **/
  public double doubleValue() {
    return value;
  }
}
