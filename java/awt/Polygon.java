/* Polygon.java -- Class representing a polygon
   Copyright (C) 1999 Free Software Foundation, Inc.

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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.awt;

/**
  * This class represents a polygon
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Polygon implements Shape, java.io.Serializable
{

/*
 * Instance Variables
 */

/**
  * This total number of endpoints 
  */
public int npoints;

/**
  * The array of X coordinates of endpoints.
  */
public int xpoints[];

/**
  * The array of Y coordinates of endpoints.
  */
public int ypoints[];

/**
  * The bounding box of this polygon
  */
protected Rectangle bounds;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Polygon</code> that is empty.
  */
public
Polygon()
{
  xpoints = new int[0];
  ypoints = new int[0];

  bounds = new Rectangle(0,0,0,0);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Polygon</code> that has the
  * specified endpoints.
  *
  * @param xpoints The array of X coordinates for this polygon.
  * @param ypoints The array of Y coordinates for this polygon.
  * @param npoints The total number of endpoints in this polygon.
  *
  * @exception NegativeArraySizeException If <code>npoints</code> is negative.
  */
public
Polygon(int[] xpoints, int[] ypoints, int npoints)
{
  if (npoints < 0)
    throw new NegativeArraySizeException();

  this.xpoints = xpoints;
  this.ypoints = ypoints;
  this.npoints = npoints;

  calculateBounds();
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Calculates the bounding rectangle of this polygon.
  */
public void
calculateBounds()
{
  int minx = xpoints[0], maxx = xpoints[0];
  int miny = ypoints[0], maxy = ypoints[0];

  for (int i = 0; i < npoints; i++)
    {
      if (xpoints[i] < minx)
        minx = xpoints[i];

      if (xpoints[i] > maxx)
        maxx = xpoints[i];

      if (ypoints[i] < miny)
        miny = ypoints[i];

      if (ypoints[i] > maxy)
        maxy = ypoints[i];
    }

  bounds = new Rectangle(minx, maxy, maxx-minx, maxy-miny);
}

/*************************************************************************/

/**
  * Translates the polygon by adding the specified values to all X and Y
  * coordinates.
  *
  * @param dx The amount to add to all X coordinates.
  * @param dy The amount to add to all Y coordinates.
  */
public void
translate(int dx, int dy)
{
  for (int i = 0; i < npoints; i++)
    {
      xpoints[i] += dx;
      xpoints[i] += dy;
    }

  calculateBounds();
}

/*************************************************************************/

/**
  * Adds the specified endpoint to the polygon.
  *
  * @param x The X coordinate of the point to add.
  * @param y The Y coordiante of the point to add.
  */
public void
addPoint(int x, int y)
{
  int newxpoints[] = new int[npoints + 1];
  int newypoints[] = new int[npoints + 1];

  System.arraycopy(xpoints, 0, newxpoints, 0, npoints);
  System.arraycopy(ypoints, 0, newypoints, 0, npoints);

  newxpoints[npoints] = x;
  newypoints[npoints] = y;

  xpoints = newxpoints;
  ypoints = newypoints;
  ++npoints;
}

/*************************************************************************/

/**
  * Returns the bounding box of this polygon.  This is the smallest 
  * rectangle with sides parallel to the X axis that will contain this
  * polygon.
  *
  * @return The bounding box for this polygon.
  */
public Rectangle
getBounds()
{
  return(bounds);
}

/*************************************************************************/

/**
  * Returns the bounding box of this polygon.  This is the smallest 
  * rectangle with sides parallel to the X axis that will contain this
  * polygon.
  *
  * @return The bounding box for this polygon.
  *
  * @deprecated This method has been replaced by <code>getBounds()</code>.
  */
public Rectangle
getBoundingBox()
{
  return(bounds);
}

/*************************************************************************/

/**
  * Tests whether or not the specified point is inside this polygon.
  *
  * @param x The X coordinate of the point to test.
  * @param y the Y coordinate of the point to test.
  *
  * @return <code>true</code> if the point is inside this polygon,
  * <code>false</code> otherwise.
  */
public boolean
contains(int x, int y)
{
  // Is inside bounding box.
  if (!bounds.contains(x, y))
    return(false);

  int sign = 0;
  for (int i = 0; i < npoints; i ++)
    {
      int nx = xpoints[(i + 1) % npoints] - xpoints[i];
      int ny = ypoints[(i + 1) % npoints] - ypoints[i];

      int dx = x - xpoints[i];
      int dy = y - ypoints[i];

      int val = (dx*nx) + (dy*nx);

      if (sign == 0)
        {
          if (val < 1)
            sign = -1;
          else if (val > 1)
            sign = 1;
        }

      if ((val > 1) && (sign < 1))
        return(false);
      if ((val < 1) && (sign > 1))
        return(false);
    }

  return(true);
}

/*************************************************************************/

/**
  * Tests whether or not the specified point is inside this polygon.
  *
  * @param x The X coordinate of the point to test.
  * @param y the Y coordinate of the point to test.
  *
  * @return <code>true</code> if the point is inside this polygon,
  * <code>false</code> otherwise.
  *
  * @deprecated This method has been replaced by <code>contains()</code>.
  */
public boolean
inside(int x, int y)
{
  return(contains(x, y));
}

} // class Polygon 

