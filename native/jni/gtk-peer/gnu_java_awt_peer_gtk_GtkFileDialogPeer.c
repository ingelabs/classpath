/* gtkfiledialogpeer.c -- Native implementation of GtkFileDialogPeer
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


#include "gtkpeer.h"
#include "gnu_java_awt_peer_gtk_GtkFileDialogPeer.h"

/*
 * Make a new file selection dialog
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkFileDialogPeer_create 
  (JNIEnv *env, jobject obj)
{
  gpointer widget;

  gdk_threads_enter ();
  widget = gtk_type_new (gtk_file_selection_get_type ());
  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, widget);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkFileDialogPeer_old_create 
  (JNIEnv *env, jobject obj, jstring title)
{
  GtkWidget *window;
  const char *str;

  str = (*env)->GetStringUTFChars (env, title, NULL);
  
  gdk_threads_enter ();
  window = gtk_file_selection_new (str);

  NSA_SET_PTR (env, obj, window);
  gdk_threads_leave ();
  
  (*env)->ReleaseStringUTFChars (env, title, str);
}

/*
 * Set the filename in the file selection dialog.
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkFileDialogPeer_gtkFileSelectionSetFilename 
    (JNIEnv *env, jobject obj, jstring filename)
{
  void *ptr;
  const char *str;

  ptr = NSA_GET_PTR (env, obj);
    
  str = (*env)->GetStringUTFChars (env, filename, 0);      
  gdk_threads_enter ();
  gtk_file_selection_set_filename (GTK_FILE_SELECTION (ptr), str);
  gdk_threads_leave ();
  (*env)->ReleaseStringUTFChars (env, filename, str);
}
