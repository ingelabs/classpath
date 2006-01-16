/* target_posix.c - Native methods for POSIX
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

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <assert.h>

#include "target_posix.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

#ifdef NEW_CP
static int  cp_LastErrorCode=0;
static char cp_LastErrorString[CP_MAX_ERROR_STRING_LENGTH];
#endif /* NEW_CP */

/****************************** Macros *********************************/

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef NEW_CP

#ifdef CP_LAST_ERROR_POSIX
  #include <errno.h>
  int cp_last_error(void)
    {
      if (errno != 0)
        {
          return errno;
        }
      else if (cp_LastErrorCode != 0)
        {
          return cp_LastErrorCode;
        }
      else
        {
          return 0;
        }
    }
#endif

#ifdef CP_LAST_ERROR_STRING_POSIX
  #include <string.h>
  #include <errno.h>
  const char *cp_last_error_string(void)
    {
      if (errno != 0)
        {
          return strerror(errno);
        }
      else if (cp_LastErrorCode != 0)
        {
          return cp_LastErrorString;
        }
      else
        {
          return "";
        }
    }
#endif

#ifdef CP_LAST_ERROR_STRING_FORMAT_POSIX
  #include <string.h>
  #include <errno.h>
  char *cp_last_error_string_format(char *buffer, int bufferSize, const char *format)
    {
      assert(buffer != NULL);

      snprintf(buffer,bufferSize,format);
      strncat(buffer," (error: ",bufferSize-strlen(buffer));
      if      (errno!=0)
      {
        strncat(buffer,strerror(errno),bufferSize-strlen(buffer));
      }
      else if (cp_LastErrorCode!=0)
      {
        strncat(buffer,cp_LastErrorString,bufferSize-strlen(buffer));
      }
      else
      {
        strncat(buffer,"none",bufferSize-strlen(buffer));
      }
      strncat(buffer,")",bufferSize-strlen(buffer));

      return buffer;
    }
#endif

#ifdef CP_SET_LAST_ERROR_POSIX
  void cp_set_last_error(int errorCode, const char *errorString)
    {
      errno = 0;
      cp_LastErrorCode = errorCode;
      strncpy(cp_LastErrorString,errorString,CP_MAX_ERROR_STRING_LENGTH);
    }
#endif

#endif /* NEW_CP */

#ifdef __cplusplus
}
#endif

/* end of file */

