/* NetworkInterface.c - Native methods for NetworkInterface class
   Copyright (C) 2003 Free Software Foundation, Inc.

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

#include <config.h>
#include <errno.h>

#include <jni.h>
#include <jcl.h>

#include "target_native.h"
#include "target_native_math_int.h"

#include "java_nio_channels_FileChannelImpl.h"

#define IO_EXCEPTION "java/io/IOException"

JNIEXPORT jlong JNICALL
Java_java_nio_channels_FileChannelImpl_size (JNIEnv *env, jclass clazz)
{
  JCL_ThrowException (env, IO_EXCEPTION, "java.nio.FileChannelImpl.(): not implemented");
  return TARGET_NATIVE_MATH_INT_INT64_CONST_0;
}

JNIEXPORT jlong JNICALL
Java_java_nio_channels_FileChannelImpl_implPosition__ (JNIEnv *env, jclass clazz)
{
  JCL_ThrowException (env, IO_EXCEPTION, "java.nio.FileChannelImpl.implPosition(): not implemented");
  return TARGET_NATIVE_MATH_INT_INT64_CONST_0;
}

JNIEXPORT jobject JNICALL
Java_java_nio_channels_FileChannelImpl_implPosition__J (JNIEnv *env, jclass clazz, jlong newPosition)
{
  JCL_ThrowException (env, IO_EXCEPTION, "java.nio.FileChannelImpl.implPosition(): not implemented");
  return 0;
}

JNIEXPORT jobject JNICALL
Java_java_nio_channels_FileChannelImpl_implTruncate (JNIEnv *env, jclass clazz, jlong size)
{
  JCL_ThrowException (env, IO_EXCEPTION, "java.nio.FileChannelImpl.implTruncate(): not implemented");
  return 0;
}

JNIEXPORT jobject JNICALL
Java_java_nio_channels_FileChannelImpl_nio_1mmap_1file (JNIEnv *env, jclass clazz, jlong pos, jlong size, jint mode)
{
  JCL_ThrowException (env, IO_EXCEPTION, "java.nio.FileChannelImpl.nio_mmap_file(): not implemented");
  return 0;
}

JNIEXPORT void JNICALL
Java_java_nio_channels_FileChannelImpl_nio_1unmmap_1file (JNIEnv *env, jclass clazz, jobject map_address, jint size)
{
  JCL_ThrowException (env, IO_EXCEPTION, "java.nio.FileChannelImpl.nio_unmmap_file(): not implemented");
  return 0;
}

JNIEXPORT void JNICALL
Java_java_nio_channels_FileChannelImpl_nio_1msync (JNIEnv *env, jclass clazz, jobject map_address, jint length)
{
  JCL_ThrowException (env, IO_EXCEPTION, "java.nio.FileChannelImpl.nio_msync(): not implemented");
  return 0;
}

JNIEXPORT jint JNICALL
Java_java_nio_channels_FileChannelImpl_implRead (JNIEnv *env, jclass clazz, jbyteArray buffer, jint offset, jint length)
{
  JCL_ThrowException (env, IO_EXCEPTION, "java.nio.FileChannelImpl.implRead(): not implemented");
  return 0;
}

JNIEXPORT jint JNICALL
Java_java_nio_channels_FileChannelImpl_implWrite (JNIEnv *env, jclass clazz, jbyteArray buffer, jint offset, jint length)
{
  JCL_ThrowException (env, IO_EXCEPTION, "java.nio.FileChannelImpl.implWrite(): not implemented");
  return 0;
}

