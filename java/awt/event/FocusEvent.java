/*************************************************************************
/* FocusEvent.java -- Generated for a focus change.
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
  * This class represents an event generated when a focus change occurs
  * for a component.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FocusEvent extends ComponentEvent implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the first id in the range of ids used by this class.
  */
public static final int FOCUS_FIRST = 1004;

/**
  * This is the last id in the range of ids used by this class.
  */
public static final int FOCUS_LAST = 1005;

/**
  * This is the event id for a focus gained event.
  */
public static final int FOCUS_GAINED = 1004;

/**
  * This is the event id for a focus lost event.
  */
public static final int FOCUS_LOST = 1005;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial Indicates whether or not the focus change is temporary
  */
private boolean temporary;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>FocusEvent</code> with the
  * specified source and id.
  *
  * @param source The component that is gaining or losing focus.
  * @param id The event id.
  */
public 
FocusEvent(Component source, int id)
{
  super(source, id);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>FocusEvent</code> with the
  * specified source and id.  A third parameter indicates whether or
  * not the focus change is temporary.
  *
  * @param source The component that is gaining or losing focus.
  * @param id The event id.
  * @param temporary <code>true</code> if the focus change is temporary,
  * <code>false</code> otherwise.
  */
public
FocusEvent(Component source, int id, boolean temporary)
{
  this(source, id);
  this.temporary = temporary;
} 

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method tests whether or not the focus change is temporary or
  * permanent.
  *
  * @return <code>true</code> if the focus change is temporary,
  * <code>false</code> otherwise.
  */
public boolean
isTemporary()
{
  return(temporary);
}

/*************************************************************************/

/**
  * Returns a string identifying this event.
  *
  * @return A string identifying this event.
  */
public String
paramString()
{
  return(getClass().getName() + " source=" + getSource() + " id=" + getID() +
         " temporary=" + isTemporary());
}

} // class FocusEvent

