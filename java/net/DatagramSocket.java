/*************************************************************************
/* DatagramSocket.java -- A class to model UDP sockets
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
private DatagramSocketImpl impl;

/**
  * This is the address we are bound to
  */
private InetAddress local_addr;

/**
  * This is the address we are "connected" to
  */
private InetAddress remote_addr;

/**
  * This is the port we are "connected" to
  */
private int remote_port = -1;

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
  * Closes this socket. 
  */
public synchronized void
close()
{
  impl.close();
}
  
/*************************************************************************/

/**
  * This method returns the remote address to which this socket is 
  * connected.  If this socket is not connected, then this method will
  * return <code>null</code>.
  *
  * @return The remote address.
  */
public InetAddress
getInetAddress()
{
  return(remote_address);
}

/*************************************************************************/

/**
  * This method returns the remote port to which this socket is
  * connected.  If this socket is not connected, then this method will
  * return -1.
  *
  * @return The remote port
  */
public int
getPort()
{
  return(remote_port);
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
  * This method returns the value of the system level socket option
  * SO_SNDBUF, which is used by the operating system to tune buffer
  * sizes for data transfers.
  *
  * @return The send buffer size.
  *
  * @exception SocketException If an error occurs.
  */
public synchronized int
getSendBufferSize() throws SocketException
{
  Object obj = impl.getOption(SocketOptions.SO_SNDBUF);

  if (obj instanceof Integer)
    return(((Integer)obj).intValue());
  else
    throw new SocketException("Unexpected type");
}

/*************************************************************************/

/**
  * This method sets the value for the system level socket option
  * SO_SNDBUF to the specified value.  Note that valid values for this
  * option are specific to a given operating system.
  *
  * @param size The new send buffer size.
  *
  * @exception SocketException If an error occurs.
  */
public synchronized void
setSendBufferSize(int size)
{
  impl.setOption(SocketOptions.SO_SNDBUF, new Integer(size));
}

/*************************************************************************/

/**
  * This method returns the value of the system level socket option
  * SO_RCVBUF, which is used by the operating system to tune buffer
  * sizes for data transfers.
  *
  * @return The receive buffer size.
  *
  * @exception SocketException If an error occurs.
  */
public synchronized int
getReceiveBufferSize() throws SocketException
{
  Object obj = impl.getOption(SocketOptions.SO_RCVBUF);

  if (obj instanceof Integer)
    return(((Integer)obj).intValue());
  else
    throw new SocketException("Unexpected type");
}

/*************************************************************************/

/**
  * This method sets the value for the system level socket option
  * SO_RCVBUF to the specified value.  Note that valid values for this
  * option are specific to a given operating system.
  *
  * @param size The new receive buffer size.
  *
  * @exception SocketException If an error occurs.
  */
public synchronized void
setReceiveBufferSize(int size)
{
  impl.setOption(SocketOptions.SO_RCVBUF, new Integer(size));
}

/*************************************************************************/

/**
  * This method connects this socket to the specified address and port.
  * When a datagram socket is connected, it will only send or receive
  * packate to and from the host to which it is connected.  A multicast
  * socket that is connected may only send and not receive packets.
  *
  * @param addr The address to connect this socket to.
  * @param port The port to connect this socket to.
  *
  * @exception SecurityException If connections to this addr/port are not allowed.
  * @exception IllegalArgumentException If the addr or port are invalid.
  */
public void
connect(InetAddress addr, int port) throws SecurityException, 
                                           IllegalArgumentException
{
  if ((port < 1) || (port > 65535))
    throw new IllegalArgumentException("Bad port number: " + port);

  SecurityManager sm = System.getSecurityManager();
  if (sm != null)
    sm.checkConnect(addr, port);

  this.remote_addr = addr;
  this.remote_port = port;

  /* Shit, we can't do this even though the OS supports it since this method
  isn't in DatagramSocketImpl.  What was Sun thinking? */
//  impl.connect(addr, port);
} 

/*************************************************************************/

/**
  * This method disconnects this socket from the addr/port it was 
  * connected to.  If the socket was not connected in the first place,
  * this method does nothing.
  */
public void
disconnect()
{
  // See my comments on connect()
  this.remote_addr = null;
  this.remote_port = -1;
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

