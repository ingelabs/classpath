/*************************************************************************
/* PrivilegedActionException.java -- An exception occurred in a privileged action
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

import java.io.PrintStream;
import java.io.PrintWriter;

/**
  * This exception is thrown when an exception is thrown during a
  * privileged action being performed with the 
  * <code>AccessController.doPrivileged()</code> method.  It wrappers the
  * actual exception thrown in the privileged code.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class PrivilegedActionException extends Exception
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the actual exception that occurred
  */
private Exception e;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>PrivilegedActionException</code>
  * that wrappers the specified <code>Exception</code>.
  *
  * @param e The <code>Exception</code> to wrapper
  */
public
PrivilegedActionException(Exception e)
{
  this.e = e;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the underlying <code>Exception</code> that caused
  * this exception to be raised.
  *
  * @return The wrappered <code>Exception</code>.
  */
public Exception
getException()
{
  return(e);
}

/*************************************************************************/

/**
  * This method prints the stack trace of the wrappered exception.
  */
public void
printStackTrace()
{
  e.printStackTrace();
}

/*************************************************************************/

/**
  * This method prints the stack trace of the wrappered exception to the
  * specified <code>PrintStream</code>.
  *
  * @param ps The <code>PrintStream</code> to print the stack trace to.
  */
public void
printStackTrace(PrintStream ps)
{
  e.printStackTrace(ps);
}

/*************************************************************************/

/**
  * This method prints the stack trace of the wrappered exception to the
  * specified <code>PrintWriter</code>.
  *
  * @param pw The <code>PrintWriter</code> to print the stack trace to.
  */
public void
printStackTrace(PrintWriter pw)
{
  e.printStackTrace(pw);
}

} // class PrivilegedActionException

