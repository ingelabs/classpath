/* AccessibleSelection.java -- Java interface for aiding in accessibly rendering 
   other Java components
   Copyright (C) 2000 Free Software Foundation, Inc.

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

package javax.accessibility;

/**
 * Proper implementation of the methods of this interface should 
 * allow an application to determine the currently selected children
 * and modify the selection set.  If an object has children which are
 * selectable then it should implement this interface.
 * <p>
 * The <code>AccessibleContext.getAccessibleSelection()</code> method
 * should return an object which extends <code>AccessibleSelection</code> 
 * if it supports this interface.  If the method returns <code>null</code>
 * the object does not support this interface.
 *
 * @see AccessibleContext.getAccessibleText()
 */
public interface AccessibleSelection {

    /**
     * Select the specified child if it is not already 
     * selected, placing it in the objects current selection.
     * If the object does not support multiple selections
     * then the specified child placed in a new selection 
     * replacing the old selection.  If the specified
     * child is already selected, this method does nothing.
     *
     * @param i zero-based index of child objects
     */
    public abstract void addAccessibleSelection(int i);

    /**
     * Unselect all children of this Accessible object.
     */
    public abstract void clearAccessibleSelection();

    /**
     * Returns the Accessible child specified of this
     * Accessible object.
     *
     * @param i zero-based index of child objects
     * @return the Accessible child
     */
    public abstract Accessible getAccessibleSelection(int i);

    /**
     * Returns the number of currently selected Accessible 
     * children if any, else 0.
     * 
     * @return the number of selected children
     */
    public abstract int getAccessibleSelectionCount();

    /**
     * Determine if the specified child of this Accessible
     * object is selected.
     *
     * @param i zero-based index of child objects
     * @return true if specified child exists and is selected, else false
     */
    public abstract boolean isAccessibleChildSelected(int i);

    /**
     * Unselect the specified child of this Accessible object
     * if it is currently selected.
     *
     * @param i the zero-based index of the child objects
     */
    public abstract void removeAccessibleSelection(int i);

    /**
     * Select all children of this Accessible object
     * if the object supports multiple selections.
     */
    public abstract void selectAllAccessibleSelection();
}

