/* gtkpopupmenupeer.c -- Native implementation of GtkPopupMenuPeer
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
#include "gnu_java_awt_peer_gtk_GtkPopupMenuPeer.h"

struct pos
{
  gint x;
  gint y;
};

void 
menu_pos (GtkMenu *menu, gint *x, gint *y, gpointer user_data)
{
  struct pos *p = (struct pos *) user_data;

  *x = p->x;
  *y = p->y;
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkPopupMenuPeer_show
  (JNIEnv *env, jobject obj, jint x, jint y, jlong time)
{
  void *ptr;
  struct pos *p;

  ptr = NSA_GET_PTR (env, obj);

  p = g_malloc (sizeof (struct pos));
  p->x = x;
  p->y = y;
  
  gdk_threads_enter ();
  gtk_menu_popup (GTK_MENU (GTK_MENU_ITEM (ptr)->submenu), 
		  NULL, NULL, menu_pos, p, 3, time);
  gdk_threads_leave ();

  g_free (p);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkPopupMenuPeer_setupAccelGroup
  (JNIEnv *env, jobject obj, jobject parent)
{
  void *ptr1, *ptr2;
  GtkMenu *menu;

  ptr1 = NSA_GET_PTR (env, obj);
  ptr2 = NSA_GET_PTR (env, parent);

  gdk_threads_enter ();
  menu = GTK_MENU (GTK_MENU_ITEM (ptr1)->submenu);
  gtk_menu_set_accel_group (menu, gtk_accel_group_new ());
  gtk_accel_group_attach (gtk_menu_get_accel_group (menu),
			  GTK_OBJECT (gtk_widget_get_toplevel (ptr2)));
  gdk_threads_leave ();
}
