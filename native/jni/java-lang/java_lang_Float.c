/* Float.c - java.lang.Float native functions
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

#include "java_lang_Float.h"

/*
 * Class:     java_lang_Float
 * Method:    floatToIntBits
 * Signature: (F)I
 */
JNIEXPORT jint JNICALL Java_java_lang_Float_floatToIntBits
  (JNIEnv * env, jclass cls, jfloat value)
{
    jvalue u;
    jint e, f;
    u.f = value;
    e = u.i & 0x7f800000;
    f = u.i & 0x007fffff;

    if (e == 0x7f800000 && f != 0)
      u.i = 0x7fc00000;
    
    return u.i;
}

/*
 * Class:     java_lang_Float
 * Method:    floatToRawIntBits
 * Signature: (F)I
 */
JNIEXPORT jint JNICALL Java_java_lang_Float_floatToRawIntBits
  (JNIEnv * env, jclass cls, jfloat value)
{
  jvalue u;
  u.f = value;
  return u.i;
}

/*
 * Class:     java_lang_Float
 * Method:    intBitsToFloat
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_java_lang_Float_intBitsToFloat
  (JNIEnv * env, jclass cls, jint bits)
{
    jvalue u;
    u.i = bits;
    return u.f;
}

