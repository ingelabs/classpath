/*************************************************************************
/* SequenceInputStream.java -- Reads multiple input streams in sequence
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

import java.util.Enumeration;

/**
  * This class merges a sequence of multiple <code>InputStream</code>'s in order
  * to form a single logical stream that can be read by applications that
  * expect only one stream.
  * <p>
  * The streams passed to the constructor method are read in order until
  * they return -1 to indicate they are at end of stream.  When a stream
  * reports end of stream, it is closed, then the next stream is read.
  * When the last stream is closed, the next attempt to read from this
  * stream will return a -1 to indicate it is at end of stream.
  * <p>
  * If this stream is closed prior to all subordinate streams being read
  * to completion, all subordinate streams are closed.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class SequenceInputStream extends InputStream
{

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This variable holds the <code>Enumeration</code> containing the list of
  * streams to be read, or <code>null</code> if the subordinate streams are not
  * being obtained from an <code>Enumeration</code>
  */
private Enumeration stream_list;

/**
  * This variable holds the first <code>InputStream</code> to read, if the list
  * of streams is not being obtained from an <code>Enumeration</code>
  */
private InputStream first_stream;

/**
  * This variable holds the second <code>InputStream</code> to read, if the list
  * of streams is not being obtained from an <code>Enumeration</code>
  */
private InputStream second_stream;

/**
  * This is the current <code>InputStream</code> that is being read
  */
private InputStream current_stream;

/**
  * This value is <code>true</code> if this stream has been closed.  It is
  * <code>false</code> otherwise.
  */
private boolean closed = false;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method creates a new <code>SequenceInputStream</code> that obtains its
  * list of subordinate <code>InputStream</code>s from the specified
  * <code>Enumeration</code>
  *
  * @param stream_list An <code>Enumeration</code> that will return a list of <code>InputStream</code>s to read in sequence
  */
public
SequenceInputStream(Enumeration stream_list)
{
  this.stream_list = stream_list;

  current_stream = (InputStream)stream_list.nextElement();
}

/*************************************************************************/

/**
  * This method creates a new <code>SequenceInputStream</code> that will read the
  * two specified subordinate <code>InputStream</code>s in sequence.
  *
  * @param first_stream The first <code>InputStream</code> to read
  * @param second_stream The second <code>InputStream</code> to read
  */
public 
SequenceInputStream(InputStream first_stream, InputStream second_stream)
{
  this.first_stream = first_stream;
  this.second_stream = second_stream;
  current_stream = first_stream;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the number of bytes than can be read from the
  * currently being read subordinate stream before that stream could
  * block.  Note that it is possible more bytes than this can actually
  * be read without the stream blocking.  If a 0 is returned, then the
  * stream could block on the very next read.
  *
  * @return The number of bytes that can be read before blocking could occur
  *
  * @exception IOException If an error occurs
  */
public int
available() throws IOException
{
  if (closed)
    return(0);

  return(current_stream.available());
}

/*************************************************************************/

/**
  * Closes this stream.  This will cause any remaining unclosed subordinate
  * <code>InputStream</code>'s to be closed as well.  Subsequent attempts to 
  * read from this stream may cause an exception.
  *
  * @exception IOException If an error occurs
  */
public synchronized void
close() throws IOException
{
  if (closed)
    return;

  if ((current_stream == first_stream) && (first_stream != null))
    {
      first_stream.close();
      second_stream.close();
    }

  if ((current_stream == second_stream) && (second_stream != null))
    second_stream.close();

  if (stream_list != null)
    while (stream_list.hasMoreElements())
      {
        InputStream stream = (InputStream)stream_list.nextElement();
        stream.close();
      }

  closed = true;
}

/*************************************************************************/

/**
  * This method reads an unsigned byte from the input stream and returns it
  * as an int in the range of 0-255.  This method also will return -1 if
  * the end of the stream has been reached.  This will only happen when
  * all of the subordinate streams have been read.
  * <p>
  * This method will block until the byte can be read.
  *
  * @return The byte read, or -1 if end of stream
  *
  * @exception IOException If an error occurs
  */
public synchronized int
read() throws IOException
{
  if (closed)
    return(-1);

  int byte_read = current_stream.read();

  while (byte_read == -1)
    {
      getNextStream();
      if (current_stream == null)
        {
          close();
          return(-1);
        }

      byte_read = current_stream.read();
    }    

  return(byte_read);
}

/*************************************************************************/

/**
  * This method reads bytes from a stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index <code>offset</code> into
  * the buffer and attempts to read <code>len</code> bytes.  This method can
  * return before reading the number of bytes requested.  The actual number
  * of bytes read is returned as an int.  A -1 is returend to indicate the
  * end of the stream.  This will only happen when all of the subordinate
  * streams have been read.
  * <p>
  * This method will block until at least one byte can be read.
  *
  * @param buf The array into which bytes read should be stored
  * @param offset The offset into the array to start storing bytes
  * @param len The requested number of bytes to read
  *
  * @return The actual number of bytes read, or -1 if end of stream
  *
  * @exception IOException If an error occurs
  */
public synchronized int
read(byte[] buf, int offset, int len) throws IOException
{
  if (closed)
    return(-1);

  if (len == 0)
    return(0);

  int total_read = 0;
  for (;;)
    {
      int bytes_read = current_stream.read(buf, offset + total_read, 
                                          len - total_read);

      // Are we done?
      if (bytes_read == (len - total_read))
        return(len);

      // Is the current stream empty?
      if (bytes_read == -1)
        {
          getNextStream();
          if (current_stream == null)
            {
              close();
              if (total_read == 0)
                return(-1);
              else
                return(total_read);
            }
        }
      else
        total_read += bytes_read;
    }
}

/*************************************************************************/

/**
  * This private method is used to get the next <code>InputStream</code> to read
  * from
  */
private void
getNextStream()
{
  if (stream_list == null)
    {
      if (current_stream == first_stream)
        current_stream = second_stream;
      else
        current_stream = null;
    }
  else
    {
      if (stream_list.hasMoreElements())
        current_stream = (InputStream)stream_list.nextElement();
      else
        current_stream = null;
    }

  return;
}

} // class SequenceInputStream

