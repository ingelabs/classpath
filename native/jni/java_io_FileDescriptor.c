/* FileDescriptor.c - Native methods for java.io.FileDescriptor class
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
#include "java_io_FileDescriptor.h"
#include "javaio.h"

/*************************************************************************/

/*
 * Method to force all data for this descriptor to be flushed to disk 
 */

JNIEXPORT void JNICALL
Java_java_io_FileDescriptor_syncInternal(JNIEnv *env, jobject obj, jint fd)
{
  int rc;

  rc = fsync(fd); 
  if (rc == -1) 
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));
}

/*************************************************************************/

/*
 * Method to check if a given descriptor is valid.
 */

JNIEXPORT jboolean JNICALL
Java_java_io_FileDescriptor_validInternal(JNIEnv *env, jobject obj, jint fd)
{
  int rc;

  /* Try a miscellaneous operation */
  rc = fcntl(fd, F_GETFL, 0);
  if (rc == -1) 
    return(0);
  else
    return(1);
}

