/*
 * gnu.java.beans.ExplicitBeanInfo: part of the Java Class Libraries project.
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

package gnu.java.beans;

import java.beans.*;

/**
 ** ExplicitBeanInfo lets you specify in the constructor
 ** all the various parts of the BeanInfo.
 **
 ** @author John Keiser
 ** @version 1.1.0, 30 Jul 1998
 ** @see java.beans.BeanInfo
 **/

public class ExplicitBeanInfo implements BeanInfo {
	/** The BeanDescriptor returned by getBeanDescriptor. **/
	protected BeanDescriptor beanDescriptor;

	/** The EventSetDescriptor array returned by
	 ** getEventSetDescriptors().
	 **/
	protected EventSetDescriptor[] eventSetDescriptors = new EventSetDescriptor[0];

	/** The PropertyDescriptor array returned by
	 ** getPropertyDescriptors().
	 **/
	protected PropertyDescriptor[] propertyDescriptors = new PropertyDescriptor[0];

	/** The MethodDescriptor array returned by
	 ** getMethodDescriptors().
	 **/
	protected MethodDescriptor[]   methodDescriptors;

	/** The default property index. **/
	protected int defaultPropertyIndex;

	/** The default event index. **/
	protected int defaultEventIndex;

	/** The BeanInfo array returned by
	 ** getAdditionalBeanInfo().
	 **/
	protected BeanInfo[] additionalBeanInfo;

	/** The set of icons. **/
	protected java.awt.Image[] icons;

	public ExplicitBeanInfo(BeanDescriptor beanDescriptor,
	                        BeanInfo[] additionalBeanInfo,
	                        PropertyDescriptor[] propertyDescriptors,
				int defaultPropertyIndex,
	                        EventSetDescriptor[] eventSetDescriptors,
				int defaultEventIndex,
	                        MethodDescriptor[] methodDescriptors,
				java.awt.Image[] icons) {
		this.beanDescriptor = beanDescriptor;
		this.additionalBeanInfo = additionalBeanInfo;
		this.propertyDescriptors = propertyDescriptors;
		this.defaultPropertyIndex = defaultPropertyIndex;
		this.eventSetDescriptors = eventSetDescriptors;
		this.defaultEventIndex = defaultEventIndex;
		this.methodDescriptors = methodDescriptors;
		this.icons = icons;
	}

	/** Get Bean descriptor. **/
	public BeanDescriptor getBeanDescriptor() {
		return beanDescriptor;
	}

	/** Get Bean events. **/
	public EventSetDescriptor[] getEventSetDescriptors() {
		return eventSetDescriptors;
	}

	/** Get default event set. **/
	public int getDefaultEventIndex() {
		return defaultEventIndex;
	}

	/** Get Bean properties. **/
	public PropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	/** Get "default" property. **/
	public int getDefaultPropertyIndex() {
		return defaultPropertyIndex;
	}

	/** Get Bean methods. **/
	public MethodDescriptor[] getMethodDescriptors() {
		return methodDescriptors;
	}

	/** Get additional Bean info. **/
	public BeanInfo[] getAdditionalBeanInfo() {
		return additionalBeanInfo;
	}

	/** Get Bean icons.
	 ** @param iconType the type of icon
	 **/
	public java.awt.Image getIcon(int iconType) {
		return icons != null ? icons[iconType] : null;
	}
}