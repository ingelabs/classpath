/////////////////////////////////////////////////////////////////////////////
// EmptyStackException.java -- Attempt to pop from an empty stack
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
 * This exception is thrown by the Stack class when an attempt is made to pop
 * or otherwise access elements from an empty stack.
 */
public class EmptyStackException extends RuntimeException {

  /**
   * Constructs an EmptyStackException with no detail message.
   */
  public EmptyStackException() {
    super();
  }
}
