/*************************************************************************
/* OptionalDataException.java -- Unexpected end of file exception
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
  * This exception is thrown when unexpected data appears in the input
  * stream from which a serialized object is being read.  The field
  * <code>eof</code> will always be set to true (***Why even have it?***) and 
  * the <code>count</code> field will contain the number of valid bytes
  * available to be read.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class OptionalDataException extends ObjectStreamException
{

/*
 * Instance Variables
 */

/**
  * Whether or not the end of the stream has been reached
  */
public boolean eof;

/**
  * The number of valid bytes that can be read
  */
public int length;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create a new OptionalDataException with an eof parameter indicating
  * whether or not the end of stream is reached and the number of valid
  * bytes that may be read.
  *
  * @param eof 'true' if end of stream reached, 'false' otherwise
  * @param count The number of valid bytes to be read.
  */
OptionalDataException()
{
  this.eof = eof;
  this.length = length;
}

} // class OptionalDataException

