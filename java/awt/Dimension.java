/*************************************************************************
/* Dimension.java -- Class for width and height
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
  * This class holds a width and height value pair.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Dimension implements java.io.Serializable
{

/*
 * Instance Variables
 */

/**
  * This width of this object.
  */
public int width;

/**
  * The height of this object.
  */
public int height;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Dimension</code> with a width
  * and height of zero.
  */
public
Dimension()
{
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Dimension</code> to have a width
  * and height identical to that of the specified dimension object.
  *
  * @param dim The <code>Dimension</code> to take the width and height from.
  */
public
Dimension(Dimension dim)
{
  width = dim.width;
  height = dim.height;

  // Your a dim.width, Beavis!
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Dimension</code> with the
  * specified width and height.
  *
  * @param width The width of this object.
  * @param height The height of this object.
  */
public 
Dimension(int width, int height)
{
  this.width = width;
  this.height = height;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the size of this object.  Not very useful.
  *
  * @return This object.
  */
public Dimension
getSize()
{
  return(this);
}

/*************************************************************************/

/**
  * Sets the width and height of this object to match that of the
  * specified object.
  *
  * @param dim The <code>Dimension</code> object to get the new width and
  * height from.
  */
public void
setSize(Dimension dim)
{
  synchronized(this)
    {
      this.width = dim.width;
      this.height = dim.height;
    }
}

/*************************************************************************/

/**
  * Sets the width and height of this object to the specified values.
  *
  * @param width The new width value.
  * @param height The new height value.
  */
public void
setSize(int width, int height)
{
  synchronized(this)
    {
      this.width = width;
      this.height = height;
    }
}

/*************************************************************************/

/**
  * Tests this object for equality against the specified object.  This will
  * be true if and only if the specified object:
  * <p>
  * <ul>
  * <li>Is not <code>null</code>.
  * <li>Is an instance of <code>Dimension</code>.
  * <li>Has width and height values identical to this object.
  * </ul>
  *
  * @param obj The object to test against.
  *
  * @return <code>true</code> if the specified object is equal to this
  * object, <code>false</code> otherwise.
  */
public boolean
equals(Object obj)
{
  if (obj == null)
    return(false);

  if (!(obj instanceof Dimension))
    return(false);

  Dimension dim = (Dimension)obj;

  if ((dim.width != width) || (dim.height != height))
    return(false);

  return(true);
}

/*************************************************************************/

/**
  * Returns a string representation of this object.
  *
  * @return A string representation of this object.
  */
public String
toString()
{
  return(getClass().getName() + "(width=" + width + ", height=" + height +
         ")");
}

} // class Dimension 

