/*************************************************************************
/* ParseException.java -- An error occurred while parsing.
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
  * This exception is thrown when an unexpected error occurs during parsing.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ParseException extends Exception
{

/*
 * Instance Variables
 */

/**
  * This is the position where the error was encountered.
  */
private int offset;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>ParseException</code>
  * with a detailed error message and a error position.
  *
  * @param msg The descriptive message describing the error.
  * @param offset The position where the error was encountered.
  */
public
ParseException(String s, int offset)
{
  super(s);
  
  this.offset = offset;
}

/*************************************************************************/

/**
  * This method returns the position where the error occurred.
  * 
  * @return The position where the error occurred.
  */
public int
getErrorOffset()
{
  return(offset);
}

} // class ParseException

