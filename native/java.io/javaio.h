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
extern void _javaio_skip_bytes(JNIEnv *, jint, jlong);
extern jint _javaio_open(JNIEnv *, jstring, int);
extern void _javaio_close(JNIEnv *, jint fd);
extern jint _javaio_read(JNIEnv *, jobject obj, jint, jarray, jint, jint);
extern jint _javaio_write(JNIEnv *, jobject obj, jint, jarray, jint, jint);


#endif /* JAVAIO_H_INCLUDED */

