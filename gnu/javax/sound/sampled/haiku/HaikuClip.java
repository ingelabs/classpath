/* HaikuClip.java -- a Clip for the Haiku media server
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;

/**
 * A clip. All frames are kept in the native player, which also does the
 * looping, so loops have no gaps.
 */
final class HaikuClip
  extends HaikuLine
  implements Clip
{

  private int frames;

  /** Loop points; loopEnd is the last frame, -1 for the end. */
  private int loopStart, loopEnd = -1;

  /** Counts start()/loop() calls, so old monitor threads stop. */
  private int generation;

  HaikuClip(HaikuMixer mixer)
  {
    super(mixer);
  }

  public Info getLineInfo()
  {
    return new DataLine.Info(Clip.class, Pcm.FORMATS,
                             javax.sound.sampled.AudioSystem.NOT_SPECIFIED,
                             javax.sound.sampled.AudioSystem.NOT_SPECIFIED);
  }

  public void open()
    throws LineUnavailableException
  {
    throw new IllegalArgumentException("a clip needs audio data");
  }

  public void open(AudioInputStream ais)
    throws LineUnavailableException, IOException
  {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buf = new byte[8192];
    int n;
    while ((n = ais.read(buf)) > 0)
      out.write(buf, 0, n);
    byte[] data = out.toByteArray();
    open(ais.getFormat(), data, 0, data.length);
  }

  public void open(AudioFormat fmt, byte[] data, int offset, int len)
    throws LineUnavailableException
  {
    if (isOpen())
      throw new IllegalStateException("the clip is already open");
    openNative(fmt);
    short[] samples = Pcm.toShorts(fmt, data, offset, len);
    long h;
    synchronized (this)
      {
        h = handle;
        frames = samples.length / Pcm.channels(fmt);
        loopStart = 0;
        loopEnd = -1;
      }
    HaikuAudio.loadClip(h, samples);
    fire(LineEvent.Type.OPEN);
  }

  public int getFrameLength()
  {
    return frames;
  }

  public long getMicrosecondLength()
  {
    float rate = getFormat().getSampleRate();
    return rate > 0 ? (long) (frames * 1000000.0 / rate) : 0;
  }

  public synchronized void setFramePosition(int where)
  {
    if (handle != 0)
      HaikuAudio.setPosition(handle, where);
  }

  public void setMicrosecondPosition(long us)
  {
    float rate = getFormat().getSampleRate();
    setFramePosition((int) (us * rate / 1000000.0));
  }

  public synchronized void setLoopPoints(int begin, int end)
  {
    if (end == -1)
      end = frames - 1;
    if (begin < 0 || end >= frames || begin > end)
      throw new IllegalArgumentException("bad loop points");
    loopStart = begin;
    loopEnd = end;
  }

  public void start()
  {
    play(0);
  }

  public void loop(int count)
  {
    play(count == LOOP_CONTINUOUSLY ? -1 : count);
  }

  /** Plays from the current position; loops as in loop(). */
  private void play(int loops)
  {
    final int gen;
    synchronized (this)
      {
        if (handle == 0)
          return;
        int end = (loopEnd < 0 ? frames - 1 : loopEnd) + 1;
        long pos = HaikuAudio.position(handle);
        if (pos >= frames && loops == 0)
          pos = 0; // start() at the end plays again from the start
        HaikuAudio.playClip(handle, pos, loopStart, end, loops);
        running = true;
        gen = ++generation;
      }
    fire(LineEvent.Type.START);
    Thread t = new Thread("Haiku clip")
      {
        public void run()
        {
          monitor(gen);
        }
      };
    t.setDaemon(true);
    t.start();
  }

  /** Sends STOP when the clip reaches its end. */
  private void monitor(int gen)
  {
    while (true)
      {
        long h;
        synchronized (this)
          {
            if (gen != generation || ! running || handle == 0)
              return;
            h = handle;
          }
        if (HaikuAudio.waitIdle(h, 250))
          {
            synchronized (this)
              {
                if (gen != generation || ! running)
                  return;
                running = false;
              }
            fire(LineEvent.Type.STOP);
            return;
          }
      }
  }

  public void stop()
  {
    synchronized (this)
      {
        if (! running || handle == 0)
          return;
        HaikuAudio.pause(handle);
        running = false;
        generation++;
      }
    fire(LineEvent.Type.STOP);
  }

  public void drain()
  {
    while (true)
      {
        long h;
        synchronized (this)
          {
            if (! running || handle == 0)
              return;
            h = handle;
          }
        HaikuAudio.waitIdle(h, 250);
      }
  }

  public void flush()
  {
    // A clip has no queued data to discard.
  }

  public int available()
  {
    return 0;
  }

  public int getBufferSize()
  {
    return frames * Pcm.frameSize(getFormat());
  }
}
