/* PreferenceChangeEvent - ObjectEvent fired when a Preferences entry changes
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
 * ObjectEvent fired when a Preferences entry changes.
 * This event is generated when a entry is added, changed or removed.
 * When an entry is removed then <code>getNewValue</code> will return null.
 * <p>
 * Preference change events are only generated for entries in one particular
 * preference node. Notification of subnode addition/removal is given by a
 * <code>NodeChangeEvent</code>.
 *
 * @since 1.4
 * @author Mark Wielaard (mark@klomp.org)
 */
public class PreferenceChangeEvent extends EventObject {
    /**
     * The key of the changed entry.
     */
    private final String key;

    /**
     * The new value of the changed entry, or null when the entry was removed.
     */
    private final String newValue;

    /**
     * Creates a new PreferenceChangeEvent.
     *
     * @param node The source preference node for which an entry was added,
     * changed or removed
     * @param key The key of the entry that was added, changed or removed
     * @param value The new value of the entry that was added or changed, or
     * null when the entry was removed
     */
    public PreferenceChangeEvent(Preferences node, String key, String value) {
        super(node);
        this.key = key;
        this.newValue = value;
    }

    /**
     * Returns the source Preference node from which an entry was added,
     * changed or removed.
     */
    public Preferences getNode() {
        return (Preferences) source;
    }

    /**
     * Returns the key of the entry that was added, changed or removed.
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the new value of the entry that was added or changed, or
     * returns null when the entry was removed.
     */
    public String getNewValue() {
        return newValue;
    }
}
