/*************************************************************************
/* Byte.java -- object wrapper for boolean
/*
/* Copyright (c) 1998 by Free Software Foundation, Inc.
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.lang;

/**
 * Instances of class <code>Byte</code> represent primitive <code>byte</code>
 * values.
 *
 * @since JDK 1.1
 */
public final class Byte extends Number implements Comparable 
{
    static final long serialVersionUID = -7183698231559129828L;
    
    /**
     * The minimum value a <code>byte</code> can represent is -128.
     */
    public static final byte MIN_VALUE = -128;
    
    /**
     * The maximum value a <code>byte</code> can represent is 127.
     */
    public static final byte MAX_VALUE = 127;
    
    /**
     * The primitive type <code>byte</code> is represented by this 
     * <code>Class</code> object.
     */
    public static final Class TYPE = VMClassLoader.getPrimitiveClass("byte");
    
    private byte value;
    
    private static final char digits[] = 
    { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z' };
    
    /**
     * Create a <code>Byte</code> object representing the value of the 
     * <code>byte</code> argument.
     */     
    public Byte(byte value) 
    {
	this.value = value;
    }

    /**
     * Create a <code>Byte</code> object representing the value specified 
     * by the <code>String</code> argument.
     */
    public Byte(String s) throws NumberFormatException 
    {
	value = parseByte(s, 10);
    }

    /**
     * Return a hashcode representing this <code>Byte</code>.
     */    
    public int hashCode() 
    {
	return value;
    }

    /**
     * Returns <code>true</code> if <code>obj</code> is an instance of
     * <code>Byte</code> and represents the same byte value.
     */    
    public boolean equals(Object obj) 
    {
	return ((obj instanceof Byte) && (value == ((Byte)obj).byteValue()));
    }

    /**
     * Converts the <code>byte</code> to a <code>String</code> and assumes
     * a radix of 10.
     */    
    public static String toString(byte i) 
    {
	return toStringStatic(i);
    }

    /**
     * Converts the <code>Byte</code> to a <code>String</code> and assumes
     * a radix of 10.
     */
    public String toString() 
    {
	return toStringStatic(value);
    }
    
    private static String toStringStatic(byte i) 
    {
	StringBuffer tmp = new StringBuffer();
	
	boolean negative = (i < 0);
	do
	    tmp.append(digits[Math.abs(i % 10)]);
	while ((i /= 10) != 0);
	if (negative) tmp.append('-');
	return tmp.reverse().toString();
    }

    /**
     * Calls <code>Byte(String)</code> and assumes a radix of 10.
     * @see #Byte(java.lang.String)
     * @see #parseByte(java.lang.String)
     */    
    public static Byte valueOf(String s) throws NumberFormatException 
    {
	return new Byte(parseByte(s));
    }

    /**
     * Calls <code>Byte(String</code> but uses the specified radix.
     * @see #Byte(java.lang.String)
     * @see #parseByte(java.lang.String int)
     */    
    public static Byte valueOf(String s, int radix) 
	throws NumberFormatException 
    {
	return new Byte(parseByte(s, radix));
    }

    /**
     * Converts the specified <code>String</code> into a <code>byte</code>.
     * This function assumes a radix of 10.
     *
     * @param s the <code>String</code> to convert
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>byte</code>.    
    public static byte parseByte(String s) throws NumberFormatException 
    {
	return parseByte(s, 10);
    }

    /**
     * Converts the specified <code>String</code> into a <code>byte</code>.
     *
     * @param s the <code>String</code> to convert
     * @param radix the radix to use in the conversion
     * @exception NumberFormatException thrown if the <code>String</code> 
     * cannot be parsed as a <code>byte</code>.    
         
    public static byte parseByte(String s, int radix) 
	throws NumberFormatException 
    {
	return parseByte(s, radix, false);
    }

    /**
     * Convert the specified <code>String</code> into a <code>Byte</code>.
     * The <code>String</code> may represent decimal, hexadecimal, or 
     * octal numbers.
     */    
    public static Byte decode(String s) throws NumberFormatException 
    {
	return new Byte(parseByte(s, 10, true));
    }
    
    private static byte parseByte(String s, int radix, boolean decode) 
	throws NumberFormatException 
    {
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

    /**
     * Return the value of this <code>Byte</code> as a <code>byte</code>.
     */    
    public byte byteValue() 
    {
	return value;
    }
    
    /**
     * Return the value of this <code>Byte</code> as a <code>short</code>.
     */    
    public short shortValue() 
    {
	return value;
    }
    
    /**
     * Return the value of this <code>Byte</code> as a <code>int</code>.
     */    
    public int intValue() 
    {
	return value;
    }
    
    /**
     * Return the value of this <code>Byte</code> as a <code>long</code>.
     */    
    public long longValue() 
    {
	return value;
    }
    
    /**
     * Return the value of this <code>Byte</code> as a <code>float</code>.
     */    
    public float floatValue() 
    {
	return value;
    }
    
    /**
     * Return the value of this <code>Byte</code> as a <code>double</code>.
     */    
    public double doubleValue() 
    {
	return value;
    }
    
    /**
     * Compare two Bytes numerically by comparing their
     * <code>byte</code> values.
     * @return a positive value if this <code>Byte</code> is greater
     * in value than the argument <code>Byte</code>; a negative value
     * if this <code>Byte</code> is smaller in value than the argument
     * <code>Byte</code>; and <code>0</code>, zero, if this
     * <code>Byte</code> is equal in value to the argument
     * <code>Byte</code>.  
     */
    public int compareTo(Byte b)
    {
	return (int)(value - b.byteValue());
    }
    
    /**
     * Behaves like <code>compareTo(java.lang.Byte)</code> unless the Object
     * is not a <code>Byte</code>.  Then it throws a 
     * <code>ClassCastException</code>.
     * @exception ClassCastException if the argument is not a
     * <code>Byte</code>.  
     */
    public int compareTo(Object o)
    {
	return compareTo((Byte)o);
    }
}
