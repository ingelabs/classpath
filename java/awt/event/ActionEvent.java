/* ActionEvent.java -- An action has been triggered
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.awt.event;

/**
  * This event is generated when an action on a component (such as a
  * button press) occurs.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ActionEvent extends java.awt.AWTEvent 
             implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * The first id number in the range of action id's.
  */
public static final int ACTION_FIRST = 1001;

/**
  * The last id number in the range of action id's.
  */
public static final int ACTION_LAST = 1001;

/**
  * An event id indicating that an action has occurred.
  */
public static final int ACTION_PERFORMED = 1001;

/**
  * Bit mask indicating the shift key was pressed.
  */
public static final int SHIFT_MASK = 1;

/**
  * Bit mask indicating the control key was pressed.
  */
public static final int CTRL_MASK = 1;

/**
  * Bit mask indicating the that meta key was pressed.
  */
public static final int META_MASK = 1;

/**
  * Bit mask indicating that the alt key was pressed.
  */
public static final int ALT_MASK = 1;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial Modifiers for this event
  */
private int modifiers;

/**
  * @serial The command for this event
  */
private String actionCommand;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>ActionEvent</code> with the
  * specified source, id, and command.
  *
  * @param source The event source.
  * @param id The event id.
  * @param command The command string for this action.
  */
public
ActionEvent(Object source, int id, String command)
{
  super(source, id);
  this.actionCommand = command;
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>ActionEvent</code> with the
  * specified source, id, command, and modifiers.
  *
  * @param source The event source.
  * @param id The event id.
  * @param command The command string for this action.
  * @param modifiers The keys held down during the action, which is 
  * combination of the bit mask constants defined in this class.
  */
public
ActionEvent(Object source, int id, String command, int modifiers)
{
  this(source, id, command);
  this.modifiers = modifiers;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the command string associated with this action.
  *
  * @return The command string associated with this action.
  */
public String
getActionCommand()
{
  return(actionCommand);
}

/*************************************************************************/

/**
  * Returns the keys held down during the action.  This value will be a
  * combination of the bit mask constants defined in this class.
  *
  * @return The modifier bits.
  */
public int
getModifiers()
{
  return(modifiers);
}

/*************************************************************************/

/**
  * Returns a string that identifies the action event.
  *
  * @return A string identifying the event.
  */
public String
paramString()
{
  return("ActionEvent: source=" + getSource().toString() + " id=" + getID() +
         " command=" + getActionCommand() + " modifiers=" + getModifiers());
}

} // class ActionEvent 

