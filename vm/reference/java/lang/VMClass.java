/*
 * java.lang.Class: part of the Java Class Libraries project.
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

package java.lang;

import java.lang.reflect.*;

/**
 ** This is a package-private helper class for Class.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 6 1998
 ** @since JDK1.0
 **/

class VMClass {
	/** Get the name of this class, separated by dots for
	 ** package separators.
	 ** @return the name of this class.
	 ** @since JDK1.0
	 **/ 
	static String getName(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get whether this class is an interface or not.  Array
	 ** types are not interfaces.
	 ** @return whether this class is an interface or not.
	 ** @since JDK1.0
	 **/
	static boolean isInterface(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get the direct superclass of this class.  If this is
	 ** an interface, it will get the direct superinterface.
	 ** @return the direct superclass of this class.
	 ** @since JDK1.0
	 **/
	static Class getSuperclass(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get the interfaces this class <EM>directly</EM>
	 ** implements, in the order that they were declared.
	 ** This method may return an empty array, but will
	 ** never return null.
	 ** @return the interfaces this class directly implements.
	 ** @since JDK1.0
	 **/
	static Class[] getInterfaces(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get the ClassLoader that loaded this class.  If it was
	 ** loaded by the system classloader, this method will
	 ** return null.
	 ** @return the ClassLoader that loaded this class.
	 ** @since JDK1.0
	 **/
	static ClassLoader getClassLoader(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Use the system classloader to load and link a class.
	 ** @param name the name of the class to find.
	 ** @return the Class object representing the class.
	 ** @exception ClassNotFoundException if the class was not
	 **            found by the system classloader.
	 ** @since JDK1.0
	 **/
	static Class forName(String name) throws ClassNotFoundException {
		throw new UnsupportedOperationException();
	}

	/** Discover whether an Object is an instance of this
	 ** Class.  Think of it as almost like
	 ** <CODE>o instanceof (this class)</CODE>.
	 ** @param o the Object to check
	 ** @return whether o is an instance of this class.
	 ** @since JDK1.1
	 **/
	static boolean isInstance(Class c, Object o) {
		throw new UnsupportedOperationException();
	}

	/** Discover whether an instance of the Class parameter
	 ** would be an instance of this Class as well.  Think of
	 ** doing <CODE>isInstance(c.newInstance())</CODE> or even
	 ** <CODE>c instanceof (this class)</CODE>.
	 ** @param c the class to check
	 ** @return whether an instance of c would be an instance
	 **         of this class as well.
	 ** @since JDK1.1
	 **/
	static boolean isAssignableFrom(Class theClass, Class c) {
		throw new UnsupportedOperationException();
	}

	/** Return whether this class is a primitive type.  A
	 ** primitive type class is a class representing a kind of
	 ** "placeholder" for the various primitive types.  You
	 ** can access the various primitive type classes through
	 ** java.lang.Boolean.TYPE, java.lang.Integer.TYPE, etc.
	 ** @return whether this class is a primitive type.
	 ** @since JDK1.1
	 **/
	static boolean isPrimitive(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get the modifiers of this class.  These can be checked
	 ** against using java.lang.reflect.Modifier.
	 ** @return the modifiers of this class.
	 ** @see java.lang.reflect.Modifer
	 ** @since JDK1.1
	 **/
	static int getModifiers(Class c) {
		throw new UnsupportedOperationException();
	}

	/** If this is an inner class, return the class that
	 ** declared it.  If not, return null.
	 ** @return the declaring class of this class.
	 ** @since JDK1.1
	 **/
	static Class getDeclaringClass(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get all the public inner classes, declared in this
	 ** class or inherited from superclasses, that are
	 ** members of this class.
	 ** @return all public inner classes in this class.
	 **/
	static Class[] getClasses(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get all the inner classes declared in this class.
	 ** @return all inner classes declared in this class.
	 **/
	static Class[] getDeclaredClasses(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get a public constructor from this class.
	 ** @param args the argument types for the constructor.
	 ** @return the constructor.
	 ** @exception NoSuchMethodException if the constructor does
	 **            not exist.
	 **/
	static Constructor getConstructor(Class c, Class[] args) throws NoSuchMethodException {
		throw new UnsupportedOperationException();
	}

	/** Get a constructor declared in this class.
	 ** @param args the argument types for the constructor.
	 ** @return the constructor.
	 ** @exception NoSuchMethodException if the constructor does
	 **            not exist in this class.
	 **/
	static Constructor getDeclaredConstructor(Class c, Class[] args) throws NoSuchMethodException {
		throw new UnsupportedOperationException();
	}

	/** Get all public constructors from this class.
	 ** @return all public constructors in this class.
	 **/
	static Constructor[] getConstructors(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get all constructors declared in this class.
	 ** @return all constructors declared in this class.
	 **/
	static Constructor[] getDeclaredConstructors(Class c) {
		throw new UnsupportedOperationException();
	}


	/** Get a public method from this class.
	 ** @param name the name of the method.
	 ** @param args the argument types for the method.
	 ** @return the method.
	 ** @exception NoSuchMethodException if the method does
	 **            not exist.
	 **/
	static Method getMethod(Class c, String name, Class[] args) throws NoSuchMethodException {
		throw new UnsupportedOperationException();
	}

	/** Get a method declared in this class.
	 ** @param name the name of the method.
	 ** @param args the argument types for the method.
	 ** @return the method.
	 ** @exception NoSuchMethodException if the method does
	 **            not exist in this class.
	 **/
	static Method getDeclaredMethod(Class c, String name, Class[] args) throws NoSuchMethodException {
		throw new UnsupportedOperationException();
	}

	/** Get all public methods from this class.
	 ** @return all public methods in this class.
	 **/
	static Method[] getMethods(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get all methods declared in this class.
	 ** @return all methods declared in this class.
	 **/
	static Method[] getDeclaredMethods(Class c) {
		throw new UnsupportedOperationException();
	}


	/** Get a public field from this class.
	 ** @param name the name of the field.
	 ** @return the field.
	 ** @exception NoSuchFieldException if the field does
	 **            not exist.
	 **/
	static Field getField(Class c, String name) throws NoSuchFieldException {
		throw new UnsupportedOperationException();
	}

	/** Get a field declared in this class.
	 ** @param name the name of the field.
	 ** @return the field.
	 ** @exception NoSuchFieldException if the field does
	 **            not exist in this class.
	 **/
	static Field getDeclaredField(Class c, String name) throws NoSuchFieldException {
		throw new UnsupportedOperationException();
	}

	/** Get all public fields from this class.
	 ** @return all public fields in this class.
	 **/
	static Field[] getFields(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get all fields declared in this class.
	 ** @return all fieilds declared in this class.
	 **/
	static Field[] getDeclaredFields(Class c) {
		throw new UnsupportedOperationException();
	}

	/** Get a class for Integer.TYPE, Byte.TYPE, etc.
	 ** using the name of the primitive type.  This will
	 ** only be called once for each time a native class
	 ** like java.lang.Integer is initialized.<P>
	 ** <B>Note:</B> if there are multiple classloaders,
	 ** this method could be called more than once for a
	 ** given type.
	 ** @param name the name of the type, i.e. "int", "byte",
	 **             etc.
	 **/
	static Class getPrimitiveClass(String name) {
		throw new UnsupportedOperationException();
	}
}
