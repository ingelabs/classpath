/* GtkToggleButtonPeer.java -- Base class for CheckboxButtons and RadioButtons
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
import java.awt.Checkbox;
import java.awt.Component;

public class GtkToggleButtonPeer extends GtkComponentPeer
{
  public GtkToggleButtonPeer (Checkbox c)
  {
    super (c);
  }
  
  public void setLabel (String label)
  {
    set ("label", label);
  }

  public void setState (boolean state)
  {
    set ("active", state);
  }

  public void getArgs (Component component, GtkArgList args)
  {
    super.getArgs (component, args);

    Checkbox cb = (Checkbox) component;

    args.add ("label", cb.getLabel ());
    args.add ("active", cb.getState ());
  }
}
