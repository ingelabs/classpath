/*************************************************************************
/* FieldPosition.java -- Keeps track of field positions while formatting
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
  * This class is used by the java.text formatting classes to track
  * field positions.  A field position is defined by an identifier value
  * and begin and end index positions.  The formatting classes in java.text
  * typically define constant values for the field identifiers.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FieldPosition
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the field identifier value.
  */
private int field_id;

/**
  * This is the beginning index of the field.
  */
private int begin;

/**
  * This is the ending index of the field.
  */
private int end;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>FieldPosition</code> to
  * have the specified field id.
  *
  * @param field_id The field identifier value.
  */
public
FieldPosition(int field_id)
{
  this.field_id = field_id;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This method returns the field identifier value for this object.
  *
  * @return The field identifier.
  */
public int
getField()
{
  return(field_id);
}

/*************************************************************************/

/**
  * This method returns the beginning index for this field.
  *
  * @return The beginning index.
  */
public int
getBeginIndex()
{
  return(begin);
}

/*************************************************************************/

/**
  * This method sets the beginning index of this field to the specified value.
  *
  * @param begin The new beginning index.
  */
public void
setBeginIndex(int begin)
{
  this.begin = begin;
}

/*************************************************************************/

/**
  * This method returns the ending index for the field.
  *
  * @return The ending index.
  */
public int
getEndIndex()
{
  return(end);
}

/*************************************************************************/

/**
  * This method sets the ending index of this field to the specified value.
  *
  * @param end The new ending index.
  */
public void
setEndIndex(int end)
{
  this.end = end;
}

/*************************************************************************/

/**
  * This method tests this object for equality against the specified object.
  * The objects will be considered equal if and only if:
  * <p>
  * <ul>
  * <li>The specified object is not <code>null</code>.
  * <li>The specified object is an instance of <code>FieldPosition</code>.
  * <li>The specified object has the same field identifier and beginning
  * and ending index as this object.
  * </ul>
  *
  * @param obj The object to test for equality to this object.
  *
  * @return <code>true</code> if the specified object is equal to this object, <code>false</code> otherwise.
  */
public boolean
equals(Object obj)
{
  if (obj == null)
    return(false);

  if (!(obj instanceof FieldPosition))
    return(false);

  FieldPosition fp = (FieldPosition)obj;

  if (fp.getField() != getField())
    return(false);

  if (fp.getBeginIndex() != getBeginIndex())
    return(false);

  if (fp.getEndIndex() != getEndIndex())
    return(false);

  return(true);
}

/*************************************************************************/

/**
  * This method returns a <code>String</code> representation of this
  * object.
  *
  * @return A <code>String</code> representation of this object.
  */
public String
toString()
{
  return(getClass().getName() + "[field=" + getField() + ",beginIndex=" +
         getBeginIndex() + ",endIndex=" + getEndIndex() + "]");
}

} // class FieldPosition

