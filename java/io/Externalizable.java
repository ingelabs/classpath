/*************************************************************************
/* Externalizable.java -- Interface for saving and restoring object data
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
  * This interface provides a way that classes can completely control how
  * the data of their object instances  are written and read to and from 
  * streams.  It has two methods which are used to write the data to a stream 
  * and to read the data from a stream.  The read method must read the data 
  *in exactly the way it was written by the write method. 
  *
  * Note that classes which implement this interface must take into account
  * that all superclass data must also be written to the stream as well.  
  * The class implementing this interface must figure out how to make that
  * happen.
  *
  * This interface can be used to provide object persistence.  When an 
  * object is to be stored externally, the @code{writeExternal} method is
  * called to save state.  When the object is restored, an instance is
  * created using the default no-argument constructor and the 
  * @{readExternal} method is used to restore the state.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Externalizable
{

/**
  * This method restores an object's state by reading in the instance data
  * for the object from the passed in stream.  Note that this stream is not
  * a subclass of @code{InputStream}, but rather is a class that implements
  * the @code{ObjectInput} interface.  That interface provides a mechanism for
  * reading in Java data types from a stream.
  *
  * Note that this method must be compatible with @code{writeExternal}.
  * It must read back the exact same types that were written by that
  * method in the exact order they were written.
  *
  * If this method needs to read back an object instance, then the class
  * for that object must be found and loaded.  If that operation fails,
  * then this method throws a @code{ClassNotFoundException}
  *
  * @param in An @code{ObjectInput} instance for reading in the object state
  *
  * @exception ClassNotFoundException If the class of an object being restored cannot be found
  * @exception IOException If any other error occurs
  */
public abstract void
readExternal(ObjectInput in) throws ClassNotFoundException, IOException;

/*************************************************************************/

/**
  * This method is responsible for writing the instance data of an object
  * to the passed in stream.  Note that this stream is not a subclass of
  * @code{OutputStream}, but rather is a class that implements the
  * @code{ObjectOutput} interface.  That interface provides a number of methods
  * for writing Java data values to a stream.
  *
  * Not that the implementation of this method must be coordinated with
  * the implementation of @{readExternal}.
  *
  * @param out An @{ObjectOutput} instance for writing the object state
  *
  * @exception IOException If an error occurs
  */
public abstract void
writeExternal(ObjectOutput out) throws IOException;

} // interface Externalizable

