/* TextEvent.java -- Event for text changes
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
  * This event is generated when text changes for an object.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class TextEvent extends java.awt.AWTEvent implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the first id in the range of event ids used by this class.
  */
public static final int TEXT_FIRST = 900;

/**
  * This is the last id in the range of event ids used by this class.
  */
public static final int TEXT_LAST = 900;

/**
  * This event id indicates that the text of an object has changed.
  */
public static final int TEXT_VALUE_CHANGED = 900;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>TextEvent</code> with the
  * specified source and id.
  *
  * @param source The object that generated this event.
  * @param id The event id for this event.
  */
public
TextEvent(Object source, int id)
{
  super(source, id);
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * Returns a string identifying this event.
  *
  * @return A string identifying this event.
  */
public String
paramString()
{
  return(getClass().getName() + " source=" + getSource() + " id=" + getID());
}

} // class TextEvent

