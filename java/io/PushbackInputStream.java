/*************************************************************************
/* PushbackInputStream.java -- An input stream that can unread bytes
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

package java.io;

/**
  * This subclass of <code>FilterInputStream</code> provides the ability to 
  * unread data from a stream.  It maintains an internal buffer of unread
  * data that is supplied to the next read operation.  This is conceptually
  * similar to mark/reset functionality, except that in this case the 
  * position to reset the stream to does not need to be known in advance.
  *
  * The default pushback buffer size one byte, but this can be overridden
  * by the creator of the stream.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class PushbackInputStream extends FilterInputStream
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the default buffer size
  */
private static final int DEFAULT_BUFFER_SIZE = 1;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the buffer that is used to store the pushed back data
  */
private byte[] buf;

/**
  * This is the position in the buffer from which the next byte will be
  * read.  Bytes are stored in reverse order in the buffer, starting from
  * <code>buf[buf.length - 1]</code> to <code>buf[0]</code>.  Thus when 
  * <code>pos</code> is 0 the buffer is full and <code>buf.length</code> when 
  * it is empty
  */
private int pos;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a <code>PushbackInputStream</code> to read from the
  * specified subordinate <code>InputStream</code> with a default pushback buffer 
  * size of 1.
  *
  * @code in The subordinate stream to read from
  */
public
PushbackInputStream(InputStream in)
{
  this(in, DEFAULT_BUFFER_SIZE);
}

/*************************************************************************/

/**
  * This method initializes a <code>PushbackInputStream</code> to read from the
  * specified subordinate <code>InputStream</code> with the specified buffer
  * size
  *
  * @param in The subordinate <code>InputStream</code> to read from
  * @param bufsize The pushback buffer size to use
  */
public
PushbackInputStream(InputStream in, int bufsize)
{
  super(in);

  buf = new byte[bufsize];
  pos = bufsize;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns <code>false</code> to indicate that it does not support
  * mark/reset functionality.
  *
  * @return This method returns <code>false</code> to indicate that this class does not support mark/reset functionality
  *
  */
public boolean
markSupported()
{
  return(false);
}

/*************************************************************************/

/**
  * This method always throws an IOException in this class because
  * mark/reset functionality is not supported.
  *
  * @exception IOException Always thrown for this class
  */
public void
reset() throws IOException
{
  throw new IOException("Mark not supported in this class");
}

/*************************************************************************/

/**
  * This method returns the number of bytes that can be read from this
  * stream before a read can block.  A return of 0 indicates that blocking
  * might (or might not) occur on the very next read attempt.
  *
  * This method will return the number of bytes available from the
  * pushback buffer plus the number of bytes available from the 
  * underlying stream.
  *
  * @return The number of bytes that can be read before blocking could occur
  *
  * @return IOException If an error occurs
  */
public int
available() throws IOException
{
  return((buf.length - pos) + in.available());
}

/*************************************************************************/

/**
  * This method skips the specified number of bytes in the stream.  It
  * returns the actual number of bytes skipped, which may be less than the
  * requested amount.
  *
  * This method first discards bytes from the buffer, then calls the
  * <code>skip</code> method on the underlying <code>InputStream</code> to 
  * skip additional bytes if necessary.
  *
  * @param num_bytes The requested number of bytes to skip
  *
  * @return The actual number of bytes skipped.
  *
  * @exception IOException If an error occurs
  */
public synchronized long
skip(long num_bytes) throws IOException
{
  if (num_bytes <= 0)
    return(0);

  if ((buf.length - pos) >= num_bytes)
    {
      pos += num_bytes;
      return(num_bytes);
    }

  int bytes_discarded = buf.length - pos;
  pos = buf.length;

  long bytes_skipped = in.skip(num_bytes - bytes_discarded);

  return(bytes_discarded + bytes_skipped);
}

/*************************************************************************/

/**
  * This method reads an unsigned byte from the input stream and returns it
  * as an int in the range of 0-255.  This method also will return -1 if
  * the end of the stream has been reached.  The byte returned will be read
  * from the pushback buffer, unless the buffer is empty, in which case
  * the byte will be read from the underlying stream.
  *
  * This method will block until the byte can be read.
  *
  * @return The byte read or -1 if end of stream
  *
  * @exception IOException If an error occurs
  */
public synchronized int
read() throws IOException
{
  if (pos == buf.length)
    return(in.read());
 
  ++pos;
  return((buf[pos - 1] & 0xFF));
}

/*************************************************************************/

/**
  * This method read bytes from a stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index <code>offset</code> into
  * the buffer and attempts to read <code>len</code> bytes.  This method can
  * return before reading the number of bytes requested.  The actual number
  * of bytes read is returned as an int.  A -1 is returned to indicate the
  * end of the stream.
  * 
  * This method will block until some data can be read.
  *
  * This method first reads bytes from the pushback buffer in order to 
  * satisfy the read request.  If the pushback buffer cannot provide all
  * of the bytes requested, the remaining bytes are read from the 
  * underlying stream.
  *
  * @param buf The array into which the bytes read should be stored
  * @param offset The offset into the array to start storing bytes
  * @param len The requested number of bytes to read
  *
  * @return The actual number of bytes read, or -1 if end of stream.
  *
  * @exception IOException If an error occurs.
  */
public synchronized int
read(byte[] buf, int offset, int len) throws IOException
{
  if (len == 0)
    return(0);

  // Read the first byte here in order to allow IOException's to 
  // propagate up

  int byte_read = read();
  if (byte_read == -1)
    return(-1);
  buf[offset] = (byte)byte_read;

  if (len == 1)
    return(1);

  int total_read = 1;

  // Grab bytes from pushback buffer if available
  if (pos != this.buf.length)
    {
      int desired_bytes = 0;
      if ((this.buf.length - pos) >= (len - total_read))
        desired_bytes = len - total_read;
      else
        desired_bytes = this.buf.length - pos;

      System.arraycopy(this.buf, pos, buf, offset + total_read, desired_bytes);

      total_read += desired_bytes;
      pos += desired_bytes;
    }

  // Read from underlying stream if we still need bytes
  if (total_read != len)
    {
      int bytes_read = 0;
      try
        {
          bytes_read = in.read(buf, offset + total_read, len - total_read);
        }
      catch(IOException e)
        {
          return(total_read);
        }

      if (bytes_read == -1)
        return(total_read);

      total_read += bytes_read;
    }

  return(total_read);
}

/*************************************************************************/

/**
  * This method pushes a single byte of data into the pushback buffer.
  * The byte pushed back is the one that will be returned as the first byte
  * of the next read.
  *
  * If the pushback buffer is full, this method throws an exception.
  *
  * The argument to this method is an <code>int</code>.  Only the low eight bits
  * of this value are pushed back.
  *
  * @param b The byte to be pushed back, passed as an int
  *
  * @exception IOException If the pushback buffer is full.
  */
public synchronized void
unread(int b) throws IOException
{
  if (pos == 0)
    throw new IOException("Pushback buffer is full");

  --pos;
  buf[pos] = (byte)(b & 0xFF);
}

/*************************************************************************/

/**
  * This method pushes all of the bytes in the passed byte array into 
  * the pushback buffer.  These bytes are pushed in reverse order so that
  * the next byte read from the stream after this operation will be
  * <code>buf[0]</code> followed by <code>buf[1]</code>, etc.
  *
  * If the pushback buffer cannot hold all of the requested bytes, an
  * exception is thrown.
  *
  * @param buf The byte array to be pushed back
  *
  * @exception IOException If the pushback buffer is full
  */
public synchronized void
unread(byte[] buf) throws IOException
{
  unread(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * This method pushed back bytes from the passed in array into the pushback
  * buffer.  The bytes from <code>buf[offset]</code> to <cdoe>buf[offset + len]</code>
  * are pushed in reverse order so that the next byte read from the stream
  * after this operation will be <code>buf[offset]</code> followed by
  * <code>buf[offset + 1]</code>, etc.
  *
  * If the pushback buffer cannot hold all of the requested bytes, an
  * exception is thrown.
  *
  * @param buf The byte array to be pushed back
  * @param offset The index into the array where the bytes to be push start
  * @param len The number of bytes to be pushed.
  *
  * @exception IOException If the pushback buffer is full
  */
public synchronized void
unread(byte[] buf, int offset, int len) throws IOException
{
  if (pos < (len - 1))
    throw new IOException("Insufficient space in pushback buffer");

  for (int i = (offset + len) - 1; i >= offset; i--)
    {
      --pos;
      this.buf[pos] = buf[i];
    }
}

} // class PushbackInputStream

