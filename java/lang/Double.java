package java.lang;

public final class Double extends Number 
{
    public static final double MIN_VALUE = 5e-324;
    public static final double MAX_VALUE = 1.7976931348623157e+308;
    public static final double NEGATIVE_INFINITY = -1.0/0.0;
    public static final double POSITIVE_INFINITY = 1.0/0.0;
    public static final double NaN = 0.0/0.0;
    
    public static final int WIDEFP_SIGNIFICAND_BITS = 53;
    public static final int WIDEFP_MIN_EXPONENT = -1022;
    public static final int WIDEFP_MAX_EXPONENT = 1023;
    
    public static final Class TYPE = VMClassLoader.getPrimitiveClass("double");
    
    private double value;
    
    static 
    {
	System.loadLibrary("javalang");
    }
 
   public Double(double value) {
	this.value = value;
    }
    
    public Double(String s) throws NullPointerException, 
    NumberFormatException 
    {
	value = parseDouble(s);
    }
    
    public String toString() {
	return toString(value);
    }
    
    public boolean equals(Object obj) {
	return (obj instanceof Double && ((Double)obj).value == value);
    }
    
    public int hashCode() {
	long v = doubleToLongBits(value);
	return (int)(v^(v>>>32));
    }
    
    public int intValue() {
	return (int)value;
    }
    
    public long longValue() {
	return (long)value;
    }
    
    public float floatValue() {
	return (float)value;
    }
    
    public double doubleValue() {
	return value;
    }
    
    public native static String toString(double d);
    
    public static Double valueOf(String s)
	throws NullPointerException, NumberFormatException {
	return new Double(s);
    }
    
    public boolean isNaN() {
	return isNaN(value);
    }
    
    public static boolean isNaN(double d) {
	return (doubleToLongBits(d) == 0x7ff8000000000000L);
    }
    
    public boolean isInifinite() {
	return isInfinite(value);
    }
    
    public boolean isInfinite(double d) 
    {
	return (d == POSITIVE_INFINITY || d == NEGATIVE_INFINITY);
    }
    
    public native static long doubleToLongBits(double value);
    public native static double longBitsToDouble(long bits);
    public native static double parseDouble(String str);
}

  
