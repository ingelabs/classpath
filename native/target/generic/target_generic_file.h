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
Description: generic target defintions of file functions
Systems    : all
*/

#ifndef __TARGET_GENERIC_FILE__
#define __TARGET_GENERIC_FILE__

#ifdef __cplusplus
extern "C" {
#endif

/* check if target_native_file.h included */
#ifndef __TARGET_NATIVE_FILE__
  #error Do NOT INCLUDE generic target files! Include the corresponding native target files instead!
#endif

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>

#include "target_native.h"
#include "target_native_math_int.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN_READ
* Purpose    : open an existing file for reading
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_OPEN_READ
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <fcntl.h>
  #define TARGET_NATIVE_FILE_OPEN_READ(filename,filedescriptor,result) \
    do { \
      filedescriptor=open(filename,O_RDONLY); \
      result=(filedescriptor>=0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
   } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN_READWRITE
* Purpose    : create/open a file for reading/writing
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : file is created if it does not exist
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_OPEN_READWRITE
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <fcntl.h>
  #define TARGET_NATIVE_FILE_OPEN_READWRITE(filename,filedescriptor,result) \
    do { \
      filedescriptor=open(filename,O_CREAT|O_RDWR,0666); \
      result=(filedescriptor>=0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
   } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_CLOSE
* Purpose    : close a file
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_CLOSE
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_CLOSE(filedescriptor,result) \
    do  { \
      result=(close(filedescriptor)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
   } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR
* Purpose    : check if file-descriptor is valid
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR
  #define TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR(filedescriptor,result) \
    do { \
      result=(fcntl(filedescriptor,F_GETFL,0)!=-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while(0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_TELL
* Purpose    : get current file position
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_TELL
  #include <sys/types.h>
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_TELL(filedescriptor,offset,result) \
    do { \
      offset=lseek(filedescriptor,TARGET_NATIVE_MATH_INT_INT64_CONST_0,SEEK_CUR); \
      result=((offset)!=(off_t)-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_SEEK_BEGIN|CURRENT|END
* Purpose    : set file position relativ to begin/current/end
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_SEEK_BEGIN
  #include <sys/types.h>
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_SEEK_BEGIN(filedescriptor,offset,newoffset,result) \
    do { \
      newoffset=lseek(filedescriptor,offset,SEEK_SET); \
      result=((newoffset)!=(off_t)-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif
#ifndef TARGET_NATIVE_FILE_SEEK_CURRENT
  #include <sys/types.h>
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_SEEK_CURRENT(filedescriptor,offset,newoffset,result) \
    do { \
      newoffset=lseek(filedescriptor,offset,SEEK_CUR); \
      result=((newoffset)!=(off_t)-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif
#ifndef TARGET_NATIVE_FILE_SEEK_END
  #include <sys/types.h>
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_SEEK_END(filedescriptor,offset,newoffset,result) \
    do { \
      newoffset=lseek(filedescriptor,offset,SEEK_END); \
      result=((newoffset)!=(off_t)-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_TRUNCATE
* Purpose    : truncate a file
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_TRUNCATE
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_TRUNCATE(filedescriptor,offset,result) \
    do { \
      result=(ftruncate(filedescriptor,offset)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_SIZE
* Purpose    : get size of file (in bytes)
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_SIZE
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
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_READ|WRITE
* Purpose    : read/write from/to frile
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_READ
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_READ(filedescriptor,buffer,length,bytesRead,result) \
    do { \
      bytesRead=read(filedescriptor,buffer,length); \
      result=(bytesRead!=-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif
#ifndef TARGET_NATIVE_FILE_WRITE
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_WRITE(filedescriptor,buffer,length,bytesWritten,result) \
    do { \
      bytesWritten=write(filedescriptor,buffer,length); \
      result=(bytesWritten!=-1)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_SET_MODE_READONLY
* Purpose    : set file mode to read-only
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_SET_MODE_READONLY
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_SET_MODE_READONLY(filename,result) \
    do { \
      struct stat __statBuffer; \
      \
      if (stat(filename,&__statBuffer)==0) { \
        result=(chmod(filename,__statBuffer.st_mode & (~(S_IWRITE|S_IWGRP|S_IWOTH)))==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
      } else { \
        result=TARGET_NATIVE_ERROR; \
      } \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_EXISTS
* Purpose    : check if file exists
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_EXISTS
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_EXISTS(filename,result) \
    do { \
      struct stat __statBuffer; \
      \
      result=(stat(filename,&__statBuffer)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_IS_FILE
* Purpose    : check if directory entry is a file
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_IS_FILE
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_IS_FILE(filename,result) \
    do { \
      struct stat __statBuffer; \
      \
      result=((lstat(filename,&__statBuffer)==0) && (S_ISREG(__statBuffer.st_mode)))?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_IS_DIRECTORY
* Purpose    : check if directory entry is a directory
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_IS_DIRECTORY
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_IS_DIRECTORY(filename,result) \
    do { \
      struct stat __statBuffer; \
      \
      result=((lstat(filename,&__statBuffer)==0) && (S_ISDIR(__statBuffer.st_mode)))?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_GET_LAST_MODIFIED
* Purpose    : get last modification time of file (milliseconds)
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_GET_LAST_MODIFIED
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
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_SET_LAST_MODIFIED
* Purpose    : set last modification time of file (milliseconds)
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_SET_LAST_MODIFIED
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  #include <utime.h>
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
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_DELETE
* Purpose    : delete a file,link or directory
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_DELETE
  #define TARGET_NATIVE_FILE_DELETE(filename,result) \
    do { \
      result=((unlink(filename)==0) || (rmdir(filename)==0))?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_RENAME
* Purpose    : delete a file, link or directory
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_RENAME
  #define TARGET_NATIVE_FILE_RENAME(oldfilename,newfilename,result) \
    do { \
      result=(rename(oldfilename,newfilename)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_MAKE_DIR
* Purpose    : create new directory
* Input      : name - directory name
* Output     : result - 1 if successful, 0 otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_MAKE_DIR
  #include <sys/stat.h>
  #define TARGET_NATIVE_FILE_MAKE_DIR(name,result) \
    do { \
      result=((mkdir(name,S_IRWXU)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR); \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_GET_CWD
* Purpose    : get current working directory
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_GET_CWD
  #include <unistd.h>
  #define TARGET_NATIVE_FILE_GET_CWD(path,maxPathLength,result) \
    do {\
      result=(getcwd(path,maxPathLength)!=NULL)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_OPEN_DIR
* Purpose    : open directory for reading entries. 
* Input      : -
* Output     : handle - handle if not error, NULL otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_OPEN_DIR
  #include <sys/types.h>
  #include <dirent.h>
  #define TARGET_NATIVE_FILE_OPEN_DIR(filename,handle,result) \
    do { \
      handle=(void*)opendir(filename); \
      result=(handle!=NULL)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while(0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_CLOSE_DIR
* Purpose    : close directory
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_CLOSE_DIR
  #include <sys/types.h>
  #include <dirent.h>
  #define TARGET_NATIVE_FILE_CLOSE_DIR(handle,result) \
    do { \
      closedir((DIR*)handle); \
      result=TARGET_NATIVE_OK; \
    }  while(0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_READ_DIR
* Purpose    : read directory entry
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

//??? name als buffer?
#ifndef TARGET_NATIVE_FILE_READ_DIR
  #include <sys/types.h>
  #include <dirent.h>
  #define TARGET_NATIVE_FILE_READ_DIR(handle,name,result) \
    do { \
      struct dirent *__direntBuffer; \
      \
      name=NULL; \
      \
      __direntBuffer=readdir((DIR*)handle); \
      if (__direntBuffer!=NULL) { \
        name=__direntBuffer->d_name; \
        result=TARGET_NATIVE_OK; \
      } else { \
        result=TARGET_NATIVE_ERROR; \
      } \
    } while (0)
#endif

/***********************************************************************\
* Name       : TARGET_NATIVE_FILE_FSYNC
* Purpose    : do filesystem sync
* Input      : -
* Output     : -
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef TARGET_NATIVE_FILE_FSYNC
  #define TARGET_NATIVE_FILE_FSYNC(filedescriptor,result) \
    do { \
      result=(fsync(filedescriptor)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR; \
    } while(0)
#endif

/***************************** Functions *******************************/

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_GENERIC_FILE__ */

/* end of file */
