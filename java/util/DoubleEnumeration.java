/*
 * java.util.DoubleEnumeration: part of the Java Class Libraries project.
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
import java.io.*;

/**
 * This is a package private class that combines two Enumerations.
 * It returns the elements of the first Enumeration until it has
 * no more elements and then returns the elements of the second
 * Enumeration.<br>
 * 
 * In the default case:
 * <pre>
 * while (doubleEnum.hasMoreElements()) {
 *    Object o = doubleEnum.nextElement();
 *    do_something(o);
 * }
 * </pre>
 * it calls hasMoreElements of the first Enumeration as few times as
 * possible.
 *
 * @author Jochen Hoenicke */
class DoubleEnumeration implements Enumeration {
    /**
     * This is true as long as the first enumeration may have more
     * elements.  
     */
    boolean hasMore;
    /**
     * This is true, if it is sure that the first enumeration has
     * more elements.
     */
    boolean hasChecked;
    /**
     * The first enumeration.
     */
    Enumeration e1;
    /**
     * The second enumeration.
     */
    Enumeration e2;

    /**
     * Creates a new Enumeration combining the given two enumerations.
     * The enumerations mustn't be accessed by other classes.
     */
    DoubleEnumeration(Enumeration e1, Enumeration e2) {
        this.e1 = e1;
        this.e2 = e2;
        hasMore = true;
	hasChecked = false;
    }

    /**
     * Returns true, if at least one of the two enumerations has more
     * elements.
     */
    public boolean hasMoreElements() {
        if (hasMore)
            hasChecked = hasMore = e1.hasMoreElements();
        return hasMore ? true : e2.hasMoreElements();
    }

    /**
     * Returns the next element.  This returns the next element of the
     * first enumeration, if it has more elements, otherwise the next
     * element of the second enumeration.
     */
    public Object nextElement() {
        if (hasMore && !hasChecked)
            hasMore = e1.hasMoreElements();
	hasChecked = false;
        return hasMore ? e1.nextElement() : e2.nextElement();
    }
}
