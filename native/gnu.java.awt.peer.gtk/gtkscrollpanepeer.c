/*
 * gtkscrollpanepeer.c -- Native implementation of GtkScrollPanePeer
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
#include "GtkScrollPanePeer.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollPanePeer_gtkScrolledWindowNew 
    (JNIEnv *env, jobject obj, jint policy, jint width, jint height,
     jintArray jdims)
{
  jint *dims = (*env)->GetIntArrayElements (env, jdims, 0);  
  GtkRequisition myreq;
  GtkWidget *sw, *fix;
  guint mypolicy;

  switch (policy)
    {
    case 1:
      mypolicy=GTK_POLICY_ALWAYS; 
      break;
    case 2: 
      mypolicy=GTK_POLICY_AUTOMATIC; 
      break;
    case 3: 
      mypolicy=GTK_POLICY_NEVER; 
      break;
    }

  (*env)->MonitorEnter (env,java_mutex);

  sw=gtk_scrolled_window_new(NULL,NULL);

  gtk_widget_set_usize (sw, width, height);
  gtk_scrolled_window_set_policy (GTK_SCROLLED_WINDOW (sw),
                                  mypolicy,
                                  mypolicy);

  gtk_container_border_width (GTK_CONTAINER (sw), 0);

  /* Uh-oh.  Magic numbers.  I think this discrepancy comes from 
   The grey lines that border the viewport. */

  gtk_widget_size_request(GTK_SCROLLED_WINDOW(sw)->vscrollbar,&myreq);
  dims[0]=myreq.width+GTK_SCROLLED_WINDOW_CLASS (GTK_OBJECT (sw)->klass)->scrollbar_spacing+4;

  gtk_widget_size_request(GTK_SCROLLED_WINDOW(sw)->hscrollbar,&myreq);
  dims[1]=myreq.height+GTK_SCROLLED_WINDOW_CLASS (GTK_OBJECT (sw)->klass)->scrollbar_spacing+4;

  printf("sbsize: %i %i\n",dims[0],dims[1]);

  fix=gtk_fixed_new();
  gtk_scrolled_window_add_with_viewport (GTK_SCROLLED_WINDOW (sw), fix);
  
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  NSA_SET_PTR (env, obj, sw);
  
  (*env)->ReleaseIntArrayElements(env, jdims, dims, 0);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollPanePeer_gtkScrolledWindowSetScrollPosition
  (JNIEnv *env, jobject obj, jint x, jint y)
{
  GtkAdjustment *hadj, *vadj;
  GtkScrolledWindow *sw;
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  (*env)->MonitorEnter (env,java_mutex);
  sw=GTK_SCROLLED_WINDOW(ptr);

  hadj=gtk_scrolled_window_get_hadjustment(sw);
  vadj=gtk_scrolled_window_get_vadjustment(sw);

  gtk_adjustment_set_value (hadj, (float)x);
  gtk_adjustment_set_value (vadj, (float)y);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollPanePeer_gtkScrolledWindowSetHScrollIncrement
  (JNIEnv *env, jobject obj, jint u)
{
  GtkAdjustment *hadj;
  GtkScrolledWindow *sw;
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  (*env)->MonitorEnter (env,java_mutex);
  sw=GTK_SCROLLED_WINDOW(ptr);

  hadj=gtk_scrolled_window_get_hadjustment(sw);
  hadj->step_increment=u;

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollPanePeer_gtkScrolledWindowSetVScrollIncrement
  (JNIEnv *env, jobject obj, jint u)
{
  GtkAdjustment *vadj;
  GtkScrolledWindow *sw;
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  (*env)->MonitorEnter (env,java_mutex);
  sw=GTK_SCROLLED_WINDOW(ptr);

  vadj=gtk_scrolled_window_get_hadjustment(sw);
  vadj->step_increment=u;

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkScrollPanePeer_gtkScrolledWindowSetSize
  (JNIEnv *env, jobject obj, jint w, jint h)
{
  GtkScrolledWindow *sw;
  GtkWidget *fix;
  GList *child;
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  (*env)->MonitorEnter (env,java_mutex);
  sw=GTK_SCROLLED_WINDOW(ptr);

  child=gtk_container_children (GTK_CONTAINER 
				(GTK_BIN(sw)->child));
      
  while (child && !GTK_IS_FIXED(child->data))
    child=g_list_next(child);
  
  fix=GTK_WIDGET(child->data);
  
  g_list_free(child);

  gtk_widget_set_usize(fix,w,h);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

