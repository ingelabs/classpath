/* FlavorMap.java -- Maps between flavor names and MIME types.
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

import java.util.Map;

/**
  * This interface maps between data flavor names and MIME types.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface FlavorMap
{

/**
  * Maps the specified <code>DataFlavor</code> objects to the native
  * data type name.  The returned <code>Map</code> has keys that are
  * the data flavors and values that are strings.  The returned map
  * may be modified.  This can be useful for implementing nested mappings.
  *
  * @param flavors An array of data flavors to map.
  *
  * @return A <code>Map</code> of native data types.
  */
public abstract Map
getNativesForFlavors(DataFlavor[] flavors);

/*************************************************************************/

/**
  * Maps the specified native type names to <code>DataFlavor</code>'s.
  * The returned <code>Map</code> has keys that are strings and values
  * that are <code>DataFlavor</code>'s.  The returned map may be
  * modified.  This can be useful for implementing nested mappings.
  *
  * @param natives An array of native types to map.
  *
  * @return A <code>Map</code> of data flavors.
  */
public abstract Map
getFlavorsForNatives(String[] natives);

} // interface FlavorMap

