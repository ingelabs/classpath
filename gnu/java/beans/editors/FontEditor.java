/*
 * gnu.java.beans.editors.FontEditor: part of the Java Class Libraries project.
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
import java.awt.Font;

/**
 ** FontEditor is a property editor for java.awt.Font.
 **
 ** <STRONG>To Do:</STRONG> Add custom font chooser
 ** component.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 **/

public class FontEditor extends PropertyEditorSupport {
	/** setAsText for Font calls Font.decode(). **/
	public void setAsText(String val) throws IllegalArgumentException {
		setValue(Font.decode(val));
	}

	/** getAsText for Font returns a value in the format
	 ** expected by Font.decode().
	 **/
	public String getAsText() {
		Font f = (Font)getValue();
		if(f.isBold()) {
			if(f.isItalic()) {
				return f.getName()+"-bolditalic-"+f.getSize();
			} else {
				return f.getName()+"-bold-"+f.getSize();
			}
		} else if(f.isItalic()) {
			return f.getName()+"-italic-"+f.getSize();
		} else {
			return f.getName()+"-"+f.getSize();
		}
	}
}
