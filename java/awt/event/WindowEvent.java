/*************************************************************************
/* WindowEvent.java -- Window change event
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

import java.awt.Window;

/**
  * This event is generated when there is a change in the window.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class WindowEvent extends ComponentEvent 
             implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the first id in the range of event ids used by this class.
  */ 
public static final int WINDOW_FIRST = 200;

/**
  * This is the last id in the range of event ids used by this class.
  */
public static final int WINDOW_LAST = 206;

/**
  * This is the id for a window that is opened.
  */
public static final int WINDOW_OPENED = 200;

/**
  * This is the id for a window that is closing.
  */
public static final int WINDOW_CLOSING = 201;

/**
  * This is the id for a window that is closed.
  */
public static final int WINDOW_CLOSED = 202;

/**
  * This is the id for a window that is iconified.
  */
public static final int WINDOW_ICONIFIED = 203;

/**
  * This is the id for a window that is de-iconified.
  */
public static final int WINDOW_DEICONIFIED = 204;

/**
  * This is the id for a window that is activated.
  */
public static final int WINDOW_ACTIVATED = 205;

/**
  * This is the id for a window that is de-activated.
  */
public static final int WINDOW_DEACTIVATED = 206;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>WindowEvent</code> with the
  * specified source and id.
  *
  * @param source The window that generated this event.
  * @param id The event id.
  */
public
WindowEvent(Window source, int id)
{
  super(source, id);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the event source as a <code>Window</code>.
  *
  * @return The event source as a <code>Window</code>.
  */
public Window
getWindow()
{
  return((Window)getSource());
}

/*************************************************************************/

/**
  * Returns a string that identifies this event.
  *
  * @return A string that identifies this event.
  */
public String
paramString()
{
  return(getClass().getName() + " source=" + getSource() + " id=" + getID());
}

} // class WindowEvent

