/* StringSelection.java -- Clipboard handler for text.
   Copyright (C) 1999 Free Software Foundation, Inc.

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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.awt.datatransfer;

import java.io.StringBufferInputStream;
import java.io.IOException;

/**
  * This class transfers a string as plain text using the clipboard.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class StringSelection implements Transferable, ClipboardOwner
{

/*
 * Class Variables
 */

// List of flavors we support
public static final DataFlavor[] supported_flavors 
   = { DataFlavor.plainTextFlavor };

/*************************************************************************/

/*
 * Instance Variables
 */

// This is the data to transfer
private String data;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Transfer the specfied string as text.
  */
public
StringSelection(String data)
{
  this.data = data;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns a list of supported data flavors.
  *
  * @return A list of supported data flavors.
  */
public DataFlavor[]
getTransferDataFlavors()
{
  return(supported_flavors);
}

/*************************************************************************/

/**
  * Tests whether or not the specified data flavor is supported.
  *
  * @param flavor The data flavor to test.
  *
  * @return <code>true</code> if the data flavor is supported,
  * <code>false</code> otherwise.
  */
public boolean
isDataFlavorSupported(DataFlavor flavor)
{
  for (int i = 0; i < supported_flavors.length; i++)
    if (supported_flavors[i].equals(flavor))
       return(true);

  return(false);
}

/*************************************************************************/

/**
  * This method returns the data in the requested format.
  *
  * @param flavor The desired data flavor.
  *
  * @return The transferred data.
  *
  * @exception UnsupportedFlavorException If the specified flavor is not
  * supported.
  * @exception IOException If any other error occurs.
  */
public Object
getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
                                          IOException
{
  if (!isDataFlavorSupported(flavor))
    throw new UnsupportedFlavorException(flavor);

  return(new StringBufferInputStream(data));
}

/*************************************************************************/

/**
  * Called when ownership of the clipboard object is lost.
  *
  * @param clipboard The affected clipboard.
  * @param contents The clipboard contents.
  */
public void
lostOwnership(Clipboard clipboard, Transferable contents)
{
  // FIXME: What does this do?
}

} // class StringSelection 

