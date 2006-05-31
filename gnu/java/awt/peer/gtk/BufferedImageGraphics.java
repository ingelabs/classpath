/* BufferedImageGraphics.java
   Copyright (C) 2006 Free Software Foundation, Inc.

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


package gnu.java.awt.peer.gtk;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.ImageObserver;

/**
 * Implementation of Graphics2D on a Cairo surface.
 *
 * Simutanously maintains a CairoSurface and updates the 
 * BufferedImage from that after each drawing operation.
 */
public class BufferedImageGraphics extends CairoGraphics2D
{
  /**
   * the buffered Image.
   */
  private BufferedImage image;

  /**
   * Image size.
   */
  private int imageWidth, imageHeight;

  /**
   * The cairo surface that we actually draw on.
   */
  private CairoSurface surface;

  /**
   * Its corresponding cairo_t.
   */
  private long cairo_t;
  
  public BufferedImageGraphics(BufferedImage bi)
  {
    this.image = bi;
    imageWidth = bi.getWidth();
    imageHeight = bi.getHeight();

    surface = new CairoSurface( imageWidth, imageHeight );
    cairo_t = surface.newCairoContext();

    DataBuffer db = bi.getRaster().getDataBuffer();
    int[] pixels;
    // get pixels

    if(db instanceof CairoSurface)
      pixels = ((CairoSurface)db).getPixels(imageWidth * imageHeight * 4);
    else
      pixels = CairoGraphics2D.findSimpleIntegerArray (image.getColorModel(),
						       image.getData());
    surface.setPixels( pixels );

    setup( cairo_t );
    setClip(0, 0, imageWidth, imageHeight);
  }
  
  private BufferedImageGraphics(BufferedImageGraphics copyFrom)
  {

    cairo_t = surface.newCairoContext();
    copy( copyFrom, cairo_t );
    setClip(0, 0, surface.width, surface.height);
  }

  /**
   * Update a rectangle of the bufferedImage. This can be improved upon a lot.
   */
  private void updateBufferedImage(int x, int y, int width, int height)
  {
    int[] pixels = surface.getPixels(imageWidth * imageHeight * 4);
    if( x > imageWidth || y > imageHeight )
      return;
    // Clip edges.
    if( x < 0 ){ width = width + x; x = 0; }
    if( y < 0 ){ height = height + y; y = 0; }
    if( x + width > imageWidth ) 
      width = imageWidth - x;
    if( y + height > imageHeight ) 
      height = imageHeight - y;
    
    int index = 0;
    for (int j = y; j < y + height; ++j)
      for (int i = x; i < x + width; ++i)
	image.setRGB(i, j, pixels[ i + j*imageWidth ]);
  }

  /**
   * Abstract methods.
   */  
  public Graphics create()
  {
    return new BufferedImageGraphics( this );
  }
  
  public GraphicsConfiguration getDeviceConfiguration()
  {
    return null;
  }
  
  public void copyArea(int x, int y, int width, int height, int dx, int dy)
  {
    // FIXME
  }

  /**
   * Overloaded methods that do actual drawing need to enter the gdk threads 
   * and also do certain things before and after.
   */
  public void draw(Shape s)
  {
    super.draw(s);
    Rectangle r = s.getBounds();
    updateBufferedImage(r.x, r.y, r.width, r.height);
  }

  public void fill(Shape s)
  {
    super.fill(s);
    Rectangle r = s.getBounds();
    updateBufferedImage(r.x, r.y, r.width, r.height);
  }

  public void drawRenderedImage(RenderedImage image, AffineTransform xform)
  {
    super.drawRenderedImage(image, xform);
    updateBufferedImage(0, 0, imageWidth, imageHeight);
  }

  protected boolean drawImage(Image img, AffineTransform xform,
			      Color bgcolor, ImageObserver obs)
  {
    boolean rv = super.drawImage(img, xform, bgcolor, obs);
    updateBufferedImage(0, 0, imageWidth, imageHeight);
    return rv;
  }

  public void drawGlyphVector(GlyphVector gv, float x, float y)
  {
    super.drawGlyphVector(gv, x, y);
    Rectangle r = gv.getPixelBounds(getFontRenderContext(), x , y);
    updateBufferedImage(r.x, r.y, r.width, r.height);
  }
}
