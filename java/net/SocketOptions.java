/*************************************************************************
/* SocketOptions.java -- Implements options for sockets (duh!)
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
  * This interface is used by <code>SocketImpl</code> and 
  * <code>DatagramSocketImpl</code> to implement options
  * on sockets.  
  *
  * @version 1.2
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract interface SocketOptions
{

/*************************************************************************/

/*
 * Static Variables
 */


/**
  * Option id for the SO_LINGER value
  */
public static final int SO_LINGER = 128;

/**
  * Option id for the SO_TIMEOUT value
  */
public static final int SO_TIMEOUT = 4102;

/**
  * Retrieve the local address to which the socket is bound.
  */
public static final int SO_BINDADDR = XXX;

/**
  * Option id for the send buffer size
  */
public static final int SO_SNDBUF = XXX;

/**
  * Option id for the receive buffer size
  */
public static final int SO_RCVBUF = XXX;

/**
  * Sets the SO_REUSEADDR parameter on a socket
  */
public static final int SO_REUSEADDR = XXX;

/**
  * Option id for the TCP_NODELAY value
  */
public static final int TCP_NODELAY = 1;

/**
  * Option id for the IP_TTL (time to live) value.  Not public
  */
static final int IP_TTL = 7777;

/**
  * Options id for the IP_MULTICAST_IF value
  */
public static final int IP_MULTICAST_IF = 7778;

/*************************************************************************/

/*
 * Interface Methods
 */

/**
  * Sets the specified option on a socket to the passed in object.  For
  * options that take an integer argument, the passed in object is an
  * <code>Integer</code>.  For options that are set to on or off, the
  * value passed will be a <code>Boolean</code>.   The <code>option_id</code> 
  * parameter is one of the defined constants in this interface.
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
  * Returns the current setting of the specified option.  The 
  * <code>Object</code> returned will be an <code>Integer</code> for options 
  * that have integer values.  For options that are set to on or off, a 
  * <code>Boolean</code> will be returned.   The <code>option_id</code>
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

