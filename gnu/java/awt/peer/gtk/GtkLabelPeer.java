/*
 * GtkLabelPeer.java -- Implements LabelPeer with GTK
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
import java.awt.peer.*;

public class GtkLabelPeer extends GtkComponentPeer
    implements LabelPeer
{

  native void gtkLabelNew (String text, int just);
  native void gtkLabelSet (String label);    
  native void gtkLabelSetJustify (int just);    

  public GtkLabelPeer (Label l, ComponentPeer cp)
    {
      int j=1;

      switch (l.getAlignment())
	{
	case Label.LEFT:
	  j=0;
	  break;
	case Label.RIGHT:
	  j=2;
	  break;
	}

      gtkLabelNew (l.getText(), j);
      Point p=l.getLocation();
      
      gtkFixedPut (cp,p.x,p.y);
    }
    
    public void setText (String text) 
    {
      gtkLabelSet(text);
    }    

    public void setAlignment (int alignment) 
    {
      int j=1;

      switch (alignment)
	{
	case Label.LEFT:
	  j=0;
	  break;
	case Label.RIGHT:
	  j=2;
	  break;
	}
      gtkLabelSetJustify(j);
    }    
}
