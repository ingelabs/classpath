/*
 * gtkcontainerpeer.c -- Native implementation of GtkContainerPeer
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
#include "gnu_java_awt_peer_gtk_GtkContainerPeer.h"

/*
 * Make sure that all the children of this container are the proper size.
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkContainerPeer_gtkContainerCheckResize
  (JNIEnv *env, jobject obj)
{
  void *ptr;

  return;
  ptr = NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  gtk_layout_thaw (GTK_LAYOUT (ptr));
/*    gtk_container_check_resize (GTK_CONTAINER (ptr)); */
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkContainerPeer_gtkContainerStartResize
  (JNIEnv *env, jobject obj)
{
  void *ptr;

  return;
  ptr = NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  gtk_layout_freeze (GTK_LAYOUT (ptr));
  gdk_threads_leave ();
}
