/*************************************************************************
/* Reader.java -- Base class for character streaminput
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
  * This abstract class forms the base of the hierarchy of classes that read
  * input as a stream of characters.  It provides a common set of methods for
  * reading characters from streams.  Subclasses implement and extend these
  * methods to read characters from a particular input source such as a file
  * or network connection.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class Reader
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the <code>Object</code> used for synchronizing critical code
  * sections.  Subclasses should use this variabel instead of a 
  * synchronized method or an explicit synchronization on <code>this</code>
  */
protected Object lock;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a <code>Reader</code> that will use the object
  * itself for synchronization of critical code sections.
  */
public
Reader()
{
  lock = this;
  return;
}

/*************************************************************************/

/**
  * This method initializes a <code>Reader</code> that will use the specified
  * <code>Object</code> for synchronization of critical code sections.
  *
  * @param lock The <code>Object</code> to use for synchronization
  */
public
Reader(Object lock)
{
  this.lock = lock;
  return;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method marks a position in the input to which the stream can be
  * "reset" by calling the <code>reset()</code> method.  The parameter
  * <code>readlimit</code> is the number of chars that can be read from the 
  * stream after setting the mark before the mark becomes invalid.  For
  * example, if <code>mark()</code> is called with a read limit of 10, then when
  * 11 chars of data are read from the stream before the <code>reset()</code>
  * method is called, then the mark is invalid and the stream object
  * instance is not required to remember the mark.
  *  <p>
  * This method does nothing in this class, but subclasses may override it
  * to provide mark/reset functionality.
  *
  * @param readlimit The number of chars that can be read before the mark becomes invalid
  *
  * @exception IOException If an error occurs such as mark not being supported for this class
  */
public void
mark(int readlimit) throws IOException
{
  throw new IOException("Mark not supported");
}

/*************************************************************************/

/**
  * This method returns a boolean that indicates whether the mark/reset
  * methods are supported in this class.  Those methods can be used to
  * remember a specific point in the stream and reset the stream to that
  * point.
  * <p>
  * This method always returns <code>false</code> in this class, but subclasses
  * can override this method to return <code>true</code> if they support 
  * mark/reset functionality.
  *
  * @return <code>true</code> if mark/reset functionality is supported, <code>false</code> otherwise
  *
  */
public boolean
markSupported()
{
  return(false);
}

/*************************************************************************/

/**
  * This method resets a stream to the point where the <code>mark()</code> method
  * was called.  Any chars that were read after the mark point was set will
  * be re-read during subsequent reads.
  * <p>
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
  * This method determines whether or not this stream is ready to be
  * read.  If it returns <code>false</code> the stream may block if a
  * read is attempted, but it is not guaranteed to do so.
  * <p>
  * This method always returns <code>false</code> in this class
  *
  * @return <code>true</code> if the stream is ready to be read, <code>false</code> otherwise.
  *
  * @return IOException If an error occurs
  */
public boolean
ready() throws IOException
{
  return(false);
}

/*************************************************************************/

/**
  * This method skips the specified number of chars in the stream.  It
  * returns the actual number of chars skipped, which may be less than the
  * requested amount.
  * <p>
  * This method reads and discards chars into a 256 char array until the
  * specified number of chars were skipped or until either the end of stream
  * is reached or a read attempt returns a short count.  Subclasses can
  * override this metho to provide a more efficient implementation where
  * one exists.
  *
  * @param num_chars The requested number of chars to skip
  *
  * @return The actual number of chars skipped.
  *
  * @exception IOException If an error occurs
  */
public long
skip(long num_chars) throws IOException
{
  int buf_size = 256;
  char[] buf = new char[buf_size];

  if (num_chars <= 0)
    return(0);

  synchronized (lock) {

  long loop_count = num_chars / buf_size;
  int extra_chars = (int)(num_chars % buf_size);
  long chars_read = 0;
  long total_read = 0;

  for (long i = 0; i < loop_count; i++)
    {
      chars_read = read(buf, 0, buf.length);

      if (chars_read == -1)
        return(total_read);

      if (chars_read != buf_size)
        return(total_read + chars_read);

      total_read += chars_read;
    }

  chars_read = read(buf, 0, extra_chars);

  if (chars_read == -1)
    return(total_read);

  return(total_read + chars_read);

  } // synchronized
}

/*************************************************************************/

/**
  * This method reads an char from the input stream and returns it
  * as an int in the range of 0-65535.  This method also will return -1 if
  * the end of the stream has been reached.
  * <p>
  * This method will block until the char can be read.
  *
  * @return The char read or -1 if end of stream
  *
  * @exception IOException If an error occurs
  */
public int
read() throws IOException
{
  char[] buf = new char[1];

  int rc = read(buf, 0, buf.length);
  if (rc == -1)
    return(-1);
  else
    return(buf[0] & 0xFFFF);
}

/*************************************************************************/

/**
  * This method reads chars from a stream and stores them into a caller
  * supplied buffer.  This method attempts to completely fill the buffer,
  * but can return before doing so.  The actual number of chars read is
  * returned as an int.  A -1 is returned to indicate the end of the stream.
  * <p>
  * This method will block until some data can be read.
  * <p>
  * This method operates by calling an overloaded read method like so:
  * <code>read(buf, 0, buf.length)</code>
  *
  * @param buf The buffer into which the chars read will be stored.
  *
  * @return The number of chars read or -1 if end of stream.
  *
  * @exception IOException If an error occurs.
  */
public int
read(char[] buf) throws IOException
{
  return(read(buf, 0, buf.length));
}

/*************************************************************************/

/**
  * This method read chars from a stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index <code>offset</code> into
  * the buffer and attempts to read <code>len</code> chars.  This method can
  * return before reading the number of chars requested.  The actual number
  * of chars read is returned as an int.  A -1 is returned to indicate the
  * end of the stream.
  *  <p>
  * This method will block until some data can be read.
  * <p>
  * This method operates by calling the single char <code>read()</code> method
  * in a loop until the desired number of chars are read.  The read loop
  * stops short if the end of the stream is encountered or if an IOException
  * is encountered on any read operation except the first.  If the first
  * attempt to read a chars fails, the IOException is allowed to propagate
  * upward.  And subsequent IOException is caught and treated identically
  * to an end of stream condition.  Subclasses can (and should if possible)
  * override this method to provide a more efficient implementation.
  *
  * @param buf The array into which the chars read should be stored
  * @param offset The offset into the array to start storing chars
  * @param len The requested number of chars to read
  *
  * @return The actual number of chars read, or -1 if end of stream.
  *
  * @exception IOException If an error occurs.
  */
public abstract int
read(char[] buf, int offset, int len) throws IOException;

/*************************************************************************/

/**
  * This method closes the stream.  Any futher attempts to read from the
  * stream may generate an <code>IOException</code>.
  *
  * @exception IOException If an error occurs
  */
public abstract void
close() throws IOException;

} // class Reader

