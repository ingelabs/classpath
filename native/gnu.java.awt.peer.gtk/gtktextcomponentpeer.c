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
#include "gnu_java_awt_peer_gtk_GtkTextComponentPeer.h"

#define GET_EDITABLE(obj) (GTK_IS_EDITABLE (obj) ? GTK_EDITABLE (obj) : \
  GTK_EDITABLE (GTK_SCROLLED_WINDOW (obj)->container.child))

JNIEXPORT jint JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_getCaretPosition
  (JNIEnv *env, jobject obj)
{
  void *ptr;
  int pos;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  pos = gtk_editable_get_position (GET_EDITABLE (ptr));
  gdk_threads_leave ();
  
  return pos;
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_setCaretPosition
  (JNIEnv *env, jobject obj, jint pos)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gtk_editable_set_position (GET_EDITABLE (ptr), pos);
  gdk_threads_leave ();
}

JNIEXPORT jint JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_getSelectionStart
  (JNIEnv *env, jobject obj)
{
  void *ptr;
  int pos;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  pos = GET_EDITABLE (ptr)->selection_start_pos;
  gdk_threads_leave ();

  return pos;
}

JNIEXPORT jint JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_getSelectionEnd
  (JNIEnv *env, jobject obj)
{
  void *ptr;
  int pos;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  pos = GET_EDITABLE (ptr)->selection_end_pos;
  gdk_threads_leave ();

  return pos;
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_select
  (JNIEnv *env, jobject obj, jint start, jint end)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gtk_editable_select_region (GET_EDITABLE (ptr), start, end);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_setEditable
  (JNIEnv *env, jobject obj, jboolean state)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gtk_editable_set_editable (GET_EDITABLE (ptr), state);
  gdk_threads_leave ();
}

JNIEXPORT jstring JNICALL
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_getText
  (JNIEnv *env, jobject obj)
{
  void *ptr;
  char *contents;
  jstring jcontents;

  ptr = NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  contents = gtk_editable_get_chars (GET_EDITABLE (ptr), 0, -1);
  gdk_threads_leave ();

  jcontents = (*env)->NewStringUTF (env, contents);
  g_free (contents);

  return jcontents;
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextComponentPeer_setText
  (JNIEnv *env, jobject obj, jstring contents)
{
  void *ptr;
  GtkEditable *text;
  const char *str;
  int pos = 0;

  ptr = NSA_GET_PTR (env, obj);
  str = (*env)->GetStringUTFChars (env, contents, NULL);
  
  gdk_threads_enter ();

  text = GET_EDITABLE (ptr);

  if (GTK_IS_TEXT (text))
    gtk_text_freeze (GTK_TEXT (text));

  gtk_editable_delete_text (text, 0, -1);
  gtk_editable_insert_text (text, str, strlen (str), &pos);

  if (GTK_IS_TEXT (text))
    gtk_text_thaw (GTK_TEXT (text));

  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, contents, str);
}
