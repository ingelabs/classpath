/*
 * java.beans.PropertyVetoException: part of the Java Class Libraries project.
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
 ** PropertyVetoException is thrown when a VetoableChangeListener doesn't like the proposed change.
 **
 ** @author John Keiser
 ** @version 1.1.0, 31 May 1998
 ** @see java.beans.VetoableChangeListener
 **/

public class PropertyVetoException extends Exception {
	PropertyChangeEvent changeEvent;

	/** Instantiate this exception with the given message and property change.
	 ** @param msg the reason for the veto.
	 ** @param changeEvent the PropertyChangeEvent that was thrown.
	 **/
	public PropertyVetoException(String msg, PropertyChangeEvent changeEvent) {
		super(msg);
		this.changeEvent = changeEvent;
	}

	/** Get the PropertyChange event that was vetoed. **/
	public PropertyChangeEvent getPropertyChangeEvent() {
		return changeEvent;
	}
}