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
// ~ Serialization is very much broken. Blame Sun for not specifying it.
// ~ The unimplemented methods don't have doc-comments.

// Note: In as much as this is implemented, it is totally to beta4 spec.

package java.util;

import java.io.Serializable;

/**
 * Utility class consisting of static methods that operate on, or return
 * Collections. Contains methods to sort, search, reverse, fill and shuffle
 * Collections, methods to facilitate interoperability with legacy APIs that
 * are unaware of collections, a method to return a list which consists of
 * multiple copies of one element, and methods which "wrap" collections to give
 * them extra properties, such as thread-safety and unmodifiability.
 */
public class Collections {

  /**
   * This class is non-instantiable.
   */
  private Collections() {
  }

  /**
   * An immutable, empty Set.
   * Note: This implementation isn't Serializable, although it should be by the
   * spec.
   */
  public static final Set EMPTY_SET = new AbstractSet() {

    public int size() {
      return 0;
    }

    // This is really cheating! I think it's perfectly valid, though - the
    // more conventional code is here, commented out, in case anyone disagrees.
    public Iterator iterator() {
      return EMPTY_LIST.iterator();
    }

    // public Iterator iterator() {
    //   return new Iterator() {
    // 
    //     public boolean hasNext() {
    //       return false;
    //     }
    // 
    //     public Object next() {
    //       throw new NoSuchElementException();
    //     }
    // 
    //     public void remove() {
    //       throw new UnsupportedOperationException();
    //     }
    //   };
    // }

  };

  /**
   * An immutable, empty List.
   * Note: This implementation isn't serializable, although it should be by the
   * spec.
   */
  public static final List EMPTY_LIST = new AbstractList() {

    public int size() {
      return 0;
    }

    public Object get(int index) {
      throw new IndexOutOfBoundsException();
    }
  };

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
        final int d = compare(key, l.get(pos), c);
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
   * avoid pathological behaviour on sequential-access lists, a linear search
   * is used if (l instanceof AbstractSequentialList). Note: although the
   * specification allows for an infinite loop if the list is unsorted, it will
   * not happen in this (Classpath) implementation.
   *
   * @param l the list to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
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
   * found. To avoid pathological behaviour on sequential-access lists, a
   * linear search is used if (l instanceof AbstractSequentialList). Note:
   * although the specification allows for an infinite loop if the list is
   * unsorted, it will not happen in this (Classpath) implementation.
   *
   * @param l the list to search (must be sorted)
   * @param key the value to search for
   * @param c the comparator by which the list is sorted
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
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
   * Copy one list to another. If the destination list is longer than the
   * source list, the remaining elements are unaffected. This method runs in
   * linear time.
   *
   * @param dest the destination list.
   * @param source the source list.
   * @exception IndexOutOfBoundsException if the destination list is shorter
   *   than the source list (the elements that can be copied will be, prior to
   *   the exception being thrown).
   * @exception UnsupportedOperationException if dest.listIterator() does not
   *   support the set operation.
   */
  public static void copy(List dest, List source) {
    Iterator i1 = source.iterator();
    ListIterator i2 = dest.listIterator();
    while (i1.hasNext()) {
      i2.next();
      i2.set(i1.next());
    }
  }

  /**
   * Returns an Enumeration over a collection. This allows interoperability
   * with legacy APIs that require an Enumeration as input.
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
   * Replace every element of a list with a given value. This method runs in
   * linear time.
   *
   * @param l the list to fill.
   * @param val the object to vill the list with.
   * @exception UnsupportedOperationException if l.listIterator() does not
   *   support the set operation.
   */
  public static void fill(List l, Object val) {
    ListIterator i = l.listIterator();
    while (i.hasNext()) {
      i.next();
      i.set(val);
    }
  }

  /**
   * Find the maximum element in a Collection, according to the natural
   * ordering of the elements. This implementation iterates over the
   * Collection, so it works in linear time.
   *
   * @param c the Collection to find the maximum element of
   * @returns the maximum element of c
   * @exception NoSuchElementException if c is empty
   * @exception ClassCastException if elements in c are not mutually comparable
   * @exception NullPointerException if null.compareTo is called
   */
  public static Object max(Collection c) {
    Iterator i = c.iterator();
    Comparable max = (Comparable)i.next(); // throws NoSuchElementException
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
    Object max = i.next(); // throws NoSuchElementException
    while (i.hasNext()) {
      Object o = i.next();
      if (order.compare(max, o) < 0) {
        max = o;
      }
    }
    return max;
  }

  /**
   * Find the minimum element in a Collection, according to the natural
   * ordering of the elements. This implementation iterates over the
   * Collection, so it works in linear time.
   *
   * @param c the Collection to find the minimum element of
   * @returns the minimum element of c
   * @exception NoSuchElementException if c is empty
   * @exception ClassCastException if elements in c are not mutually comparable
   * @exception NullPointerException if null.compareTo is called
   */
  public static Object min(Collection c) {
    Iterator i = c.iterator();
    Comparable min = (Comparable)i.next(); // throws NoSuchElementException
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
    Object min = i.next(); // throws NoSuchElementExcception
    while (i.hasNext()) {
      Object o = i.next();
      if (order.compare(min, o) > 0) {
        min = o;
      }
    }
    return min;
  }

  /**
   * Creates an immutable list consisting of the same object repeated n times.
   * The returned object is tiny, consisting of only a single reference to the
   * object and a count of the number of elements. It is Serializable.
   *
   * @param n the number of times to repeat the object
   * @param o the object to repeat
   * @returns a List consisting of n copies of o
   * @throws IllegalArgumentException if n < 0
   */
  // It's not Serializable, because the serialized form is unspecced.
  // Also I'm only assuming that it should be because I don't think it's
  // stated - I just would be amazed if it isn't...
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
   * Reverse a given list. This method works in linear time.
   *
   * @param l the list to reverse.
   * @exception UnsupportedOperationException if l.listIterator() does not
   *   support the set operation.
   */
  public static void reverse(List l) {
    ListIterator i1 = l.listIterator();
    ListIterator i2 = l.listIterator(l.size());
    while (i1.nextIndex() < i2.previousIndex()) {
      Object o = i1.next();
      i1.set(i2.previous());
      i2.set(o);
    }
  }

  /**
   * Get a comparator that implements the reverse of natural ordering. This is
   * intended to make it easy to sort into reverse order, by simply passing
   * Collections.reverseOrder() to the sort method. The return value of this
   * method is Serializable.
   */
  // The return value isn't Serializable, because the spec is broken.
  public static Comparator reverseOrder() {
    return new Comparator() {
      public int compare(Object a, Object b) {
        return -((Comparable)a).compareTo(b);
      }
    };
  }

  /**
   * Shuffle a list according to a default source of randomness. The algorithm
   * used would result in a perfectly fair shuffle (that is, each element would
   * have an equal chance of ending up in any position) with a perfect source
   * of randomness; in practice the results are merely very close to perfect.
   * <p>
   * This method operates in linear time on a random-access list, but may take
   * quadratic time on a sequential-access list.
   * Note: this (classpath) implementation will never take quadratic time, but
   * it does make a copy of the list. This is in line with the behaviour of the
   * sort methods and seems preferable.
   *
   * @param l the list to shuffle.
   * @exception UnsupportedOperationException if l.listIterator() does not
   *   support the set operation.
   */
  public static void shuffle(List l) {
    shuffle(l, new Random());
  }

  /**
   * Shuffle a list according to a given source of randomness. The algorithm
   * used iterates backwards over the list, swapping each element with an
   * element randomly selected from the elements in positions less than or
   * equal to it (using r.nextInt(int)).
   * <p>
   * This algorithm would result in a perfectly fair shuffle (that is, each
   * element would have an equal chance of ending up in any position) if r were
   * a perfect source of randomness. In practise (eg if r = new Random()) the
   * results are merely very close to perfect.
   * <p>
   * This method operates in linear time on a random-access list, but may take
   * quadratic time on a sequential-access list.
   * Note: this (classpath) implementation will never take quadratic time, but
   * it does make a copy of the list. This is in line with the behaviour of the
   * sort methods and seems preferable.
   *
   * @param l the list to shuffle.
   * @param r the source of randomness to use for the shuffle.
   * @exception UnsupportedOperationException if l.listIterator() does not
   *   support the set operation.
   */
  public static void shuffle(List l, Random r) {
    Object[] a = l.toArray(); // Dump l into an array
    ListIterator i = l.listIterator(l.size());

    // Iterate backwards over l
    while (i.hasPrevious()) {

      // Obtain a random position to swap with. nextIndex is used so that the
      // range of the random number includes the current position.
           int swap = r.nextInt(i.nextIndex());

      // Swap the swapth element of the array with the next element of the
      // list.
      Object o = i.previous();
      i.set(a[swap]);
      a[swap] = o;
    }
  }

  /**
   * Obtain an immutable Set consisting of a single element. The return value
   * of this method is Serializable.
   *
   * @param o the single element.
   * @returns an immutable Set containing only o.
   */
  // It's not serializable because the spec is broken.
  public static Set singleton(final Object o) {

    return new AbstractSet() {

      public int size() {
        return 1;
      }

      public Iterator iterator() {
        return new Iterator() {

          private boolean hasNext = true;

          public boolean hasNext() {
            return hasNext;
          }

          public Object next() {
            if (hasNext) {
              return o;
            } else {
              throw new NoSuchElementException();
            }
          }

          public void remove() {
            throw new UnsupportedOperationException();
          }
	};
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

  // All methods from here on in do NOT work, but DO return something which
  // will in most circumstances have similar behaviour to the correct return
  // value.
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
    public static Collection unmodifiableCollection(final Collection c) {
	return new AbstractCollection() {
	    public int size() {
		return c.size();
	    }
	    public Iterator iterator() {
		return new Iterator() {
		    final Iterator i = c.iterator();
		    public Object next() {
			return i.next();
		    }
		    public boolean hasNext() {
			return i.hasNext();
		    }
		    public void remove() {
			throw new UnsupportedOperationException();
		    }
		};
	    }
	    public boolean contains(Object o) {
		return c.contains(o);
	    }
	    public boolean containsAll(Collection c1) {
		return c.containsAll(c1);
	    }
	    public Object[] toArray() {
		return c.toArray();
	    }
	    public Object[] toArray(Object[] a) {
		return c.toArray(a);
	    }
	};
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

  // The beginnings of work on the previously unimplemented inner classes.
  // Sun's spec will need to be checked for the precise names of these
  // classes, for serializability's sake. However, from what I understand,
  // serialization is broken for these classes anyway.

  private class UnmodifiableCollection implements Collection, Serializable {
  }

  private class UnmodifiableList extends UnmodifiableCollection
      implements List {
  }

  private class UnmodifiableSet extends UnmodifiableCollection implements Set {
  }

  private class UnmodifiableSortedSet extends UnmodifiableSet
      implements SortedSet {
  }

  private class UnmodifiableMap implements Map, Serializable {
  }

  private class UnmodifiableSortedMap extends UnmodifiableMap
      implements SortedMap {
  }

  private class SynchronizedCollection implements Collection, Serializable {
  }

  private class SynchronizedList extends SynchronizedCollection
      implements List {
  }

  private class SynchronizedSet extends SynchronizedCollection implements Set {
  }

  private class SynchronizedSortedSet extends SynchronizedSet
      implements SortedSet {
  }

  private class SynchronizedMap implements Map, Serializable {
  }

  private class SynchronizedSortedMap extends SynchronizedMap
      implements SortedMap {
  }


}
