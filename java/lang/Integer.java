/* java.lang.Integer
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
 * Instances of class <code>Integer</code> represent primitive
 * <code>int</code> values.
 *
 * Additionally, this class provides various helper functions and variables
 * related to ints.
 *
 * @author Paul Fisher
 * @author John Keiser
 * @since JDK 1.0
 */
public final class Integer extends Number {

  // compatible with JDK 1.0.2+
  static final long serialVersionUID = 1360826667806852920L;

    /**
     * The minimum value an <code>int</code> can represent is (someone calculate, please).
     * @XXX calculate the value for the docs
     */
  public static final int MIN_VALUE = 0x80000000;

    /**
     * The maximum value an <code>int</code> can represent is (someone calculate, please).
     * @XXX calculate the value for the docs
     */
  public static final int MAX_VALUE = 0x7fffffff;

    
    /**
     * The primitive type <code>int</code> is represented by this 
     * <code>Class</code> object.
     */
  public static final Class TYPE = VMClassLoader.getPrimitiveClass("int");

  private int value;
    
  private static final char digits[] = 
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z' };

    /**
     * Create an <code>Integer</code> object representing the value of the 
     * <code>int</code> argument.
     *
     * @param value the value to use
     */
  public Integer(int value) {
    this.value = value;
  }

    /**
     * Create an <code>Integer</code> object representing the value of the 
     * argument after conversion to an <code>int</code>.
     *
     * @param s the string to convert.
     */
  public Integer(String s) throws NumberFormatException {
    value = parseInt(s, 10);
  }

    /**
     * Return a hashcode representing this Object.
     *
     * <code>Integer</code>'s hash code is calculated by simply returning its
     * value.
     *
     * @return this Object's hash code.
     */
  public int hashCode() {
    return value;
  }

    /**
     * If the <code>Object</code> is not <code>null</code>, is an
     * <code>instanceof</code> <code>Integer</code>, and represents
     * the same primitive <code>int</code> value return 
     * <code>true</code>.  Otherwise <code>false</code> is returned.
     */
  public boolean equals(Object obj) {
    return (obj instanceof Integer && value == ((Integer)obj).value);
  }

    /**
     * Get the specified system property as an <code>Integer</code>.
     *
     * The <code>decode()</code> method will be used to interpret the value of
     * the property.
     * @param nm the name of the system property
     * @return the system property as an <code>Integer</code>, or
     *         <code>null</code> if the property is not found or cannot be
     *         decoded as an <code>Integer</code>.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see #decode(int)
     */
  public static Integer getInteger(String nm) {
    return getInteger(nm, null);
  }

    /**
     * Get the specified system property as an <code>Integer</code>, or use a
     * default <code>int</code> value if the property is not found or is not
     * decodable.
     * 
     * The <code>decode()</code> method will be used to interpret the value of
     * the property.
     *
     * @param nm the name of the system property
     * @param val the default value to use if the property is not found or not
     *        a number.
     * @return the system property as an <code>Integer</code>, or the default
     *         value if the property is not found or cannot be decoded as an
     *         <code>Integer</code>.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see #decode(int)
     * @see #getInteger(java.lang.String,java.lang.Integer)
     */
  public static Integer getInteger(String nm, int val) {
    Integer result = getInteger(nm, null);
    return (result == null) ? new Integer(val) : result;
  }

    /**
     * Get the specified system property as an <code>Integer</code>, or use a
     * default <code>Integer</code> value if the property is not found or is
     * not decodable.
     * 
     * The <code>decode()</code> method will be used to interpret the value of
     * the property.
     *
     * @param nm the name of the system property
     * @param val the default value to use if the property is not found or not
     *        a number.
     * @return the system property as an <code>Integer</code>, or the default
     *         value if the property is not found or cannot be decoded as an
     *         <code>Integer</code>.
     * @see java.lang.System#getProperty(java.lang.String)
     * @see #decode(int)
     * @see #getInteger(java.lang.String,int)
     */
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

    /**
     * Converts the <code>int</code> to a <code>String</code> assuming it is
     * unsigned in base 16.
     * @param i the <code>int</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument.
     */
  public static String toHexString(int i) {
    return toUnsignedString(i, 4);
  }

    /**
     * Converts the <code>int</code> to a <code>String</code> assuming it is
     * unsigned in base 8.
     * @param i the <code>int</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument.
     */
  public static String toOctalString(int i) {
    return toUnsignedString(i, 3);
  }

    /**
     * Converts the <code>int</code> to a <code>String</code> assuming it is
     * unsigned in base 2.
     * @param i the <code>int</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument.
     */
  public static String toBinaryString(int i) {
    return toUnsignedString(i, 1);
  }


    /**
     * Converts the <code>int</code> to a <code>String</code> and assumes
     * a radix of 10.
     * @param i the <code>int</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument.
     */
  public static String toString(int i) {
    return toString(i, 10);
  }

    /**
     * Converts the <code>Integer</code> value to a <code>String</code> and
     * assumes a radix of 10.
     * @return the <code>String</code> representation of this <code>Integer</code>.
     */    
  public String toString() {
    return toString(value, 10);
  }
  
    /**
     * Converts the <code>int</code> to a <code>String</code> using
     * the specified radix (base).
     * @param i the <code>int</code> to convert to <code>String</code>.
     * @param radix the radix (base) to use in the conversion.
     * @return the <code>String</code> representation of the argument.
     */
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
    
    /**
     * Creates a new <code>Integer</code> object using the <code>String</code>,
     * assuming a radix of 10.
     * @param s the <code>String</code> to convert.
     * @return the new <code>Integer</code>.
     * @see #Integer(java.lang.String)
     * @see #parseInt(java.lang.String)
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as an <code>int</code>.
     */
  public static Integer valueOf(String s) throws NumberFormatException {
    return new Integer(parseInt(s));
  }

    /**
     * Creates a new <code>Integer</code> object using the <code>String</code>
     * and specified radix (base).
     * @param s the <code>String</code> to convert.
     * @param radix the radix (base) to convert with.
     * @return the new <code>Integer</code>.
     * @see #parseInt(java.lang.String,int)
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as an <code>int</code>.
     */
  public static Integer valueOf(String s, int radix) 
    throws NumberFormatException {
    return new Integer(parseInt(s, radix));
  }

    /**
     * Converts the specified <code>String</code> into an <code>int</code>.
     * This function assumes a radix of 10.
     *
     * @param s the <code>String</code> to convert
     * @return the <code>int</code> value of the <code>String</code>
     *         argument.
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as an <code>int</code>.
     */
  public static int parseInt(String s) throws NumberFormatException {
    return parseInt(s, 10);
  }

    /**
     * Converts the specified <code>String</code> into an <code>int</code>
     * using the specified radix (base).
     *
     * @param s the <code>String</code> to convert
     * @param radix the radix (base) to use in the conversion
     * @return the <code>String</code> argument converted to </code>int</code>.
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>int</code>.    
     */
  public static int parseInt(String s, int radix) 
  throws NumberFormatException {
    return parseInt(s, radix, false);
  }

    /**
     * Convert the specified <code>String</code> into an <code>Integer</code>.
     * The <code>String</code> may represent decimal, hexadecimal, or 
     * octal numbers.
     *
     * The <code>String</code> argument is interpreted based on the leading
     * characters.  Depending on what the String begins with, the base will be
     * interpreted differently:
     *
     * <table border=1>
     * <tr><th>Leading<br>Characters</th><th>Base</th></tr>
     * <tr><td>#</td><td>16</td></tr>
     * <tr><td>0x</td><td>16</td></tr>
     * <tr><td>0X</td><td>16</td></tr>
     * <tr><td>0</td><td>8</td></tr>
     * <tr><td>Anything<br>Else</td><td>10</td></tr>
     * </table>
     *
     * @param s the <code>String</code> to interpret.
     * @return the value of the String as an <code>Integer</code>.
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as an <code>int</code>.    
     */
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

  /** Return the value of this <code>Integer</code> as a <code>byte</code>.
   ** @return the value of this <code>Integer</code> as a <code>byte</code>.
   **/
  public byte byteValue() {
    return (byte) value;
  }

  /** Return the value of this <code>Integer</code> as a <code>short</code>.
   ** @return the value of this <code>Integer</code> as a <code>short</code>.
   **/
  public short shortValue() {
    return (short) value;
  }

  /** Return the value of this <code>Integer</code> as an <code>int</code>.
   ** @return the value of this <code>Integer</code> as an <code>int</code>.
   **/
  public int intValue() {
    return value;
  }

  /** Return the value of this <code>Integer</code> as a <code>long</code>.
   ** @return the value of this <code>Integer</code> as a <code>long</code>.
   **/
  public long longValue() {
    return value;
  }

  /** Return the value of this <code>Integer</code> as a <code>float</code>.
   ** @return the value of this <code>Integer</code> as a <code>float</code>.
   **/
  public float floatValue() {
    return value;
  }

  /** Return the value of this <code>Integer</code> as a <code>double</code>.
   ** @return the value of this <code>Integer</code> as a <code>double</code>.
   **/
  public double doubleValue() {
    return value;
  }
}
