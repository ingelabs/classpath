/* Math.c - java.lang.Math native functions
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

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


#include <config.h>
#include <java_lang_Math.h>
#include <fdlibm.h>

JNIEXPORT jdouble JNICALL Java_java_lang_Math_sin (JNIEnv *env, jclass cls, jdouble x)
{
  return sin (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_cos (JNIEnv *env, jclass cls, jdouble x)
{
  return cos (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_tan (JNIEnv *env, jclass cls, jdouble x)
{
  return tan (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_asin (JNIEnv *env, jclass cls, jdouble x)
{
  return asin (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_acos (JNIEnv *env, jclass cls, jdouble x)
{
  return acos (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_atan (JNIEnv *env, jclass cls, jdouble x)
{
  return atan (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_atan2 (JNIEnv *env, jclass cls, jdouble y, jdouble x)
{
  return atan2 (y, x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_exp (JNIEnv *env, jclass cls, jdouble x)
{
  return exp (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_log (JNIEnv *env, jclass cls, jdouble x)
{
  return log (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_sqrt (JNIEnv *env, jclass cls, jdouble x)
{
  return sqrt (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_pow (JNIEnv *env, jclass cls, jdouble x, jdouble y)
{
  return pow (x, y);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_IEEEremainder (JNIEnv *env, jclass cls, jdouble x, jdouble y)
{
  return remainder (x, y);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_ceil (JNIEnv *env, jclass cls, jdouble x)
{
  return ceil (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_floor (JNIEnv *env, jclass cls, jdouble x)
{
  return floor (x);
}

JNIEXPORT jdouble JNICALL Java_java_lang_Math_rint (JNIEnv *env, jclass cls, jdouble x)
{
  return rint (x);
}

