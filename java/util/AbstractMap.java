/////////////////////////////////////////////////////////////////////////////
// AbstractMap.java -- Abstract implementation of most of Map
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
// ~ Everything.

package java.util;

public abstract class AbstractMap implements Map {
  public void clear() {}
  public boolean containsKey(Object key) {}
  public boolean containsValue(Object value) {}
  public abstract Set entries();
  public boolean equals(Object o) {}
  public Object get(Object key) {}
  public int hashCode() {}
  public boolean isEmpty() {}
  public Set keySet() {}
  public Object put(Object key, Object value) {}
  public void putAll(Map m) {}
  public Object remove(Object key) {}
  public int size() {}
  public String toString() {}
  public Collection values() {}
}
