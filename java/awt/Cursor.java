/* Cursor.java -- Mouse pointer class
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
  * This class represents various predefined cursor types.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Cursor implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * Constant for the system default cursor type
  */
public static final int DEFAULT_CURSOR = 0;

/**
  * Constant for a cross-hair cursor.
  */
public static final int CROSSHAIR_CURSOR = 1;

/**
  * Constant for a cursor over a text field.
  */
public static final int TEXT_CURSOR = 2;

/**
  * Constant for a cursor to display while waiting for an action to complete.
  */
public static final int WAIT_CURSOR = 3;

/**
  * Cursor used over SW corner of window decorations.
  */
public static final int SW_RESIZE_CURSOR = 4;

/**
  * Cursor used over SE corner of window decorations.
  */
public static final int SE_RESIZE_CURSOR = 5;

/**
  * Cursor used over NW corner of window decorations.
  */
public static final int NW_RESIZE_CURSOR = 6;

/**
  * Cursor used over NE corner of window decorations.
  */
public static final int NE_RESIZE_CURSOR = 7;

/**
  * Cursor used over N edge of window decorations.
  */
public static final int N_RESIZE_CURSOR = 8;

/**
  * Cursor used over S edge of window decorations.
  */
public static final int S_RESIZE_CURSOR = 9;

/**
  * Cursor used over E edge of window decorations.
  */
public static final int E_RESIZE_CURSOR = 10;

/**
  * Cursor used over W edge of window decorations.
  */
public static final int W_RESIZE_CURSOR = 11;

/**
  * Constant for a hand cursor.
  */
public static final int HAND_CURSOR = 12;

/**
  * Constant for a cursor used during window move operations.
  */
public static final int MOVE_CURSOR = 13;

// Serialization constant
private static final long serialVersionUID = 8028237497568985504L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The numeric id of this cursor.
  */
private int type;

/*************************************************************************/

/*
 * Static Methods
 */

/**
  * Returns an instance of the system default cursor type.
  *
  * @return The system default cursor.
  */
public static Cursor
getDefaultCursor()
{
  return(new Cursor(DEFAULT_CURSOR));
}

/*************************************************************************/

/**
  * Returns an instance of <code>Cursor</code> for one of the specified
  * predetermined types.
  *
  * @param type The type contant from this class.
  *
  * @return The requested predefined cursor.
  *
  * @exception IllegalArgumentException If the constant is not one of the
  * predefined cursor type constants from this class.
  */
public static Cursor
getPredefinedCursor(int type) throws IllegalArgumentException
{
  if ((type < DEFAULT_CURSOR) || (type > MOVE_CURSOR))
    throw new IllegalArgumentException("Bad predefined cursor type: " + type);

  return(new Cursor(type));
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Cursor</code> with the specified
  * type.
  *
  * @param type The cursor type.
  */
public
Cursor(int type)
{
  this.type = type;
}

/*************************************************************************/

/**
  * Instance Variables
  */

/**
  * Returns the numeric type identifier for this cursor.
  *
  * @return The cursor id.
  */
public int
getType()
{
  return(type);
}

} // class Cursor 

