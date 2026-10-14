/* HaikuMixer.java -- the Haiku media server as a javax.sound mixer
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

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 * A mixer that plays through the Haiku media server. It has Clip and
 * SourceDataLine source lines and no target lines.
 */
final class HaikuMixer
  implements Mixer
{

  static final Mixer.Info INFO = new HaikuInfo();

  private static final class HaikuInfo
    extends Mixer.Info
  {
    HaikuInfo()
    {
      super("Haiku Media Kit", "GNU Classpath",
            "Plays sound through the Haiku media server", "1.0");
    }
  }

  private static final Line.Info[] NONE = new Line.Info[0];

  public Mixer.Info getMixerInfo()
  {
    return INFO;
  }

  public Line.Info[] getSourceLineInfo()
  {
    return new Line.Info[] {
      new DataLine.Info(Clip.class, Pcm.FORMATS, AudioSystem.NOT_SPECIFIED,
                        AudioSystem.NOT_SPECIFIED),
      new DataLine.Info(SourceDataLine.class, Pcm.FORMATS,
                        AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED)
    };
  }

  public Line.Info[] getSourceLineInfo(Line.Info info)
  {
    ArrayList<Line.Info> l = new ArrayList<Line.Info>();
    Line.Info[] all = getSourceLineInfo();
    for (int i = 0; i < all.length; i++)
      if (lineClass(info) == ((DataLine.Info) all[i]).getLineClass())
        l.add(all[i]);
    return l.toArray(new Line.Info[l.size()]);
  }

  public Line.Info[] getTargetLineInfo()
  {
    return NONE;
  }

  public Line.Info[] getTargetLineInfo(Line.Info info)
  {
    return NONE;
  }

  /**
   * The line class that can serve info: Clip, SourceDataLine, or null.
   */
  private static Class<?> lineClass(Line.Info info)
  {
    Class<?> c = info.getLineClass();
    Class<?> result;
    if (c.isAssignableFrom(SourceDataLine.class))
      result = SourceDataLine.class;
    else if (c.isAssignableFrom(Clip.class))
      result = Clip.class;
    else
      return null;
    if (info instanceof DataLine.Info)
      {
        javax.sound.sampled.AudioFormat[] f =
          ((DataLine.Info) info).getFormats();
        if (f.length > 0)
          {
            boolean ok = false;
            for (int i = 0; i < f.length && ! ok; i++)
              ok = Pcm.isSupported(f[i]);
            if (! ok)
              return null;
          }
      }
    return result;
  }

  public boolean isLineSupported(Line.Info info)
  {
    return lineClass(info) != null;
  }

  public Line getLine(Line.Info info)
    throws LineUnavailableException
  {
    Class<?> c = lineClass(info);
    if (c == Clip.class)
      return new HaikuClip(this);
    if (c == SourceDataLine.class)
      return new HaikuSourceDataLine(this);
    throw new IllegalArgumentException("unsupported line: " + info);
  }

  public int getMaxLines(Line.Info info)
  {
    return isLineSupported(info) ? AudioSystem.NOT_SPECIFIED : 0;
  }

  /**
   * New, closed lines. GNU Classpath's AudioSystem.getClip() takes its
   * clip from here.
   */
  public Line[] getSourceLines()
  {
    return new Line[] { new HaikuClip(this), new HaikuSourceDataLine(this) };
  }

  public Line[] getTargetLines()
  {
    return new Line[0];
  }

  public boolean isSynchronizationSupported(Line[] lines, boolean sync)
  {
    return false;
  }

  public void synchronize(Line[] lines, boolean sync)
  {
    throw new IllegalArgumentException("synchronization is not supported");
  }

  public void unsynchronize(Line[] lines)
  {
    throw new IllegalArgumentException("synchronization is not supported");
  }

  // The mixer itself is always open.

  public Line.Info getLineInfo()
  {
    return new Line.Info(Mixer.class);
  }

  public void open()
  {
  }

  public void close()
  {
  }

  public boolean isOpen()
  {
    return true;
  }

  public Control[] getControls()
  {
    return new Control[0];
  }

  public boolean isControlSupported(Control.Type type)
  {
    return false;
  }

  public Control getControl(Control.Type type)
  {
    throw new IllegalArgumentException("unsupported control: " + type);
  }

  public void addLineListener(LineListener l)
  {
  }

  public void removeLineListener(LineListener l)
  {
  }
}
