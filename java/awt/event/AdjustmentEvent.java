/* AdjustmentEvent.java -- An adjustable value was changed.
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

import java.awt.Adjustable;

/**
  * This class represents an event that is generated when an adjustable
  * value is changed.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class AdjustmentEvent extends java.awt.AWTEvent
             implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the first id in the range of ids used by adjustment events.
  */
public static final int ADJUSTMENT_FIRST = 601;

/**
  * This is the last id in the range of ids used by adjustment events.
  */
public static final int ADJUSTMENT_LAST = 601;

/**
  * This is the id indicating an adjustment value changed.
  */
public static final int ADJUSTMENT_VALUE_CHANGED = 601;

/**
  * Adjustment type for unit increments
  */
public static final int UNIT_INCREMENT = 1;

/**
  * Adjustment type for unit decrements
  */
public static final int UNIT_DECREMENT = 2;

/**
  * Adjustment type for block decrements
  */
public static final int BLOCK_DECREMENT = 3;

/**
  * Adjustment type for block increments
  */
public static final int BLOCK_INCREMENT = 4;

/**
  * Adjustment type for tracking adjustments
  */
public static final int TRACK = 5;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The object that caused the event.
  */ 
private Adjustable adjustable;

/**
  * @serial The adjustment type
  */
private int adjustmentType;

/**
  * @serial The adjustment value
  */ 
private int value;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes an instance of <code>AdjustmentEvent</code> with the
  * specified source, id, type, and value.
  *
  * @param source The source of the event.
  * @param id The event id
  * @param type The event type, which will be one of the constants in
  * this class.
  * @param value The value of the adjustment.
  */
public
AdjustmentEvent(Adjustable source, int id, int type, int value)
{
  super(source, id);
  this.adjustmentType = type;
  this.value = value;
  this.adjustable = source;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the source of the event as an <code>Adjustable</code>.
  *
  * @return The source of the event as an <code>Adjustable</code>.
  */
public Adjustable
getAdjustable()
{
  return((Adjustable)getSource());
}

/*************************************************************************/

/**
  * Returns the type of the event, which will be one of the constants
  * defined in this class.
  *
  * @return The type of the event.
  */
public int
getAdjustmentType()
{
  return(adjustmentType);
}

/*************************************************************************/

/**
  * Returns the value of the event.
  *
  * @return The value of the event.
  */
public int
getValue()
{
  return(value);
}

/*************************************************************************/

/**
  * Returns a string that describes the event.
  *
  * @param A string that describes the event.
  */
public String
paramString()
{
  return(getClass().getName() + " source= " + getSource() + " id=" + getID() +
         " type=" + getAdjustmentType() + " value=" + getValue());
}

} // class AdjustmentEvent

