/*
 * java.lang.ClassLoader: part of the Java Class Libraries project.
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
import gnu.java.lang.*;
import java.io.*;
import java.net.*;

/**
 ** The ClassLoader is a way of customizing the way Java
 ** gets its classes and loads them into memory.  The
 ** verifier and other standard Java things still run, but
 ** the ClassLoader is allowed great flexibility in
 ** determining where to get the classfiles and when to
 ** load and resolve them.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 6 1998
 ** @since JDK1.0
 **/

public abstract class ClassLoader {
	/** Create a new ClassLoader.
	 ** @exception SecurityException if you do not have permission
	 **            to create a ClassLoader.
	 **/
	public ClassLoader() throws SecurityException {
		try {
			System.getSecurityManager().checkCreateClassLoader();
		} catch(NullPointerException e) {
		}
	}

	/** Load a class using this ClassLoader.
	 ** @param name the name of the class relative to this ClassLoader.
	 ** @exception ClassNotFoundException if the class cannot be found to
	 **            be loaded.
	 ** @return the loaded class.
	 **/
	public native Class loadClass(String name) throws ClassNotFoundException;

	/** Load a class using this ClassLoader, possibly resolving it as well
	 ** using resolveClass().
	 ** @param name the name of the class relative to this ClassLoader.
	 ** @param resolve whether or not to resolve the class.
	 ** @exception ClassNotFoundException if the class cannot be found to
	 **            be loaded.
	 ** @return the loaded class.
	 **/
	protected Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class c = loadClass(name);
		if(resolve) {
			resolveClass(c);
		}
		return c;
	}

	/** Get the URL to a resource using this classloader.
	 ** @param name the name of the resource relative to this
	 **        classloader.
	 ** @return the URL to the resource.
	 **/
	public URL getResource(String name) {
		return ClassLoader.getSystemResource(name);
	}

	/** Get a resource using this classloader.
	 ** @param name the name of the resource relative to this
	 **        classloader.
	 ** @return the resource.
	 **/
	public InputStream getResourceAsStream(String name) {
		return ClassLoader.getSystemResourceAsStream(name);
	}


	/** Helper to define a class using a string of bytes.
	 ** @param data the data representing the classfile, in classfile format.
	 ** @param offset the offset into the data where the classfile starts.
	 ** @param len the length of the classfile data in the array.
	 ** @return the class that was defined.
	 ** @deprecated use defineClass(String,...) instead.
	 **/
	protected final native Class defineClass(byte[] data, int offset, int len) throws ClassFormatError;

	/** Helper to define a class using a string of bytes.
	 ** @param name the name to give the class.
	 ** @param data the data representing the classfile, in classfile format.
	 ** @param offset the offset into the data where the classfile starts.
	 ** @param len the length of the classfile data in the array.
	 ** @return the class that was defined.
	 ** @exception ClassFormatError if the byte array is not in proper classfile format.
	 **/
	protected final native Class defineClass(String name, byte[] data, int offset, int len) throws ClassFormatError;

	/** Helper to resolve all references to other classes from this class.
	 ** @param c the class to resolve.
	 **/
	protected final native void resolveClass(Class c);

	/** Helper to find a Class using the system classloader.
	 ** @param name the name of the class to find.
	 ** @return the found class
	 ** @exception ClassNotFoundException if the class cannot be found.
	 **/
	protected final native Class findSystemClass(String name) throws ClassNotFoundException;

	/** Helper to set the signers of a class.
	 ** @param c the Class to set signers of
	 ** @param signers the signers to set
	 **/
	protected final void setSigners(Class c, Object[] signers) {
		c.setSigners(signers);
	}

	/** Helper to find an already-loaded class in this ClassLoader.
	 ** @param name the name of the class to find.
	 ** @return the found Class, or null if it is not found.
	 **/
	protected final native Class findLoadedClass(String name);

	/** Get the URL to a resource using the system classloader.
	 ** @param name the name of the resource relative to the
	 **        system classloader.
	 ** @return the URL to the resource.
	 **/
	public static final native URL getSystemResource(String name);

	/** Get a resource using the system classloader.
	 ** @param name the name of the resource relative to the
	 **        system classloader.
	 ** @return the resource.
	 **/
	public static final native InputStream getSystemResourceAsStream(String name);
}

