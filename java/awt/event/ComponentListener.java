/* ComponentListener.java -- Receive all events for a component.
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
  * This interface is for classes that receive all events from a component.
  * Normally it is not necessary to process these events since the AWT
  * handles them internally, taking all appropriate actions.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface ComponentListener extends java.util.EventListener
{

/**
  * This method is called when the component is resized.
  *
  * @param event The <code>ComponentEvent</code> indicating the resize.
  */
public abstract void 
componentResized(ComponentEvent event);

/*************************************************************************/

/**
  * This method is called when the component is moved.
  *
  * @param event The <code>ComponentEvent</code> indicating the move.
  */
public abstract void
componentMoved(ComponentEvent event);

/*************************************************************************/

/**
  * This method is called when the component is made visible.
  *
  * @param event The <code>ComponentEvent</code> indicating the visibility
  * change.
  */
public abstract void
componentShown(ComponentEvent event);
 
/*************************************************************************/

/**
  * This method is called when the component is hidden.
  *
  * @param event The <code>ComponentEvent</code> indicating the visibility
  * change.
  */
public abstract void
componentHidden(ComponentEvent event);

} // interface ComponentListener 

