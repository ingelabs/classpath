/*
 * java.beans.VetoableChangeListener: part of the Java Class Libraries project.
 * Copyright (C) 1998 John Keiser
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.beans;

/**
 ** VetoableChangeListener allows a class to monitor
 ** proposed changes to properties of a Bean and, if
 ** desired, prevent them from occurring.<P>
 **
 ** A vetoableChange() event will be fired <EM>before</EM>
 ** the property has changed.  If any listener rejects the
 ** change by throwing the PropertyChangeException, a new
 ** vetoableChange() event will be fired to all listeners
 ** who received a vetoableChange() event in the first
 ** place informing them of a reversion to the old value.
 ** The value, of course, never actually changed.<P>
 **
 ** <STRONG>Note:</STRONG> This class may not be reliably
 ** used to determine whether a property has actually
 ** changed.  Use the PropertyChangeListener interface
 ** for that instead.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 ** @see java.beans.PropertyChangeListener
 ** @see java.beans.VetoableChangeSupport
 **/

public interface VetoableChangeListener {
	/** Fired before a Bean's property changes.
	 ** @param e the change (containing the old and new values)
	 ** @exception PropertyChangeException if the listener
	 **            does not desire the change to be made.
	 **/
	public abstract void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException;
}
