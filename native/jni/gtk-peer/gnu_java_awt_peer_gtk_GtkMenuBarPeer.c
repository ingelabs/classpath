/* gtkmenubarpeer.c -- Native implementation of GtkMenuBarPeer
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
#include "gnu_java_awt_peer_gtk_GtkMenuBarPeer.h"

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkMenuBarPeer_create
  (JNIEnv *env, jobject obj)
{
  GtkWidget *widget;

  gdk_threads_enter ();
  widget = gtk_menu_bar_new ();
  gtk_widget_show (widget);
  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, widget);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkMenuBarPeer_addMenu
  (JNIEnv *env, jobject obj, jobject menupeer)
{
  void *mbar, *menu;

  mbar = NSA_GET_PTR (env, obj);
  menu = NSA_GET_PTR (env, menupeer);

  gdk_threads_enter ();
  gtk_menu_bar_append (GTK_MENU_BAR (mbar), GTK_WIDGET (menu));
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkMenuBarPeer_delMenu
  (JNIEnv *env, jobject obj, jint index)
{
  void *ptr;
  GList *list;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  list = gtk_container_children (GTK_CONTAINER (ptr));
  list = g_list_nth (list, index);
  gtk_container_remove (GTK_CONTAINER (ptr), GTK_WIDGET (list->data));
  gdk_threads_leave ();
}
