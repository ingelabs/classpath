/* javaio.h - Declaration for common java.io native functions
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


#ifndef JAVAIO_H_INCLUDED
#define JAVAIO_H_INCLUDED

/*
 * Function Prototypes
 */

extern jlong _javaio_get_file_length(JNIEnv *, jint);
extern jlong _javaio_skip_bytes(JNIEnv *, jint, jlong);
extern jint _javaio_open(JNIEnv *, jstring, int);
extern void _javaio_close(JNIEnv *, jint fd);
extern jint _javaio_read(JNIEnv *, jobject obj, jint, jarray, jint, jint);
extern jint _javaio_write(JNIEnv *, jobject obj, jint, jarray, jint, jint);

JNIEXPORT jclass JNICALL _javaio_FindClass(JNIEnv * env, char * className);
JNIEXPORT void JNICALL _javaio_ThrowException(JNIEnv * env, char * className, char * errMsg);
JNIEXPORT void * JNICALL _javaio_malloc(JNIEnv * env, size_t size);
JNIEXPORT void * JNICALL _javaio_realloc(JNIEnv * env, void *, size_t size);
JNIEXPORT void JNICALL _javaio_free(JNIEnv * env, void * p);
JNIEXPORT char * JNICALL _javaio_jstring_to_cstring(JNIEnv * env, jstring s);
JNIEXPORT void JNICALL _javaio_free_cstring(JNIEnv * env, jstring s, char * cstr);
JNIEXPORT jint JNICALL _javaio_MonitorEnter(JNIEnv * env, jobject o);
JNIEXPORT jint JNICALL _javaio_MonitorExit(JNIEnv * env, jobject o);

#define _javaio_RETHROW_EXCEPTION(env) if((*(env))->ExceptionOccurred((env)) != NULL) return NULL;

#ifndef TRUE
#define TRUE 1
#endif

#ifndef FALSE
#define FALSE 0
#endif

#endif /* JAVAIO_H_INCLUDED */

