/*************************************************************************
/* ObjectStreamException.java -- Unexpected end of file exception
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
  * This exception is thrown when a problem occurs during serialization.
  * There are more specific subclasses than give more fine grained 
  * indications of the precise failure.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class ObjectStreamException extends IOException 
                                            implements Serializable
{

/*
 * Constructors
 */

/**
  * Create a new ObjectStreamException without a descriptive error message
  */
protected
ObjectStreamException()
{
  super();
}

/*************************************************************************/

/**
  * Create a new ObjectStreamException with a descriptive error message String
  *
  * @param message The descriptive error message
  */
protected
ObjectStreamException(String message)
{
  super(message);
}

} // class ObjectStreamException

