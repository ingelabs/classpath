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

#endif /* __GTKPEER_H */
