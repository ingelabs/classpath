#ifndef __JNILINK_H__
#define __JNILINK_H__

#include <jni.h>

typedef void* linkPtr;

/* These functions are called to get the link pointers. */
/* One possible optimization for Japhar would be to store the slot number of the method in the linkPtr.
 * Another, which works for JNI too, is to see if the class or method is final and simply store the jmethodID.
 * For JNI, the linkPtr must point to a struct containing the name and sig so that it can be re-resolved for
 * every object.
 * If the linkPtr parameter is not null, it will be linked if and only if it is not already linked.
 * Otherwise, the Link* method will always relink.  It will always return the value it put in p or would have
 * put in p.
 */
JNIEXPORT linkPtr JNICALL LINK_LinkMethod      (JNIEnv * env, linkPtr * p, jclass clazz, char * name, char * sig);
JNIEXPORT linkPtr JNICALL LINK_LinkStaticMethod(JNIEnv * env, linkPtr * p, jclass clazz, char * name, char * sig);
JNIEXPORT linkPtr JNICALL LINK_LinkField       (JNIEnv * env, linkPtr * p, jclass clazz, char * name, char * sig);
JNIEXPORT linkPtr JNICALL LINK_LinkStaticField (JNIEnv * env, linkPtr * p, jclass clazz, char * name, char * sig);
JNIEXPORT linkPtr JNICALL LINK_LinkClass       (JNIEnv * env, linkPtr * p, char * name);
/* Extra convenience functions */
JNIEXPORT linkPtr JNICALL LINK_LinkConstructor (JNIEnv * env, linkPtr * p, jclass clazz, char * sig);
JNIEXPORT linkPtr JNICALL LINK_LinkKnownClass  (JNIEnv * env, linkPtr * p, jclass clazz);

/* The GetXXX functions can be inlined. */
/* Note: GetMethod does actual resolution of the method based on the object type.
 * The object in question *must* be of the correct type.  No type checking is done.
 */
JNIEXPORT jmethodID JNICALL LINK_ResolveMethod      (JNIEnv * env, linkPtr methodLink);
JNIEXPORT jmethodID JNICALL LINK_ResolveStaticMethod(JNIEnv * env, linkPtr methodLink);
JNIEXPORT jfieldID  JNICALL LINK_ResolveField       (JNIEnv * env, linkPtr fieldLink);
JNIEXPORT jfieldID  JNICALL LINK_ResolveStaticField (JNIEnv * env, linkPtr fieldLink);
JNIEXPORT jclass    JNICALL LINK_ResolveClass       (JNIEnv * env, linkPtr classLink);

/* These are for when the class referencing the symbols is unloaded; it
destroys any object references
 * the linker might have kept around.
 */
JNIEXPORT void JNICALL LINK_UnlinkMethod      (JNIEnv * env, linkPtr methodLink);
JNIEXPORT void JNICALL LINK_UnlinkStaticMethod(JNIEnv * env, linkPtr methodLink);
JNIEXPORT void JNICALL LINK_UnlinkField       (JNIEnv * env, linkPtr fieldLink);
JNIEXPORT void JNICALL LINK_UnlinkStaticField (JNIEnv * env, linkPtr fieldLink);
JNIEXPORT void JNICALL LINK_UnlinkClass       (JNIEnv * env, linkPtr classLink);

#endif
