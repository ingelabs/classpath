/*
 * java.lang.reflect.Array native functions.
 * Author: John Keiser
 * Version: 1.1.0
 * Date: 2 Jun 1998
 */
#include "Array.h"

/*
 * Class:     java_lang_reflect_Array
 * Method:    getLength
 * Signature: (Ljava/lang/Object;)I
 */
JNIEXPORT jint JNICALL Java_java_lang_reflect_Array_getLength
  (JNIEnv * env, jclass thisClass, jobject arr) {
	return (*env)->GetArrayLength(env, arr);
}

/*
 * Class:     java_lang_reflect_Array
 * Method:    createObjectArray
 * Signature: (Ljava/lang/Class;I)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_java_lang_reflect_Array_createObjectArray
  (JNIEnv * env, jclass thisClass, jclass arrayType, jint arrayLength) {
	return (jobject)(*env)->NewObjectArray(env,arrayLength,arrayType,NULL);
}
