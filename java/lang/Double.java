/*************************************************************************
 * Double.java -- object wrapper for double primitive
 *
 * Copyright (c) 1998 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/

package java.lang;


/**
 * Instances of class <code>Double</code> represent primitive
 * <code>double</code> values.
 *
 * Additionally, this class provides various helper functions and variables
 * related to doubles.
 *
 * @author Paul Fisher
 * @author John Keiser
 * @since JDK 1.0
 */
public final class Double extends Number implements Comparable
{
    /**
     * The minimum positive value a <code>double</code> may represent
     * is 5e-324.
     */
    public static final double MIN_VALUE = 5e-324;
    
    /**
     * The maximum positive value a <code>double</code> may represent
     * is 1.7976931348623157e+308.
     */
    public static final double MAX_VALUE = 1.7976931348623157e+308;
    
    /**
     * The value of a double representation -1.0/0.0, negative infinity.
     */
    public static final double NEGATIVE_INFINITY = -1.0/0.0;
    
    /**
     * The value of a double representing 1.0/0.0, positive infinity.
     */
    public static final double POSITIVE_INFINITY = 1.0/0.0;
    
    /**
     * All IEEE 754 values of NaN have the same value in Java.
     */
    public static final double NaN = 0.0/0.0;
    
    /**
     * The primitive type <code>double</code> is represented by this
     * <code>Class</code> object.
     */
    public static final Class TYPE = VMClassLoader.getPrimitiveClass("double");
    
    private double value;
    
    /**
     * Load native routines necessary for this class.
     */    
    static 
    {
	System.loadLibrary("javalang");
    }

    /**
     * Create a <code>Double</code> from the primitive <code>double</code>
     * specified.
     *
     * @param value the <code>double</code> argument
     */ 
    public Double(double value) {
	this.value = value;
    }
    
    /**
     * Create a <code>Double</code> from the specified <code>String</code>.
     * @param s the <code>String</code> to convert
     */
    public Double(String s) throws NullPointerException, 
    NumberFormatException 
    {
	value = parseDouble(s);
    }

    /**
     * Convert the <code>double</code> value of this <code>Double</code>
     * to a <code>String</code>
     */
    public String toString() 
    {
	return toString(value);
    }
    
    /**
     * If the <code>Object</code> is not <code>null</code>, is an
     * <code>instanceof</code> <code>Double</code>, and represents
     * the same primitive <code>double</code> value return 
     * <code>true</code>.  Otherwise <code>false</code> is returned.
     */
    public boolean equals(Object obj) 
    {
	return (obj instanceof Double && ((Double)obj).value == value);
    }

    /**
     * The hashcode is the value of the expression: <br>
     * <br>
     * <code>(int)(v^(v>>>32))</code><br>
     * <br>
     * where v is defined by: <br>
     * <code>long v = Double.doubleToLongBits(this.longValue());</code><br>
     */
    public int hashCode() 
    {
	long v = doubleToLongBits(value);
	return (int)(v^(v>>>32));
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
     * Return the result of calling <code>Double(java.lang.String)</code>.
     *
     * @exception NullPointerException thrown if <code>String</code> is 
     * <code>null</code>
     * @exception NumberFormatException thrown if <code>String</code> cannot
     * be parsed as a <code>double</code>.
     */
    public static Double valueOf(String s)
	throws NullPointerException, NumberFormatException 
    {
	return new Double(s);
    }

    /**
     * Return <code>true</code> if the value of this <code>Double</code>
     * is the same as <code>NaN</code>, otherwise return <code>false</code>.
     */    
    public boolean isNaN() 
    {
	return isNaN(value);
    }
    
    /**
     * Return <code>true</code> if the <code>double</code> has the same
     * value as <code>NaN</code>, otherwise return <code>false</code>.
     *
     * @param d the <code>double</code> to compare
     */
    public static boolean isNaN(double d) 
    {
	return (doubleToLongBits(d) == 0x7ff8000000000000L);
    }

    /**
     * Return <code>true</code> if the value of this <code>Double</code>
     * is the same as <code>NEGATIVE_INFINITY</code> or 
     * <code>POSITIVE_INFINITY</code>, otherwise return <code>false</code>.
     */    
    public boolean isInifinite() 
    {
	return isInfinite(value);
    }
    
    /**
     * Return <code>true</code> if the <code>double</code> has a value 
     * equal to either <code>NEGATIVE_INFINITY</code> or 
     * <code>POSITIVE_INFINITY</code>, otherwise return <code>false</code>.
     *
     * @param d the <code>double</code> to compare
     */    
    public boolean isInfinite(double d) 
    {
	return (d == POSITIVE_INFINITY || d == NEGATIVE_INFINITY);
    }

    /**
     * Returns 0 if the <code>double</code> value of the argument is 
     * equal to the value of this <code>Double</code>.  Returns a number
     * less than zero if the value of this <code>Double</code> is less 
     * than the <code>double</code> value of the argument, and returns a 
     * number greater than zero if the value of this <code>Double</code> 
     * is greater than the <code>double</code> value of the argument.
     * <br>
     * <code>Double.NaN</code> is greater than any number other than itself, 
     * even <code>Double.POSITIVE_INFINITY</code>.
     * <br>
     * <code>0.0d</code> is greater than <code>-0.0d</code>.
     */
    public int compareTo(Double d)
    {
	double x = d.doubleValue();

	if (value == NaN)
	    return (x == NaN) ? 0 : 1;
	if ((value == 0.0d) && (x == -0.0d))
	    return 1;
	if ((value == -0.0d) && (x == 0.0d))
	    return -1;

	return ((value - x) > 0) ? 1 : -1;
    }
    
    /**
     * Compares the specified <code>Object</code> to this <code>Double</code>
     * if and only if the <code>Object</code> is an instanceof 
     * <code>Double</code>.
     *
     * @throws ClassCastException if the argument is not a <code>Double</code>
     */
    public int compareTo(Object o)
    {
	return compareTo((Double)o);
    }

    /**
     * Convert the <code>double</code> to a <code>String</code>.
     */
    public native static String toString(double d);

    /**
     * Return the long bits of the specified <code>double</code>.
     * The result of this function can be used as the argument to
     * <code>Double.longBitsToDouble(long)</code> to obtain the
     * original <code>double</code> value.
     *
     * @param value the <code>double</code> to convert
     */    
    public native static long doubleToLongBits(double value);

    /**
     * Return the <code>double</code> represented by the long
     * bits specified.
     *
     * @param bits the long bits representing a <code>double</code>
     */
    public native static double longBitsToDouble(long bits);

    /**
     * Parse the specified <code>String</code> as a <code>double</code>.
     * 
     * @param str the <code>String</code> to convert
     * @since JDK 1.2
     */
    public native static double parseDouble(String str) 
    throws NumberFormatException;
}
