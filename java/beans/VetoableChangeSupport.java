/*
 * java.beans.VetoableChangeSupport: part of the Java Class Libraries project.
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
 ** VetoableChangeSupport makes it easy to fire vetoable
 ** change events and handle listeners as well as reversion
 ** of old values when things go wrong.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 **/

public class VetoableChangeSupport implements java.io.Serializable {
	java.util.Vector listeners = new java.util.Vector();
	Object bean;

	/** Create VetoableChangeSupport to work with a specific
	 ** source bean.
	 ** @param bean the source bean to use.
	 **/
	public VetoableChangeSupport(Object bean) {
		this.bean = bean;
	}

	/** Adds a VetoableChangeListener to the list of listeners.
	 ** @param l the listener to add.
	 **/
	public synchronized void addVetoableChangeListener(VetoableChangeListener l) {
		listeners.addElement(l);
	}

	/** Removes a VetoableChangeListener from the list of listeners.
	 ** @param l the listener to remove.
	 **/
	public synchronized void removeVetoableChangeListener(VetoableChangeListener l) {
		listeners.removeElement(l);
	}

	/** Fire a VetoableChangeEvent containing the old and new
	 ** values of the property to all the listeners.  If any
	 ** listener objects, a reversion event will be sent to
	 ** those listeners who received the initial event.
	 **
	 ** @param propertyName the name of the property that
	 ** changed.
	 ** @param oldVal the old value.
	 ** @param newVal the new value.
	 **/
	public synchronized void fireVetoableChange(String propertyName, Object oldVal, Object newVal) throws PropertyVetoException {
		PropertyChangeEvent proposedChange = new PropertyChangeEvent(bean,propertyName,oldVal,newVal);
		int currentListener=0;
		try {
			for(;currentListener<listeners.size();currentListener++) {
				((VetoableChangeListener)listeners.elementAt(currentListener)).vetoableChange(proposedChange);
			}
		} catch(PropertyVetoException e) {
			PropertyChangeEvent reversion = new PropertyChangeEvent(bean,propertyName,newVal,oldVal);
			for(int sendAgain=0;sendAgain<currentListener;sendAgain++) {
				try {
					((VetoableChangeListener)listeners.elementAt(sendAgain)).vetoableChange(reversion);
				} catch(PropertyVetoException e2) {
				}
			}
			throw e;
		}
	}
}
