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
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_GtkWidgetShowChildren (JNIEnv *env,
    jobject obj)
{
  GtkWidget *widget;
  void *ptr;
  GList *child;

  printf("showing children\n");

  ptr=get_state (env,obj,window_table);
  
  if (ptr==NULL)
    {
      printf ("can't get state\n");
    }
  else
    {
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
}

/*
 * Show a widget
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_GtkWidgetShow (JNIEnv *env, 
    jobject obj)
{
  GtkWidget *widget;
  void *ptr;
  GList *child;

  ptr=get_state (env,obj,window_table);
  
  if (ptr==NULL)
    {
      printf ("can't get state\n");
    }
  else
    {
      (*env)->MonitorEnter (env,java_mutex);
      widget=GTK_WIDGET (ptr);

      /* Windows are a special case; they have a fixed widget
	 which needs to be shown as well. */
      if (GTK_IS_WINDOW(GTK_OBJECT(ptr)))
	{
	  child=gtk_container_children (GTK_CONTAINER(widget));
	  gtk_widget_show(GTK_WIDGET(child->data));
	  g_list_free(child);
	}
      
      gtk_widget_show (widget);
      gdk_threads_wake();
      (*env)->MonitorExit (env,java_mutex);
    }
}

/*
 * Find the preferred size of a widget.
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_GtkWidgetGetDimensions
    (JNIEnv *env, jobject obj, jintArray jdims)
{
    void *ptr=get_state (env,obj,window_table);
    jint *dims = (*env)->GetIntArrayElements (env, jdims, 0);  
    GtkRequisition myreq;

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
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_GtkWidgetSetUsize (JNIEnv *env, 
    jobject obj, jint w, jint h)
{
  GtkWidget *widget;
  void *ptr=NULL;

  ptr=get_state (env,obj,window_table);
  
  (*env)->MonitorEnter (env,java_mutex);
  widget=GTK_WIDGET(ptr);

  gtk_widget_set_usize(widget,w,h);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_GtkFixedNew (JNIEnv *env, 
    jobject obj, jint width, jint height)
{
  GtkWidget *fix;

  (*env)->MonitorEnter (env,java_mutex);
  fix=gtk_fixed_new();
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  if (window_table!=NULL)
    {
      if (set_state (env,obj,window_table,((void *)fix))<0)
	{
	  printf ("can't set state\n");
	}
    }
}

/*
 * Place a widget on the layout widget. 
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_GtkFixedPut 
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
  
  containerptr=get_state (env,container,window_table);
  objptr=get_state (env,obj,window_table);
  
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
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_GtkFixedMove (JNIEnv *env, 
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
      ptr=get_state (env,obj,window_table);
      
      (*env)->MonitorEnter (env,java_mutex);
      widget=GTK_WIDGET(ptr);
      if (!GTK_IS_WINDOW (widget))
	{
	  fix=widget->parent;
	  
	  gtk_fixed_move(GTK_FIXED(fix),widget,x,y);
	}
      gdk_threads_wake();
      (*env)->MonitorExit (env,java_mutex);
    }
}

