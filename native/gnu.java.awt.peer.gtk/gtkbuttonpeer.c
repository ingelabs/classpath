/*
 * gtkbuttonpeer.c -- Native implementation of GtkButtonPeer
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
#include "GtkButtonPeer.h"

/*
 * Make a new button.
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkButtonPeer_gtkButtonNewWithLabel
    (JNIEnv *env, jobject obj, jstring label)
{
  GtkWidget *button;
  char *str;

  str=(char *)(*env)->GetStringUTFChars (env, label, 0);      

  /* All buttons get a label, even if it is blank.  This is okay for java. */
  (*env)->MonitorEnter (env,java_mutex);
  button=gtk_button_new_with_label (str);
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  NSA_SET_PTR (env, obj, button);

  (*env)->ReleaseStringUTFChars (env, label, str);
}

/*
 * Set the label of a button
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkButtonPeer_gtkButtonLabelSet
    (JNIEnv *env, jobject obj, jstring label)
{
  void *ptr;
  char *str;
  GList *child;

  ptr=NSA_GET_PTR (env, obj);
  
  printf("labelset\n");

  str=(char *)(*env)->GetStringUTFChars (env, label, 0);      
  (*env)->MonitorEnter (env,java_mutex);
  
  /* We assume that the button has 1 child, a label. */
  /* We'd better not be wrong. */
  
  child=gtk_container_children (GTK_CONTAINER(ptr));
  if (!child)
    printf("No children in button!\n");
  if(!GTK_IS_LABEL(child->data))
    printf("Child is not label!\n");
  
  gtk_label_set (GTK_LABEL(child->data),str);
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
  (*env)->ReleaseStringUTFChars (env, label, str);
  g_list_free(child);
}

