/* ServerSocket.java -- Class for implementing server side sockets
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

import java.io.IOException;

/**
  * This class models server side sockets.  The basic model is that the
  * server socket is created and bound to some well known port.  It then
  * listens for and accepts connections.  At that point the client and
  * server sockets are ready to communicate with one another utilizing
  * whatever application layer protocol they desire.
  * <p>
  * As with the <code>Socket</code> class, most instance methods of this class 
  * simply redirect their calls to an implementation class.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  * @author Per Bothner <bothner@cygnus.com>
  */
public class ServerSocket
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the user defined SocketImplFactory, if one is supplied
  */
private static SocketImplFactory factory;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the SocketImp object to which most instance methods in this
  * class are redirected
  */
private SocketImpl impl;

/*************************************************************************/

/*
 * Class methods
 */

/**
  * Sets the <code>SocketImplFactory</code> for all 
  * <code>ServerSocket</code>'s.  This may only be done
  * once per virtual machine.  Subsequent attempts will generate an
  * exception.  Note that a <code>SecurityManager</code> check is made prior to
  * setting the factory.  If insufficient privileges exist to set the factory,
  * an exception will be thrown
  *
  * @exception SecurityException If this operation is not allowed by the
  * <code>SecurityManager</code>.
  * @exception SocketException If the factory object is already defined
  * @exception IOException If any other error occurs
  */
public static synchronized void
setSocketFactory(SocketImplFactory factory) throws IOException
{
  // See if already set
  if (ServerSocket.factory != null)
    throw new SocketException("SocketImplFactory already defined");

  // Check permission to perform this operation
  SecurityManager sm = System.getSecurityManager();
  if (sm != null)
    sm.checkSetFactory();

  ServerSocket.factory = factory;
}
     
/*************************************************************************/

/**
  * This constructor simply load the implementation and returns.  
  */
ServerSocket()
{
  if (factory != null)
    impl = factory.createSocketImpl();
  else
    impl = new PlainSocketImpl();
}

/*************************************************************************/

/**
  * Creates a server socket and binds it to the specified port.  If the
  * port number is 0, a random free port will be chosen.  The pending
  * connection queue on this socket will be set to 50.
  *
  * @param port The port number to bind to
  * 
  * @exception IOException If an error occurs
  */
public 
ServerSocket(int port) throws IOException
{
  this(port, 50, null);
}

/*************************************************************************/

/**
  * Creates a server socket and binds it to the specified port.  If the
  * port number is 0, a random free port will be chosen.  The pending
  * connection queue on this socket will be set to the value passed as
  * arg2.
  *
  * @param port The port number to bind to
  * @param queuelen The length of the pending connection queue
  *
  * @exception IOException If an error occurs
  */
public
ServerSocket(int port, int queuelen) throws IOException
{
  this(port, queuelen, null);
}

/*************************************************************************/

/**
  * Creates a server socket and binds it to the specified port.  If the
  * port number is 0, a random free port will be chosen.  The pending
  * connection queue on this socket will be set to the value passed as
  * arg2.  The third argument specifies a particular local address to
  * bind to.
  *
  * @param port The port number to bind to
  * @param queuelen The length of the pending connection queue
  * @param addr The address to bind to
  *
  * @exception IOException If an error occurs
  */
public
ServerSocket(int port, int queuelen, InetAddress addr) throws IOException
{
  this();
  if (impl == null)
    throw new IOException("Cannot initialize Socket implementation");

  SecurityManager sm = System.getSecurityManager();
  if (sm != null)
    sm.checkListen(port);

  if (addr == null)
    addr = InetAddress.getInaddrAny();

  impl.create(true);
  impl.bind(addr, port);
  impl.listen(queuelen);
}

/*************************************************************************/

/**
  * This protected method is used to help subclasses override 
  * <code>ServerSocket.accept()</code>.  The passed in socket will be connected 
  * when this method returns.
  *
  * @param socket The socket that is used for the accepted connection
  *
  * @exception IOException If an error occurs
  */
protected final synchronized void
implAccept(Socket socket) throws IOException
{
  impl.accept(socket.impl);
}

/*************************************************************************/

/**
  * Accepts a new connection and returns a connected <code>Socket</code> 
  * instance representing that connection.  This method will block until a 
  * connection is available.
  *
  * @exception IOException If an error occurs
  */
public synchronized Socket
accept() throws IOException
{
  Socket socket = new Socket();
  impl.accept(socket.impl);

  return(socket);
}

/*************************************************************************/

/**
  * Closes this socket and stops listening for connections
  *
  * @exception IOException If an error occurs
  */
public synchronized void
close() throws IOException
{
  impl.close();
}

/*************************************************************************/

/**
  * This method returns the local address to which this socket is bound
  *
  * @return The socket's local address
  */
public InetAddress
getInetAddress()
{
  return(impl.getInetAddress());
}

/*************************************************************************/

/**
  * This method returns the local port number to which this socket is bound
  *
  * @return The socket's port number
  */
public int
getLocalPort()
{
  return(impl.getLocalPort());
}

/*************************************************************************/

/**
  * Retrieves the current value of the SO_TIMEOUT setting.  A value of 0
  * implies that SO_TIMEOUT is disabled (ie, operations never time out).
  * This is the number of milliseconds a socket operation can block before
  * an InterruptedIOException is thrown.
  *
  * @return The value of SO_TIMEOUT
  *
  * @exception IOException If an error occurs
  */
public synchronized int
getSoTimeout() throws IOException
{
  Object obj = impl.getOption(SocketOptions.SO_TIMEOUT);

  if (!(obj instanceof Integer))
    throw new IOException("Internal Error");

  return(((Integer)obj).intValue());
}

/*************************************************************************/

/**
  * Sets the value of SO_TIMEOUT.  A value of 0 implies that SO_TIMEOUT is
  * disabled (ie, operations never time out).  This is the number of 
  * milliseconds a socket operation can block before an
  * InterruptedIOException is thrown.
  *
  * @param timeout The new SO_TIMEOUT value
  *
  * @exception IOException If an error occurs
  */
public synchronized void
setSoTimeout(int timeout) throws IOException
{
  if (timeout < 0)
    throw new IllegalArgumentException("SO_TIMEOUT value must be >= 0");

  impl.setOption(SocketOptions.SO_TIMEOUT, new Integer(timeout));
}

/*************************************************************************/

/**
  * Returns the value of this socket as a <code>String</code>. 
  *
  * @return This socket represented as a <code>String</code>.
  */
public String
toString()
{
  return("Server Socket " + impl.toString());
}

} // class ServerSocket

