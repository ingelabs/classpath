/*
 * gtkfiledialogpeer.c -- Native implementation of GtkFileDialogPeer
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
#include "GtkFileDialogPeer.h"

/*
 * Make a new file selection dialog
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkFileDialogPeer_gtkFileSelectionNew (JNIEnv *env, 
    jobject obj)
{
  GtkWidget *window;
  
  (*env)->MonitorEnter (env,java_mutex);
  window=GTK_WIDGET (gtk_file_selection_new (NULL));
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
  NSA_SET_PTR (env, obj, window);
}

/*
 * Set the filename in the file selection dialog.
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkFileDialogPeer_gtkFileSelectionSetFilename 
    (JNIEnv *env, jobject obj, jstring filename)
{
  void *ptr;
  char *str;

  ptr=NSA_GET_PTR (env, obj);
    
  str=(char *)(*env)->GetStringUTFChars (env, filename, 0);      
  (*env)->MonitorEnter (env,java_mutex);
  gtk_file_selection_set_filename (GTK_FILE_SELECTION (ptr), str);
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
  (*env)->ReleaseStringUTFChars (env, filename, str);
}
