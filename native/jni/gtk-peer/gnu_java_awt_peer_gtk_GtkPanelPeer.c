/* gtkpanelpeer.c -- Native implementation of GtkPanelPeer
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
#include "gnu_java_awt_peer_gtk_GtkPanelPeer.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkPanelPeer_create
  (JNIEnv *env, jobject obj)
{
  gpointer widget;

  gdk_threads_enter ();
  widget = gtk_layout_new (NULL, NULL);
  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, widget);
}

typedef struct _GtkLayoutChild   GtkLayoutChild;

struct _GtkLayoutChild {
  GtkWidget *widget;
  gint x;
  gint y;
};

void sr (GtkWidget *widget, GtkRequisition *requisition,
	 gpointer user_data)
{
  GtkLayout *layout;
  GtkLayoutChild *child;
  GList *children;

  layout = GTK_LAYOUT (widget);
  requisition->width = GTK_WIDGET (widget)->allocation.width;
  requisition->height = GTK_WIDGET (widget)->allocation.height;

  if (user_data)
    {
      printf ("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11\n");
      printf ("%i, %i\n",   requisition->width,   requisition->height);
    }
  return;

  children = layout->children;
  while (children)
    {
      child = children->data;
      children = children->next;

      if (GTK_WIDGET_VISIBLE (child->widget))
	{
          requisition->height = MAX (requisition->height,
                                     child->y +
                                     child->widget->allocation.height);
          requisition->width = MAX (requisition->width,
                                    child->x +
                                    child->widget->allocation.width);
	}
    }

  requisition->height += GTK_CONTAINER (layout)->border_width * 2;
  requisition->width += GTK_CONTAINER (layout)->border_width * 2;

  printf ("width, height %i, %i\n", requisition->width, requisition->height);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkPanelPeer_connectHooks
  (JNIEnv *env, jobject obj)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gtk_widget_realize (GTK_WIDGET (ptr));
  connect_awt_hook (env, obj, 1, GTK_LAYOUT (ptr)->bin_window);

/*    gtk_signal_connect (GTK_OBJECT (ptr), "size_request", GTK_SIGNAL_FUNC (sr), */
/*  		      NULL); */
  gdk_threads_leave ();
}

/*
 * Make a new panel.
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkPanelPeer_gtkPanelNew
    (JNIEnv *env, jobject obj, jobject parent_obj)
{
  GtkWidget *layout;
  void *parent;

  parent = NSA_GET_PTR (env, parent_obj);

  gdk_threads_enter ();
  layout = gtk_layout_new (NULL, NULL);
  
  set_parent (layout, GTK_CONTAINER (parent));

  gtk_widget_realize (layout);
  connect_awt_hook (env, obj, 1, GTK_LAYOUT (layout)->bin_window);
  set_visible (layout, 1);

  NSA_SET_PTR (env, obj, layout);
  gdk_threads_leave ();
}


