/* Magical NSA API -- Associate a C ptr with an instance of an object
   Copyright (C) 1998 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


#ifndef JCL_NATIVE_STATE
#define JCL_NATIVE_STATE

#include <jni.h>

struct state_table
{
  jint size;			/* number of slots, should be prime */
  jfieldID hash;		/* field containing System.identityHashCode(this) */
  jclass clazz;			/* lock aquired for reading/writing nodes */
  struct state_node **head;
};

struct state_node
{
  jint key;
  void *c_state;
  struct state_node *next;
};

struct state_table * init_state_table_with_size (JNIEnv *, jclass, jint);
struct state_table * init_state_table (JNIEnv *, jclass);

/* lowlevel api */
void set_state_oid (JNIEnv *, jobject, struct state_table *, jint, void *);
void * get_state_oid (JNIEnv *, jobject, struct state_table *, jint);
void * remove_state_oid (JNIEnv *, jobject, struct state_table *, jint);

/* highlevel api */
int set_state (JNIEnv *, jobject, struct state_table *, void *);
void * get_state (JNIEnv *, jobject, struct state_table *);
void * remove_state_slot (JNIEnv *, jobject, struct state_table *);

#endif
