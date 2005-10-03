/* dssi_data.h - DSSI data
   Copyright (C) 2005 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

#include <stdlib.h>
#include <dlfcn.h>
#include <sys/time.h>
#include <jni.h>
#include <dssi.h>
#include <jack/jack.h>
#include <alsa/asoundlib.h>
#include <alsa/seq.h>

#include <stdio.h>

#include "target_native.h"
#include "target_native_misc.h"
#include "../classpath/jcl.h"

#define EVENT_BUFFER_SIZE 1024

#define JLONG_TO_PTR(T,P) ((T *)(long)P)
#define PTR_TO_JLONG(P) ((jlong)(long)P)

typedef struct
{
  void *dlhandle;
  DSSI_Descriptor_Function fn;
  const DSSI_Descriptor *desc;
  jack_client_t *jack_client;
  jack_port_t *jack_left_output_port;
  jack_port_t *jack_right_output_port;
  snd_seq_event_t midiEventBuffer[EVENT_BUFFER_SIZE];
  int midiEventReadIndex; 
  int midiEventWriteIndex;
  LADSPA_Handle plugin_handle;
  float *left_buffer;
  float *right_buffer;
} dssi_data;

