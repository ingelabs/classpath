/*************************************************************************
 * javaio.h - Declaration for common java.io native functions
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

