/* target_posix.h - Native methods for POSIX operations
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
Description: POSIX target global defintions
Systems    : all
*/

#ifndef __TARGET_POSIX__
#define __TARGET_POSIX__

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/
#define CP_OK    1
#define CP_ERROR 0

#define CP_MAX_ERROR_STRING_LENGTH 128

#ifndef CP_ERROR_PERMISION_DENIED
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_PERMISION_DENIED        EACCES
#endif
#ifndef CP_ERROR_BAD_FILE_DESCRIPTOR
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_BAD_FILE_DESCRIPTOR     EBADF
#endif
#ifndef CP_ERROR_FILE_EXISTS
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_FILE_EXISTS             EEXIST
#endif
#ifndef CP_ERROR_INPUT_OUTPUT
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_INPUT_OUTPUT            EIO
#endif
#ifndef CP_ERROR_TOO_MANY_OPEN_FILES
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_TOO_MANY_OPEN_FILES     EMFILE
#endif
#ifndef CP_ERROR_FILENAME_TO_LONG
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_FILENAME_TO_LONG        ENAMETOOLONG
#endif
#ifndef CP_ERROR_NO_SUCH_DEVICE
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_NO_SUCH_DEVICE          ENODEV
#endif
#ifndef CP_ERROR_NO_SUCH_FILE
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_NO_SUCH_FILE            ENOENT
#endif
#ifndef CP_ERROR_NO_SPACE_LEFT
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_NO_SPACE_LEFT           ENOSPC
#endif
#ifndef CP_ERROR_DIRECTORY_NOT_EMPTY
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_DIRECTORY_NOT_EMPTY     ENOTEMPTY
#endif
#ifndef CP_ERROR_OPERATION_NOT_PERMITTED
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_OPERATION_NOT_PERMITTED EPERM
#endif
#ifndef CP_ERROR_READ_ONLY_FILE_SYSTEM
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_READ_ONLY_FILE_SYSTEM   EROFS
#endif
#ifndef CP_ERROR_INVALID_SEEK
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_INVALID_SEEK            ESPIPE
#endif
#ifndef CP_ERROR_INTERRUPT_FUNCTION_CALL
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_INTERRUPT_FUNCTION_CALL EINTR
#endif
#ifndef CP_ERROR_INVALID_ARGUMENT
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_INVALID_ARGUMENT        EINVAL
#endif
#ifndef CP_ERROR_TRY_AGAIN
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_TRY_AGAIN               EAGAIN
#endif
#ifndef CP_ERROR_CONNECTION_REFUSED
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_CONNECTION_REFUSED      ECONNREFUSED
#endif
#ifndef CP_ERROR_TIMEOUT
// NYI: CLEANUP: how to autoconf?
  #include <errno.h>
  #define CP_ERROR_TIMEOUT                 ETIMEDOUT
#endif
#ifndef CP_ERROR_INSUFFICIENT_MEMORY
  #define CP_ERROR_INSUFFICIENT_MEMORY     253
#endif
#ifndef CP_ERROR_NOT_IMPLEMENTED
  #define CP_ERROR_NOT_IMPLEMENTED         254
#endif
#ifndef CP_ERROR_UNKNOWN
  #define CP_ERROR_UNKNOWN                 255
#endif

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : CP_LAST_ERROR
* Purpose    : return last error code
* Input      : -
* Output     : -
* Return     : error code
* Side-effect: unknown
* Notes      : return errno if not 0, otherwise return last set error
*              code
\***********************************************************************/

#ifndef CP_LAST_ERROR
  #define CP_LAST_ERROR_POSIX
  #define CP_LAST_ERROR() \
    cp_last_error()
#endif

/***********************************************************************\
* Name       : CP_LAST_ERROR_STRING
* Purpose    : return last error string
* Input      : -
* Output     : -
* Return     : error string (read only!)
* Side-effect: unknown
* Notes      : return system error string if errno is not 0, otherwise
*              return last set error string
\***********************************************************************/

#ifndef CP_LAST_ERROR_STRING
  #define CP_LAST_ERROR_STRING_POSIX
  #define CP_LAST_ERROR_STRING() \
    cp_last_error_string()
#endif

// NYI: FUTURE PROBLEM: buffer size?

#ifndef CP_LAST_ERROR_STRING_FORMAT
  #define CP_LAST_ERROR_STRING_FORMAT_POSIX
  #define CP_LAST_ERROR_STRING_FORMAT(buffer,bufferSize,format) \
    cp_last_error_string_format(buffer,bufferSize,format)
#endif

/***********************************************************************\
* Name       : CP_SET_LAST_ERROR
* Purpose    : set last error code and string
* Input      : errorCode   - error code
*              errorString - error string
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_SET_LAST_ERROR
  #define CP_SET_LAST_ERROR_POSIX
  #define CP_SET_LAST_ERROR(errorCode,errorString) \
    cp_set_last_error(errorCode,errorString)
#endif

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

int cp_last_error(void);
const char *cp_last_error_string(void);
char *cp_last_error_string_format(char *buffer, int bufferSize, const char *format);
void cp_set_last_error(int errorCode, const char *errorString);

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_POSIX__ */

/* end of file */

