/*************************************************************************
/* NotOwnerException.java -- Attempt to modify an unowned ACL
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

package java.security.acl;

/**
  * This exception is thrown whenever an operation is attempted that requires
  * the caller to be the owner of the access control list (ACL) when the caller
  * is in fact not the owner of the ACL.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class NotOwnerException extends java.lang.Exception
{

/**
  * Initializes a new instance of <code>NotOwnerException</code> that does
  * not have a descriptive message.
  */
public
NotOwnerException()
{
  super();
}

} // class NotOwnerException 

