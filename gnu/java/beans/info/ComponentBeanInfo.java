/*
 * gnu.java.beans.info.ComponentBeanInfo: part of the Java Class Libraries project.
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

package gnu.java.beans.info;

import gnu.java.beans.*;
import java.beans.*;

/** BeanInfo class for java.awt.Component.
 ** This provides a few properties, but that's
 ** it.
 ** @author John Keiser
 ** @version 1.1.0, Aug 1 1998
 **/
public class ComponentBeanInfo extends SimpleBeanInfo {
	static PropertyDescriptor[] properties;
	static {
		try {
		properties = new PropertyDescriptor[6];
		properties[0] = new PropertyDescriptor("name",java.awt.Component.class);
		properties[1] = new PropertyDescriptor("background",java.awt.Component.class);
		properties[2] = new PropertyDescriptor("foreground",java.awt.Component.class);
		properties[3] = new PropertyDescriptor("font",java.awt.Component.class);
		properties[4] = new PropertyDescriptor("enabled",java.awt.Component.class);
		properties[5] = new PropertyDescriptor("visible",java.awt.Component.class);
		} catch(IntrospectionException E) {
			properties = null;
			throw new UnknownError("Could not introspect some java.awt.Component properties.");
		}
	}
	public ComponentBeanInfo() {
		super();
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		return properties;
	}
}

