/*
 * gtkwindowpeer.c -- Native implementation of GtkWindowPeer
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
#include "GtkWindowPeer.h"

/*
 * Make a new window (any type)
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_gtkWindowNew (JNIEnv *env, 
  jobject obj, jint type, jint width, jint height, jboolean visible)
{
  GtkWidget *window;

  gdk_threads_enter ();
  switch (type)
    {
    case 1:
      window = gtk_window_new (GTK_WINDOW_TOPLEVEL);
      break;
    case 3:
      window = gtk_window_new (GTK_WINDOW_POPUP);
      break;
    }

  setup_window (env, obj, window, width, height, visible);

  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, window);
}


void
setup_window (JNIEnv *env, jobject obj, GtkWidget *window, jint width, 
	      jint height, jboolean visible)
{
  GtkWidget *fixed;

  gtk_window_set_policy (GTK_WINDOW (window), 1, 1, 1);
  gtk_widget_set_usize (window, width, height);
  
  fixed = gtk_fixed_new ();
  gtk_container_add (GTK_CONTAINER (window), fixed);
  gtk_widget_realize (fixed);
  connect_awt_hook (env, obj, fixed, 1, fixed->window);
  gtk_widget_show (fixed);

  gtk_widget_realize (window);
  connect_awt_hook (env, obj, window, 1, window->window);
  set_visible (window, visible);

  gtk_widget_set_usize (window, width, height);

  gtk_container_add (GTK_CONTAINER (window), fixed);
}


/*
 * Set a frame's title
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_gtkWindowSetTitle (JNIEnv *env, 
  jobject obj, jstring title)
{
  void *ptr;
  const char *str;

  ptr = NSA_GET_PTR (env, obj);
  
  str = (*env)->GetStringUTFChars (env, title, NULL);
  
  gdk_threads_enter ();
  gtk_window_set_title (GTK_WINDOW (ptr), str);
  gdk_threads_leave ();
  
  (*env)->ReleaseStringUTFChars (env, title, str);
}

/*
 * Set a window's resizing policy
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_gtkWindowSetPolicy (JNIEnv *env, 
    jobject obj, jint shrink, jint grow, jint autos)
{
  void *ptr;
  
  ptr = NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  gtk_window_set_policy (GTK_WINDOW (ptr), shrink, grow, autos);
  gdk_threads_leave ();
}


/*
 * Lower the z-level of a window. 
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_toBack (JNIEnv *env, 
    jobject obj)
{
  void *ptr;
  ptr = NSA_GET_PTR (env, obj);
    
  gdk_threads_enter ();
  gdk_window_lower (GTK_WIDGET (ptr)->window);
  gdk_threads_leave ();
}

/*
 * Raise the z-level of a window.
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_toFront (JNIEnv *env, 
    jobject obj)
{
  void *ptr;
  ptr = NSA_GET_PTR (env, obj);
    
  gdk_threads_enter ();
  gdk_window_raise (GTK_WIDGET (ptr)->window);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkWindowPeer_setBounds
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  GtkWidget *widget;
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();

  widget = GTK_WIDGET (ptr);
  gdk_window_set_hints (widget->window, x, y, 0, 0, 0, 0, GDK_HINT_POS);
  gdk_window_move (widget->window, x, y);
  gtk_widget_set_usize (widget, width, height);

  gdk_threads_leave ();
}
