/*************************************************************************
/* OutputStream.java -- Base class for byte output streams
/*
/* Copyright (c) 1998 by Aaron M. Renn (arenn@urbanophile.com)
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
/* You should have received a copy of the GNU Library General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.io;

/**
  * This abstract class forms the base of the hierarchy of classes that 
  * write output as a stream of bytes.  It provides a common set of methods
  * for writing bytes to stream.  Subclasses implement and/or extend these
  * methods to write bytes in a particular manner or to a particular 
  * destination such as a file on disk or network connection.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class OutputStream
{

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This is the default no-argument constructor for this class.  This method
  * does nothing in this class.
  */
public
OutputStream()
{
  ; // Do Nothing
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method forces any data that may have been buffered to be written
  * to the underlying output device.  Please note that the host environment
  * might perform its own buffering unbeknowst to Java.  In that case, a
  * write made (for example, to a disk drive) might be cached in OS
  * buffers instead of actually being written to disk.
  *
  * This method in this class does nothing.
  *
  * @exception IOException If an error occurs
  */
public void
flush() throws IOException
{
  ;
}

/*************************************************************************/

/**
  * This method closes the stream.  Any internal or native resources associated
  * with this stream are freed.  Any subsequent attempt to access the stream
  * might throw an exception.
  *
  * This method in this class does nothing.
  *
  * @exception IOException If an error occurs
  */
public void
close() throws IOException
{
  ;
}

/*************************************************************************/

/**
  * This method writes a single byte to the output stream.  The byte written
  * is the low eight bits of the @code{int} passed and a argument.
  *
  * Subclasses must provide an implementation of this abstract method
  *
  * @param b The byte to be written to the output stream, passed as the low eight bits of an <code>int</code>
  *
  * @exception IOException If an error occurs
  */
public abstract void
write(int b) throws IOException;

/*************************************************************************/

/**
  * This method all the writes bytes from the passed array to the output stream.
  * This method is equivalent to <code>write(buf, 0, buf.length)</code> which
  * is exactly how it is implemented in this class.
  *
  * @param buf The array of bytes to write
  *
  * @exception If an error occurs
  */
public void
write(byte[] buf) throws IOException
{
  write(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * This method writes <code>len</code> bytes from the specified array
  * <code>buf</code> starting at index <code>offset</code> into the array.
  *
  * This method in this class calls the single byte <code>write()</code>
  * method in a loop until att bytes have been written.  Subclasses should
  * override this method if possible in order to provide a more efficent
  * implementation.
  *
  * @param buf The array of bytes to write from
  * @param offset The index into the array to start writing from
  * @param len The number of bytes to write
  * 
  * @exception If an error occurs
  */
public void
write(byte[] buf, int offset, int len) throws IOException
{
  for (int i = offset; i < (len + offset); i++)
    write(buf[i]);
}

} // class OutputStream

