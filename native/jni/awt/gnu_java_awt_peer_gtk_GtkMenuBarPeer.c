/* gtkmenubarpeer.c -- Native implementation of GtkMenuBarPeer
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of the peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


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
