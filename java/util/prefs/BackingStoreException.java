/* BackingStoreException - Chained exception thrown when backing store fails
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

package java.util.prefs;

/**
 * Chained exception thrown when backing store fails.
 * This exception is only thrown from methods that actually have to access
 * the backing store such as <code>clear(), keys(), childrenNames(),
 * nodeExists(), removeNode(), flush(), sync(), exportNode(), exportSubTree()
 * </code>, normal operations do not throw BackingStoreExceptions.
 *
 * @since 1.4
 * @author Mark Wielaard (mark@klomp.org)
 */
public class BackingStoreException extends Exception {

    /**
     * Creates a new exception with a descriptive message.
     */
    public BackingStoreException(String message) {
        super(message);
    }

    /**
     * Create a new exception with the given cause.
     */
    public BackingStoreException(Throwable cause) {
        // XXX - use when we have 1.4 Throwable support
        // super(cause);
        super();
    }
}
