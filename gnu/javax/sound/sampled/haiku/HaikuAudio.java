/* HaikuAudio.java -- native sound output for Haiku
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

/**
 * Native entry points of the Haiku sound lines. They are in libhaikupeer,
 * which also holds the AWT peer, and use its BApplication. Each line owns
 * one BSoundPlayer; all audio is 16-bit signed PCM here.
 */
final class HaikuAudio
{

  private static boolean initialized;

  private HaikuAudio()
  {
  }

  /** Loads libhaikupeer and starts the BApplication. */
  static synchronized boolean ensureInitialized()
  {
    if (initialized)
      return true;
    try
      {
        String explicit =
          System.getProperty("gnu.java.awt.peer.haiku.library");
        if (explicit != null)
          System.load(explicit);
        else
          System.loadLibrary("haikupeer");
        initialized = init();
      }
    catch (UnsatisfiedLinkError e)
      {
        initialized = false;
      }
    return initialized;
  }

  private static native boolean init();

  /** Returns 0 if the media server could not make a player. */
  static native long open(float rate, int channels);

  static native void close(long h);

  static native void loadClip(long h, short[] samples);

  /** loopEnd is exclusive; loops is -1 for ever. */
  static native void playClip(long h, long start, long loopStart,
                              long loopEnd, int loops);

  static native void startStream(long h, int frames);

  static native void pause(long h);

  static native boolean isPlaying(long h);

  static native long position(long h);

  static native void setPosition(long h, long frame);

  static native boolean waitIdle(long h, int timeoutMs);

  /** Returns the number of samples taken. */
  static native int write(long h, short[] samples, int off, int len,
                          int timeoutMs);

  /** Free space, in samples. */
  static native int available(long h);

  static native void flush(long h);

  static native void setGain(long h, float gain);
}
