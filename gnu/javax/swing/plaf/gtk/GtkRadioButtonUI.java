/*************************************************************************
 * GtkRadioButtonUI.java
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
import javax.swing.plaf.basic.*;

/**
 *
 * @author Brian Jones
 * @see javax.swing.LookAndFeel
 */
public class GtkRadioButtonUI extends BasicRadioButtonUI
{
    public GtkRadioButtonUI() 
    {
	super();
    }

    public static ComponentUI createUI(JComponent c)
    {
	return new GtkRadioButtonUI();
    }

    public String getPropertyPrefix()
    {
	// FIXME
	System.err.println(super.getPropertyPrefix());
	return super.getPropertyPrefix();
    }
}

