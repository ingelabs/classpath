/*
 * gtkchoicepeer.c -- Native implementation of GtkChoicePeer
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
#include "GtkChoicePeer.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkChoicePeer_gtkOptionMenuNew (JNIEnv *env, 
    jobject obj, jobjectArray items)
{
  GtkWidget *menu, *optionmenu;
  GtkWidget *menuitem;
  jsize count;
  jobject item;
  int i;
  char *label;

  count=(*env)->GetArrayLength (env, items);

  (*env)->MonitorEnter (env,java_mutex);

  menu = gtk_menu_new();
  
  for (i=0; i<count; i++) 
    {
      item=(*env)->GetObjectArrayElement (env, items, i);
      label=(char *)(*env)->GetStringUTFChars (env, item, 0);

      menuitem = gtk_menu_item_new_with_label (label);
      gtk_menu_append (GTK_MENU (menu), menuitem);
      gtk_widget_show (menuitem);

      (*env)->ReleaseStringUTFChars (env, item, label);
    }

  optionmenu = gtk_option_menu_new ();
  gtk_option_menu_set_menu (GTK_OPTION_MENU (optionmenu), menu);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  NSA_SET_PTR (env, obj, optionmenu);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkChoicePeer_gtkOptionMenuAdd (JNIEnv *env, 
    jobject obj, jstring item, jint index)
{
  void *ptr;
  char *label;
  GtkWidget *menu, *menuitem;

  ptr=NSA_GET_PTR (env, obj);
  
  printf("add\n");

  label=(char *)(*env)->GetStringUTFChars (env, item, 0);      
  (*env)->MonitorEnter (env,java_mutex);
  
  menu=gtk_option_menu_get_menu (GTK_OPTION_MENU (ptr));

  menuitem = gtk_menu_item_new_with_label (label);
  gtk_menu_insert (GTK_MENU (menu), menuitem, index);
  gtk_widget_show (menuitem);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
  (*env)->ReleaseStringUTFChars (env, item, label);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkChoicePeer_gtkOptionMenuRemove (JNIEnv *env, 
    jobject obj, jint index)
{
  void *ptr;
  GtkMenuShell *menu_shell;
  GList *tmp_list, *tmp;

  ptr = NSA_GET_PTR (env, obj);
  
  printf("remove\n");

  (*env)->MonitorEnter (env,java_mutex);

  menu_shell = GTK_MENU_SHELL (gtk_option_menu_get_menu (GTK_OPTION_MENU 
							 (ptr)));

  tmp_list=gtk_container_children (GTK_CONTAINER(menu_shell));
  tmp=g_list_nth (tmp_list, index);
  gtk_container_remove (GTK_CONTAINER (menu_shell), GTK_WIDGET 
			(tmp->data));

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkChoicePeer_gtkOptionMenuSelect (JNIEnv *env, 
    jobject obj, jint index)
{
  void *ptr;

  ptr=NSA_GET_PTR (env, obj);
  
  printf("set\n");

  (*env)->MonitorEnter (env,java_mutex);
  
  gtk_option_menu_set_history (GTK_OPTION_MENU (ptr), index);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);
}


