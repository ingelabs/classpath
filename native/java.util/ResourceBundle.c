/*************************************************************************
 * ResourceBundle.c - Native methods for java.util.ResourceBundle
 *
 * Copyright (c) 1999 Free Software Foundation, Inc.
 * Written by Jochen Hoenicke (jochen@gnu.org)
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/

#include <jni.h>

#include "java_util_ResourceBundle.h"

/*
 * This is a wrapper to VMSecurityManager.getClassContext().
 * Note that this relies on the fact that native methods aren't listed
 * in the returned array.
 */
JNIEXPORT jobjectArray JNICALL 
Java_java_util_ResourceBundle_getClassContext(JNIEnv * env, jclass cls)
{
  jclass vmSecManager = (*env)->FindClass(env, "java/lang/VMSecurityManager");
  jmethodID mid = (*env)->GetStaticMethodID(env, vmSecManager, 
					    "getClassContext", 
					    "()[Ljava/lang/Class;");
  return (*env)->CallStaticObjectMethod(env, vmSecManager, mid);
}
