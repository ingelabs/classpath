/*************************************************************************
/* LineNumberInputStream.java -- An input stream which counts line numbers
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
  * This class functions like a standard <code>InputStream</code> except that it
  * counts line numbers, and canonicalizes newline characters.  As data
  * is read, whenever the byte sequences "\r", "\n", or "\r\n" are encountered,
  * the running line count is incremeted by one.  Additionally, the whatever
  * line termination sequence was encountered will be converted to a "\n"
  * byte.  Note that this class numbers lines from 0.  When the first
  * line terminator is encountered, the line number is incremented to 1, and
  * so on.
  *
  * This class counts only line termination characters.  If the last line
  * read from the stream does not end in a line termination sequence, it
  * will not be counted as a line.
  *
  * Note that since this class operates as a filter on an underlying stream,
  * it has the same mark/reset functionality as the underlying stream.  The
  * <code>mark()</code> and <code>reset()</code> methods in this class handle line numbers
  * correctly.  Calling @code{reset()} resets the line number to the point
  * at which <code>mark()</code> was called if the subordinate stream supports
  * that functionality.
  *
  * This class is deprecated in favor if <code>LineNumberReader</code> because
  * it operates on ASCII bytes instead of an encoded character stream.  This
  * class is for backward compatibility only and should not be used in
  * new applications.
  *
  * @deprecated
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class LineNumberInputStream extends FilterInputStream
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This variable is used to keep track of the current line number
  */
private int line_number;

/**
  * This variable is used to keep track of the line number that was
  * current when the <code>mark()</code> method was called.
  */
private int marked_line_number;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create a new <code>LineNumberInputStream</code> that reads from the 
  * specified subordinate <code>InputStream</code>
  *
  * @param in The subordinate <code>InputStream</code> to read from
  */
public
LineNumberInputStream(InputStream in)
{
  super(in);

  // Override this.in with another filter
  this.in = new PushbackInputStream(in);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the current line number
  *
  * @returns The current line number
  */
public int 
getLineNumber()
{
  return(line_number);
}

/*************************************************************************/

/**
  * This method sets the current line number to the specified value.
  * 
  * @param line_number The new line number
  */
public void
setLineNumber(int line_number)
{
  this.line_number = line_number;
}

/*************************************************************************/

/**
  * This method returns the number of bytes that can be read from the 
  * stream before the stream can block.  This method is tricky because
  * the subordinate <code>InputStream</code> might return only "\r\n" characters,
  * which are replaced by a single "\n" character by the <code>read()</code> method
  * of this class.  So this method can only guarantee that
  * <code>in.available() / 2</code> bytes can actually be read before blocking.
  * In practice, considerably more bytes might be read before blocking
  *
  * Note that the stream may not block if additional bytes beyond the count
  * returned by this method are read.
  *
  * @return The number of bytes that can be read before blocking could occur
  *
  * @exception IOException If an error occurs
  */
public int
available() throws IOException
{
  return(in.available() / 2);
}

/*************************************************************************/

/**
  * This method marks a position in the input to which the stream can be
  * "reset" byte calling the <code>reset()</code> method.  The parameter
  * <code>readlimit</code> is the number of bytes that can be read from the
  * stream after setting the mark before the mark becomes invalid.   For
  * example, if <code>mark()</code> is called with a read limit of 10, then when
  * 11 bytes of data are read from the stream before the <code>reset()</code>
  * method is called, then the mark is invalid and the stream object
  * instance is not required to remember the mark.
  *
  * In this class, this method will remember the current line number as well
  * as the current position in the stream.  When the <code>reset()</code> method 
  * is called, the line number will be restored to the saved line number in
  * addition to the stream position.
  *
  * This method only works if the subordinate stream supports mark/reset
  * functionality.
  *
  * @param readlimit The number of bytes that can be read before the mark becomes invalid
  */
public synchronized void
mark(int readlimit)
{
  in.mark(readlimit);

  marked_line_number = line_number;
}

/*************************************************************************/

/**
  * This method resets a stream to the point where the <code>mark()</code> method
  * was called.  Any bytes that were read after the mark point was set will
  * be re-read during subsequent reads.
  *
  * In this class, this method will also restore the line number that was
  * current when the <code>mark()</code> method was called.
  * 
  * This method only works if the subordinate stream supports mark/reset
  * functionality.
  *
  * @exception IOException If an error occurs
  */
public synchronized void
reset() throws IOException
{
  in.reset();

  line_number = marked_line_number;
}

/*************************************************************************/

/**
  * This method reads an unsigned byte from the input stream and returns it
  * as an int in the range of 0-255.  This method will return -1 if the
  * end of the stream has been reached.
  *
  * Note that if a line termination sequence is encountered (ie, "\r",
  * "\n", or "\r\n") then that line termination sequence is converted to
  * a single "\n" value which is returned from this method.  This means
  * that it is possible this method reads two bytes from the subordinate
  * stream instead of just one.
  *
  * Note that this method will block until a byte of data is available
  * to be read.
  *
  * @return The byte read or -1 if end of stream
  * 
  * @exception IOException If an error occurs
  */
public synchronized int
read() throws IOException
{
  int byte_read = in.read();

  if (byte_read == '\n')
    ++line_number;

  if (byte_read == '\r')
    {
      int extra_byte_read = in.read();

      if ((extra_byte_read != '\n') && (extra_byte_read != -1))
        ((PushbackInputStream)in).unread(extra_byte_read);

      byte_read = '\n';
      ++line_number;
    }

  return(byte_read);
}

/*************************************************************************/

/**
  * This method reads bytes from a stream and stores them into a caller
  * supplied buffer.  It starts storing data at index <code>offset</code> into
  * the buffer and attemps to read <code>len</code> bytes.  This method can
  * return before reading the number of bytes requested.  The actual number
  * of bytes read is returned as an int.  A -1 is returned to indicated the
  * end of the stream.
  *
  * This method will block until some data can be read.
  *
  * Note that if a line termination sequence is encountered (ie, "\r",
  * "\n", or "\r\n") then that line termination sequence is converted to
  * a single "\n" value which is stored in the buffer.  Only a single
  * byte is counted towards the number of bytes read in this case.
  *
  * @param buf The array into which the bytes read should be stored
  * @param offset The offset into the array to start storing bytes
  * @param len The requested number of bytes to read
  *
  * @return The actual number of bytes read, or -1 if end of stream
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

  int total_read = 1;

  // Read the rest of the bytes.  We do this in a single read() loop
  // like a raw InputStream. That's ok in my book since this class is
  // deprecated anyway.
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

} // class LineNumberInputStream

