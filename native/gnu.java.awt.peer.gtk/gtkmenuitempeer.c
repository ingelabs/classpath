/*
 * gtkmenuitempeer.c -- Native implementation of GtkMenuItemPeer
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
#include "GtkMenuItemPeer.h"

static void
connect_activate_hook (JNIEnv *, jobject, GtkMenuItem *);

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkMenuItemPeer_create
  (JNIEnv *env, jobject obj, jstring label)
{
  GtkWidget *widget;
  const char *str;

  str = (*env)->GetStringUTFChars (env, label, NULL);

  gdk_threads_enter ();

  if (strcmp (str, "-") == 0) /* "-" signals that we need a separator */
    widget = gtk_menu_item_new ();
  else
    widget = gtk_menu_item_new_with_label (str);

  connect_activate_hook (env, obj, GTK_MENU_ITEM (widget));
  gtk_widget_show (widget);
  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, label, str);

  NSA_SET_PTR (env, obj, widget);
}

static void
item_activate (GtkMenuItem *item, jobject *peer_obj)
{
  (*gdk_env)->CallVoidMethod (gdk_env, *peer_obj,
			      postMenuActionEventID);
}

static void
connect_activate_hook (JNIEnv *env, jobject peer_obj, GtkMenuItem *item)
{
  jobject *obj;

  obj = (jobject *) malloc (sizeof (jobject));
  *obj = (*env)->NewGlobalRef (env, peer_obj);

  gtk_signal_connect (GTK_OBJECT (item), "activate", 
		      GTK_SIGNAL_FUNC (item_activate), obj);
}
