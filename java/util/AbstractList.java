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
// ~ Better general commenting
// ~ Hope that the collections guy is successful in getting the various
//   spec changes that he's agreed are good ideas, into 1.2FCS.

package java.util;

/**
 * A basic implementation of most of the methods in the List interface to make
 * it easier to create a List based on a random-access data structure. To
 * create an unmodifiable list, it is only necessary to override the size() and
 * get(int) methods (this contrasts with all other abstract collection classes
 * which require an iterator to be provided). To make the list modifiable, the
 * set(int, Object)  method should also be overridden, and to make the list
 * resizable, the add(int, Object) and remove(int) methods should be overridden
 * too. Other methods should be overridden if the backing data structure allows
 * for a more efficient implementation. The precise implementation used by
 * AbstractList is documented, so that subclasses can tell which methods could
 * be implemented more efficiently.
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

  // New in 1.2FCS
  public void clear() {
    removeRange(0, size());
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
    int index = 0;
    ListIterator i = listIterator();
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
        knownMod = modCount;
        removed = true;
      }
    };
  }

  public int lastIndexOf(Object o) {
    int index = size();
    ListIterator i = listIterator(index);
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

  public ListIterator listIterator(final int index) {

    if (index < 0 || index > size()) {
      throw new IndexOutOfBoundsException();
    }

    return new ListIterator() {
      private int knownMod = modCount;
      private int position = index;
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
        knownMod = modCount;
        position = lastReturned;
        lastReturned = -1;
      }

      public void set(Object o) {
        checkMod();
        if (lastReturned < 0) {
          throw new IllegalStateException();
        }
        AbstractList.this.set(lastReturned, o);
      }

      public void add(Object o) {
        checkMod();
        AbstractList.this.add(position, o);
        lastReturned = -1;
        knownMod = modCount;
      }
    };
  }

  public Object remove(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * Remove a subsection of the list. This is called by the clear and
   * removeRange methods of the class which implements subList, which are
   * difficult for subclasses to override directly. Therefore, this method
   * should be overridden instead by the more efficient implementation, if one
   * exists.
   * <p>
   * This implementation first checks for illegal or out of range arguments. It
   * then obtains a ListIterator over the list using listIterator(fromIndex).
   * It then calls next() and remove() on this iterator repeatedly, toIndex -
   * fromIndex times.
   *
   * @param fromIndex the index, inclusive, to remove from.
   * @param toIndex the index, exclusive, to remove to.
   * @exception UnsupportedOperationException if this list does not support
   *   the removeRange operation.
   * @exception IndexOutOfBoundsException if fromIndex > toIndex || fromIndex <
   *   0 || toIndex > size().
   */
  // New in 1.2FCS
  protected void removeRange(int fromIndex, int toIndex) {
    if (fromIndex > toIndex) {
      throw new IllegalArgumentException();
    } else if (fromIndex < 0 || toIndex > size()) {
      throw new IndexOutOfBoundsException();
    } else {
      ListIterator i = listIterator(fromIndex);
      for (int index = fromIndex; index < toIndex; index++) {
        i.next();
        i.remove();
      }
    }
  }

  public Object set(int index, Object o) {
    throw new UnsupportedOperationException();
  }

  public List subList(final int fromIndex, final int toIndex) {

    // Note that within this class two fields called modCount are inherited -
    // one from the superclass, and one from the outer class. These are
    // explicitly disambiguated in the code by referring to "this.modCount"
    // and "AbstractList.this.modCount".
    // The code uses both these two fields and *no other* to provide fail-fast
    // behaviour. For correct operation, the two fields should contain equal
    // values. Therefore, if this.modCount < AbstractList.this.modCount, there
    // has been a concurrent modification. This is all achieved purely by using
    // the modCount field, precisely according to the docs of AbstractList.
    // See the methods upMod and checkMod.

    return new AbstractList() {

      private final int offset = fromIndex;
      private int size = toIndex - fromIndex;

      { // This is an instance initializer, called whenever this anonymous
        // class is instantiated.
        upMod();
      }

      /**
       * This method checks the two modCount fields to ensure that there has
       * not been a concurrent modification. It throws an exception if there
       * has been, and otherwise returns normally.
       * Note that since this method is private, it will be inlined.
       *
       * @exception ConcurrentModificationException if there has been a
       *   concurrent modification.
       */
      private void checkMod() {
        if (this.modCount < AbstractList.this.modCount) {
          throw new ConcurrentModificationException();
        }
      }

      /**
       * This method is called after every method that causes a structural
       * modification to the backing list. It updates the local modCount field
       * to match that of the backing list.
       * Note that since this method is private, it will be inlined.
       */
      private void upMod() {
        this.modCount = AbstractList.this.modCount;
      }

      /**
       * This method checks that a value is between 0 and size (inclusive). If
       * it is not, an exception is thrown.
       * Note that since this method is private, it will be inlined.
       *
       * @exception IndexOutOfBoundsException if the value is out of range.
       */
      private void checkBoundsInclusive(int index) {
        if (index < 0 || index > size) {
          throw new IndexOutOfBoundsException();
        }
      }

      /**
       * This method checks that a value is between 0 (inclusive) and size
       * (exclusive). If it is not, an exception is thrown.
       * Note that since this method is private, it will be inlined.
       *
       * @exception IndexOutOfBoundsException if the value is out of range.
       */
      private void checkBoundsExclusive(int index) {
        if (index < 0 || index >= size) {
          throw new IndexOutOfBoundsException();
        }
      }

      public int size() {
        checkMod();
        return size;
      }

      public Iterator iterator() {
        return listIterator();
      }

      public ListIterator listIterator(final int index) {

        checkMod();
        checkBoundsInclusive(index);

        return new ListIterator() {
          ListIterator i = AbstractList.this.listIterator(index + offset);
          int position = index;

          public boolean hasNext() {
            checkMod();
            return position < size;
          }

          public boolean hasPrevious() {
            checkMod();
            return position > 0;
          }

          public Object next() {
            if (position < size) {
              Object o = i.next();
              position++;
              return o;
            } else {
              throw new NoSuchElementException();
            }
          }

          public Object previous() {
            if (position > 0) {
              Object o = i.previous();
              position--;
              return o;
            } else {
              throw new NoSuchElementException();
            }
          }

          public int nextIndex() {
            return offset + i.nextIndex();
          }

          public int previousIndex() {
            return offset + i.previousIndex();
          }

          public void remove() {
            i.remove();
            upMod();
            size--;
            position = nextIndex();
          }

          public void set(Object o) {
            i.set(o);
          }

          public void add(Object o) {
            i.add(o);
            upMod();
            size++;
          }

          // Here is the reason why the various modCount fields are mostly
          // ignored in this wrapper listIterator.
          // IF the backing listIterator is failfast, then the following holds:
          //   Using any other method on this list will call a corresponding
          //   method on the backing list *after* the backing listIterator
          //   is created, which will in turn cause a ConcurrentModException
          //   when this listIterator comes to use the backing one. So it is
          //   implicitly failfast.
          // If the backing listIterator is NOT failfast, then the whole of
          //   this list isn't failfast, because the modCount field of the
          //   backing list is not valid. It would still be *possible* to
          //   make the iterator failfast wrt modifications of the sublist
          //   only, but somewhat pointless when the list can be changed under
          //   us.
          // Either way, no explicit handling of modCount is needed.
          // However upMod() must be called in add and remove, and size
          // must also be updated in these two methods, since they do not go
          // through the corresponding methods of the subList.

        };
      }

      public Object set(int index, Object o) {
        checkMod();
        checkBoundsExclusive(index);
        o = AbstractList.this.set(index + offset, o);
        upMod();
        return o;
      }

      public Object get(int index) {
        checkMod();
        checkBoundsExclusive(index);
        return AbstractList.this.get(index + offset);
      }

      public void add(int index, Object o) {
        checkMod();
        checkBoundsInclusive(index);
        AbstractList.this.add(index + offset, o);
        upMod();
        size++;
      }

      public Object remove(int index) {
        checkMod();
        checkBoundsExclusive(index);
        Object o = AbstractList.this.remove(index + offset);
        upMod();
        size--;
        return o;
      }

      public void removeRange(int fromIndex, int toIndex) {
        AbstractList.this.removeRange(offset + fromIndex, offset + toIndex);
      }

      // I'm hoping for further methods here: *all* possible methods
      // should delegate to the backing list, or many O(n) optimizations
      // will be lost. This is one aspect of the spec bug which has been
      // submitted to the JDC - no number yet.
      // 22 Sep 1998 - at least addAll *is* in here as of 1.2FCS.

    };
  }
}
