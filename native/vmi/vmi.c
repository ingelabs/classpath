/* Japhar implementation of VMI. */

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


