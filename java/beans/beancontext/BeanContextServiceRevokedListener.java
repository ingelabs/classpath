/*
 * java.beans.beancontext.BeanContextServiceRevokedListener: part of the Java Class Libraries project.
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

import java.util.EventListener;

/**
 * Listens for service revoke events.
 *
 * @author John Keiser
 * @since JDK1.2
 */

public interface BeanContextServiceRevokedListener extends EventListener {
	/**
	 * Called by <code>BeanContextServices.revokeService()</code>.
	 * <B>Assumption:</B> If you have a reference to such a service,
	 * it should be discarded and may no longer function properly.
	 * <code>getService()</code> will no longer work on the specified
	 * service class after this event has been fired.
	 *
	 * @param event the service revoked event.
	 * @see java.beans.beancontext.BeanContextServices#revokeService(java.lang.Class,java.beans.beancontext.BeanContextServiceProvider,boolean)
	 */
	public void serviceRevoked(BeanContextServiceRevokedEvent event);
}