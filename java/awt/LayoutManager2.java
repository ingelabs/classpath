/* LayoutManager2.java -- Enhanced layout manager.
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
  * Layout manager for laying out containers based on contraints.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface LayoutManager2 extends LayoutManager
{

/**
  * Adds the specified component to the layout, with the specified
  * constraint object.
  *
  * @param component The component to add.
  * @param constraint The constraint object.
  */
public abstract void
addLayoutComponent(Component component, Object contraint);

/*************************************************************************/

/**
  * Determines the minimum size of the specified target container.
  *
  * @param target The target container.
  */
public abstract Dimension
maximumLayoutSize(Container target);

/*************************************************************************/

/**
  * Returns the preferred X axis alignment for the specified target
  * container.  This value will range from 0 to 1 where 0 is alignment 
  * closest to the origin, 0.5 is centered, and 1 is aligned furthest 
  * from the origin.
  *
  * @param target The target container.
  */
public abstract float
getLayoutAlignmentX(Container target);

/*************************************************************************/

/**
  * Returns the preferred Y axis alignment for the specified target
  * container.  This value will range from 0 to 1 where 0 is alignment 
  * closest to the origin, 0.5 is centered, and 1 is aligned furthest 
  * from the origin.
  *
  * @param target The target container.
  */
public abstract float
getLayoutAlignmentY(Container target);

/*************************************************************************/

/**
  * Forces the layout manager to purge any cached information about
  * the layout of the target container.  This will force it to be
  * recalculated.
  *
  * @param target The target container.
  */
public abstract void
invalidateLayout(Container target);

} // interface LayoutManager2 

