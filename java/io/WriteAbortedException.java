/*************************************************************************
/* WriteAbortedException.java -- An exception occured while writing a serialization stream
/*
/* Copyright (c) 1998 by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.io;

/** 
    This exception is thrown when an
    <code>ObjectStreamException</code> is read in from a serialized
    stream.  This is the result of the exception having been thrown
    when the stream was written.
  
    @version 0.1
  
    @author Aaron M. Renn (arenn@urbanophile.com)
*/
public class WriteAbortedException extends ObjectStreamException
{

/*
 * Instance Variables
 */

/**
  * The detailed exception that caused this exception to be thrown
  */
public Exception detail;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create a new WriteAbortedException with an eof parameter indicating
  * the detailed Exception that caused this exception to be thrown.
  *
  * @param detail The exception that caused this exception to be thrown
  * @param msg A message explainging why this exception was thrown
  */
WriteAbortedException(String msg, Exception detail)
{
  super(msg);
  this.detail = detail;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This method returns a message indicating what went wrong, including 
  * the message text from the initial exception that caused this one to
  * be thrown
  */
public String
getMessage()
{
  return(super.getMessage() + ": " + detail.getMessage());
}

} // class WriteAbortedException

