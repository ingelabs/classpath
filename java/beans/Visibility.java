/*
 * java.beans.Visibility: part of the Java Class Libraries project.
 * Copyright (C) 1998 Free Software Foundation
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

package java.beans;

/**
 ** Visibility is an interface a Bean may implement so that
 ** the environment can tell the Bean whether there is a
 ** GUI or not, and so that the Bean can tell the
 ** environment whether it needs one or can run without
 ** one.<P>
 **
 ** Sun decided not to use standard Introspection patterns
 ** so that these methods did not get included when
 ** the Introspector made its sweep on the class.
 **
 ** @author John Keiser
 ** @since JDK1.1
 ** @version 1.1.0, 29 Jul 1998
 **/

public interface Visibility {
	/** Tells whether the Bean can run without a GUI or not.
	 ** @return false Bean can run without a GUI, else true.
	 **/
	public abstract boolean needsGui();

	/** Tells whether Bean is trying not to use the GUI.
	 ** If needsGui() is true, this method should always
	 ** return false.
	 ** @return true if definitely not using GUI, otherwise
	 **         false.
	 **/
	public abstract boolean avoidingGui();

	/** Tells the Bean not to use GUI methods.  If needsGUI()
	 ** is false, then after this method is called,
	 ** avoidingGui() should return true.
	 **/
	public abstract void dontUseGui();

	/** Tells the Bean it may use the GUI.  The Bean is not
	 ** required to use the GUI in this case, it is merely
	 ** being <EM>permitted</EM> to use it.  If needsGui() is
	 ** false, avoidingGui() may return true or false after
	 ** this method is called.
	 **/
	public abstract void okToUseGui();
}