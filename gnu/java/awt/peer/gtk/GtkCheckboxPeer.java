/*
 * GtkCheckboxPeer.java -- Implements CheckboxPeer with GTK
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
import java.awt.peer.*;
import java.awt.*;

public class GtkCheckboxPeer extends GtkComponentPeer
  implements CheckboxPeer
{

  native void GtkRadioButtonSetGroup (Object group);
  native void GtkRadioButtonNew (Object group, boolean checked, String label);
  native void GtkCheckButtonNew (boolean checked, String label);
  native void GtkCheckButtonSetState (boolean checked);
  native void GtkCheckButtonSetLabel (String label);

  public GtkCheckboxPeer (Checkbox c, ComponentPeer cp)
    {
      Point p = c.getLocation();

      System.out.println("checkboxpeer: location: "+p.x+","+p.y);

      CheckboxGroup group = c.getCheckboxGroup ();
      
      if (group == null)
	GtkCheckButtonNew (c.getState (), c.getLabel ());
      else
	GtkRadioButtonNew (group, c.getState(), c.getLabel ());

      GtkFixedPut (cp, p.x, p.y);
    }

  public void setCheckboxGroup (CheckboxGroup group)
    {
      GtkRadioButtonSetGroup (group);
    }
  
  public void setLabel (String label)
    {
      GtkCheckButtonSetLabel (label);
    }
  
  public void setState (boolean state)
    {
      GtkCheckButtonSetState (state);
    }
}
