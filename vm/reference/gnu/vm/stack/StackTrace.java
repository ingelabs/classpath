/*
 * gnu.java.lang.ExecutionStack: part of the Java Class Libraries project.
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
 ** StackTrace represents a Java system execution
 ** stack and allows you to get information off of it.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 11 1998
 **/
public class StackTrace {
	StackFrame[] frames;
	int len;

	public static StackTrace copyCurrentStackTrace() {
		return new StackTrace(new StackFrame[0]);
	}

	public static StackTrace copyStackTrace(Thread t) {
		return new StackTrace(new StackFrame[0]);
	}

	StackTrace(StackFrame[] frames) {
		this.frames = frames;
		len = frames.length;
	}

	public synchronized StackFrame pop() {
		if(len <= 0)
			return null;
			//Note: cannot throw exception here, since this method
			//is used in exception throwing itself and could cause
			//an infinite loop.
			//throw new ArrayIndexOutOfBoundsException("stack trace empty.");
		len--;
		return frames[len];
	}

	public synchronized StackFrame frameAt(int i) {
		if(i > len)
			throw new ArrayIndexOutOfBoundsException(i + " > " + len);
		return frames[i];
	}

	public synchronized int numFrames() {
		return len;
	}
}
