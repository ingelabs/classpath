/*
 * gnu.java.io.ClassLoaderObjectInputStream: part of the Java Class Libraries project.
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

package gnu.java.io;

import java.io.*;

/**
 * ClassLoaderObjectInputStream is ObjectInputStream, with
 * the ability to use a specific ClassLoader.
 *
 * @author Geoff Berry
 * @version 1.1.0, 29 Jul 1998
 */

public class ClassLoaderObjectInputStream extends ObjectInputStream {
	ClassLoader myClassLoader;

	/** Create the new ClassLoaderObjectInputStream.
	 * @param in the InputStream to read the Objects from.
	 * @param myClassLoader the ClassLoader to load classes
	 *        with.
	 */
	public ClassLoaderObjectInputStream(InputStream in, ClassLoader myClassLoader) throws IOException,StreamCorruptedException {
		super(in);
		this.myClassLoader = myClassLoader;
	}

	/** Overriden method to use the loadClass() method from
	 * the ClassLoader.
	 */
	public Class resolveClass(String name) throws IOException, ClassNotFoundException {
		return myClassLoader.loadClass(name);
	}
}