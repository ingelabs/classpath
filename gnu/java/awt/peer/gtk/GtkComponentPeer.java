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
import java.awt.image.*;
import java.awt.peer.ComponentPeer;

public class GtkComponentPeer extends GtkGenericPeer
  implements ComponentPeer
{
  native void gtkWidgetSetVisible(boolean b);
  native void gtkWidgetShowChildren();
  native void gtkWidgetGetDimensions(int[] dim);
  native void gtkWidgetGetLocationOnScreen(int[] point);
  native void gtkWidgetSetUsize(int width, int height);

  native void gtkFixedNew (int w, int h);
  native void gtkFixedPut (Object parent, int x, int y);
  native void gtkFixedMove(int x, int y);
    
  public int checkImage (Image image, int width, int height, 
			 ImageObserver observer) 
    {
      return 0;
    }

  public Image createImage (ImageProducer producer) 
    {
      return null;
    }

  public Image createImage (int width, int height)
    {
      return null;
    }

  public void disable () 
    {
      System.out.println("componentpeer: disable");
    }

  public void dispose () 
    {
      System.out.println("componentpeer: dispose");
    }

  public void enable () 
    {
      System.out.println("componentpeer: enable");
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
      System.out.println("componentpeer: getgraphics");
      return null;
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
      System.out.println ("componentpeer: min: " + dim[0] + ", " + dim[1]);
      Dimension d = new Dimension (dim[0],dim[1]);
      return (d);
    }

  public Dimension getPreferredSize ()
    {
      int dim[]=new int[2];
      gtkWidgetGetDimensions (dim);
      Dimension d = new Dimension (dim[0],dim[1]);

      System.out.println ("Cpeer.pref: " +this+":"+ dim[0] + ", " + dim[1]);
      return (d);
    }

  public Toolkit getToolkit ()
    {
      return Toolkit.getDefaultToolkit();
    }
  
  public boolean handleEvent (Event e)
    {
      System.out.println("componentpeer: handleevent");
      return false;
    }
  
  public void handleEvent (AWTEvent e)
    {
      System.out.println("componentpeer: handleawtevent");
    }
  
  public boolean isFocusTraversable () 
    {
      return false;
    }

  public Dimension minimumSize () 
    {
      System.out.println("componentpeer: minimumsize");
      return getMinimumSize();
    }

  public void paint(Graphics g)
    {
      System.out.println("componentpeer: paint");
      gtkWidgetSetVisible (true);
    }

  public Dimension preferredSize()
    {
      System.out.println("Cpeer.preferredsize");
      return getPreferredSize();
    }

  public boolean prepareImage (Image image, int width, int height,
			       ImageObserver observer) 
    {
      return false;
    }

  public void print (Graphics g) 
    {
    }

  native public void repaint (long tm, int x, int y, int width, int height);

  public void requestFocus () 
    {
    }

  public void reshape (int x, int y, int width, int height) 
    {
      setBounds (x, y, width, height);
    }

  public void setBackground (Color c) 
    {
    }

  public void setBounds (int x, int y, int width, int height) 
    {
      System.out.println("Cpeer.setbounds: "+this+": "+x+","+y+","+width+","+height);
      gtkWidgetSetUsize(width,height);
      gtkFixedMove(x,y);
    }

  public void setCursor (Cursor cursor) 
    {
    }

  public void setEnabled (boolean b) 
    {
      System.out.println("componentpeer: setenabled");      
    }

  public void setFont (Font f) 
    {
    }

  public void setForeground (Color c) 
    {
    }

  public void setVisible (boolean b) 
    {
      if (b)
	{
	  System.out.println ("componentpeer: setting visible: "+ this);
	  gtkWidgetSetVisible (b);
	}
      else
	{
	  System.out.println ("componentpeer: setting invisible: " + this);
	  gtkWidgetSetVisible (b);
	}
    }
  
  public void hide () 
    {
      setVisible (false);
    }

  public void show () 
    {
      setVisible (true);
    }
}


