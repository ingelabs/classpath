/* Rectangle.java -- Class representing a rectangle.
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of the non-peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


package java.awt;

/**
  * This class represents a rectangle and all the interesting things you
  * might want to do with it.  Note that the coordinate system uses
  * the origin (0,0) as the top left of the screen, with the x and y
  * values increasing as they move to the right and down respectively.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Rectangle implements Shape, java.io.Serializable
{

/*
 * Instance Variables
 */

/**
  * The X coordinate of the top-left corner of the rectangle.
  */
public int x;

/**
  * The Y coordinate of the top-left corner of the rectangle;
  */
public int y;

/**
  * The width of the rectangle
  */
public int width;

/**
  * The height of the rectangle
  */
public int height;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Rectangle</code> with a top
  * left corner at (0,0) and a width and heigth of 0.
  */
public
Rectangle()
{
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Rectangle</code> from the
  * coordinates of the specified rectangle.
  *
  * @param rect The rectangle to copy from.
  */
public
Rectangle(Rectangle rect)
{
  this.x = rect.x;
  this.y = rect.y;
  this.width = rect.width;
  this.height = rect.height;
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Rectangle</code> from the specified
  * inputs.
  *
  * @param x The X coordinate of the top left corner of the rectangle.
  * @param y The Y coordinate of the top left corner of the rectangle.
  * @param width The width of the rectangle.
  * @param height The height of the rectangle.
  */
public
Rectangle(int x, int y, int width, int height)
{
  this.x = x;
  this.y = y;
  this.width = width;
  this.height = height;
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Rectangle</code> with the specified
  * width and height.  The upper left corner of the rectangle will be at
  * the origin (0,0).
  *
  * @param width The width of the rectangle.
  * @param height the height of the rectange.
  */
public
Rectangle(int width, int height)
{
  this(0, 0, width, height);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Rectangle</code> with a top-left
  * corner represented by the specified point and the width and height
  * represented by the specified dimension.
  *
  * @param point The upper left corner of the rectangle.
  * @param dim The width and height of the rectangle.
  */
public 
Rectangle(Point point, Dimension dim)
{
  this(point.x, point.y, dim.width, dim.height);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Rectangle</code> with a top left
  * corner at the specified point and a width and height of zero.
  *
  * @param poin The upper left corner of the rectangle.
  */
public
Rectangle(Point point)
{
  this(point.x, point.y, 0, 0);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Rectangle</code> with an
  * upper left corner at the origin (0,0) and a width and height represented
  * by the specified dimension.
  *
  * @param dim The width and height of the rectangle.
  */
public
Rectangle(Dimension dim)
{
  this(0, 0, dim.width, dim.height);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the bounding rectangle for this rectangle, which is simply
  * this rectange itself.
  *
  * @return This rectangle.
  */
public Rectangle
getBounds()
{
  return(this);
}

/*************************************************************************/

/**
  * Updates this rectangle to match the dimensions of the specified 
  * rectangle.
  *
  * @param rect The rectangle to update from.
  */
public void
setBounds(Rectangle rect)
{
  synchronized(this)
    {
      this.x = rect.x;
      this.y = rect.y;
      this.width = rect.width;
      this.height = rect.height; 
    }
}

/*************************************************************************/

/**
  * Updates this rectangle to have the specified dimensions.
  *
  * @param x The new X coordinate of the upper left hand corner.
  * @param y The new Y coordinate of the upper left hand corner.
  * @param width The new width of this rectangle.
  * @param height The new height of this rectangle.
  */
public void
setBounds(int x, int y, int width, int height)
{
  synchronized(this)
    {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }
}

/*************************************************************************/

/**
  * Updates this rectangle to have the specified dimensions.
  *
  * @param x The new X coordinate of the upper left hand corner.
  * @param y The new Y coordinate of the upper left hand corner.
  * @param width The new width of this rectangle.
  * @param height The new height of this rectangle.
  *
  * @deprecated This method is deprecated in favor of 
  * <code>setBounds(int, int, int, int)</code>.
  */
public void
reshape(int x, int y, int width, int height)
{
  setBounds(x, y, width, height);
}

/*************************************************************************/

/**
  * Returns the location of this rectangle, which is the coordinates of
  * its upper left corner.
  * // FIXME: Is this true?
  *
  * @return The point where this rectangle is located.
  */
public Point
getLocation()
{
  return(new Point(x, y));
}

/*************************************************************************/

/**
  * Moves the location of this rectangle by setting its upper left
  * corner to the specified point.
  * // FIXME: Is this true?
  *
  * @param point The point to move the rectange to.
  */
public void
setLocation(Point point)
{
  synchronized(this)
    {
      this.x = point.x;
      this.y = point.y;
    }
}

/*************************************************************************/

/**
  * Moves the location of this rectangle by setting its upper left
  * corner to the specified coordinates.
  * // FIXME: Is this true?
  *
  * @param x The new X coordinate for this rectangle.
  * @param y The new Y coordinate for this rectangle.
  */
public void
setLocation(int x, int y)
{
  synchronized(this)
    {
      this.x = x;
      this.y = y;
    }
}

/*************************************************************************/

/**
  * Moves the location of this rectangle by setting its upper left
  * corner to the specified coordinates.
  * // FIXME: Is this true?
  *
  * @param x The new X coordinate for this rectangle.
  * @param y The new Y coordinate for this rectangle.
  *
  * @deprecated This method is deprecated in favor of
  * <code>setLocation(int, int)</code>.
  */
public void
move(int x, int y)
{
  setLocation(x, y);
}

/*************************************************************************/

/**
  * Returns the size of this rectangle.
  *
  * @return The size of this rectangle.
  */
public Dimension
getSize()
{
  return(new Dimension(width, height));
}

/*************************************************************************/

/**
  * Sets the size of this rectangle based on the specified dimensions.
  *
  * @param dim The new dimensions of the rectangle.
  */
public void
setSize(Dimension dim)
{
  synchronized(this)
    {
      this.width = dim.width;
      this.height = dim.height;
    }
}

/*************************************************************************/

/**
  * Sets the size of this rectangle based on the specified dimensions.
  *
  * @param width The new width of the rectangle.
  * @param height The new height of the rectangle.
  */
public void
setSize(int width, int height)
{
  synchronized(this)
    {
      this.width = width;
      this.height = height;
    }
}

public void
translate (int dx, int dy)
{
  synchronized (this)
    {
      this.x += dx;
      this.y += dy;
    }
}

/*************************************************************************/

/**
  * Sets the size of this rectangle based on the specified dimensions.
  *
  * @param width The new width of the rectangle.
  * @param height The new height of the rectangle.
  *
  * @deprecated This method is deprecated in favor of
  * <code>setSize(int, int)</code>.
  */
public void
resize(int width, int height)
{
  setSize(width, height);
}

/*************************************************************************/

/**
  * Tests whether or not the specified point is inside this rectangle.
  *
  * @param point The point to test.
  *
  * @return <code>true</code> if the point is inside the rectangle,
  * <code>false</code> otherwise.
  */
public boolean
contains(Point point)
{
  return(contains(point.x, point.y));
}

/*************************************************************************/

/**
  * Tests whether or not the specified point is inside this rectangle.
  *
  * @param x The X coordinate of the point to test.
  * @param y The Y coordinate of the point to test.
  *
  * @return <code>true</code> if the point is inside the rectangle,
  * <code>false</code> otherwise.
  */
public boolean
contains(int x, int y)
{
  // FIXME: Do we count points on the rectangle?
  if ((x < this.x) || (x > (this.x + width)))
    return(false);
  if ((y < this.y) || (y > (this.y + height)))
    return(false);

  return(true);
}

/*************************************************************************/

/**
  * Tests whether or not the specified point is inside this rectangle.
  *
  * @param x The X coordinate of the point to test.
  * @param y The Y coordinate of the point to test.
  *
  * @return <code>true</code> if the point is inside the rectangle,
  * <code>false</code> otherwise.
  *
  * @deprecated This method is deprecated in favor of
  * <code>contains(int, int)</code>.
  */
public boolean
inside(int x, int y)
{
  return(contains(x, y));
}

/*************************************************************************/

/**
  * Tests whether or not the specified rectangle intersects this rectangle.
  *
  * @param rect The rectangle to test against.
  *
  * @return <code>true</code> if the specified rectangle intersects this
  * one, <code>false</code> otherwise.
  */
public boolean
intersects(Rectangle rect)
{
  if (contains(rect.x, rect.y))
    return(true);
  if (contains(rect.x + width, rect.y))
    return(true);
  if (contains(rect.x, rect.y + height))
    return(true);
  if (contains(rect.x + width, rect.y + height))
    return(true);

  return(false);
}

/*************************************************************************/

/**
  * Determines the rectange which is formed by the intersection of this
  * rectangle with the specified rectangle.
  *
  * @param rect The rectange to calculate the intersection with.
  *
  * @return The rectangle bounding the intersection.
  */
public Rectangle
intersection(Rectangle rect)
{
  // FIXME: What do we do if there is no intersection?  I'll return an
  // empty rectangle.  
  if (!intersects(rect))
    return(new Rectangle());

  int nx, ny, nw, nh;

  if (x > rect.x)
    nx = x;
  else
    nx = rect.x;

  if (y > rect.y)
    ny = y;
  else
    ny = rect.y;

  if ((x + width) < (rect.x + rect.width))
    nw = (x + width) - nx;
  else
    nw = (rect.x + rect.width) - nx;

  if ((y + height) < (rect.y + rect.height))
    nh = (y + height) - ny;
  else
    nh = (rect.y + rect.height) - ny;

  return(new Rectangle(nx, ny, nw, nh));
}

/*************************************************************************/

/**
  * Returns the smallest rectangle that contains both this rectangle
  * and the specified rectangle.
  *
  * @param rect The rectangle to compute the union with.
  *
  * @return The smallest rectangle containing both rectangles.
  */
public Rectangle
union(Rectangle rect)
{
  int nx, ny, nw, nh;

  if (x < rect.x)
    nx = x;
  else
    nx = rect.x;

  if (y < rect.y)
    ny = y;
  else
    ny = rect.y;

  if ((x + width) > (rect.x + rect.width))
    nw = (x + width) - nx;
  else
    nw = (rect.x + rect.width) - nx;

  if ((y - height) > (rect.y - rect.height))
    nh = (y + height) - ny;
  else
    nh = (rect.y + rect.height) - ny;

  return(new Rectangle(nx, ny, nw, nh));
}

/*************************************************************************/

/**
  * Modifies this rectangle so that it represents the smallest rectangle 
  * that contains both the existing rectangle and the specified point.
  *
  * @param point The point to add to this rectangle.
  */
public void
add(Point point)
{
  add(new Rectangle(point));
}

/*************************************************************************/

/**
  * Modifies this rectangle so that it represents the smallest rectangle 
  * that contains both the existing rectangle and the specified point.
  *
  * @param x The X coordinate of the point to add to this rectangle.
  * @param y The Y coordinate of the point to add to this rectangle.
  */
public void
add(int x, int y)
{
  add(new Rectangle(new Point(x, y)));
}

/*************************************************************************/

/**
  * Modifies this rectangle so that it represents the smallest rectangle 
  * that contains both the existing rectangle and the specified rectangle.
  *
  * @param rect The rectangle to add to this rectangle.
  */
public void
add(Rectangle rect)
{
  Rectangle newrect = union(rect);

  synchronized(this)
    {
      this.x = newrect.x;
      this.y = newrect.y;
      this.width = newrect.width;
      this.height = newrect.height;
    }
}

/*************************************************************************/

/**
  * Expands the rectangle by the specified amount.  The horizontal
  * and vertical expansion values are applied both to the X,Y coordinate
  * of this rectangle, and its width and height.  Thus the width and
  * height will increase by 2h and 2v accordingly.
  *
  * @param h The horizontal expansion value.
  * @param v The vertical expansion value.
  */
public void
grow(int h, int v)
{
  synchronized(this)
    {
      x -= h;
      y -= v;
      width += (2*h);
      height += (2*h);
    }
}

/*************************************************************************/

/**
  * Tests whether or not this rectangle is empty.  An empty rectangle
  * has a width or height of zero.
  *
  * @return <code>true</code> if the rectangle is empty, <code>false</code>
  * otherwise.
  */
public boolean
isEmpty()
{
  if ((width <= 0) || (height <= 0))
    return(true);
  else
    return(false);
}

/*************************************************************************/

/**
  * Tests this rectangle for equality against the specified object.  This
  * will be true if an only if the specified object:
  * <p>
  * <ul>
  * <li>Is not <code>null</code>.
  * <li>Is an instance of <code>Rectangle</code>.
  * <li>Has X and Y coordinates identical to this rectangle.
  * <li>Has a width and height identical to this rectangle.
  * </ul>
  *
  * @param obj The object to test against for equality.
  *
  * @return <code>true</code> if the specified object is equal to this one,
  * <code>false</code> otherwise.
  */
public boolean
equals(Object obj)
{
  if (obj == null)
    return(false);

  if (!(obj instanceof Rectangle))
    return(false);

  Rectangle rect = (Rectangle)obj;

  if ((rect.x != x) || (rect.y != y) || (rect.width != width) ||
      (rect.height != height))
    return(false);

  return(true);
}
 
/*************************************************************************/

/**
  * Returns a hash value for this object.
  *
  * @return A hash value for this object.
  */
public int
hashCode()
{
  return(x*y*width*height*37);
}

/*************************************************************************/

/**
  * Returns a string representation of this rectangle.
  *
  * @return A string representation of this rectangle.
  */
public String
toString()
{
  return(getClass().getName() + "(x=" + x + ", y=" + y + ", width=" +
         width + ", height=" + height + ")");
}

} // class Rectangle 

