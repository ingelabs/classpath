/*************************************************************************
/* CheckboxMenuItem.java -- A menu option with a checkbox on it.
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

package java.awt;

import java.awt.peer.CheckboxMenuItemPeer;
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuComponentPeer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
  * This class implements a menu item that has a checkbox on it indicating
  * the selected state of some option.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class CheckboxMenuItem extends MenuItem implements ItemSelectable,
                                                          java.io.Serializable
{

/*
 * Static Variables
 */

// Serialization constant
private static final long serialVersionUID = 6190621106981774043L;

/*
 * Instance Variables
 */

/**
  * @serial The state of the checkbox, with <code>true</code> being on and
  * <code>false</code> being off.
  */
private boolean state;

// List of registered ItemListeners
private transient ItemListener item_listeners;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>CheckboxMenuItem</code> with no
  * label and an initial state of off.
  */
public
CheckboxMenuItem()
{
  this("", false);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>CheckboxMenuItem</code> with the
  * specified label and an initial state of off.
  *
  * @param label The label of the menu item.
  */
public
CheckboxMenuItem(String label)
{
  this(label, false);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>CheckboxMenuItem</code> with the
  * specified label and initial state.
  *
  * @param label The label of the menu item.
  * @param state The initial state of the menu item, where <code>true</code>
  * is on, and <code>false</code> is off.
  */
public
CheckboxMenuItem(String label, boolean state)
{
  super(label);
  this.state = state;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the state of this menu item.
  *
  * @return The state of this menu item.
  */
public boolean
getState()
{
  return(state);
}

/*************************************************************************/

/**
  * Sets the state of this menu item.
  *
  * @param state The initial state of the menu item, where <code>true</code>
  * is on, and <code>false</code> is off.
  */
public synchronized void
setState(boolean state)
{
  this.state = state;

  CheckboxMenuItemPeer cmip = (CheckboxMenuItemPeer)getPeer();
  if (cmip != null)
    cmip.setState(state);
}

/*************************************************************************/

/**
  * Returns an array of length 1 with the menu item label for this object
  * if the state is on.  Otherwise <code>null</code> is returned.
  *
  * @param An array with this menu item's label if it has a state of on,
  * or <code>null</code> otherwise.
  */
public Object[]
getSelectedObjects()
{
  if (state == false)
    return(null);

  Object[] obj = new Object[0];
  obj[1] = getLabel();

  return(obj);
}

/*************************************************************************/

/**
  * Create's this object's native peer
  */
public synchronized void
addNotify()
{
  if (getPeer() == null)
    setPeer((MenuComponentPeer)getToolkit().createCheckboxMenuItem(this));
}

/*************************************************************************/

/**
  * Adds the specified listener to the list of registered item listeners
  * for this object.
  *
  * @param listener The listener to add.
  */
public synchronized void
addItemListener(ItemListener listener)
{
  item_listeners = AWTEventMulticaster.add(item_listeners, listener);

  enableEvents(AWTEvent.ITEM_EVENT_MASK);
}

/*************************************************************************/

/**
  * Removes the specified listener from the list of registered item
  * listeners for this object.
  *
  * @param listener The listener to remove.
  */
public synchronized void
removeItemListener(ItemListener listener)
{
  item_listeners = AWTEventMulticaster.remove(item_listeners, listener);
}

/*************************************************************************/

/**
  * Processes the specified event by calling <code>processItemEvent()</code>
  * if it is an instance of <code>ItemEvent</code> or calling the superclass
  * method otherwise.
  *
  * @param event The event to process.
  */
protected void
processEvent(AWTEvent event)
{
  if (event instanceof ItemEvent)
    processItemEvent((ItemEvent)event);
}

/*************************************************************************/

/**
  * Processes the specified event by dispatching it to any registered listeners.
  *
  * @param event The event to process.
  */
protected void
processItemEvent(ItemEvent event)
{
  if (item_listeners != null)
    item_listeners.itemStateChanged(event);
}

/*************************************************************************/

/**
  * Returns a debugging string for this object.
  *
  * @return A debugging string for this object.
  */
public String
paramString()
{
  return(getClass().getName() + "(label=" + getLabel() + ",state=" + state + ")");
}

} // class CheckboxMenuItem

