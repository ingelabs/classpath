/*************************************************************************
/* FileDescriptor.java -- Opaque file handle class
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
  * This is a @code{FileDescriptor} object representing the standard
  * input stream.
  */
public static final FileDescriptor in = new FileDescriptor(0);

/**
  * This is a @code{FileDescriptor} object representing the standard
  * output stream.
  */
public static final FileDescriptor out = new FileDescriptor(1);

/**
  * This is a @code{FileDescriptor} object representing the standard
  * error stream.
  */
public static final FileDescriptor err = new FileDescriptor(2);

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
  * This method is used to initialize a @code{FileDescriptor} that will
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
  * the underlying storage medium associated with this @code{FileDescriptor}
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
  * @return @code{true} if this object represents a valid native file handle, @code{false} otherwise
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
  * @return @code{true} if this object represents a valid native file handle, @code{false} otherwise
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

