/*
 * gnu.java.lang.StackFrame: part of the Java Class Libraries project.
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

package gnu.vm.stack;

import java.lang.reflect.*;

/**
 ** StackFrame represents a single frame of the Java
 ** execution stack, frozen in time.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 11 1998
 **/
public class StackFrame {
	StackFrame caller;
	Object obj;
	Method method;
	int lineNum;
	String filename;

	private StackFrame(Object obj, Method method, int lineNum, String filename) {
		this.caller = caller;
		this.obj = obj;
		this.method = method;
		this.lineNum = lineNum;
		this.filename = filename;
	}

	public String getSourceFilename() {
		return filename;
	}

	public Object getCalledObject() {
		return obj;
	}

	public Method getCalledMethod() {
		return method;
	}

	public int getSourceLineNumber() {
		return lineNum;
	}
}
