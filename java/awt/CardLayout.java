/*************************************************************************
/* CardLayout.java -- Layout manager for a card stack
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

import java.util.Enumeration;
import java.util.Hashtable;

/**
  * This class is a layout manager that treats components as cards in
  * a stack.  Only one is visible at a time.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class CardLayout implements LayoutManager2, java.io.Serializable
{

/*
 * Static Variables
 */

// Layout size constants
private static final int PREF = 0;
private static final int MAX = 1;
private static final int MIN = 2;

// Serialization constant
private static final long serialVersionUID = -4328196481005934313L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial Horitzontal gap value.
  */
private int hgap;

/**
  * @serial Vertical gap value.
  */
private int vgap;

/**
  * @serial Table of named components.
  */
private Hashtable tab = new Hashtable();

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>CardLayout</code> with horizontal
  * and vertical gaps of 0.
  */
public
CardLayout()
{
  this(0,0);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>CardLayout</code> with the specified
  * horizontal and vertical gaps.
  *
  * @param hgap The horizontal gap.
  * @param vgap The vertical gap.
  */
public
CardLayout(int hgap, int vgap)
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
  * Adds the specified component to this object's table of components.
  * Doing this allows random access by name.
  *
  * @param component The component to add.
  * @param name The name of the component, which must be a <code>String</code>.
  *
  * @exception IllegalArgumentException If the name object is not a
  * <code>String</code>.
  */
public void
addLayoutComponent(Component component, Object name)
{
  if (!(name instanceof String))
    throw new IllegalArgumentException("Name must a string");

  tab.put(name, component);
}

/*************************************************************************/

/**
  * Adds the specified component to this object's table of components.
  * Doing this allows random access by name.
  *
  * @param name The name of the component.
  * @param component The component to add.
  *
  * @deprecated This method is deprecated in favor of
  * <code>addLayoutComponent(Component, Object)</code>.
  */
public void
addLayoutComponent(String name, Component component)
{
  addLayoutComponent(component, name);
}

/*************************************************************************/

/**
  * Removes the specified component from the table of internal names.
  *
  * @param Component The component to remove.
  */
public void
removeLayoutComponent(Component component)
{
  Enumeration keys = tab.keys();
  while (keys.hasMoreElements())
    {
      String name = (String)keys.nextElement();
      Object obj = tab.get(name);

      if (obj == component)
        {
          tab.remove(name);
          return;
        }
    }
}

/*************************************************************************/

// Internal layout size calculator
private Dimension
layoutSize(Container parent, int type)
{
  int width = 0, height = 0;

  Component[] clist = parent.getComponents();
  if (clist.length > 0)
    for (int i = 0; i < clist.length; i++)
      {
        Component comp = clist[i];
        Dimension dim = null;

        if (type == PREF)
           dim = comp.getPreferredSize();
        else if (type == MAX)
           dim = comp.getMaximumSize();
        else if (type == MIN)
           dim = comp.getMinimumSize();

        if (dim.width > width)
          width = dim.width;
        if (dim.height > height)
          width = dim.height;
      }

  Insets ins = parent.getInsets();

  width += (ins.left + ins.right);
  height += (ins.top + ins.bottom);

  return(new Dimension(width, height));
}

/*************************************************************************/

/**
  * Returns the preferred size of the container for supporting this 
  * layout.
  *
  * @param parent The parent container.
  */
public Dimension
preferredLayoutSize(Container parent)
{
  return(layoutSize(parent, PREF));
}

/*************************************************************************/

/**
  * Returns the minimum size of the container for supporting this 
  * layout.
  *
  * @param parent The parent container.
  */
public Dimension
minimumLayoutSize(Container parent)
{
  return(layoutSize(parent, MIN));
}

/*************************************************************************/

/**
  * Returns the maximum size of the container for supporting this 
  * layout.
  *
  * @param parent The parent container.
  */
public Dimension
maximumLayoutSize(Container parent)
{
  return(layoutSize(parent, MAX));
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
  * Goes to the first card in the container.
  *
  * @param parent The parent container.
  */
public void
first(Container parent)
{
  Component[] clist = parent.getComponents();
  if (clist.length > 0)
    {
      for (int i = 0; i < clist.length; i++)
        clist[i].setVisible(false);

      clist[0].setVisible(true);
    }
}

/*************************************************************************/

/**
  * Goes to the last card in the container.
  *
  * @param parent The parent container.
  */
public void
last(Container parent)
{
  Component[] clist = parent.getComponents();
  if (clist.length > 0)
    {
      for (int i = 0; i < clist.length; i++)
        clist[i].setVisible(false);

      clist[clist.length-1].setVisible(true);
    }
}

/*************************************************************************/

/**
  * Goes to the next card in the container.  If this current card is the
  * last one in the deck, the first component is displayed.
  *
  * @param parent The parent container.
  */
public void
next(Container parent)
{
  Component[] clist = parent.getComponents();
  if (clist.length > 0)
    {
      for (int i = 0; i < clist.length; i++)
        {
          if (clist[i].isVisible())
            {
               clist[i].setVisible(false);

               if ((i + 1) == clist.length)
                 clist[0].setVisible(true);
               else
                 clist[i+1].setVisible(true);

               break;
            }
        }
    }
}

/*************************************************************************/

/**
  * Goes to the next card in the container.  If this current card is the
  * first one in the deck, the last component is displayed.
  *
  * @param parent The parent container.
  */
public void
previous(Container parent)
{
  Component[] clist = parent.getComponents();
  if (clist.length > 0)
    {
      for (int i = 0; i < clist.length; i++)
        {
          if (clist[i].isVisible())
            {
               clist[i].setVisible(false);

               if (i == 0)
                 clist[clist.length-1].setVisible(true);
               else
                 clist[i-1].setVisible(true);

               break;
            }
        }
    }
}

/*************************************************************************/

/**
  * Displays the specified component that was previous added by name
  * using the <code>addLayoutComponent()</code> method.  If the named
  * component doesn't exist, this method returns silently.
  *
  * @param parent The parent container.
  * @param name The name of the component to display.
  */
public void
show(Container parent, String name)
{
  Component comp = (Component)tab.get(name);
  if (comp == null)
    return;

  Component[] clist = parent.getComponents();
  if (clist.length > 0)
    {
      for (int i = 0; i < clist.length; i++)
        {
          if (clist[i] == comp)
            continue;

          if (clist[i].isVisible())
            clist[i].setVisible(false);
        }
    }
}

/*************************************************************************/

/**
  * Lays out the container.  This is done by resizing the child components
  * to be the same size as the parent, less insets and gaps.
  *
  * @param parent The parent container.
  */ 
public void
layoutContainer(Container parent)
{
  Insets ins = parent.getInsets();
  Dimension dim = parent.getSize();
  Component[] clist = parent.getComponents();

  if (clist.length > 0)
    for (int i = 0; i < clist.length; i++)
      {
        int x = ins.left + hgap;
        int y = ins.top + vgap;
        int width = dim.width - (ins.left + ins.right + hgap);
        int height = dim.height - (ins.top + ins.bottom + vgap);

        clist[i].setLocation(x, y);
        clist[i].setSize(width, height);
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

} // class CardLayout 

