/* GtkBorders.java
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
