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
 * This is a package private class that compines two Enumerations.
 *
 * @author Jochen Hoenicke 
 */
class DoubleEnumeration implements Enumeration {
    boolean hasMore1;
    Enumeration e1;
    Enumeration e2;

    DoubleEnumeration(Enumeration e1, Enumeration e2) {
        this.e1 = e1;
        this.e2 = e2;
        hasMore1 = true;
    }

    public boolean hasMoreElements() {
        if (hasMore1)
            hasMore1 = e1.hasMoreElements();
        return hasMore1 ? true : e2.hasMoreElements();
    }

    public Object nextElement() {
        if (hasMore1)
            hasMore1 = e1.hasMoreElements();
        return hasMore1 ? e1.nextElement() : e2.nextElement();
    }
}

