/* java_io_ObjectOutputStream.c -- Native methods for ObjectOutputStream 
   class
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

#include "java_io_ObjectOutputStream.h"

#ifndef NDEBUG
#  define DEBUG( msg ) printf( msg ); 
#else
#  define DEBUG( msg )
#endif
#define RETHROW_EXCEPTION( env ) if((*(env))->ExceptionOccurred((env)) != NULL) return;

JNIEXPORT void JNICALL
Java_java_io_ObjectOutputStream_callWriteMethod( JNIEnv * env,
						 jobject self,
						 jobject obj )
{
  jclass obj_class;
  jmethodID id;

  obj_class = (*env)->GetObjectClass( env, obj );
  id = (*env)->GetMethodID( env, obj_class,
			    "writeObject",
			    "(Ljava/io/ObjectOutputStream;)V" );
  
  if( id == NULL )
    return;
  
  (*env)->CallNonvirtualVoidMethod( env, obj, obj_class, id, self );
}


JNIEXPORT jboolean JNICALL
Java_java_io_ObjectOutputStream_getBooleanField( JNIEnv * env,
						 jobject self,
						 jobject obj,
						 jstring field_name )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return JNI_FALSE;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "Z" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return JNI_FALSE;
  
  return (*env)->GetBooleanField( env, obj, id );
}


JNIEXPORT jbyte JNICALL
Java_java_io_ObjectOutputStream_getByteField( JNIEnv * env,
					      jobject self,
					      jobject obj,
					      jstring field_name )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "B" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  return (*env)->GetByteField( env, obj, id );
}


JNIEXPORT jchar JNICALL
Java_java_io_ObjectOutputStream_getCharField( JNIEnv * env,
					      jobject self,
					      jobject obj,
					      jstring field_name )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "C" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  return (*env)->GetCharField( env, obj, id );
}


JNIEXPORT jdouble JNICALL
Java_java_io_ObjectOutputStream_getDoubleField( JNIEnv * env,
						jobject self,
						jobject obj,
						jstring field_name )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "D" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  return (*env)->GetDoubleField( env, obj, id );
}


JNIEXPORT jfloat JNICALL
Java_java_io_ObjectOutputStream_getFloatField( JNIEnv * env,
					       jobject self,
					       jobject obj,
					       jstring field_name )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "F" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  return (*env)->GetFloatField( env, obj, id );
}


JNIEXPORT jint JNICALL
Java_java_io_ObjectOutputStream_getIntField( JNIEnv * env,
					     jobject self,
					     jobject obj,
					     jstring field_name )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "I" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  return (*env)->GetIntField( env, obj, id );
}


JNIEXPORT jlong JNICALL
Java_java_io_ObjectOutputStream_getLongField( JNIEnv * env,
					      jobject self,
					      jobject obj,
					      jstring field_name )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "J" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  return (*env)->GetLongField( env, obj, id );
}


JNIEXPORT jshort JNICALL
Java_java_io_ObjectOutputStream_getShortField( JNIEnv * env,
					       jobject self,
					       jobject obj,
					       jstring field_name )
{
  jfieldID id;
  char * name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "S" );
  
  JCL_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  return (*env)->GetShortField( env, obj, id );
}


JNIEXPORT jobject JNICALL
Java_java_io_ObjectOutputStream_getObjectField( JNIEnv * env,
						jobject self,
						jobject obj,
						jstring field_name,
						jstring type_code )
{
  jfieldID id;
  char * name_cstr;
  char * type_cstr;

  name_cstr = JCL_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return NULL;
  
  type_cstr = JCL_jstring_to_cstring( env, type_code );
  
  if( type_cstr == NULL )
  {
    JCL_free_cstring( env, field_name, name_cstr );
    return NULL;
  }

  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, type_cstr );
  
  JCL_free_cstring( env, field_name, name_cstr );
  JCL_free_cstring( env, type_code, type_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return NULL;
  
  return (*env)->GetObjectField( env, obj, id );
}
