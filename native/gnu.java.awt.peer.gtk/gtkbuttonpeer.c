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
    (JNIEnv *env, jobject obj, jobject parent_obj, 
     jstring label, jboolean visible)
{
  GtkWidget *button;
  void *parent;
  const char *str;

  parent = NSA_GET_PTR (env, parent_obj);

  str = (*env)->GetStringUTFChars (env, label, NULL);

  /* All buttons get a label, even if it is blank.  This is okay for java. */
  gdk_threads_enter ();
  button = gtk_button_new_with_label (str);
  
  set_parent (button, GTK_CONTAINER (parent));

  gtk_widget_realize (button);
  connect_awt_hook (env, obj, button, 1, button->window);
  set_visible (button, visible);

  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, label, str);

  NSA_SET_PTR (env, obj, button);
}

/*
 * Set the label of a button
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkButtonPeer_gtkButtonLabelSet
    (JNIEnv *env, jobject obj, jstring label)
{
  void *ptr;
  const char *str;
  GList *child;

  ptr=NSA_GET_PTR (env, obj);
  
  printf("labelset\n");

  str = (*env)->GetStringUTFChars (env, label, NULL);
  gdk_threads_enter ();
  
  /* We assume that the button has 1 child, a label. */
  /* We'd better not be wrong. */
  
  child=gtk_container_children (GTK_CONTAINER(ptr));
  if (!child)
    printf("No children in button!\n");
  if (!GTK_IS_LABEL(child->data))
    printf("Child is not label!\n");
  
  gtk_label_set (GTK_LABEL(child->data),str);

  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, label, str);
  g_list_free (child);
}

