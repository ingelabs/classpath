/* FileOutputStream.c - Native methods for java.io.FileOutputStream class
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

#include <jni.h>
#include <jcl.h>
#include "javaio.h"

#include "java_io_FileOutputStream.h"

/*************************************************************************/

/*
 * Opens the file for writing.
 *
 * Class:     java_io_FileOutputStream
 * Method:    open
 * Signature: (Ljava/lang/String;Z)I
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
          JCL_ThrowException(env, "java/io/FileNotFoundException",
			     strerror(errno));
          close(fd);
          return(-1);
        }
    }

  return(fd);
}

/*************************************************************************/

/*
 * Write bytes to file.
 *
 * Class:     java_io_FileOutputStream
 * Method:    writeInternal
 * Signature: (I[BII)V
 */ 

JNIEXPORT void JNICALL
Java_java_io_FileOutputStream_writeInternal(JNIEnv *env, jobject obj, jint fd,
                                            jarray buf, jint offset, jint len)
{
  _javaio_write(env, obj, fd, buf, offset, len);
}

/*************************************************************************/

/*
 * Closes the file.
 *
 * Class:     java_io_FileOutputStream
 * Method:    closeInternal
 * Signature: (I)V
 */

JNIEXPORT void JNICALL
Java_java_io_FileOutputStream_closeInternal(JNIEnv *env, jobject obj, jint fd)
{
  _javaio_close(env, fd);
}


