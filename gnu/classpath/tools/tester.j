/* tester
   Copyright (C) 2001 Free Software Foundation, Inc.

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
02111-1307 USA. */

import java.util.Vector;

public class tester implements Runnable, Cloneable
{
  public void run () { }

  private static final short n = 4;
  private static final int n\u0332 = 4;
  private static final long m = 31444322344523L;
  private static final float o = 1.0F;
  private static final double p = 2.032232;
  private static final String s = "TALKING \u0331 HEADS";

  public native void avoid ();
  public native boolean aboolean ();
  public native byte abyte ();
  public native char achar ();
  public native short ashort ();
  public native int aint ();
  public native long along ();
  public native float afloat ();
  public native double adouble ();

  public static native boolean[] bboolean ();
  public static native byte[] bbyte ();
  public static native char[] bchar ();
  public static native short[] bshort ();
  public static native int[] bint ();
  public static native long[] blong ();
  public static native float[] bfloat ();
  public static native double[] bdouble ();

  public native NullPointerException cthrowable ();
  public native String cstring ();
  public native Class cclass ();
  public native Vector cobject ();

  public native int d\u0332 (boolean b1, byte b2, char b3, short b4, int b5,
                             long b6, float b7, double b8, boolean[] c1, 
                             byte[] c2, char[] c3, short[] c4, int[] c5,
                             long[] c6\u0031, float[]c7, double[]c8, 
                             ClassNotFoundException d1, String d2, Class d3,
                             Vector d4);

  public native Vector[][] e (String b);
  public native Runnable f ();

  private class bob 
  {
    public void foo () { System.out.println ("foo"); }
    public native void x ();
   
    private class sue
    {
      public void bar () { System.out.println ("bar"); }
    }
  }
}
