/*************************************************************************
/* SocketException.java -- An exception occured while performing a socket op
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
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.net;

/**
  * This exception indicates that a generic error occured related to an
  * operation on a socket.  Check the descriptive message (if any) for
  * details on the nature of this error
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class SocketException extends java.io.IOException
{

/*
 * Constructors
 */

/**
  * Constructs a new SocketException with no descriptive message.
  */
public
SocketException()
{
  super();
}

/*************************************************************************/

/**
  * Constructs a new SocketException with a descriptive message (such as the
  * text from strerror(3)) passed in as an argument
  *
  * @param message A message describing the error that occurs
  */
public
SocketException(String message)
{
  super(message);
}

} // class SocketException

