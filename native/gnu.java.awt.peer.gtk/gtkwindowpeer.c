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
  GtkWidget *fixed, *vbox;

  gtk_window_set_policy (GTK_WINDOW (window), 1, 1, 1);
  gtk_widget_set_usize (window, width, height);
  
  vbox = gtk_vbox_new (0, 0);
  fixed = gtk_fixed_new ();
  gtk_box_pack_end (GTK_BOX (vbox), fixed, 1, 1, 0);
  gtk_container_add (GTK_CONTAINER (window), vbox);
  gtk_widget_realize (fixed);
  connect_awt_hook (env, obj, fixed, 1, fixed->window);
  gtk_widget_show (fixed);
  gtk_widget_show (vbox);

  gtk_widget_realize (window);
  connect_awt_hook (env, obj, window, 1, window->window);
  set_visible (window, visible);

  gtk_widget_set_usize (window, width, height);
}


JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_setMenuBarPeer
  (JNIEnv *env, jobject obj, jobject menubar)
{
  void *wptr, *mptr;
  GtkBox *box;
  GtkMenuBar *mbar;

  if (!menubar) return;

  wptr = NSA_GET_PTR (env, obj);
  mptr = NSA_GET_PTR (env, menubar);

  if (!mptr) return; /* this case should remove a menu */

  gdk_threads_enter ();
  box = GTK_BOX (GTK_BIN (wptr)->child);
  gtk_box_pack_start (box, GTK_WIDGET (mptr), 0, 0, 0);
  gdk_threads_leave ();
}


/*
 * Set a frame's title
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_setTitle
  (JNIEnv *env, jobject obj, jstring title)
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
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_setResizable
  (JNIEnv *env, jobject obj, jboolean resize)
{
  void *ptr;
  
  ptr = NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  gtk_window_set_policy (GTK_WINDOW (ptr), resize, resize, 0);
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
  gint current_x, current_y;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();

  widget = GTK_WIDGET (ptr);
  gdk_window_get_root_origin (widget->window, &current_x, &current_y);
  
  if (current_x != x || current_y != y)
    {
      gdk_window_set_hints (widget->window, x, y, 0, 0, 0, 0, GDK_HINT_POS);
      gdk_window_move (widget->window, x, y);
    }

  gtk_widget_set_usize (widget, width, height);

  gdk_threads_leave ();
}
