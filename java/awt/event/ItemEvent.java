/*************************************************************************
/* ItemEvent.java -- Event for item state changes.
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

import java.awt.ItemSelectable;

/**
  * This event is generated when an item changes state.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ItemEvent extends java.awt.AWTEvent implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the first id in the event id range used by this class.
  */
public static final int ITEM_FIRST = 701;

/**
  * This is the last id in the event id range used by this class.
  */
public static final int ITEM_LAST = 701;

/**
  * This event id indicates a state change occurred.
  */
public static final int ITEM_STATE_CHANGED = 701;

/**
  * This type indicates that the item was selected.
  */
public static final int SELECTED = 1;

/**
  * This type indicates that the item was deselected.
  */
public static final int DESELECTED = 2;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The item affected by this event
  */
private Object item;

/**
  * @serial  The state change direction
  */
private int stateChange;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>ItemEvent</code> with the specified
  * source, id, and state change constant.
  *
  * @param source The source of the event.
  * @param id The event id.
  * @param item The item affected by the state change.
  * @param stateChange The state change, either <code>SELECTED</code> or
  * <code>DESELECTED</code>.
  */
public 
ItemEvent(ItemSelectable source, int id, Object item, int stateChange)
{
  super(source, id);
  this.item = item;
  this.stateChange = stateChange;
}
  
/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the event source as an <code>ItemSelectable</code>.
  * 
  * @return The event source as an <code>ItemSelected</code>.
  */
public ItemSelectable
getItemSelectable()
{
  return((ItemSelectable)getSource());
}

/*************************************************************************/

/**
  * Returns the item affected by this state change.
  *
  * @return The item affected by this state change.
  */
public Object
getItem()
{
  return(item);
}

/*************************************************************************/

/**
  * Returns the type of state change, ether <code>SELECTED</code> or
  * <code>DESELECTED</code>.
  *
  * @return The type of state change.
  */
public int
getStateChange()
{
  return(stateChange);
}

/*************************************************************************/

/**
  * Returns a string identifying this event.
  *
  * @param A string identifying this event.
  */
public String
paramString()
{
  return(getClass().getName() + " source=" + getSource() + " id=" + getID() +
         " item=" + getItem() + " stateChange=" + getStateChange());
}

} // class ItemEvent

