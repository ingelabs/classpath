/*************************************************************************
/* BufferedWriter.java -- Buffer output into large blocks before writing
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
  * This class accumulates chars written in a buffer instead of immediately
  * writing the data to the underlying output sink. The chars are instead
  * as one large block when the buffer is filled, or when the stream is
  * closed or explicitly flushed. This mode operation can provide a more
  * efficient mechanism for writing versus doing numerous small unbuffered
  * writes.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class BufferedWriter extends Writer
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the default buffer size
  */
private static final int DEFAULT_BUFFER_SIZE = 512;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the internal char array used for buffering output before
  * writing it.
  */
private char[] buf;

/**
  * This is the number of chars that are currently in the buffer and
  * are waiting to be written to the underlying stream.  It always points to
  * the index into the buffer where the next char of data will be stored
  */
private int count;

/**
  * This is the underlying <code>Writer</code> to which this object
  * sends its output.
  */
private Writer out;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new <code>BufferedWriter</code> instance
  * that will write to the specified subordinate <code>Writer</code>
  * and which will use a default buffer size of 512 chars.
  *
  * @param out The underlying <code>Writer</code> to write data to
  */
public
BufferedWriter(Writer out)
{
  this(out, DEFAULT_BUFFER_SIZE);
}

/*************************************************************************/

/**
  * This method initializes a new <code>BufferedWriter</code> instance
  * that will write to the specified subordinate <code>Writer</code>
  * and which will use the specified buffer size
  *
  * @param out The underlying <code>Writer</code> to write data to
  * @param size The size of the internal buffer
  */
public
BufferedWriter(Writer out, int size)
{
  this.out = out;
  buf = new char[size];
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method causes any currently buffered chars to be immediately
  * written to the underlying output stream.
  *
  * @exception IOException If an error occurs
  */
public void
flush() throws IOException
{
  if (count == 0)
    return;

  synchronized (lock) {

  out.write(buf, 0, count);
  count = 0;

  } // synchronized
}

/*************************************************************************/

/**
  * This method flushes any remaining buffered chars then closes the 
  * underlying output stream.  Any further attempts to write to this stream
  * may throw an exception
  */
public void
close() throws IOException
{
  synchronized (lock) {

  flush();
  out.close();

  } // synchronized
}

/*************************************************************************/

/*
  * This method runs when the object is garbage collected.  It is 
  * responsible for ensuring that all buffered chars are written and
  * for closing the underlying stream.
  *
  * @exception IOException If an error occurs (ignored by the Java runtime)
  *
protected void
finalize() throws IOException
{
  close();
}
*/

/*************************************************************************/

/**
  * This method writes a single char of data.  This will be written to the
  * buffer instead of the underlying data source.  However, if the buffer
  * is filled as a result of this write request, it will be flushed to the
  * underlying output stream.
  *
  * @param b The char of data to be written, passed as an int
  *
  * @exception IOException If an error occurs
  */
public void
write(int b) throws IOException
{
  synchronized (lock) {

  buf[count] = (char)(b & 0xFFFF);

  ++count;
  if (count == buf.length)
    flush();

  } // synchronized
}

/*************************************************************************/

/**
  * This method writes out a system depedent line separator sequence.  The
  * actual value written is detemined from the <xmp>line.separator</xmp>
  * system property.
  *
  * @exception IOException If an error occurs
  */
public void
newLine() throws IOException
{
  String linesep = System.getProperty("line.separator");
  if (linesep == null)
    throw new Error("Cannot determine the system line separator sequence");

  write(linesep);
}

/*************************************************************************/

/**
  * This method writes <code>len</code> chars from the char array 
  * <code>buf</code> starting at position <code>offset</code> in the buffer. 
  * These chars will be written to the internal buffer.  However, if this
  * write operation fills the buffer, the buffer will be flushed to the
  * underlying output stream.
  *
  * @param buf The array of chars to write.
  * @param offset The index into the char array to start writing from.
  * @param len The number of chars to write.
  *
  * @exception IOException If an error occurs
  */
public void
write(char[] buf, int offset, int len) throws IOException
{
  if (len <= 0)
    return;

  synchronized (lock) {

  // Buffer can hold everything
  if (len < (this.buf.length - count))
    {
      System.arraycopy(buf, offset, this.buf, count, len);
      count += len;

      if (count == buf.length)
        flush();
    }
  else
    {
      flush();
       
      // Loop and write out full buffer chunks. As an optimization, 
      // don't buffer these, just write them
      int i = 0;
      if ((len / this.buf.length) != 0)
        for (i = 0; i < (len / this.buf.length); i++)
          out.write(buf, offset + (i * this.buf.length), this.buf.length);

      // Buffer the remaining chars
      if ((len % buf.length) != 0)
        {
          System.arraycopy(buf, offset + (i * this.buf.length), this.buf, count, 
                           len - (i * this.buf.length));
          count += (len - (i * this.buf.length));
        }
    }

  } // synchronized
}

/*************************************************************************/

/**
  * This method writes <code>len</code> chars from the <code>String</code>
  * <code>str</code> starting at position <code>offset</code> in the string. 
  * These chars will be written to the internal buffer.  However, if this
  * write operation fills the buffer, the buffer will be flushed to the
  * underlying output stream.
  *
  * @param str The <code>String</code> to write.
  * @param offset The index into the string to start writing from.
  * @param len The number of chars to write.
  *
  * @exception IOException If an error occurs
  */
public void
write(String str, int offset, int len) throws IOException
{
  char[] buf = new char[len];
  str.getChars(offset, len - offset, buf, 0);

  write(buf, 0, buf.length);
}

} // class BufferedWriter 

