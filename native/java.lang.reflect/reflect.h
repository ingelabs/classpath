#ifndef __REFLECT_H__
#define __REFLECT_H__

#include <jni.h>

#define MAX_SIGNATURE_SIZE 4096

JNIEXPORT jboolean JNICALL REFLECT_CallerHasAccess(JNIEnv * env, jclass accessee, jint memberMods, jint callerStackPos);
JNIEXPORT jboolean JNICALL REFLECT_HasLinkLevelAccessToMember(JNIEnv * env, jclass accessor, jclass accessee, jint memberMods);
JNIEXPORT jint JNICALL REFLECT_GetMethodSignature(JNIEnv * env, char * signatureBuf, jobjectArray argTypes, jclass retType);
JNIEXPORT jint JNICALL REFLECT_GetConstructorSignature(JNIEnv * env, char * signatureBuf, jobjectArray argTypes);
JNIEXPORT jint JNICALL REFLECT_GetFieldSignature (JNIEnv * env, char * signatureBuf, jclass fieldType);

#endif