/*************************************************************************
/* UnknownHostException.java -- The hostname is not unknown
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
  * This exception indicates that an attempt was made to reference a hostname
  * or IP address that is not valid.  This could possibly indicate that a
  * DNS problem has occurred, but most often means that the host was not
  * correctly specified.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class UnknownHostException extends java.io.IOException
{

/*
 * Constructors
 */

/**
  * Constructs a new UnknownHostException with no descriptive message.
  */
public
UnknownHostException()
{
  super();
}

/*************************************************************************/

/**
  * Constructs a new UnknownHostException with a descriptive message (such as the
  * text from strerror(3)) passed in as an argument
  *
  * @param message A message describing the error that occurs
  */
public
UnknownHostException(String message)
{
  super(message);
}

} // class UnknownHostException

