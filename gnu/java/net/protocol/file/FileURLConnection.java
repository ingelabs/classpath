/* FileURLConnection.java -- URLConnection class for "file" protocol
   Copyright (C) 1998 Free Software Foundation, Inc.

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


package gnu.java.net.protocol.file;

/**
  * This subclass of java.net.URLConnection models a URLConnection via
  * the "file" protocol.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FileURLConnection extends java.net.URLConnection
{

/*
 * Instance Variables
 */

/**
  * This is a File object for this connection
  */
private java.io.File file;

/**
  * InputStream if we are reading from the file
  */
private java.io.FileInputStream in_stream;

/**
  * OutputStream if we are writing to the file
  */
private java.io.FileOutputStream out_stream;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Calls superclass constructor to initialize
  */
protected
FileURLConnection(java.net.URL url)
{
  super(url);

  /* Set up some variables */
  doOutput = false;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * "Connects" to the file by opening it.
  */
public void
connect() throws java.io.IOException
{
  file = new java.io.File(getURL().getFile());
}

/*************************************************************************/

/**
  * Opens the file for reading and returns a stream for it.
  *
  * @return An InputStream for this connection.
  *
  * @exception IOException If an error occurs
  */
public java.io.InputStream
getInputStream() throws java.io.IOException
{
  if (!connected)
    connect();

  in_stream = new java.io.FileInputStream(file);

  return(in_stream);
}

/*************************************************************************/

/**
  * Opens the file for writing and returns a stream for it.
  *
  * @return An OutputStream for this connection.
  *
  * @exception IOException If an error occurs.
  */
public java.io.OutputStream
getOutputStream() throws java.io.IOException
{
  if (!connected)
    connect();

  out_stream = new java.io.FileOutputStream(file);

  return(out_stream);
}

} // class FileURLConnection

