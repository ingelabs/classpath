/* gtkmenupeer.c -- Native implementation of GtkMenuPeer
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
#include "gnu_java_awt_peer_gtk_GtkMenuPeer.h"

static void
accel_attach (GtkMenuItem *menu_item, gpointer *user_data)
{
  GtkAccelGroup *accel;

  accel = gtk_menu_get_accel_group (GTK_MENU (menu_item->submenu));
  gtk_accel_group_attach (accel, 
    GTK_OBJECT (gtk_widget_get_toplevel (GTK_WIDGET(menu_item))));
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkMenuPeer_setupAccelGroup
  (JNIEnv *env, jobject obj, jobject parent)
{
  void *ptr1, *ptr2;

  ptr1 = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  if (!parent)
    {
      gtk_menu_set_accel_group (GTK_MENU (GTK_MENU_ITEM (ptr1)->submenu), 
				gtk_accel_group_new ());

      if (GTK_WIDGET_REALIZED (GTK_WIDGET (ptr1)))
	accel_attach (GTK_MENU_ITEM (ptr1), NULL);
      else
	gtk_signal_connect (GTK_OBJECT (ptr1),
			    "realize",
			    GTK_SIGNAL_FUNC (accel_attach), 
			    NULL);
    }
  else
    {
      GtkAccelGroup *parent_accel;

      ptr2 = NSA_GET_PTR (env, parent);
      parent_accel = gtk_menu_get_accel_group (GTK_MENU (GTK_MENU_ITEM (ptr2)->submenu));
      
      gtk_menu_set_accel_group (GTK_MENU (GTK_MENU_ITEM (ptr1)->submenu),
				parent_accel);
    }
      
  gdk_threads_leave ();
}


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

  NSA_SET_PTR (env, obj, menu_title);
  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, label, str);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkMenuPeer_addItem
  (JNIEnv *env, jobject obj, jobject menuitempeer, jint key, jboolean shift)
{
  void *ptr1, *ptr2;
  GtkMenu *menu;

  ptr1 = NSA_GET_PTR (env, obj);
  ptr2 = NSA_GET_PTR (env, menuitempeer);

  gdk_threads_enter ();

  menu = GTK_MENU (GTK_MENU_ITEM (ptr1)->submenu);
  gtk_menu_append (menu, GTK_WIDGET (ptr2));

  if (key)
    {
      gtk_widget_add_accelerator (GTK_WIDGET (ptr2), "activate",
				  gtk_menu_get_accel_group (menu), key, 
				  (GDK_CONTROL_MASK
				   | ((shift) ? GDK_SHIFT_MASK : 0)), 
				  GTK_ACCEL_VISIBLE);
    }

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


