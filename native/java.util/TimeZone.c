/*************************************************************************
 * TimeZone.c - Native methods for java.util.TimeZone
 *
 * Copyright (c) 1999 Free Software Foundation, Inc.
 * Written by Aaron M. Renn (arenn@urbanophile.com)
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#if TIME_WITH_SYS_TIME
# include <sys/time.h>
# include <time.h>
#else
# if HAVE_SYS_TIME_H
#  include <sys/time.h>
# else
#  include <time.h>
# endif
#endif

#include <jni.h>

#include "config.h"

#include "java_util_TimeZone.h"

/*
 * This method returns a time zone string that is used by the static
 * initializer in java.util.TimeZone to create the default timezone
 * instance.  This is a key into the timezone table used by
 * that class.
 */
JNIEXPORT jstring JNICALL
Java_java_util_TimeZone_getDefaultTimeZoneId(JNIEnv *env, jclass clazz)
{
  time_t current_time;
  char **tzinfo, *tzid;
  long tzoffset;
  jstring retval;

  current_time = time(0);

  mktime(localtime(&current_time));
  tzinfo = tzname;
  tzoffset = timezone;

  if ((tzoffset % 3600) == 0)
    tzoffset = tzoffset / 3600;

  if (!strcmp(tzinfo[0], tzinfo[1]))  
    {
      tzid = (char*)malloc(strlen(tzinfo[0]) + 6);
      if (!tzid)
        return(0);

      sprintf(tzid, "%s%ld", tzinfo[0], tzoffset);
    }
  else
    {
      tzid = (char*)malloc(strlen(tzinfo[0]) + strlen(tzinfo[1]) + 6);
      if (!tzid)
        return(0);

      sprintf(tzid, "%s%ld%s", tzinfo[0], tzoffset, tzinfo[1]);
    }

  retval = (*env)->NewStringUTF(env, tzid);
  if (!retval)
    return(0);
 
  (*env)->NewGlobalRef(env, retval);

  return(retval);
}

