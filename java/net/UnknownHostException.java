/* UnknownHostException.java -- The hostname is not unknown
   Copyright (C) 1998,2000 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */

package java.net;

/**
  * This exception indicates that an attempt was made to reference a hostname
  * or IP address that is not valid.  This could possibly indicate that a
  * DNS problem has occurred, but most often means that the host was not
  * correctly specified.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class UnknownHostException extends java.io.IOException
{

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>UnknownHostException</code>
  * without a descriptive error message.
  */
public
UnknownHostException()
{
  super();
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>UnknownHostException</code>
  * with a descriptive error message, such as the name of the host
  * that could not be resolved.
  *
  * @param message A message describing the error that occurrred.
  */
public
UnknownHostException(String message)
{
  super(message);
}

} // class UnknownHostException

