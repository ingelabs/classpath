/* javaio.c - Common java.io native functions
   Copyright (C) 1998, 2002 Free Software Foundation, Inc.

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
#include <sys/stat.h>
#include <malloc.h>

#include <jni.h>
#include <jcl.h>

#include "javaio.h"


/*
 * Function to open a file
 */

jint
_javaio_open(JNIEnv *env, jstring name, int flags)
{
  char *str_name;
  int fd;

  str_name = JCL_jstring_to_cstring(env, name); 
  if (!str_name)
    return(-1);

  fd = open(str_name, flags, 0777);
  (*env)->ReleaseStringUTFChars(env, name, str_name);
  if (fd == -1)
    {
      if (errno == ENOENT)
        JCL_ThrowException(env, "java/io/FileNotFoundException", 
                           strerror(errno));
      else
        JCL_ThrowException(env, "java/io/IOException", strerror(errno));
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
  int rc = 0;

  if (fd != -1)
    rc = close(fd);

  if (rc == -1)
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));
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
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));

  new = lseek(fd, num_bytes, SEEK_CUR);
  if (new == -1)
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));

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
      JCL_ThrowException(env, "java/io/IOException", strerror(errno));
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

  if (len == 0)
    return 0; /* Nothing todo, and GetByteArrayElements() seems undefined. */

  bufptr = (*env)->GetByteArrayElements(env, buf, JNI_FALSE);
  if (!bufptr)
    {
      JCL_ThrowException(env, "java/io/IOException", "Internal Error");
      return(-1);
    }

  rc = read(fd, (bufptr + offset), len);
  if (rc == -1)
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));

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

  if (len == 0)
    return 0; /* Nothing todo, and GetByteArrayElements() seems undefined. */

  bufptr = (*env)->GetByteArrayElements(env, buf, 0);
  if (!bufptr)
    {
      JCL_ThrowException(env, "java/io/IOException", "Internal Error");
      return(-1);
    }

  rc = write(fd, (bufptr + offset), len);
  if (rc == -1)
    JCL_ThrowException(env, "java/io/IOException", strerror(errno));

  (*env)->ReleaseByteArrayElements(env, buf, bufptr, 0);

  if (rc == 0)
    rc = -1;

  return(rc);
}





