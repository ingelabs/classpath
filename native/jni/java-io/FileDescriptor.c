/* FileDescriptor.c - Native methods for java.io.FileDescriptor class
   Copyright (C) 1998,2003 Free Software Foundation, Inc.

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
 * Note that these functions skip many error checks because these
 * checks are made in Java before the methods are invoked.  See
 * the Java source to FileDescriptor for more info
 *
 * Aaron M. Renn (arenn@urbanophile.com)
 *
 * Some of this coded adoped from the gcj native libraries
 */

#include <config.h>

/* FIXME: Need to make configure set these for us */
/* #define HAVE_SYS_IOCTL_H */
/* #define HAVE_SYS_FILIO_H */
#define HAVE_FTRUNCATE
#define HAVE_FSYNC
#define HAVE_SELECT

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/time.h>
#include <sys/stat.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>

#ifdef HAVE_SYS_IOCTL_H
#define BSD_COMP /* Get FIONREAD on Solaris2 */
#include <sys/ioctl.h>
#endif

#ifdef HAVE_SYS_FILIO_H /* Get FIONREAD on Solaris 2.5 */
#include <sys/filio.h>
#endif

#include <jni.h>

#include "jcl.h"
#include "java_io_FileDescriptor.h"

#define true (1)
#define false (0)

// FIXME: This can't be right.  Need converter macros
#define CONVERT_JLONG_TO_INT(x) ((int)(x & 0xFFFF))

// FIXME: This can't be right.  Need converter macros
#define CONVERT_JLONG_TO_OFF_T(x) (x)

/* These are initialized in nativeInit() */
static jint SET;
static jint CUR;
static jint END;

/*************************************************************************/

/*
 * Library initialization routine.  Called as part of java.io.FileDescriptor
 * static initialization.
 */
JNIEXPORT void JNICALL
Java_java_io_FileDescriptor_nativeInit(JNIEnv *env, jclass clazz)
{
  /* FIXME: Should set these to same values as in the FileDescriptor java
   * code */
  SET = 0;
  CUR = 1;
  END = 2;

  /* FIXME: If stdin, stdout, and stderr not fd 0, 1, 2, then set
   * appropriate values to the static native fields in, out, err. */
}

/*************************************************************************/
/*
 * Open the specified file and return a native file descriptor
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeOpen(JNIEnv *env, jobject obj, jstring name, 
                                       jstring mode)
{
  int rc;
  char *cname, *cmode;

  cname = JCL_jstring_to_cstring(env, name);
  cmode = JCL_jstring_to_cstring(env, mode);
  if (!cname || !cmode)
    return(-1); /* Exception will already have been thrown */

  // FIXME: Should we manually set permission mode?
  if (!strcmp(cmode,"r"))
    rc = open(cname, O_RDONLY);
  else if (!strcmp(cmode, "rw"))
    rc = open(cname, O_RDWR | O_CREAT);
  else if (!strcmp(cmode, "ra"))
    rc = open(cname, O_RDWR | O_CREAT | O_APPEND);
  else if (!strcmp(cmode, "rws") || !strcmp(cmode,"rwd"))
    rc = open(cname, O_RDWR | O_CREAT | O_SYNC);
  else
    rc = -1; /* Invalid mode */

  (*env)->ReleaseStringUTFChars(env, name, cname);
  (*env)->ReleaseStringUTFChars(env, mode, cmode);

  if (rc != -1)
    return(rc);

  if (errno == ENOENT)
    JCL_ThrowException(env,"java/io/FileNotFoundException", strerror(errno));
  else
    JCL_ThrowException(env,"java/io/IOException", strerror(errno)); 

  return(rc);
}

/*************************************************************************/
/*
 * Closes the specified file descriptor and return status code.
 * Exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeClose(JNIEnv *env, jobject obj, jlong fd)
{
  int rc, native_fd;

  native_fd = CONVERT_JLONG_TO_INT(fd);
 
  rc = close(native_fd);
  if (rc == -1)
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));

  return(rc);
}

/*************************************************************************/
/*
 * Writes a single byte to the specified file descriptor
 * Return status code, exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeWriteByte(JNIEnv *env, jobject obj,
                                            jlong fd, jlong b)
{
  int native_fd;
  int native_byte;
  char buf[1];
  ssize_t rc; 

  native_fd = CONVERT_JLONG_TO_INT(fd);
  native_byte = CONVERT_JLONG_TO_INT(b);
  buf[0] = (char)(native_byte & 0xFF);

  while (rc != 1)
    {
      rc = write(fd, buf, 1);
      if ((rc == -1) && (errno != EINTR))
        {
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
          return(rc);
	}
    }

  return(rc);
}

/*************************************************************************/
/*
 * Writes a byte buffer to the specified file descriptor
 * Return status code, exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeWriteBuf(JNIEnv *env, jobject obj,
                                           jlong fd, jarray buf, jlong offset,
					   jlong len)
{
  int native_fd;
  ssize_t rc, bytes_written = 0;
  jbyte *bufptr;

  native_fd = CONVERT_JLONG_TO_INT(fd);

  bufptr = (*env)->GetByteArrayElements(env, buf, 0);
  if (!bufptr)
    {
      JCL_ThrowException(env, "java/io/IOException", "Unexpected JNI error");
      return(-1);
    }

  while (bytes_written < len)
    {
      rc = write(fd, (bufptr + offset + bytes_written), (len - bytes_written));
      if ((rc == -1) && (errno != EINTR))
        {
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
          (*env)->ReleaseByteArrayElements(env, buf, bufptr, 0);
          return(-1);
        }
      bytes_written += rc;
    }

  (*env)->ReleaseByteArrayElements(env, buf, bufptr, 0);
  return(0);
}

/*************************************************************************/
/*
 * Read a single byte from the file descriptor
 * Return byte read or -1 on eof, exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeReadByte(JNIEnv *env, jobject obj, jlong fd)
{
  int native_fd;
  jbyte b;
  ssize_t rc;

  native_fd = CONVERT_JLONG_TO_INT(fd);

  while (rc != 1)
    {
      rc = read(fd, &b, 1);
      if (rc == 0)
        return(-1); /* Signal end of file to Java */
      if ((rc == -1) && (errno != EINTR))
        {
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
          return(-1);
	}
    }
  return(b);
}

/*************************************************************************/
/*
 * Reads to a byte buffer from the specified file descriptor
 * Return number of bytes read or -1 on eof, exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeReadBuf(JNIEnv *env, jobject obj,
                                         jlong fd, jarray buf, jlong offset,
					 jlong len)
{
  int native_fd;
  ssize_t rc, bytes_read = 0;
  jbyte *bufptr;

  native_fd = CONVERT_JLONG_TO_INT(fd);

  bufptr = (*env)->GetByteArrayElements(env, buf, 0);
  if (!bufptr)
    {
      JCL_ThrowException(env, "java/io/IOException", "Unexpected JNI error");
      return(-1);
    }

  while (bytes_read < len)
    {
      rc = read(fd, (bufptr + offset + bytes_read), (len - bytes_read));

      if (rc == 0)
        {
          (*env)->ReleaseByteArrayElements(env, buf, bufptr, 0);
          if (bytes_read == 0)
            return(-1); /* Signal end of file to Java */
          else 
            return(bytes_read);
        }

      if ((rc == -1) && (errno != EINTR))
        {
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
          (*env)->ReleaseByteArrayElements(env, buf, bufptr, 0);
          return(-1);
        }
      bytes_read += rc;
    }

  (*env)->ReleaseByteArrayElements(env, buf, bufptr, 0);
  return(bytes_read);
}

/*************************************************************************/
/*
 * Return number of bytes that can be read from the file w/o blocking.
 * Exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeAvailable(JNIEnv *env, jobject obj, jlong fd)
{
#if defined(FIONREAD) || defined(HAVE_SELECT) || defined(HAVE_FSTAT)
  int native_fd, found = 0;
  ssize_t num = 0, rc;
  off_t cur_pos;
  struct stat sb;
  fd_set fds;
  struct timeval tv;

  native_fd = CONVERT_JLONG_TO_INT(fd);

#if defined(FIONREAD)
  rc = ioctl(native_fd, FIONREAD, &num);
  if (rc == -1) /* We don't care if this fails.  Try something else */
    {
      rc = 0;
      num = 0;
    }
  else
    {
      found = 1;
    }
#endif /* defined FIONREAD */
#if defined (HAVE_FSTAT)
  if (!found)
    {
      rc = fstat(native_fd, &sb);
      if (rc != -1) /* Don't bomb here either - just try the next method */
        {
          if (S_ISREG(sb.st_mode))
            {
              cur_pos = lseek(native_fd, 0, SEEK_CUR);
              if (cur_pos != 1)
                {
                  num = (ssize_t)(sb.st_size - cur_pos);
                  found = 1;
                }
            }
        }
    }
#endif /* defined HAVE_FSTAT */
#if defined (HAVE_SELECT)
  if (!found)
    {
      FD_ZERO(&fds);
      FD_SET(native_fd, &fds);
      memset(&tv, 0, sizeof(struct timeval));

      rc = select(native_fd + 1, &fds, 0, 0, &tv);
      if (rc == -1) /* Finally, we give up */
        {
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
          return(-1);
        }
      found = 1;
      if (rc == 0) 
        num = 0; /* Nothing to read here */
      else
        num = 1; /* We know there is something, but not how much */
    }
#endif /* defined HAVE_SELECT */

  if (!found)
    return(0);
  else
    return(num);

#else /* defined FIONREAD, HAVE_SELECT, HAVE_FSTAT */
 /* FIXME: Probably operation isn't supported, but this exception
  * is too harsh as it will probably crash the program without need
  JCL_ThrowException(env, "java/lang/UnsupportedOperationException",
  "not implemented - can't shorten files on this platform");
  
  This even seems rather harsh
  JCL_ThrowException(env, "java/io/IOException",
                     "Unable to shorten file length");
  */
  return(0);
#endif 
}

/*************************************************************************/
/*
 * Wrapper around lseek call.  Return new file position
 * Exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeSeek(JNIEnv *env, jobject obj, jlong fd,
		                       jlong offset, jint whence,
				       jboolean stop_at_eof)
{
  int native_fd;
  off_t rc, native_offset, cur_pos, file_size;
  struct stat sb;

  native_fd = CONVERT_JLONG_TO_INT(fd);
  native_offset = CONVERT_JLONG_TO_OFF_T(offset);

  /* FIXME: What do we do if offset > the max value of off_t on this 32bit
   * system?  How do we detect that and what do we do? */
  if ((jlong)native_offset != offset)  
    {
      JCL_ThrowException(env, "java/io/IOException",
                         "Cannot represent position correctly on this system");
      return(-1);
    }
    
  if (stop_at_eof)
    {
      rc = fstat(native_fd, &sb);
      file_size = sb.st_size;
      if (rc == -1)
        {
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
          return(-1);
        }
      if (whence == SET)
        {
          if (native_offset > file_size)
            native_offset = file_size;
        }
      else if (whence == CUR)
        {
          cur_pos = lseek(native_fd, 0, SEEK_CUR);
          if (cur_pos == -1)
            {
              JCL_ThrowException(env, "java/io/IOException", strerror(errno));
              return(-1);
            }
          if ((cur_pos + native_offset) > file_size)
            {
              native_offset = file_size;
              whence = SET;
            }
        }
      else if (native_offset > 0) /* Default to END case */
        {
          native_offset = 0;
        }
    }

  /* Now do it */
  rc = -1;
  if (whence == SET)
    rc = lseek(native_fd, native_offset, SEEK_SET);  
  if (whence == CUR)
    rc = lseek(native_fd, native_offset, SEEK_CUR);
  if (whence == END)
    rc = lseek(native_fd, native_offset, SEEK_END);

  if (rc == -1)
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));
  return(rc);
}

/*************************************************************************/
/*
 * Return the current position of the file pointer
 * Exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeGetFilePointer(JNIEnv *env, jobject obj, 
		                                 jlong fd)
{
  int native_fd;
  off_t rc;

  native_fd = CONVERT_JLONG_TO_INT(fd);

  rc = lseek(native_fd, 0, SEEK_CUR);
  if (rc == -1)
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));

  return(rc);
}

/*************************************************************************/
/*
 * Return the length of the file
 * Exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeGetLength(JNIEnv *env, jobject obj, jlong fd)
{
  int rc, native_fd;
  struct stat sb;

  native_fd = CONVERT_JLONG_TO_INT(fd);

  rc = fstat(native_fd, &sb);
  if (rc == -1)
    {
      JCL_ThrowException(env, "java/io/IOException", strerror(errno));
      return(-1);
    }

  return(sb.st_size);
}

/*************************************************************************/
/*
 * Set the length of the file
 * Exception on error
 */
JNIEXPORT void JNICALL
Java_java_io_FileDescriptor_nativeSetLength(JNIEnv *env, jobject obj, 
		                            jlong fd, jlong len)
{
  int native_fd;
  off_t rc, native_len, save_pos;
  struct stat sb;
  char c = '\0';

  native_fd = CONVERT_JLONG_TO_INT(fd);
  native_len = CONVERT_JLONG_TO_OFF_T(len);

  /* FIXME: What do we do if len > the max value of off_t on this 32bit
   * system?  How do we detect that and what do we do? */
  if ((jlong)native_len != len)  
    {
      JCL_ThrowException(env, "java/io/IOException",
                         "Cannot represent position correctly on this system");
      return;
    }
    
  rc = fstat(native_fd, &sb);
  if (rc == -1)
    {
      JCL_ThrowException(env, "java/io/IOException", strerror(errno));
      return;
    }

  /* Lucky us, perfect size */
  if ((jlong)sb.st_size == len)
    return;

  /* File is too short -- seek to one byte short of where we want,
   * then write a byte */
  if ((jlong)sb.st_size < len)
    {
      /* Save off current position */
      save_pos = lseek(native_fd, 0, SEEK_CUR);
      if (save_pos != -1)
        rc = lseek(native_fd, native_len - 1, SEEK_SET);
      if ((save_pos == -1) || (rc == -1))
        {
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
          return;
        }

      /* Note: This will fail if we somehow get here in read only mode
       * That shouldn't happen */
      rc = write(native_fd, &c, 1);
      if (rc == -1)
        {
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
          return;
        }

      /* Reposition file pointer to where we started */
      rc = lseek(native_fd, save_pos, SEEK_SET);
      if (rc == -1)
        {
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
          return;
        }
      return;
    }

  /* File is too long - use ftruncate if available */
#ifdef HAVE_FTRUNCATE
  rc = ftruncate(native_fd, native_len);
  if (rc == -1)
    {
      JCL_ThrowException(env, "java/io/IOException", strerror(errno));      
      return;
    }
#else /* HAVE_FTRUNCATE */
  /* FIXME: Probably operation isn't supported, but this exception
   * is too harsh as it will probably crash the program without need
  JCL_ThrowException(env, "java/lang/UnsupportedOperationException",
    "not implemented - can't shorten files on this platform");
  */
  JCL_ThrowException(env, "java/io/IOException", 
                     "Unable to shorten file length");
#endif /* HAVE_FTRUNCATE */
}

/*************************************************************************/
/*
 * Test file descriptor for validity
 * Exception on error
 */
JNIEXPORT jboolean JNICALL
Java_java_io_FileDescriptor_nativeValid(JNIEnv *env, jobject obj, jlong fd)
{
  int rc, native_fd;
  struct stat sb;

  native_fd = CONVERT_JLONG_TO_INT(fd);

  rc = fstat(native_fd, &sb);
  if (rc == -1)
    return(false);
  else
    return(true);
}

/*************************************************************************/
/*
 * Flush data to deks
 * Exception on error
 */
JNIEXPORT void JNICALL
Java_java_io_FileDescriptor_nativeSync(JNIEnv *env, jobject obj, jlong fd)
{
  int rc, native_fd;

  native_fd = CONVERT_JLONG_TO_INT(fd);

#ifdef HAVE_FSYNC
  rc = fsync(native_fd);
  /* FIXME: gcj does not throw an exception on EROFS or EINVAL.
   * Should we emulate? */
  if (rc == -1)
    JCL_ThrowException(env, "java/io/SyncFailedException", strerror(errno));
#else
  JCL_ThrowException(env, "java/io/SyncFailedException", 
                     "Sync not supported");
#endif
}

