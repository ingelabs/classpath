/* HaikuGlyphVector.java -- a glyph vector of a Haiku font
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
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A glyph vector of a {@link HaikuFontPeer}. The glyph codes are the
 * characters; the positions come from the BFont advances.
 *
 * HaikuGraphics2D draws a glyph vector whose positions were not changed
 * by asking the app_server to draw its text. Outlines (for shapes, other
 * transforms and paints) come from BFont::GetGlyphShapes.
 */
final class HaikuGlyphVector
  extends GlyphVector
{

  final Font font;

  final HaikuFontPeer peer;

  final String text;

  private final FontRenderContext frc;

  /** x, y of each glyph and the end position: 2 * (n + 1) values. */
  private final float[] positions;

  /** True after setGlyphPosition() or setGlyphTransform(). */
  boolean customLayout;

  private Shape outline;

  HaikuGlyphVector(Font font, HaikuFontPeer peer, FontRenderContext frc,
                   String text)
  {
    this.font = font;
    this.peer = peer;
    this.frc = frc;
    this.text = text;
    positions = new float[2 * (text.length() + 1)];
    performDefaultLayout();
  }

  public void performDefaultLayout()
  {
    float[] adv = peer.advances(text, font.getSize2D());
    float x = 0;
    for (int i = 0; i < adv.length; i++)
      {
        positions[2 * i] = x;
        positions[2 * i + 1] = 0;
        x += adv[i];
      }
    positions[2 * adv.length] = x;
    positions[2 * adv.length + 1] = 0;
    customLayout = false;
    outline = null;
  }

  public boolean equals(GlyphVector gv)
  {
    if (! (gv instanceof HaikuGlyphVector))
      return false;
    HaikuGlyphVector o = (HaikuGlyphVector) gv;
    return o.font.equals(font) && o.text.equals(text)
           && java.util.Arrays.equals(o.positions, positions);
  }

  public Font getFont()
  {
    return font;
  }

  public FontRenderContext getFontRenderContext()
  {
    return frc;
  }

  public int getNumGlyphs()
  {
    return text.length();
  }

  public int getGlyphCode(int i)
  {
    return text.charAt(i);
  }

  public int[] getGlyphCodes(int begin, int count, int[] codes)
  {
    if (codes == null)
      codes = new int[count];
    for (int i = 0; i < count; i++)
      codes[i] = text.charAt(begin + i);
    return codes;
  }

  public Point2D getGlyphPosition(int i)
  {
    return new Point2D.Float(positions[2 * i], positions[2 * i + 1]);
  }

  public float[] getGlyphPositions(int begin, int count, float[] out)
  {
    if (out == null)
      out = new float[2 * count];
    System.arraycopy(positions, 2 * begin, out, 0, 2 * count);
    return out;
  }

  public void setGlyphPosition(int i, Point2D p)
  {
    positions[2 * i] = (float) p.getX();
    positions[2 * i + 1] = (float) p.getY();
    customLayout = true;
    outline = null;
  }

  public AffineTransform getGlyphTransform(int i)
  {
    return null;
  }

  public void setGlyphTransform(int i, AffineTransform t)
  {
    // Glyphs are not transformed one by one.
  }

  public GlyphJustificationInfo getGlyphJustificationInfo(int i)
  {
    return null;
  }

  private float advance(int i)
  {
    return positions[2 * (i + 1)] - positions[2 * i];
  }

  public Rectangle2D getLogicalBounds()
  {
    HaikuFontPeer.Metrics m = peer.metrics(font.getSize2D());
    return new Rectangle2D.Float(0, -m.ascent, positions[2 * text.length()],
                                 m.ascent + m.descent + m.leading);
  }

  public Rectangle2D getVisualBounds()
  {
    Rectangle2D r = getOutline().getBounds2D();
    if (r.isEmpty())
      return getLogicalBounds();
    return r;
  }

  public Shape getGlyphLogicalBounds(int i)
  {
    HaikuFontPeer.Metrics m = peer.metrics(font.getSize2D());
    return new Rectangle2D.Float(positions[2 * i],
                                 positions[2 * i + 1] - m.ascent, advance(i),
                                 m.ascent + m.descent + m.leading);
  }

  public Shape getGlyphVisualBounds(int i)
  {
    return getGlyphOutline(i).getBounds2D();
  }

  public GlyphMetrics getGlyphMetrics(int i)
  {
    Rectangle2D b = getGlyphOutline(i).getBounds2D();
    b.setRect(b.getX() - positions[2 * i], b.getY() - positions[2 * i + 1],
              b.getWidth(), b.getHeight());
    char c = text.charAt(i);
    byte type = Character.isWhitespace(c) ? GlyphMetrics.WHITESPACE
                                          : GlyphMetrics.STANDARD;
    return new GlyphMetrics(advance(i), b, type);
  }

  /** The outline of all glyphs at their positions. */
  public Shape getOutline()
  {
    if (outline == null)
      outline = path(text, 0, text.length());
    return outline;
  }

  public Shape getOutline(float x, float y)
  {
    GeneralPath p = new GeneralPath(getOutline());
    p.transform(AffineTransform.getTranslateInstance(x, y));
    return p;
  }

  public Shape getGlyphOutline(int i)
  {
    return path(text, i, i + 1);
  }

  /** The outline of glyphs begin..end at their positions. */
  private Shape path(String t, int begin, int end)
  {
    String part = t.substring(begin, end);
    float[] xs = new float[part.length()];
    for (int i = 0; i < xs.length; i++)
      xs[i] = positions[2 * (begin + i)];
    float[] p = HaikuNative.fontOutline(peer.handle, font.getSize2D(), part,
                                        xs);
    GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
    int k = 0;
    while (k < p.length)
      {
        switch ((int) p[k++])
          {
          case 0:
            path.moveTo(p[k], p[k + 1]);
            k += 2;
            break;
          case 1:
            path.lineTo(p[k], p[k + 1]);
            k += 2;
            break;
          case 2:
            path.curveTo(p[k], p[k + 1], p[k + 2], p[k + 3], p[k + 4],
                         p[k + 5]);
            k += 6;
            break;
          default:
            path.closePath();
            break;
          }
      }
    AffineTransform ft = font.getTransform();
    if (ft != null && ! ft.isIdentity())
      path.transform(ft);
    return path;
  }
}
