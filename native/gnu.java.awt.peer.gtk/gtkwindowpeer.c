/*
 * gtkwindowpeer.c -- Native implementation of GtkWindowPeer
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
#include "GtkWindowPeer.h"

/*
 * Make a new window (any type)
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_GtkWindowNew (JNIEnv *env, jobject obj, jint type, jint width, jint height)
{
  GtkWidget *window, *fix;

  (*env)->MonitorEnter (env,java_mutex);
  switch(type)
    {
    case 1:
      window=gtk_window_new (GTK_WINDOW_TOPLEVEL);
      break;
    case 2:
      window=gtk_window_new (GTK_WINDOW_DIALOG);
      break;
    case 3:
      window=gtk_window_new (GTK_WINDOW_POPUP);
      break;
    }
  /* Every window needs a fixed widget to support absolute positioning. */

  fix=gtk_fixed_new();
  
  gtk_widget_set_usize(window,width,height);
  
  printf("c: requisition: %i x %i allocation:%i x %i\n",
	 GTK_WIDGET(window)->requisition.width,
	 GTK_WIDGET(window)->requisition.height,
	 GTK_WIDGET(window)->allocation.width,
	 GTK_WIDGET(window)->allocation.height);
  
  gtk_container_add(GTK_CONTAINER(window),fix);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  NSA_SET_PTR (env, obj, window);
}


/*
 * Set a frame's title
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_GtkWindowSetTitle (JNIEnv *env, 
  jobject obj, jstring title)
{
  void *ptr;
  char *str;

  ptr = NSA_GET_PTR (env, obj);
  
  str=(char *)(*env)->GetStringUTFChars (env, title, 0);      
  
  (*env)->MonitorEnter (env,java_mutex);
  gtk_window_set_title (GTK_WINDOW (ptr),str);
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
  
  (*env)->ReleaseStringUTFChars (env, title, str);
}

/*
 * Set a window's resizing policy
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_GtkWindowSetPolicy (JNIEnv *env, 
    jobject obj, jint shrink, jint grow, jint autos)
{
  void *ptr;
  
  ptr = NSA_GET_PTR (env, obj);
  
  (*env)->MonitorEnter (env,java_mutex);
  gtk_window_set_policy (GTK_WINDOW (ptr),shrink,grow,autos);
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}


/*
 * Lower the z-level of a window. 
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_GdkWindowLower (JNIEnv *env, 
    jobject obj)
{
  void *ptr;
  ptr = NSA_GET_PTR (env, obj);
    
  (*env)->MonitorEnter (env,java_mutex);
  gdk_window_lower (GTK_WIDGET (ptr)->window);
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

/*
 * Raise the z-level of a window.
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkWindowPeer_GdkWindowRaise (JNIEnv *env, 
    jobject obj)
{
  void *ptr;
  ptr = NSA_GET_PTR (env, obj);
    
  (*env)->MonitorEnter (env,java_mutex);
  gdk_window_raise (GTK_WIDGET (ptr)->window);
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}
