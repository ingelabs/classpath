/*************************************************************************
/* DataTruncation.java -- Warning when data has been truncated.
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
  * This exception is thrown when a piece of data is unexpectedly 
  * truncated in JDBC.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class DataTruncation extends SQLWarning implements java.io.Serializable
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The original size of the data.
  * @serialized
  */
private int dataSize;

/**
  * The index of the parameter or column whose value was truncated.
  * @serialized
  */
private int index;

/**
  * Indicates whether or not a parameter value was truncated.
  * @serialized
  */
private boolean parameter;

/**
  * Indicates whether or not a data column value was truncated.
  * @serialized
  */
private boolean read;

/**
  * This is the size of the data after truncation.
  * @serialized
  */
private int transferSize;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>DataTruncation</code>
  * with the specified values.  The descriptive error message for this 
  * exception will be "Data truncation", the SQL state will be "01004"
  * and the vendor specific error code will be set to 0.
  *
  * @param index The index of the parameter or column that was truncated.
  * @param parameter <code>true</code> if a parameter was truncated,
  * <code>false</code> otherwise.
  * @param read <code>true</code> if a data column was truncated,
  * <code>false</code> otherwise.
  * @param dataSize The original size of the data.
  * @param transferSize The size of the data after truncation.
  */
public
DataTruncation(int index, boolean parameter, boolean read, int dataSize,
               int transferSize)
{
  super("Data truncation", "01004");

  this.index = index;
  this.parameter = parameter;
  this.read = read;
  this.dataSize = dataSize;
  this.transferSize = transferSize;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the index of the column or parameter that was
  * truncated.
  *
  * @return The index of the column or parameter that was truncated.
  */
public int
getIndex()
{
  return(index);
}

/*************************************************************************/

/**
  * This method determines whether or not it was a parameter that was
  * truncated.
  *
  * @return <code>true</code> if a parameter was truncated, <code>false</code>
  * otherwise.
  */
public boolean
getParameter()
{
  return(parameter);
}

/*************************************************************************/

/**
  * This method determines whether or not it was a column that was
  * truncated.
  *
  * @return <code>true</code> if a column was truncated, <code>false</code>
  * otherwise.
  */
public boolean
getRead()
{
  return(read);
}

/*************************************************************************/

/**
  * This method returns the original size of the parameter or column that
  * was truncated.
  *
  * @return The original size of the parameter or column that was truncated.
  */
public int
getDataSize()
{
  return(dataSize);
}

/*************************************************************************/

/**
  * This method returns the size of the parameter or column after it was
  * truncated.
  *
  * @return The size of the parameter or column after it was truncated.
  */
public int
getTransferSize()
{
  return(transferSize);
}

} // class DataTruncation

