/*
 * gtklabelpeer.c -- Native implementation of GtkLabelPeer
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by James E. Blair <corvus@gnu.org>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later verion.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

#include "gtkpeer.h"
#include "GtkLabelPeer.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkLabelPeer_gtkLabelNew
(JNIEnv *env, jobject obj, jstring text, jint just, jboolean visible)
{
  GtkWidget *label, *box;
  const char *str;
  GtkJustification j = GTK_JUSTIFY_CENTER;
 
  switch (just)
    {
    case 0:
      j = GTK_JUSTIFY_LEFT;
      break;
    case 1:
      j = GTK_JUSTIFY_RIGHT;
      break;
    }
 
  str = (*env)->GetStringUTFChars (env, text, NULL);

  gdk_threads_enter ();

  box = gtk_event_box_new ();
  connect_awt_hook (env, obj, box, 1, &box->window);

  label = gtk_label_new (str);
  gtk_widget_show (label);
  gtk_container_add (GTK_CONTAINER (box), label);
  gtk_label_set_justify (GTK_LABEL (label), j);

  set_visible (box, visible);
  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, box);

  (*env)->ReleaseStringUTFChars (env, text, str);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkLabelPeer_gtkLabelSet
(JNIEnv *env, jobject obj, jstring text)
{
  void *ptr;
  const char *str;
  GtkLabel *label;

  ptr = NSA_GET_PTR (env, obj);
  str = (*env)->GetStringUTFChars (env, text, NULL);

  gdk_threads_enter ();

  label = GTK_LABEL (GTK_BIN (ptr)->child);
  gtk_label_set (label, str);

  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, text, str);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkLabelPeer_gtkLabelSetJustify
(JNIEnv *env, jobject obj, jint just)
{
  void *ptr;
  GtkLabel *label;
  GtkJustification j = GTK_JUSTIFY_CENTER;

  switch (just)
    {
    case 0:
      j = GTK_JUSTIFY_LEFT;
      break;
    case 1:
      j = GTK_JUSTIFY_RIGHT;
      break;
    }

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();

  label = GTK_LABEL (GTK_BIN (ptr)->child);
  gtk_label_set_justify (label, j);

  gdk_threads_leave ();
}

