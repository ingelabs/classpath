/*************************************************************************
/* HeaderFieldHelper.java -- Helps manage headers fields 
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

package gnu.java.net;

import java.util.Vector;

/**
  * This class manages header field keys and values.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class HeaderFieldHelper
{

/*
 * Instance Variables
 */

private Vector headerFieldKeys;
private Vector headerFieldValues;

/*************************************************************************/

/*
 * Constructors
 */

public 
HeaderFieldHelper()
{
  this(10);
}

/*************************************************************************/

public
HeaderFieldHelper(int size)
{
  headerFieldKeys = new Vector(size);
  headerFieldValues = new Vector(size);
}

/*************************************************************************/

/*
 * Instance Variables
 */

public void
addHeaderField(String key, String value)
{
  headerFieldKeys.addElement(key);
  headerFieldValues.addElement(value);
}

/*************************************************************************/

public String
getHeaderFieldKeyByIndex(int index)
{
  String key = null;

  try
    {
      key = (String)headerFieldKeys.elementAt(index);
    }
  catch(ArrayIndexOutOfBoundsException e) { ; }

  return(key);
}

/*************************************************************************/

public String
getHeaderFieldValueByIndex(int index)
{
  String value = null;

  try
    {
      value = (String)headerFieldValues.elementAt(index);
    }
  catch(ArrayIndexOutOfBoundsException e) { ; }

  return(value);
}

/*************************************************************************/

public int
getNumberOfEntries()
{
  return(headerFieldKeys.size());
}

} // class HeaderFieldHelper

