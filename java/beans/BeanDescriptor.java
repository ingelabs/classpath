/*
 * java.beans.BeanDescriptor: part of the Java Class Libraries project.
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

import java.util.*;

/**
 ** BeanDescriptor describes general information about a Bean, plus
 ** stores the Bean's Class and it's customizer's Class.<P>
 **
 ** @author John Keiser
 ** @version 1.1.0, 31 May 1998
 **/

public class BeanDescriptor extends FeatureDescriptor {
	Class beanClass;
	Class customizerClass;

	/** Create a new BeanDescriptor with the given beanClass and
	 ** no customizer class.
	 ** @param beanClass the class of the Bean.
	 **/
	public BeanDescriptor(Class beanClass) {
		this(beanClass,null);
	}

	/** Create a new BeanDescriptor with the given bean class and
	 ** customizer class.
	 ** @param beanClass the class of the Bean.
	 ** @param customizerClass the class of the Bean's Customizer.
	 **/
	public BeanDescriptor(Class beanClass, Class customizerClass) {
		this.beanClass = beanClass;
		this.customizerClass = customizerClass;
	}

	/** Get the Bean's class. **/
	public Class getBeanClass() {
		return beanClass;
	}

	/** Get the Bean's customizer's class. **/
	public Class getCustomizerClass() {
		return customizerClass;
	}
}
