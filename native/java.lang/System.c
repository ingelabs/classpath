#include "System.h"
#include <time.h>

/*
 * Class:     java_lang_System
 * Method:    setIn
 * Signature: (Ljava/io/InputStream;)V
 */
JNIEXPORT void JNICALL Java_java_lang_System_setIn
  (JNIEnv * env, jclass thisClass, jobject in) {
	jfieldID inField = (*env)->GetStaticFieldID(env,thisClass,"in","Ljava/io/InputStream");
	(*env)->SetStaticObjectField(env,thisClass,inField,in);
}

/*
 * Class:     java_lang_System
 * Method:    setOut
 * Signature: (Ljava/io/PrintStream;)V
 */
JNIEXPORT void JNICALL Java_java_lang_System_setOut
  (JNIEnv * env, jclass thisClass, jobject out) {
	jfieldID outField = (*env)->GetStaticFieldID(env,thisClass,"out","Ljava/io/PrintStream");
	(*env)->SetStaticObjectField(env,thisClass,outField,out);
}

/*
 * Class:     java_lang_System
 * Method:    setErr
 * Signature: (Ljava/io/PrintStream;)V
 */
JNIEXPORT void JNICALL Java_java_lang_System_setErr
  (JNIEnv * env, jclass thisClass, jobject err) {
	jfieldID errField = (*env)->GetStaticFieldID(env,thisClass,"err","Ljava/io/PrintStream");
	(*env)->SetStaticObjectField(env,thisClass,errField,err);
}

/*
 * Class:     java_lang_System
 * Method:    currentTimeMillis
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_java_lang_System_currentTimeMillis
  (JNIEnv * env, jclass thisClass) {
	return time(NULL) * 1000;
}
