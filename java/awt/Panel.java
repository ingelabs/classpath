/* Panel.java -- Simple container object.
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

import java.awt.peer.PanelPeer;
import java.awt.peer.ContainerPeer;
import java.awt.peer.ComponentPeer;

/**
  * A panel is a simple container class. 
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Panel extends Container implements java.io.Serializable
{

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Panel</code> that has a default
  * layout manager of <code>FlowLayout</code>.
  */
public
Panel()
{
  this(new FlowLayout());
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Panel</code> with the specified
  * layout manager.
  *
  * @param layoutManager The layout manager for this object.
  */
public
Panel(LayoutManager layoutManager)
{
  setLayout(layoutManager);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Notifies this object to create its native peer.
  */
public void
addNotify()
{
  if (getPeer() != null)
    return;

  setPeer((ComponentPeer)getToolkit().createPanel(this));
}

} // class Panel 

