/*************************************************************************
/* ConnectException.java -- An exception occured while connecting to a host
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

package java.net;

/**
  * This exception indicates that an error occured while attempting to
  * connect to a remote host.  Often this indicates that the remote host
  * refused the connection (ie, is not listening on the target socket).
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ConnectException extends SocketException
{

/*
 * Constructors
 */

/**
  * Constructs a new ConnectException with no descriptive message.
  */
public
ConnectException()
{
  super();
}

/*************************************************************************/

/**
  * Constructs a new ConnectException with a descriptive message (such as the
  * text from strerror(3)) passed in as an argument
  *
  * @param message A message describing the error that occurs
  */
public
ConnectException(String message)
{
  super(message);
}

} // class ConnectException

