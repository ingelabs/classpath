/* Transferable.java -- Data transfer source
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of the non-peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


package java.awt.datatransfer;

import java.io.IOException;

/**
  * This interface is implemented by classes that can transfer data.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Transferable
{

/**
  * Returns the data in the specified <code>DataFlavor</code>
  *
  * @param flavor The data flavor to return.
  *
  * @return The data in the appropriate flavor.
  *
  * @exception UnsupportedFlavorException If the flavor is not supported.
  * @exception IOException If the data is not available.
  */
public abstract Object
getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
                                          IOException;

/*************************************************************************/

/**
  * This method returns a list of available data flavors for the 
  * data being transferred.  The array returned will be sorted from most
  * preferred flavor at the beginning to least preferred at the end.
  *
  * @return A list of data flavors for this data.
  */
public abstract DataFlavor[]
getTransferDataFlavors();

/*************************************************************************/

/**
  * Tests whether or not this data can be delivered in the specified
  * data flavor.
  *
  * @param flavor The data flavor to test.
  *
  * @return <code>true</code> if the data flavor is supported,
  * <code>false</code> otherwise.
  */
public abstract boolean
isDataFlavorSupported(DataFlavor flavor);

} // interface Transferable

