/*
 * Test.java -- Tests the GTK Toolkit
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

import java.util.*;
import java.awt.*;
import java.awt.peer.*;

class Test
{
  public static void main(String args[])
    {

      Properties prop=System.getProperties ();
      prop.put ("awt.toolkit","gnu.java.awt.peer.gtk.GtkToolkit");

      Frame f=new Frame();
      
      f.setSize(200,200);

      Panel pan=new Panel();

      TextField tf = new TextField("Hello world!");
      pan.add(tf);

      Choice ch=new Choice();
      ch.add("Ding");
      ch.add("September");
      ch.add("Red");
      ch.add("Quassia");
      ch.add("Pterodactyl");

      pan.add(ch);
      f.add(pan,"North");

      ScrollPane sp=new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
      Panel p=new Panel();
      p.add(new Button("Stop"));
      p.add(new Button("evil"));
      p.add(new Button("hoarders"));
      p.add(new Button("use"));
      p.add(new Button("GNU"));

      sp.add(p);
      f.add(sp,"South");

      Panel east_panel = new Panel();
      east_panel.setLayout(new GridLayout (0,1));

      CheckboxGroup group = new CheckboxGroup();

      Checkbox cb=new Checkbox("one", group, true);
      east_panel.add(cb);
      cb=new Checkbox("two", group, false);
      east_panel.add(cb);

      f.add(east_panel,"East");

      Button wb=new Button();
      wb.setLabel("Hello World west!");
      f.add(wb,"West");
      
      f.pack();
      f.show();
      sp.setScrollPosition (10,0);
      Toolkit t = Toolkit.getDefaultToolkit();
      /* t.beep(); */
      System.out.println("screen size: " + t.getScreenSize());
      System.out.println("resolution : " + t.getScreenResolution());
      System.out.println("done");
    }
}

