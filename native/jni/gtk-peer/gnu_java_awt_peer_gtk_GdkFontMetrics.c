/* gdkfontmetrics.c
   Copyright (C) 1999 Free Software Foundation, Inc.

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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */

#include "gtkpeer.h"
#include "gnu_java_awt_peer_gtk_GdkGraphics.h"
#include <gdk/gdkx.h>

#define ASCENT      0
#define MAX_ASCENT  1
#define DESCENT     2
#define MAX_DESCENT 3
#define MAX_ADVANCE 4
#define NUM_METRICS 5

JNIEXPORT jintArray JNICALL Java_gnu_java_awt_peer_gtk_GdkFontMetrics_initState
  (JNIEnv *env, jobject obj, jstring fname, jint size)
{
  jintArray array;
  jint *metrics;
  const char *cfname;
  char *xlfd;
  GdkFont *font;
  XFontStruct *xfont;

  cfname = (*env)->GetStringUTFChars (env, fname, NULL);
  xlfd = g_strdup_printf (cfname, (size * 10));
  (*env)->ReleaseStringUTFChars (env, fname, cfname);

  array = (*env)->NewIntArray (env, NUM_METRICS);
  metrics = (*env)->GetIntArrayElements (env, array, NULL);

  gdk_threads_enter ();
  font = gdk_font_load (xlfd);
  xfont = GDK_FONT_XFONT (font);

  metrics[ASCENT]      = font->ascent;
  metrics[MAX_ASCENT]  = xfont->max_bounds.ascent;
  metrics[DESCENT]     = font->descent;
  metrics[MAX_DESCENT] = xfont->max_bounds.descent;
  metrics[MAX_ADVANCE] = xfont->max_bounds.width;
  gdk_threads_leave ();

  g_free (xlfd);
  (*env)->ReleaseIntArrayElements (env, array, metrics, 0);

  NSA_SET_PTR (env, obj, font);

  return array;
}

JNIEXPORT jint JNICALL Java_gnu_java_awt_peer_gtk_GdkFontMetrics_stringWidth
  (JNIEnv *env, jobject obj, jstring str)
{
  GdkFont *font;
  const char *cstr;
  jint width;

  font = (GdkFont *) NSA_GET_PTR (env, obj);
  cstr = (*env)->GetStringUTFChars (env, str, NULL);

  gdk_threads_enter ();
  width = gdk_string_width (font, cstr);
  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, str, cstr);

  return width;
}
