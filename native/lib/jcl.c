#include <jcl.h>
#include <malloc.h>

JNIEXPORT void JNICALL JCL_ThrowException(JNIEnv * env, char * className, char * errMsg) {
	jclass excClass = (*env)->FindClass(env, className);
	if((*env)->ExceptionOccurred(env)) {
		return;
	}
	(*env)->ThrowNew(env, excClass, errMsg);
}

JNIEXPORT void * JNICALL JCL_malloc(JNIEnv * env, size_t size) {
	void * mem = malloc(size);
	if(mem == NULL) {
		JCL_ThrowException(env, "java/lang/OutOfMemoryError", "malloc() failed.");
		return NULL;
	}
	return mem;
}

JNIEXPORT char * JNICALL JCL_jstring_to_cstring(JNIEnv * env, jstring s) {
	char* cstr;
	if(s == NULL) {
		JCL_ThrowException(env, "java/lang/NullPointerException","Null string");
		return NULL;
	}
	cstr = (char*)(*env)->GetStringUTFChars(env, s, NULL);
	if(cstr == NULL) {
		JCL_ThrowException(env, "java/lang/InternalError", "GetStringUTFChars() failed.");
		return NULL;
	}
	return cstr;
}

JNIEXPORT void JNICALL JCL_free_cstring(JNIEnv * env, jstring s, char * cstr) {
	(*env)->ReleaseStringUTFChars(env, s, cstr);
}

JNIEXPORT jint JNICALL JCL_MonitorEnter(JNIEnv * env, jobject o) {
	jint retval = (*env)->MonitorEnter(env,o);
	if(retval != 0) {
		JCL_ThrowException(env, "java/lang/InternalError", "MonitorEnter() failed.");
	}
	return retval;
}

JNIEXPORT jint JNICALL JCL_MonitorExit(JNIEnv * env, jobject o) {
	jint retval = (*env)->MonitorExit(env,o);
	if(retval != 0) {
		JCL_ThrowException(env, "java/lang/InternalError", "MonitorExit() failed.");
	}
	return retval;
}
