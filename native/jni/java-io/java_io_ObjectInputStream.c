/* java_io_ObjectInputStream.c -- Native methods for ObjectInputStream class
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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


/*  TODO: check exceptions */
/*        comments */

#include <jni.h>
#include <jcl.h>
#include "java_io_ObjectInputStream.h"

/*
 * Class:     java_io_ObjectInputStream
 * Method:    currentClassLoader
 * Signature: (Ljava/lang/SecurityManager;)Ljava/lang/ClassLoader;
 */
JNIEXPORT jobject JNICALL
Java_java_io_ObjectInputStream_currentClassLoader( JNIEnv * env,
						   jclass clazz,
						   jobject loader )
{
  jmethodID id = (*env)->GetMethodID( env,
				      (*env)->GetObjectClass( env, loader ),
				      "currentClassLoader",
				      "()Ljava/lang/ClassLoader;" );
  
  if( id == NULL )
    return NULL;
  
  (*env)->CallObjectMethod( env, loader, id );
  return loader;
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    allocateObject
 * Signature: (Ljava/lang/Class;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL
Java_java_io_ObjectInputStream_allocateObject( JNIEnv * env,
					       jobject self,
					       jclass clazz )
{
  return (*env)->AllocObject( env, clazz );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    callConstructor
 * Signature: (Ljava/lang/Class;Ljava/lang/Object;)V
 */
JNIEXPORT void JNICALL 
Java_java_io_ObjectInputStream_callConstructor( JNIEnv * env,
						jclass clazz,
						jclass constr_class,
						jobject obj )
{
  jmethodID id = (*env)->GetMethodID( env, constr_class,
				      "<init>", "()V" );
  if( id == NULL )
    return;
  
  (*env)->CallNonvirtualVoidMethod( env, obj, constr_class, id );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    callReadMethod
 * Signature: (Ljava/lang/Object;Ljava/lang/Class;)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_callReadMethod( JNIEnv * env,
					       jobject self,
					       jobject obj,
					       jclass clazz )
{
  jmethodID id = (*env)->GetMethodID( env, clazz,
				      "readObject",
				      "(Ljava/io/ObjectInputStream;)V" );
  
  if( id == NULL )
    return;
  
  (*env)->CallNonvirtualVoidMethod( env, obj, clazz, id, self );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    setBooleanField
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Z)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_setBooleanField( JNIEnv * env,
						 jobject self,
						 jobject obj,
						 jstring field_name,
						 jboolean val )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    return;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "Z" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    return;
  
  (*env)->SetBooleanField( env, obj, id, val );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    setByteField
 * Signature: (Ljava/lang/Object;Ljava/lang/String;B)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_setByteField( JNIEnv * env,
					      jobject self,
					      jobject obj,
					      jstring field_name,
					      jbyte val )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    return;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "B" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    return;
  
  (*env)->SetByteField( env, obj, id, val );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    setCharField
 * Signature: (Ljava/lang/Object;Ljava/lang/String;C)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_setCharField( JNIEnv * env,
					      jobject self,
					      jobject obj,
					      jstring field_name,
					      jchar val )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    return;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "C" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    return;
  
  (*env)->SetCharField( env, obj, id, val );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    setDoubleField
 * Signature: (Ljava/lang/Object;Ljava/lang/String;D)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_setDoubleField( JNIEnv * env,
						jobject self,
						jobject obj,
						jstring field_name,
						jdouble val )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    return;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "D" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    return;
  
  (*env)->SetDoubleField( env, obj, id, val );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    setFloatField
 * Signature: (Ljava/lang/Object;Ljava/lang/String;F)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_setFloatField( JNIEnv * env,
					       jobject self,
					       jobject obj,
					       jstring field_name,
					       jfloat val )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    return;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "F" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    return;
  
  (*env)->SetFloatField( env, obj, id, val );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    setIntField
 * Signature: (Ljava/lang/Object;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_setIntField( JNIEnv * env,
					     jobject self,
					     jobject obj,
					     jstring field_name,
					     jint val )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    return;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "I" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    return;
  
  (*env)->SetIntField( env, obj, id, val );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    setLongField
 * Signature: (Ljava/lang/Object;Ljava/lang/String;J)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_setLongField( JNIEnv * env,
					      jobject self,
					      jobject obj,
					      jstring field_name,
					      jlong val )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    return;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "J" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    return;
  
  (*env)->SetLongField( env, obj, id, val );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    setShortField
 * Signature: (Ljava/lang/Object;Ljava/lang/String;S)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_setShortField( JNIEnv * env,
					       jobject self,
					       jobject obj,
					       jstring field_name,
					       jshort val )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    return;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "S" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    return;
  
  (*env)->SetShortField( env, obj, id, val );
}


/*
 * Class:     java_io_ObjectInputStream
 * Method:    setObjectField
 * Signature: (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/
Object;)V
 */
JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_setObjectField( JNIEnv * env,
						jobject self,
						jobject obj,
						jstring field_name,
						jstring type_code,
						jobject val )
{
  jfieldID id;
  char * name_cstr;
  char * type_cstr;

  name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    return;
  
  type_cstr = JCL_jstring_to_cstring( env, type_code );
  
  if( type_cstr == NULL )
  {
    JCL_free_cstring( env, field_name, name_cstr );
    return;
  }

  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, type_cstr );
  
  JCL_free_cstring( env, field_name, name_cstr );
  JCL_free_cstring( env, type_code, type_cstr );

  if( id == NULL )
    return;
  
  (*env)->SetObjectField( env, obj, id, val );
}
