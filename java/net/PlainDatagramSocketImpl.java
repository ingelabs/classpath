/* PlainDatagramSocketImpl.java -- Default DatagramSocket implementation
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.net;

import java.io.IOException;
import gnu.classpath.Configuration;

/**
  * This is the default socket implementation for datagram sockets.
  * It makes native calls to C routines that implement BSD style
  * SOCK_DGRAM sockets in the AF_INET family.
  *
  * @version 0.1
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class PlainDatagramSocketImpl extends DatagramSocketImpl
{

/*************************************************************************/

/*
 * Static Variables
 */

// Static initializer to load native library
  static
  {
    if (Configuration.INIT_LOAD_LIBRARY)
      {
	System.loadLibrary("javanet");
      }
  }

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the actual underlying file descriptor
  */
protected int native_fd = -1;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Default do nothing constructor
  */
public
PlainDatagramSocketImpl()
{
  ;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Creates a new datagram socket
  *
  * @exception SocketException If an error occurs
  */
protected native synchronized void create() throws SocketException;

/*************************************************************************/

/**
  * Closes the socket
  */
protected native synchronized void close();

/*************************************************************************/

/**
  * Binds this socket to a particular port and interface
  *
  * @param port The port to bind to
  * @param addr The address to bind to
  *
  * @exception SocketException If an error occurs
  */
protected native synchronized void bind(int port, InetAddress addr) throws SocketException;

/*************************************************************************/

/**
  * Sends a packet of data to a remote host
  *
  * @param packet The packet to send
  *
  * @exception IOException If an error occurs
  */
protected synchronized void
send(DatagramPacket packet) throws IOException
{
  sendto(packet.getAddress(), packet.getPort(), packet.getData(), 
        packet.getLength());
}

/*************************************************************************/

/**
  * Sends a packet of data to a remote host
  *
  * @param addr The address to send to
  * @param port The port to send to 
  * @param buf The buffer to send
  * @param len The length of the data to send
  *
  * @exception IOException If an error occurs
  */
private native synchronized void sendto(InetAddress addr, int port, byte[] buf, int len) throws IOException;

/*************************************************************************/

/**
  * What does this method really do?
  */
protected synchronized int
peek(InetAddress addr) throws IOException
{
  throw new IOException("Not Implemented Yet");
}

/*************************************************************************/

/**
  * Receives a UDP packet from the network
  *
  * @param packet The packet to fill in with the data received
  *
  * @exception IOException IOException If an error occurs
  */
protected native synchronized void receive(DatagramPacket packet) throws IOException;

/*************************************************************************/

/**
  * Joins a multicast group
  *
  * @param addr The group to join
  *
  * @exception IOException If an error occurs
  */
protected native synchronized void join(InetAddress addr) throws IOException;

/*************************************************************************/

/**
  * Leaves a multicast group
  *
  * @param addr The group to leave
  *
  * @exception IOException If an error occurs
  */
protected native synchronized void leave(InetAddress addr) throws IOException;

/*************************************************************************/

/**
  * Gets the Time to Live value for the socket
  *
  * @return The TTL value
  *
  * @exception IOException If an error occurs
  */
protected synchronized byte
getTTL() throws IOException
{
  Object obj = getOption(SocketOptions.IP_TTL);

  if (!(obj instanceof Integer))
    throw new IOException("Internal Error");

  return(((Integer)obj).byteValue());
}

/*************************************************************************/

/**
  * Sets the Time to Live value for the socket
  *
  * @param ttl The new TTL value
  *
  * @exception IOException If an error occurs
  */
protected synchronized void
setTTL(byte ttl) throws IOException
{
  setOption(SocketOptions.IP_TTL, new Integer(ttl));
}

/*************************************************************************/

/**
  * Gets the Time to Live value for the socket
  *
  * @return The TTL value
  *
  * @exception IOException If an error occurs
  */
protected synchronized int
getTimeToLive() throws IOException
{
  Object obj = getOption(SocketOptions.IP_TTL);

  if (!(obj instanceof Integer))
    throw new IOException("Internal Error");

  return(((Integer)obj).intValue());
}

/*************************************************************************/

/**
  * Sets the Time to Live value for the socket
  *
  * @param ttl The new TTL value
  *
  * @exception IOException If an error occurs
  */
protected synchronized void
setTimeToLive(int ttl) throws IOException
{
  setOption(SocketOptions.IP_TTL, new Integer(ttl));
}

/*************************************************************************/

/**
  * Retrieves the value of an option on the socket
  *
  * @param option_id The identifier of the option to retrieve
  *
  * @return The value of the option
  *
  * @exception SocketException If an error occurs
  */
public native synchronized Object getOption(int option_id) throws SocketException;

/*************************************************************************/

/**
  * Sets the value of an option on the socket
  *
  * @param option_id The identifier of the option to set
  * @param val The value of the option to set
  *
  * @exception SocketException If an error occurs
  */
public native synchronized void setOption(int option_id, Object val) throws SocketException;

} // class PlainDatagramSocketImpl

