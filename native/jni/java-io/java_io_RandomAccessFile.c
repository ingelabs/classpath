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


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>

#include <jni.h>
#include <jcl.h>
#include "java_io_RandomAccessFile.h"

#include "javaio.h"

/*************************************************************************/

/*
 * Returns the length of the file being read.
 *
 * Class:     java_io_RandomAccessFile
 * Method:    lengthInternal
 * Signature: (I)J
 */

JNIEXPORT jlong JNICALL
Java_java_io_RandomAccessFile_lengthInternal(JNIEnv *env, jobject obj, jint fd)
{
  return(_javaio_get_file_length(env, fd));
}

/*************************************************************************/

/*
 * Method to skip bytes in a file.
 *
 * Class:     java_io_RandomAccessFile
 * Method:    skipInternal
 * Signature: (II)I
 */

JNIEXPORT jint JNICALL
Java_java_io_RandomAccessFile_skipInternal(JNIEnv *env, jobject obj, jint fd,
                                           jint num_bytes)
{
  return(_javaio_skip_bytes(env, fd, num_bytes));
}

/*************************************************************************/

/*
 * Opens the file for reading.
 *
 * Class:     java_io_RandomAccessFile
 * Method:    open
 * Signature: (Ljava/lang/String;Z)I
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
 * Closes the file.
 *
 * Class:     java_io_RandomAccessFile
 * Method:    closeInternal
 * Signature: (I)V
 */

JNIEXPORT void JNICALL
Java_java_io_RandomAccessFile_closeInternal(JNIEnv *env, jobject obj, jint fd)
{
  _javaio_close(env, fd);
}

/*************************************************************************/

/*
 * Reads bytes from the file.
 *
 * Class:     java_io_RandomAccessFile
 * Method:    readInternal
 * Signature: (I[BII)I
 */ 

JNIEXPORT jint JNICALL
Java_java_io_RandomAccessFile_readInternal(JNIEnv *env, jobject obj, jint fd,
                                           jarray buf, jint offset, jint len)
{
  return(_javaio_read(env, obj, fd, buf, offset, len));
}

/*************************************************************************/

/*
 * Write bytes to the file.
 *
 * Class:     java_io_RandomAccessFile
 * Method:    writeInternal
 * Signature: (I[BII)V
 */ 

JNIEXPORT void JNICALL
Java_java_io_RandomAccessFile_writeInternal(JNIEnv *env, jobject obj, jint fd,
                                            jarray buf, jint offset, jint len)
{
  _javaio_write(env, obj, fd, buf, offset, len);
}

/*************************************************************************/

/*
 * This method returns the current position in the file.
 *
 * Class:     java_io_RandomAccessFile
 * Method:    getFilePointerInternal
 * Signature: (I)J
 */

JNIEXPORT jlong JNICALL
Java_java_io_RandomAccessFile_getFilePointerInternal(JNIEnv *env, jobject obj,
                                                     jint fd)
{
  int rc = lseek(fd, 0, SEEK_CUR);
  if (rc == -1)
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));

  return(rc);
}

/*************************************************************************/

/*
 * This method seeks to the specified position from the beginning of the file.
 *
 * Class:     java_io_RandomAccessFile
 * Method:    seekInternal
 * Signature: (IJ)V
 */

JNIEXPORT void JNICALL
Java_java_io_RandomAccessFile_seekInternal(JNIEnv *env, jobject obj,
                                           jint fd, jlong pos)
{
  int rc = lseek(fd, pos, SEEK_SET);
  if (rc == -1)
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));
}

/*************************************************************************/

/* 
 * This method sets the length of the file.  Hmm.  Do all platforms have
 * ftruncate?  Probably not so we migth have to do some non-atomic stuff
 * on those.
 *
 * Class:     java_io_RandomAccessFile
 * Method:    setLengthInternal
 * Signature: (IJ)V
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
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));
}

