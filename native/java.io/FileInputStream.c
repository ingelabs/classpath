/*************************************************************************
 * FileInputStream.c - Native methods for java.io.FileInputStream class
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by Aaron M. Renn (arenn@urbanophile.com)
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>

#include <jni.h>

#include "javaio.h"

/*************************************************************************/

/*
 * Returns the length of the file being read.
 */

JNIEXPORT jlong JNICALL
Java_java_io_FileInputStream_getFileLength(JNIEnv *env, jobject obj, jint fd)
{
  return(_javaio_get_file_length(env, fd));
}

/*************************************************************************/

/*
 * Method to skip bytes in a file
 */

JNIEXPORT jlong JNICALL
Java_java_io_FileInputStream_skipInternal(JNIEnv *env, jobject obj, jint fd,
                                          jlong num_bytes)
{
  _javaio_skip_bytes(env, fd, num_bytes);
  return(num_bytes);
}

/*************************************************************************/

/*
 * Opens the file for reading
 */

JNIEXPORT jint JNICALL
Java_java_io_FileInputStream_open(JNIEnv *env, jobject obj, jstring name)
{
  return(_javaio_open(env, name, O_RDONLY));
}

/*************************************************************************/

/*
 * Reads bytes from the file
 */ 

JNIEXPORT jint JNICALL
Java_java_io_FileInputStream_readInternal(JNIEnv *env, jobject obj, jint fd,
                                          jarray buf, jint offset, jint len)
{
  return(_javaio_read(env, obj, fd, buf, offset, len));
}

/*************************************************************************/

/*
 * Closes the file
 */

JNIEXPORT void JNICALL
Java_java_io_FileInputStream_closeInternal(JNIEnv *env, jobject obj, jint fd)
{
  return(_javaio_close(env, fd));
}


