/* java_io_ObjectStreamClass.c -- Native methods for ObjectStreamClass
   class.

 Copyright (c) 1998 by Free Software Foundation, Inc.

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU Library General Public License as published 
 by the Free Software Foundation, version 2. (see COPYING.LIB)

 This program is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software Foundation
 Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA */
 
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
