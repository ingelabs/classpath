/* GtkDialogPeer.java -- Implements DialogPeer with GTK
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

This file is part of the peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


package gnu.java.awt.peer.gtk;
import java.awt.*;
import java.awt.peer.*;
import java.awt.event.*;

public class GtkDialogPeer extends GtkWindowPeer
  implements DialogPeer
{
  public GtkDialogPeer (Dialog dialog)
  {
    super (dialog);
  }

  void create ()
  {
    create (GTK_WINDOW_DIALOG);
  }

  public void getArgs (Component component, GtkArgList args)
  {
    super.getArgs (component, args);

    Dialog dialog = (Dialog) component;

    args.add ("modal", dialog.isModal ());
    args.add ("allow_shrink", dialog.isResizable ());
    args.add ("allow_grow", dialog.isResizable ());
  }

  public void handleEvent (AWTEvent event)
  {
    int id = event.getID();
    
    if (id == WindowEvent.WINDOW_CLOSING)
      System.out.println ("got a closing event");
  }

}
