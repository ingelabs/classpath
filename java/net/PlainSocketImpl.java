/*************************************************************************
/* PlainSocketImpl.java -- Default socket implementation
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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
  * Unless the application installs its own SocketImplFactory, this is the
  * default socket implemetation that will be used.  It simply uses a
  * combination of Java and native routines to implement standard BSD
  * style sockets of family AF_INET and types SOCK_STREAM and SOCK_DGRAM
  *
  * @version 0.1
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
class PlainSocketImpl extends SocketImpl
{

/*************************************************************************/

/*
 * Static Variables
 */

// Static initializer to load native library
static
{
  System.loadLibrary("javanet");
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the native file descriptor for this socket
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
PlainSocketImpl()
{
  ;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * Accepts a new connection on this socket and returns in in the 
  * passed in SocketImpl.
  *
  * @param impl The SocketImpl object to accept this connection.
  */
protected native synchronized void
accept(SocketImpl impl) throws IOException;

/*************************************************************************/

/**
  * Returns the number of bytes that the caller can read from this socket
  * without blocking. //*****Figure out if we can do something here
  *
  * @return The number of readable bytes before blocking
  *
  * @exception IOException If an error occurs
  */
protected int
available() throws IOException
{
  return(0);
}

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
protected native synchronized void
bind(InetAddress addr, int port) throws IOException;

/*************************************************************************/

/**
  * Closes the socket.  This will cause any InputStream or OutputStream
  * objects for this Socket to be closed as well.
  * <p>
  * Note that if the SO_LINGER option is set on this socket, then the
  * operation could block.
  *
  * @exception IOException If an error occurs
  */
protected native synchronized void
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
protected native synchronized void 
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
protected synchronized void 
connect(String hostname, int port) throws IOException
{
  InetAddress addr = InetAddress.getByName(hostname);
  connect(addr, port);
}

/*************************************************************************/

/**
  * Creates a new socket that is not bound to any local address/port and
  * is not connected to any remote address/port.  This will be created as
  * a stream socket if the stream parameter is true, or a datagram socket
  * if the stream parameter is false.
  *
  * @param stream true for a stream socket, false for a datagram socket
  */
protected native synchronized void 
create(boolean stream) throws IOException;

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
protected native synchronized void
listen(int queuelen) throws IOException;

/*************************************************************************/

/**
  * Internal method used by SocketInputStream for reading data from
  * the connection.  Reads up to len bytes of data into the buffer
  * buf starting at offset bytes into the buffer.
  *
  * @return The actual number of bytes read or -1 if end of stream.
  *
  * @exception IOException If an error occurs
  */
protected native synchronized int
read(byte[] buf, int offset, int len) throws IOException;

/*************************************************************************/

/**
  * Internal method used by SocketOuputStream for writing data to
  * the connection.  Writes up to len bytes of data from the buffer
  * buf starting at offset bytes into the buffer.
  *
  * @exception IOException If an error occurs
  */
protected native synchronized void
write(byte[] buf, int offset, int len) throws IOException;

/*************************************************************************/

/**
  * Sets the specified option on a socket to the passed in object.  For
  * options that take an integer argument, the passed in object is an
  * Integer.  The option_id parameter is one of the defined constants in
  * this interface.
  *
  * @param option_id The identifier of the option
  * @param val The value to set the option to
  *
  * @exception SocketException If an error occurs
  */
public native synchronized void
setOption(int option_id, Object val) throws SocketException;

/*************************************************************************/

/**
  * Returns the current setting of the specified option.  The Object returned
  * will be an Integer for options that have integer values.  The option_id
  * is one of the defined constants in this interface.
  *
  * @param option_id The option identifier
  *
  * @return The current value of the option
  *
  * @exception SocketException If an error occurs
  */
public native synchronized Object
getOption(int option_id) throws SocketException;

/*************************************************************************/

/**
  * Returns an InputStream object for reading from this socket.  This will
  * be an instance of SocketInputStream.
  *
  * @return An InputStream
  *
  * @exception IOException If an error occurs
  */
protected synchronized InputStream
getInputStream() throws IOException
{
  return(new SocketInputStream(this));
}
  
/*************************************************************************/

/**
  * Returns an OutputStream object for writing to this socket.  This will
  * be an instance of SocketOutputStream.
  * 
  * @return An OutputStream
  *
  * @exception IOException If an error occurs
  */
protected synchronized OutputStream
getOutputStream() throws IOException
{
  return(new SocketOutputStream(this));
}

} // class PlainSocketImpl

