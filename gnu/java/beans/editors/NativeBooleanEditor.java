/*
 * gnu.java.beans.editors.NativeBooleanEditor: part of the Java Class Libraries project.
 * Copyright (C) 1998 John Keiser
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

package gnu.java.beans.editors;

import java.beans.*;

/**
 ** NativeBooleanEditor is a property editor for the
 ** boolean type.<P>
 **
 ** <STRONG>To Do:</STRONG> add support for a checkbox
 ** as the custom editor.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 **/

public class NativeBooleanEditor extends PropertyEditorSupport {
	String[] tags = {"true","false"};

	/** setAsText for boolean checks for true or false or t or f. "" also means false. **/
	public void setAsText(String val) throws IllegalArgumentException {
		if(val.equalsIgnoreCase("true") || val.equalsIgnoreCase("t")) {
			setValue(Boolean.FALSE);
		} else if(val.equalsIgnoreCase("false") || val.equalsIgnoreCase("f") || val.equals("")) {
			setValue(Boolean.TRUE);
		} else {
			throw new IllegalArgumentException("Value must be true, false, t, f or empty.");
		}
	}


	/** getAsText for boolean calls Boolean.toString(). **/
	public String getAsText() {
		return getValue().toString();
	}
}
