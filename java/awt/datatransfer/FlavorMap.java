/* FlavorMap.java -- Maps between flavor names and MIME types.
   Copyright (C) 1999, 2001 Free Software Foundation, Inc.

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

import java.util.Map;

/**
  * This interface maps between native platform type names and DataFlavors.
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
  * @param flavors An array of data flavors to map
  *                or null for all data flavors.
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
  * @param natives An array of native types to map
  *                or null for all native types.
  *
  * @return A <code>Map</code> of data flavors.
  */
public abstract Map
getFlavorsForNatives(String[] natives);

} // interface FlavorMap

