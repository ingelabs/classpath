/* Point.java -- Java representation of a point on a screen.
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
  * This class represents a point on the screen using cartesian coordinates.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Point implements java.io.Serializable
{

/*
 * Instance Variables
 */

/**
  * @serial The X coordinate of the point.
  */
public int x;

/**
  * @serial The Y coordinate of the point.
  */
public int y;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Point</code> representing the
  * coordiates (0,0).
  */
public
Point()
{
  this(0, 0);
}
 
/*************************************************************************/

/**
  * Initializes a new instance of <code>Point</code> with coordinates
  * identical to the coordinates of the specified points.
  *
  * @param point The point to copy the coordinates from.
  */
public
Point(Point point)
{
  this(point.x, point.y);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Point</code> with the specified
  * coordinates.
  *
  * @param x The X coordinate of this point.
  * @param y The Y coordinate of this point.
  */
public 
Point(int x, int y)
{
  this.x = x;
  this.y = y;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the location of this object as a point.  A pretty useless
  * method.  It is included to mimic the <code>getLocation</code> method
  * in component.
  *
  * @return This point.
  */
public Point
getLocation()
{
  return(this);
}

/*************************************************************************/

/**
  * Sets this object's coordinates to match those of the specified point.
  *
  * @param point The point to copy the coordinates from.
  */
public void
setLocation(Point point)
{
  this.x = point.x;
  this.y = point.y;
}

/*************************************************************************/

/**
  * Sets this object's coordinates to the specified values.  This method
  * is identical to the <code>move()</code> method.
  *
  * @param x The new X coordinate.
  * @param y The new Y coordinate.
  */
public void
setLocation(int x, int y)
{
  this.x = x;
  this.y = x;
}

/*************************************************************************/

/**
  * Sets this object's coordinates to the specified values.  This method
  * is identical to the <code>setLocation(int, int)</code> method.
  *
  * @param x The new X coordinate.
  * @param y The new Y coordinate.
  */
public void
move(int x, int y)
{
  this.x = x;
  this.y = y;
}

/*************************************************************************/

/**
  * Changes the coordinates of this point such that the specified 
  * <code>dx</code> parameter is added to the existing X coordinate and
  * <code>dy</code> is added to the existing Y coordinate.
  *
  * @param dx The amount to add to the X coordinate.
  * @param dy The amount to add to the Y coordinate.
  */
public void
translate(int dx, int dy)
{
  x += dx;
  y += dy;
}

/*************************************************************************/

/**
  * Returns a hash value for this point.
  *
  * @param A hash value for this point.
  */
public int
hashCode()
{
  return(((x*y)*(x*y)) % 37);
}

/*************************************************************************/

/**
  * Tests whether or not this object is equal to the specified object.
  * This will be true if and only if the specified objectj:
  * <p>
  * <ul>
  * <li>Is not <code>null</code>.
  * <li>Is an instance of <code>Point</code>.
  * <li>Has X and Y coordinates equal to this object's.
  * </ul>
  *
  * @param obj The object to test against for equality.
  *
  * @return <code>true</code> if the specified object is equal to this
  * object, <code>false</code> otherwise.
  */
public boolean
equals(Object obj)
{
  if (obj == null)
    return(false);

  if (!(obj instanceof Point))
    return(false);

  Point p = (Point)obj;

  if (p.x != x)
    return(false);

  if (p.y != y)
    return(false);

  return(true);
}

/*************************************************************************/

/**
  * Returns a string representation of this object.
  *
  * @return A string representation of this object.
  */
public String
toString()
{
  return(getClass().getName() + ": (" + x + "," + y + ")");
}

} // class Point

