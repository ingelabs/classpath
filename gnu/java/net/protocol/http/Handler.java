/*************************************************************************
/* Handler.java -- HTTP protocol handler for java.net
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

package gnu.java.net.protocol.http;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLConnection;
import java.io.IOException;

/**
  * This is the protocol handler for the HTTP protocol.  It implements
  * the abstract openConnection() method from URLStreamHandler by returning
  * a new HttpURLConnection object (from this package).  All other 
  * methods are inherited
  *
  * @version 0.1
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
  * This method returs a new HttpURLConnection for the specified URL
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
  return(new gnu.java.net.protocol.http.HttpURLConnection(url));
}

} // class Handler

