/*************************************************************************
/* Annotation.java -- Wrapper for a text attribute object
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

package java.text;

/**
  * This class is used as a wrapper for a text attribute object.  Annotation
  * objects are associated with a specific range of text.  Changing either
  * the text range or the underlying text invalidates the object.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Annotation
{

/*
 * Instance Variables
 */

/**
  * This is the attribute object being wrappered
  */
private Object attrib;

/*************************************************************************/

/**
  * Constructors
  */

/**
  * This method initializes a new instance of <code>Annotation</code> to
  * wrapper the specified text attribute object.
  *
  * @param attrib The text attribute <code>Object</code> to wrapper.
  */
public
Annotation(Object attrib)
{
  this.attrib = attrib;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This method returns the text attribute object this <code>Annotation</code>
  * instance is wrappering.
  *
  * @return The text attribute object for this <code>Annotation</code>.
  */
public Object
getValue()
{
  return(attrib);
}

/*************************************************************************/

/**
  * This method returns a <code>String</code> representation of this
  * object.
  *
  * @return This object as a <code>String</code>.
  */
public String
toString()
{
  return("java.text.Annotation for: " + attrib.toString());
}

} // class Annotation

