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
#include <stdlib.h>  /* for strtod() */
#include <math.h>  /* for HUGE_VAL */

#include "javalang.h"
#include "java_lang_Float.h"

/*
 * Class:     java_lang_Float
 * Method:    floatToIntBits
 * Signature: (F)I
 */
JNIEXPORT jint JNICALL Java_java_lang_Float_floatToIntBits
  (JNIEnv * env, jclass thisClass, jfloat floatValue)
{
    jvalue val;
    val.f = floatValue;
    return val.i;
}

/*
 * Class:     java_lang_Float
 * Method:    intBitsToFloat
 * Signature: (I)F
 */
JNIEXPORT jfloat JNICALL Java_java_lang_Float_intBitsToFloat
  (JNIEnv * env, jclass thisClass, jint intValue)
{
    jvalue val;
    val.i = intValue;
    return val.f;
}

/*
 * Class:    java_lang_Float
 * Method:   toString(float f)
 * Signature: (F)Ljava/lang/String
 */
JNIEXPORT jstring JNICALL Java_java_lang_Float_toString
  (JNIEnv * env, jclass thisClass, jfloat f)
{
  char buf[1024];
  jstring retval;

  sprintf((char*)&buf, "%G", f);
  retval = (*env)->NewStringUTF(env, buf);
  return retval;
}

/*
 * Class:     java_lang_Float
 * Method:    parseFloat
 * Signature: (Ljava/lang/String)F
 */
JNIEXPORT jfloat JNICALL Java_java_lang_Float_parseFloat
  (JNIEnv * env, jclass thisClass, jstring s)
{
    const char *nptr;
    char *endptr, *myptr;
    jvalue val;

    if (s == NULL)
	{
	    _javalang_ThrowException(env, "java/lang/NullPointerException", "null argument");
	    return 0.0;
	}

    nptr = (char*)((*env)->GetStringUTFChars(env, s, 0));
    if (nptr == NULL)
	{
	    _javalang_ThrowException(env, "java/lang/NullPointerException", "null returned by GetStringUTFChars");
	    return 0.0;
	}
#if defined(HAVE_STRTOD)
    val.d = strtod(nptr, &endptr);


    /* to catch non-white space characters after conversion */
    myptr = endptr;
    while ((myptr) && (*myptr != 0))  /* the null character */
	{
	    switch (*myptr)
		{
		case ' ':
		case '\t':
		case '\r':
		case '\n':
		case 'f':
		case 'F':
		    myptr++;
		    break;
		default:
		    (*env)->ReleaseStringUTFChars(env, s, nptr);
		    _javalang_ThrowException(env, "java/lang/NumberFormatException", "bad number format for float");
		    return 0.0;
		    break;
		}
	}

    if ((val.d == 0) && (nptr == endptr))
	{
	    (*env)->ReleaseStringUTFChars(env, s, nptr);
	    _javalang_ThrowException(env, "java/lang/NumberFormatException", "no conversion performed, possible underflow");
	    return 0.0;
	}
    if ((val.d == -HUGE_VAL) || (val.d == HUGE_VAL))
	{
	    (*env)->ReleaseStringUTFChars(env, s, nptr);
	    _javalang_ThrowException(env, "java/lang/NumberFormatException", "conversion would cause overflow");
	    return 0.0;
	}
#else
    val.d = atof(nptr);
#endif

    (*env)->ReleaseStringUTFChars(env, s, nptr);
    return val.f;
}


