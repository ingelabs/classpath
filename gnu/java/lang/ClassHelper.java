/*
 * gnu.java.lang.ClassHelper: part of the Java Class Libraries project.
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

package gnu.java.lang;

import java.util.*;
import java.lang.reflect.*;

/**
 ** ClassHelper has various methods that ought to have been
 ** in class.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 **/

public class ClassHelper {
	/** Strip the package part from the class name.
	 ** @param clazz the class to get the truncated name from
	 ** @return the truncated class name.
	 **/
	public static String getTruncatedClassName(Class clazz) {
		return getTruncatedName(clazz.getName());
	}
	/** Strip the package part from the class name, or the
	 ** class part from the method or field name.
	 ** @param name the name to truncate.
	 ** @return the truncated name.
	 **/
	public static String getTruncatedName(String name) {
		int lastInd = name.lastIndexOf('.');
		if(lastInd == -1) {
			return name;
		} else {
			return name.substring(lastInd+1);
		}
	}

	static Hashtable allMethods = new Hashtable();
	static Hashtable allMethodsAtDeclaration = new Hashtable();

	/** Get all the methods, public, private and
	 ** otherwise, from the class, getting them
	 ** from the most recent class to find them.
	 **/
	public static Method[] getAllMethods(Class clazz) {
		Method[] retval = (Method[])allMethods.get(clazz);
		if(retval == null) {
			Method[] superMethods;
			if(clazz.getSuperclass() != null) {
				superMethods = getAllMethods(clazz.getSuperclass());
			} else {
				superMethods = new Method[0];
			}
			Vector v = new Vector();
			Method[] currentMethods = clazz.getDeclaredMethods();
			for(int i=0;i<currentMethods.length;i++) {
				v.addElement(currentMethods[i]);
			}
			for(int i=0;i<superMethods.length;i++) {
				for(int j=0;j<currentMethods.length;j++) {
					if(!getTruncatedName(superMethods[i].getName()).equals(getTruncatedName(currentMethods[j].getName()))
					   || !ArrayHelper.equalsArray(superMethods[i].getParameterTypes(),currentMethods[j].getParameterTypes())) {
						v.addElement(superMethods[i]);
					}
				}
			}

			retval = new Method[v.size()];
			v.copyInto(retval);
			allMethods.put(clazz,retval);
		}
		return retval;
	}

	/** Get all the methods, public, private and
	 ** otherwise, from the class, and get them from
	 ** their point of declaration.
	 **/
	public static Method[] getAllMethodsAtDeclaration(Class clazz) {
		Method[] retval = (Method[])allMethods.get(clazz);
		if(retval == null) {
			Method[] superMethods;
			if(clazz.getSuperclass() != null) {
				superMethods = getAllMethodsAtDeclaration(clazz.getSuperclass());
			} else {
				superMethods = new Method[0];
			}
			Vector v = new Vector();
			Method[] currentMethods = clazz.getDeclaredMethods();
			for(int i=0;i<superMethods.length;i++) {
				v.addElement(superMethods[i]);
			}
			for(int i=0;i<superMethods.length;i++) {
				for(int j=0;j<currentMethods.length;j++) {
					if(!getTruncatedName(superMethods[i].getName()).equals(getTruncatedName(currentMethods[j].getName()))
					   || !ArrayHelper.equalsArray(superMethods[i].getParameterTypes(),currentMethods[j].getParameterTypes())) {
						v.addElement(currentMethods[i]);
					}
				}
			}

			retval = new Method[v.size()];
			v.copyInto(retval);
			allMethods.put(clazz,retval);
		}
		return retval;
	}
}
