/* GridBagLayout - Layout manager for components according to GridBagConstraints
   Copyright (C) 2002, 2003 Free Software Foundation, Inc.

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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package java.awt;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * @author Michael Koch <konqueror@gmx.de>
 */
public class GridBagLayout
  implements Serializable, LayoutManager2
{
  private static final long serialVersionUID = 8838754796412211005L;

  protected static final int MINSIZE = 1;
  protected static final int PREFERREDSIZE = 2;
  protected static final int MAXGRIDSIZE = 512;

  protected Hashtable comptable;
  protected GridBagLayoutInfo layoutInfo;
  protected GridBagConstraints defaultConstraints = new GridBagConstraints();

  public double[] columnWeights;
  public int[] columnWidths;
  public double[] rowWeights;
  public int[] rowHeights;

  public GridBagLayout ()
  {
    // Do nothing here.
  }

  public void addLayoutComponent (String name, Component component)
  {
    // do nothing here.
  }

  public void addLayoutComponent (Component component, Object constraints)
  {
    if (!(constraints instanceof GridBagConstraints))
      throw new IllegalArgumentException();

    setConstraints (component, (GridBagConstraints) constraints);
  }

  public void removeLayoutComponent (Component component)
  {
    // do nothing here
  }

  public Dimension preferredLayoutSize (Container parent)
  {
    if (parent == null)
      return new Dimension (0, 0);
    
    GridBagLayoutInfo li = getLayoutInfo (parent, PREFERREDSIZE);
    return getMinSize (parent, li);
  }

  public Dimension minimumLayoutSize (Container parent)
  {
    if (parent == null)
      return new Dimension (0, 0);
    
    GridBagLayoutInfo li = getLayoutInfo (parent, MINSIZE);
    return getMinSize (parent, li);
  }

  public Dimension maximumLayoutSize (Container target)
  {
    return new Dimension (Integer.MAX_VALUE, Integer.MAX_VALUE);
  }

  public void layoutContainer (Container parent)
  {
    arrangeGrid (parent);
  }

  public float getLayoutAlignmentX (Container target)
  {
    return Component.CENTER_ALIGNMENT;
  }

  public float getLayoutAlignmentY (Container target)
  {
    return Component.CENTER_ALIGNMENT;
  }

  public void invalidateLayout (Container target)
  {
    this.layoutInfo = null;
  }

  /**
   * @since 1.4
   */
  protected void adjustForGravity (GridBagConstraints gbc, Rectangle rect)
  {
    throw new Error ("Not implemented");
  }

  protected void AdjustForGravity (GridBagConstraints gbc, Rectangle rect)
  {
    adjustForGravity (gbc, rect);
  }

  /**
   * @since 1.4
   */
  protected void arrangeGrid (Container parent)
  {
    throw new Error ("Not implemented");
  }

  protected void ArrangeGrid (Container parent)
  {
    arrangeGrid (parent);
  }

  public void setConstraints (Component component,
			      GridBagConstraints constraints)
  {
    comptable.put (component, constraints);
  }

  public GridBagConstraints getConstraints (Component component)
  {
    GridBagConstraints constraints = lookupConstraints (component);

    if (constraints == null)
      return null;

    return (GridBagConstraints) constraints.clone();
  }

  protected GridBagConstraints lookupConstraints (Component component)
  {
    GridBagConstraints result = (GridBagConstraints) comptable.get (component);

    if (result == null)
      {
	setConstraints (component, defaultConstraints);
	result = (GridBagConstraints) comptable.get (component);
      }
    
    return result;
  }

  /**
   * @since 1.1
   */
  public Point getLayoutOrigin ()
  {
    if (layoutInfo == null)
      return new Point (0, 0);
    
    return new Point (layoutInfo.x, layoutInfo.y);
  }

  /**
   * @since 1.1
   */
  public int[][] getLayoutDimensions ()
  {
    if (layoutInfo == null)
      return new int [2][];

    int[][] result = new int [2][];
    result [0] = new int [layoutInfo.cols];
    System.arraycopy (layoutInfo.colWidths, 0, result [0], 0, layoutInfo.cols);
    result [1] = new int [layoutInfo.rows];
    System.arraycopy (layoutInfo.rowHeights, 0, result [1], 0, layoutInfo.rows);
    return result;
  }

  public double[][] getLayoutWeights ()
  {
    if (layoutInfo == null)
      return new double [2][];
      
    double[][] result = new double [2][];
    result [0] = new double [layoutInfo.cols];
    System.arraycopy (layoutInfo.colWeights, 0, result [0], 0, layoutInfo.cols);
    result [1] = new double [layoutInfo.rows];
    System.arraycopy (layoutInfo.rowWeights, 0, result [1], 0, layoutInfo.rows);
    return result;
  }

  /**
   * @since 1.1
   */
  public Point location (int x, int y)
  {
    if (layoutInfo == null)
      return new Point (0, 0);

    int col;
    int row;
    int pixel_x = layoutInfo.x;
    int pixel_y = layoutInfo.y;

    for (col = 0; col < layoutInfo.cols; col++)
      {
        if (pixel_x < x)
	  break;

	pixel_x += layoutInfo.colWidths [col];
      }

    for (row = 0; row < layoutInfo.rows; row++)
      {
	if (pixel_y < y)
	  break;

	pixel_y += layoutInfo.rowHeights [row];
      }

    return new Point (col, row);
  }

  /**
   * @since 1.4
   */
  protected GridBagLayoutInfo getLayoutInfo (Container parent, int sizeflag)
  {
    if (sizeflag != MINSIZE && sizeflag != PREFERREDSIZE)
      throw new IllegalArgumentException();

    throw new Error ("Not implemented");
  }

  protected GridBagLayoutInfo GetLayoutInfo (Container parent, int sizeflag)
  {
    return getLayoutInfo (parent, sizeflag);
  }

  /**
   * @since 1.4
   */
  protected Dimension getMinSize (Container parent, GridBagLayoutInfo info)
  {
    if (parent == null || info == null)
      return new Dimension (0, 0);

    int width = 0;
    int height = 0;

    for (int i = 0; i < info.cols; i++)
      width += info.colWidths [i];

    for (int i = 0; i < info.rows; i++)
      height += info.rowHeights [i];
  
    Insets insets = parent.getInsets();
    width += insets.left + insets.right;
    height += insets.top + insets.bottom;

    return new Dimension (width, height);
  }

  protected Dimension GetMinSize (Container parent, GridBagLayoutInfo info)
  {
    return getMinSize (parent, info);
  }
}
