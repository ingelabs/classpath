/////////////////////////////////////////////////////////////////////////////
// Stack.java - Class that provides a Last In First Out (LIFO)
// datatype, known more commonly as a Stack
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

package java.util;

/**
 * Stack provides a Last In First Out (LIFO) data type, commonly known
 * as a Stack.  
 *
 * Stack itself extends Vector and provides the additional methods
 * for stack manipulation (push, pop, peek). 
 */
public class Stack extends Vector {
  
  /**
   * This constructor creates a new Stack, initially empty
   */
  public Stack() {
    super();
  }

  /**
   * Pushes an Object onto the top of the stack.  This method is effectively
   * the same as addElement(item)
   *
   * @param item the Object to push onto the stack
   * @returns the Object pushed onto the stack
   * @see java.util.Vector#addElement(java.util.Object)
   */
  public Object push(Object item) {
    addElement(item);
    return item;
  }
  
  /**
   * Pops an item from the stack and returns it.  The item popped is
   * removed from the Stack
   *
   * @returns the Object popped from the stack
   */
  public Object pop() {
    if (isEmpty()) throw new EmptyStackException();
    return remove(size() - 1);
  }

  /**
   * Returns the top Object on the stack without removing it
   *
   * @returns the top Object on the stack
   */
  public Object peek() {
    if (isEmpty()) throw new EmptyStackException();
    return lastElement();
  }

  /**
   * Tests if the stack is empty
   *
   * @returns true if the stack contains no items, false otherwise
   */
  public boolean empty() {
    return isEmpty();
  }

  /**
   * Returns the position of an Object on the stack, with the top
   * most Object being at position 1, and each Object deeper in the
   * stack at depth + 1
   *
   * @param o The object to search for
   * @returns The 1 based depth of the Object, or -1 if the Object 
   * is not on the stack.
   */
  public int search(Object o) {
    int idx=lastIndexOf(o);
    return ( idx == -1 ? -1 : elementCount - idx );
  }
}

  
  




