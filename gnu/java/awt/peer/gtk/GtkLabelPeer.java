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
  native void create ();

  public GtkLabelPeer (Label l)
  {
    super (l);
  }
    
  public void setText (String text)
  {
    set ("label", text);
  }

  public void setAlignment (int alignment)
  {
    set ("xalign", getGtkAlignment (alignment));
  }

  float getGtkAlignment (int alignment)
  {
    switch (alignment)
      {
      case Label.LEFT:
	return 0.0f;
      case Label.CENTER:
	return 0.5f;
      case Label.RIGHT:
	return 1.0f;
      }

    return 0.0f;
  }

  public void getArgs (Component component, GtkArgList args)
  {
    super.getArgs (component, args);

    Label label = (Label) component;

    args.add ("label", label.getText ());
    args.add ("xalign", getGtkAlignment (label.getAlignment ()));
    args.add ("yalign", 0.5f);
  }
}
