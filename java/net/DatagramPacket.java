/*************************************************************************
/* DatagramPacket.java -- Class to model a packet to be sent via UDP
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

/**
  * This class models a packet of data that is to be sent across the network
  * using a connectionless protocol such as UDP.  It contains the data
  * to be send, as well as the destination address and port.  Note that
  * datagram packets can arrive in any order and are not guaranteed to be
  * delivered at all.
  * <p>
  * This class can also be used for receiving data from the network.
  * <p>
  * Note that for all method below where the buffer length passed by the
  * caller cannot exceed the actually length of the byte array passed as
  * the buffer, if this condition is not true, then the method silently
  * reduces the length value to maximum allowable value.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public final class DatagramPacket
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The data buffer to send
  */
private byte[] buf;

/**
  * The length of the data buffer to send
  */
private int len;

/**
  * This is the offset into the buffer to start sending from or receiving to.
  */
private int offset;

/**
  * The address to which the packet should be sent or from which it
  * was received
  */
private InetAddress addr;

/**
  * The port to which the packet should be sent or from which it was
  * was received.
  */
private int port;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Creates a DatagramPacket for receiving packets from the network
  *
  * @param buf A buffer for storing the returned packet data
  * @param len The length of the buffer (must be <= buf.length)
  */
public
DatagramPacket(byte[] buf, int len)
{
  this(buf, 0, len, null, 0);
}

/*************************************************************************/

/**
  * This method initializes a new instance of <code>DatagramPacket</code>
  * which has the specified buffer, offset, and length.
  *
  * @param buf The buffer for holding the incoming datagram.
  * @param offset The offset into the buffer to start writing.
  * @param len The maximum number of bytes to read.
  */
public
DatagramPacket(byte[] buf, int offset, int len)
{
  this(buf, offset, len, null, 0);
}

/*************************************************************************/

/**
  * Creates a DatagramPacket for transmitting packets across the network.
  *
  * @param buf A buffer containing the data to send
  * @param len The length of the buffer (must be <= buf.length)
  * @param addr The address to send to
  * @param port The port to send to
  */
public
DatagramPacket(byte[] buf, int len, InetAddress addr, int port)
{
  this(buf, 0, len, addr, port);
}

/*************************************************************************/

/**
  * Creates a DatagramPacket for transmitting packets across the network.
  *
  * @param buf A buffer containing the data to send
  * @param offset The offset into the buffer to start writing from.
  * @param len The length of the buffer (must be <= buf.length)
  * @param addr The address to send to
  * @param port The port to send to
  */
public
DatagramPacket(byte[] buf, int offset, int len, InetAddress addr, int port)
{
  this.buf = buf;
  this.offset = offset;
  this.len = len;
  this.addr = addr;
  this.port = port;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the address that this packet is being sent to or, if it was used
  * to receive a packet, the address that is was received from.  If the
  * constructor that doesn not take an address was used to create this object
  * and no packet was actually read into this object, then this method
  * returns null
  *
  * @return The address for this packet
  */
public InetAddress
getAddress()
{
  return(addr);
}

/*************************************************************************/

/**
  * This sets the address to which the data packet will be transmitted.
  *
  * @param addr The destination address
  */
public synchronized void
setAddress(InetAddress addr)
{
  this.addr = addr;
}

/*************************************************************************/

/**
  * Returns the port number this packet is being sent to or, if it was used
  * to receive a packet, the port that it was received from. If the
  * constructor that doesn not take an address was used to create this object
  * and no packet was actually read into this object, then this method
  * will return 0.
  *
  * @return The port number for this packet
  */
public int
getPort()
{
  return(port);
}

/*************************************************************************/

/**
  * This sets the port to which the data packet will be transmitted.
  *
  * @param port The destination port
  */
public synchronized void
setPort(int port)
{
  this.port = port;
}

/*************************************************************************/

/**
  * Returns the data buffer for this packet
  *
  * @return This packet's data buffer
  */
public byte[]
getData()
{
  return(buf);
}

/*************************************************************************/

/**
  * Sets the data buffer for this packet.
  *
  * @return The new buffer for this packet
  */
public synchronized void
setData(byte[] buf)
{
  this.buf = buf;
  offset = 0;

  if (buf == null)
    {
      len = 0;
    }
  else 
    {
      len = buf.length;
    }
}

/*************************************************************************/

/**
  * This method sets the data buffer for the packet.
  *
  * @param buf The byte array containing the data for this packet.
  * @param offset The offset into the buffer to start reading data from.
  * @param len The number of bytes of data in the buffer.
  */
public synchronized void
setData(byte[] buf, int offset, int len)
{
  this.buf = buf;
  this.offset = offset;
  this.len = len;
}

/*************************************************************************/

/**
  * This method returns the current offset value into the data buffer
  * where data will be sent from.
  *
  * @return The buffer offset.
  */
public int
getOffset()
{
  return(offset);
}

/*************************************************************************/

/**
  * Returns the length of the data in the buffer
  *
  * @return The length of the data
  */
public int
getLength()
{
  return(len);
}

/*************************************************************************/

/**
  * Sets the length of the data in the buffer. 
  *
  * @param len The new length.  (Where len <= buf.length)
  */
public synchronized void
setLength(int len)
{
  this.len = len;
}

} // class DatagramPacket

