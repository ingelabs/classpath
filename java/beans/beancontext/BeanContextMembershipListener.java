/*
 * java.beans.beancontext.BeanContextMembershipListener: part of the Java Class Libraries project.
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
 * This is the interface to which <code>BeanContextMembershipEvent</code>s are sent.
 * This happens when children are added to or removed from a
 * <code>BeanContext</code>.
 *
 * @author John Keiser
 * @since JDK1.2
 */

public interface BeanContextMembershipListener extends EventListener {
	/**
	 * When beans are added to a <code>BeanContext</code>,
	 * this method is called to fire the event.
	 *
	 * @param event the event, including which children were added.
	 * @see java.beans.beancontext.BeanContext#add(java.lang.Object)
	 */
	public void childrenAdded(BeanContextMembershipEvent event);

	/**
	 * When beans are removed from a <code>BeanContext</code>,
	 * this method is called to fire the event.
	 *
	 * @param event the event, including which children were removed.
	 * @see java.beans.beancontext.BeanContext#remove(java.lang.Object)
	 */
	public void childrenRemoved(BeanContextMembershipEvent event);
}
