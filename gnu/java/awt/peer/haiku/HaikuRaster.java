/* HaikuRaster.java -- native pixel loops for HaikuGraphics2D
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

import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;

/**
 * Native pixel loops for {@link HaikuGraphics2D}. On Kaffe's interpreter
 * per-pixel Java code is very slow; these functions work directly on the
 * int[] pixels (0x00RRGGBB, or non-premultiplied 0xAARRGGBB when alpha
 * is true). clip is a device rectangle {x, y, w, h}.
 */
final class HaikuRaster
{

  static
  {
    HaikuNative.ensureInitialized();
  }

  private HaikuRaster()
  {
  }

  static native void fillRect(int[] dst, int scan, boolean alpha, int[] clip,
                              int x, int y, int w, int h, int argb,
                              int xorBits);

  static native void drawLine(int[] dst, int scan, boolean alpha, int[] clip,
                              int x0, int y0, int x1, int y1, int argb,
                              int xorBits);

  /** path: see {@link #path}. */
  static native void drawPathThin(int[] dst, int scan, boolean alpha,
                                  int[] clip, float[] path, int n, int argb,
                                  int xorBits);

  static native void fillPath(int[] dst, int scan, boolean alpha, int[] clip,
                              float[] path, int n, boolean evenOdd,
                              boolean aa, int argb, int xorBits);

  /** g = x1, y1, x2, y2 (user space), then the inverse transform (6). */
  static native void fillPathGradient(int[] dst, int scan, boolean alpha,
                                      int[] clip, float[] path, int n,
                                      boolean evenOdd, boolean aa,
                                      double[] g, int c1, int c2,
                                      boolean cyclic);

  /**
   * Draws the source rectangle (sx, sy, sw, sh) of src through a
   * transform. inverse maps device points to source points (relative to
   * sx, sy); box is the device area {x, y, w, h} to visit.
   */
  static native void drawImage(int[] dst, int scan, boolean alpha,
                               int[] clip, int[] src, int sscan,
                               boolean srcAlpha, int sx, int sy, int sw,
                               int sh, double[] inverse, int[] box,
                               boolean bilinear, int extraAlpha);

  static native void copyArea(int[] dst, int scan, int x, int y, int w,
                              int h, int dx, int dy);

  /**
   * A shape in device space as a flat path: 0 x y = move, 1 x y = line,
   * 2 x1 y1 x2 y2 = quad, 3 x1 y1 x2 y2 x3 y3 = cubic, 4 = close. The
   * winding rule goes to evenOdd[0].
   */
  static float[] path(Shape s, AffineTransform t,
                                   boolean[] evenOdd)
  {
    PathIterator pi = s.getPathIterator(t);
    evenOdd[0] = pi.getWindingRule() == PathIterator.WIND_EVEN_ODD;
    float[] out = new float[64];
    float[] c = new float[6];
    int n = 0;
    while (! pi.isDone())
      {
        int type = pi.currentSegment(c);
        int pts = type == PathIterator.SEG_MOVETO
                  || type == PathIterator.SEG_LINETO ? 1
                  : type == PathIterator.SEG_QUADTO ? 2
                  : type == PathIterator.SEG_CUBICTO ? 3 : 0;
        if (n + 1 + 2 * pts > out.length)
          {
            float[] bigger = new float[out.length * 2];
            System.arraycopy(out, 0, bigger, 0, n);
            out = bigger;
          }
        out[n++] = type == PathIterator.SEG_MOVETO ? 0
                   : type == PathIterator.SEG_LINETO ? 1
                   : type == PathIterator.SEG_QUADTO ? 2
                   : type == PathIterator.SEG_CUBICTO ? 3 : 4;
        for (int i = 0; i < 2 * pts; i++)
          out[n++] = c[i];
        pi.next();
      }
    if (n != out.length)
      {
        float[] exact = new float[n];
        System.arraycopy(out, 0, exact, 0, n);
        out = exact;
      }
    return out;
  }

  /** The int pixels of an image: data, scan, offset, size, alpha. */
  static final class Source
  {
    int[] data;
    int scan, x, y, w, h;
    boolean alpha;
  }

  /**
   * The pixels of an image as 0x(AA)RRGGBB ints without copying when the
   * image is INT_RGB or INT_ARGB (also a sub-image); other images are
   * converted. Returns null for images that are not BufferedImages.
   */
  static Source source(Image img)
  {
    if (! (img instanceof BufferedImage))
      return null;
    BufferedImage b = (BufferedImage) img;
    int type = b.getType();
    if (type != BufferedImage.TYPE_INT_RGB
        && type != BufferedImage.TYPE_INT_ARGB)
      {
        b = HaikuToolkit.toDirect(b);
        type = b.getType();
      }
    Raster r = b.getRaster();
    DataBuffer db = r.getDataBuffer();
    if (! (db instanceof DataBufferInt) || db.getNumBanks() != 1
        || ! (r.getSampleModel() instanceof SinglePixelPackedSampleModel))
      return null;
    SinglePixelPackedSampleModel sm =
      (SinglePixelPackedSampleModel) r.getSampleModel();
    Source s = new Source();
    s.data = ((DataBufferInt) db).getData();
    s.scan = sm.getScanlineStride();
    int off = db.getOffset();
    s.x = r.getMinX() - r.getSampleModelTranslateX() + off % s.scan;
    s.y = r.getMinY() - r.getSampleModelTranslateY() + off / s.scan;
    s.w = b.getWidth();
    s.h = b.getHeight();
    s.alpha = type == BufferedImage.TYPE_INT_ARGB;
    return s;
  }
}
