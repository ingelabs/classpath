#include <jcl.h>
#include <malloc.h>

static char errstr[4098]; // this way the memory is pre-allocated, so that we do not have to worry if we are out of memory.

JNIEXPORT void JNICALL JCL_ThrowException(JNIEnv * env, char * className, char * errMsg) {
	jclass excClass;
	if((*env)->ExceptionOccurred(env)) {
		(*env)->ExceptionClear(env);
	}
	excClass = (*env)->FindClass(env, className);
	if(excClass == NULL) {
		jclass errExcClass;
		errExcClass = (*env)->FindClass(env, "java/lang/ClassNotFoundException");
		if(errExcClass == NULL) {
			errExcClass = (*env)->FindClass(env, "java/lang/InternalError");
			if(errExcClass == NULL) {
				sprintf(errstr,"JCL: Utterly failed to throw exeption %s with message %s.",className,errMsg);
				fprintf(stderr, errstr);
				return;
			}
		}
		sprintf(errstr,"JCL: Failed to throw exception %s with message %s: could not find exception class.", className, errMsg);
		(*env)->ThrowNew(env, errExcClass, errstr);
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

JNIEXPORT void JNICALL JCL_free(JNIEnv * env, void * p) {
	if(p != NULL) {
		free(p);
	}
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

JNIEXPORT jclass JNICALL JCL_FindClass(JNIEnv * env, char * className) {
	jclass retval = (*env)->FindClass(env,className);
	if(retval == NULL) {
		JCL_ThrowException(env, "java/lang/ClassNotFoundException", className);
	}
	return retval;
}
