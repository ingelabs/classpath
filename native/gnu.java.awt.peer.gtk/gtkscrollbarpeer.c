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

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollbarPeer_gtkScrollbarNew
    (JNIEnv *env, jobject obj, jint orientation, jint value, jint min, 
     jint max)
{
  GtkWidget *sb;
  GtkObject *adj;

  gdk_threads_enter ();
  adj = gtk_adjustment_new (value, min, max, 1, 10, 10);
  if (orientation) 
    sb = gtk_hscrollbar_new (GTK_ADJUSTMENT (adj));
  else
    sb = gtk_vscrollbar_new (GTK_ADJUSTMENT (adj));
  connect_awt_hook (env, obj, sb, 4, 
		    &(GTK_RANGE (sb)->trough),
		    &(GTK_RANGE (sb)->slider),
		    &(GTK_RANGE (sb)->step_forw),
		    &(GTK_RANGE (sb)->step_back));
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

  adj = GTK_RANGE(ptr)->adjustment;
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

  adj = GTK_RANGE(ptr)->adjustment;
  adj->page_increment = amount;
  adj->page_size = amount;
  gtk_adjustment_changed (adj);

  gdk_threads_leave ();
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkScrollbarPeer_setValues
    (JNIEnv *env, jobject obj, jint value, int size, int min, int max)
{
  void *ptr;
  GtkAdjustment *adj;

  ptr = NSA_GET_PTR (env, obj);

  gdk_threads_enter ();

  adj = GTK_RANGE(ptr)->adjustment;
  adj->lower = min;
  adj->upper = max;
  adj->value = value;
  gtk_adjustment_changed (adj);

  gdk_threads_leave ();
}

