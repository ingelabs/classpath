/* target_posix_misc.h - Native methods for misc operations
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

#ifndef __TARGET_POSIX_MISC__
#define __TARGET_POSIX_MISC__

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

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : CP_MISC_FORMAT_STRING
* Purpose    : format a string with arguments
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

#ifndef CP_MISC_FORMAT_STRING
  #define CP_MISC_FORMAT_STRING_POSIX
  #if defined(CPP_VARARGS)
    #define CP_MISC_FORMAT_STRING(buffer,bufferSize,format,args...) \
      cp_misc_format_string(buffer,bufferSize,format, ## args)
  #else
    #define CP_MISC_FORMAT_STRING \
      cp_misc_format_string
  #endif
#endif

/***********************************************************************\
* Name       : CP_MISC_FORMAT_STRING<n>
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

#ifndef CP_MISC_FORMAT_STRING0
  #define CP_MISC_FORMAT_STRING0_POSIX
  #define CP_MISC_FORMAT_STRING0(buffer,bufferSize,format) \
    cp_misc_format_string0(buffer,bufferSize,format)
#endif
#ifndef CP_MISC_FORMAT_STRING1
  #define CP_MISC_FORMAT_STRING1_POSIX
  #define CP_MISC_FORMAT_STRING1(buffer,bufferSize,format,arg1) \
    cp_misc_format_string1(buffer,bufferSize,format,(int)arg1)
#endif
#ifndef CP_MISC_FORMAT_STRING2
  #define CP_MISC_FORMAT_STRING2_POSIX
  #define CP_MISC_FORMAT_STRING2(buffer,bufferSize,format,arg1,arg2) \
    cp_misc_format_string2(buffer,bufferSize,format,(int)arg1,(int)arg2)
#endif
#ifndef CP_MISC_FORMAT_STRING3
  #define CP_MISC_FORMAT_STRING3_POSIX
  #define CP_MISC_FORMAT_STRING3(buffer,bufferSize,format,arg1,arg2,arg3) \
    cp_misc_format_string3(buffer,bufferSize,format,(int)arg1,(int)arg2,(int)arg3)
#endif
#ifndef CP_MISC_FORMAT_STRING4
  #define CP_MISC_FORMAT_STRING4_POSIX
  #define CP_MISC_FORMAT_STRING4(buffer,bufferSize,format,arg1,arg2,arg3,arg4) \
    cp_misc_format_string4(buffer,bufferSize,format,(int)arg1,(int)arg2,(int)arg3,(int)arg4)
#endif
#ifndef CP_MISC_FORMAT_STRING5
  #define CP_MISC_FORMAT_STRING5_POSIX
  #define CP_MISC_FORMAT_STRING5(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5) \
    cp_misc_format_string5(buffer,bufferSize,formatt,(int)arg1,(int)arg2,(int)arg3,(int)arg4,(int)arg5)
#endif
#ifndef CP_MISC_FORMAT_STRING6
  #define CP_MISC_FORMAT_STRING6_POSIX
  #define CP_MISC_FORMAT_STRING6(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6) \
    cp_misc_format_string6(buffer,bufferSize,format,(int)arg1,(int)arg2,(int)arg3,(int)arg4,(int)arg5,(int)arg6)
#endif
#ifndef CP_MISC_FORMAT_STRING7
  #define CP_MISC_FORMAT_STRING7_POSIX
  #define CP_MISC_FORMAT_STRING7(buffer,bufferSize,format,arg1,arg2,arg3,arg14,arg5,arg6,arg7) \
    cp_misc_format_string7(buffer,bufferSize,format,(int)arg1,(int)arg2,(int)arg3,(int)arg4,(int)arg5,(int)arg6,(int)arg7)
#endif
#ifndef CP_MISC_FORMAT_STRING8
  #define CP_MISC_FORMAT_STRING8_POSIX
  #define CP_MISC_FORMAT_STRING8(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8) \
    cp_misc_format_string8(buffer,bufferSize,format,(int)arg1,(int)arg2,(int)arg3,(int)arg4,(int)arg5,(int)arg6,(int)arg7,(int)arg8)
#endif
#ifndef CP_MISC_FORMAT_STRING9
  #define CP_MISC_FORMAT_STRING9_POSIX
  #define CP_MISC_FORMAT_STRING9(buffer,bufferSize,format,arg1,arg2,arg3,arg4,arg5,arg6,arg7,arg8,arg9) \
    cp_misc_format_string9(buffer,bufferSize,format,(int)arg1,(int)arg2,(int)arg3,(int)arg4,(int)arg5,(int)arg6,(int)arg7,(int)arg8,(int)arg9)
#endif

/***********************************************************************\
* Name       : CP_FORMAT_STRING_ELLIPSE
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

#ifndef CP_MISC_FORMAT_STRING_ELLIPSE
  #include <stdarg.h>
  #ifdef HAVE_VSNPRINTF
    #define CP_MISC_FORMAT_STRING_ELLIPSE(buffer,bufferSize,format) \
      do { \
        va_list __arguments; \
        \
        va_start(__arguments,format); \
        vsnprintf(buffer,bufferSize,format,__arguments); \
        va_end(__arguments); \
      } while (0)
  #else
    #define CP_MISC_FORMAT_STRING_ELLIPSE(buffer,bufferSize,format) \
      do { \
        va_list __arguments; \
        \
        va_start(__arguments,format); \
        vsprintf(buffer,format,__arguments); \
        va_end(__arguments); \
      } while (0)
  #endif
#endif

/***********************************************************************\
* Name       : CP_MISC_GET_TIMEZONE_STRING
* Purpose    : get timezone string
* Input      : string          - buffer for timezone string
*              maxStringLength - max. string length
* Output     : string - timezone string
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : set WITH_TIMEZONE_VARIABLE to timezone variable if not
*              'timezone' (e. g. Cygwin)
\***********************************************************************/

#ifndef CP_MISC_GET_TIMEZONE_STRING
  #define CP_MISC_GET_TIMEZONE_STRING_POSIX
  #define CP_MISC_GET_TIMEZONE_STRING(string,maxStringLength,result) \
    (result) = cp_misc_get_timezone_string(string,maxStringLength)
#endif

#ifndef CP_MISC_GET_ENV
  #define CP_MISC_GET_ENV_POSIX
  #define CP_MISC_GET_ENV(name,value,maxValueLen,result) \
    (result) = cp_misc_get_env(name,value,maxValueLen)
#endif

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

int cp_misc_format_string(char *buffer, int bufferSize, const char *format, ...);
int cp_misc_format_string0(char *buffer, int bufferSize, const char *format);
int cp_misc_format_string1(char *buffer, int bufferSize, const char *format, int arg1);
int cp_misc_format_string2(char *buffer, int bufferSize, const char *format, int arg1, int arg2);
int cp_misc_format_string3(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3);
int cp_misc_format_string4(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4);
int cp_misc_format_string5(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5);
int cp_misc_format_string6(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6);
int cp_misc_format_string7(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7);
int cp_misc_format_string8(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8);
int cp_misc_format_string9(char *buffer, int bufferSize, const char *format, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9);
int cp_misc_get_timezone_string(char *string, int maxStringLength);
int cp_misc_get_env(const char *name, char *value, int maxValueLen);

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_POSIX_MISC__ */

/* end of file */

