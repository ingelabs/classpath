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
  native void GtkWidgetShow();
  native void GtkWidgetShowChildren();
  native void GtkWidgetGetDimensions(int[] dim);
  native void GtkWidgetSetUsize(int width, int height);

  native void GtkFixedNew (int w, int h);
  native void GtkFixedPut (Object parent, int x, int y);
  native void GtkFixedMove(int x, int y);
    
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
      System.out.println("componentpeer: getlocationonscreen");
      return null;
    }

  public Dimension getMinimumSize () 
    {
      int dim[]=new int[2];
      GtkWidgetGetDimensions (dim);
      System.out.println ("componentpeer: min: " + dim[0] + ", " + dim[1]);
      Dimension d = new Dimension (dim[0],dim[1]);
      return (d);
    }

  public Dimension getPreferredSize ()
    {
      int dim[]=new int[2];
      GtkWidgetGetDimensions (dim);
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
  
  public void hide () 
    {
      System.out.println("componentpeer: hide");
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
      GtkWidgetShow();
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

  public void repaint (long tm, int x, int y, int width, int height) 
    {
      System.out.println("componentpeer: repaint");
    }

  public void requestFocus () 
    {
    }

  public void reshape (int x, int y, int width, int height) 
    {
      System.out.println("componentpeer: reshape");
    }

  public void setBackground (Color c) 
    {
    }

  public void setBounds (int x, int y, int width, int height) 
    {
      System.out.println("Cpeer.setbounds: "+this+": "+x+","+y+","+width+","+height);
      GtkWidgetSetUsize(width,height);
      GtkFixedMove(x,y);
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
      if(b)
	{
	  System.out.println("componentpeer: setting visible: "+ this);
	  GtkWidgetShow();
	}
    }
  
  public void show () 
    {
      setVisible(true);
    }
}


