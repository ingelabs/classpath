#include <jni.h>
#ifndef _Included_IconvEncoder
#define _Included_IconvEncoder
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_gnu_java_nio_charset_iconv_IconvEncoder_openIconv
  (JNIEnv *, jobject, jstring);
JNIEXPORT jint JNICALL Java_gnu_java_nio_charset_iconv_IconvEncoder_encode
  (JNIEnv *, jobject, jcharArray, jbyteArray, jint,jint,jint,jint);
JNIEXPORT void JNICALL Java_gnu_java_nio_charset_iconv_IconvEncoder_closeIconv
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
