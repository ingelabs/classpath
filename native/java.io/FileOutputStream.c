/* FileOutputStream.c - Native methods for java.io.FileOutputStream class
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

#include "javaio.h"

#include "java_io_FileOutputStream.h"

/*************************************************************************/

/*
 * Opens the file for writing
 */

JNIEXPORT jint JNICALL
Java_java_io_FileOutputStream_open(JNIEnv *env, jobject obj, jstring name,
                                   jboolean append)
{
  int fd = _javaio_open(env, name, O_RDWR|O_CREAT);

  if ((append) && (fd != -1))
    {
      int rc = lseek(fd, 0, SEEK_END);
      if (rc == -1)
        {
          _javaio_ThrowException(env, "java/io/IOException", strerror(errno));
          close(fd);
          return(-1);
        }
    }

  return(fd);
}

/*************************************************************************/

/*
 * Write bytes to file
 */ 

JNIEXPORT void JNICALL
Java_java_io_FileOutputStream_writeInternal(JNIEnv *env, jobject obj, jint fd,
                                            jarray buf, jint offset, jint len)
{
  _javaio_write(env, obj, fd, buf, offset, len);
}

/*************************************************************************/

/*
 * Closes the file
 */

JNIEXPORT void JNICALL
Java_java_io_FileOutputStream_closeInternal(JNIEnv *env, jobject obj, jint fd)
{
  _javaio_close(env, fd);
}


