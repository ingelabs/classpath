/*************************************************************************
 * javalang.c - Common java.lang native functions (ripped from javaio.c)
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by Aaron M. Renn (arenn@urbanophile.com)
 * Modified slightly by Brian Jones (cbj@gnu.org)
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
#include <unistd.h>

#include <jni.h>

#include "javalang.h"

#include <malloc.h>

static char errstr[4098]; // this way the memory is pre-allocated, so that we do not have to worry if we are out of memory.

JNIEXPORT void JNICALL _javalang_ThrowException(JNIEnv * env, char * className, char * errMsg) 
{
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

JNIEXPORT void * JNICALL _javalang_malloc(JNIEnv * env, size_t size) 
{
    void * mem = malloc(size);
    if(mem == NULL) {
	_javalang_ThrowException(env, "java/lang/OutOfMemoryError", "malloc() failed.");
	return NULL;
    }
    return mem;
}

JNIEXPORT void * JNICALL
    _javalang_realloc(JNIEnv *env, void *ptr, size_t size)
{
    ptr = realloc(ptr, size);
    if (ptr == 0)
	{
	    _javalang_ThrowException(env, "java/lang/OutOfMemoryError",
				   "malloc() failed.");
	    return NULL;
	}
    return(ptr);
}


JNIEXPORT void JNICALL _javalang_free(JNIEnv * env, void * p) {
    if(p != NULL)
	free(p);
}
