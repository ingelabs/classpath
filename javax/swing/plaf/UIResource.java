/*************************************************************************
 * UIResource.java 
 *
 * Copyright (c) 1999 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package javax.swing.plaf;

/**
 * This interface is used to designate which objects were created by
 * <code>ComponentUI</code> delegates.  When uninstalling the user interface
 * renderer with <code>ComponentUI.uninstallUI()</code> the renderer
 * property is set to <code>null</code>.
 * <br>
 * A comparison against null can be used with all properties except for 
 * the <code>java.awt.Component</code> properties font, foreground, and 
 * background.  The container can provide the value of the properties if
 * they are initialized or set to <code>null</code>.
 * 
 * @author Brian Jones
 * @see java.lang.ComponentUI 
 */
public abstract interface UIResource { }
