/*************************************************************************
/* FileReader.java -- Convenience class for reading characters from a file
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
  * This class provides a convenient way to set up a <code>Reader</code>
  * to read from a file.  It opens the specified file for reading and creates
  * the <code>InputStreamReader</code> to read from the 
  * resulting <code>FileInputStream</code>.  This class can only be used
  * to read from files using the default character encoding.  Use
  * <code>InputStreamReader</code> directly to use a non-default encoding.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FileReader extends InputStreamReader
{

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a <code>FileReader</code> instance to read from
  * the specified <code>File</code> object.
  *
  * @param file The <code>File</code> object representing the file to read from
  *
  * @exception SecurityException If read access to the file is forbidden by the <code>SecurityManager</code>
  * @exception FileNotFoundException If the file is not found or some other error occurs
  */
public
FileReader(File file) throws FileNotFoundException, SecurityException
{
  super(new FileInputStream(file));
}

/*************************************************************************/

/**
  * This method initializes a <code>FileReader</code> instance to read from
  * this specified <code>FileDescriptor</code> object.
  *
  * @param fd The <code>FileDescriptor</code> to read from.
  *
  * @exception SecurityException If read access to the file is forbidden by the <code>SecurityManager</code>
  */
public
FileReader(FileDescriptor fd) throws SecurityException
{
  super(new FileInputStream(fd));
}
 
/*************************************************************************/

/**
  * This method initializes a <code>FileReader</code> instance to read from
  * the specified named file.
  *
  * @param name The name of the file to read from
  *
  * @exception SecurityException If read access to the file is forbidden by the <code>SecurityManager</code>
  * @exception FileNotFoundException If the file is not found or some other error occurs
  */
public
FileReader(String name) throws FileNotFoundException, SecurityException
{
  super(new FileInputStream(name));
}

} // class FileReader

