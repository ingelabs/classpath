/*
 * java.lang.System: part of the Java Class Libraries project.
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

package java.lang;

import java.io.*;
import java.util.*;

/**
 ** System represents system-wide resources; things that
 ** represent the general environment.  As such, all
 ** methods are static.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 8 1998
 ** @since JDK1.0
 **/

public class System {
	/* This class is uninstantiable. */
	private System() { }

	private static SecurityManager securityManager;
	private static Properties properties;

	/** The standard InputStream.  This is assigned at
	 ** startup and starts its life perfectly valid.<P>
	 ** This corresponds to the C stdin and C++ cin
	 ** variables, which typically input from the keyboard,
	 ** but may be used to pipe input from other processes
	 ** or files.  That should all be transparent to you,
	 ** however.
	 **/
	public static final InputStream in;

	/** The standard output PrintStream.  This is assigned at
	 ** startup and starts its life perfectly valid.<P>
	 ** This corresponds to the C stdout and C++ cout
	 ** variables, which typically output to the screen,
	 ** but may be used to pipe output to other processes
	 ** or files.  That should all be transparent to you,
	 ** however.
	 **/
	public static final PrintStream out;

	/** The standard error PrintStream.  This is assigned at
	 ** startup and starts its life perfectly valid.<P>
	 ** This corresponds to the C stdout and C++ cout
	 ** variables, which typically output to the screen,
	 ** but may be used to pipe output to other processes
	 ** or files.  That should all be transparent to you,
	 ** however.
	 **/
	public static final PrintStream err;

	static {
		properties = new Properties();
		VMSystem.insertSystemProperties(properties);
		in = new FileInputStream(FileDescriptor.in);
		out = new PrintStream(new FileOutputStream(FileDescriptor.out));
		err = new PrintStream(new FileOutputStream(FileDescriptor.err));
	}

	/** Set in to a new InputStream.
	 ** @param in the new InputStream.
	 ***/
	public static native void setIn(InputStream in);

	/** Set out to a new PrintStream.
	 ** @param out the new PrintStream.
	 ***/
	public static native void setOut(PrintStream out);

	/** Set err to a new PrintStream.
	 ** @param err the new PrintStream.
	 ***/
	public static native void setErr(PrintStream err);

	/** Get the current SecurityManager.
	 ** If the SecurityManager has not been set yet, then this
	 ** method returns null.
	 ** @return the current SecurityManager, or null.
	 **/
	public static SecurityManager getSecurityManager() {
		return securityManager;
	}

	/** Set the current SecurityManager.
	 ** This may only be done once.  If you try to re-set the
	 ** current SecurityManager, then you will get a
	 ** SecurityException.  If you use null and there is no
	 ** SecurityManager set, then the state will not
	 ** change.<P>
	 ** <STRONG>Spec Note:</STRONG> Don't ask me, I didn't
	 ** write it.  It looks pretty vulnerable; whoever gets to
	 ** the gate first gets to set the policy.
	 ** @param securityManager the new SecurityManager.
	 ** @exception SecurityException if the SecurityManger is
	 **            already set.
	 **/
	public static void setSecurityManager(SecurityManager securityManager) {
		if(securityManager != null) {
			throw new SecurityException("Security Manager already set");
		}
		System.securityManager = securityManager;
	}

	/** Get the current time, measured in the number of
	 ** milliseconds from the beginning of Jan. 1, 1970.
	 ** This is gathered from the system clock, with any
	 ** attendant incorrectness (it may be timezone
	 ** dependent).
	 ** @return the current time.
	 **/
	public static native long currentTimeMillis();

	/** Copy one array onto another from
	 ** <CODE>src[srcStart] ... src[srcStart+len]</CODE> to
	 ** <CODE>dest[destStart] ... dest[destStart+len]</CODE>
	 ** @param src the array to copy elements from
	 ** @param srcStart the starting position to copy elements
	 **        from in the src array
	 ** @param dest the array to copy elements to
	 ** @param destStart the starting position to copy
	 **        elements from in the src array
	 ** @param len the number of elements to copy
	 ** @exception ArrayStoreException if src or dest is not
	 **            an array, or if one is a primitive type
	 **            and the other is a reference type or a
	 **            different primitive type.  The array will
	 **            not be modified if any of these is the
	 **            case.  If there is an element in src that
	 **            is not assignable to dest's type, this will
	 **            be thrown and all elements up to but not
	 **            including that element will have been
	 **            modified.
	 ** @exception ArrayIndexOutOfBoundsException if len is
	 **            negative, or if the start or end copy
	 **            position in either array is out of bounds.
	 **            The array will not be modified if this
	 **            exception is thrown.
	 **/
	public static void arraycopy(Object src, int srcStart, Object dest, int destStart, int len) {
		VMSystem.arraycopy(src,srcStart,dest,destStart,len);
	}

	/** Get a hash code computed by the VM for the Object.
	 ** This hash code will be the same as Object's hashCode()
	 ** method.  It is usually some convolution of the pointer
	 ** to the Object internal to the VM.  It follows standard
	 ** hash code rules, in that it will remain the same for a
	 ** given Object for the lifetime of that Object.
	 ** @param o the Object to get the hash code for
	 ** @return the VM-dependent hash code for this Object
	 **/
	public static int identityHashCode(Object o) {
		return VMSystem.identityHashCode(o);
	}

	/** Get all the system properties at once.
	 ** @XXX list the standard system properties
	 ** @return the system properties
	 ** @exception SecurityException if thrown by
	 **            getSecurityManager().checkPropertiesAccess()
	 **/
	public static Properties getProperties() {
		try {
			getSecurityManager().checkPropertiesAccess();			
		} catch(NullPointerException e) {
		}
		return properties;
	}

	/** Set all the system properties at once.
	 ** @param properties the new set of system properties.
	 ** @exception SecurityException if thrown by
	 **            getSecurityManager().checkPropertiesAccess()
	 **/
	public static void setProperties(Properties properties) {
		try {
			getSecurityManager().checkPropertiesAccess();			
		} catch(NullPointerException e) {
		}
		System.properties = properties;
	}


	/** Get a single system property by name.
	 ** @param name the name of the system property to get
	 ** @return the property, or null if not found.
	 ** @exception SecurityException if thrown by
	 **            getSecurityManager().checkPropertyAccess(name)
	 **/
	public static String getProperty(String name) {
		SecurityManager sm = getSecurityManager();
		try {
			sm.checkPropertyAccess(name);
		} catch(NullPointerException e) {
		}
		return properties.getProperty(name);
	}

	/** Get a single property by name, with a possible default
	 ** value returned if not found.
	 ** @param name the name of the system property to set
	 ** @param def the default value to use if the
	 **        property does not exist.
	 ** @return the property, or default if not found.
	 ** @exception SecurityException if thrown by
	 **            getSecurityManager().checkPropertyAccess(name)
	 **/
	public static String getProperty(String name, String def) {
		try {
			getSecurityManager().checkPropertyAccess(name);
		} catch(NullPointerException e) {
		}
		return properties.getProperty(name,def);
	}

	/** Get a single property by name.  Calls getProperty(name).
	 ** @deprecated use getProperty(name).
	 ** @see #getProperty(java.lang.String)
	 **/
	public static String getenv(String name) {
		return getProperty(name);
	}

	/** Helper method to exit the Java runtime using
	 ** <CODE>Runtime.getRuntime().exit()</CODE>.
	 ** @see java.lang.Runtime#exit(int)
	 **/
	public static void exit(int status) {
		Runtime.getRuntime().exit(status);
	}

	/** Helper method to run the garbage collector using
	 ** <CODE>Runtime.getRuntime().gc()</CODE>.
	 ** @see java.lang.Runtime#gc()
	 **/
	public static void gc() {
		Runtime.getRuntime().gc();
	}

	/** Helper method to run finalization using
	 ** <CODE>Runtime.getRuntime().runFinalization()</CODE>.
	 ** @see java.lang.Runtime#runFinalization()
	 **/
	public static void runFinalization() {
		Runtime.getRuntime().runFinalization();
	}

	/** Tell the Runtime whether to run finalization before
	 ** exiting the JVM.  Just uses
	 ** <CODE>Runtime.getRuntime().runFinalizersOnExit()</CODE>.
	 ** @see java.lang.Runtime#runFinalizersOnExit()
	 **/
	public static void runFinalizersOnExit(boolean finalizeOnExit) {
		Runtime.getRuntime().runFinalizersOnExit(finalizeOnExit);
	}

	/** Helper method to load a library using its explicit
	 ** system-dependent filename.  This just calls
	 ** <CODE>Runtime.getRuntime().load(filename)</CODE>.
	 ** @see java.lang.Runtime#load(java.lang.String)
	 **/
	public static void load(String filename) {
		Runtime.getRuntime().load(filename);
	}

	/** Helper method to load a library using just a
	 ** short identifier for the name.  This just calls
	 ** <CODE>Runtime.getRuntime().loadLibrary(libname)</CODE>.
	 ** @see java.lang.Runtime#loadLibrary(java.lang.String)
	 **/
	public static void loadLibrary(String libname) {
		Runtime.getRuntime().loadLibrary(libname);
	}
}
