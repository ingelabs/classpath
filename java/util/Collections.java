/////////////////////////////////////////////////////////////////////////////
// Collections.java -- Utility class with methods to operate on collections
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
// ~ Most of the inner classes are not yet implemented.
// ~ The methods that would return unimplemented inner classes return their
//   arguments - ie, the collection is NOT made synchronized or unmodifiable.
// ~ Serialization is very much broken until JDK 1.2beta4, which will specify
//   this properly, comes out.
// ~ The unimplemented methods don't have doc-comments.

package java.util;

import java.io.Serializable;

/**
 * Utility class consisting of static methods that operate on, or return
 * Collections. Contains methods to sort and search Collections, methods to
 * facilitate interoperability with legacy APIs that are unaware of collections,
 * a method to return a list which consists of multiple copies of one element,
 * and methods which "wrap" collections to give them extra properties, such as
 * thread-safety and unmodifiability.
 */
public class Collections {

  /**
   * A comparator that implements the reverse of natural ordering. This is
   * intended to make it easy to sort into reverse order, by simply passing
   * Collections.REVERSE_ORDER to the sort routine.
   * Note: This implementation isn't Serializable. It should be, by the spec,
   * but the serialization APIs of the collections classes aren't really clear
   * at all (thanks Sun). They plan to sort them out by 1.2beta4, apparently.
   */
  public static final Comparator REVERSE_ORDER = new Comparator() {
    public int compare(Object a, Object b) {
      return -((Comparable)a).compareTo(b);
    }
  };
  // Personally I'd like a public top-level class ReverseOrder that could either
  // reverse the natural ordering or a given Comparator.
  // This also blatantly fails to solve the reverse sorting problem for the
  // primitive types.

  /**
   * Compare two objects with or without a Comparator. If c is null, uses the
   * natural ordering. Slightly slower than doing it inline if the JVM isn't
   * clever, but worth it for removing a duplicate of the search code.
   * Note: This same code is used in Arrays (for sort as well as search)
   */
  private static int compare(Object o1, Object o2, Comparator c) {
    if (c == null) {
      return ((Comparable)o1).compareTo(o2);
    } else {
      return c.compare(o1, o2);
    }
  }

  /**
   * The hard work for the search routines. If the Comparator given is null,
   * uses the natural ordering of the elements.
   */
  private static int search(List l, Object key, final Comparator c) {

    int pos = 0;

    // We use a linear search using an iterator if we can guess that the list
    // is sequential-access.
    if (l instanceof AbstractSequentialList) {
      ListIterator i = l.listIterator();
      while (i.hasNext()) {
        final int d = compare(key, i.next(), c);
        if (d == 0) {
          return pos;
        } else if (d < 0) {
          return -pos - 1;
        }
        pos++;
      }

    // We assume the list is random-access, and use a binary search
    } else {
      int low = 0;
      int hi = l.size() - 1;
      while (low <= hi) {
        pos = (low + hi) >> 1;
        final int d = compare(key, a.get(pos), c);
        if (d == 0) {
          return pos;
        } else if (d < 0) {
          hi = pos - 1;
        } else {
          low = ++pos; // This gets the insertion point right on the last loop
        }
      }
    }

    // If we failed to find it, we do the same whichever search we did.
    return -pos - 1;
  }

  /**
   * Perform a binary search of a List for a key, using the natural ordering of
   * the elements. The list must be sorted (as by the sort() method) - if it is
   * not, the behaviour of this method is undefined, and may be an infinite
   * loop. Further, the key must be comparable with every item in the list. If
   * the list contains the key more than once, any one of them may be found. To
   * avoid pathological behaviour on sequential-access lists, a linear search is
   * used if (l instanceof AbstractSequentialList). Note: although the
   * specification allows for an infinite loop if the list is unsorted, it will
   * not happen in this (JCL) implementation.
   *
   * @param l the list to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not found,
   *   where n is the index of the first value higher than key or a.length if
   *   there is no such value.
   * @exception ClassCastException if key could not be compared with one of the
   *   elements of l
   * @exception NullPointerException if a null element has compareTo called
   */
  public static int binarySearch(List l, Object key) {
    return search(l, key, null);
  }

  /**
   * Perform a binary search of a List for a key, using a supplied Comparator.
   * The list must be sorted (as by the sort() method with the same Comparator)
   * - if it is not, the behaviour of this method is undefined, and may be an
   * infinite loop. Further, the key must be comparable with every item in the
   * list. If the list contains the key more than once, any one of them may be
   * found. To avoid pathological behaviour on sequential-access lists, a linear
   * search is used if (l instanceof AbstractSequentialList). Note: although the
   * specification allows for an infinite loop if the list is unsorted, it will
   * not happen in this (JCL) implementation.
   *
   * @param l the list to search (must be sorted)
   * @param key the value to search for
   * @param c the comparator by which the list is sorted
   * @returns the index at which the key was found, or -n-1 if it was not found,
   *   where n is the index of the first value higher than key or a.length if
   *   there is no such value.
   * @exception ClassCastException if key could not be compared with one of the
   *   elements of l
   */
  public static int binarySearch(List l, Object key, Comparator c) {
    if (c == null) {
      throw new NullPointerException();
    }
    return search(l, key, c);
  }

  /**
   * Returns an Enumeration over a collection. This allows interoperability with
   * legacy APIs that require an Enumeration as input.
   *
   * @param c the Collection to iterate over
   * @returns an Enumeration backed by an Iterator over c
   */
  public static Enumeration enumeration(Collection c) {
    final Iterator i = c.iterator();
    return new Enumeration() {
      public final boolean hasMoreElements() {
        return i.hasNext();
      }
      public final Object nextElement() {
        return i.next();
      }
    };
  }

  /**
   * Find the maximum element in a Collection, according to the natural ordering
   * of the elements. This implementation iterates over the Collection, so it
   * works in linear time.
   *
   * @param c the Collection to find the maximum element of
   * @returns the maximum element of c
   * @exception NoSuchElementException if c is empty
   * @exception ClassCastException if elements in c are not mutually comparable
   * @exception NullPointerException if null.compareTo is called
   */
  public static Object max(Collection c) {
    Iterator i = c.iterator();
    Comparable max = (Comparable)i.next();
    while (i.hasNext()) {
      Object o = i.next();
      if (max.compareTo(o) < 0) {
        max = (Comparable)o;
      }
    }
    return max;
  }

  /**
   * Find the maximum element in a Collection, according to a specified
   * Comparator. This implementation iterates over the Collection, so it
   * works in linear time.
   *
   * @param c the Collection to find the maximum element of
   * @param order the Comparator to order the elements by
   * @returns the maximum element of c
   * @exception NoSuchElementException if c is empty
   * @exception ClassCastException if elements in c are not mutually comparable
   */
  public static Object max(Collection c, Comparator order) {
    Iterator i = c.iterator();
    Object max = i.next();
    while (i.hasNext()) {
      Object o = i.next();
      if (c.compare(max, o) < 0) {
        max = o;
      }
    }
    return max;
  }

  /**
   * Find the minimum element in a Collection, according to the natural ordering
   * of the elements. This implementation iterates over the Collection, so it
   * works in linear time.
   *
   * @param c the Collection to find the minimum element of
   * @returns the minimum element of c
   * @exception NoSuchElementException if c is empty
   * @exception ClassCastException if elements in c are not mutually comparable
   * @exception NullPointerException if null.compareTo is called
   */
  public static Object min(Collection c) {
    Iterator i = c.iterator();
    Comparable min = (Comparable)i.next();
    while (i.hasNext()) {
      Object o = i.next();
      if (min.compareTo(o) > 0) {
        min = (Comparable)o;
      }
    }
    return min;
  }

  /**
   * Find the minimum element in a Collection, according to a specified
   * Comparator. This implementation iterates over the Collection, so it
   * works in linear time.
   *
   * @param c the Collection to find the minimum element of
   * @param order the Comparator to order the elements by
   * @returns the minimum element of c
   * @exception NoSuchElementException if c is empty
   * @exception ClassCastException if elements in c are not mutually comparable
   */
  public static Object min(Collection c, Comparator order) {
    Iterator i = c.iterator();
    Object min = i.next();
    while (i.hasNext()) {
      Object o = i.next();
      if (c.compare(min, o) > 0) {
        min = o;
      }
    }
    return min;
  }

  /**
   * Creates an immutable list consisting of the same object repeated n times.
   * The returned object is tiny, consisting of only a single reference to the
   * object and a count of the number of elements.
   *
   * @param n the number of times to repeat the object
   * @param o the object to repeat
   * @returns a List consisting of n copies of o
   * @throws IllegalArgumentException if n < 0
   */
  public static List nCopies(final int n, final Object o) {

    // Check for insane arguments
    if (n < 0) {
      throw new IllegalArgumentException();
    }

    // Create a minimal implementation of List
    return new AbstractList() {

      public int size() {
        return n;
      }

      public Object get(int index) {
        if (index < 0 || index >= n) {
          throw new IndexOutOfBoundsException();
        }
        return o;
      }
    };
  }

  /**
   * Sort a list according to the natural ordering of its elements. The list
   * must be modifiable, but can be of fixed size. The sort algorithm is
   * precisely that used by Arrays.sort(Object[]), which offers guaranteed
   * nlog(n) performance. This implementation dumps the list into an array,
   * sorts the array, and then iterates over the list setting each element from
   * the array.
   *
   * @param l the List to sort
   * @exception ClassCastException if some items are not mutually comparable
   * @exception UnsupportedOperationException if the List is not modifiable
   */
  public static void sort(List l) {
    Object[] a = l.toArray();
    Arrays.sort(a);
    ListIterator i = l.listIterator();
    for (int pos = 0; pos < a.length; pos++) {
      i.next();
      i.set(a[pos]);
    }
  }

  /**
   * Sort a list according to a specified Comparator. The list must be
   * modifiable, but can be of fixed size. The sort algorithm is precisely that
   * used by Arrays.sort(Object[], Comparator), which offers guaranteed
   * nlog(n) performance. This implementation dumps the list into an array,
   * sorts the array, and then iterates over the list setting each element from
   * the array.
   *
   * @param l the List to sort
   * @param c the Comparator specifying the ordering for the elements
   * @exception ClassCastException if c will not compare some pair of items
   * @exception UnsupportedOperationException if the List is not modifiable
   */
  public static void sort(List l, Comparator c) {
    Object[] a = l.toArray();
    Arrays.sort(a, c);
    ListIterator i = l.listIterator();
    for (int pos = 0; pos < a.length; pos++) {
      i.next();
      i.set(a[pos]);
    }
  }

  /**
   * Returns a sub-section of a List. The List returned is backed by the
   * original list and represents the section from fromIndex, inclusive, to
   * toIndex, exclusive. The size of the returned list is fixed at toIndex -
   * fromIndex, but changes to the returned list write through to the original
   * list, if it is modifiable. If the original list is later shortened such
   * that the whole of the sub-list is no longer there, the behaviour is
   * undefined.
   *
   * @param l the list to take a section from
   * @param fromIndex the start (inclusive) of the section to take
   * @param toIndex the end (exclusive) of the section to take
   * @exception IllegalArgumentException if fromIndex > toIndex
   * @exception IndexOutOfBoundsException if fromIndex < 0 || toIndex > l.size()
   */
  public static List subList(List l, int fromIndex, int toIndex) {
    if (fromIndex > toIndex) {
      throw new IllegalArgumentException();
    } else if (fromIndex < 0 || toIndex >= l.size()) {
      throw new IndexOutOfBoundsException();
    }
    return new SubList(l, fromIndex, toIndex);
  }

  // All methods from here on in do NOT work, but DO return something which will
  // in most circumstances have similar behaviour to the correct return value.
  // They all simply return their argument, so they do not add synchronization
  // or unmodifiability. Absence of synchronization is a potential problem for
  // programs, which might be relying on it, but absence of unmodifiability is
  // probably "merely" a security hole.
  public static Collection synchronizedCollection(Collection c) {
    return c;
  }
  public static List synchronizedList(List l) {
    return l;
  }
  public static Map synchronizedMap(Map m) {
    return m;
  }
  public static Set synchronizedSet(Set s) {
    return s;
  }
  public static SortedMap synchronizedSortedMap(SortedMap m) {
    return m;
  }
  public static SortedSet synchronizedSortedSet(SortedSet s) {
    return s;
  }
  public static Collection unmodifiableCollection(Collection c) {
    return c;
  }
  public static List unmodifiableList(List l) {
    return l;
  }
  public static Map unmodifiableMap(Map m) {
    return m;
  }
  public static Set unmodifiableSet(Set s) {
    return s;
  }
  public static SortedMap unmodifiableSortedMap(SortedMap m) {
    return m;
  }
  public static SortedSet unmodifiableSortedSet(SortedSet s) {
    return s;
  }

  // The class returned by subList().
  static class SubList extends AbstractList {
    private List list;
    private int start;
    private int len;

    SubList(List l, int fromIndex, int toIndex) {
      list = l;
      start = fromIndex;
      len = toIndex - fromIndex;
    }

    public Object get(int index) {
      if (index < 0 || index >= len) {
        throw new IndexOutOfBoundsException();
      }
      return list.get(start + index);
    }

    public int indexOf(Object o, int index) {
      if (index < 0 || index >= len) {
        throw new IndexOutOfBoundsException();
      }
      int i = list.indexOf(o, start + index) - start;
      return i < len ? i : -1;
    }

    public Iterator iterator() {
      return listIterator();
    }

    public int lastIndexOf(Object o, int index) {
      if (index < 0 || index >= len) {
        throw new IndexOutOfBoundsException();
      }
      int i = list.lastIndexOf(o, start + index) - start;
      return i >= 0 ? i : -1;
    }

    public ListIterator listIterator(int index) {
      if (index < 0 || index >= len) {
        throw new IndexOutOfBoundsException();
      }

      final ListIterator i = list.listIterator(index + start);
      return new ListIterator() {

        public boolean hasNext() {
          return i.hasNext() && nextIndex() < len;
        }

        public boolean hasPrevious() {
          return i.hasPrevious() && previousIndex() >= 0;
        }

        public Object next() {
          if (!hasNext()) {
            throw new NoSuchElementException();
          }
          return i.next();
        }

        public Object previous() {
          if (!hasPrevious()) {
            throw new NoSuchElementException();
          }
          return i.previous();
        }

        public int nextIndex() {
          return i.nextIndex() - start;
        }

        public int previousIndex() {
          return i.previousIndex() - start;
        }

        public void remove() {
          throw new UnsupportedOperationException();
        }

        public void set(Object o) {
          i.set(o);
        }

        public void add(Object o) {
          throw new UnsupportedOperationException();
        }
      };
    }

    public Object set(int index, Object o) {
      if (index < 0 || index >= len) {
        throw new IndexOutOfBoundsException();
      }
      return list.set(start + index, o);
    }

    public int size() {
      return len;
    }
  }

  /*
  // All these classes are commented out until we're ready to actually implement
  // them.
  static class UnmodifiableCollection implements Collection, Serializable {
    UnmodifiableCollection(Collection);
    public boolean add(Object);
    public boolean addAll(Collection);
    public void clear();
    public boolean contains(Object);
    public boolean containsAll(Collection);
    public boolean isEmpty();
    public Iterator iterator();
    public boolean remove(Object);
    public boolean removeAll(Collection);
    public boolean retainAll(Collection);
    public int size();
    public Object toArray()[];
    public Object toArray(Object[])[];
  }

  static class UnmodifiableSet extends UnmodifiableCollection implements Set {
    UnmodifiableSet(Set);
    public boolean equals(Object);
    public int hashCode();
  }

  static class UnmodifiableSortedSet extends UnmodifiableSet implements SortedSet {
    UnmodifiableSortedSet(SortedSet);
    public Comparator comparator();
    public Object first();
    public SortedSet headSet(Object);
    public Object last();
    public SortedSet subSet(Object, Object);
    public SortedSet tailSet(Object);
  }

  static class UnmodifiableList extends UnmodifiableCollection implements List 
  {
    UnmodifiableList(List);
    public void add(int, Object);
    public boolean addAll(int, Collection);
    public boolean equals(Object);
    public Object get(int);
    public int hashCode();
    public int indexOf(Object);
    public int indexOf(Object, int);
    public int lastIndexOf(Object);
    public int lastIndexOf(Object, int);
    public ListIterator listIterator();
    public ListIterator listIterator(int);
    public Object remove(int);
    public void removeRange(int, int);
    public Object set(int, Object);
  }

  static class UnmodifiableSortedMap extends UnmodifiableMap implements SortedMap , Serializable {
    UnmodifiableSortedMap(SortedMap);
    public Comparator comparator();
    public Object firstKey();
    public SortedMap headMap(Object);
    public Object lastKey();
    public SortedMap subMap(Object, Object);
    public SortedMap tailMap(Object);
  }

  static class SynchronizedCollection implements Collection, Serializable {
    SynchronizedCollection(Collection);
    SynchronizedCollection(Collection,Object);
    public boolean add(Object);
    public boolean addAll(Collection);
    public void clear();
    public boolean contains(Object);
    public boolean containsAll(Collection);
    public boolean isEmpty();
    public Iterator iterator();
    public boolean remove(Object);
    public boolean removeAll(Collection);
    public boolean retainAll(Collection);
    public int size();
    public Object toArray()[];
    public Object toArray(Object[])[];
  }

  static class SynchronizedSet extends SynchronizedCollection implements Set {
    SynchronizedSet(Set);
    SynchronizedSet(Set,Object);
    public boolean equals(Object);
    public int hashCode();
  }

  static class SynchronizedSortedSet extends SynchronizedSet implements SortedSet {
    SynchronizedSortedSet(SortedSet);
    SynchronizedSortedSet(SortedSet,Object);
    public Comparator comparator();
    public Object first();
    public SortedSet headSet(Object);
    public Object last();
    public SortedSet subSet(Object, Object);
    public SortedSet tailSet(Object);
  }

static class SynchronizedList extends SynchronizedCollection implements List {
    SynchronizedList(List);
    public void add(int, Object);
    public boolean addAll(int, Collection);
    public boolean equals(Object);
    public Object get(int);
    public int hashCode();
    public int indexOf(Object);
    public int indexOf(Object, int);
    public int lastIndexOf(Object);
    public int lastIndexOf(Object, int);
    public ListIterator listIterator();
    public ListIterator listIterator(int);
    public Object remove(int);
    public void removeRange(int, int);
    public Object set(int, Object);
}

static class SynchronizedSortedMap extends SynchronizedMap implements SortedMap {
    SynchronizedSortedMap(SortedMap);
    SynchronizedSortedMap(SortedMap,Object);
    public Comparator comparator();
    public Object firstKey();
    public SortedMap headMap(Object);
    public Object lastKey();
    public SortedMap subMap(Object, Object);
    public SortedMap tailMap(Object);
  }
  */
}
