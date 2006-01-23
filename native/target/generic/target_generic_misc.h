/* target_gneric_misc.h - Native methods for generic misc operations
   Copyright (C) 1998, 2006 Free Software Foundation, Inc.

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

#ifndef __TARGET_GENERIC_MISC__
#define __TARGET_GENERIC_MISC__

/* check if target_native_misc.h included */
#ifndef __TARGET_NATIVE_MISC__
  #error Do NOT INCLUDE generic target files! Include the corresponding native target files instead!
#endif

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>

#include "target_native.h"

/*
#ifndef HAVE_SNPRINTF
  #warning Function snprintf() not available - use insecure sprintf()!
#endif
#ifndef HAVE_VSNPRINTF
  #warning Function vsnprintf() not available - use insecure vsprintf()!
#endif
*/

#ifdef NEW_CP
#include "../posix/target_posix_misc.h"
#endif

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : TARGET_NATIVE_MISC_FORMAT_STRING<n>
* Purpose    : format a string (with a fixed number of) arguments
* Input      : buffer     - buffer for string
*              bufferSize - size of buffer
*              format     - format string (like printf)
*              args       - optional arguments (GNU CPP only!)
* Output     : -
* Return     : length of formated string
* Side-effect: unknown
* Notes      : - this is a "safe" macro to format string; buffer-
*                overflows will be avoided. Direct usage of e. g.
*                snprintf() is not permitted because it is not ANSI C
*                (not portable!)
*              - if length of string is >= bufferSize the buffer was
*                to small to format string completely!
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING0
  #include <stdio.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING0(buffer,bufferSize,format) \
      snprintf(buffer,bufferSize,format)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING0(buffer,bufferSize,format) \
      targetGenericMisc_formatString(buffer,bufferSize,format)
  #endif
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING1
  #include <stdarg.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING1(buffer,bufferSize,format,arg1) \
      snprintf(buffer,bufferSize,format,arg1)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING1(buffer,bufferSize,format,arg1) \
      targetGenericMisc_formatString(buffer,bufferSize,format,arg1)
  #endif
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING2
  #include <stdarg.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING2(buffer,bufferSize,format,arg1,arg2) \
      snprintf(buffer,bufferSize,format,arg1,arg2)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING2(buffer,bufferSize,format,arg1,arg2) \
      targetGenericMisc_formatString(buffer,bufferSize,format,arg1,arg2)
  #endif
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING3
  #include <stdarg.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING3(buffer,bufferSize,format,arg1,arg2,arg3) \
      snprintf(buffer,bufferSize,format,arg1,arg2,arg3)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING3(buffer,bufferSize,format,arg1,arg2,arg3) \
      targetGenericMisc_formatString(buffer,bufferSize,format,arg1,arg2,arg3)
  #endif
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING4
  #include <stdarg.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING4(buffer,bufferSize,format,arg1,arg2,arg3,arg4) \
        snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING4(buffer,bufferSize,format,arg1,arg2,arg3,arg4) \
        targetGenericMisc_formatString(buffer,bufferSize,format,arg1,arg2,arg3,arg4)
  #endif
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING5
  #include <stdarg.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING5(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5) \
      snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING5(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5) \
      targetGenericMisc_formatString(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5)
  #endif
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING6
  #include <stdarg.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING6(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6) \
      snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING6(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6) \
      targetGenericMisc_formatString(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6)
  #endif
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING7
  #include <stdarg.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING7(buffer,bufferSize,format,arg1,arg2,arg3,arg14,arg5,arg6,arg7) \
      snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING7(buffer,bufferSize,format,arg1,arg2,arg3,arg14,arg5,arg6,arg7) \
      targetGenericMisc_formatString(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7)
  #endif
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING8
  #include <stdarg.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING8(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8) \
      snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING8(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8) \
      targetGenericMisc_formatString(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8)
  #endif
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING9
  #include <stdarg.h>
  #ifdef HAVE_SNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING9(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9) \
      snprintf(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
    #define TARGET_NATIVE_MISC_FORMAT_STRING9(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9) \
      targetGenericMisc_formatString(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9)
  #endif
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING0
  #define TARGET_NATIVE_MISC_FORMAT_STRING0(buffer,bufferSize,format) \
    CP_MISC_FORMAT_STRING0(buffer,bufferSize,format)
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING1
  #define TARGET_NATIVE_MISC_FORMAT_STRING1(buffer,bufferSize,format,arg1) \
    CP_MISC_FORMAT_STRING1(buffer,bufferSize,format,arg1)
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING2
  #define TARGET_NATIVE_MISC_FORMAT_STRING2(buffer,bufferSize,format,arg1,arg2) \
    CP_MISC_FORMAT_STRING2(buffer,bufferSize,format,arg1,arg2)
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING3
  #define TARGET_NATIVE_MISC_FORMAT_STRING3(buffer,bufferSize,format,arg1,arg2,arg3) \
    CP_MISC_FORMAT_STRING3(buffer,bufferSize,format,arg1,arg2,arg3)
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING4
  #define TARGET_NATIVE_MISC_FORMAT_STRING4(buffer,bufferSize,format,arg1,arg2,arg3,arg4) \
    CP_MISC_FORMAT_STRING4(buffer,bufferSize,format,arg1,arg2,arg3,arg4)
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING5
  #define TARGET_NATIVE_MISC_FORMAT_STRING5(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5) \
    CP_MISC_FORMAT_STRING5(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5)
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING6
  #define TARGET_NATIVE_MISC_FORMAT_STRING6(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6) \
    CP_MISC_FORMAT_STRING6(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6)
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING7
  #define TARGET_NATIVE_MISC_FORMAT_STRING7(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7) \
    CP_MISC_FORMAT_STRING7(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7)
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING8
  #define TARGET_NATIVE_MISC_FORMAT_STRING8(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8) \
    CP_MISC_FORMAT_STRING8(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8)
#endif
#ifndef TARGET_NATIVE_MISC_FORMAT_STRING9
  #define TARGET_NATIVE_MISC_FORMAT_STRING9(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9) \
    CP_MISC_FORMAT_STRING9(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FORMAT_STRING_ELLIPSE
* Purpose    : format a string with arguments
* Input      : buffer     - buffer for string
*              bufferSize - size of buffer
*              format     - format string (like printf)
* Output     : length - length of formated string
* Return     : -
* Side-effect: unknown
* Notes      : - this is a "safe" macro to format a string; buffer-
*                overflows will be avoided. Direct usage of e. g.
*                snprintf() is not permitted because it is not ANSI C
*                (not portable!)
*              - do not use this routine in a function without
*                variable number of arguments (ellipses), because
*                va_list/va_start/va_end is used!
\***********************************************************************/

#ifndef TARGET_NATIVE_MISC_FORMAT_STRING_ELLIPSE
  #include <stdarg.h>
  #ifdef HAVE_VSNPRINTF
    #define TARGET_NATIVE_MISC_FORMAT_STRING_ELLIPSE(buffer,bufferSize,format,length) \
      do { \
        va_list __arguments; \
        \
        va_start(__arguments,format); \
        length=vsnprintf(buffer,bufferSize,format,__arguments); \
        va_end(__arguments); \
      } while (0)
  #else
    #define TARGET_NATIVE_MISC_FORMAT_STRING_ELLIPSE(buffer,bufferSize,format,length) \
      do { \
        va_list __arguments; \
        \
        va_start(__arguments,format); \
        length=vsprintf(buffer,format,__arguments); \
        va_end(__arguments); \
      } while (0)
  #endif
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_MISC_GET_TIMEZONE_STRING
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

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MISC_GET_TIMEZONE_STRING
  #define TARGET_NATIVE_MISC_GET_TIMEZONE_STRING_GENERIC
  #define TARGET_NATIVE_MISC_GET_TIMEZONE_STRING(string,maxStringLength,result) \
    do { \
      result=targetGenericMisc_getTimeZoneString(string,maxStringLength); \
    } while (0)
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_MISC_GET_TIMEZONE_STRING
  #define TARGET_NATIVE_MISC_GET_TIMEZONE_STRING(string,maxStringLength,result) \
    CP_MISC_GET_TIMEZONE_STRING(string,maxStringLength,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_MISC_GET_ENV
* Purpose    : get environment variable
* Input      : name           - environment variable name
*              value          - buffer for value
*              maxValueLength - max. length of value
* Output     : value  - environment variable value
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : value of environment is only copied if there is enought
*              space to store the whole value!
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_MISC_GET_ENV
  #include <stdlib.h>
  #define TARGET_NATIVE_MISC_GET_ENV(name,value,maxValueLength,result) \
    do { \
      const char *__s; \
      \
      value[0]='\0'; \
      result = TARGET_NATIVE_ERROR; \
      __s=getenv((char*)name); \
      if (__s!=NULL) \
      { \
        if (strlen(__s)<(size_t)maxValueLength) \
        { \
          strcpy(value,__s); \
          result = TARGET_NATIVE_OK; \
        } \
      } \
    } while (0)
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_MISC_GET_ENV
  #define TARGET_NATIVE_MISC_GET_ENV(name,value,maxValueLength,result) \
    CP_MISC_GET_ENV(name,value,maxValueLength,result)
#endif
#endif /* NEW_CP */

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC
int targetGenericMisc_formatString(char *buffer, unsigned int bufferSize, const char *format,...);
#endif /* TARGET_NATIVE_MISC_FORMAT_STRING_GENERIC */

#ifdef TARGET_NATIVE_MISC_GET_TIMEZONE_STRING_GENERIC
int targetGenericMisc_getTimeZoneString(char *buffer, unsigned int bufferSize);
#endif /* TARGET_NATIVE_MISC_GET_TIMEZONE_STRING_GENERIC */

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_GENERIC_MISC__ */

/* end of file */

