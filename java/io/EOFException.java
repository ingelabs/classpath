/*************************************************************************
/* EOFException.java -- Unexpected end of file exception
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
  * This exception is thrown when the end of the file or stream was 
  * encountered unexpectedly.  This is not the normal way that a normal
  * EOF condition is reported.  Normally a special value such as -1 is
  * returned.  However, certain types of streams expecting certain data
  * in a certain format might reach EOF before reading their expected
  * data pattern and thus throw this exception.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class EOFException extends IOException
{

/*
 * Constructors
 */

/**
  * Create a new EOFException without a descriptive error message
  */
public
EOFException()
{
  super();
}

/*************************************************************************/

/**
  * Create a new EOFException with a descriptive error message String
  *
  * @param message The descriptive error message
  */
public
EOFException(String message)
{
  super(message);
}

} // class EOFException

