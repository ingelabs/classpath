/*************************************************************************
/* Choice.java -- Java choice button widget.
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

import java.awt.peer.ChoicePeer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;
import java.util.Vector;

/**
  * This class implements a drop down choice list.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Choice extends Component implements ItemSelectable, Serializable
{

/*
 * Static Variables
 */

// Serialization constant
private static final long serialVersionUID = -4075310674757313071L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial A list of items for the choice box, which can be <code>null</code>.
  */
private Vector pItems = new Vector();

/**
  * @serial The index of the selected item in the choice box.
  */
private int selectedIndex;

// Listener chain
private ItemListener item_listeners;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Choice</code>.
  */
public
Choice()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the number of items in the list.
  *
  * @return The number of items in the list.
  */
public int
getItemCount()
{
  return(pItems.size());
}

/*************************************************************************/

/**
  * Returns the number of items in the list.
  *
  * @return The number of items in the list.
  *
  * @deprecated This method is deprecated in favor of <code>getItemCount</code>.
  */
public int
countItems()
{
  return(pItems.size());
}

/*************************************************************************/

/**
  * Returns the item at the specified index in the list.
  *
  * @param index The index into the list to return the item from.
  *
  * @exception ArrayIndexOutOfBoundsException If the index is invalid.
  */
public String
getItem(int index)
{
  return((String)pItems.elementAt(index));
}

/*************************************************************************/

/**
  * Adds the specified item to this choice box.
  *
  * @param item The item to add.
  */
public synchronized void
add(String item)
{
  pItems.addElement(item);

  ChoicePeer cp = (ChoicePeer)getPeer();
  if (cp != null)
    cp.add(item, getItemCount());
}

/*************************************************************************/

/**
  * Adds the specified item to this choice box.
  *
  * @param item The item to add.
  */
public synchronized void
addItem(String item)
{
  pItems.addElement(item);

  ChoicePeer cp = (ChoicePeer)getPeer();
  if (cp != null)
    cp.addItem(item, getItemCount());
}

/*************************************************************************/

/**
  * Inserts the specified item to this choice box at the specified index.
  *
  * @param item The item to add.
  * @param index The index at which the item should be inserted.
  */
public synchronized void
insert(String item, int index)
{
  pItems.insertElementAt(item, index);

  ChoicePeer cp = (ChoicePeer)getPeer();
  if (cp != null)
    cp.addItem(item, getItemCount());
}

/*************************************************************************/

/**
  * Removes the specified item from the choice box.
  *
  * @param item The item to remove.
  *
  * @param IllegalArgumentException If the specified item doesn't exist.
  */
public synchronized void
remove(String item)
{
  int index = pItems.indexOf(item);
  remove(index);
}

/*************************************************************************/

/**
  * Removes the item at the specified index from the choice box.
  *
  * @param index The index of the item to remove.
  *
  * @exception ArrayIndexOutOfBoundException If the index is not valid.
  */
public synchronized void
remove(int index)
{
  pItems.removeElementAt(index);

  ChoicePeer cp = (ChoicePeer)getPeer();
  if (cp != null)
    cp.remove(index);
}

/*************************************************************************/

/**
  * Removes all of the objects from this choice box.
  */
public synchronized void
removeAll()
{
  int count = getItemCount();

  if (count > 0)
    for (int i = 0; i < count; i++)
      remove(i);

  pItems = new Vector();
}

/*************************************************************************/

/**
  * Returns the currently selected item.
  *
  * @return The currently selected item.
  */
public synchronized String
getSelectedItem()
{
  return((String)pItems.elementAt(selectedIndex));
}

/*************************************************************************/

/**
  * Returns an array with one row containing the selected item.
  *
  * @return An array containing the selected item.
  */
public synchronized Object[]
getSelectedObjects()
{
  Object[] objs = new Object[1];
  objs[0] = pItems.elementAt(selectedIndex);

  return(objs);
}

/*************************************************************************/

/**
  * Returns the index of the selected item.
  *
  * @return The index of the selected item.
  */
public int
getSelectedIndex()
{
  return(selectedIndex);
}

/*************************************************************************/

/**
  * Forces the item at the specified index to be selected.
  *
  * @param index The index of the row to make selected.
  *
  * @param IllegalArgumentException If the specified index is invalid.
  */
public synchronized void
select(int index)
{
  if ((index < 0) || (index > getItemCount()))
    throw new IllegalArgumentException("Bad index: " + index);

  this.selectedIndex = index;

  ChoicePeer cp = (ChoicePeer)getPeer();
  if (cp != null)
    cp.select(index);
}

/*************************************************************************/

/**
  * Forces the named item to be selected.
  *
  * @param item The item to be selected.
  *
  * @exception IllegalArgumentException If the specified item does not exist.
  */
public synchronized void
select(String item)
{
  int index = pItems.indexOf(item);
  select(index);
}

/*************************************************************************/

/**
  * Creates the native peer for this object.
  */
public void
addNotify()
{
  if (getPeer() == null)
    setPeer(getToolkit().createChoice(this));
}

/*************************************************************************/

/**
  * Adds the specified listener to the list of registered listeners for
  * this object.
  *
  * @param listener The listener to add.
  */
public synchronized void
addItemListener(ItemListener listener)
{
  item_listeners = AWTEventMulticaster.add(item_listeners, listener);
}

/*************************************************************************/

/**
  * Removes the specified listener from the list of registered listeners for
  * this object.
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
  * Processes this event by invoking <code>processItemEvent()</code> if the
  * event is an instance of <code>ItemEvent</code>, otherwise the event
  * is passed to the superclass.
  *
  * @param event The event to process.
  */
protected void
processEvent(AWTEvent event)
{
  if (event instanceof ItemEvent)
    processItemEvent((ItemEvent)event);
  else
    super.processEvent(event);
}

/*************************************************************************/

/**
  * Processes item event by dispatching to any registered listeners.
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
protected String
paramString()
{
  return(getClass().getName() + "(selectedIndex=" + selectedIndex + ")");
}

} // class Choice 

