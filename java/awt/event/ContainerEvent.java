/*************************************************************************
/* ContainerEvent.java -- Components added/removed from a container
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

import java.awt.Component;
import java.awt.Container;

/**
  * This event is generated when a component is added or removed from
  * a container.  Applications do not ordinarily need to received these
  * events since the AWT system handles them internally.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ContainerEvent extends ComponentEvent
             implements java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the first id in the id range used by this class.
  */
public static final int CONTAINER_FIRST = 300;

/**
  * This is the last id in the id range used by this class.
  */
public static final int CONTAINER_LAST = 301;

/**
  * This id indicates a component was added to the container.
  */
public static final int COMPONENT_ADDED = 300;

/**
  * This id indicates a component was removed from the container.
  */
public static final int COMPONENT_REMOVED = 301;

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial This is the child component that was added or removed
  */
private Component child;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>ContainerEvent</code> with the
  * specified source and id.  Additionally, the affected child component
  * is also passed as a parameter.
  *
  * @param source The source container of the event.
  * @param id The event id.
  * @param child The child component affected by this event.
  */
public
ContainerEvent(Component source, int id, Component child)
{
  super(source, id);
  this.child = child;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the child object that was added or removed from
  * the container.
  *
  * @return The child object added or removed.
  */
public Component
getChild()
{
  return(child);
}

/*************************************************************************/

/**
  * Returns the source of this event as a <code>Container</code>.
  *
  * @return The source of the event.
  */
public Container
getContainer()
{
  return((Container)getSource());
}

/*************************************************************************/

/**
  * This method returns a string identifying this event.
  *
  * @return A string identifying this event.
  */
public String
paramString()
{
  return(getClass().getName() + " source=" + getSource() + " id=" + getID() +
         " child=" + getChild());
}

} // class ContainerEvent 

