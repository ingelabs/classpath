package java.lang;

import java.util.Random;

public final class Math {

  static {
    System.loadLibrary("java_lang_Math");
  }

  static Random rand;

  public static final double E = 2.7182818284590452354; 
  public static final double PI = 3.14159265358979323846;

  public static int abs(int a) {
    return (a < 0) ? -a : a;
  }

  public static long abs(long a) {
    return (a < 0) ? -a : a;
  }

  public static float abs(float a) {
    return Float.intBitsToFloat(0x7fffffff & Float.floatToIntBits(a));
  }

  public static double abs(double a) {
    return Double.longBitsToDouble((Double.doubleToLongBits(a) << 1) >>> 1);
  }

  public static int min(int a, int b) {
    return (a < b) ? a : b;
  }

  public static long min(long a, long b) {
    return (a < b) ? a : b;
  }

  public static float min(float a, float b) {
    if (a == 0.0f && b == 0.0f)	// return -0.0f, if a or b is -0.0f
      return ((Float.floatToIntBits(a) >> 31) == 1) ? a : b;
    return (a < b) ? a : b;
  }

  public static double min(double a, double b) {
    if (a == 0.0d && b == 0.0d)	// return -0.0d, if a or b is -0.0d
      return ((Double.doubleToLongBits(a) >> 63) == 1) ? a : b;
    return (a < b) ? a : b;
  }

  public static int max(int a, int b) {
    return (a > b) ? a : b;
  }

  public static long max(long a, long b) {
    return (a > b) ? a : b;
  }

  public static float max(float a, float b) {
    if (a == 0.0f && b == 0.0f)	// return +0.0f, if a or b is +0.0f
      return ((Float.floatToIntBits(a) >> 31) == 0) ? a : b;
    return (a > b) ? a : b;
  }

  public static double max(double a, double b) {
    if (a == 0.0d && b == 0.0d)	// return +0.0d, if a or b is +0.0d
      return ((Double.doubleToLongBits(a) >> 63) == 0) ? a : b;
    return (a > b) ? a : b;
  }

  public native static double sin(double a);
  public native static double cos(double a);
  public native static double tan(double a);
  public native static double asin(double a);
  public native static double acos(double a);
  public native static double atan(double a);
  public native static double atan2(double y, double x);
  public native static double exp(double a);
  public native static double log(double a);
  public native static double sqrt(double a);
  public native static double pow(double a, double b);
  public native static double IEEEremainder(double x, double y);
  public native static double ceil(double a);
  public native static double floor(double a);
  public native static double rint(double a);

  public static int round(float a) {
    return (int)floor(a + 0.5f);
  }

  public static int round(double a) {
    return (int)floor(a + 0.5d);
  }

  public static synchronized double random() {
    if (rand == null)
      rand = new Random();
    return rand.nextDouble();
  }

  public static double toRadians(double degrees) {
    return degrees * 0.017453292519943295; /* (degrees * (PI/180)) */
  }

  public static double toDegrees(double rads) {
    return rads / 0.017453292519943295; /* (rads / (PI/180)) */
  }
}
