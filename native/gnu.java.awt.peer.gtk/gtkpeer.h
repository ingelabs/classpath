/*
 * gtkpeer.h -- Some global variables and #defines
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

#include <gtk/gtk.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "native_state.h"

#include <jni.h>

#define RC_FILE      ".classpath-gtkrc"
#define JVM_SUN
/*
  #define JVM_JAPHAR
*/

#ifndef __GTKPEER_H__
#define __GTKPEER_H__

#ifdef JVM_SUN

extern struct state_table *native_state_table;

#define NSA_INIT(env, clazz) \
  native_state_table = init_state_table (env, clazz)

#define NSA_GET_PTR(env, obj) \
  get_state (env, obj, native_state_table)

#define NSA_SET_PTR(env, obj, ptr) \
  set_state (env, obj, native_state_table, (void *)ptr)

#endif /* JVM_SUN */

#define AWT_DEFAULT_CURSOR 0
#define AWT_CROSSHAIR_CURSOR 1
#define AWT_TEXT_CURSOR 2
#define AWT_WAIT_CURSOR 3
#define AWT_SW_RESIZE_CURSOR 4
#define AWT_SE_RESIZE_CURSOR 5
#define AWT_NW_RESIZE_CURSOR 6
#define AWT_NE_RESIZE_CURSOR 7
#define AWT_N_RESIZE_CURSOR 8
#define AWT_S_RESIZE_CURSOR 9
#define AWT_W_RESIZE_CURSOR 10
#define AWT_E_RESIZE_CURSOR 11
#define AWT_HAND_CURSOR 12
#define AWT_MOVE_CURSOR 13

#define SYNTHETIC_EVENT_MASK (1 << 10)

#define AWT_SHIFT_MASK   (1 << 0)
#define AWT_CTRL_MASK    (1 << 1)
#define AWT_META_MASK    (1 << 2)
#define AWT_ALT_MASK     (1 << 3)

#define AWT_BUTTON1_MASK (1 << 4)
#define AWT_BUTTON2_MASK AWT_ALT_MASK
#define AWT_BUTTON3_MASK AWT_META_MASK

#define MULTI_CLICK_TIME   250
/* as opposed to a MULTI_PASS_TIME :) */

#define AWT_MOUSE_CLICKED  500
#define AWT_MOUSE_PRESSED  501
#define AWT_MOUSE_RELEASED 502
#define AWT_MOUSE_MOVED    503
#define AWT_MOUSE_ENTERED  504
#define AWT_MOUSE_EXITED   505
#define AWT_MOUSE_DRAGGED  506

extern jmethodID postActionEventID;
extern jmethodID postMouseEventID;
extern jmethodID postConfigureEventID;
extern jmethodID postExposeEventID;
extern jmethodID syncAttrsID;

extern JNIEnv *gdk_env;

void awt_event_handler (GdkEvent *event);
void connect_awt_hook (JNIEnv *env, jobject peer_obj, GtkWidget *widget,
		       int nwindows, ...);

void set_visible (GtkWidget *widget, jboolean visible);

#endif /* __GTKPEER_H */
