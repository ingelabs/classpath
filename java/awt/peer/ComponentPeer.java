/* ComponentPeer.java -- Toplevel component peer
   Copyright (C) 1999 Free Software Foundation, Inc.

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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.awt.peer;

import java.awt.*;
import java.awt.image.*;

public interface ComponentPeer
{

public abstract int checkImage(Image img, int width, int height, 
                               ImageObserver ob);
public abstract Image createImage(ImageProducer prod);
public abstract Image createImage(int width, int height);
public abstract void disable();
public abstract void dispose();
public abstract void enable();
public abstract ColorModel getColorModel();
public abstract FontMetrics getFontMetrics(Font f);
public abstract Graphics getGraphics();
public abstract Point getLocationOnScreen();
public abstract Dimension getMinimumSize();
public abstract Dimension getPreferredSize();
public abstract Toolkit getToolkit();
public abstract void handleEvent(AWTEvent e);
public abstract void hide();
public abstract boolean isFocusTraversable();
public abstract Dimension minimumSize();
public abstract Dimension preferredSize();
public abstract void paint(Graphics graphics);
public abstract boolean prepareImage(Image img, int width, int height,
                                     ImageObserver ob);
public abstract void print(Graphics graphics);
public abstract void repaint(long tm, int x, int y, int width, int height);
public abstract void requestFocus();
public abstract void reshape(int x, int y, int width, int height);
public abstract void setBackground(Color color);
public abstract void setBounds(int x, int y, int width, int height);
public abstract void setCursor(Cursor cursor);
public abstract void setEnabled(boolean enabled);
public abstract void setFont(Font font);
public abstract void setForeground(Color color);
public abstract void setVisible(boolean visible);
public abstract void show();

} // interface ComponentPeer

