/*
 * GtkButtonPeer.java -- Implements ButtonPeer with GTK
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by James E. Blair <corvus@gnu.org>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later verion.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package gnu.java.awt.peer.gtk;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.peer.*;

public class GtkButtonPeer extends GtkComponentPeer
    implements ButtonPeer
{
  native void gtkButtonNewWithLabel (ComponentPeer parent, String label);
  native void gtkButtonLabelSet (String label);    

  public GtkButtonPeer (Button b, ComponentPeer cp)
  {
    super (b);
    gtkButtonNewWithLabel (cp, b.getLabel());
    syncAttributes ();
  }
    
  public void setLabel (String label) 
  {
    gtkButtonLabelSet (label);
  }

  public void handleEvent (AWTEvent e)
  {
    if (e.getID () == MouseEvent.MOUSE_CLICKED && isEnabled () 
	&& !modalHasGrab ())
      {
	MouseEvent me = (MouseEvent) e;
	if (!me.isConsumed ()
	    && (me.getModifiers () & MouseEvent.BUTTON1_MASK) != 0)
	  postActionEvent (((Button)awtComponent).getActionCommand (), 
			   me.getModifiers ());
      }

    if (e.getID () == KeyEvent.KEY_PRESSED)
      {
	KeyEvent ke = (KeyEvent) e;
	if (!ke.isConsumed () && ke.getKeyCode () == KeyEvent.VK_SPACE)
	  postActionEvent (((Button)awtComponent).getActionCommand (),
			   ke.getModifiers ());
      }

    super.handleEvent (e);
  }
}



