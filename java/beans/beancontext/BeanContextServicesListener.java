/*
 * java.beans.beancontext.BeanContextServicesListener: part of the Java Class Libraries project.
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
 * Listens for service add and revoke events.
 *
 * @author John Keiser
 * @since JDK1.2
 */

public interface BeanContextServicesListener extends BeanContextServiceRevokedListener {
	/**
	 * Called by <code>BeanContextServices</code> whenever a service is made available.
	 *
	 * @param event the service revoked event, with useful information
	 *        about the new service.
	 */
	public void serviceRevoked(BeanContextServiceAvailableEvent event);
}