/*************************************************************************
/* WindowAdapter.java -- Convenience class for writing window listeners.
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
  * This is a convenience class for writing window listeners.  It implements
  * the <code>WindowListener</code> interface with empty method bodies.  This
  * allows a subclass to override only those methods of interest.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class WindowAdapter implements WindowListener
{

/*
 * Constructors
 */

/**
  * Do nothing default constructor for subclasses.
  */
public
WindowAdapter()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Implements this method from the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
windowActivated(WindowEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method from the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
windowDeactivated(WindowEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method from the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
windowOpened(WindowEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method from the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
windowClosing(WindowEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method from the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
windowClosed(WindowEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method from the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
windowIconified(WindowEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method from the interface with an empty method body.
  *
  * @param event Ignored.
  */
public void
windowDeiconified(WindowEvent event)
{
}

} // class WindowAdapter 

