/*************************************************************************
/* FilterWriter.java -- Parent class for output streams that filter
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
  * This class is the common superclass of output character stream classes 
  * that filter the output they write.  These classes typically transform the
  * data in some way prior to writing it out to another underlying
  * <code>Writer</code>.  This class simply overrides all the 
  * methods in <code>Writer</code> to redirect them to the
  * underlying stream.  Subclasses provide actual filtering.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class FilterWriter extends Writer
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the subordinate <code>Writer</code> that this class
  * redirects its method calls to.
  */
protected Writer out;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes an instance of <code>FilterWriter</code>
  * to write to the specified subordinate <code>Writer</code>.
  *
  * @param out The <code>Writer</code> to write to
  */
protected
FilterWriter(Writer out)
{
  this.out = out;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method closes the underlying <code>Writer</code>.  Any
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
  * This method writes a single char of output to the underlying
  * <code>Writer</code>.
  *
  * @param b The char to write, passed as an int.
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
  * This method writes <code>len</code> chars from the array <code>buf</code>
  * starting at index <code>offset</code> to the underlying
  * <code>Writer</code>.
  *
  * @param buf The char array to write chars from
  * @param offset The index into the array to start writing chars from
  * @param len The number of chars to write
  *
  * @exception IOException If an error occurs
  */
public void
write(char[] buf, int offset, int len) throws IOException
{
  out.write(buf, offset, len);
}

/*************************************************************************/

/**
  * This method writes <code>len</code> chars from the <code>String</code>
  * starting at position <code>offset</code>.
  *
  * @param str The <code>String</code> that is to be written
  * @param offset The character offset into the <code>String</code> to start writing from
  * @param len The number of chars to write
  *
  * @exception IOException If an error occurs
  */
public void
write(String str, int offset, int len) throws IOException
{
  out.write(str, offset, len);
}

} // class FilterWriter

