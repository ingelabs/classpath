/* DatagramSocket.java -- A class to model UDP sockets
   Copyright (C) 1998, 1999, 2000, 2002 Free Software Foundation, Inc.

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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

package java.net;

import java.io.IOException;

/*
 * Written using on-line Java Platform 1.2 API Specification, as well
 * as "The Java Class Libraries", 2nd edition (Addison-Wesley, 1998).
 * Status:  Believed complete and correct.
 */

/**
 * This class models a connectionless datagram socket that sends 
 * individual packets of data across the network.  In the TCP/IP world,
 * this means UDP.  Datagram packets do not have guaranteed delivery,
 * or any guarantee about the order the data will be received on the
 * remote host.
 * 
 * @author Aaron M. Renn (arenn@urbanophile.com)
 * @author Warren Levy (warrenl@cygnus.com)
 */
public class DatagramSocket
{

  // Instance Variables

  /**
   * This is the implementation object used by this socket.
   */
  DatagramSocketImpl impl;

  /**
   * This is the local address which cannot be changed
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

  /**
   * Is this a "connected" datagram socket?
   */
  private boolean connected = false;

  // Constructors

  /**
   * Initializes a new instance of <code>DatagramSocket</code> that binds to 
   * a random port and every address on the local machine.
   *
   * @exception SocketException If an error occurs.
   */
  public DatagramSocket() throws SocketException
  {
    this(0, null);
  }

  /**
   * Initializes a new instance of <code>DatagramSocket</code> that binds to 
   * the specified port and every address on the local machine.
   *
   * @param port The local port number to bind to
   *
   * @exception SocketException If an error occurs.
   */
  public DatagramSocket(int port) throws SocketException
  {
    this(port, null);
  }

  /**
   * Initializes a new instance of <code>DatagramSocket</code> that binds to 
   * the specified local port and address.
   *
   * @param port The local port number to bind to
   * @param laddr The local address to bind to
   *
   * @exception SocketException If an error occurs
   */
  public DatagramSocket(int port, InetAddress laddr) throws SocketException
  {
    if (port < 0 || port > 65535)
      throw new IllegalArgumentException("Invalid port: " + port);

    SecurityManager s = System.getSecurityManager();
    if (s != null)
      s.checkListen(port);
  
    // Why is there no factory for this?
    impl = new PlainDatagramSocketImpl();

    impl.create();

    if (laddr == null)
       laddr = InetAddress.ANY_IF;

    local_addr = laddr;
    impl.bind(port, laddr);
  }

  /**
   * Closes this socket. 
   */
  public void close()
  {
    impl.close();
  }
  
  /**
   * This method returns the remote address to which this socket is 
   * connected.  If this socket is not connected, then this method will
   * return <code>null</code>.
   *
   * @return The remote address.
   *
   * @since 1.2
   */
  public InetAddress getInetAddress()
  {
    return remote_addr;
  }

  /**
   * This method returns the remote port to which this socket is
   * connected.  If this socket is not connected, then this method will
   * return -1.
   *
   * @return The remote port
   *
   * @since 1.2
   */
  public int getPort()
  {
    return remote_port;
  }

  /**
   * Returns the local address this socket is bound to.
   */
  public InetAddress getLocalAddress()
  {
    if (impl == null)
      return(null);

    // FIXME: According to libgcj, checkConnect() is supposed to be called
    // before performing this operation.  Problems: 1) We don't have the
    // addr until after we do it, so we do a post check.  2). The docs I
    // see don't require this in the Socket case, only DatagramSocket, but
    // we'll assume they mean both.
    SecurityManager sm = System.getSecurityManager();
    if (sm != null)
      sm.checkConnect(local_addr.getHostName(), getLocalPort());

    return local_addr;
  }

  /**
   * Returns the local port this socket is bound to
   */
  public int getLocalPort()
  {
    return impl.getLocalPort();
  }

  /**
   * Returns the value of the socket's SO_TIMEOUT setting.  If this method
   * returns 0 then SO_TIMEOUT is disabled.
   *
   * @exception SocketException If an error occurs
   */
  public synchronized int getSoTimeout() throws SocketException
  {
    Object timeout = impl.getOption(SocketOptions.SO_TIMEOUT);

    if (timeout instanceof Integer)
      return(((Integer)timeout).intValue());
    else
      throw new SocketException("Internal Error");
  }

  /**
   * Sets the value of the socket's SO_TIMEOUT value.  A value of 0 will
   * disable SO_TIMEOUT.  Any other value is the number of milliseconds
   * a socket read/write will block before timing out.
   *
   * @param timeout The new SO_TIMEOUT value
   *
   * @exception SocketException If an error occurs
   */
  public void setSoTimeout(int timeout) throws SocketException
  {
    if (timeout < 0)
      throw new IllegalArgumentException("Timeout value is less than 0");

    impl.setOption(SocketOptions.SO_TIMEOUT, new Integer(timeout));
  }

  /**
   * This method returns the value of the system level socket option
   * SO_SNDBUF, which is used by the operating system to tune buffer
   * sizes for data transfers.
   *
   * @return The send buffer size.
   *
   * @exception SocketException If an error occurs.
   *
   * @since 1.2
   */
  public int getSendBufferSize() throws SocketException
  {
    Object obj = impl.getOption(SocketOptions.SO_SNDBUF);

    if (obj instanceof Integer)
      return(((Integer)obj).intValue());
    else
      throw new SocketException("Unexpected type");
  }

  /**
   * This method sets the value for the system level socket option
   * SO_SNDBUF to the specified value.  Note that valid values for this
   * option are specific to a given operating system.
   *
   * @param size The new send buffer size.
   *
   * @exception SocketException If an error occurs.
   *
   * @since 1.2
   */
  public void setSendBufferSize(int size) throws SocketException
  {
    if (size < 0)
      throw new IllegalArgumentException("Buffer size is less than 0");

    impl.setOption(SocketOptions.SO_SNDBUF, new Integer(size));
  }

  /**
   * This method returns the value of the system level socket option
   * SO_RCVBUF, which is used by the operating system to tune buffer
   * sizes for data transfers.
   *
   * @return The receive buffer size.
   *
   * @exception SocketException If an error occurs.
   *
   * @since 1.2
   */
  public int getReceiveBufferSize() throws SocketException
  {
    Object obj = impl.getOption(SocketOptions.SO_RCVBUF);

    if (obj instanceof Integer)
      return(((Integer)obj).intValue());
    else
      throw new SocketException("Unexpected type");
  }

  /**
   * This method sets the value for the system level socket option
   * SO_RCVBUF to the specified value.  Note that valid values for this
   * option are specific to a given operating system.
   *
   * @param size The new receive buffer size.
   *
   * @exception SocketException If an error occurs.
   *
   * @since 1.2
   */
  public void setReceiveBufferSize(int size) throws SocketException
  {
    if (size < 0)
      throw new IllegalArgumentException("Buffer size is less than 0");

    impl.setOption(SocketOptions.SO_RCVBUF, new Integer(size));
  }

  /**
   * This method connects this socket to the specified address and port.
   * When a datagram socket is connected, it will only send or receive
   * packate to and from the host to which it is connected.  A multicast
   * socket that is connected may only send and not receive packets.
   *
   * @param addr The address to connect this socket to.
   * @param port The port to connect this socket to.
   *
   * @exception SecurityException If connections to this addr/port are not
   * allowed.
   * @exception IllegalArgumentException If the addr or port are invalid.
   *
   * @since 1.2
   */
  public void connect(InetAddress addr, int port)
    throws SecurityException, IllegalArgumentException
  {
    if (addr == null)
      throw new IllegalArgumentException("Connect address is null");

    if ((port < 1) || (port > 65535))
      throw new IllegalArgumentException("Bad port number: " + port);

    SecurityManager sm = System.getSecurityManager();
    if (sm != null)
      sm.checkConnect(addr.getHostName(), port);

    this.remote_addr = addr;
    this.remote_port = port;

    /* FIXME: Shit, we can't do this even though the OS supports it since this 
       method isn't in DatagramSocketImpl. */
    //  impl.connect(addr, port);

    connected = true;
  } 

  /**
   * This method disconnects this socket from the addr/port it was 
   * connected to.  If the socket was not connected in the first place,
   * this method does nothing.
   *
   * @since 1.2
   */
  public void disconnect()
  {
    // FIXME: See my comments on connect()
    this.remote_addr = null;
    this.remote_port = -1;
    connected = false;
  }

  /**
   * Reads a datagram packet from the socket.  Note that this method
   * will block until a packet is received from the network.  On return,
   * the passed in <code>DatagramPacket</code> is populated with the data 
   * received and all the other information about the packet.
   *
   * @param p A <code>DatagramPacket</code> for storing the data
   *
   * @exception IOException If an error occurs
   */
  public synchronized void receive(DatagramPacket p) throws IOException
  {
    SecurityManager s = System.getSecurityManager();
    if (s != null)
      s.checkAccept(p.getAddress().getHostAddress(), p.getPort());

    impl.receive(p);
  }

  /**
   * Sends the specified packet.  The host and port to which the packet
   * are to be sent should be set inside the packet.
   *
   * @param p The packet of data to send
   *
   * @exception IOException If an error occurs
   */
  public void send(DatagramPacket p) throws IOException
  {
    if (!connected)
      {
        SecurityManager s = System.getSecurityManager();
        if (s != null)
          {
             InetAddress addr = p.getAddress();
             if (addr.isMulticastAddress())
               s.checkMulticast(addr);
             else
               s.checkConnect(addr.getHostAddress(), p.getPort());
          }
      }

    // FIXME: if this is a subclass of MulticastSocket, use getTTL for TTL val.
    impl.send(p);
  }

} // class DatagramSocket

