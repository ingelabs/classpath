/*
 * java.beans.beancontext.BeanContextServiceProviderBeanInfo: part of the Java Class Libraries project.
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

import java.beans.BeanInfo;

/**
 * <code>BeanContextServiceProvider</code>s implement this to provide information about all of the services they provide.
 * <P>
 *
 * This is apparently so that you can import a bunch of services into a
 * RAD tool and it will know about all of them and export them to the
 * user in a readable manner.
 *
 * @author John Keiser
 * @since JDK1.2
 */
public interface BeanContextServiceProviderBeanInfo extends BeanInfo {
	/**
	 * Get <code>BeanInfo</code>s for all of the service classes of this <code>BeanInfoServiceProvider</code>.
	 * @return <code>BeanInfo</code>s for all provided service classes.
	 */
	public BeanInfo[] getServicesBeanInfo();
}