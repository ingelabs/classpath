/*
 * gtkdialogpeer.c -- Native implementation of GtkDialogPeer
 *
 * Copyright (c) 1999 Free Software Foundation, Inc.
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
#include "GtkDialogPeer.h"

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkDialogPeer_create
  (JNIEnv *env, jobject obj, jstring title)
{
  GtkWidget *window;
  const char *str;

  str = (*env)->GetStringUTFChars (env, title, NULL);
  
  gdk_threads_enter ();

  window = gtk_window_new (GTK_WINDOW_DIALOG);
  gtk_window_set_title (GTK_WINDOW (window), str);
  
  NSA_SET_PTR (env, obj, window);
  gdk_threads_leave ();
  
  (*env)->ReleaseStringUTFChars (env, title, str);
}

/* equivalent functionality should eventually be added,
   since we no longer use this function. */
JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkDialogPeer_gtkDialogNew
  (JNIEnv *env, jobject obj, jint width, jint height, jboolean visible, 
   jboolean resizable, jstring title, jboolean modal, jobject parent)
{
  GtkWidget *window, *parent_window;
  const char *titlestr;


  titlestr = (*env)->GetStringUTFChars (env, title, NULL);

  gdk_threads_enter ();

  parent_window = GTK_WIDGET (NSA_GET_PTR (env, parent));
  printf ("modal: %i\n", modal);

  window = gtk_window_new (GTK_WINDOW_DIALOG);
  setup_window (env, obj, window, width, height, visible);
  gtk_window_set_title (GTK_WINDOW (window), titlestr);
  gtk_window_set_transient_for (GTK_WINDOW (window), 
				GTK_WINDOW (parent_window));
  if (modal)
    gtk_window_set_modal (GTK_WINDOW (window), TRUE);

  gdk_threads_leave ();
  (*env)->ReleaseStringUTFChars (env, title, titlestr);
  NSA_SET_PTR (env, obj, window);
}



