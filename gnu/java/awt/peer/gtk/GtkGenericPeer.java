/* GtkGenericPeer.java - Has a hashcode.  Yuck.
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
import java.awt.event.*;

/* This class will go away with Japhar integration.  For use with Sun's JDK
   this may be required, unless another method of associating Java objects
   with GTK objects is used. */

public class GtkGenericPeer
{
  final int native_state = java.lang.System.identityHashCode(this);
  protected Object awtWidget;
  protected static EventQueue q = null;

  protected GtkGenericPeer (Object awtWidget)
  {
    this.awtWidget = awtWidget;
  }

  public static void enableQueue (EventQueue sq) 
  {
    if (q == null)
      q = sq;
  }

  protected void postActionEvent (String command, int mods) 
  {
    q.postEvent (new ActionEvent (awtWidget, ActionEvent.ACTION_PERFORMED, 
				  command, mods));
  }
}
