#ifndef __PRIMLIB_H__
#define __PRIMLIB_H__

#include <jni.h>

#define PRIMLIB_UNKNOWN  0
#define PRIMLIB_OBJECT   1
#define PRIMLIB_BOOLEAN  2
#define PRIMLIB_BYTE     3
#define PRIMLIB_CHAR     4
#define PRIMLIB_SHORT    5
#define PRIMLIB_INT      6
#define PRIMLIB_LONG     7
#define PRIMLIB_FLOAT    8
#define PRIMLIB_DOUBLE   9
#define PRIMLIB_VOID     10
#define PRIMLIB_NULL     11
#define PRIMLIB_NUMTYPES 12

/* Low-level primitive class accessor functions. */
JNIEXPORT jclass JNICALL PRIMLIB_GetNativeWrapClass(JNIEnv * env, int reflectType);
JNIEXPORT jclass JNICALL PRIMLIB_GetNativeTypeClass(JNIEnv * env, int reflectType);
JNIEXPORT jmethodID JNICALL PRIMLIB_GetNativeWrapClassConstructor(JNIEnv * env, int reflectType);
JNIEXPORT jmethodID JNICALL PRIMLIB_GetNativeWrapClassAccessor(JNIEnv * env, int reflectType);

/* Type discovery functions: WrapperType finds out j.l.Boolean/Byte/etc., and
   Type finds out j.l.Boolean.TYPE, etc.
*/
JNIEXPORT jint JNICALL PRIMLIB_GetReflectiveWrapperType(JNIEnv * env, jobject obj);
JNIEXPORT jint JNICALL PRIMLIB_GetReflectiveType(JNIEnv * env, jclass returnType);

/* Constructor functions. */
JNIEXPORT jobject JNICALL PRIMLIB_WrapBoolean(JNIEnv * env, jboolean b);
JNIEXPORT jobject JNICALL PRIMLIB_WrapByte   (JNIEnv * env, jbyte b);
JNIEXPORT jobject JNICALL PRIMLIB_WrapChar   (JNIEnv * env, jchar c);
JNIEXPORT jobject JNICALL PRIMLIB_WrapShort  (JNIEnv * env, jshort s);
JNIEXPORT jobject JNICALL PRIMLIB_WrapInt    (JNIEnv * env, jint i);
JNIEXPORT jobject JNICALL PRIMLIB_WrapLong   (JNIEnv * env, jlong l);
JNIEXPORT jobject JNICALL PRIMLIB_WrapFloat  (JNIEnv * env, jfloat f);
JNIEXPORT jobject JNICALL PRIMLIB_WrapDouble (JNIEnv * env, jdouble d);

/* Widening conversion unwrapping functions. */
JNIEXPORT jboolean JNICALL PRIMLIB_UnwrapBoolean(JNIEnv * env, jobject obj);
JNIEXPORT jbyte    JNICALL PRIMLIB_UnwrapByte   (JNIEnv * env, jobject obj);
JNIEXPORT jshort   JNICALL PRIMLIB_UnwrapShort  (JNIEnv * env, jobject obj);
JNIEXPORT jchar    JNICALL PRIMLIB_UnwrapChar   (JNIEnv * env, jobject obj);
JNIEXPORT jint     JNICALL PRIMLIB_UnwrapInt    (JNIEnv * env, jobject obj);
JNIEXPORT jlong    JNICALL PRIMLIB_UnwrapLong   (JNIEnv * env, jobject obj);
JNIEXPORT jfloat   JNICALL PRIMLIB_UnwrapFloat  (JNIEnv * env, jobject obj);
JNIEXPORT jdouble  JNICALL PRIMLIB_UnwrapDouble (JNIEnv * env, jobject obj);

/* Simple unwrapping functions. Objects *must* be of correct type. */
JNIEXPORT jboolean JNICALL PRIMLIB_GetBooleanObjectValue(JNIEnv * env, jobject obj);
JNIEXPORT jbyte    JNICALL PRIMLIB_GetByteObjectValue   (JNIEnv * env, jobject obj);
JNIEXPORT jshort   JNICALL PRIMLIB_GetShortObjectValue  (JNIEnv * env, jobject obj);
JNIEXPORT jchar    JNICALL PRIMLIB_GetCharObjectValue   (JNIEnv * env, jobject obj);
JNIEXPORT jint     JNICALL PRIMLIB_GetIntObjectValue    (JNIEnv * env, jobject obj);
JNIEXPORT jlong    JNICALL PRIMLIB_GetLongObjectValue   (JNIEnv * env, jobject obj);
JNIEXPORT jfloat   JNICALL PRIMLIB_GetFloatObjectValue  (JNIEnv * env, jobject obj);
JNIEXPORT jdouble  JNICALL PRIMLIB_GetDoubleObjectValue (JNIEnv * env, jobject obj);

/* jvalue conversion: Unwrap obj to the type of classType, with widening conversion. */
JNIEXPORT jvalue JNICALL PRIMLIB_UnwrapJValue(JNIEnv* env, jobject obj, jclass classType);

#endif
