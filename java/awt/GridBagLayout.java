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
    comptable.remove (component);
  }

  public Dimension preferredLayoutSize (Container parent)
  {
    if (layoutInfo == null)
      layoutContainer (parent);
    
    throw new Error ("Not implemented");
  }

  public Dimension minimumLayoutSize (Container parent)
  {
    if (layoutInfo == null)
      layoutContainer (parent);
    
    throw new Error ("Not implemented");
  }

  public Dimension maximumLayoutSize (Container target)
  {
    throw new Error ("Not implemented");
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
    return (GridBagConstraints) comptable.get (component);
  }

  /**
   * @since 1.1
   */
  public int[][] getLayoutDimensions ()
  {
    throw new Error ("Not implemented");
  }

  /**
   * @since 1.1
   */
  public Point getLayoutOrigin ()
  {
    throw new Error ("Not implemented");
  }

  public double[][] getLayoutWeights ()
  {
    throw new Error ("Not implemented");
  }

  /**
   * @since 1.1
   */
  public Point location (int x, int y)
  {
    throw new Error ("Not implemented");
  }

  /**
   * @since 1.4
   */
  protected GridBagLayoutInfo getLayoutInfo (Container parent, int sizeflag)
  {
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
    throw new Error ("Not implemented");
  }

  protected Dimension GetMinSize (Container parent, GridBagLayoutInfo info)
  {
    throw new Error ("Not implemented");
  }
}
