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
#include "gdkjnithreads.h"

/*
 * Call gtk_init.  It is very important that this happen before any other
 * gtk calls.
 */

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkMainThread_GtkInit (JNIEnv *env, jclass clazz)
{
  int argc=1;
  char **argv;
  GdkJavaMutex *initret;
  char *homedir, *rcpath=NULL;

  printf ("init\n");

  NSA_INIT(env, clazz);

  /* GTK requires a program's argc and argv variables, and requires that they
     be valid.  */

  argv=(char **)malloc (sizeof(char*)*2);
  argv[0]="";
  argv[1]=NULL;

  /* This sets the gdk thread function pointers to our set. */

  gdk_threads_set_funcs (&jni_threads_mutex_funcs);
  initret=gdk_threads_init (env);
  java_mutex=*(initret->mutex);

  gtk_init (&argc,&argv);

  if ((homedir=getenv("HOME")))
    {
      rcpath=(char *)malloc (strlen (homedir)+strlen (RC_FILE)+2);
      sprintf (rcpath, "%s/%s", homedir, RC_FILE);
    }
  
  gtk_rc_parse ((rcpath) ? rcpath : RC_FILE);

  if (rcpath)
    free (rcpath);

  free (argv);
}

/*
 * Run gtk_main and block.
 */ 
JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkMainThread_GtkMain (JNIEnv *env, jobject obj)
{
    (*env)->MonitorEnter (env,java_mutex);
    /* This won't return until GTK is _done_.
       It is okay (absolutely necessary) to have it inside of a monitor
       block because gtk_main itself releases the monitor when appropriate. */
    gtk_main();
    gdk_threads_wake();
    (*env)->MonitorExit (env,java_mutex);
}
