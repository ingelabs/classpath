/*************************************************************************
/* plain.java -- Content Handler for text/plain type
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

package gnu.java.net.content.text;

import java.net.ContentHandler;
import java.net.URLConnection;
import java.io.IOException;

/**
  * This class is the ContentHandler for the text/plain MIME type.  It
  * simply returns an InputStream object of the text being read.
  *
  * @version 0.1
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class plain extends ContentHandler
{

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Default do nothing constructor
  */
public
plain()
{
  ; 
}

/*************************************************************************/

/**
  * Returns an InputStream as the content for this object
  *
  * @param url_con The URLConnection to get the content of
  *
  * @return An InputStream for that connection
  *
  * @exception IOException If an error occurs
  */
public Object
getContent(URLConnection url_con) throws IOException
{
  return(url_con.getInputStream());
}

} // class plain

