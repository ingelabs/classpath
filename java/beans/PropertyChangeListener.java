/*
 * java.beans.PropertyChangeListener: part of the Java Class Libraries project.
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
 ** PropertyChangeListener allows a class to monitor
 ** properties of a Bean for changes.<P>
 **
 ** A propertyChange() event will only be fired
 ** <EM>after</EM> the property has changed.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 ** @see java.beans.PropertyChangeSupport
 **/

public interface PropertyChangeListener {
	/** Fired after a Bean's property has changed.
	 ** @param e the change (containing the old and new values)
	 **/
	public abstract void propertyChange(PropertyChangeEvent e);
}
