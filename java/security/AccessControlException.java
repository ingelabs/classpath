/*************************************************************************
/* AccessControlException.java -- Permission is denied
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

package java.security;

/**
  * This exception is thrown when the <code>AccessController</code> denies
  * an attempt to perform an operation.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class AccessControlException extends SecurityException
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The <code>Permission</code> associated with this exception
  */
private Permission perm;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>AccessControlException</code>
  * with a descriptive error message.  There will be no <code>Permission</code>
  * object associated with this exception.
  *
  * @param msg The descriptive error message
  */
public
AccessControlException(String msg)
{
  super(msg);
}

/*************************************************************************/

/**
  * This method initializes a new instance of <code>AccessControlException</code>
  * with a descriptive error message and an instance of <code>Permission</code>
  * that is the permission that caused the exception to be thrown.
  *
  * @param msg The descriptive error message
  * @param perm The <code>Permission</code> object that caused this exception.
  */
public 
AccessControlException(String msg, Permission perm)
{
  super(msg);

  this.perm = perm;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the <code>Permission</code> object that caused
  * this exception to be thrown.
  *
  * @return The requested <code>Permission</code> object, or <code>null</code> if none is available.
  */
public Permission
getPermission()
{
  return(perm);
}

} // class AccessControlException

