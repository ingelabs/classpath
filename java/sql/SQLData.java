/*************************************************************************
/* SQLData.java -- Custom mapping for a user defined datatype
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
  * This interface is used for mapping SQL data to user defined datatypes.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract interface SQLData
{

/**
  * This method returns the user defined datatype name for this object.
  *
  * @return The user defined data type name for this object.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getSQLTypeName() throws SQLException;

/*************************************************************************/

/**
  * This method populates the data in the object from the specified stream.
  *
  * @param stream The stream to read the data from.
  * @param name The data type name of the data on the stream.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
readSQL(SQLInput stream, String name) throws SQLException;

/*************************************************************************/

/**
  * This method writes the data in this object to the specified stream.
  *
  * @param stream The stream to write the data to.
  *
  * @exception SQLException If an error occurs.
  */
public abstract void
writeSQL(SQLOutput stream) throws SQLException;

} // interface SQLData

