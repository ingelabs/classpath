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
    
    public static final int WIDEFP_SIGNIFICAND_BITS = 24;
    public static final int WIDEFP_MIN_EXPONENT = -126;
    public static final int WIDEFP_MAX_EXPONENT = 127;
    
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
     * @param s the <code>String</code> to convert
     */
    public Float(String s) throws NullPointerException, 
    NumberFormatException 
    {
	value = parseFloat(s);
    }
    
    /**
     * Convert the <code>float</code> value of this <code>Float</code>
     * to a <code>String</code>
     * @return the <code>String</code> representation of this <code>Float</code>.
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
     */
    public static Float valueOf(String s)
	throws NullPointerException, NumberFormatException 
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
     * 
     * @param str the <code>String</code> to convert
     * @return the value of the <code>String</code> as a <code>float</code>.
     * @since JDK 1.2
     */
    public native static float parseFloat(String s);
}
