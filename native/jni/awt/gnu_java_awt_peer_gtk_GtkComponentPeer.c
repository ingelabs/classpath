/* gtkcomponentpeer.c -- Native implementation of GtkComponentPeer
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

This file is part of the peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


#include "gtkpeer.h"
#include "gnu_java_awt_peer_gtk_GtkComponentPeer.h"
#include <gtk/gtkprivate.h>

#define GTK_OBJECT_SETV(ptr, arg)                \
  gdk_threads_enter ();                          \
  {                                              \
    GtkArgInfo *info = NULL;                     \
    char *error;                                 \
                                                 \
    error = gtk_object_arg_get_info (GTK_OBJECT_TYPE (ptr), arg.name, &info); \
    if (error)                                   \
      {                                          \
	/* assume the argument is destined for the container's only child */ \
	ptr = gtk_container_children (GTK_CONTAINER (ptr))->data;            \
      }                                          \
    gtk_object_setv (GTK_OBJECT (ptr), 1, &arg); \
  }                                              \
  gdk_threads_leave ();                          \

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkWidgetSetCursor 
  (JNIEnv *env, jobject obj, jint type) 
{
  void *ptr;
  GtkWidget *widget;
  GdkCursorType gdk_cursor_type;
  GdkCursor *gdk_cursor;

  ptr = NSA_GET_PTR (env, obj);

  switch (type)
    {
    case AWT_CROSSHAIR_CURSOR:
      gdk_cursor_type = GDK_CROSSHAIR;
      break;
    case AWT_TEXT_CURSOR:
      gdk_cursor_type = GDK_XTERM;
      break;
    case AWT_WAIT_CURSOR:
      gdk_cursor_type = GDK_WATCH;
      break;
    case AWT_SW_RESIZE_CURSOR:
      gdk_cursor_type = GDK_BOTTOM_LEFT_CORNER;
      break;
    case AWT_SE_RESIZE_CURSOR:
      gdk_cursor_type = GDK_BOTTOM_RIGHT_CORNER;
      break;
    case AWT_NW_RESIZE_CURSOR:
      gdk_cursor_type = GDK_TOP_LEFT_CORNER;
      break;
    case AWT_NE_RESIZE_CURSOR:
      gdk_cursor_type = GDK_TOP_RIGHT_CORNER;
      break;
    case AWT_N_RESIZE_CURSOR:
      gdk_cursor_type = GDK_TOP_SIDE;
      break;
    case AWT_S_RESIZE_CURSOR:
      gdk_cursor_type = GDK_BOTTOM_SIDE;
      break;
    case AWT_W_RESIZE_CURSOR:
      gdk_cursor_type = GDK_LEFT_SIDE;
      break;
    case AWT_E_RESIZE_CURSOR:
      gdk_cursor_type = GDK_RIGHT_SIDE;
      break;
    case AWT_HAND_CURSOR:
      gdk_cursor_type = GDK_HAND2;
      break;
    case AWT_MOVE_CURSOR:
      gdk_cursor_type = GDK_FLEUR;
      break;
    default:
      gdk_cursor_type = GDK_LEFT_PTR;
    }
      
  gdk_threads_enter ();

  widget = GTK_WIDGET(ptr);

  gdk_cursor = gdk_cursor_new (gdk_cursor_type);
  gdk_window_set_cursor (widget->window, gdk_cursor);
  gdk_cursor_destroy (gdk_cursor);

  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkComponentPeer_dispose
  (JNIEnv *env, jobject obj)
{
  void *ptr;

  ptr = NSA_DEL_PTR (env, obj);

  gdk_threads_enter ();
  gtk_widget_destroy (GTK_WIDGET (ptr));
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkComponentPeer_requestFocus
  (JNIEnv *env, jobject obj)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  gtk_widget_grab_focus (GTK_WIDGET (ptr));
  gdk_threads_leave ();
}


/*
 * Show a widget (NO LONGER USED)
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_setVisible
  (JNIEnv *env, jobject obj, jboolean visible)
{
  GtkWidget *widget;
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  widget = GTK_WIDGET (ptr);

  if (visible)
    gtk_widget_show (widget);
  else
    gtk_widget_hide (widget);

  gdk_flush ();
  gdk_threads_leave ();
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

  gdk_threads_enter ();
  gdk_window_get_origin (GTK_WIDGET (ptr)->window, point, point+1);
  gdk_threads_leave ();

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
    GtkRequisition req;

    ptr = NSA_GET_PTR (env, obj);
    dims = (*env)->GetIntArrayElements (env, jdims, 0);  

    gdk_threads_enter ();
    gtk_signal_emit_by_name (GTK_OBJECT (ptr), "size_request", &req);
    
	
    dims[0] = req.width;
    dims[1] = req.height;
    
    gdk_threads_leave ();

    (*env)->ReleaseIntArrayElements(env, jdims, dims, 0);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkWidgetSetUsize (JNIEnv *env, 
    jobject obj, jint w, jint h)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
  gtk_widget_set_usize (GTK_WIDGET (ptr), w, h);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkFixedNew (JNIEnv *env, 
    jobject obj, jint width, jint height, jboolean visible)
{
  GtkWidget *layout;

  gdk_threads_enter ();
  layout = gtk_layout_new (NULL, NULL);
  gtk_widget_realize (layout);
  connect_awt_hook (env, obj, 1, GTK_LAYOUT (layout)->bin_window);
  set_visible (layout, visible);
  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, layout);
}

/*
 * Place a widget on the layout widget. 
 */
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkFixedPut 
    (JNIEnv *env, jobject obj, jobject container, jint x, jint y)
{
  GList *child;
  GtkWidget *fix;
  void *containerptr=NULL;
  void *objptr=NULL;

  /* We hawe a container which, if it is a window, will have
     this component added to its fixed.  If it is a fixed, we add the
     component to it. */
  
  containerptr=NSA_GET_PTR (env, container);
  objptr=NSA_GET_PTR (env, obj);
  
  gdk_threads_enter ();
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

      child=gtk_container_children (GTK_CONTAINER (GTK_BIN(containerptr)->child));
      
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
  gtk_widget_realize (GTK_WIDGET (objptr));
  gtk_widget_show (GTK_WIDGET (objptr));
  
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkFixedMove (JNIEnv *env, 
    jobject obj, jint x, jint y)
{
  GtkWidget *widget;
  void *ptr=NULL;

  /* For some reason, ScrolledWindow tries to scroll its contents
     by moving them using this function.  Since we want to use GTK's
     nice fast scrolling, we try to second guess it here.  This
     might cause problems later.  */
  
  printf ("GTKFIXED MOVE CALLED\n");

  if (x >= 0 && y >= 0) 
    {
      ptr = NSA_GET_PTR (env, obj);
      
      gdk_threads_enter ();
      widget=GTK_WIDGET (ptr);
      if (!GTK_IS_WINDOW (widget))
	  gtk_fixed_move (GTK_FIXED (widget->parent), widget, x, y);
      gdk_threads_leave ();
    }
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkComponentPeer_setNativeBounds
  (JNIEnv *env, jobject obj, jint x, jint y, jint width, jint height)
{
  GtkWidget *widget;
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  printf ("Cpeer.setBounds: %i, %i  %ix%i\n",
	  x, y, width, height);

  gdk_threads_enter ();

  widget = GTK_WIDGET (ptr);
  if (GTK_IS_VIEWPORT (widget->parent))
    {
      gtk_widget_set_usize (widget, width, height);
    }
  else
    {
      gtk_widget_set_usize (widget, width, height);
      gtk_layout_move (GTK_LAYOUT (widget->parent), widget, x, y);
    }

  gdk_threads_leave ();
}

JNIEXPORT jintArray JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkWidgetGetBackground
  (JNIEnv *env, jobject obj)
{
  void *ptr;
  jintArray array;
  int *rgb;
  GdkColor bg;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  bg = GTK_WIDGET (ptr)->style->bg[GTK_STATE_NORMAL];
  gdk_threads_leave ();

  array = (*env)->NewIntArray (env, 3);
  rgb = (*env)->GetIntArrayElements (env, array, NULL);
  /* convert color data from 16 bit values down to 8 bit values */
  rgb[0] = bg.red   >> 8;
  rgb[1] = bg.green >> 8;
  rgb[2] = bg.blue  >> 8;
  (*env)->ReleaseIntArrayElements (env, array, rgb, 0);

  return array;
}

JNIEXPORT jintArray JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_gtkWidgetGetForeground
  (JNIEnv *env, jobject obj)
{
  void *ptr;
  jintArray array;
  jint *rgb;
  GdkColor fg;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  fg = GTK_WIDGET (ptr)->style->fg[GTK_STATE_NORMAL];
  gdk_threads_leave ();

  array = (*env)->NewIntArray (env, 3);
  rgb = (*env)->GetIntArrayElements (env, array, NULL);
  /* convert color data from 16 bit values down to 8 bit values */
  rgb[0] = fg.red   >> 8;
  rgb[1] = fg.green >> 8;
  rgb[2] = fg.blue  >> 8;
  (*env)->ReleaseIntArrayElements (env, array, rgb, 0);

  return array;
}

void
set_visible (GtkWidget *widget, jboolean visible)
{
  if (visible)
    gtk_widget_show (widget);
  else
    gtk_widget_hide (widget);
}

GtkLayout *
find_gtk_layout (GtkWidget *parent)
{
  printf ("FINDGTKLAYTOU BEGIN\n");
  if (GTK_IS_WINDOW (parent))
    {
      GList *children = gtk_container_children 
	                  (GTK_CONTAINER (GTK_BIN (parent)->child));

  printf ("FINDGTKLAYTOU END\n");


      if (GTK_IS_MENU_BAR (children->data))
	return GTK_LAYOUT (children->next->data);
      else /* GTK_IS_LAYOUT (children->data) */
	return GTK_LAYOUT (children->data);
    }

  return NULL;
}

#define WIDGET_CLASS(w)  GTK_WIDGET_CLASS (GTK_OBJECT (w)->klass)

void
set_parent (GtkWidget *widget, GtkContainer *parent)
{
  printf ("SET PARENT START\n");

  if (GTK_IS_WINDOW (parent))
    {
      GList *children = gtk_container_children 
	                  (GTK_CONTAINER (GTK_BIN (parent)->child));

  printf ("SET PARENT ENDISH\n");

      if (GTK_IS_MENU_BAR (children->data))
	gtk_layout_put (GTK_LAYOUT (children->next->data), widget, 0, 0);
      else /* GTK_IS_LAYOUT (children->data) */
	gtk_layout_put (GTK_LAYOUT (children->data), widget, 0, 0);
    }
  else
    if (GTK_IS_SCROLLED_WINDOW (parent))
      {
/*  	if (WIDGET_CLASS (widget)->set_scroll_adjustments_signal) */
/*  	  gtk_container_add (GTK_CONTAINER (parent), widget); */
/*  	else */
/*  	  { */
	    gtk_scrolled_window_add_with_viewport 
	      (GTK_SCROLLED_WINDOW (parent), widget);
	    gtk_viewport_set_shadow_type (GTK_VIEWPORT (widget->parent), 
					  GTK_SHADOW_NONE);
/*  	  } */

      }
/*        gtk_layout_put  */
/*  	(GTK_LAYOUT (GTK_BIN (parent)->child), widget, 0, 0); */

/*      if (GTK_IS_SCROLLED_WINDOW (parent)) */
/*        gtk_layout_put  */
/*  	(GTK_LAYOUT (GTK_BIN (GTK_BIN (parent)->child)->child), widget, 0, 0); */
    else
      gtk_layout_put (GTK_LAYOUT (parent), widget, 0, 0);
}

JNIEXPORT jboolean JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_isEnabled 
  (JNIEnv *env, jobject obj)
{
  void *ptr;
  jboolean ret_val;
  
  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  ret_val = GTK_WIDGET_IS_SENSITIVE (GTK_WIDGET (ptr));
  gdk_threads_leave ();

  return ret_val;
}

JNIEXPORT jboolean JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_modalHasGrab
  (JNIEnv *env, jclass clazz)
{
  GtkWidget *widget;
  jboolean retval;

  gdk_threads_enter ();
  widget = gtk_grab_get_current ();
  retval = (widget && GTK_IS_WINDOW (widget) && GTK_WINDOW (widget)->modal);
  gdk_threads_leave ();

  return retval;
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_set__Ljava_lang_String_2Ljava_lang_String_2
  (JNIEnv *env, jobject obj, jstring jname, jstring jvalue)
{
  const char *name;
  const char *value;
  void *ptr;
  GtkArg arg;

  ptr = NSA_GET_PTR (env, obj);
  name = (*env)->GetStringUTFChars (env, jname, NULL);
  value = (*env)->GetStringUTFChars (env, jvalue, NULL);

  arg.type = GTK_TYPE_STRING;
  arg.name = (char *) name;
  GTK_VALUE_STRING (arg) = (char *) value;

  GTK_OBJECT_SETV (ptr, arg);  

  (*env)->ReleaseStringUTFChars (env, jname, name);
  (*env)->ReleaseStringUTFChars (env, jvalue, value);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkComponentPeer_set__Ljava_lang_String_2Z
  (JNIEnv *env, jobject obj, jstring jname, jboolean value)
{
  const char *name;
  void *ptr;
  GtkArg arg;

  ptr = NSA_GET_PTR (env, obj);
  name = (*env)->GetStringUTFChars (env, jname, NULL);

  arg.type = GTK_TYPE_BOOL;
  arg.name = (char *) name;
  GTK_VALUE_BOOL (arg) = value;

  GTK_OBJECT_SETV (ptr, arg);  

  (*env)->ReleaseStringUTFChars (env, jname, name);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkComponentPeer_set__Ljava_lang_String_2I
  (JNIEnv *env, jobject obj, jstring jname, jint value)
{
  const char *name;
  void *ptr;
  GtkArg arg;

  ptr = NSA_GET_PTR (env, obj);
  name = (*env)->GetStringUTFChars (env, jname, NULL);

  arg.type = GTK_TYPE_INT;
  arg.name = (char *) name;
  GTK_VALUE_INT (arg) = value;
  
  GTK_OBJECT_SETV (ptr, arg);  

  (*env)->ReleaseStringUTFChars (env, jname, name);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkComponentPeer_set__Ljava_lang_String_2F
  (JNIEnv *env, jobject obj, jstring jname, jfloat value)
{
  const char *name;
  void *ptr;
  GtkArg arg;

  ptr = NSA_GET_PTR (env, obj);
  name = (*env)->GetStringUTFChars (env, jname, NULL);

  arg.type = GTK_TYPE_FLOAT;
  arg.name = (char *) name;
  GTK_VALUE_FLOAT (arg) = value;
  
  GTK_OBJECT_SETV (ptr, arg);  

  (*env)->ReleaseStringUTFChars (env, jname, name);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkComponentPeer_set__Ljava_lang_String_2Ljava_lang_Object_2
  (JNIEnv *env, jobject obj1, jstring jname, jobject obj2)
{
  const char *name;
  void *ptr1, *ptr2;
  GtkArg arg;

  ptr1 = NSA_GET_PTR (env, obj1);
  ptr2 = NSA_GET_PTR (env, obj2);
  
  name = (*env)->GetStringUTFChars (env, jname, NULL);

  /* special case to catch where we need to set the parent */
  if (!strcmp (name, "parent"))
    {
      gdk_threads_enter ();
      printf ("CON CHECK\n");
      printf ("we have a %s\n", gtk_type_name (GTK_OBJECT_TYPE (ptr1)));
      if (GTK_IS_WINDOW (ptr1))
	printf ("we have a window!\n");
      if (!ptr2)
	printf ("ptr2 is null!\n");
      set_parent (GTK_WIDGET (ptr1), GTK_CONTAINER (ptr2));
      printf ("CON END\n");
      gdk_threads_leave ();

      (*env)->ReleaseStringUTFChars (env, jname, name);
      return;
    }

  arg.type = GTK_TYPE_OBJECT;
  arg.name = (char *) name;
  GTK_VALUE_OBJECT (arg) = GTK_OBJECT (ptr2);
  
  GTK_OBJECT_SETV (ptr1, arg);  

  (*env)->ReleaseStringUTFChars (env, jname, name);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkComponentPeer_create
  (JNIEnv *env, jobject obj, jstring jtypename)
{
  const char *typename;
  gpointer widget;

  typename = (*env)->GetStringUTFChars (env, jtypename, NULL);

  gdk_threads_enter ();
  gtk_button_get_type ();
  printf ("typename: %s\n", typename);
  printf ("%i\n", gtk_type_from_name (typename));
  widget = gtk_object_newv (gtk_type_from_name (typename),
			    0, NULL);
/*    widget = gtk_type_new (gtk_type_from_name (typename)); */
  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, jtypename, typename);
  NSA_SET_PTR (env, obj, widget);
}

JNIEXPORT void JNICALL Java_gnu_java_awt_peer_gtk_GtkComponentPeer_connectHooks
  (JNIEnv *env, jobject obj)
{
  void *ptr;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();
  gtk_widget_realize (GTK_WIDGET (ptr));
  connect_awt_hook (env, obj, 1, GTK_WIDGET (ptr)->window);
  gdk_threads_leave ();
}
