#include "jnilink.h"
#include <string.h>
#include <vmi.h>
#include <jcl.h>

#include <malloc.h>

typedef struct jniMethodInfo {
	int isStatic;
	union {
		struct {
			char * name;
			char * sig;
		} dynamic;
		jmethodID statID;
	} data;
} jniMethodInfo;

/* These functions are called to get the link pointers. */
/* One possible optimization for Japhar would be to store the slot number of the method in the linkPtr.
 * Another, which works for JNI too, is to see if the class or method is final and simply store the jmethodID.
 * For JNI, the linkPtr must point to a struct containing the name and sig so that it can be re-resolved for
 * every object.
 */
JNIEXPORT linkPtr JNICALL LINK_LinkMethod      (JNIEnv * env, jclass clazz, char * name, char * sig) {
	jniMethodInfo * m;

	jint classMods;
	jint methodMods;
	vmiError vmiErr;

	jmethodID theMethod;

	m = JCL_malloc(env, sizeof(jniMethodInfo));
	if(m == NULL)
		return NULL;

	vmiErr = VMI_GetClassModifiers(env, clazz, &classMods);
	if(vmiErr != VMI_ERROR_NONE) {
		VMI_ThrowAppropriateException(env, vmiErr);
		free(m);
		return NULL;
	}

	theMethod = (*env)->GetMethodID(env, clazz, name, sig);
	if((*env)->ExceptionOccurred(env)) {
		free(m);
		return NULL;
	}

	if(classMods & VMI_MOD_FINAL) {
		m->isStatic = TRUE;
	} else {
		vmiErr = VMI_GetMethodModifiers(env, theMethod, &methodMods);
		if(vmiErr != VMI_ERROR_NONE) {
			VMI_ThrowAppropriateException(env, vmiErr);
			free(m);
			return NULL;
		}

		if(methodMods & VMI_MOD_FINAL || methodMods & VMI_MOD_STATIC || methodMods & VMI_MOD_PRIVATE) {
			m->isStatic = TRUE;
		} else {
			if(!strcmp(name,"<init>"))
				m->isStatic = TRUE;
			else
				m->isStatic = FALSE;
		}
	}

	if(m->isStatic) {
		m->data.statID = theMethod;
	} else {
		m->data.dynamic.name = JCL_malloc(env, strlen(name) + 1);
		if(m->data.dynamic.name == NULL) {
			free(m);
			return NULL;
		}

		strcpy(m->data.dynamic.name, name);

		m->data.dynamic.sig = JCL_malloc(env, strlen(sig) + 1);
		if(m->data.dynamic.sig == NULL) {
			free(m->data.dynamic.name);
			free(m->data.dynamic.sig);
			return NULL;
		}

		strcpy(m->data.dynamic.sig, sig);
	}
} 

/* Do we need to re-resolve fields based on objects?  I don't think so, but I could be wrong ... */
JNIEXPORT linkPtr JNICALL LINK_LinkField       (JNIEnv * env, jclass clazz, char * name, char * sig) {
	return (linkPtr)(*env)->GetFieldID(env, clazz, name, sig);
}

JNIEXPORT linkPtr JNICALL LINK_LinkClass       (JNIEnv * env, char * name) {
	jclass c = (*env)->FindClass(env, name);
	if((*env)->ExceptionOccurred(env)) {
		return NULL;
	}
	return (linkPtr)(*env)->NewGlobalRef(env, c);
}


/* The GetXXX functions can be inlined. */
/* Note: GetMethod does actual resolution of the method based on the object type.
 * The object in question *must* be of the correct type.  No type checking is done.
 * If the object is NULL, then the jmethodID will be NULL as well, and no exception
 * will be thrown.  If the method is not found, a MethodNotFoundException will be
 * thrown.
 */
JNIEXPORT jmethodID JNICALL LINK_GetMethod      (JNIEnv * env, linkPtr methodLink, jobject obj) {
	jniMethodInfo * m;
	jclass objClass;

	m = (jniMethodInfo *)methodLink;
	if(m->isStatic) {
		return m->data.statID;
	} else {
		if(obj == NULL) {
			JCL_ThrowException(env, "java/lang/NullPointerException", "Attempt to access non-static method with null object in LINK_GetMethod");
			return NULL;
		}
		objClass = (*env)->GetObjectClass(env, obj);
		return (*env)->GetMethodID(env, objClass, m->data.dynamic.name, m->data.dynamic.sig);
	}
}

JNIEXPORT jfieldID JNICALL  LINK_GetField       (JNIEnv * env, linkPtr fieldLink) {
	return (jfieldID)fieldLink;
}

JNIEXPORT jclass JNICALL    LINK_GetClass       (JNIEnv * env, linkPtr classLink) {
	return (jclass)classLink;
}


/* These are for when the class referencing the symbols is unloaded; it
destroys any object references
 * the linker might have kept around.
 */
JNIEXPORT void JNICALL LINK_UnlinkMethod      (JNIEnv * env, linkPtr methodLink) {
	jniMethodInfo * m = (jniMethodInfo *)methodLink;
	if(m != NULL) {
		if(!m->isStatic) {
			if(m->data.dynamic.name != NULL) free(m->data.dynamic.name);
			if(m->data.dynamic.sig != NULL) free(m->data.dynamic.sig);
		}
		free(m);
	}
}

JNIEXPORT void JNICALL LINK_UnlinkField       (JNIEnv * env, linkPtr fieldLink) {
	return;
}

JNIEXPORT void JNICALL LINK_UnlinkClass       (JNIEnv * env, linkPtr classLink) {
	if(classLink != NULL)
		(*env)->DeleteGlobalRef(env, (jclass)classLink);
}
