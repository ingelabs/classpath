/*************************************************************************
/* BorderLayout.java -- A layout manager class
/*
/* Copyright (c) 1999 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.awt;

/**
  * This class implements a layout manager that positions components
  * in certain sectors of the parent container.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class BorderLayout implements LayoutManager2, java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * Constant indicating the top of the container
  */
public static final String NORTH = "North";

/**
  * Constant indicating the bottom of the container
  */
public static final String SOUTH = "South";

/**
  * Constant indicating the right side of the container
  */
public static final String EAST = "East";

/**
  * Constant indicating the left side of the container
  */
public static final String WEST = "West";

/**
  * Constant indicating the center of the container
  */
public static final String CENTER = "Center";

// Serialization constant
private static final long serialVersionUID = -8658291919501921765L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial
  */
private Component north;

/**
  * @serial
  */
private Component south;

/**
  * @serial
  */
private Component east;

/**
  * @serial
  */
private Component west;

/**
  * @serial
  */
private Component center;

/**
  * @serial The horizontal gap between components
  */
private int hgap;

/**
  * @serial The vertical gap between components
  */
private int vgap;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>BorderLayout</code> with no
  * horiztonal or vertical gaps between components.
  */
public
BorderLayout()
{
  this(0,0);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>BorderLayout</code> with the
  * specified horiztonal and vertical gaps between components.
  *
  * @param hgap The horizontal gap between components.
  * @param vgap The vertical gap between components.
  */
public
BorderLayout(int hgap, int vgap)
{
  this.hgap = hgap;
  this.vgap = vgap;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * Returns the horitzontal gap value.
  *
  * @return The horitzontal gap value.
  */
public int
getHgap()
{
  return(hgap);
}

/*************************************************************************/

/**
  * Sets the horizontal gap to the specified value.
  *
  * @param hgap The new horizontal gap.
  */
public void
setHgap(int hgap)
{
  this.hgap = hgap;
}

/*************************************************************************/

/**
  * Returns the vertical gap value.
  *
  * @return The vertical gap value.
  */
public int
getVgap()
{
  return(vgap);
}

/*************************************************************************/

/**
  * Sets the vertical gap to the specified value.
  *
  * @param vgap The new vertical gap value.
  */
public void
setVgap(int vgap)
{
  this.vgap = vgap;
}

/*************************************************************************/

/**
  * Adds a component to the layout in the specified constraint position, 
  * which must be one of the string constants defined in this class.
  *
  * @param component The component to add.
  * @param constraints The constraint string.
  *
  * @exception IllegalArgumentException If the constraint object is not
  * a string, or is not one of the specified constants in this class.
  */
public void
addLayoutComponent(Component component, Object constraints)
{
  if (!(constraints instanceof String))
    throw new IllegalArgumentException("Constraint must be a string");

  String str = (String)constraints;

  if (constraints.equals(NORTH))
    north = component;
  else if (constraints.equals(SOUTH))
    south = component;
  else if (constraints.equals(EAST))
    east = component;
  else if (constraints.equals(WEST))
    west = component;
  else if (constraints.equals(CENTER))
    center = component;
  else
    throw new IllegalArgumentException("Constraint value not valid: " + str);
}

/*************************************************************************/

/**
  * Adds a component to the layout in the specified constraint position, 
  * which must be one of the string constants defined in this class.
  *
  * @param constraints The constraint string.
  * @param component The component to add.
  *
  * @exception IllegalArgumentException If the constraint object is not
  * one of the specified constants in this class.
  *
  * @deprecated This method is deprecated in favor of
  * <code>addLayoutComponent(Component, Object)</code>.
  */
public void
addLayoutComponent(String constraints, Component component)
{
  addLayoutComponent(component, constraints);
}

/*************************************************************************/

/**
  * Removes the specified component from the layout.
  *
  * @param component The component to remove from the layout.
  */
public void
removeLayoutComponent(Component component)
{
  if (north == component)
    north = null;
  if (south == component)
    south = null;
  if (east == component)
    east = null;
  if (west == component)
    west = null;
  if (center == component)
    center = null;
}

/*************************************************************************/

/**
  * Returns the minimum size of the specified container using this layout.
  *
  * @param target The container to calculate the minimum size for.
  *
  * @return The minimum size of the container
  */
public Dimension 
minimumLayoutSize(Container target)
{
  Insets ins = target.getInsets();

  Dimension ndim = new Dimension(0,0);
  Dimension sdim = new Dimension(0,0);
  Dimension edim = new Dimension(0,0);
  Dimension wdim = new Dimension(0,0);
  Dimension cdim = new Dimension(0,0);

  if (north != null)
    ndim = north.getMinimumSize();
  if (south != null)
    sdim = south.getMinimumSize();
  if (east != null)
    edim = east.getMinimumSize();
  if (west != null)
    wdim = west.getMinimumSize();
  if (center != null)
    cdim = center.getMinimumSize();

  int width = edim.width + cdim.width + wdim.width + (hgap * 2);
  if (ndim.width > width)
    width = ndim.width;
  if (sdim.width > width)
    width = sdim.width;

  width += (ins.left + ins.right);

  int height = edim.height;
  if (cdim.height > height)
    height = cdim.height;
  if (wdim.height > height)
    height = wdim.height;

  height += (ndim.height + sdim.height + (vgap * 2) + ins.top + ins.bottom);

  return(new Dimension(width, height));
}

/*************************************************************************/

/**
  * Returns the preferred size of the specified container using this layout.
  *
  * @param target The container to calculate the preferred size for.
  *
  * @return The preferred size of the container
  */
public Dimension 
preferredLayoutSize(Container target)
{
  Insets ins = target.getInsets();

  Dimension ndim = new Dimension(0,0);
  Dimension sdim = new Dimension(0,0);
  Dimension edim = new Dimension(0,0);
  Dimension wdim = new Dimension(0,0);
  Dimension cdim = new Dimension(0,0);

  if (north != null)
    ndim = north.getPreferredSize();
  if (south != null)
    sdim = south.getPreferredSize();
  if (east != null)
    edim = east.getPreferredSize();
  if (west != null)
    wdim = west.getPreferredSize();
  if (center != null)
    cdim = center.getPreferredSize();

  int width = edim.width + cdim.width + wdim.width + (hgap * 2);
  if (ndim.width > width)
    width = ndim.width;
  if (sdim.width > width)
    width = sdim.width;

  width += (ins.left + ins.right);

  int height = edim.height;
  if (cdim.height > height)
    height = cdim.height;
  if (wdim.height > height)
    height = wdim.height;

  height += (ndim.height + sdim.height + (vgap * 2) + ins.top + ins.bottom);

  return(new Dimension(width, height));
}

/*************************************************************************/

/**
  * Returns the maximum size of the specified container using this layout.
  *
  * @param target The container to calculate the maximum size for.
  *
  * @return The maximum size of the container
  */
public Dimension 
maximumLayoutSize(Container target)
{
  Insets ins = target.getInsets();

  Dimension ndim = new Dimension(0,0);
  Dimension sdim = new Dimension(0,0);
  Dimension edim = new Dimension(0,0);
  Dimension wdim = new Dimension(0,0);
  Dimension cdim = new Dimension(0,0);

  if (north != null)
    ndim = north.getMaximumSize();
  if (south != null)
    sdim = south.getMaximumSize();
  if (east != null)
    edim = east.getMaximumSize();
  if (west != null)
    wdim = west.getMaximumSize();
  if (center != null)
    cdim = center.getMaximumSize();

  int width = edim.width + cdim.width + wdim.width + (hgap * 2);
  if (ndim.width > width)
    width = ndim.width;
  if (sdim.width > width)
    width = sdim.width;

  width += (ins.left + ins.right);

  int height = edim.height;
  if (cdim.height > height)
    height = cdim.height;
  if (wdim.height > height)
    height = wdim.height;

  height += (ndim.height + sdim.height + (vgap * 2) + ins.top + ins.bottom);

  return(new Dimension(width, height));
}

/*************************************************************************/

/**
  * Returns the X axis alignment, which is a <code>float</code> indicating
  * where along the X axis this container wishs to position its layout.
  * 0 indicates align to the left, 1 indicates align to the right, and 0.5
  * indicates align to the center.
  *
  * @param parent The parent container.
  *
  * @return The X alignment value.
  */
public float
getLayoutAlignmentX(Container parent)
{
  return(parent.getAlignmentX());
}

/*************************************************************************/

/**
  * Returns the Y axis alignment, which is a <code>float</code> indicating
  * where along the Y axis this container wishs to position its layout.
  * 0 indicates align to the top, 1 indicates align to the bottom, and 0.5
  * indicates align to the center.
  *
  * @param parent The parent container.
  *
  * @return The Y alignment value.
  */
public float
getLayoutAlignmentY(Container parent)
{
  return(parent.getAlignmentY());
}

/*************************************************************************/

/**
  * Instructs this object to discard any layout information it might
  * have cached.
  *
  * @param parent The parent container.
  */
public void
invalidateLayout(Container parent)
{
}

/*************************************************************************/

/**
  * Lays out the specified container according to the constraints
  * in this object.
  *
  * @param target The container to lay out.
  */
public void
layoutContainer(Container target)
{
  Insets ins = target.getInsets();
  Dimension tdim = target.getSize();

  Dimension ndim = new Dimension(0,0);
  Dimension sdim = new Dimension(0,0);
  Dimension edim = new Dimension(0,0);
  Dimension wdim = new Dimension(0,0);
  Dimension cdim = new Dimension(0,0);

  if (north != null)
    ndim = north.getPreferredSize();
  if (south != null)
    sdim = south.getPreferredSize();
  if (east != null)
    edim = east.getPreferredSize();
  if (west != null)
    wdim = west.getPreferredSize();
  if (center != null)
    cdim = center.getPreferredSize();

  if (north != null)
    {
      north.setLocation(ins.left, ins.top);
      north.setSize(tdim.width - (ins.left + ins.right), ndim.height);
    }

  // Blech! - This will bomb if height goes negative
  int maxh = tdim.height - (ins.top + ins.bottom + ndim.height +
                            sdim.height + (vgap * 2));

  if (west != null)
    {
      west.setLocation(ins.left, ins.top + ndim.height + vgap);
      west.setSize(wdim.width, maxh);
    }

  // Blech! - This will bomb if width goes negative
  int cwidth = tdim.width - (ins.left + ins.right + wdim.width +
                             edim.width + (vgap * 2));

  if (center != null)
    {
      center.setLocation(ins.left + wdim.width + hgap, 
                         ins.top + ndim.height + vgap);
      center.setSize(cwidth, maxh);
    }

  if (east != null)
    {
      east.setLocation(ins.left + wdim.width + cwidth + (hgap * 2),
                       ins.top + ndim.height + vgap);
      east.setSize(edim.width, maxh);
    }

  if (south != null)
    {
      south.setLocation(ins.left, ins.top + ndim.height + maxh + (vgap * 2));
      south.setSize(tdim.width - (ins.left + ins.right), sdim.height);                        
    }
}

/*************************************************************************/

/**
  * Returns a string representation of this layout manager.
  *
  * @return A string representation of this object.
  */
public String
toString()
{
  return(getClass().getName());
}

} // class BorderLayout 

