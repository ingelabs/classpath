#ifndef __JNILINK_H__
#define __JNILINK_H__

#include <jni.h>

typedef void* linkPtr;

/* These functions are called to get the link pointers. */
/* One possible optimization for Japhar would be to store the slot number of the method in the linkPtr.
 * Another, which works for JNI too, is to see if the class or method is final and simply store the jmethodID.
 * For JNI, the linkPtr must point to a struct containing the name and sig so that it can be re-resolved for
 * every object.
 */
JNIEXPORT linkPtr JNICALL LINK_LinkMethod      (JNIEnv * env, jclass class, char * name, char * sig);
/* Do we need to re-resolve fields based on objects?  I don't think so, but I could be wrong ... */
JNIEXPORT linkPtr JNICALL LINK_LinkField       (JNIEnv * env, jclass class, char * name, char * sig);
JNIEXPORT linkPtr JNICALL LINK_LinkClass       (JNIEnv * env, char * name);

/* The GetXXX functions can be inlined. */
/* Note: GetMethod does actual resolution of the method based on the object type.
 * The object in question *must* be of the correct type.  No type checking is done.
 */
JNIEXPORT jmethodID JNICALL LINK_GetMethod      (JNIEnv * env, linkPtr methodLink, jobject obj);
JNIEXPORT jfieldID  JNICALL LINK_GetField       (JNIEnv * env, linkPtr fieldLink);
JNIEXPORT jclass    JNICALL LINK_GetClass       (JNIEnv * env, linkPtr classLink);

/* These are for when the class referencing the symbols is unloaded; it
destroys any object references
 * the linker might have kept around.
 */
JNIEXPORT void JNICALL LINK_UnlinkMethod      (JNIEnv * env, linkPtr methodLink);
JNIEXPORT void JNICALL LINK_UnlinkField       (JNIEnv * env, linkPtr fieldLink);
JNIEXPORT void JNICALL LINK_UnlinkClass       (JNIEnv * env, linkPtr classLink);

#endif
