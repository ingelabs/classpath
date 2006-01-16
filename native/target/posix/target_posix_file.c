/* target_posix_file.c - Native methods for POSIX file operations
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
Description: POSIX target defintions of file functions
Systems    : all
*/

/****************************** Includes *******************************/
/* do not move; needed here because of some macro definitions */
#include "config.h"

#include <stdlib.h>
#include <assert.h>

#include "jni.h"

#include "target_native.h"
#include "target_native_math.h"

#include "target_posix.h"
#include "target_posix_math.h"

/* needed for O_* flags */
#ifdef HAVE_FCNTL_H
  #include <fcntl.h>
#endif

#include "target_posix_file.h"

/****************** Conditional compilation switches *******************/

/***************************** Constants *******************************/

/***************************** Datatypes *******************************/

/***************************** Variables *******************************/

/****************************** Macros *********************************/

/***************************** Functions *******************************/

#ifdef __cplusplus
extern "C" {
#endif

#ifdef NEW_CP

#ifdef CP_FILE_OPEN_POSIX
  #include <sys/types.h>
  #include <sys/stat.h>
  #ifdef HAVE_FCNTL_H
    #include <fcntl.h>
  #endif

  int cp_file_open(const char *filename, int *filedescriptor, int flags, int permissions)
    {
      assert(filedescriptor!=NULL);

      (*filedescriptor)=open(filename,
                             flags,
                             permissions
                            );
      #if defined(HAVE_FCNTL) && defined(FD_CLOEXEC_IN_FCNTL_H)
        if ((*filedescriptor) >= 0)
          {
            fcntl(*filedescriptor,F_SETFD,FD_CLOEXEC);
          }
      #endif

      return ((*filedescriptor) >= 0)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_OPEN_CREATE_POSIX
  int cp_file_open_create(const char *filename, int *filedescriptor)
    {
      return cp_file_open(filename,
                          filedescriptor,
                          CP_FILE_FILEFLAG_CREATE_FORCE|CP_FILE_FILEFLAG_WRITE|CP_FILE_FILEFLAG_BINARY,
                          CP_FILE_FILEPERMISSION_NORMAL
                         );                          
    }
#endif

#ifdef CP_FILE_OPEN_READ_POSIX
  int cp_file_open_read(const char *filename, int *filedescriptor)
    {
      return cp_file_open(filename,
                          filedescriptor,
                          CP_FILE_FILEFLAG_READ|CP_FILE_FILEFLAG_BINARY,
                          CP_FILE_FILEPERMISSION_NORMAL
                         );                          
    }
#endif

#ifdef CP_FILE_OPEN_WRITE_POSIX
  int cp_file_open_write(const char *filename, int *filedescriptor)
    {
      return cp_file_open(filename,
                          filedescriptor,
                          CP_FILE_FILEFLAG_WRITE|CP_FILE_FILEFLAG_BINARY,
                          CP_FILE_FILEPERMISSION_NORMAL
                         );                          
    }
#endif

#ifdef CP_FILE_OPEN_READWRITE_POSIX
  int cp_file_open_readwrite(const char *filename, int *filedescriptor)
    {
      return cp_file_open(filename,
                          filedescriptor,
                          CP_FILE_FILEFLAG_READWRITE|CP_FILE_FILEFLAG_BINARY,
                          CP_FILE_FILEPERMISSION_NORMAL
                         );                          
    }
#endif

#ifdef CP_FILE_OPEN_APPEND_POSIX
  int cp_file_open_append(const char *filename, int *filedescriptor)
    {
      return cp_file_open(filename,
                          filedescriptor,
                          CP_FILE_FILEFLAG_CREATE|CP_FILE_FILEFLAG_WRITE|CP_FILE_FILEFLAG_APPEND|CP_FILE_FILEFLAG_BINARY,
                          CP_FILE_FILEPERMISSION_NORMAL
                         );                          
    }
#endif

#ifdef CP_FILE_CLOSE_POSIX
  #include <unistd.h>
  int cp_file_close(int filedescriptor)
    {
      return (close(filedescriptor) == 0)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_VALID_FILE_DESCRIPTOR_POSIX
  #if   defined(HAVE_FCNTL)
    #include <unistd.h>
    #ifdef HAVE_FCNTL_H
      #include <fcntl.h>
    #endif
    int cp_file_valide_file_descriptor(int filedescriptor)
      {
        return (fcntl(filedescriptor,F_GETFL,0)!=-1)?CP_OK:CP_ERROR;
      }
  #elif defined(HAVE_FSTAT)
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    int cp_file_valide_file_descriptor(int filedescriptor)
      {
        struct stat stat;

        return (fstat(filedescriptor,&stat) == 0)?CP_OK:CP_ERROR;
      }
  #else
    #error fcntl() nor fstat() available for checking if file descriptor is valid
  #endif
#endif

#ifdef CP_FILE_TELL_POSIX
  #include <sys/types.h>
  #include <unistd.h>
  int cp_file_tell(int filedescriptor, jlong *offset)
    {
      off_t value;
      int   result;

      assert(offset!=NULL);

      value = lseek(filedescriptor,0,SEEK_CUR);

      if (value != (off_t)(-1))
        {
          (*offset) = (jlong)value;
          result = CP_OK;
        }
      else
        {
          result = CP_ERROR;
        }

      return result;
    }
#endif

#ifdef CP_FILE_SEEK_BEGIN_POSIX
  #include <sys/types.h>
  #include <unistd.h>
  int cp_file_seek_begin(int filedescriptor, jlong offset, jlong *newoffset)
    {
      off_t value;
      int   result;

      assert(newoffset!=NULL);

      value = lseek(filedescriptor,offset,SEEK_SET);

      if (value != (off_t)(-1))
        {
          (*newoffset) = (jlong)value;
          result = CP_OK;
        }
      else
        {
          result = CP_ERROR;
        }

      return result;
    }
#endif
#ifdef CP_FILE_SEEK_CURRENT_POSIX
  #include <sys/types.h>
  #include <unistd.h>
  int cp_file_seek_current(int filedescriptor, jlong offset, jlong *newoffset)
    {
      off_t value;
      int   result;

      assert(newoffset!=NULL);

      value = lseek(filedescriptor,CP_MATH_INT64_TO_INT32(offset),SEEK_CUR);

      if (value != (off_t)(-1))
        {
          (*newoffset) = (jlong)value;
          result = CP_OK;
        }
      else
        {
          result = CP_ERROR;
        }

      return result;
    }
#endif
#ifdef CP_FILE_SEEK_END_POSIX
  #include <sys/types.h>
  #include <unistd.h>
  int cp_file_seek_end(int filedescriptor, jlong offset, jlong *newoffset)
    {
      off_t value;
      int   result;

      assert(newoffset!=NULL);

      value = lseek(filedescriptor,offset,SEEK_END);

      if (value != (off_t)(-1))
        {
          (*newoffset) = (jlong)value;
          result = CP_OK;
        }
      else
        {
          result = CP_ERROR;
        }

      return result;
    }
#endif

#ifdef CP_FILE_TRUNCATE_POSIX
  #include <unistd.h>
  int cp_file_truncate(int filedescriptor, int offset)
    {
      return (ftruncate(filedescriptor,offset) == 0)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_SIZE_POSIX
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  int cp_file_size(int filedescriptor, jlong *length)
    {
      struct stat statBuffer;
      int         result;

      assert(length!=NULL);

      result = (fstat(filedescriptor,&statBuffer) == 0)?CP_OK:CP_ERROR;
      (*length) = CP_MATH_INT32_TO_INT64(statBuffer.st_size);;

      return result;
    }
#endif

#ifdef CP_FILE_AVAILABLE_POSIX
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
    int cp_file_available(int filedescriptor, jlong *length)
      {
        ssize_t n;
        int     result;

        assert(length!=NULL);

        result = (ioctl(filedescriptor,FIONREAD,(char*)&n) == 0)?CP_OK:CP_ERROR;
        (*length) = CP_MATH_INT32_TO_INT64(n);

        return result;
      }
  #elif defined(HAVE_FSTAT) && defined(HAVE_SELECT)
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    #include <string.h>
    #include <sys/select.h>
    int cp_file_available(int filedescriptor, jlong *length)
      {
        struct stat    statBuffer;
        off_t          n;
        fd_set         filedescriptset;
        struct timeval timeval;
        int            result;

        assert(length!=NULL);

        (*length)=CP_MATH_INT64_CONST_0;

        if   ((fstat(filedescriptor,&statBuffer) == 0) && S_ISREG(statBuffer.st_mode))
        {
          n=(lseek(filedescriptor,0,SEEK_CUR));
          if (n!=-1)
          {
            (*length) = CP_MATH_INT32_TO_INT64(statBuffer.st_size-n);
            result    = CP_OK;
          }
          else
          {
            result = CP_ERROR;
          }
        }
        else
        {
          FD_ZERO(&filedescriptset);
          FD_SET(filedescriptor,&filedescriptset);
          memset(&timeval,0,sizeof(__timeval));
          /* Hint: __filedescriptset is a set of bits, thus the size of the used set
                   elements is the last element+1, e. g. filescriptor is 3, then
                   __filedescriptset is 0001000... thus...
                                        |--| size is 4 (not 3!)
          */
          switch (select(filedescriptor+1,&filedescriptset,NULL,NULL,&timeval) == 0)
          {
            case -1:                                  result=CP_ERROR; break;
            case  0: (*length)=CP_MATH_INT64_CONST_0; result=CP_OK;    break;
            default: (*length)=CP_MATH_INT64_CONST_1; result=CP_OK;    break;
          }
        }

        return result;
      }
  #elif defined(HAVE_FSTAT)
    #include <sys/types.h>
    #include <sys/stat.h>
    #include <unistd.h>
    int cp_file_available(int filedescriptor, jlong *length)
      {
        struct stat statBuffer;
        off_t       n;
        int         result;

        assert(length!=NULL);

        length=CP_MATH_INT64_CONST_0;

        if ((fstat(filedescriptor,&statBuffer) == 0) && S_ISREG(statBuffer.st_mode))
        {
          n=(lseek(filedescriptor,0,SEEK_CUR));
          if (n!=-1)
          {
            (*length) = CP_MATH_INT32_TO_INT64(__statBuffer.st_size-__n);
            result    = CP_OK;
          }
          else
          {
            result = CP_ERROR;
          }
        }
        else
        {
          result = CP_ERROR;
        }

        return result;
      }
  #elif defined(HAVE_SELECT)
    #include <string.h>
    #include <sys/select.h>
    int cp_file_available(int filedescriptor, jlong *length)
      {
        fd_set         filedescriptset;
        struct timeval timeval;
        int            result;

        assert(length!=NULL);

        (*length)=CP_MATH_INT64_CONST_0;

        FD_ZERO(&filedescriptset);
        FD_SET(filedescriptor,&filedescriptset);
        memset(&timeval,0,sizeof(timeval));
        /* Hint: __filedescriptset is a set of bits, thus the size of the used set
                 elements is the last element+1, e. g. filescriptor is 3, then
                 __filedescriptset is 0001000... thus...
                                      |--| size is 4 (not 3!)
        */
        switch (select(filedescriptor+1,&filedescriptset,NULL,NULL,&timeval) == 0)
        {
          case -1:                                  result=CP_ERROR; break;
          case  0: (*length)=CP_MATH_INT64_CONST_0; result=CP_OK;    break;
          default: (*length)=CP_MATH_INT64_CONST_1; result=CP_OK;    break;
        }

        return result;
      }
  #else
    #ifndef WITHOUT_FILESYSTEM
      #error No suitable implementation for CP_FILE_AVAILABLE - please check configuration!
    #else
      int cp_file_available(int filedescriptor, jlong *length)
        {
          ssize_t n;
          int     result;

          assert(length!=NULL);

          errno = CP_ERROR_OPERATION_NOT_PERMITTED;
          (*length) = CP_MATH_INT64_CONST_0;
          
          return CP_ERROR;
        }
    #endif
  #endif
#endif

#ifdef CP_FILE_READ_POSIX
  #include <unistd.h>
  int cp_file_read(int filedescriptor, void *buffer, int length, jint *bytesRead)
    {
      assert(bytesRead!=NULL);

      (*bytesRead)=read(filedescriptor,buffer,length);

      return ((*bytesRead)!=-1)?CP_OK:CP_ERROR;
    }
#endif
#ifdef CP_FILE_WRITE_POSIX
  #include <unistd.h>
  int cp_file_write(int filedescriptor, void *buffer, int length, jint *bytesWritten)
    {
      assert(bytesWritten!=NULL);

      (*bytesWritten)=write(filedescriptor,buffer,length);

      return ((*bytesWritten)!=-1)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_SET_MODE_READONLY_POSIX
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  int cp_file_set_mode_readonly(const char *filename)
    {
      struct stat statBuffer;
      int         result;

      if (stat(filename,&statBuffer) == 0)
        {
          result = (chmod(filename,statBuffer.st_mode & CP_FILE_FILEPERMISSION_READONLY) == 0)?CP_OK:CP_ERROR;
        }
      else
        {
          result = CP_ERROR;
        }

      return result;
    }
#endif

#ifdef CP_FILE_EXISTS_POSIX
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  int cp_file_exists(const char *filename)
    {
      struct stat statBuffer;

      return (stat(filename,&statBuffer) == 0)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_IS_FILE_POSIX
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  int cp_file_is_file(const char *filename)
    {
      struct stat statBuffer;

      return ((stat(filename,&statBuffer) == 0) && (S_ISREG(statBuffer.st_mode)))?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_IS_DIRECTORY_POSIX
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  int cp_file_is_directory(const char *filename)
    {
      struct stat statBuffer;

      return ((stat(filename,&statBuffer) == 0) && (S_ISDIR(statBuffer.st_mode)))?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_IS_EXECUTABLE_POSIX
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  int cp_file_is_executable(const char *filename)
    {
      struct stat statBuffer;

      return ((stat(filename,&statBuffer) == 0) && (S_ISREG(statBuffer.st_mode)))?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_GET_LAST_MODIFIED_POSIX
  #include <sys/types.h>
  #include <sys/stat.h>
  #include <unistd.h>
  int cp_file_get_last_modified(const char *filename, jlong *time)
    {
      struct stat statBuffer;
      int         result;

      assert(time!=NULL);

      (*time) = CP_MATH_INT64_CONST_0;
      if (stat(filename,&statBuffer) == 0)
        {
          (*time)=CP_MATH_INT64_MUL(CP_MATH_INT32_TO_INT64(statBuffer.st_mtime),
                                    CP_MATH_INT32_TO_INT64(1000)
                                   );
          result = CP_OK;
        }
      else
        {
          result = CP_ERROR;
        }

      return result;
    }
#endif

#ifdef CP_FILE_SET_LAST_MODIFIED_POSIX
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
  int cp_file_set_last_modified(const char *filename, jlong time)
    {
      struct stat    statBuffer;
      struct utimbuf utimeBuffer;
      int            result;

      if (stat(filename,&statBuffer) == 0)
        {
          utimeBuffer.actime  = statBuffer.st_atime;
          utimeBuffer.modtime = CP_MATH_INT64_TO_INT32(CP_MATH_INT64_DIV(time,
                                                                         CP_MATH_INT32_TO_INT64(1000)
                                                                        )
                                                      );
        result = (utime(filename,&utimeBuffer) == 0)?CP_OK:CP_ERROR;
        }
      else
        {
          result = CP_ERROR;
        }

      return result;
    }
#endif

#ifdef CP_FILE_DELETE_POSIX
  #include <unistd.h>
  int cp_file_delete(const char *filename)
    {
       return ((unlink(filename) == 0) || (rmdir(filename) == 0))?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_RENAME_POSIX
  #include <stdio.h>
  int cp_file_rename(const char *oldfilename, const char *newfilename)
    {
       return (rename(oldfilename,newfilename) == 0)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_MAKE_DIR_POSIX
  #include <sys/stat.h>
  int cp_file_make_dir(const char *pathname)
    {
       return ((mkdir(pathname,(S_IRWXO|S_IRWXG|S_IRWXU)) == 0)?CP_OK:CP_ERROR);
    }
#endif

#ifdef CP_FILE_GET_CWD_POSIX
  #include <unistd.h>
  int cp_file_get_cwd(char *pathname, int maxPathnameLength)
    {
      return (getcwd(pathname,maxPathnameLength)!=NULL)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_CHANGE_DIR_POSIX
  #include <unistd.h>
  int cp_file_change_dir(const char *pathname)
    {
      return (chdir(pathname) == 0)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_OPEN_DIR_POSIX
  #include <sys/types.h>
  #include <dirent.h>
  int cp_file_open_dir(const char *pathname, void **handle)
    {
      assert(handle != NULL);

      (*handle) = (void*)opendir(pathname);

      return ((*handle) != NULL)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_CLOSE_DIR_POSIX
  #include <sys/types.h>
  #include <dirent.h>
  int cp_file_close_dir(void *handle)
    {
      closedir((DIR*)handle);

      return CP_OK;
    }
#endif

//NYI: UNDER DEVELOPMENT: name as buffer?
#ifdef CP_FILE_READ_DIR_POSIX
  #include <sys/types.h>
  #include <dirent.h>
  int cp_file_read_dir(void *handle, char *name, int maxNameLength)
    {
      struct dirent *direntBuffer;
      int           result;

      assert(name != NULL);

      (*name) = NULL;

      direntBuffer=readdir((DIR*)handle);
      if (direntBuffer != NULL)
        {
          strncpy(name,maxNameLength,direntBuffer->d_name);
          result = CP_OK;
        }
      else
        {
          result = CP_ERROR;
        }

      return result;
    }
#endif

#ifdef CP_FILE_FSYNC_POSIX
  #include <unistd.h>
  int cp_file_fsync(int filedescriptor)
    {
      return (fsync(filedescriptor) == 0)?CP_OK:CP_ERROR;
    }
#endif

#ifdef CP_FILE_LOCK_POSIX
  #ifdef HAVE_FCNTL
    #include <unistd.h>
    #include <fcntl.h>
  #endif
  int cp_file_lock(int filedescriptor, int mode, jlong offset, jlong length, jboolean wait)
    {
      #ifdef HAVE_FCNTL
        struct flock flock;

        flock.l_type  =mode;
        flock.l_whence=SEEK_SET;
        flock.l_start =(off_t)offset;
        flock.l_len   =(off_t)length;
        return (fcntl(filedescriptor,wait?F_SETLKW:F_SETLK,&flock)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR;
      #else /* not HAVE_FCNTL */
        CP_SET_LAST_ERROR(CP_ERROR_NOT_IMPLEMENTED,"unlock");
        return TARGET_NATIVE_ERROR;
      #endif /* HAVE_FCNTL */
    }
#endif

#ifdef CP_FILE_UNLOCK_POSIX
  #ifdef HAVE_FCNTL
    #include <unistd.h>
    #include <fcntl.h>
  #endif
  int cp_file_unlock(int filedescriptor, jlong offset, jlong length)
    {
      #ifdef HAVE_FCNTL
        struct flock flock;

        flock.l_type  =F_UNLCK;
        flock.l_whence=SEEK_SET;
        flock.l_start =(off_t)offset;
        flock.l_len   =(off_t)length;
        return (fcntl(filedescriptor,F_SETLK,&flock)==0)?TARGET_NATIVE_OK:TARGET_NATIVE_ERROR;
      #else /* not HAVE_FCNTL */
        CP_SET_LAST_ERROR(CP_ERROR_NOT_IMPLEMENTED,"unlock");
        return TARGET_NATIVE_ERROR;
      #endif /* HAVE_FCNTL */
    }
#endif

#endif /* NEW_CP */

#ifdef __cplusplus
}
#endif

/* end of file */

