/* Japhar implementation of VMI.
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


#include <jcl.h>
#include <vmi.h>
#include <jvmdi.h>
#include <interp.h>
#include <native-threads.h>

JNIEXPORT vmiError JNICALL
VMI_GetFrameClass(JNIEnv *env,
		      jframeID frame,
		      jclass *clazz) {
	
	return VMI_ERROR_NONE;
}

JNIEXPORT vmiError JNICALL
VMI_GetFrameObject(JNIEnv *env,
		      jframeID frame,
		      jobject *obj) {
  StackFrame *sframe = (StackFrame*)frame;
  if(env == NULL || obj == NULL)
    return VMI_ERROR_NULL_POINTER;
  if(frame == NULL)
    return VMI_ERROR_INVALID_FRAMEID;

  *obj = THISPTR(sframe);
  return VMI_ERROR_NONE;
}

JNIEXPORT vmiError JNICALL
VMI_GetThisFrame(JNIEnv *env, jframeID *frame) {
  JThreadInfo *thread_info;

  if(env == NULL || frame == NULL)
    return VMI_ERROR_NULL_POINTER;

  thread_info = THREAD_getJavaInfo();
  *frame = (jframeID)TOPFRAME(thread_info);
  return VMI_ERROR_NONE;
}

JNIEXPORT vmiError JNICALL
VMI_GetThisThreadObject(JNIEnv* env, jthread *thread) {
  JThreadInfo *thread_info;
  if(env == NULL || thread == NULL)
    return VMI_ERROR_NULL_POINTER;
  thread_info = THREAD_getJavaInfo();
  *thread = (jthread)thread_info->java_thread;
  return VMI_ERROR_NONE;
}

JNIEXPORT void JNICALL
VMI_ThrowAppropriateException(JNIEnv *env, vmiError err) {
	switch(err) {
	case VMI_ERROR_NONE:
		JCL_ThrowException(env, "java/lang/InternalError", "ERROR_NONE passed to VMI exception thrower.");
		break;
	case VMI_ERROR_NULL_POINTER:
		JCL_ThrowException(env, "java/lang/NullPointerException", "null pointer in VMI detected.");
		break;
	case VMI_ERROR_OUT_OF_MEMORY:
		JCL_ThrowException(env, "java/lang/OutOfMemoryError", "Out of memory! (in VMI).");
		break;
	case VMI_ERROR_INVALID_METHODID:
		JCL_ThrowException(env, "java/lang/InternalError", "VMI error: INVALID_METHODID");
		break;
	case VMI_ERROR_INVALID_CLASS:
		JCL_ThrowException(env, "java/lang/InternalError", "VMI error: INVALID_CLASS");
		break;
	case VMI_ERROR_INVALID_BCI:
		JCL_ThrowException(env, "java/lang/InternalError", "VMI error: INVALID_BCI");
		break;
	case VMI_ERROR_NO_SUCH_BREAKPOINT:
		JCL_ThrowException(env, "java/lang/InternalError", "VMI error: NO_SUCH_BREAKPOINT");
		break;
	case VMI_ERROR_VM_DEAD:
		JCL_ThrowException(env, "java/lang/InternalError", "VMI error: VM Dead!  Kinda makes ya wonder how this exception got thrown, huh?");
		break;
	case VMI_ERROR_INVALID_FRAMEID:
		JCL_ThrowException(env, "java/lang/IllegalThreadStateException", "NULL Frame ID detected in VMI.");
		break;
	case VMI_ERROR_INVALID_SLOT:
		JCL_ThrowException(env, "java/lang/InternalError", "VMI error: INVALID_SLOT");
		break;
	case VMI_ERROR_TYPE_MISMATCH:
		JCL_ThrowException(env, "java/lang/InternalError", "VMI error: INVALID_SLOT");
		break;
	case VMI_ERROR_NATIVE_FRAME:
		JCL_ThrowException(env, "java/lang/InternalError", "VMI error: NATIVE_FRAME");
		break;
	case VMI_ERROR_NO_MORE_FRAMES:
		JCL_ThrowException(env, "java/lang/InternalError", "VMI error: NO_MORE_FRAMES");
		break;
	case VMI_ERROR_INVALID_THREAD:
		JCL_ThrowException(env, "java/lang/IllegalThreadStateException", "Invalid thread in VMI.");
		break;
	case VMI_ERROR_THREAD_NOT_SUSPENDED:
		JCL_ThrowException(env, "java/lang/IllegalThreadStateException", "Attempt to introspect unsuspended thread in VMI.");
		break;
	default:
		JCL_ThrowException(env, "java/lang/UnknownError", "VMI returned erroneous error value ...");
		break;
	}
}


