/*
 * java.beans.MethodDescriptor: part of the Java Class Libraries project.
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

package java.beans;

import java.lang.reflect.*;

/** MethodDescriptor describes information about a JavaBeans method.
 ** It's a fairly straightforward class (at least something in this
 ** package is straightforward!).
 **
 ** @author John Keiser
 ** @since JDK1.1
 ** @version 1.1.0, 26 Jul 1998
 **/
public class MethodDescriptor extends FeatureDescriptor {
	private Method m;
	private ParameterDescriptor[] parameterDescriptors;

	/** Create a new MethodDescriptor.
	 ** This method sets the name to the name of the method (Method.getName()).
	 ** @param m the method it will represent.
	 **/
	public MethodDescriptor(Method m) {
		setName(m.getName());
		this.m = m;
	}

	/** Create a new MethodDescriptor.
	 ** This method sets the name to the name of the method (Method.getName()).
	 ** @param m the method it will represent.
	 ** @param parameterDescriptors descriptions of the parameters (especially names).
	 **/
	public MethodDescriptor(Method m, ParameterDescriptor[] parameterDescriptors) {
		setName(m.getName());
		this.m = m;
		this.parameterDescriptors = parameterDescriptors;
	}

	/** Get the parameter descriptors from this method.
	 ** Since MethodDescriptor has no way of determining what
	 ** the parameter names were, this defaults to null.
	 **/
	public ParameterDescriptor[] getParameterDescriptors() {
		return parameterDescriptors;
	}

	/** Get the method this MethodDescriptor represents. **/
	public Method getMethod() {
		return m;
	}
}

