/* AWTEvent.java -- Toplevel AWT event class
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
  * 
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class AWTEvent extends java.util.EventObject 
       implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * Mask for selecting component events.
  */
public static final int COMPONENT_EVENT_MASK = 0x001;

/**
  * Mask for selecting container events.
  */
public static final int CONTAINER_EVENT_MASK = 0x002;

/**
  * Mask for selecting component focus events.
  */
public static final int FOCUS_EVENT_MASK = 0x004;

/**
  * Mask for selecting keyboard events.
  */
public static final int KEY_EVENT_MASK = 0x008;

/**
  * Mask for mouse button events.
  */
public static final int MOUSE_EVENT_MASK = 0x010;

/**
  * Mask for mouse motion events.
  */
public static final int MOUSE_MOTION_EVENT_MASK = 0x020;

/**
  * Mask for window events.
  */
public static final int WINDOW_EVENT_MASK = 0x040;

/**
  * Mask for action events.
  */
public static final int ACTION_EVENT_MASK = 0x080;

/**
  * Mask for adjustment events.
  */
public static final int ADJUSTMENT_EVENT_MASK = 0x100;

/**
  * Mask for item events.
  */
public static final int ITEM_EVENT_MASK = 0x200;

/**
  * Mask for text events.
  */
public static final int TEXT_EVENT_MASK = 0x400;

/**
  * This is the highest number for event ids that are reserved for use by
  * the AWT system itself.
  */
public static final int RESERVED_ID_MAX = 1999;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The identifier number of this event.
  */
protected int id;

/**
  * @serial Indicates whether or not this event has been consumed.
  */
protected boolean consumed;

// Used by EventQueue
AWTEvent next;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>AWTEvent</code> from the
  * specified Java 1.0 event object.
  *
  * @param event The Java 1.0 event to initialize from.
  */
public
AWTEvent(Event event)
{
  this(event.target, event.id);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>AWTEvent</code> with the specified
  * source and id.
  *
  * @param source The object that caused the event.
  * @param id The event id.
  */
public
AWTEvent(Object source, int id)
{
  super(source);
  this.id = id;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the id number of this event.
  *
  * @return The id number of this event.
  */
public int
getID()
{
  return(id);
}

/*************************************************************************/

/**
  * Tests whether not not this event has been consumed.  A consumed event
  * is not processed in the default manner.
  *
  * @return <code>true</code> if this event has been consumed, 
  * <code>false</code> otherwise.
  */
public boolean
isConsumed()
{
  return(consumed);
}

/*************************************************************************/

/**
  * Consumes this event so that it will not be processed in the default
  * manner.
  */
protected void
consume()
{
  consumed = true;
}

/*************************************************************************/

/**
  * Returns a string representation of this event.
  *
  * @return A string representation of this event.
  */
public String
paramString()
{
  return(toString());
}

/*************************************************************************/

/**
  * Returns a string representation of this event.
  *
  * @return A string representation of this event.
  */
public String
toString()
{
  return(getClass().getName() + "(id=" + getID() + ")");
}

} // class AWTEvent

