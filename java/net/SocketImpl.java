/*************************************************************************
/* SocketImpl.java -- Abstract socket implementation class
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

import java.io.*;

/**
  * This abstract class serves as the parent class for socket implementations.
  * The implementation class serves an intermediary to native routines that
  * perform system specific socket operations.
  * <p>
  * A default implementation is provided by the system, but this can be
  * changed via installing a SocketImplFactory (through a call to the
  * static method Socket.setSocketImplFactory).  A subclass of Socket can
  * also pass in a SocketImpl to the Socket(SocketImpl) constructor to
  * use an implementation different from the system default without installing
  * a factory.
  * <p>
  * Note that the SocketOptions interface is protected.  It contains the
  * declaration of the methods getOption and setOption (***???***) that are
  * used by Socket() for setting various options on the socket.  This is
  * of interest only to implementors.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class SocketImpl implements SocketOptions
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * The address of the remote end of the socket connection
  */
protected InetAddress address;

/**
  * The port number of the remote end of the socket connection
  */
protected int port;

/**
  * The port number the socket is bound to locally
  */
protected int localport;

/**
  * A FileDescriptor object representing this socket connection.  
  * ***** How do I create one of these? ********
  */
protected FileDescriptor fd;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * A do nothing default public construtor
  */
public
SocketImpl()
{
  ;
}

/*************************************************************************/

/**
  * Accepts a connection on this socket.
  *
  * @param impl The implementation object for the accepted connection.
  *
  * @exception IOException If an error occurs
  */
protected abstract synchronized void
accept(SocketImpl impl) throws IOException;

/*************************************************************************/

/**
  * Returns the number of bytes that the caller can read from this socket
  * without blocking.
  *
  * @return The number of readable bytes before blocking
  *
  * @exception IOException If an error occurs
  */
protected abstract synchronized int
available() throws IOException;

/*************************************************************************/

/**
  * Binds to the specified port on the specified addr.  Note that this addr
  * must represent a local IP address.  **** How bind to INADDR_ANY? ****
  *
  * @param addr The address to bind to
  * @param port The port number to bind to
  *
  * @exception IOException If an error occurs
  */
protected abstract synchronized void
bind(InetAddress addr, int port) throws IOException;

/*************************************************************************/

/**
  * Closes the socket.  This will cause any InputStream or OutputStream
  * objects for this Socket to be closed as well ******* Will it? *********
  * <p>
  * Note that if the SO_LINGER option is set on this socket, then the
  * operation could block.
  *
  * @exception IOException If an error occurs
  */
protected abstract synchronized void
close() throws IOException;

/*************************************************************************/

/**
  * Connects to the remote address and port specified as arguments.
  *
  * @param addr The remote address to connect to
  * @param port The remote port to connect to
  *
  * @exception IOException If an error occurs
  */
protected abstract synchronized void 
connect(InetAddress addr, int port) throws IOException;

/*************************************************************************/

/**
  * Connects to the remote hostname and port specified as arguments.
  *
  * @param hostname The remote hostname to connect to
  * @param port The remote port to connect to
  *
  * @exception IOException If an error occurs
  */
protected abstract synchronized void 
connect(String hostname, int port) throws IOException;

/*************************************************************************/

/**
  * Creates a new socket that is not bound to any local address/port and
  * is not connected to any remote address/port.  This will be created as
  * a stream socket if the stream parameter is true, or a datagram socket
  * if the stream parameter is false.
  *
  * @param stream true for a stream socket, false for a datagram socket
  */
protected abstract void 
create(boolean stream) throws IOException;

/*************************************************************************/

/**
  * Returns the FileDescriptor objects for this socket.
  *
  * @return A FileDescriptor for this socket.
  */
protected FileDescriptor
getFileDescriptor()
{
  return(fd);
}

/*************************************************************************/

/**
  * Returns the local port this socket is bound to
  *
  * @return The local port
  */
protected int
getLocalPort()
{
  return(localport);
}

/*************************************************************************/

/**
  * Returns the remote address this socket is connected to
  *
  * @return The remote address
  */
protected InetAddress
getInetAddress()
{
  return(address);
}

/*************************************************************************/

/**
  * Returns the remote port this socket is connected to
  *
  * @return The remote port
  */
protected int
getPort()
{
  return(port);
}

/*************************************************************************/

/**
  * Returns an InputStream object for reading from this socket
  *
  * @return An InputStream
  *
  * @exception IOException If an error occurs
  */
protected abstract synchronized InputStream
getInputStream() throws IOException;
  
/*************************************************************************/

/**
  * Returns an OutputStream object for writing to this socket
  * 
  * @return An OutputStream
  *
  * @exception IOException If an error occurs
  */
protected abstract synchronized OutputStream
getOutputStream() throws IOException;

/*************************************************************************/

/**
  * Starts listening for connections on a socket. The queuelen parameter
  * is how many pending connections will queue up waiting to be serviced
  * before being accept'ed.  If the queue of pending requests exceeds this
  * number, additional connections will be refused.
  *
  * @param queuelen The length of the pending connection queue
  * 
  * @exception IOException If an error occurs
  */
protected abstract synchronized void
listen(int queuelen) throws IOException;

/*************************************************************************/

/**
  * Returns a String representing the remote host and port of this
  * socket.
  */
public String
toString()
{
  StringBuffer sb = new StringBuffer("");

  if (address == null)
    sb.append("<null>:");
  else
    sb.append(address.getHostAddress() + ":");

  sb.append(port);

  return(sb.toString());
}

} // class SocketImpl

