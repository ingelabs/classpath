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
  GtkEditable *edit;
  int pos;

  edit = GTK_EDITABLE (NSA_GET_PTR (env, jedit));

  (*env)->MonitorEnter (env,java_mutex);

  pos = gtk_editable_get_position (edit);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
  
  return pos;
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableSetPosition
  (JNIEnv *env, jobject obj, jobject jedit, jint pos)
{
  GtkEditable *edit;

  edit = GTK_EDITABLE (NSA_GET_PTR (env, jedit));

  (*env)->MonitorEnter (env,java_mutex);

  gtk_editable_set_position (edit, pos);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

JNIEXPORT jint JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableGetSelectionStart
  (JNIEnv *env, jobject obj, jobject jedit)
{
  GtkEditable *edit;
  int pos;

  edit = GTK_EDITABLE (NSA_GET_PTR (env, jedit));

  (*env)->MonitorEnter (env,java_mutex);

  pos = edit->selection_start_pos;

  (*env)->MonitorExit (env,java_mutex);

  return pos;
}

JNIEXPORT jint JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableGetSelectionEnd
  (JNIEnv *env, jobject obj, jobject jedit)
{
  GtkEditable *edit;
  int pos;

  edit = GTK_EDITABLE (NSA_GET_PTR (env, jedit));

  (*env)->MonitorEnter (env,java_mutex);

  pos = edit->selection_end_pos;

  (*env)->MonitorExit (env,java_mutex);

  return pos;
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableSelectRegion
  (JNIEnv *env, jobject obj, jobject jedit, jint start, jint end)
{
  GtkEditable *edit;

  edit = GTK_EDITABLE (NSA_GET_PTR (env, jedit));

  (*env)->MonitorEnter (env,java_mutex);

  gtk_editable_select_region (edit, start, end);

  (*env)->MonitorExit (env,java_mutex);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_gtkEditableSetEditable
  (JNIEnv *env, jobject obj, jobject jedit, jint state)
{
  GtkEditable *edit;

  edit = GTK_EDITABLE (NSA_GET_PTR (env, jedit));

  (*env)->MonitorEnter (env,java_mutex);

  gtk_editable_set_editable (edit, state);

  (*env)->MonitorExit (env,java_mutex);
}

