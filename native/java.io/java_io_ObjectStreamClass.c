/* java_io_ObjectStreamClass.c -- Native methods for ObjectStreamClass
   class.
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

 
#include "java_io_ObjectStreamClass.h"

/*
  Returns the value of CLAZZ's static long field named `serialVersionUID'.

  A NoSuchFieldError is raised if CLAZZ has no such field.
*/
JNIEXPORT jlong JNICALL
Java_java_io_ObjectStreamClass_getDefinedSUID( JNIEnv * env,
					       jobject self,
					       jclass clazz )
{
  jfieldID suid_fid = (*env)->GetStaticFieldID( env, clazz,
						"serialVersionUID",
						"J" );

  if( suid_fid == NULL )
    return 0;
  
  return (*env)->GetStaticLongField( env, clazz, suid_fid );
}


JNIEXPORT jobjectArray JNICALL
Java_java_io_ObjectStreamClass_getSerialPersistantFields( JNIEnv * env, 
							  jobject self, 
							  jclass clazz )
{
  jfieldID spf_fid
    = (*env)->GetStaticFieldID( env, clazz,
				"serialPersistantFields",
				"[Ljava/io/ObjectStreamField;" );
  
  if( spf_fid == NULL )
    return 0;
  
  return (*env)->GetStaticObjectField( env, clazz, spf_fid );  
}

/* 
   Returns true if CLAZZ has a static class initializer
   (a.k.a. <clinit>).

   A NoSuchMethodError is raised if CLAZZ has no such method.
*/
JNIEXPORT jboolean JNICALL
Java_java_io_ObjectStreamClass_hasClassInitializer( JNIEnv * env,
						    jclass myclass,
						    jclass clazz )
{
  jmethodID mid = (*env)->GetStaticMethodID( env, clazz,
					     "<clinit>",
					     "()V" );

  return mid != NULL;
}
