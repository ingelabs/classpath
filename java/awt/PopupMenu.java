/*************************************************************************
/* PopupMenu.java -- An AWT popup menu
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

import java.awt.peer.PopupMenuPeer;
import java.awt.peer.MenuPeer;
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuComponentPeer;
/**
  * This class implement an AWT popup menu widget
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class PopupMenu extends Menu implements java.io.Serializable
{

/*
 * Static Variables
 */

// Serialization Constant
private static final long serialVersionUID = -4620452533522760060L;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>PopupMenu</code>.
  */
public
PopupMenu()
{
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>PopupMenu</code> with the specified
  * label.
  *
  * @param label The label for this popup menu.
  */
public
PopupMenu(String label)
{
  super(label);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Creates this object's native peer.
  */
public void
addNotify()
{
  if (getPeer() != null)
    return;

   setPeer((MenuComponentPeer)getToolkit().createPopupMenu(this));
}

/*************************************************************************/

/**
  * Displays this popup menu at the specified coordinates relative to
  * the specified component.
  *
  * @param component The component to which the display coordinates are relative.
  * @param x The X coordinate of the menu.
  * @param y The Y coordinate of the menu.
  */
public void
show(Component component, int x, int y)
{
  PopupMenuPeer pmp = (PopupMenuPeer)getPeer();
  if (pmp != null)
    {
      Event e = new Event (component, Event.ACTION_EVENT, component);
      e.x = x;
      e.y = y;
      pmp.show (e);
    }
}

} // class PopupMenu

