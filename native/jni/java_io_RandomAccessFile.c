/* RandomAccessFile.c - Native methods for java.io.RandomAccessFile
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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>

#include <jni.h>

#include "java_io_RandomAccessFile.h"

#include "javaio.h"

/*************************************************************************/

/*
 * Returns the length of the file being read.
 */

JNIEXPORT jlong JNICALL
Java_java_io_RandomAccessFile_lengthInternal(JNIEnv *env, jobject obj, jint fd)
{
  return(_javaio_get_file_length(env, fd));
}

/*************************************************************************/

/*
 * Method to skip bytes in a file.
 */

JNIEXPORT jint JNICALL
Java_java_io_RandomAccessFile_skipInternal(JNIEnv *env, jobject obj, jint fd,
                                           jint num_bytes)
{
  return(_javaio_skip_bytes(env, fd, num_bytes));
}

/*************************************************************************/

/*
 * Opens the file for reading
 */

JNIEXPORT jint JNICALL
Java_java_io_RandomAccessFile_open(JNIEnv *env, jobject obj, jstring name,
                                   jboolean read_only)
{
  if (read_only)
    return(_javaio_open(env, name, O_RDONLY));
  else
    return(_javaio_open(env, name, O_RDWR|O_CREAT));
}

/*************************************************************************/

/*
 * Closes the file
 */

JNIEXPORT void JNICALL
Java_java_io_RandomAccessFile_closeInternal(JNIEnv *env, jobject obj, jint fd)
{
  _javaio_close(env, fd);
}

/*************************************************************************/

/*
 * Reads bytes from the file
 */ 

JNIEXPORT jint JNICALL
Java_java_io_RandomAccessFile_readInternal(JNIEnv *env, jobject obj, jint fd,
                                           jarray buf, jint offset, jint len)
{
  return(_javaio_read(env, obj, fd, buf, offset, len));
}

/*************************************************************************/

/*
 * Write bytes to the file
 */ 

JNIEXPORT void JNICALL
Java_java_io_RandomAccessFile_writeInternal(JNIEnv *env, jobject obj, jint fd,
                                            jarray buf, jint offset, jint len)
{
  _javaio_write(env, obj, fd, buf, offset, len);
}

/*************************************************************************/

/*
 * This method returns the current position in the file
 */

JNIEXPORT jlong JNICALL
Java_java_io_RandomAccessFile_getFilePointerInternal(JNIEnv *env, jobject obj,
                                                     jint fd)
{
  int rc = lseek(fd, 0, SEEK_CUR);
  if (rc == -1)
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));

  return(rc);
}

/*************************************************************************/

/*
 * This method seeks to the specified position from the beginning of the file
 */

JNIEXPORT void JNICALL
Java_java_io_RandomAccessFile_seekInternal(JNIEnv *env, jobject obj,
                                           jint fd, jlong pos)
{
  int rc = lseek(fd, pos, SEEK_SET);
  if (rc == -1)
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));
}

/*************************************************************************/

/* 
 * This method sets the length of the file.  Hmm.  Do all platforms have
 * ftruncate?  Probably not so we migth have to do some non-atomic stuff
 * on those
 */

JNIEXPORT void JNICALL
Java_java_io_RandomAccessFile_setLengthInternal(JNIEnv *env, jobject obj,
                                                jint fd, jlong len)
{
  int rc;

  jlong cur_len = _javaio_get_file_length(env, fd);
  if (cur_len == -1)
    return;

  if (cur_len > len)
    rc = ftruncate(fd, len);
  else if (cur_len < len)
    rc = lseek(fd, len - cur_len, SEEK_CUR);
  else
    return;

  if (rc == -1)
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));
}

