/* target_posix_filx.h - Native methods for POSIX file operations
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
Description: generic target defintions of file functions
Systems    : all
*/

#ifndef __TARGET_POSIX_FILE__
#define __TARGET_POSIX_FILE__

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>

/* needed for O_* flags */
#ifdef HAVE_FCNTL_H
  #include <fcntl.h>
#endif

#ifdef HAVE_SYS_STAT_H
  #include <sys/stat.h>
#endif

#include "jni.h"

#include "target_native.h"
#include "target_native_math.h"

#include "target_posix.h"
#include "target_posix_math.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/
#ifndef CP_FILE_STDIN
  #define CP_FILE_STDIN 0
#endif
#ifndef CP_FILE_STDOUT
  #define CP_FILE_STDOUT 1
#endif
#ifndef CP_STDERR
  #define CP_FILE_STDERR 2
#endif

#ifndef CP_FILE_FILEFLAG_NONE
  #define CP_FILE_FILEFLAG_NONE 0
#endif
#ifndef CP_FILE_FILEFLAG_CREATE
  #define CP_FILE_FILEFLAG_CREATE O_CREAT
#endif
#ifndef CP_FILE_FILEFLAG_CREATE_FORCE
  #define CP_FILE_FILEFLAG_CREATE_FORCE (O_CREAT|O_EXCL)
#endif
#ifndef CP_FILE_FILEFLAG_READ
  #define CP_FILE_FILEFLAG_READ O_RDONLY
#endif
#ifndef CP_FILE_FILEFLAG_WRITE
  #define CP_FILE_FILEFLAG_WRITE O_WRONLY
#endif
#ifndef CP_FILE_FILEFLAG_READWRITE
  #define CP_FILE_FILEFLAG_READWRITE O_RDWR
#endif
#ifndef CP_FILE_FILEFLAG_TRUNCATE
  #define CP_FILE_FILEFLAG_TRUNCATE O_TRUNC
#endif
#ifndef CP_FILE_FILEFLAG_APPEND
  #define CP_FILE_FILEFLAG_APPEND O_APPEND
#endif
#ifndef CP_FILE_FILEFLAG_SYNC
  #if !defined (O_SYNC) && defined (O_FSYNC)
    #define TARGET_NATIVE_FILE_FILEFLAG_SYNC O_FSYNC
  #else
    #define TARGET_NATIVE_FILE_FILEFLAG_SYNC O_SYNC
  #endif
#endif
#ifndef CP_FILE_FILEFLAG_DSYNC
  #ifdef O_DSYNC
    #define CP_FILE_FILEFLAG_DSYNC O_DSYNC
  #else
    #define CP_FILE_FILEFLAG_DSYNC CP_FILE_FILEFLAG_SYNC
  #endif
#endif
#ifndef CP_FILE_FILEFLAG_BINARY
  #ifdef O_BINARY
    #define CP_FILE_FILEFLAG_BINARY O_BINARY
  #else
    #define CP_FILE_FILEFLAG_BINARY 0
  #endif
#endif

#ifndef CP_FILE_FILEPERMISSION_NORMAL
  #define CP_FILE_FILEPERMISSION_NORMAL (S_IRUSR | S_IWUSR | S_IRGRP | S_IWGRP | S_IROTH | S_IWOTH)
#endif

#ifndef CP_FILE_FILEPERMISSION_PRIVATE 
  #define CP_FILE_FILEPERMISSION_PRIVATE (S_IRUSR | S_IWUSR)
#endif

#ifndef CP_FILE_FILEPERMISSION_READONLY
  #define CP_FILE_FILEPERMISSION_READONLY (~(S_IWRITE|S_IWGRP|S_IWOTH))
#endif

#ifndef CP_FILE_LOCKMODE_READ
  #ifdef HAVE_F_RDLCK
    #define CP_FILE_LOCKMODE_READ F_RDLCK
  #else
    #define CP_FILE_LOCKMODE_READ 0
  #endif
#endif
#ifndef CP_FILE_LOCKMODE_WRITE
  #ifdef HAVE_F_WRLCK
    #define CP_FILE_LOCKMODE_WRITE F_WRLCK
  #else
    #define CP_FILE_LOCKMODE_WRITE 0
  #endif
#endif

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***********************************************************************\
* Name       : CP_FILE_OPEN
* Purpose    : open a file
* Input      : filename    - filename
*              flags       - file open flags
*              permissions - file permissions
* Output     : filedescriptor - file descriptor
*              result         - CP_OK or CP_ERROR
* Return     : 
* Side-effect: unknown
* Notes      : file is created if it does not exist
\***********************************************************************/

#ifndef CP_FILE_OPEN
  #define CP_FILE_OPEN_POSIX
  #define CP_FILE_OPEN(filename,filedescriptor,flags,permissions,result) \
    (result) = cp_file_open(filename,&filedescriptor,flags,permissions)
#endif

/***********************************************************************\
* Name       : CP_FILE_OPEN_CREATE
* Purpose    : create a file and open for writing
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result         - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : file is created if it does not exist
\***********************************************************************/

#ifndef CP_FILE_OPEN_CREATE
  #define CP_FILE_OPEN_CREATE_POSIX
  #define CP_FILE_OPEN_CREATE(filename,filedescriptor,result) \
    (result) = cp_file_open_create(filename,&filedescriptor)
#endif

/***********************************************************************\
* Name       : CP_FILE_OPEN_READ
* Purpose    : open an existing file for reading
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result         - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_OPEN_READ
  #define CP_FILE_OPEN_READ_POSIX
  #define CP_FILE_OPEN_READ(filename,filedescriptor,result) \
    (result) = cp_file_open_read(filename,&filedescriptor)
#endif

/***********************************************************************\
* Name       : CP_FILE_OPEN_WRITE
* Purpose    : open an existing file for writing
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result         - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_OPEN_WRITE
  #define CP_FILE_OPEN_WRITE_POSIX
  #define CP_FILE_OPEN_WRITE(filename,filedescriptor,result) \
    (result) = cp_file_open_write(filename,&filedescriptor)
#endif

/***********************************************************************\
* Name       : CP_FILE_OPEN_READWRITE
* Purpose    : create/open a file for reading/writing
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result         - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : file is created if it does not exist
\***********************************************************************/

#ifndef CP_FILE_OPEN_READWRITE
  #define CP_FILE_OPEN_READWRITE_POSIX
  #define CP_FILE_OPEN_READWRITE(filename,filedescriptor,result) \
    (result) = cp_file_open_readwrite(filename,&filedescriptor)
#endif

/***********************************************************************\
* Name       : CP_FILE_OPEN_APPEND
* Purpose    : create/open a file for append
* Input      : filename    - filename
* Output     : filedescriptor - file descriptor
*              result         - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : file is created if it does not exist
\***********************************************************************/

#ifndef CP_FILE_OPEN_APPEND
  #define CP_FILE_OPEN_APPEND_POSIX
  #define CP_FILE_OPEN_APPEND(filename,filedescriptor,result) \
    (result) = cp_file_open_append(filename,&filedescriptor)
#endif

/***********************************************************************\
* Name       : CP_FILE_CLOSE
* Purpose    : close a file
* Input      : filedescriptor - file descriptor
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_CLOSE
  #define CP_FILE_CLOSE_POSIX
  #define CP_FILE_CLOSE(filedescriptor,result) \
    (result) = cp_file_close(filedescriptor)
#endif

/***********************************************************************\
* Name       : CP_FILE_VALID_FILE_DESCRIPTOR
* Purpose    : check if file-descriptor is valid
* Input      : filedescriptor - file descriptor
* Output     : result         - CP_OK if file descriptor
*                               valid, CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_VALID_FILE_DESCRIPTOR
  #define CP_FILE_VALID_FILE_DESCRIPTOR_POSIX
  #define CP_FILE_VALID_FILE_DESCRIPTOR(filedescriptor,result) \
    (result) = cp_file_valide_file_descriptor(filedescriptor)
#endif

/***********************************************************************\
* Name       : CP_FILE_TELL
* Purpose    : get current file position
* Input      : filedescriptor - file descriptor
* Output     : tell   - position (0..n)
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_TELL
  #define CP_FILE_TELL_POSIX
  #define CP_FILE_TELL(filedescriptor,offset,result) \
    (result) = cp_file_tell(filedescriptor,&offset)
#endif

/***********************************************************************\
* Name       : CP_FILE_SEEK_BEGIN|CURRENT|END
* Purpose    : set file position relativ to begin/current/end
* Input      : filedescriptor - file descriptor
*              offset         - file position
* Output     : newoffset - new file position
*              result    - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_SEEK_BEGIN
  #define CP_FILE_SEEK_BEGIN_POSIX
  #define CP_FILE_SEEK_BEGIN(filedescriptor,offset,newoffset,result) \
    (result) = cp_file_seek_begin(filedescriptor,offset,&newoffset)
#endif
#ifndef CP_FILE_SEEK_CURRENT
  #define CP_FILE_SEEK_CURRENT_POSIX
  #define CP_FILE_SEEK_CURRENT(filedescriptor,offset,newoffset,result) \
    (result) = cp_file_seek_current(filedescriptor,offset,&newoffset)
#endif
#ifndef CP_FILE_SEEK_END
  #define CP_FILE_SEEK_END_POSIX
  #define CP_FILE_SEEK_END(filedescriptor,offset,newoffset,result) \
    (result) = cp_file_seek_end(filedescriptor,offset,&newoffset)
#endif

/***********************************************************************\
* Name       : CP_FILE_TRUNCATE
* Purpose    : truncate a file
* Input      : filedescriptor - file descriptor
* Output     : offset - offset (0..n)
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_TRUNCATE
  #define CP_FILE_TRUNCATE_POSIX
  #define CP_FILE_TRUNCATE(filedescriptor,offset,result) \
    (result) = cp_file_truncate(filedescriptor,offset)
#endif

/***********************************************************************\
* Name       : CP_FILE_SIZE
* Purpose    : get size of file (in bytes)
* Input      : filedescriptor - file descriptor
* Output     : length - file size (0..n)
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_SIZE
  #define CP_FILE_SIZE_POSIX
  #define CP_FILE_SIZE(filedescriptor,length,result) \
    (result) = cp_file_size(filedescriptor,&length)
#endif

/***********************************************************************\
* Name       : CP_FILE_AVAILABLE
* Purpose    : get available bytes for read
* Input      : filedescriptor - file descriptor
* Output     : length - available bytes
*              result - CP_OK or CP_ERROR
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

#ifndef CP_FILE_AVAILABLE
  #define CP_FILE_AVAILABLE_POSIX
  #define CP_FILE_AVAILABLE(filedescriptor,length,result) \
    (result) = cp_file_available(filedescriptor,&length)
#endif

/***********************************************************************\
* Name       : CP_FILE_READ|WRITE
* Purpose    : read/write from/to frile
* Input      : filedescriptor - file descriptor
*              length         - number of bytes to read
* Output     : buffer    - buffer with read bytes
*              bytesRead - number of bytes read
*              result    - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_READ
  #define CP_FILE_READ_POSIX
  #define CP_FILE_READ(filedescriptor,buffer,length,bytesRead,result) \
    (result) = cp_file_read(filedescriptor,buffer,length,&bytesRead)
#endif
#ifndef CP_FILE_WRITE
  #define CP_FILE_WRITE_POSIX
  #define CP_FILE_WRITE(filedescriptor,buffer,length,bytesWritten,result) \
    (result) = cp_file_write(filedescriptor,buffer,length,&bytesWritten)
#endif

/***********************************************************************\
* Name       : CP_FILE_SET_MODE_READONLY
* Purpose    : set file mode to read-only
* Input      : filename - filename
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_SET_MODE_READONLY
  #define CP_FILE_SET_MODE_READONLY_POSIX
  #define CP_FILE_SET_MODE_READONLY(filename,result) \
    (result) = cp_file_set_mode_readonly(filename)
#endif

/***********************************************************************\
* Name       : CP_FILE_EXISTS
* Purpose    : check if file exists
* Input      : filename - filename
* Output     : result - CP_OK if file exists,
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_EXISTS
  #define CP_FILE_EXISTS_POSIX
  #define CP_FILE_EXISTS(filename,result) \
    (result) = cp_file_exists(filename)
#endif

/***********************************************************************\
* Name       : CP_FILE_IS_FILE
* Purpose    : check if directory entry is a file
* Input      : filename - filename
* Output     : result - CP_OK if file is a file,
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_IS_FILE
  #define CP_FILE_IS_FILE_POSIX
  #define CP_FILE_IS_FILE(filename,result) \
    (result) = cp_file_is_file(filename)
#endif

/***********************************************************************\
* Name       : CP_FILE_IS_DIRECTORY
* Purpose    : check if directory entry is a directory
* Input      : filename - filename
* Output     : result - CP_OK is file is a directory,
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_IS_DIRECTORY
  #define CP_FILE_IS_DIRECTORY_POSIX
  #define CP_FILE_IS_DIRECTORY(filename,result) \
    (result) = cp_file_is_directory(filename)
#endif

/***********************************************************************\
* Name       : CP_FILE_IS_EXECUTABLE
* Purpose    : check if directory entry is an executable
* Input      : filename - filename
* Output     : result - CP_OK if file is an executable,
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_IS_EXECUTABLE
  #define CP_FILE_IS_EXECUTABLE_POSIX
  #define CP_FILE_IS_EXECUTABLE(filename,result) \
    (result) = cp_file_is_executable(filename)
#endif

/***********************************************************************\
* Name       : CP_FILE_GET_LAST_MODIFIED
* Purpose    : get last modification time of file (milliseconds)
* Input      : filename - filename
* Output     : time   - time [ms]
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_GET_LAST_MODIFIED
  #define CP_FILE_GET_LAST_MODIFIED_POSIX
  #define CP_FILE_GET_LAST_MODIFIED(filename,time,result) \
    (result) = cp_file_get_last_modified(filename,&time)
#endif

/***********************************************************************\
* Name       : CP_FILE_SET_LAST_MODIFIED
* Purpose    : set last modification time of file (milliseconds)
* Input      : filename - filename
*              time     - time [ms]
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_SET_LAST_MODIFIED
  #define CP_FILE_SET_LAST_MODIFIED_POSIX
  #define CP_FILE_SET_LAST_MODIFIED(filename,time,result) \
    (result) = cp_file_set_last_modified(filename,time)
#endif

/***********************************************************************\
* Name       : CP_FILE_DELETE
* Purpose    : delete a file, link or directory
* Input      : filename - filename
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_DELETE
  #define CP_FILE_DELETE_POSIX
  #define CP_FILE_DELETE(filename,result) \
    (result) = cp_file_delete(filename)
#endif

/***********************************************************************\
* Name       : CP_FILE_RENAME
* Purpose    : delete a file, link or directory
* Input      : oldfilename - old filename
*              newfilename - new filename
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_RENAME
  #define CP_FILE_RENAME_POSIX
  #define CP_FILE_RENAME(oldfilename,newfilename,result) \
    (result) = cp_file_rename(oldfilename,newfilename)
#endif

/***********************************************************************\
* Name       : CP_FILE_MAKE_DIR
* Purpose    : create new directory
* Input      : pathname - directory name
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_MAKE_DIR
  #define CP_FILE_MAKE_DIR_POSIX
  #define CP_FILE_MAKE_DIR(pathname,result) \
    (result) = cp_file_make_dir(pathname)
#endif

/***********************************************************************\
* Name       : CP_FILE_GET_CWD
* Purpose    : get current working directory
* Input      : maxPathnameLength - max. length of path
* Output     : pathname - pathname
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_GET_CWD
  #define CP_FILE_GET_CWD_POSIX
  #define CP_FILE_GET_CWD(pathname,maxPathnameLength,result) \
    (result) = cp_file_get_cwd(pathname,maxPathnameLength)
#endif

/***********************************************************************\
* Name       : CP_FILE_CHANGE_DIR
* Purpose    : change current working directory
* Input      : pathname - pathname
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_CHANGE_DIR
  #define CP_FILE_CHANGE_DIR_POSIX
  #define CP_FILE_CHANGE_DIR(pathname,result) \
    (result) = cp_file_change_dir(pathname)
#endif

/***********************************************************************\
* Name       : CP_FILE_OPEN_DIR
* Purpose    : open directory for reading entries. 
* Input      : pathname - pathname
* Output     : handle - handle if not error, NULL otherwise
*              result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_OPEN_DIR
  #define CP_FILE_OPEN_DIR_POSIX
  #define CP_FILE_OPEN_DIR(pathname,handle,result) \
    (result) = cp_file_open_dir(pathname,&handle)
#endif

/***********************************************************************\
* Name       : CP_FILE_CLOSE_DIR
* Purpose    : close directory
* Input      : handle - directory handle
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_CLOSE_DIR
  #define CP_FILE_CLOSE_DIR_POSIX
  #define CP_FILE_CLOSE_DIR(handle,result) \
    (result) = cp_file_close_dir(handle)
#endif

/***********************************************************************\
* Name       : CP_FILE_READ_DIR
* Purpose    : read directory entry
* Input      : -
* Output     : name   - name (const string; do not change!)
*              result - CP_OK if entry read,
*                       CP_ERROR otherwise
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_READ_DIR
  #define CP_FILE_READ_DIR_POSIX
  #define CP_FILE_READ_DIR(handle,name,maxNameLength,result) \
    (result) = cp_file_read_dir(handle,name,maxNameLength)
#endif

/***********************************************************************\
* Name       : CP_FILE_FSYNC
* Purpose    : do filesystem sync
* Input      : filedescriptor - file descriptor
* Output     : result - CP_OK or CP_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_FSYNC
  #define CP_FILE_FSYNC_POSIX
  #define CP_FILE_FSYNC(filedescriptor,result) \
    (result) = cp_file_fsync(filedescriptor)
#endif

/***********************************************************************\
* Name       : CP_FILE_LOCK
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

#ifndef CP_FILE_LOCK
  #define CP_FILE_LOCK_POSIX
  #define CP_FILE_LOCK(filedescriptor,mode,offset,length,wait,result) \
    (result) = cp_file_lock(filedescriptor,mode,offset,length,wait)
#endif

/***********************************************************************\
* Name       : CP_FILE_UNLOCK
* Purpose    : do filesystem sync
* Input      : filedescriptor - file descriptor
*              offset         - offset (0..n)
*              length         - length (0..n)
* Output     : result - TARGET_NATIVE_OK or TARGET_NATIVE_ERROR
* Return     : -
* Side-effect: unknown
* Notes      : -
\***********************************************************************/

#ifndef CP_FILE_UNLOCK
  #define CP_FILE_UNLOCK_POSIX
  #define CP_FILE_UNLOCK(filedescriptor,offset,length,result) \
    (result) = cp_file_unlock(filedescriptor,offset,length)
#endif

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

int cp_file_open(const char *filename, int *filedescriptor, int flags, int permissions);
int cp_file_open_create(const char *filename, int *filedescriptor);
int cp_file_open_read(const char *filename, int *filedescriptor);
int cp_file_open_write(const char *filename, int *filedescriptor);
int cp_file_open_readwrite(const char *filename, int *filedescriptor);
int cp_file_open_append(const char *filename, int *filedescriptor);
int cp_file_close(int filedescriptor);
int cp_file_valide_file_descriptor(int filedescriptor);
int cp_file_tell(int filedescriptor, jlong *offset);
int cp_file_seek_begin(int filedescriptor, jlong offset, jlong *newoffset);
int cp_file_seek_current(int filedescriptor, jlong offset, jlong *newoffset);
int cp_file_seek_end(int filedescriptor, jlong offset, jlong *newoffset);
int cp_file_truncate(int filedescriptor, int offset);
int cp_file_size(int filedescriptor, jlong *length);
int cp_file_available(int filedescriptor, jlong *length);
int cp_file_read(int filedescriptor, void *buffer, int length, jint *bytesRead);
int cp_file_write(int filedescriptor, void *buffer, int length, jint *bytesWritten);
int cp_file_set_mode_readonly(const char *filename);
int cp_file_exists(const char *filename);
int cp_file_is_file(const char *filename);
int cp_file_is_directory(const char *filename);
int cp_file_get_last_modified(const char *filename, jlong *time);
int cp_file_set_last_modified(const char *filename, jlong time);
int cp_file_delete(const char *filename);
int cp_file_rename(const char *oldfilename, const char *newfilename);
int cp_file_make_dir(const char *pathname);
int cp_file_get_cwd(char *pathname, int maxPathnameLength);
int cp_file_change_dir(const char *pathname);
int cp_file_open_dir(const char *pathname, void **handle);
int cp_file_close_dir(void *handle);
int cp_file_read_dir(void *handle, char *name, int maxNameLength);
int cp_file_fsync(int filedescriptor);
int cp_file_lock(int filedescriptor, int mode, jlong offset, jlong length, jboolean wait);
int cp_file_unlock(int filedescriptor, jlong offset, jlong length);

#ifdef __cplusplus
}
#endif

#endif /* __TARGET_POSIX_FILE__ */

/* end of file */

