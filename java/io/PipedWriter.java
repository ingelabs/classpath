/*************************************************************************
/* PipedWriter.java -- Write portion of piped streams.
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
  * This class writes its chars to a <code>PipedReader</code> to 
  * which it is connected.
  * <p>
  * It is highly recommended that a <code>PipedWriter</code> and its
  * connected <code>PipedReader</code> be in different threads.  If 
  * they are in the same thread, read and write operations could deadlock
  * the thread.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class PipedWriter extends Writer
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the <code>PipedReader</code> to which this object
  * is connected.
  */
private PipedReader snk;

/**
  * This flag indicates whether or not this stream has ever been
  * connected to an input stream
  */
private boolean ever_connected;

/**
  * This flag indicates whether the <code>close</code> method has ever
  * been called for this stream.
  */
private boolean closed;

/*************************************************************************/

/**
  * This method initializes a new <code>PipedWriter</code> instance.
  * This constructor creates an unconnected object.  It must be connected
  * to a <code>PipedReader</code> object using the <code>connect</code>
  * method prior to writing any data or an exception will be thrown.
  */
public
PipedWriter()
{
  ; // Do Nothing
}

/*************************************************************************/

/**
  * This method initializes a new <code>PipedWriter</code> instance
  * to write to the specified <code>PipedReader</code>.  This stream
  * is then ready for writing.
  *
  * @param snk The <code>PipedReader</code> to connect this stream to.
  *
  * @exception IOException If an error occurs
  */
public
PipedWriter(PipedReader snk) throws IOException
{
  connect(snk);
} 

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method connects this object to the specified 
  * <code>PipedReader</code> object.  This stream will then be ready 
  * for writing.  If this stream is already connected or has been 
  * previously closed, then an exception is thrown.
  *
  * @param snk The <code>PipedReader</code> to connect this stream to
  *
  * @exception IOException If an error occurs
  */
public void
connect(PipedReader snk) throws IOException
{
  if (snk == this.snk)
    return;

  if (ever_connected)
    throw new IOException("Already connected");

  if (closed)
    throw new IOException("Stream is closed and cannot be reopened");

  synchronized (lock) {

  this.snk = snk;
  ever_connected = true;
  snk.src = this;

  snk.connect(this);

  } // synchronized
}

/*************************************************************************/

/**
  * This method closes this stream so that no more data can be written
  * to it. Any further attempts to write to this stream may throw an
  * exception
  *
  * @exception IOException If an error occurs
  */
public void
close() throws IOException
{
  synchronized (lock) {

  closed = true;
  snk.close();
  notifyAll();

  } // synchronized
}

/*************************************************************************/

/**
  * This method writes <code>len</code> chars of data from the char array
  * <code>buf</code> starting at index <code>offset</code> in the array
  * to the stream.  Note that this method will block if the  
  * <code>PipedReader</code> to which this object is connected has
  * a buffer that cannot hold all of the chars to be written.
  *
  * @param buf The array containing chars to write to thes stream.
  * @param offset The index into the array to start writing chars from.
  * @param len The number of chars to write.
  *
  * @exception IOException If an error occurs
  */
public void
write(char[] buf, int offset, int len) throws IOException
{
  snk.write(buf, 0, len);
}

/*************************************************************************/

/**
  * This method flushes any unwritten chars to the underlying output
  * sink.  This method does nothing in this class because this class does
  * not buffer any chars.
  *
  * @exception IOException If an error occurs
  */
public void
flush() throws IOException
{
  ; // Do Nothing
}

} // class PipedWriter

