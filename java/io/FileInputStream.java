/*************************************************************************
/* FileInputStream.java -- An input stream that reads from disk files.
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
  * This class is a stream that reads its bytes from a file. 
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FileInputStream extends InputStream
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
  * This is the native file handle for the file this stream is reading from
  */
private int native_fd;

/**
  * This variable keeps track of the total number of bytes read from
  * the stream
  */
long grand_total_read;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a <code>FileInputStream</code> to read from the
  * specified named file.  A security check is first made to determine
  * whether or not access to this file is allowed.  This is done by
  * calling the <code>checkRead()</code> method of the <code>SecurityManager</code>
  * (if one exists) with the name of this file.  An exception is thrown
  * if reading is not allowed.  If the file does not exist, an exception
  * is also thrown.
  *
  * @param name The name of the file this stream should read from
  *
  * @exception SecurityException If read access to the file is not allowed
  * @exception FileNotFoundException If the file does not exist.
  */
public
FileInputStream(String name) throws SecurityException, FileNotFoundException
{
  this(new File(name));
}

/*************************************************************************/

/**
  * This method initializes a <code>FileInputStream</code> to read from the
  * specified <code>File</code> object.  A security check is first made to determine
  * whether or not access to this file is allowed.  This is done by
  * calling the <code>checkRead()</code> method of the <code>SecurityManager</code>
  * (if one exists) with the name of this file.  An exception is thrown
  * if reading is not allowed.  If the file does not exist, an exception
  * is also thrown.
  *
  * @param file The <code>File</code> object this stream should read from
  *
  * @exception SecurityException If read access to the file is not allowed
  * @exception FileNotFoundException If the file does not exist.
  */
public
FileInputStream(File file) throws SecurityException, FileNotFoundException
{
  // Talk about the lazy man's way out.  The File.exists() method does
  // a security check so don't repeat it here.
  if (!file.exists())
    throw new FileNotFoundException(file.getName());

  native_fd = open(file.getPath());
}

/*************************************************************************/

/**
  * This method initializes a <code>FileInputStream</code> to read from the
  * specified <code>FileDescriptor</code> object.  A security check is first made to 
  * determine whether or not access to this file is allowed.  This is done by
  * calling the <code>checkRead()</code> method of the <code>SecurityManager</code>
  * (if one exists) with the specified <code>FileDescriptor</code>  An exception is 
  * thrown if reading is not allowed.
  *
  * @param fd The <code>FileDescriptor</code> object this stream should read from
  *
  * @exception SecurityException If read access to the file is not allowed
  */
public
FileInputStream(FileDescriptor fd) throws SecurityException
{
  // Hmmm, no other exception but this one to throw, but if the descriptor
  // isn't valid, we surely don't have "permission" to read from it.
  if (!fd.valid())
    throw new SecurityException("Invalid FileDescriptor");

  SecurityManager sm = System.getSecurityManager();
  if (sm != null)
    {
//      try
//        {
          //sm.checkRead(fd);
          throw new SecurityException("ffo");
//        }
//      catch (AccessControlException e)
//        {
//          throw new SecurityException(e.getMessage());
//        }
    }

  native_fd = fd.getNativeFD();
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns a <code>FileDescriptor</code> object representing the
  * underlying native file handle of the file this stream is reading
  * from
  *
  * @return A <code>FileDescriptor</code> for this stream
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
  * This method returns the number of bytes that can be read from this
  * stream before a read can block.  A return of 0 indicates that blocking
  * might (or might not) occur on the very next read attempt.
  * <p>
  * This method returns the number of unread bytes remaining in the file if
  * the descriptor being read from is an actual file.  If this method is
  * reading from a ''special'' file such a the standard input, this method
  * will return the appropriate value for the stream being read.
  * <p>
  * Be aware that reads on plain files that do not reside locally might
  * possibly block even if this method says they should not.  For example,
  * a remote server might crash, preventing an NFS mounted file from being
  * read.
  *
  * @return The number of bytes that can be read before blocking could occur
  *
  * @exception IOException If an error occurs
  */
public int
available() throws IOException
{
  int retval = (int)(getFileLength(native_fd) - grand_total_read);

  if (retval <= 0)
    return(0);

  return(retval);
}

/*************************************************************************/

/**
  * This private method returns the length of the file being read
  *
  * @param native_fd The native file handle
  *
  * @exception IOException If an error occurs
  */
private native long
getFileLength(int native_fd) throws IOException;

/*************************************************************************/

/**
  * This method skips the specified number of bytes in the stream.  It
  * returns the actual number of bytes skipped, which may be less than the
  * requested amount.
  * <p>
  * This method reads and discards bytes into a 256 byte array until the
  * specified number of bytes were skipped or until either the end of stream
  * is reached or a read attempt returns a short count.  Subclasses can
  * override this metho to provide a more efficient implementation where
  * one exists.
  *
  * @param num_bytes The requested number of bytes to skip
  *
  * @return The actual number of bytes skipped.
  *
  * @exception IOException If an error occurs
  */
public synchronized long
skip(long num_bytes) throws IOException
{
  if (num_bytes <= 0)
    return(0);

  if (num_bytes > available())
    num_bytes = available();

  long bytes_skipped = skipInternal(native_fd, num_bytes);
  grand_total_read += bytes_skipped;

  return(bytes_skipped);
}

/*************************************************************************/

/**
  * This private native method does the actual skipping of values by
  * doing an lseek() on the native file descriptor.
  *
  * @param native_fd The native file handle
  * @param num_bytes The requested number of bytes to skip
  *
  * @return The actual number of bytes skipped
  *
  * @exception If an error occurs
  */
private native long
skipInternal(int native_fd, long num_bytes) throws IOException;

/*************************************************************************/

/**
  * This method reads an unsigned byte from the input stream and returns it
  * as an int in the range of 0-255.  This method also will return -1 if
  * the end of the stream has been reached.
  * <p>
  * This method will block until the byte can be read.
  *
  * @return The byte read or -1 if end of stream
  *
  * @exception IOException If an error occurs
  */
public synchronized int
read() throws IOException
{
  byte[] buf = new byte[1];

  int bytes_read = readInternal(native_fd, buf, 0, buf.length);
  if (bytes_read == -1)
    return(-1);

  ++grand_total_read;

  return((buf[0] & 0xFF));
}

/*************************************************************************/

/**
  * This method reads bytes from a stream and stores them into a caller
  * supplied buffer.  This method attempts to completely fill the buffer,
  * but can return before doing so.  The actual number of bytes read is
  * returned as an int.  A -1 is returned to indicate the end of the stream.
  * <p>
  * This method will block until some data can be read.
  * <p>
  * This method operates by calling an overloaded read method like so:
  * <code>read(buf, 0, buf.length)</code>
  *
  * @param buf The buffer into which the bytes read will be stored.
  *
  * @return The number of bytes read or -1 if end of stream.
  *
  * @exception IOException If an error occurs.
  */
public int
read(byte[] buf) throws IOException
{
  return(read(buf, 0, buf.length));
}

/*************************************************************************/

/**
  * This method read bytes from a stream and stores them into a caller
  * supplied buffer.  It starts storing the data at index <code>offset</code> into
  * the buffer and attempts to read <code>len</code> bytes.  This method can
  * return before reading the number of bytes requested.  The actual number
  * of bytes read is returned as an int.  A -1 is returned to indicate the
  * end of the stream.
  * <p>
  * This method will block until some data can be read.
  *
  * @param buf The array into which the bytes read should be stored
  * @param offset The offset into the array to start storing bytes
  * @param len The requested number of bytes to read
  *
  * @return The actual number of bytes read, or -1 if end of stream.
  *
  * @exception IOException If an error occurs.
  */
public synchronized int
read(byte[] buf, int offset, int len) throws IOException
{
  if (len == 0)
    return(0);

  int bytes_read = readInternal(native_fd, buf, offset, len);
  if (bytes_read == -1)
    return(-1);

  grand_total_read += bytes_read;
  return(bytes_read);
}

/*************************************************************************/

/**
  * This private native method reads the actual requested bytes from the
  * file into the caller supplied buffer.
  *
  * @param native_fd The native file handle
  * @param buf The array into which the bytes read should be stored
  * @param offset The offset into the array to start storing bytes
  * @param len The requested number of bytes to read
  *
  * @return The actual number of bytes read, or -1 if end of stream.
  *
  * @exception IOException If an error occurs.
  */
private native int
readInternal(int native_fd,  byte[] buf, int offset, int len) throws IOException;

/*************************************************************************/

/**
  * This method opens the underlying file for read only access
  *
  * @param name The name of the file to open
  *
  * @exception IOException If an error occurs
  */
private native int
open(String name) throws FileNotFoundException;

/*************************************************************************/

/**
  * This method closes the stream.  Any futher attempts to read from the
  * stream will likely generate an IOException since the underlying file
  * will be closed.
  *
  * @exception IOException If an error occurs.
  */
public synchronized void
close() throws IOException
{
  closeInternal(native_fd);
  native_fd = -1; // Ensures we don't pick up a stray file descriptor later on
}

/*************************************************************************/

/**
  * This is the method that actually closes the native file
  *
  * @param native_fd The native file handle of the file to close
  */
private native void
closeInternal(int native_fd);

/*************************************************************************/

/**
  * This method ensures that the underlying file is closed when this object
  * is garbage collected.  Since there is a system dependent limit on how
  * many files a single process can have open, it is a good idea to close
  * streams when they are no longer needed rather than waiting for garbage
  * collectio to automatically close them.
  *
  * @exception IOException If an error occurs
  */
protected void
finalize() throws IOException
{
  close();
}

} // class FileInputStream

