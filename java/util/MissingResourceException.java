/*
 * java.util.MissingResourceException: part of the Java Class Libraries project.
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
 * This exception is thrown when a resource is missing.
 *
 * @see ResourceBundle
 * @author Jochen Hoenicke
 */
public class MissingResourceException extends RuntimeException {

    /**
     * The name of the resource bundle requested by user.
     */
    private String className;
    
    /**
     * The key of the resource in the bundle requested by user.
     */
    private String key;

    /**
     * Creates a new exception, with the specified parameters.
     * @param s the detail message.
     * @param className the name of the resource bundle.
     * @param key the key of the missing resource.
     */
    public MissingResourceException(String s, String className, String key) {
        super(s);
        this.className = className;
        this.key = key;
    }

    /**
     * Gets the name of the resource bundle, for which a resource is missing.
     * @return the name of the resource bundle.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets the key of the resource that is missing bundle, this is an empty
     * string if the whole resource bundle is missing.
     * @return the name of the resource bundle.
     */
    public String getKey() {
        return key;
    }
}
