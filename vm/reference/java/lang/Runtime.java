/*
 * java.lang.Runtime: part of the Java Class Libraries project.
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

import java.util.*;
import java.io.*;

/**
 ** Runtime represents the Virtual Machine.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 8 1998
 **/

public class Runtime extends Object {
	static Runtime current = new Runtime();
	String[] libpath;

	private Runtime() {
		String path = getLibraryPath();
		int numColons = 0;
		for(int i=0;i<path.length();i++) {
			if(path.charAt(i) == ':')
				numColons++;
		}

		libpath = new String[numColons+1];
		int current = 0;
		int libpathIndex = 0;
		while(true) {
			int next = path.indexOf(':',current);
			if(next == -1) {
				libpath[libpathIndex] = path.substring(current);
				break;
			}
			libpath[libpathIndex] = path.substring(current,next);
			libpathIndex++;
			current = next+1;
		}
	}

	/** Get the current Runtime object for this JVM.
	 ** @return the current Runtime object
	 **/
	public static Runtime getRuntime() {
		return current;
	}

	/** Exit the Java runtime. This method will either throw
	 ** a SecurityException or it will never return.
	 ** @param status the status to exit with
	 ** @exception SecurityException if
	 **            System.getSecurityManager().checkExit(status)
	 **            fails.
	 **/
	public void exit(int status) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null)
			sm.checkExit(status);
		exitInternal(status);
	}

	/**
	 ** Native method that actually shuts down the virtual machine
	 **/
	public native void exitInternal(int status); 

	/** Run the garbage collector.
	 ** This method is more of a suggestion than anything.
	 ** All this method guarantees is that the garbage
	 ** collector will have "done its best" by the time
	 ** it returns.
	 **/
	public native void gc();

	/** Run finalization on all Objects that are waiting to be
	 ** finalized.  Again, a suggestion, though a stronger
	 ** one.
	 **/
	public native void runFinalization();

	/** Tell the VM to run the finalize() method on every
	 ** single Object before it exits.  Note that the JVM may
	 ** still exit abnormally and not perform this, so you
	 ** still don't have a guarantee.  This value defaults to
	 ** <CODE>false</CODE>.
	 ** @param finalizeOnExit whether to finalize all Objects
	 **        before the JVM exits
	 **/
	public static void runFinalizersOnExit(boolean finalizeOnExit) {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null)
			sm.checkExit(0);
		runFinalizersOnExitInternal(finalizeOnExit);
	}

	/**
	 ** Native method that actually sets the finalizer setting.
	 **/
	public static native void runFinalizersOnExitInternal(boolean value);

	/** Load a native library using the system-dependent
	 ** filename.
         ** @exception SecurityException if
	 **            System.getSecurityManager().checkLink(filename)
	 **            fails.
	 ** @exception UnsatisfiedLinkError if the library is not
	 **            found.
	 **/
	public void load(String filename) {
		SecurityManager sm = System.getSecurityManager();
		if(sm != null) {
			sm.checkLink(filename);
		}
		nativeLoad(filename);
	}

	/** Load a native library using a system-independent "short
	 ** name" for the library.  It will be transformed to a 
	 ** correct filename in a system-dependent manner (for
	 ** example, in Windows, "mylib" will be turned into
	 ** "mylib.dll") and then passed to load(filename).
         ** @exception SecurityException if
	 **            System.getSecurityManager().checkLink(filename)
	 **            fails.
	 ** @exception UnsatisfiedLinkError if the library is not
	 **            found.
	 **/
	public void loadLibrary(String libname) {
		for(int i=0;i<libpath.length;i++) {
			try {
				String filename = nativeGetLibname(libpath[i],libname);
				load(filename);
				return;
			} catch(UnsatisfiedLinkError e) {
			}
		}
		throw new UnsatisfiedLinkError("Could not find library " + libname + ".");
	}

	/** Create a new subprocess with the specified command
	 ** line.  Calls exec(cmdline, null).
	 ** @param cmdline the command to call
	 ** @exception SecurityException if you cannot call this
	 **            command
	 **/
	public Process exec(String cmdline) {
		return exec(cmdline,null);
	}

	/** Create a new subprocess with the specified command
	 ** line and environment.  Parses the command line into
	 ** pieces using StringTokenizer and then calls exec(cmd,env)
	 ** @param cmdline the command to call
	 ** @exception SecurityException if you cannot call this
	 **            command
	 **/
	public Process exec(String cmdline, String[] env) {
		StringTokenizer t = new StringTokenizer(cmdline);
		Vector v = new Vector();
		while(t.hasMoreTokens()) {
			v.addElement(t.nextElement());
		}
		String[] cmd = new String[v.size()];
		v.copyInto(cmd);
		return exec(cmd, env);
	}

	/** Create a new subprocess with the specified command
	 ** line.  Calls exec(cmd,null).
	 ** @param cmd the command line, already separated
	 ** @exception SecurityException if you cannot call this
	 **            command.
	 **/
	public Process exec(String[] cmd) {
		return exec(cmd,null);
	}

	/** Create a new subprocess with the specified command
	 ** line.  Calls exec(cmd,null).
	 ** @param cmd the command line, already separated
	 ** @exception SecurityException if you cannot call this
	 **            command (checks using
	 **            <CODE>System.getSecuritymanager().checkExec(cmd[0])</CODE>.
	 **/
	public Process exec(String[] cmd, String[] env) {
		SecurityManager sm = System.getSecurityManager();
                if (sm != null)
                  sm.checkExec(cmd[0]);
		return execInternal(cmd,env);
	}

	/** Find out how much memory is still free for allocating
	 ** Objects on the heap.
	 ** @return the amount of free memory for more Objects.
	 **/
	public native long freeMemory();

	/** Find out how much memory total is available on the
	 ** heap for allocating Objects.
	 ** @return the total amount of memory for Objects.
	 **/
	public native long totalMemory();

	/** Tell the VM to trace every bytecode instruction that
	 ** executes (print out a trace of it).  No guarantees
	 ** are made as to where it will be printed, and the VM is
	 ** allowed to ignore this request.
	 ** @param on whether to turn instruction tracing on
	 **/
	public native void traceInstructions(boolean on);

	/** Tell the VM to trace every method call that executes
	 ** (print out a trace of it).  No guarantees are made as
	 ** to where it will be printed, and the VM is allowed to
	 ** ignore this request.
	 ** @param on whether to turn method tracing on
	 **/
	public native void traceMethodCalls(boolean on);

	/** Return a localized version of this InputStream,
	 ** meaning all characters are localized before they come
	 ** out the other end.
	 ** @XXX I must confess I have absolutely no idea how to
	 **      do this, and the thing is deprecated now anyway,
	 **      so I await Mr. Localization to work on it.
	 **/
	public InputStream getLocalizedInputStream(InputStream in) {
		return in;
	}

	/** Return a localized version of this InputStream,
	 ** meaning all characters are localized before they come
	 ** out the other end.
	 ** @XXX I must confess I have absolutely no idea how to
	 **      do this, and the thing is deprecated now anyway,
	 **      so I await Mr. Localization to work on it.
	 **/
	public OutputStream getLocalizedOutputStream(OutputStream out) {
		return out;
	}

	native void nativeLoad(String filename);
	native String nativeGetLibname(String pathname, String libname);
	native Process execInternal(String[] cmd, String[] env);
	static native String getLibraryPath();
}
