/* Handler.java -- "file" protocol handler for java.net
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

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLConnection;
import java.io.IOException;

/**
  * This is the protocol handler for the "file" protocol.  It implements
  * the abstract openConnection() method from URLStreamHandler by returning
  * a new FileURLConnection object (from this package).  All other 
  * methods are inherited
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Handler extends URLStreamHandler
{

/*************************************************************************/

/*
 * Constructors
 */

/**
  * A do nothing constructor
  */
public
Handler()
{
  ;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returs a new FileURLConnection for the specified URL
  *
  * @param url The URL to return a connection for
  *
  * @return The URLConnection
  *
  * @exception IOException If an error occurs
  */
protected URLConnection
openConnection(URL url) throws IOException
{
  return(new gnu.java.net.protocol.file.FileURLConnection(url));
}

} // class Handler

