/*************************************************************************
 * GtkBorders.java
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
import javax.swing.border.*;
import javax.swing.plaf.*;

/**
 * Optional class, can be used to define nifty borders.
 * 
 * @author Brian Jones
 * @see javax.swing.LookAndFeel
 */
public class GtkBorders
{
    public static class ButtonBorder extends AbstractBorder 
	implements UIResource
    {
	private Border raised;  // use by default
	private Border lowered;  // use this one when pressed

	// creat the border
        public ButtonBorder() 
	{
	    raised = BorderFactory.createRaisedBevelBorder();
	    lowered = BorderFactory.createLoweredBevelBorder();
	}

	// define the insets (in terms of one of the others)
	public Insets getBorderInsets(Component c)
	{
	    return raised.getBorderInsets(c);
	}
	
	public void paintBorder(Component c, Graphics g, int x, int y, 
				int width, int height)
	{
	    AbstractButton b = (AbstractButton)c;
	    ButtonModel model = b.getModel();
	    
	    if (model.isPressed() && model.isArmed()) 
		lowered.paintBorder(c, g, x, y, width, height);
	    else
		raised.paintBorder(c, g, x, y, width, height);
	}
    }
}
