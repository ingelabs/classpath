/////////////////////////////////////////////////////////////////////////////
// ArrayEnumeration.java -- Class that provides an Enumeration
// for arrays.  Vector uses this for its enumerations
//
// Copyright (c) 1998 by Scott G. Miller (scgmille@indiana.edu)
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

package java.util;

/**
 * VectorListEnumerator provides an Enumeration for Vectors
 */
public class ArrayEnumeration implements Enumeration {
  protected int index;
  protected Object[] elements;

  public ArrayEnumeration(Object[] data) {
    elements=data;
    index=0;
  }

  public boolean hasMoreElements() {
    return index < elements.length - 1;
  }

  public Object nextElement() {
    if (index >= elements.length) throw new NoSuchElementException();
    return elements[index++];
  }
}

