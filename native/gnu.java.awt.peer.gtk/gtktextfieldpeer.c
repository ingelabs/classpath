/*
 * gtktextfieldpeer.c -- Native implementation of GtkTextFieldPeer
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
#include "GtkTextFieldPeer.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextFieldPeer_gtkEntryNew
  (JNIEnv *env, jobject obj, jstring text)
{
  GtkWidget *entry;
  char *str;

  str=(char *)(*env)->GetStringUTFChars (env, text, 0);      

  (*env)->MonitorEnter (env,java_mutex);

  entry = gtk_entry_new ();
  gtk_entry_set_text (GTK_ENTRY(entry), str);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  NSA_SET_PTR (env, obj, entry);

  (*env)->ReleaseStringUTFChars (env, text, str);
}


JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextFieldPeer_gtkEntryGetSize
  (JNIEnv *env, jobject obj, jint cols, jintArray jdims)
{
  jint *dims;
  GtkRequisition myreq;
  GtkEntry *entry;
  
  entry = GTK_ENTRY (NSA_GET_PTR (env, obj));
  dims = (*env)->GetIntArrayElements (env, jdims, 0);  
  
  (*env)->MonitorEnter (env, java_mutex);
  
  gtk_widget_size_request (GTK_WIDGET(entry), &myreq);
  
  dims[0]=myreq.width-150 + (cols * 
			     gdk_char_width (GTK_WIDGET (entry)->style->font,
					    'W'));
  dims[1]=myreq.height;
  
  printf("native: %i x %i\n", dims[0], dims[1]);

  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);
  
  (*env)->ReleaseIntArrayElements (env, jdims, dims, 0);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextFieldPeer_gtkEntrySetEchoChar
  (JNIEnv *env, jobject obj, jchar c)
{
  GtkEntry *entry;

  entry = GTK_ENTRY (NSA_GET_PTR (env, obj));
  
  (*env)->MonitorEnter (env, java_mutex);
  
  if (c!=0)
    {
/*        gtk_entry_set_echo_char (entry, c); */
      gtk_entry_set_visibility (entry, FALSE);
    }
  else
    gtk_entry_set_visibility (entry, TRUE);

  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);
}

JNIEXPORT jstring JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextFieldPeer_gtkEntryGetText
  (JNIEnv *env, jobject obj)
{
  GtkEntry *entry;
  gchar *text;

  entry = GTK_ENTRY (NSA_GET_PTR (env, obj));
  
  (*env)->MonitorEnter (env, java_mutex);
  
  text = gtk_entry_get_text (entry);
  
  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);

  return (*env)->NewStringUTF (env, text);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextFieldPeer_gtkEntrySetText
  (JNIEnv *env, jobject obj, jstring text)
{
  GtkEntry *entry;
  char *str;

  entry = GTK_ENTRY (NSA_GET_PTR (env, obj));
  str=(char *)(*env)->GetStringUTFChars (env, text, 0);      

  (*env)->MonitorEnter (env, java_mutex);
  
  gtk_entry_set_text (entry, str);
  
  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);

  (*env)->ReleaseStringUTFChars (env, text, str);
}
