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
#include <X11/Xlib.h>

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
       || event->type == GDK_LEAVE_NOTIFY)
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
	    break;
	  default:
	  }
	  free (obj_ptr);
      }

  gtk_main_do_event (event);
}

static gint
awt_realize_hook (GtkWidget *widget, gpointer data)
{
  jobject *obj = (jobject *) data;
  GdkAtom addr_atom = gdk_atom_intern ("_GNU_GTKAWT_ADDR", FALSE);
  GdkAtom type_atom = gdk_atom_intern ("CARDINAL", FALSE);

  gdk_property_change (widget->window,
		       addr_atom,
		       type_atom,
		       8,
		       GDK_PROP_MODE_REPLACE,
		       (guchar *)obj,
		       sizeof (jobject));

  return FALSE;
}

static gint
awt_unrealize_hook (GtkWidget *widget, gpointer data)
{
  jobject *obj = (jobject *) data;

  (*gdk_env)->DeleteGlobalRef (gdk_env, *obj);
  free (obj);

  return FALSE;
}

void
connect_awt_hook (JNIEnv *env, jobject peer_obj, GtkWidget *widget)
{
  jobject *gobj = (jobject *) malloc (sizeof (jobject));
  *gobj = (*env)->NewGlobalRef (env, peer_obj);

  gtk_signal_connect_after (GTK_OBJECT(widget), "realize", 
			    GTK_SIGNAL_FUNC(awt_realize_hook), 
			    gobj);
  gtk_signal_connect (GTK_OBJECT(widget), "unrealize",
		      GTK_SIGNAL_FUNC(awt_unrealize_hook),
		      gobj);
}

