/* GtkCheckboxPeer.java -- Implements CheckboxPeer with GTK
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
import java.awt.peer.*;
import java.awt.*;

public class GtkCheckboxPeer extends GtkComponentPeer
  implements CheckboxPeer
{

  native void gtkRadioButtonSetGroup (Object group);
  native void gtkRadioButtonNew (ComponentPeer parent,
				 Object group, boolean checked, String label);
  native void gtkCheckButtonNew (ComponentPeer parent,
				 boolean checked, String label);
  native void gtkCheckButtonSetState (boolean checked);
  native void gtkCheckButtonSetLabel (String label);

  public GtkCheckboxPeer (Checkbox c, ComponentPeer cp)
  {
    super (c);
    
    CheckboxGroup group = c.getCheckboxGroup ();
    
    if (group == null)
      gtkCheckButtonNew (cp, c.getState (), c.getLabel ());
    else
      gtkRadioButtonNew (cp, group, c.getState(), c.getLabel ());
  }

  public void setCheckboxGroup (CheckboxGroup group)
  {
    gtkRadioButtonSetGroup (group);
  }
  
  public void setLabel (String label)
  {
    gtkCheckButtonSetLabel (label);
  }
  
  public void setState (boolean state)
  {
    gtkCheckButtonSetState (state);
  }
}
