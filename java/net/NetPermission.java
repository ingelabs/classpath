/*************************************************************************
/* NetPermission.java -- A class for basic miscellaneous network permission
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

import java.security.BasicPermission;

/**
  * This class is used to model miscellaneous network permissions.  It is
  * a subclass of BasicPermission.  This means that it models a "boolean"
  * permission.  One that you either have or do not have.  Thus there is
  * no permitted action list associated with this object. 
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public final class NetPermission extends BasicPermission
{

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Creates a new NetPermission with the specified name which is any valid
  * String.
  *
  * @param name The name of this permission
  */
public
NetPermission(String name)
{
  super(name);
}

/*************************************************************************/

/**
  * Creates a new NetPermission with the specified name and value.  Note that
  * the value field is irrelevant and is ignored.  This constructor should
  * never need to be used.
  *
  * @param name The name of this permission
  * @param perms The permitted actions of this permission (ignored)
  */
public
NetPermission(String name, String perms)
{
  super(name);
}

} // class NetPermission

