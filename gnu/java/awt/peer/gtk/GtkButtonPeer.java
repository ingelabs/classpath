/* GtkButtonPeer.java -- Implements ButtonPeer with GTK
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

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


package gnu.java.awt.peer.gtk;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.peer.*;

public class GtkButtonPeer extends GtkComponentPeer
    implements ButtonPeer
{
  native void create ();

  public GtkButtonPeer (Button b)
  {
    super (b);
  }

  public void setLabel (String label) 
  {
    set ("label", label);
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

  public void getArgs (Component component, GtkArgList args)
  {
    super.getArgs (component, args);

    args.add ("label", ((Button)component).getLabel ());
  }
}
