/*
 * gtklistpeer.c -- Native implementation of GtkListPeer
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
#include "GtkListPeer.h"

JNIEXPORT void JNICALL 
Java_gnu_java_awt_peer_gtk_GtkListPeer_gtkListNew
  (JNIEnv *env, jobject obj, jobject parent_obj,
   jobject jlist, jobjectArray items, int mode, jboolean visible)
{
  GtkWidget *list, *listitem, *sw, *parent;
  jsize count;
  int i;

  parent = NSA_GET_PTR (env, parent_obj);

  count = (*env)->GetArrayLength (env, items);

  gdk_threads_enter ();

  list = gtk_list_new ();
  gtk_widget_show (list);

  sw = gtk_scrolled_window_new (NULL, NULL);
  set_parent (sw, GTK_CONTAINER (parent));
  gtk_widget_realize (sw);
  set_visible (sw, visible);

  gtk_scrolled_window_set_policy (GTK_SCROLLED_WINDOW (sw), 
				  GTK_POLICY_AUTOMATIC,
				  GTK_POLICY_AUTOMATIC);
  gtk_scrolled_window_add_with_viewport (GTK_SCROLLED_WINDOW (sw), list);
  connect_awt_hook (env, obj, list, 1, list->window);

  gtk_list_set_selection_mode (GTK_LIST (list),
			       mode? GTK_SELECTION_MULTIPLE : 
			       GTK_SELECTION_SINGLE);
  
  for (i = 0; i < count; i++) 
    {
      const char *text;
      jobject item;

      item = (*env)->GetObjectArrayElement (env, items, i);
      text = (*env)->GetStringUTFChars (env, item, NULL);

      listitem = gtk_list_item_new_with_label (text);

      gtk_widget_show (listitem);
      gtk_container_add (GTK_CONTAINER (list), listitem);
      gtk_widget_realize (listitem);
      connect_awt_hook (env, obj, listitem, 1, listitem->window);

      (*env)->ReleaseStringUTFChars (env, item, text);
    }

  gdk_threads_leave ();

  NSA_SET_PTR (env, obj, sw);
  NSA_SET_PTR (env, jlist, list);
}

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkListPeer_gtkListInsert
  (JNIEnv *env, jobject obj, jobject jlist, jstring text, jint index)
{
  void *ptr;
  GtkList *list;
  const char *str;
  GList *item_list;
    
  ptr = NSA_GET_PTR (env, jlist);
  str = (*env)->GetStringUTFChars (env, text, NULL);

  gdk_threads_enter ();
  list = GTK_LIST (ptr);
  
  item_list = g_list_alloc ();
  item_list->data = gtk_list_item_new_with_label (str);
  gtk_widget_show (item_list->data);

  gtk_list_insert_items (GTK_LIST (list), item_list, index);
  gtk_widget_realize (item_list->data);
  connect_awt_hook (env, obj, item_list->data, 1, 
		    GTK_WIDGET(item_list->data)->window);

  gdk_threads_leave ();

  (*env)->ReleaseStringUTFChars (env, text, str);
}


JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkListPeer_gtkListClearItems
  (JNIEnv *env, jobject obj, jobject jlist, jint start, jint end)
{
  void *ptr;
  GtkList *list;
    
  ptr = NSA_GET_PTR (env, jlist);

  gdk_threads_enter ();
  list = GTK_LIST (ptr);
  gtk_list_clear_items (GTK_LIST (list), start, end);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkListPeer_gtkListSelectItem
  (JNIEnv *env, jobject obj, jobject jlist, jint index)
{
  void *ptr;
  GtkList *list;
    
  ptr = NSA_GET_PTR (env, jlist);

  gdk_threads_enter ();
  list = GTK_LIST (ptr);
  gtk_list_select_item (GTK_LIST (list), index);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkListPeer_gtkListUnselectItem
  (JNIEnv *env, jobject obj, jobject jlist, jint index)
{
  void *ptr;
  GtkList *list;
    
  ptr = NSA_GET_PTR (env, jlist);

  gdk_threads_enter ();
  list = GTK_LIST (ptr);
  gtk_list_unselect_item (GTK_LIST (list), index);
  gdk_threads_leave ();
}

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkListPeer_gtkListGetSize
  (JNIEnv *env, jobject obj, jobject jlist, jint rows, jintArray jdims)
{
  void *ptr;
  jint *dims;
  GtkWidget *list;
  GtkScrolledWindow *sw;
  GtkRequisition myreq;

  dims = (*env)->GetIntArrayElements (env, jdims, NULL);
  dims[0] = dims[1] = 0;

  if (rows < 3)
    rows = 3;

  ptr = NSA_GET_PTR (env, jlist);
  gdk_threads_enter ();

  list = GTK_WIDGET (ptr);
  sw = GTK_SCROLLED_WINDOW (NSA_GET_PTR (env, obj));

  /*
  gtk_widget_size_request(GTK_WIDGET (GTK_SCROLLED_WINDOW(sw)->hscrollbar), 
                                      &myreq);
  dims[1]=myreq.height+GTK_SCROLLED_WINDOW_CLASS 
    (GTK_OBJECT (sw)->klass)->scrollbar_spacing;
  */

  gtk_signal_emit_by_name (GTK_OBJECT (GTK_SCROLLED_WINDOW(sw)->vscrollbar), 
			   "size_request", &myreq);
  /*

    gtk_widget_size_request(GTK_WIDGET (GTK_SCROLLED_WINDOW(sw)->vscrollbar), 
                                      &myreq);
  */

  dims[0]=myreq.width+GTK_SCROLLED_WINDOW_CLASS
    (GTK_OBJECT (sw)->klass)->scrollbar_spacing;

  gtk_signal_emit_by_name (GTK_OBJECT (list), "size_request", &myreq);
  
  //  gtk_widget_size_request(GTK_WIDGET (list), &myreq);
                                      
  dims[0] += myreq.width + gdk_char_width (list->style->font, 'W');
             
  dims[1] += ((rows * (gdk_char_height (list->style->font, 'W')+7))
	      + (2 * (list->style->klass->ythickness)));
		 
  
  gdk_threads_leave ();

  (*env)->ReleaseIntArrayElements (env, jdims, dims, 0);
}


JNIEXPORT jintArray JNICALL
Java_gnu_java_awt_peer_gtk_GtkListPeer_gtkListGetSelected
  (JNIEnv *env, jobject obj, jobject jlist)
{
  void *ptr;
  GtkList *list;
  jintArray selection;
  jint *sel;
  GList *child;
  int count = 0;
  int pos = 0;
  int index = 0;

  ptr = NSA_GET_PTR (env, jlist);
  gdk_threads_enter ();

  list = GTK_LIST (ptr);

  child = list->selection;
  while (child)
    {
      count++;
      child = g_list_next (child);
    }

  selection = (*env)->NewIntArray (env, count);
  sel = (*env)->GetIntArrayElements (env, selection, NULL);  

  child = list->children;
  while (child && (pos < count))
    {
      if(GTK_WIDGET_STATE (child->data) == GTK_STATE_SELECTED)
	{
	  sel[pos]=index;
	  pos++;
	}
      index++;
      child = g_list_next (child);
    }

  gdk_threads_leave ();
  (*env)->ReleaseIntArrayElements (env, selection, sel, 0);

  return selection;
}

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkListPeer_gtkListScrollVertical
  (JNIEnv *env, jobject obj, jobject jlist, jint index)
{
  void *ptr1, *ptr2;
  GtkList *list;
  GtkWidget *sw;
  GList *child;
  int count = 0;
  GtkAdjustment *adj;

  ptr1 = NSA_GET_PTR (env, obj);
  ptr2 = NSA_GET_PTR (env, jlist);

  gdk_threads_enter ();

  sw = GTK_WIDGET (ptr1);
  list = GTK_LIST (ptr2);

  child = list->children;
  while (child)
    {
      count++;
      child = g_list_next (child);
    }
  
  gtk_list_scroll_vertical (GTK_LIST (list), GTK_SCROLL_JUMP, 
			    (gfloat) ((gfloat) index / (gfloat) count));
  
  adj = GTK_RANGE (GTK_SCROLLED_WINDOW (sw)->vscrollbar)->adjustment;

  adj->value = CLAMP (adj->lower + (adj->upper - adj->lower) * 
		      ((gfloat) ((gfloat) index / (gfloat) count)),
		      adj->lower, adj->upper - adj->page_size);
  gtk_adjustment_value_changed (adj);

  gdk_threads_leave ();
}

JNIEXPORT void JNICALL
Java_gnu_java_awt_peer_gtk_GtkListPeer_gtkListSetSelectionMode
  (JNIEnv *env, jobject obj, jobject jlist, jint mode)
{
  void *ptr;
  GtkList *list;
    
  ptr = NSA_GET_PTR (env, jlist);

  gdk_threads_enter ();
  list = GTK_LIST (ptr);
  gtk_list_set_selection_mode (GTK_LIST (list),
			       mode ? GTK_SELECTION_MULTIPLE : 
			       GTK_SELECTION_SINGLE);
  gdk_threads_leave ();
}

