/*************************************************************************
 * javalang.h - Declaration for common java.lang native functions
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

#include <jni.h>

#ifndef JAVALANG_H_INCLUDED
#define JAVALANG_H_INCLUDED

/*
 * Function Prototypes
 */
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL _javalang_ThrowException(JNIEnv *, char *, char *);
JNIEXPORT void * JNICALL _javalang_malloc(JNIEnv *, size_t);
JNIEXPORT void * JNICALL _javalang_realloc(JNIEnv *, void *, size_t);
JNIEXPORT void JNICALL _javalang_free(JNIEnv *, void *);

#ifdef __cplusplus
}
#endif

#ifndef TRUE
#define TRUE 1
#endif

#ifndef FALSE
#define FALSE 0
#endif

#endif /* JAVALANG_H_INCLUDED */

