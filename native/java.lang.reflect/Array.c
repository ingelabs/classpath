/* java.lang.reflect.Array native functions
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

/*
 * java.lang.reflect.Array native functions.
 * Author: John Keiser
 * Version: 1.1.0
 * Date: 2 Jun 1998
 */
#include "java_lang_reflect_Array.h"

/*
 * Class:     java_lang_reflect_Array
 * Method:    getLength
 * Signature: (Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_java_lang_reflect_Array_getLength
  (JNIEnv * env, jclass thisClass, jobject arr) {
	return (*env)->GetArrayLength(env, arr);
}

/*
 * Class:     java_lang_reflect_Array
 * Method:    createObjectArray
 * Signature: (Ljava/lang/Class;I)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_java_lang_reflect_Array_createObjectArray
  (JNIEnv * env, jclass thisClass, jclass arrayType, jint arrayLength) {
	return (jobject)(*env)->NewObjectArray(env,arrayLength,arrayType,NULL);
}
