/*************************************************************************
/* InputStream.java -- Base class for input
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
/* You should have received a copy of the GNU Library General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.io;

/**
  * This abstract class forms the base of the hierarchy of classes that read
  * input as a stream of bytes.  It provides a common set of methods for
  * reading bytes from streams.  Subclasses implement and extend these
  * methods to read bytes from a particular input source such as a file
  * or network connection.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class InputStream
{

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Default, no-arg, public constructor
  */
public
InputStream()
{
  return;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method marks a position in the input to which the stream can be
  * "reset" by calling the @code{reset()} method.  The parameter
  * @code{readlimit} is the number of bytes that can be read from the 
  * stream after setting the mark before the mark becomes invalid.  For
  * example, if @code{mark()} is called with a read limit of 10, then when
  * 11 bytes of data are read from the stream before the @code{reset()} 
  * method is called, then the mark is invalid and the stream object
  * instance is not required to remember the mark.
  * 
  * This method does nothing in this class, but subclasses may override it
  * to provide mark/reset functionality.
  *
  * @param readlimit The number of bytes that can be read before the mark becomes invalid
  */
public void
mark(int readlimit)
{
  return;
}

/*************************************************************************/

/**
  * This method returns a boolean that indicates whether the mark/reset
  * methods are supported in this class.  Those methods can be used to
  * remember a specific point in the stream and reset the stream to that
  * point.
  *
  * This method always returns @code{false} in this class, but subclasses
  * can override this method to return @code{true} if they support 
  * mark/reset functionality.
  *
  * @return @code{true} if mark/reset functionality is supported, @code{false} otherwise
  *
  */
public boolean
markSupported()
{
  return(false);
}

/*************************************************************************/

/**
  * This method resets a stream to the point where the @code{mark()} method
  * was called.  Any bytes that were read after the mark point was set will
  * be re-read during subsequent reads.
  *
  * This method always throws an IOException in this class, but subclasses
  * can override this method if they provide mark/reset functionality.
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
  * This method always returns 0 in this class
  *
  * @return The number of bytes that can be read before blocking could occur
  *
  * @return IOException If an error occurs
  */
public int
available() throws IOException
{
  return(0);
}

/*************************************************************************/

/**
  * This method skips the specified number of bytes in the stream.  It
  * returns the actual number of bytes skipped, which may be less than the
  * requested amount.
  *
  * This method reads and discards bytes into a 256 byte array until the
  * specified number of bytes were skipped or until either the end of stream
  * is reached or a read attempt returns a short count.  Subclasses can
  * override this metho to provide a more efficient implementation where
  * one exists.
  *
  * @param num_bytes The requested number of bytes to skip
  *
  * @return The actual number of bytes skipped.
  *
  * @exception IOException If an error occurs
  */
public long
skip(long num_bytes) throws IOException
{
  int buf_size = 256;
  byte[] buf = new byte[buf_size];

  if (num_bytes <= 0)
    return(0);

  long loop_count = num_bytes / buf_size;
  int extra_bytes = (int)(num_bytes % buf_size);
  long bytes_read = 0;
  long total_read = 0;

  for (long i = 0; i < loop_count; i++)
    {
      bytes_read = read(buf, 0, buf.length);

      if (bytes_read == -1)
        return(total_read);

      if (bytes_read != buf_size)
        return(total_read + bytes_read);

      total_read += bytes_read;
    }

  bytes_read = read(buf, 0, extra_bytes);

  if (bytes_read == -1)
    return(total_read);

  return(total_read + bytes_read);
}

/*************************************************************************/

/**
  * This method reads an unsigned byte from the input stream and returns it
  * as an int in the range of 0-255.  This method also will return -1 if
  * the end of the stream has been reached.
  *
  * This method will block until the byte can be read.
  *
  * @return The byte read or -1 if end of stream
  *
  * @exception IOException If an error occurs
  */
public abstract int
read() throws IOException;

/*************************************************************************/

/**
  * This method reads bytes from a stream and stores them into a caller
  * supplied buffer.  This method attempts to completely fill the buffer,
  * but can return before doing so.  The actual number of bytes read is
  * returned as an int.  A -1 is returned to indicate the end of the stream.
  *
  * This method will block until some data can be read.
  *
  * This method operates by calling an overloaded read method like so:
  * @code{read(buf, 0, buf.length)}
  *
  * @param buf The buffer into which the bytes read will be stored.
  *
  * @return The number of bytes read or -1 if end of stream.
  *
  * @exception IOException If an error occurs.
  */
public int
read(byte[] buf) throws IOException
{
  return(read(buf, 0, buf.length));
}

/*************************************************************************/

/**
  * This method read bytes from a stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index @code{offset} into
  * the buffer and attempts to read @code{len} bytes.  This method can
  * return before reading the number of bytes requested.  The actual number
  * of bytes read is returned as an int.  A -1 is returned to indicate the
  * end of the stream.
  * 
  * This method will block until some data can be read.
  *
  * This method operates by calling the single byte @code{read()} method
  * in a loop until the desired number of bytes are read.  The read loop
  * stops short if the end of the stream is encountered or if an IOException
  * is encountered on any read operation except the first.  If the first
  * attempt to read a bytes fails, the IOException is allowed to propagate
  * upward.  And subsequent IOException is caught and treated identically
  * to an end of stream condition.  Subclasses can (and should if possible)
  * override this method to provide a more efficient implementation.
  *
  * @param buf The array into which the bytes read should be stored
  * @param offset The offset into the array to start storing bytes
  * @param len The requested number of bytes to read
  *
  * @return The actual number of bytes read, or -1 if end of stream.
  *
  * @exception IOException If an error occurs.
  */
public int
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

  int total_read = 1;

  // Read the rest of the bytes
  try
    {
      for (int i = 1; i < len; i++)
        {
          byte_read = read();
          if (byte_read == -1)
            return(total_read);

          buf[offset + i] = (byte)byte_read;
          
          ++total_read;  
        }
    }
  catch (IOException e)
    {
      return(total_read);
    }

  return(total_read);
}

/*************************************************************************/

/**
  * This method closes the stream.  Any futher attempts to read from the
  * stream may generate an @code{IOException}
  *
  * This method does nothing in this class, but subclasses may override
  * this method in order to provide additional functionality.
  *
  * @exception IOException If an error occurs, which can only happen in a subclass
  */
public void
close() throws IOException
{
  return;
}

} // class InputStream

