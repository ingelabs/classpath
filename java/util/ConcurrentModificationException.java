/////////////////////////////////////////////////////////////////////////////
// ConcurrentModificationException.java -- Data structure concurrently modified
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
 * Exception that is thrown by the collections classes when it is detected that
 * a modification has been made to a data structure when this is not allowed,
 * such as when a collection is structurally modified while an Iterator is
 * operating over it. In cases where this can be detected, a
 * ConcurrentModificationException will be thrown. An Iterator that detects this
 * condition is referred to as fail-fast.
 */
public class ConcurrentModificationException extends RuntimeException {
  private static final long serialVersionUID = -3666751008965953603L;

  /**
   * Constructs a ConcurrentModificationException with no detail message.
   */
  public ConcurrentModificationException() {
    super();
  }

  /**
   * Constructs a ConcurrentModificationException with a detail message.
   *
   * @param detail the detail message for the exception
   */
  public ConcurrentModificationException(String detail) {
    super(detail);
  }
}
