/*************************************************************************
/* ObjectOutput.java -- Interface for writing objects to a stream
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
  * This interface extends <code>DataOutput</code> to provide the additional
  * facility of writing object instances to a stream.  It also adds some
  * additional methods to make the interface more <code>OutputStream</code> like.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract interface ObjectOutput extends DataOutput
{


/**
  * This method writes the specified byte to the output stream.
  *
  * @param b The byte to write.
  *
  * @exception IOException If an error occurs.
  */
public abstract void
write(int b) throws IOException;

/*************************************************************************/

/**
  * This method writes all the bytes in the specified byte array to the
  * output stream.
  *
  * @param buf The array of bytes to write.
  * 
  * @exception IOException If an error occurs.
  */
public abstract void
write(byte[] buf) throws IOException;

/*************************************************************************/

/**
  * This method writes <code>len</code> bytes from the specified array
  * starting at index <code>offset</code> into that array.
  *
  * @param buf The byte array to write from.
  * @param offset The index into the byte array to start writing from.
  * @param len The number of bytes to write.
  *
  * @exception IOException If an error occurs.
  */
public abstract void
write(byte[] buf, int offset, int len) throws IOException;

/*************************************************************************/

/**
  * This method writes a object instance to a stream.  The format of the
  * data written is determined by the actual implementation of this method
  *
  * @param obj The object to write
  *
  * @exception IOException If an error occurs
  */
public abstract void
writeObject(Object obj) throws IOException;

/*************************************************************************/

/**
  * This method causes any buffered data to be flushed out to the underlying
  * stream
  *
  * @exception IOException If an error occurs
  */
public abstract void
flush() throws IOException;

/*************************************************************************/

/**
  * This method closes the underlying stream.
  *
  * @exception IOException If an error occurs
  */
public abstract void
close() throws IOException;

} // interface ObjectOutput

