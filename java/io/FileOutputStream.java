/*************************************************************************
/* FileOutputStream.java -- Writes to a file on disk.
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
  * This classes allows a stream of data to be written to a disk file or
  * any open <code>FileDescriptor</code>.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FileOutputStream extends OutputStream
{

/*************************************************************************/

/*
 * Class Variables and Initializers
 */

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the native file handle
  */
private int native_fd;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a <code>FileOutputStream</code> object to write
  * to the named file.  The file is created if it does not exist, and
  * the bytes written are written starting at the beginning of the file.
  * <p>
  * Before opening a file, a security check is performed by calling the
  * <code>checkWrite</code> method of the <code>SecurityManager</code> (if
  * one exists) with the name of the file to be opened.  An exception is
  * thrown if writing is not allowed. 
  *
  * @param name The name of the file this stream should write to
  *
  * @exception SecurityException If write access to the file is not allowed
  * @exception IOException If a non-security error occurs
  */
public
FileOutputStream(String name) throws SecurityException, IOException
{
  this(name, false);
}

/*************************************************************************/

/**
  * This method initializes a <code>FileOutputStream</code> object to write
  * to the specified <code>File</code> object.  The file is created if it 
  * does not exist, and the bytes written are written starting at the 
  * beginning of the file.
  * <p>
  * Before opening a file, a security check is performed by calling the
  * <code>checkWrite</code> method of the <code>SecurityManager</code> (if
  * one exists) with the name of the file to be opened.  An exception is
  * thrown if writing is not allowed. 
  *
  * @param file The <code>File</code> object this stream should write to
  *
  * @exception SecurityException If write access to the file is not allowed
  * @exception IOException If a non-security error occurs
  */
public
FileOutputStream(File file) throws SecurityException, IOException
{
  this(file.getPath(), false);
}

/*************************************************************************/

/**
  * This method initializes a <code>FileOutputStream</code> object to write
  * to the named file.  The file is created if it does not exist, and
  * the bytes written are written starting at the beginning of the file if
  * the <code>append</code> argument is <code>false</code> or at the end
  * of the file if the <code>append</code> argument is true.
  * <p>
  * Before opening a file, a security check is performed by calling the
  * <code>checkWrite</code> method of the <code>SecurityManager</code> (if
  * one exists) with the name of the file to be opened.  An exception is
  * thrown if writing is not allowed. 
  *
  * @param name The name of the file this stream should write to
  * @param append <code>true</code> to append bytes to the end of the file, or <code>false</code> to write bytes to the beginning
  *
  * @exception SecurityException If write access to the file is not allowed
  * @exception IOException If a non-security error occurs
  */
public
FileOutputStream(String name, boolean append) throws SecurityException, 
                                                     IOException
{
  SecurityManager sm = System.getSecurityManager();
  if (sm != null)
    {
//      try
//        {
          sm.checkWrite(name);
//        }
//      catch(AccessControlException e)
//        {
//          throw new SecurityException(e.getMessage());
//        }
    }

  native_fd = open((new File(name)).getAbsolutePath(), append);
}

/*************************************************************************/

/**
  * This method initializes a <code>FileOutputStream</code> object to write
  * to the file represented by the specified <code>FileDescriptor</code>
  * object.  This method does not create any underlying disk file or
  * reposition the file pointer of the given descriptor.  It assumes that
  * this descriptor is ready for writing as is.
  * <p>
  * Before opening a file, a security check is performed by calling the
  * <code>checkWrite</code> method of the <code>SecurityManager</code> (if
  * one exists) with the specified <code>FileDescriptor</code> as an argument.
  * An exception is thrown if writing is not allowed. 
  *
  * @param file The <code>FileDescriptor</code> this stream should write to
  *
  * @exception SecurityException If write access to the file is not allowed
  */
public
FileOutputStream(FileDescriptor fd) throws SecurityException
{
  // Hmm, no other exception but this one to throw, but if the descriptor
  // isn't valid, we surely don't have "permission" to write to it.
  if (!fd.valid())
    throw new SecurityException("Invalid FileDescriptor");

  SecurityManager sm = System.getSecurityManager();
  if (sm != null)
    {
//      try
//        {
         // sm.checkWrite(fd);
//        }
//      catch(AccessControlException e)
//        {
//          throw new SecurityException(e.getMessage());
//        }
    }

  native_fd = fd.getNativeFD();
}

/*************************************************************************/

/**
  * This method returns a <code>FileDescriptor</code> object representing
  * the file that is currently being written to
  *
  * @return A <code>FileDescriptor</code> object for this stream
  *
  * @exception IOException If an error occurs
  */
public final FileDescriptor
getFD() throws IOException
{
  return(new FileDescriptor(native_fd));
}

/*************************************************************************/

/**
  * This method writes a single byte of data to the file.  
  *
  * @param b The byte of data to write, passed as an <code>int</code>
  *
  * @exception IOException If an error occurs
  */
public synchronized void
write(int b) throws IOException
{
  byte[] buf = new byte[1];

  buf[0] = (byte)(b & 0xFF);
  writeInternal(native_fd, buf, 0, buf.length);
}

/*************************************************************************/

/**
  * This method writes all the bytes in the specified array to the
  * file.
  *
  * @param buf The array of bytes to write to the file
  *
  * @exception IOException If an error occurs
  */
public synchronized void
write(byte[] buf) throws IOException
{
  writeInternal(native_fd, buf, 0, buf.length);
}

/*************************************************************************/

/**
  * This method writes <code>len</code> bytes from the byte array 
  * <code>buf</code> to the file starting at index <code>offset</code>.
  *
  * @param buf The array of bytes to write to the file
  * @param offset The offset into the array to start writing bytes from
  * @param len The number of bytes to write to the file
  *
  * @exception IOException If an error occurs
  */
public synchronized void
write(byte[] buf, int offset, int len) throws IOException
{
  writeInternal(native_fd, buf, 0, len);
}

/*************************************************************************/

/**
  * This internal method does the actual writing of bytes to the underlying
  * file
  *
  * @param native_fd The native file descriptor
  * @param buf The array of bytes to write to the file
  * @param offset The offset into the array to start writing bytes from
  * @param len The number of bytes to write to the file
  *
  * @exception IOException If an error occurs
  */
private synchronized native void
writeInternal(int native_fd, byte[] buf, int offset, int len) throws IOException;

/*************************************************************************/

/**
  * This internal method opens the specfied file for writing.  If the
  * <code>append</code> variable is <code>true</code> the file is positioned
  * for writing at the end, otherwise it is positioned for writing at the
  * beginning.
  *
  * @param name The name of the file to open
  * @param append  <code>true</code> to write starting at the end of the file, <code>false</code> to start writing at the beginning
  *
  * @return A native file descriptor for this file
  *
  * @exception IOException If an error occurs
  */
private synchronized native int
open(String name, boolean append) throws IOException;

/*************************************************************************/

/**
  * This method closes the underlying file.  Any further attempts to
  * write to this stream will likely generate an exception since the
  * file is closed.
  *
  * @exception IOException If an error occurs
  */
public synchronized void
close() throws IOException
{
  closeInternal(native_fd);
  native_fd = -1;
}

/*************************************************************************/

/**
  * This internal method closes the actual file
  *
  * @param native_fd The native file descriptor to close
  *
  * @exception IOException If an error occurs
  */
public synchronized native void
closeInternal(int native_fd) throws IOException;

/*************************************************************************/

/**
  * This method closes the stream when this object is being garbage
  * collected.
  *
  * @exception IOException If an error occurs (ignored by the Java runtime)
  */
public void
finalize() throws IOException
{
  close();
}

} // class FileOutputStream

