/*
 * gnu.java.beans.EmptyBeanInfo: part of the Java Class Libraries project.
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

package gnu.java.beans;

import java.beans.*;

/**
 ** EmptyBeanInfo is a BeanInfo that discloses no
 ** information about the Bean and does not allow
 ** Introspection.  The Introspector uses instances of this
 ** class to create empty BeanInfos, but it could also be
 ** used as a base class for BeanInfos that do not allow
 ** Introspection and provide only a little bit of
 ** information.<P>
 **
 ** @author John Keiser
 ** @version 1.1.0, 30 Jul 1998
 ** @see gnu.java.beans.ExplicitBeanInfo
 ** @see java.beans.BeanInfo
 **/

public class EmptyBeanInfo extends ExplicitBeanInfo {
	/** Create a new EmptyBeanInfo. **/
	public EmptyBeanInfo(Class beanClass) {
		super(new BeanDescriptor(beanClass,null),
		      new BeanInfo[0],
		      new PropertyDescriptor[0],
		      -1,
		      new EventSetDescriptor[0],
		      -1,
		      new MethodDescriptor[0],
		      null);
	}
}