/*************************************************************************
/* MenuShortcut.java -- A class for menu accelerators
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

/**
  * This class implements a keyboard accelerator for a menu item.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class MenuShortcut implements java.io.Serializable
{

/*
 * Static Variables
 */

// Serialization Constant
private static final long serialVersionUID = 143448358473180225L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The virtual keycode for the shortcut.
  */
private int key;

/**
  * @serial <code>true</code> if the shift key was used with this shortcut,
  * or <code>false</code> otherwise.
  */
private boolean usesShift;

/*************************************************************************/

/**
  * Initializes a new instance of <code>MenuShortcut</code> with the
  * specified virtual key value.
  *
  * @param key The virtual keycode for the shortcut.
  */
public
MenuShortcut(int key)
{
  this(key, false);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>MenuShortcut</code> with the
  * specified virtual key value and shift setting.
  *
  * @param key The virtual keycode for the shortcut.
  * @param usesShift <code>true</code> if the shift key was pressed,
  * <code>false</code> otherwise.
  */
public
MenuShortcut(int key, boolean usesShift)
{
  this.key = key;
  this.usesShift = usesShift;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the virtual keycode for this shortcut.
  *
  * @return The virtual keycode for this shortcut.
  */
public int
getKey()
{
  return(key);
}

/*************************************************************************/

/**
  * Returns the shift setting for this shortcut.
  *
  * @return <code>true</code> if the shift key was pressed, <code>false</code>
  * otherwise.
  */
public boolean
usesShiftModifier()
{
  return(usesShift);
}

/*************************************************************************/

/**
  * Tests this object for equality against the specified object.  The two
  * objects will be considered equal if and only if the specified object
  * is an instance of <code>MenuShortcut</code> and has the same key value
  * and shift setting as this object.
  *
  * @param obj The object to test for equality against.
  *
  * @return <code>true</code> if the two objects are equal, <code>false</code>
  * otherwise.
  */
public boolean
equals(MenuShortcut obj)
{
  if (obj == null)
    return(false);

  if (obj.key != this.key)
    return(false);

  if (obj.usesShift != this.usesShift)
    return(false);

  return(true);
}

/*************************************************************************/

/**
  * Returns a string representation of this shortcut.
  *
  * @return A string representation of this shortcut.
  */
public String
toString()
{
  return(getClass().getName() + "(key=" + key + ",usesShift=" + usesShift +
         ")");
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
  return(toString());
}

} // class MenuShortcut 

