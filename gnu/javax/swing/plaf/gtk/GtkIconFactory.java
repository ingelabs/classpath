/*************************************************************************
 * GtkIconFactory.java
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
		    g.setColor(UIManager.getColor("RadioButton.pressed"));
		    g.fillOval(x,y,size-1, size-1);
		}
	    // draw an outer circle
	    g.setColor(UIManager.getColor("RadioButton.foreground"));
	    g.drawOval(x,y,size-1, size-1);
	    
	    // fill a small circle inside if the button is selected
	    if (model.isSelected())
		g.fillOval(x+4, y+4, size-8, size-8);
	}
    }
}
