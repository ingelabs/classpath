/*
 * gtktextcomponentpeer.c -- Native implementation of GtkTextComponentPeer
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
#include "GtkTextComponentPeer.h"

JNIEXPORT jint JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableGetPosition
  (JNIEnv *env, jobject obj, jobject jedit)
{
  void *ptr;
  int pos;

  ptr = NSA_GET_PTR (env, jedit);

  gdk_threads_enter ();
  pos = gtk_editable_get_position (GTK_EDITABLE (ptr));
  gdk_threads_leave ();
  
  return pos;
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableSetPosition
  (JNIEnv *env, jobject obj, jobject jedit, jint pos)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, jedit);

  gdk_threads_enter ();
  gtk_editable_set_position (GTK_EDITABLE (ptr), pos);
  gdk_threads_leave ();
}

JNIEXPORT jint JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableGetSelectionStart
  (JNIEnv *env, jobject obj, jobject jedit)
{
  void *ptr;
  int pos;

  ptr = NSA_GET_PTR (env, jedit);

  gdk_threads_enter ();
  pos = GTK_EDITABLE (ptr)->selection_start_pos;
  gdk_threads_leave ();

  return pos;
}

JNIEXPORT jint JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableGetSelectionEnd
  (JNIEnv *env, jobject obj, jobject jedit)
{
  void *ptr;
  int pos;

  ptr = NSA_GET_PTR (env, jedit);

  gdk_threads_enter ();
  pos = GTK_EDITABLE (ptr)->selection_end_pos;
  gdk_threads_leave ();

  return pos;
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableSelectRegion
  (JNIEnv *env, jobject obj, jobject jedit, jint start, jint end)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, jedit);

  gdk_threads_enter ();
  gtk_editable_select_region (GTK_EDITABLE (ptr), start, end);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableSetEditable
  (JNIEnv *env, jobject obj, jobject jedit, jint state)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, jedit);

  gdk_threads_enter ();
  gtk_editable_set_editable (GTK_EDITABLE (ptr), state);
  gdk_threads_leave ();
}

