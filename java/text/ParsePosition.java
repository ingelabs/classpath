/*************************************************************************
/* ParsePosition.java -- Keep track of position while parsing.
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
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

package java.text;

/**
  * This class is used to keep track of the current position during parsing
  * operations.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ParsePosition
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the index of the current parse position.
  */
private int index;

/**
  * This is the index of the position where an error occurred during parsing.
  */
private int error_index = -1;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>ParsePosition</code> to
  * have the specified initial index value.
  *
  * @param index The initial parsing index.
  */
public
ParsePosition(int index)
{
  this.index = index;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the current parsing index.
  *
  * @return The current parsing index
  */
public int
getIndex()
{
  return(index);
}

/*************************************************************************/

/**
  * This method sets the current parsing index to the specified value.
  *
  * @param index The new parsing index.
  */
public void
setIndex(int index)
{
  this.index = index;
}

/*************************************************************************/

/**
  * This method returns the error index value.  This value defaults to -1
  * unless explicitly set to another value.
  *
  * @return The error index.
  */
public int
getErrorIndex()
{
  return(error_index);
}

/*************************************************************************/

/**
  * This method sets the error index to the specified value.
  *
  * @param error_index The new error index
  */
public void
setErrorIndex(int error_index)
{
  this.error_index = error_index;
}

/*************************************************************************/

/**
  * This method tests the specified object for equality with this object.  The
  * two objects will be considered equal if and only if all of the following
  * conditions are met.
  * <p>
  * <ul>
  * <li>The specified object is not <code>null</code>.
  * <li>The specified object is an instance of <code>ParsePosition</code>.
  * <li>The specified object has the same index and error index as this object. 
  * </ul>
  *
  * @param obj The <code>Object</code> to test for equality against this object.
  *
  * @return <code>true</code> if the specified object is equal to this object, <code>false</code> otherwise.
  */
public boolean
equals(Object obj)
{
  if (obj == null)
    return(false);

  if (!(obj instanceof ParsePosition))
    return(false);

  ParsePosition pp = (ParsePosition)obj;

  if (pp.getIndex() != getIndex())
    return(false);

  if (pp.getErrorIndex() != getErrorIndex())
    return(false);

  return(true);
}

} // class ParsePosition

