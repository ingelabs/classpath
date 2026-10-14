/* HaikuSourceDataLine.java -- a SourceDataLine for the Haiku media server
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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * A source data line. Written data goes into a native ring buffer that
 * the player reads.
 */
final class HaikuSourceDataLine
  extends HaikuLine
  implements SourceDataLine
{

  /** The ring buffer size in frames. */
  private int bufferFrames;

  /** Counts flush() calls, so a blocked write() can return. */
  private int flushes;

  HaikuSourceDataLine(HaikuMixer mixer)
  {
    super(mixer);
  }

  public Info getLineInfo()
  {
    return new DataLine.Info(SourceDataLine.class, Pcm.FORMATS,
                             AudioSystem.NOT_SPECIFIED,
                             AudioSystem.NOT_SPECIFIED);
  }

  public void open()
    throws LineUnavailableException
  {
    open(new AudioFormat(44100, 16, 2, true, false));
  }

  public void open(AudioFormat fmt)
    throws LineUnavailableException
  {
    open(fmt, AudioSystem.NOT_SPECIFIED);
  }

  /** size is the buffer size in bytes; the default is half a second. */
  public void open(AudioFormat fmt, int size)
    throws LineUnavailableException
  {
    if (isOpen())
      throw new IllegalStateException("the line is already open");
    openNative(fmt);
    int fs = Pcm.frameSize(fmt);
    float rate = fmt.getSampleRate() > 0 ? fmt.getSampleRate() : 44100;
    int frames = size > 0 ? size / fs : (int) (rate / 2);
    long h;
    synchronized (this)
      {
        h = handle;
        bufferFrames = Math.max(frames, 256);
      }
    // Make the ring buffer now: data can be written before start().
    HaikuAudio.startStream(h, bufferFrames);
    HaikuAudio.pause(h);
    fire(LineEvent.Type.OPEN);
  }

  /**
   * Writes whole frames. Blocks until all data is in the buffer, or the
   * line is flushed or closed.
   */
  public int write(byte[] buf, int offset, int length)
  {
    AudioFormat f = getFormat();
    int fs = Pcm.frameSize(f);
    if (length % fs != 0)
      throw new IllegalArgumentException("length is not a whole number of frames");
    short[] samples = Pcm.toShorts(f, buf, offset, length);
    int channels = Pcm.channels(f);
    int done = 0;
    int flushesAtStart;
    synchronized (this)
      {
        flushesAtStart = flushes;
      }
    while (done < samples.length)
      {
        long h;
        synchronized (this)
          {
            if (handle == 0 || flushes != flushesAtStart)
              break;
            h = handle;
          }
        done += HaikuAudio.write(h, samples, done, samples.length - done,
                                 250);
      }
    return done / channels * fs;
  }

  public void start()
  {
    synchronized (this)
      {
        if (handle == 0 || running)
          return;
        HaikuAudio.startStream(handle, bufferFrames);
        running = true;
      }
    fire(LineEvent.Type.START);
  }

  public void stop()
  {
    synchronized (this)
      {
        if (handle == 0 || ! running)
          return;
        HaikuAudio.pause(handle);
        running = false;
      }
    fire(LineEvent.Type.STOP);
  }

  /** Waits until all buffered data has played. */
  public void drain()
  {
    while (true)
      {
        long h;
        synchronized (this)
          {
            if (handle == 0 || ! running)
              return;
            h = handle;
          }
        if (HaikuAudio.waitIdle(h, 250))
          return;
      }
  }

  public void flush()
  {
    synchronized (this)
      {
        if (handle == 0)
          return;
        HaikuAudio.flush(handle);
        flushes++;
      }
  }

  public synchronized int available()
  {
    if (handle == 0)
      return 0;
    AudioFormat f = getFormat();
    return HaikuAudio.available(handle) / Pcm.channels(f) * Pcm.frameSize(f);
  }

  public synchronized int getBufferSize()
  {
    return bufferFrames * Pcm.frameSize(getFormat());
  }
}
