/////////////////////////////////////////////////////////////////////////////
// Iterator.java -- Interface for iterating over collections
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

/**
 * An object which iterates over a collection. An Iterator is used to return the
 * items once only, in sequence, by successive calls to the next method. It is
 * also possible to remove elements from the underlying collection by using the
 * optional remove method. Iterator is intended as a replacement for the
 * Enumeration interface of previous versions of Java, which did not have the
 * remove method and had less conveniently named methods.
 */
public interface Iterator {

  /**
   * Tests whether there are elements remaining in the collection.
   *
   * @return true if there is at least one more element in the collection,
   *   that is, if the next call to next will not throw NoSuchElementException.
   */
  boolean hasNext();

  /**
   * Obtain the next element in the collection.
   *
   * @return the next element in the collection
   * @exception NoSuchElementException if there are no more elements
   */
  Object next();

  /**
   * Remove from the underlying collection the last element returned by next.
   * This method can be called only once after each call to next. It does not
   * affect what will be returned by subsequent calls to next. This operation is
   * optional, it may throw an UnsupportedOperationException.
   *
   * @exception IllegalStateException if next has not yet been called or remove
   *   has already been called since the last call to next.
   * @exception UnsupportedOperationException if this Iterator does not support
   *   the remove operation.
   */
  void remove();
}
