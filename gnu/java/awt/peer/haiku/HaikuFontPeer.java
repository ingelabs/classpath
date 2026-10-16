/* HaikuFontPeer.java -- Java fonts backed by Haiku's BFont
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

import gnu.java.awt.peer.ClasspathFontPeer;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A font backed by a Haiku BFont. Haiku measures the text (BFont
 * escapements) and the app_server draws it, so Java text looks like the
 * rest of Haiku: hinted, anti-aliased, and with Haiku's font fallback
 * for scripts that the font lacks.
 *
 * The Java logical fonts map to Haiku's fonts: Dialog and SansSerif to
 * the plain font, Monospaced and DialogInput to the fixed font, Serif to
 * a serif family. Other names are Haiku font families.
 */
public class HaikuFontPeer
  extends ClasspathFontPeer
{

  // Face flags (Font.h).
  private static final int B_ITALIC_FACE = 0x01;
  private static final int B_BOLD_FACE = 0x20;
  private static final int B_REGULAR_FACE = 0x40;

  private static final String[] SERIF_FAMILIES = {
    "Noto Serif", "DejaVu Serif", "Liberation Serif", "Noto Serif Display"
  };

  /** The native BFont. */
  final long handle;

  /** The Haiku family name. */
  final String haikuFamily;

  private final HashMap<Float,Metrics> metrics = new HashMap<Float,Metrics>();

  private static String[] families;

  HaikuFontPeer(String name, Map attrs)
  {
    super(name, attrs);
    HaikuNative.ensureInitialized();
    String fam = resolveFamily(name);
    int face = 0;
    if ((style & Font.BOLD) != 0)
      face |= B_BOLD_FACE;
    if ((style & Font.ITALIC) != 0)
      face |= B_ITALIC_FACE;
    if (face == 0)
      face = B_REGULAR_FACE;
    long h = HaikuNative.fontOpen(fam, face);
    if (h == 0)
      {
        fam = "";
        h = HaikuNative.fontOpen("", face);
      }
    handle = h;
    haikuFamily = fam.length() > 0 ? fam : HaikuNative.fontFamilies(1)[0];
  }

  /** The installed Haiku font families. */
  static synchronized String[] haikuFamilies()
  {
    if (families == null)
      {
        HaikuNative.ensureInitialized();
        families = HaikuNative.fontFamilies(0);
      }
    return families;
  }

  private static String findFamily(String name)
  {
    String[] f = haikuFamilies();
    for (int i = 0; i < f.length; i++)
      if (f[i].equalsIgnoreCase(name))
        return f[i];
    return null;
  }

  /** The Haiku family for a Java font name; "" is the plain font. */
  private static String resolveFamily(String name)
  {
    String n = name == null ? "" : name.toLowerCase(Locale.ENGLISH);
    if (n.equals("dialog") || n.equals("sansserif") || n.equals("default")
        || n.equals("helvetica") || n.equals("arial") || n.length() == 0)
      return "";
    if (n.equals("monospaced") || n.equals("dialoginput")
        || n.equals("courier") || n.equals("courier new"))
      return HaikuNative.fontFamilies(2)[0];
    if (n.equals("serif") || n.equals("timesroman") || n.equals("times")
        || n.equals("times new roman"))
      {
        for (int i = 0; i < SERIF_FAMILIES.length; i++)
          if (findFamily(SERIF_FAMILIES[i]) != null)
            return SERIF_FAMILIES[i];
        return "";
      }
    String f = findFamily(name);
    return f != null ? f : "";
  }

  /** Metrics at one size: ascent, descent, leading, max advance. */
  static final class Metrics
  {
    final float ascent, descent, leading, maxAdvance;
    /** Advances of the characters 0..255. */
    final float[] latin = new float[256];

    Metrics(long font, float size)
    {
      float[] m = new float[4];
      HaikuNative.fontMetrics(font, size, m);
      ascent = m[0];
      descent = m[1];
      leading = m[2];
      maxAdvance = m[3];
      char[] cs = new char[256];
      for (int i = 0; i < 256; i++)
        cs[i] = i < 32 || (i >= 0x7f && i < 0xa0) ? ' ' : (char) i;
      HaikuNative.fontAdvances(font, size, new String(cs), latin);
      for (int i = 0; i < 256; i++)
        if (i < 32 || (i >= 0x7f && i < 0xa0))
          latin[i] = 0;
    }
  }

  Metrics metrics(float size)
  {
    synchronized (metrics)
      {
        Float key = new Float(size);
        Metrics m = metrics.get(key);
        if (m == null)
          {
            m = new Metrics(handle, size);
            metrics.put(key, m);
          }
        return m;
      }
  }

  /** The advance of each char of text. */
  float[] advances(String text, float size)
  {
    int n = text.length();
    float[] a = new float[n];
    boolean latin = true;
    for (int i = 0; i < n && latin; i++)
      latin = text.charAt(i) < 256;
    if (latin)
      {
        Metrics m = metrics(size);
        for (int i = 0; i < n; i++)
          a[i] = m.latin[text.charAt(i)];
      }
    else if (n > 0)
      HaikuNative.fontAdvances(handle, size, text, a);
    return a;
  }

  float width(String text, float size)
  {
    float[] a = advances(text, size);
    float w = 0;
    for (int i = 0; i < a.length; i++)
      w += a[i];
    return w;
  }

  static String text(CharacterIterator ci, int start, int limit)
  {
    StringBuilder b = new StringBuilder(Math.max(0, limit - start));
    for (char c = ci.setIndex(start); ci.getIndex() < limit; c = ci.next())
      b.append(c);
    return b.toString();
  }

  // ClasspathFontPeer.

  public boolean canDisplay(Font font, int c)
  {
    boolean[] has = new boolean[1];
    HaikuNative.fontHasGlyphs(handle, String.valueOf((char) c), has);
    return has[0];
  }

  public int canDisplayUpTo(Font font, CharacterIterator ci, int start,
                            int limit)
  {
    String t = text(ci, start, limit);
    boolean[] has = new boolean[t.length()];
    HaikuNative.fontHasGlyphs(handle, t, has);
    for (int i = 0; i < has.length; i++)
      if (! has[i])
        return start + i;
    return -1;
  }

  public String getSubFamilyName(Font font, Locale locale)
  {
    if ((style & Font.BOLD) != 0 && (style & Font.ITALIC) != 0)
      return "Bold Italic";
    if ((style & Font.BOLD) != 0)
      return "Bold";
    if ((style & Font.ITALIC) != 0)
      return "Italic";
    return "Regular";
  }

  public String getPostScriptName(Font font)
  {
    return haikuFamily.replace(" ", "") + "-"
           + getSubFamilyName(font, null).replace(" ", "");
  }

  public int getNumGlyphs(Font font)
  {
    return 0x10000;
  }

  public int getMissingGlyphCode(Font font)
  {
    return 0;
  }

  public byte getBaselineFor(Font font, char c)
  {
    return Font.ROMAN_BASELINE;
  }

  public String getGlyphName(Font font, int glyphIndex)
  {
    String hex = Integer.toHexString(glyphIndex).toUpperCase();
    while (hex.length() < 4)
      hex = "0" + hex;
    return "uni" + hex;
  }

  public GlyphVector createGlyphVector(Font font, FontRenderContext frc,
                                       CharacterIterator ci)
  {
    return new HaikuGlyphVector(font, this, frc,
                                text(ci, ci.getBeginIndex(), ci.getEndIndex()));
  }

  /** Glyph codes are the characters. */
  public GlyphVector createGlyphVector(Font font, FontRenderContext frc,
                                       int[] glyphCodes)
  {
    char[] cs = new char[glyphCodes.length];
    for (int i = 0; i < cs.length; i++)
      cs[i] = (char) glyphCodes[i];
    return new HaikuGlyphVector(font, this, frc, new String(cs));
  }

  public GlyphVector layoutGlyphVector(Font font, FontRenderContext frc,
                                       char[] chars, int start, int limit,
                                       int flags)
  {
    return new HaikuGlyphVector(font, this, frc,
                                new String(chars, start, limit - start));
  }

  public FontMetrics getFontMetrics(Font font)
  {
    return new HaikuFontMetrics(font, this);
  }

  public boolean hasUniformLineMetrics(Font font)
  {
    return true;
  }

  public LineMetrics getLineMetrics(Font font, CharacterIterator ci,
                                    int begin, int limit,
                                    FontRenderContext frc)
  {
    return new HaikuLineMetrics(metrics(font.getSize2D()), limit - begin);
  }

  public Rectangle2D getMaxCharBounds(Font font, FontRenderContext frc)
  {
    Metrics m = metrics(font.getSize2D());
    return new Rectangle2D.Float(0, -m.ascent, m.maxAdvance,
                                 m.ascent + m.descent + m.leading);
  }

  /** Line metrics from the BFont height. */
  static final class HaikuLineMetrics
    extends LineMetrics
  {
    private final Metrics m;
    private final int chars;

    HaikuLineMetrics(Metrics m, int chars)
    {
      this.m = m;
      this.chars = chars;
    }

    public float getAscent()
    {
      return m.ascent;
    }

    public int getBaselineIndex()
    {
      return Font.ROMAN_BASELINE;
    }

    public float[] getBaselineOffsets()
    {
      return new float[] { 0, (m.descent - m.ascent) / 2, -m.ascent };
    }

    public float getDescent()
    {
      return m.descent;
    }

    public float getHeight()
    {
      return m.ascent + m.descent + m.leading;
    }

    public float getLeading()
    {
      return m.leading;
    }

    public int getNumChars()
    {
      return chars;
    }

    public float getStrikethroughOffset()
    {
      return -m.ascent / 3;
    }

    public float getStrikethroughThickness()
    {
      return Math.max(1, m.ascent / 12);
    }

    public float getUnderlineOffset()
    {
      return Math.max(1, m.descent / 2);
    }

    public float getUnderlineThickness()
    {
      return Math.max(1, m.ascent / 12);
    }
  }
}
