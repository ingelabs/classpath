/*
 * gnu.java.beans.BeanInfoEmbryo: part of the Java Class Libraries project.
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
import java.util.*;

/**
 ** A BeanInfoEmbryo accumulates information about a Bean
 ** while it is in the process of being created, and then
 ** when you are done accumulating the information, the
 ** getBeanInfo() method may be called to create a BeanInfo
 ** object based on the information.<P>
 **
 ** This class is not well-synchronized.  (It can be, it
 ** just isn't yet.)
 **
 ** @author John Keiser
 ** @version 1.1.0, 30 Jul 1998
 ** @see java.beans.BeanInfo
 **/

public class BeanInfoEmbryo {
	BeanDescriptor beanDescriptor;
	Vector additionalBeanInfo = new Vector();
	Hashtable properties = new Hashtable();
	Hashtable events = new Hashtable();
	Vector methods = new Vector();

	BeanInfo[] AadditionalBeanInfo;
	PropertyDescriptor[] Aproperties;
	EventSetDescriptor[] Aevents;
	MethodDescriptor[] Amethods;

	public BeanInfoEmbryo() {
	}

	public BeanInfo getBeanInfo() {
		if(additionalBeanInfo != null) {
			AadditionalBeanInfo = new BeanInfo[additionalBeanInfo.size()];
			additionalBeanInfo.copyInto(AadditionalBeanInfo);
		}
		if(properties != null) {
			Aproperties = new PropertyDescriptor[properties.size()];
			int i = 0;
			Enumeration enum = properties.elements();
			while(enum.hasMoreElements()) {
				Aproperties[i] = (PropertyDescriptor)enum.nextElement();
				i++;
			}
		}
		if(events != null) {
			Aevents = new EventSetDescriptor[events.size()];
			int i = 0;
			Enumeration enum = events.elements();
			while(enum.hasMoreElements()) {
				Aevents[i] = (EventSetDescriptor)enum.nextElement();
				i++;
			}
		}
		if(methods != null) {
			Amethods = new MethodDescriptor[methods.size()];
			methods.copyInto(Amethods);
		}
		return new ExplicitBeanInfo(beanDescriptor,AadditionalBeanInfo,Aproperties,-1,Aevents,-1,Amethods,null);
	}

	public void setBeanDescriptor(BeanDescriptor b) {
		beanDescriptor = b;
	}

	public void setAdditionalBeanInfo(BeanInfo[] b) {
		additionalBeanInfo = null;
		AadditionalBeanInfo = b;
	}
	public void addAdditionalBeanInfo(BeanInfo b) {
		additionalBeanInfo.addElement(b);
	}
	public void setParentBeanInfo(BeanInfo b) {
		BeanInfo[] ba = new BeanInfo[1];
		ba[0] = b;
		setAdditionalBeanInfo(ba);
	}

	public boolean hasProperty(String name) {
		return properties.get(name) != null;
	}
	public void setProperties(PropertyDescriptor[] p) {
		properties = null;
		Aproperties = p;
	}
	public void addProperty(PropertyDescriptor p) {
		properties.put(p.getName(),p);
	}
	public void addIndexedProperty(IndexedPropertyDescriptor p) {
		properties.put(p.getName(),p);
	}

	public boolean hasEvent(String name) {
		return events.get(name) != null;
	}
	public void addEvent(EventSetDescriptor e) {
		events.put(e.getName(),e);
	}
	public void setEvents(EventSetDescriptor[] e) {
		events = null;
		Aevents = e;
	}

	public void addMethod(MethodDescriptor m) {
		methods.addElement(m);
	}
	public void setMethods(MethodDescriptor[] m) {
		methods = null;
		Amethods = m;
	}
}
