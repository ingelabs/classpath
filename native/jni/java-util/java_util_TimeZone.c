/* TimeZone.c - Native methods for java.util.TimeZone
   Copyright (C) 1999 Free Software Foundation, Inc.

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

/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdio.h>
#include <stdlib.h>

#include <jni.h>

#include "target_native.h"
#include "target_native_misc.h"

#include "java_util_TimeZone.h"

/*
 * This method returns a time zone string that is used by the static
 * initializer in java.util.TimeZone to create the default timezone
 * instance.  This is a key into the timezone table used by
 * that class.
 *
 * Class:     java_util_TimeZone
 * Method:    getDefaultTimeZoneId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_java_util_TimeZone_getDefaultTimeZoneId(JNIEnv *env, jclass clazz)
{
#ifdef HAVE_TZNAME
  char    buffer[128];
  int     result;
  jstring retval;

  TARGET_NATIVE_UTIL_GET_TIMEZONE_STRING(buffer,sizeof(buffer),result);

  retval = (*env)->NewStringUTF(env, buffer);
 
  return(retval);
#else
  return(0); /* added this statement (crashes without..:-) --Fridi. */
#endif /* HAVE_TZNAME */
}

