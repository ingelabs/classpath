/* UIResource.java 
   Copyright (C) 1999 Free Software Foundation, Inc.

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
