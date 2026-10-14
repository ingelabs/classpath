/* Pcm.java -- audio format conversion for the Haiku sound lines
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

/**
 * Converts the formats the Haiku lines accept to 16-bit signed PCM:
 * signed and unsigned 8 and 16 bit PCM in both byte orders, mu-law and
 * a-law, with one or two channels.
 */
final class Pcm
{

  private static final short[] ULAW = new short[256];
  private static final short[] ALAW = new short[256];

  static
  {
    for (int i = 0; i < 256; i++)
      {
        int u = ~i & 0xff;
        int t = (((u & 0x0f) << 3) + 0x84) << ((u & 0x70) >> 4);
        ULAW[i] = (short) ((u & 0x80) != 0 ? 0x84 - t : t - 0x84);

        int a = i ^ 0x55;
        int v = (a & 0x0f) << 4;
        int seg = (a & 0x70) >> 4;
        if (seg == 0)
          v += 8;
        else
          {
            v += 0x108;
            if (seg > 1)
              v <<= seg - 1;
          }
        ALAW[i] = (short) ((a & 0x80) != 0 ? v : -v);
      }
  }

  /** All formats the lines accept, at any sample rate. */
  static final AudioFormat[] FORMATS = makeFormats();

  private static AudioFormat[] makeFormats()
  {
    float any = AudioSystem.NOT_SPECIFIED;
    java.util.ArrayList<AudioFormat> l = new java.util.ArrayList<AudioFormat>();
    for (int ch = 1; ch <= 2; ch++)
      {
        l.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, any, 16, ch,
                              2 * ch, any, false));
        l.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, any, 16, ch,
                              2 * ch, any, true));
        l.add(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, any, 8, ch,
                              ch, any, false));
        l.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, any, 8, ch,
                              ch, any, false));
        l.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, any, 16, ch,
                              2 * ch, any, false));
        l.add(new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, any, 16, ch,
                              2 * ch, any, true));
        l.add(new AudioFormat(AudioFormat.Encoding.ULAW, any, 8, ch, ch, any,
                              false));
        l.add(new AudioFormat(AudioFormat.Encoding.ALAW, any, 8, ch, ch, any,
                              false));
      }
    return l.toArray(new AudioFormat[l.size()]);
  }

  private Pcm()
  {
  }

  /** True if the lines can play this format. */
  static boolean isSupported(AudioFormat f)
  {
    AudioFormat.Encoding e = f.getEncoding();
    int bits = f.getSampleSizeInBits();
    int ch = f.getChannels();
    if (ch != AudioSystem.NOT_SPECIFIED && (ch < 1 || ch > 2))
      return false;
    if (e.equals(AudioFormat.Encoding.ULAW)
        || e.equals(AudioFormat.Encoding.ALAW))
      return bits == 8 || bits == AudioSystem.NOT_SPECIFIED;
    if (e.equals(AudioFormat.Encoding.PCM_SIGNED)
        || e.equals(AudioFormat.Encoding.PCM_UNSIGNED))
      return bits == 8 || bits == 16 || bits == AudioSystem.NOT_SPECIFIED;
    return false;
  }

  /** Bytes per frame, with a sensible value if it is not specified. */
  static int frameSize(AudioFormat f)
  {
    if (f.getFrameSize() > 0)
      return f.getFrameSize();
    int bits = f.getSampleSizeInBits() > 0 ? f.getSampleSizeInBits() : 16;
    return (bits / 8) * channels(f);
  }

  static int channels(AudioFormat f)
  {
    return f.getChannels() > 0 ? f.getChannels() : 1;
  }

  /** Converts whole frames of data[off .. off + len) to 16-bit samples. */
  static short[] toShorts(AudioFormat f, byte[] data, int off, int len)
  {
    int fs = frameSize(f);
    int ch = channels(f);
    int frames = len / fs;
    int bytesPerSample = fs / ch;
    short[] out = new short[frames * ch];
    AudioFormat.Encoding e = f.getEncoding();
    boolean signed = e.equals(AudioFormat.Encoding.PCM_SIGNED);
    boolean big = f.isBigEndian();
    int n = frames * ch;
    if (e.equals(AudioFormat.Encoding.ULAW))
      for (int i = 0; i < n; i++)
        out[i] = ULAW[data[off + i] & 0xff];
    else if (e.equals(AudioFormat.Encoding.ALAW))
      for (int i = 0; i < n; i++)
        out[i] = ALAW[data[off + i] & 0xff];
    else if (bytesPerSample == 1)
      for (int i = 0; i < n; i++)
        out[i] = (short) (signed ? data[off + i] << 8
                                 : ((data[off + i] & 0xff) - 128) << 8);
    else
      for (int i = 0; i < n; i++)
        {
          int p = off + i * bytesPerSample;
          int hi = big ? data[p] : data[p + 1];
          int lo = (big ? data[p + 1] : data[p]) & 0xff;
          int v = (hi << 8) | lo;
          out[i] = (short) (signed ? v : v ^ 0x8000);
        }
    return out;
  }
}
