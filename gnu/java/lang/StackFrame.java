/* gnu.java.lang.StackFrame
   Copyright (C) 1998 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package gnu.java.lang;

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

	private StackFrame(StackFrame caller, Object obj, Method method, int lineNum, String filename) {
		this.caller = caller;
		this.obj = obj;
		this.method = method;
		this.lineNum = lineNum;
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}

	public StackFrame getCaller() {
		return caller;
	}

	public Object getCalledObject() {
		return obj;
	}

	public Method getCalledMethod() {
		return method;
	}

	public int getLineNum() {
		return lineNum;
	}
}
