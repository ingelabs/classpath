/* gtkmenuitempeer.c -- Native implementation of GtkMenuItemPeer
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
#include "gnu_java_awt_peer_gtk_GtkMenuItemPeer.h"

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkCheckboxMenuItemPeer_create
  (JNIEnv *env, jobject obj, jstring label)
{
  GtkWidget *widget;
  const char *str;

  str = (*env)->GetStringUTFChars (env, label, NULL);

  gdk_threads_enter ();
  widget = gtk_check_menu_item_new_with_label (str);
  gtk_check_menu_item_set_show_toggle (GTK_CHECK_MENU_ITEM (widget), 1);
  gtk_widget_show (widget);
  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, label, str);

  NSA_SET_PTR (env, obj, widget);
}

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkCheckboxMenuItemPeer_setState
  (JNIEnv *env, jobject obj, jboolean state)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  gtk_check_menu_item_set_active (GTK_CHECK_MENU_ITEM (ptr), state);
  gdk_threads_leave ();
}
