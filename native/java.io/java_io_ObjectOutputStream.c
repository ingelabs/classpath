/*************************************************************************
/* java_io_ObjectOutputStream.c -- Native methods for
/*                                 ObjectOutputStream class
/*
/* Copyright (c) 1998 by Free Software Foundation, Inc.
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

//TODO: check exceptions
//      comments

#include <jni.h>

#include "javaio.h"

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
  char * name_cstr = _javaio_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return FALSE;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "Z" );
  
  _javaio_free_cstring( env, field_name, name_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return FALSE;
  
  return (*env)->GetBooleanField( env, obj, id );
}


JNIEXPORT jbyte JNICALL
Java_java_io_ObjectOutputStream_getByteField( JNIEnv * env,
					      jobject self,
					      jobject obj,
					      jstring field_name )
{
  jfieldID id;
  char * name_cstr = _javaio_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "B" );
  
  _javaio_free_cstring( env, field_name, name_cstr );

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
  char * name_cstr = _javaio_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "C" );
  
  _javaio_free_cstring( env, field_name, name_cstr );

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
  char * name_cstr = _javaio_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "D" );
  
  _javaio_free_cstring( env, field_name, name_cstr );

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
  char * name_cstr = _javaio_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "F" );
  
  _javaio_free_cstring( env, field_name, name_cstr );

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
  char * name_cstr = _javaio_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "I" );
  
  _javaio_free_cstring( env, field_name, name_cstr );

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
  char * name_cstr = _javaio_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "J" );
  
  _javaio_free_cstring( env, field_name, name_cstr );

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
  char * name_cstr = _javaio_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return -1;
  
  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, "S" );
  
  _javaio_free_cstring( env, field_name, name_cstr );

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

  name_cstr = _javaio_jstring_to_cstring( env, field_name );

  if( name_cstr == NULL )
    /* Exception was thrown, so value is arbitrary */
    return NULL;
  
  type_cstr = _javaio_jstring_to_cstring( env, type_code );
  
  if( type_cstr == NULL )
  {
    _javaio_free_cstring( env, field_name, name_cstr );
    return NULL;
  }

  id = (*env)->GetFieldID( env, (*env)->GetObjectClass( env, obj ),
			   name_cstr, type_cstr );
  
  _javaio_free_cstring( env, field_name, name_cstr );
  _javaio_free_cstring( env, type_code, type_cstr );

  if( id == NULL )
    /* Exception was thrown, so value is arbitrary */
    return NULL;
  
  return (*env)->GetObjectField( env, obj, id );
}
