/* MenuComponent.java -- Superclass of all AWT menu components
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

import java.awt.peer.MenuComponentPeer;

// FIXME: Java 1.0 event model unimplemented

/**
  * This is the superclass of all non-menu AWT widgets. 
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class MenuComponent implements java.io.Serializable
{

/*
 * Static Variables
 */

// Serialization Constant
private static final long serialVersionUID = -4536902356223894379L;

/*************************************************************************/

/*
 * Instance Variables
 */

// The font for this component
private Font font;

// The name of the component
private String name;

// The parent of this component
private MenuContainer parent;

// The native peer for this componet
private MenuComponentPeer peer;

// The synchronization locking object for this component
private Object tree_lock = this;

// The toolkit for this object
private Toolkit toolkit = Toolkit.getDefaultToolkit();

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Default constructor for subclasses.
  */
protected
MenuComponent()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the font in use for this component.
  *
  * @return The font for this component.
  */
public Font
getFont()
{
  return(font);
}

/*************************************************************************/

/**
  * Sets the font for this component to the specified font.
  *
  * @param font The new font for this component.
  */
public void
setFont(Font font)
{
  this.font = font;
}

/*************************************************************************/

/**
  * Returns the name of this component.
  *
  * @return The name of this component.
  */
public String
getName()
{
  return(name);
}

/*************************************************************************/

/**
  * Sets the name of this component to the specified name.
  *
  * @param name The new name of this component.
  */
public void
setName(String name)
{
  this.name = name;
}

/*************************************************************************/

/**
  * Returns the parent of this component.
  * 
  * @return The parent of this component.
  */
public MenuContainer
getParent()
{
  return(parent);
} 

/*************************************************************************/

// Sets the parent of this component.
final void
setParent(MenuContainer parent)
{
  this.parent = parent;
}

/*************************************************************************/

/**
  * Returns the native windowing system peer for this component.
  *
  * @return The peer for this component.
  */
public MenuComponentPeer
getPeer()
{
  return(peer);
}

/*************************************************************************/

// Sets the peer for this component.
final void
setPeer(MenuComponentPeer peer)
{
  this.peer = peer;
}

/*************************************************************************/

/**
  * Destroys this component's native peer
  */
public void
removeNotify()
{
  if (peer != null)
    peer.dispose();
  peer = null;
}

/*************************************************************************/

/**
  * Returns the toolkit in use for this component.
  *
  * @return The toolkit for this component.
  */
final Toolkit
getToolkit()
{
  return(toolkit);
}

/*************************************************************************/

/**
  * Returns the object used for synchronization locks on this component
  * when performing tree and layout functions.
  *
  * @return The synchronization lock for this component.
  */
public final Object
getTreeLock()
{
  return(tree_lock);
}

/*************************************************************************/

// The sync lock object for this component.
final void
setTreeLock(Object tree_lock)
{
  this.tree_lock = tree_lock;
}

/*************************************************************************/

/**
  * AWT 1.0 event dispatcher.
  *
  * @deprecated Deprecated in favor of <code>dispatchEvent()</code>.

public boolean
postEvent(Event event)
{
  return(true);
}
  */
/*************************************************************************/

/**
  * Sends this event to this component or a subcomponent for processing.
  *
  * @param event The event to dispatch
  */
public final void
dispatchEvent(AWTEvent event)
{
  // FIXME: What is this really supposed to do?
  ((MenuComponent)event.getSource()).processEvent(event);
}

/*************************************************************************/

/**
  * Processes the specified event.  In this class, this method simply
  * calls one of the more specific event handlers.
  * 
  * @param event The event to process.
  */
protected void
processEvent(AWTEvent event)
{
  return;
}

/*************************************************************************/

/**
  * Returns a string representation of this component.
  *
  * @return A string representation of this component
  */
public String
toString()
{
  return(getClass().getName() + "(" + getName() + ")");
}

/*************************************************************************/

/**
  * Returns a debugging string for this component
  */
protected String
paramString()
{
  return(toString());
}

} // class Component

