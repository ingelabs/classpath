/*************************************************************************
/* java_io_ObjectInputStream.c -- Native methods for
/*                                ObjectInputStream class
/*
/* Copyright (c) 1998 by Geoffrey C. Berry (gcb@cs.duke.edu)
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

#include <jcl.h>

#include "java_io_ObjectInputStream.h"

#ifndef NDEBUG
#  define DEBUG( msg ) printf( msg ); 
#else
#  define DEBUG( msg )
#endif

JNIEXPORT jobject JNICALL
Java_java_io_ObjectInputStream_allocateObject( JNIEnv * env,
					       jobject self,
					       jclass clazz )
{
  return (*env)->AllocObject( env, clazz );
}


JNIEXPORT void JNICALL
Java_java_io_ObjectInputStream_callConstructor( JNIEnv * env,
						jobject self,
						jobject obj,
						jclass clazz )
{
  jmethodID id = (*env)->GetMethodID( env, clazz, "<init>", "()V" );
  
  if( id == NULL )
    return;
  
  (*env)->CallNonvirtualVoidMethod( env, obj, clazz, id );
}


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
