/* ContainerListener.java -- Listen for container events.
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
  * This interface is for classes that wish to listen for all events from
  * container objects.  This is normally not necessary since the AWT system
  * listens for and processes these events.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface ContainerListener extends java.util.EventListener
{

/**
  * This method is called when a component is added to the container.
  *
  * @param event The <code>ContainerEvent</code> indicating that the 
  * component was added.
  */
public abstract void
componentAdded(ContainerEvent event);

/*************************************************************************/

/**
  * This method is called when a component is removed from the container.
  *
  * @param event The <code>ContainerEvent</code> indicating that the 
  * component was removed.
  */
public abstract void
componentRemoved(ContainerEvent event); 

} // interface ContainerListener

