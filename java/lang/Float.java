/*
 * java.lang.Float: part of the Java Class Libraries project.
 * Copyright (C) 1998 Free Software Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.lang;

/**
 * Instances of class <code>Float</code> represent primitive
 * <code>float</code> values.
 *
 * Additionally, this class provides various helper functions and variables
 * related to floats.
 *
 * @author Paul Fisher
 * @since JDK 1.0
 */
public final class Float extends Number 
{
    /**
     * The minimum positive value a <code>float</code> may represent
     * is 1.4e-45.
     */
    public static final float MIN_VALUE = 1.4e-45f;

    /**
     * The maximum positive value a <code>double</code> may represent
     * is 3.4028235e+38f.
     */
    public static final float MAX_VALUE = 3.4028235e+38f;

    /**
     * The value of a float representation -1.0/0.0, negative infinity.
     */
    public static final float NEGATIVE_INFINITY = -1.0f/0.0f;

    /**
     * The value of a float representation 1.0/0.0, positive infinity.
     */
    public static final float POSITIVE_INFINITY = 1.0f/0.0f;

    /**
     * All IEEE 754 values of NaN have the same value in Java.
     */
    public static final float NaN = 0.0f/0.0f;

    /**
     * The primitive type <code>float</code> is represented by this 
     * <code>Class</code> object.
     */
    public static final Class TYPE = VMClassLoader.getPrimitiveClass("float");

    private float value;

    static
    {
	System.loadLibrary("javalang");
    }
    
    /**
     * Create a <code>float</code> from the primitive <code>Float</code>
     * specified.
     *
     * @param value the <code>Float</code> argument
     */ 
    public Float(float value) 
    {
	this.value = value;
    }

    /**
     * Create a <code>Float</code> from the primitive <code>double</code>
     * specified.
     *
     * @param value the <code>double</code> argument
     */ 
    public Float(double value) 
    {
	this((float)value);
    }

    /**
     * Create a <code>Float</code> from the specified <code>String</code>.
     *
     * This method calls <code>Float.parseFloat()</code>.
     *
     * @exception NumberFormatException when the <code>String</code> cannot
     *            be parsed into a <code>Float</code>.
     * @exception NullPointerException when the argument is <code>null</code>.
     * @param s the <code>String</code> to convert
     * @see #parseFloat(java.lang.String)
     */
    public Float(String s) throws NumberFormatException, NullPointerException
    {
	value = parseFloat(s);
    }
    
    /**
     * Convert the <code>float</code> value of this <code>Float</code>
     * to a <code>String</code>.  This method calls
     * <code>Float.toString(float)</code> to do its dirty work.
     *
     * @return the <code>String</code> representation of this <code>Float</code>.
     * @see #toString(float)
     */
    public String toString() {
	return toString(value);
    }
    
    /**
     * If the <code>Object</code> is not <code>null</code>, is an
     * <code>instanceof</code> <code>Float</code>, and represents
     * the same primitive <code>float</code> value return 
     * <code>true</code>.  Otherwise <code>false</code> is returned.
     *
     * @param obj the object to compare to
     * @return whether the objects are semantically equal.
     */
    public boolean equals(Object obj) 
    {
	return (obj instanceof Float && ((Float)obj).value == value);
    }

    /**
     * Return a hashcode representing this Object.
     * <code>Float</code>'s hash code is calculated by calling the
     * <code>floatToIntBits()</code> function.
     * @return this Object's hash code.
     * @see java.lang.Float.floatToIntBits(float)
     */    
    public int hashCode() 
    {
	return floatToIntBits(value);
    }
    /**
     * Return the value of this <code>Double</code> when cast to an 
     * <code>int</code>.
     */
    public int intValue() 
    {
	return (int)value;
    }

    /**
     * Return the value of this <code>Double</code> when cast to a
     * <code>long</code>.
     */    
    public long longValue() 
    {
	return (long)value;
    }
    
    /**
     * Return the value of this <code>Double</code> when cast to a
     * <code>float</code>.
     */
    public float floatValue() 
    {
	return (float)value;
    }

    /**
     * Return the primitive <code>double</code> value represented by this
     * <code>Double</code>.
     */    
    public double doubleValue() 
    {
	return value;
    }
    
    /**
     * Convert the <code>float</code> to a <code>String</code>.
     * <P>
     *
     * Floating-point string representation is fairly complex: here is a
     * rundown of the possible values.  "<CODE>[-]</CODE>" indicates that a
     * negative sign will be printed if the value (or exponent) is negative.
     * "<CODE>&lt;number&gt;</CODE>" means a string of digits (0-9).
     * "<CODE>&lt;digit&gt;</CODE>" means a single digit (0-9).
     * <P>
     *
     * <TABLE BORDER=1>
     * <TR><TH>Value of Float</TH><TH>String Representation</TH></TR>
     * <TR>
     *     <TD>[+-] 0</TD>
     *     <TD>[<CODE>-</CODE>]<CODE>0.0</CODE></TD>
     * </TR>
     * <TR>
     *     <TD>Between [+-] 10<SUP>-3</SUP> and 10<SUP>7</SUP></TD>
     *     <TD><CODE>[-]number.number</CODE></TD>
     * </TR>
     * <TR>
     *     <TD>Other numeric value</TD>
     *     <TD><CODE>[-]&lt;digit&gt;.&lt;number&gt;E[-]&lt;number&gt;</CODE></TD>
     * </TR>
     * <TR>
     *     <TD>[+-] infinity</TD>
     *     <TD><CODE>[-]Infinity</CODE></TD>
     * </TR>
     * <TR>
     *     <TD>NaN</TD>
     *     <TD><CODE>NaN</CODE></TD>
     * </TR>
     * </TABLE>
     *
     * Yes, negative zero <EM>is</EM> a possible value.  Note that there is
     * <EM>always</EM> a <CODE>.</CODE> and at least one digit printed after
     * it: even if the number is 3, it will be printed as <CODE>3.0</CODE>.
     * After the ".", all digits will be printed except trailing zeros.  No
     * truncation or rounding is done by this function.
     *
     * @XXX specify where we are not in accord with the spec.
     *
     * @param f the <code>float</code> to convert
     * @return the <code>String</code> representing the <code>float</code>.
     */
    public native static String toString(float f);

    /**
     * Return the result of calling <code>new Float(java.lang.String)</code>.
     *
     * @param s the <code>String</code> to convert to a <code>Float</code>.
     * @return a new <code>Float</code> representing the <code>String</code>'s
     *         numeric value.
     *
     * @exception NullPointerException thrown if <code>String</code> is 
     * <code>null</code>.
     * @exception NumberFormatException thrown if <code>String</code> cannot
     * be parsed as a <code>double</code>.
     * @see #Float(java.lang.String)
     * @see #parseFloat(java.lang.String)
     */
    public static Float valueOf(String s)
	throws NumberFormatException, NullPointerException
    {
	return new Float(s);
    }
    
    /**
     * Return <code>true</code> if the value of this <code>Float</code>
     * is the same as <code>NaN</code>, otherwise return <code>false</code>.
     * @return whether this <code>Float</code> is <code>NaN</code>.
     */
    public boolean isNaN() 
    {
	return isNaN(value);
    }

    /**
     * Return <code>true</code> if the <code>float</code> has the same
     * value as <code>NaN</code>, otherwise return <code>false</code>.
     *
     * @param d the <code>float</code> to compare
     * @return whether the argument is <code>NaN</code>.
     */
    public static boolean isNaN(float v) 
    {
	return (floatToIntBits(v) == 0x7fc00000);
    }

    /**
     * Return <code>true</code> if the value of this <code>Float</code>
     * is the same as <code>NEGATIVE_INFINITY</code> or 
     * <code>POSITIVE_INFINITY</code>, otherwise return <code>false</code>.
     *
     * @return whether this <code>Float</code> is (-/+) infinity.
     */
    public boolean isInfinite() 
    {
	return isInfinite(value);
    }
    
    /**
     * Return <code>true</code> if the <code>float</code> has a value 
     * equal to either <code>NEGATIVE_INFINITY</code> or 
     * <code>POSITIVE_INFINITY</code>, otherwise return <code>false</code>.
     *
     * @param f the <code>float</code> to compare
     * @return whether the argument is (-/+) infinity.
     */
    public boolean isInfinite(float f) 
    {
	return (f == POSITIVE_INFINITY || f == NEGATIVE_INFINITY);
    }

    /**
     * Return the int bits of the specified <code>float</code>.
     * The result of this function can be used as the argument to
     * <code>Float.intBitsToFloat(long)</code> to obtain the
     * original <code>float</code> value.
     *
     * @param value the <code>float</code> to convert
     * @return the bits of the <code>float</code>.
     */
    public native static int floatToIntBits(float value);

    /**
     * Return the <code>float</code> represented by the long
     * bits specified.
     *
     * @param bits the long bits representing a <code>double</code>
     * @return the <code>float</code> represented by the bits.
     */
    public native static float intBitsToFloat(int bits);

    /**
     * Parse the specified <code>String</code> as a <code>float</code>.
     * <P>
     *
     * The number is really read as <em>n * 10<sup>exponent</sup></em>.  The
     * first number is <em>n</em>, and if there is an "<code>E</code>"
     * ("<code>e</code>" is also acceptable), then the integer after that is
     * the exponent.
     * <P>
     *
     * Here are the possible forms the number can take:
     * <BR>
     * <TABLE BORDER=1>
     *     <TR><TH>Form</TH><TH>Examples</TH></TR>
     *     <TR><TD><CODE>[+-]&lt;number&gt;[.]</CODE></TD><TD>345., -10, 12</TD></TR>
     *     <TR><TD><CODE>[+-]&lt;number&gt;.&lt;number&gt;</CODE></TD><TD>40.2, 80.00, -12.30</TD></TR>
     *     <TR><TD><CODE>[+-]&lt;number&gt;[.]E[+-]&lt;number&gt;</CODE></TD><TD>80E12, -12e+7, 4.E-123</TD></TR>
     *     <TR><TD><CODE>[+-]&lt;number&gt;.&lt;number&gt;E[+-]&lt;number&gt;</CODE></TD><TD>6.02e-22, -40.2E+6, 12.3e9</TD></TR>
     * </TABLE>
     *
     * "<code>[+-]</code>" means either a plus or minus sign may go there, or
     * neither, in which case + is assumed.
     * <BR>
     * "<code>[.]</code>" means a dot may be placed here, but is optional.
     * <BR>
     * "<code>&lt;number&gt;</code>" means a string of digits (0-9), basically
     * an integer.  "<code>&lt;number&gt;.&lt;number&gt;</code>" is basically
     * a real number, a floating-point value.
     * <P>
     *
     * Remember that a <code>float</code> has a limited range.  If the
     * number you specify is greater than <code>Float.MAX_VALUE</code> or less
     * than <code>-Float.MAX_VALUE</code>, it will be set at
     * <code>Float.POSITIVE_INFINITY</code> or
     * <code>Float.NEGATIVE_INFINITY</code>, respectively.
     * <P>
     *
     * Note also that <code>float</code> does not have perfect precision.  Many
     * numbers cannot be precisely represented.  The number you specify
     * will be rounded to the nearest representable value.  <code>Float.MIN_VALUE</code> is
     * the margin of error for <code>float</code> values.
     * <P>
     *
     * If an unexpected character is found in the <code>String</code>, a
     * <code>NumberFormatException</code> will be thrown.  Spaces are not
     * allowed and will cause this exception to be thrown.
     *
     * @XXX specify where/how we are not in accord with the spec.
     *
     * @param str the <code>String</code> to convert
     * @return the value of the <code>String</code> as a <code>float</code>.
     * @exception NumberFormatException when the string cannot be parsed to a
     *            <code>float</code>.
     * @exception NullPointerException when the String is null.
     * @since JDK 1.2
     * @see #MIN_VALUE
     * @see #MAX_VALUE
     * @see #POSITIVE_INFINITY
     * @see #NEGATIVE_INFINITY
     */
    public native static float parseFloat(String s)
    throws NumberFormatException, NullPointerException;
}
