/*
  FileURLConnection.java -- URLConnection class for "file" protocol
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
  exception statement from your version.
*/
package gnu.java.net.protocol.file;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.NoSuchElementException;
import java.io.*;

/**
  * This subclass of java.net.URLConnection models a URLConnection via
  * the "file" protocol.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  * @author Nic Ferrier (nferrier@tapsellferrier.co.uk)
  */
public class FileURLConnection extends java.net.URLConnection
{

  /**
   * This is a File object for this connection
   */
  private java.io.File file;

  /**
   * InputStream if we are reading from the file
   */
  private java.io.FileInputStream in_stream;

  /**
   * OutputStream if we are writing to the file
   */
  private java.io.FileOutputStream out_stream;
  
  /**
   * Calls superclass constructor to initialize.
   */
  protected FileURLConnection(java.net.URL url)
  {
    super(url);
    
    /* Set up some variables */
    doOutput = false;
  }
  
  /*************************************************************************/
  
  /*
   * Instance Methods
   */
  
  /**
   * "Connects" to the file by opening it.
   */
  public void connect() throws java.io.IOException
  {
    if (connected)
      return;
    file = new java.io.File(getURL().getFile());
    if (!file.exists())
      throw new java.io.FileNotFoundException(file.getPath());
    connected = true;
  }
  
  /**
   * Opens the file for reading and returns a stream for it.
   *
   * @return An InputStream for this connection.
   *
   * @exception IOException If an error occurs
   */
  public java.io.InputStream getInputStream ()
    throws java.io.IOException
  {
    if (!connected)
      connect();
    in_stream = new java.io.FileInputStream(file);
    return (in_stream);
  }

  /**
   * Opens the file for writing and returns a stream for it.
   *
   * @return An OutputStream for this connection.
   *
   * @exception IOException If an error occurs.
   */
  public java.io.OutputStream getOutputStream ()
    throws java.io.IOException
  {
    if (!connected)
      connect();
    out_stream = new java.io.FileOutputStream(file);
    return (out_stream);
  }

  /** Get the last modified time of the resource.
   *
   * @return the time since epoch that the resource was modified.
   */
  public long getLastModified ()
  {
    try
      {
	if (! connected)
	  connect();
	return file.lastModified();
      }
    catch (IOException e)
      {
	return -1;
      }
  }

  /** Get the length of content.
   *
   * @return the length of the content.
   */
  public int getContentLength ()
  {
    try
      {
	if (! connected)
	  connect();
	return (int) file.length();
      }
    catch (IOException e)
      {
	return -1;
      }
  }


  // These are GNU only implementation methods.

  /** Does the resource pointed to actually exist?
   */
  public final boolean exists ()
  {
    if (file == null)
      return false;
    return file.exists();
  }

  /** Is the resource pointed to a directory?
   */
  public final boolean isDirectory ()
  {
    return file.isDirectory();
  }
  
  /** Get a listing of the directory, if it is a directory.
   *
   * @return a set which can supply an iteration of the
   * contents of the directory.
   * @throws IllegalStateException if this is not pointing
   * to a directory.
   */
  public Set getListing ()
  {
    if (! file.isDirectory())
      throw new IllegalStateException("this is not a directory");
    final File[] directoryList = file.listFiles();
    return new AbstractSet()
      {
	File[] dirList = directoryList;

	public int size ()
	{
	  return dirList.length;
	}

	public Iterator iterator ()
	{
	  return new Iterator()
	    {
	      int index = 0;

	      public boolean hasNext ()
	      {
		return index < dirList.length;
	      }

	      public Object next ()
	      {
		try
		  {
		    String value = dirList[index++].getName();
		    return value;
		  }
		catch (ArrayIndexOutOfBoundsException e)
		  {
		    throw new NoSuchElementException("no more content");
		  }
	      }

	      public void remove ()
	      {
		try
		  {
		    File[] newDirList = new File[dirList.length - 1];
		    int realIndex = index - 1;
		    if (realIndex < 1)
		      {
			System.arraycopy(dirList, 1, newDirList, 0, dirList.length - 1);
			index--;
		      }
		    else
		      {
			System.arraycopy(dirList, 0, newDirList, 0, realIndex);
			if (index < dirList.length - 1)
			  System.arraycopy(dirList, index,
					   newDirList, realIndex, dirList.length - realIndex);
		      }
		    dirList = newDirList;
		  }
		catch (ArrayIndexOutOfBoundsException e)
		  {
		    throw new NoSuchElementException("no more content");
		  }
	      }
	    };
	}
      };
  }
} // class FileURLConnection

