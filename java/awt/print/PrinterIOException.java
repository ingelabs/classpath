/* PrinterIOException.java -- The print job encountered an I/O error
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


package java.awt.print;

import java.io.Serializable;
import java.io.IOException;

/**
  * This exception is thrown when the print job encounters an I/O problem
  * of some kind.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class PrinterIOException extends PrinterException 
                                implements Serializable
{

/*
 * Instance Variables
 */

/**
  * @serial The <code>IOException</code> that lead to this 
  * exception being thrown.
  */
private IOException mException;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>PrinterIOException</code> with the
  * text description of the specified <code>IOException</code>.
  *
  * @param mException The <code>IOException</code> that caused this
  * exception to be thrown.
  */
public
PrinterIOException(IOException mException)
{
  this(mException.toString());
  this.mException = mException;  
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>PrinterIOException</code> with a
  * descriptive error message.  Note that this constructor is private.
  *
  * @param message The descriptive error message.
  */
private
PrinterIOException(String message)
{
  super(message);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the underlying <code>IOException</code> that
  * caused this exception.
  *
  * @return The <code>IOException</code> that caused this exception.
  */
public IOException
getIOException()
{
  return(mException);
}

} // class PrinterIOException

