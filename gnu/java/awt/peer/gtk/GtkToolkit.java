/*
 * GtkToolkit.java -- Implements an AWT Toolkit using GTK for peers
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
import java.net.*;
import java.util.*;
import java.awt.datatransfer.*;
import java.awt.image.*;
import java.awt.peer.*;
import java.util.Hashtable;

/* This class uses a deprecated method java.awt.peer.ComponentPeer.getPeer().
   This merits comment.  We are basically calling Sun's bluff on this one.
   We think Sun has deprecated it simply to discourage its use as it is 
   bad programming style.  However, we need to get at a component's peer in
   this class.  If getPeer() ever goes away, we can implement a hash table
   that will keep up with every window's peer, but for now this is faster. */

public class GtkToolkit extends java.awt.Toolkit
{
  GtkMainThread main;
  Hashtable containers = new Hashtable();
  
  static 
    {
      System.out.println("loading library");	
      System.loadLibrary("gtkpeer");
    }

  public GtkToolkit ()
    {
      main=new GtkMainThread();
    }
  
  native public void beep();
  native private void getScreenSizeDimensions(int[] xy);
  
  public int checkImage (Image image, int width, int height, 
			 ImageObserver observer) 
    {
      return 0;
    }

  public Image createImage (ImageProducer producer) 
    {
      return null;
    }

  public Image createImage (byte[] imagedata, int imageoffset, 
			    int imagelength) 
    {
      return null;
    }

  public ColorModel getColorModel () 
    {
      return null;
    }

  public String[] getFontList () 
    {
      return null;
    }

  public FontMetrics getFontMetrics (Font font) 
    {
      return null;
    }

  public Image getImage (String filename) 
    {
      return null;
    }

  public Image getImage (URL url) 
    {
      return null;
    }

  public PrintJob getPrintJob (Frame frame, String jobtitle, 
			       Properties props) 
    {
      return null;
    }

  native public int getScreenResolution();

  public Dimension getScreenSize () {
    int dim[] = new int[2];
    getScreenSizeDimensions(dim);
    return new Dimension(dim[0], dim[1]);
  }

  public Clipboard getSystemClipboard() 
    {
      return null;
    }

  public boolean prepareImage (Image image, int width, int height, 
			       ImageObserver observer) 
    {
      return false;
    }

  public void sync() 
    {
    }

  protected ButtonPeer createButton (Button b)
    {
      return(new GtkButtonPeer (b,b.getParent().getPeer()));
    }

  protected CanvasPeer createCanvas (Canvas c) 
    {
      return null;
    }

  protected CheckboxPeer createCheckbox (Checkbox cb) 
    {
      return (new GtkCheckboxPeer (cb, cb.getParent().getPeer()));
    }

  protected CheckboxMenuItemPeer createCheckboxMenuItem (CheckboxMenuItem cmi)
    {
      return null;
    }

  protected ChoicePeer createChoice (Choice c) 
    {
      return (new GtkChoicePeer (c, c.getParent().getPeer()));
    }

  protected DialogPeer createDialog (Dialog d)
    {
      return(new GtkDialogPeer(d));
    }

  protected FileDialogPeer createFileDialog (FileDialog fd)
    {
      return(new GtkFileDialogPeer(fd));
    }

  protected FramePeer createFrame (Frame f)
    {
	return(new GtkFramePeer (f));
    }

  protected LabelPeer createLabel (Label l) 
    {
      return null;
    }

  protected ListPeer createList (List l) 
    {
      return null;
    }

  protected MenuPeer createMenu (Menu m) 
    {
      return null;
    }

  protected MenuBarPeer createMenuBar (MenuBar mb) 
    {
      return null;
    }

  protected MenuItemPeer createMenuItem (MenuItem mi) 
    {
      return null;
    }

  protected PanelPeer createPanel (Panel p) 
    {
      return (new GtkPanelPeer (p,p.getParent().getPeer()));
    }

  protected PopupMenuPeer createPopupMenu (PopupMenu target) 
    {
      return null;
    }

  protected ScrollPanePeer createScrollPane (ScrollPane sp) 
    {
      return (new GtkScrollPanePeer (sp,sp.getParent().getPeer()));
    }

  protected ScrollbarPeer createScrollbar (Scrollbar sb) 
    {
      return null;
    }

  protected TextAreaPeer createTextArea (TextArea ta) 
    {
      return (new GtkTextAreaPeer (ta, ta.getParent().getPeer()));
    }

  protected TextFieldPeer createTextField (TextField tf) 
    {
      return (new GtkTextFieldPeer (tf, tf.getParent().getPeer()));
    }

  protected WindowPeer createWindow(Window w)
    {
      return(new GtkWindowPeer(w));
    }

  protected FontPeer getFontPeer (String name, int style) 
    {
      return null;
    }

  protected EventQueue getSystemEventQueueImpl() 
    {
      return null;
    }

  protected void loadSystemColors (int[] systemColors) 
    {
    }
}
