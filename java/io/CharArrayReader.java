/*************************************************************************
/* CharArrayReader.java -- Read an array of characters as a stream
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
  * This class permits an array of chars to be read as an input stream.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class CharArrayReader extends Reader
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The array that contains the data supplied during read operations
  */
protected char[] buf;

/**
  * The array index of the next char to be read from the buffer <code>buf</code>
  */
protected int pos;

/**
  * The currently marked position in the stream.  This defaults to 0, so a
  * reset operation on the stream resets it to read from array index 0 in
  * the buffer - even if the stream was initially created with an offset
  * greater than 0
  */
protected int markedPos;

/**
  * This indicates the maximum number of chars that can be read from this
  * stream.  It is the array index of the position after the last valid
  * char in the buffer <code>buf</code>
  */
protected int count;
  
/*************************************************************************/

/**
  * Create a new CharArrayReader that will read chars from the passed
  * in char array.  This stream will read from the beginning to the end
  * of the array.  It is identical to calling an overloaded constructor
  * as <code>CharArrayReader(buf, 0, buf.length)</code>.
  * <p>
  * Note that this array is not copied.  If its contents are changed 
  * while this stream is being read, those changes will be reflected in the
  * chars supplied to the reader.  Please use caution in changing the 
  * contents of the buffer while this stream is open.
  *
  * @param buf The char array buffer this stream will read from.
  */
public
CharArrayReader(char[] buf)
{
  this(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * Create a new CharArrayReader that will read chars from the passed
  * in char array.  This stream will read from position <code>offset</code> in
  * the array for a length of <code>length</code> chars past <code>offset</code>.  If the
  * stream is reset to a position before <code>offset</code> then more than
  * <code>length</code> chars can be read from the stream.  The <code>length</code> value
  * should be viewed as the array index one greater than the last position
  * in the buffer to read.
  * <p>
  * Note that this array is not copied.  If its contents are changed 
  * while this stream is being read, those changes will be reflected in the
  * chars supplied to the reader.  Please use caution in changing the 
  * contents of the buffer while this stream is open.
  *
  * @param buf The char array buffer this stream will read from.
  * @param offset The index into the buffer to start reading chars from
  * @param length The number of chars to read from the buffer
  */
public
CharArrayReader(char[] buf, int offset, int length)
{
  this.buf = buf;
  this.pos = offset;
  this.count = length;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns <code>true</code> always unless since instances of
  * <code>CharArrayReader</code> are always ready to be read.
  *
  * @return <code>true</code> to indicate that this stream is ready to be read.
  */
public boolean
ready()
{
  return(true);
}

/*************************************************************************/

/**
  * This method overrides the <code>markSupported</code> method in <code>Reader</code>
  * in order to return <code>true</code> - indicating that this stream class
  * supports mark/reset functionality.
  *
  * @return <code>true</code> to indicate that this class supports mark/reset.
  */
public boolean
markSupported() 
{
  return(true);
}

/*************************************************************************/

/**
  * This method sets the mark position in this stream to the current
  * position.  Note that the <code>readlimit</code> parameter in this method does
  * nothing as this stream is always capable of remembering all the chars
  * int it.
  * <p>
  * Note that in this class the mark position is set by default to
  * position 0 in the stream.  This is in constrast to some other stream types
  * where there is no default mark position.
  *
  * @param readlimit The number of chars this stream must remember.  This parameter is ignored.
  *
  * @exception IOException If an error occurs
  */
public void
mark(int readlimit) throws IOException
{
  markedPos = pos;
}

/*************************************************************************/

/**
  * This method sets the read position in the stream to the mark point by
  * setting the <code>pos</code> variable equal to the <code>mark</code> variable.
  * Since a mark can be set anywhere in the array, the mark/reset methods
  * int this class can be used to provide random search capabilities for
  * this type of stream.
  */
public void
reset()
{
  pos = markedPos;
} 

/*************************************************************************/

/**
  * This method attempts to skip the requested number of chars in the
  * input stream.  It does this by advancing the <code>pos</code> value by the
  * specified number of chars.  It this would exceed the length of the
  * buffer, then only enough chars are skipped to position the stream at
  * the end of the buffer.  The actual number of chars skipped is returned.
  *
  * @param num_chars The requested number of chars to skip
  *
  * @return The actual number of chars skipped.
  */
public long
skip(long num_chars)
{
  if (num_chars <= 0)
    return(0);

  synchronized (lock) {

  if (num_chars > (count - pos))
    {
      int retval = count - pos;
      pos = count;
      return(retval);
    }

  pos += num_chars;
  return(num_chars); 

  } // synchronized
}

/*************************************************************************/

/**
  * This method reads one char from the stream.  The <code>pos</code> counter is
  * advanced to the next char to be read.  The char read is returned as
  * an int in the range of 0-65535.  If the stream position is already at the
  * end of the buffer, no char is read and a -1 is returned in order to
  * indicate the end of the stream.
  *
  * @return The char read, or -1 if end of stream
  */
public int
read()
{
  if (pos >= count)
    return(-1);

  synchronized (lock) {

  ++pos;

  return((buf[pos - 1] & 0xFFFF));

  } // synchronized 
}  

/*************************************************************************/

/**
  * This method reads chars from the stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index <code>offset</code> into
  * the buffer and attempts to read <code>len</code> chars.  This method can
  * return before reading the number of chars requested if the end of the
  * stream is encountered first.  The actual number of chars read is 
  * returned.  If no chars can be read because the stream is already at 
  * the end of stream position, a -1 is returned.
  * <p>
  * This method does not block.
  *
  * @param buf The array into which the chars read should be stored.
  * @param offset The offset into the array to start storing chars
  * @param len The requested number of chars to read
  *
  * @return The actual number of chars read, or -1 if end of stream.
  */
public int
read(char[] buf, int offset, int len)
{
  if (len == 0)
    return(0);

  if (pos == count)
    return(-1);

  synchronized (lock) {

  // All requested chars can be read
  if (len < (count - pos))
    {
      System.arraycopy(this.buf, pos, buf, offset, len);
      pos += len;
      return(len);
    }
  // Cannot read all requested chars because there aren't enough left in buf
  else
    {
      System.arraycopy(this.buf, pos, buf, offset, count - pos);

      int retval = count - pos;
      pos = count;
      return(retval);
    }

  } // synchronized
}

/*************************************************************************/

/**
  * This method closes the stream.  In this class, this method does nothing
  *
  * @exception IOException If an error occurs
  */
public void
close() throws IOException
{
  ;
}

} // class CharArrayReader

