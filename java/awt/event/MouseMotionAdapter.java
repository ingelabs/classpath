/* MouseMotionAdapter.java -- Convenience class for mouse motion listeners
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


package java.awt.event;

/**
  * This is a convenience class for writing mouse motion listeners.  It
  * implements the <code>MouseMotionListener</code> interface with empty
  * method bodies.  This allows a subclass to override only those methods
  * of interest.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class MouseMotionAdapter implements MouseMotionListener
{

/*
 * Constructor
 */

/**
  * Do nothing default constructor for subclasses.
  */
public
MouseMotionAdapter()
{
}

/*************************************************************************/

/*
 * Instance Methods 
 */

/**
  * Implement this method in the interface with an empty body.
  *
  * @param event Ignored.
  */
public void
mouseDragged(MouseEvent event)
{
}

/*************************************************************************/

/**
  * Implement this method in the interface with an empty body.
  *
  * @param event Ignored.
  */
public void
mouseMoved(MouseEvent event)
{
}

} // class MouseMotionAdapter

