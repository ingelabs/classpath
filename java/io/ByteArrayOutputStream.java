/*************************************************************************
/* ByteArrayOutputStream.java -- Write bytes to a buffer
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
  * This class allows data to be written to a byte array buffer and
  * and then retrieved by an application.   The internal byte array
  * buffer is dynamically resized to hold all the data written.  Please
  * be aware that writing large amounts to data to this stream will
  * cause large amounts of memory to be allocated.
  * <p>
  * The size of the internal buffer defaults to 32 and it is resized
  * in increments of 1024 bytes.  This behavior can be over-ridden by using the
  * following two properties:
  * <p>
  * <ul>
  * <li><xmp>gnu.java.io.ByteArrayOutputStream.initialBufferSize</xmp>
  * <li><xmp>gnu.java.io.ByteArrayOutputStream.bufferIncrementSize</xmp>
  * </ul>
  * <p>
  * There is a constructor that specified the initial buffer size and
  * that is the preferred way to set that value because it it portable
  * across all Java class library implementations.
  * <p>
  * Note that this class also has methods that convert the byte array
  * buffer to a <code>String</code> using either the system default or an
  * application specified character encoding.  Thus it can handle 
  * multibyte character encodings.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ByteArrayOutputStream extends OutputStream
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
  * The number of bytes by which the buffer will be enlarged if it
  * is filled up
  */
private static int buffer_increment_size;

static
{
  String ibs_str = System.getProperty(
                      "gnu.java.io.ByteArrayOutputStream.initialBufferSize");
  String bis_str = System.getProperty(
                      "gnu.java.io.ByteArrayOutputStream.bufferIncrementSize");

  try
    {
      initial_buffer_size = Integer.parseInt(ibs_str);
    }
  catch (NumberFormatException e) { ; }

  try
    {
      buffer_increment_size = Integer.parseInt(bis_str);
    }
  catch (NumberFormatException e) { ; }

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
protected byte[] buf;

/**
  * The number of bytes that have been written to the buffer
  */
protected int count;

/*************************************************************************/

/*
 * Construtors
 */

/**
  * This method initializes a new <code>ByteArrayOutputStream</code> with
  * the default buffer size of 32 bytes.  If a different initial buffer
  * size is desired, see the constructor <code>ByteArrayOutputStream(int size)</code>.
  * For applications where the source code is not available, the default buffer
  * size can be set using the system property
  * <xmp>gnu.java.io.ByteArrayOutputStream.initialBufferSize</xmp>
  */
public
ByteArrayOutputStream()
{
  this(initial_buffer_size);
}

/*************************************************************************/

/**
  * This method initializes a new <code>ByteArrayOutputStream</code> with
  * a specified initial buffer size.
  *
  * @param size The initial buffer size in bytes
  */
public
ByteArrayOutputStream(int size)
{
  buf = new byte[size];
}
  
/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method discards all of the bytes that have been written to the
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
  * This method returns the number of bytes that have been written to
  * the buffer so far.  This is the same as the value of the protected
  * <code>count</code> variable.  If the <code>reset</code> method is
  * called, then this value is reset as well.  Note that this method does
  * not return the length of the internal buffer, but only the number
  * of bytes that have been written to it.
  *
  * @return The number of bytes in the internal buffer
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
  * This method returns a byte array containing the bytes that have been
  * written to this stream so far.  This array is a copy of the valid
  * bytes in the internal buffer and its length is equal to the number of
  * valid bytes, not necessarily to the the length of the current 
  * internal buffer.  Note that since this method allocates a new array,
  * it should be used with caution when the internal buffer is very large.
  */
public byte[]
toByteArray()
{
  byte[] buf = new byte[count];

  System.arraycopy(this.buf, 0, buf, 0, count);

  return(buf);
}

/*************************************************************************/

/**
  * Returns the bytes in the internal array as a <code>String</code>.  The
  * bytes in the buffer are converted to characters using the system default
  * encoding.  There is an overloaded <code>toString()</code> method that
  * allows an application specified character encoding to be used.
  *
  * @return A <code>String</code> containing the data written to this stream so far
  */
public String
toString()
{
  return("Implement Me!");
}

/*************************************************************************/

/**
  * Returns the bytes in the internal array as a <code>String</code>.  The
  * bytes in the buffer are converted to characters using the specified
  * encoding. 
  *
  * @param encoding The name of the character encoding to use
  *
  * @return A <code>String</code> containing the data written to this stream so far
  */
public String
toString(String encoding)
{
  return("Implement Me!");
}

/*************************************************************************/

/**
  * This method returns the bytes in the internal array as a <code>String</code>.
  * It uses each byte in the array as the low order eight bits of the Unicode
  * character value and the passed in parameter as the high eight bits.
  * <p>
  * This method does not convert bytes to characters in the proper way and
  * so is deprecated in favor of the other overloaded <code>toString</code>
  * methods which use a true character encoding.
  *
  * @param high_byte The high eight bits to use for each character in the <code>String</code>
  *
  * @return A <code>String</code> containing the data written to this stream so far
  */
public String
toString(int high_byte)
{
  StringBuffer sb = new StringBuffer("");

  for (int i = 0; i < count; i++)
    {
      char c = (char)(((high_byte & 0xFF) << 8) | (buf[i] & 0xFF));
      sb.append(c);
    }

  return(sb.toString());
}

/*************************************************************************/

/**
  * This method writes the writes the specified byte into the internal
  * buffer.
  *
  * @param b The byte to be read passed as an int
  */
public synchronized void
write(int b)
{
  if (count == buf.length)
    enlargeBuffer();

  buf[count] = (byte)b;
  ++count;
}

/*************************************************************************/

/**
  * This method writes <code>len</code> bytes from the passed in array 
  * <code>buf</code> starting at index <code>offset</code> into the
  * internal buffer.
  *
  * @param buf The byte array to write data from
  * @param offset The index into the buffer to start writing data from
  * @param len The number of bytes to write
  */
public synchronized void
write(byte[] buf, int offset, int len)
{
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
}

/*************************************************************************/

/**
  * This method writes all the bytes that have been written to this stream
  * from the internal buffer to the specified <code>OutputStream</code>.
  *
  * @param out The <code>OutputStream</code> to write to
  *
  * @exception IOException If an error occurs
  */
public void
writeTo(OutputStream out) throws IOException
{
  out.write(buf, 0, count);
}

/*************************************************************************/

/**
  * This private method makes the buffer bigger when we run out of room
  * by allocating a larger buffer and copying the valid bytes from the
  * old array into it.  This is obviously slow and should be avoided by
  * application programmers by setting their initial buffer size big
  * enough to hold everything if possible.
  */
private void
enlargeBuffer()
{
  byte[] newbuf = new byte[buf.length + buffer_increment_size];

  System.arraycopy(buf, 0, newbuf, 0, count);

  buf = newbuf;
}

} // class ByteArrayOutputStream

