/*
 * gtktextareapeer.c -- Native implementation of GtkTextAreaPeer
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
#include "GtkTextAreaPeer.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextAreaPeer_gtkTextNew
  (JNIEnv *env, jobject obj, jobject jedit, jstring contents, jint hscroll, 
   jint vscroll)
{
  GtkWidget *text, *sw;
  char *str;
  int pos=0;

  str=(char *)(*env)->GetStringUTFChars (env, contents, 0);

  (*env)->MonitorEnter (env, java_mutex);

  text = gtk_text_new (NULL, NULL);
  gtk_text_set_editable (GTK_TEXT (text), TRUE);

  gtk_editable_insert_text (GTK_EDITABLE (text), str,
			    strlen (str), &pos);

  sw = gtk_scrolled_window_new (NULL, NULL);
  gtk_container_add (GTK_CONTAINER (sw), text);
  gtk_scrolled_window_set_policy (GTK_SCROLLED_WINDOW (sw), 
				  hscroll? GTK_POLICY_ALWAYS : 
				  GTK_POLICY_NEVER,
				  vscroll? GTK_POLICY_ALWAYS : 
				  GTK_POLICY_NEVER);

  gtk_widget_show (text);
  
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  NSA_SET_PTR (env, obj, sw);
  NSA_SET_PTR (env, jedit, text);

  (*env)->ReleaseStringUTFChars (env, contents, str);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextAreaPeer_gtkTextGetSize
  (JNIEnv *env, jobject obj, jobject jedit, jint rows, jint cols, 
   jintArray jdims)
{
  jint *dims;
  GtkWidget *text;
  GtkScrolledWindow *sw;
  GtkRequisition myreq;

  text = GTK_WIDGET (NSA_GET_PTR (env, jedit));
  sw = GTK_SCROLLED_WINDOW (NSA_GET_PTR (env, obj));

  dims = (*env)->GetIntArrayElements (env, jdims, 0);  
  dims[0]=dims[1]=0;

  (*env)->MonitorEnter (env, java_mutex);

  gtk_widget_size_request(GTK_WIDGET (GTK_SCROLLED_WINDOW(sw)->hscrollbar), 
				      &myreq);
  dims[0]=myreq.width+GTK_SCROLLED_WINDOW_CLASS 
    (GTK_OBJECT (sw)->klass)->scrollbar_spacing;

  gtk_widget_size_request(GTK_WIDGET (GTK_SCROLLED_WINDOW(sw)->vscrollbar), 
				      &myreq);
  dims[1]=myreq.width+GTK_SCROLLED_WINDOW_CLASS 
    (GTK_OBJECT (sw)->klass)->scrollbar_spacing;
  
  /* The '1' in the following assignments is from 
     #define TEXT_BORDER_ROOM         1
     in gtktext.c */

  dims[0] += ((cols * gdk_char_width (text->style->font, 'W'))
	     + (2 * (text->style->klass->xthickness + 1)));
  dims[1] += ((rows * gdk_char_height (text->style->font, 'W'))
	     + (2 * (text->style->klass->ythickness + 1)));

  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);
  
  (*env)->ReleaseIntArrayElements (env, jdims, dims, 0);
}

JNIEXPORT jstring JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextAreaPeer_gtkTextGetText
  (JNIEnv *env, jobject obj, jobject jedit)
{
  GtkEditable *text;
  char *contents;

  text = GTK_EDITABLE (NSA_GET_PTR (env, jedit));
  
  (*env)->MonitorEnter (env, java_mutex);
  
  contents = gtk_editable_get_chars (text, 0, 
				     gtk_text_get_length (GTK_TEXT(text)));

  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);

  return (*env)->NewStringUTF (env, contents);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextAreaPeer_gtkTextSetText
  (JNIEnv *env, jobject obj, jobject jedit, jstring contents)
{
  GtkEditable *text;
  char *str;
  int pos=0;

  text = GTK_EDITABLE (NSA_GET_PTR (env, jedit));
  str=(char *)(*env)->GetStringUTFChars (env, contents, 0);
  
  (*env)->MonitorEnter (env, java_mutex);
  
  gtk_text_freeze (GTK_TEXT (text));

  gtk_editable_delete_text (text, 0,
			    gtk_text_get_length (GTK_TEXT (text)));
  gtk_editable_insert_text (text, str,
			    strlen (str), &pos);
  gtk_text_thaw (GTK_TEXT (text));

  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);
  (*env)->ReleaseStringUTFChars (env, contents, str);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextAreaPeer_gtkTextInsert
  (JNIEnv *env, jobject obj, jobject jedit, jstring contents, jint position)
{
  GtkEditable *text;
  char *str;
  int pos=position;

  text = GTK_EDITABLE (NSA_GET_PTR (env, jedit));
  str=(char *)(*env)->GetStringUTFChars (env, contents, 0);
  
  (*env)->MonitorEnter (env, java_mutex);
  
  gtk_editable_insert_text (text, str,
			    strlen (str), &pos);

  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);
  (*env)->ReleaseStringUTFChars (env, contents, str);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkTextAreaPeer_gtkTextReplace
  (JNIEnv *env, jobject obj, jobject jedit, jstring contents, jint start, 
   jint end)
{
  GtkEditable *text;
  char *str;
  int pos=start;

  text = GTK_EDITABLE (NSA_GET_PTR (env, jedit));
  str=(char *)(*env)->GetStringUTFChars (env, contents, 0);
  
  (*env)->MonitorEnter (env, java_mutex);
  
  gtk_text_freeze (GTK_TEXT (text));
  gtk_editable_delete_text (text, start, end);
  gtk_editable_insert_text (text, str,
			    strlen (str), &pos);
  gtk_text_thaw (GTK_TEXT (text));

  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);
  (*env)->ReleaseStringUTFChars (env, contents, str);
}
