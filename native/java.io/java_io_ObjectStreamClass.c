/*************************************************************************
/* java_io_ObjectStreamClass.c -- Native methods for
/*                                ObjectStreamClass class
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

//TODO: comments

#include "java_io_ObjectStreamClass.h"

static jfieldID mysuid_fid = NULL;
static const char * myUID_str = "myUID";
static const char * myLongTypeId = "J";
static const char * serialVersionUID_str = "serialVersionUID";
static const char * class_init_str = "<clinit>";
static const char * class_init_sig = "()V";

JNIEXPORT void JNICALL
Java_java_io_ObjectStreamClass_initializeClass( JNIEnv * env,
						jclass myclass )
{
  mysuid_fid = (*env)->GetFieldID( env, myclass, myUID_str, myLongTypeId );
}


JNIEXPORT jboolean JNICALL
Java_java_io_ObjectStreamClass_hasDefinedSUID( JNIEnv * env,
					       jobject self,
					       jclass clazz )
{
  jfieldID suid_fid = (*env)->GetStaticFieldID( env, clazz,
						serialVersionUID_str,
						myLongTypeId );

  if( suid_fid == NULL )
    return FALSE;
  
  (*env)->SetLongField( env, self, mysuid_fid,
			(*env)->GetStaticLongField( env, clazz, suid_fid ) );
  
  return TRUE;
}


JNIEXPORT jboolean JNICALL
Java_java_io_ObjectStreamClass_hasClassInitializer( JNIEnv * env,
						    jclass myclass,
						    jclass clazz )
{
  jmethodID mid = (*env)->GetStaticMethodID( env, clazz,
					     class_init_str,
					     class_init_sig );

  return !(mid == NULL);
}
