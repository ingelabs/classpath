#ifndef __JCL_H__
#define __JCL_H__

#include <jni.h>

JNIEXPORT jclass JNICALL JCL_FindClass(JNIEnv * env, char * className);
JNIEXPORT void JNICALL JCL_ThrowException(JNIEnv * env, char * className, char * errMsg);
JNIEXPORT void * JNICALL JCL_malloc(JNIEnv * env, size_t size);
JNIEXPORT void JNICALL JCL_free(JNIEnv * env, void * p);
JNIEXPORT char * JNICALL JCL_jstring_to_cstring(JNIEnv * env, jstring s);
JNIEXPORT void JNICALL JCL_free_cstring(JNIEnv * env, jstring s, char * cstr);
JNIEXPORT jint JNICALL JCL_MonitorEnter(JNIEnv * env, jobject o);
JNIEXPORT jint JNICALL JCL_MonitorExit(JNIEnv * env, jobject o);

#define JCL_RETHROW_EXCEPTION(env) if((*(env))->ExceptionOccurred((env)) != NULL) return NULL;

#ifndef TRUE
#define TRUE 1
#endif

#ifndef FALSE
#define FALSE 0
#endif

#endif