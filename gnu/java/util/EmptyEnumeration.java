/* gnu.java.util.EmptyEnumeration
   Copyright (C) 2001 Free Software Foundation, Inc.

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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */

package gnu.java.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * This is a helper class that produces an empty Enumerations.
 * There is only one instance of this class that can be used whenever one
 * needs a non-null but empty enumeration. Using this class prevents multiple
 * small objects and inner classes. <code>getInstance()</code> returns
 * the only instance of this class. It can be shared by multiple objects and
 * threads
 * <p>
 * <code>hasMoreElements()</code> always returns <code>false</code>.
 * <code>nextElement()</code> always throws <code>NoSuchElementException</code>.
 * 
 * @author Mark Wielaard (mark@klomp.org)
 */
public final class EmptyEnumeration implements Enumeration {

    /** The only instance of this class */
    private static final EmptyEnumeration instance = new EmptyEnumeration();

    /**
     * Private constructor that creates a new empty Enumeration.
     */
    private EmptyEnumeration() {
    }

    /**
     * Returns the only instance of this class.
     * It can be shared by multiple objects and threads.
     */
    public static EmptyEnumeration getInstance() {
        return instance;
    }

    /**
     * Always returns <code>false</code>.
     */
    public boolean hasMoreElements() {
        return false;
    }

    /**
     * Always throws <code>NoSuchElementException</code>.
     */
    public Object nextElement() throws NoSuchElementException {
        throw new NoSuchElementException();
    }
}
