/* GridLayout.java -- Layout components in columns and rows
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


package java.awt;

/**
  * This class layouts out components in a grid patterns.  Either the
  * number of columns or the number of rows may be specified.  If both
  * are specified, then the number of columns is ignored and is derived
  * from the number of rows and the total number of components.  (For
  * example, three rows and twelve components would be laid out in a
  * 3x4 grid).
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class GridLayout implements LayoutManager, java.io.Serializable
{

/*
 * Instance Variables
 */

/**
  * @serial The number of columns in the grid.
  */
private int cols;

/**
  * @serial The number of rows in the grid.
  */
private int rows;

/**
  * @serial The horizontal gap between columns
  */
private int hgap;

/**
  * @serial The vertical gap between rows
  */
private int vgap;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>GridLayout</code> with one
  * row and a horizontal gap of zero.
  */
public
GridLayout()
{
  this(1, 0, 0, 0);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>GridLayout</code> with the specified
  * row and column settings.  The horizontal and vertical gaps will be
  * set to zero.  Note that the row and column settings cannot both be
  * zero.  If both the row and column values are non-zero, the rows value
  * takes precedence.
  *
  * @param rows The number of rows in the grid.
  * @param cols The number of columns in the grid.
  *
  * @exception IllegalArgumentException If the row or column values are
  * not valid.
  */
public
GridLayout(int rows, int cols) throws IllegalArgumentException
{
  this(rows, cols, 0, 0);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>GridLayout</code> with the specified
  * row, column, horizontal gap and vertical gap settings.  
  * Note that the row and column settings cannot both be
  * zero.  If both the row and column values are non-zero, the rows value
  * takes precedence.
  *
  * @param rows The number of rows in the grid.
  * @param cols The number of columns in the grid.
  * @param hgap The horizontal gap between columns.
  * @param vgap The vertical gap between rows.
  *
  * @exception IllegalArgumentException If the row or column values are
  * not valid.
  */
public
GridLayout(int rows, int cols, int hgap, int vgap) 
           throws IllegalArgumentException
{
  if ((rows < 0) ||
      (cols < 0) ||
      (hgap < 0) ||
      (vgap < 0) ||
      ((rows == 0) && (cols == 0)))
    throw new IllegalArgumentException("Bad parameters");

  this.rows = rows;
  this.cols = cols;
  this.hgap = hgap;
  this.vgap = vgap;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * Returns the number of rows in the grid.
  *
  * @return The number of rows in the grid.
  */
public int
getRows()
{
  return(rows);
}

/*************************************************************************/

/**
  * Sets the number of rows in the grid to the specified value.
  *
  * @param rows The new number of rows in the grid.
  *
  * @exception IllegalArgumentException If both the row and column setttings
  * would be set to zero, or the specified value of the rows setting is
  * less than zero.
  */
public void
setRows(int rows)
{
  if ((rows < 0) ||
      ((rows == 0) && (cols == 0)))
    throw new IllegalArgumentException("Bad rows value");

  this.rows = rows;
}

/*************************************************************************/

/**
  * Returns the number of columns in the grid.
  *
  * @return The number of columns in the grid.
  */
public int
getColumns()
{
  return(cols);
}

/*************************************************************************/

/**
  * Sets the number of columns in the grid to the specified value.
  *
  * @param cols The new number of columns in the grid.
  *
  * @exception IllegalArgumentException If both the row and column setttings
  * would be set to zero, or the specified value of the columns setting is
  * less than zero.
  */
public void
setColumns(int cols) throws IllegalArgumentException
{
  if ((cols < 0) ||
      ((cols == 0) && (rows == 0)))
    throw new IllegalArgumentException("Bad columns value");

  this.cols = cols;
}

/*************************************************************************/

/**
  * Returns the horiztonal gap between columns.
  *
  * @return The horizontal gap between columns.
  */
public int
getHgap()
{
  return(hgap);
}

/*************************************************************************/

/**
  * Sets the horiztonal gap between columns to the specified value.
  *
  * @param hgap The new horizontal gap value.
  *
  * @exception IllegalArgumentException If the hgap value is less than zero.
  */
public void
setHgap(int hgap) throws IllegalArgumentException
{
  if (hgap < 0) 
    throw new IllegalArgumentException("hgap is negative: " + hgap);

  this.hgap = hgap;
}

/*************************************************************************/

/**
  * Returns the vertical gap between rows.
  *
  * @return The vertical gap between rows.
  */
public int
getVgap()
{
  return(vgap);
}

/*************************************************************************/

/**
  * Sets the vertical gap between rows to the specified value.
  *
  * @param vgap The new vertical gap value.
  *
  * @exception IllegalArgumentException If the vgap value is less than zero.
  */
public void
setVgap(int vgap) throws IllegalArgumentException
{
  if (vgap < 0)
    throw new IllegalArgumentException("vgap is negative: " + vgap);

  this.vgap = vgap;
}

/*************************************************************************/

/**
  * Adds the specified component to this layout with the given name.
  *
  * @param name The name of the component to add.
  * @param component The component to add.
  */
public void
addLayoutComponent(String name, Component component)
{
  // Not needed in this class.
}

/*************************************************************************/

/**
  * Removes the specified component from this layout.
  *
  * @param component The component to remove.
  */
public void
removeLayoutComponent(Component component)
{
  // Not needed in this class.
}

/*************************************************************************/

private Dimension
calcLayoutSize(Container parent, boolean minimum)
{
  int row_height = 0, col_width = 0, nrows, ncols;
 
  Component[] clist = parent.getComponents();
  if (clist.length > 0)
    for(int i = 0; i < clist.length; i++)
      {
         Dimension dim;
         if (minimum)
           dim = clist[i].getMinimumSize();
         else
          dim = clist[i].getPreferredSize();

         if (dim.width > col_width)
           col_width = dim.width;

         if (dim.height > row_height)
           row_height = dim.height;
      }

  int cnum = parent.getComponentCount();

  if (rows > 0)
    {
      nrows = rows;
      ncols = cnum / nrows;
      if ((cnum % nrows) != 0)
        ++ncols;
    }
  else
    {
      ncols = cols;
      nrows = cnum / ncols;
      if ((cnum % ncols) != 0)
        ++nrows;
    }

  Insets ins = parent.getInsets();

  int width = (ncols * col_width) + ((ncols + 1) * hgap) + ins.left +
              ins.right;
  int height = (nrows * row_height) + ((nrows + 1) * vgap) + ins.top +
               ins.bottom;

  return(new Dimension(width, height));
}

/*************************************************************************/

/**
  * Calculates the minimum size of this layout for the specified container.
  * This will calculate using the minimum size of the components in the
  * container, the horizontal and vertical padding of this layout, and
  * the container inset values.
  *
  * @param parent The container to calculate the minimum layout size for.
  */
public Dimension
minimumLayoutSize(Container parent)
{
  return(calcLayoutSize(parent, true));
}

/*************************************************************************/

/**
  * Calculates the preferred size of this layout for the specified container.
  * This will calculate using the preferred size of the components in the
  * container, the horizontal and vertical padding of this layout, and
  * the container inset values.
  *
  * @param parent The container to calculate the preferred layout size for.
  */
public Dimension
preferredLayoutSize(Container parent)
{
  return(calcLayoutSize(parent, false));
}

/*************************************************************************/

/**
  * Lays out the specified container using the constraints in this object.
  * The free space in the container is divided evenly into the specified
  * number of rows and columns in this object.
  *
  * @param parent The container to layout.
  */
public void
layoutContainer(Container parent)
{
  Insets ins = parent.getInsets();
  Dimension psize = parent.getSize();
  int cnum = parent.getComponentCount();
  if (cnum == 0)
    return;

  int nrows, ncols;
  if (rows > 0)
    {
      if (cnum < rows)
        nrows = cnum;
      else
        nrows = rows;

      ncols = cnum / nrows;
      if ((cnum % nrows) != 0)
        ++ncols;
    }
  else
    {
      if (cnum < cols)
        ncols = cnum;
      else
        ncols = cols;

      nrows = cnum / ncols;
      if ((cnum % ncols) != 0)
        ++nrows;
    }

  int freew = psize.width - (ins.left + ins.right + ((ncols + 1) * hgap));
  int freeh = psize.height - (ins.top + ins.bottom + ((nrows + 1) * vgap));
  if ((freew < 0) || (freeh < 0))
    return; // Give up

  int col_width = freew / ncols;
  int row_height = freew / nrows;

  Component[] clist = parent.getComponents();
  int cur_row = 1;
  int cur_col = 1;
  for (int i = 0; i < clist.length; i++)
    {
      // FIXME: What do we do about components that would be smaller
      // than their minimum size.
      int new_width, new_height, new_x, new_y;
     
      Dimension maxsize = clist[i].getMaximumSize();
      if (maxsize.width > col_width)
        new_width = col_width;
      else
        new_width = maxsize.width;

      if (maxsize.height > row_height)
        new_height = row_height;
      else
        new_height = maxsize.height; 

      new_x = ins.left + (cur_col * col_width) + ((cur_col + 1) * hgap);
      new_y = ins.top + (cur_row * row_height) + ((cur_row + 1) * vgap);

      clist[i].setLocation(new_x, new_y);
      clist[i].setSize(new_width, new_height);

      ++cur_col;
      if (cur_col == ncols)
        {
          ++cur_row;
          cur_col = 1;
        }
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
  return(getClass().getName() + "(rows=" + getRows() + ",cols=" + getColumns() +
         ",hgap=" + getHgap() + ",vgap=" + getVgap() + ")");
}

} // class GridLayout

