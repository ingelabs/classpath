/////////////////////////////////////////////////////////////////////////////
// AbstractSequentialList.java -- List implementation for sequential access
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
// ~ Lots of doc comments still missing.
// ~ The class comment should include a description of what should be overridden
//   to provide what features, as should the listIterator comment.

package java.util;

/**
 * Abstract superclass to make it easier to implement the List interface when
 * backed by a sequential-access store, such as a linked list.
 */
public abstract class AbstractSequentialList extends AbstractList {

  /**
   * Returns a ListIterator over the list, starting from position index.
   * Subclasses must provide an implementation of this method.
   *
   * @exception IndexOutOfBoundsException if index < 0 || index > size()
   */
  public abstract ListIterator listIterator(int index);

  /**
   * Add an element to the list at a given index. This implementation obtains a
   * ListIterator positioned at the specified index, and then adds the element
   * using the ListIterator's add method.
   *
   * @param index the position to add the element
   * @param o the element to insert
   * @exception IndexOutOfBoundsException if index < 0 || index > size()
   * @exception UnsupportedOperationException if the iterator returned by
   *   listIterator(index) does not support the add method.
   */
  public void add(int index, Object o) {
    ListIterator i = listIterator(index);
    i.add(o);
  }

  public boolean addAll(int index, Collection c) {
    boolean changed = false;
    Iterator ci = c.iterator();
    ListIterator i = listIterator(index);
    while (ci.hasNext()) {
      i.add(ci.next());
      changed = true;
    }
    return changed;
  }

  public Object get(int index) {
    ListIterator i = listIterator(index);
    if (!i.hasNext()) {
      throw new IndexOutOfBoundsException();
    }
    return i.next();
  }

  /**
   * Return an Iterator over this List. This implementation returns
   * listIterator().
   *
   * @return an Iterator over this List
   */
  public Iterator iterator() {
    return listIterator();
  }

  public Object remove(int index) {
    ListIterator i = listIterator(index);
    if (!i.hasNext()) {
      throw new IndexOutOfBoundsException();
    }
    Object removed = i.next();
    i.remove();
    return removed;
  }

  public Object set(int index, Object o) {
    ListIterator i = listIterator(index);
    if (!i.hasNext()) {
      throw new IndexOutOfBoundsException();
    }
    Object old = i.next();
    i.set(o);
    return old;
  }
}
