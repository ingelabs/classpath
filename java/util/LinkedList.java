/////////////////////////////////////////////////////////////////////////////
// LinkedList.java -- Linked list implementation of the List interface
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

package java.util;
import java.io.Serializable;

// TO DO:
// ~ Doc comment for the class.
// ~ Serialization (which means trying to comprehend Sun's serialized form
//   docs)
// ~ Doc comments for the non-list methods.
// ~ Some commenting on the Backing API and other general implementation notes.

/**
 * Linked list implementation of the List interface.
 */
public class LinkedList extends AbstractSequentialList implements Serializable{

  /**
   * An Entry containing the head (in the next field) and the tail (in the
   * previous field) of the list. The data field is null. If the list is empty,
   * both the head and the tail point to ends itself.
   */
  Entry ends = new Entry();

  /**
   * The current length of the list.
   */
  int size = 0;

  /**
   * Class to represent an entry in the list. Holds a single element.
   */
  private static class Entry {

    /**
     * The list element.
     */
    Object data = null;

    /**
     * The next entry in the list. If this is the last entry in the list, the
     * ends field of the list is held here.
     */
    Entry next;

    /**
     * The previous entry in the list. If this is the first entry in the list,
     * the ends field of the list is held here.
     */
    Entry previous;

    /**
     * Create an entry with given data and linkage.
     */
    Entry(Object d, Entry n, Entry p) {
      data = d;
      next = n;
      previous = p;
    }

    /**
     * Create an entry with no data and linking to itself, for use as the ends
     * field of the list.
     */
    Entry() {
      next = previous = this;
    }

    /**
     * Remove this entry.
     */
    Object remove() {
      previous.next = next;
      next.previous = previous;
      return data;
    }
  }

  private static interface Backing {
    void checkMod(int known);
    void upMod();
    void incSize(int by);
    void decSize(int by);
  }

  private final Backing back = new Backing() {
    public void checkMod(int known) {
      if (known < modCount) {
	throw new ConcurrentModificationException();
      }
    }
    public void upMod() {
      modCount++;
    }
    public void incSize(int by) {
      size += by;
    }
    public void decSize(int by) {
      size -= by;
    }
  };

  /** A ListIterator over the list. This class keeps track of its
   * position in the list, the size of the list, and the two list
   * entries it is between.  This enables it to be used identically
   * for both the list itself and a sublist of the list.
   */
  private static class Iter implements ListIterator {

    /**
     * The index of the element that will be returned by next().
     */
    int pos;

    /**
     * The size of the backing list.
     */
    int size;

    /**
     * The entry containing the element that will be returned by next().
     */
    Entry next;

    /**
     * The entry containing the element that will be returned by previous().
     */
    Entry previous;

    /**
     * The entry that will be affected by remove() or set().
     */
    Entry recent;

    /**
     * The known value of the modCount of the backing list.
     */
    int knownMod;

    private final Backing b;

    /**
     * Create a new Iter starting at a given Entry within the list, at a given
     * position, in a list of given size.
     *
     * @param index the index to begin iteration.
     * @exception IndexOutOfBoundsException if index < 0 || index > size.
     */
    Iter(Backing backing, Entry n, int index, int s) {
      b = backing;
      pos = index;
      size = s;
      next = n;
      previous = n.previous;
    }

    public int nextIndex() {
      b.checkMod(knownMod);
      return pos;
    }

    public int previousIndex() {
      b.checkMod(knownMod);
      return pos - 1;
    }

    public boolean hasNext() {
      b.checkMod(knownMod);
      return pos < size;
    }

    public boolean hasPrevious() {
      b.checkMod(knownMod);
      return pos > 0;
    }

    public Object next() {
      b.checkMod(knownMod);
      if (pos >= size) {
	throw new NoSuchElementException();
      } else {
	pos++;
	recent = previous = next;
	next = recent.next;
	return recent.data;
      }
    }

    public Object previous() {
      b.checkMod(knownMod);
      if (pos <= 0) {
	throw new NoSuchElementException();
      } else {
	pos--;
	recent = next = previous;
	previous = recent.previous;
	return recent.data;
      }
    }

    public void remove() {
      b.checkMod(knownMod);
      if (recent == null) {
	throw new IllegalStateException();
      }

      // Adjust the position to before the removed element
      if (recent == previous) pos--;

      // Could use recent.remove() but this way is quicker, and also correctly
      // fixes next and previous.
      next = recent.previous.next = recent.next;
      previous = recent.next.previous = recent.previous;
      size--;
      b.decSize(1);
      knownMod++;
      b.upMod();
      recent = null;
    }

    public void add(Object o) {
      b.checkMod(knownMod);
      previous.next = next.previous = new Entry(o, next, previous);

      // New for 1.2RC1 - the semantics changed so that the iterator is
      // positioned *after* the new element.
      previous = next;
      next = previous.next;
      pos++;

      size++;
      b.incSize(1);
      knownMod++;
      b.upMod();
      recent = null;
    }

    public void set(Object o) {
      b.checkMod(knownMod);
      if (recent == null) {
	throw new IllegalStateException();
      }
      recent.data = o;
    }
  }

  /**
   * Obtain the Entry at a given position in a list. This method of course
   * takes linear time, but it is intelligent enough to take the shorter of the
   * paths to get to the Entry required. This implies that the first or last
   * entry in the list is obtained in constant time, which is a very desirable
   * property.
   * For speed and flexibility in which ranges are valid, range checking is not
   * done in this method, and if n is outside the range -1 <= n <= size, the
   * result will be wrong (but no exception will be thrown).
   * Note that you *can* obtain entries at position -1 and size, which are
   * equal to prehead and posttail respectively.
   * This method is static so that it can also be used in subList.
   *
   * @param n the number of the entry to get.
   * @param size the size of the list to get the entry in.
   * @param head the entry before the first element of the list (usually ends).
   * @param tail the entry after the last element of the list (usually ends).
   */
  static Entry getEntry(int n, int size, Entry head, Entry tail) {

    // n less than size/2, iterate from start
    if (n < size >> 1) {
      while (n-- >= 0) {
	head = head.next;
      }
      return head;

    // n greater than size/2, iterate from end
    } else {
      while (++n <= size) {
	tail = tail.previous;
      }
      return tail;
    }
  }

  /**
   * Create an empty linked list.
   */
  public LinkedList() {
    super();
  }

  /**
   * Create a linked list containing the elements, in order, of a given
   * collection.
   *
   * @param c the collection to populate this list from.
   */
  public LinkedList(Collection c) {
    super();
    // Note: addAll could be made slightly faster, but not enough so to justify
    // re-implementing it from scratch. It is just a matter of a relatively
    // small constant factor.
    addAll(c);
  }

  public Object getFirst() {
    if (size == 0) {
      throw new NoSuchElementException();
    }
    return ends.next.data;
  }

  public Object getLast() {
    if (size == 0) {
      throw new NoSuchElementException();
    }
    return ends.previous.data;
  }

  public Object removeFirst() {
    if (size == 0) {
      throw new NoSuchElementException();
    }
    size--;
    modCount++;
    return ends.next.remove();
  }

  public Object removeLast() {
    if (size == 0) {
      throw new NoSuchElementException();
    }
    size--;
    modCount++;
    return ends.previous.remove();
  }

  public void addFirst(Object o) {
    ends.next.previous = ends.next = new Entry(o, ends.next, ends);
    size++;
    modCount++;
  }

  public void addLast(Object o) {
    ends.previous.next = ends.previous = new Entry(o, ends, ends.previous);
    size++;
    modCount++;
  }

  /**
   * Obtain the number of elements currently in this list.
   *
   * @returns the number of elements currently in this list.
   */
  public int size() {
    return size;
  }

  /**
   * Remove a range of elements from this list.
   *
   * @param fromIndex the index, inclusive, to remove from.
   * @param toIndex the index, exclusive, to remove to.
   * @exception IndexOutOfBoundsException if fromIndex > toIndex || fromIndex <
   *   0 || toIndex > size().
   */
  // Note: normally removeRange is provided to allow efficient ways to
  // implement clear() on subLists. However, in this case clear on subLists
  // works anyway, so this implementation is included just for completeness
  // and because subclasses might try to use it.
  protected void removeRange(int fromIndex, int toIndex) {
    subList(fromIndex, toIndex).clear();
  }

  /**
   * Clear the list.
   */
  public void clear() {
    ends.next = ends.previous = ends;
    modCount++;
    size = 0;
  }

  /**
   * Obtain a ListIterator over this list, starting at a given index. The
   * ListIterator returned by this method supports the add, remove and set
   * methods.
   *
   * @param index the index of the element to be returned by the first call to
   *   next(), or size() to be initially positioned at the end of the list.
   * @exception IndexOutOfBoundsException if index < 0 || index > size().
   */
  public ListIterator listIterator(int index) {

    // Check bounds
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException();
    }

    return new Iter(back, getEntry(index, size, ends, ends), index, size);
  }

  /**
   * Obtain a List view of a subsection of this list, from fromIndex
   * (inclusive) to toIndex (exclusive). The returned list is modifiable in
   * every respect. Changes to the returned list are reflected in this list. If
   * this list is structurally modified is any way other than through the
   * returned list, any subsequent operations on the returned list will result
   * in a ConcurrentModificationException (that is, the returned list is
   * fail-fast).
   *
   * @param fromIndex the index that the returned list should start from
   *    (inclusive).
   * @param toIndex the index that the returned list should go to (exclusive).
   * @returns a List backed by a subsection of this list.
   * @exception IndexOutOfBoundsException if fromIndex < 0 || toIndex > size()
   *   || fromIndex > toIndex.
   */
  public List subList(int fromIndex, int toIndex) {

    // Check bounds
    if (fromIndex > toIndex || fromIndex < 0 || toIndex > size) {
      throw new IndexOutOfBoundsException();
    }

    return new SubLinkedList(back, getEntry(fromIndex - 1, size, ends, ends),
			     getEntry(toIndex, size, ends, ends),
			     toIndex - fromIndex);
  }

  private static class SubLinkedList extends AbstractSequentialList {

    Entry head; // entry before the beginning
    Entry tail; // entry after the end
    int size;
    private final Backing b;

    private final Backing back = new Backing() {
      public void checkMod(int known) {
	if (known < modCount) {
	  throw new ConcurrentModificationException();
	}
      }
      public void upMod() {
	modCount++;
      }
      public void incSize(int by) {
	size += by;
      }
      public void decSize(int by) {
	size -= by;
      }
    };

    SubLinkedList(Backing backing, Entry h, Entry t, int s) {
      b = backing;
      head = h;
      tail = t;
      size = s;
    }

    public int size() {
      b.checkMod(this.modCount);
      return size;
    }

    public ListIterator listIterator(int index) {
      b.checkMod(this.modCount);

      // Check bounds
      if (index < 0 || index > size) {
	throw new IndexOutOfBoundsException();
      }

      return new Iter(back, getEntry(index, size, head, tail), index, size);
    }

    public void clear() {
      b.checkMod(this.modCount);
      head.next = tail;
      tail.previous = head;
      size = 0;
      b.decSize(size);
      modCount++;
      b.upMod();
    }

    // No removeRange because this class cannot be publically subclassed.

    public List subList(int fromIndex, int toIndex) {
      b.checkMod(this.modCount);

      // Check bounds
      if (fromIndex > toIndex || fromIndex < 0 || toIndex > size) {
	throw new IndexOutOfBoundsException();
      }

      return new SubLinkedList(back, getEntry(fromIndex - 1, size, head, tail),
			       getEntry(toIndex, size, head, tail),
			       toIndex - fromIndex);
    }
  }
}
