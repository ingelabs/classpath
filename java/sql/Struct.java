/*************************************************************************
/* Struct.java -- Mapping for a SQL structured type.
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

import java.util.Map;

/**
  * This interface implements the standard type mapping for a SQL 
  * structured type.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract interface Struct
{

/**
  * This method returns the name of the SQL structured type for this
  * object.
  *
  * @return The SQL structured type name.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getSQLTypeName() throws SQLException;

/*************************************************************************/

/**
  * This method returns the attributes of this SQL structured type.
  *
  * @return The attributes of this structure type.
  *
  * @exception SQLException If an error occurs.
  */
public abstract Object[]
getAttributes() throws SQLException;

/*************************************************************************/

/**
  * This method returns the attributes of this SQL structured type.
  * The specified map of type mappings overrides the default mappings.
  *
  * @param map The map of SQL type mappings.
  *
  * @return The attributes of this structure type.
  *
  * @exception SQLException If a error occurs.
  */
public abstract Object[]
getAttributes(Map map) throws SQLException;

} // interface Struct

