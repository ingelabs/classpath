/* GtkComponentPeer.java -- Implements ComponentPeer with GTK
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

This file is part of the peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


package gnu.java.awt.peer.gtk;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.peer.ComponentPeer;

public class GtkComponentPeer extends GtkGenericPeer
  implements ComponentPeer
{
  Component awtComponent;

  /* this isEnabled differs from Component.isEnabled, in that it
     knows if a parent is disabled.  In that case Component.isEnabled 
     may return true, but our isEnabled will always return false */
  native boolean isEnabled ();
  native static boolean modalHasGrab ();

  native int[] gtkWidgetGetForeground ();
  native int[] gtkWidgetGetBackground ();
  native void gtkWidgetSetVisible (boolean b);
  native void gtkWidgetGetDimensions(int[] dim);
  native void gtkWidgetGetLocationOnScreen(int[] point);
  native void gtkWidgetSetCursor (int type);

  void create ()
  {
    throw new RuntimeException ();
  }

  native void connectHooks ();

  protected GtkComponentPeer (Component awtComponent)
  {
    super (awtComponent);
    this.awtComponent = awtComponent;

    /* temporary try/catch block until all peers use this creation method */
    try {
      create ();
      
      GtkArgList args = new GtkArgList ();
      getArgs (awtComponent, args);
      args.setArgs (this);

      connectHooks ();

      if (awtComponent.getForeground () == null)
	awtComponent.setForeground (getForeground ());
      if (awtComponent.getBackground () == null)
	awtComponent.setBackground (getBackground ());
      //        if (c.getFont () == null)
      //  	c.setFont (cp.getFont ());
      
      if (!(awtComponent instanceof Window))
	setCursor (awtComponent.getCursor ());
    } catch (RuntimeException ex) { ; }
  }

  public int checkImage (Image image, int width, int height, 
			 ImageObserver observer) 
  {
    GtkImage i = (GtkImage) image;
    return i.checkImage ();
  }

  public Image createImage (ImageProducer producer) 
  {
    return new GtkImage (producer, null);
  }

  public Image createImage (int width, int height)
  {
    GdkGraphics g = new GdkGraphics (width, height);
    return new GtkOffScreenImage (null, g, width, height);
  }

  public void disable () 
  {
    setEnabled (false);
  }

  native public void dispose ();

  public void enable () 
  {
    setEnabled (true);
  }

  public ColorModel getColorModel () 
  {
    return ColorModel.getRGBdefault ();
  }

  public FontMetrics getFontMetrics (Font font)
  {
    return new GdkFontMetrics (font);
  }

  public Graphics getGraphics ()
  {
    throw new InternalError ();
  }

  public Point getLocationOnScreen () 
  { 
    int point[] = new int[2];
    gtkWidgetGetLocationOnScreen (point);
    return new Point (point[0], point[1]);
  }

  public Dimension getMinimumSize () 
  {
    int dim[]=new int[2];
    gtkWidgetGetDimensions (dim);
    Dimension d = new Dimension (dim[0],dim[1]);
    System.out.println ("min: " + this + d);
    return (d);
  }

  public Dimension getPreferredSize ()
  {
    int dim[]=new int[2];
    gtkWidgetGetDimensions (dim);
    Dimension d = new Dimension (dim[0],dim[1]);
    System.out.println ("pref: " + this + d);
    return (d);
  }

  public Toolkit getToolkit ()
  {
    return Toolkit.getDefaultToolkit();
  }
  
  public void handleEvent (AWTEvent event)
  {
  }
  
  public boolean isFocusTraversable () 
  {
    return true;
  }

  public Dimension minimumSize () 
  {
    return getMinimumSize();
  }

  public void paint (Graphics g)
  {
    awtComponent.paint (g);
  }

  public Dimension preferredSize()
  {
    return getPreferredSize();
  }

  public boolean prepareImage (Image image, int width, int height,
			       ImageObserver observer) 
  {
    GtkImage i = (GtkImage) image;

    if (i.isLoaded ()) return true;

    class PrepareImage extends Thread
    {
      GtkImage image;
      ImageObserver observer;

      PrepareImage (GtkImage image, ImageObserver observer)
      {
	this.image = image;
	this.observer = observer;
      }
      
      public void run ()
      {
	// XXX: need to return data to image observer
	image.source.startProduction (null);
      }
    }

    new PrepareImage (i, observer).start ();
    return false;
  }

  public void print (Graphics g) 
  {
    throw new RuntimeException ();
  }

  public void repaint (long tm, int x, int y, int width, int height)
  {
    q.postEvent (new PaintEvent (awtComponent, PaintEvent.UPDATE,
				 new Rectangle (x, y, width, height)));
  }

  native public void requestFocus ();

  public void reshape (int x, int y, int width, int height) 
  {
    setBounds (x, y, width, height);
  }

  public void setBackground (Color c) 
  {
    System.out.println ("setBackground [UNIMPLEMENTED");
  }

  native public void setNativeBounds (int x, int y, int width, int height);

  public void setBounds (int x, int y, int width, int height)
  {
    Component parent = awtComponent.getParent ();
    
    if (parent instanceof Frame)
      {
	Insets insets = ((Frame)parent).getInsets ();
	/* convert Java's coordinate space into GTK+'s coordinate space */
	setNativeBounds (x-insets.left, y-insets.top, width, height);
      }
    else
      setNativeBounds (x, y, width, height);
  }

  public void setCursor (Cursor cursor) 
  {
    gtkWidgetSetCursor (cursor.getType ());
  }

  public void setEnabled (boolean b)
  {
    set ("sensitive", b);
  }

  public void setFont (Font f)
  {
  }

  public void setForeground (Color c) 
  {
    System.out.println ("setForeground [UNIMPLEMENTED");
  }

  public Color getForeground ()
  {
    int rgb[] = gtkWidgetGetForeground ();
    return new Color (rgb[0], rgb[1], rgb[2]);
  }

  public Color getBackground ()
  {
    int rgb[] = gtkWidgetGetBackground ();
    return new Color (rgb[0], rgb[1], rgb[2]);
  }

  public void setVisible (boolean b)
  {
    set ("visible", b);
  }
  
  public void hide () 
  {
    setVisible (false);
  }

  public void show () 
  {
    setVisible (true);
  }

  protected void postMouseEvent(int id, long when, int mods, int x, int y, 
				int clickCount, boolean popupTrigger) 
  {
    q.postEvent(new MouseEvent(awtComponent, id, when, mods, x, y, 
			       clickCount, popupTrigger));
  }

  protected void postExposeEvent (int x, int y, int width, int height)
  {
    q.postEvent (new PaintEvent (awtComponent, PaintEvent.PAINT,
				 new Rectangle (x, y, width, height)));
  }

  protected void postKeyEvent (int id, long when, int mods, 
			       int keyCode, char keyChar)
  {
    q.postEvent (new KeyEvent (awtComponent, id, when, mods, 
			       keyCode, keyChar));
  }
  
  protected void postFocusEvent (int id, boolean temporary)
  {
    q.postEvent (new FocusEvent (awtComponent, id, temporary));
  }

  protected void postItemEvent (Object item, int stateChange)
  {
    q.postEvent (new ItemEvent ((ItemSelectable)awtComponent, 
				ItemEvent.ITEM_STATE_CHANGED,
				item, stateChange));
  }

  public void getArgs (Component component, GtkArgList args)
  {
    args.add ("visible", component.isVisible ());
    args.add ("sensitive", component.isEnabled ());

    ComponentPeer p;

    do
      {
	component = component.getParent ();
	System.out.println ("COMPONPEER: " + component);
	p = component.getPeer ();
      } while (p instanceof java.awt.peer.LightweightPeer);
    
    args.add ("parent", p);
  }

  native void set (String name, String value);
  native void set (String name, boolean value);
  native void set (String name, int value);
  native void set (String name, float value);
  native void set (String name, Object value);

  void set (GtkArg arg)
  {
    String name = arg.getName ();
    Object value = arg.getValue ();

    if (value instanceof Boolean)
      set (name, ((Boolean)value).booleanValue ());
    else if (value instanceof Integer)
      set (name, ((Integer)value).intValue ());
    else if (value instanceof Float)
      set (name, ((Float)value).floatValue ());
    else if (value instanceof String)
      set (name, ((String) value));
    else
      set (name, value);
  }
}
