/* java.lang.ClassLoader
   Copyright (C) 1998, 1999, 2001 Free Software Foundation, Inc.

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

import java.lang.reflect.*;
import gnu.java.lang.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import gnu.java.util.DoubleEnumeration;
import gnu.java.util.EmptyEnumeration;

/**
 ** The ClassLoader is a way of customizing the way Java
 ** gets its classes and loads them into memory.  The
 ** verifier and other standard Java things still run, but
 ** the ClassLoader is allowed great flexibility in
 ** determining where to get the classfiles and when to
 ** load and resolve them.
 **
 ** XXX - Not all support has been written for the new 1.2 methods yet!
 **
 ** <p>
 ** Every classloader has a parent classloader that is consulted before
 ** the 'child' classloader when classes or resources should be loaded.
 ** This is done to make sure that classes can be loaded from an hierarchy of
 ** multiple classloaders and classloaders do not accidentially redefine
 ** already loaded classes by classloaders higher in the hierarchy.
 ** <p>
 ** The grandparent of all classloaders is the bootstrap classloader.
 ** It is one of two special classloaders.
 ** The bootstrap classloader loads all the standard system classes as
 ** implemented by GNU Classpath. The other special classloader is the
 ** system classloader (also called application classloader) that loads
 ** all classes from the classpath (<code>java.class.path</code> system
 ** property). The system classloader is responsible for finding the
 ** application classes from the classpath, and delegates all requests for
 ** the standard library classes to its parent the bootstrap classloader.
 ** Most programs will load all their classes through the system classloaders.
 ** <p>
 ** The bootstrap classloader in GNU Classpath is implemented as a couple of
 ** static (native) methods on the package private class
 ** <code>java.lang.VMClassLoader</code>, the system classloader is an
 ** instance of <code>gnu.java.lang.SystemClassloader</code>
 ** (which is a subclass of <code>java.net.URLClassLoader</code>).
 ** <p>
 ** Users of a <code>ClassLoader</code> will normally just use the methods
 ** <ul>
 ** <li> <code>loadClass()</code> to load a class.
 ** <li> <code>getResource()</code> or <code>getResourceAsStream()</code>
 ** to access a resource.
 ** <li> <code>getResources()</code> to get an Enumeration of URLs to all
 ** the resources provided by the classloader and its parents with the same
 ** name.
 ** </ul>
 ** <p>
 ** Subclasses should implement the methods
 ** <ul>
 ** <li> <code>findClass()</code> which is called by <code>loadClass()</code>
 ** when the parent classloader cannot provide a named class.
 ** <li> <code>findResource()</code> which is called by
 ** <code>getResource()</code> when the parent classloader cannot provide
 ** a named resource.
 ** <li> <code>findResources()</code> which is called by
 ** <code>getResource()</code> to combine all the resources with the same name
 ** from the classloader and its parents.
 ** <li> <code>findLibrary()</code> which is called by
 ** <code>Runtime.loadLibrary()</code> when a class defined by the classloader
 ** wants to load a native library.
 ** </ul>
 **
 ** @author John Keiser
 ** @author Mark Wielaard
 ** @version 1.1.99, Jan 2000
 ** @since JDK1.0
 **/

public abstract class ClassLoader {
	/* Each instance gets a list of these. */
	private Hashtable loadedClasses = new Hashtable();

    /* Each instance gets a list of these. */
    private Hashtable definedPackages = new Hashtable();

    /* The classloader that is consulted before this classloader.
       if null then the parent is the bootstrap classloader. */
    private final ClassLoader parent;

    /* System/Application classloader gnu.java.lang.SystemClassLoader. */
    static final ClassLoader systemClassLoader
        = null; // XXX = SystemClassLoader.getInstance();

    /** Create a new ClassLoader with as parent the system classloader.
     ** @exception SecurityException if you do not have permission
     **            to create a ClassLoader.
     **/
    protected ClassLoader() throws SecurityException {
        this(systemClassLoader);
    }
 
    /** Create a new ClassLoader with the specified parent.
     ** The parent will be consulted when a class or resource is
     ** requested through <code>loadClass()</code> or
     ** <code>getResource()</code>. Only when the parent classloader
     ** cannot provide the requested class or resource the
     ** <code>findClass()</code> or <code>findResource()</code> method
     ** of this classloader will be called.
     **
     ** @param parent the classloader that should be consulted before
     ** this classloader. Use <code>null</code> to specify the bootstrap
     ** classloader.
     ** @exception SecurityException if you do not have permission
     **            to create a ClassLoader.
     **
     ** @since 1.2
     **/
    protected ClassLoader(ClassLoader parent) {
        // May we create a new classloader?
        SecurityManager sm = System.getSecurityManager();
        if (sm != null)
            sm.checkCreateClassLoader();
        
        this.parent = parent;
    }
    
	/** Load a class using this ClassLoader or its parent,
	 ** without resolving it. Calls <code>loadClass(name, false)</code>.
	 ** <p>
	 ** Subclasses should not override this method but should override
	 ** <code>findClass()</code> which is called by this method.
	 **
	 ** @param name the name of the class relative to this ClassLoader.
	 ** @exception ClassNotFoundException if the class cannot be found to
	 **            be loaded.
	 ** @return the loaded class.
	 **/
	public Class loadClass(String name) throws ClassNotFoundException {
		return loadClass(name,false);
	}

	/** Load a class using this ClassLoader or its parent,
	 ** possibly resolving it as well using <code>resolveClass()</code>.
	 ** It first tries to find out if the class has already been loaded
	 ** through this classloader by calling <code>findLoadedClass()</code>.
	 ** Then it calls <code>loadClass()</code> on the parent classloader
	 ** (or when there is no parent on the bootstrap classloader).
	 ** When the parent could not load the class it tries to create
	 ** a new class by calling <code>findClass()</code>. Finally when
	 ** <code>resolve</code> is <code>true</code> it also calls
	 ** <code>resolveClass()</code> on the newly loaded class.
	 ** <p>
	 ** Subclasses should not override this method but should override
	 ** <code>findClass()</code> which is called by this method.
	 **
	 ** @param name the fully qualified name of the class to load.
	 ** @param resolve whether or not to resolve the class.
	 ** @exception ClassNotFoundException if the class cannot be found to
	 **            be loaded.
	 ** @return the loaded class.
	 **/
    protected synchronized Class loadClass(String name, boolean resolve)
        throws ClassNotFoundException
    {
        // Have we already loaded this class?
        Class c = findLoadedClass(name);
        if (c != null)
            return c;
        
        // Can the class be loaded by one of our parent?
        try {
            if (parent == null)
                // XXX - use the bootstrap classloader
                // return VMClassLoader.loadClass(name, resolve);
                return findSystemClass(name);
            else
                return parent.loadClass(name, resolve);
        } catch (ClassNotFoundException e) { /* ignore use findClass() */ }
        
        // Still not found, we have to do it ourselfs.
        c = findClass(name);
        
        // resolve if necessary
        if (resolve)
            resolveClass(c);
        
        return c;
    }
    
    /** Get the URL to a resource using this classloader
     ** or one of its parents. First tries to get the resource by calling
     ** <code>getResource()</code> on the parent classloader.
     ** If the parent classloader returns null then it tries finding the
     ** resource by calling <code>findResource()</code> on this
     ** classloader.
     ** <p>
     ** Subclasses should not override this method but should override
     ** <code>findResource()</code> which is called by this method.
     **
     ** @param name the name of the resource relative to this
     **        classloader.
     ** @return the URL to the resource or null when not found.
     **/
    public URL getResource(String name) {
        URL result;
        
        if (parent == null)
            // XXX - try bootstrap classloader;
            // result = VMClassLoader.getResource(name);
            return ClassLoader.getSystemResource(name);
        else
            result = parent.getResource(name);
        
        if (result == null)
            result = findResource(name);
        
        return result;
	}

    /** Get a resource as stream using this classloader or one of its
     ** parents. First calls <code>getResource()</code> and if that
     ** returns a URL to the resource then it calls and returns the
     ** InputStream given by <code>URL.openStream()</code>.
     ** <p>
     ** Subclasses should not override this method but should override
     ** <code>findResource()</code> which is called by this method.
     **
     ** @param name the name of the resource relative to this
     **        classloader.
     ** @return An InputStream to the resource or null when the resource
     ** could not be found or when the stream could not be opened.
     **/
    public InputStream getResourceAsStream(String name) {
        URL url = getResource(name);
        if (url == null)
            return(null);
        
        try {
            return url.openStream();
        } catch(IOException e) {
            return null;
        }
    }
    
	/** Helper to define a class using a string of bytes.
	 ** @param data the data representing the classfile, in classfile format.
	 ** @param offset the offset into the data where the classfile starts.
	 ** @param len the length of the classfile data in the array.
	 ** @return the class that was defined.
	 ** @deprecated use defineClass(String,...) instead.
	 **/
	protected final Class defineClass(byte[] data, int offset, int len) throws ClassFormatError {
		return defineClass(null,data,offset,len);
	}

	/** Helper to define a class using a string of bytes without a
	 ** ProtectionDomain.
	 ** <p>
	 ** Subclasses should call this method from their
	 ** <code>findClass()</code> implementation.
	 ** @param name the name to give the class.  null if unknown.
	 ** @param data the data representing the classfile, in classfile format.
	 ** @param offset the offset into the data where the classfile starts.
	 ** @param len the length of the classfile data in the array.
	 ** @return the class that was defined.
	 ** @exception ClassFormatError if the byte array is not in proper classfile format.
	 **/
	protected final Class defineClass(String name, byte[] data, int offset, int len) throws ClassFormatError {
        // XXX - return defineClass(name,data,offset,len,null);
		Class retval = VMClassLoader.defineClass(this,name,data,offset,len);
		loadedClasses.put(retval.getName(),retval);
		return retval;
	}

    /** Helper to define a class using a string of bytes.
     ** <p>
     ** Subclasses should call this method from their
     ** <code>findClass()</code> implementation.
     **
     ** XXX - not implemented yet. Needs native support.
     **
     ** @param name the name to give the class.  null if unknown.
     ** @param data the data representing the classfile, in classfile format.
     ** @param offset the offset into the data where the classfile starts.
     ** @param len the length of the classfile data in the array.
     ** @param domain the ProtectionDomain to give to the class.
     ** null if unknown (the class will get the default protection domain).
     ** @return the class that was defined.
     ** @exception ClassFormatError if the byte array is not in proper
     ** classfile format.
     **
     ** @since 1.2
     **/
    protected final Class defineClass(String name, byte[] data, int offset,
                                      int len, ProtectionDomain domain)
        throws ClassFormatError
    {
        /*
          XXX - needs native support.
          Class retval
          = VMClassLoader.defineClass(this,name,data,offset,len,domain);
            loadedClasses.put(retval.getName(),retval);
          return retval;
        */
        return defineClass(name, data, offset, len, domain);
    }

	/** Helper to resolve all references to other classes from this class.
	 ** @param c the class to resolve.
	 **/
	protected final void resolveClass(Class c) {
		VMClassLoader.resolveClass(c);
	}

	/** Helper to find a Class using the system classloader,
	 ** possibly loading it.
	 ** @param name the name of the class to find.
	 ** @return the found class
	 ** @exception ClassNotFoundException if the class cannot be found.
	 **/
	protected final Class findSystemClass(String name) throws ClassNotFoundException {
		return Class.forName(name);
	}

	/** Helper to set the signers of a class.
	 ** @param c the Class to set signers of
	 ** @param signers the signers to set
	 **/
	protected final void setSigners(Class c, Object[] signers) {
		c.setSigners(signers);
	}

	/** Helper to find an already-loaded class in this ClassLoader.
	 ** @param name the name of the class to find.
	 ** @return the found Class, or null if it is not found.
	 **/
	protected final Class findLoadedClass(String name) {
		return (Class)loadedClasses.get(name);
	}

	/** Get the URL to a resource using the system classloader.
	 ** @param name the name of the resource relative to the
	 **        system classloader.
	 ** @return the URL to the resource.
	 **/
	public static final URL getSystemResource(String name) {
		if (name.startsWith("/"))
			name = name.substring(1);
		String cp = System.getProperty("java.class.path");
		if (cp == null)
			return(null);

		StringTokenizer st = new StringTokenizer(cp, 
							 File.pathSeparator);
		while(st.hasMoreTokens()) {
			String path = st.nextToken();
			if (path.toLowerCase().endsWith(".zip") ||
			    path.toLowerCase().endsWith(".jar"))
				return(null); // Not implemented yet
			File f;
			if (path.endsWith(File.separator))
				f = new File(path + name);
			else
				f = new File(path + File.separator + name);

			if (f.exists())
				try {
					return new URL("file://" + 
							f.getAbsolutePath());
				} catch(MalformedURLException e) {
					return null;
				}
		}
		return(null);
	}

	/** Get a resource using the system classloader.
	 ** @param name the name of the resource relative to the
	 **        system classloader.
	 ** @return the resource.
	 **/
	public static final InputStream getSystemResourceAsStream(String name) {
		try {
			URL url = getSystemResource(name);
			if (url == null)
				return(null);
			return url.openStream();
		} catch(IOException e) {
			return null;
		}
	}

    /**
     * Defines a new package and creates a Package object.
     * The package should be defined before any class in the package is
     * defined with <code>defineClass()</code>. The package should not yet
     * be defined before in this classloader or in one of its parents (which
     * means that <code>getPackage()</code> should return <code>null</code>).
     * All parameters except the <code>name</code> of the package may be
     * <code>null</code>.
     * <p>
     * Subclasses should call this method from their <code>findClass()</code>
     * implementation before calling <code>defineClass()</code> on a Class
     * in a not yet defined Package (which can be checked by calling
     * <code>getPackage()</code>).
     *
     * @param name The name of the Package
     * @param specTitle The name of the specification
     * @param specVendor The name of the specification designer
     * @param specVersion The version of this specification
     * @param implTitle The name of the implementation
     * @param implVendor The vendor that wrote this implementation
     * @param implVersion The version of this implementation
     * @param sealed If sealed the origin of the package classes
     * @return the Package object for the specified package
     *
     * @exception IllegalArgumentException if the package name is null or if
     * it was already defined by this classloader or one of its parents.
     *
     * @see Package
     * @since 1.2
     */
    protected Package definePackage(String name,
            String specTitle, String specVendor, String specVersion,
            String implTitle, String implVendor, String implVersion,
            URL sealed) {

        if (getPackage(name) != null)
            throw new IllegalArgumentException("Package " + name
                                               + " already defined");
        Package p = new Package(name,
                                specTitle, specVendor, specVersion,
                                implTitle, implVendor, implVersion,
                                sealed);
        definedPackages.put(name, p);

        return p;
    }

    /**
     * Returns the Package object for the requested package name. It returns
     * null when the package is not defined by this classloader or one of its
     * parents.
     *
     * @since 1.2
     */
    protected final Package getPackage(String name) {
        Package p;
        if (parent == null)
            // XXX - Should we use the bootstrap classloader?
            p = null;
        else
            p = parent.getPackage(name);

        if (p == null)
            p = (Package)definedPackages.get(name);

        return p;
    }

    /**
     * Returns all Package objects defined by this classloader and its parents.
     *
     * @since 1.2
     */
    protected Package[] getPackages() {
        Package[] allPackages;
        
        // Get all our packages.
        Package[] packages;
        synchronized(definedPackages) {
            packages = new Package[definedPackages.size()];
            Enumeration e = definedPackages.elements();
            int i = 0;
            while (e.hasMoreElements()) {
                packages[i] = (Package)e.nextElement();
                i++;
            }
        }
        
        // If we have a parent get all packages defined by our parents.
        if (parent != null) {
            Package[] parentPackages = parent.getPackages();
            allPackages = new Package[parentPackages.length+packages.length];
            System.arraycopy(parentPackages, 0, allPackages, 0,
                             parentPackages.length);
            System.arraycopy(packages, 0, allPackages, parentPackages.length,
                             packages.length);
        } else
            // XXX - Should we use the bootstrap classloader?
            allPackages = packages;
        
        return allPackages;
    }

    /**
     * Returns the parent of this classloader.
     * If the parent of this classloader is the bootstrap classloader then
     * this method returns <code>null</code>.
     *
     * @exception SecurityException thrown when the classloader of the calling
     * class is not the bootstrap (null) or the current classloader and the
     * caller also doesn't have the
     * <code>RuntimePermission("getClassLoader")</code>.
     *
     * @since 1.2
     */
    public final ClassLoader getParent() {
        // Check if we may return the parent classloader
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            Class c = VMSecurityManager.getClassContext()[1];
            ClassLoader cl = c.getClassLoader();
            if (cl != null && cl != this)
                sm.checkPermission(new RuntimePermission("getClassLoader"));
        }
        return parent;
    }

    /**
     * Returns the system classloader. The system classloader (also called
     * the application classloader) is the classloader that was used to
     * load the application classes on the classpath (given by the system
     * property <code>java.class.path</code>.
     * <p>
     * Note that this is different from the bootstrap classloader that
     * actually loads all the real "system" classes (the bootstrap classloader
     * is the parent of the returned system classloader).
     *
     * @exception SecurityException thrown when the classloader of the calling
     * class is not the bootstrap (null) or system classloader and the caller
     * also doesn't have the <code>RuntimePermission("getClassLoader")</code>.
     *
     * @since 1.2
     */
    public static ClassLoader getSystemClassLoader() {
        // Check if we may return the system classloader
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            Class c = VMSecurityManager.getClassContext()[1];
            ClassLoader cl = c.getClassLoader();
            if (cl != null && cl != systemClassLoader)
                sm.checkPermission(new RuntimePermission("getClassLoader"));
        }
        return systemClassLoader;
    }

    /**
     * Called for every class name that is needed but has not yet been
     * defined by this classloader or one of its parents. It is called by
     * <code>loadClass()</code> after both <code>findLoadedClass()</code> and
     * <code>parent.loadClass()</code> couldn't provide the requested class.
     * <p>
     * The default implementation throws a <code>ClassNotFoundException</code>.
     * Subclasses should override this method. An implementation of this
     * method in a subclass should get the class bytes of the class (if it can
     * find them), if the package of the requested class doesn't exist it
     * should define the package and finally it should call define the actual
     * class. It does not have to resolve the class. It should look something
     * like the following:
     * <p>
     <pre>
         // Get the bytes that describe the requested class
         byte[] classBytes = classLoaderSpecificWayToFindClassBytes(name);
         // Get the package name
         int lastDot = name.lastIndexOf('.');
         if (lastDot != -1) {
             String packageName = name.substring(0,lastDot);
             // Look if the package already exists
             if (getPackage(pkg) == null) {
                 // define the package
                 definePackage(packageName, ...);
             }
         // Define and return the class
         return defineClass(name, classBytes, 0, classBytes.length);
     </pre>
     * <p>
     * <code>loadClass()</code> makes sure that the <code>Class</code>
     * returned by <code>findClass()</code> will later be returned by
     * <code>findLoadedClass()</code> when the same class name is
     * requested.
     *
     * @param name class name to find (including the package name)
     * @return the requested Class
     * @exception ClassNotFoundException when the class can not be found
     *
     * @since 1.2
     */
    protected Class findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }

    /**
     * Called whenever a resource is needed that could not be provided by
     * one of the parents of this classloader. It is called by
     * <code>getResource()</code> after <code>parent.getResource()</code>
     * couldn't provide the requested resource.
     * <p>
     * The default implementation always returns null. Subclasses should
     * override this method when they can provide a way to return a URL
     * to a named resource.
     *
     * @param name the name of the resource to be found.
     * @return a URL to the named resource or null when not found.
     *
     * @since 1.2
     */
    protected URL findResource(String name) {
        return null;
    }

    /**
     * Called whenever all locations of a named resource are needed.
     * It is called by <code>getResources()</code> after it has called
     * <code>parent.getResources()</code>. The results are combined by
     * the <code>getResources()</code> method.
     * <p>
     * The default implementation always returns an empty Enumeration.
     * Subclasses should override it when they can provide an Enumeration of
     * URLS (possibly just one element) to the named resource.
     * The first URL of the Enumeration should be the same as the one
     * returned by <code>findResource</code>.
     *
     * @param name the name of the resource to be found.
     * @return a possibly empty Enumeration of URLs to the named resource.
     *
     * @since 1.2
     */
    protected Enumeration findResources(String name) throws IOException {
        return EmptyEnumeration.getInstance();
    }

    /**
     * Returns an Enumeration of all resources with a given name that can
     * be found by this classloader and its parents. Certain classloaders
     * (such as the URLClassLoader when given multiple jar files) can have
     * multiple resources with the same name that come from multiple locations.
     * It can also occur that a parent classloader offers a resource with a
     * certain name and the child classloader also offers a resource with that
     * same name. <code>getResource() only offers the first resource (of the
     * parent) with a given name. This method lists all resources with the
     * same name.
     * <p>
     * The Enumeration is created by first calling <code>getResources()</code>
     * on the parent classloader and then calling <code>findResources()</code>
     * on this classloader.
     *
     * @since 1.2
     */
    public final Enumeration getResources(String name) throws IOException {
        Enumeration parentResources;
        if (parent == null)
            // XXX - Should use the bootstrap classloader
            parentResources = EmptyEnumeration.getInstance();
        else
            parentResources = parent.getResources(name);

        return new DoubleEnumeration(parentResources, findResources(name));
    }

    /**
     * Called by <code>Runtime.loadLibrary()</code> to get an absolute path
     * to a (system specific) library that was requested by a class loaded
     * by this classloader. The default implementation returns
     * <code>null</code>. It should be implemented by subclasses when they
     * have a way to find the absolute path to a library. If this method
     * returns null the library is searched for in the default locations
     * (the directories listed in the <code>java.library.path</code> system
     * property).
     *
     * @param name the (system specific) name of the requested library.
     * @return the full pathname to the requested library
     * or null when not found
     *
     * @see Runtime#loadLibrary()
     * @since 1.2
     */
    protected String findLibrary(String name)
    {
        return null;
    }

    /** Get an Enumeration of URLs to resources with a given name using
     ** the system classloader. The enumeration firsts lists the resources
     ** with the given name that can be found by the bootstrap classloader
     ** followed by the resources with the given name that can be found
     ** on the classpath.
     ** @param name the name of the resource relative to the
     **        system classloader.
     ** @return an Enumeration of URLs to the resources.
     **
     ** @since 1.2
     **/
    public static Enumeration getSystemResources(String name)
        throws IOException
    {
        return systemClassLoader.getResources(name);
    }

}

