/* CopyOnWriteArrayList.java
   Copyright (C) 2006 Free Software Foundation

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

package java.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * A thread-safe implementation of an ArrayList. A CopyOnWriteArrayList is
 * as special ArrayList which performs copies of the underlying storage
 * each time a write (<code>remove</code>, <code>add</code> etc..) operation
 * is performed.<br />
 * <br />
 * The update operation in this class run usually in <code>O(n)</code> or worse,
 * but traversal operations are fast and efficient, especially when running in
 * a multi-thread environment without the need to design complex synchronize
 * mechanisms.<br />
 * <br />
 * <code>Iterator</code>s in this class work on a snapshot of the backing store
 * at the moment the iterator itself was created, hence the iterator will not
 * reflect changes in the underlying storage. Thus, update operation on the
 * <code>Iterator</code>s are not supported, but as interferences from other
 * threads are impossible, no <code>ConcurrentModificationException</code>
 * will be ever thrown from within the <code>Iterator</code>.
 * <br /><br />
 * This class is especially useful when used with event handling, like the
 * following code demonstrates:<br />
 * <code><pre>
 * 
 * CopyOnWriteArrayList<EventListener> listeners =
 *   new CopyOnWriteArrayList<EventListener>();
 * 
 * [...]
 * 
 * for (final EventListener listener : listeners)
 *   {
 *     Runnable dispatcher = new Runnable() {
 *       public void run()
 *       {
 *         listener.preferenceChange(event);
 *       }
 *     };
 *         
 *     Executor executor = Executors.newSingleThreadExecutor();
 *     executor.execute(dispatcher);
 *   }
 * </pre></code>
 * 
 * @since 1.5
 */
public class CopyOnWriteArrayList<E> extends AbstractList<E> implements
    List<E>, RandomAccess, Cloneable, Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 8673264195747942595L;
  
  /**
   * Where the data is stored.
   */
  private transient E[] data;

  /**
   * Construct a new ArrayList with the default capacity (16).
   */
  public CopyOnWriteArrayList()
  {
    data = (E[]) new Object[0];
  }

  /**
   * Construct a new ArrayList, and initialize it with the elements in the
   * supplied Collection. The initial capacity is 110% of the Collection's size.
   * 
   * @param c
   *          the collection whose elements will initialize this list
   * @throws NullPointerException
   *           if c is null
   */
  public CopyOnWriteArrayList(Collection< ? extends E> c)
  {
    // FIXME ... correct?  use c.toArray()
    data = (E[]) new Object[c.size()];
    int index = 0;
    for (E value : c)
      data[index++] = value;
  }

  /**
   * Construct a new ArrayList, and initialize it with the elements in the
   * supplied array.
   * 
   * @param array
   *          the array used to initialize this list
   * @throws NullPointerException
   *           if array is null
   */
  public CopyOnWriteArrayList(E[] array)
  {
    data = array.clone();
  }

  /**
   * Returns the number of elements in this list.
   * 
   * @return the list size
   */
  public int size()
  {
    return data.length;
  }

  /**
   * Checks if the list is empty.
   * 
   * @return true if there are no elements
   */
  public boolean isEmpty()
  {
    return data.length == 0;
  }

  /**
   * Returns true if element is in this ArrayList.
   * 
   * @param e
   *          the element whose inclusion in the List is being tested
   * @return true if the list contains e
   */
  public boolean contains(Object e)
  {
    return indexOf(e) != -1;
  }

  /**
   * Returns the lowest index at which element appears in this List, or -1 if it
   * does not appear.
   * 
   * @param e
   *          the element whose inclusion in the List is being tested
   * @return the index where e was found
   */
  public int indexOf(Object e)
  {
    E[] data = this.data;
    for (int i = 0; i < data.length; i++)
      if (equals(e, data[i]))
        return i;
    return -1;
  }

  /**
   * Return the lowest index greater equal <code>index</code> at which
   * <code>e</code> appears in this List, or -1 if it does not
   * appear.
   *
   * @param e the element whose inclusion in the list is being tested
   * @param index the index at which the search begins
   * @return the index where <code>e</code> was found
   */
  public int indexOf(E e, int index)
  {
    E[] data = this.data;

    for (int i = index; i < data.length; i++)
      if (equals(e, data[i]))
	return i;
    return -1;
  }

  /**
   * Returns the highest index at which element appears in this List, or -1 if
   * it does not appear.
   * 
   * @param e
   *          the element whose inclusion in the List is being tested
   * @return the index where e was found
   */
  public int lastIndexOf(Object e)
  {
    E[] data = this.data;
    for (int i = data.length - 1; i >= 0; i--)
      if (equals(e, data[i]))
        return i;
    return -1;
  }

  /**
   * Returns the highest index lesser equal <code>index</code> at
   * which <code>e</code> appears in this List, or -1 if it does not
   * appear.
   *
   * @param e the element whose inclusion in the list is being tested
   * @param index the index at which the search begins
   * @return the index where <code>e</code> was found
   */
  public int lastIndexOf(E e, int index)
  {
    E[] data = this.data;

    for (int i = index; i >= 0; i--)
      if (equals(e, data[i]))
	return i;
    return -1;
  }

  /**
   * Creates a shallow copy of this ArrayList (elements are not cloned).
   * 
   * @return the cloned object
   */
  public Object clone()
  {
    CopyOnWriteArrayList<E> clone = null;
    try
      {
        clone = (CopyOnWriteArrayList<E>) super.clone();
        clone.data = (E[]) data.clone();
      }
    catch (CloneNotSupportedException e)
      {
        // Impossible to get here.
      }
    return clone;
  }

  /**
   * Returns an Object array containing all of the elements in this ArrayList.
   * The array is independent of this list.
   * 
   * @return an array representation of this list
   */
  public Object[] toArray()
  {
    E[] data = this.data;
    E[] array = (E[]) new Object[data.length];
    System.arraycopy(data, 0, array, 0, data.length);
    return array;
  }

  /**
   * Returns an Array whose component type is the runtime component type of the
   * passed-in Array. The returned Array is populated with all of the elements
   * in this ArrayList. If the passed-in Array is not large enough to store all
   * of the elements in this List, a new Array will be created and returned; if
   * the passed-in Array is <i>larger</i> than the size of this List, then
   * size() index will be set to null.
   * 
   * @param a
   *          the passed-in Array
   * @return an array representation of this list
   * @throws ArrayStoreException
   *           if the runtime type of a does not allow an element in this list
   * @throws NullPointerException
   *           if a is null
   */
  public <T> T[] toArray(T[] a)
  {
    E[] data = this.data;
    if (a.length < data.length)
      a = (T[]) Array.newInstance(a.getClass().getComponentType(), data.length);
    else if (a.length > data.length)
      a[data.length] = null;
    System.arraycopy(data, 0, a, 0, data.length);
    return a;
  }

  /**
   * Retrieves the element at the user-supplied index.
   * 
   * @param index
   *          the index of the element we are fetching
   * @throws IndexOutOfBoundsException
   *           if index &lt; 0 || index &gt;= size()
   */
  public E get(int index)
  {
    return data[index];
  }

  /**
   * Sets the element at the specified index. The new element, e, can be an
   * object of any type or null.
   * 
   * @param index
   *          the index at which the element is being set
   * @param e
   *          the element to be set
   * @return the element previously at the specified index
   * @throws IndexOutOfBoundsException
   *           if index &lt; 0 || index &gt;= 0
   */
  public synchronized E set(int index, E e)
  {
    E result = data[index];
    E[] newData = data.clone();
    newData[index] = e;
    data = newData;
    return result;
  }

  /**
   * Appends the supplied element to the end of this list. The element, e, can
   * be an object of any type or null.
   * 
   * @param e
   *          the element to be appended to this list
   * @return true, the add will always succeed
   */
  public synchronized boolean add(E e)
  {
    E[] data = this.data;
    E[] newData = (E[]) new Object[data.length + 1];
    System.arraycopy(data, 0, newData, 0, data.length);
    newData[data.length] = e;
    this.data = newData;
    return true;
  }

  /**
   * Adds the supplied element at the specified index, shifting all elements
   * currently at that index or higher one to the right. The element, e, can be
   * an object of any type or null.
   * 
   * @param index
   *          the index at which the element is being added
   * @param e
   *          the item being added
   * @throws IndexOutOfBoundsException
   *           if index &lt; 0 || index &gt; size()
   */
  public synchronized void add(int index, E e)
  {
    E[] data = this.data;
    E[] newData = (E[]) new Object[data.length + 1];
    System.arraycopy(data, 0, newData, 0, index);
    newData[index] = e;
    System.arraycopy(data, index, newData, index + 1, data.length - index);
    this.data = newData;
  }

  /**
   * Removes the element at the user-supplied index.
   * 
   * @param index
   *          the index of the element to be removed
   * @return the removed Object
   * @throws IndexOutOfBoundsException
   *           if index &lt; 0 || index &gt;= size()
   */
  public synchronized E remove(int index)
  {
    if (index < 0 || index >= this.size())
      throw new IndexOutOfBoundsException("index = " +  index);
    
    E[] snapshot = this.data;
    E[] newData = (E[]) new Object[snapshot.length - 1];
    
    E result = snapshot[index];
    
    if (index > 0)
      System.arraycopy(snapshot, 0, newData, 0, index);
    
    System.arraycopy(snapshot, index + 1, newData, index,
                     snapshot.length - index - 1);
    
    this.data = newData;
    
    return result;
  }

  /**
   * Remove the first occurrence, if any, of the given object from this list,
   * returning <code>true</code> if the object was removed, <code>false</code>
   * otherwise.
   * 
   * @param element the object to be removed.
   * @return true if element was removed, false otherwise. false means also that
   * the underlying storage was unchanged after this operation concluded.
   */
  public synchronized boolean remove(Object element)
  {
    E[] snapshot = this.data;
    E[] newData = (E[]) new Object[snapshot.length - 1];
    
    // search the element to remove while filling the backup array
    // this way we can run this method in O(n)
    int elementIndex = -1;
    for (int i = 0; i < snapshot.length; i++)
      {
        if (equals(element, snapshot[i]))
          {
            elementIndex = i;
            break;
          }
        
        if (i < newData.length)
          newData[i] = snapshot[i];
      }
    
    if (elementIndex < 0)
      return false;
     
    System.arraycopy(snapshot, elementIndex + 1, newData, elementIndex,
                     snapshot.length - elementIndex - 1);
    this.data = newData;
    
    return true;
  }
  
  /**
   * Removes all the elements contained in the given collection.
   * This method removes the elements that are contained in both
   * this list and in the given collection.
   * 
   * @param c the collection containing the elements to be removed from this
   * list.
   * @return true if at least one element was removed, indicating that
   * the list internal storage changed as a result, false otherwise. 
   */
  public synchronized boolean removeAll(Collection<?> c)
  {
    if (c.size() == 0)
      return false;
    
    E [] snapshot = this.data;
    E [] storage = (E[]) new Object[this.data.length]; 
    boolean changed = false;
    
    int length = 0;
    for (E element : snapshot)
      {
        // copy all the elements, including null values
        // if the collection can hold it
        // FIXME: slow operation
        if (c.contains(element))
          changed = true;
        else
          storage[length++] = element;
      }
    
    if (!changed)
      return false;
    
    E[] newData = (E[]) new Object[length];
    System.arraycopy(storage, 0, newData, 0, length);
    
    this.data = newData;
    
    return true;
  }
  
  /**
   * Removes all the elements that are not in the passed collection.
   * If the collection is void, this method has the same effect of
   * <code>clear()</code>.
   * Please, note that this method is extremely slow (unless the argument has
   * <code>size == 0</code>) and has bad performance is both space and time
   * usage.
   * 
   * @param c the collection containing the elements to be retained by this
   * list.
   * @return true the list internal storage changed as a result of this
   * operation, false otherwise.
   */
  public synchronized boolean retainAll(Collection<?> c)
  {
    // if the given collection does not contain elements
    // we remove all the elements from our storage
    if (c.size() == 0)
      {
        this.clear();
        return true;
      }
    
    E [] snapshot = this.data;
    E [] storage = (E[]) new Object[this.data.length]; 
    
    int length = 0;
    for (E element : snapshot)
      {
        if (c.contains(element))
          storage[length++] = element;
      }
    
    // means we retained all the elements previously in our storage
    // we are running already slow here, but at least we avoid copying
    // another array and changing the internal storage
    if (length == snapshot.length)
      return false;
    
    E[] newData = (E[]) new Object[length];
    System.arraycopy(storage, 0, newData, 0, length);
    
    this.data = newData;
    
    return true;
  }
  
  /**
   * Removes all elements from this List
   */
  public synchronized void clear()
  {
    data = (E[]) new Object[0];
  }

  /**
   * Add each element in the supplied Collection to this List. It is undefined
   * what happens if you modify the list while this is taking place; for
   * example, if the collection contains this list. c can contain objects of any
   * type, as well as null values.
   * 
   * @param c
   *          a Collection containing elements to be added to this List
   * @return true if the list was modified, in other words c is not empty
   * @throws NullPointerException
   *           if c is null
   */
  public synchronized boolean addAll(Collection< ? extends E> c)
  {
    return addAll(data.length, c);
  }

  /**
   * Add all elements in the supplied collection, inserting them beginning at
   * the specified index. c can contain objects of any type, as well as null
   * values.
   * 
   * @param index
   *          the index at which the elements will be inserted
   * @param c
   *          the Collection containing the elements to be inserted
   * @throws IndexOutOfBoundsException
   *           if index &lt; 0 || index &gt; 0
   * @throws NullPointerException
   *           if c is null
   */
  public synchronized boolean addAll(int index, Collection< ? extends E> c)
  {
    if (index < 0 || index > this.size())
      throw new IndexOutOfBoundsException("index = " +  index);
    
    int csize = c.size();
    if (csize == 0)
      return false;
    
    E[] data = this.data;
    Iterator<? extends E> itr = c.iterator();
    
    E[] newData = (E[]) new Object[data.length + csize];
    
    // avoid this call at all if we were asked to put the elements at the
    // beginning of our storage
    if (index != 0)
      System.arraycopy(data, 0, newData, 0, index);
    
    int itemsLeft = index;
    
    for (E value : c)
      newData[index++] = value;
    
    // now copy the remaining elements
    System.arraycopy(data, itemsLeft, newData, 0, data.length - itemsLeft);
    
    this.data = newData;
    
    return true;
  }
  
  /**
   * Adds an element if the list does not contains it already.
   * 
   * @param val the element to add to the list.
   * @return true if the element was added, false otherwise.
   */
  public synchronized boolean addIfAbsent(E val)
  {
    if (contains(val))
      return false;
    add(val);
    return true;
  }

  /**
   * Adds all the element from the given collection that are not already
   * in this list.
   * 
   * @param c the Collection containing the elements to be inserted
   * @return true the list internal storage changed as a result of this
   * operation, false otherwise.
   */
  public synchronized int addAllAbsent(Collection<? extends E> c)
  {
    int size = c.size();
    if (size == 0)
      return 0;
    
    E [] snapshot = this.data;
    E [] storage = (E[]) new Object[size];
    
    size = 0;
    for (E val : c)
      {
        if (!this.contains(val))
          storage[size++] = val;
      }
    
    if (size == 0)
      return 0;
    
    // append storage to data
    E [] newData = (E[]) new Object[snapshot.length + size];
    
    System.arraycopy(snapshot, 0, newData, 0, snapshot.length);
    System.arraycopy(storage, 0, newData, snapshot.length, size);
    
    this.data = newData;
    
    return size;
  }

  /**
   * Return an Iterator containing the elements of this list.
   * The Iterator uses a snapshot of the state of the internal storage
   * at the moment this method is called and does <strong>not</strong> support
   * update operations, so no synchronization is needed to traverse the
   * iterator.
   * 
   * @return an Iterator containing the elements of this list in sequence.
   */
  public Iterator<E> iterator()
  {
    return new Iterator<E>()
    {
      E [] iteratorData = CopyOnWriteArrayList.this.data;
      int currentElement = 0;
      
      public boolean hasNext()
      {
        return (currentElement < iteratorData.length);
      }

      public E next()
      {
        return iteratorData[currentElement++];
      }

      public void remove()
      {
        throw new UnsupportedOperationException("updating of elements in " +
                                                "iterators is not supported " +
                                                "by this class");
      }
    };
  }
  
  /**
   * Return a ListIterator containing the elements of this list.
   * The Iterator uses a snapshot of the state of the internal storage
   * at the moment this method is called and does <strong>not</strong> support
   * update operations, so no synchronization is needed to traverse the
   * iterator.
   * 
   * @return a ListIterator containing the elements of this list in sequence.
   */
  public ListIterator<E> listIterator(final int index)
  {
    if (index < 0 || index > size())
      throw new IndexOutOfBoundsException("Index: " + index + ", Size:"
                                          + size());

    return new ListIterator<E>()
    {
      E [] iteratorData = CopyOnWriteArrayList.this.data;
      int currentElement = index;
      
      public void add(E o)
      {
        throw new UnsupportedOperationException("updating of elements in " +
                                                "iterators is not supported " +
                                                "by this class");
      }

      public boolean hasNext()
      {
        return (currentElement < iteratorData.length);
      }

      public boolean hasPrevious()
      {
        return (currentElement > 0);
      }

      public E next()
      {
        if (hasNext() == false)
          throw new java.util.NoSuchElementException();
        
        return iteratorData[currentElement++];
      }

      public int nextIndex()
      {
        return (currentElement + 1);
      }

      public E previous()
      {
        if (hasPrevious() == false)
          throw new java.util.NoSuchElementException();
        
        return iteratorData[--currentElement];
      }

      public int previousIndex()
      {
        return (currentElement - 1);
      }

      public void remove()
      {
        throw new UnsupportedOperationException("updating of elements in " +
                                                "iterators is not supported " +
                                                "by this class");
      }

      public void set(E o)
      {
        throw new UnsupportedOperationException("updating of elements in " +
                                                "iterators is not supported " +
                                                "by this class");
      }
      
    };
  }
  
  /**
   * Serializes this object to the given stream.
   * 
   * @param s
   *          the stream to write to
   * @throws IOException
   *           if the underlying stream fails
   * @serialData the size field (int), the length of the backing array (int),
   *             followed by its elements (Objects) in proper order.
   */
  private void writeObject(ObjectOutputStream s) throws IOException
  {
    // The 'size' field.
    s.defaultWriteObject();
    // We serialize unused list entries to preserve capacity.
    int len = data.length;
    s.writeInt(len);
    // it would be more efficient to just write "size" items,
    // this need readObject read "size" items too.
    for (int i = 0; i < data.length; i++)
      s.writeObject(data[i]);
  }

  /**
   * Deserializes this object from the given stream.
   * 
   * @param s
   *          the stream to read from
   * @throws ClassNotFoundException
   *           if the underlying stream fails
   * @throws IOException
   *           if the underlying stream fails
   * @serialData the size field (int), the length of the backing array (int),
   *             followed by its elements (Objects) in proper order.
   */
  private void readObject(ObjectInputStream s) throws IOException,
      ClassNotFoundException
  {
    // the `size' field.
    s.defaultReadObject();
    int capacity = s.readInt();
    data = (E[]) new Object[capacity];
    for (int i = 0; i < capacity; i++)
      data[i] = (E) s.readObject();
  }

  static final boolean equals(Object o1, Object o2)
  {
    return o1 == null ? o2 == null : o1.equals(o2);
  }
  
  Object[] getArray()
  {
    return data;
  }
}
