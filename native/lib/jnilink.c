/* JNILINK 1.1: JNI version.
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


#include "jnilink.h"
#include <string.h>
#include <jcl.h>

#include <malloc.h>

#define GETCLASS(c) *(jclass*)(c)

JNIEXPORT jclass JNICALL
LINK_RelinkClass     (JNIEnv * env, linkedClass * c, char * name) {
	jclass found;
	LINK_UnlinkClass(env,*c);

	found = (*env)->FindClass(env,name);
	if(found == NULL)
		return NULL;

	*c = JCL_malloc(env,sizeof(jclass));
	if(*c == NULL)
		return NULL;

	GETCLASS(*c) = (*env)->NewGlobalRef(env,found);
	return GETCLASS(*c);
}

JNIEXPORT jclass JNICALL
LINK_RelinkKnownClass(JNIEnv * env, linkedClass * c, jclass newClass) {
	LINK_UnlinkClass(env,*c);

	*c = JCL_malloc(env,sizeof(jclass));
	if(*c == NULL)
		return NULL;

	GETCLASS(*c) = (*env)->NewGlobalRef(env,newClass);
	return newClass;
}

JNIEXPORT jmethodID JNICALL
LINK_RelinkMethod      (JNIEnv * env, jmethodID * m, linkedClass c,
                        char * name, char * sig) {
	*m = (*env)->GetMethodID(env,GETCLASS(c),name,sig);
	return *m;
}

JNIEXPORT jmethodID JNICALL
LINK_RelinkStaticMethod(JNIEnv * env, jmethodID * m, linkedClass c,
                        char * name, char * sig) {
	*m = (*env)->GetStaticMethodID(env,GETCLASS(c),name,sig);
	return *m;
}

JNIEXPORT jfieldID JNICALL
LINK_RelinkField       (JNIEnv * env, jfieldID * f, linkedClass c,
                        char * name, char * sig) {
	*f = (*env)->GetFieldID(env,GETCLASS(c),name,sig);
	return *f;
}

JNIEXPORT jfieldID JNICALL
LINK_RelinkStaticField (JNIEnv * env, jfieldID * f, linkedClass c,
                        char * name, char * sig) {
	*f = (*env)->GetStaticFieldID(env,GETCLASS(c),name,sig);
	return *f;
}


/* These are for when the class referencing the symbols is unloaded; it
destroys any object references
 * the linker might have kept around.
 */
JNIEXPORT void JNICALL LINK_UnlinkClass       (JNIEnv * env, linkedClass * c) {
	if(*c != NULL) {
		if(GETCLASS(*c) != NULL)
			(*env)->DeleteGlobalRef(env,GETCLASS(*c));
		JCL_free(env,*c);
		*c = NULL;
	}
}

