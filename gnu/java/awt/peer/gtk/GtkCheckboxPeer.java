/* GtkCheckboxPeer.java -- Implements CheckboxPeer with GTK
   Copyright (C) 1998, 1999, 2002 Free Software Foundation, Inc.

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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package gnu.java.awt.peer.gtk;
import java.awt.peer.*;
import java.awt.*;

public class GtkCheckboxPeer extends GtkComponentPeer
  implements CheckboxPeer
{
  // Group from last time it was set.
  public CheckboxGroup old_group;

  public native void nativeCreate (CheckboxGroup group);
  public native void nativeSetCheckboxGroup (CheckboxGroup group,
					     CheckboxGroup old_group);
  public native void connectHooks ();

  public GtkCheckboxPeer (Checkbox c)
  {
    super (c);
  }

  // We can't fully use the ordinary getArgs code here, due to
  // oddities of this particular widget.  In particular we must be
  // able to switch between a checkbutton and a radiobutton
  // dynamically.
  public void create ()
  {
    CheckboxGroup g = ((Checkbox) awtComponent).getCheckboxGroup ();
    nativeCreate (g);
  }

  public void setState (boolean state)
  {
    set ("active", state);
  }

  public void setLabel (String label)
  {
    set ("label", label);
  }

  public void setCheckboxGroup (CheckboxGroup group)
  {
    nativeSetCheckboxGroup (group, old_group);
    old_group = group;
  }

  public void getArgs (Component component, GtkArgList args)
  {
    super.getArgs (component, args);
    args.add ("active", ((Checkbox) component).getState ());
    args.add ("label", ((Checkbox) component).getLabel ());
  }

  // Override the superclass postItemEvent so that the peer doesn't
  // need information that we have.
  public void postItemEvent (Object item, int stateChange)
  {
    super.postItemEvent (awtComponent, stateChange);
  }
}
