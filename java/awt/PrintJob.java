/* PrintJob.java -- A print job class
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
  * This abstract class represents a print job.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class PrintJob
{

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>PrintJob</code>.
  */
public
PrintJob()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns a graphics context suitable for rendering the next page.
  *
  * @return A graphics context for printing the next page.
  */
public abstract Graphics
getGraphics();

/*************************************************************************/

/**
  * Returns the dimension of the page in pixels.  The resolution will be
  * chosen to be similar to the on screen image.
  *
  * @return The page dimensions.
  */
public abstract Dimension
getPageDimension();

/*************************************************************************/

/**
  * Returns the resolution of the page in pixels per inch.
  *
  * @return The resolution of the page in pixels per inch.
  */
public abstract int
getPageResolution();

/*************************************************************************/

/**
  * Tests whether or not the last page will be printed first.
  *
  * @return <code>true</code> if the last page prints first, <code>false</code>
  * otherwise.
  */
public abstract boolean
lastPageFirst();

/*************************************************************************/

/**
  * Informs the print job that printing is complete.
  */
public abstract void
end();

/*************************************************************************/

/**
  * This method explicitly ends the print job in the event the job
  * becomes un-referenced without the application having done so.
  */
public void
finalize()
{
  end();
}

} // class PrintJob

