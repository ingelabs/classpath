/*************************************************************************
/* PasswordAuthentication.java -- Container class for username/password pairs
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

/**
  * This class serves a container for username/password pairs.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public final class PasswordAuthentication
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The username 
  */
private String username;

/**
  * The password
  */
private char[] password;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Creates a new PasswordAuthentication object from the specified username
  * and password.
  *
  * @param username The username for this object
  * @param password The password for this object
  */
public
PasswordAuthentication(String username, char[] password)
{
  this.username = username;
  this.password = password;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the username associated with this object
  *
  * @return The username
  */
public String
getUserName()
{
  return(username);
}
 
/*************************************************************************/

/**
  * Returns the password associated with this object
  *
  * @return The password
  */
public char[]
getPassword()
{
  return(password);
}

} // class PasswordAuthentication

