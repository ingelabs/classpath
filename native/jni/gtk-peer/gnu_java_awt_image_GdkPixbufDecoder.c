/* gdkpixbufdecoder.c
   Copyright (C) 1999, 2002 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

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

#include <gtk/gtk.h>
#include <gdk-pixbuf/gdk-pixbuf.h>
#include <gdk-pixbuf/gdk-pixbuf-loader.h>
#include <libart_lgpl/art_misc.h>
#include <libart_lgpl/art_pixbuf.h>
#include "gnu_java_awt_image_GdkPixbufDecoder.h"

#define BUFSIZE 4096

jmethodID areaPreparedID;
jmethodID areaUpdatedID;
jfieldID getFDID;

extern JNIEnv *gdk_env;

#define SWAPU32(w) \
  (((w) << 24) | (((w) & 0xff00) << 8) | (((w) >> 8) & 0xff00) | ((w) >> 24))

struct loader_vector
{
  jobject *loader;
  jobject *vector;
};

JNIEXPORT void JNICALL 
Java_gnu_java_awt_image_GdkPixbufDecoder_initState (JNIEnv *env, jclass clazz)
{
  jclass filedescriptor;

  areaPreparedID = (*env)->GetMethodID (env, clazz, 
				        "areaPrepared", 
					"(Ljava/util/Vector;II)V");

  areaUpdatedID = (*env)->GetMethodID (env, clazz,
				       "areaUpdated",
				       "(Ljava/util/Vector;IIII[II)V");

  filedescriptor = (*env)->FindClass (env, "java/io/FileDescriptor");
  getFDID = (*env)->GetFieldID (env, filedescriptor, "fd", "I"); 
}

static void
area_prepared (GdkPixbufLoader *loader, struct loader_vector *lv)
{
  ArtPixBuf *pixbuf;

  //  pixbuf = GDK_PIXBUF_LOADER(gdk_pixbuf_loader_get_pixbuf (loader))->art_pixbuf;

  gdk_threads_leave ();
  (*gdk_env)->CallVoidMethod (gdk_env,
			      *(lv->loader), 
			      areaPreparedID,
			      *(lv->vector),
			      (jint) pixbuf->width,
			      (jint) pixbuf->height);
  gdk_threads_enter ();
}

static void
area_updated (GdkPixbufLoader *loader, 
	      gint x, gint y, 
	      gint width, gint height,
	      struct loader_vector *lv)
{
  ArtPixBuf *pixbuf;
  jintArray jpixels;
  jint *pixels, scansize, num_pixels;

  /*  pixbuf = gdk_pixbuf_loader_get_pixbuf (loader)->art_pixbuf;

  g_return_if_fail (pixbuf->format == ART_PIX_RGB);
  g_return_if_fail (pixbuf->bits_per_sample == 8);
  g_return_if_fail (pixbuf->n_channels == 3 + (pixbuf->has_alpha != 0));

  scansize = pixbuf->rowstride / pixbuf->n_channels;
  num_pixels = height * scansize;
  jpixels = (*gdk_env)->NewIntArray (gdk_env, num_pixels);
  pixels = (*gdk_env)->GetIntArrayElements  (gdk_env, jpixels, NULL);

  if (pixbuf->has_alpha)
    {
      memcpy (pixels, pixbuf->pixels + 
	      (y * pixbuf->rowstride + (x << 2)),
	      height * pixbuf->rowstride);
    }
  else // add in alpha data 
    {
      int i;
      art_u8 *src, *dest;

      src = pixbuf->pixels + (y * pixbuf->rowstride + x * 3);
      dest = (art_u8 *) pixels;
      
      for (i = 0; i < num_pixels; i++)
	{
	  *dest++ = *src++;	//red
	  *dest++ = *src++;	// green 
	  *dest++ = *src++;	// blue 
	  *dest++ = 0xff;	// alpha 
	}
    }
  */
  gdk_threads_leave ();

#ifndef WORDS_BIGENDIAN
  {
    int i;
    for (i = 0; i < num_pixels; i++)
      pixels[i] = SWAPU32 ((unsigned)pixels[i]);
  }
#endif

  (*gdk_env)->ReleaseIntArrayElements (gdk_env, jpixels, pixels, 0);

  (*gdk_env)->CallVoidMethod (gdk_env, 
			      *(lv->loader), 
			      areaUpdatedID,
			      *(lv->vector), 
			      (jint) x, (jint) y,
			      (jint) width, (jint) height,
			      jpixels,
			      scansize);
  gdk_threads_enter ();
}

static void
closed (GdkPixbufLoader *loader, struct loader_vector *lv)
{
  gdk_threads_leave ();

  (*gdk_env)->DeleteGlobalRef (gdk_env, *(lv->loader)); 
  (*gdk_env)->DeleteGlobalRef (gdk_env, *(lv->vector)); 
  
  free (lv->loader);
  free (lv->vector);
  free (lv);

  gdk_threads_enter ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_image_GdkPixbufDecoder_loaderWrite
  (JNIEnv *env, jobject obj, jobject vector, jobject fd_obj)
{
  jint fd;
  GdkPixbufLoader *loader;
  guchar buf[BUFSIZE];
  ssize_t num_read;
  struct loader_vector *lv;
  ArtPixBuf *pixbuf;

  fd = (*env)->GetIntField (env, fd_obj, getFDID) - 1;

  lv = (struct loader_vector *) malloc (sizeof (struct loader_vector));

  lv->loader = (jobject *) malloc (sizeof (jobject));
  *(lv->loader) = (*env)->NewGlobalRef (env, obj);

  lv->vector = (jobject *) malloc (sizeof (jobject));
  *(lv->vector) = (*env)->NewGlobalRef (env, vector);

  gdk_threads_enter ();
  loader = gdk_pixbuf_loader_new ();

  gtk_signal_connect (GTK_OBJECT (loader),
		      "area_prepared",
		      GTK_SIGNAL_FUNC (area_prepared),
		      lv);

  gtk_signal_connect (GTK_OBJECT (loader),
		      "area_updated",
		      GTK_SIGNAL_FUNC (area_updated),
		      lv);

  gtk_signal_connect (GTK_OBJECT (loader),
		      "closed",
		      GTK_SIGNAL_FUNC (closed),
		      lv);
  gdk_threads_leave ();

  do
    {
      num_read = read (fd, buf, BUFSIZE);

      /* we should throw an exception here */
      if (num_read < 0)
	perror ("error while reading fd");

      gdk_threads_enter ();
      gdk_pixbuf_loader_write (loader, buf, num_read);
      gdk_threads_leave ();

    } while (num_read > 0);

/*    gdk_threads_enter (); */
/*    pixbuf = gdk_pixbuf_loader_get_pixbuf (loader)->art_pixbuf; */

/*     gtk_signal_emit_by_name (GTK_OBJECT (loader), "area_updated", 0, 0,  */
/*  			   pixbuf->width, pixbuf->height, NULL); */
/*    gdk_threads_leave (); */

/*    printf ("READY TO CLOSE!\n"); */

/*    gdk_threads_enter (); */
/*    gdk_pixbuf_loader_close (loader); */
/*    gtk_object_destroy (GTK_OBJECT (loader)); */
/*    gdk_threads_leave (); */
}
