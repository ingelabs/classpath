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
import java.awt.event.*;
import java.awt.peer.*;

class Test
{
  public static void main(String args[])
    {

      Properties prop=System.getProperties ();
      prop.put ("awt.toolkit","gnu.java.awt.peer.gtk.GtkToolkit");

      final Frame f=new Frame();
      
//        f.addComponentListener (new ComponentAdapter() {
//  	public void componentMoved (ComponentEvent e) {
//  	  System.out.println("component moved");
//  	}
//  	public void componentResized (ComponentEvent e) {
//  	  System.out.println("component resized");
//  	}
//        });
      f.setSize(200,200);

      Panel pan=new Panel();

      Label l = new Label ("Pithy Message:");
      l.setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));
      pan.add (l);

      TextField tf = new TextField("Hello world!");
      pan.add(tf);

      List ch=new List();
      ch.add("Ding");
      ch.add("September");
      ch.add("Red");
      ch.add("Quassia");
      ch.add("Pterodactyl");

      ch.addMouseListener(new MouseAdapter() {
	public void mousePressed(MouseEvent e) {
	  System.out.println("mouse pressed ch");
	  System.out.println("shift = " + e.isShiftDown());
	  System.out.println("meta = " + e.isMetaDown());
	  System.out.println("alt = " + e.isAltDown());
	  System.out.println("ctrl = " + e.isControlDown());
	  System.out.println("x = " + e.getX());
	  System.out.println("y = " + e.getY());
	  System.out.println("clickcount = " + e.getClickCount());
	  System.out.println("when = " + e.getWhen());
	  System.out.println();
	}
	public void mouseReleased(MouseEvent e) {
	  System.out.println("mouse released ch");
	}
	public void mouseClicked(MouseEvent e) {
	  System.out.println("mouse clicked ch");
	}
      });

      pan.add(ch);
      f.add(pan,"North");

      ScrollPane sp=new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
      Panel p=new Panel();
      p.add(new Button("Stop"));
      p.add(new Button("evil"));
      p.add(new Button("hoarders"));
      p.add(new Button("use"));
      p.add(new Button("GNU"));
      p.add(new Scrollbar(Scrollbar.HORIZONTAL));

      sp.add(p);
      f.add(sp,"South");

      Panel east_panel = new Panel();
      east_panel.setLayout(new GridLayout (0,1));

      CheckboxGroup group = new CheckboxGroup();

      Checkbox cb=new Checkbox("one", group, true);
      east_panel.add(cb);
      cb=new Checkbox("two", group, false);
      east_panel.add(cb);

      cb.addMouseListener(new MouseAdapter() {
	  public void mousePressed(MouseEvent e) {
	  System.out.println("mouse pressed cb");
	  System.out.println("shift = " + e.isShiftDown());
	  System.out.println("meta = " + e.isMetaDown());
	  System.out.println("alt = " + e.isAltDown());
	  System.out.println("ctrl = " + e.isControlDown());
	  System.out.println("x = " + e.getX());
	  System.out.println("y = " + e.getY());
	  System.out.println("clickcount = " + e.getClickCount());
	  System.out.println("when = " + e.getWhen());
	  System.out.println();
	}
	public void mouseReleased(MouseEvent e) {
	  System.out.println("mouse released cb");
	}
	public void mouseClicked(MouseEvent e) {
	  System.out.println("mouse clicked cb");
	}
	public void mouseEntered(MouseEvent e) {
	  System.out.println("mouse entered cb");
	}
	public void mouseExited(MouseEvent e) {
	  System.out.println("mouse exited cb");
	}
      });

      f.add(east_panel,"East");

      Button wb=new Button();
      wb.setLabel("Destroy Frame on Click");
      wb.addActionListener (new ActionListener () {
	public void actionPerformed (ActionEvent e) {
	  System.out.println ("action listener on wb called");
	  System.exit (0);
	}
      });

      wb.addMouseListener(new MouseAdapter() {
	public void mousePressed(MouseEvent e) {
	  System.out.println("mouse pressed wb");
	}
	public void mouseReleased(MouseEvent e) {
	  System.out.println("mouse released wb");
	}
	public void mouseClicked(MouseEvent e) {
	  System.out.println("mouse clicked wb");
	}
	public void mouseEntered(MouseEvent e) {
	  System.out.println("mouse entered wb");
	}
	public void mouseExited(MouseEvent e) {
	  System.out.println("mouse exited wb");
	}
      });

      f.add(wb,"West");
      
      f.pack();
      f.show();
      sp.setScrollPosition (10,0);

      Toolkit t = Toolkit.getDefaultToolkit();
      /* t.beep(); */
      System.out.println("screen size: " + t.getScreenSize());
      System.out.println("resolution : " + t.getScreenResolution());
      try {
	Thread.sleep (5000);
      } catch (InterruptedException e) {}
      f.setSize(500,500);

      System.out.println("done");
    }
}



