/*************************************************************************
/* Ref.java -- Reference to a SQL structured type.
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
  * This interface provides a mechanism for obtaining information about
  * a SQL structured type
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract interface Ref
{

/**
  * This method returns the fully qualified name of the SQL structured
  * type of the referenced item.
  *
  * @return The fully qualified name of the SQL structured type.
  *
  * @exception SQLException If an error occurs.
  */
public abstract String
getBaseTypeName() throws SQLException;

} // interface Ref

