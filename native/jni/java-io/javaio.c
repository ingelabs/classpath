/* javaio.c - Common java.io native functions
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
#include <sys/stat.h>

#include <jni.h>

#include "javaio.h"

#include <malloc.h>

/*
 * Function to open a file
 */

jint
_javaio_open(JNIEnv *env, jstring name, int flags)
{
  char *str_name;
  int fd;

  str_name = _javaio_jstring_to_cstring(env, name); 
  if (!str_name)
    return(-1);

  fd = open(str_name, flags, 0777);
  (*env)->ReleaseStringUTFChars(env, name, str_name);
  if (fd == -1)
    {
      if (errno == ENOENT)
        _javaio_ThrowException(env, "java/io/FileNotFoundException", 
                           strerror(errno));
      else
        _javaio_ThrowException(env, "java/io/IOException", strerror(errno));
    }

  return(fd);
}

/*************************************************************************/

/*
 * Function to close a file
 */

void
_javaio_close(JNIEnv *env, jint fd)
{
  int rc;

  rc = close(fd);
  if (rc == -1)
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));
}

/*************************************************************************/

/*
 * Skips bytes in a file
 */

jlong
_javaio_skip_bytes(JNIEnv *env, jint fd, jlong num_bytes)
{
  int cur, new;

  cur = lseek(fd, 0, SEEK_CUR);
  if (cur == -1)
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));

  new = lseek(fd, num_bytes, SEEK_CUR);
  if (new == -1)
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));

  return(new - cur);
}

/*************************************************************************/

/*
 * Gets the size of the file
 */

jlong
_javaio_get_file_length(JNIEnv *env, jint fd)
{
  struct stat buf;
  int rc;

  rc = fstat(fd, &buf);
  if (rc == -1)
    {
      _javaio_ThrowException(env, "java/io/IOException", strerror(errno));
      return(-1);
    }

  return(buf.st_size);
}

/*************************************************************************/

/*
 * Reads data from a file
 */

jint
_javaio_read(JNIEnv *env, jobject obj, jint fd, jarray buf, jint offset,
             jint len)
{
  jbyte *bufptr;
  int rc;

  bufptr = (*env)->GetByteArrayElements(env, buf, JNI_FALSE);
  if (!bufptr)
    {
      _javaio_ThrowException(env, "java/io/IOException", "Internal Error");
      return(-1);
    }

  rc = read(fd, (bufptr + offset), len);
  if (rc == -1)
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));

  (*env)->ReleaseByteArrayElements(env, buf, bufptr, 0);

  if (rc == 0)
    rc = -1;

  return(rc);
}

/*************************************************************************/

/*
 * Writes data to a file
 */

jint
_javaio_write(JNIEnv *env, jobject obj, jint fd, jarray buf, jint offset,
              jint len)
{
  jbyte *bufptr;
  int rc;

  bufptr = (*env)->GetByteArrayElements(env, buf, 0);
  if (!bufptr)
    {
      _javaio_ThrowException(env, "java/io/IOException", "Internal Error");
      return(-1);
    }

  rc = write(fd, (bufptr + offset), len);
  if (rc == -1)
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));

  (*env)->ReleaseByteArrayElements(env, buf, bufptr, 0);

  if (rc == 0)
    rc = -1;

  return(rc);
}

