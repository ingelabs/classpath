/*gst_native_pipeline.c - Header file for the GstClasspathPlugin
 Copyright (C) 2007 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

#include <jni.h>
#include <jcl.h>

#include <string.h>
#include <stdlib.h>

#include <gdk/gdk.h>
#include <glib.h>

#include <gst/gst.h>

#include "cpio.h"
#include "gst_peer.h"

#include "gnu_javax_sound_sampled_gstreamer_lines_GstPipeline.h"
#include "gst_native_pipeline.h"

static jmethodID pointerConstructorMID = NULL;

static jfieldID pipelineFID = NULL;
static jfieldID pointerDataFID = NULL;
static jfieldID nameFID = NULL;

enum
{
  PLAY,
  PAUSE,
  STOP
};

struct _GstNativePipelinePrivate
{
  JavaVM *vm;
  jclass GstPipelineClass;
  jclass PointerClass;
  
  jobject jni_pipeline;
  char *name;
  GstElement *pipeline;
};

/* ************************************************************************** */

static void init_pointer_IDs (JNIEnv* env);

/* ************************************************************************** */

/* JNI Methods */

JNIEXPORT void JNICALL
Java_gnu_javax_sound_sampled_gstreamer_lines_GstPipeline_init_1id_1cache
  (JNIEnv *env, jclass clazz)
{
  pipelineFID = (*env)->GetFieldID (env, clazz, "pipeline",
                                    "Lgnu/classpath/Pointer;");
  nameFID = (*env)->GetFieldID (env, clazz, "name", "Ljava/lang/String;");

  init_pointer_IDs(env);
}

JNIEXPORT void JNICALL
Java_gnu_javax_sound_sampled_gstreamer_lines_GstPipeline_init_1instance
  (JNIEnv *env, jobject pipeline)
{
  GstNativePipeline *_pipeline = NULL;
  
  jclass localGstPipelineClass = NULL;
  jclass localPointerClass = NULL;
  jobject _pointer = NULL;
  
  _pipeline =
    (GstNativePipeline *) JCL_malloc (env, sizeof (GstNativePipeline));
  if (_pipeline == NULL)
    return;
  
  _pipeline->priv = (GstNativePipelinePrivate *)
    JCL_malloc (env, sizeof (GstNativePipelinePrivate));
  if (_pipeline->priv == NULL)
    {
      JCL_free (env, _pipeline);
      return;
    }
  
#if SIZEOF_VOID_P == 8
  localPointerClass = JCL_FindClass (env, "gnu/classpath/Pointer64");
#else
# if SIZEOF_VOID_P == 4
  localPointerClass = JCL_FindClass (env, "gnu/classpath/Pointer32");
# else
#   error "Pointer size is not supported."
# endif /* SIZEOF_VOID_P == 4 */
#endif /* SIZEOF_VOID_P == 8 */

  localGstPipelineClass = (*env)->GetObjectClass(env, pipeline);
  if (localGstPipelineClass == NULL || localGstPipelineClass == NULL)
    {
      JCL_free (env, _pipeline->priv);
      JCL_free (env, _pipeline);
      JCL_ThrowException (env, "java/lang/InternalError",
                               "Class Initialization failed.");
      return;
    }

  /* fill the object */
  (*env)->GetJavaVM(env, &_pipeline->priv->vm);
  _pipeline->priv->jni_pipeline = (*env)->NewGlobalRef(env, pipeline);
  _pipeline->priv->GstPipelineClass =
    (*env)->NewGlobalRef(env, localGstPipelineClass);
  _pipeline->priv->PointerClass = (*env)->NewGlobalRef(env, localPointerClass);
  _pipeline->priv->pipeline = NULL;
  
  _pointer = (*env)->GetObjectField(env, pipeline, pipelineFID);
  
  if (_pointer == NULL)
    {
#if SIZEOF_VOID_P == 8
      _pointer = (*env)->NewObject(env, _pipeline->priv->PointerClass,
                                   pointerConstructorMID, (jlong) _pipeline);
#else
      _pointer = (*env)->NewObject(env, _pipeline->priv->PointerClass,
                                   pointerConstructorMID, (jint) _pipeline);
#endif
    }
  else
    {
#if SIZEOF_VOID_P == 8
      (*env)->SetLongField(env, pipeline, pipelineFID, (jlong) _pipeline);
#else
      (*env)->SetIntField(env, pipeline, pipelineFID, (jint) _pipeline);
#endif
    }
      
  /* store back our pointer into the calling class */
  (*env)->SetObjectField(env, pipeline, pipelineFID, _pointer);
}

JNIEXPORT jboolean JNICALL
Java_gnu_javax_sound_sampled_gstreamer_lines_GstPipeline_set_1state
  (JNIEnv *env, jclass clazz __attribute__ ((unused)), 
   jobject pointer, jint state)
{
  GstNativePipeline *jpipeline = NULL;
  jboolean result = JNI_FALSE;
  
  if (pointer == NULL)
    {
      JCL_ThrowException (env, "javax/sound/sampled/LineUnavailableException",
                               "Can't change pipeline state: " \
                               "pipeline not initialized");
      return result;
    }
    
  jpipeline = (GstNativePipeline *) get_object_from_pointer (env, pointer,
                                                             pointerDataFID);
  if (jpipeline == NULL)
    return JNI_FALSE;
                                                         
  switch (state)
    {
      case (PLAY):
        gst_element_set_state(GST_ELEMENT(jpipeline->priv->pipeline),
                              GST_STATE_PLAYING);
        result = JNI_TRUE;
        break;
        
      case (PAUSE):
        gst_element_set_state(GST_ELEMENT(jpipeline->priv->pipeline),
                              GST_STATE_PAUSED);
        result = JNI_TRUE;
        break;
        
      case (STOP):
#ifndef WITHOUT_FILESYSTEM
        /* clean the pipeline and kill named pipe */
        if (jpipeline->priv->name)
          {
            cpio_removeFile (jpipeline->priv->name);
            g_free (jpipeline->priv->name);
          }
#endif /* WITHOUT_FILESYSTEM */
  
        if (jpipeline->priv->pipeline != NULL)
          gst_object_unref (GST_OBJECT(jpipeline->priv->pipeline));
        result = JNI_TRUE;
        break;
        
      default:
        /* nothing */
        result = JNI_FALSE;
        break; 
    }
    
  return result;
}

JNIEXPORT jboolean JNICALL
Java_gnu_javax_sound_sampled_gstreamer_lines_GstPipeline_create_1named_1pipe
  (JNIEnv *env, jobject GstPipeline, jobject pointer)
{
#ifndef WITHOUT_FILESYSTEM
  /*
   * We get a temp name for the named pipe, create the named pipe and then
   * set the relative field in the java class.
   */
  GstNativePipeline *jpipeline = NULL;
  jstring *name = NULL;
  
  jpipeline = (GstNativePipeline *) get_object_from_pointer (env, pointer,
                                                             pointerDataFID);
  if (jpipeline == NULL)
    return JNI_FALSE;                                                        
  
  jpipeline->priv->name = tempnam (NULL, "cpgst");
  if (jpipeline->priv->name == NULL)
    return JNI_FALSE;
    
  if (mkfifo (jpipeline->priv->name, 0600) < 0)
    {
      if (jpipeline->priv->name != NULL)
        free (jpipeline->priv->name);
      return JNI_FALSE;
    }
  
  /* now set the String field */
  name = (*env)->NewStringUTF(env, jpipeline->priv->name);
  if (name == NULL)
    {
      cpio_removeFile (jpipeline->priv->name);
      if (jpipeline->priv->name != NULL)
        free (jpipeline->priv->name);
      
      return JNI_FALSE;
    }
  
  (*env)->SetObjectField(env, GstPipeline, nameFID, name);
  
  return JNI_TRUE;
  
#else /* not WITHOUT_FILESYSTEM */
  return JNI_FALSE;
#endif /* not WITHOUT_FILESYSTEM */

}

/* exported library functions */

void gst_native_pipeline_clean (GstNativePipeline *self)
{
  JNIEnv *env = NULL;
  
  env = gst_get_jenv (self->priv->vm);
  
  (*env)->DeleteGlobalRef (env, self->priv->jni_pipeline);
  (*env)->DeleteGlobalRef (env, self->priv->GstPipelineClass);
  (*env)->DeleteGlobalRef (env, self->priv->PointerClass);
  
  if (self->priv->pipeline != NULL)
    gst_object_unref (GST_OBJECT (self->priv->pipeline));
  
  JCL_free (env, self->priv);
  JCL_free (env, self);
}

void gst_native_pipeline_set_pipeline (GstNativePipeline *self,
                                       GstElement *pipeline)
{
  if (self->priv->pipeline != NULL)
    gst_object_unref (GST_OBJECT (self->priv->pipeline));
    
  self->priv->pipeline = pipeline;
}

GstElement *gst_native_pipeline_get_pipeline (GstNativePipeline *self)
{
  return self->priv->pipeline;
}

char *gst_native_pipeline_get_pipeline_name (GstNativePipeline *self)
{
  return self->priv->name;
}

/* private functions */

static void init_pointer_IDs (JNIEnv* env)
{
  jclass PointerClass = NULL;
  
#if SIZEOF_VOID_P == 8
  PointerClass = JCL_FindClass (env, "gnu/classpath/Pointer64");
  if (PointerClass != NULL)
    {
      pointerDataFID = (*env)->GetFieldID (env, PointerClass, "data", "J");
      pointerConstructorMID = (*env)->GetMethodID (env, PointerClass, "<init>",
                                                   "(J)V");
    }
#else
# if SIZEOF_VOID_P == 4
  PointerClass = JCL_FindClass (env, "gnu/classpath/Pointer32"); 
  if (PointerClass != NULL)
    { 
      pointerDataFID = (*env)->GetFieldID(env, PointerClass, "data", "I");
      pointerConstructorMID = (*env)->GetMethodID(env, PointerClass,
                                                  "<init>", "(I)V");
    }
# else
#   error "Pointer size is not supported."
# endif /* SIZEOF_VOID_P == 4 */
#endif /* SIZEOF_VOID_P == 8 */
}

