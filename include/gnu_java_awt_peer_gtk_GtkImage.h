#ifndef __gnu_java_awt_peer_gtk_GtkImage__
#define __gnu_java_awt_peer_gtk_GtkImage__
#include <jni.h>

#ifdef __cplusplus

extern "C" {
#endif

JNIEXPORT jintArray JNICALL Java_gnu_java_awt_peer_gtk_GtkImage_getPixels
  (JNIEnv *, jobject);

JNIEXPORT  void JNICALL Java_gnu_java_awt_peer_gtk_GtkImage_setPixels
  (JNIEnv *, jobject, jintArray);

JNIEXPORT jboolean JNICALL Java_gnu_java_awt_peer_gtk_GtkImage_loadPixbuf
  (JNIEnv *, jobject, jstring);

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkImage_createPixmap
  (JNIEnv *, jobject);

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkImage_freePixmap
  (JNIEnv *, jobject);

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkImage_createScaledPixmap
  (JNIEnv *, jobject, jobject, jint);

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkImage_drawPixelsScaled 
  (JNIEnv *, jobject, jobject, jint, jint, jint, jint, jint, jint, jint, jboolean);

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkImage_drawPixelsScaledFlipped 
  (JNIEnv *, jobject, jobject, jint, jint, jint, 
   jboolean, jboolean, 
   jint, jint, jint, jint, 
   jint, jint, jint, jint, 
   jboolean);

#ifdef __cplusplus
}
#endif
#endif

