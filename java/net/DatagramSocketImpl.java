/*************************************************************************
/* DatagramSocketImpl.java -- Abstract class for UDP socket implementations
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

import java.io.FileDescriptor;
import java.io.IOException;

/**
  * This abstract class models a datagram socket implementation.  An
  * actual implementation class would implement these methods, probably
  * via redirecting them to native code.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class DatagramSocketImpl implements SocketOptions
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The FileDescriptor object for this object.  *** I would really like to know
  * how to create one of these. ******
  */
protected FileDescriptor fd;

/**
  * The local port to which this socket is bound
  */
protected int localPort;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Do nothing constructor
  */
public
DatagramSocketImpl()
{
  ;
}

/*************************************************************************/

/*
 * Instance Methods
 */ 

/**
  * This method binds the socket to the specified local port and address.
  *
  * @param port The port number to bind to
  * @param addr The address to bind to
  *
  * @exception SocketException If an error occurs
  */
protected abstract void
bind(int port, InetAddress addr) throws SocketException;

/*************************************************************************/

/**
  * This methods closes the socket
  */
protected abstract void
close();

/*************************************************************************/

/**
  * Creates a new datagram socket.
  *
  * @exception SocketException If an error occurs
  */
protected abstract void
create() throws SocketException;

/*************************************************************************/

/**
  * Returns the FileDescriptor for this socket
  */
protected FileDescriptor
getFileDescriptor()
{
  return(fd);
}

/*************************************************************************/

/**
  * Returns the local port this socket is bound to
  */
protected int
getLocalPort()
{
  return(localPort);
}

/*************************************************************************/

/**
  * This method returns the current Time to Live (TTL) setting on this
  * socket.
  *
  * @exception IOException If an error occurs
  */
protected abstract byte
getTTL() throws IOException;

/*************************************************************************/

/**
  * Sets the Time to Live (TTL) setting on this socket to the specified
  * value.
  *
  * @param ttl The new Time to Live value
  *
  * @exception IOException If an error occurs
  */
protected abstract void
setTTL(byte ttl) throws IOException;

/*************************************************************************/

/**
  * Causes this socket to join the specified multicast group
  *
  * @param addr The multicast address to join with
  *
  * @exception IOException If an error occurs
  */
protected abstract void
join(InetAddress addr) throws IOException;

/*************************************************************************/

/**
  * Causes the socket to leave the specified multicast group.
  *
  * @param addr The multicast address to leave
  *
  * @exception IOException If an error occurs
  */
protected abstract void
leave(InetAddress addr) throws IOException;

/*************************************************************************/

/**
  * Takes a peek at the next packet received in order to retrieve the
  * address of the sender
  *
  * @param ********** Wish I knew what this was for ************
  *
  * @return ******* Wish I knew ************
  *
  * @exception If an error occurs
  */
protected abstract int
peek(InetAddress addr) throws IOException;

/*************************************************************************/

/**
  * Receives a packet of data from the network  Will block until a packet
  * arrives.  The packet info in populated into the passed in 
  * DatagramPacket object.
  *
  * @param packet A place to store the incoming packet.
  *
  * @exception IOException If an error occurs
  */
protected abstract void
receive(DatagramPacket packet) throws IOException;

/*************************************************************************/

/**
  * Transmits the specified packet of data to the network.  The destination
  * host and port should be encoded in the packet.
  *
  * @param packet The packet to send
  *
  * @exception IOException If an error occurs
  */
protected abstract void
send(DatagramPacket packet) throws IOException;

} // class DatagramSocketImpl 

