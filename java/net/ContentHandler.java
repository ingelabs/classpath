/*************************************************************************
/* ContentHandler.java -- Abstract class for handling content from URL's
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

package java.net;

import java.io.IOException;

/**
  * This is an abstract class that is the superclass for classes that read
  * objects from URL's.  Calling the getContent() method in the URL class
  * or the URLConnection class will cause an instance of a subclass of
  * ContentHandler to be created for the MIME type of the object being
  * downloaded from the URL.  Thus, this class is seldom needed by 
  * applications/applets directly, but only indirectly through methods in
  * other classes.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class ContentHandler
{

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Do nothing constructor
  */
public
ContentHandler()
{
  ;
}

/*************************************************************************/

/**
  * This method reads from the InputStream of the passed in URL connection
  * and uses the data downloaded to create an Object represening the
  * content.  For example, if the URL is pointing to a GIF file, this 
  * method might return an Image object.  This method should be overridden
  * by subclasses.
  *
  * @param urlcon A URLConnection object to read data from
  *
  * @return An object representing the data read
  *
  * @exception IOException If an error occurs
  */
public abstract synchronized Object
getContent(URLConnection urlcon) throws IOException;

} // class ContentHandler

