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
  const char *str;

  str = (*env)->GetStringUTFChars (env, text, NULL);

  gdk_threads_enter ();

  entry = gtk_entry_new ();
  connect_awt_hook (env, obj, entry, 2, 
		    &entry->window, &GTK_ENTRY (entry)->text_area);
  gtk_entry_set_text (GTK_ENTRY (entry), str);


  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, text, str);

  NSA_SET_PTR (env, obj, entry);
}


JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextFieldPeer_gtkEntryGetSize
  (JNIEnv *env, jobject obj, jint cols, jintArray jdims)
{
  void *ptr;
  jint *dims;
  GtkRequisition myreq;
  GtkEntry *entry;
  
  ptr = NSA_GET_PTR (env, obj);
  dims = (*env)->GetIntArrayElements (env, jdims, 0);  
  
  gdk_threads_enter ();
  entry = GTK_ENTRY (ptr);

  gtk_signal_emit_by_name (GTK_OBJECT (entry), "size_request", &myreq);  
  //  gtk_widget_size_request (GTK_WIDGET(entry), &myreq);
  
  dims[0]=myreq.width-150 + (cols * 
			     gdk_char_width (GTK_WIDGET (entry)->style->font,
					    'W'));
  dims[1]=myreq.height;
  
  printf("native: %i x %i\n", dims[0], dims[1]);

  gdk_threads_leave ();
  
  (*env)->ReleaseIntArrayElements (env, jdims, dims, 0);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextFieldPeer_gtkEntrySetEchoChar
  (JNIEnv *env, jobject obj, jchar c)
{
  void *ptr;
  GtkEntry *entry;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();

  entry = GTK_ENTRY (ptr);
    
  if (c!=0)
    {
/*        gtk_entry_set_echo_char (entry, c); */
      gtk_entry_set_visibility (entry, FALSE);
    }
  else
    gtk_entry_set_visibility (entry, TRUE);

  gdk_threads_leave ();
}

JNIEXPORT jstring JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextFieldPeer_gtkEntryGetText
  (JNIEnv *env, jobject obj)
{
  void *ptr;
  GtkEntry *entry;
  gchar *text;
  jstring jtext;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  
  entry = GTK_ENTRY (ptr);
  text = gtk_entry_get_text (entry);
  jtext = (*env)->NewStringUTF (env, text);

  gdk_threads_leave ();

  return jtext;
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextFieldPeer_gtkEntrySetText
  (JNIEnv *env, jobject obj, jstring text)
{
  void *ptr;
  GtkEntry *entry;
  const char *str;

  ptr = NSA_GET_PTR (env, obj);
  str = (*env)->GetStringUTFChars (env, text, NULL);
  
  gdk_threads_enter ();
  
  entry = GTK_ENTRY (ptr);
  gtk_entry_set_text (entry, str);
  
  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, text, str);
}
