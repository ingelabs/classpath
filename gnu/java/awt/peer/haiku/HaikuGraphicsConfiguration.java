/* HaikuGraphicsConfiguration.java -- part of the Haiku AWT peer
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

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.VolatileImage;

/**
 * The single 32-bit configuration of the Haiku screen.
 */
class HaikuGraphicsConfiguration
  extends GraphicsConfiguration
{

  private final HaikuGraphicsDevice device;

  private int width, height, dpi;

  HaikuGraphicsConfiguration(HaikuGraphicsDevice dev)
  {
    device = dev;
    int[] info = new int[3];
    HaikuNative.ensureInitialized();
    HaikuNative.screenInfo(info);
    width = info[0];
    height = info[1];
    dpi = info[2];
  }

  int getResolution()
  {
    return dpi;
  }

  public GraphicsDevice getDevice()
  {
    return device;
  }

  public BufferedImage createCompatibleImage(int w, int h)
  {
    return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
  }

  public BufferedImage createCompatibleImage(int w, int h, int transparency)
  {
    if (transparency == Transparency.OPAQUE)
      return createCompatibleImage(w, h);
    return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
  }

  public VolatileImage createCompatibleVolatileImage(int w, int h)
  {
    return new HaikuVolatileImage(w, h);
  }

  public VolatileImage createCompatibleVolatileImage(int w, int h,
                                                     int transparency)
  {
    return new HaikuVolatileImage(w, h);
  }

  public ColorModel getColorModel()
  {
    return ColorModel.getRGBdefault();
  }

  public ColorModel getColorModel(int transparency)
  {
    return ColorModel.getRGBdefault();
  }

  public AffineTransform getDefaultTransform()
  {
    return new AffineTransform();
  }

  public AffineTransform getNormalizingTransform()
  {
    double s = dpi / 72.0;
    return AffineTransform.getScaleInstance(s, s);
  }

  public Rectangle getBounds()
  {
    return new Rectangle(0, 0, width, height);
  }
}
