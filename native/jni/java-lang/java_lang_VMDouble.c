/* VMDouble.c - java.lang.VMDouble native functions
   Copyright (C) 1998, 1999, 2001, 2003, 2004 Free Software Foundation, Inc.

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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


#include <config.h>

#include "java_lang_Double.h"
#include "java_lang_VMDouble.h"

/*
 * Class:     java_lang_VMDouble
 * Method:    doubleToLongBits
 * Signature: (D)J
 */
JNIEXPORT jlong JNICALL
Java_java_lang_VMDouble_doubleToLongBits
  (JNIEnv *env __attribute__((__unused__)),
   jclass cls __attribute__((__unused__)),
   jdouble doubleValue)
{
  jvalue val;
  jlong e, f;
  val.d = doubleValue;
  
  e = val.j & 0x7ff0000000000000LL;
  f = val.j & 0x000fffffffffffffLL;
  
  if (e == 0x7ff0000000000000LL && f != 0L)
    val.j = 0x7ff8000000000000LL;

  return val.j;
}

/*
 * Class:     java_lang_VMDouble
 * Method:    doubleToRawLongBits
 * Signature: (D)J
 */
JNIEXPORT jlong JNICALL
Java_java_lang_VMDouble_doubleToRawLongBits
  (JNIEnv *env __attribute__((__unused__)),
   jclass cls __attribute__((__unused__)),
   jdouble doubleValue)
{
  jvalue val;
  val.d = doubleValue;
  return val.j;
}

/*
 * Class:     java_lang_VMDouble
 * Method:    longBitsToDouble
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL
Java_java_lang_VMDouble_longBitsToDouble
  (JNIEnv *env __attribute__((__unused__)),
   jclass cls __attribute__((__unused__)),
   jlong longValue)
{
  jvalue val;
  val.j = longValue;
  return val.d;
}

