/* CharArrayWriter.java -- Write chars to a buffer
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

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


package java.io;

/**
  * This class allows data to be written to a char array buffer and
  * and then retrieved by an application.   The internal char array
  * buffer is dynamically resized to hold all the data written.  Please
  * be aware that writing large amounts to data to this stream will
  * cause large amounts of memory to be allocated.
  * <p>
  * The size of the internal buffer defaults to 32 and it is resized
  * in increments of 1024 chars.  This behavior can be over-ridden by using the
  * following two properties:
  * <p>
  * <ul>
  * <li><xmp>gnu.java.io.CharArrayWriter.initialBufferSize</xmp>
  * <li><xmp>gnu.java.io.CharArrayWriter.bufferIncrementSize</xmp>
  * </ul>
  * <p>
  * There is a constructor that specified the initial buffer size and
  * that is the preferred way to set that value because it it portable
  * across all Java class library implementations.
  * <p>
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class CharArrayWriter extends Writer
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * The default initial buffer size
  */
private static final int DEFAULT_INITIAL_BUFFER_SIZE = 32;

/**
  * The default buffer increment size
  */
private static final int DEFAULT_BUFFER_INCREMENT_SIZE = 1024;

/**
  * The initial size of the internal buffer
  */
private static int initial_buffer_size;

/**
  * The number of chars by which the buffer will be enlarged if it
  * is filled up
  */
private static int buffer_increment_size;

static
{
  initial_buffer_size = Integer.getInteger(
                      "gnu.java.io.CharArrayWriter.initialBufferSize",
                      DEFAULT_INITIAL_BUFFER_SIZE).intValue();
  buffer_increment_size = Integer.getInteger(
                      "gnu.java.io.CharArrayWriter.bufferIncrementSize",
                      DEFAULT_BUFFER_INCREMENT_SIZE).intValue();

  if (initial_buffer_size <= 0)
    initial_buffer_size = DEFAULT_INITIAL_BUFFER_SIZE;
  if (buffer_increment_size <= 0)
    buffer_increment_size = DEFAULT_BUFFER_INCREMENT_SIZE;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The internal buffer where the data written is stored
  */
protected char[] buf;

/**
  * The number of chars that have been written to the buffer
  */
protected int count;

/*************************************************************************/

/*
 * Construtors
 */

/**
  * This method initializes a new <code>CharArrayWriter</code> with
  * the default buffer size of 32 chars.  If a different initial buffer
  * size is desired, see the constructor <code>CharArrayWriter(int size)</code>.
  * For applications where the source code is not available, the default buffer
  * size can be set using the system property
  * <xmp>gnu.java.io.CharArrayWriter.initialBufferSize</xmp>
  */
public
CharArrayWriter()
{
  this(initial_buffer_size);
}

/*************************************************************************/

/**
  * This method initializes a new <code>CharArrayWriter</code> with
  * a specified initial buffer size.
  *
  * @param size The initial buffer size in chars
  */
public
CharArrayWriter(int size)
{
  buf = new char[size];
}
  
/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method flushes all buffered chars to the stream.  It does nothing
  * in this class
  */
public void
flush()
{
  ;
}

/*************************************************************************/

/**
  * Closes the stream.  This method is guaranteed not to free the contents
  * of the internal buffer, which can still be retrieved.
  */
public void
close()
{
  ;
}

/*************************************************************************/

/**
  * This method discards all of the chars that have been written to the
  * internal buffer so far by setting the <code>count</code> variable to
  * 0.  The internal buffer remains at its currently allocated size.
  */
public void
reset()
{
  count = 0;
}

/*************************************************************************/

/**
  * This method returns the number of chars that have been written to
  * the buffer so far.  This is the same as the value of the protected
  * <code>count</code> variable.  If the <code>reset</code> method is
  * called, then this value is reset as well.  Note that this method does
  * not return the length of the internal buffer, but only the number
  * of chars that have been written to it.
  *
  * @return The number of chars in the internal buffer
  *
  * @see reset
  */
public int
size()
{
  return(count);
}

/*************************************************************************/

/**
  * This method returns a char array containing the chars that have been
  * written to this stream so far.  This array is a copy of the valid
  * chars in the internal buffer and its length is equal to the number of
  * valid chars, not necessarily to the the length of the current 
  * internal buffer.  Note that since this method allocates a new array,
  * it should be used with caution when the internal buffer is very large.
  */
public char[]
toCharArray()
{
  char[] buf = new char[count];

  System.arraycopy(this.buf, 0, buf, 0, count);

  return(buf);
}

/*************************************************************************/

/**
  * Returns the chars in the internal array as a <code>String</code>.  The
  * chars in the buffer are converted to characters using the system default
  * encoding.  There is an overloaded <code>toString()</code> method that
  * allows an application specified character encoding to be used.
  *
  * @return A <code>String</code> containing the data written to this stream so far
  */
public String
toString()
{
  return(new String(buf));
}

/*************************************************************************/

/**
  * This method writes the writes the specified char into the internal
  * buffer.
  *
  * @param b The char to be read passed as an int
  */
public void
write(int b)
{
  synchronized (lock) {

  if (count == buf.length)
    enlargeBuffer();

  buf[count] = (char)b;
  ++count;

  }
  // synchronized
}

/*************************************************************************/

/**
  * This method writes <code>len</code> chars from the passed in array 
  * <code>buf</code> starting at index <code>offset</code> into that buffer
  *
  * @param buf The char array to write data from
  * @param offset The index into the buffer to start writing data from
  * @param len The number of chars to write
  */
public void
write(char[] buf, int offset, int len)
{
  synchronized (lock) {

  int i = 0;
  while (i < (len / buffer_increment_size))
    {
      enlargeBuffer();
      System.arraycopy(buf, offset + (i * buffer_increment_size), this.buf, 
                       count, buffer_increment_size);

      count += buffer_increment_size;
      ++i;
    }

  if ((len % buffer_increment_size) != 0)
    {
      if ((buf.length - count) < (len % buffer_increment_size))
        enlargeBuffer();

      System.arraycopy(buf, offset + (i * buffer_increment_size), this.buf,
                       count, len % buffer_increment_size);

      count += (len % buffer_increment_size);
    }

  } // synchronized
}

/*************************************************************************/

/**
  * This method writes <code>len</code> chars from the passed in <code>String</code>
  * <code>buf</code> starting at index <code>offset</code> into the
  * internal buffer.
  *
  * @param str The <code>String</code> to write data from
  * @param offset The index into the string to start writing data from
  * @param len The number of chars to write
  */
public void
write(String str, int offset, int len)
{
  char[] tmpbuf = new char[len];
  str.getChars(offset, len - offset, tmpbuf, 0);

  write(tmpbuf, 0, tmpbuf.length);
}

/*************************************************************************/

/**
  * This method writes all the chars that have been written to this stream
  * from the internal buffer to the specified <code>Writer</code>.
  *
  * @param out The <code>Writer</code> to write to
  *
  * @exception IOException If an error occurs
  */
public void
writeTo(Writer out) throws IOException
{
  out.write(buf, 0, count);
}

/*************************************************************************/

/**
  * This private method makes the buffer bigger when we run out of room
  * by allocating a larger buffer and copying the valid chars from the
  * old array into it.  This is obviously slow and should be avoided by
  * application programmers by setting their initial buffer size big
  * enough to hold everything if possible.
  */
private void
enlargeBuffer()
{
  char[] newbuf = new char[buf.length + buffer_increment_size];

  System.arraycopy(buf, 0, newbuf, 0, count);

  buf = newbuf;
}

} // class CharArrayWriter

