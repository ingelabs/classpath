/* ElementFilter.java -- Filter for a collection of elements.
   Copyright (C) 2012  Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

package javax.lang.model.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * <p>Filter for selecting elements of interest from a collection.
 * The returned collections are new instances, but use the original
 * collection as a backing store.  They are not safe for concurrent
 * access.  The iteration order remains the same.  Null values are
 * not allowed and will throw a {@code NullPointerException}.</p>
 *<p>For convenience, a static import may be used to allow the
 * methods to be called more succinctly.</p>
 * 
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 * @since 1.6
 */
public class ElementFilter
{

  /**
   * Returns a set containing just the {@link TypeElement}s
   * held in {@code elements}.
   *
   * @param elements the elements to filter.
   * @return the filtered set.
   */
  public static Set<TypeElement> typesIn(Set<? extends Element> elements)
  {
    return new FilteredSet<TypeElement>(elements, ElementKind.CLASS,
					ElementKind.INTERFACE,
					ElementKind.ENUM,
					ElementKind.ANNOTATION_TYPE);
  }

  /**
   * Provides a filtered view of the given set, returning only
   * instances which are of one of the specified kinds.
   */
  private static final class FilteredSet<E extends Element> implements Set<E>
  {
    
    /**
     * The set being filtered.
     */
    private Set<Element> elements;

    /**
     * The kinds accepted by this filter.
     */
    private ElementKind[] kinds;

    /**
     * Constructs a new filtered set, returning
     * only instances of {@code clazz} from
     * {@code elements}.
     *
     * @param elements the set to filter.
     * @param kinds the kinds to accept
     * @throws NullPointerException if the set contains a null element.
     */
    @SuppressWarnings("unchecked")
    public FilteredSet(Set<? extends Element> elements, ElementKind... kinds)
    {
      this.elements = (Set<Element>) elements;
      this.kinds = kinds;
      Arrays.sort(kinds);
      for (Element e : elements)
	if (e == null)
	  throw new NullPointerException("Sets can not contain null values.");
    }

    /**
     * Adds an element to the set by passing it to the backing set.
     *
     * @param elem the element to add.
     * @return true if the element was added.
     */
    @Override
    public boolean add(E elem)
    {
      return elements.add(elem);
    }

    /**
     * Add all elements of the given collection, which aren't already
     * present in this set, to this set.  If the supplied collection
     * is also a set, the resulting set is the union of the two.
     * This call is passed to the backing set.
     *
     * @param coll the collection of elements to add.
     * @return true if the set was modified.
     */
    @Override
    public boolean addAll(Collection<? extends E> coll)
    {
      return elements.addAll(coll);
    }

    /**
     * Removes all elements from the set.
     */
    @Override
    public void clear()
    {
      elements.clear();
    }

    /**
     * Returns true if the element is an instance of one
     * of the specified kinds and the backing set contains
     * the given element.
     *
     * @param obj the object to check for.
     * @return true if the backing set contains the element.
     */
    @Override
    public boolean contains(Object obj)
    {
      if (obj instanceof Element)
      {
	Element elem = (Element) obj;
	if (Arrays.binarySearch(kinds, elem.getKind()) >= 0)
	  return elements.contains(obj);
      }
      return false;
    }

    /**
     * Returns true if the set contains all elements of the
     * given collection.  If the collection is also a set,
     * a return value of {@code true} means that the given
     * set is a subset of this set.
     *
     * @param coll the collection of elements to check.
     * @return true if the set contains all elements in {@code coll}.
     */
    @Override
    public boolean containsAll(Collection<?> coll)
    {
      for (Object obj : coll)
	if (!contains(obj))
	  return false;
      return true;
    }

    /**
     * Returns true if the specified object is also a set
     * of the same size and every member of it is also contained
     * in this set.
     *
     * @param obj the object to compare.
     * @return true if the above requirements are met.
     */
    @Override
    public boolean equals(Object obj)
    {
      if (obj == null)
	return false;
      if (obj instanceof Set)
	{
	  Set<?> otherSet = (Set<?>) obj;
	  return size() == otherSet.size() &&
	    containsAll(otherSet);
	}
      return false;
    }
  
    /**
     * Returns the hashcode of the set.  The hash code is computed
     * by taking the sum of the hash codes of the objects in the set.
     *
     * @return the hashcode of this set.
     */
    @Override
    public int hashCode()
    {
      int sum = 0;
      for (E elem : this)
	sum += elem.hashCode();
      return sum;
    }

    /**
     * Returns true if the set contains no elements.
     *
     * @return true if the size is zero.
     */
    @Override
    public boolean isEmpty()
    {
      return size() == 0;
    }

    /**
     * Returns an iterator over the set's elements.
     *
     * @return the iterator.
     */
    @Override
    public Iterator<E> iterator()
    {
      return new FilteredIterator<E>(elements.iterator(), kinds);
    }

    /**
     * Removes an element from the set if present.
     *
     * @param obj the object to remove.
     * @return true if the set contained the element.
     */
    @Override
    public boolean remove(Object obj)
    {
      if (contains(obj))
	return elements.remove(obj);
      return false;
    }

    /**
     * Removes from the set all elements contained in the specified
     * collection.  If the collection is also a set, the resulting
     * set is the asymmetric set difference of the two.
     *
     * @param coll the collection of elements to remove.
     * @return true if the set changed.
     */
    @Override
    public boolean removeAll(Collection<?> coll)
    {
      boolean modified = false;
      for (Object obj : coll)
	if (remove(obj))
	  modified = true;
      return modified;
    }

    /**
     * Retains only the elements in this set which are also contained in
     * the specified collection.  If the collection is also a set, the
     * resulting set is the intersection of the two.
     *
     * @param coll the collection of elements to remove.
     * @return true if the set changed.
     */
    @Override
    public boolean retainAll(Collection<?> coll)
    {
      boolean modified = false;
      for (E elem : this)
	if (!coll.contains(elem))
	  {
	    remove(elem);
	    modified = true;
	  }
      return modified;
    }
    
    /**
     * Returns the size of this set.  This is the size of the backing
     * set, minus any elements which aren't of one of the specified
     * kinds.
     *
     * @return the size of the set.
     */
    @Override
    public int size()
    {
      int count = 0;
      for (Element elem : elements)
	if (Arrays.binarySearch(kinds, elem.getKind()) >= 0)
	  ++count;
      return count;
    }

    /**
     * Returns a new array containing all the elements in the set.
     * Modifications to the array do not affect the set.
     *
     * @return an array of all elements in the set.
     */
    @Override
    public Object[] toArray()
    {
      int size = size();
      Object[] array = new Object[size];
      Iterator<E> iterator = iterator();
      for (int a = 0; a < size; ++a)
	array[a] = iterator.next();
      return array;
    }

    /**
     * Returns a array containing all the elements in the set with
     * the runtime type of the specified array.  If all the elements
     * from the set fit into the specified array, it will be used
     * for the returned array and any remaining space will be
     * populated with {@code null} values.  Otherwise, a new array
     * will be allocated.
     *
     * @param array the array which may be reused to provide the
     *              return value of this method.
     * @return an array containing all elements in the set.
     */
    @Override
    public <T> T[] toArray(T[] array)
    {
      int a, size = size();
      Iterator<E> iterator = iterator();
      if (array.length < size)
	{
	  @SuppressWarnings("unchecked")
	    T[] newArray = (T[]) new Object[size];
	  array = newArray;
	}
      Object[] elemArray = (Object[]) array;
      for (a = 0; a < size; ++a)
	elemArray[a] = iterator.next();
      for (; a < array.length; ++a)
	elemArray[a] = null;
      return array;
    }

    /**
     * Returns a textual representation of the filtered set.
     *
     * @return a textual representation.
     */
    @Override
    public String toString()
    {
      StringBuilder builder = new StringBuilder("[");
      for (E elem : this)
      {
	builder.append(elem.toString());
	builder.append(",");
      }
      builder.insert(builder.length() - 1, "]");
      return builder.toString();
    }

  }

  /**
   * Provides a filtered view of the given iterator, returning only
   * instances which are of one of the specified kinds.
   */
  private static final class FilteredIterator<E extends Element> implements Iterator<E>
  {

    /**
     * The iterator to filter.
     */
    private Iterator<Element> iterator;

    /**
     * The kinds accepted by this filter.
     */
    private ElementKind[] kinds;

    /**
     * Holds the next object if we had to retrieve it
     * in the {@link #hasNext()} method.
     */
    private Element next;

    /**
     * Constructs a new filtered iterator which only returns
     * elements that are of one of the specified kinds.
     *
     * @param iterator the iterator to filter.
     * @param kinds the kinds to accept.  This is assumed
     *              to be sorted.
     */
    public FilteredIterator(Iterator<Element> iterator,
			    ElementKind... kinds)
    {
      this.iterator = iterator;
      this.kinds = kinds;
    }

    /**
     * Returns true if there are more elements to come.
     *
     * @return true if there are more elements to retrieve.
     */
    @Override
    public boolean hasNext()
    {
      while (iterator.hasNext() && next == null)
	{
	  next = iterator.next();
	  if (Arrays.binarySearch(kinds, next.getKind()) < 0)
	    next = null;
	}
      return next != null;
    }

    /**
     * Returns the next element in the iteration which is of
     * one of the specified kinds.
     *
     * @return the next element.
     */
    @Override
    public E next()
    {
      if (next == null)
	if (!hasNext())
	  throw new NoSuchElementException("No more elements to return.");
      // The kind check means it should be of the correct type.
      @SuppressWarnings("unchecked")
	E retVal = (E) next;
      next = null;
      return retVal;
    }

    /**
     * Removes the last element returned by the iterator.
     * As we only return elements that match the filter,
     * the underlying iterator will always remove one of those.
     */
    @Override
    public void remove()
    {
      iterator.remove();
    }
  }
}

