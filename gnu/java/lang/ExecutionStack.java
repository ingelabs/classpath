/*
 * gnu.java.lang.ExecutionStack: part of the Java Class Libraries project.
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
 ** ExecutionStack represents the Java system execution
 ** stack and allows you to get information off of it.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 11 1998
 **/
public class ExecutionStack {
	public static native Class[] getClasses();
	public static native Class getClass(int stackPos);

	public static native Method[] getMethods();
	public static native Method getMethod(int stackPos);

	public static native StackFrame getFrame(int stackPos);
	public static native StackFrame getFrame(Thread t, int stackPos);

	public static native StackFrame getFrame();
	public static native StackFrame getFrame(Thread t);
}
