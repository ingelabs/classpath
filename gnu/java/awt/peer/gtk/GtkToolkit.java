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
import java.util.Hashtable;
import java.util.Properties;
import java.util.MissingResourceException;
import java.awt.datatransfer.*;
import java.awt.image.*;
import java.awt.peer.*;

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
  static EventQueue q = new EventQueue();
  static Clipboard systemClipboard;

  static 
  {
    System.out.println("loading library");	
    System.loadLibrary("gtkpeer");
  }

  public GtkToolkit ()
  {
    main = new GtkMainThread ();
    systemClipboard = new GtkClipboard ();
    GtkGenericPeer.enableQueue (q);
  }
  
  native public void beep ();
  native private void getScreenSizeDimensions (int[] xy);
  
  public int checkImage (Image image, int width, int height, 
			 ImageObserver observer) 
  {
    return 0;
  }

  public Image createImage (ImageProducer producer) 
  {
    return new GtkImage (producer, null);
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
    return (new String[] { "Dialog", 
			   "DialogInput", 
			   "Monospaced", 
			   "Serif", 
			   "SansSerif" });
  }

  public FontMetrics getFontMetrics (Font font) 
  {
    return new GdkFontMetrics (font);
  }

  public Image getImage (String filename) 
  {
    return null;
  }

  public Image getImage (URL url) 
  {
    return null;
  }

  public PrintJob getPrintJob (Frame frame, String jobtitle, Properties props) 
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
    return systemClipboard;
  }

  public boolean prepareImage (Image image, int width, int height, 
			       ImageObserver observer) 
  {
    return false;
  }

  native public void sync ();

  protected void setComponentState (Component c, GtkComponentPeer cp)
  {
    /* Make the Component reflect Peer defaults */
    if (c.getForeground () == null)
      c.setForeground (cp.getForeground ());
    if (c.getBackground () == null)
      c.setBackground (cp.getBackground ());
    //        if (c.getFont () == null)
    //  	c.setFont (cp.getFont ());
      
    /* Make the Peer reflect the state of the Component */
    if (! (c instanceof Window))
      {
	cp.setCursor (c.getCursor ());
	
	Rectangle bounds = c.getBounds ();
	cp.setBounds (bounds.x, bounds.y, bounds.width, bounds.height);
	if (c instanceof Canvas)
	  System.out.println ("width " + bounds.width + " height " + bounds.height);
	
	cp.setVisible (c.isVisible ());
      }
  }

  protected ButtonPeer createButton (Button b)
  {
    GtkButtonPeer bp = new GtkButtonPeer (b);
    Rectangle bounds = b.getBounds ();
    bp.setBounds (bounds.x, bounds.y, bounds.width, bounds.height);
    return bp;
  }

  protected CanvasPeer createCanvas (Canvas c) 
  {
    return new GtkCanvasPeer (c);
  }

  protected CheckboxPeer createCheckbox (Checkbox cb) 
  {
    if (cb.getCheckboxGroup () != null)
      return new GtkRadioButtonPeer (cb);
    else
      return new GtkCheckButtonPeer (cb);
  }

  protected CheckboxMenuItemPeer createCheckboxMenuItem (CheckboxMenuItem cmi)
  {
    return new GtkCheckboxMenuItemPeer (cmi);
  }

  protected ChoicePeer createChoice (Choice c) 
  {
    return new GtkChoicePeer (c);
  }

  protected DialogPeer createDialog (Dialog d)
  {
    return new GtkDialogPeer (d);
  }

  protected FileDialogPeer createFileDialog (FileDialog fd)
  {
    return new GtkFileDialogPeer (fd);
  }

  protected FramePeer createFrame (Frame f)
  {
    return new GtkFramePeer (f);
  }

  protected LabelPeer createLabel (Label label) 
  {
    return new GtkLabelPeer (label);
  }

  protected ListPeer createList (List list)
  {
    return new GtkListPeer (list);
  }

  protected MenuPeer createMenu (Menu m) 
  {
    return new GtkMenuPeer (m);
  }

  protected MenuBarPeer createMenuBar (MenuBar mb) 
  {
    return new GtkMenuBarPeer (mb);
  }

  protected MenuItemPeer createMenuItem (MenuItem mi) 
  {
    return new GtkMenuItemPeer (mi);
  }

  protected PanelPeer createPanel (Panel p) 
  {
    return new GtkPanelPeer (p);
  }

  protected PopupMenuPeer createPopupMenu (PopupMenu target) 
  {
    return new GtkPopupMenuPeer (target);
  }

  protected ScrollPanePeer createScrollPane (ScrollPane sp) 
  {
    return null;
//      return new GtkScrollPanePeer (sp);
  }

  protected ScrollbarPeer createScrollbar (Scrollbar sb) 
  {
    return new GtkScrollbarPeer (sb);
  }

  protected TextAreaPeer createTextArea (TextArea ta) 
  {
    return new GtkTextAreaPeer (ta);
  }

  protected TextFieldPeer createTextField (TextField tf) 
  {
    return new GtkTextFieldPeer (tf);
  }

  protected WindowPeer createWindow (Window w)
  {
    return new GtkWindowPeer (w);
  }

  protected FontPeer getFontPeer (String name, int style) 
  {
    try {
      GtkFontPeer fp = new GtkFontPeer (name, style);
      return fp;
    } catch (MissingResourceException ex) {
      return null;
    }
  }

  protected EventQueue getSystemEventQueueImpl() 
  {
    return q;
  }

  protected void loadSystemColors (int[] systemColors) 
  {
  }
}
