#ifndef __VMI_H__
#define __VMI_H__

#include <jni.h>

typedef void * jframeID;
typedef void * jthread;

typedef enum {
  VMI_ERROR_NONE,
  VMI_ERROR_NULL_POINTER,
  VMI_ERROR_OUT_OF_MEMORY,
  VMI_ERROR_INVALID_METHODID,
  VMI_ERROR_INVALID_CLASS,
  VMI_ERROR_INVALID_BCI,
  VMI_ERROR_NO_SUCH_BREAKPOINT,
  VMI_ERROR_VM_DEAD,
  VMI_ERROR_INVALID_FRAMEID,
  VMI_ERROR_INVALID_SLOT,
  VMI_ERROR_TYPE_MISMATCH,
  VMI_ERROR_NATIVE_FRAME,
  VMI_ERROR_NO_MORE_FRAMES,
  VMI_ERROR_INVALID_THREAD,
  VMI_ERROR_THREAD_NOT_SUSPENDED
} vmiError;


#define VMI_MOD_PUBLIC       0x0001
#define VMI_MOD_PRIVATE      0x0002
#define VMI_MOD_PROTECTED    0x0004
#define VMI_MOD_STATIC       0x0008
#define VMI_MOD_FINAL        0x0010
#define VMI_MOD_SYNCHRONIZED 0x0020
#define VMI_MOD_VOLATILE     0x0040
#define VMI_MOD_TRANSIENT    0x0080
#define VMI_MOD_NATIVE       0x0100
#define VMI_MOD_INTERFACE    0x0200
#define VMI_MOD_ABSTRACT     0x0400


JNIEXPORT vmiError JNICALL
VMI_GetFrameObject(JNIEnv *env, jframeID frame, jobject *obj);

JNIEXPORT vmiError JNICALL
VMI_GetThisFrame(JNIEnv *env, jframeID *frame);

JNIEXPORT vmiError JNICALL
VMI_GetThisThreadObject(JNIEnv *env, jthread *thread);

JNIEXPORT void JNICALL
VMI_ThrowAppropriateException(JNIEnv *env, vmiError err);


/*
 * 1.2 placeholders: stuff that will be in JVMDI but has to be in VMI for now.
 */

/* Thread / Frame Stuff */

JNIEXPORT vmiError JNICALL
VMI_GetCallerFrame(JNIEnv *env, jframeID called, jframeID *framePtr);


/* Class Introspection */

JNIEXPORT vmiError JNICALL
VMI_GetClassModifiers(JNIEnv *env, jclass clazz, jint *modifiers);

JNIEXPORT vmiError JNICALL
VMI_GetClassName(JNIEnv *env, jclass clazz, jstring *namePtr);

JNIEXPORT vmiError JNICALL
VMI_GetClassMethods(JNIEnv *env, jclass clazz, 
                      jint *methodCountPtr, jmethodID **methodsPtr);

JNIEXPORT vmiError JNICALL
VMI_GetClassFields(JNIEnv *env, jclass clazz, 
                     jint *fieldCountPtr, jfieldID **fieldsPtr);

JNIEXPORT vmiError JNICALL
VMI_GetImplementedInterfaces(JNIEnv *env, jclass clazz,
                               jint *interfaceCountPtr, 
                               jclass **interfacesPtr);

JNIEXPORT vmiError JNICALL
VMI_IsInterface(JNIEnv *env, jclass clazz, jboolean *isInterfacePtr);

JNIEXPORT vmiError JNICALL
VMI_IsArray(JNIEnv *env, jclass clazz, jboolean *isArrayPtr);

JNIEXPORT vmiError JNICALL
VMI_ClassLoader(JNIEnv *env, jclass clazz, jobject *classloaderPtr);

/* Method Introspection */

JNIEXPORT vmiError JNICALL
VMI_GetMethodModifiers(JNIEnv *env, jmethodID m, jint *modifiers);

JNIEXPORT vmiError JNICALL
VMI_GetMethodName(JNIEnv *env, jclass clazz, jmethodID method, 
		    jstring *namePtr, jstring *signaturePtr);

JNIEXPORT vmiError JNICALL
VMI_GetMethodDefiningClass(JNIEnv *env, jclass clazz, jmethodID method,
			     jclass *definingClassPtr);

/* Field Introspection */

JNIEXPORT vmiError JNICALL
VMI_GetFieldName(JNIEnv *env, jclass clazz, jfieldID field, 
                   char **namePtr, char **signaturePtr);

JNIEXPORT vmiError JNICALL
VMI_GetFieldDeclaringClass(JNIEnv *env, jclass clazz, jfieldID field,
 			     jclass *declaringClassPtr);

JNIEXPORT vmiError JNICALL
VMI_GetFieldModifiers(JNIEnv *env, jclass clazz, jfieldID field,
                        jint *modifiersPtr);

JNIEXPORT vmiError JNICALL
VMI_GetThrownExceptions(JNIEnv *env, jclass clazz, jmethodID method,
                          jint *exceptionCountPtr, jclass **exceptionsPtr);

JNIEXPORT vmiError JNICALL
VMI_IsMethodNative(JNIEnv *env, jclass clazz, jmethodID method, 
                    jboolean *isNativePtr);

#endif
