#include "Object.h"

/*
 * Class:     java_lang_Object
 * Method:    getClass
 * Signature: ()Ljava/lang/Class;
 */
JNIEXPORT jclass JNICALL Java_java_lang_Object_getClass
  (JNIEnv * env, jobject thisObj) {
	return (*env)->GetObjectClass(env,thisObj);
}

