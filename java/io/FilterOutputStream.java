/*************************************************************************
/* FilterOutputStream.java -- Parent class for output streams that filter
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.io;

/**
  * This class is the common superclass of output stream classes that 
  * filter the output they write.  These classes typically transform the
  * data in some way prior to writing it out to another underlying
  * <code>OutputStream</code>.  This class simply overrides all the 
  * methods in <code>OutputStream</code> to redirect them to the
  * underlying stream.  Subclasses provide actual filtering.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FilterOutputStream extends OutputStream
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the subordinate <code>OutputStream</code> that this class
  * redirects its method calls to.
  */
protected OutputStream out;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes an instance of <code>FilterOutputStream</code>
  * to write to the specified subordinate <code>OutputStream</code>.
  *
  * @param out The <code>OutputStream</code> to write to
  */
public
FilterOutputStream(OutputStream out)
{
  this.out = out;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method closes the underlying <code>OutputStream</code>.  Any
  * further attempts to write to this stream may throw an exception.
  *
  * @exception IOException If an error occurs
  */
public void
close() throws IOException
{
  out.close();
}

/*************************************************************************/

/**
  * This method attempt to flush all buffered output to be written to the
  * underlying output sink.
  *
  * @exception IOException If an error occurs
  */
public void
flush() throws IOException
{
  out.flush();
}

/*************************************************************************/

/**
  * This method writes a single byte of output to the underlying
  * <code>OutputStream</code>.
  *
  * @param b The byte to write, passed as an int.
  *
  * @exception IOException If an error occurs
  */
public void
write(int b) throws IOException
{
  out.write(b);
}

/*************************************************************************/

/**
  * This method writes all the bytes in the specified array to the underlying
  * <code>OutputStream</code>.  It does this by calling the three parameter
  * version of this method - <code>write(byte[], int, int)</code> in this
  * class instead of writing to the underlying <code>OutputStream</code>
  * directly.  This allows most subclasses to avoid overriding this method.
  *
  * @param buf The byte array to write bytes from
  *
  * @exception IOException If an error occurs
  */
public void
write(byte[] buf) throws IOException
{
  write(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * This method writes <code>len</code> bytes from the array <code>buf</code>
  * starting at index <code>offset</code> to the underlying
  * <code>OutputStream</code>.
  *
  * @param buf The byte array to write bytes from
  * @param offset The index into the array to start writing bytes from
  * @param len The number of bytes to write
  *
  * @exception IOException If an error occurs
  */
public void
write(byte[] buf, int offset, int len) throws IOException
{
  out.write(buf, offset, len);
}

} // class FilterOutputStream

