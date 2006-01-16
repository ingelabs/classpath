/* target_posix_misc.c - Native methods for misc operations
   Copyright (C) 2006 Free Software Foundation, Inc.

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

/*
Description: generic target defintions of miscellaneous functions
Systems    : all
*/

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>

#include "target_posix.h"

/*
#ifndef HAVE_SNPRINTF
  #warning Function snprintf() not available - use insecure sprintf()!
#endif
#ifndef HAVE_VSNPRINTF
  #warning Function vsnprintf() not available - use insecure vsprintf()!
#endif
*/

#include "target_posix_misc.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef NEW_CP

#ifdef CP_MISC_FORMAT_STRING_POSIX
  #include <stdio.h>
  int cp_misc_format_string(char *buffer, int bufferSize, const char *format, ...)
    {
      va_list arguments;
      int     n;

      va_start(arguments,format);
      #ifdef HAVE_VSNPRINTF
        n = vsnprintf(buffer,bufferSize,format,arguments);
      #else
        n = vsprintf(buffer,bufferSize,format,arguments);
        assert(n<=bufferSize);
      #endif
      va_end(arguments);

      return n;
    }
#endif

#ifdef CP_MISC_FORMAT_STRING0_POSIX
  #include <stdio.h>
  int cp_misc_format_string0(char *buffer, int bufferSize, const char *format)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format);
      #else
        n = sprintf(buffer,format);
        assert(n<=bufferSize);
      #endif

      return n;
    }
#endif
#ifdef CP_MISC_FORMAT_STRING1_POSIX
  #include <stdarg.h>
  int cp_misc_format_string1(char *buffer, int bufferSize, const char *format, int arg1)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format,arg1);
      #else
        n = sprintf(buffer,format,arg1);
        assert(n<=bufferSize);
      #endif

      return n;
    }
#endif
#ifdef CP_MISC_FORMAT_STRING2_POSIX
  #include <stdarg.h>
  int cp_misc_format_string2(char *buffer, int bufferSize, const char *format, int arg1, int arg2)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format,arg1,arg2);
      #else
        n = sprintf(buffer,format,arg1,arg2);
        assert(n<=bufferSize);
      #endif

      return n;
    }
#endif
#ifdef CP_MISC_FORMAT_STRING3_POSIX
  #include <stdarg.h>
  int cp_misc_format_string3(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format,arg1,arg2,arg3);
      #else
        n = sprintf(buffer,format,arg1,arg2,arg3);
      #endif

      return n;
    }
#endif
#ifdef CP_MISC_FORMAT_STRING4_POSIX
  #include <stdarg.h>
  int cp_misc_format_string4(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4);
      #else
        n = sprintf(buffer,format,arg1,arg2,arg3,arg4);
        assert(n<=bufferSize);
      #endif

      return n;
    }
#endif
#ifdef CP_MISC_FORMAT_STRING5_POSIX
  #include <stdarg.h>
  int cp_misc_format_string5(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5);
      #else
        n = sprintf(buffer,format,arg1,arg2,arg3,arg4,arg5);
        assert(n<=bufferSize);
      #endif

      return n;
    }
#endif
#ifdef CP_MISC_FORMAT_STRING6_POSIX
  #include <stdarg.h>
  int cp_misc_format_string6(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6);
      #else
        n = sprintf(buffer,format,arg1,arg2,arg3,arg4,arg5,arg6);
      #endif

      return n;
    }
#endif
#ifdef CP_MISC_FORMAT_STRING7_POSIX
  #include <stdarg.h>
  int cp_misc_format_string7(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7);
      #else
        n = sprintf(buffer,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7);
        assert(n<=bufferSize);
      #endif

      return n;
    }
#endif
#ifdef CP_MISC_FORMAT_STRING8_POSIX
  #include <stdarg.h>
  int cp_misc_format_string8(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8);
      #else
        n = sprintf(buffer,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8);
        assert(n<=bufferSize);
      #endif

      return n;
    }
#endif
#ifdef CP_MISC_FORMAT_STRING9_POSIX
  #include <stdarg.h>
  int cp_misc_format_string9(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9)
    {
      int n;

      #ifdef HAVE_SNPRINTF
        n = snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9);
      #else
        n = sprintf(buffer,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9);
        assert(n<=bufferSize);
      #endif

      return n;
    }
#endif

#ifdef CP_MISC_GET_TIMEZONE_STRING_POSIX
  #if TIME_WITH_SYS_TIME
     #include <sys/time.h>
     #include <time.h>
   #else
     #if HAVE_SYS_TIME_H
       #include <sys/time.h>
     #else
       #include <time.h>
     #endif
  #endif
  #include <string.h>
  #ifndef WITH_TIMEZONE_VARIABLE
    #define WITH_TIMEZONE_VARIABLE timezone
  #endif
  int cp_misc_get_timezone_string(char *string, int maxStringLength)
    {
      int result;

#if 0
      struct tm tim;
      #ifndef HAVE_LOCALTIME_R
        struct tm *lt_tim;
      #endif
      #ifdef HAVE_TM_ZONE
        int month;
      #endif
      time_t current_time;
      long tzoffset;
      const char *tz1, *tz2;
      char tzoff[11];
      size_t tz1_len, tz2_len, tzoff_len;
      char *tzid;

      //  time(&current_time);
      #ifdef HAVE_LOCALTIME_R
      //  localtime_r(&current_time, &tim);
      #else
        /* Fall back on non-thread safe localtime. */
        lt_tim = localtime(&current_time);
        memcpy(&tim, lt_tim, sizeof (struct tm));
      #endif
      mktime(&tim);

      #ifdef HAVE_STRUCT_TM_TM_ZONE
        /* We will cycle through the months to make sure we hit dst. */
        month = tim.tm_mon;
        tz1 = tz2 = NULL;
        while (tz1 == NULL || tz2 == NULL)
          {
            if (tim.tm_isdst > 0)
              tz2 = tim.tm_zone;
            else if (tz1 == NULL)
              {
                tz1 = tim.tm_zone;
                month = tim.tm_mon;
              }

            if (tz1 == NULL || tz2 == NULL)
              {
                tim.tm_mon++;
                tim.tm_mon %= 12;
              }

            if (tim.tm_mon == month && tz2 == NULL)
              tz2 = "";
            else
              mktime(&tim);
          }
        /* We want to make sure the tm struct we use later on is not dst. */
        tim.tm_mon = month;
        mktime(&tim);
      #elif defined (HAVE_TZNAME)
        /* If dst is never used, tzname[1] is the empty string. */
        tzset();
        tz1 = tzname[0];
        tz2 = tzname[1];
      #else
        /* Some targets have no concept of timezones. Assume GMT without dst. */
        tz1 = "GMT";
        tz2 = "";
      #endif

      #ifdef STRUCT_TM_HAS_GMTOFF
        /* tm_gmtoff is the number of seconds that you must add to GMT to get
           local time, we need the number of seconds to add to the local time
           to get GMT. */
        tzoffset = -1L * tim.tm_gmtoff;
      #elif HAVE_UNDERSCORE_TIMEZONE
        /* On some systems _timezone is actually defined as time_t. */
        tzoffset = (long) _timezone;
      #elif HAVE_TIMEZONE
        /* timezone is secs WEST of UTC. */
        tzoffset = timezone;	
      #else
        /* FIXME: there must be another global if neither tm_gmtoff nor timezone
           is available, esp. if tzname is valid.
           Richard Earnshaw <rearnsha@arm.com> has suggested using difftime to
           calculate between gmtime and localtime (and accounting for possible
           daylight savings time) as an alternative. */
        tzoffset = 0L;
      #endif

      if ((tzoffset % 3600) == 0)
        tzoffset = tzoffset / 3600;

      tz1_len = strlen(tz1);
      tz2_len = strlen(tz2);
      tzoff_len = jint_to_charbuf (tzoff + 11, tzoffset);
      if (bufferSize>=(tz1_len + tz2_len + tzoff_len + 1))
      {
        memcpy (buffer, tz1, tz1_len);
        memcpy (buffer + tz1_len, tzoff + 11 - tzoff_len, tzoff_len);
        memcpy (buffer + tz1_len + tzoff_len, tz2, tz2_len);
        buffer[tz1_len + tzoff_len + tz2_len] = '\0';
        result = TARGET_NATIVE_OK;
      }
      else
      {
        result = TARGET_NATIVE_ERROR;
      }
#else
      result=CP_ERROR;
#endif /* 0 */

      return result;
    }
#endif

#ifdef CP_MISC_GET_ENV_POSIX
int cp_misc_get_env(const char *name, char *value, int maxValueLen)
{
  const char *s;
  int        result;

  s = getenv(name);
  if ((s!=NULL) && (strlen(s)<(size_t)maxValueLen))
    {
      strcpy(value,s);
      result = CP_OK;
    }
  else
    {
      result = CP_ERROR;
    }

  return result;
}
#endif

#endif /* NEW_CP */

#ifdef __cplusplus
}
#endif

/* end of file */

