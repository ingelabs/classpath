/* java.lang.Short
   Copyright (C) 1998, 2001 Free Software Foundation, Inc.

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
 * Instances of class <code>Short</code> represent primitive
 * <code>short</code> values.
 *
 * Additionally, this class provides various helper functions and variables
 * related to shorts.
 *
 * @author Paul Fisher
 * @author John Keiser
 * @since JDK 1.0
 */
public final class Short extends Number implements Comparable {

  static final long serialVersionUID = 7515723908773894738L;

    /**
     * The minimum value a <code>short</code> can represent is -32768.
     */
  public static final short MIN_VALUE = -32768;

    /**
     * The minimum value a <code>short</code> can represent is 32767.
     */
  public static final short MAX_VALUE =  32767;

    
    /**
     * The primitive type <code>short</code> is represented by this 
     * <code>Class</code> object.
     */
  public static final Class TYPE = VMClassLoader.getPrimitiveClass("short");

  private short value;

  private static final char digits[] = 
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z' };


    /**
     * Create a <code>Short</code> object representing the value of the 
     * <code>short</code> argument.
     *
     * @param value the value to use
     */
  public Short(short value) {
    this.value = value;
  }

    /**
     * Create a <code>Short</code> object representing the value of the 
     * argument after conversion to a <code>short</code>.
     *
     * @param s the string to convert.
     */
  public Short(String s) throws NumberFormatException {
    value = parseShort(s, 10);
  }

    /**
     * Return a hashcode representing this Object.
     *
     * <code>Short</code>'s hash code is calculated by simply returning its
     * value.
     *
     * @return this Object's hash code.
     */
  public int hashCode() {
    return value;
  }

    /**
     * If the <code>Object</code> is not <code>null</code>, is an
     * <code>instanceof</code> <code>Short</code>, and represents
     * the same primitive <code>short</code> value return 
     * <code>true</code>.  Otherwise <code>false</code> is returned.
     */
  public boolean equals(Object obj) {
	return (obj instanceof Short && ((Short)obj).value == value);
  }

    /**
     * Converts the <code>short</code> to a <code>String</code> and assumes
     * a radix of 10.
     * @param i the <code>short</code> to convert to <code>String</code>
     * @return the <code>String</code> representation of the argument.
     */    
  public static String toString(short i) {
    return toString(i, 10);
  }

    /**
     * Converts the <code>Short</code> value to a <code>String</code> and
     * assumes a radix of 10.
     * @return the <code>String</code> representation of this <code>Short</code>.
     */    
  public String toString() {
    return toString(value, 10);
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

    /**
     * Creates a new <code>Short</code> object using the <code>String</code>,
     * assuming a radix of 10.
     * @param s the <code>String</code> to convert.
     * @return the new <code>Short</code>.
     * @see #Short(java.lang.String)
     * @see #parseShort(java.lang.String)
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>short</code>.
     */
  public static Short valueOf(String s) throws NumberFormatException {
    return new Short(parseShort(s));
  }

    /**
     * Creates a new <code>Short</code> object using the <code>String</code>
     * and specified radix (base).
     * @param s the <code>String</code> to convert.
     * @param radix the radix (base) to convert with.
     * @return the new <code>Short</code>.
     * @see #parseShort(java.lang.String,int)
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>short</code>.
     */
  public static Short valueOf(String s, int radix) 
    throws NumberFormatException {
    return new Short(parseShort(s, radix));
  }

    /**
     * Converts the specified <code>String</code> into a <code>short</code>.
     * This function assumes a radix of 10.
     *
     * @param s the <code>String</code> to convert
     * @return the <code>short</code> value of the <code>String</code>
     *         argument.
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>short</code>.
     */
  public static short parseShort(String s) throws NumberFormatException {
    return parseShort(s, 10);
  }

    /**
     * Converts the specified <code>String</code> into a <code>short</code>
     * using the specified radix (base).
     *
     * @param s the <code>String</code> to convert
     * @param radix the radix (base) to use in the conversion
     * @return the <code>String</code> argument converted to </code>short</code>.
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>short</code>.
     */
  public static short parseShort(String s, int radix) 
  throws NumberFormatException {
    return parseShort(s, radix, false);
  }

    /**
     * Convert the specified <code>String</code> into a <code>Short</code>.
     * The <code>String</code> may represent decimal, hexadecimal, or 
     * octal numbers.
     *
     * The <code>String</code> argument is interpreted based on the leading
     * characters.  Depending on what the String begins with, the base will be
     * interpreted differently:
     *
     * <table>
     * <tr><th>Leading<br>Characters</th><th>Base</th></tr>
     * <tr><td>#</td><td>16</td></tr>
     * <tr><td>0x</td><td>16</td></tr>
     * <tr><td>0X</td><td>16</td></tr>
     * <tr><td>0</td><td>8</td></tr>
     * <tr><td>Anything<br>Else</td><td>10</td></tr>
     * </table>
     *
     * @param s the <code>String</code> to interpret.
     * @return the value of the String as a <code>Short</code>.
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>short</code>.    
     */
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

  /** Return the value of this <code>Short</code> as an <code>short</code>.
   ** @return the value of this <code>Short</code> as an <code>short</code>.
   **/
  public byte byteValue() {
    return (byte) value;
  }

  /** Return the value of this <code>Short</code> as an <code>short</code>.
   ** @return the value of this <code>Short</code> as an <code>short</code>.
   **/
  public short shortValue() {
    return value;
  }

  /** Return the value of this <code>Short</code> as an <code>int</code>.
   ** @return the value of this <code>Short</code> as an <code>int</code>.
   **/
  public int intValue() {
    return value;
  }

  /** Return the value of this <code>Short</code> as a <code>long</code>.
   ** @return the value of this <code>Short</code> as a <code>long</code>.
   **/
  public long longValue() {
    return value;
  }

  /** Return the value of this <code>Short</code> as a <code>float</code>.
   ** @return the value of this <code>Short</code> as a <code>float</code>.
   **/
  public float floatValue() {
    return value;
  }

  /** Return the value of this <code>Short</code> as a <code>double</code>.
   ** @return the value of this <code>Short</code> as a <code>double</code>.
   **/
  public double doubleValue() {
    return value;
  }

    /**
     * Compare two Shorts numerically by comparing their
     * <code>short</code> values.
     * @return a positive value if this <code>Short</code> is greater
     * in value than the argument <code>Short</code>; a negative value
     * if this <code>Short</code> is smaller in value than the argument
     * <code>Short</code>; and <code>0</code>, zero, if this
     * <code>Short</code> is equal in value to the argument
     * <code>Short</code>.  
     *
     * @since 1.2
     */
    public int compareTo(Short s)
    {
        return (value - s.shortValue());
    }
    
    /**
     * Behaves like <code>compareTo(java.lang.Short)</code> unless the Object
     * is not a <code>Short</code>.  Then it throws a 
     * <code>ClassCastException</code>.
     * @exception ClassCastException if the argument is not a
     * <code>Short</code>.
     *
     * @since 1.2
     */
    public int compareTo(Object o)
    {
        return compareTo((Short)o);
    }

}
