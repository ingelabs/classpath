/* Socket.java -- Client socket implementation
   Copyright (C) 1998,2000 Free Software Foundation, Inc.

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

import java.io.*;

/**
  * This class models a client site socket.  A socket is a TCP/IP endpoint
  * for network communications conceptually similar to a file handle.
  * <p>
  * This class does not actually do any work.  Instead, it redirects all of
  * its calls to a socket implementation object which implements the
  * <code>SocketImpl</code> interface.  The implementation class is 
  * instantiated by factory class that implements the 
  * <code>SocketImplFactory interface</code>.  A default
  * factory is provided, however the factory may be set by a call to
  * the <code>setSocketImplFactory</code> method.  Note that this may only be 
  * done once per virtual machine.  If a subsequent attempt is made to set the
  * factory, a <code>SocketException</code> will be thrown.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  * @author Per Bothner <bothner@cygnus.com>
  */
public class Socket
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the user SocketImplFactory for this class.  If this variable is
  * null, a default factory is used.
  */
private static SocketImplFactory factory;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The implementation object to which calls are redirected
  */
SocketImpl impl;

/*************************************************************************/

/*
 * Class Methods
 */

/**
  * Sets the <code>SocketImplFactory</code>.  This may be done only once per 
  * virtual machine.  Subsequent attempts will generate a 
  * <code>SocketException</code>.  Note that a <code>SecurityManager</code>
  * check is made prior to setting the factory.  If 
  * insufficient privileges exist to set the factory, then an 
  * <code>IOException</code> will be thrown.
  *
  * @exception SecurityException If the <code>SecurityManager</code> does
  * not allow this operation.
  * @exception SocketException If the SocketImplFactory is already defined
  * @exception IOException If any other error occurs
  */
public static synchronized void
setSocketImplFactory(SocketImplFactory factory) throws IOException
{
  // See if already set
  if (Socket.factory != null)
    throw new SocketException("SocketImplFactory already defined");

  // Check permissions
  SecurityManager sm = System.getSecurityManager();
  if (sm != null)
    sm.checkSetFactory();

  Socket.factory = factory;
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Socket</code> object without 
  * connecting to a remote host.  This useful for subclasses of socket that 
  * might want this behavior.
  */
protected
Socket()
{
  if (factory != null)
    impl = factory.createSocketImpl();
  else
    impl = new PlainSocketImpl();
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Socket</code> object without connecting 
  * to a remote host.  This is useful for subclasses of socket that might want 
  * this behavior.  
  * <p>
  * Additionally, this socket will be created using the supplied implementation
  * class instead the default class or one returned by a factory.  This
  * value can be <code>null</code>, but if it is, all instance methods in 
  * <code>Socket</code> should be overridden because most of them rely on this 
  * value being populated.
  *
  * @param impl The <code>SocketImpl</code> to use for this <code>Socket</code>
  *
  * @exception SocketException If an error occurs
  */
protected
Socket(SocketImpl impl) throws SocketException
{
  this.impl = impl;
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Socket</code> and connects to the 
  * address and port number specified as arguments.
  *
  * @param add The address to connect to
  * @param port The port number to connect to
  *
  * @exception IOException If an error occurs
  */
public
Socket(InetAddress addr, int port) throws IOException
{
  this(addr, port, null, 0, true);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Socket</code> and connects to the 
  * address and port number specified as arguments.  If the stream param is 
  * <code>true</code>, a stream socket will be created, otherwise a datagram 
  * socket is created.
  *
  * @param add The address to connect to
  * @param port The port number to connect to
  * @param stream <code>true</code> to create a stream socket, 
  * <code>false</code> to create a datagram socket.
  *
  * @exception IOException If an error occurs
  *
  * @deprecated Use the <code>DatagramSocket</code> class to create
  * datagram oriented sockets.
  */
public
Socket(InetAddress addr, int port, boolean stream) throws IOException
{
  this(addr, port, null, 0, stream);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Socket</code> and connects to the 
  * address and port number specified as arguments, plus binds to the 
  * specified local address and port.
  *
  * @param raddr The remote address to connect to
  * @param rport The remote port to connect to
  * @param laddr The local address to connect to
  * @param lport The local port to connect to
  *
  * @exception IOException If an error occurs
  */
public
Socket(InetAddress raddr, int rport, InetAddress laddr, 
       int lport) throws IOException
{
  this(raddr, rport, laddr, lport, true);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Socket</code> and connects to the 
  * hostname and port specified as arguments.
  *
  * @param hostname The name of the host to connect to
  * @param port The port number to connect to
  *
  * @exception UnknownHostException If the hostname cannot be resolved to a
  * network address.
  * @exception IOException If an error occurs
  */
public
Socket(String hostname, int port) throws IOException
{
  this(InetAddress.getByName(hostname), port, null, 0, true);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Socket</code> and connects to the 
  * hostname and port specified as arguments.  If the stream argument is set 
  * to <code>true</code>, then a stream socket is created.  If it is 
  * <code>false</code>, a datagram socket is created.
  *
  * @param hostname The name of the host to connect to
  * @param port The port to connect to
  * @param stream <code>true</code> for a stream socket, <code>false</code>
  * for a datagram socket
  *
  * @exception IOException If an error occurs
  *
  * @deprecated Use the <code>DatagramSocket</code> class to create
  * datagram oriented sockets.
  */
public
Socket(String hostname, int port, boolean stream) throws IOException
{
  this(InetAddress.getByName(hostname), port, null, 0, stream);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>Socket</code> that connects to the 
  * named host on the specified port and binds to the specified local address 
  * and port.
  *
  * @param host The name of the remote host to connect to.
  * @param port The remote port to connect to.
  * @param loadAddr The local address to bind to.
  * @param localPort The local port to bind to.
  *
  * @exception SecurityException If the <code>SecurityManager</code>
  * exists and does not allow a connection to the specified host/port or
  * binding to the specified local host/port.
  * @exception IOException If a connection error occurs.
  */
public
Socket(String host, int port, InetAddress localAddr, int localPort)
       throws IOException
{
  this(InetAddress.getByName(host), port, localAddr, localPort, true);
}

/*************************************************************************/

/**
  * This constructor is where the real work takes place.  Connect to the
  * specified address and port.  Use default local values if not specified,
  * otherwise use the local host and port passed in.  Create as stream or
  * datagram based on "stream" argument.
  * <p>
  *
  * @param raddr The remote address to connect to
  * @param rport The remote port to connect to
  * @param laddr The local address to connect to
  * @param lport The local port to connect to
  * @param stream true for a stream socket, false for a datagram socket
  *
  * @exception IOException If an error occurs
  */
private
Socket(InetAddress raddr, int rport, InetAddress laddr, int lport,
       boolean stream) throws IOException
{
  this();
  if (impl == null)
    throw new IOException("Cannot initialize Socket implementation");

  SecurityManager sm = System.getSecurityManager();
  if (sm != null)
    sm.checkConnect(raddr.getHostName(), rport);

  impl.create(stream);

  if (laddr != null)
    impl.bind(laddr, lport);

  if (raddr != null)
    impl.connect(raddr, rport);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Closes the socket.
  *
  * @exception IOException If an error occurs
  */
public synchronized void
close() throws IOException
{
  if (impl != null)
    impl.close();
}

/*************************************************************************/

/**
  * Returns the address of the remote end of the socket.  If this socket
  * is not connected, then <code>null</code> is returned.
  *
  * @return The remote address this socket is connected to
  */
public InetAddress
getInetAddress()
{
  if (impl != null)
    return(impl.getInetAddress());

  return(null);
}

/*************************************************************************/

/**
  * Returns the port number of the remote end of the socket connection.  If
  * this socket is not connected, then -1 is returned.
  *
  * @return The remote port this socket is connected to
  */
public int
getPort()
{
  if (impl != null)
    return(impl.getPort());

  return(-1);
}

/*************************************************************************/

/**
  * Returns the local address to which this socket is bound.  If this socket
  * is not connected, then <code>null</code> is returned.
  *
  * @return The local address
  */
public InetAddress
getLocalAddress()
{
  if (impl == null)
    return(null);

  InetAddress addr = null;
  try
    {
      addr = (InetAddress)impl.getOption(SocketOptions.SO_BINDADDR);
    }
  catch(SocketException e)
    {
      return(null);
    }

  // FIXME: According to libgcj, checkConnect() is supposed to be called
  // before performing this operation.  Problems: 1) We don't have the
  // addr until after we do it, so we do a post check.  2). The docs I
  // see don't require this in the Socket case, only DatagramSocket, but
  // we'll assume they mean both.
  SecurityManager sm = System.getSecurityManager();
  if (sm != null)
    sm.checkConnect(addr.getHostName(), getLocalPort());

  return(addr);
}

/*************************************************************************/

/**
  * Returns the local port number to which this socket is bound.  If this
  * socket is not connected, then -1 is returned.
  *
  * @return The local port
  */
public int
getLocalPort()
{
  if (impl != null)
    return(impl.getLocalPort());

  return(-1);
}

/*************************************************************************/

/**
  * Returns an InputStream for reading from this socket.
  *
  * @return The InputStream object
  *
  * @exception IOException If an error occurs
  */
public synchronized InputStream
getInputStream() throws IOException
{
  if (impl != null)
    return(impl.getInputStream());

  throw new IOException("Not connected");
}

/*************************************************************************/

/**
  * Returns an OutputStream for writing to this socket.
  *
  * @return The OutputStream object
  *
  * @exception IOException If an error occurs
  */
public synchronized OutputStream
getOutputStream() throws IOException
{
  if (impl != null)
    return(impl.getOutputStream());

  throw new IOException("Not connected");
}

/*************************************************************************/

/**
  * Returns the value of the SO_LINGER option on the socket.  If the 
  * SO_LINGER option is set on a socket and there is still data waiting to
  * be sent when the socket is closed, then the close operation will block
  * until either that data is delivered or until the timeout period
  * expires.  This method either returns the timeouts (in hundredths of
  * of a second (****????****)) if SO_LINGER is set, or -1 if SO_LINGER is
  * not set.
  *
  * @return The SO_LINGER timeout in hundreths of a second or -1 
  * if SO_LINGER not set
  *
  * @exception SocketException If an error occurs
  */
public synchronized int
getSoLinger() throws SocketException
{
  if (impl == null)
    throw new SocketException("Not connected");

  Object obj = impl.getOption(SocketOptions.SO_LINGER);

  if (obj instanceof Boolean)
    return(-1); // Boolean is only returned in unset case

  if (obj instanceof Integer)
    return(((Integer)obj).intValue());
  else
    throw new SocketException("Internal Error");
}

/*************************************************************************/

/**
  * Sets the value of the SO_LINGER option on the socket.  If the 
  * SO_LINGER option is set on a socket and there is still data waiting to
  * be sent when the socket is closed, then the close operation will block
  * until either that data is delivered or until the timeout period
  * expires.  The linger interval is specified in hundreths of a second
  * *********???????********
  *
  * @param state true to enable SO_LINGER, false to disable
  * @param timeout The SO_LINGER timeout in hundreths of a second or -1 if 
  * SO_LINGER not set.
  *
  * @exception SocketException If an error occurs
  */
public synchronized void
setSoLinger(boolean state, int timeout) throws SocketException
{
  if (impl == null)
    throw new SocketException("No socket created");

  if (state == true)
    {
      if (timeout < 0)
        throw new IllegalArgumentException("SO_LINGER value must be > 0");

      if (timeout > 65535)
        timeout = 65535;

      impl.setOption(SocketOptions.SO_LINGER, new Integer(timeout));
    }
  else
    {
      impl.setOption(SocketOptions.SO_LINGER, new Boolean(false));
    }

  return;
}

/*************************************************************************/

/**
  * Returns the value of the SO_TIMEOUT option on the socket.  If this value
  * is set, and an read/write is performed that does not complete within
  * the timeout period, a short count is returned (or an EWOULDBLOCK signal
  * would be sent in Unix if no data had been read).  A value of 0 for
  * this option implies that there is no timeout (ie, operations will 
  * block forever).  On systems that have separate read and write timeout
  * values, this method returns the read timeout.  This
  * value is in thousandths of a second. (*****Is it *******); 
  *
  * @return The length of the timeout in thousandth's of a second or 0 
  * if not set
  *
  * @exception SocketException If an error occurs
  */
public synchronized int
getSoTimeout() throws SocketException
{
  if (impl == null)
    throw new SocketException("Not connected");

  Object obj = impl.getOption(SocketOptions.SO_TIMEOUT);

  if (obj instanceof Integer)
    return(((Integer)obj).intValue());
  else
    throw new SocketException("Internal Error");
}

/*************************************************************************/

/**
  * Sets the value of the SO_TIMEOUT option on the socket.  If this value
  * is set, and an read/write is performed that does not complete within
  * the timeout period, a short count is returned (or an EWOULDBLOCK signal
  * would be sent in Unix if no data had been read).  A value of 0 for
  * this option implies that there is no timeout (ie, operations will 
  * block forever).  On systems that have separate read and write timeout
  * values, this method returns the read timeout.  This
  * value is in thousandths of a second (****????*****)
  *
  * @param timeout The length of the timeout in thousandth's of a second or 
  * 0 if not set
  *
  * @exception SocketException If an error occurs
  */
public synchronized void
setSoTimeout(int timeout) throws SocketException
{
  if (impl == null)
    throw new SocketException("Not connected");

  if (timeout < 0)
    throw new IllegalArgumentException("SO_TIMEOUT value must be > 0");

  impl.setOption(SocketOptions.SO_TIMEOUT, new Integer(timeout));

  return;
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
  *
  * @since Java 1.2
  */
public synchronized int
getSendBufferSize() throws SocketException
{
  if (impl == null)
    throw new SocketException("Not connected");

  Object obj = impl.getOption(SocketOptions.SO_SNDBUF);

  if (obj instanceof Integer)
    return(((Integer)obj).intValue());
  else
    throw new SocketException("Internal Error: Unexpected type");
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
  *
  * @since Java 1.2
  */
public synchronized void
setSendBufferSize(int size) throws SocketException
{
  if (impl == null)
    throw new SocketException("Not connected");

  if (size <= 0)
    throw new IllegalArgumentException("SO_SNDBUF value must be > 0");
  
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
  *
  * @since Java 1.2
  */
public synchronized int
getReceiveBufferSize() throws SocketException
{
  if (impl == null)
    throw new SocketException("Not connected");

  Object obj = impl.getOption(SocketOptions.SO_RCVBUF);

  if (obj instanceof Integer)
    return(((Integer)obj).intValue());
  else
    throw new SocketException("Internal Error: Unexpected type");
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
  *
  * @since Java 1.2
  */
public synchronized void
setReceiveBufferSize(int size) throws SocketException
{
  if (impl == null)
    throw new SocketException("Not connected");

  if (size <= 0)
    throw new IllegalArgumentException("SO_RCVBUF value must be > 0");
  
  impl.setOption(SocketOptions.SO_RCVBUF, new Integer(size));
}

/*************************************************************************/

/**
  * Tests whether or not the TCP_NODELAY option is set on the socket. 
  * Returns true if enabled, false if disabled.  *** Need good explanation
  * of this parameter.
  *
  * @return Whether or not TCP_NODELAY is set
  * 
  * @exception SocketException If an error occurs
  */
public synchronized boolean
getTcpNoDelay() throws SocketException
{
  if (impl == null)
    throw new SocketException("Not connected");

  Object obj = impl.getOption(SocketOptions.TCP_NODELAY);

  if (obj instanceof Boolean)
    return(((Boolean)obj).booleanValue());
  else
    throw new SocketException("Internal Error");
}

/*************************************************************************/

/**
  * Sets the TCP_NODELAY option is set on the socket. 
  * Returns true if enabled, false if disabled.  *** Need good explanation
  * of this parameter.
  *
  * @param state true to enable, false to disable
  * 
  * @exception SocketException If an error occurs
  */
public synchronized void
setTcpNoDelay(boolean state) throws SocketException
{
  if (impl == null)
    throw new SocketException("Not connected");

  impl.setOption(SocketOptions.TCP_NODELAY, new Boolean(state));

  return;
}

/*************************************************************************/

/**
  * Converts this <code>Socket</code> to a <code>String</code>.
  *
  * @return The <code>String</code> representation of this <code>Socket</code>
  */
public String
toString()
{
  return("Socket " + impl);
}

} // class Socket

