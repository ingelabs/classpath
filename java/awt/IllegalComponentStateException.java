/* IllegalComponentStateException.java -- Bad component state
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
  * This exception is thrown when the requested operation failed because
  * a component was not in the proper state.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class IllegalComponentStateException extends IllegalStateException
{

/**
  * Initializes a new instance of <code>IllegalComponentStateException</code>
  * with the specified detailed error message.
  *
  * @param message The detailed error message.
  */
public
IllegalComponentStateException(String message)
{
  super(message);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>IllegalComponentStateException</code>
  * with no detailed error message.
  */
public
IllegalComponentStateException()
{
  super();
}

} // class IllegalComponentStateException

