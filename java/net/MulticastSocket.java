/*************************************************************************
/* MulticastSocket.java -- Class for using multicast sockets
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

import java.io.IOException;

/**
  * This class models a multicast UDP socket.  A multicast address is a
  * class D internet address (one whose most significant bits are 1110).  
  * A multicast group consists of a multicast address and a well known
  * port number.  All members of the group listening on that address and
  * port will receive all the broadcasts to the group.
  * <p>
  * Please note that applets are not allowed to use multicast sockets 
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class MulticastSocket extends DatagramSocket
{

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create a MulticastSocket that this not bound to any address
  *
  * @exception IOException If an error occurs
  */
public
MulticastSocket() throws IOException
{
  super();
}

/*************************************************************************/

/**
  * Create a multicast socket bound to the specified port
  *
  * @param The port to bind to
  *
  * @exception IOException If an error occurs
  */
public
MulticastSocket(int port) throws IOException
{
  super(port);
}
 
/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the current value of the "Time to Live" option.  This is the
  * number of hops a packet can make before it "expires".   This method id
  * deprecated.  Use <code>getTimeToLive</code> instead.
  * 
  * @return The TTL value
  *
  * @exception IOException If an error occurs
  * @deprecated
  */
public synchronized byte
getTTL() throws IOException
{
  return(impl.getTTL());
}

/*************************************************************************/

/**
  * Sets the "Time to Live" value for a socket.  The value must be between
  * 1 and 255.  This method deprecated.  Use <code>setTimeToLive</code>
  * instead.
  *
  * @param ttl The new TTL value
  *
  * @exception IOException If an error occurs
  * @deprecated
  */
public synchronized void
setTTL(byte ttl) throws IOException
{
  impl.setTTL(ttl);
}

/*************************************************************************/

/**
  * Returns the current value of the "Time to Live" option.  This is the
  * number of hops a packet can make before it "expires". 
  * 
  * @return The TTL value
  *
  * @exception IOException If an error occurs
  */
public synchronized int
getTimeToLive() throws IOException
{
  return(impl.getTimeToLive());
}

/*************************************************************************/

/**
  * Sets the "Time to Live" value for a socket.  The value must be between
  * 1 and 255.  
  *
  * @param ttl The new TTL value
  *
  * @exception IOException If an error occurs
  */
public synchronized void
setTimeToLive(int ttl) throws IOException
{
  impl.setTimeToLive(ttl);
}

/*************************************************************************/

/**
  * Returns the interface being used for multicast packets
  * 
  * @return The multicast interface
  *
  * @exception SocketException If an error occurs
  */
public synchronized InetAddress
getInterface() throws SocketException
{
  Object obj;

  obj = impl.getOption(SocketOptions.IP_MULTICAST_IF);

  if (!(obj instanceof InetAddress))
    throw new SocketException("Internal Error");

  return(((InetAddress)obj));
}

/*************************************************************************/

/**
  * Sets the interface to use for multicast packets.
  *
  * @param addr The new interface to use
  *
  * @exception SocketException If an error occurs
  */
public synchronized void
setInterface(InetAddress addr) throws SocketException
{
  impl.setOption(SocketOptions.IP_MULTICAST_IF, addr);
}

/*************************************************************************/

/**
  * Joins the specified mulitcast group.
  *
  * @param addr The address of the group to join
  * 
  * @exception IOException If an error occurs
  */
public synchronized void
joinGroup(InetAddress addr) throws IOException
{
  impl.join(addr);
}

/*************************************************************************/

/**
  * Leaves the specified multicast group
  *
  * @param addr The address of the group to leave
  *
  * @exception IOException If an error occurs
  */
public synchronized void
leaveGroup(InetAddress addr) throws IOException
{
  impl.leave(addr);
}

/*************************************************************************/

/**
  * Sends a packet of data to a multicast address with a TTL that is
  * different from the default TTL on this socket.  The default TTL for
  * the socket is not changed.
  *
  * @param packet The packet of data to send
  * @param ttl The TTL for this packet
  *
  * @exception IOException If an error occurs
  */
public synchronized void
send(DatagramPacket packet, byte ttl) throws IOException
{
  byte old_ttl = getTTL();
  setTTL(ttl);
  send(packet);
  setTTL(old_ttl);
}

} // class MulticastSocket

