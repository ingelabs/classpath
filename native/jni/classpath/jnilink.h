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


#ifndef __JNILINK_H__
#define __JNILINK_H__

#include <jni.h>

typedef void* linkedClass;

#define LINK_LinkClass(env,c,name)              ((c)==NULL ? LINK_ReallyLinkClass((env),&(c),(name)) : (c))
#define LINK_LinkKnownClass(env,c,newClass)     ((c)==NULL ? LINK_ReallyLinkKnownClass((env),&(c),(newClass)) : (c))
#define LINK_LinkMethod(env,m,c,name,sig)       ((m)==NULL ? LINK_RelinkMethod((env),&(m),(c),(name),(sig)) : (m))
#define LINK_LinkStaticMethod(env,m,c,name,sig) ((m)==NULL ? LINK_RelinkStaticMethod((env),&(m),(c),(name),(sig)) : (m))
#define LINK_LinkField(env,f,c,name,sig)        ((m)==NULL ? LINK_RelinkField((env),&(f),(c),(name),(sig)) : (f))
#define LINK_LinkStaticField(env,f,c,name,sig)  ((m)==NULL ? LINK_RelinkStaticField((env),&(f),(c),(name),(sig)) : (f))

#define LINK_LinkConstructor(env,m,c,sig)       ((m)==NULL ? LINK_RelinkMethod((env),&(m),(c),"<init>",(sig)) : (m))

JNIEXPORT jclass JNICALL
LINK_ReallyLinkClass     (JNIEnv * env, linkedClass * c,
                          char * name);
JNIEXPORT jclass JNICALL
LINK_ReallyLinkKnownClass(JNIEnv * env, linkedClass * c,
                          jclass newClass);
JNIEXPORT jclass JNICALL
LINK_RelinkClass       (JNIEnv * env, linkedClass * c,
                        char * name);
JNIEXPORT jclass JNICALL
LINK_RelinkKnownClass  (JNIEnv * env, linkedClass * c,
                        jclass newClass);
JNIEXPORT jmethodID JNICALL
LINK_RelinkMethod      (JNIEnv * env, jmethodID * m, linkedClass c,
                        char * name, char * sig);
JNIEXPORT jmethodID JNICALL
LINK_RelinkStaticMethod(JNIEnv * env, jmethodID * m, linkedClass c,
                        char * name, char * sig);
JNIEXPORT jfieldID JNICALL
LINK_RelinkField       (JNIEnv * env, jfieldID * f, linkedClass c,
                        char * name, char * sig);
JNIEXPORT jfieldID JNICALL
LINK_RelinkStaticField (JNIEnv * env, jfieldID * f, linkedClass c,
                        char * name, char * sig);

/* These are for when the class referencing the symbols is unloaded; it
destroys any object references
 * the linker might have kept around.
 */
JNIEXPORT void JNICALL LINK_UnlinkClass       (JNIEnv * env, linkedClass * c);

#endif
