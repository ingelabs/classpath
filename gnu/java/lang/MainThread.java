/*
 * gnu.java.lang.MainThread: part of the Java Class Libraries project.
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

package gnu.java.lang;

import java.lang.reflect.*;

/**
 ** MainThread is a Thread which uses the main() method of some class.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 11 1998
 **/
public class MainThread {
	String[] args;
	Method mainMethod;

	public MainThread(String classname, String[] args) throws ClassNotFoundException, NoSuchMethodException {
		Class found = Class.forName(classname);
		Class[] argTypes = new Class[1];
		argTypes[0] = args.getClass();
		mainMethod = found.getMethod("main", argTypes);
		this.args = args;
	}

	public void run() {
		try {
			mainMethod.invoke(null,args);
		} catch(IllegalAccessException e) {
		} catch(InvocationTargetException e) {
		}
	}
}
