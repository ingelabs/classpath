/*
 * GtkComponentPeer.java -- Implements ComponentPeer with GTK
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

  protected GtkComponentPeer (Component awtComponent)
  {
    super (awtComponent);
    this.awtComponent = awtComponent;
  }

  public int checkImage (Image image, int width, int height, 
			 ImageObserver observer) 
  {
    return 0;
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
    System.out.println("componentpeer: getcolormodel");
    return null;
  }

  public FontMetrics getFontMetrics (Font f)
  {
    System.out.println("componentpeer: getfontmetrics");
    return null;
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
    System.out.println ("min: " + this + d);
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
    return false;
  }

  public void print (Graphics g) 
  {
    throw new RuntimeException();
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

  native public void setEnabled (boolean b);

  public void setFont (Font f)
  {
  }

  public void setForeground (Color c) 
  {
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

  native public void setVisible (boolean b);
  
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
}
