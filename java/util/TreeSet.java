/* TreeSet.java -- a class providing a TreeMap-backet SortedSet
   Copyright (C) 1999, 2000 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.util;

import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class provides a TreeMap-backed implementation of the 
 * SortedSet interface.
 *
 * Each element in the Set is a key in the backing TreeMap; each key
 * maps to a static token, denoting that the key does, in fact, exist.
 *
 * Most operations are O(log n).
 *
 * TreeSet is a part of the JDK1.2 Collections API.
 *
 * @author      Jon Zeppieri
 * @version     $Revision: 1.5 $
 * @modified    $Id: TreeSet.java,v 1.5 2000-03-15 21:59:22 rao Exp $
 */

public class TreeSet extends AbstractSet
  implements SortedSet, Cloneable, Serializable
{
  // INSTANCE VARIABLES -------------------------------------------------

  /** The TreeMap which backs this Set */
  transient SortedMap _oMap;
  static final long serialVersionUID = -2479143000061671589L;


  // CONSTRUCTORS -------------------------------------------------------

  /**
   * Construct a new TreeSet whose backing TreeMap using the "natural" ordering
   * of keys.
   */
  public TreeSet()
  {
    this((Comparator) null);
  }

  /** 
   * Construct a new TreeSet whose backing TreeMap uses the supplied 
   * Comparator.
   *
   * @param     oComparator      the Comparator this Set will use
   */
  public TreeSet(Comparator oComparator)
  {
    _oMap = new TreeMap(oComparator);
  }

  /** 
   * Construct a new TreeSet whose backing TreeMap uses the "natural"
   * orering of the keys and which contains all of the elements in the
   * supplied Collection.
   *
   * @param     oCollection      the new Set will be initialized with all
   *                             of the elements in this Collection
   */
  public TreeSet(Collection oCollection)
  {
    this((Comparator) null);
    addAll(oCollection);
  }

  /**
   * Construct a new TreeSet, using the same key ordering as the supplied
   * SortedSet and containing all of the elements in the supplied SortedSet.
   * This constructor runs in linear time.
   *
   * @param     oSortedSet       the new TreeSet will use this SortedSet's
   *                             comparator and will initialize itself
   *                             with all of the elements in this SortedSet
   */
  public TreeSet(SortedSet oSortedSet)
  {
    TreeMap oMap = new TreeMap(oSortedSet.comparator());
    _oMap = oMap;
    int i = 0;
    Map.Entry[] arEntries = new Map.Entry[oSortedSet.size()];
    Iterator itEntries = oSortedSet.iterator();

    while (itEntries.hasNext())
      arEntries[i++] = new BasicMapEntry(itEntries.next(), Boolean.TRUE);

    oMap._iSize = i;
    oMap.putAllLinear(arEntries);
  }

  TreeSet(SortedMap oMap)
  {
    _oMap = oMap;
  }

  // METHODS -----------------------------------------------------------

  /** 
   * Adds the spplied Object to the Set if it is not already in the Set;
   * returns true if the element is added, false otherwise
   *
   * @param       oObject       the Object to be added to this Set
   */
  public boolean add(Object oObject)
  {
    if (_oMap.containsKey(oObject))
    {
      return false;
    }
    else
    {
      internalAdd(_oMap, oObject);
      return true;
    }
  }
  
  /**
   * Adds all of the elements in the supplied Collection to this TreeSet.
   *
   * @param        oCollection     All of the elements in this Collection
   *                               will be added to the Set.
   *
   * @return       true if the Set is altered, false otherwise
   */
  public boolean addAll(Collection oCollection)
  {
    boolean boResult = false;
    Iterator itElements = oCollection.iterator();

    while (itElements.hasNext())
      boResult |= add(itElements.next());

    return boResult;
  }

  /**
   * Removes all elements in this Set.
   */
  public void clear()
  {
    _oMap.clear();
  }

  /** Returns a shallow copy of this Set. */
  public Object clone()
  {
    TreeSet oClone;

    try
    {
      oClone = (TreeSet) super.clone();
      oClone._oMap = new TreeMap(_oMap);
    }
    catch(CloneNotSupportedException e)
    {
      throw new InternalError(e.toString());
    }
    return oClone;
  }

  /** Returns this Set's comparator */
  public Comparator comparator()
  {
    return _oMap.comparator();
  }

  /** 
   * Returns true if this Set contains the supplied Object, 
   * false otherwise 
   *
   * @param       oObject        the Object whose existence in the Set is
   *                             being tested
   */
  public boolean contains(Object oObject)
  {
    return _oMap.containsKey(oObject);
  }

  /** Returns true if this Set has size 0, false otherwise */
  public boolean isEmpty()
  {
    return _oMap.isEmpty();
  }

  /** Returns the number of elements in this Set */
  public int size()
  {
    return _oMap.size();
  }

  /** 
   * If the supplied Object is in this Set, it is removed, and true is
   * returned; otherwise, false is returned.
   *
   * @param         oObject        the Object we are attempting to remove
   *                               from this Set
   */
  public boolean remove(Object oObject)
  {
    return (_oMap.remove(oObject) != null);
  }

  /** Returns the first (by order) element in this Set */
  public Object first()
  {
    return _oMap.firstKey();
  }

  /** Returns the last (by order) element in this Set */
  public Object last()
  {
    return _oMap.lastKey();
  }

  /**
   * Returns a view of this Set including all elements in the interval
   * [oFromElement, oToElement).
   *
   * @param       oFromElement  the resultant view will contain all
   *                            elements greater than or equal to this
   *                            element
   * @param       oToElement    the resultant view will contain all
   *                            elements less than this element
   */
  public SortedSet subSet(Object oFromElement, Object oToElement)
  {
    return new TreeSet(_oMap.subMap(oFromElement, oToElement));
  }

  /**
   * Returns a view of this Set including all elements less than oToElement
   *
   * @param       oToElement    the resultant view will contain all
   *                            elements less than this element
   */
  public SortedSet headSet(Object oToElement)
  {
    return new TreeSet(_oMap.headMap(oToElement));
  }

  /**
   * Returns a view of this Set including all elements greater than or
   * equal to oFromElement.
   *
   * @param       oFromElement  the resultant view will contain all
   *                            elements greater than or equal to this
   *                            element
   */
  public SortedSet tailSet(Object oFromElement)
  {
    return new TreeSet(_oMap.tailMap(oFromElement));
  }


  /** Returns in Iterator over the elements in this TreeSet */
  public Iterator iterator()
  {
    return _oMap.keySet().iterator();
  }

  private void writeObject(ObjectOutputStream oOut) throws IOException
  {
    Iterator itElements = iterator();

    oOut.writeObject(comparator());
    oOut.writeInt(size());

    while (itElements.hasNext())
      oOut.writeObject(itElements.next());
  }

  private void readObject(ObjectInputStream oIn) 
    throws IOException, ClassNotFoundException
  {
    int i;
    Map.Entry[] arEntries;
    TreeMap oMap;
    Comparator oComparator = (Comparator) oIn.readObject();
    int iSize = oIn.readInt();

    arEntries = new Map.Entry[iSize];

    for (i = 0; i < iSize; i++)
      arEntries[iSize] = new BasicMapEntry(oIn.readObject(), Boolean.TRUE);

    oMap = new TreeMap(oComparator);
    oMap._iSize = iSize;
    oMap.putAllLinear(arEntries);
    _oMap = oMap;
  }

  /** 
   * adds the supplied element to this Set; this private method is used
   * internally instead of add(), because add() can be overridden
   * to do unexpected things
   *
   * @param         o        the Object to add to this Set
   */
  private static final void internalAdd(Map oMap, Object oObject)
  {
    oMap.put(oObject, Boolean.TRUE);
  }
}
