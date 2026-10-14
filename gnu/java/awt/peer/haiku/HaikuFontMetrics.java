/* HaikuFontMetrics.java -- font metrics from Haiku's BFont
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

package gnu.java.awt.peer.haiku;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

/**
 * FontMetrics of a {@link HaikuFontPeer}. Widths are the BFont advances
 * that the app_server uses to draw the text.
 */
final class HaikuFontMetrics
  extends FontMetrics
{

  private final HaikuFontPeer peer;

  private final HaikuFontPeer.Metrics m;

  HaikuFontMetrics(Font font, HaikuFontPeer peer)
  {
    super(font);
    this.peer = peer;
    m = peer.metrics(font.getSize2D());
  }

  public int getAscent()
  {
    return (int) Math.ceil(m.ascent);
  }

  public int getDescent()
  {
    return (int) Math.ceil(m.descent);
  }

  public int getLeading()
  {
    return Math.round(m.leading);
  }

  public int getMaxAscent()
  {
    return getAscent();
  }

  public int getMaxDescent()
  {
    return getDescent();
  }

  public int getMaxAdvance()
  {
    return (int) Math.ceil(m.maxAdvance);
  }

  public int charWidth(char c)
  {
    if (c < 256)
      return Math.round(m.latin[c]);
    return Math.round(peer.width(String.valueOf(c), font.getSize2D()));
  }

  public int charWidth(int codePoint)
  {
    if (codePoint < 0x10000)
      return charWidth((char) codePoint);
    return Math.round(peer.width(new String(Character.toChars(codePoint)),
                                 font.getSize2D()));
  }

  public int stringWidth(String s)
  {
    return Math.round(peer.width(s, font.getSize2D()));
  }

  public int charsWidth(char[] data, int off, int len)
  {
    return stringWidth(new String(data, off, len));
  }

  public int[] getWidths()
  {
    int[] w = new int[256];
    for (int i = 0; i < 256; i++)
      w[i] = Math.round(m.latin[i]);
    return w;
  }

  public LineMetrics getLineMetrics(String s, Graphics g)
  {
    return new HaikuFontPeer.HaikuLineMetrics(m, s.length());
  }

  public Rectangle2D getStringBounds(String s, Graphics g)
  {
    return new Rectangle2D.Float(0, -m.ascent, peer.width(s, font.getSize2D()),
                                 m.ascent + m.descent + m.leading);
  }

  public boolean hasUniformLineMetrics()
  {
    return true;
  }
}
