/*
 * gtkmenupeer.c -- Native implementation of GtkMenuPeer
 *
 * Copyright (c) 1999 Free Software Foundation, Inc.
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
#include "GtkMenuPeer.h"

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkMenuPeer_create
  (JNIEnv *env, jobject obj, jstring label)
{
  GtkWidget *menu_title, *menu;
  const char *str;

  str = (*env)->GetStringUTFChars (env, label, NULL);

  gdk_threads_enter ();
  menu = gtk_menu_new ();
  menu_title = gtk_menu_item_new_with_label (str);
  gtk_menu_item_set_submenu (GTK_MENU_ITEM (menu_title), menu);

  gtk_widget_show (menu);
  gtk_widget_show (menu_title);

  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, label, str);

  NSA_SET_PTR (env, obj, menu_title);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkMenuPeer_addItem
  (JNIEnv *env, jobject obj, jobject menuitempeer)
{
  void *ptr1, *ptr2;

  ptr1 = NSA_GET_PTR (env, obj);
  ptr2 = NSA_GET_PTR (env, menuitempeer);

  gdk_threads_enter ();
  gtk_menu_append (GTK_MENU (GTK_MENU_ITEM (ptr1)->submenu), 
		   GTK_WIDGET (ptr2));
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkMenuPeer_delItem
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


