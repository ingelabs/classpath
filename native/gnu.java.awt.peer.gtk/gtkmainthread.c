/*
 * gtkmainthread.c -- Native implementation of GtkMainThread
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
#include "GtkMainThread.h"
#include "gthread-jni.h"

#ifdef JVM_SUN
  struct state_table *native_state_table;
#endif

jmethodID postActionEventID;
jmethodID postMouseEventID;
jmethodID postConfigureEventID;
jmethodID postExposeEventID;
jmethodID postKeyEventID;
jmethodID postFocusEventID;
jmethodID postAdjustmentEventID;
jmethodID postItemEventID;
jmethodID postListItemEventID;
JNIEnv *gdk_env;

/*
 * Call gtk_init.  It is very important that this happen before any other
 * gtk calls.
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkMainThread_gtkInit (JNIEnv *env, jclass clazz)
{
  int argc = 1;
  char **argv;
  char *homedir, *rcpath = NULL;
/*    jclass gtkgenericpeer; */
  jclass gtkcomponentpeer, gtkwindowpeer, gtkscrollbarpeer, gtklistpeer;

  printf ("init\n");

  NSA_INIT (env, clazz);

  /* GTK requires a program's argc and argv variables, and requires that they
     be valid.  */

  argv = (char **) malloc (sizeof (char *) * 2);
  argv[0] = "";
  argv[1] = NULL;

  /* until we have JDK 1.2 JNI, assume we have a VM with threads that 
     match what GLIB was compiled for */
  g_thread_init (NULL);

  gtk_init (&argc, &argv);

  gdk_env = env;
  gdk_event_handler_set ((GdkEventFunc)awt_event_handler, NULL, NULL);

  if ((homedir = getenv ("HOME")))
    {
      rcpath = (char *) malloc (strlen (homedir) + strlen (RC_FILE) + 2);
      sprintf (rcpath, "%s/%s", homedir, RC_FILE);
    }
  
  gtk_rc_parse ((rcpath) ? rcpath : RC_FILE);

  if (rcpath)
    free (rcpath);

  free (argv);

  /* setup cached IDs for posting GTK events to Java */
/*    gtkgenericpeer = (*env)->FindClass (env,  */
/*  				      "gnu/java/awt/peer/gtk/GtkGenericPeer"); */
  gtkcomponentpeer = (*env)->FindClass (env,
				     "gnu/java/awt/peer/gtk/GtkComponentPeer");
  gtkwindowpeer = (*env)->FindClass (env,
				     "gnu/java/awt/peer/gtk/GtkWindowPeer");
  gtkscrollbarpeer = (*env)->FindClass (env, 
				     "gnu/java/awt/peer/gtk/GtkScrollbarPeer");
  gtklistpeer = (*env)->FindClass (env, "gnu/java/awt/peer/gtk/GtkListPeer");
/*    gdkColor = (*env)->FindClass (env, */
/*  				"gnu/java/awt/peer/gtk/GdkColor"); */
/*    gdkColorID = (*env)->GetMethodID (env, gdkColor, "<init>", "(III)V"); */
/*    postActionEventID = (*env)->GetMethodID (env, gtkgenericpeer,  */
/*  					   "postActionEvent",  */
/*  					   "(Ljava/lang/String;I)V"); */
  postMouseEventID = (*env)->GetMethodID (env, gtkcomponentpeer, 
					  "postMouseEvent", "(IJIIIIZ)V");
  postConfigureEventID = (*env)->GetMethodID (env, gtkwindowpeer, 
					  "postConfigureEvent", "(IIII)V");
  postExposeEventID = (*env)->GetMethodID (env, gtkcomponentpeer, 
					  "postExposeEvent", "(IIII)V");
  postKeyEventID = (*env)->GetMethodID (env, gtkcomponentpeer,
					"postKeyEvent", "(IJIIC)V");
  postFocusEventID = (*env)->GetMethodID (env, gtkcomponentpeer,
					  "postFocusEvent", "(IZ)V");
  postAdjustmentEventID = (*env)->GetMethodID (env, gtkscrollbarpeer,
					       "postAdjustmentEvent", 
					       "(II)V");
  postItemEventID = (*env)->GetMethodID (env, gtkcomponentpeer,
					 "postItemEvent", 
					 "(Ljava/lang/Object;I)V");
  postListItemEventID = (*env)->GetMethodID (env, gtklistpeer,
					     "postItemEvent",
					     "(II)V");
}

/*
 * Run gtk_main and block.
 */ 
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkMainThread_gtkMain (JNIEnv *env, jobject obj)
{
  gdk_threads_enter ();
  gtk_main ();
  gdk_threads_leave ();
}
