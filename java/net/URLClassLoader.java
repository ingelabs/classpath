/* URLConnection.java --  Class loader that loads classes from one or more URLs
   Copyright (C) 1999, 2001 Free Software Foundation, Inc.

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

package java.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.FilePermission;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.security.PermissionCollection;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * A secure class loader that can load classes and resources from multiple
 * locations. Given an array of URLs this class loader will retrieve classes
 * and resources by fetching them from possible remote locations. Each URL is
 * searched in order in which it was added. If the file portion of the URL ends
 * with a / character then it is interpreted as a base directory, otherwise it
 * is interpreted as a jar file from which the classes/resources are resolved.
 * <p>
 * New instances can be created by two static newInstance() methods or by
 * three public contructors. Both give the option to supply an initial array
 * of URLs and (optionally) a parent classloader (that is different from the
 * standard system class loader).
 * Creating a <code>URLClassLoader</code> can always throw a
 * <code>SecurityException</code> if the checkes performed by the constructor of
 * <code>SecureClassLoader</code>, which are called when creating a new
 * instance, fail.
 * Note that only subclasses can add new URLs after the URLClassLoader had been
 * created. But it is always possible to get an array of all URLs that the class
 * loader uses to resolve classes and resources by way of the
 * <code>getURLs()</code> method.
 * <p>
 * XXX - TODO - FIXME This implementation is not finished or tested!<br>
 * Open issues:
 * <ul>
 * <li>Should the URLClassLoader actually add the locations found in the
 * manifest or is this the responsibility of some other loader/(sub)class?
 * (see <a href="http://java.sun.com/products/jdk/1.3/docs/guide/extensions/spec.html#bundled">Extension Machanism Architecture - Bundles Extensions</a>)
 * <li> How does <code>definePackage()</code> and sealing work precisely?
 * (Note that the method is currently not even implemented in
 * <code>java.lang.ClassLoader</code>.)
 * <li> Should the static <code>newInstance()</code> methods always succeed? Or
 * are they also restricted by the normal security checks?
 * <li> We might want to do some caching of URLs, Connections or JarFiles.
 * For now we just assume that the (Jar)URLConnection takes care of this.
 * <li> Is the way we create the "jar" URLs correct? What if it was already a
 * "jar" URL?
 * <li> The createInstance methods should record the security context and the
 * rest of the methods should use this context to to make doPriveliged() calls.
 * </ul>
 *
 * @since 1.2
 * @author Mark Wielaard (mark@klomp.org)
 */
public class URLClassLoader extends SecureClassLoader {

    // Variables

    /** Locations to load classes from */
    private Vector urls = new Vector();

    /** Factory used to get the protocol handlers of the URLs */
    private URLStreamHandlerFactory factory = null;

    // Constructors

    /**
     * Creates a URLClassLoader that gets classes from the supplied URLs.
     * To determine if this classloader may be created the constructor of
     * the super class (<CODE>SecureClassLoader</CODE>) is called first, which
     * can throw a SecurityException. Then the supplied URLs are added
     * in the order given to the URLClassLoader which uses these URLs to
     * load classes and resources (after using the default parent ClassLoader).
     * @exception SecurityException if the SecurityManager disallows the
     * creation of a ClassLoader.
     * @param urls Locations that should be searched by this ClassLoader when
     * resolving Classes or Resources.
     * @see SecureClassLoader
     */
    public URLClassLoader(URL[] urls) throws SecurityException {
        super();
        addURLs(urls);
    }

    /**
     * Creates a URLClassLoader that gets classes from the supplied URLs.
     * To determine if this classloader may be created the constructor of
     * the super class (<CODE>SecureClassLoader</CODE>) is called first, which
     * can throw a SecurityException. Then the supplied URLs are added
     * in the order given to the URLClassLoader which uses these URLs to
     * load classes and resources (after using the supplied parent ClassLoader).
     * @exception SecurityException if the SecurityManager disallows the
     * creation of a ClassLoader.
     * @exception SecurityException 
     * @param urls Locations that should be searched by this ClassLoader when
     * resolving Classes or Resources.
     * @param parent The parent class loader used before trying this class
     * loader.
     * @see SecureClassLoader
     */
    public URLClassLoader(URL[] urls, ClassLoader parent) throws
                                                          SecurityException {
        super(parent);
        addURLs(urls);
    }

    /**
     * Creates a URLClassLoader that gets classes from the supplied URLs.
     * To determine if this classloader may be created the constructor of
     * the super class (<CODE>SecureClassLoader</CODE>) is called first, which
     * can throw a SecurityException. Then the supplied URLs are added
     * in the order given to the URLClassLoader which uses these URLs to
     * load classes and resources (after using the supplied parent ClassLoader).
     * It will use the supplied <CODE>URLStreamHandlerFactory</CODE> to get the
     * protocol handlers of the supplied URLs.
     * @exception SecurityException if the SecurityManager disallows the
     * creation of a ClassLoader.
     * @exception SecurityException 
     * @param urls Locations that should be searched by this ClassLoader when
     * resolving Classes or Resources.
     * @param parent The parent class loader used before trying this class
     * loader.
     * @param factory Used to get the protocol handler for the URLs.
     * @see SecureClassLoader
     */
    public URLClassLoader(URL[] urls,
                          ClassLoader parent,
                          URLStreamHandlerFactory factory) throws
                                                           SecurityException {
        super(parent);
        addURLs(urls);
        this.factory = factory;
    }

    // Methods

    /**
     * Adds a new location to the end of the internal URL store.
     * @param newUrl the location to add
     */
    protected void addURL(URL newUrl) {
        urls.add(newUrl);
    }

    /**
     * Adds an array of new locations to the end of the internal URL store.
     * @param newUrls the locations to add
     */
    private void addURLs(URL[] newUrls) {
        for (int i = 0; i < newUrls.length; i++) {
            addURL(newUrls[i]);
        }
    }

    /** 
     * Defines a Package based on the given name and the supplied manifest
     * information. The manifest indicates the tile, version and
     * vendor information of the specification and implementation and wheter the
     * package is sealed. If the Manifest indicates that the package is sealed
     * then the Package will be sealed with respect to the supplied URL.
     *
     * @exception IllegalArgumentException If this package name already exists
     * in this class loader
     * @param name The name of the package
     * @param manifest The manifest describing the specification,
     * implementation and sealing details of the package
     * @param url the code source url to seal the package
     * @return the defined Package
     */
    protected Package definePackage(String name, Manifest manifest, URL url) 
                                             throws IllegalArgumentException {
        Attributes attr = manifest.getMainAttributes();
        String specTitle =
            attr.getValue(Attributes.Name.SPECIFICATION_TITLE); 
        String specVersion =
            attr.getValue(Attributes.Name.SPECIFICATION_VERSION); 
        String specVendor =
            attr.getValue(Attributes.Name.SPECIFICATION_VENDOR); 
        String implTitle =
            attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE); 
        String implVersion =
            attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION); 
        String implVendor =
            attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);

        // Look if the Manifest indicates that this package is sealed
        // XXX - most likely not completely correct!
        // Shouldn't we also check the sealed attribute of the complete jar?
// http://java.sun.com/products/jdk/1.3/docs/guide/extensions/spec.html#bundled
        // But how do we get that jar manifest here?
        String sealed = attr.getValue(Attributes.Name.SEALED);
        if ("false".equals(sealed)) {
            // make sure that the URL is null so the package is not sealed
            url = null;
        }

        return definePackage(name, specTitle, specVersion, specVendor,
                             implTitle, implVersion, implVendor, url);
    }

    /**
     * Finds (the first) class by name from one of the locations. The locations
     * are searched in the order they were added to the URLClassLoader.
     * @param className the classname to find
     * @exception ClassNotFoundException when the class could not be found or
     * loaded
     * @return a Class object representing the found class
     */
    protected Class findClass(String className) throws ClassNotFoundException {
        // Just try to find the resource by the (almost) same name
        String resourceName = className.replace('.', '/') + ".class";
        URL url = findResource(resourceName);
        if (url == null) {
            throw new ClassNotFoundException(className);
        }

        // Try to read the class data, create the CodeSource and construct the
        // class (and watch out for those nasty IOExceptions)
        try {
            byte [] classData;
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            int length = conn.getContentLength();
            if (length != -1) {
                // We know the length of the data, just read it all in at once
                classData = new byte[length];
                in.read(classData);
            } else {
                // We don't know the data length so we have to read it in chunks
                ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                byte b[] = new byte[1024];
                int l = 0;
                while (l != -1) {
                    l = in.read(b);
                    if (l != -1) {
                        out.write(b, 0, l);
                    }
                }
                classData = out.toByteArray();
            }

            // Now construct the CodeSource (if loaded from a jar file)
            CodeSource source = null;
            if (url.getProtocol().equals("jar")) {
                Certificate[] certificates =
                                   ((JarURLConnection)conn).getCertificates();
                source = new CodeSource(url, certificates);
            }

            // And finally construct the class!
            return defineClass(className, classData,
                               0, classData.length,
                               source);
        } catch (IOException ioe) {
            throw new ClassNotFoundException(className + ": " + ioe);
        }
    }

    /**
     * Finds the first occurrence of a resource that can be found. The locations
     * are searched in the order they were added to the URLClassLoader.
     * @param resourceName the resource name to look for
     * @return the URL if found, null otherwise
     */
    public URL findResource(String resourceName) {
        Enumeration e = urls.elements();
        while (e.hasMoreElements()) {
            URL url = findResource(resourceName, (URL)e.nextElement());
            if (url != null) {
                // Found the resource
                return url;
            }
        }
        // Resource not found
        return null;
    }

    /**
     * Find a resource relative to a base URL. If the base URL ends with a /
     * character then the base URL is interpreted as a directory and the
     * resource name is appended to it, otherwise the base URL is interpreted
     * as a jar file and a "jar" URL is constructed from the base URL and the
     * supplied resource name. This method tries to open a connection to the
     * resulting URL to make sure the resource is actually there.
     * <p>
     * XXX - We might want to do some caching of URLs, Connections or JarFiles.
     * For now we just assume that the (Jar)URLConnection takes care of this.
     * XXX - Should we also disconnect/close the URLConnection again? And how
     * would we do that? For now just assume the connection will actually be
     * used.
     * @param resourceName the resource name to look for
     * @param url the base URL
     * @return the URL if found, null otherwise
     * @see JarURLConnection
     */
    private URL findResource(String resourceName, URL url) {
        URL resourceURL;
        // Get the file portion of the base URL
        String file = url.getFile();

        // Construct the resourceURL
        if (file.endsWith("/")) {
            // Interpret it as a directory and just append the resource name
            try {
                resourceURL = new URL(url, resourceName,
                                    createURLStreamHandler(url.getProtocol()));
            } catch (MalformedURLException e) {
                return null;
            }
        } else {
            // Interpret it as a jar file and construct a "jar" URL
            String external = url.toExternalForm();
            try {
                // XXX - Is this correct? Why is there no URL constructor that
                // takes just a string and a URLStreamHandler?
                resourceURL = new URL(new URL("jar:" + external + "!/"),
                                      resourceName,
                                      createURLStreamHandler("jar"));
            } catch (MalformedURLException e) {
                return null;
            }
        }

        // Check if the resource is actually at that location
        // we MUST open the connection to ensure the location gets checked
        try {
            URLConnection u = resourceURL.openConnection();
            u.connect();
        } catch (IOException ioe) {
            return null;
        }

        return resourceURL;
    }

    /**
     * If the URLStreamHandlerFactory has been set this return the appropriate
     * URLStreamHandler for the given protocol.
     * @param protocol the protocol for which we need a URLStreamHandler
     * @return the appropriate URLStreamHandler or null
     */
    private URLStreamHandler createURLStreamHandler(String protocol) {
        if (factory != null) {
            return factory.createURLStreamHandler(protocol);
        } else {
            return null;
        }
    }

    /**
     * Finds all the resources with a particular name from all the locations.
     * @exception IOException when an error occurs accessing one of the
     * locations
     * @param resourceName the name of the resource to lookup
     * @return a (possible empty) enumeration of URLs where the resource can be
     * found
     */
    public Enumeration findResources(String resourceName) throws IOException {
        Vector resources = new Vector();
        Enumeration e = urls.elements();
        while (e.hasMoreElements()) {
            URL url = findResource(resourceName, (URL)e.nextElement());
            if (url != null) {
                // Found another resource
                resources.add(url);
            }
        }
        return resources.elements();
    }

    /**
     * Returns the permissions needed to access a particular code source.
     * These permissions includes those returned by
     * <CODE>SecureClassLoader.getPermissions</CODE> and the actual permissions
     * to access the objects referenced by the URL of the code source.
     * The extra permissions added depend on the protocol and file portion of
     * the URL in the code source. If the URL has the "file" protocol ends with
     * a / character then it must be a directory and a file Permission to read
     * everthing in that directory and all subdirectories is added. If the URL
     * had the "file" protocol and doesn't end with a / character then it must
     * be a normal file and a file permission to read that file is added. If the
     * URL has any other protocol then a socket permission to connect and accept
     * connections from the host portion of the URL is added.
     * @param source The codesource that needs the permissions to be accessed
     * @return the collection of permissions needed to access the code resource
     * @see SecureClassLoader.getPermissions()
     */
    protected PermissionCollection getPermissions(CodeSource source) {
        // XXX - This implementation does exactly as the Javadoc describes.
        // But maybe we should/could use URLConnection.getPermissions()?

        // First get the permissions that would normally be granted
        PermissionCollection permissions = super.getPermissions(source);
        
        // Now add the any extra permissions depending on the URL location
        URL url = source.getLocation();
        String protocol = url.getProtocol();
        if (protocol.equals("file")) {
            String file = url.getFile();
            // If the file end in / it must be an directory
            if (file.endsWith("/")) {
                // Grant permission to read everything in that directory and
                // all subdirectories
                permissions.add(new FilePermission(file + "-", "read"));
            } else { // It is a 'normal' file
                // Grant permission to access that file
                permissions.add(new FilePermission(file, "read"));
            }
        } else {
            // Grant permission to connect to and accept connections from host
            String host = url.getHost();
            permissions.add(new SocketPermission(host, "connect,accept"));
        }

        return permissions;
    }
    
    /**
     * Returns all the locations that this class loader currently uses the
     * resolve classes and resource. This includes both the initially supplied
     * URLs as any URLs added later by the loader.
     * @return All the currently used URLs
     */
    public URL[] getURLs() {
        return (URL[]) urls.toArray();
    }

    /**
     * Creates a new instance of a URLClassLoader that gets classes from the
     * supplied URLs. This class loader will have as parent the standard
     * system class loader.
     * @param urls the initial URLs used to resolve classes and resources
     */
    public static URLClassLoader newInstance(URL urls[]) throws
                                                         SecurityException {
        return new URLClassLoader(urls);
    }

    /**
     * Creates a new instance of a URLClassLoader that gets classes from the
     * supplied URLs and with the supplied loader as parent class loader.
     * @param urls the initial URLs used to resolve classes and resources
     * @param parent the parent class loader
     */
    public static URLClassLoader newInstance(URL urls[],
                                             ClassLoader parent) throws
                                                         SecurityException {
        return new URLClassLoader(urls, parent);
    }
}
