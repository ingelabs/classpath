/* SocketOutputStream.java -- OutputStream for PlainSocketImpl
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

import java.io.OutputStream;
import java.io.IOException;

/**
  * This class is used internally by <code>PlainSocketImpl</code> to be the 
  * <code>OutputStream</code> subclass returned by its 
  * <code>getOutputStream method</code>.  It expects only to  be used in that 
  * context.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
class SocketOutputStream extends OutputStream
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The PlainSocketImpl object this stream is associated with
  */
private PlainSocketImpl impl;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Build an instance of this class from a PlainSocketImpl object
  */
protected
SocketOutputStream(PlainSocketImpl impl)
{
  this.impl = impl;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method closes the stream and the underlying socket connection.  This
  * action also effectively closes any other InputStream or OutputStream
  * object associated with the connection.
  *
  * @exception IOException If an error occurs
  */
public void
close() throws IOException
{
  impl.close();
}

/*************************************************************************/

/**
  * Hmmm, we don't seem to have a flush() method in Socket impl, so just
  * return for now, but this might need to be looked at later.
  *
  * @exception IOException Can't happen
  */
public void
flush() throws IOException
{
  return;
}

/*************************************************************************/

/**
  * Writes a byte (passed in as an int) to the given output stream
  * 
  * @param b The byte to write
  *
  * @exception IOException If an error occurs
  */
public void
write(int b) throws IOException
{
  byte buf[] = new byte[1];

  Integer i = new Integer(b);
  buf[0] = i.byteValue();

  write(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * Write an array of bytes to the output stream
  *
  * @param buf The array of bytes to write
  *
  * @exception IOException If an error occurs
  */
public void
write(byte[] buf) throws IOException
{
  write(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * Writes len number of bytes from the array buf to the stream starting
  * at offset bytes into the buffer.
  *
  * @param buf The buffer
  * @param offset Offset into the buffer to start writing from
  * @param len The number of bytes to write
  */
public void
write(byte[] buf, int offset, int len) throws IOException
{
  impl.write(buf, offset, len);
}

} // class SocketOutputStream

