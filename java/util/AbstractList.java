/////////////////////////////////////////////////////////////////////////////
// AbstractList.java -- Abstract implementation of most of List
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
// ~ Doc comments for almost everything.

package java.util;

/**
 * A basic implementation of most of the methods in the List interface to make
 * it easier to create a List based on a random-access data structure. To create
 * an unmodifiable list, it is only necessary to override the size and get
 * methods (this contrasts with all other abstract collection classes which
 * require an iterator to be provided). To make the list modifiable, the set
 * method should also be overridden, and to make the list resizable, the
 * add(int, Object) and remove(int) methods should be overridden too.
 */
public abstract class AbstractList extends AbstractCollection implements List {

  /**
   * A count of the number of structural modifications that have been made to
   * the list (that is, insertions and removals).
   */
  protected transient int modCount = 0;

  public abstract Object get(int index);

  public void add(int index, Object o) {
    throw new UnsupportedOperationException();
  }

  public boolean add(Object o) {
    add(size(), o);
    return true;
  }

  public boolean addAll(int index, Collection c) {
    Iterator i = c.iterator();
    if (i.hasNext()) {
      do {
        add(index++, i.next());
      } while (i.hasNext());
      return true;
    } else {
      return false;
    }
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof List)) {
      return false;
    } else {
      Iterator i1 = iterator();
      Iterator i2 = ((List)o).iterator();
      while (i1.hasNext()) {
        if (!i2.hasNext()) {
          return false;
        } else {
          Object e = i1.next();
          if (e == null ? i2.next() != null : !e.equals(i2.next())) {
            return false;
          }
        }
      }
      if (i2.hasNext()) {
        return false;
      } else {
        return true;
      }
    }
  }

  public int hashCode() {
    int hashCode = 1;
    Iterator i = iterator();
    while (i.hasNext()) {
      Object obj = i.next();
      hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
    }
    return hashCode;
  }

  public int indexOf(Object o) {
    return indexOf(o, 0);
  }

  public int indexOf(Object o, int index) {
    ListIterator i = listIterator(index);
    if (o == null) {
      while (i.hasNext()) {
        if (i.next() == null) {
          return index;
        }
        index++;
      }
    } else {
      while (i.hasNext()) {
        if (o.equals(i.next())) {
          return index;
        }
        index++;
      }
    }
    return -1;
  }

  public Iterator iterator() {
    return new Iterator() {
      private int knownMod = modCount;
      private int position = 0;
      boolean removed = true;

      private void checkMod() {
        if (knownMod < modCount) {
          throw new ConcurrentModificationException();
        }
      }

      public boolean hasNext() {
        checkMod();
        return position < size();
      }

      public Object next() {
        checkMod();
        removed = false;
        try {
          return get(position++);
        } catch (IndexOutOfBoundsException e) {
          throw new NoSuchElementException();
        }
      }

      public void remove() {
        checkMod();
        if (removed) {
          throw new IllegalStateException();
        }
        AbstractList.this.remove(--position);
        knownMod++;
        removed = true;
      }
    };
  }

  public int lastIndexOf(Object o) {
    return lastIndexOf(o, size() - 1);
  }

  public int lastIndexOf(Object o, int index) {
    ListIterator i = listIterator(index + 1);
    if (o == null) {
      while (i.hasPrevious()) {
        if (i.previous() == null) {
          return index;
        }
        index--;
      }
    } else {
      while (i.hasPrevious()) {
        if (o.equals(i.previous())) {
          return index;
        }
        index--;
      }
    }
    return -1;
  }

  public ListIterator listIterator() {
    return listIterator(0);
  }

  public ListIterator listIterator(int index) {
    return new ListIterator() {
      private int knownMod = modCount;
      private int position = 0;
      private int lastReturned = -1;

      private void checkMod() {
        if (knownMod < modCount) {
          throw new ConcurrentModificationException();
        }
      }

      public boolean hasNext() {
        checkMod();
        return position < size();
      }

      public boolean hasPrevious() {
        checkMod();
        return position > 0;
      }

      public Object next() {
        checkMod();
        if (hasNext()) {
          lastReturned = position++;
          return get(lastReturned);
        } else {
          throw new NoSuchElementException();
        }
      }

      public Object previous() {
        checkMod();
        if (hasPrevious()) {
          lastReturned = --position;
          return get(lastReturned);
        } else {
          throw new NoSuchElementException();
        }
      }

      public int nextIndex() {
        checkMod();
        return position;
      }

      public int previousIndex() {
        checkMod();
        return position - 1;
      }

      public void remove() {
        checkMod();
        if (lastReturned < 0) {
          throw new IllegalStateException();
        }
        AbstractList.this.remove(lastReturned);
        knownMod++;
        position = lastReturned;
        lastReturned = -1;
      }

      public void set(Object o) {
        checkMod();
        if (lastReturned < 0) {
          throw new IllegalStateException();
        }
        AbstractList.this.set(lastReturned, o);
        knownMod++;
      }

      public void add(Object o) {
        checkMod();
        AbstractList.this.add(position, o);
        lastReturned = -1;
        knownMod++;
      }
    };
  }

  public Object remove(int index) {
    throw new UnsupportedOperationException();
  }

  public void removeRange(int fromIndex, int toIndex) {
    if (fromIndex > toIndex) {
      throw new IllegalArgumentException();
    } else if (fromIndex < 0 || toIndex > size()) {
      throw new IndexOutOfBoundsException();
    } else {
      while (toIndex-- > fromIndex) {
        remove(fromIndex);
      }
    }
  }

  public Object set(int index, Object o) {
    throw new UnsupportedOperationException();
  }
}
