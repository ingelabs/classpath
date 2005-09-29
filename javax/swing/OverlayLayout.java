/* OverlayLayout.java --
   Copyright (C) 2002, 2004 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

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

package javax.swing;

import java.awt.AWTError;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.io.Serializable;

/**
 * OverlayLayout
 * @author	Andrew Selkirk
 * @version	1.0
 */
public class OverlayLayout implements LayoutManager2, Serializable
{
  private static final long serialVersionUID = 18082829169631543L;

  /**
   * target
   */
  private Container target;

  /**
   * xChildren
   */
  private SizeRequirements[] xChildren;

  /**
   * yChildren
   */
  private SizeRequirements[] yChildren;

  /**
   * xTotal
   */
  private SizeRequirements xTotal;

  /**
   * yTotal
   */
  private SizeRequirements yTotal;

  /**
   * The offsets of the child components in the X direction.
   */
  private int[] offsetsX;

  /**
   * The offsets of the child components in the Y direction.
   */
  private int[] offsetsY;

  /**
   * The spans of the child components in the X direction.
   */
  private int[] spansX;

  /**
   * The spans of the child components in the Y direction.
   */
  private int[] spansY;

  /**
   * Constructor OverlayLayout
   * @param target TODO
   */
  public OverlayLayout(Container target)
  {
    this.target = target;
  }

  /**
   * invalidateLayout
   * @param target TODO
   */
  public void invalidateLayout(Container target)
  {
    xChildren = null;
    yChildren = null;
    xTotal = null;
    yTotal = null;
    offsetsX = null;
    offsetsY = null;
    spansX = null;
    spansY = null;
  }

  /**
   * addLayoutComponent
   * @param string TODO
   * @param component TODO
   */
  public void addLayoutComponent(String string, Component component)
  {
    // Nothing to do here.
  }

  /**
   * addLayoutComponent
   * @param component TODO
   * @param constraints TODO
   */
  public void addLayoutComponent(Component component, Object constraints)
  {
    // Nothing to do here.
  }

  /**
   * removeLayoutComponent
   * @param component TODO
   */
  public void removeLayoutComponent(Component component)
  {
    // Nothing to do here.
  }

  /**
   * preferredLayoutSize
   * @param target TODO
   * @returns Dimension
   */
  public Dimension preferredLayoutSize(Container target)
  {
    if (target != this.target)
      throw new AWTError("OverlayLayout can't be shared");

    checkTotalRequirements();
    return new Dimension(xTotal.preferred, yTotal.preferred);
  }

  /**
   * minimumLayoutSize
   * @param target TODO
   * @returns Dimension
   */
  public Dimension minimumLayoutSize(Container target)
  {
    if (target != this.target)
      throw new AWTError("OverlayLayout can't be shared");

    checkTotalRequirements();
    return new Dimension(xTotal.minimum, yTotal.minimum);
  }

  /**
   * maximumLayoutSize
   * @param target TODO
   * @returns Dimension
   */
  public Dimension maximumLayoutSize(Container target)
  {
    if (target != this.target)
      throw new AWTError("OverlayLayout can't be shared");

    checkTotalRequirements();
    return new Dimension(xTotal.maximum, yTotal.maximum);
  }

  /**
   * getLayoutAlignmentX
   * @param target TODO
   * @returns float
   */
  public float getLayoutAlignmentX(Container target)
  {
    if (target != this.target)
      throw new AWTError("OverlayLayout can't be shared");

    checkTotalRequirements();
    return xTotal.alignment;
  }

  /**
   * getLayoutAlignmentY
   * @param target TODO
   * @returns float
   */
  public float getLayoutAlignmentY(Container target)
  {
    if (target != this.target)
      throw new AWTError("OverlayLayout can't be shared");

    checkTotalRequirements();
    return yTotal.alignment;
  }

  /**
   * layoutContainer
   * @param target TODO
   */
  public void layoutContainer(Container target)
  {
    if (target != this.target)
      throw new AWTError("OverlayLayout can't be shared");

    checkLayout();
    Component[] children = target.getComponents();
    for (int i = 0; i < children.length; i++)
      children[i].setBounds(offsetsX[i], offsetsY[i], spansX[i], spansY[i]);
  }

  /**
   * Makes sure that the xChildren and yChildren fields are correctly set up.
   * A call to {@link #invalidateLayout(Container)} sets these fields to null,
   * so they have to be set up again.
   */
  private void checkRequirements()
  {
    if (xChildren == null || yChildren == null)
      {
        Component[] children = target.getComponents();
        xChildren = new SizeRequirements[children.length];
        yChildren = new SizeRequirements[children.length];
        for (int i = 0; i < children.length; i++)
          {
            if (! children[i].isVisible())
              {
                xChildren[i] = new SizeRequirements();
                yChildren[i] = new SizeRequirements();
              }
            else
              {
                xChildren[i] =
                  new SizeRequirements(children[i].getMinimumSize().width,
                                       children[i].getPreferredSize().width,
                                       children[i].getMaximumSize().width,
                                       children[i].getAlignmentX());
                yChildren[i] =
                  new SizeRequirements(children[i].getMinimumSize().height,
                                       children[i].getPreferredSize().height,
                                       children[i].getMaximumSize().height,
                                       children[i].getAlignmentY());
              }
          }
      }
  }

  /**
   * Makes sure that the xTotal and yTotal fields are set up correctly. A call
   * to {@link #invalidateLayout} sets these fields to null and they have to be
   * recomputed.
   */
  private void checkTotalRequirements()
  {
    if (xTotal == null || yTotal == null)
      {
        checkRequirements();
        xTotal = SizeRequirements.getAlignedSizeRequirements(xChildren);
        yTotal = SizeRequirements.getAlignedSizeRequirements(yChildren);
      }
  }

  /**
   * Makes sure that the offsetsX, offsetsY, spansX and spansY fields are set
   * up correctly. A call to {@link #invalidateLayout} sets these fields
   * to null and they have to be recomputed.
   */
  private void checkLayout()
  {
    if (offsetsX == null || offsetsY == null || spansX == null
        || spansY == null)
      {
        checkRequirements();
        checkTotalRequirements();
        int len = target.getComponents().length;
        offsetsX = new int[len];
        offsetsY = new int[len];
        spansX = new int[len];
        spansY = new int[len];
        SizeRequirements.calculateAlignedPositions(target.getWidth(), xTotal,
                                                   xChildren, offsetsX, spansX);
        SizeRequirements.calculateAlignedPositions(target.getHeight(), yTotal,
                                                   yChildren, offsetsY, spansY);
      }
  }
}
