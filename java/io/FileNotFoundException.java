/*************************************************************************
/* FileNotFoundException.java -- The requested file could not be found
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
  * This exception is thrown when an attempt is made to access a file that
  * does not exist.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FileNotFoundException extends IOException
{

/*
 * Constructors
 */

/**
  * Create a new FileNotFoundException without a descriptive error message
  */
public
FileNotFoundException()
{
  super();
}

/*************************************************************************/

/**
  * Create a new FileNotFoundException with a descriptive error message String
  *
  * @param message The descriptive error message
  */
public
FileNotFoundException(String message)
{
  super(message);
}

} // class FileNotFoundException

