/*
 * gtkevents.c -- GDK/GTK event handlers
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by Paul Fisher <rao@gnu.org>
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
#include <stdarg.h>

/* A widget can be composed of multipled windows, so we need to hook
   events on all of them. */
struct event_hook_info
{
  jobject *peer_obj;
  int nwindows;
  GdkWindow ***windows;		/* array of pointers to (GdkWindow *) */
};

static int
button_to_awt_mods (int button)
{
  switch (button)
    {
    case 1:
      return AWT_BUTTON1_MASK;
    case 2:
      return AWT_BUTTON2_MASK;
    case 3:
      return AWT_BUTTON3_MASK;
    }

  return 0;
}

static int
state_to_awt_mods (int mods)
{
  int result = 0;

  if (mods & (GDK_SHIFT_MASK | GDK_LOCK_MASK))
    result |= AWT_SHIFT_MASK;
  if (mods & GDK_CONTROL_MASK)
    result |= AWT_CTRL_MASK;
  
  return result;
}

void
awt_event_handler (GdkEvent *event)
{
  jobject *obj_ptr;
  static guint32 button_click_time = 0;
  static GdkWindow *button_window = NULL;
  static guint button_number = -1;
  static jint click_count = 1;
  static int grab_counter = 0;

  /* keep synthetic AWT events from being processed recursively */
  if (event->type & SYNTHETIC_EVENT_MASK && event->type != GDK_NOTHING)
    {
      event->type ^= SYNTHETIC_EVENT_MASK;
      gtk_main_do_event (event);
      return;
    }

  /* keep track of clickCount ourselves, since the AWT allows more
     than a triple click to occur */
  if (event->type == GDK_BUTTON_PRESS)
    {
      if ((event->button.time < (button_click_time + MULTI_CLICK_TIME))
	  && (event->button.window == button_window)
	  && (event->button.button == button_number))
	click_count++;
      else
	click_count = 1;
      
      button_click_time = event->button.time;
      button_window = event->button.window;
      button_number = event->button.button;
    }

  /* for all input events, which have a window with a jobject attached,
     send the input event off to Java before GTK has a chance to process
     the event */
  if ((event->type == GDK_BUTTON_PRESS
       || event->type == GDK_BUTTON_RELEASE
       || event->type == GDK_ENTER_NOTIFY
       || event->type == GDK_LEAVE_NOTIFY
       || event->type == GDK_CONFIGURE
       || event->type == GDK_EXPOSE)
      && gdk_property_get (event->any.window,
			   gdk_atom_intern ("_GNU_GTKAWT_ADDR", FALSE),
			   gdk_atom_intern ("CARDINAL", FALSE),
			   0,
			   sizeof (jobject),
			   FALSE,
			   NULL,
			   NULL,
			   NULL,
			   (guchar **)&obj_ptr))
    {
      switch (event->type)
	{
	case GDK_BUTTON_PRESS:
	  (*gdk_env)->CallVoidMethod (gdk_env, *obj_ptr, postMouseEventID,
				      AWT_MOUSE_PRESSED, 
				      (jlong)event->button.time,
				    state_to_awt_mods (event->button.state) |
				    button_to_awt_mods (event->button.button), 
				      (jint)event->button.x,
				      (jint)event->button.y, 
				      click_count, JNI_FALSE);

	  /*	  grab_counter++;
	  gdk_pointer_grab (event->any.window,
			    FALSE,
			    GDK_POINTER_MOTION_MASK |
			    GDK_BUTTON_MOTION_MASK |
			    GDK_BUTTON_PRESS_MASK |
			    GDK_BUTTON_RELEASE_MASK |
			    GDK_ENTER_NOTIFY_MASK |
			    GDK_LEAVE_NOTIFY_MASK,
			    NULL,
			    NULL,
			    event->button.time);*/
	  break;
	case GDK_BUTTON_RELEASE:
	  {
	    int width, height;

	    /* only ungrab if no other buttons are pressed down */
	    /*	    if (--grab_counter == 0)
	      gdk_pointer_ungrab (event->button.time);
	    */
	    (*gdk_env)->CallVoidMethod (gdk_env, *obj_ptr, postMouseEventID,
					AWT_MOUSE_RELEASED, 
					(jlong)event->button.time,
				    state_to_awt_mods (event->button.state) |
				    button_to_awt_mods (event->button.button), 
					(jint)event->button.x,
					(jint)event->button.y, 
					click_count, JNI_FALSE);

	    /* check to see if the release occured in the window it was pressed
	       in, and if so, generate an AWT click event */
	    gdk_window_get_size (event->any.window, &width, &height);
	    if (event->button.x >= 0
		&& event->button.y >= 0
		&& event->button.x <= width 
		&& event->button.y <= height)
	      (*gdk_env)->CallVoidMethod (gdk_env, *obj_ptr, postMouseEventID,
					  AWT_MOUSE_CLICKED, 
					  (jlong)event->button.time,
				   state_to_awt_mods (event->button.state) |
				  button_to_awt_mods (event->button.button), 
					  (jint)event->button.x,
					  (jint)event->button.y, 
					  click_count, JNI_FALSE);
	    
	  }
	  break;
	case GDK_ENTER_NOTIFY:
	  (*gdk_env)->CallVoidMethod (gdk_env, *obj_ptr, postMouseEventID,
				      AWT_MOUSE_ENTERED, 
				      (jlong)event->crossing.time,
				    state_to_awt_mods (event->crossing.state), 
				      (jint)event->crossing.x,
				      (jint)event->crossing.y, 
				      0, JNI_FALSE);
	  break;
	case GDK_LEAVE_NOTIFY:
	  if (event->crossing.mode == GDK_CROSSING_NORMAL)
	    (*gdk_env)->CallVoidMethod (gdk_env, *obj_ptr, postMouseEventID,
					AWT_MOUSE_EXITED, 
					(jlong)event->crossing.time,
				    state_to_awt_mods (event->crossing.state),
					(jint)event->crossing.x,
					(jint)event->crossing.y, 
					0, JNI_FALSE);
	  break;
	case GDK_CONFIGURE:
	  {
	    GtkWidget *widget;

	    gdk_window_get_user_data (event->any.window, (void **) &widget);
	    if (!widget) break;
	    
	    if (GTK_IS_WINDOW (widget))
	      {
		gint x, y;
		/* ignore where the WM puts us */
		gdk_window_get_root_origin (widget->window, &x, &y);

		/* configure events are not posted to the AWT event queue,
		   and as such, gdk/gtk will be called back before
		   postConfigureEvent returns */
		gdk_threads_leave ();
		(*gdk_env)->CallVoidMethod (gdk_env, *obj_ptr, 
					    postConfigureEventID,
					    (jint)x,
					    (jint)y,
					    (jint)event->configure.width,
					    (jint)event->configure.height);
		gdk_threads_enter ();
	      }
	  }
	  break;
	case GDK_EXPOSE:
	  {
	    GtkWidget *widget;

	    gdk_window_get_user_data (event->any.window, (void **) &widget);
	    if (!widget) break;

	    /* only canvases and containers can be drawn on in Java */
	    if (GTK_IS_DRAWING_AREA (widget) || GTK_IS_FIXED (widget))
	      (*gdk_env)->CallVoidMethod (gdk_env, *obj_ptr,
					  postExposeEventID,
					  (jint)event->expose.area.x,
					  (jint)event->expose.area.y,
					  (jint)event->expose.area.width,
					  (jint)event->expose.area.height);
	  }
	  break;
	default:
	}
      g_free (obj_ptr);
    } 
  
  gtk_main_do_event (event);
}

static void
attach_jobject (GdkWindow *window, jobject *obj)
{
  GdkAtom addr_atom = gdk_atom_intern ("_GNU_GTKAWT_ADDR", FALSE);
  GdkAtom type_atom = gdk_atom_intern ("CARDINAL", FALSE);

  gdk_window_set_events (window, 
			 gdk_window_get_events (window)
			 | GDK_POINTER_MOTION_MASK
			 | GDK_BUTTON_MOTION_MASK
			 | GDK_BUTTON_PRESS_MASK
			 | GDK_BUTTON_RELEASE_MASK
			 | GDK_KEY_PRESS_MASK
			 | GDK_KEY_RELEASE_MASK
			 | GDK_ENTER_NOTIFY_MASK
			 | GDK_LEAVE_NOTIFY_MASK
			 | GDK_STRUCTURE_MASK);

  gdk_property_change (window,
		       addr_atom,
		       type_atom,
		       8,
		       GDK_PROP_MODE_REPLACE,
		       (guchar *)obj,
		       sizeof (jobject));
}

static gint
awt_realize_hook (GtkWidget *widget, gpointer data)
{
  struct event_hook_info *hook_info = (struct event_hook_info *) data;
  int i;
  
  for (i = 0; i < hook_info->nwindows; i++)
    attach_jobject (*(hook_info->windows[i]), hook_info->peer_obj);
  
/*    attach_jobject (widget->window, obj); */
/*    if (GTK_IS_ENTRY (widget)) */
/*      attach_jobject (GTK_ENTRY (widget)->text_area, obj); */
/*    if (GTK_IS_TOGGLE_BUTTON (widget)) */
/*      attach_jobject (GTK_TOGGLE_BUTTON (widget)->event_window, obj); */
/*    if (GTK_IS_CHECK_BUTTON (widget)) */
/*      attach_jobject (GTK_CHECK_BUTTON (widget)->toggle_button.event_window, */
/*  		    obj); */

/*    gdk_threads_leave (); */
/*    (*gdk_env)->CallVoidMethod (gdk_env, *(hook_info->peer_obj), syncAttrsID); */
/*    gdk_threads_enter (); */

  return FALSE;
}

static gint
awt_unrealize_hook (GtkWidget *widget, gpointer data)
{
  struct event_hook_info *hook_info = (struct event_hook_info *) data;

  (*gdk_env)->DeleteGlobalRef (gdk_env, *(hook_info->peer_obj));

  free (hook_info->peer_obj);
  free (hook_info->windows);
  free (hook_info);

  return FALSE;
}

void
connect_awt_hook (JNIEnv *env, jobject peer_obj, GtkWidget *widget,
		  int nwindows, ...)
{
  struct event_hook_info *hook_info;
  int i;
  va_list ap;

  hook_info = (struct event_hook_info *) malloc (sizeof 
						 (struct event_hook_info));
  hook_info->peer_obj = (jobject *) malloc (sizeof (jobject));
  *(hook_info->peer_obj) = (*env)->NewGlobalRef (env, peer_obj);
  hook_info->nwindows = nwindows;
  hook_info->windows = (GdkWindow ***) malloc (sizeof (GdkWindow **) 
					       * nwindows);
  va_start (ap, nwindows);
  for (i = 0; i < nwindows; i++)
    hook_info->windows[i] = va_arg (ap, GdkWindow **);
  va_end (ap);

  gtk_signal_connect_after (GTK_OBJECT(widget), "realize", 
			    GTK_SIGNAL_FUNC(awt_realize_hook), 
			    hook_info);
  gtk_signal_connect (GTK_OBJECT(widget), "unrealize",
		      GTK_SIGNAL_FUNC(awt_unrealize_hook),
		      hook_info);
}

