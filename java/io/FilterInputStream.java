/*************************************************************************
/* FilterInputStream.java -- Base class for classes that filter input
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
  * This is the common superclass of all standard classes that filter 
  * input.  It acts as a layer on top of an underlying @code{InputStream}
  * and simply redirects calls made to it to the subordinate InputStream
  * instead.  Subclasses of this class perform additional filtering
  * functions in addition to simply redirecting the call.
  *
  * This class is not abstract.  However, since it only redirects calls
  * to a subordinate @code{InputStream} without adding any functionality on top
  * of it, this class should not be used directly.  Instead, various
  * subclasses of this class should be used.  This is enforced with a
  * protected constructor.  Do not try to hack around it.
  *
  * When creating a subclass of @code{FilterInputStream}, override the
  * appropriate methods to implement the desired filtering.  However, note
  * that the @code{read(byte[])} method does not need to be overridden
  * as this class redirects calls to that method to 
  * @code{read(byte[], int, int)} instead of to the subordinate
  * @code{InputStream} @code{read(byte[])} method.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FilterInputStream extends InputStream
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the subordinate @code{InputStream} to which method calls
  * are redirected
  */
protected InputStream in;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Create a @code{FilterInputStream} with the specified subordinate
  * @code{InputStream}.
  *
  * @param in The subordinate @code{InputStream}
  */
protected 
FilterInputStream(InputStream in)
{
  this.in = in;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Calls the @code{in.mark(int)} method.
  *
  * @param readlimit The parameter passed to @code{in.mark(int)}
  */
public void
mark(int readlimit)
{
  in.mark(readlimit);
}

/*************************************************************************/

/**
  * Calls the @code{in.markSupported()} method.
  *
  * @return @code{true} if mark/reset is supported, @code{false} otherwise
  */
public boolean
markSupported()
{
  return(in.markSupported());
}

/*************************************************************************/

/**
  * Calls the @code{in.reset()} method.
  *
  * @exception IOException If an error occurs
  */
public void
reset() throws IOException
{
  in.reset();
}

/*************************************************************************/

/**
  * Calls the @code{in.available()} method.
  *
  * @return The value returned from @code{in.available()}
  *
  * @exception IOException If an error occurs
  */
public int
available() throws IOException
{
  return(in.available());
}

/*************************************************************************/

/**
  * Calls the @code{in.skip(long)} method
  *
  * @param The requested number of bytes to skip. 
  *
  * @return The value returned from @code{in.skip(long)}
  *
  * @exception IOException If an error occurs
  */
public long
skip(long num_bytes) throws IOException
{
  return(in.skip(num_bytes));
}

/*************************************************************************/

/**
  * Calls the @code{in.read()} method
  *
  * @return The value returned from @code{in.read()}
  *
  * @exception IOException If an error occurs
  */
public int
read() throws IOException
{
  return(in.read());
}

/*************************************************************************/

/**
  * Calls the @code{read(byte[], int, int)} overloaded method.  Note that 
  * this method does not redirect its call directly to a corresponding
  * method in @code{in}.  This allows subclasses to override only the
  * three argument version of @code{read}.
  *
  * @param buf The buffer to read bytes into
  *
  * @return The value retured from @code{in.read(byte[], int, int)}
  *
  * @exception IOException If an error occurs
  */
public int
read(byte[] buf) throws IOException
{
  return(read(buf, 0, buf.length));
}

/*************************************************************************/

/**
  * Calls the @code{in.read(byte[], int, int)} method.
  *
  * @param buf The buffer to read bytes into
  * @param offset The index into the buffer to start storing bytes
  * @param len The maximum number of bytes to read.
  *
  * @return The value retured from @code{in.read(byte[], int, int)}
  *
  * @exception IOException If an error occurs
  */
public int
read(byte[] buf, int offset, int len) throws IOException
{
  return(in.read(buf, offset, len));
}

} // class FilterInputStream

