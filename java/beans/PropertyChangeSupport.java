/*
 * java.beans.PropertyChangeSupport: part of the Java Class Libraries project.
 * Copyright (C) 1998 Free Software Foundation
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
 ** PropertyChangeSupport makes it easy to fire property
 ** change events and handle listeners.
 **
 ** @author John Keiser
 ** @since JDK1.1
 ** @version 1.1.0, 29 Jul 1998
 **/

public class PropertyChangeSupport implements java.io.Serializable {
	java.util.Vector listeners = new java.util.Vector();
	Object bean;

	/** Create PropertyChangeSupport to work with a specific
	 ** source bean.
	 ** @param bean the source bean to use.
	 **/
	public PropertyChangeSupport(Object bean) {
		this.bean = bean;
	}

	/** Adds a PropertyChangeListener to the list of listeners.
	 ** @param l the listener to add.
	 **/
	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addElement(l);
	}

	/** Removes a PropertyChangeListener from the list of listeners.
	 ** @param l the listener to remove.
	 **/
	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removeElement(l);
	}

	/** Fire a PropertyChangeEvent containing the old and new
	 ** values of the property to all the listeners.
	 ** @param propertyName the name of the property that
	 ** changed.
	 ** @param oldVal the old value.
	 ** @param newVal the new value.
	 **/
	public void firePropertyChange(String propertyName, Object oldVal, Object newVal) {
		PropertyChangeEvent e = new PropertyChangeEvent(bean,propertyName,oldVal,newVal);
		for(int i=0;i<listeners.size();i++) {
			((PropertyChangeListener)listeners.elementAt(i)).propertyChange(e);
		}
	}
}
