/*
 * java.util.ResourceBundle: part of the Java Class Libraries project.
 * Copyright (C) 1998 Jochen Hoenicke
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

package java.util;

/**
 * A resouce bundle contains locale-specific data.  If you need
 * localized data, you can load a resource bundle that matches the
 * locale with <code>getBundle</code>.  Now you can get your object by
 * calling <code>getObject</code> or <code>getString</code> on that
 * bundle.
 * <br>
 * When a bundle is demanded for a specific locale, the ResourceBundle
 * is searched in following order (<i>def. language code<i> stands for
 * the two letter ISO language code of the default locale (see
 * <code>Locale.getDefault()</code>).
 * <pre>
 * baseName_<i>language code</i>_<i>country code</i>_<i>variant</i>
 * baseName_<i>language code</i>_<i>country code</i>
 * baseName_<i>language code</i>
 * baseName_<i>def. language code</i>_<i>def. country code</i>_<i>def. variant</i>
 * baseName_<i>def. language code</i>_<i>def. country code</i>
 * baseName_<i>def. language code</i>
 * baseName
 * </pre>
 * The bundle is backed up by the bundles appearing below in the above
 * list.  That means, that if a resource if missing in the bundle,
 * the back up bundles are searched for the resource.
 * <br>
 * If you provide a bundle for a given locale, say
 * <code>Bundle_en_UK_POSIX</code>, you must also provide a bundle for
 * all sub locales, ie. <code>Bundle_en_UK</code>, <code>Bundle_en</code>, and
 * <code>Bundle</code>.
 * <br>
 * When a bundle is searched, we look first for a class with
 * the given name and if that is not found for a file with
 * <code>.properties</code> extension in the classpath.  The name
 * must be a fully qualified classname (with dots as path separators).  
 * <br>
 * (Note: This implementation always backs up the class with a
 * properties file if that is existing, but you shouldn't rely on
 * this, if you want to be compatible to the standard JDK.)
 *
 * @see Locale
 * @see PropertyResourceBundle
 * @author Jochen Hoenicke */
public abstract class ResourceBundle {
    /**
     * The parent bundle.  This is consulted when you call getObject
     * and there is no such resource in the current bundle.  This
     * field may be null.  
     */
    protected ResourceBundle parent;

    /**
     * The locale of this resource bundle.  You can read this with
     * <code>getLocale</code> and it is automatically set in
     * <code>getBundle</code>.  
     */
    private Locale locale;

    /**
     * The constructor.  It does nothing special.
     */
    public ResourceBundle() {
    }

    /**
     * Get a String from this resource bundle.  Since most localized
     * Objects are Strings, this method provides a convenient way to get
     * them without casting.
     * @param key the name of the resource.
     * @exception MissingResourceException
     *   if that particular object could not be found in this bundle nor
     *   the parent bundle.
     */
    public final String getString(String key) throws MissingResourceException {
        return (String) getObject(key);
    }

    /**
     * Get an array of Strings from this resource bundle.  This method
     * provides a convenient way to get it without casting.
     * @param key the name of the resource.
     * @exception MissingResourceException
     *   if that particular object could not be found in this bundle nor
     *   the parent bundle.
     */
    public final String[] getStringArray(String key) 
        throws MissingResourceException {
        return (String[]) getObject(key);
    }

    /**
     * Get an object from this resource bundle.
     * @param key the name of the resource.
     * @exception MissingResourceException
     *   if that particular object could not be found in this bundle nor
     *   the parent bundle.
     */
    public final Object getObject(String key) 
        throws MissingResourceException {

	for (ResourceBundle bundle = this; 
	     bundle != null; bundle = bundle.parent) {
	    try {
		Object o = bundle.handleGetObject(key);
		if (o != null)
		    return o;
	    } catch (MissingResourceException ex) {
	    }
	}
	throw new MissingResourceException
	    ("Key not found", getClass().getName(), key);
    }

    /**
     * This method returns an array with the classes of the calling
     * methods.  The zeroth entry is the class that called this method
     * (should always be ResourceBundle), the first contains the class
     * that called the caller (i.e. the class that called getBundle).
     *
     * Implementation note: This depends on the fact, that getBundle
     * doesn't get inlined, but since it calls a private method, it
     * isn't inlineable.
     *
     * @return an array containing the classes for the callers.  
     */
    private static native Class[] getClassContext();

    /**
     * Get the appropriate ResourceBundle for the default locale.  
     * @param baseName the name of the ResourceBundle.  This should be
     * a name of a Class or a properties-File.  See the class
     * description for details.  
     * @return the desired resource bundle
     * @exception MissingResourceException 
     *    if the resource bundle couldn't be found.  */
    public static final ResourceBundle getBundle(String baseName) 
        throws MissingResourceException {
	return getBundle(baseName, Locale.getDefault(), 
			 getClassContext()[1].getClassLoader());
    }

    /**
     * Get the appropriate ResourceBundle for the given locale.  
     * @param baseName the name of the ResourceBundle.  This should be
     * a name of a Class or a properties-File.  See the class
     * description for details.  
     * @param locale A locale.
     * @return the desired resource bundle
     * @exception MissingResourceException 
     *    if the resource bundle couldn't be found.
     */
    public static final ResourceBundle getBundle(String baseName, 
                                                 Locale locale) 
        throws MissingResourceException {
        return getBundle(baseName, locale, 
                         getClassContext()[1].getClassLoader());
    }

    /**
     * The resource bundle cache.  The key is the localized name.
     * @XXX make this a WeakHashMap() as soon that is implemented.
     */
    //     private static Map resourceBundleCache = new [Weak]HashMap();
    private static Hashtable resourceBundleCache = new Hashtable();

    /**
     * Tries to load a class or a property file with the specified name.
     * @param name the name.
     * @param locale the locale, that must be used exactly.
     * @param classloader the classloader.
     * @param bundle the back up (parent) bundle
     * @return the resource bundle if that could be loaded, otherwise 
     * <code>bundle</code>.
     */
    private static final ResourceBundle tryBundle(String name, 
                                                  Locale locale,
                                                  ClassLoader classloader,
                                                  ResourceBundle bundle) {
        if (locale.toString().length() > 0)
            name += "_" + locale.toString();

        {
            // First look into the cache.
            ResourceBundle rb = (ResourceBundle) resourceBundleCache.get(name);
            if (rb != null)
                // The bundle should already be the fallback, except something
                // very strange happened.
                return rb;
        }
        
        try {
            java.io.InputStream is; 
            if (classloader == null)
                is = ClassLoader.getSystemResourceAsStream
                    (name.replace('.','/') + ".properties");
            else
                is = classloader.getResourceAsStream
                    (name.replace('.','/') + ".properties");
            if (is != null) {
                ResourceBundle rb = new PropertyResourceBundle(is);
                rb.parent = bundle;
                rb.locale = locale;
                bundle = rb;
            }
        } catch (java.io.IOException ex) {
        }

        try {
            Class rbClass;
            if (classloader == null)
                rbClass = Class.forName(name);
            else
                rbClass = classloader.loadClass(name);
            ResourceBundle rb = (ResourceBundle) rbClass.newInstance();
            rb.parent = bundle;
            rb.locale = locale;
            bundle = rb;
        } catch (ClassNotFoundException ex) {
        } catch (IllegalAccessException ex) {
        } catch (InstantiationException ex) {
            // ignore them all
            // XXX should we also ignore ClassCastException?
        }
        // Put the bundle in the cache
        if (bundle != null)
            resourceBundleCache.put(name, bundle);

        return bundle;
    }

    /**
     * Get the appropriate ResourceBundle for the given locale.  
     * @param baseName the name of the ResourceBundle.  This should be
     * a name of a Class or a properties file.  See the class
     * description for details.  
     * @param locale A locale.
     * @param classloader a ClassLoader.
     * @return the desired resource bundle
     * @exception MissingResourceException 
     *    if the resource bundle couldn't be found.
     */
    public static final ResourceBundle getBundle(String baseName, 
                                                 Locale locale,
                                                 ClassLoader classloader) 
        throws MissingResourceException {
        // This implementation searches the bundle in the reverse direction
        // and builds the parent chain on the fly.

        ResourceBundle bundle = tryBundle(baseName, new Locale("",""),
                                          classloader, null);
        if (bundle == null)
            // JDK says, that if one provides a bundle base_en_UK, one
            // must also provide the bundles base_en and base.
            // This implies that if there is no bundle for base, there
            // is no bundle at all.
            throw new MissingResourceException
                ("Bundle not found", baseName, "");
        
        // Now use the defaultLocale.
        Locale myLocale = Locale.getDefault();
        if (myLocale.getLanguage().length() != 0) {
            bundle = tryBundle(baseName, 
                               new Locale(myLocale.getLanguage(),""), 
                               classloader, bundle);

            if (myLocale.getCountry().length() != 0) {
                bundle = tryBundle(baseName, 
                                   new Locale(myLocale.getLanguage(),
                                              myLocale.getCountry()), 
                                   classloader, bundle);

                if (myLocale.getVariant().length() != 0) {
                    bundle = tryBundle(baseName, myLocale,
                                       classloader, bundle);
                }
            }
        }
        if (myLocale.equals(locale))
            return bundle;

        // Last but not least use the desired locale.
        myLocale = locale;
        if (myLocale.getLanguage().length() != 0) {
            bundle = tryBundle(baseName, 
                               new Locale(myLocale.getLanguage(),""), 
                               classloader, bundle);

            if (myLocale.getCountry().length() != 0) {
                bundle = tryBundle(baseName, 
                                   new Locale(myLocale.getLanguage(),
                                              myLocale.getCountry()), 
                                   classloader, bundle);

                if (myLocale.getVariant().length() != 0) {
                    bundle = tryBundle(baseName, myLocale,
                                       classloader, bundle);
                }
            }
        }
        return bundle;
    }

    /**
     * Return the actual locale of this bundle.  You can use it after
     * calling getBundle, to know if the bundle for the desired locale
     * was loaded or if the fallback was used.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the parent of this bundle. This is consulted when you call
     * getObject and there is no such resource in the current bundle.
     * @param parent the parent of this bundle.
     */
    protected void setParent(ResourceBundle parent) {
        // Shall we ignore the old parent?
        this.parent = parent;
    }

    /**
     * Override this method to provide the resource for a keys.  This gets
     * called by <code>getObject</code>.  If you don't have a resource
     * for the given key, you should return null instead throwing a
     * MissingResourceException.   You don't have to ask the parent, 
     * getObject() already does this.
     *
     * @param key The key of the resource.
     * @return The resource for the key.
     * @exception MissingResourceException
     *   you shouldn't throw this.
     */
    protected abstract Object handleGetObject(String key)
        throws MissingResourceException;

    /**
     * This method should return all keys for which a resource exists.
     * @return An enumeration of the keys.
     */
    public abstract Enumeration getKeys();
}
