/*
 * gtkscrollbarpeer.c -- Native implementation of GtkScrollbarPeer
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
#include "GtkScrollbarPeer.h"

struct range_scrollbar
{
  GtkRange *range;
  jobject *scrollbar;
};

static void 
post_adjustment_event (GtkAdjustment *adj, struct range_scrollbar *rs)
{
  jint type;

  switch (rs->range->scroll_type)
    {
    case GTK_SCROLL_STEP_FORWARD:
      type = AWT_ADJUSTMENT_UNIT_INCREMENT;
      break;
    case GTK_SCROLL_STEP_BACKWARD:
      type = AWT_ADJUSTMENT_UNIT_DECREMENT;
      break;
    case GTK_SCROLL_PAGE_FORWARD:
      type = AWT_ADJUSTMENT_BLOCK_INCREMENT;
      break;
    case GTK_SCROLL_PAGE_BACKWARD:
      type = AWT_ADJUSTMENT_BLOCK_DECREMENT;
      break;
    case GTK_SCROLL_JUMP:
    case GTK_SCROLL_NONE:
      type = AWT_ADJUSTMENT_TRACK;
      break;
    }
  
  (*gdk_env)->CallVoidMethod (gdk_env, *(rs->scrollbar), postAdjustmentEventID,
			      type, (jint) adj->value);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollbarPeer_gtkScrollbarNew
    (JNIEnv *env, jobject obj, jobject parent_obj,
     jint orientation, jint value, jint visible_amount, jint min, 
     jint max, jboolean visible)
{
  GtkWidget *sb;
  GtkObject *adj;
  void *parent;
  struct range_scrollbar *rs;

  rs = (struct range_scrollbar *) malloc (sizeof (struct range_scrollbar));
  parent = NSA_GET_PTR (env, parent_obj);

  gdk_threads_enter ();
  adj = gtk_adjustment_new (value, min, max, 1, 10, visible_amount);

  sb = (orientation) ? gtk_vscrollbar_new (GTK_ADJUSTMENT (adj)) :
                       gtk_hscrollbar_new (GTK_ADJUSTMENT (adj));

  set_parent (sb, GTK_CONTAINER (parent));
  gtk_widget_realize (sb);
  connect_awt_hook (env, obj, sb, 4, 
		    GTK_RANGE (sb)->trough,
		    GTK_RANGE (sb)->slider,
		    GTK_RANGE (sb)->step_forw,
		    GTK_RANGE (sb)->step_back);
  set_visible (sb, visible);

  rs->range = GTK_RANGE (sb);
  rs->scrollbar = (jobject *) malloc (sizeof (jobject));
  *(rs->scrollbar) = (*env)->NewGlobalRef (env, obj);
  gtk_signal_connect (GTK_OBJECT (adj), "value_changed", 
		      GTK_SIGNAL_FUNC (post_adjustment_event), rs);

  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, sb);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollbarPeer_setLineIncrement
    (JNIEnv *env, jobject obj, jint amount)
{
  void *ptr;
  GtkAdjustment *adj;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();

  adj = GTK_RANGE (ptr)->adjustment;
  adj->step_increment = amount;
  gtk_adjustment_changed (adj);

  gdk_threads_leave ();
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollbarPeer_setPageIncrement
    (JNIEnv *env, jobject obj, jint amount)
{
  void *ptr;
  GtkAdjustment *adj;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();

  adj = GTK_RANGE (ptr)->adjustment;
  adj->page_increment = amount;
  gtk_adjustment_changed (adj);

  gdk_threads_leave ();
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollbarPeer_setValues
    (JNIEnv *env, jobject obj, jint value, int visible, int min, int max)
{
  void *ptr;
  GtkAdjustment *adj;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();

  adj = GTK_RANGE (ptr)->adjustment;
  adj->value = value;
  adj->page_size = visible;
  adj->lower = min;
  adj->upper = max;
  gtk_adjustment_changed (adj);

  gdk_threads_leave ();
}
