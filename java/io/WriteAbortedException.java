/*************************************************************************
/* WriteAbortedException.java -- An exception occured while writing a serialization stream
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

package java.io;

/**
  * This exception is thrown when one of the other ObjectStreamException 
  * subclasses was thrown during a serialization write.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
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
private String message;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create a new WriteAbortedException with an eof parameter indicating
  * the detailed Exception that caused this exception to be thrown.
  *
  * @param detail The exception that caused this exception to be thrown
  */
WriteAbortedException(String msg, Exception detail)
{
  this.message = msg;
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
  return(message + ": " + detail.getMessage());
}

} // class WriteAbortedException

