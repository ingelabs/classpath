/////////////////////////////////////////////////////////////////////////////
// Vector.java -- Class that provides growable arrays.
//
// This is a JDK 1.2 compliant version of Vector.java
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

// TODO:
//  toArray(Array) seems to be broken when creating a new
//    array of the same runtime type.  Not sure exactly how to do this.

package java.util;

/**
 * the <b>Vector</b> classes implements growable arrays of Objects.
 * You can access elements in a Vector with an index, just as you
 * can in a built in array, but Vectors can grow and shrink to accomodate
 * more or fewer objects.  
 *
 * Vectors try to mantain efficiency in growing by having a
 * <b>capacityIncrement</b> that can be specified at instantiation.
 * When a Vector can no longer hold a new Object, it grows by the amount
 * in <b>capacityIncrement</b>.  
 *
 * Vector implements the JDK 1.2 List interface, and is therefor a fully
 * compliant Collection object.
 * 
 */
public class Vector extends AbstractList implements List, 
    Cloneable, java.io.Serializable {
    
  /**
   * The amount the Vector's internal array should be increased in size when
   * a new element is added that exceeds the current size of the array,
   * or when @see #ensureCapacity is called.
   */
  protected int capacityIncrement;

  /**
   * The number of elements currently in the vector, also returned by
   * @see #size.
   */
  protected int elementCount;

  /**
   * The internal array used to hold members of a Vector
   */
  protected Object[] elementData;

  /**
   * Constructs an empty vector with an initial size of 10, and
   * a capacity increment of 0
   */
  public Vector() {
    this(10, 0);
  }
  
  /**
   * Constructs a vector containing the contents of Collection, in the
   * order given by the collection
   *
   * @param c A collection of elements to be added to the newly constructed
   * vector
   */
  public Vector(Collection c) {
    this(c.size(), 0);
    elementCount = elementData.length;
    Iterator collectionIterator=c.iterator();
    int counter = 0;
    while (collectionIterator.hasNext()) {
      elementData[counter++] = collectionIterator.next();
    }
  }

  /**
   * Constructs a Vector with the initial capacity and capacity 
   * increment specified
   *
   * @param initialCapacity The initial size of the Vector's internal
   * array
   * @param capacityIncrement The amount the internal array should be
   * increased if necessary
   */
  public Vector(int initialCapacity, int capacityIncrement) {
    modCount=0;
    elementData = new Object[initialCapacity];
    this.capacityIncrement = capacityIncrement;
    elementCount = 0;
  }

  /**
   * Constructs a Vector with the initial capacity specified
   *
   * @param initialCapacity The initial size of the Vector's internal array
   */
  public Vector(int initialCapacity) {
    this(initialCapacity, 0);
  }

  /**
   * Copies the contents of a provided array into the Vector.  If the 
   * array is too large to fit in the Vector, an IndexOutOfBoundsException
   * is thrown.  Old elements in the Vector are overwritten by the new
   * elements
   *
   * @param anArray An array from which elements will be copied into the Vector
   * 
   * @throws IndexOutOfBoundsException the array being copied
   * is larger than the Vectors internal data array
   */
  public void copyInto(Object[] anArray) {
      System.arraycopy(elementData, 0, anArray, 0, elementCount);
  }

  /**
   * Trims the Vector down to size.  If the internal data array is larger
   * than the number of Objects its holding, a new array is constructed
   * that precisely holds the elements.  
   */
  public void trimToSize() {
    // Check if the Vector is already trimmed, to save execution time
    if (elementCount == elementData.length) return;
    // Guess not

    Object[] newArray = new Object[elementCount];
    System.arraycopy(elementData, 0, newArray, 0, elementCount);
    elementData = newArray;
  }

  /**
   * Ensures that <b>minCapacity</b> elements can fit within this Vector.
   * If it cannot hold this many elements, the internal data array is expanded
   * in the following manner.  If the current size plus the capacityIncrement
   * is sufficient, the internal array is expanded by capacityIncrement.  
   * If capacityIncrement is non-positive, the size is doubled.  If 
   * neither is sufficient, the internal array is expanded to size minCapacity
   *
   * @param minCapacity The minimum capacity the internal array should be
   * able to handle after executing this method
   */
  public void ensureCapacity(int minCapacity) {
    if (elementData.length >= minCapacity) return;

    int newCapacity = elementData.length + capacityIncrement;
    if (capacityIncrement <= 0) newCapacity = elementData.length * 2;
   
    Object[] newArray = new Object[Math.max(newCapacity, minCapacity)];

    System.arraycopy(elementData, 0, newArray, 0, elementData.length);
    elementData = newArray;
  }
      
  /**
   * Explicitly sets the size of the internal data array, copying the 
   * old values to the new internal array.  If the new array is smaller
   * than the old one, old values that don't fit are lost.
   *
   * @param newSize The new size of the internal array
   */
  public void setSize(int newSize) {
    if (newSize<elementCount) {
      modCount++;
      elementCount=newSize;
    }
    Object[] newArray = new Object[newSize];
    System.arraycopy(elementData, 0, newArray, 0, 
		     Math.min(newSize, elementData.length));
    elementData=newArray;
  }
  
  /**
   * Returns the size of the internal data array (not the amount of elements
   * contained in the Vector)
   *
   * @returns capacity of the internal data array
   */
  public int capacity() {
    return elementData.length;
  }

  /**
   * Returns the number of elements stored in this Vector
   *
   * @returns the number of elements in this Vector
   */
  public int size() {
    return elementCount;
  }

  /**
   * Returns true if this Vector is empty, false otherwise
   *
   * @returns true if the Vector is empty, false otherwise
   */
  public boolean isEmpty() {
    return elementCount == 0;
  }

  /**
   * Searches the vector starting at <b>index</b> for object <b>elem</b>
   * and returns the index of the first occurence of this Object.  If
   * the object is not found, -1 is returned
   *
   * @param elem The Object to search for
   * @param index Start searching at this index
   * @returns The index of the first occurence of <b>elem</b>, or -1
   * if it is not found
   */
  public int indexOf(Object elem, int index) {
    for (int i=index; i < elementCount; i++) {
      if (elementData[i].equals(elem)) return i;
    }
    return -1;
  }

  /**
   * Returns the first occurence of <b>elem</b> in the Vector, or -1 if
   * <b>elem</b> is not found.
   *
   * @param elem The object to search for
   * @returns The index of the first occurence of <b>elem</b> or -1 if 
   * not found
   */
  public int indexOf(Object elem) {
    return indexOf(elem, 0);
  }

  /**
   * Returns true if <b>elem</b> is contained in this Vector, false otherwise.
   *
   * @param elem The element to check
   * @returns true if the object is contained in this Vector, false otherwise
   */
  public boolean contains(Object elem) {
    return indexOf(elem, 0) != -1;
  }

  /**
   * Returns the index of the first occurence of <b>elem</b>, when searching
   * backwards from <b>index</b>.  If the object does not occur in this Vector,
   * -1 is returned.
   *
   * @param elem The object to search for
   * @param index The index to start searching in reverse from
   * @returns The index of the Object if found, -1 otherwise
   */
  public int lastIndexOf(Object elem, int index) {
    if (index > elementCount - 1) 
      throw new ArrayIndexOutOfBoundsException(index);
    for (int i = index; i>=0; i--) {
      if ((elementData[i]==elem) ||
	  elementData[i].equals(elem)) return i;
    }
    return -1;
  }

  /**
   * Returns the last index of <b>elem</b> within this Vector, or -1
   * if the object is not within the Vector
   *
   * @param elem The object to search for
   * @returns the last index of the object, or -1 if not found
   */
  public int lastIndexOf(Object elem) {
    return lastIndexOf(elem, elementCount - 1);
  }

  /**
   * Returns the Object stored at <b>index</b>.  If index is out of range
   * an ArrayIndexOutOfBoundsException is thrown.
   *
   * @param index the index of the Object to retrieve
   * @returns The object at <b>index</b>
   * @throws ArrayIndexOutOfBoundsException <b>index</b> is
   * larger than the Vector
   */
  public Object elementAt(int index) {
    //Within the bounds of this Vector does not necessarily mean within 
    //the bounds of the internal array
    if (index >= elementCount) 
      throw new ArrayIndexOutOfBoundsException(index);
    
    return elementData[index];
  }

  /**
   * Returns the first element in the Vector.  If there is no first Object 
   * (The vector is empty), a NoSuchElementException is thrown.
   *
   * @returns The first Object in the Vector
   * @throws NoSuchElementException the Vector is empty
   */
  public Object firstElement() {
    if (isEmpty()) throw new NoSuchElementException();

    return elementAt(0);
  }

  /**
   * Returns the last element in the Vector.  If the Vector has no last element
   * (The vector is empty), a NoSuchElementException is thrown.
   *
   * @returns The last Object in the Vector
   * @throws NoSuchElementException the Vector is empty
   */
  public Object lastElement() {
    if (isEmpty()) throw new NoSuchElementException();

    return elementAt(elementCount - 1);
  }

  /**
   * Places <b>obj</b> at <b>index</b> within the Vector.  If <b>index</b>
   * refers to an index outside the Vector, an ArrayIndexOutOfBoundsException
   * is thrown.  
   * 
   * @param obj The object to store
   * @param index The position in the Vector to store the object
   * @throws ArrayIndexOutOfBoundsException the index is out of range
   */
  public void setElementAt(Object obj, int index) {
    if ((index < 0) ||
	(index >= elementCount)) 
      throw new ArrayIndexOutOfBoundsException(index);

    modCount++;
    elementData[index] = obj;
  }

  /**
   * Puts <b>element</b> into the Vector at position <b>index</b> and returns
   * the Object that previously occupied that position.
   *
   * @param index The index within the Vector to place the Object
   * @param element The Object to store in the Vector
   * @returns The previous object at the specified index
   * @throws ArrayIndexOutOfBoundsException the index is out of range
   */
  public Object set(int index, Object element) {
    if ((index < 0) ||
	(index >= elementCount)) 
      throw new ArrayIndexOutOfBoundsException(index);
    
    modCount++;
    Object temp = elementData[index];
    elementData[index] = element;
    return temp;
  }

  /**
   * Removes the element at <b>index</b>, and shifts all elements at
   * positions greater than index to their index - 1.  
   *
   * @param index The index of the element to remove
   */
  public void removeElementAt(int index) {
    if (index >= elementCount) 
      throw new ArrayIndexOutOfBoundsException(index);    
    modCount++;
    if (index < elementCount - 1) 
      System.arraycopy(elementData, index + 1, elementData, index, 
		       elementCount - (index + 1));
    //Delete the last element (which has been copied back one index)
    //so it can be garbage collected;
    elementData[(elementCount--)-1]=null;
  }

  /**
   * Inserts a new element into the Vector at <b>index</b>.  Any elements
   * at or greater than index are shifted up one position.
   *
   * @param obj The object to insert
   * @param index The index at which the object is inserted
   */
  public void insertElementAt(Object obj, int index) {
    if ((index < 0) ||
	(index > elementCount)) 
      throw new ArrayIndexOutOfBoundsException(index);

    if (index == elementCount - 1) {
      addElement(obj);
    } else {
      ensureCapacity(++elementCount);
      modCount++;
      System.arraycopy(elementData, index, elementData, index + 1, 
		       (elementCount - index) - 1);
      elementData[index] = obj;
    }
  }

  /**
   * Adds an element to the Vector at the end of the Vector.  If the vector
   * cannot hold the element with its present capacity, its size is increased
   * based on the same rules followed if ensureCapacity was called with the
   * argument currentSize+1.
   *
   * @param obj The object to add to the Vector
   */
  public void addElement(Object obj) {
    ensureCapacity(elementCount + 1);
    modCount++;
    elementData[elementCount++] = obj;
  }

  /**
   * Removes the first occurence of the given object from the Vector.
   * If such a remove was performed (the object was found), true is returned.
   * If there was no such object, false is returned.
   *
   * @param obj The object to remove from the Vector
   * @returns true if the Object was in the Vector, false otherwise
   */
  public boolean removeElement(Object obj) {
    int ix = indexOf(obj);
    if (ix != -1) {
      removeElementAt(ix);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Removes all elements from the Vector.  Note that this does not
   * resize the internal data array.
   */
  public void removeAllElements() {
    if (size()!=0) modCount++;
    else return;

    for (int i = 0; i < elementData.length; i++) {
      elementData[i] = null;
    }
    elementCount = 0;
  }

  /**
   * Creates a new Vector with the same contents as this one.
   */
  public Object clone() {
    return new Vector(this);
  }

  /**
   * Returns an Object array with the contents of this Vector, in the order
   * they are stored within this Vector.  Note that the Object array returned 
   * is not the internal data array, and that it holds only the elements 
   * within the Vector.  This is similar to creating a new Object[] with the 
   * size of this Vector, then calling Vector.copyInto(yourArray).
   *
   * @returns An Object[] containing the contents of this Vector in order
   *
   */
  public Object[] toArray() {
    Object[] newArray = new Object[elementCount];
    copyInto(newArray);
    return newArray;
  }

  /**
   * Returns an array containing the contents of this Vector.  
   * If the provided array is large enough, the contents are copied
   * into that array, and a null is placed in the position size(). 
   * In this manner, you can obtain the size of a Vector by the position
   * of the null element.  If the type of the provided array cannot 
   * hold the elements, an ArrayStoreException is thrown.
   * 
   * If the provided array is not large enough,
   * a new one is created with the contents of the Vector, and no null 
   * element.  The new array is of the same runtime type as the provided
   * array.
   *
   *
   * @param a An array to copy the Vector into if large enough
   * @returns An array with the contents of this Vector in order
   * @throws ArrayStoreException the runtime type of the provided array
   * cannot hold the elements of the Vector
   */
  public Object[] toArray(Object[] a) {
    if (a.length >= elementCount) {
      copyInto(a);
      if (a.length > elementCount) {
	a[elementCount] = null;
      }
      return a;
    } else {
      //This seems like a kludge.. Is there a better way to find the
      //Runtime type of an array?
      Object[] newArray = 
	(Object[])
	java.lang.reflect.Array.newInstance(a[0].getClass(), elementCount);
      copyInto(newArray);
      return newArray;
    }
  }

  /**
   * Returns the element at position <b>index</b>
   *
   * @param index the position from which an element will be retrieved
   * @throws ArrayIndexOutOfBoundsException the index is not within the 
   * range of the Vector
   */
  public Object get(int index) {
    return elementAt(index);
  }

  /**
   * Removes the given Object from the Vector.  If it exists, true
   * is returned, if not, false is returned.
   *
   * @param o The object to remove from the Vector
   * @returns true if the Object existed in the Vector, false otherwise
   */
  public boolean remove(Object o) {
    return removeElement(o);
  }

  /**
   * Adds an object at the specified index.  Elements at or above
   * index are shifted up one position.
   *
   * @param index The index at which to add the element
   * @param element The element to add to the Vector
   */
  public void add(int index, Object element) {
    modCount++;
    insertElementAt(element, index);
  }

  /**
   * Removes the element at the specified index, and returns it.
   *
   * @param index The position from which to remove the element
   * @returns The object removed
   * @throws ArrayIndexOutOfBoundsException the index was out of the range
   * of the Vector
   */
  public Object remove(int index) {
    modCount++;
    Object temp=elementData[index];
    removeElementAt(index);
    return temp;
  }  

  /**
   * Clears all elements in the Vector and sets its size to 0
   */
  public void clear() {
    removeAllElements();
  }

  /**
   * Returns true if the Vector contains every element within the Collection
   * <b>c</b>
   *
   * @param c The collection to search for within the Vector
   * @returns true if every element in the Collection is in this Vector
   */
  public boolean containsAll(Collection c) {
    for (Iterator i=c.iterator(); i.hasNext();) {
      if (!contains(i.next())) return false;
    }
    return true;
  }

  /**
   * Adds every element of the provided Collection to the Vector, in the
   * order provided by the Collection's iterator.  This overrides the
   * addAll in the AbstractList class in order to optimize for Vectors.
   *
   * @param c A collection to add to this Vector
   * @returns true per the Collections specification
   */
  public boolean addAll(Collection c) {
    modCount++;
    ensureCapacity(c.size());
    for (Iterator i=c.iterator(); i.hasNext();) {
      addElement(i.next());
    }
    return true; //?? API isn't clear on this
  }

  /**
   * Adds all the elements of Collection in order, at position <b>index</b>
   * 
   * @param index the position in the Vector at which the Collection
   * should be added
   * @param c The collection to add to the Vector
   */
  public boolean addAll(int index, Collection c) {
    modCount++;
    int idx=index;
    ensureCapacity(size() + c.size());
    if (index < elementCount) {
      System.arraycopy(elementData, index, elementData, 
		       index+c.size(), elementCount - index);
    }
    for (Iterator i=c.iterator(); i.hasNext();) {
      elementData[idx++]=i.next();
    }
    elementCount=size() + c.size();
    return true; //?? Again, not sure about this
  }

  /**
   * Removes all the elements of the provided Collection from the Vector.
   * If no elements were removed, returns false, otherwise true.
   *
   * @param c The collection to remove from this Vector
   * @returns true if the Vector was modified
   */
  public boolean removeAll(Collection c) {
    boolean result=false;
    for (Iterator i=c.iterator(); i.hasNext();) {
      result=remove(i.next()) || result;
    }
    if (result) modCount++;
    return result;
  }

  /**
   * Keeps only those elements that appear in the provided collection.  All
   * other elements are removed.  If all the elements in this Vector appear
   * within the collection, false is returned, otherwise true.
   *
   * @param c The collection of elements to retain within this Vector
   * @returns true if the Vector was modified, false otherwise
   */
  public boolean retainAll(Collection c) {
    boolean result=false;
    for (Iterator i=listIterator(); i.hasNext();) {
      Object temp=i.next();
      if (!c.contains(temp)) {
	i.remove();
	result=true;
      }
    }
    if (result) modCount++;
    return result;
  }

  /**
   * Returns a string representation of this Vector in the form 
   * [element0, element1, ... elementN]
   *
   * @returns the String representation of this Vector
   */
  public String toString() {
    String toReturn="[";
    for (int i=0; i < elementCount; i++) {
      toReturn+=elementData[i];
      if (i < elementCount - 1) toReturn+=", ";
    }
    return toReturn+"]";
  }

  /**
   * Returns an Enumeration of the elements of this List
   *
   * @returns an Enumeration
   */
  public Enumeration elements() {
    return java.util.Collections.enumeration(this);
  }

} // Vector

