/* SystemFlavorMap.java -- Maps between native flavor names and MIME types.
   Copyright (C) 2001 Free Software Foundation, Inc.

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

import java.util.HashMap;
import java.util.Map;

/**
  * This class maps between native platform type names and DataFlavors.
  *
  * XXX - The current implementation does no mapping at all.
  *
  * @author Mark Wielaard (mark@klomp.org)
  */
public final class SystemFlavorMap implements FlavorMap
{

/**
  * The default (instance) flavor map.
  */
private static FlavorMap defaultFlavorMap;

/**
  * Private constructor.
  */
private SystemFlavorMap()
{
}

/*************************************************************************/

/**
  * Maps the specified <code>DataFlavor</code> objects to the native
  * data type name.  The returned <code>Map</code> has keys that are
  * the data flavors and values that are strings.  The returned map
  * may be modified.  This can be useful for implementing nested mappings.
  *
  * @param flavors An array of data flavors to map
  *                or null for all data flavors.
  *
  * @return A <code>Map</code> of native data types to data flavors.
  */
public Map
getNativesForFlavors(DataFlavor[] flavors)
{
  return(new HashMap());
}

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
  * @return A <code>Map</code> of data flavors to native type names.
  */
public Map
getFlavorsForNatives(String[] natives)
{
  return(new HashMap());
}

/*************************************************************************/

/**
  * Returns the default (instance) (System)FlavorMap.
  */
public static FlavorMap
getDefaultFlavorMap()
{
  if (defaultFlavorMap == null)
    defaultFlavorMap = new SystemFlavorMap();

  return(defaultFlavorMap);
}

/*************************************************************************/

/**
  * Returns the native type name for the given java mime type.
  */
public static String
encodeJavaMIMEType(String mime)
{
  return null;
}

/*************************************************************************/

/**
  * Returns the native type name for the given data flavor.
  */
public static String
encodeDataFlavor(DataFlavor df)
{
  return null;
}

/*************************************************************************/

/**
  * Returns true if the native type name can be represented as
  * a java mime type.
  */
public static boolean
isJavaMIMEType(String name)
{
  return(false);
}

/*************************************************************************/

/**
  * Returns the java mime type for the given the native type name.
  */
public static String
decodeJavaMIMEType(String name)
{
  return null;
}

/*************************************************************************/

/**
  * Returns the data flavor given the native type name
  * or null when no such data flavor exists.
  */
public static DataFlavor
decodeDataFlavor(String name) throws ClassNotFoundException
{
  String javaMIMEType = decodeJavaMIMEType(name);
  if (javaMIMEType != null)
    return(new DataFlavor(javaMIMEType));
  else
    return(null);
}

} // class SystemFlavorMap

