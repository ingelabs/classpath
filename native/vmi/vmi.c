/* Japhar implementation of VMI. */

#include <jcl.h>
#include <vmi.h>
#include <jvmdi.h>
#include <interp.h>
#include <native-threads.h>

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


/* 1.2 placeholders: can be implemented for Japhar using JNI */

/* Thread / Frame Stuff */

JNIEXPORT vmiError JNICALL
VMI_GetCallerFrame(JNIEnv *env, jframeID called, jframeID *framePtr) {
	return JVMDI_GetCallerFrame(env, called, framePtr);
}


/* Class Introspection */

JNIEXPORT vmiError JNICALL
VMI_GetClassModifiers(JNIEnv *env, jclass clazz, jint *modifiers) {
	return JVMDI_GetClassModifiers(env, clazz, modifiers);
}

JNIEXPORT vmiError JNICALL
VMI_GetClassName(JNIEnv *env, jclass clazz, jstring *namePtr) {
	return JVMDI_GetClassName(env, clazz, namePtr);
}

JNIEXPORT vmiError JNICALL
VMI_GetClassMethods(JNIEnv *env, jclass clazz, 
					jint *methodCountPtr, jmethodID **methodsPtr) {
	return JVMDI_GetClassMethods(env, clazz, methodCountPtr, methodsPtr);
}

JNIEXPORT vmiError JNICALL
VMI_GetClassFields(JNIEnv *env, jclass clazz, 
				   jint *fieldCountPtr, jfieldID **fieldsPtr) {
	return JVMDI_GetClassFields(env, clazz, fieldCountPtr, fieldsPtr);
}

JNIEXPORT vmiError JNICALL
VMI_GetImplementedInterfaces(JNIEnv *env, jclass clazz,
                               jint *interfaceCountPtr, 
                               jclass **interfacesPtr) {
	return JVMDI_GetImplementedInterfaces(env, clazz, interfaceCountPtr, interfacesPtr);
}

JNIEXPORT vmiError JNICALL
VMI_IsInterface(JNIEnv *env, jclass clazz, jboolean *isInterfacePtr) {
	return JVMDI_IsInterface(env, clazz, isInterfacePtr);
}

JNIEXPORT vmiError JNICALL
VMI_IsArray(JNIEnv *env, jclass clazz, jboolean *isArrayPtr) {
	return JVMDI_IsArray(env, clazz, isArrayPtr);
}

JNIEXPORT vmiError JNICALL
VMI_ClassLoader(JNIEnv *env, jclass clazz, jobject *classloaderPtr) {
	return JVMDI_ClassLoader(env, clazz, classloaderPtr);
}

/* Method Introspection */

JNIEXPORT vmiError JNICALL
VMI_GetMethodModifiers(JNIEnv *env, jmethodID m, jint *modifiers) {
	return JVMDI_GetMethodModifiers(env, m, modifiers);
}

JNIEXPORT vmiError JNICALL
VMI_GetMethodName(JNIEnv *env, jclass clazz, jmethodID method, 
				  jstring *namePtr, jstring *signaturePtr) {
	return JVMDI_GetMethodName(env, clazz, method, namePtr, signaturePtr);
}

JNIEXPORT vmiError JNICALL
VMI_GetMethodDefiningClass(JNIEnv *env, jclass clazz, jmethodID method,
						   jclass *definingClassPtr) {
	return JVMDI_GetMethodDefiningClass(env, clazz, method, definingClassPtr);
}

/* Field Introspection */

JNIEXPORT vmiError JNICALL
VMI_GetFieldName(JNIEnv *env, jclass clazz, jfieldID field, 
				 char **namePtr, char **signaturePtr) {
	return JVMDI_GetFieldName(env, clazz, field, namePtr, signaturePtr);
}

JNIEXPORT vmiError JNICALL
VMI_GetFieldDeclaringClass(JNIEnv *env, jclass clazz, jfieldID field,
						   jclass *declaringClassPtr) {
	return JVMDI_GetFieldDeclaringClass(env, clazz, field, declaringClassPtr);
}

JNIEXPORT vmiError JNICALL
VMI_GetFieldModifiers(JNIEnv *env, jclass clazz, jfieldID field,
					  jint *modifiersPtr) {
	return JVMDI_GetFieldModifiers(env, clazz, field, modifiersPtr);
}

JNIEXPORT vmiError JNICALL
VMI_GetThrownExceptions(JNIEnv *env, jclass clazz, jmethodID method,
						jint *exceptionCountPtr, jclass **exceptionsPtr) {
	return JVMDI_GetThrownExceptions(env, clazz, method, exceptionCountPtr, exceptionsPtr);
}

JNIEXPORT vmiError JNICALL
VMI_IsMethodNative(JNIEnv *env, jclass clazz, jmethodID method, 
				   jboolean *isNativePtr) {
	return JVMDI_IsMethodNative(env, clazz, method, isNativePtr);
}

