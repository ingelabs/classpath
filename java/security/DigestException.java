/*************************************************************************
/* DigestException.java -- A generic message digest exception
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
  * This exception indicates that a generic message digest exception has 
  * occurred.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class DigestException extends GeneralSecurityException
{

/**
  * This method initializes a new <code>DigestException</code> with no
  * descriptive message.
  */
public
DigestException()
{
  super();
}

/*************************************************************************/

/**
  * This method initializes a new instance of <code>DigestException</code>
  * with a descriptive error message.
  *
  * @param msg The descriptive message
  */
public
DigestException(String msg)
{
  super(msg);
}

} // class DigestException

