/*
 * java.util.ListResourceBundle: part of the Java Class Libraries project.
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
 * A <code>ListResouceBundle</code> provides an easy way, to create
 * your own resource bundle.  It is an abstract class that you can
 * subclass.  You should then overwrite the getContents method, that
 * provides a key/value list.
 * <br>
 * The key/value list is a two dimensional list of Object.  The first
 * dimension ranges over the resources. The second dimension ranges
 * from zero (key) to one (value).  The keys must be of type String.
 * <br>
 * XXX Example!
 *
 * @see Locale
 * @see PropertyResourceBundle
 * @author Jochen Hoenicke */
public abstract class ListResourceBundle extends ResourceBundle {
    /**
     * The constructor.  It does nothing special.
     */
    public ListResourceBundle() {
    }

    /**
     * Gets the key/value list.  You must override this method.
     * @return a two dimensional list of Objects.  The first dimension
     * ranges over the objects, and the second dimension ranges from
     * zero (key) to one (value).  
     */
    protected abstract Object[][] getContents();

    /**
     * Override this method to provide the resource for a keys.  This gets
     * called by <code>getObject</code>.
     * @param key The key of the resource.
     * @return The resource for the key.
     * @exception MissingResourceException
     *   if that particular object could not be found in this bundle.
     */
    protected Object handleGetObject(String key)
        throws MissingResourceException {
        Object[][] contents = getContents();
        for (int i=0; i<contents.length; i++) {
            if (key.equals(contents[i][0]))
                return contents[i][1];
        }
        throw new MissingResourceException("Key not found.", 
                                           getClass().getName(), key);
    }

    /**
     * This method should return all keys for which a resource exists.
     * @return An enumeration of the keys.
     */
    public Enumeration getKeys() {
        final Object[][] contents = getContents();
        return new Enumeration() {
            int i=0;
            public boolean hasMoreElements() {
                return i < contents.length;
            }
            public Object nextElement() {
                return contents[i++][0];
            }
        };
    }
}
