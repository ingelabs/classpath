/////////////////////////////////////////////////////////////////////////////
// VectorIterator.java -- Class that provides Enumeration and Iterators
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
 * Class VectorIterator provides an Iterator for Vectors
 */
class VectorIterator implements Iterator {
  protected Vector source;
  //Used to make Vector's iterators Fail-Fast
  protected int vectorModificationCount;
  protected boolean fail;
  protected int index, lastindex;

  VectorIterator(Vector v) {
    vectorModificationCount=v.modifies;
    fail=false;
    source = v;
    index=0;
    lastindex = -1;
  }

  protected boolean checkConcurrentModification() {
    if (fail) return true;
    return fail = vectorModificationCount != source.modifies;
  }

  public boolean hasNext() {
    if (checkConcurrentModification()) 
      throw new ConcurrentModificationException();
    return index < source.size();
  }

  public Object next() {
    if (checkConcurrentModification()) 
      throw new ConcurrentModificationException();
    if (!hasNext()) throw new NoSuchElementException();
    lastindex = index;
    return source.elementAt(index++);
  }

  public void remove() {
    if (checkConcurrentModification()) 
      throw new ConcurrentModificationException();
    if (lastindex != -1) {
      source.removeElementAt(lastindex);
      source.modifies--;
      lastindex = -1;
    } else throw new IllegalStateException();
  }

}




