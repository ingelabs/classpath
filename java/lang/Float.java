package java.lang;

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
    
    public static final Class TYPE = VMClassLoader.getPrimitiveClass("float");
    
    private float value;

    static
    {
	System.loadLibrary("javalang");
    }
    
    public Float(float value) 
    {
	this.value = value;
    }
    
    public Float(double value) 
    {
	this((float)value);
    }
    
    public Float(String s) throws NullPointerException, 
    NumberFormatException 
    {
	value = parseFloat(s);
    }
    
    public String toString() {
	return toString(value);
    }
    
    public boolean equals(Object obj) 
    {
	return (obj instanceof Float && ((Float)obj).value == value);
    }

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
