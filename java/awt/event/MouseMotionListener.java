/* MouseMotionListener.java -- Listen to mouse motion events.
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
  * This interface is for classes that wish to be notified of mouse movements.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface MouseMotionListener extends java.util.EventListener
{

/**
  * This method is called when the mouse is moved over a component
  * while the button has been pressed.
  *
  * @param event The <code>MouseEvent</code> indicating the motion.
  */
public abstract void
mouseDragged(MouseEvent event);

/*************************************************************************/

/**
  * This method is called when the mouse is moved over a component
  * while the button is not pressed.
  *
  * @param event The <code>MouseEvent</code> indicating the motion.
  */
public abstract void
mouseMoved(MouseEvent event);

} // interface MouseMotionListener

