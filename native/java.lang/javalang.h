/* javalang.h - Declaration for common java.lang native functions
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

