/////////////////////////////////////////////////////////////////////////////
// Enumeration.java -- Interface for enumerating lists of objects
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
 * Interface for lists of objects that can be returned in sequence. Successive
 * objects are obtained by the nextElement method.
 * <P>
 * As of Java 1.2, the Iterator interface provides the same functionality, but
 * with shorter method names and a new optional method to remove items from the
 * list. If writing for 1.2, consider using Iterator instead. Enumerations over
 * the new collections classes, for use with legacy APIs that require them, can
 * be obtained by the enumeration method in class Collections.
 */
public interface Enumeration {

  /**
   * Tests whether there are elements remaining in the enumeration.
   *
   * @return true if there is at least one more element in the enumeration,
   *   that is, if the next call to nextElement will not throw a
   *   NoSuchElementException.
   */
  boolean hasMoreElements();

  /**
   * Obtain the next element in the enumeration.
   *
   * @return the next element in the enumeration
   * @exception NoSuchElementException if there are no more elements
   */
  Object nextElement();
}
