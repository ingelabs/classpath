/* FileDescriptor.java -- Opaque file handle class
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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package java.io;

import gnu.classpath.Configuration;

/**
  * This class represents an opaque file handle as a Java class.  It should
  * be used only to pass to other methods that expect an object of this
  * type.  No system specific information can be obtained from this object.
  *
  * @version 0.1
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public final class FileDescriptor
{

/*************************************************************************/

/*
 * Class Variables and Initializers
 */

/**
  * This is a <code>FileDescriptor</code> object representing the standard
  * input stream.
  */
public static final FileDescriptor in = new FileDescriptor(0);

/**
  * This is a <code>FileDescriptor</code> object representing the standard
  * output stream.
  */
public static final FileDescriptor out = new FileDescriptor(1);

/**
  * This is a <code>FileDescriptor</code> object representing the standard
  * error stream.
  */
public static final FileDescriptor err = new FileDescriptor(2);

  static
  {
    if (Configuration.INIT_LOAD_LIBRARY)
      {
        System.loadLibrary ("javaio");
      }
  }

/*************************************************************************/

/**
  * Instance Variables
  */

/**
  * This is the actual native file descriptor value
  */
private int native_fd;

/*************************************************************************/

/*
 * Class Methods
 */
private static FileDescriptor
getFileDescriptor(int native_fd)
{
  return(new FileDescriptor(native_fd));
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method is used to initialize an invalid FileDescriptor object.
  */
public
FileDescriptor()
{
  ;
}

/*************************************************************************/

/**
  * This method is used to initialize a <code>FileDescriptor</code> that will
  * represent the specified native file handle. 
  *
  * @param native_fd The native file handle this object should represent
  */
FileDescriptor(int native_fd)
{
  this.native_fd = native_fd;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method forces all data that has not yet been physically written to
  * the underlying storage medium associated with this <code>FileDescriptor</code>
  * to be written out.  This method will not return until all data has
  * been fully written to the underlying device.  If the device does not
  * support this functionality or if an error occurs, then an exception
  * will be thrown.
  */
public void
sync() throws SyncFailedException
{
  syncInternal(native_fd);
}

/*************************************************************************/

/**
  * This is the native method where the actual sync'ing of data to disk
  * is performed.
  *
  * @param native_fd The native file handle
  *
  * @exception SyncFailedException If an error occurs or sync is not supported
  */
native void
syncInternal(int native_fd);

/*************************************************************************/

/**
  * This methods tests whether or not this object represents a valid open
  * native file handle.
  *
  * @return <code>true</code> if this object represents a valid native file handle, <code>false</code> otherwise
  */
public boolean
valid()
{
  return(validInternal(native_fd));
}

/*************************************************************************/

/**
  * This is the native method which actually tests whether or not this
  * object represents a valid native file handle.
  *
  * @param native_fd The native file handle
  *
  * @return <code>true</code> if this object represents a valid native file handle, <code>false</code> otherwise
  */
native boolean
validInternal(int native_fd);

/*************************************************************************/

/**
  * This method eturns the native file handle represented by this object
  *
  * @return The native file handle this object represents
  */
int
getNativeFD()
{
  return(native_fd);
}

/*************************************************************************/

/**
  * This method sets the native file descriptor this object represents to 
  * the specified value.
  * 
  * @param The native file handle this object should represent.
  */
void
setNativeFD(int native_fd)
{
  this.native_fd = native_fd;
}

} // class FileDescriptor

