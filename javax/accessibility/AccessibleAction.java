/* AccessibleAction.java -- Java interface for aiding in accessibly determining
   what actions an object can perform
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
 * If an object implements this interface then it must be able to
 * perform one or more actions.  By implementing the interface it
 * is possible to discover what those actions are and perform those
 * actions.
 * <p>
 * The <code>AccessibleContext.getAccessibleAction()</code> method 
 * should return <code>null</code> if an object does not implement
 * this interface.
 * 
 * @see AccessibleContext#getAccessibleAction()
 */
public interface AccessibleAction {

    /**
     * Perform the specified action.
     *
     * @param i the action to perform.  Actions are counted starting with zero.
     * @return false if the action was not performed and true if was performed
     */
    public abstract boolean doAccessibleAction(int i);

    /**
     * Get the number possible actions for this object with the first
     * representing the default action.
     */
    public abstract int getAccessibleActionCount();

    /**
     * Get a description for the specified action.
     * 
     * @param i the action to describe.  Actions are counted starting with zero.
     * @return description of the action
     */
    public abstract String getAccessibleActionDescription(int i);
}
