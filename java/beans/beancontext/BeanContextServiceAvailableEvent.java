/*
 * java.beans.beancontext.BeanContextServiceAvailableEvent: part of the Java Class Libraries project.
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
 * Event fired when new services become available through a <code>BeanContextServices</code>.
 *
 * @author John Keiser
 * @since JDK1.2
 * @see java.beans.beancontext.BeanContextServicesListener
 */

public class BeanContextServiceAvailableEvent extends BeanContextEvent {
	/**
	 * The <code>Class</code> representing the service which is now
	 * available.
	 */
	protected class serviceClass;

	/**
	 * Create a new service available event.
	 * @param services the <code>BeanContextServices</code> through
	 *        which the service is available.  This is also the source
	 *        of the event.
	 * @param serviceClass the service class that is now available.
	 */
	public BeanContextServiceAvailableEvent(BeanContextServices services, Class serviceClass) {
		super(serviceClass);
		this.services = services;
	}

	/**
	 * Get the current service selectors of the service class.
	 * This is identical to <code>getSourceAsBeanContextServices().getCurrentServiceSelectors(getServiceClass())</code>
	 * @return the current service selectors of the service class.
	 */
	public Iterator getCurrentServiceSelectors() {
		return getSourceAsBeanContextServices().getCurrentServiceSelectors(serviceClass);
	}

	/**
	 * Get the newly available service class.
	 * @return the service class.
	 */
	public Class getServiceClass() {
		return serviceClass;
	}

	/**
	 * Get the <code>BeanContextServices</code> through which the new service is available.
	 * @return the <code>BeanContextServices</code> through which the
	 *         new service is available.
	 */
	public BeanContextServices getSourceAsBeanContextServices() {
		return (BeanContextServices)getSource();
	}
}
