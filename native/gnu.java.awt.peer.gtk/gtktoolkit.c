/*
 * gtktoolkit.c -- Native portion of GtkToolkit
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
#include "GtkToolkit.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkToolkit_beep (JNIEnv *env, jobject obj)
{
  (*env)->MonitorEnter (env, java_mutex);

  gdk_beep ();

  gdk_threads_wake ();
  (*env)->MonitorExit (env, java_mutex);
}

