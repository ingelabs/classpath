/*************************************************************************
/* InterruptedIOException.java -- An I/O operation was interrupted.
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
  * This exception is thrown when a in process I/O operation is 
  * interrupted for some reason.  The field bytesTransferred will contain
  * the number of bytes that were read/written prior to the interruption.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class InterruptedIOException extends IOException
{

/*
 * Instance Variables
 */

/**
  * The number of bytes read/written prior to the interruption
  */
public int bytesTransferred;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create a new InterruptedIOException without a descriptive error message
  */
public
InterruptedIOException()
{
  super();
}

/*************************************************************************/

/**
  * Create a new InterruptedIOException with a descriptive error message String
  *
  * @param message The descriptive error message
  */
public
InterruptedIOException(String message)
{
  super(message);
}

/*************************************************************************/

/**
  * Create a new InterruptedIOException with a descriptive error message 
  * String.  Also sets the value of the bytesTransferred field.
  * 
  * @param message The descriptive error message
  * @param bytesTransferred The number of bytes tranferred before the interruption
  */
InterruptedIOException(String message, int bytesTransferred)
{
  super(message);
  this.bytesTransferred = bytesTransferred;
}

} // class InterruptedIOException

