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
 * Some of this coded adopted from the gcj native libraries
 */

/* do not move; needed here because of some macro definitions */
#include <config.h>

#include <stdlib.h>

/* FIXME: Need to make configure set these for us */
/* #define HAVE_SYS_IOCTL_H */
/* #define HAVE_SYS_FILIO_H */

#include <jni.h>
#include "jcl.h"

#include "target_native.h"
#ifndef WITHOUT_FILESYSTEM
#include "target_native_file.h"
#endif
#include "target_native_math_int.h"

#include "java_io_FileDescriptor.h"

// FIXME: This can't be right.  Need converter macros
#define CONVERT_JLONG_TO_INT(x) TARGET_NATIVE_MATH_INT_INT64_TO_INT32(x)
#define CONVERT_INT_TO_JLONG(x) TARGET_NATIVE_MATH_INT_INT32_TO_INT64(x)

// FIXME: This can't be right.  Need converter macros
#define CONVERT_JLONG_TO_OFF_T(x) TARGET_NATIVE_MATH_INT_INT64_TO_INT32(x)
#define CONVERT_OFF_T_TO_JLONG(x) TARGET_NATIVE_MATH_INT_INT32_TO_INT64(x)

// FIXME: This can't be right.  Need converter macros
#define CONVERT_JINT_TO_INT(x) ((int)(x & 0xFFFFFFFF))
#define CONVERT_INT_TO_JINT(x) ((int)(x & 0xFFFFFFFF))

// FIXME: This can't be right.  Need converter macros
#define CONVERT_SSIZE_T_TO_JINT(x) ((jint)(x & 0xFFFFFFFF))
#define CONVERT_JINT_TO_SSIZE_T(x) (x)

/* These values must be kept in sync with FileDescriptor.java.  */
#define FILEDESCRIPTOR_FILESEEK_SET          0
#define FILEDESCRIPTOR_FILESEEK_CUR          1
#define FILEDESCRIPTOR_FILESEEK_END          2

#define FILEDESCRIPTOR_FILEOPEN_FLAG_READ    1
#define FILEDESCRIPTOR_FILEOPEN_FLAG_WRITE   2
#define FILEDESCRIPTOR_FILEOPEN_FLAG_APPEND  4
#define FILEDESCRIPTOR_FILEOPEN_FLAG_EXCL    8
#define FILEDESCRIPTOR_FILEOPEN_FLAG_SYNC   16
#define FILEDESCRIPTOR_FILEOPEN_FLAG_DSYNC  32

/*************************************************************************/

/*
 * Library initialization routine.  Called as part of java.io.FileDescriptor
 * static initialization.
 */
JNIEXPORT void JNICALL
Java_java_io_FileDescriptor_nativeInit (JNIEnv *env, jclass clazz)
{
  jfieldID field, filedes_field;
  jobject filedes;

  filedes_field = (*env)->GetFieldID (env, clazz, "nativeFd", "J");
  if (!filedes_field)
    return;

#define INIT_FIELD(FIELDNAME, FDVALUE)					\
  field = (*env)->GetStaticFieldID (env, clazz, FIELDNAME,		\
				    "Ljava/io/FileDescriptor;");	\
  if (! field)								\
    return;								\
  filedes = (*env)->GetStaticObjectField (env, clazz, field);           \
  if (! filedes)							\
    return;								\
  (*env)->SetLongField (env, filedes, filedes_field, (jlong) FDVALUE);  \
  if ((*env)->ExceptionOccurred (env))                                  \
    return;

  INIT_FIELD ("in", 0);
  INIT_FIELD ("out", 1);
  INIT_FIELD ("err", 2);

#undef INIT_FIELD
}

/*************************************************************************/
/*
 * Open the specified file and return a native file descriptor
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeOpen (JNIEnv *env, jobject obj,
					jstring name, jint jflags)
{
  const char *filename;
  int flags;
  int permissions;
  int native_fd;
  int result;

  filename = JCL_jstring_to_cstring (env, name);
  if (filename == NULL)
    return (-1);		/* Exception will already have been thrown */

  /* get file/permission flags for open() */
  if ((jflags & FILEDESCRIPTOR_FILEOPEN_FLAG_READ)
      && (jflags & FILEDESCRIPTOR_FILEOPEN_FLAG_WRITE))
    {
      /* read/write */
      flags =
	TARGET_NATIVE_FILE_FILEFLAG_CREATE |
	TARGET_NATIVE_FILE_FILEFLAG_READWRITE;
      permissions = TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL;
    }
  else if ((jflags & FILEDESCRIPTOR_FILEOPEN_FLAG_READ))
    {
      /* read */
      flags = TARGET_NATIVE_FILE_FILEFLAG_READ;
      permissions = TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL;
    }
  else
    {
      /* write */
      flags =
	TARGET_NATIVE_FILE_FILEFLAG_CREATE |
	TARGET_NATIVE_FILE_FILEFLAG_WRITE;
      if ((jflags & FILEDESCRIPTOR_FILEOPEN_FLAG_APPEND))
	{
	  flags |= TARGET_NATIVE_FILE_FILEFLAG_APPEND;
	}
      else
	{
	  flags |= TARGET_NATIVE_FILE_FILEFLAG_TRUNCATE;
	}
      permissions = TARGET_NATIVE_FILE_FILEPERMISSION_NORMAL;
    }

  if ((jflags & FILEDESCRIPTOR_FILEOPEN_FLAG_SYNC))
    {
      flags |= TARGET_NATIVE_FILE_FILEFLAG_SYNC;
    }

  if ((jflags & FILEDESCRIPTOR_FILEOPEN_FLAG_DSYNC))
    {
      flags |= TARGET_NATIVE_FILE_FILEFLAG_DSYNC;
    }
#ifdef O_BINARY
  flags |= TARGET_NATIVE_FILE_FILEFLAG_BINARY;
#endif

  TARGET_NATIVE_FILE_OPEN (filename, native_fd, flags, permissions, result);
  (*env)->ReleaseStringUTFChars (env, name, filename);

  if (result != TARGET_NATIVE_OK)
    {
      /* We can only throw FileNotFoundException.  */
      JCL_ThrowException (env,
			  "java/io/FileNotFoundException",
			  TARGET_NATIVE_LAST_ERROR_STRING ());
      return TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1;
    }

  return CONVERT_INT_TO_JLONG (native_fd);
}

/*************************************************************************/
/*
 * Closes the specified file descriptor and return status code.
 * Exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeClose (JNIEnv *env, jobject obj, jlong fd)
{
  int native_fd;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

  TARGET_NATIVE_FILE_CLOSE (native_fd, result);
  if (result != TARGET_NATIVE_OK)
    {
      JCL_ThrowException (env,
			  "java/io/IOException",
			  TARGET_NATIVE_LAST_ERROR_STRING ());
      return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
    }

  return (TARGET_NATIVE_MATH_INT_INT64_CONST_0);
}

/*************************************************************************/
/*
 * Writes a single byte to the specified file descriptor
 * Return status code, exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeWriteByte (JNIEnv *env, jobject obj,
					     jlong fd, jint b)
{
  int native_fd;
  char native_data;
  ssize_t bytes_written;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);
  native_data = (char) (CONVERT_JINT_TO_INT (b) & 0xFF);

  do
    {
      TARGET_NATIVE_FILE_WRITE (native_fd, &native_data, 1, bytes_written,
				result);
      if ((result != TARGET_NATIVE_OK)
	  && (TARGET_NATIVE_LAST_ERROR () !=
	      TARGET_NATIVE_ERROR_INTERRUPT_FUNCTION_CALL))
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
	}
    }
  while (result != TARGET_NATIVE_OK);

  return (TARGET_NATIVE_MATH_INT_INT64_CONST_0);
}

/*************************************************************************/
/*
 * Writes a byte buffer to the specified file descriptor
 * Return status code, exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeWriteBuf (JNIEnv *env, jobject obj,
					    jlong fd, jbyteArray buf,
					    jint offset, jint len)
{
  int native_fd;
  jbyte *bufptr;
  ssize_t bytes_written;
  ssize_t n;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

  bufptr = (*env)->GetByteArrayElements (env, buf, 0);
  if (!bufptr)
    {
      JCL_ThrowException (env, "java/io/IOException", "Unexpected JNI error");
      return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
    }

  bytes_written = 0;
  while (bytes_written < CONVERT_JINT_TO_SSIZE_T (len))
    {
      TARGET_NATIVE_FILE_WRITE (native_fd, (bufptr + offset + bytes_written),
				(len - bytes_written), n, result);
      if ((result != TARGET_NATIVE_OK)
	  && (TARGET_NATIVE_LAST_ERROR () !=
	      TARGET_NATIVE_ERROR_INTERRUPT_FUNCTION_CALL))
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  (*env)->ReleaseByteArrayElements (env, buf, bufptr, 0);
	  return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
	}
      bytes_written += n;
    }

  (*env)->ReleaseByteArrayElements (env, buf, bufptr, 0);

  return (TARGET_NATIVE_MATH_INT_INT64_CONST_0);
}

/*************************************************************************/
/*
 * Read a single byte from the file descriptor
 * Return byte read or -1 on eof, exception on error
 */
JNIEXPORT jint JNICALL
Java_java_io_FileDescriptor_nativeReadByte (JNIEnv *env, jobject obj,
					    jlong fd)
{
  int native_fd;
  char data;
  ssize_t bytes_read;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

  bytes_read = 0;
  do
    {
      TARGET_NATIVE_FILE_READ (native_fd, &data, 1, bytes_read, result);
      if ((result == TARGET_NATIVE_OK) && (bytes_read == 0))
	{
	  return (-1);
	}
      if ((result != TARGET_NATIVE_OK)
	  && (TARGET_NATIVE_LAST_ERROR () !=
	      TARGET_NATIVE_ERROR_INTERRUPT_FUNCTION_CALL))
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  return (-1);
	}
    }
  while (bytes_read != 1);

  return ((jint) (data & 0xFF));
}

/*************************************************************************/
/*
 * Reads to a byte buffer from the specified file descriptor
 * Return number of bytes read or -1 on eof, exception on error
 */
JNIEXPORT jint JNICALL
Java_java_io_FileDescriptor_nativeReadBuf (JNIEnv *env, jobject obj,
					   jlong fd, jarray buf, jint offset,
					   jint len)
{
  int native_fd;
  jbyte *bufptr;
  ssize_t bytes_read;
  ssize_t n;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

  bufptr = (*env)->GetByteArrayElements (env, buf, 0);
  if (!bufptr)
    {
      JCL_ThrowException (env, "java/io/IOException", "Unexpected JNI error");
      return (-1);
    }

  bytes_read = 0;
  while (bytes_read < len)
    {
      TARGET_NATIVE_FILE_READ (native_fd, (bufptr + offset + bytes_read),
			       (len - bytes_read), n, result);
      if ((result == TARGET_NATIVE_OK) && (n == 0))
	{
	  (*env)->ReleaseByteArrayElements (env, buf, bufptr, 0);
	  if (bytes_read == 0)
	    return (-1);	/* Signal end of file to Java */
	  else
	    return (CONVERT_SSIZE_T_TO_JINT (bytes_read));
	}
      if ((result != TARGET_NATIVE_OK)
	  && (TARGET_NATIVE_LAST_ERROR () !=
	      TARGET_NATIVE_ERROR_INTERRUPT_FUNCTION_CALL))
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  (*env)->ReleaseByteArrayElements (env, buf, bufptr, 0);
	  return (-1);
	}
      bytes_read += n;
    }

  (*env)->ReleaseByteArrayElements (env, buf, bufptr, 0);
  return (CONVERT_SSIZE_T_TO_JINT (bytes_read));
}

/*************************************************************************/
/*
 * Return number of bytes that can be read from the file w/o blocking.
 * Exception on error
 */
JNIEXPORT jint JNICALL
Java_java_io_FileDescriptor_nativeAvailable (JNIEnv *env, jobject obj,
					     jlong fd)
{
  int native_fd;
  jlong bytes_available;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

  TARGET_NATIVE_FILE_AVAILABLE (native_fd, bytes_available, result);
  if (result != TARGET_NATIVE_OK)
    {
      JCL_ThrowException (env,
			  "java/io/IOException",
			  TARGET_NATIVE_LAST_ERROR_STRING ());
      return (0);
    }

// FIXME NYI ??? why only jint and not jlong?
  return (TARGET_NATIVE_MATH_INT_INT64_TO_INT32 (bytes_available));
}

/*************************************************************************/
/*
 * Wrapper around lseek call.  Return new file position
 * Exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeSeek (JNIEnv *env, jobject obj, jlong fd,
					jlong offset, jint whence,
					jboolean stop_at_eof)
{
  int native_fd;
  jlong file_size;
  jlong current_offset, new_offset;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

#if 0
  /* Should there be such an exception? All native layer macros should
     be accepting 64bit-values if needed. It some target is not able
     to handle such values it should simply operate with 32bit-values
     and convert 64bit-values appriopated. In this case I assume
     problems should not occurre: if some specific target is not able
     to handle 64bit-values the system is limited to 32bit at all, thus
     the application can not do a seek() or something else beyond the
     32bit limit. It this true?
   */

  /* FIXME: What do we do if offset > the max value of off_t on this 32bit
   * system?  How do we detect that and what do we do? */
  if (CONVERT_OFF_T_TO_JLONG (native_offset) != offset)
    {
      JCL_ThrowException (env, "java/io/IOException",
			  "Cannot represent position correctly on this system");
      return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
    }
#endif /* 0 */

  if (stop_at_eof)
    {
      /* get file size */
      TARGET_NATIVE_FILE_SIZE (native_fd, file_size, result);
      if (result != TARGET_NATIVE_OK)
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
	}

      /* set file read/write position (seek) */
      if (whence == FILEDESCRIPTOR_FILESEEK_SET)
	{
	  if (TARGET_NATIVE_MATH_INT_INT64_GT (offset, file_size))
	    offset = file_size;
	}
      else if (whence == FILEDESCRIPTOR_FILESEEK_CUR)
	{
	  TARGET_NATIVE_FILE_TELL (native_fd, current_offset, result);
	  if (result != TARGET_NATIVE_OK)
	    {
	      JCL_ThrowException (env,
				  "java/io/IOException",
				  TARGET_NATIVE_LAST_ERROR_STRING ());
	      return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
	    }
	  if (TARGET_NATIVE_MATH_INT_INT64_GT
	      (TARGET_NATIVE_MATH_INT_INT64_ADD (current_offset, offset),
	       file_size))
	    {
	      offset = file_size;
	      whence = FILEDESCRIPTOR_FILESEEK_SET;
	    }
	}
      else if (TARGET_NATIVE_MATH_INT_INT64_GT (offset, 0))	/* Default to END case */
	{
	  offset = TARGET_NATIVE_MATH_INT_INT64_CONST_0;
	}
    }

  /* Now do it */
  result = TARGET_NATIVE_ERROR;
  new_offset = TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1;
  if (whence == FILEDESCRIPTOR_FILESEEK_SET)
    TARGET_NATIVE_FILE_SEEK_BEGIN (native_fd, offset, new_offset, result);
  if (whence == FILEDESCRIPTOR_FILESEEK_CUR)
    TARGET_NATIVE_FILE_SEEK_CURRENT (native_fd, offset, new_offset, result);
  if (whence == FILEDESCRIPTOR_FILESEEK_END)
    TARGET_NATIVE_FILE_SEEK_END (native_fd, offset, new_offset, result);

  if (result != TARGET_NATIVE_OK)
    {
      JCL_ThrowException (env,
			  "java/io/IOException",
			  TARGET_NATIVE_LAST_ERROR_STRING ());
      return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
    }

  return (new_offset);
}

/*************************************************************************/
/*
 * Return the current position of the file pointer
 * Exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeGetFilePointer (JNIEnv *env, jobject obj,
						  jlong fd)
{
  int native_fd;
  jlong current_offset;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

  TARGET_NATIVE_FILE_TELL (native_fd, current_offset, result);
  if (result != TARGET_NATIVE_OK)
    {
      JCL_ThrowException (env,
			  "java/io/IOException",
			  TARGET_NATIVE_LAST_ERROR_STRING ());
      return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
    }

  return (current_offset);
}

/*************************************************************************/
/*
 * Return the length of the file
 * Exception on error
 */
JNIEXPORT jlong JNICALL
Java_java_io_FileDescriptor_nativeGetLength (JNIEnv *env, jobject obj,
					     jlong fd)
{
  int native_fd;
  jlong file_size;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

  TARGET_NATIVE_FILE_SIZE (native_fd, file_size, result);
  if (result != TARGET_NATIVE_OK)
    {
      JCL_ThrowException (env,
			  "java/io/IOException",
			  TARGET_NATIVE_LAST_ERROR_STRING ());
      return (TARGET_NATIVE_MATH_INT_INT64_CONST_MINUS_1);
    }

  return (file_size);
}

/*************************************************************************/
/*
 * Set the length of the file
 * Exception on error
 */
JNIEXPORT void JNICALL
Java_java_io_FileDescriptor_nativeSetLength (JNIEnv *env, jobject obj,
					     jlong fd, jlong len)
{
  int native_fd;
  jlong file_size;
  int bytes_written;
  jlong save_offset, new_offset;
  char data;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

#if 0
  /* Should there be such an exception? All native layer macros should
     be accepting 64bit-values if needed. It some target is not able
     to handle such values it should simply operate with 32bit-values
     and convert 64bit-values appriopated. In this case I assume
     problems should not occurre: if some specific target is not able
     to handle 64bit-values the system is limited to 32bit at all, thus
     the application can not do a seek() or something else beyond the
     32bit limit. It this true?
   */

  /* FIXME: What do we do if len > the max value of off_t on this 32bit
   * system?  How do we detect that and what do we do? */
  if (CONVERT_OFF_T_TO_JLONG (native_len) != len)
    {
      JCL_ThrowException (env, "java/io/IOException",
			  "Cannot represent position correctly on this system");
      return;
    }
#endif /* 0 */

  /* get file size */
  TARGET_NATIVE_FILE_SIZE (native_fd, file_size, result);
  if (result != TARGET_NATIVE_OK)
    {
      JCL_ThrowException (env,
			  "java/io/IOException",
			  TARGET_NATIVE_LAST_ERROR_STRING ());
      return;
    }

  if (TARGET_NATIVE_MATH_INT_INT64_LT (file_size, len))
    {
      /* File is too short -- seek to one byte short of where we want,
       * then write a byte */

      /* Save off current position */
      TARGET_NATIVE_FILE_TELL (native_fd, save_offset, result);
      if (result != TARGET_NATIVE_OK)
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  return;
	}

      /* move to position n-1 */
      TARGET_NATIVE_FILE_SEEK_BEGIN (native_fd,
				     TARGET_NATIVE_MATH_INT_INT64_SUB (len,
								       1),
				     new_offset, result);
      if (result != TARGET_NATIVE_OK)
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  return;
	}

      /* write a byte
         Note: This will fail if we somehow get here in read only mode
         * That shouldn't happen */
      data = '\0';
      TARGET_NATIVE_FILE_WRITE (native_fd, &data, 1, bytes_written, result);
      if (result != TARGET_NATIVE_OK)
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  return;
	}

      /* Reposition file pointer to where we started */
      TARGET_NATIVE_FILE_SEEK_BEGIN (native_fd, save_offset, new_offset,
				     result);
      if (result != TARGET_NATIVE_OK)
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  return;
	}
    }
  else if (TARGET_NATIVE_MATH_INT_INT64_GT (file_size, len))
    {
      /* File is too long - use ftruncate if available */
#ifdef HAVE_FTRUNCATE
      TARGET_NATIVE_FILE_TRUNCATE (native_fd, len, result);
      if (result != TARGET_NATIVE_OK)
	{
	  JCL_ThrowException (env,
			      "java/io/IOException",
			      TARGET_NATIVE_LAST_ERROR_STRING ());
	  return;
	}
#else /* HAVE_FTRUNCATE */
      /* FIXME: Probably operation isn't supported, but this exception
       * is too harsh as it will probably crash the program without need
       JCL_ThrowException(env, "java/lang/UnsupportedOperationException",
       "not implemented - can't shorten files on this platform");
       */
      JCL_ThrowException (env, "java/io/IOException",
			  "Unable to shorten file length");
#endif /* HAVE_FTRUNCATE */
    }
}

/*************************************************************************/
/*
 * Test file descriptor for validity
 */
JNIEXPORT jboolean JNICALL
Java_java_io_FileDescriptor_nativeValid (JNIEnv *env, jobject obj, jlong fd)
{
  int native_fd;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

  TARGET_NATIVE_FILE_VALID_FILE_DESCRIPTOR (native_fd, result);

  return ((result == TARGET_NATIVE_OK) ? JNI_TRUE : JNI_FALSE);
}

/*************************************************************************/
/*
 * Flush data to deks
 * Exception on error
 */
JNIEXPORT void JNICALL
Java_java_io_FileDescriptor_nativeSync (JNIEnv *env, jobject obj, jlong fd)
{
#ifdef HAVE_FSYNC
  int native_fd;
  int result;

  native_fd = CONVERT_JLONG_TO_INT (fd);

  TARGET_NATIVE_FILE_FSYNC (native_fd, result);
  /* FIXME: gcj does not throw an exception on EROFS or EINVAL.
   * Should we emulate? */
  if (result != TARGET_NATIVE_OK)
    {
      JCL_ThrowException (env,
			  "java/io/SyncFailedException",
			  TARGET_NATIVE_LAST_ERROR_STRING ());
    }
#else
  JCL_ThrowException (env, "java/io/SyncFailedException",
		      "Sync not supported");
#endif
}
