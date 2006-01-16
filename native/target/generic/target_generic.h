/* target_generic.h - Native methods for generic operations
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
Description: generic target global defintions
Systems    : all
*/

#ifndef __TARGET_GENERIC__
#define __TARGET_GENERIC__

/* check if target_native_network.h included */
#ifndef __TARGET_NATIVE__
  #error Do NOT INCLUDE generic target files! Include the corresponding native target files instead!
#endif

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <string.h>

#ifdef NEW_CP
#include "../posix/target_posix.h"
#endif

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/
#ifndef NEW_CP
#define TARGET_NATIVE_OK    1
#define TARGET_NATIVE_ERROR 0

#define TARGET_NATIVE_MAX_ERROR_STRING_LENGTH 128

#ifndef TARGET_NATIVE_ERROR_PERMISION_DENIED
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_PERMISION_DENIED        EACCES
#endif
#ifndef TARGET_NATIVE_ERROR_BAD_FILE_DESCRIPTOR
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_BAD_FILE_DESCRIPTOR     EBADF
#endif
#ifndef TARGET_NATIVE_ERROR_FILE_EXISTS
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_FILE_EXISTS             EEXIST
#endif
#ifndef TARGET_NATIVE_ERROR_INPUT_OUTPUT
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_INPUT_OUTPUT            EIO
#endif
#ifndef TARGET_NATIVE_ERROR_TOO_MANY_OPEN_FILES
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_TOO_MANY_OPEN_FILES     EMFILE
#endif
#ifndef TARGET_NATIVE_ERROR_FILENAME_TO_LONG
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_FILENAME_TO_LONG        ENAMETOOLONG
#endif
#ifndef TARGET_NATIVE_ERROR_NO_SUCH_DEVICE
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_NO_SUCH_DEVICE          ENODEV
#endif
#ifndef TARGET_NATIVE_ERROR_NO_SUCH_FILE
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_NO_SUCH_FILE            ENOENT
#endif
#ifndef TARGET_NATIVE_ERROR_NO_SPACE_LEFT
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_NO_SPACE_LEFT           ENOSPC
#endif
#ifndef TARGET_NATIVE_ERROR_DIRECTORY_NOT_EMPTY
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_DIRECTORY_NOT_EMPTY     ENOTEMPTY
#endif
#ifndef TARGET_NATIVE_ERROR_OPERATION_NOT_PERMITTED
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_OPERATION_NOT_PERMITTED EPERM
#endif
#ifndef TARGET_NATIVE_ERROR_READ_ONLY_FILE_SYSTEM
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_READ_ONLY_FILE_SYSTEM   EROFS
#endif
#ifndef TARGET_NATIVE_ERROR_INVALID_SEEK
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_INVALID_SEEK            ESPIPE
#endif
#ifndef TARGET_NATIVE_ERROR_INTERRUPT_FUNCTION_CALL
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_INTERRUPT_FUNCTION_CALL EINTR
#endif
#ifndef TARGET_NATIVE_ERROR_INVALID_ARGUMENT
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_INVALID_ARGUMENT        EINVAL
#endif
#ifndef TARGET_NATIVE_ERROR_TRY_AGAIN
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_TRY_AGAIN               EAGAIN
#endif
#ifndef TARGET_NATIVE_ERROR_CONNECTION_REFUSED
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_CONNECTION_REFUSED      ECONNREFUSED
#endif
#ifndef TARGET_NATIVE_ERROR_TIMEDOUT
  #include <errno.h>
  #define TARGET_NATIVE_ERROR_TIMEDOUT                ETIMEDOUT
#endif
#ifndef TARGET_NATIVE_ERROR_INSUFFICIENT_MEMORY
  #define TARGET_NATIVE_ERROR_INSUFFICIENT_MEMORY     253
#endif
#ifndef TARGET_NATIVE_ERROR_NOT_IMPLEMENTED
  #define TARGET_NATIVE_ERROR_NOT_IMPLEMENTED         254
#endif
#ifndef TARGET_NATIVE_ERROR_UNKNOWN
  #define TARGET_NATIVE_ERROR_UNKNOWN                 255
#endif
#else /* NEW_CP */
#define TARGET_NATIVE_OK    CP_OK
#define TARGET_NATIVE_ERROR CP_ERROR

#define TARGET_NATIVE_MAX_ERROR_STRING_LENGTH CP_MAX_ERROR_STRING_LENGTH

#ifndef TARGET_NATIVE_ERROR_PERMISION_DENIED
  #define TARGET_NATIVE_ERROR_PERMISION_DENIED        CP_ERROR_PERMISION_DENIED
#endif
#ifndef TARGET_NATIVE_ERROR_BAD_FILE_DESCRIPTOR
  #define TARGET_NATIVE_ERROR_BAD_FILE_DESCRIPTOR     CP_ERROR_BAD_FILE_DESCRIPTOR
#endif
#ifndef TARGET_NATIVE_ERROR_FILE_EXISTS
  #define TARGET_NATIVE_ERROR_FILE_EXISTS             CP_ERROR_FILE_EXISTS
#endif
#ifndef TARGET_NATIVE_ERROR_INPUT_OUTPUT
  #define TARGET_NATIVE_ERROR_INPUT_OUTPUT            CP_ERROR_INPUT_OUTPUT
#endif
#ifndef TARGET_NATIVE_ERROR_TOO_MANY_OPEN_FILES
  #define TARGET_NATIVE_ERROR_TOO_MANY_OPEN_FILES     CP_ERROR_TOO_MANY_OPEN_FILES
#endif
#ifndef TARGET_NATIVE_ERROR_FILENAME_TO_LONG
  #define TARGET_NATIVE_ERROR_FILENAME_TO_LONG        CP_ERROR_INPUT_OUTPUT
#endif
#ifndef TARGET_NATIVE_ERROR_NO_SUCH_DEVICE
  #define TARGET_NATIVE_ERROR_NO_SUCH_DEVICE          CP_ERROR_NO_SUCH_DEVICE
#endif
#ifndef TARGET_NATIVE_ERROR_NO_SUCH_FILE
  #define TARGET_NATIVE_ERROR_NO_SUCH_FILE            CP_ERROR_NO_SUCH_FILE
#endif
#ifndef TARGET_NATIVE_ERROR_NO_SPACE_LEFT
  #define TARGET_NATIVE_ERROR_NO_SPACE_LEFT           CP_ERROR_NO_SPACE_LEFT
#endif
#ifndef TARGET_NATIVE_ERROR_DIRECTORY_NOT_EMPTY
  #define TARGET_NATIVE_ERROR_DIRECTORY_NOT_EMPTY     CP_ERROR_DIRECTORY_NOT_EMPTY
#endif
#ifndef TARGET_NATIVE_ERROR_OPERATION_NOT_PERMITTED
  #define TARGET_NATIVE_ERROR_OPERATION_NOT_PERMITTED CP_ERROR_OPERATION_NOT_PERMITTED
#endif
#ifndef TARGET_NATIVE_ERROR_READ_ONLY_FILE_SYSTEM
  #define TARGET_NATIVE_ERROR_READ_ONLY_FILE_SYSTEM   CP_ERROR_READ_ONLY_FILE_SYSTEM
#endif
#ifndef TARGET_NATIVE_ERROR_INVALID_SEEK
  #define TARGET_NATIVE_ERROR_INVALID_SEEK            CP_ERROR_INVALID_SEEK
#endif
#ifndef TARGET_NATIVE_ERROR_INTERRUPT_FUNCTION_CALL
  #define TARGET_NATIVE_ERROR_INTERRUPT_FUNCTION_CALL CP_ERROR_INTERRUPT_FUNCTION_CALL
#endif
#ifndef TARGET_NATIVE_ERROR_INVALID_ARGUMENT
  #define TARGET_NATIVE_ERROR_INVALID_ARGUMENT        CP_ERROR_INVALID_ARGUMENT
#endif
#ifndef TARGET_NATIVE_ERROR_TRY_AGAIN
  #define TARGET_NATIVE_ERROR_TRY_AGAIN               CP_ERROR_TRY_AGAIN
#endif
#ifndef TARGET_NATIVE_ERROR_CONNECTION_REFUSED
  #define TARGET_NATIVE_ERROR_CONNECTION_REFUSED      CP_ERROR_CONNECTION_REFUSED
#endif
#ifndef TARGET_NATIVE_ERROR_TIMEDOUT
  #define TARGET_NATIVE_ERROR_TIMEDOUT                CP_ERROR_TIMEOUT
#endif
#ifndef TARGET_NATIVE_ERROR_INSUFFICIENT_MEMORY
  #define TARGET_NATIVE_ERROR_INSUFFICIENT_MEMORY     CP_INSUFFICIENT_MEMORY
#endif
#ifndef TARGET_NATIVE_ERROR_NOT_IMPLEMENTED
  #define TARGET_NATIVE_ERROR_NOT_IMPLEMENTED         CP_NOT_IMPLEMENTED
#endif
#ifndef TARGET_NATIVE_ERROR_UNKNOWN
  #define TARGET_NATIVE_ERROR_UNKNOWN                 CP_ERROR_UNKNOWN
#endif
#endif /* NEW_CP */

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : TARGET_NATIVE_LAST_ERROR
* Purpose    : return last error code
* Input      : -
* Output     : -
* Return     : error code
* Side-effect: unknown
* Notes      : return errno if not 0, otherwise return last set error
*              code
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_LAST_ERROR
  #include <errno.h>
  #define TARGET_NATIVE_LAST_ERROR_GENERIC
  extern int  targetNativeLastErrorCode;
  extern char targetNativeLastErrorString[];
  #define TARGET_NATIVE_LAST_ERROR() \
    ((errno!=0)?errno:((targetNativeLastErrorCode!=0)?targetNativeLastErrorCode:0))
#endif
#else /* NEW_CP */
  #define TARGET_NATIVE_LAST_ERROR() \
    CP_LAST_ERROR()
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_LAST_ERROR_STRING
* Purpose    : return last error string
* Input      : -
* Output     : -
* Return     : error string (read only!)
* Side-effect: unknown
* Notes      : return system error string if errno is not 0, otherwise
*              return last set error string
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_LAST_ERROR_STRING
  #include <string.h>
  #include <errno.h>
  #define TARGET_NATIVE_LAST_ERROR_GENERIC
  extern int  targetNativeLastErrorCode;
  extern char targetNativeLastErrorString[];
  #define TARGET_NATIVE_LAST_ERROR_STRING() \
    ((errno!=0)?strerror(errno):((targetNativeLastErrorCode!=0)?targetNativeLastErrorString:""))
#endif
#else /* NEW_CP */
  #define TARGET_NATIVE_LAST_ERROR_STRING() \
    CP_LAST_ERROR_STRING()
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_LAST_ERROR_STRING_FORMAT
* Purpose    : format last error into string "... (error: ...)"
* Input      : buffer     - buffer
*              bufferSize - buffer size
*              text       - error text
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
/* NYI: FUTURE PROBLEM: buffer size? */
#ifndef TARGET_NATIVE_LAST_ERROR_STRING_FORMAT
  #include <string.h>
  #include <errno.h>
  #define TARGET_NATIVE_LAST_ERROR_GENERIC
  extern int  targetNativeLastErrorCode;
  extern char targetNativeLastErrorString[];
  #define TARGET_NATIVE_LAST_ERROR_STRING_FORMAT(buffer,bufferSize,text) \
    do \
    { \
      sprintf(buffer,text); \
      strcat(" (error: "); \
      if      (errno!=0) \
      { \
        strcat(strerror(errno)); \
      } \
      else if (targetNativeLastErrorCode!=0) \
      { \
        strcat(targetNativeLastErrorString); \
      } \
      else \
      { \
        strcat("none"); \
      } \
      strcat(")"); \
    } while (0)
#endif
#else /* NEW_CP */
  #define TARGET_NATIVE_LAST_ERROR_STRING_FORMAT(buffer,bufferSize,format) \
    CP_LAST_ERROR_STRING_FORMAT(buffer,bufferSize,format
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_SET_LAST_ERROR
* Purpose    : set last error code and string
* Input      : errorCode   - error code
*              errorString - error string
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_SET_LAST_ERROR
  #include <errno.h>
  #define TARGET_NATIVE_SET_LAST_ERROR(errorCode,errorString) \
    do { \
      errno=0; \
      targetNativeLastErrorCode=errorCode; \
      strncpy(targetNativeLastErrorString,errorString,TARGET_NATIVE_MAX_ERROR_STRING_LENGTH); \
    } while(0)
#endif
#else /* NEW_CP */
  #define TARGET_NATIVE_SET_LAST_ERROR(errorCode,errorString) \
    CP_SET_LAST_ERROR(errorCode,errorString)
#endif /* NEW_CP */

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_GENERIC__ */

/* end of file */


