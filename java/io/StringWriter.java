/* StringWriter.java -- Writes bytes to a StringBuffer
   Copyright (C) 1998 Free Software Foundation, Inc.

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

// Wow is this a dumb class.  CharArrayWriter can do all this and
// more.  I would redirect all calls to one in fact, but the javadocs say
// use a StringBuffer so I will comply.

/**
  * This class writes chars to an internal <code>StringBuffer</code> that
  * can then be used to retrieve a <code>String</code>.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class StringWriter extends Writer
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * This is the default size of the buffer if the user doesn't specify it.
  */
private static final int DEFAULT_BUFFER_SIZE = 32;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the <code>StringBuffer</code> that we use to store bytes that
  * are written.
  */
private StringBuffer buf;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new <code>StringWriter</code> to write to a
  * <code>StringBuffer</code> initially sized to a default size of 32
  * chars.
  */
public
StringWriter()
{
  this(DEFAULT_BUFFER_SIZE);
}

/*************************************************************************/

/**
  * This method initializes a new <code>StringWriter</code> to write to a
  * <code>StringBuffer</code> with the specified initial size.
  *
  * @param size The initial size to make the <code>StringBuffer</code>
  */
public
StringWriter(int size)
{
  buf = new StringBuffer(size);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the <code>StringBuffer</code> object that this
  * object is writing to.  Note that this is the actual internal buffer, so
  * any operations performed on it will affect this stream object.
  *
  * @return The <code>StringBuffer</code> object being written to
  */
public StringBuffer
getBuffer()
{
  return(buf);
}

/*************************************************************************/

/**
  * This method returns the contents of the internal <code>StringBuffer</code>
  * as a <code>String</code>.
  *
  * @return A <code>String</code> representing the chars written to this stream.
  */
public String
toString()
{
  return(buf.toString());
}

/*************************************************************************/

/**
  * This method flushes any buffered characters to the underlying output.
  * It does nothing in this class.
  */
public void
flush()
{
  ; // Do Nothing
}

/*************************************************************************/

/**
  * This method closes the stream.  The contents of the internal buffer
  * can still be retrieved, but future writes are not guaranteed to work.
  */
public void
close()
{
  ; // Do Nothing
}

/*************************************************************************/

/**
  * This method writes a single character to the output, storing it in
  * the internal buffer.
  *
  * @param c The <code>char</code> to write, passed as an int.
  */
public void
write(int c)
{
  buf.append((char)(c & 0xFFFF));
}

/*************************************************************************/

/**
  * This method writes <code>len</code> chars from the specified array starting
  * at index <code>offset</code> in that array to this stream by appending
  * the chars to the end of the internal buffer.
  *
  * @param buf The array of chars to write
  * @param offset The index into the array to start writing from
  * @param len The number of chars to write
  */
public void
write(char[] buf, int offset, int len)
{
  this.buf.append(buf, offset, len);
}

/*************************************************************************/

/**
  * This method writes the characters in the specified <code>String</code>
  * to the stream by appending them to the end of the internal buffer.
  *
  * @param str The <code>String</code> to write to the stream.
  */
public void
write(String str)
{
  buf.append(str);
}

/*************************************************************************/

/**
  * This method writes out <code>len</code> characters of the specified
  * <code>String</code> to the stream starting at character position
  * <code>offset</code> into the stream.  This is done by appending the
  * characters to the internal buffer.
  *
  * @param str The <code>String</code> to write characters from
  * @param offset The character position to start writing from
  * @param len The number of characters to write.
  */ 
public void
write(String str, int offset, int len)
{
  char[] tmpbuf = new char[len];
  str.getChars(offset, offset+len, tmpbuf, 0);

  buf.append(tmpbuf, 0, tmpbuf.length);
}

} // class StringWriter

