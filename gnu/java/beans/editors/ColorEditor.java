/*
 * gnu.java.beans.editors.ColorEditor: part of the Java Class Libraries project.
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
import java.awt.Color;

/**
 ** NativeByteEditor is a property editor for the
 ** byte type.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 **/

public class ColorEditor extends PropertyEditorSupport {
	Color[] stdColors = {Color.black,Color.blue,Color.cyan,
	                     Color.darkGray,Color.gray,Color.green,
	                     Color.lightGray,Color.magenta,Color.orange,
	                     Color.pink,Color.red,Color.white,
	                     Color.yellow};
	String[] stdColorNames = {"black","blue","cyan",
	                          "dark gray","gray","green",
	                          "light gray","magenta","orange",
	                          "pink","red","white",
	                          "yellow"};

	/** setAsText for Color checks for standard color names
	 ** and then checks for a #RRGGBB value or just RRGGBB,
	 ** both in hex.
	 **/
	public void setAsText(String val) throws IllegalArgumentException {
		if(val.length() == 0) {
			throw new IllegalArgumentException("Tried to set empty value!");
		}
		for(int i=0;i<stdColorNames.length;i++) {
			if(stdColorNames[i].equalsIgnoreCase(val)) {
				setValue(stdColors[i]);
				return;
			}
		}
		if(val.charAt(0) == '#') {
			setValue(new Color(Integer.parseInt(val.substring(1),16)));
		} else {
			setValue(new Color(Integer.parseInt(val,16)));
		}
	}

	/** getAsText for Color turns the color into either one of the standard
	 ** colors or into an RGB hex value with # prepended. **/
	public String getAsText() {
		for(int i=0;i<stdColors.length;i++) {
			if(stdColors[i].equals(getValue())) {
				return stdColorNames[i];
			}
		}
		return "#" + Integer.toHexString(((Color)getValue()).getRGB() & 0x00FFFFFF);
	}

	/** getTags for Color returns a list of standard colors. **/
	public String[] getTags() {
		return stdColorNames;
	}
}
