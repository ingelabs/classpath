/* HaikuLine.java -- base class of the Haiku sound lines
   Copyright (C) 2026 Free Software Foundation, Inc.

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

package gnu.javax.sound.sampled.haiku;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/**
 * Common parts of the Haiku clip and source data line: the native
 * player, the open and running state, line events and the gain control.
 */
abstract class HaikuLine
  implements DataLine
{

  final HaikuMixer mixer;

  /** The native player, 0 when the line is closed. */
  long handle;

  AudioFormat format;

  boolean running;

  private final ArrayList<LineListener> listeners =
    new ArrayList<LineListener>();

  private final Gain gain = new Gain();

  HaikuLine(HaikuMixer mixer)
  {
    this.mixer = mixer;
  }

  /** Opens the native player for format f. */
  void openNative(AudioFormat f)
    throws LineUnavailableException
  {
    if (! Pcm.isSupported(f))
      throw new IllegalArgumentException("unsupported format: " + f);
    if (! HaikuAudio.ensureInitialized())
      throw new LineUnavailableException("no Haiku sound library");
    float rate = f.getSampleRate() > 0 ? f.getSampleRate() : 44100;
    long h = HaikuAudio.open(rate, Pcm.channels(f));
    if (h == 0)
      throw new LineUnavailableException("the media server refused the line");
    synchronized (this)
      {
        handle = h;
        format = f;
      }
    HaikuAudio.setGain(h, gain.linear());
  }

  public void close()
  {
    long h;
    synchronized (this)
      {
        if (handle == 0)
          return;
        if (running)
          {
            HaikuAudio.pause(handle);
            running = false;
          }
        h = handle;
        handle = 0;
      }
    HaikuAudio.close(h);
    fire(LineEvent.Type.CLOSE);
  }

  public synchronized boolean isOpen()
  {
    return handle != 0;
  }

  public synchronized boolean isRunning()
  {
    return running;
  }

  public synchronized boolean isActive()
  {
    return running && handle != 0 && HaikuAudio.isPlaying(handle);
  }

  public synchronized AudioFormat getFormat()
  {
    return format != null ? format : Pcm.FORMATS[1];
  }

  public float getLevel()
  {
    return AudioSystem.NOT_SPECIFIED;
  }

  public int getFramePosition()
  {
    return (int) getLongFramePosition();
  }

  public synchronized long getLongFramePosition()
  {
    return handle != 0 ? HaikuAudio.position(handle) : 0;
  }

  public long getMicrosecondPosition()
  {
    float rate = getFormat().getSampleRate();
    return rate > 0 ? (long) (getLongFramePosition() * 1000000.0 / rate) : 0;
  }

  // Line events.

  public void addLineListener(LineListener l)
  {
    synchronized (listeners)
      {
        listeners.add(l);
      }
  }

  public void removeLineListener(LineListener l)
  {
    synchronized (listeners)
      {
        listeners.remove(l);
      }
  }

  /** Sends a line event. Do not call with the line locked. */
  void fire(LineEvent.Type type)
  {
    LineListener[] ls;
    synchronized (listeners)
      {
        ls = listeners.toArray(new LineListener[listeners.size()]);
      }
    LineEvent ev = new LineEvent(this, type, getLongFramePosition());
    for (int i = 0; i < ls.length; i++)
      ls[i].update(ev);
  }

  // Controls.

  public Control[] getControls()
  {
    return new Control[] { gain };
  }

  public boolean isControlSupported(Control.Type type)
  {
    return FloatControl.Type.MASTER_GAIN.equals(type);
  }

  public Control getControl(Control.Type type)
  {
    if (! isControlSupported(type))
      throw new IllegalArgumentException("unsupported control: " + type);
    return gain;
  }

  /** The master gain in decibels. */
  private final class Gain
    extends FloatControl
  {
    Gain()
    {
      super(FloatControl.Type.MASTER_GAIN, -80f, 6f, 0.1f, -1, 0f, "dB");
    }

    float linear()
    {
      return (float) Math.pow(10.0, getValue() / 20.0);
    }

    public void setValue(float value)
    {
      super.setValue(value);
      long h;
      synchronized (HaikuLine.this)
        {
          h = handle;
        }
      if (h != 0)
        HaikuAudio.setGain(h, linear());
    }
  }
}
