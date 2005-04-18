#include <jni.h>
#ifndef _Included_IconvDecoder
#define _Included_IconvDecoder
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_gnu_java_nio_charset_iconv_IconvDecoder_openIconv
  (JNIEnv *, jobject, jstring);
JNIEXPORT jint JNICALL Java_gnu_java_nio_charset_iconv_IconvDecoder_decode
  (JNIEnv *, jobject, jbyteArray, jcharArray, jint,jint,jint,jint);
JNIEXPORT void JNICALL Java_gnu_java_nio_charset_iconv_IconvDecoder_closeIconv
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
