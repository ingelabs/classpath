package java.lang;

public final class Math {
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
}
