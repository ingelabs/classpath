/* Canvas.java -- An AWT canvas widget
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

import java.awt.peer.CanvasPeer;
import java.awt.peer.ComponentPeer;

/**
  * This class implements an AWT canvas widget.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Canvas extends Component implements java.io.Serializable
{

/*
 * Static Variables
 */

// Serialization constant
private static final long serialVersionUID = -2284879212465893870L;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Canvas</code>.
  */
public 
Canvas()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Creates the native peer for this object.
  */
public void
addNotify()
{
  if (getPeer() != null)
    return;

  setPeer((ComponentPeer)getToolkit().createCanvas(this));
}

/*************************************************************************/

/**
  * Repaints the canvas window.  This method should be overriden by 
  * a subclass to do something useful, as this method simply paints
  * the window with the background color.
  */
public void
paint(Graphics graphics)
{
  Dimension dim = getSize();

  graphics.setColor(getBackground());
  graphics.fillRect(0, 0, dim.width, dim.height);
}

} // class Canvas 

