/*
 * gtkcheckboxpeer.c -- Native implementation of GtkCheckboxPeer
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
#include "GtkCheckboxPeer.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkCheckboxPeer_GtkRadioButtonSetGroup
  (JNIEnv *env, jobject obj, jobject group)
{
  GtkRadioButton *button;
  void *native_group, *ptr;

  native_group = get_state (env, group, window_table);
  ptr = get_state (env, obj, window_table);

  (*env)->MonitorEnter (env,java_mutex);

  button=GTK_RADIO_BUTTON(ptr);

  if (native_group==NULL)
    gtk_radio_button_set_group (button, NULL);
  else
    gtk_radio_button_set_group (button,
				gtk_radio_button_group 
				(GTK_RADIO_BUTTON (native_group)));
				
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  if (window_table!=NULL)
    {
      if (native_group==NULL)
	{
	  if (set_state (env, group, window_table, native_group)<0)
	    {
	      printf ("can't set state\n");
	    }
	}
    }
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkCheckboxPeer_GtkRadioButtonNew
  (JNIEnv *env, jobject obj, jobject group, jboolean checked, jstring label)
{
  GtkWidget *button;
  char *str;
  void *native_group;

  str=(char *)(*env)->GetStringUTFChars (env, label, 0);      
  native_group = get_state (env, group, window_table);

  (*env)->MonitorEnter (env,java_mutex);

  /* All checkboxes get a label, even if it is blank. */  

  if (native_group==NULL)
    button=gtk_radio_button_new_with_label_from_widget (NULL, str);
  else
    button=gtk_radio_button_new_with_label_from_widget (GTK_RADIO_BUTTON 
							(native_group), str);
  

  if (checked)
    gtk_toggle_button_set_state (GTK_TOGGLE_BUTTON (button), TRUE);
  
  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  if (window_table!=NULL)
    {
      if (native_group==NULL)
	{
	  if (set_state (env, group, window_table, ((void *)button))<0)
	    {
	      printf ("can't set state\n");
	    }
	}

      if (set_state (env,obj,window_table,((void *)button))<0)
	{
	  printf ("can't set state\n");
	}
    }

  (*env)->ReleaseStringUTFChars (env, label, str);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkCheckboxPeer_GtkCheckButtonNew
  (JNIEnv *env, jobject obj, jboolean checked, jstring label)
{
  GtkWidget *button;
  char *str;

  str=(char *)(*env)->GetStringUTFChars (env, label, 0);      

  (*env)->MonitorEnter (env,java_mutex);

  /* All checkboxes get a label, even if it is blank. */  
  button=gtk_check_button_new_with_label (str);
  if (checked)
    gtk_toggle_button_set_state (GTK_TOGGLE_BUTTON (button), TRUE);

  gdk_threads_wake();
  (*env)->MonitorExit (env,java_mutex);

  if (window_table!=NULL)
    {
      if (set_state (env,obj,window_table,((void *)button))<0)
	{
	  printf ("can't set state\n");
	}
    }

  (*env)->ReleaseStringUTFChars (env, label, str);
}


JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkCheckboxPeer_GtkCheckButtonSetState
  (JNIEnv *env, jobject obj, jboolean checked)
{
  void *ptr;

  ptr=get_state(env,obj,window_table);

  if (checked)
    gtk_toggle_button_set_state (GTK_TOGGLE_BUTTON (ptr), TRUE);
}

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkCheckboxPeer_GtkCheckButtonSetLabel
  (JNIEnv *env, jobject obj, jstring label)
{
  void *ptr;
  char *str;
  GList *child;

  ptr=get_state(env,obj,window_table);
  
  printf("labelset\n");

  if (ptr==NULL)
    {
      printf ("can't get state\n");
    }
  else
    {
      str=(char *)(*env)->GetStringUTFChars (env, label, 0);      
      (*env)->MonitorEnter (env,java_mutex);
   
      /* We assume that the button has 1 child, a label. */
      /* We'd better not be wrong. */

      child=gtk_container_children (GTK_CONTAINER(ptr));
      if (!child)
	printf("No children in button!\n");
      if(!GTK_IS_LABEL(child->data))
	printf("Child is not label!\n");

      gtk_label_set (GTK_LABEL(child->data),str);
      g_list_free(child);

      gdk_threads_wake();
      (*env)->MonitorExit (env,java_mutex);
      (*env)->ReleaseStringUTFChars (env, label, str);
    }
}

