/*************************************************************************
/* StringBufferInputStream.java -- Read an String as a stream
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
  * This class permits a <code>String</code> to be read as an input stream.  The 
  * low eight bits of each character in the <code>String</code> are the bytes that
  * are returned.  The high eight bits of each character are discarded.
  * <p>
  * The mark/reset functionality in this class behaves differently than
  * normal.  The <code>mark()</code> method is always ignored and the 
  * <code>reset()</code> method always resets in stream to start reading from 
  * position 0 in the String.  Note that since this method does not override 
  * <code>markSupported()</code> in <code>InputStream</code>, calling that 
  * method will return <code>false</code>.
  * <p>
  * Note that this class is deprecated because it does not properly handle
  * 16-bit Java characters.  It is provided for backwards compatibility only
  * and should not be used for new development.  The <code>StringReader</code>
  * class should be used instead.
  *
  * @deprecated
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class StringBufferInputStream extends InputStream
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The <code>String</code> that contains the data supplied during read 
  * operations
  */
protected String buffer;

/**
  * The index of the next byte to be read from <code>buffer</code>
  */
protected int pos = 0;

/**
  * This indicates the maximum number of bytes that can be read from this
  * stream.  It is the length of the <code>String</code> that this stream is
  * reading bytes from.
  */
protected int count;
  
/*************************************************************************/

/**
  * Create a new <code>StringBufferInputStream</code> that will read bytes from the 
  * passed in <code>String</code>.  This stream will read from the beginning to the 
  * end of the <code>String</code>.
  *
  * @param s The <code>String</code> this stream will read from.
  */
public
StringBufferInputStream(String s)
{
  buffer = s;
  count = s.length();
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the number of bytes available to be read from this
  * stream.  The value returned will be equal to <code>count - pos</code>.
  *
  * @return The number of bytes that can be read from this stream before blocking, which is all of them
  */
public int
available()
{
  return(count - pos);
}

/*************************************************************************/

/**
  * This method sets the read position in the stream to the beginning
  * setting the <code>pos</code> variable equal to 0.  Note that this differs
  * from the common implementation of the <code>reset()</code> method.
  */
public void
reset()
{
  pos = 0;
} 

/*************************************************************************/

/**
  * This method attempts to skip the requested number of bytes in the
  * input stream.  It does this by advancing the <code>pos</code> value by the
  * specified number of bytes.  It this would exceed the length of the
  * buffer, then only enough bytes are skipped to position the stream at
  * the end of the buffer.  The actual number of bytes skipped is returned.
  *
  * @param num_bytes The requested number of bytes to skip
  *
  * @return The actual number of bytes skipped.
  */
public synchronized long
skip(long num_bytes)
{
  if (num_bytes <= 0)
    return(0);

  if (num_bytes > (count - pos))
    {
      int retval = count - pos;
      pos = count;
      return(retval);
    }

  pos += num_bytes;
  return(num_bytes); 
}

/*************************************************************************/

/**
  * This method reads one byte from the stream.  The <code>pos</code> counter 
  * is advanced to the next byte to be read.  The byte read is returned as
  * an int in the range of 0-255.  If the stream position is already at the
  * end of the buffer, no byte is read and a -1 is returned in order to
  * indicate the end of the stream.
  *
  * @return The byte read, or -1 if end of stream
  */
public synchronized int
read()
{
  if (pos >= count)
    return(-1);

  ++pos;

  return(buffer.charAt(pos - 1) & 0xFF);
}  

/*************************************************************************/

/**
  * This method reads bytes from the stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index <code>offset</code> 
  * into the buffer and attempts to read <code>len</code> bytes.  This method can
  * return before reading the number of bytes requested if the end of the
  * stream is encountered first.  The actual number of bytes read is 
  * returned.  If no bytes can be read because the stream is already at 
  * the end of stream position, a -1 is returned.
  * <p>
  * This method does not block.
  *
  * @param buf The array into which the bytes read should be stored.
  * @param offset The offset into the array to start storing bytes
  * @param len The requested number of bytes to read
  *
  * @return The actual number of bytes read, or -1 if end of stream.
  */
public synchronized int
read(byte[] buf, int offset, int len)
{
  if (len == 0)
    return(0);

  if (pos == count)
    return(-1);

  // All requested bytes can be read
  if (len < (count - pos))
    {
      for (int i = 0; i < len; i++)
        buf[offset + i] = (byte)(buffer.charAt(pos + i) & 0xFF);

      pos +=len;
      return(len);
    }
  // Cannot read all requested bytes because there aren't enough left in buf
  else
    {
      for (int i = 0; i < (count - pos); i++)
        buf[offset + i] = (byte)(buffer.charAt(pos + i) & 0xFF);

      int retval = count - pos;
      pos = count;
      return(retval);
    }
}

} // class StringBufferInputStream

