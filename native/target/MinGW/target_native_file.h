/* target_native_file.h - File operations for the MinGW platform
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
Description: MinGW target defintions of file functions
Systems    : all
*/

#ifndef __TARGET_NATIVE_FILE__
#define __TARGET_NATIVE_FILE__

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include <config.h>

#include <stdlib.h>
#include <sys/stat.h>
#include <io.h>
#include <windows.h>
#include <winbase.h>

#include "target_native.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/
#define TARGET_NATIVE_FILE_FILEFLAG_SYNC  0
#define TARGET_NATIVE_FILE_FILEFLAG_DSYNC 0

#define TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL (S_IRUSR | S_IWUSR)
#define TARGET_NATIVE_FILE_FILEPERMISSION_PRIVATE (S_IRUSR | S_IWUSR)
#define TARGET_NATIVE_FILE_FILEPERMISSION_READONLY (~(S_IWRITE))

/* not available */
#define TARGET_NATIVE_FILE_LOCKMODE_READ  0
#define TARGET_NATIVE_FILE_LOCKMODE_WRITE 0

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/
#define TARGET_NATIVE_FILE_OPEN(filename,filedescriptor,flags,permissions,result) \
  do { \
    filedescriptor=open(filename, \
                        flags, \
                        permissions \
                        ); \
    result=(filedescriptor>=0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
   } while (0)

#define TARGET_NATIVE_FILE_TRUNCATE(filedescriptor,offset,result) \
  do { \
    result=(chsize(filedescriptor,offset)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
  } while (0)

#define TARGET_NATIVE_FILE_IS_EXECUTABLE(filename,result) \
  do { \
    const char *__FILE_EXTENSIONS[] = {"",".exe",".com",".bat"}; \
    \
    int         __z; \
    char        __tmpFilename[MAX_PATH]; \
    struct stat __statBuffer; \
    \
    __z = 0; \
    do { \
      strncpy(__tmpFilename,filename,MAX_PATH); \
      strncat(__tmpFilename,__FILE_EXTENSIONS[__z],MAX_PATH-strlen(__tmpFilename)); \
      result = ((stat(__tmpFilename,&__statBuffer)==0) && (S_ISREG(__statBuffer.st_mode)))?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      __z++; \
    } while ((result == TARGET_NATIVE_ERROR) && (__z<4)); \
  } while (0)

#define TARGET_NATIVE_FILE_MAKE_DIR(name,result) \
  do { \
    result=((mkdir(name)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR); \
  } while (0)

#define TARGET_NATIVE_FILE_FSYNC(filedescriptor,result) \
  do { \
    result=TARGET_NATIVE_OK; \
  } while (0)

#define TARGET_NATIVE_FILE_LOCK(filedescriptor,mode,offset,length,wait,result) \
  do { \
    result=TARGET_NATIVE_ERROR; \
  } while (0)
#define TARGET_NATIVE_FILE_UNLOCK(filedescriptor,offset,length,result) \
  do { \
    result=TARGET_NATIVE_ERROR; \
  } while (0)

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

/* include rest of definitions from generic file (do not move it to 
   another position!) */
#include "target_generic_file.h"

#endif /* __TARGET_NATIVE_FILE__ */

/* end of file */
