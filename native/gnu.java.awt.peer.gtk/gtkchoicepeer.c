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

struct item_event_hook_info
{
  jobject peer_obj;
  jobject item_obj;
};

static void connect_choice_item_selectable_hook (JNIEnv *env, 
						 jobject peer_obj, 
						 GtkItem *item, 
						 jobject item_obj);

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkChoicePeer_gtkOptionMenuNew (JNIEnv *env, 
    jobject obj, jobject parent_obj, jobjectArray items, jboolean visible)
{
  GtkWidget *menu, *optionmenu, *parent;
  GtkWidget *menuitem;
  jsize count;
  int i;

  parent = NSA_GET_PTR (env, parent_obj);
  count = (*env)->GetArrayLength (env, items);

  gdk_threads_enter ();

  menu = gtk_menu_new();
  gtk_widget_show (menu);

  for (i = 0; i < count; i++) 
    {
      jobject item;
      const char *label;

      item = (*env)->GetObjectArrayElement (env, items, i);
      label = (*env)->GetStringUTFChars (env, item, NULL);

      menuitem = gtk_menu_item_new_with_label (label);

      (*env)->ReleaseStringUTFChars (env, item, label);

      gtk_menu_append (GTK_MENU (menu), menuitem);
      gtk_widget_show (menuitem);

      connect_choice_item_selectable_hook (env, obj, 
					   GTK_ITEM (menuitem), item);
    }

  optionmenu = gtk_option_menu_new ();

  gtk_option_menu_set_menu (GTK_OPTION_MENU (optionmenu), menu);

  set_parent (optionmenu, GTK_CONTAINER (parent));
  gtk_widget_realize (optionmenu);
  connect_awt_hook (env, obj, optionmenu, 1, optionmenu->window);
  set_visible (optionmenu, visible);

  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, optionmenu);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkChoicePeer_gtkOptionMenuAdd (JNIEnv *env, 
    jobject obj, jstring item, jint index)
{
  void *ptr;
  const char *label;
  GtkWidget *menu, *menuitem;

  ptr = NSA_GET_PTR (env, obj);
  
  printf("add\n");

  label = (*env)->GetStringUTFChars (env, item, 0);      
  gdk_threads_enter ();
  
  menu = gtk_option_menu_get_menu (GTK_OPTION_MENU (ptr));

  menuitem = gtk_menu_item_new_with_label (label);
  gtk_menu_insert (GTK_MENU (menu), menuitem, index);
  gtk_widget_show (menuitem);

  connect_choice_item_selectable_hook (env, obj, GTK_ITEM (menuitem), item);

  gdk_threads_leave ();
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

  gdk_threads_enter ();

  menu_shell = GTK_MENU_SHELL (gtk_option_menu_get_menu (GTK_OPTION_MENU 
							 (ptr)));

  tmp_list=gtk_container_children (GTK_CONTAINER(menu_shell));
  tmp=g_list_nth (tmp_list, index);
  gtk_container_remove (GTK_CONTAINER (menu_shell), GTK_WIDGET 
			(tmp->data));

  gdk_threads_leave ();
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkChoicePeer_gtkOptionMenuSelect (JNIEnv *env, 
    jobject obj, jint index)
{
  void *ptr;

  ptr=NSA_GET_PTR (env, obj);
  
  printf("set\n");

  gdk_threads_enter ();
  gtk_option_menu_set_history (GTK_OPTION_MENU (ptr), index);
  gdk_threads_leave ();
}


static void
item_activate (GtkItem *item, struct item_event_hook_info *ie)
{
  (*gdk_env)->CallVoidMethod (gdk_env, ie->peer_obj,
			      postItemEventID,
			      ie->item_obj,
			      (jint) AWT_ITEM_SELECTED);
}

static void
connect_choice_item_selectable_hook (JNIEnv *env, jobject peer_obj, 
				     GtkItem *item, jobject item_obj)
{
  struct item_event_hook_info *ie;

  ie = (struct item_event_hook_info *) 
    malloc (sizeof (struct item_event_hook_info));

  ie->peer_obj = (*env)->NewGlobalRef (env, peer_obj);
  ie->item_obj = (*env)->NewGlobalRef (env, item_obj);

  gtk_signal_connect (GTK_OBJECT (item), "activate", 
		      GTK_SIGNAL_FUNC (item_activate), ie);
}
