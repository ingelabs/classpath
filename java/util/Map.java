/////////////////////////////////////////////////////////////////////////////
// Map.java -- An object that maps keys to values
//
// Copyright (c) 1998 by Stuart Ballard (stuart.ballard@mcmail.com)
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU Library General Public License as published
// by the Free Software Foundation, version 2. (see COPYING.LIB)
//
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU Library General Public License
// along with this program; if not, write to the Free Software Foundation
// Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/////////////////////////////////////////////////////////////////////////////

// TO DO:
// ~ Doc comments for everything.

package java.util;

public interface Map {
  void clear();
  boolean containsKey(Object key);
  boolean containsValue(Object value);
  Set entrySet();
  boolean equals(Object o);
  Object get(Object key);
  int hashCode();
  boolean isEmpty();
  Set keySet();
  Object put(Object key, Object value);
  void putAll(Map m);
  Object remove(Object o);
  int size();
  Collection values();

  public static interface Entry {
    boolean equals(Object o);
    Object getKey();
    Object getValue();
    int hashCode();
    Object setValue(Object value);
  }
}
