/*************************************************************************
/* SocketOptions.java -- Implements options for sockets (duh!)
/*
/* Copyright (c) 1998 by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.net;

/**
  * This internal interface is used by SocketImpl to implement options
  * on sockets.  At least I think.  The javadocs for SocketImpl show it
  * implementing this interface and the Networking Enhancements description
  * for Java 1.1 show two methods that aren't in the public javadocs, so
  * I'll assume this is where they live.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
interface SocketOptions
{

/*************************************************************************/

/*
 * Static Variables
 */

// Note that these contant values were determined by experimentation and
// there is no way to currently know if the symbolic names are the
// same as in the JDK.

/**
  * Option id for the SO_LINGER value
  */
static final int SO_LINGER = 128;

/**
  * Option id for the SO_TIMEOUT value
  */
static final int SO_TIMEOUT = 4102;

/**
  * Option id for the TCP_NODELAY value
  */
static final int TCP_NODELAY = 1;

/**
  * Option id for the IP_TTL (time to live) value. 
  */
static final int IP_TTL = 7777;

/**
  * Options id for the IP_MULTICAST_IF value
  */
static final int IP_MULTICAST_IF = 7778;

/*************************************************************************/

/*
 * Interface Methods
 */

/**
  * Sets the specified option on a socket to the passed in object.  For
  * options that take an integer argument, the passed in object is an
  * Integer.  The option_id parameter is one of the defined constants in
  * this interface.
  *
  * @param option_id The identifier of the option
  * @param val The value to set the option to
  *
  * @exception SocketException If an error occurs
  */
abstract void
setOption(int option_id, Object val) throws SocketException;

/*************************************************************************/

/**
  * Returns the current setting of the specified option.  The Object returned
  * will be an Integer for options that have integer values.  The option_id
  * is one of the defined constants in this interface.
  *
  * @param option_id The option identifier
  *
  * @return The current value of the option
  *
  * @exception SocketException If an error occurs
  */
abstract Object
getOption(int option_id) throws SocketException;

} // interface SocketOptions

