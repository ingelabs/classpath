/*
 * JNILINK 1.1: JNI version.
 */

#include "jnilink.h"
#include <string.h>
#include <jcl.h>

#include <malloc.h>

typedef struct linkedMethod {
	jclass theClass;
	jmethodID m;
} linkedMethod;

typedef struct linkedField {
	jclass theClass;
	jfieldID f;
} linkedField;

/* These functions are called to get the link pointers. */
/* One possible optimization for Japhar would be to store the slot number of the method in the linkPtr.
 * Another, which works for JNI too, is to see if the class or method is final and simply store the jmethodID.
 * For JNI, the linkPtr must point to a struct containing the name and sig so that it can be re-resolved for
 * every object.
 */
JNIEXPORT linkPtr JNICALL LINK_LinkMethod      (JNIEnv * env, linkPtr * p, jclass clazz, char * name, char * sig) {
	linkedMethod * m;
	if(p != NULL && *p != NULL) {
		return *p;
	}
	m = JCL_malloc(env,sizeof(linkedMethod));
	if(m == NULL)
		return NULL;

	m->theClass = (*env)->NewGlobalRef(env,clazz);
	if(m->theClass == NULL)
		return NULL;

	m->m = (*env)->GetMethodID(env, clazz, name, sig);
	if(m->m == NULL)
		return NULL;

	if(p != NULL)
		*p=m;
	return (linkPtr)m;
} 

JNIEXPORT linkPtr JNICALL LINK_LinkStaticMethod(JNIEnv * env, linkPtr * p, jclass clazz, char * name, char * sig) {
	linkedMethod * m;
	if(p != NULL && *p != NULL) {
		return *p;
	}
	m = JCL_malloc(env,sizeof(linkedMethod));
	if(m == NULL)
		return NULL;

	m->theClass = (*env)->NewGlobalRef(env,clazz);
	if(m->theClass == NULL)
		return NULL;

	m->m = (*env)->GetStaticMethodID(env, clazz, name, sig);
	if(m->m == NULL)
		return NULL;

	if(p != NULL)
		*p=m;
	return (linkPtr)m;
}

JNIEXPORT linkPtr JNICALL LINK_LinkField       (JNIEnv * env, linkPtr * p, jclass clazz, char * name, char * sig) {
	linkedField * f;
	if(p != NULL && *p != NULL) {
		return *p;
	}
	f = JCL_malloc(env,sizeof(linkedMethod));
	if(f == NULL)
		return NULL;

	f->theClass = (*env)->NewGlobalRef(env,clazz);
	if(f->theClass == NULL)
		return NULL;

	f->f = (*env)->GetFieldID(env, clazz, name, sig);
	if(f->f == NULL)
		return NULL;

	if(p != NULL)
		*p=f;
	return (linkPtr)f;
}

JNIEXPORT linkPtr JNICALL LINK_LinkStaticField (JNIEnv * env, linkPtr * p, jclass clazz, char * name, char * sig) {
	linkedField * f;
	if(p != NULL && *p != NULL) {
		return *p;
	}
	f = JCL_malloc(env,sizeof(linkedMethod));
	if(f == NULL)
		return NULL;

	f->theClass = (*env)->NewGlobalRef(env,clazz);
	if(f->theClass == NULL)
		return NULL;

	f->f = (*env)->GetStaticFieldID(env, clazz, name, sig);
	if(f->f == NULL)
		return NULL;

	if(p != NULL)
		*p=f;
	return (linkPtr)f;
}

JNIEXPORT linkPtr JNICALL LINK_LinkClass       (JNIEnv * env, linkPtr * p, char * name) {
	jclass c;
	if(p != NULL && *p != NULL)
		return *p;
	c = (*env)->FindClass(env, name);
	if((*env)->ExceptionOccurred(env)) {
		return NULL;
	}
	c = (*env)->NewGlobalRef(env, c);
	if(p != NULL)
		*p = c;
	return (linkPtr)c;
}

/* Extra convenience functions */
JNIEXPORT linkPtr JNICALL LINK_LinkConstructor (JNIEnv * env, linkPtr * p, jclass clazz, char * sig) {
	return LINK_LinkMethod(env, p, clazz, "<init>", sig);
}

JNIEXPORT linkPtr JNICALL LINK_LinkKnownClass  (JNIEnv * env, linkPtr * p, jclass clazz) {
	jclass c;
	if(p!=NULL && *p!=NULL)
		return *p;
	c = (*env)->NewGlobalRef(env, clazz);
	if(p!=NULL)
		*p=c;
	return (linkPtr)c;
}

/* The GetXXX functions can be inlined. */
/* Note: GetMethod does actual resolution of the method based on the object type.
 * The object in question *must* be of the correct type.  No type checking is done.
 * If the object is NULL, then the jmethodID will be NULL as well, and no exception
 * will be thrown.  If the method is not found, a MethodNotFoundException will be
 * thrown.
 */
JNIEXPORT jmethodID JNICALL LINK_ResolveMethod      (JNIEnv * env, linkPtr methodLink) {
	return ((linkedMethod*)methodLink)->m;
}

JNIEXPORT jmethodID JNICALL LINK_ResolveStaticMethod(JNIEnv * env, linkPtr methodLink) {
	return ((linkedMethod*)methodLink)->m;
}

JNIEXPORT jfieldID JNICALL  LINK_ResolveField       (JNIEnv * env, linkPtr fieldLink) {
	return ((linkedField*)fieldLink)->f;
}

JNIEXPORT jfieldID JNICALL  LINK_ResolveStaticField (JNIEnv * env, linkPtr fieldLink) {
	return ((linkedField*)fieldLink)->f;
}

JNIEXPORT jclass JNICALL    LINK_ResolveClass       (JNIEnv * env, linkPtr classLink) {
	return (jclass)classLink;
}


/* These are for when the class referencing the symbols is unloaded; it
destroys any object references
 * the linker might have kept around.
 */
JNIEXPORT void JNICALL LINK_UnlinkMethod      (JNIEnv * env, linkPtr methodLink) {
	if(methodLink != NULL)
		(*env)->DeleteGlobalRef(env,((linkedMethod*)methodLink)->theClass);
}

JNIEXPORT void JNICALL LINK_UnlinkStaticMethod(JNIEnv * env, linkPtr methodLink) {
	if(methodLink != NULL)
		(*env)->DeleteGlobalRef(env,((linkedMethod*)methodLink)->theClass);
}

JNIEXPORT void JNICALL LINK_UnlinkField       (JNIEnv * env, linkPtr fieldLink) {
	if(fieldLink != NULL)
		(*env)->DeleteGlobalRef(env,((linkedMethod*)fieldLink)->theClass);
}

JNIEXPORT void JNICALL LINK_UnlinkStaticField (JNIEnv * env, linkPtr fieldLink) {
	if(fieldLink != NULL)
		(*env)->DeleteGlobalRef(env,((linkedMethod*)fieldLink)->theClass);
}

JNIEXPORT void JNICALL LINK_UnlinkClass       (JNIEnv * env, linkPtr classLink) {
	if(classLink != NULL)
		(*env)->DeleteGlobalRef(env, (jclass)classLink);
}
