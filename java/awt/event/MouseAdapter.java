/*************************************************************************
/* MouseAdapter.java -- Convenience class for writing mouse listeners
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

package java.awt.event;

/**
  * This is a convenience class for writing mouse listeners.  It implements
  * the <code>MouseListener</code> interface with empty method bodies.  This
  * allows a subclass to override just those methods of interest.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class MouseAdapter implements MouseListener
{

/*
 * Constructors
 */

/**
  * Do nothing default constructor for subclasses.
  */
public 
MouseAdapter()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Implements this method in the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
mouseClicked(MouseEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method in the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
mouseEntered(MouseEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method in the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
mouseExited(MouseEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method in the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
mousePressed(MouseEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method in the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
mouseReleased(MouseEvent event)
{
}

} // class MouseAdapter

