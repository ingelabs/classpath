/* ComponentEvent.java -- Notification events for components
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

import java.awt.Component;

/**
  * This class is for events generated when a component is moved,
  * resized, hidden, or shown.  These events normally do not need to be
  * handled by the application, since the AWT system automatically takes
  * care of them.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ComponentEvent extends java.awt.AWTEvent
             implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the first id in the range of ids used by this class.
  */
public static final int COMPONENT_FIRST = 100;

/**
  * This is the last id in the range of ids used by this class.
  */
public static final int COMPONENT_LAST = 103;

/**
  * This id indicates that a component was moved.
  */
public static final int COMPONENT_MOVED = 100;

/**
  * This id indicates that a component was resized.
  */
public static final int COMPONENT_RESIZED = 101;

/**
  * This id indicates that a component was shown.
  */
public static final int COMPONENT_SHOWN = 102;

/**
  * This id indicates that a component was hidden.
  */
public static final int COMPONENT_HIDDEN = 103;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>ComponentEvent</code> with the
  * specified source and id.
  *
  * @param source The source of the event.
  * @param id The event id.
  */
public
ComponentEvent(Component source, int id)
{
  super(source, id);
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This method returns the event source as a <code>Component</code>.
  *
  * @return The event source as a <code>Component</code>.
  */
public Component
getComponent()
{
  return((Component)getSource());
}

/*************************************************************************/

/**
  * This method returns a string identifying this event.
  *
  * @return A string identifying this event.
  */
public String
paramString()
{
  return(getClass().getName() + " source= " + getSource() + " id=" + getID());
}

} // class ComponentEvent

