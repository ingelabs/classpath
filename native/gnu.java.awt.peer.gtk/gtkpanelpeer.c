/*
 * gtkpanelpeer.c -- Native implementation of GtkPanelPeer
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by Paul Fisher <rao@gnu.org>
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
#include "GtkPanelPeer.h"

/*
 * Make a new panel.
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkPanelPeer_gtkPanelNew
    (JNIEnv *env, jobject obj, jobject parent_obj)
{
  GtkWidget *layout;
  void *parent;

  parent = NSA_GET_PTR (env, parent_obj);

  gdk_threads_enter ();
  layout = gtk_layout_new (NULL, NULL);
  
  set_parent (layout, GTK_CONTAINER (parent));

  gtk_widget_realize (layout);
  connect_awt_hook (env, obj, layout, 1, GTK_LAYOUT (layout)->bin_window);
  set_visible (layout, 1);

  NSA_SET_PTR (env, obj, layout);
  gdk_threads_leave ();
}


