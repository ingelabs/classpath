/* Arc2D.java -- represents an arc in 2-D space
   Copyright (C) 2002 Free Software Foundation

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

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


package java.awt.geom;

import java.util.NoSuchElementException;

/**
 * STUBS ONLY
 * XXX Implement and document.
 */
public abstract class Arc2D extends RectangularShape
{
  public static final int OPEN = 0;
  public static final int CHORD = 1;
  public static final int PIE = 2;

  private int type;

  protected Arc2D(int type)
  {
    if (type < OPEN || type > PIE)
      throw new IllegalArgumentException();
    this.type = type;
  }

  /** Start in degrees. */
  public abstract double getAngleStart();
  /** Extent in degrees. */
  public abstract double getAngleExtent();

  public int getArcType()
  {
    return type;
  }
  public Point2D getStartPoint()
  {
    // XXX Implement.
    throw new Error("not implemented");
  }
  public Point2D getEndPoint()
  {
    // XXX Implement.
    throw new Error("not implemented");
  }
  /** Start, extent in degrees. */
  public abstract void setArc(double x, double y, double w, double h,
                              double start, double extent, int type);

  public void setArc(Point2D p, Dimension2D d,
                     double start, double extent, int type)
  {
    setArc(p.getX(), p.getY(), d.getWidth(), d.getHeight(),
           start, extent, type);
  }
  public void setArc(Rectangle2D r, double start, double extent, int type)
  {
    setArc(r.getX(), r.getY(), r.getWidth(), r.getHeight(),
           start, extent, type);
  }
  public void setArc(Arc2D a)
  {
    setArc(a.getX(), a.getY(), a.getWidth(), a.getHeight(),
           a.getAngleStart(), a.getAngleExtent(), a.getArcType());
  }
  public void setArcByCenter(double x, double y, double r,
                             double start, double extent, int type)
  {
    setArc(x - r, y - r, r + r, r + r, start, extent, type);
  }
  public void setArcByTangent(Point2D p1, Point2D p2, Point2D p3, double r)
  {
    // XXX Implement.
    throw new Error("not implemented");
  }

  /** Start in degrees. */
  public abstract void setAngleStart(double start);
  /** Extent in degrees. */
  public abstract void setAngleExtent(double extent);
  public void setAngleStart(Point2D p)
  {
    setAngleStart(Math.atan2(p.getY() - getCenterY(),
                             p.getX() - getCenterX()));
  }
  public void setAngles(double x1, double y1, double x2, double y2)
  {
    // XXX Implement.
    throw new Error("not implemented");
  }
  public void setAngles(Point2D p1, Point2D p2)
  {
    setAngles(p1.getX(), p1.getY(), p2.getX(), p2.getY());
  }
  public void setArcType(int type)
  {
    if (type < OPEN || type > PIE)
      throw new IllegalArgumentException();
    this.type = type;
  }
  public void setFrame(double x, double y, double w, double h)
  {
    // XXX Implement.
    throw new Error("not implemented");
  }
  public Rectangle2D getBounds2D()
  {
    // XXX Implement.
    throw new Error("not implemented");
  }
  protected abstract Rectangle2D makeBounds(double x, double y,
                                            double w, double h);
  public boolean containsAngle(double a)
  {
    // XXX Implement.
    throw new Error("not implemented");
  }
  public boolean contains(double x, double y)
  {
    // XXX Implement.
    throw new Error("not implemented");
  }
  public boolean intersects(double x, double y, double w, double h)
  {
    // XXX Implement.
    throw new Error("not implemented");
  }
  public boolean contains(double x, double y, double w, double h)
  {
    // XXX Implement.
    throw new Error("not implemented");
  }
  public boolean contains(Rectangle2D r)
  {
    return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
  }
  public PathIterator getPathIterator(AffineTransform at)
  {
    return new ArcIterator(this, at);
  }

  /**
   * STUBS ONLY
   * Used by Ellipse2D as well.
   */
  static final class ArcIterator implements PathIterator
  {
    /** Current coordinate. */
    private int current;
    /** Last iteration. */
    private final int limit;

    private final AffineTransform xform;

    private final double x;
    private final double y;
    private final double w;
    private final double h;
    /** Start in radians. */
    private final double start;
    /** Extent in radians. */
    private final double extent;
    private final int type;

    ArcIterator(Arc2D a, AffineTransform xform)
    {
      this.xform = xform;
      x = a.getX();
      y = a.getY();
      w = a.getWidth();
      h = a.getHeight();
      start = a.getAngleStart() * (Math.PI / 180);
      extent = a.getAngleExtent() * (Math.PI / 180);
      type = a.type;
      double e = extent < 0 ? -extent : extent;
      if (w < 0 || h < 0)
        limit = -1;
      else if (e == 0)
        limit = type;
      else if (e <= 90)
        limit = type + 1;
      else if (e <= 180)
        limit = type + 2;
      else if (e <= 270)
        limit = type + 3;
      else
        limit = type + 4;
    }

    ArcIterator(Ellipse2D e, AffineTransform xform)
    {
      this.xform = xform;
      x = e.getX();
      y = e.getY();
      w = e.getWidth();
      h = e.getHeight();
      start = 0;
      extent = -2 * Math.PI;
      type = CHORD;
      limit = (w < 0 || h < 0) ? -1 : 5;
    }

    public int getWindingRule()
    {
      return WIND_NON_ZERO;
    }

    public boolean isDone()
    {
      return current > limit;
    }

    public void next()
    {
      current++;
    }

    public int currentSegment(float[] coords)
    {
      if (current > limit)
        throw new NoSuchElementException("arc iterator out of bounds");
      if (current == 0)
        {
          // XXX Fill coords[0-1] with starting point.
          coords[0] = (float) 0;
          coords[1] = (float) 0;
          if (xform != null)
            xform.transform(coords, 0, coords, 0, 1);
          return SEG_MOVETO;
        }
      if (type != OPEN && current == limit)
        return SEG_CLOSE;
      if (type == PIE && current == limit - 1)
        {
          coords[0] = (float) (x + w / 2);
          coords[1] = (float) (y + h / 2);
          if (xform != null)
            xform.transform(coords, 0, coords, 0, 1);
          return SEG_LINETO;
        }
      // XXX Fill coords with 2 control points and next quarter point
      coords[0] = (float) 0;
      coords[1] = (float) 0;
      coords[2] = (float) 0;
      coords[3] = (float) 0;
      coords[4] = (float) 0;
      coords[5] = (float) 0;
      if (xform != null)
        xform.transform(coords, 0, coords, 0, 3);
      return SEG_CUBICTO;
    }

    public int currentSegment(double[] coords)
    {
      if (current > limit)
        throw new NoSuchElementException("arc iterator out of bounds");
      if (current == 0)
        {
          // XXX Fill coords[0-1] with starting point.
          coords[0] = 0;
          coords[1] = 0;
          if (xform != null)
            xform.transform(coords, 0, coords, 0, 1);
          return SEG_MOVETO;
        }
      if (type != OPEN && current == limit)
        return SEG_CLOSE;
      if (type == PIE && current == limit - 1)
        {
          coords[0] = (float) (x + w / 2);
          coords[1] = (float) (y + h / 2);
          if (xform != null)
            xform.transform(coords, 0, coords, 0, 1);
          return SEG_LINETO;
        }
      // XXX Fill coords with 2 control points and next quarter point
      coords[0] = 0;
      coords[1] = 0;
      coords[2] = 0;
      coords[3] = 0;
      coords[4] = 0;
      coords[5] = 0;
      if (xform != null)
        xform.transform(coords, 0, coords, 0, 3);
      return SEG_CUBICTO;
    }
  } // class ArcIterator

  /**
   * STUBS ONLY
   */
  public static class Double extends Arc2D
  {
    public double x;
    public double y;
    public double width;
    public double height;
    public double start;
    public double extent;

    public Double()
    {
      super(OPEN);
    }
    public Double(int type)
    {
      super(type);
    }
    public Double(double x, double y, double w, double h,
                  double start, double extent, int type)
    {
      super(type);
      this.x = x;
      this.y = y;
      width = w;
      height = h;
      this.start = start;
      this.extent = extent;
    }
      
    public Double(Rectangle2D r, double start, double extent, int type)
    {
      super(type);
      x = r.getX();
      y = r.getY();
      width = r.getWidth();
      height = r.getHeight();
      this.start = start;
      this.extent = extent;
    }

    public double getX()
    {
      return x;
    }
    public double getY()
    {
      return y;
    }
    public double getWidth()
    {
      return width;
    }
    public double getHeight()
    {
      return height;
    }
    public double getAngleStart()
    {
      return start;
    }
    public double getAngleExtent()
    {
      return extent;
    }
    public boolean isEmpty()
    {
      return width <= 0 || height <= 0;
    }
    public void setArc(double x, double y, double w, double h,
                       double start, double extent, int type)
    {
      this.x = x;
      this.y = y;
      width = w;
      height = h;
      this.start = start;
      this.extent = extent;
      setArcType(type);
    }
    public void setAngleStart(double start)
    {
      this.start = start;
    }
    public void setAngleExtent(double extent)
    {
      this.extent = extent;
    }
    protected Rectangle2D makeBounds(double x, double y, double w, double h)
    {
      return new Rectangle2D.Double(x, y, w, h);
    }
  } // class Double

  /**
   * STUBS ONLY
   */
  public static class Float extends Arc2D
  {
    public float x;
    public float y;
    public float width;
    public float height;
    public float start;
    public float extent;

    public Float()
    {
      super(OPEN);
    }
    public Float(int type)
    {
      super(type);
    }
    public Float(float x, float y, float w, float h,
                 float start, float extent, int type)
    {
      super(type);
      this.x = x;
      this.y = y;
      width = w;
      height = h;
      this.start = start;
      this.extent = extent;
    }
      
    public Float(Rectangle2D r, float start, float extent, int type)
    {
      super(type);
      x = (float) r.getX();
      y = (float) r.getY();
      width = (float) r.getWidth();
      height = (float) r.getHeight();
      this.start = start;
      this.extent = extent;
    }

    public double getX()
    {
      return x;
    }
    public double getY()
    {
      return y;
    }
    public double getWidth()
    {
      return width;
    }
    public double getHeight()
    {
      return height;
    }
    public double getAngleStart()
    {
      return start;
    }
    public double getAngleExtent()
    {
      return extent;
    }
    public boolean isEmpty()
    {
      return width <= 0 || height <= 0;
    }
    public void setArc(double x, double y, double w, double h,
                       double start, double extent, int type)
    {
      this.x = (float) x;
      this.y = (float) y;
      width = (float) w;
      height = (float) h;
      this.start = (float) start;
      this.extent = (float) extent;
      setArcType(type);
    }
    public void setAngleStart(double start)
    {
      this.start = (float) start;
    }
    public void setAngleExtent(double extent)
    {
      this.extent = (float) extent;
    }
    protected Rectangle2D makeBounds(double x, double y, double w, double h)
    {
      return new Rectangle2D.Float(x, y, w, h);
    }
  } // class Float
} // class Arc2D
