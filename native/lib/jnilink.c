/*
 * JNILINK 1.1: JNI version.
 */

#include "jnilink.h"
#include <string.h>
#include <jcl.h>

#include <malloc.h>

#define GETCLASS(c) *(jclass*)(c)

JNIEXPORT jclass JNICALL
LINK_RelinkClass     (JNIEnv * env, linkedClass * c, char * name) {
	jclass found;
	LINK_UnlinkClass(env,*c);

	found = (*env)->FindClass(env,name);
	if(found == NULL)
		return NULL;

	*c = JCL_malloc(env,sizeof(jclass));
	if(*c == NULL)
		return NULL;

	GETCLASS(*c) = (*env)->NewGlobalRef(env,found);
	return GETCLASS(*c);
}

JNIEXPORT jclass JNICALL
LINK_RelinkKnownClass(JNIEnv * env, linkedClass * c, jclass newClass) {
	LINK_UnlinkClass(env,*c);

	*c = JCL_malloc(env,sizeof(jclass));
	if(*c == NULL)
		return NULL;

	GETCLASS(*c) = (*env)->NewGlobalRef(env,newClass);
	return newClass;
}

JNIEXPORT jmethodID JNICALL
LINK_RelinkMethod      (JNIEnv * env, jmethodID * m, linkedClass c,
                        char * name, char * sig) {
	*m = (*env)->GetMethodID(env,GETCLASS(c),name,sig);
	return *m;
}

JNIEXPORT jmethodID JNICALL
LINK_RelinkStaticMethod(JNIEnv * env, jmethodID * m, linkedClass c,
                        char * name, char * sig) {
	*m = (*env)->GetStaticMethodID(env,GETCLASS(c),name,sig);
	return *m;
}

JNIEXPORT jfieldID JNICALL
LINK_RelinkField       (JNIEnv * env, jfieldID * f, linkedClass c,
                        char * name, char * sig) {
	*f = (*env)->GetFieldID(env,GETCLASS(c),name,sig);
	return *f;
}

JNIEXPORT jfieldID JNICALL
LINK_RelinkStaticField (JNIEnv * env, jfieldID * f, linkedClass c,
                        char * name, char * sig) {
	*f = (*env)->GetStaticFieldID(env,GETCLASS(c),name,sig);
	return *f;
}


/* These are for when the class referencing the symbols is unloaded; it
destroys any object references
 * the linker might have kept around.
 */
JNIEXPORT void JNICALL LINK_UnlinkClass       (JNIEnv * env, linkedClass * c) {
	if(*c != NULL) {
		if(GETCLASS(*c) != NULL)
			(*env)->DeleteGlobalRef(env,GETCLASS(*c));
		JCL_free(env,*c);
		*c = NULL;
	}
}

