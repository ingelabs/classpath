/* gnu_javax_sound_midi_dssi_DSSISynthesizer.c - DSSI Synth
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

#include <config.h>
#include <gnu_javax_sound_midi_dssi_DSSISynthesizer.h> 

#include "dssi_data.h"

/**
 * The jack callback routine.
 *
 * This function is called by the jack audio system in its own thread
 * whenever it needs new audio data.
 *
 */
static int
process (jack_nframes_t nframes, void *arg)
{    
  struct timeval tv;
  dssi_data *data = (dssi_data *) arg;
  int index;
  jack_default_audio_sample_t *buffer;

  /* Look through the event buffer to see if anything needs doing.  */
  for ( index = data->midiEventReadIndex; 
	index != data->midiEventWriteIndex;
	index = (index + 1) % EVENT_BUFFER_SIZE);

  /* Call the synth audio processing routine.  */
  data->desc->run_synth(data->plugin_handle,
			nframes,
			&data->midiEventBuffer[data->midiEventReadIndex],
			data->midiEventWriteIndex - data->midiEventReadIndex);

  /* Update the read index on our circular buffer.  */
  data->midiEventReadIndex = data->midiEventWriteIndex;

  /* Copy output from the synth to jack.  

     FIXME: This is hack that only gets one channel from the synth and
     send that to both jack ports (until we handle stero synths
     properly).

     FIXME: Can we avoid this copying?  */
  buffer = jack_port_get_buffer(data->jack_left_output_port, nframes);
  memcpy (buffer, data->left_buffer, nframes * sizeof(LADSPA_Data));
  buffer = jack_port_get_buffer(data->jack_right_output_port, nframes);
  memcpy (buffer, data->left_buffer, nframes * sizeof(LADSPA_Data));
  
  return 0;   
}

/* FIXME: Temporary hack.  */
float mctrl = 0.9f;

/**
 * Open a new synthesizer.  This currently involves instantiating a
 * new synth, creating a new jack client connection, and activating
 * both.
 *
 */
JNIEXPORT void JNICALL
Java_gnu_javax_sound_midi_dssi_DSSISynthesizer_open_1 
  (JNIEnv *env, jclass clazz __attribute__((unused)), jlong handle)
{
  unsigned int port_count, j;
  dssi_data *data = (dssi_data *) (long) handle;
  if ((data->jack_client = jack_client_new (data->desc->LADSPA_Plugin->Label)) == 0)
    {
      /*	JCL_ThrowException (env, "javax/sound/midi/MidiUnavailableException",   */
      JCL_ThrowException (env, "java/io/IOException", 
			  "can't create jack client");
      return;
    } 
  
  data->plugin_handle = (data->desc->LADSPA_Plugin->instantiate)(data->desc->LADSPA_Plugin, 
								 jack_get_sample_rate (data->jack_client));
  
  if (jack_set_process_callback (data->jack_client, process, data) != 0)
    {
      JCL_ThrowException (env, "java/io/IOException", 
			  "can't set jack process callback");
      return;
    }
  
  data->jack_left_output_port =
    jack_port_register (data->jack_client, "output_left",
                        JACK_DEFAULT_AUDIO_TYPE, JackPortIsOutput, 0);
  data->jack_right_output_port =
    jack_port_register (data->jack_client, "output_right",
                        JACK_DEFAULT_AUDIO_TYPE, JackPortIsOutput, 0);
  
  /* Count the number of output audio ports.  */
  port_count = 0;
  for (j = 0; j < data->desc->LADSPA_Plugin->PortCount; j++) 
    {
      LADSPA_PortDescriptor pod =
    	data->desc->LADSPA_Plugin->PortDescriptors[j];
      
      if (LADSPA_IS_PORT_AUDIO(pod) && LADSPA_IS_PORT_OUTPUT(pod))
	port_count++;
    }
  printf ("LADSPA output ports = %d\n", port_count);
  
  /* Create buffers for each port.  */
  for (j = 0; j < data->desc->LADSPA_Plugin->PortCount; j++) 
    {  
      LADSPA_PortDescriptor pod =
	data->desc->LADSPA_Plugin->PortDescriptors[j];
      if (LADSPA_IS_PORT_AUDIO(pod) && LADSPA_IS_PORT_OUTPUT(pod))
  	{
	  data->left_buffer = 
	    (float *) calloc(jack_get_buffer_size(data->jack_client), 
			     sizeof(float));
	  (data->desc->LADSPA_Plugin->connect_port)(data->plugin_handle, j, 
						    data->left_buffer);
  	}
      else 
	if (LADSPA_IS_PORT_CONTROL(pod) && LADSPA_IS_PORT_INPUT(pod))
	  {
	    (data->desc->LADSPA_Plugin->connect_port)
	      (data->plugin_handle, j, &mctrl);
	  }
    }

  (data->desc->LADSPA_Plugin->activate)(data->plugin_handle);

  if (jack_activate (data->jack_client))
    JCL_ThrowException (env, "java/io/IOException", 
			"can't activate jack client"); 
}

/**
 * This is called when we receive a new MIDI NOTE ON message.  Simply
 * stick an appropriate event in the event buffer.  This will get
 * processed in the jack callback function.
 */
JNIEXPORT void JNICALL 
Java_gnu_javax_sound_midi_dssi_DSSISynthesizer_noteOn_1 
  (JNIEnv *env __attribute__((unused)), jclass clazz __attribute__((unused)), 
   jlong handle, jint channel, jint note, jint velocity)
{
  dssi_data *data = (dssi_data *) (long) handle;

  /* Insert this event in the event buffer.  */
  snd_seq_event_t *ev = & data->midiEventBuffer[data->midiEventWriteIndex];

  ev->type = SND_SEQ_EVENT_NOTEON;
  ev->data.control.channel = channel;
  ev->data.note.note = note;
  ev->data.note.velocity = velocity;

  data->midiEventWriteIndex = 
    (data->midiEventWriteIndex + 1) % EVENT_BUFFER_SIZE;
}

/**
 * This is called when we receive a new MIDI NOTE OFF message.  Simply
 * stick an appropriate event in the event buffer.  This will get
 * processed in the jack callback function.
 */
JNIEXPORT void JNICALL 
Java_gnu_javax_sound_midi_dssi_DSSISynthesizer_noteOff_1 
  (JNIEnv *env __attribute__((unused)), 
   jclass clazz __attribute__((unused)), 
   jlong handle, jint channel, jint note, jint velocity)
{
  dssi_data *data = (dssi_data *) (long) handle;

  /* Insert this event in the event buffer.  */
  snd_seq_event_t *ev = & data->midiEventBuffer[data->midiEventWriteIndex];

  ev->type = SND_SEQ_EVENT_NOTEOFF;
  ev->data.control.channel = channel;
  ev->data.note.note = note;
  ev->data.note.velocity = velocity;

  data->midiEventWriteIndex = 
    (data->midiEventWriteIndex + 1) % EVENT_BUFFER_SIZE;
}

JNIEXPORT void JNICALL 
Java_gnu_javax_sound_midi_dssi_DSSISynthesizer_setPolyPressure_1 
  (JNIEnv *env __attribute__((unused)), jclass clazz __attribute__((unused)), 
   jlong handle __attribute__((unused)), jint channel __attribute__((unused)), 
   jint note __attribute__((unused)), jint velocity __attribute__((unused)))
{
}

JNIEXPORT jint JNICALL 
Java_gnu_javax_sound_midi_dssi_DSSISynthesizer_getPolyPressure_1 
  (JNIEnv *env __attribute__((unused)), jclass clazz __attribute__((unused)), 
   jlong handle __attribute__((unused)), jint channel __attribute__((unused)), 
   jint note __attribute__((unused)))
{
  return 0;
}

JNIEXPORT void JNICALL 
Java_gnu_javax_sound_midi_dssi_DSSISynthesizer_close_1 
  (JNIEnv *env __attribute__((unused)), jclass clazz __attribute__((unused)), 
   jlong handle __attribute__((unused)))
{
}

