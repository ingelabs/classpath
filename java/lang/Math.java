/* java.lang.Math
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

import java.util.Random;
import gnu.classpath.Configuration;

/**
 * Helper class containing useful mathematical functions and constants.
 * <P>
 *
 * Note that angles are specified in radians.  Conversion functions are
 * provided for your convenience.
 *
 * @author Paul Fisher
 * @author John Keiser
 * @author Eric Blake <ebb9@email.byu.edu>
 * @since 1.0
 */
public final class Math
{
  /**
   * Math is non-instantiable
   */
  private Math ()
  {
  }

  static
  {
    if (Configuration.INIT_LOAD_LIBRARY)
      {
	System.loadLibrary ("javalangmath");
      }
  }

  static Random rand;

  /**
   * The mathematical constant <em>e</em>.
   * Used in natural log and exp.
   * @see #log(double)
   * @see #exp(double)
   */
  public static final double E = 2.7182818284590452354;

  /**
   * The mathematical constant <em>pi</em>.
   * This is the ratio of a circle's diameter to its circumference.
   */
  public static final double PI = 3.14159265358979323846;

  /**
   * Take the absolute value of the argument.
   * (Absolute value means make it positive.)
   * <P>
   *
   * Note that the the largest negative value (Integer.MIN_VALUE) cannot
   * be made positive.  In this case, because of the rules of negation in
   * a computer, MIN_VALUE is what will be returned.
   * This is a <em>negative</em> value.  You have been warned.
   *
   * @param a the number to take the absolute value of.
   * @return the absolute value.
   * @see java.lang.Integer#MIN_VALUE
   */
  public static int abs (int a)
  {
    return (a < 0) ? -a : a;
  }

  /**
   * Take the absolute value of the argument.
   * (Absolute value means make it positive.)
   * <P>
   *
   * Note that the the largest negative value (Long.MIN_VALUE) cannot
   * be made positive.  In this case, because of the rules of negation in
   * a computer, MIN_VALUE is what will be returned.
   * This is a <em>negative</em> value.  You have been warned.
   *
   * @param a the number to take the absolute value of.
   * @return the absolute value.
   * @see java.lang.Long#MIN_VALUE
   */
  public static long abs (long a)
  {
    return (a < 0) ? -a : a;
  }

  /**
   * Take the absolute value of the argument.
   * (Absolute value means make it positive.)
   * @param a the number to take the absolute value of.
   * @return the absolute value.
   */
  public static float abs (float a)
  {
    // avoid method call overhead, but treat -0.0 correctly
    // return Float.intBitsToFloat(0x7fffffff & Float.floatToIntBits(a));
    return (a <= 0) ? 0 - a : a;
  }

  /**
   * Take the absolute value of the argument.
   * (Absolute value means make it positive.)
   * @param a the number to take the absolute value of.
   * @return the absolute value.
   */
  public static double abs (double a)
  {
    // avoid method call overhead, but treat -0.0 correctly
    // return Double.longBitsToDouble((Double.doubleToLongBits(a)<<1)>>>1);
    return (a <= 0) ? 0 - a : a;
  }

  /**
   * Return whichever argument is smaller.
   * @param a the first number
   * @param b a second number
   * @return the smaller of the two numbers.
   */
  public static int min (int a, int b)
  {
    return (a < b) ? a : b;
  }

  /**
   * Return whichever argument is smaller.
   * @param a the first number
   * @param b a second number
   * @return the smaller of the two numbers.
   */
  public static long min (long a, long b)
  {
    return (a < b) ? a : b;
  }

  /**
   * Return whichever argument is smaller.
   * Return whichever argument is smaller. If either argument is NaN, the
   * result is NaN, and when comparing 0 and -0, -0 is always smaller.
   *
   * @param a the first number
   * @param b a second number
   * @return the smaller of the two numbers.
   */
  public static float min (float a, float b)
  {
    // this check for NaN, from JLS 15.21.1, saves a method call
    if (a != a)
      return a;
    // no need to check if b is NaN; < will work correctly
    // recall that -0.0 == 0.0, but [+-]0.0 - [+-]0.0 behaves special
    if (a == 0 && b == 0)
      return -(-a - b);
    return (a < b) ? a : b;
  }

  /**
   * Return whichever argument is smaller. If either argument is NaN, the
   * result is NaN, and when comparing 0 and -0, -0 is always smaller.
   *
   * @param a the first number
   * @param b a second number
   * @return the smaller of the two numbers.
   */
  public static double min (double a, double b)
  {
    // this check for NaN, from JLS 15.21.1, saves a method call
    if (a != a)
      return a;
    // no need to check if b is NaN; < will work correctly
    // recall that -0.0 == 0.0, but [+-]0.0 - [+-]0.0 behaves special
    if (a == 0 && b == 0)
      return -(-a - b);
    return (a < b) ? a : b;
  }

  /**
   * Return whichever argument is larger.
   * @param a the first number
   * @param b a second number
   * @return the larger of the two numbers.
   */
  public static int max (int a, int b)
  {
    return (a > b) ? a : b;
  }

  /**
   * Return whichever argument is larger.
   * @param a the first number
   * @param b a second number
   * @return the larger of the two numbers.
   */
  public static long max (long a, long b)
  {
    return (a > b) ? a : b;
  }

  /**
   * Return whichever argument is larger. If either argument is NaN, the
   * result is NaN, and when comparing 0 and -0, 0 is always larger.
   *
   * @param a the first number
   * @param b a second number
   * @return the larger of the two numbers.
   */
  public static float max (float a, float b)
  {
    // this check for NaN, from JLS 15.21.1, saves a method call
    if (a != a)
      return a;
    // no need to check if b is NaN; > will work correctly
    // recall that -0.0 == 0.0, but [+-]0.0 - [+-]0.0 behaves special
    if (a == 0 && b == 0)
      return a - -b;
    return (a > b) ? a : b;
  }

  /**
   * Return whichever argument is larger. If either argument is NaN, the
   * result is NaN, and when comparing 0 and -0, 0 is always larger.
   *
   * @param a the first number
   * @param b a second number
   * @return the larger of the two numbers.
   */
  public static double max (double a, double b)
  {
    // this check for NaN, from JLS 15.21.1, saves a method call
    if (a != a)
      return a;
    // no need to check if b is NaN; > will work correctly
    // recall that -0.0 == 0.0, but [+-]0.0 - [+-]0.0 behaves special
    if (a == 0 && b == 0)
      return a - -b;
    return (a > b) ? a : b;
  }

  /**
   * The trigonometric function <em>sin</em>.
   * @param a the angle (in radians).
   * @return sin(a).
   */
  public native static double sin (double a);

  /**
   * The trigonometric function <em>cos</em>.
   * @param a the angle (in radians).
   * @return cos(a).
   */
  public native static double cos (double a);

  /**
   * The trigonometric function <em>tan</em>.
   * @param a the angle (in radians).
   * @return tan(a).
   */
  public native static double tan (double a);

  /**
   * The trigonometric function <em>arcsin</em>.
   * The range of angles you will get are from -pi/2 to pi/2 radians (-90 to 90 degrees)
   * @param a the sin to turn back into an angle.
   * @return arcsin(a).
   */
  public native static double asin (double a);

  /**
   * The trigonometric function <em>arccos</em>.
   * The range of angles you will get are from 0 to pi radians (0 to 180 degrees).
   * @param a the cos to turn back into an angle.
   * @return arccos(a).
   */
  public native static double acos (double a);

  /**
   * The trigonometric function <em>arctan</em>.
   * The range of angles you will get are from -pi/2 to pi/2 radians (-90 to 90 degrees)
   * @param a the sin to turn back into an angle.
   * @return arcsin(a).
   * @see #atan(double,double)
   */
  public native static double atan (double a);

  /**
   * A special version of the trigonometric function <em>arctan</em>.
   * Given a position (x,y), this function will give you the angle of
   * that position.
   * The range of angles you will get are from -pi to pi radians (-180 to 180 degrees),
   * the whole spectrum of angles.  That is what makes this function so
   * much more useful than the other <code>atan()</code>.
   * @param y the y position
   * @param x the x position
   * @return arcsin(a).
   * @see #atan(double)
   */
  public native static double atan2 (double y, double x);

  /**
   * Take <em>e</em><sup>a</sup>.  The opposite of <code>log()</code>.
   * @param a the number to raise to the power.
   * @return the number raised to the power of <em>e</em>.
   * @see #log(double)
   * @see #pow(double,double)
   */
  public native static double exp (double a);

  /**
   * Take ln(a) (the natural log).  The opposite of <code>exp()</code>.
   * Note that the way to get log<sub>b</sub>(a) is to do this:
   * <code>ln(a) / ln(b)</code>.
   * @param a the number to take the natural log of.
   * @return the natural log of <code>a</code>.
   * @see #exp(double)
   */
  public native static double log (double a);

  /**
   * Take a square root.
   * For other roots, to pow(a,1/rootNumber).
   * @param a the numeric argument
   * @return the square root of the argument.
   * @see #pow(double,double)
   */
  public native static double sqrt (double a);

  /**
   * Take a number to a power.
   * @param a the number to raise.
   * @param b the power to raise it to.
   * @return a<sup>b</sup>.
   */
  public native static double pow (double a, double b);

  /**
   * Get the floating point remainder on two numbers,
   * which really does the following:
   * <P>
   *
   * <OL>
   *   <LI>
   *       Takes x/y and finds the nearest integer <em>n</em> to the
   *       quotient.  (Uses the <code>rint()</code> function to do this.
   *   </LI>
   *   <LI>
   *       Takes x - y*<em>n</em>.
   *   </LI>
   *   <LI>
   *       If x = y*n, then the result is 0 if x is positive and -0 if x
   *       is negative.
   *   </LI>
   * </OL>
   *
   * @param x the dividend (the top half)
   * @param y the divisor (the bottom half)
   * @return the IEEE 754-defined floating point remainder of x/y.
   * @see #rint(double)
   */
  public native static double IEEEremainder (double x, double y);

  /**
   * Take the nearest integer that is that is greater than or equal to the
   * argument.
   * @param a the value to act upon.
   * @return the nearest integer >= <code>a</code>.
   */
  public native static double ceil (double a);

  /**
   * Take the nearest integer that is that is less than or equal to the
   * argument.
   * @param a the value to act upon.
   * @return the nearest integer <= <code>a</code>.
   */
  public native static double floor (double a);

  /**
   * Take the nearest integer to the argument.  If it is exactly between
   * two integers, the even integer is taken.
   * @param a the value to act upon.
   * @return the nearest integer to <code>a</code>.
   */
  public native static double rint (double a);

  /**
   * Take the nearest integer to the argument.  If it is exactly between
   * two integers, then the lower of the two (-10 lower than -9) is taken.
   * If the argument is less than Integer.MIN_VALUE or negative infinity,
   * Integer.MIN_VALUE will be returned.  If the argument is greater than
   * Integer.MAX_VALUE, Integer.MAX_VALUE will be returned.
   *
   * @param a the argument to round.
   * @return the nearest integer to the argument.
   * @see java.lang.Integer#MIN_VALUE
   * @see java.lang.Integer#MAX_VALUE
   */
  public static int round (float a)
  {
    return (int) floor (a + 0.5f);
  }

  /**
   * Take the nearest integer to the argument.  If it is exactly between
   * two integers, then the lower of the two (-10 lower than -9) is taken.
   * If the argument is less than Long.MIN_VALUE or negative infinity,
   * Long.MIN_VALUE will be returned.  If the argument is greater than
   * Long.MAX_VALUE, Long.MAX_VALUE will be returned.
   *
   * @param a the argument to round.
   * @return the nearest integer to the argument.
   * @see java.lang.Long#MIN_VALUE
   * @see java.lang.Long#MAX_VALUE
   */
  public static long round (double a)
  {
    return (long) floor (a + 0.5d);
  }

  /**
   * Get a random number.  This behaves like Random.nextDouble().
   * @return a random number.
   * @see java.lang.Random#nextDouble()
   */
  public static synchronized double random ()
  {
    if (rand == null)
      rand = new Random ();
    return rand.nextDouble ();
  }

  /**
   * Convert from degrees to radians.
   * The formula for this is radians = degrees * (pi/180).
   * @param degrees an angle in degrees
   * @return the angle in radians
   */
  public static double toRadians (double degrees)
  {
    return degrees * 0.017453292519943295;	/* (degrees * (PI/180)) */
  }

  /**
   * Convert from radians to degrees.
   * The formula for this is degrees = radians * (180/pi).
   * @param rads an angle in radians
   * @return the angle in degrees
   */
  public static double toDegrees (double rads)
  {
    return rads / 0.017453292519943295;	/* (rads / (PI/180)) */
  }
}
