/*************************************************************************
/* Button.java -- AWT button widget
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.ButtonPeer;
import java.awt.peer.ComponentPeer;

/**
  * This class provides a button widget for the AWT. 
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Button extends Component implements java.io.Serializable
{

/*
 * Static Variables
 */

// FIXME: Need readObject/writeObject for serialization

// Serialization version constant
private static final long serialVersionUID = -8774683716313001058L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The action command name for this button.
  */
private String actionCommand;

/**
  * @serial The label for this button.
  */
private String label;

// List of ActionListeners for this class.
private transient ActionListener action_listeners;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Button</code> with no label.
  */
public
Button()
{
  this("");
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Button</code> with the specified
  * label.  The action command name is also initialized to this value.
  *
  * @param label The label to display on the button.
  */
public
Button(String label)
{
  this.label = label;
  actionCommand = label;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * Returns the label for this button.
  *
  * @return The label for this button.
  */
public String
getLabel()
{
  return(label);
}

/*************************************************************************/

/**
  * Sets the label for this button to the specified value.
  *
  * @param label The new label for this button.
  */
public synchronized void
setLabel(String label)
{
  this.label = label;

  ButtonPeer bp = (ButtonPeer)getPeer();
  if (bp != null)
    bp.setLabel(label);
}

/*************************************************************************/

/**
  * Returns the action command name for this button.
  *
  * @return The action command name for this button.
  */
public String
getActionCommand()
{
  return(actionCommand);
}

/*************************************************************************/

/**
  * Sets the action command name for this button to the specified value.
  *
  * @param actionCommand The new action command name.
  */
public void
setActionCommand(String actionCommand)
{
  this.actionCommand = actionCommand;
}

/*************************************************************************/

/**
  * Adds a new entry to the list of listeners that will receive
  * action events from this button.
  *
  * @param listener The listener to add.
  */
public synchronized void
addActionListener(ActionListener listener)
{
  action_listeners = AWTEventMulticaster.add(action_listeners, listener);
}

/*************************************************************************/

/**
  * Removes the specified listener from the list of listeners that will
  * receive action events from this button.
  * 
  * @param listener The listener to remove.
  */
public synchronized void
removeActionListener(ActionListener listener)
{
  action_listeners = AWTEventMulticaster.remove(action_listeners, listener);
}

/*************************************************************************/

/**
  * Notifies this button that it should create its native peer object.
  */
public void
addNotify()
{
  ButtonPeer bp = (ButtonPeer)getPeer();
  if (bp != null)
    return;

  setPeer((ComponentPeer)getToolkit().createButton(this));
}

/*************************************************************************/

/**
  * Processes an event for this button.  If the specified event is an
  * instance of <code>ActionEvent</code>, then the
  * <code>processActionEvent()</code> method is called to dispatch it
  * to any registered listeners.  Otherwise, the superclass method
  * will be invoked.  Note that this method will not be called at all
  * unless <code>ActionEvent</code>'s are enabled.  This will be done
  * implicitly if any listeners are added.
  *
  * @param event The event to process.
  */
protected void
processEvent(AWTEvent event)
{
  if (event instanceof ActionEvent)
    processActionEvent((ActionEvent)event);
  else
    super.processEvent(event);
}

/*************************************************************************/

/**
  * This method dispatches an action event for this button to any
  * registered listeners.
  *
  * @param event The event to process.
  */
protected void
processActionEvent(ActionEvent event)
{
  if (action_listeners != null)
    action_listeners.actionPerformed(event);
}

/*************************************************************************/

/**
  * Returns a debugging string for this button.
  *
  * @return A debugging string for this button.
  */
protected String
paramString()
{
  return(getClass().getName() + "(label=" + getLabel() + ",actionCommand=" +
         getActionCommand() + ")");
}

} // class Button 

