/* ContainerAdapter.java -- Convenience class for writing container listeners
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
  * This is a convenience class for writing container listeners.  It
  * implements the <code>ContainerListner</code> interface with empty
  * method bodies.  This allows a listener to subclass this class and
  * override only the methods of interest.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class ContainerAdapter implements ContainerListener
{

/*
 * Constructors
 */

/**
  * Do nothing default constructor for subclasses.
  */
public
ContainerAdapter()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Implements this method from the interface with an empty body.
  *
  * @param event Ignored
  */
public void
componentAdded(ContainerEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method from the interface with an empty body.
  *
  * @param event Ignored
  */
public void
componentRemoved(ContainerEvent event)
{
}

} // class ContainerAdapter 

