/* PrinterAbortException.java -- Indicates the print job was aborted
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

/**
  * This exception is thrown when the print job is aborted, either by the
  * user or by the application.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class PrinterAbortException extends PrinterException 
                                   implements Serializable
{

/**
  * Initializes a new instance of <code>PrinterAbortException</code> with no
  * detailed error message.
  */
public
PrinterAbortException()
{
  super();
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>PrinterAbortException</code> with a
  * descriptive error message.
  *
  * @param message The descriptive error message.
  */
public
PrinterAbortException(String message)
{
  super(message);
}

} // class PrinterAbortException

