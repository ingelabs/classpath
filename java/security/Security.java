/* Security.java --- Java base security class implmentation
   Copyright (C) 1999, 2001, 2002, 2003, Free Software Foundation, Inc.

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

package java.security;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.security.Provider;
import java.util.Properties;
import java.util.Vector;

/**
 * This class centralizes all security properties and common security methods.
 * One of its primary uses is to manage providers.
 *
 * @author Mark Benvenuto <ivymccough@worldnet.att.net>
 */
public final class Security extends Object
{
  private static Vector providers = new Vector();
  private static Properties secprops;
  static
  {
    String base = System.getProperty("gnu.classpath.home.url");
    loadProviders(base, System.getProperty("gnu.classpath.vm.shortname"));
    loadProviders(base, "classpath");
  }

  // This class can't be instantiated.
  private Security ()
  {
  }

  private static void loadProviders(String baseUrl, String vendor)
  {
    if (baseUrl == null || vendor == null)
      return;

    String secfilestr = baseUrl + "/security/" + vendor + ".security";
    try
      {
	InputStream fin = new URL(secfilestr).openStream();
	secprops = new Properties();
	secprops.load(fin);

	int i = 1;
	String name;
	while ((name = secprops.getProperty("security.provider." + i)) != null)
	  {
	    Exception exception = null;
	    try
	      {
		providers.addElement(Class.forName(name).newInstance());
	      }
	    catch (ClassNotFoundException x)
	      {
	        exception = x;
	      }
	    catch (InstantiationException x)
	      {
	        exception = x;
	      }
	    catch (IllegalAccessException x)
	      {
	        exception = x;
	      }

	    if (exception != null)
	      System.err.println (
	          "Error loading security provider " + name + ": " + exception);
	    i++;
	  }
      }
    catch (FileNotFoundException ignored)
      {
        // Actually we probably shouldn't ignore these, once the security
	// properties file is actually installed somewhere.
      }
    catch (IOException ignored)
      {
      }
  }

  /**
   * Gets a specified property for an algorithm. The algorithm name should be a
   * standard name. See Appendix A in the Java Cryptography Architecture API
   * Specification &amp; Reference for information about standard algorithm
   * names. One possible use is by specialized algorithm parsers, which may map
   * classes to algorithms which they understand (much like {@link Key} parsers
   * do).
   *
   * @param algName the algorithm name.
   * @param propName the name of the property to get.
   * @return the value of the specified property.
   * @deprecated This method used to return the value of a proprietary property
   * in the master file of the "SUN" Cryptographic Service Provider in order to
   * determine how to parse algorithm-specific parameters. Use the new
   * provider-based and algorithm-independent {@link AlgorithmParameters} and
   * {@link KeyFactory} engine classes (introduced in the Java 2 platform)
   * instead.
   */
  public static String getAlgorithmProperty(String algName, String propName)
  {
    /* TODO: Figure out what this actually does */
    return null;
  }

  /**
   * <p>Adds a new provider, at a specified position. The position is the
   * preference order in which providers are searched for requested algorithms.
   * Note that it is not guaranteed that this preference will be respected. The
   * position is 1-based, that is, <code>1</code> is most preferred, followed by
   * <code>2</code>, and so on.</p>
   *
   * <p>If the given provider is installed at the requested position, the
   * provider that used to be at that position, and all providers with a
   * position greater than position, are shifted up one position (towards the
   * end of the list of installed providers).</p>
   *
   * <p>A provider cannot be added if it is already installed.</p>
   *
   * <p>First, if there is a security manager, its <code>checkSecurityAccess()
   * </code> method is called with the string <code>"insertProvider."+provider.
   * getName()</code> to see if it's ok to add a new provider. If the default
   * implementation of <code>checkSecurityAccess()</code> is used (i.e., that
   * method is not overriden), then this will result in a call to the security
   * manager's <code>checkPermission()</code> method with a
   * <code>SecurityPermission("insertProvider."+provider.getName())</code>
   * permission.</p>
   *
   * @param provider the provider to be added.
   * @param position the preference position that the caller would like for
   * this provider.
   * @return the actual preference position in which the provider was added, or
   * <code>-1</code> if the provider was not added because it is already
   * installed.
   * @throws SecurityException if a security manager exists and its
   * {@link SecurityManager#checkSecurityAccess(String)} method denies access
   * to add a new provider.
   * @see #getProvider(String)
   * @see #removeProvider(String)
   * @see SecurityPermission
   */
  public static int insertProviderAt(Provider provider, int position)
  {
    SecurityManager sm = System.getSecurityManager();
    if (sm != null)
      sm.checkSecurityAccess("insertProvider." + provider.getName());

    position--;
    int max = providers.size ();
    for (int i = 0; i < max; i++)
      {
	if (((Provider) providers.elementAt(i)).getName() == provider.getName())
	  return -1;
      }

    if (position < 0)
      position = 0;
    if (position > max)
      position = max;

    providers.insertElementAt(provider, position);

    return position + 1;
  }

  /**
   * <p>Adds a provider to the next position available.</p>
   *
   * <p>First, if there is a security manager, its <code>checkSecurityAccess()
   * </code> method is called with the string <code>"insertProvider."+provider.
   * getName()</code> to see if it's ok to add a new provider. If the default
   * implementation of <code>checkSecurityAccess()</code> is used (i.e., that
   * method is not overriden), then this will result in a call to the security
   * manager's <code>checkPermission()</code> method with a
   * <code>SecurityPermission("insertProvider."+provider.getName())</code>
   * permission.</p>
   *
   * @param provider the provider to be added.
   * @return the preference position in which the provider was added, or
   * <code>-1</code> if the provider was not added because it is already
   * installed.
   * @throws SecurityException if a security manager exists and its
   * {@link SecurityManager#checkSecurityAccess(String)} method denies access
   * to add a new provider.
   * @see #getProvider(String)
   * @see #removeProvider(String)
   * @see SecurityPermission
   */
  public static int addProvider(Provider provider)
  {
    return insertProviderAt (provider, providers.size () + 1);
  }

  /**
   * <p>Removes the provider with the specified name.</p>
   *
   * <p>When the specified provider is removed, all providers located at a
   * position greater than where the specified provider was are shifted down
   * one position (towards the head of the list of installed providers).</p>
   *
   * <p>This method returns silently if the provider is not installed.</p>
   *
   * <p>First, if there is a security manager, its <code>checkSecurityAccess()
   * </code> method is called with the string <code>"removeProvider."+name</code>
   * to see if it's ok to remove the provider. If the default implementation of
   * <code>checkSecurityAccess()</code> is used (i.e., that method is not
   * overriden), then this will result in a call to the security manager's
   * <code>checkPermission()</code> method with a <code>SecurityPermission(
   * "removeProvider."+name)</code> permission.</p>
   *
   * @param name the name of the provider to remove.
   * @throws SecurityException if a security manager exists and its
   * {@link SecurityManager#checkSecurityAccess(String)} method denies access
   * to remove the provider.
   * @see #getProvider(String)
   * @see #addProvider(Provider)
   */
  public static void removeProvider(String name)
  {
    SecurityManager sm = System.getSecurityManager();
    if (sm != null)
      sm.checkSecurityAccess("removeProvider." + name);

    int max = providers.size ();
    for (int i = 0; i < max; i++)
      {
	if (((Provider) providers.elementAt(i)).getName() == name)
	  {
	    providers.remove(i);
	    break;
	  }
      }
  }

  /**
   * Returns an array containing all the installed providers. The order of the
   * providers in the array is their preference order.
   *
   * @return an array of all the installed providers.
   */
  public static Provider[] getProviders()
  {
    Provider array[] = new Provider[providers.size ()];
    providers.copyInto (array);
    return array;
  }

  /**
   * Returns the provider installed with the specified name, if any. Returns
   * <code>null</code> if no provider with the specified name is installed.
   *
   * @param name the name of the provider to get.
   * @return the provider of the specified name.
   * @see #removeProvider(String)
   * @see #addProvider(Provider)
   */
  public static Provider getProvider(String name)
  {
    Provider p;
    int max = providers.size ();
    for (int i = 0; i < max; i++)
      {
	p = (Provider) providers.elementAt(i);
	if (p.getName() == name)
	  return p;
      }
    return null;
  }

  /**
   * <p>Gets a security property value.</p>
   *
   * <p>First, if there is a security manager, its <code>checkPermission()</code>
   * method is called with a <code>SecurityPermission("getProperty."+key)</code>
   * permission to see if it's ok to retrieve the specified security property
   * value.</p>
   *
   * @param key the key of the property being retrieved.
   * @return the value of the security property corresponding to key.
   * @throws SecurityException if a security manager exists and its
   * {@link SecurityManager#checkPermission(Permission)} method denies access
   * to retrieve the specified security property value.
   * @see #setProperty(String, String)
   * @see SecurityPermission
   */
  public static String getProperty(String key)
  {
    SecurityManager sm = System.getSecurityManager();
    if (sm != null)
      sm.checkSecurityAccess("getProperty." + key);

    return secprops.getProperty(key);
  }


  /**
   * <p>Sets a security property value.</p>
   *
   * <p>First, if there is a security manager, its <code>checkPermission()</code>
   * method is called with a <code>SecurityPermission("setProperty."+key)</code>
   * permission to see if it's ok to set the specified security property value.
   * </p>
   *
   * @param key the name of the property to be set.
   * @param datnum the value of the property to be set.
   * @throws SecurityException if a security manager exists and its
   * {@link SecurityManager#checkPermission(Permission)} method denies access
   * to set the specified security property value.
   * @see #getProperty(String)
   * @see SecurityPermission
   */
  public static void setProperty(String key, String datnum)
  {
    SecurityManager sm = System.getSecurityManager();
    if (sm != null)
      sm.checkSecurityAccess("setProperty." + key);

    secprops.put(key, datnum);
  }
}
