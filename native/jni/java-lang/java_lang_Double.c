/* Double.c - java.lang.Double native functions
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
#include <stdlib.h>
#include <string.h>

#include "mprec.h"
#include "fdlibm.h"
#include "jcl.h"

#include "java_lang_Double.h"


/*
 * Class:     java_lang_Double
 * Method:    initIDs
 * Signature: ()
 */
jmethodID isNaNID;
jdouble NEGATIVE_INFINITY;
jdouble POSITIVE_INFINITY;

JNIEXPORT void JNICALL Java_java_lang_Double_initIDs
  (JNIEnv *env, jclass cls)
{
  jfieldID negInfID;
  jfieldID posInfID;

  isNaNID = (*env)->GetStaticMethodID(env, cls, "isNaN", "(D)Z");
  if (isNaNID == NULL)
    {
      DBG("unable to determine method id of isNaN\n")
      return;
    }
  negInfID = (*env)->GetStaticFieldID(env, cls, "NEGATIVE_INFINITY", "D");
  if (negInfID == NULL)
    {
      DBG("unable to determine field id of NEGATIVE_INFINITY\n")
      return;
    }
  posInfID = (*env)->GetStaticFieldID(env, cls, "POSITIVE_INFINITY", "D");
  if (posInfID == NULL)
    {
      DBG("unable to determine field id of POSITIVE_INFINITY\n")
      return;
    }
  POSITIVE_INFINITY = (*env)->GetStaticDoubleField(env, cls, posInfID);
  NEGATIVE_INFINITY = (*env)->GetStaticDoubleField(env, cls, negInfID);
} 

/*
 * Class:     java_lang_Double
 * Method:    doubleToLongBits
 * Signature: (D)J
 */
JNIEXPORT jlong JNICALL Java_java_lang_Double_doubleToLongBits
  (JNIEnv * env, jclass cls, jdouble doubleValue)
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
 * Class:     java_lang_Double
 * Method:    doubleToRawLongBits
 * Signature: (D)J
 */
JNIEXPORT jlong JNICALL Java_java_lang_Double_doubleToRawLongBits
  (JNIEnv * env, jclass cls, jdouble doubleValue)
{
  jvalue val;
  val.d = doubleValue;
  return val.j;
}

/*
 * Class:     java_lang_Double
 * Method:    longBitsToDouble
 * Signature: (J)D
 */
JNIEXPORT jdouble JNICALL Java_java_lang_Double_longBitsToDouble
  (JNIEnv * env, jclass cls, jlong longValue)
{
  jvalue val;
  val.j = longValue;
  return val.d;
}

/*
 * Class:    java_lang_Double
 * Method:   toString(double d)
 * Signature: (D)Ljava/lang/String
 */
JNIEXPORT jstring JNICALL Java_java_lang_Double_toString
  (JNIEnv * env, jclass cls, jdouble value, jboolean isFloat)
{
  char buffer[50], result[50];
  int decpt, sign;
  char *s, *d;
  int i;

  if ((*env)->CallStaticBooleanMethod(env, cls, isNaNID, value))
    return (*env)->NewStringUTF(env, "NaN");
  
  if ( ((jlong) value) == ((jlong)POSITIVE_INFINITY))
    return (*env)->NewStringUTF(env, "Infinity");

  if (value == NEGATIVE_INFINITY)
    return (*env)->NewStringUTF(env, "-Infinity");

  _dtoa (value, 0, 20, &decpt, &sign, NULL, buffer, (int)isFloat);

  value = fabs (value);

  s = buffer;
  d = result;

  if (sign)
    *d++ = '-';

  if (value >= 1e-3 && value < 1e7 || value == 0)
    {
      if (decpt <= 0)
	*d++ = '0';
      else
	{
	  for (i = 0; i < decpt; i++)
	    if (*s)
	      *d++ = *s++;
	    else
	      *d++ = '0';
	}

      *d++ = '.';

      if (*s == 0)
	{
	  *d++ = '0';
	  decpt++;
	}
	  
      while (decpt++ < 0)
	*d++ = '0';      
      
      while (*s)
	*d++ = *s++;

      *d = 0;

      return (*env)->NewStringUTF(env, result);
    }

  *d++ = *s++;
  decpt--;
  *d++ = '.';
  
  if (*s == 0)
    *d++ = '0';

  while (*s)
    *d++ = *s++;

  *d++ = 'E';
  
  if (decpt < 0)
    {
      *d++ = '-';
      decpt = -decpt;
    }

  {
    char exp[4];
    char *e = exp + sizeof exp;
    
    *--e = 0;
    do
      {
	*--e = '0' + decpt % 10;
	decpt /= 10;
      }
    while (decpt > 0);

    while (*e)
      *d++ = *e++;
  }
  
  *d = 0;

  return (*env)->NewStringUTF(env, result);
}
  
/*
 * Class:     java_lang_Double
 * Method:    parseDouble
 * Signature: (Ljava/lang/String)D
 */
JNIEXPORT jdouble JNICALL Java_java_lang_Double_parseDouble0
  (JNIEnv * env, jclass cls, jstring str)
{
  jboolean isCopy;
  int length;
  char *buf, *endptr;
  jdouble val;

  buf = (*env)->GetStringUTFChars(env, str, &isCopy);
  if (buf == NULL)
    {
      return 0.0; /* OutOfMemoryError already thrown */
    }

  if (strlen(buf) > 0)
    {
      struct _Jv_reent reent;  
      memset (&reent, 0, sizeof reent);

#ifdef KISSME_LINUX_USER
      val = strtod ( buf, &endptr);
   #else
      val = _strtod_r (&reent, buf, &endptr);
   #endif
      if (endptr == buf + strlen(buf))
	return val;
    }
  JCL_ThrowException(env, "java/lang/NumberFormatException", "unable to parse double");
  return 0.0; /* NumberFormatException already thrown */
}
