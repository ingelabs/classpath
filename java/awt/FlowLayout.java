/* FlowLayout.java -- Left to right flow layout
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of the non-peer AWT libraries of GNU Classpath.

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


package java.awt;

/**
  * This layout manager class lays out its child objects in a left to 
  * right pattern across the available width of the container window.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class FlowLayout implements LayoutManager, java.io.Serializable
{

/*
 * Class Variables
 */

/**
  * Constant indicating that components should be left justified on
  * each line.
  */
public static final int LEFT = 0;

/**
  * Constant indicating that components should be centered within each
  * line.
  */
public static final int CENTER = 1;

/**
  * Constant indicating that components should be right justified on
  * each line.
  */
public static final int RIGHT = 2;

// Serialization constant
private static final long serialVersionUID = -7262534875583282631L;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial The justification alignment of the lines of components, which
  * will be one of the constants defined in this class.
  */
private int align;

/**
  * @serial The horizontal gap between components.
  */
private int hgap;

/**
  * @serial The vertical gap between lines of components.
  */
private int vgap;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>FlowLayout</code> with a center
  * justification and a default horizontal and vertical gap of 5.
  */
public
FlowLayout()
{
  this(CENTER, 5, 5);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>FlowLayout</code> with the specified
  * justification and a default horizontal and vertical gap of 5.
  *
  * @param align The justification setting, which should be one of the
  * contants in this class.
  */
public
FlowLayout(int align)
{
  this(align, 5, 5);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>FlowLayout</code> with the specified
  * justification and gap values
  *
  * @param align The justification setting, which should be one of the
  * contants in this class.
  * @param hgap The horizontal gap between components.
  * @param vgap The vertical gap between lines of components.
  */
public
FlowLayout(int align, int hgap, int vgap)
{
  if ((align != LEFT) && (align != CENTER) && (align != RIGHT))
    throw new IllegalArgumentException("Bad alignment: " + align);

  if ((hgap < 0) || (vgap < 0))
    throw new IllegalArgumentException("Bad gap value");

  this.align = align;
  this.hgap = hgap;
  this.vgap = vgap;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the current justification value for this object.
  *
  * @return The current justification value for this object.
  */
public int
getAlignment()
{
  return(align);
}

/*************************************************************************/

/**
  * Sets the justification value for this object to the specified value.
  *
  * @param align The new justification value for this object, which must
  * be one of the constants in this class.
  */
public void
setAlignment(int align)
{
  if ((align != LEFT) && (align != CENTER) && (align != RIGHT))
    throw new IllegalArgumentException("Bad alignment: " + align);

  this.align = align;
}

/*************************************************************************/

/**
  * Returns the horizontal gap between components.
  *
  * @return The horizontal gap between components.
  */
public int
getHgap()
{
  return(hgap);
}

/*************************************************************************/

/**
  * Sets the horizontal gap between components to the specified value.
  *
  * @param hgap The new horizontal gap between components.
  */
public void
setHgap(int hgap)
{
  if (hgap < 0)
    throw new IllegalArgumentException("Bad hgap value: " + hgap);

  this.hgap = hgap;
}

/*************************************************************************/

/**
  * Returns the vertical gap between lines of components.
  *
  * @return The vertical gap between lines of components.
  */
public int
getVgap()
{
  return(vgap);
}

/*************************************************************************/

/**
  * Sets the vertical gap between lines of components to the specified value.
  *
  * @param vgap The new vertical gap.
  */
public void
setVgap(int vgap)
{
  if (vgap < 0)
    throw new IllegalArgumentException("Bad vgap value: " + vgap);

  this.vgap = vgap;
}

/*************************************************************************/

/**
  * Adds a component to the layout.  This method does nothing in this class.
  */
public void
addLayoutComponent(String name, Component component)
{
}

/*************************************************************************/

/**
  * Removes a component from the layout.  This method does nothing in this class.
  */
public void
removeLayoutComponent(Component component)
{
}

/*************************************************************************/

/**
  * Returns the preferred layout size for the specified container using
  * this layout.
  *
  * @param target The container to return the preferred size for.
  *
  * @return The preferred layout size.
  */
public Dimension
preferredLayoutSize(Container target)
{
  // FIXME: How to calculate?  Do NOT call container since it uses this
  // method to calculate!
  // Assume all one row for now.

  Insets ins = target.getInsets();
  int preferred_width = ins.left + ins.right;
  int preferred_height = ins.top + ins.bottom;
  int max_comp_height = 0;

  Component clist[] = target.getComponents();
  for (int i = 0; i < clist.length; i++)
    {
      Dimension comp_size = clist[i].getPreferredSize();

      preferred_width += comp_size.width;
      if (i != 0)
        preferred_width += hgap;

      if (comp_size.height > max_comp_height)
        max_comp_height = comp_size.height;
    }
  preferred_height += max_comp_height;

  return(new Dimension(preferred_width, preferred_height));
}

/*************************************************************************/

/**
  * Returns the minimum layout size for the specified container using
  * this layout.
  *
  * @param target The container to return the minimum size for.
  *
  * @return The minimum layout size.
  */
public Dimension
minimumLayoutSize(Container target)
{
  // FIXME: How to calculate?  Do NOT call container since it uses this
  // method to calculate!
  // Just call preferred layout for now since this method's layoutContainer()
  // method resizes components to preferred size.
  return(preferredLayoutSize(target));
}

/*************************************************************************/

/**
  * Lays out this specified container using the values in this 
  * object, with components taking their preferred size.
  *
  * @param target The container to layout.
  */
public void
layoutContainer(Container target)
{
  // Ok, I'm not sure what this is really supposed to do, so I just
  // make componets take their preferred size, leave the container its
  // existing size, and lay things out so that the components are forced
  // to fit horizontally, but perhaps not vertically.

  Insets target_insets = target.getInsets();
  Dimension target_size = target.getSize();

  int avail_width = target_size.width - 
                    (target_insets.left + target_insets.right);

  Component clist[] = target.getComponents();
  int h_offset = target_insets.left;
  int v_offset = target_insets.top;
  int max_comp_height = 0;
  int initial_component = 0;
  for (int i = 0; i < clist.length; i++)
    {
      Component comp = clist[i];
      Dimension comp_size = comp.getPreferredSize();

      // Wrap logic
      if (((h_offset + comp_size.width) > 
          (target_size.width - target_insets.right)) && 
          (h_offset != target_insets.left))
        {
          // Reposition for justification - this is gawdawful logic
          int leftover;
          if (align == RIGHT)
            {
              leftover = (target_size.width - target_insets.right) - 
                         h_offset;
            }
          else if (align == CENTER)
            {
              leftover = (target_size.width - target_insets.right) - 
                         h_offset;
              leftover = leftover / 2;
            }
          else
            leftover = 0;

          h_offset = target_insets.left + leftover;
          for (int j = initial_component; j < i; j++)
            {
              Dimension size = clist[i].getPreferredSize();
                 
              clist[i].setSize(size);
              clist[i].setLocation(h_offset, v_offset);
              h_offset += (size.width + hgap);
           }

          h_offset = target_insets.left;
          v_offset += (max_comp_height + vgap);
          initial_component = i;
        }
      
      if (comp_size.height > max_comp_height)
        max_comp_height = comp_size.height;

      h_offset += (comp_size.width + hgap);
    }
}

/*************************************************************************/

/**
  * Returns a string representation of this object.
  *
  * @return A string representation of this object.
  */
public String
toString()
{
  return(getClass().getName() + "(hgap=" + hgap + ",vgap=" + vgap +
         ",align=" + align + ")");
}

} // class FlowLayout 

