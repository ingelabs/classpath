/* NodeChangeListener - EventListener for Preferences node addition/removal
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

import java.util.EventListener;

/**
 * EventListener for Preferences node addition/removal.
 * <p>
 * Note that these events are only generated for the addition and removal
 * of sub nodes from the preference node. Entry changes in the preference
 * node can be monitored with a <code>PreferenceChangeListener</code>.
 *
 * @since 1.4
 * @author Mark Wielaard (mark@klomp.org)
 */
public interface NodeChangeListener extends EventListener {

    /**
     * Fired when a sub node is added to the preference node.
     */
    void childAdded(NodeChangeEvent event);

    /**
     * Fired when a sub node is removed from the preference node.
     */
    void childRemoved(NodeChangeEvent event);

}
