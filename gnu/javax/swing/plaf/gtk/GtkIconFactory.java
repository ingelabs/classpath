/* GtkIconFactory.java
   Copyright (c) 1999 by Free Software Foundation, Inc.

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

package gnu.javax.swing.plaf.gtk;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.io.Serializable;

/**
 *
 * @author Brian Jones
 * @see javax.swing.LookAndFeel
 */
public class GtkIconFactory implements Serializable
{
    private static Icon radioButtonIcon;
    private static Icon checkBoxIcon;

    public static Icon getRadioButtonIcon() 
    {
	if (radioButtonIcon == null)
	    radioButtonIcon = new RadioButtonIcon();
	return radioButtonIcon;
    }
    
    private static class RadioButtonIcon 
	implements Icon, UIResource, Serializable
    {
	private static final int size = 15;
	
	public int getIconWidth() { return size; }
	public int getIconHeight() { return size; }

	public void paintIcon(Component c, Graphics g, int x, int y) 
	{
	    System.out.println("radiobuttonicon: paintIcon()");
	    // get the button and model containing the state we are 
	    // supposed to show
	    AbstractButton b = (AbstractButton)c;
	    ButtonModel model = b.getModel();

	    // If the button is being pressed (& armed), change the 
	    // background color 
	    // Note: could also do something different if the button is 
	    // disabled
	    
	    if (model.isPressed() && model.isArmed())
		{
		    System.out.println("radiobuttonicon: pressed & armed");
		    g.setColor(UIManager.getColor("RadioButton.pressed"));
		    g.fillOval(x,y,size-1, size-1);
		}
	    // draw an outer circle
	    g.setColor(UIManager.getColor("RadioButton.foreground"));
	    g.drawOval(x,y,size-1, size-1);
	    
	    // fill a small circle inside if the button is selected
	    if (model.isSelected()) {
		g.fillOval(x+4, y+4, size-8, size-8);
		System.out.println("radiobuttonicon: is selected");
	    }
	}
    }
}
