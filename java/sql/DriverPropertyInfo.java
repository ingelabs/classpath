/*************************************************************************
/* DriverPropertyInfo.java -- Property information about drivers.
/*
/* Copyright (c) 1999 Free Software Foundation, Inc.
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

package java.sql;

/**
  * This class holds a driver property that can be used for querying or
  * setting driver configuration parameters.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class DriverPropertyInfo
{

/*
 * Instance Variables
 */

/**
  * The name of the property.
  */
public String name;

/**
  * This is the value of the property.
  */
public String value;

/**
  * A description of the property, possibly <code>null</code>.
  */
public String description;

/**
  * A flag indicating whether or not a value for this property is required
  * in order to connect to the database.
  */
public boolean required;

/**
  * If values are restricted to certain choices, this is the list of valid
  * ones.  Otherwise it is <code>null</code>.
  */
public String[] choices;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>DriverPropertyInfo</code>
  * with the specified name and value.  All other fields are defaulted.
  *
  * @param name The name of the property.
  * @param value The value to assign to the property.
  */
public
DriverPropertyInfo(String name, String value)
{
  this.name = name;
  this.value = value;
}

} // DriverPropertyInfo

