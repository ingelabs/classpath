/*************************************************************************
/* FocusAdapter.java -- Convenience class for writing focus listeners.
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
  * This is a convenience class for writing focus listeners.  It implements
  * the <code>FocusListener</code> interface with empty method bodies.  A
  * subclass simply overrides those methods for the events it is interested
  * in.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class FocusAdapter implements FocusListener
{

/*
 * Constructors
 */

/**
  * Do nothing default constructor for subclasses.
  */
public
FocusAdapter()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Implements this method from the interface with an empty body.
  *
  * @param event Ignored.
  */
public void
focusGained(FocusEvent event)
{
}

/*************************************************************************/

/**
  * Implements this method from the interface with an empty body.
  *
  * @param event Ignored.
  */
public void
focusLost(FocusEvent event)
{
}

} // class FocusAdapter

