/*
 * java.beans.Introspector: part of the Java Class Libraries project.
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

package java.beans;

import gnu.java.beans.*;
import java.util.*;
import java.lang.reflect.*;
import gnu.java.lang.*;

/**
 ** Introspector is the class that does the bulk of the
 ** design-time work in Java Beans.  Every class must have
 ** a BeanInfo in order for an RAD tool to use it; but, as
 ** promised, you don't have to write the BeanInfo class
 ** yourself if you don't want to.  All you have to do is
 ** call getBeanInfo() in the Introspector and it will use
 ** standard JavaBeans-defined method signatures to
 ** determine the information about your class.<P>
 **
 ** Don't worry about it too much, though: you can provide
 ** JavaBeans with as much customized information as you
 ** want, or as little as you want, using the BeanInfo
 ** interface (see BeanInfo for details).<P>
 **
 ** <STRONG>Order of Operations</STRONG><P>
 **
 ** When you call getBeanInfo(class c), the Introspector
 ** first searches for BeanInfo class to see if you
 ** provided any explicit information.  It searches for a
 ** class named <bean class name>BeanInfo in different
 ** packages, first searching the bean class's package
 ** and then moving on to search the beanInfoSearchPath.<P>
 **
 ** If it does not find a BeanInfo class, it acts as though
 ** it had found a BeanInfo class returning null from all
 ** methods (meaning it should discover everything through
 ** Introspection).  If it does, then it takes the
 ** information it finds in the BeanInfo class to be
 ** canonical (that is, the information speaks for its
 ** class as well as all superclasses).<P>
 **
 ** When it has introspected the class, calls
 ** getBeanInfo(c.getSuperclass) and adds that information
 ** to the information it has, not adding to any information
 ** it already has that is canonical.<P>
 **
 ** <STRONG>Introspection Design Patterns</STRONG><P>
 **
 ** When the Introspector goes in to read the class, it
 ** follows a well-defined order in order to not leave any
 ** methods unaccounted for.  Its job is to step over all
 ** of the public methods in a class and determine whether
 ** they are part of a property, an event, or a method (in
 ** that order).
 **
 **
 ** <STRONG>Properties:</STRONG><P>
 ** 
 ** <OL>
 ** <LI>If there is a <CODE>public boolean isXXX()</CODE>
 **     method, then XXX is a read-only boolean property.
 **     <CODE>boolean getXXX()</CODE> may be supplied in
 **     addition to this method, although isXXX() is the
 **     one that will be used in this case and getXXX()
 **     will be ignored.  If there is a
 **     <CODE>public void setXXX(boolean)</CODE> method,
 **     it is part of this group and makes it a read-write
 **     property.</LI>
 ** <LI>If there is a
 **     <CODE>public &lt;type&gt; getXXX(int)</CODE>
 **     method, then XXX is a read-only indexed property of
 **     type &lt;type&gt;.  If there is a
 **     <CODE>public void setXXX(int,&lt;type&gt;)</CODE>
 **     method, then it is a read-write indexed property of
 **     type &lt;type&gt;.  There may also be a
 **     <CODE>public &lt;type&gt;[] getXXX()</CODE> and a
 **     <CODE>public void setXXX(&lt;type&gt;)</CODE>
 **     method as well.</CODE></LI>
 ** <LI>If there is a
 **     <CODE>public void setXXX(int,&lt;type&gt;)</CODE>
 **     method, then it is a write-only indexed property of
 **     type &lt;type&gt;.  There may also be a
 **     <CODE>public &lt;type&gt;[] getXXX()</CODE> and a
 **     <CODE>public void setXXX(&lt;type&gt;)</CODE>
 **     method as well.</CODE></LI>
 ** <LI>If there is a
 **     <CODE>public &lt;type&gt; getXXX()</CODE> method,
 **     then XXX is a read-only property of type
 **     &lt;type&gt;.  If there is a
 **     <CODE>public void setXXX(&lt;type&gt;)</CODE>
 **     method, then it will be used for the property and
 **     the property will be considered read-write.</LI>
 ** <LI>If there is a
 **     <CODE>public void setXXX(&lt;type&gt;)</CODE>
 **     method, then as long as XXX is not already used as
 **     the name of a property, XXX is assumed to be a
 **     write-only property of type &lt;type&gt;.</LI>
 ** <LI>In all of the above cases, if the setXXX() method
 **     throws <CODE>PropertyVetoException</CODE>, then the
 **     property in question is assumed to be constrained.
 **     No properties are ever assumed to be bound.  See
 **     PropertyDescriptor for a description of bound and
 **     constrained properties.</LI>
 ** </OL>
 **
 ** <STRONG>Events:</STRONG><P>
 **
 ** If there is a pair of methods,
 ** <CODE>public void addXXX(&lt;type&gt;)</CODE> and
 ** <CODE>public void removeXXX(&lt;type&gt;)</CODE>, where
 ** XXX is the unqualified class name of &lt;type&gt;, and
 ** &lt;type&gt; is a descendant of
 ** <CODE>java.util.EventListener</CODE>, then the pair of
 ** methods imply that this Bean will fire events to
 ** listeners of type &lt;type&gt;.<P>
 **
 ** If the addXXX() method throws
 ** <CODE>java.util.TooManyListenersException</LI>, then
 ** the event set is assumed to be <EM>unicast</EM>.  See
 ** EventSetDescriptor for a discussion of unicast event
 ** sets.<P>
 **
 ** <STRONG>Methods:</STRONG><P>
 ** 
 ** Any public methods which were not used for Properties
 ** or Events are used as Methods.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 ** @see java.beans.BeanInfo
 **/

public class Introspector {
	static String[] beanInfoSearchPath = {"gnu.java.beans.info", "sun.beans.infos"};
	static Hashtable beanInfoCache = new Hashtable();
	static BeanInfo theSimpleBeanInfo = new SimpleBeanInfo();

	/** Get the BeanInfo for class <CODE>beanClass</CODE>,
	 ** first by looking for explicit information, next by
	 ** using standard design patterns to determine
	 ** information about the class.
	 ** @param beanClass the class to get BeanInfo about.
	 ** @return the BeanInfo object representing the class.
	 **/
	public static BeanInfo getBeanInfo(Class beanClass) {
		return getBeanInfo(beanClass,null);
	}

	/** Get the BeanInfo for class <CODE>beanClass</CODE>,
	 ** first by looking for explicit information, next by
	 ** using standard design patterns to determine
	 ** information about the class.  It crawls up the
	 ** inheritance tree until it hits <CODE>topClass</CODE>.
	 ** @param beanClass the Bean class.
	 ** @param stopClass the class to stop at.
	 ** @return the BeanInfo object representing the class.
	 **/
	public static BeanInfo getBeanInfo(Class beanClass, Class topClass) {
		synchronized(beanClass) {

		if(beanClass == null) {
			return null;
		}

		if(topClass == null) {
			BeanInfo cachedInfo;
			cachedInfo = (BeanInfo)beanInfoCache.get(beanClass);
			if(cachedInfo != null) {
				return cachedInfo;
			}
		}

		boolean findBeanDescriptor;
		boolean findProperties;
		boolean findEvents;
		boolean findMethods;
		boolean findExtraInfo;

		BeanInfo explicitInfo = findExplicitBeanInfo(beanClass);
		if(explicitInfo != null) {
			findBeanDescriptor = (explicitInfo.getBeanDescriptor() == null);
			findProperties = (explicitInfo.getPropertyDescriptors() == null);
			findEvents = (explicitInfo.getEventSetDescriptors() == null);
			findMethods = (explicitInfo.getMethodDescriptors() == null);
			findExtraInfo = (explicitInfo.getAdditionalBeanInfo() == null);
		} else {
			findBeanDescriptor = true;
			findProperties = true;
			findEvents = true;
			findMethods = true;
			findExtraInfo = true;
		}

		BeanInfo parentInfo;
		if((!findProperties && !findEvents && !findMethods) || beanClass.equals(topClass) || !findExtraInfo) {
			parentInfo = null;
		} else {
			parentInfo = getBeanInfo(beanClass.getSuperclass(),topClass);
			if(!findProperties || !findEvents || !findMethods) {
				parentInfo = copyBeanInfo(parentInfo);
			}
		}

		BeanInfoEmbryo currentInfo = new IntrospectionIncubator(beanClass).getBeanInfoEmbryo();
		
		if(!findBeanDescriptor) {
			currentInfo.setBeanDescriptor(explicitInfo.getBeanDescriptor());
		} else {
			currentInfo.setBeanDescriptor(new BeanDescriptor(beanClass, null));
		}

		if(!findExtraInfo) {
			currentInfo.setAdditionalBeanInfo(explicitInfo.getAdditionalBeanInfo());
		} else {
			if(parentInfo != null) {
				currentInfo.setParentBeanInfo(parentInfo);
			}
		}

		if(!findProperties) {
			currentInfo.setProperties(explicitInfo.getPropertyDescriptors());
		}

		if(!findEvents) {
			currentInfo.setEvents(explicitInfo.getEventSetDescriptors());
		}

		if(!findMethods) {
			currentInfo.setMethods(explicitInfo.getMethodDescriptors());
		}

		BeanInfo retval = currentInfo.getBeanInfo();
		if(topClass == null) {
			beanInfoCache.put(beanClass,retval);
		}
		return retval;

		} // end synchronized(beanClass)
	}

	/** Get the search path for BeanInfo classes.
	 ** @return the BeanInfo search path.
	 **/
	public static String[] getBeanInfoSearchPath() {
		return beanInfoSearchPath;
	}

	/** Set the search path for BeanInfo classes.
	 ** @param beanInfoSearchPath the new BeanInfo search
	 **        path.
	 **/
	public static void getBeanInfoSearchPath(String[] beanInfoSearchPath) {
		Introspector.beanInfoSearchPath = beanInfoSearchPath;
	}

	/** A helper method to convert a name to standard Java
	 ** naming conventions: anything with two capitals as the
	 ** first two letters remains the same, otherwise the
	 ** first letter is decapitalized.  URL = URL, I = i,
	 ** MyMethod = myMethod.
	 ** @param name the name to decapitalize.
	 ** @return the decapitalized name.
	 **/
	public static String decapitalize(String name) {
		try {
			if(!Character.isUpperCase(name.charAt(0))) {
				return name;
			} else {
				try {
					if(Character.isUpperCase(name.charAt(1))) {
						return name;
					} else {
						char[] c = name.toCharArray();
						c[0] = Character.toLowerCase(c[0]);
						return new String(c);
					}
				} catch(StringIndexOutOfBoundsException E) {
					char[] c = new char[1];
					c[0] = Character.toLowerCase(name.charAt(0));
					return new String(c);
				}
			}
		} catch(StringIndexOutOfBoundsException E) {
			return name;
		} catch(NullPointerException E) {
			return null;
		}
	}

	static BeanInfo copyBeanInfo(BeanInfo b) {
		java.awt.Image[] icons = new java.awt.Image[4];
		for(int i=1;i<=4;i++) {
			icons[i-1] = b.getIcon(i);
		}
		return new ExplicitBeanInfo(b.getBeanDescriptor(),b.getAdditionalBeanInfo(),
		                            b.getPropertyDescriptors(),b.getDefaultPropertyIndex(),
		                            b.getEventSetDescriptors(),b.getDefaultEventIndex(),
		                            b.getMethodDescriptors(),icons);
	}

	static BeanInfo findExplicitBeanInfo(Class beanClass) {
		try {
		try {
			return (BeanInfo)Class.forName(beanClass.getName()+"BeanInfo").newInstance();
		} catch(ClassNotFoundException E) {
		}
		String newName = ClassHelper.getTruncatedClassName(beanClass) + "BeanInfo";
		for(int i=0;i<beanInfoSearchPath.length;i++) {
			try {
				if(beanInfoSearchPath[i].equals("")) {
					return (BeanInfo)Class.forName(newName).newInstance();
				} else {
					return (BeanInfo)Class.forName(beanInfoSearchPath[i] + "." + newName).newInstance();
				}
			} catch(ClassNotFoundException E) {
			}
		}
		} catch(IllegalAccessException E) {
		} catch(InstantiationException E) {
		}
		return null;
	}
}


