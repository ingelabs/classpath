#include "gdkjnithreads.h"
#include <gdk/gdk.h>
#include <glib.h>
#include "jni.h"

gpointer
jni_threads_create_mutex (gpointer init)
{
  jclass obj_class;
  JNIEnv *env = (JNIEnv *) init;
  jobject *mutex;
  GdkJavaMutex *mutex_wrapper;
  
  if (env == NULL)
    return NULL;

  obj_class = (*env)->FindClass (env, "java/lang/Object");
  if (obj_class == NULL)
    return NULL;

  mutex = (jobject *) g_malloc (sizeof (jobject));
  *mutex = (*env)->AllocObject (env, obj_class);
  if (*mutex == NULL)
    {
      g_free (mutex);
      return NULL;
    }
  *mutex = (*env)->NewGlobalRef (env, *mutex);

  mutex_wrapper = (GdkJavaMutex *) g_malloc (sizeof (GdkJavaMutex));
  mutex_wrapper->env = env;
  mutex_wrapper->mutex = mutex;

  return mutex_wrapper;
}

void
jni_threads_lock_mutex (gpointer mutex)
{
  GdkJavaMutex *mutex_wrapper = (GdkJavaMutex *) mutex;

  (*mutex_wrapper->env)->MonitorEnter (mutex_wrapper->env, 
				       *mutex_wrapper->mutex);
}

void
jni_threads_unlock_mutex (gpointer mutex)
{
  GdkJavaMutex *mutex_wrapper = (GdkJavaMutex *) mutex;

  (*mutex_wrapper->env)->MonitorExit (mutex_wrapper->env, 
				      *mutex_wrapper->mutex);
}

void
jni_threads_destroy_mutex (gpointer mutex)
{
  GdkJavaMutex *mutex_wrapper = (GdkJavaMutex *) mutex;

  (*mutex_wrapper->env)->DeleteGlobalRef (mutex_wrapper->env,
					  *mutex_wrapper->mutex);
  g_free (mutex_wrapper->mutex);
  g_free (mutex_wrapper);
}

GdkMutexFuncs jni_threads_mutex_funcs = 
{
  jni_threads_create_mutex,
  jni_threads_lock_mutex,
  jni_threads_unlock_mutex,
  jni_threads_destroy_mutex
};
