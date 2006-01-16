/* target_generic_file - Native methods for file operations
   Copyright (C) 1998, 2004 Free Software Foundation, Inc.

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
Description: generic target defintions of file functions
Systems    : all
*/

#ifndef __TARGET_GENERIC_FILE__
#define __TARGET_GENERIC_FILE__

/* check if target_native_file.h included */
#ifndef __TARGET_NATIVE_FILE__
  #error Do NOT INCLUDE generic target files! Include the corresponding native target files instead!
#endif

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>

#include "target_native.h"
#include "target_native_math.h"

/* needed for O_* flags */
#ifdef HAVE_FCNTL_H
  #include <fcntl.h>
#endif

#ifdef NEW_CP
#include "../posix/target_posix_file.h"
#endif

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/
#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_STDIN
  #define TARGET_NATIVE_FILE_STDIN 0
#endif
#ifndef TARGET_NATIVE_FILE_STDOUT
  #define TARGET_NATIVE_FILE_STDOUT 1
#endif
#ifndef TARGET_NATIVE_FILE_STDERR
  #define TARGET_NATIVE_FILE_STDERR 2
#endif

#ifndef TARGET_NATIVE_FILE_FILEFLAG_NONE
  #define TARGET_NATIVE_FILE_FILEFLAG_NONE 0
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_CREATE
  #define TARGET_NATIVE_FILE_FILEFLAG_CREATE O_CREAT
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_CREATE_FORCE
  #define TARGET_NATIVE_FILE_FILEFLAG_CREATE_FORCE (O_CREAT|O_EXCL)
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_READ
  #define TARGET_NATIVE_FILE_FILEFLAG_READ O_RDONLY
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_WRITE
  #define TARGET_NATIVE_FILE_FILEFLAG_WRITE O_WRONLY
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_READWRITE
  #define TARGET_NATIVE_FILE_FILEFLAG_READWRITE O_RDWR
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_TRUNCATE
  #define TARGET_NATIVE_FILE_FILEFLAG_TRUNCATE O_TRUNC
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_APPEND
  #define TARGET_NATIVE_FILE_FILEFLAG_APPEND O_APPEND
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_SYNC
  #if !defined (O_SYNC) && defined (O_FSYNC)
    #define TARGET_NATIVE_FILE_FILEFLAG_SYNC O_FSYNC
  #else
    #define TARGET_NATIVE_FILE_FILEFLAG_SYNC O_SYNC
  #endif
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_DSYNC
  #ifdef O_DSYNC
    #define TARGET_NATIVE_FILE_FILEFLAG_DSYNC O_DSYNC
  #else
    #define TARGET_NATIVE_FILE_FILEFLAG_DSYNC TARGET_NATIVE_FILE_FILEFLAG_SYNC
  #endif
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_BINARY
  #ifdef O_BINARY
    #define TARGET_NATIVE_FILE_FILEFLAG_BINARY O_BINARY
  #else
    #define TARGET_NATIVE_FILE_FILEFLAG_BINARY 0
  #endif
#endif

#ifndef TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL
  #define TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL (S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH)
#endif

#ifndef TARGET_NATIVE_FILE_FILEPERMISSION_PRIVATE 
  #define TARGET_NATIVE_FILE_FILEPERMISSION_PRIVATE (S_IRUSR | S_IWUSR)
#endif

#ifndef TARGET_NATIVE_FILE_FILEPERMISSION_READONLY
  #define TARGET_NATIVE_FILE_FILEPERMISSION_READONLY (~(S_IWRITE|S_IWGRP|S_IWOTH))
#endif

#ifndef TARGET_NATIVE_FILE_LOCKMODE_READ
  #ifdef HAVE_F_RDLCK
    #define TARGET_NATIVE_FILE_LOCKMODE_READ F_RDLCK
  #else
    #define TARGET_NATIVE_FILE_LOCKMODE_READ 0
  #endif
#endif
#ifndef TARGET_NATIVE_FILE_LOCKMODE_WRITE
  #ifdef HAVE_F_WRLCK
    #define TARGET_NATIVE_FILE_LOCKMODE_WRITE F_WRLCK
  #else
    #define TARGET_NATIVE_FILE_LOCKMODE_WRITE 0
  #endif
#endif

#else  /* NEW_CP */

#ifndef TARGET_NATIVE_FILE_STDIN
  #define TARGET_NATIVE_FILE_STDIN CP_FILE_STDIN
#endif
#ifndef TARGET_NATIVE_FILE_STDOUT
  #define TARGET_NATIVE_FILE_STDOUT CP_FILE_STDOUT
#endif
#ifndef TARGET_NATIVE_FILE_STDERR
  #define TARGET_NATIVE_FILE_STDERR CP_FILE_STDERR
#endif

#ifndef TARGET_NATIVE_FILE_FILEFLAG_NONE
  #define TARGET_NATIVE_FILE_FILEFLAG_NONE CP_FILE_FILEFLAG_NONE
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_CREATE
  #define TARGET_NATIVE_FILE_FILEFLAG_CREATE CP_FILE_FILEFLAG_CREATE
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_CREATE_FORCE
  #define TARGET_NATIVE_FILE_FILEFLAG_CREATE_FORCE CP_FILE_FILEFLAG_CREATE_FORCE
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_READ
  #define TARGET_NATIVE_FILE_FILEFLAG_READ CP_FILE_FILEFLAG_READ
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_WRITE
  #define TARGET_NATIVE_FILE_FILEFLAG_WRITE CP_FILE_FILEFLAG_WRITE
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_READWRITE
  #define TARGET_NATIVE_FILE_FILEFLAG_READWRITE CP_FILE_FILEFLAG_READWRITE
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_TRUNCATE
  #define TARGET_NATIVE_FILE_FILEFLAG_TRUNCATE CP_FILE_FILEFLAG_TRUNCATE
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_APPEND
  #define TARGET_NATIVE_FILE_FILEFLAG_APPEND CP_FILE_FILEFLAG_APPEND
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_SYNC
  #define TARGET_NATIVE_FILE_FILEFLAG_SYNC CP_FILE_FILEFLAG_SYNC
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_DSYNC
  #define TARGET_NATIVE_FILE_FILEFLAG_DSYNC CP_FILE_FILEFLAG_DSYNC
#endif
#ifndef TARGET_NATIVE_FILE_FILEFLAG_BINARY
  #define TARGET_NATIVE_FILE_FILEFLAG_BINARY CP_FILE_FILEFLAG_BINARY
#endif

#ifndef TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL
  #define TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL CP_FILE_FILEPERMISSION_NORMAL
#endif

#ifndef TARGET_NATIVE_FILE_FILEPERMISSION_PRIVATE 
  #define TARGET_NATIVE_FILE_FILEPERMISSION_PRIVATE CP_FILE_FILEPERMISSION_PRIVATE
#endif

#ifndef TARGET_NATIVE_FILE_FILEPERMISSION_READONLY
  #define TARGET_NATIVE_FILE_FILEPERMISSION_READONLY CP_FILE_FILEPERMISSION_READONLY
#endif

#ifndef TARGET_NATIVE_FILE_LOCKMODE_READ
  #define TARGET_NATIVE_FILE_LOCKMODE_READ CP_FILE_LOCKMODE_READ
#endif
#ifndef TARGET_NATIVE_FILE_LOCKMODE_WRITE
  #define TARGET_NATIVE_FILE_LOCKMODE_WRITE CP_FILE_LOCKMODE_WRITE
#endif

#endif /* NEW_CP */

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN
* Purpose    : open a file
* Input      : filename    - filename
*              flags       - file open flags
*              permissions - file permissions
* Output     : filedescriptor - file descriptor
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : file is created if it does not exist
\***********************************************************************/
#include <stdio.h>
#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_OPEN
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <sys/stat.h>
    #ifdef HAVE_FCNTL_H
      #include <fcntl.h>
    #endif
    #if defined(HAVE_FCNTL) && defined(FD_CLOEXEC_IN_FCNTL_H)
      #define TARGET_NATIVE_FILE_OPEN(filename,filedescriptor,flags,permissions,result) \
        do { \
          filedescriptor=open(filename, \
                              flags, \
                              permissions \
                              ); \
          if (filedescriptor >= 0) \
          { \
            fcntl(filedescriptor,F_SETFD,FD_CLOEXEC); \
          } \
          result=(filedescriptor>=0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } while (0)
    #else
      #define TARGET_NATIVE_FILE_OPEN(filename,filedescriptor,flags,permissions,result) \
        do { \
          filedescriptor=open(filename, \
                              flags, \
                              permissions \
                              ); \
          result=(filedescriptor>=0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } while (0)
    #endif
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_OPEN(filename,filedescriptor,flags,permissions,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_OPEN
  #define TARGET_NATIVE_FILE_OPEN(filename,filedescriptor,flags,permissions,result) \
    CP_FILE_OPEN(filename,filedescriptor,flags,permissions,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN_CREATE
* Purpose    : create a file and open for writing
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : file is created if it does not exist
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_OPEN_CREATE
  #ifndef WITHOUT_FILESYSTEM
    #define TARGET_NATIVE_FILE_OPEN_CREATE(filename,filedescriptor,result) \
      TARGET_NATIVE_FILE_OPEN(filename,\
                              filedescriptor,\
                              TARGET_NATIVE_FILE_FILEFLAG_WRITE|TARGET_NATIVE_FILE_FILEFLAG_CREATE_FORCE|TARGET_NATIVE_FILE_FILEFLAG_BINARY, \
                              TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL, \
                              result \
                             )
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_OPEN_CREATE(filename,filedescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_OPEN_CREATE
  #define TARGET_NATIVE_FILE_OPEN_CREATE(filename,filedescriptor,result) \
    CP_FILE_OPEN_CREATE(filename,filedescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN_READ
* Purpose    : open an existing file for reading
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_OPEN_READ
  #ifndef WITHOUT_FILESYSTEM
    #define TARGET_NATIVE_FILE_OPEN_READ(filename,filedescriptor,result) \
      TARGET_NATIVE_FILE_OPEN(filename, \
                              filedescriptor,\
                              TARGET_NATIVE_FILE_FILEFLAG_READ|TARGET_NATIVE_FILE_FILEFLAG_BINARY, \
                              TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL, \
                              result \
                             )
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_OPEN_READ(filename,filedescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
 #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_OPEN_READ
  #define TARGET_NATIVE_FILE_OPEN_READ(filename,filedescriptor,result) \
    CP_FILE_OPEN_READ(filename,filedescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN_WRITE
* Purpose    : open an existing file for writing
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_OPEN_WRITE
  #ifndef WITHOUT_FILESYSTEM
    #define TARGET_NATIVE_FILE_OPEN_WRITE(filename,filedescriptor,result) \
      TARGET_NATIVE_FILE_OPEN(filename, \
                              filedescriptor, \
                              TARGET_NATIVE_FILE_FILEFLAG_WRITE|TARGET_NATIVE_FILE_FILEFLAG_BINARY, \
                              TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL, \
                              result \
                             )
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_OPEN_WRITE(filename,filedescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_OPEN_WRITE
  #define TARGET_NATIVE_FILE_OPEN_WRITE(filename,filedescriptor,result) \
    CP_FILE_OPEN_WRITE(filename,filedescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN_READWRITE
* Purpose    : create/open a file for reading/writing
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : file is created if it does not exist
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_OPEN_READWRITE
  #ifndef WITHOUT_FILESYSTEM
    #define TARGET_NATIVE_FILE_OPEN_READWRITE(filename,filedescriptor,result) \
      TARGET_NATIVE_FILE_OPEN(filename, \
                              filedescriptor, \
                              TARGET_NATIVE_FILE_FILEFLAG_READWRITE|TARGET_NATIVE_FILE_FILEFLAG_BINARY, \
                              TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL, \
                              result \
                             )
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_OPEN_READWRITE(filename,filedescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_OPEN_READWRITE
  #define TARGET_NATIVE_FILE_OPEN_READWRITE(filename,filedescriptor,result) \
    CP_FILE_OPEN_READWRITE(filename,filedescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN_READWRITE
* Purpose    : create/open a file for append
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : file is created if it does not exist
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_OPEN_APPEND
  #ifndef WITHOUT_FILESYSTEM
    #define TARGET_NATIVE_FILE_OPEN_APPEND(filename,filedescriptor,result) \
      TARGET_NATIVE_FILE_OPEN(filename, \
                              filedescriptor, \
                              TARGET_NATIVE_FILE_FILEFLAG_CREATE|TARGET_NATIVE_FILE_FILEFLAG_WRITE|TARGET_NATIVE_FILE_FILEFLAG_APPEND|TARGET_NATIVE_FILE_FILEFLAG_BINARY, \
                              TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL, \
                              result \
                             )
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_OPEN_APPEND(filename,filedescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_OPEN_APPEND
  #define TARGET_NATIVE_FILE_OPEN_APPEND(filename,filedescriptor,result) \
    CP_FILE_OPEN_APPEND(filename,filedescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_CLOSE
* Purpose    : close a file
* Input      : filedescriptor - file descriptor
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_CLOSE
  #ifndef WITHOUT_FILESYSTEM
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_CLOSE(filedescriptor,result) \
      do  { \
        result=(close(filedescriptor)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
     } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_CLOSE(filedescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_CLOSE
  #define TARGET_NATIVE_FILE_CLOSE(filedescriptor,result) \
    CP_FILE_CLOSE(filedescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR
* Purpose    : check if file-descriptor is valid
* Input      : filedescriptor - file descriptor
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR
  #ifndef WITHOUT_FILESYSTEM
    #if   defined(HAVE_FCNTL)
      #include <unistd.h>
      #include <fcntl.h>
      #define TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR(filedescriptor,result) \
        do { \
          result=(fcntl(filedescriptor,F_GETFL,0)!=-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } while(0)
    #elif defined(HAVE_FSTAT)
      #include <sys/types.h>
      #include <sys/stat.h>
      #include <unistd.h>
      #define TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR(filedescriptor,result) \
        do { \
          struct stat __stat; \
          \
          result=(fstat(filedescriptor,&__stat)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } while(0)
    #else
      #error fcntl() nor fstat() available for checking if file descriptor is valid. 
    #endif
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR(filedescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR
  #define TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR(filedescriptor,result) \
    CP_FILE_VALID_FILE_DESCRIPTOR(filedescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_TELL
* Purpose    : get current file position
* Input      : filedescriptor - file descriptor
* Output     : tell   - position
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_TELL
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_TELL(filedescriptor,offset,result) \
      do { \
        offset=lseek(filedescriptor,0,SEEK_CUR); \
        result=((offset)!=(off_t)-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_TELL(filedescriptor,offset,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_TELL
  #define TARGET_NATIVE_FILE_TELL(filedescriptor,offset,result) \
    CP_FILE_TELL(filedescriptor,offset,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_SEEK_BEGIN|CURRENT|END
* Purpose    : set file position relativ to begin/current/end
* Input      : filedescriptor - file descriptor
*              offset         - file position (0..n)
* Output     : newoffset - new file position (0..n)
*              result    - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_SEEK_BEGIN
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_SEEK_BEGIN(filedescriptor,offset,newoffset,result) \
      do { \
        newoffset=lseek(filedescriptor,offset,SEEK_SET); \
        result=((newoffset)!=(off_t)-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_SEEK_BEGIN(filedescriptor,offset,newoffset,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#ifndef TARGET_NATIVE_FILE_SEEK_CURRENT
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_SEEK_CURRENT(filedescriptor,offset,newoffset,result) \
      do { \
        newoffset=lseek(filedescriptor,offset,SEEK_CUR); \
        result=((newoffset)!=(off_t)-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_SEEK_CURRENT(filedescriptor,offset,newoffset,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#ifndef TARGET_NATIVE_FILE_SEEK_END
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_SEEK_END(filedescriptor,offset,newoffset,result) \
      do { \
        newoffset=lseek(filedescriptor,offset,SEEK_END); \
        result=((newoffset)!=(off_t)-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_SEEK_END(filedescriptor,offset,newoffset,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_SEEK_BEGIN
  #define TARGET_NATIVE_FILE_SEEK_BEGIN(filedescriptor,offset,newoffset,result) \
    CP_FILE_SEEK_BEGIN(filedescriptor,offset,newoffset,result)
#endif
#ifndef TARGET_NATIVE_FILE_SEEK_CURRENT
  #define TARGET_NATIVE_FILE_SEEK_CURRENT(filedescriptor,offset,newoffset,result) \
    CP_FILE_SEEK_CURRENT(filedescriptor,offset,newoffset,result)
#endif
#ifndef TARGET_NATIVE_FILE_SEEK_END
  #define TARGET_NATIVE_FILE_SEEK_END(filedescriptor,offset,newoffset,result) \
    CP_FILE_SEEK_END(filedescriptor,offset,newoffset,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_TRUNCATE
* Purpose    : truncate a file
* Input      : filedescriptor - file descriptor
*              offset         - offset for truncate
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_TRUNCATE
  #ifndef WITHOUT_FILESYSTEM
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_TRUNCATE(filedescriptor,offset,result) \
      do { \
        result=(ftruncate(filedescriptor,offset)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_TRUNCATE(filedescriptor,offset,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_TRUNCATE
  #define TARGET_NATIVE_FILE_TRUNCATE(filedescriptor,offset,result) \
    CP_FILE_TRUNCATE(filedescriptor,offset,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_SIZE
* Purpose    : get size of file (in bytes)
* Input      : filedescriptor - file descriptor
* Output     : length - file size
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_SIZE
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_SIZE(filedescriptor,length,result) \
      do { \
        struct stat __statBuffer; \
        \
        result=(fstat(filedescriptor,&__statBuffer)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        length=TARGET_NATIVE_MATH_INT_INT32_TO_INT64(__statBuffer.st_size); \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_SIZE(filedescriptor,length,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_SIZE
  #define TARGET_NATIVE_FILE_SIZE(filedescriptor,length,result) \
    CP_FILE_SIZE(filedescriptor,length,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_AVAILABLE
* Purpose    : get available bytes for read
* Input      : filedescriptor - file descriptor
* Output     : length - available bytes for read
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : Hint: 4 different methods are available
*               - ioctl FIONREAD
*               - fstat()+select() (get number of available bytes for
*                 files and 0/1 for block/character devices)
*               - fstat() (get number of available bytes for files, 0
*                 otherwise)
*               - select() (return 0/1 only)
*              Depending on the configuration the best available method
*              is selected (do not rearrange the if-elif-blocks!)
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_AVAILABLE
  #ifndef WITHOUT_FILESYSTEM
    #if   defined(HAVE_FIONREAD) || defined(FIONREAD_IN_SYS_IOCTL_H) || defined(FIONREAD_IN_SYS_FILIO_H)
      #ifdef HAVE_FIONREAD
        #ifdef HAVE_SYS_IOCTL_H
          #define BSD_COMP /* Get FIONREAD on Solaris2 */
          #include <sys/ioctl.h>
        #endif
        #ifdef HAVE_SYS_FILIO_H /* Get FIONREAD on Solaris 2.5 */
          #include <sys/filio.h>
        #endif
      #else
        #if   defined(FIONREAD_IN_SYS_IOCTL_H)
          #include <sys/ioctl.h>
        #elif defined(FIONREAD_IN_SYS_FILIO_H)
          #include <sys/types.h>
          #include <sys/filio.h>
        #endif
      #endif
      #define TARGET_NATIVE_FILE_AVAILABLE(filedescriptor,length,result) \
        do { \
          ssize_t __n; \
          \
          result=(ioctl(filedescriptor,FIONREAD,(char*)&__n)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
          length=TARGET_NATIVE_MATH_INT_INT32_TO_INT64(__n); \
        } while (0)
    #elif defined(HAVE_FSTAT) && defined(HAVE_SELECT)
      #include <sys/types.h>
      #include <sys/stat.h>
      #include <unistd.h>
      #include <string.h>
      #include <sys/select.h>
      #define TARGET_NATIVE_FILE_AVAILABLE(filedescriptor,length,result) \
        do { \
          struct stat    __statBuffer; \
          off_t          __n; \
          fd_set         __filedescriptset; \
          struct timeval __timeval; \
          \
          length=TARGET_NATIVE_MATH_INT_INT64_CONST_0; \
          \
          if   ((fstat(filedescriptor,&__statBuffer)==0) && S_ISREG(__statBuffer.st_mode)) \
          { \
            __n=(lseek(filedescriptor,0,SEEK_CUR)); \
            if (__n!=-1) \
            { \
              length=TARGET_NATIVE_MATH_INT_INT32_TO_INT64(__statBuffer.st_size-__n); \
              result=TARGET_NATIVE_OK; \
            } \
            else \
            { \
              result=TARGET_NATIVE_ERROR; \
            } \
          } \
          else \
          { \
            FD_ZERO(&__filedescriptset); \
            FD_SET(filedescriptor,&__filedescriptset); \
            memset(&__timeval,0,sizeof(__timeval)); \
            /* Hint: __filedescriptset is a set of bits, thus the size of the used set
                     elements is the last element+1, e. g. filescriptor is 3, then
                     __filedescriptset is 0001000... thus...
                                          |--| size is 4 (not 3!)
            */ \
            switch (select(filedescriptor+1,&__filedescriptset,NULL,NULL,&__timeval)==0) \
            { \
              case -1:                                              result=TARGET_NATIVE_ERROR; break; \
              case  0: length=TARGET_NATIVE_MATH_INT_INT64_CONST_0; result=TARGET_NATIVE_OK;    break; \
              default: length=TARGET_NATIVE_MATH_INT_INT64_CONST_1; result=TARGET_NATIVE_OK;    break; \
            } \
          } \
        } while (0)
    #elif defined(HAVE_FSTAT)
      #include <sys/types.h>
      #include <sys/stat.h>
      #include <unistd.h>
      #define TARGET_NATIVE_FILE_AVAILABLE(filedescriptor,length,result) \
        do { \
          struct stat __statBuffer; \
          off_t       __n; \
          \
          length=TARGET_NATIVE_MATH_INT_INT64_CONST_0; \
          \
          if ((fstat(filedescriptor,&__statBuffer)==0) && S_ISREG(__statBuffer.st_mode)) \
          { \
            __n=(lseek(filedescriptor,0,SEEK_CUR)); \
            if (__n!=-1) \
            { \
              length=TARGET_NATIVE_MATH_INT_INT32_TO_INT64(__statBuffer.st_size-__n); \
              result=TARGET_NATIVE_OK; \
            } \
            else \
            { \
              result=TARGET_NATIVE_ERROR; \
            } \
          } \
          else \
          { \
            result=TARGET_NATIVE_ERROR; \
          } \
        } while (0)
    #elif defined(HAVE_SELECT)
      #include <string.h>
      #include <sys/select.h>
      #define TARGET_NATIVE_FILE_AVAILABLE(filedescriptor,length,result) \
        do { \
          fd_set         __filedescriptset; \
          struct timeval __timeval; \
          \
          length=TARGET_NATIVE_MATH_INT_INT64_CONST_0; \
          \
          FD_ZERO(&__filedescriptset); \
          FD_SET(filedescriptor,&__filedescriptset); \
          memset(&__timeval,0,sizeof(__timeval)); \
          /* Hint: __filedescriptset is a set of bits, thus the size of the used set
                   elements is the last element+1, e. g. filescriptor is 3, then
                   __filedescriptset is 0001000... thus...
                                        |--| size is 4 (not 3!)
          */ \
          switch (select(filedescriptor+1,&__filedescriptset,NULL,NULL,&__timeval)==0) \
          { \
            case -1:                                              result=TARGET_NATIVE_ERROR; break; \
            case  0: length=TARGET_NATIVE_MATH_INT_INT64_CONST_0; result=TARGET_NATIVE_OK;    break; \
            default: length=TARGET_NATIVE_MATH_INT_INT64_CONST_1; result=TARGET_NATIVE_OK;    break; \
          } \
        } while (0)
    #else
      #ifndef WITHOUT_FILESYSTEM
        #error No suitable implementation for TARGET_NATIVE_FILE_AVAILABLE - please check configuration!
      #else
        #define TARGET_NATIVE_FILE_AVAILABLE(filedescriptor,length,result) \
          do { \
            errno=TARGET_NATIVE_ERROR_OPERATION_NOT_PERMITTED; \
            length=TARGET_NATIVE_MATH_INT_INT64_CONST_0; \
            result=TARGET_NATIVE_ERROR; \
          } while (0)
      #endif
    #endif
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_AVAILABLE(filedescriptor,length,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_AVAILABLE
  #define TARGET_NATIVE_FILE_AVAILABLE(filedescriptor,length,result) \
    CP_FILE_AVAILABLE(filedescriptor,length,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_READ
* Purpose    : read from file
* Input      : filedescriptor - file descriptor
*              length         - number of bytes to read
* Output     : buffer    - buffer with read bytes
*              bytesRead - number of bytes read/written
*              result    - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_READ
  #ifndef WITHOUT_FILESYSTEM
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_READ(filedescriptor,buffer,length,bytesRead,result) \
      do { \
        bytesRead=read(filedescriptor,buffer,length); \
        result=(bytesRead!=-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_READ(filedescriptor,buffer,length,bytesRead,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_WRITE
* Purpose    : write to file
* Input      : filedescriptor - file descriptor
*              buffer         - buffer with bytes to write
*              length         - number of bytes to write
* Output     : bytesWritten - number of bytes read/written
*              result       - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_WRITE
  #ifndef WITHOUT_FILESYSTEM
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_WRITE(filedescriptor,buffer,length,bytesWritten,result) \
      do { \
        bytesWritten=write(filedescriptor,buffer,length); \
        result=(bytesWritten!=-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_WRITE(filedescriptor,buffer,length,bytesWritten,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_READ
  #define TARGET_NATIVE_FILE_READ(filedescriptor,buffer,length,bytesRead,result) \
    CP_FILE_READ(filedescriptor,buffer,length,bytesRead,result)
#endif
#ifndef TARGET_NATIVE_FILE_WRITE
  #define TARGET_NATIVE_FILE_WRITE(filedescriptor,buffer,length,bytesWritten,result) \
    CP_FILE_WRITE(filedescriptor,buffer,length,bytesWritten,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_SET_MODE_READONLY
* Purpose    : set file mode to read-only
* Input      : filename - filename
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_SET_MODE_READONLY
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_SET_MODE_READONLY(filename,result) \
      do { \
        struct stat __statBuffer; \
        \
        if (stat(filename,&__statBuffer)==0) { \
          result=(chmod(filename,__statBuffer.st_mode & TARGET_NATIVE_FILE_FILEPERMISSION_READONLY)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } else { \
          result=TARGET_NATIVE_ERROR; \
        } \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_SET_MODE_READONLY(filename,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_SET_MODE_READONLY
  #define TARGET_NATIVE_FILE_SET_MODE_READONLY(filename,result) \
    CP_FILE_SET_MODE_READONLY(filename,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_EXISTS
* Purpose    : check if file exists
* Input      : filename - filename
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_EXISTS
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_EXISTS(filename,result) \
      do { \
        struct stat __statBuffer; \
        \
        result=(stat(filename,&__statBuffer)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_EXISTS(filename,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_EXISTS
  #define TARGET_NATIVE_FILE_EXISTS(filename,result) \
    CP_FILE_EXISTS(filename,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_IS_FILE
* Purpose    : check if directory entry is a file
* Input      : filename - filename
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_IS_FILE
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_IS_FILE(filename,result) \
      do { \
        struct stat __statBuffer; \
        \
        result=((stat(filename,&__statBuffer)==0) && (S_ISREG(__statBuffer.st_mode)))?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
} while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_IS_FILE(filename,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_IS_FILE
  #define TARGET_NATIVE_FILE_IS_FILE(filename,result) \
    CP_FILE_IS_FILE(filename,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_IS_DIRECTORY
* Purpose    : check if directory entry is a directory
* Input      : filename - filename
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_IS_DIRECTORY
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_IS_DIRECTORY(filename,result) \
      do { \
        struct stat __statBuffer; \
        \
        result=((stat(filename,&__statBuffer)==0) && (S_ISDIR(__statBuffer.st_mode)))?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_IS_DIRECTORY(filename,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_IS_DIRECTORY
  #define TARGET_NATIVE_FILE_IS_DIRECTORY(filename,result) \
    CP_FILE_IS_DIRECTORY(filename,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_IS_EXECUTABLE
* Purpose    : check if directory entry is an executable (in path)
* Input      : filename - filename
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

// NYI: CLEANUP: CHECK
#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_IS_EXECUTABLE
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_IS_EXECUTABLE(filename,result) \
      do { \
        struct stat __statBuffer; \
        \
        result=((stat(filename,&__statBuffer)==0) && (S_ISREG(__statBuffer.st_mode)))?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_IS_EXECUTABLE(filename,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_IS_EXECUTABLE
  #define TARGET_NATIVE_FILE_IS_EXECUTABLE(filename,result) \
    CP_FILE_IS_EXECUTABLE(filename,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_GET_LAST_MODIFIED
* Purpose    : get last modification time of file (milliseconds)
* Input      : filename - filename
* Output     : time   - time [ms]
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_GET_LAST_MODIFIED
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_GET_LAST_MODIFIED(filename,time,result) \
      do { \
        struct stat __statBuffer; \
        \
        time=TARGET_NATIVE_MATH_INT_INT64_CONST_0; \
        if (stat(filename,&__statBuffer)==0) { \
          time=TARGET_NATIVE_MATH_INT_INT64_MUL(TARGET_NATIVE_MATH_INT_INT32_TO_INT64(__statBuffer.st_mtime), \
                                                TARGET_NATIVE_MATH_INT_INT32_TO_INT64(1000) \
                                               ); \
          result=TARGET_NATIVE_OK; \
        } else { \
          result=TARGET_NATIVE_ERROR; \
        } \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_GET_LAST_MODIFIED(filename,time,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_GET_LAST_MODIFIED
  #define TARGET_NATIVE_FILE_GET_LAST_MODIFIED(filename,time,result) \
    CP_FILE_GET_LAST_MODIFIED(filename,time,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_SET_LAST_MODIFIED
* Purpose    : set last modification time of file (milliseconds)
* Input      : filename - filename
*              time     - time [ms]
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_SET_LAST_MODIFIED
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    #ifdef HAVE_UTIME_H
      #include <utime.h>
    #elif HAVE_SYS_UTIME_H
      #include <sys/utime.h>
    #else
      #error utime.h not found. Please check configuration.
    #endif
    #define TARGET_NATIVE_FILE_SET_LAST_MODIFIED(filename,time,result) \
      do { \
        struct stat    __statBuffer; \
        struct utimbuf __utimeBuffer; \
        \
        if (stat(filename,&__statBuffer)==0) { \
          __utimeBuffer.actime =__statBuffer.st_atime; \
          __utimeBuffer.modtime=TARGET_NATIVE_MATH_INT_INT64_TO_INT32(TARGET_NATIVE_MATH_INT_INT64_DIV(time, \
                                                                                                       TARGET_NATIVE_MATH_INT_INT32_TO_INT64(1000) \
                                                                                                      ) \
                                                                ); \
          result=(utime(filename,&__utimeBuffer)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } else { \
          result=TARGET_NATIVE_ERROR; \
        } \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_SET_LAST_MODIFIED(filename,time,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_SET_LAST_MODIFIED
  #define TARGET_NATIVE_FILE_SET_LAST_MODIFIED(filename,time,result) \
    CP_FILE_SET_LAST_MODIFIED(filename,time,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_DELETE
* Purpose    : delete a file, link or directory
* Input      : filename - filename
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_DELETE
  #ifndef WITHOUT_FILESYSTEM
    #define TARGET_NATIVE_FILE_DELETE(filename,result) \
      do { \
        result=((unlink(filename)==0) || (rmdir(filename)==0))?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_DELETE(filename,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_DELETE
  #define TARGET_NATIVE_FILE_DELETE(filename,result) \
    CP_FILE_DELETE(filename,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_RENAME
* Purpose    : delete a file, link or directory
* Input      : oldfilename - old filename
*              newfilename - new filename
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_RENAME
  #ifndef WITHOUT_FILESYSTEM
    #define TARGET_NATIVE_FILE_RENAME(oldfilename,newfilename,result) \
      do { \
        result=(rename(oldfilename,newfilename)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_RENAME(oldfilename,newfilename,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_RENAME
  #define TARGET_NATIVE_FILE_RENAME(oldfilename,newfilename,result) \
    CP_FILE_RENAME(oldfilename,newfilename,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_MAKE_DIR
* Purpose    : create new directory
* Input      : name - directory name
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_MAKE_DIR
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/stat.h>
    #define TARGET_NATIVE_FILE_MAKE_DIR(name,result) \
      do { \
        result=((mkdir(name,(S_IRWXO|S_IRWXG|S_IRWXU))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR); \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_MAKE_DIR(name,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_MAKE_DIR
  #define TARGET_NATIVE_FILE_MAKE_DIR(name,result) \
    CP_FILE_MAKE_DIR(name,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_GET_CWD
* Purpose    : get current working directory
* Input      : maxPathLength - max. length of path
* Output     : path   - path
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_GET_CWD
  #ifndef WITHOUT_FILESYSTEM
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_GET_CWD(path,maxPathLength,result) \
      do {\
        result=(getcwd(path,maxPathLength)!=NULL)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_GET_CWD(path,maxPathLength,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_GET_CWD
  #define TARGET_NATIVE_FILE_GET_CWD(path,maxPathLength,result) \
    CP_FILE_GET_CWD(path,maxPathLength,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_CHANGE_DIR
* Purpose    : change current working directory
* Input      : path - pathname
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_CHANGE_DIR
  #ifndef WITHOUT_FILESYSTEM
    #include <unistd.h>
    #define TARGET_NATIVE_FILE_CHANGE_DIR(path,result) \
      do {\
        result=(chdir(path)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_CHANGE_DIR(path,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_CHANGE_DIR
  #define TARGET_NATIVE_FILE_CHANGE_DIR(path,result) \
    CP_FILE_CHANGE_DIR(path,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN_DIR
* Purpose    : open directory for reading entries
* Input      : pathname - pathname
* Output     : handle - handle if not error, NULL otherwise
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_OPEN_DIR
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <dirent.h>
    #define TARGET_NATIVE_FILE_OPEN_DIR(pathname,handle,result) \
      do { \
        handle=(void*)opendir(pathname); \
        result=(handle!=NULL)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while(0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_OPEN_DIR(pathname,handle,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_OPEN_DIR
  #define TARGET_NATIVE_FILE_OPEN_DIR(pathname,handle,result) \
    CP_FILE_OPEN_DIR(pathname,handle,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_CLOSE_DIR
* Purpose    : close directory
* Input      : handle - directory handle
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_CLOSE_DIR
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <dirent.h>
    #define TARGET_NATIVE_FILE_CLOSE_DIR(handle,result) \
      do { \
        closedir((DIR*)handle); \
        result=TARGET_NATIVE_OK; \
      }  while(0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_CLOSE_DIR(handle,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_CLOSE_DIR
  #define TARGET_NATIVE_FILE_CLOSE_DIR(handle,result) \
    CP_FILE_CLOSE_DIR(handle,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_READ_DIR
* Purpose    : read next directory entry
* Input      : handle        - directory handle
*              maxNameLength - max. name length
* Output     : name   - name
*              result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
//NYI: FUTURE PROBLEM: name as buffer?
#ifndef TARGET_NATIVE_FILE_READ_DIR
  #ifndef WITHOUT_FILESYSTEM
    #include <sys/types.h>
    #include <dirent.h>
    #define TARGET_NATIVE_FILE_READ_DIR(handle,name,maxNameLength,result) \
      do { \
        struct dirent *__direntBuffer; \
        \
        __direntBuffer=readdir((DIR*)handle); \
        if (__direntBuffer!=NULL) { \
          strncpy(name,__direntBuffer->d_name,maxNameLength); \
          result=TARGET_NATIVE_OK; \
        } else { \
          result=TARGET_NATIVE_ERROR; \
        } \
      } while (0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_READ_DIR(handle,name,maxNameLength,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_READ_DIR
  #define TARGET_NATIVE_FILE_READ_DIR(handle,name,maxNameLength,result) \
    CP_FILE_READ_DIR(handle,name,maxNameLength,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_FSYNC
* Purpose    : do filesystem sync
* Input      : filedescriptor - file descriptor
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_FSYNC
  #ifndef WITHOUT_FILESYSTEM
    #define TARGET_NATIVE_FILE_FSYNC(filedescriptor,result) \
      do { \
        result=(fsync(filedescriptor)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } while(0)
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_FSYNC(filedescriptor,result) \
      do { \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_FSYNC
  #define TARGET_NATIVE_FILE_FSYNC(filedescriptor,result) \
    CP_FILE_FSYNC(filedescriptor,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_LOCK
* Purpose    : lock file
* Input      : filedescriptor - file descriptor
*              mode           - lock mode; see TARGET_NATIVE_FILE_LOCKMODE_*
*              offset         - offset (0..n)
*              length         - length (0..n)
*              wait           - TRUE for wait, FALSE otherwise
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_LOCK
  #ifndef WITHOUT_FILESYSTEM
    #ifdef HAVE_FCNTL
      #include <unistd.h>
      #include <fcntl.h>
      #define TARGET_NATIVE_FILE_LOCK(filedescriptor,mode,offset,length,wait,result) \
        do { \
          struct flock __flock; \
          \
          __flock.l_type  =(mode); \
          __flock.l_whence=SEEK_SET; \
          __flock.l_start =(off_t)(offset); \
          __flock.l_len   =(off_t)(length); \
          result=(fcntl(filedescriptor,wait?F_SETLKW:F_SETLK,&__flock)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } while(0)
    #else /* not HAVE_FCNTL */
      #ifdef CPP_HAS_WARNING
        #warning No suitable implementation for TARGET_NATIVE_FILE_LOCK() - ignored
      #endif /* CPP_HAS_WARNING */
      #define TARGET_NATIVE_FILE_LOCK(filedescriptor,mode,offset,length,wait,result) \
        do { \
          VARIABLE_UNUSED(filedescriptor); \
          VARIABLE_UNUSED(mode); \
          VARIABLE_UNUSED(offset); \
          VARIABLE_UNUSED(length); \
          VARIABLE_UNUSED(wait); \
          result=TARGET_NATIVE_OK; \
        } while (0)
    #endif /* HAVE_FCNTL */
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_LOCK(filedescriptor,mode,offset,length,wait,result) \
      do { \
        VARIABLE_UNUSED(filedescriptor); \
        VARIABLE_UNUSED(mode); \
        VARIABLE_UNUSED(offset); \
        VARIABLE_UNUSED(length); \
        VARIABLE_UNUSED(wait); \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_LOCK
  #define TARGET_NATIVE_FILE_LOCK(filedescriptor,mode,offset,length,wait,result) \
    CP_FILE_LOCK(filedescriptor,mode,offset,length,wait,result)
#endif
#endif /* NEW_CP */

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_UNLOCK
* Purpose    : do filesystem sync
* Input      : filedescriptor - file descriptor
*              offset         - offset (0..n)
*              length         - length (0..n)
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef NEW_CP
#ifndef TARGET_NATIVE_FILE_UNLOCK
  #ifndef WITHOUT_FILESYSTEM
    #ifdef HAVE_FCNTL
      #include <unistd.h>
      #include <fcntl.h>
      #define TARGET_NATIVE_FILE_UNLOCK(filedescriptor,offset,length,result) \
        do { \
          struct flock __flock; \
          \
          __flock.l_type  =F_UNLCK; \
          __flock.l_whence=SEEK_SET; \
          __flock.l_start =(off_t)(offset); \
          __flock.l_len   =(off_t)(length); \
          result=(fcntl(filedescriptor,F_SETLK,&__flock)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
        } while(0)
    #else /* not HAVE_FCNTL */
      #ifdef CPP_HAS_WARNING
        #warning No suitable implementation for TARGET_NATIVE_FILE_UNLOCK() - ignored
      #endif /* CPP_HAS_WARNING */
      #define TARGET_NATIVE_FILE_UNLOCK(filedescriptor,offset,length,result) \
        do { \
          VARIABLE_UNUSED(filedescriptor); \
          VARIABLE_UNUSED(offset); \
          VARIABLE_UNUSED(length); \
          result=TARGET_NATIVE_OK; \
        } while (0)
    #endif /* HAVE_FCNTL */
  #else /* not WITHOUT_FILESYSTEM */
    #define TARGET_NATIVE_FILE_UNLOCK(filedescriptor,offset,length,result) \
      do { \
        VARIABLE_UNUSED(filedescriptor); \
        VARIABLE_UNUSED(offset); \
        VARIABLE_UNUSED(length); \
        result=TARGET_NATIVE_ERROR; \
      } while (0)
  #endif /* WITHOUT_FILESYSTEM */
#endif
#else /* NEW_CP */
#ifndef TARGET_NATIVE_FILE_UNLOCK
  #define TARGET_NATIVE_FILE_UNLOCK(filedescriptor,offset,length,result) \
    CP_FILE_UNLOCK(filedescriptor,offset,length,result)
#endif
#endif /* NEW_CP */

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_GENERIC_FILE__ */

/* end of file */


