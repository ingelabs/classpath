/* StackFrame.java
   Copyright (C) 1998 Free Software Foundation

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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

package gnu.vm.stack;

import java.lang.reflect.*;

/**
 ** StackFrame represents a single frame of the Java
 ** execution stack, frozen in time.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 11 1998
 **/
public final class StackFrame {
	StackFrame caller;
	Object obj;
	Method method;
	int lineNum;
	String filename;

        /**
	 * Constructs a new stack frame.  This is only called by the virtual
	 * machine.
	 * @param obj the this reference of that frame, null for static classes.
	 * @param method the called method.
	 * @param lineNum the line number or -1 if unknown.
	 * @param filename the filename of the source of the method, null if unknown.
	 */
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

	public Class getCalledClass() {
		return method.getDeclaringClass();
	}

	public Method getCalledMethod() {
		return method;
	}

	public int getSourceLineNumber() {
		return lineNum;
	}

        public String toString() {
	        return getCalledClass().getName() + "."
			+ getCalledMethod().getName()
			+ (getSourceFilename() != null ?
			   " at " + getSourceFilename()
			   + ":" + getSourceLineNumber()
			   : " (compiled code)");
        }
}
