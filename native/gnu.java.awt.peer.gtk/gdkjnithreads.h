#ifndef __GDKJNITHREADS_H__
#define __GDKJNITHREADS_H__

#include <jni.h>
#include <gdk/gdk.h>

typedef struct _GdkJavaMutex
{
  JNIEnv *env;
  jobject *mutex;
} GdkJavaMutex;

extern GdkMutexFuncs jni_threads_mutex_funcs;

#endif __GDKJNITHREADS_H__
