/*************************************************************************
/* DatagramSocket.java -- A class to model UDP sockets
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

import java.io.IOException;

/**
  * This class models a connectionless datagram socket that sends 
  * individual packets of data across the network.  In the TCP/IP world,
  * this means UDP.  Datagram packets do not have guaranteed delivery,
  * or any guarantee about the order the data will be received on the
  * remote host.
  * <p>
  * 
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class DatagramSocket
{

/*************************************************************************/

/**
  * Instance Variables
  */

/**
  * This is the implementation object used by this socket.
  */
protected DatagramSocketImpl impl;

/**
  * This is the address we are bound to
  */
protected InetAddress local_addr;

/*************************************************************************/

/**
  * Constructors
  */

/**
  * Create a DatagramSocket that binds to a random port and every
  * address on the local machine.
  *
  * @exception SocketException If an error occurs
  */
public
DatagramSocket() throws SocketException
{
  this(0, null);
}

/*************************************************************************/

/**
  * Create a DatagramSocket that binds to the specified port and every
  * address on the local machine
  *
  * @param port The local port number to bind to
  *
  * @exception SocketException If an error occurs
  */
public
DatagramSocket(int port) throws SocketException
{
  this(port, null);
}

/*************************************************************************/

/**
  * Create a DatagramSocket that binds to the specified local port and
  * address
  *
  * @param port The local port number to bind to
  * @param addr The local address to bind to
  *
  * @exception SocketException If an error occurs
  */
public
DatagramSocket(int port, InetAddress addr) throws SocketException
{
  // Why is there no factory for this?
  impl = new PlainDatagramSocketImpl();

  impl.create();

  try
    {
      if (addr == null)
         addr = InetAddress.getInaddrAny();

      impl.localPort = port;
      this.local_addr = addr;

      impl.bind(port, addr);

      local_addr = addr;
    }
  catch (UnknownHostException e)
    {
      throw new SocketException(e.toString());
    }
}

/*************************************************************************/

/**
  * Ensure that the socket is closed when this object is garbage collected
  */
protected void
finalize() throws IOException
{
  close();
}

/*************************************************************************/

/**
  * Closes this socket. 
  */
public synchronized void
close()
{
  impl.close();
}
  
/*************************************************************************/

/**
  * Returns the local address this socket is bound to
  */
public InetAddress
getLocalAddress()
{
  return(local_addr);
}

/*************************************************************************/

/**
  * Returns the local port this socket is bound to
  */
public int
getLocalPort()
{
  return(impl.getLocalPort());
}

/*************************************************************************/

/**
  * Returns the value of the socket's SO_TIMEOUT setting.  If this method
  * returns 0 then SO_TIMEOUT is disabled.
  *
  * @exception SocketException If an error occurs
  */
public synchronized int
getSoTimeout() throws SocketException
{
  Object obj = impl.getOption(SocketOptions.SO_TIMEOUT);

  if (obj instanceof Integer)
    return(((Integer)obj).intValue());
  else
    throw new SocketException("Internal Error");
}

/*************************************************************************/

/**
  * Sets the value of the socket's SO_TIMEOUT value.  A value of 0 will
  * disable SO_TIMEOUT.  Any other value is the number of milliseconds
  * a socket read/write will block before timing out.
  *
  * @param timeout The new SO_TIMEOUT value
  *
  * @exception SocketException If an error occurs
  */
public synchronized void
setSoTimeout(int timeout) throws SocketException
{
  impl.setOption(SocketOptions.SO_TIMEOUT, new Integer(timeout));
}

/*************************************************************************/

/**
  * Reads a datagram packet from the socket.  Note that this method
  * will block until a packet is received from the network.  On return,
  * the passed in DatagramPacket is populated with the data received
  * and all the other information about the packet.
  *
  * @param packet A DatagramPacket for storing the data
  *
  * @exception IOException If an error occurs
  */
public synchronized void
receive(DatagramPacket packet) throws IOException
{
  impl.receive(packet);
}

/*************************************************************************/

/**
  * Sends the specified packet.  The host and port to which the packet
  * are to be sent should be set inside the packet.
  *
  * @param packet The packet of data to send
  *
  * @exception IOException If an error occurs
  */
public synchronized void
send(DatagramPacket packet) throws IOException
{
  impl.send(packet);
}

} // class DatagramSocket

