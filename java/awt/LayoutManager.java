/*************************************************************************
/* LayoutManager.java -- Layout containers in a Window
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
  * This interface is for laying out containers.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface LayoutManager
{

/**
  * Adds the specified component to the layout group.
  *
  * @param name The name of the component to add.
  * @param component The component to add.
  */
public abstract void
addLayoutComponent(String name, Component component);

/*************************************************************************/

/**
  * Removes the specified component from the layout group.
  *
  * @param component The component to remove.
  */
public abstract void
removeLayoutComponent(Component component);

/*************************************************************************/

/**
  * Calculates the preferred size for this container, taking into account
  * the components in the specified parent container.
  *
  * @param parent The parent container.
  *
  * @return The preferred dimensions of this container.
  */
public abstract Dimension
preferredLayoutSize(Container parent);

/*************************************************************************/

/**
  * Calculates the minimum size for this container, taking into account
  * the components in the specified parent container.
  *
  * @param parent The parent container.
  *
  * @return The minimum dimensions of this container.
  */
public abstract Dimension
minimumLayoutSize(Container parent);

/*************************************************************************/

/**
  * Lays out the components in this container on the specified parent
  * container.
  *
  * @param parent The parent container.
  */
public abstract void
layoutContainer(Container parent);

} // interface LayoutManager

