/*
 * java.beans.beancontext.BeanContextServiceRevokedEvent: part of the Java Class Libraries project.
 * Copyright (C) 1999 Free Software Foundation
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

package java.beans.beancontext;

/**
 * Event fired when services are revoked from a <code>BeanContextServices</code>.
 *
 * @author John Keiser
 * @since JDK1.2
 * @see java.beans.beancontext.BeanContextServiceRevokedListener
 */

public class BeanContextServiceRevokedEvent extends BeanContextEvent {
	/**
	 * The <code>Class</code> representing the service which is now
	 * available.
	 */
	protected class serviceClass;
	private boolean revokeNow;

	/**
	 * Create a new service revoked event.
	 * @param services the <code>BeanContextServices</code> through
	 *        which the service was available.  This is also the source
	 *        of the event.
	 * @param serviceClass the service class that is now revoked.
	 * @param revokeNow whether the revocation is immediate for all
	 *        classes or just a suggestion.
	 */
	public BeanContextServiceRevokedEvent(BeanContextServices services, Class serviceClass, boolean revokeNow) {
		super(serviceClass);
		this.services = services;
		this.revokeNow = revokeNow;
	}

	/**
	 * Get the revoked service class.
	 * @return the service class.
	 */
	public Class getServiceClass() {
		return serviceClass;
	}

	/**
	 * Tell whether the revoked service class is the same as the specified class.
	 * Identical to <code>getServiceClass().equals(c)</code>.
	 * @param c the class to compare.
	 * @return whether the clases are equal.
	 */
	public boolean isServiceClass(Class c) {
		return serviceClass.equals(c);
	}

	/**
	 * Get the <code>BeanContextServices</code> through which the service was available.
	 * @return the <code>BeanContextServices</code> through which the
	 *         service was available.
	 */
	public BeanContextServices getSourceAsBeanContextServices() {
		return (BeanContextServices)getSource();
	}

	/**
	 * Tell whether current instances of the revoked service are usable or not.
	 * This is determined by whether the service was revoked
	 * immediately.
	 *
	 * @return whether current instances of the revoked service are
	 *         usable.
	 */
	public boolean isCurrentServiceInvalidNow() {
		return revokeNow;
	}
}
