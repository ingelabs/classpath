/*
 * gtkpopupmenupeer.c -- Native implementation of GtkPopupMenuPeer
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
#include "GtkPopupMenuPeer.h"

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
