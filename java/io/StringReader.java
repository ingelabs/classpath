/*************************************************************************
/* StringReader.java -- Read an String as a character stream
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
  * This class permits a <code>String</code> to be read as a character 
  * input stream.
  * <p>
  * The mark/reset functionality in this class behaves differently than
  * normal.  If no mark has been set, then calling the <code>reset()</code>
  * method rewinds the read pointer to the beginning of the <code>String</code>.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class StringReader extends Reader
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The <code>String</code> that contains the data supplied during read 
  * operations
  */
private String buffer;

/**
  * The index of the next char to be read from <code>buffer</code>
  */
private int pos = 0;

/**
  * This indicates the maximum number of chars that can be read from this
  * stream.  It is the length of the <code>String</code> that this stream is
  * reading chars from.
  */
private int count;

/**
  * This is the position in the <code>String</code> that is marked for
  * later resetting.
  */
private int markpos = 0;

/*************************************************************************/

/**
  * Create a new <code>StringReader</code> that will read chars from the 
  * passed in <code>String</code>.  This stream will read from the beginning to the 
  * end of the <code>String</code>.
  *
  * @param s The <code>String</code> this stream will read from.
  */
public
StringReader(String s)
{
  buffer = s;
  count = s.length();
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method determines if the stream is ready to be read.  This class
  * is always ready to read and so always returns <code>true</code>.
  *
  * @return <code>true</code> to indicate that this object is ready to be read.
  */
public boolean
ready()
{
  return(true);
}

/*************************************************************************/

/**
  * This method returns <code>true</code> to indicate that this class
  * supports mark/reset functionality.
  *
  * @return <code>true</code> to indicate this class supports mark/reset functionality
  */
public boolean
markSupported()
{
  return(true);
}

/*************************************************************************/

/**
  * This method marks the current position in the stream so that a subsequent
  * call to the <code>reset()</code> method will reset the read position in
  * the stream to this point.
  * <p>
  * Note that the read limit, which would normally be used to specify the
  * maximum amount of characters than could be read before the mark is
  * discarded, is ignored in this class.  Objects of this class can always
  * remember all their chars.
  *
  * @param readlimit Ignored
  *
  * @exception IOException If an error occurs, which it won't
  */
public void
mark(int readlimit) throws IOException
{
  markpos = pos;
}

/*************************************************************************/

/**
  * This method sets the read position in the stream to the previously
  * marked position or to 0 (i.e., the beginning of the stream) if the mark
  * has not already been set.
  */
public void
reset()
{
  pos = markpos;
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
  * This method reads one char from the stream.  The <code>pos</code> counter 
  * is advanced to the next char to be read.  The char read is returned as
  * an int in the range of 0-255.  If the stream position is already at the
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

  return(buffer.charAt(pos - 1));

  }
}  

/*************************************************************************/

/**
  * This method reads chars from the stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index <code>offset</code> 
  * into the buffer and attempts to read <code>len</code> chars.  This method can
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
      for (int i = 0; i < len; i++)
        buf[offset + i] = buffer.charAt(pos + i);

      pos +=len;
      return(len);
    }
  // Cannot read all requested chars because there aren't enough left in buf
  else
    {
      for (int i = 0; i < (count - pos); i++)
        buf[offset + i] = buffer.charAt(pos + i);

      int retval = count - pos;
      pos = count;
      return(retval);
    }

  } // synchronized
}

/*************************************************************************/

/**
  * Closes the stream.  This method has no effect in this class.
  */
public void
close()
{
  return;
}


} // class StringReader

