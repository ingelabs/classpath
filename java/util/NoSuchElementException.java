/////////////////////////////////////////////////////////////////////////////
// NoSuchElementException.java -- Attempt to access element that does not exist
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
 * Exception thrown when an attempt is made to access an element that does not
 * exist. This exception is thrown by the Enumeration, Iterator and ListIterator
 * classes if the nextElement, next or previous method goes beyond the end of
 * the list of elements that are being accessed.
 */
public class NoSuchElementException extends RuntimeException {

  /**
   * Constructs a NoSuchElementException with no detail message.
   */
  public NoSuchElementException() {
    super();
  }

  /**
   * Constructs a NoSuchElementException with a detail message.
   *
   * @param detail the detail message for the exception
   */
  public NoSuchElementException(String detail) {
    super(detail);
  }
}
