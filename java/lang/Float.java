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
 * @since JDK 1.0
 */
public final class Float extends Number 
{
    public static final float MIN_VALUE = 1.4e-45f;
    public static final float MAX_VALUE = 3.4028235e+38f;
    public static final float NEGATIVE_INFINITY = -1.0f/0.0f;
    public static final float POSITIVE_INFINITY = 1.0f/0.0f;
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
     */
    public String toString() {
	return toString(value);
    }
    
    /**
     * If the <code>Object</code> is not <code>null</code>, is an
     * <code>instanceof</code> <code>Float</code>, and represents
     * the same primitive <code>float</code> value return 
     * <code>true</code>.  Otherwise <code>false</code> is returned.
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

    public int intValue() 
    {
	return (int)value;
    }
    
    public long longValue() 
    {
	return (long)value;
    }

    public float floatValue() 
    {
	return value;
    }
    
    public double doubleValue() 
    {
	return value;
    }
    
    public native static String toString(float f);

    public static Float valueOf(String s)
	throws NullPointerException, NumberFormatException 
    {
	if (s == null)
	    throw new NullPointerException("Float.valueOf(String) passed null as argument");
	
	return new Float(Float.parseFloat(s));
    }
    
    public boolean isNaN() 
    {
	return isNaN(value);
    }

    public static boolean isNaN(float v) 
    {
	return (floatToIntBits(v) == 0x7fc00000);
    }

    public boolean isInfinite() 
    {
	return isInfinite(value);
    }
    
    public boolean isInfinite(float v) 
    {
	return (v == POSITIVE_INFINITY || v == NEGATIVE_INFINITY);
    }

    public native static int floatToIntBits(float value);
    public native static float intBitsToFloat(int bits);
    public native static float parseFloat(String s);
}
