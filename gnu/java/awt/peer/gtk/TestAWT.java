/*
 * Test.java -- Tests the GTK Toolkit
 *
 * Copyright (c) 1998, 1999 Free Software Foundation, Inc.
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

class TestAWT
{
  public static void main(String args[])
    {
      if (args.length==0)
	{
	  Properties prop = System.getProperties ();
	  prop.put ("awt.toolkit", "gnu.java.awt.peer.gtk.GtkToolkit");
	}
      MainWindow f = new MainWindow();
      f.show();
    }
}

class PrettyPanel extends Panel
{
  Insets myInsets;

  public PrettyPanel ()
    {
      myInsets = new Insets (10, 10, 10, 10);
    }

  public Insets getInsets ()
    {
      return myInsets;
    }
}

class PrettyFrame extends Frame
{
  Insets myInsets;

  public PrettyFrame (String title)
    {
      super (title);
      myInsets = new Insets (10, 10, 10, 10);
      ((BorderLayout) getLayout ()).setHgap (5);
      ((BorderLayout) getLayout ()).setVgap (5);
    }

  public PrettyFrame ()
    {
      this ("");
    }

  public Insets getInsets()
    {
      return myInsets;
    }
}

class MainWindow extends PrettyFrame implements ActionListener 
{
  Button closeButton, buttonsButton, dialogButton;
  Frame buttonsWindow = null;
  Dialog dialogWindow = null;

  MainWindow () 
    {
      super ("TestAWT");

      ScrollPane sp = new ScrollPane();
      PrettyPanel p = new PrettyPanel();
      p.setLayout (new GridLayout (10, 1));
      sp.add (p);
      add (sp, "Center");

      add (new Label ("Classpath v0.0.0"), "North");
      
      closeButton = new Button ("Close");
      closeButton.addActionListener (this);
      add (closeButton, "South");

      buttonsButton = new Button ("buttons");
      buttonsButton.addActionListener (this);
      p.add (buttonsButton);

      dialogButton = new Button ("dialog");
      dialogButton.addActionListener (this);
      p.add (dialogButton);

      pack();
    }

  public void actionPerformed (ActionEvent evt)
    {
      Button source = (Button) evt.getSource ();
      
      System.out.println (source);
      System.out.println (closeButton);

      if (source==closeButton)
	{
	  dispose();
	  System.exit (0);
	}
      if (source==buttonsButton)
	{
	  if (buttonsWindow == null)
	    {
	      buttonsWindow = new ButtonsWindow (this);
	      buttonsWindow.show();
	    }
	  else 
	    buttonsWindow.dispose();
	}
      if (source==dialogButton)
	{
	  if (dialogWindow == null)
	    {
	      dialogWindow = new DialogWindow (this);
	      dialogWindow.show();
	    }
	  else 
	    dialogWindow.dispose();
	}
    }
}

class ButtonsWindow extends PrettyFrame
{
  static Frame f;
  MainWindow mainWindow;

  public ButtonsWindow (MainWindow mw)
    {
      super ("GtkButton");

      mainWindow=mw;
      Panel p = new Panel ();
      p.setLayout (new GridLayout (0, 3, 5, 5));
      
      final Button b[] = new Button [9];

      for (int i=0; i<9; i++) 
	b[i]=new Button ("button" + (i+1));

      p.add (b[0]);
      p.add (b[6]);
      p.add (b[4]);
      p.add (b[8]);
      p.add (b[1]);
      p.add (b[7]);
      p.add (b[3]);
      p.add (b[5]);
      p.add (b[2]);

      for (int i=0; i<9; i++)
	{
	  final int i2=((i+1)==9)?0:(i+1);

	  b[i].addMouseListener(new MouseAdapter () {
	    public void mouseClicked (MouseEvent e) {
	      if (b[i2].isVisible())
		b[i2].setVisible(false);
	      else 
		b[i2].setVisible(true);
	    }
	  });
	}

      add (p, "North");

      Button cb = new Button ("close");
      cb.addMouseListener(new MouseAdapter () {
        public void mouseClicked (MouseEvent e) {
	  dispose();
        }
      });
      add (cb, "South");

      setTitle ("GtkButton");
      pack ();
    }

  public void dispose()
    {
      mainWindow.buttonsWindow=null;
      super.dispose();
    }
}


class DialogWindow extends Dialog
{
  static Frame f;
  MainWindow mainWindow;
  Label text;

  public DialogWindow (MainWindow mw)
    {
      super (mw);

      mainWindow = mw;

      setModal (true);

      text = new Label ("Dialog Test");
      text.setAlignment (Label.CENTER);

      add (text, "North");
      text.setVisible (false);

      Panel p = new PrettyPanel();

      Button cb = new Button ("close");
      cb.addMouseListener(new MouseAdapter () {
        public void mouseClicked (MouseEvent e) {
	  dispose();
        }
      });
      p.add (cb);

      Button toggle = new Button ("Toggle");
      p.add (toggle);

      toggle.addMouseListener(new MouseAdapter () {
	public void mouseClicked (MouseEvent e) {
	  if (text.isVisible())
	    text.setVisible(false);
	  else 
	    text.setVisible(true);
	  doLayout();
	}
      });

      add (p, "South");
      setTitle ("AWTDialog");
      setSize (130, 70);
    }

  public void dispose()
    {
      mainWindow.dialogWindow=null;
      super.dispose();
    }
}







