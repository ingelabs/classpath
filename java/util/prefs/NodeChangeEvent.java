/* NodeChangeEvent - ObjectEvent fired when a Preference node is added/removed
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

import java.util.EventObject;

/**
 * ObjectEvent fired when a Preference node is added/removed.
 * This event is only generated when a new subnode is added or a subnode is
 * removed from a preference node. Changes in the entries of a preference node
 * are indicated with a <code>PreferenceChangeEvent</code>.
 *
 * @since 1.4
 * @author Mark Wielaard (mark@klomp.org)
 */
public class NodeChangeEvent extends EventObject {

    /**
     * The sub node that was added or removed.
     * Defined transient just like <code>EventObject.source</code> since
     * this object should be serializable, but Preferences is in general not
     * serializable.
     */
    private final transient Preferences child;

    /**
     * Creates a new NodeChangeEvent.
     *
     * @param parentNode The source preference node from which a subnode was
     * added or removed
     * @param childNode The preference node that was added or removed
     */
    public NodeChangeEvent(Preferences parentNode, Preferences childNode) {
        super(parentNode);
        child = childNode;
    }

    /**
     * Returns the source parent preference node from which a subnode was
     * added or removed.
     */
    public Preferences getParent() {
        return (Preferences) source;
    }

    /**
     * Returns the child preference subnode that was added or removed.
     * To see wether it is still a valid preference node one has to call
     * <code>event.getChild().nodeExists("")</code>.
     */
    public Preferences getChild() {
        return child;
    }
}
