/*************************************************************************
 * FileOutputStream.c - Native methods for java.io.FileOutputStream class
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
#include "jcl.h"

#include "java_io_FileOutputStream.h"
#include "java_io_FileOutputStream_stubs.c"

#include "javaio.h"

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
          JCL_ThrowException(env, "java/io/IOException", strerror(errno));
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
  return(_javaio_close(env, fd));
}


