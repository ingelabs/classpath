/*************************************************************************
 * javaio.c - Common java.io native functions
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
#include <sys/stat.h>

#include <jni.h>

#include "javaio.h"

#include <malloc.h>

static char errstr[4098]; // this way the memory is pre-allocated, so that we do not have to worry if we are out of memory.

JNIEXPORT void JNICALL _javaio_ThrowException(JNIEnv * env, char * className, char * errMsg) {
	jclass excClass;
	if((*env)->ExceptionOccurred(env)) {
		(*env)->ExceptionClear(env);
	}
	excClass = (*env)->FindClass(env, className);
	if(excClass == NULL) {
		jclass errExcClass;
		errExcClass = (*env)->FindClass(env, "java/lang/ClassNotFoundException");
		if(errExcClass == NULL) {
			errExcClass = (*env)->FindClass(env, "java/lang/InternalError");
			if(errExcClass == NULL) {
				sprintf(errstr,"JCL: Utterly failed to throw exeption %s with message %s.",className,errMsg);
				fprintf(stderr, errstr);
				return;
			}
		}
		sprintf(errstr,"JCL: Failed to throw exception %s with message %s: could not find exception class.", className, errMsg);
		(*env)->ThrowNew(env, errExcClass, errstr);
	}
	(*env)->ThrowNew(env, excClass, errMsg);
}

JNIEXPORT void * JNICALL _javaio_malloc(JNIEnv * env, size_t size) {
	void * mem = malloc(size);
	if(mem == NULL) {
		_javaio_ThrowException(env, "java/lang/OutOfMemoryError", "malloc() failed.");
		return NULL;
	}
	return mem;
}

JNIEXPORT void * JNICALL
_javaio_realloc(JNIEnv *env, void *ptr, size_t size)
{
  ptr = realloc(ptr, size);
  if (ptr == 0)
    {
      _javaio_ThrowException(env, "java/lang/OutOfMemoryError", 
                             "malloc() failed.");
      return NULL;
    }
  return(ptr);
}

JNIEXPORT void JNICALL _javaio_free(JNIEnv * env, void * p) {
	if(p != NULL)
		free(p);
}

JNIEXPORT char * JNICALL _javaio_jstring_to_cstring(JNIEnv * env, jstring s) {
	char* cstr;
	if(s == NULL) {
		_javaio_ThrowException(env, "java/lang/NullPointerException","Null string");
		return NULL;
	}
	cstr = (char*)(*env)->GetStringUTFChars(env, s, NULL);
	if(cstr == NULL) {
		_javaio_ThrowException(env, "java/lang/InternalError", "GetStringUTFChars() failed.");
		return NULL;
	}
	return cstr;
}

JNIEXPORT void JNICALL _javaio_free_cstring(JNIEnv * env, jstring s, char * cstr) {
	(*env)->ReleaseStringUTFChars(env, s, cstr);
}

JNIEXPORT jint JNICALL _javaio_MonitorEnter(JNIEnv * env, jobject o) {
	jint retval = (*env)->MonitorEnter(env,o);
	if(retval != 0) {
		_javaio_ThrowException(env, "java/lang/InternalError", "MonitorEnter() failed.");
	}
	return retval;
}

JNIEXPORT jint JNICALL _javaio_MonitorExit(JNIEnv * env, jobject o) {
	jint retval = (*env)->MonitorExit(env,o);
	if(retval != 0) {
		_javaio_ThrowException(env, "java/lang/InternalError", "MonitorExit() failed.");
	}
	return retval;
}

JNIEXPORT jclass JNICALL _javaio_FindClass(JNIEnv * env, char * className) {
	jclass retval = (*env)->FindClass(env,className);
	if(retval != NULL) {
		_javaio_ThrowException(env, "java/lang/ClassNotFoundException", className);
	}
	return retval;
}

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

void
_javaio_skip_bytes(JNIEnv *env, jint fd, jlong num_bytes)
{
  int rc;

  rc = lseek(fd, num_bytes, SEEK_CUR);
  if (rc == -1)
    _javaio_ThrowException(env, "java/io/IOException", strerror(errno));
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

  bufptr = (*env)->GetByteArrayElements(env, buf, 0);
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

