/*
 * gtklabelpeer.c -- Native implementation of GtkLabelPeer
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
#include "gnu_java_awt_peer_gtk_GtkLabelPeer.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkLabelPeer_create
  (JNIEnv *env, jobject obj)
{
  GtkWidget *label;
  GtkContainer *ebox;

  gdk_threads_enter ();
  ebox = GTK_CONTAINER (gtk_type_new (gtk_event_box_get_type ()));
  label = GTK_WIDGET (gtk_type_new (gtk_label_get_type ()));
  gtk_container_add (ebox, label);
  gtk_widget_show (label);
  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, ebox);
}
