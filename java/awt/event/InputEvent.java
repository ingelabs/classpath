/*************************************************************************
/* InputEvent.java -- Common superclass of component input events.
/*
/* Copyright (c) 1999 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.awt.event;

import java.awt.Component;

/**
  * This is the common superclass for all component input classes.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class InputEvent extends ComponentEvent
                      implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the bit mask which indicates the shift key is down.
  */
public static final int SHIFT_MASK = 1;

/**
  * This is the bit mask which indicates the control key is down.
  */
public static final int CTRL_MASK = 2;

/**
  * This is the bit mask which indicates the meta key is down.
  */
public static final int META_MASK = 4;

/**
  * This is the bit mask which indicates the alt key is down.
  */
public static final int ALT_MASK = 8;

/**
  * This is the bit mask which indicates the alt-graph modifier is in effect.
  */
public static final int ALT_GRAPH_MASK = 32;

/**
  * This bit mask indicates mouse button one is down.
  */
public static final int BUTTON1_MASK = 16;

/**
  * This bit mask indicates mouse button two is down.
  */
public static final int BUTTON2_MASK = 8;

/**
  * This bit mask indicates mouse button three is down.
  */
public static final int BUTTON3_MASK = 4;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The timestamp when this event occurred.
  */
private long when;

/**
  * @serial The modifiers in effect for this event.
  */
protected int modifiers;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>InputEvent</code> with the 
  * specified source, id, timestamp, and modifiers.
  *
  * @param source The source of the event.
  * @param id The event id.
  * @param when The timestamp when the event occurred
  * @param modifiers The modifiers in effect for this event, which will be the 
  * union of the constant bitmasks in this class.
  */
protected
InputEvent(Component source, int id, long when, int modifiers)
{
  super(source, id);
  this.when = when;
  this.modifiers = modifiers;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the modifiers in effect for this event.  This will
  * be a union of the bit masks defined in this class that are applicable
  * to the event.
  *
  * @return The modifiers in effect for this event.
  */
public int
getModifiers()
{
  return(modifiers);
} 
  
/*************************************************************************/

/**
  * This method returns the timestamp when this event occurred.
  *
  * @return The timestamp when this event occurred.
  */
public long
getWhen()
{
  return(when);
}

/*************************************************************************/

/**
  * This method tests whether or not the shift key is down.
  *
  * @return <code>true</code> if the shift key is down, <code>false</code>
  * otherwise.
  */
public boolean
isShiftDown()
{
  return((getModifiers() & SHIFT_MASK) > 0);
}

/*************************************************************************/

/**
  * This method tests whether or not the control key is down.
  *
  * @return <code>true</code> if the control key is down, <code>false</code>
  * otherwise.
  */
public boolean
isControlDown()
{
  return((getModifiers() & CTRL_MASK) > 0);
}

/*************************************************************************/

/**
  * This method tests whether or not the meta key is down.
  *
  * @return <code>true</code> if the meta key is down, <code>false</code>
  * otherwise.
  */
public boolean
isMetaDown()
{
  return((getModifiers() & META_MASK) > 0);
}

/*************************************************************************/

/**
  * This method tests whether or not the alt key is down.
  *
  * @return <code>true</code> if the alt key is down, <code>false</code>
  * otherwise.
  */
public boolean
isAltDown()
{
  return((getModifiers() & ALT_MASK) > 0);
}

/*************************************************************************/

/**
  * This method tests whether or not the alt-graph modifier is in effect.
  *
  * @return <code>true</code> if the alt-graph modifier is in effect,
  * <code>false</code> otherwise.
  */
public boolean
isAltGraphDown()
{
  return((getModifiers() & ALT_GRAPH_MASK) > 0);
}

/*************************************************************************/

/**
  * Consumes this event.  A consumed event is not processed by the AWT
  * system.
  */
public void
consume()
{
  super.consume();
}
 
/*************************************************************************/

/**
  * This method tests whether or not this event has been consumed.
  *
  * @return <code>true</code> if this event has been consumed, 
  * <code>false</code> otherwise.
  */
public boolean
isConsumed()
{
  return(super.isConsumed());
}

} // class InputEvent

