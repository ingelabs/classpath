/*
 * gtkcomponentpeer.c -- Native implementation of GtkComponentPeer
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
#include "GtkComponentPeer.h"

/*
 * Show all of a widget's children (possibly recursively)
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkWidgetShowChildren (JNIEnv *env,
    jobject obj)
{
  GtkWidget *widget;
  void *ptr;
  GList *child;

  printf("showing children\n");

  ptr=NSA_GET_PTR (env, obj);
  
  (*env)->MonitorEnter (env,java_mutex);
  widget=GTK_WIDGET (ptr);
  
  /* Windows are the real reason we are here... to show the fixed and
     everything in it. */
  if (GTK_IS_WINDOW(GTK_OBJECT(ptr)))
    {
      child=gtk_container_children (GTK_CONTAINER(widget));
      gtk_widget_show_all(GTK_WIDGET(child->data));
      g_list_free(child);
    }
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

/*
 * Show a widget
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_setVisible (JNIEnv *env, 
    jobject obj, jboolean visible)
{
  GtkWidget *widget;
  void *ptr;
  GList *child;
  GtkArg arg;

  ptr = NSA_GET_PTR (env, obj);

  arg.name = "GtkWidget::visible";
  arg.type = GTK_TYPE_BOOL;
  GTK_VALUE_BOOL (arg) = visible;

  (*env)->MonitorEnter (env,java_mutex);
  widget = GTK_WIDGET (ptr);
  
  /* Windows are a special case; they have a fixed widget
     which needs to be shown/hidden as well. */
  if (GTK_IS_WINDOW (GTK_OBJECT (ptr)))
    {
      child = gtk_container_children (GTK_CONTAINER (widget));
      gtk_widget_setv (GTK_WIDGET (child->data), 1, &arg);
      g_list_free (child);
    }
  
  gtk_widget_setv (GTK_WIDGET (widget), 1, &arg);
  gdk_threads_wake ();
  (*env)->MonitorExit (env,java_mutex);
}

/*
 * Redraw a widget
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_repaint
  (JNIEnv *env, jobject obj, jlong tm, jint x, jint y, jint width, jint height)
{
  void *ptr;
  GtkWidget *widget;

  ptr = NSA_GET_PTR (env, obj);

  (*env)->MonitorEnter (env, java_mutex);
  widget = GTK_WIDGET (ptr);

  gtk_widget_queue_draw_area (widget, x, y, width, height);

  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);
}

/*
 * Find the origin of a widget's window.
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkWidgetGetLocationOnScreen
  (JNIEnv * env, jobject obj, jintArray jpoint)
{
  void *ptr;
  jint *point;

  ptr = NSA_GET_PTR (env, obj);
  point = (*env)->GetIntArrayElements (env, jpoint, 0);

  (*env)->MonitorEnter (env, java_mutex);
  gdk_window_get_origin (GTK_WIDGET(ptr)->window, point, point+1);
  gdk_threads_wake();
  (*env)->MonitorExit (env, java_mutex);

  (*env)->ReleaseIntArrayElements(env, jpoint, point, 0);
}

/*
 * Find the preferred size of a widget.
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkWidgetGetDimensions
    (JNIEnv *env, jobject obj, jintArray jdims)
{
    void *ptr;
    jint *dims;
    GtkRequisition myreq;

    ptr = NSA_GET_PTR (env,obj);
    dims = (*env)->GetIntArrayElements (env, jdims, 0);  

    (*env)->MonitorEnter (env,java_mutex);

    dims[0]=GTK_WIDGET(ptr)->allocation.width;
    dims[1]=GTK_WIDGET(ptr)->allocation.height;

    gtk_widget_size_request(GTK_WIDGET(ptr),&myreq);

    if (dims[0]<=1 && dims[1]<=1)
      {
	dims[0]=myreq.width;
	dims[1]=myreq.height;
      }

    gdk_threads_wake();
    (*env)->MonitorExit (env,java_mutex);

    (*env)->ReleaseIntArrayElements(env, jdims, dims, 0);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkWidgetSetUsize (JNIEnv *env, 
    jobject obj, jint w, jint h)
{
  GtkWidget *widget;
  void *ptr=NULL;

  ptr = NSA_GET_PTR (env, obj);
  
  (*env)->MonitorEnter (env,java_mutex);
  widget=GTK_WIDGET(ptr);

  gtk_widget_set_usize(widget,w,h);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkFixedNew (JNIEnv *env, 
    jobject obj, jint width, jint height)
{
  GtkWidget *fix;

  (*env)->MonitorEnter (env,java_mutex);
  fix=gtk_fixed_new();
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  NSA_SET_PTR (env, obj, fix);
}

/*
 * Place a widget on the layout widget. 
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkFixedPut 
    (JNIEnv *env, jobject obj, jobject container, jint x, jint y)
{
  GList *child;
  GtkWidget *fix, *hbox;
  void *containerptr=NULL;
  void *objptr=NULL;

  printf("fixedput\n");
  
  /* We hawe a container which, if it is a window, will have
     this component added to its fixed.  If it is a fixed, we add the
     component to it. */
  
  containerptr=NSA_GET_PTR (env, container);
  objptr=NSA_GET_PTR (env, obj);
  
  (*env)->MonitorEnter (env,java_mutex);
  if (GTK_IS_WINDOW(GTK_OBJECT(containerptr)))
    {
      printf("fixedput: container is window\n");

      child=gtk_container_children (GTK_CONTAINER(containerptr));
      
      if (!child)
	printf("No children in window!\n");
      while (child && !GTK_IS_FIXED(child->data))
	{
	  printf("Child\n");
	  child=g_list_next(child);
	}
      if(!child)
	printf("Child is not fixed!\n");
      
      fix=GTK_WIDGET(child->data);
      g_list_free(child);
    }
  else
    if (GTK_IS_SCROLLED_WINDOW(GTK_OBJECT(containerptr)))
    {
      printf("fixedput: container is scrolled window\n");

      child=gtk_container_children (GTK_CONTAINER (GTK_SCROLLED_WINDOW(containerptr)->child));
      
      if (!child)
	printf("No children in window!\n");
      while (child && !GTK_IS_FIXED(child->data))
	{
	  printf("Child\n");
	  child=g_list_next(child);
	}
      if(!child)
	printf("Child is not fixed!\n");
      
      fix=GTK_WIDGET(child->data);

      g_list_free(child);
    }
  else
    {
      printf("fixedput: container is fixed\n");
      fix=GTK_WIDGET(containerptr);
    }
  
  printf("fixedput: fixed found: %p\n",fix);

  gtk_fixed_put(GTK_FIXED(fix),GTK_WIDGET(objptr),x,y);
  
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkFixedMove (JNIEnv *env, 
    jobject obj, jint x, jint y)
{
  GtkWidget *fix, *widget;
  void *ptr=NULL;

  /* For some reason, ScrolledWindow tries to scroll its contents
     by moving them using this function.  Since we want to use GTK's
     nice fast scrolling, we try to second guess it here.  This
     might cause problems later.  */
  
  if(x>=0 && y>=0) 
    {
      ptr=NSA_GET_PTR (env, obj);
      
      (*env)->MonitorEnter (env, java_mutex);
      widget=GTK_WIDGET (ptr);
      if (!GTK_IS_WINDOW (widget))
	{
	  fix=widget->parent;
	  
	  gtk_fixed_move (GTK_FIXED(fix), widget, x, y);
	}
      gdk_threads_wake ();
      (*env)->MonitorExit (env, java_mutex);
    }
}

