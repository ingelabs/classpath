/////////////////////////////////////////////////////////////////////////////
// VectorListIterator.java -- Class that provides a ListIterator
// for a Vector
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
 * VectorListIterator provides the add and remove methods VectorIterator
 * lacks
 */
class VectorListIterator extends VectorIterator implements ListIterator {
  
  VectorListIterator(Vector v) {
    super(v);
  }

  public boolean hasPrevious() {
    if (checkConcurrentModification()) 
      throw new ConcurrentModificationException();
    return index > 0;
  }

  public Object previous() {
    if (checkConcurrentModification()) 
      throw new ConcurrentModificationException();
    if (!hasPrevious()) throw new NoSuchElementException();
    lastindex = index;
    return source.elementAt(--index);
  }

  public int previousIndex() {
    if (checkConcurrentModification()) 
      throw new ConcurrentModificationException();
    return index - 1;
  }

  public int nextIndex() {
    if (checkConcurrentModification()) 
      throw new ConcurrentModificationException();
    return index;
  }

  public void set(Object o) {
    if (checkConcurrentModification()) 
      throw new ConcurrentModificationException();
    if (lastindex == -1) throw new IllegalStateException();
    source.setElementAt(o, lastindex);
    source.modifies--;
  }

  public void add(Object o) {
    if (checkConcurrentModification()) 
      throw new ConcurrentModificationException();
    if (!hasNext()) {
      source.addElement(o);
      source.modifies--;
    } else {
      source.insertElementAt(o, index);
      source.modifies--;
    }
  }
}




