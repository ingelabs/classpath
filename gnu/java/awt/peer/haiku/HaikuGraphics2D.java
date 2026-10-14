/* HaikuGraphics2D.java -- part of the Haiku AWT peer
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

import gnu.java.awt.java2d.AbstractGraphics2D;
import gnu.java.awt.java2d.ScanlineCoverage;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

/**
 * A Graphics2D that renders into a BufferedImage in pure Java.
 *
 * When the image is the backing store of a {@link HaikuWindowPeer}, the
 * area that was drawn is copied to the native window when the graphics
 * is disposed (or {@link #flush()} is called).
 *
 * Images of type INT_RGB / INT_ARGB / INT_ARGB_PRE get fast paths for
 * rectangle fills, image copies and copyArea; everything else goes
 * through AbstractGraphics2D's generic rasterizer.
 */
public class HaikuGraphics2D
  extends AbstractGraphics2D
{

  private final BufferedImage image;

  private final WritableRaster raster;

  private final ColorModel colorModel;

  /** The window to flush to, or null for an off-screen image. */
  private final HaikuWindowPeer target;

  /** Direct pixel access, or null if the image has no simple int layout. */
  private int[] pixels;

  private int scan;

  /** True if the pixels carry an alpha channel. */
  private boolean hasAlpha;

  /** Area that was drawn since the last flush, in device coordinates. */
  private Rectangle dirty;

  private boolean disposed;

  /** The XOR colour, or null in paint mode. */
  private Color xorColor;

  HaikuGraphics2D(BufferedImage img, HaikuWindowPeer target)
  {
    super();
    image = img;
    raster = img.getRaster();
    colorModel = img.getColorModel();
    this.target = target;
    int type = img.getType();
    DataBuffer db = raster.getDataBuffer();
    if ((type == BufferedImage.TYPE_INT_RGB
         || type == BufferedImage.TYPE_INT_ARGB
         || type == BufferedImage.TYPE_INT_ARGB_PRE)
        && db instanceof DataBufferInt
        && db.getNumBanks() == 1 && db.getOffset() == 0
        && raster.getSampleModel() instanceof SinglePixelPackedSampleModel
        && raster.getMinX() == 0 && raster.getMinY() == 0
        && raster.getSampleModelTranslateX() == 0
        && raster.getSampleModelTranslateY() == 0)
      {
        pixels = ((DataBufferInt) db).getData();
        scan = ((SinglePixelPackedSampleModel) raster.getSampleModel())
               .getScanlineStride();
        hasAlpha = type != BufferedImage.TYPE_INT_RGB;
      }
    init();
  }

  BufferedImage getImage()
  {
    return image;
  }

  protected ColorModel getColorModel()
  {
    return colorModel;
  }

  protected WritableRaster getDestinationRaster()
  {
    return raster;
  }

  protected Rectangle getDeviceBounds()
  {
    return new Rectangle(0, 0, raster.getWidth(), raster.getHeight());
  }

  public GraphicsConfiguration getDeviceConfiguration()
  {
    return GraphicsEnvironment.getLocalGraphicsEnvironment()
           .getDefaultScreenDevice().getDefaultConfiguration();
  }

  public void renderScanline(int y, ScanlineCoverage c)
  {
    if (y < 0 || y >= raster.getHeight())
      return;
    Paint p = getPaint();
    if (pixels != null && p instanceof Color && isPlainSrcOver())
      {
        // Blend the colour with the coverage directly. The generic code
        // in AbstractGraphics2D needs an alpha raster in the paint
        // context, which opaque colours do not have.
        Color col = (Color) p;
        int rgb = col.getRGB() & 0xFFFFFF;
        int alpha = col.getAlpha();
        int xor = xorBits(col);
        int maxCoverage = c.getMaxCoverage();
        int width = raster.getWidth();
        int off = y * scan;
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        ScanlineCoverage.Iterator iter = c.iterate();
        while (iter.hasNext())
          {
            ScanlineCoverage.Range range = iter.next();
            int coverage = range.getCoverage();
            if (coverage <= 0)
              continue;
            int x0 = Math.max(range.getXPos(), 0);
            int x1 = Math.min(range.getXPosEnd(), width);
            if (x0 >= x1)
              continue;
            int a = coverage >= maxCoverage ? alpha
                                            : alpha * coverage / maxCoverage;
            if (xor != 0)
              {
                // XOR has no partial coverage; use the pixels that are
                // at least half covered.
                if (2 * coverage >= maxCoverage)
                  for (int x = x0; x < x1; x++)
                    pixels[off + x] ^= xor;
              }
            else if (a >= 255)
              java.util.Arrays.fill(pixels, off + x0, off + x1,
                                    0xFF000000 | rgb);
            else if (a > 0)
              {
                int s = (a << 24) | rgb;
                for (int x = x0; x < x1; x++)
                  pixels[off + x] = blend(s, pixels[off + x]);
              }
            minX = Math.min(minX, x0);
            maxX = Math.max(maxX, x1);
          }
        if (minX < maxX)
          markDirty(minX, y, maxX - minX, 1);
        return;
      }
    if (pixels != null && isPlainSrcOver())
      {
        renderPaintScanline(y, c);
        return;
      }
    super.renderScanline(y, c);
  }

  /**
   * Renders one scanline with an arbitrary Paint (gradients, textures)
   * by asking its PaintContext for the source pixels.
   */
  private void renderPaintScanline(int y, ScanlineCoverage c)
  {
    int width = raster.getWidth();
    int x0 = Math.max(c.getMinX(), 0);
    int x1 = Math.min(c.getMaxX(), width);
    if (x0 >= x1)
      return;
    PaintContext pCtx = getPaintContext();
    Raster src = pCtx.getRaster(x0, y, x1 - x0, 1);
    ColorModel cm = pCtx.getColorModel();
    int maxCoverage = c.getMaxCoverage();
    int off = y * scan;
    int sx0 = src.getMinX();
    int sy0 = src.getMinY();
    Object data = null;
    ScanlineCoverage.Iterator iter = c.iterate();
    while (iter.hasNext())
      {
        ScanlineCoverage.Range range = iter.next();
        int coverage = range.getCoverage();
        if (coverage <= 0)
          continue;
        int rx0 = Math.max(range.getXPos(), x0);
        int rx1 = Math.min(range.getXPosEnd(), x1);
        for (int x = rx0; x < rx1; x++)
          {
            data = src.getDataElements(sx0 + x - x0, sy0, data);
            int argb = cm.getRGB(data);
            int a = argb >>> 24;
            if (coverage < maxCoverage)
              a = a * coverage / maxCoverage;
            pixels[off + x] = blend((a << 24) | (argb & 0xFFFFFF),
                                    pixels[off + x]);
          }
      }
    markDirty(x0, y, x1 - x0, 1);
  }

  /** Cached paint context for renderPaintScanline(). */
  private PaintContext paintCtx;
  private Paint paintCtxPaint;
  private java.awt.geom.AffineTransform paintCtxTransform;

  private PaintContext getPaintContext()
  {
    Paint p = getPaint();
    if (paintCtx == null || paintCtxPaint != p
        || ! transform.equals(paintCtxTransform))
      {
        paintCtx = p.createContext(colorModel, getDeviceBounds(),
                                   getDeviceBounds(), transform,
                                   getRenderingHints());
        paintCtxPaint = p;
        paintCtxTransform = new java.awt.geom.AffineTransform(transform);
      }
    return paintCtx;
  }

  protected void updateRaster(Raster r, int x, int y, int w, int h)
  {
    markDirty(x, y, w, h);
  }

  private void markDirty(int x, int y, int w, int h)
  {
    if (w <= 0 || h <= 0)
      return;
    if (dirty == null)
      dirty = new Rectangle(x, y, w, h);
    else
      dirty.add(new Rectangle(x, y, w, h));
  }

  /**
   * Returns the current clip in device coordinates, intersected with the
   * device bounds. Only valid when the transform is a pure translation.
   */
  private Rectangle deviceClip()
  {
    Rectangle bounds = getDeviceBounds();
    Rectangle c = getClipBounds();
    if (c == null)
      return bounds;
    c.translate((int) transform.getTranslateX(),
                (int) transform.getTranslateY());
    return c.intersection(bounds);
  }

  private boolean isSimpleTransform()
  {
    int t = transform.getType();
    return t == java.awt.geom.AffineTransform.TYPE_IDENTITY
           || t == java.awt.geom.AffineTransform.TYPE_TRANSLATION;
  }

  private boolean isPlainSrcOver()
  {
    return getComposite() == AlphaComposite.SrcOver;
  }

  /**
   * The bits to XOR into a pixel for colour c, or 0 in paint mode.
   * The result is never 0 in XOR mode for a colour that differs from the
   * XOR colour; drawing in the XOR colour itself changes nothing anyway.
   */
  private int xorBits(Color c)
  {
    if (xorColor == null || c == null)
      return 0;
    return (c.getRGB() ^ xorColor.getRGB()) & 0xFFFFFF;
  }

  public void setPaintMode()
  {
    xorColor = null;
  }

  public void setXORMode(Color c)
  {
    xorColor = c;
  }

  /** Fills a device rectangle with a color, honouring the clip. */
  private boolean fastFill(int x, int y, int w, int h, Color c)
  {
    return fastFill(x, y, w, h, c, xorBits(c));
  }

  private boolean fastFill(int x, int y, int w, int h, Color c, int xor)
  {
    if (pixels == null || c == null || c.getAlpha() != 255
        || ! isPlainSrcOver() || ! isSimpleTransform())
      return false;
    Rectangle r = new Rectangle(x, y, w, h).intersection(deviceClip());
    if (r.width <= 0 || r.height <= 0)
      return true;
    int rgb = c.getRGB() | 0xFF000000;
    int[] p = pixels;
    for (int row = r.y; row < r.y + r.height; row++)
      {
        int off = row * scan + r.x;
        if (xor != 0)
          for (int i = off; i < off + r.width; i++)
            p[i] ^= xor;
        else
          java.util.Arrays.fill(p, off, off + r.width, rgb);
      }
    markDirty(r.x, r.y, r.width, r.height);
    return true;
  }

  /**
   * Draws a 1-pixel line. Called with device coordinates; the default
   * implementation would apply the transform a second time.
   */
  protected void rawDrawLine(int x0, int y0, int x1, int y1)
  {
    Paint p = getPaint();
    if (pixels != null && p instanceof Color
        && ((Color) p).getAlpha() == 255 && isPlainSrcOver())
      {
        Rectangle clip = deviceClip();
        int rgb = ((Color) p).getRGB() | 0xFF000000;
        int xor = xorBits((Color) p);
        int dx = Math.abs(x1 - x0);
        int dy = -Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;
        int x = x0;
        int y = y0;
        int cx1 = clip.x + clip.width;
        int cy1 = clip.y + clip.height;
        while (true)
          {
            if (x >= clip.x && x < cx1 && y >= clip.y && y < cy1)
              {
                if (xor != 0)
                  pixels[y * scan + x] ^= xor;
                else
                  pixels[y * scan + x] = rgb;
              }
            if (x == x1 && y == y1)
              break;
            int e2 = 2 * err;
            if (e2 >= dy)
              {
                err += dy;
                x += sx;
              }
            if (e2 <= dx)
              {
                err += dx;
                y += sy;
              }
          }
        markDirty(Math.min(x0, x1), Math.min(y0, y1), dx + 1, -dy + 1);
        return;
      }
    int tx = (int) transform.getTranslateX();
    int ty = (int) transform.getTranslateY();
    draw(new java.awt.geom.Line2D.Float(x0 - tx, y0 - ty, x1 - tx, y1 - ty));
  }

  /**
   * Draws a rectangle outline, (w + 1) x (h + 1) pixels. Called with
   * device coordinates.
   */
  protected void rawDrawRect(int x, int y, int w, int h)
  {
    if (w < 0 || h < 0)
      return;
    // No pixel is drawn twice, so XOR mode works.
    rawDrawLine(x, y, x + w, y);
    if (h > 0)
      rawDrawLine(x, y + h, x + w, y + h);
    if (h > 1)
      {
        rawDrawLine(x, y + 1, x, y + h - 1);
        if (w > 0)
          rawDrawLine(x + w, y + 1, x + w, y + h - 1);
      }
  }

  protected void rawFillRect(int x, int y, int w, int h)
  {
    Paint p = getPaint();
    if (! (p instanceof Color && fastFill(x, y, w, h, (Color) p)))
      {
        // Device coordinates; fill() applies the transform itself.
        int tx = (int) transform.getTranslateX();
        int ty = (int) transform.getTranslateY();
        fill(new Rectangle(x - tx, y - ty, w, h));
      }
  }

  public void clearRect(int x, int y, int width, int height)
  {
    Color bg = getBackground();
    if (bg == null)
      bg = Color.WHITE;
    int tx = (int) transform.getTranslateX();
    int ty = (int) transform.getTranslateY();
    if (! fastFill(x + tx, y + ty, width, height, bg, 0))
      {
        Paint saved = getPaint();
        setPaint(bg);
        fillRect(x, y, width, height);
        setPaint(saved);
      }
  }

  protected boolean rawDrawImage(Image img, int x, int y, ImageObserver obs)
  {
    img = unwrap(img);
    if (pixels != null && img instanceof BufferedImage && isPlainSrcOver())
      {
        BufferedImage src = (BufferedImage) img;
        int type = src.getType();
        Raster sr = src.getRaster();
        DataBuffer sdb = sr.getDataBuffer();
        if ((type == BufferedImage.TYPE_INT_RGB
             || type == BufferedImage.TYPE_INT_ARGB)
            && sdb instanceof DataBufferInt && sdb.getNumBanks() == 1
            && sdb.getOffset() == 0
            && sr.getSampleModel() instanceof SinglePixelPackedSampleModel
            && sr.getMinX() == 0 && sr.getMinY() == 0
            && sr.getSampleModelTranslateX() == 0
            && sr.getSampleModelTranslateY() == 0)
          {
            int[] sp = ((DataBufferInt) sdb).getData();
            int sscan = ((SinglePixelPackedSampleModel) sr.getSampleModel())
                        .getScanlineStride();
            Rectangle r = new Rectangle(x, y, src.getWidth(), src.getHeight())
                          .intersection(deviceClip());
            if (r.width > 0 && r.height > 0)
              {
                boolean opaque = type == BufferedImage.TYPE_INT_RGB;
                for (int row = r.y; row < r.y + r.height; row++)
                  {
                    int soff = (row - y) * sscan + (r.x - x);
                    int doff = row * scan + r.x;
                    if (opaque)
                      {
                        if (hasAlpha)
                          for (int i = 0; i < r.width; i++)
                            pixels[doff + i] = sp[soff + i] | 0xFF000000;
                        else
                          System.arraycopy(sp, soff, pixels, doff, r.width);
                      }
                    else
                      for (int i = 0; i < r.width; i++)
                        pixels[doff + i] = blend(sp[soff + i],
                                                 pixels[doff + i]);
                  }
                markDirty(r.x, r.y, r.width, r.height);
              }
            return true;
          }
      }
    // Device coordinates; drawImage() applies the transform itself.
    int tx = (int) transform.getTranslateX();
    int ty = (int) transform.getTranslateY();
    return drawImage(img, java.awt.geom.AffineTransform
                     .getTranslateInstance(x - tx, y - ty), obs);
  }

  /**
   * Returns a RenderedImage for images that AbstractGraphics2D cannot draw
   * itself (it silently ignores anything that is not a RenderedImage).
   */
  private static Image unwrap(Image img)
  {
    if (img instanceof gnu.java.awt.image.AsyncImage)
      img = ((gnu.java.awt.image.AsyncImage) img).getRealImage();
    if (img instanceof HaikuVolatileImage)
      img = ((HaikuVolatileImage) img).getSnapshot();
    return img;
  }

  public boolean drawImage(Image img, java.awt.geom.AffineTransform xform,
                           ImageObserver obs)
  {
    return super.drawImage(unwrap(img), xform, obs);
  }

  public boolean drawImage(Image img, int x, int y, ImageObserver obs)
  {
    return super.drawImage(unwrap(img), x, y, obs);
  }

  // The generic AbstractGraphics2D code draws scaled images and images
  // with a background colour through an ImagePaint, one pixel at a time.
  // That is very slow on Kaffe, so these overloads scale the image here
  // (nearest neighbour) and use the direct copy in rawDrawImage().

  public boolean drawImage(Image img, int x, int y, int w, int h,
                           ImageObserver obs)
  {
    return drawImage(img, x, y, w, h, null, obs);
  }

  public boolean drawImage(Image img, int x, int y, Color bg,
                           ImageObserver obs)
  {
    img = unwrap(img);
    if (bg != null && img instanceof BufferedImage)
      {
        BufferedImage b = (BufferedImage) img;
        fillBackground(bg, x, y, b.getWidth(), b.getHeight());
        return drawImage(img, x, y, obs);
      }
    return super.drawImage(img, x, y, bg, obs);
  }

  public boolean drawImage(Image img, int x, int y, int w, int h, Color bg,
                           ImageObserver obs)
  {
    img = unwrap(img);
    if (img instanceof BufferedImage && w > 0 && h > 0)
      {
        BufferedImage b = (BufferedImage) img;
        return drawImage(b, x, y, x + w, y + h, 0, 0, b.getWidth(),
                         b.getHeight(), bg, obs);
      }
    return super.drawImage(img, x, y, w, h, bg, obs);
  }

  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
                           int sx1, int sy1, int sx2, int sy2,
                           ImageObserver obs)
  {
    return drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null, obs);
  }

  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
                           int sx1, int sy1, int sx2, int sy2, Color bg,
                           ImageObserver obs)
  {
    img = unwrap(img);
    if (img instanceof BufferedImage && dx2 > dx1 && dy2 > dy1
        && sx2 > sx1 && sy2 > sy1)
      {
        BufferedImage b = HaikuToolkit.toDirect((BufferedImage) img);
        int dw = dx2 - dx1;
        int dh = dy2 - dy1;
        BufferedImage part = scaled(b, sx1, sy1, sx2 - sx1, sy2 - sy1, dw, dh);
        if (bg != null)
          fillBackground(bg, dx1, dy1, dw, dh);
        return drawImage(part, dx1, dy1, obs);
      }
    return super.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bg,
                           obs);
  }

  /** Fills a rectangle (user coordinates) with c, keeping the paint. */
  private void fillBackground(Color c, int x, int y, int w, int h)
  {
    Paint saved = getPaint();
    setPaint(c);
    fillRect(x, y, w, h);
    setPaint(saved);
  }

  /**
   * Copies the source rectangle of an INT_RGB or INT_ARGB image, scaled
   * to dw x dh with nearest neighbour sampling. The source rectangle is
   * clipped to the image; pixels outside it are transparent.
   */
  private static BufferedImage scaled(BufferedImage src, int sx, int sy,
                                      int sw, int sh, int dw, int dh)
  {
    int iw = src.getWidth();
    int ih = src.getHeight();
    if (sx == 0 && sy == 0 && sw == iw && sh == ih && dw == iw && dh == ih)
      return src;
    BufferedImage out = new BufferedImage(dw, dh,
                                          BufferedImage.TYPE_INT_ARGB);
    int[] dst = ((DataBufferInt) out.getRaster().getDataBuffer()).getData();
    int[] row = new int[iw];
    boolean opaque = src.getType() == BufferedImage.TYPE_INT_RGB;
    int lastSy = -1;
    for (int y = 0; y < dh; y++)
      {
        int syy = sy + (int) ((long) y * sh / dh);
        if (syy < 0 || syy >= ih)
          continue;
        if (syy != lastSy)
          {
            src.getRGB(0, syy, iw, 1, row, 0, iw);
            lastSy = syy;
          }
        int off = y * dw;
        for (int x = 0; x < dw; x++)
          {
            int sxx = sx + (int) ((long) x * sw / dw);
            if (sxx >= 0 && sxx < iw)
              dst[off + x] = opaque ? row[sxx] | 0xFF000000 : row[sxx];
          }
      }
    return out;
  }

  /** Non-premultiplied ARGB src over an opaque-or-ARGB destination. */
  private static int blend(int s, int d)
  {
    int a = s >>> 24;
    if (a == 255)
      return s;
    if (a == 0)
      return d;
    int na = 255 - a;
    int r = (((s >> 16) & 0xFF) * a + ((d >> 16) & 0xFF) * na) / 255;
    int g = (((s >> 8) & 0xFF) * a + ((d >> 8) & 0xFF) * na) / 255;
    int b = ((s & 0xFF) * a + (d & 0xFF) * na) / 255;
    int da = d >>> 24;
    int oa = a + da * na / 255;
    return (oa << 24) | (r << 16) | (g << 8) | b;
  }

  public void copyArea(int x, int y, int width, int height, int dx, int dy)
  {
    if (! isSimpleTransform())
      return;
    int tx = (int) transform.getTranslateX();
    int ty = (int) transform.getTranslateY();
    Rectangle bounds = getDeviceBounds();
    Rectangle src = new Rectangle(x + tx, y + ty, width, height)
                    .intersection(bounds);
    Rectangle dst = new Rectangle(src.x + dx, src.y + dy, src.width,
                                  src.height).intersection(deviceClip());
    if (dst.width <= 0 || dst.height <= 0)
      return;
    // Read the source part that maps onto dst, then write it.
    Object data = raster.getDataElements(dst.x - dx, dst.y - dy,
                                         dst.width, dst.height, null);
    raster.setDataElements(dst.x, dst.y, dst.width, dst.height, data);
    markDirty(dst.x, dst.y, dst.width, dst.height);
  }

  // Text: Haiku fonts are drawn by the app_server.

  /** A rendered string: coverage, size, baseline and origin. */
  private static final class TextMask
  {
    final byte[] cover;
    final int w, h, baseline, originX;

    TextMask(byte[] cover, int[] dims)
    {
      this.cover = cover;
      w = dims[0];
      h = dims[1];
      baseline = dims[2];
      originX = dims[3];
    }
  }

  /** Recently drawn strings; Swing draws the same labels again and again. */
  private static final java.util.LinkedHashMap<String,TextMask> textCache =
    new java.util.LinkedHashMap<String,TextMask>(256, 0.75f, true)
    {
      protected boolean removeEldestEntry(java.util.Map.Entry<String,TextMask> e)
      {
        return size() > 500;
      }
    };

  private static TextMask renderText(HaikuFontPeer peer, float size,
                                     String text)
  {
    String key = peer.handle + "/" + size + "/" + text;
    synchronized (textCache)
      {
        TextMask m = textCache.get(key);
        if (m != null)
          return m;
      }
    int[] dims = new int[4];
    byte[] cover = HaikuNative.fontRender(peer.handle, size, text, dims);
    if (cover == null)
      return null;
    TextMask m = new TextMask(cover, dims);
    synchronized (textCache)
      {
        textCache.put(key, m);
      }
    return m;
  }

  /**
   * Draws glyph vectors of Haiku fonts with the app_server, at the scale
   * of the transform. Rotated or sheared text, transformed fonts, paints
   * that are not colours and moved glyphs are filled as outlines.
   */
  public void drawGlyphVector(java.awt.font.GlyphVector gv, float x, float y)
  {
    if (gv instanceof HaikuGlyphVector && pixels != null
        && getPaint() instanceof Color)
      {
        HaikuGlyphVector hgv = (HaikuGlyphVector) gv;
        java.awt.geom.AffineTransform t = getTransform();
        java.awt.geom.AffineTransform ft = hgv.font.getTransform();
        double sx = t.getScaleX();
        double sy = t.getScaleY();
        if (! hgv.customLayout && t.getShearX() == 0 && t.getShearY() == 0
            && sx > 0 && Math.abs(sx - sy) < 1e-6
            && (ft == null || ft.isIdentity()))
          {
            if (hgv.text.length() == 0)
              return;
            TextMask m = renderText(hgv.peer,
                                    (float) (hgv.font.getSize2D() * sy),
                                    hgv.text);
            if (m != null)
              {
                int dx = (int) Math.round(t.getTranslateX() + sx * x)
                         - m.originX;
                int dy = (int) Math.round(t.getTranslateY() + sy * y)
                         - m.baseline;
                blendCoverage(m.cover, m.w, m.h, dx, dy);
                return;
              }
          }
      }
    // AbstractGraphics2D translates before filling, which also moves
    // gradient and texture paints; fill the outline in place instead.
    fillShape(gv.getOutline(x, y), true);
  }

  /** Blends the current colour through a coverage mask at (dx, dy). */
  private void blendCoverage(byte[] cover, int w, int h, int dx, int dy)
  {
    Color c = (Color) getPaint();
    int rgb = c.getRGB();
    int ca = c.getAlpha();
    int cr = (rgb >> 16) & 0xff, cg = (rgb >> 8) & 0xff, cb = rgb & 0xff;
    Rectangle area = new Rectangle(0, 0, image.getWidth(), image.getHeight());
    java.awt.Shape clip = getClip();
    if (clip != null)
      area = area.intersection(getTransform().createTransformedShape(clip)
                               .getBounds());
    area = area.intersection(new Rectangle(dx, dy, w, h));
    if (area.isEmpty())
      return;
    for (int yy = area.y; yy < area.y + area.height; yy++)
      for (int xx = area.x; xx < area.x + area.width; xx++)
        {
          int a = (cover[(yy - dy) * w + (xx - dx)] & 0xff) * ca / 255;
          if (a == 0)
            continue;
          int k = yy * scan + xx;
          int d = pixels[k];
          int r = (cr * a + ((d >> 16) & 0xff) * (255 - a)) / 255;
          int g = (cg * a + ((d >> 8) & 0xff) * (255 - a)) / 255;
          int b = (cb * a + (d & 0xff) * (255 - a)) / 255;
          int al = hasAlpha ? a + (((d >>> 24) * (255 - a)) / 255) : 0xff;
          pixels[k] = (al << 24) | (r << 16) | (g << 8) | b;
        }
    markDirty(area.x, area.y, area.width, area.height);
  }

  public Graphics create()
  {
    HaikuGraphics2D copy = (HaikuGraphics2D) super.create();
    copy.dirty = null;
    copy.disposed = false;
    return copy;
  }

  /**
   * Copies the area drawn so far to the native window.
   */
  public void flush()
  {
    if (target != null && dirty != null)
      {
        Rectangle d = dirty;
        dirty = null;
        target.flushBacking(image, d.x, d.y, d.width, d.height);
      }
  }

  public void dispose()
  {
    if (! disposed)
      {
        disposed = true;
        flush();
      }
  }
}
