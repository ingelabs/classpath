/* jcl.h
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

#ifndef __JCL_H__
#define __JCL_H__

#include <jni.h>

JNIEXPORT jclass JNICALL JCL_FindClass(JNIEnv * env, char * className);
JNIEXPORT void JNICALL JCL_ThrowException(JNIEnv * env, char * className, char * errMsg);
JNIEXPORT void * JNICALL JCL_malloc(JNIEnv * env, size_t size);
JNIEXPORT void JNICALL JCL_free(JNIEnv * env, void * p);
JNIEXPORT char * JNICALL JCL_jstring_to_cstring(JNIEnv * env, jstring s);
JNIEXPORT void JNICALL JCL_free_cstring(JNIEnv * env, jstring s, char * cstr);
JNIEXPORT jint JNICALL JCL_MonitorEnter(JNIEnv * env, jobject o);
JNIEXPORT jint JNICALL JCL_MonitorExit(JNIEnv * env, jobject o);

#define JCL_RETHROW_EXCEPTION(env) if((*(env))->ExceptionOccurred((env)) != NULL) return NULL;

#ifndef TRUE
#define TRUE 1
#endif

#ifndef FALSE
#define FALSE 0
#endif

#endif
