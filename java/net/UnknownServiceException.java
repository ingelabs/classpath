/*************************************************************************
/* UnknownServiceException.java -- A service error occured
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
  * Contrary to what you might think, this does not indicate that the
  * TCP/IP service name specified was invalid.  Instead it indicates that
  * the MIME type returned from a URL could not be determined or that an
  * attempt was made to write to a read-only URL.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class UnknownServiceException extends java.io.IOException
{

/*
 * Constructors
 */

/**
  * Constructs a new UnknownServiceException with no descriptive message.
  */
public
UnknownServiceException()
{
  super();
}

/*************************************************************************/

/**
  * Constructs a new UnknownServiceException with a descriptive message (such as the
  * text from strerror(3)) passed in as an argument
  *
  * @param message A message describing the error that occurs
  */
public
UnknownServiceException(String message)
{
  super(message);
}

} // class UnknownServiceException

