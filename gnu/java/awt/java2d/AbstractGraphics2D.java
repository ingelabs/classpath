/* AbstractGraphics2D.java -- Abstract Graphics2D implementation
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

package gnu.java.awt.java2d;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * Implements general and shared behaviour for Graphics2D implementation.
 *
 * @author Roman Kennke (kennke@aicas.com)
 */
public abstract class AbstractGraphics2D
  extends Graphics2D
{

  /**
   * The transformation for this Graphics2D instance
   */
  private AffineTransform transform;

  /**
   * The foreground.
   */
  private Paint paint;

  /**
   * The background.
   */
  private Color background;

  /**
   * The current composite setting.
   */
  private Composite composite;

  /**
   * The current stroke setting.
   */
  private Stroke stroke;

  /**
   * The current clip. This clip is in target coordinate space.
   */
  private Shape clip;

  /**
   * The rendering hints.
   */
  private RenderingHints renderingHints;

  /**
   * The paint context to use for draw and fill operations.
   */
  private PaintContext paintContext;

  /**
   * The paint raster.
   */
  private Raster paintRaster;

  /**
   * The composite context to use for draw and fill operations.
   */
  private CompositeContext compositeContext;

  /**
   * A cached pixel array.
   */
  private int[] pixel;

  /**
   * Creates a new AbstractGraphics2D instance.
   */
  protected AbstractGraphics2D()
  {
    transform = new AffineTransform();
    setPaint(Color.BLACK);
    background = Color.WHITE;
    composite = AlphaComposite.SrcOver;
    stroke = new BasicStroke();
    renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING,
                                        RenderingHints.VALUE_RENDER_DEFAULT);

    pixel = new int[4];
  }

  /**
   * Draws the specified shape. The shape is passed through the current stroke
   * and is then forwarded to {@link #fillShape}.
   *
   * @param shape the shape to draw
   */
  public void draw(Shape shape)
  {
    Shape strokedShape = stroke.createStrokedShape(shape);
    fillShape(strokedShape);
  }

  public boolean drawImage(Image image, AffineTransform xform, ImageObserver obs)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void drawImage(BufferedImage image, BufferedImageOp op, int x, int y)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void drawRenderedImage(RenderedImage image, AffineTransform xform)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void drawRenderableImage(RenderableImage image, AffineTransform xform)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void drawString(String text, int x, int y)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void drawString(String text, float x, float y)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void drawString(AttributedCharacterIterator iterator, int x, int y)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void drawString(AttributedCharacterIterator iterator, float x, float y)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Fills the specified shape with the current foreground.
   *
   * @param shape the shape to fill
   */
  public void fill(Shape shape)
  {
    fillShape(shape);
  }

  public boolean hit(Rectangle rect, Shape text, boolean onStroke)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Sets the composite.
   *
   * @param comp the composite to set
   */
  public void setComposite(Composite comp)
  {
    composite = comp;
    compositeContext = composite.createContext(getColorModel(),
                                               getDestinationColorModel(),
                                               getRenderingHints());
  }

  /**
   * Sets the current foreground.
   *
   * @param p the foreground to set.
   */
  public void setPaint(Paint p)
  {
    paint = p;
    Rectangle deviceBounds = getDeviceBounds();
    paintContext = paint.createContext(getColorModel(), deviceBounds,
                                       getUserBounds(), transform,
                                       getRenderingHints());
    paintRaster = paintContext.getRaster(deviceBounds.x, deviceBounds.y,
                                         deviceBounds.width,
                                         deviceBounds.height);
  }

  /**
   * Sets the stroke for this graphics object.
   *
   * @param s the stroke to set
   */
  public void setStroke(Stroke s)
  {
    stroke = s;
  }

  /**
   * Sets the specified rendering hint.
   *
   * @param hintKey the key of the rendering hint
   * @param hintValue the value
   */
  public void setRenderingHint(Key hintKey, Object hintValue)
  {
    renderingHints.put(hintKey, hintValue);
  }

  /**
   * Returns the rendering hint for the specified key.
   *
   * @param hintKey the rendering hint key
   *
   * @return the rendering hint for the specified key
   */
  public Object getRenderingHint(Key hintKey)
  {
    return renderingHints.get(hintKey);
  }

  /**
   * Sets the specified rendering hints.
   *
   * @param hints the rendering hints to set
   */
  public void setRenderingHints(Map hints)
  {
    renderingHints.clear();
    renderingHints.putAll(hints);
  }

  /**
   * Adds the specified rendering hints.
   *
   * @param hints the rendering hints to add
   */
  public void addRenderingHints(Map hints)
  {
    renderingHints.putAll(hints);
  }

  /**
   * Returns the current rendering hints.
   *
   * @return the current rendering hints
   */
  public RenderingHints getRenderingHints()
  {
    return (RenderingHints) renderingHints.clone();
  }

  /**
   * Translates the coordinate system by (x, y).
   *
   * @param x the translation X coordinate
   * @param y the translation Y coordinate 
   */
  public void translate(int x, int y)
  {
    transform.translate(x, y);
  }

  /**
   * Translates the coordinate system by (tx, ty).
   *
   * @param tx the translation X coordinate
   * @param ty the translation Y coordinate 
   */
  public void translate(double tx, double ty)
  {
    transform.translate(tx, ty);
  }

  /**
   * Rotates the coordinate system by <code>theta</code> degrees.
   *
   * @param theta the angle be which to rotate the coordinate system
   */
  public void rotate(double theta)
  {
    transform.rotate(theta);
  }

  /**
   * Rotates the coordinate system by <code>theta</code> around the point
   * (x, y).
   *
   * @param theta the angle by which to rotate the coordinate system
   * @param x the point around which to rotate, X coordinate
   * @param y the point around which to rotate, Y coordinate
   */
  public void rotate(double theta, double x, double y)
  {
    transform.rotate(theta, x, y);
  }

  /**
   * Scales the coordinate system by the factors <code>scaleX</code> and
   * <code>scaleY</code>.
   *
   * @param scaleX the factor by which to scale the X axis
   * @param scaleY the factor by which to scale the Y axis
   */
  public void scale(double scaleX, double scaleY)
  {
    transform.scale(scaleX, scaleY);
  }

  /**
   * Shears the coordinate system by <code>shearX</code> and
   * <code>shearY</code>.
   *
   * @param shearX the X shearing
   * @param shearY the Y shearing
   */
  public void shear(double shearX, double shearY)
  {
    transform.shear(shearX, shearY);
  }

  /**
   * Transforms the coordinate system using the specified transform
   * <code>t</code>.
   *
   * @param the transform
   */
  public void transform(AffineTransform t)
  {
    transform.concatenate(t);
  }

  /**
   * Sets the transformation for this Graphics object.
   *
   * @param t the transformation to set
   */
  public void setTransform(AffineTransform t)
  {
    transform.setTransform(t);
  }

  /**
   * Returns the transformation of this coordinate system.
   *
   * @return the transformation of this coordinate system
   */
  public AffineTransform getTransform()
  {
    return (AffineTransform) transform.clone();
  }

  /**
   * Returns the current foreground.
   *
   * @return the current foreground
   */
  public Paint getPaint()
  {
    return paint;
  }

  /**
   * Returns the current composite.
   *
   * @return the current composite
   */
  public Composite getComposite()
  {
    return composite;
  }

  /**
   * Sets the current background.
   *
   * @param color the background to set.
   */
  public void setBackground(Color color)
  {
    background = color;
  }

  /**
   * Returns the current background.
   *
   * @param the current background
   */
  public Color getBackground()
  {
    return background;
  }

  /**
   * Returns the current stroke.
   *
   * @return the current stroke
   */
  public Stroke getStroke()
  {
    return stroke;
  }

  /**
   * Intersects the clip of this graphics object with the specified clip.
   *
   * @param s the clip with which the current clip should be intersected
   */
  public void clip(Shape s)
  {
    Area current;
    if (clip instanceof Area)
      current = (Area) clip;
    else
      current = new Area(clip);

    Area intersect;
    if (s instanceof Area)
      intersect = (Area) s;
    else
      intersect = new Area(s);

    current.intersect(intersect);
    clip = current;
  }

  public FontRenderContext getFontRenderContext()
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void drawGlyphVector(GlyphVector g, float x, float y)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public Graphics create()
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Returns the current foreground.
   */
  public Color getColor()
  {
    Color c = null;
    if (paint instanceof Color)
      c = (Color) paint;
    return c;
  }

  /**
   * Sets the current foreground.
   *
   * @param color the foreground to set
   */
  public void setColor(Color color)
  {
    setPaint(color);
  }

  public void setPaintMode()
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void setXORMode(Color color)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public Font getFont()
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public void setFont(Font font)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public FontMetrics getFontMetrics(Font font)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Returns the bounds of the current clip.
   *
   * @return the bounds of the current clip
   */
  public Rectangle getClipBounds()
  {
    return clip.getBounds();
  }

  /**
   * Intersects the current clipping region with the specified rectangle.
   *
   * @param x the x coordinate of the rectangle
   * @param y the y coordinate of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  public void clipRect(int x, int y, int width, int height)
  {
    clip(new Rectangle(x, y, width, height));
  }

  /**
   * Sets the clip to the specified rectangle.
   *
   * @param x the x coordinate of the clip rectangle
   * @param y the y coordinate of the clip rectangle
   * @param width the width of the clip rectangle
   * @param height the height of the clip rectangle
   */
  public void setClip(int x, int y, int width, int height)
  {
    setClip(new Rectangle(x, y, width, height));
  }

  /**
   * Returns the current clip.
   *
   * @return the current clip
   */
  public Shape getClip()
  {
    return clip;
  }

  /**
   * Sets the current clipping area to <code>clip</code>.
   *
   * @param c the clip to set
   */
  public void setClip(Shape c)
  {
    clip = c;
  }

  public void copyArea(int x, int y, int width, int height, int dx, int dy)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Draws a line from (x1, y1) to (x2, y2).
   *
   * This implementation transforms the coordinates and forwards the call to
   * {@link #rawDrawLine}.
   */
  public void drawLine(int x1, int y1, int x2, int y2)
  {
    Line2D line = new Line2D.Double(x1, y1, x2, y2);
    draw(line);
  }

  /**
   * Fills a rectangle with the current paint.
   *
   * @param x the upper left corner, X coordinate
   * @param y the upper left corner, Y coordinate
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  public void fillRect(int x, int y, int width, int height)
  {
    fill(new Rectangle(x, y, width, height));
  }

  /**
   * Fills a rectangle with the current background color.
   *
   * This implementation temporarily sets the foreground color to the 
   * background and forwards the call to {@link #fillRect(int, int, int, int)}.
   *
   * @param x the upper left corner, X coordinate
   * @param y the upper left corner, Y coordinate
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
  public void clearRect(int x, int y, int width, int height)
  {
    Paint savedForeground = getPaint();
    setPaint(getBackground());
    fillRect(x, y, width, height);
    setPaint(savedForeground);
  }

  /**
   * Draws a rounded rectangle.
   *
   * @param x the x coordinate of the rectangle
   * @param y the y coordinate of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @param arcWidth the width of the arcs
   * @param arcHeight the height of the arcs
   */
  public void drawRoundRect(int x, int y, int width, int height, int arcWidth,
                            int arcHeight)
  {
    draw(new RoundRectangle2D.Double(x, y, width, height, arcWidth,
                                     arcHeight));
  }

  /**
   * Fills a rounded rectangle.
   *
   * @param x the x coordinate of the rectangle
   * @param y the y coordinate of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @param arcWidth the width of the arcs
   * @param arcHeight the height of the arcs
   */
  public void fillRoundRect(int x, int y, int width, int height, int arcWidth,
                            int arcHeight)
  {
    fill(new RoundRectangle2D.Double(x, y, width, height, arcWidth,
                                     arcHeight));
  }

  /**
   * Draws the outline of an oval.
   *
   * @param x the upper left corner of the bounding rectangle of the ellipse
   * @param y the upper left corner of the bounding rectangle of the ellipse
   * @param width the width of the ellipse
   * @param height the height of the ellipse
   */
  public void drawOval(int x, int y, int width, int height)
  {
    draw(new Ellipse2D.Double(x, y, width, height));
  }

  /**
   * Fills an oval.
   *
   * @param x the upper left corner of the bounding rectangle of the ellipse
   * @param y the upper left corner of the bounding rectangle of the ellipse
   * @param width the width of the ellipse
   * @param height the height of the ellipse
   */
  public void fillOval(int x, int y, int width, int height)
  {
    fill(new Ellipse2D.Double(x, y, width, height));
  }

  /**
   * Draws an arc.
   */
  public void drawArc(int x, int y, int width, int height, int arcStart,
                      int arcAngle)
  {
    draw(new Arc2D.Double(x, y, width, height, arcStart, arcAngle,
                          Arc2D.OPEN));
  }

  /**
   * Fills an arc.
   */
  public void fillArc(int x, int y, int width, int height, int arcStart,
                      int arcAngle)
  {
    fill(new Arc2D.Double(x, y, width, height, arcStart, arcAngle,
                          Arc2D.OPEN));
  }

  public void drawPolyline(int[] xPoints, int[] yPoints, int npoints)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Draws the outline of a polygon.
   */
  public void drawPolygon(int[] xPoints, int[] yPoints, int npoints)
  {
    draw(new Polygon(xPoints, yPoints, npoints));
  }

  /**
   * Fills the outline of a polygon.
   */
  public void fillPolygon(int[] xPoints, int[] yPoints, int npoints)
  {
    fill(new Polygon(xPoints, yPoints, npoints));
  }

  public boolean drawImage(Image image, int x, int y, ImageObserver observer)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public boolean drawImage(Image image, int x, int y, int width, int height,
                           ImageObserver observer)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public boolean drawImage(Image image, int x, int y, Color bgcolor,
                           ImageObserver observer)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public boolean drawImage(Image image, int x, int y, int width, int height,
                           Color bgcolor, ImageObserver observer)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2,
                           int sx1, int sy1, int sx2, int sy2,
                           ImageObserver observer)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2,
                           int sx1, int sy1, int sx2, int sy2, Color bgcolor,
                           ImageObserver observer)
  {
    // FIXME: Implement this.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Disposes this graphics object.
   */
  public void dispose()
  {
    // Nothing special to do here.
  }

  /**
   * Draws a shape that has already been stroked. This transforms the shape
   * into the target coordinate space and forwards the resulting path iterator
   * to {@link #drawPathIterator}.
   *
   * @param s the stroked shape to draw
   */
  protected void fillShape(Shape s)
  {
    GeneralPath p = new GeneralPath(s);
    Shape transformed = p.createTransformedShape(transform);

    // Transform the bounding rectangle into target space.
    Rectangle b = transformed.getBounds();
    int x2 = b.x + b.width;
    int y2 = b.y + b.height;

    // Scan the target rectangle and draw every pixel that lies inside the
    // Shape.
    for (int y = b.y; y < y2; y++)
      {
        for (int x = b.x; x < x2; x++)
          {
            if (clip.contains(x, y) && transformed.contains(x, y))
              drawPixel(x, y);
          }
      }
  }

  /**
   * Draws one pixel in the target coordinate space. This method draws the
   * specified pixel by getting the painting pixel for that coordinate
   * from the paintContext and compositing the pixel with the compositeContext.
   * The resulting pixel is then set by calling {@link #rawSetPixel}.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  protected void drawPixel(int x, int y)
  {
    int[] paintPixel = paintRaster.getPixel(x, y, pixel);
    // FIXME: Implement efficient compositing.
    rawSetPixel(x, y, paintPixel[0], paintPixel[1], paintPixel[2]);
  }

  /**
   * Draws a pixel in the target coordinate space using the specified color.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @param r the red component of the pixel
   * @param g the green component of the pixel
   * @param b the blue component of the pixel
   */
  protected abstract void rawSetPixel(int x, int y, int r, int g, int b);

  /**
   * Returns the color model of this Graphics object.
   *
   * @return the color model of this Graphics object
   */
  protected abstract ColorModel getColorModel();

  /**
   * Returns the color model of the target device.
   *
   * @return the color model of the target device
   */
  protected abstract ColorModel getDestinationColorModel();

  /**
   * Returns the bounds of the target.
   *
   * @return the bounds of the target
   */
  protected abstract Rectangle getDeviceBounds();

  /**
   * Returns the bounds of the drawing area in user space.
   *
   * @return the bounds of the drawing area in user space
   */
  protected abstract Rectangle2D getUserBounds();

  //protected abstract Raster getDestinationRaster(int x, int y, int w, int h);
}
