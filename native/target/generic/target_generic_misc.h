/* generic_math_int64.h - Native methods for 64bit math operations
   Copyright (C) 1998 Free Software Foundation, Inc.

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

/*
Description: generic target defintions of miscellaneous functions
Systems    : all
*/

#ifndef __TARGET_GENERIC_MISC__
#define __TARGET_GENERIC_MISC__

#ifdef __cplusplus
extern "C" {
#endif

/* check if target_native_misc.h included */
#ifndef __TARGET_NATIVE_MISC__
  #error Do NOT INCLUDE generic target files! Include the corresponding native target files instead!
#endif

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>

#include "target_native.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : TARGET_NATIVE_FORMAT_STRING
* Purpose    : format a string with arguments
* Input      : buffer     - buffer for string
*              bufferSize - size of buffer
*              format     - format string (like printf)
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : - this is a "safe" macro to format string; buffer-
*                overflows will be avoided. Direct usage of e. g.
*                snprintf() is not permitted because it is not ANSI C
*                (not portable!)
*              - do not use this routine in a function without
*                variable number of arguments (ellipses), because
*                va_list/va_start/va_end is used!
\***********************************************************************/

#ifndef TARGET_NATIVE_FORMAT_STRING
  #include <stdarg.h>
  #define TARGET_NATIVE_FORMAT_STRING(buffer,bufferSize,format) \
    do { \
      va_list __arguments; \
      \
      va_start(__arguments,format); \
      vsnprintf(buffer,bufferSize,format,__arguments); \
      va_end(__arguments); \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_UTIL_GET_TIMEZONE_STRING
* Purpose    : get timezone string
* Input      : string          - buffer for timezone string
*              maxStringLength - max. string length
* Output     : string - timezone string
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : set WITH_TIMEZONE_VARIABLE to timezone variable if not
*              'timezone' (e. g. Cygwin)
\***********************************************************************/

#ifndef TARGET_NATIVE_UTIL_GET_TIMEZONE_STRING
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
  #define TARGET_NATIVE_UTIL_GET_TIMEZONE_STRING(string,maxStringLength,result) \
    do { \
      tzset(); \
      \
      if (strcmp(tzname[0],tzname[1])!=0) \
      { \
        result=((strlen(tzname[0])+6)<=maxStringLength)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          snprintf(string,maxStringLength,"%s%ld",tzname[0],((WITH_TIMEZONE_VARIABLE%3600)==0)?WITH_TIMEZONE_VARIABLE/3600:WITH_TIMEZONE_VARIABLE); \
        } \
      } \
      else \
      { \
        result=((strlen(tzname[0])+strlen(tzname[1])+6)<=maxStringLength)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        if (result==TARGET_NATIVE_OK) \
        { \
          snprintf(string,maxStringLength,"%s%ld%s",tzname[0],((WITH_TIMEZONE_VARIABLE%3600)==0)?WITH_TIMEZONE_VARIABLE/3600:WITH_TIMEZONE_VARIABLE,tzname[1]); \
        } \
      } \
    } while (0)
#endif

/***************************** Functions *******************************/

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_GENERIC_MISC__ */

/* end of file */
