/* java.lang.System
   Copyright (C) 1998, 1999, 2000, 2001 Free Software Foundation, Inc.

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


package java.lang;

import java.io.*;
import java.util.*;
import gnu.classpath.Configuration;

/**
 * System represents system-wide resources; things that
 * represent the general environment.  As such, all
 * methods are static.
 *
 * @author John Keiser
 * @version 1.1.0, Aug 8 1998
 * @since JDK1.0
 */

public class System
{

  static
  {
    if (Configuration.INIT_LOAD_LIBRARY)
      {
	System.loadLibrary ("javalang");
      }
  }

  /* This class is uninstantiable. */
  private System ()
  {
  }

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
	 ** This corresponds to the C stderr and C++ cerr
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

  static
  {
    properties = new Properties ();
    VMSystem.insertSystemProperties (properties);
    insertGNUProperties ();
    in = new FileInputStream (FileDescriptor.in);
    out = new PrintStream (new FileOutputStream (FileDescriptor.out));
    err = new PrintStream (new FileOutputStream (FileDescriptor.err));
  }

	/** Set in to a new InputStream.
	 ** @param in the new InputStream.
	 ***/
  public static native void setIn (InputStream in);

	/** Set out to a new PrintStream.
	 ** @param out the new PrintStream.
	 ***/
  public static native void setOut (PrintStream out);

	/** Set err to a new PrintStream.
	 ** @param err the new PrintStream.
	 ***/
  public static native void setErr (PrintStream err);

	/** Get the current SecurityManager.
	 ** If the SecurityManager has not been set yet, then this
	 ** method returns null.
	 ** @return the current SecurityManager, or null.
	 **/
  public static SecurityManager getSecurityManager ()
  {
    return Runtime.getSecurityManager ();
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
	 **
	 ** @param securityManager the new SecurityManager.
	 ** @exception SecurityException if the SecurityManger is
	 **            already set.
	 **/
  public static void setSecurityManager (SecurityManager securityManager)
  {
    Runtime.setSecurityManager (securityManager);
  }

	/** Get the current time, measured in the number of
	 ** milliseconds from the beginning of Jan. 1, 1970.
	 ** This is gathered from the system clock, with any
	 ** attendant incorrectness (it may be timezone
	 ** dependent).
	 ** @return the current time.
	 **/
  public static native long currentTimeMillis ();

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
  public static void arraycopy (Object src, int srcStart, Object dest,
				int destStart, int len)
  {
    VMSystem.arraycopy (src, srcStart, dest, destStart, len);
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
  public static int identityHashCode (Object o)
  {
    return VMSystem.identityHashCode (o);
  }

	/** Get all the system properties at once.
	 ** @XXX list the standard system properties
	 ** @return the system properties
	 ** @exception SecurityException if thrown by
	 **            getSecurityManager().checkPropertiesAccess()
	 **/
  public static Properties getProperties ()
  {
    try
    {
      getSecurityManager ().checkPropertiesAccess ();
    }
    catch (NullPointerException e)
    {
    }
    return properties;
  }

	/** Set all the system properties at once.
	 ** @param properties the new set of system properties.
	 ** @exception SecurityException if thrown by
	 **            getSecurityManager().checkPropertiesAccess()
	 **/
  public static void setProperties (Properties properties)
  {
    try
    {
      getSecurityManager ().checkPropertiesAccess ();
    }
    catch (NullPointerException e)
    {
    }
    System.properties = properties;
  }


	/** Get a single system property by name.
	 ** @param name the name of the system property to get
	 ** @return the property, or null if not found.
	 ** @exception SecurityException if thrown by
	 **            getSecurityManager().checkPropertyAccess(name)
	 **/
  public static String getProperty (String name)
  {
    SecurityManager sm = getSecurityManager ();
    try
    {
      sm.checkPropertyAccess (name);
    }
    catch (NullPointerException e)
    {
    }
    return properties.getProperty (name);
  }

	/** Set a single system property by name.
	 ** @param name the name of the system property to set
	 ** @param value the new value of the system property
	 ** @return the old property value, or null if not yet set
	 ** @exception SecurityException if a SecurityManager is set
     **            and the caller doesn't have
	 **            <code>PropertyPermission(name, "write")</code>
	 **
	 ** @since 1.2
	 **/
  public static String setProperty (String name, String value)
  {
    SecurityManager sm = getSecurityManager ();
    if (sm != null)
      sm.checkPermission (new PropertyPermission (name, "write"));

    return (String) properties.setProperty (name, value);
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
  public static String getProperty (String name, String def)
  {
    try
    {
      getSecurityManager ().checkPropertyAccess (name);
    }
    catch (NullPointerException e)
    {
    }
    return properties.getProperty (name, def);
  }

	/** Get a single property by name.  Calls getProperty(name).
	 ** @deprecated use getProperty(name).
	 ** @see #getProperty(java.lang.String)
	 **/
  public static String getenv (String name)
  {
    return getProperty (name);
  }

	/** Helper method to exit the Java runtime using
	 ** <CODE>Runtime.getRuntime().exit()</CODE>.
	 ** @see java.lang.Runtime#exit(int)
	 **/
  public static void exit (int status)
  {
    Runtime.getRuntime ().exit (status);
  }

	/** Helper method to run the garbage collector using
	 ** <CODE>Runtime.getRuntime().gc()</CODE>.
	 ** @see java.lang.Runtime#gc()
	 **/
  public static void gc ()
  {
    Runtime.getRuntime ().gc ();
  }

	/** Helper method to run finalization using
	 ** <CODE>Runtime.getRuntime().runFinalization()</CODE>.
	 ** @see java.lang.Runtime#runFinalization()
	 **/
  public static void runFinalization ()
  {
    Runtime.getRuntime ().runFinalization ();
  }

	/** Tell the Runtime whether to run finalization before
	 ** exiting the JVM.  Just uses
	 ** <CODE>Runtime.getRuntime().runFinalizersOnExit()</CODE>.
	 ** @see java.lang.Runtime#runFinalizersOnExit()
	 **
	 ** @deprecated Since 1.2 this method is officially deprecated
	 ** because there is no guarantee and doing the actual finalization
	 ** on all objects is unsafe since not all (daemon) threads might
	 ** be finished with all objects when the VM terminates.
	 **/
  public static void runFinalizersOnExit (boolean finalizeOnExit)
  {
    Runtime.getRuntime ().runFinalizersOnExit (finalizeOnExit);
  }

	/** Helper method to load a library using its explicit
	 ** system-dependent filename.  This just calls
	 ** <CODE>Runtime.getRuntime().load(filename)</CODE>.
	 ** @see java.lang.Runtime#load(java.lang.String)
	 **/
  public static void load (String filename)
  {
    Runtime.getRuntime ().load (filename);
  }

	/** Helper method to load a library using just a
	 ** short identifier for the name.  This just calls
	 ** <CODE>Runtime.getRuntime().loadLibrary(libname)</CODE>.
	 ** @see java.lang.Runtime#loadLibrary(java.lang.String)
	 **/
  public static void loadLibrary (String libname)
  {
    Runtime.getRuntime ().loadLibrary (libname);
  }

  /** Add Classpath specific system properties.
   ** <br>
   ** Current properties:
   ** <br>
   ** <ul>
   **   <li> gnu.cpu.endian - big or little</li>
   ** </ul>
   **/
  static void insertGNUProperties ()
  {
    properties.put ("gnu.cpu.endian",
		    (isWordsBigEndian ())? "big" : "little");

    // Common encoding aliases. See gnu.java.io.EncodingManager.
    properties.put ("gnu.java.io.encoding_scheme_alias.ISO-8859-1", "8859_1");
    properties.put ("gnu.java.io.encoding_scheme_alias.ISO-8859-2", "8859_2");
    properties.put ("gnu.java.io.encoding_scheme_alias.ISO-8859-3", "8859_3");
    properties.put ("gnu.java.io.encoding_scheme_alias.ISO-8859-4", "8859_4");
    properties.put ("gnu.java.io.encoding_scheme_alias.ISO-8859-5", "8859_5");
    properties.put ("gnu.java.io.encoding_scheme_alias.ISO-8859-6", "8859_6");
    properties.put ("gnu.java.io.encoding_scheme_alias.ISO-8859-7", "8859_7");
    properties.put ("gnu.java.io.encoding_scheme_alias.ISO-8859-8", "8859_8");
    properties.put ("gnu.java.io.encoding_scheme_alias.ISO-8859-9", "8859_9");

    properties.put ("gnu.java.io.encoding_scheme_alias.iso-8859-1", "8859_1");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-8859-2", "8859_2");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-8859-3", "8859_3");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-8859-4", "8859_4");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-8859-5", "8859_5");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-8859-6", "8859_6");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-8859-7", "8859_7");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-8859-8", "8859_8");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-8859-9", "8859_9");

    properties.put ("gnu.java.io.encoding_scheme_alias.iso8859_1", "8859_1");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso8859_2", "8859_2");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso8859_3", "8859_3");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso8859_4", "8859_4");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso8859_5", "8859_5");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso8859_6", "8859_6");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso8859_7", "8859_7");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso8859_8", "8859_8");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso8859_9", "8859_9");

    properties.put ("gnu.java.io.encoding_scheme_alias.iso-latin-1", "8859_1");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-latin-2", "8859_2");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-latin-3", "8859_3");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-latin-4", "8859_4");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-latin-5", "8859_5");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-latin-6", "8859_6");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-latin-7", "8859_7");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-latin-8", "8859_8");
    properties.put ("gnu.java.io.encoding_scheme_alias.iso-latin-9", "8859_9");

    properties.put ("gnu.java.io.encoding_scheme_alias.latin1", "8859_1");
    properties.put ("gnu.java.io.encoding_scheme_alias.latin2", "8859_2");
    properties.put ("gnu.java.io.encoding_scheme_alias.latin3", "8859_3");
    properties.put ("gnu.java.io.encoding_scheme_alias.latin4", "8859_4");
    properties.put ("gnu.java.io.encoding_scheme_alias.latin5", "8859_5");
    properties.put ("gnu.java.io.encoding_scheme_alias.latin6", "8859_6");
    properties.put ("gnu.java.io.encoding_scheme_alias.latin7", "8859_7");
    properties.put ("gnu.java.io.encoding_scheme_alias.latin8", "8859_8");
    properties.put ("gnu.java.io.encoding_scheme_alias.latin9", "8859_9");

    properties.put ("gnu.java.io.encoding_scheme_alias.UTF-8", "UTF8");
    properties.put ("gnu.java.io.encoding_scheme_alias.utf-8", "UTF8");
  }

  static native boolean isWordsBigEndian ();
}
