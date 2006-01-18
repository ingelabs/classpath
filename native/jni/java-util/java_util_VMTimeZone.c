/* VMTimeZone.c - Native method for java.util.VMTimeZone
   Copyright (C) 1999, 2004 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

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

#include "config.h"

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include <jcl.h>
#include <jni.h>

#include "target_native.h"
#include "target_native_misc.h"

#include "java_util_VMTimeZone.h"

/**
 * This method returns a time zone id string which is in the form
 * (standard zone name) or (standard zone name)(GMT offset) or
 * (standard zone name)(GMT offset)(daylight time zone name).  The
 * GMT offset can be in seconds, or where it is evenly divisible by
 * 3600, then it can be in hours.  The offset must be the time to
 * add to the local time to get GMT.  If a offset is given and the
 * time zone observes daylight saving then the (daylight time zone
 * name) must also be given (otherwise it is assumed the time zone
 * does not observe any daylight savings).
 * <p>
 * The result of this method is given to getDefaultTimeZone(String)
 * which tries to map the time zone id to a known TimeZone.  See
 * that method on how the returned String is mapped to a real
 * TimeZone object.
 */
JNIEXPORT jstring JNICALL
Java_java_util_VMTimeZone_getSystemTimeZoneId(JNIEnv *env,
					      jclass clazz __attribute__ ((__unused__)))
{
  char buffer[64]; /* FIXME: large enough? (better not to use malloc(), because of possible failure!) */
  int  result;

  TARGET_NATIVE_MISC_GET_TIMEZONE_STRING(buffer,sizeof(buffer),result);
  if (result == TARGET_NATIVE_OK)
  {
    return (*env)->NewStringUTF (env, buffer);
  }
  else
  {
    return NULL;
  }
}

