/*************************************************************************
/* Container.java -- Component that holds other components
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

import java.awt.event.ContainerListener;
import java.awt.event.ContainerEvent;
import java.awt.peer.ComponentPeer;
import java.awt.peer.LightweightPeer;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
  * This is the superclass of all AWT components that can hold other
  * components.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class Container extends Component 
                implements java.io.Serializable
{

// FIXME: AWT 1.0 event processing not implemented
// FIXME: Not even close on serialization

/*
 * Instance Variables
 */

// The list of components in this container
private Vector components = new Vector();

// Determines whether or not the peer has been created for this container
private boolean is_notified;

// The list of container listeners for this object
private ContainerListener container_listeners;

// The layout manager for this container
private LayoutManager layout_manager;

// The insets of this container
private Insets insets = new Insets(0, 0, 0, 0);

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Default constructor for subclasses.
  */
protected
Container()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Adds the specified component to this container at the end of the
  * component list.
  *
  * @param component The component to add to the container.
  *
  * @return The same component that was added.
  */
public Component
add(Component component)
{
  addImpl(component, null, -1); 
  return(component);
}

/*************************************************************************/

/**
  * Adds the specified component to the container at the end of the
  * component list.  This method should not be used. Instead, use
  * <code>add(Component, Object</code>.
  *
  * @param name FIXME
  * @param component The component to be added.
  *
  * @return The same component that was added.
  */
public Component
add(String name, Component component)
{
  // FIXME: What is the "name" for? Is it the constraint object?
  addImpl(component, name, -1);
  return(component);
}

/*************************************************************************/

/**
  * Adds the specified component to this container at the specified index
  * in the component list.
  *
  * @param component The component to be added.
  * @param index The index in the component list to insert this child
  * at, or -1 to add at the end of the list.
  *
  * @return The same component that was added.
  *
  * @param throws ArrayIndexOutOfBounds If the specified index is invalid.
  */
public Component
add(Component component, int index)
{
  addImpl(component, null, index);
  return(component);
}

/*************************************************************************/

/**
  * Adds the specified component to this container at the end of the
  * component list.  The layout manager will use the specified constraints
  * when laying out this component.
  *
  * @param component The component to be added to this container.
  * @param constraints The layout constraints for this component.
  */
public void
add(Component component, Object constraints)
{
  addImpl(component, constraints, -1);  
}

/*************************************************************************/

/**
  * Adds the specified component to this container at the specified index
  * in the component list.  The layout manager will use the specified
  * constraints when layout out this component.
  *
  * @param component The component to be added.
  * @param constraints The layout constraints for this component.
  * @param index The index in the component list to insert this child
  * at, or -1 to add at the end of the list.
  *
  * @param throws ArrayIndexOutOfBounds If the specified index is invalid.
  */
public void
add(Component component, Object constraints, int index)
{
  addImpl(component, constraints, index);
}

/*************************************************************************/

/**
  * This method is called by all the <code>add()</code> methods to perform
  * the actual adding of the component.  Subclasses who wish to perform
  * their own processing when a component is added should override this
  * method.  Any subclass doing this must call the superclass version of
  * this method in order to ensure proper functioning of the container.
  *
  * @param component The component to be added.
  * @param constraints The layout constraints for this component, or
  * <code>null</code> if there are no constraints.
  * @param index The index in the component list to insert this child
  * at, or -1 to add at the end of the list.
  *
  * @param throws ArrayIndexOutOfBounds If the specified index is invalid.
  */
protected void
addImpl(Component component, Object constraints, int index)
{
  if (index == -1)
    components.addElement(component);
  else
    components.insertElementAt(component, index);

  // Create peer if necessary
  if (is_notified)
    component.addNotify();

  // Add to layout manager if it exists.
  if (layout_manager != null)
    {
       if (layout_manager instanceof LayoutManager2)
         ((LayoutManager2)layout_manager).addLayoutComponent(component, 
                                                             constraints);
       else if (constraints instanceof String)
         layout_manager.addLayoutComponent((String)constraints, component);
       else
         layout_manager.addLayoutComponent(null, component);
    }

  // Invalidate this container so that it is re-laid out
  invalidate();

  // Post event to notify of adding the container.
  getToolkit().getSystemEventQueue().postEvent(new 
          ContainerEvent(this, ContainerEvent.COMPONENT_ADDED, component));
}

/*************************************************************************/

/**
  * Removes the specified component from this container.
  *
  * @return component The component to remove from this container.
  */
public void
remove(Component component)
{
  components.removeElement(component);

  // Destroy peer if necessary
  if (is_notified)
    component.removeNotify();

  // Remove from the layout manager if it exists.
  if (layout_manager != null)
    layout_manager.removeLayoutComponent(component);
    
  // Invalidate this container so that it is re-laid out
  invalidate();

  // Post event to notify of adding the container.
  getToolkit().getSystemEventQueue().postEvent(new 
          ContainerEvent(this, ContainerEvent.COMPONENT_REMOVED, component));
}

/*************************************************************************/

/**
  * Removes the component at the specified index from this container.
  *
  * @param index The index of the component to remove.
  */
public void
remove(int index)
{
  remove((Component)components.elementAt(index));
}

/*************************************************************************/

/**
  * Removes all components from this container.
  */
public void
removeAll()
{
  // Remove one at a time in order to generate the proper events.

  Enumeration e = components.elements();
  while (e.hasMoreElements())
    remove((Component)e.nextElement());
}

/*************************************************************************/

/**
  * Returns the number of components in this container.
  *
  * @return The number of components in this container.
  */
public int
getComponentCount()
{
  return(components.size());
}

/*************************************************************************/

/**
  * Returns the number of components in this container.
  *
  * @return The number of components in this container.
  *
  * @deprecated This method is deprecated in favor of 
  * <code>getComponentCount()</code>.
  */
public int
countComponents()
{
  return(getComponentCount());
}

/*************************************************************************/

/**
  * Returns the component at the specified index.
  *
  * @param index The index of the component to retrieve.
  *
  * @return The requested component.
  *
  * @exception ArrayIndexOutOfBoundsException If the specified index is not
  * valid.
  */
public Component
getComponent(int index)
{
  return((Component)components.elementAt(index));
}

/*************************************************************************/

/**
  * Returns an array of the components in this container.
  *
  * @return The components in this container.
  */
public Component[]
getComponents()
{
  Component[] retval = new Component[getComponentCount()];
  components.copyInto(retval);
  return(retval);
}

/*************************************************************************/

/**
  * Returns the component located at the specified point.  This is done
  * by checking whether or not a child component claims to contain this
  * point.  The first child component that does is returned.  If no
  * child component claims the point, the container itself is returned,
  * unless the point does not exist within this container, in which
  * case <code>null</code> is returned.
  *
  * @param x The X coordinate of the point.
  * @param y The Y coordinate of the point.
  *
  * @return The component containing the specified point, or <code>null</code>
  * if there is no such point.
  */
public Component
getComponentAt(int x, int y)
{
  Enumeration e = components.elements();
  while (e.hasMoreElements())
    {
      Component c = (Component)e.nextElement();
      if (c.contains(x, y))
        return(c);
    }

  if (contains(x, y))
    return(this);
  else
    return(null);
}

/*************************************************************************/

/**
  * Returns the component located at the specified point.  This is done
  * by checking whether or not a child component claims to contain this
  * point.  The first child component that does is returned.  If no
  * child component claims the point, the container itself is returned,
  * unless the point does not exist within this container, in which
  * case <code>null</code> is returned.
  *
  * @param point The point to return the component at.
  *
  * @return The component containing the specified point, or <code>null</code>
  * if there is no such point.
  */
public Component
getComponetAt(Point point)
{
  return(getComponentAt(point.x, point.y));
}

/*************************************************************************/

/**
  * Returns the component located at the specified point.  This is done
  * by checking whether or not a child component claims to contain this
  * point.  The first child component that does is returned.  If no
  * child component claims the point, the container itself is returned,
  * unless the point does not exist within this container, in which
  * case <code>null</code> is returned.
  *
  * @param point The point to return the component at.
  *
  * @return The component containing the specified point, or <code>null</code>
  * if there is no such point.
  *
  * @deprecated This method is deprecated in favor of
  * <code>getComponentAt(int, int)</code>.
  */
public Component
locate(int x, int y)
{
  return(getComponentAt(x, y));
}

/*************************************************************************/

/**
  * Called when this container is added to another container to inform it
  * to create its peer.  Peers for any child components will also be
  * created.
  */
public void
addNotify()
{
  if (is_notified)
    return;

  is_notified = true;
  Enumeration e = components.elements();
  while (e.hasMoreElements())
    ((Component)e.nextElement()).addNotify();
  super.addNotify();
}

/*************************************************************************/

/**
  * Called when this container is removed from its parent container to
  * inform it to destroy its peer.  This causes the peers of all child
  * component to be destroyed as well.
  */
public void
removeNotify()
{
  if (!is_notified)
    return;

  is_notified = false;
  Enumeration e = components.elements();
  while (e.hasMoreElements())
    ((Component)e.nextElement()).removeNotify();
  super.removeNotify(); 
}

/*************************************************************************/

/**
  * Adds the specified container listener to this object's list of
  * container listeners.
  *
  * @param listener The listener to add.
  */
public synchronized void
addContainerListener(ContainerListener listener)
{
  container_listeners = AWTEventMulticaster.add(container_listeners, listener);
}

/*************************************************************************/

/**
  * Removes the specified container listener from this object's list of
  * container listeners.
  *
  * @param listener The listener to remove.
  */
public synchronized void
removeContainerListener(ContainerListener listener)
{
  container_listeners = AWTEventMulticaster.remove(container_listeners,
                                                   listener);
}

/*************************************************************************/

/**
  * Called when a container event occurs if container events are enabled.
  * This method calls any registered listeners.
  *
  * @param event The event that occurred.
  */
protected void
processContainerEvent(ContainerEvent event)
{
  if (container_listeners != null)
    switch(event.getID())
      {
        case ContainerEvent.COMPONENT_ADDED:
          container_listeners.componentAdded(event);
          break;

        case ContainerEvent.COMPONENT_REMOVED:
          container_listeners.componentRemoved(event);
          break;

        default:
          break;
      }
}

/*************************************************************************/

/**
  * Processes the specified event.  This method calls
  * <code>processContainerEvent()</code> if this method is a
  * <code>ContainerEvent</code>, otherwise it calls the superclass
  * method.
  *
  * @param event The event to be processed.
  */
protected void
processEvent(AWTEvent event)
{
  if (event instanceof ContainerEvent)
    processContainerEvent((ContainerEvent)event);
  else
    super.processEvent(event);
}

/*************************************************************************/

/**
  * AWT 1.0 event processor.
  *
  * @param event The event that occurred.
  *
  * @deprecated This method is deprecated in favor of 
  * <code>dispatchEvent()</code>.
  */
public void
deliverEvent(Event event)
{
}

/*************************************************************************/

/**
  * Returns the current layout manager for this container.
  *
  * @return The layout manager for this container.
  */
public LayoutManager
getLayout()
{
  return(layout_manager);
}

/*************************************************************************/

/**
  * Sets the layout manager for this container to the specified layout
  * manager.
  *
  * @param layout_manager The new layout manager for this container.
  */
public void
setLayout(LayoutManager layout_manager)
{
  this.layout_manager = layout_manager;
}

/*************************************************************************/

/**
  * Returns the minimum size of this container.
  *
  * @return The minimum size of this container.
  */
public Dimension
getMinimumSize()
{
  if (layout_manager != null)
    return(layout_manager.minimumLayoutSize(this));
  else
    return(super.getMinimumSize());
}

/*************************************************************************/

/**
  * Returns the minimum size of this container.
  *
  * @return The minimum size of this container.
  * 
  * @deprecated This method is deprecated in favor of 
  * <code>getMinimumSize()</code>.
  */
public Dimension
minimumSize()
{
  return(getMinimumSize());
}

/*************************************************************************/

/**
  * Returns the preferred size of this container.
  *
  * @return The preferred size of this container.
  */
public Dimension
getPreferredSize()
{
  if (layout_manager != null)
    return(layout_manager.preferredLayoutSize(this));
  else
    return(super.getPreferredSize());
}

/*************************************************************************/

/**
  * Returns the preferred size of this container.
  *
  * @return The preferred size of this container.
  * 
  * @deprecated This method is deprecated in favor of 
  * <code>getPreferredSize()</code>.
  */
public Dimension
preferredSize()
{
  return(getPreferredSize());
}

/*************************************************************************/

/**
  * Returns the maximum size of this container.
  *
  * @return The maximum size of this container.
  */
public Dimension
getMaximumSize()
{
  if (layout_manager != null)
    {
      if (layout_manager instanceof LayoutManager2)
        return(((LayoutManager2)layout_manager).maximumLayoutSize(this));
    }

  return(super.getMaximumSize());
}

/*************************************************************************/

/**
  * Returns the preferred alignment along the X axis.  This is a value
  * between 0 and 1 where 0 represents alignment flush left and
  * 1 means alignment flush right, and 0.5 means centered.
  *
  * @return The preferred alignment along the X axis.
  */
public float
getAlignmentX()
{
  if (layout_manager != null)
    {
      if (layout_manager instanceof LayoutManager2)
        return(((LayoutManager2)layout_manager).getLayoutAlignmentX(this));
    }

  return(super.getAlignmentX());
}

/*************************************************************************/

/**
  * Returns the preferred alignment along the Y axis.  This is a value
  * between 0 and 1 where 0 represents alignment flush top and
  * 1 means alignment flush bottom, and 0.5 means centered.
  *
  * @return The preferred alignment along the Y axis.
  */
public float
getAlignmentY()
{
  if (layout_manager != null)
    {
      if (layout_manager instanceof LayoutManager2)
        return(((LayoutManager2)layout_manager).getLayoutAlignmentY(this));
    }

  return(super.getAlignmentY());
}

/*************************************************************************/

/**
  * Returns the insets for this container, which is the space used for
  * borders, the margin, etc.
  *
  * @return The insets for this container.
  */
public Insets
getInsets()
{
  return((Insets)insets.clone());
}

/*************************************************************************/

/**
  * Returns the insets for this container, which is the space used for
  * borders, the margin, etc.
  *
  * @return The insets for this container.
  *
  * @deprecated This method is deprecated in favor of <code>getInsets()</code>.
  */
public Insets
insets()
{
  return(getInsets());
}

/*************************************************************************/

/**
  * Sets the cursor to the specified cursor.
  *
  * @param cursor The new cursor for this container.
  */
public synchronized void
setCursor(Cursor cursor)
{
  super.setCursor(cursor);
}

/*************************************************************************/

/**
  * Layout the components in this container.
  */
public void
doLayout()
{
  if (layout_manager != null)
    layout_manager.layoutContainer(this);
}

/*************************************************************************/

/**
  * Layout the components in this container.
  *
  * @deprecated This method is deprecated in favor of <code>doLayout()</code>.
  */
public void
layout()
{
  doLayout();
}

/*************************************************************************/

/**
  * Invalidates this container to indicate that it (and all parent
  * containers) need to be laid out.
  */
public void
invalidate()
{
  super.invalidate();
}

/*************************************************************************/

/**
  * Re-lays out the components in this container.
  */
public void
validate()
{
  synchronized(this)
    {
      validateTree();
    }
  super.validate();
}

/*************************************************************************/

/**
  * Recursively validates the container tree, recomputing any invalid
  * layouts.
  */
protected void
validateTree()
{
  if (!isValid())
    doLayout();

  Enumeration e = components.elements();
  while (e.hasMoreElements())
    {
      Component c = (Component)e.nextElement();
      if (!c.isValid())
        c.validate(); 
    }
}

/*************************************************************************/

/**
  * Tests whether or not the specified component is contained within
  * this components subtree.
  *
  * @param component The component to test.
  *
  * @return <code>true</code> if this container is an ancestor of the
  * specified component, <code>false</code>.
  */
public boolean
isAncestorOf(Component component)
{
  if (components.contains(component))
    return(true);  

  Enumeration e = components.elements();
  while (e.hasMoreElements())
    {
      Component c = (Component)e.nextElement();
      if (c instanceof Container)
        if (((Container)c).isAncestorOf(component))
          return(true);
    }

  return(false);
}

/*************************************************************************/

/**
  * Paints this container.  The implementation of this method in this
  * class forwards to any lightweight components in this container.  If
  * this method is subclassed, this method should still be invoked as
  * a superclass method so that lightweight components are properly
  * drawn.
  *
  * @param graphics The graphics context for this paint job.
  */
public void
paint(Graphics graphics)
{
  Enumeration e = components.elements();
  while (e.hasMoreElements())
    {
      Component c = (Component)e.nextElement();
      ComponentPeer p = getPeer();
      if (p instanceof LightweightPeer)
        c.paint(graphics);
    }
}

/*************************************************************************/

/**
  * Updates this container.  The implementation of this method in this
  * class forwards to any lightweight components in this container.  If
  * this method is subclassed, this method should still be invoked as
  * a superclass method so that lightweight components are properly
  * drawn.
  *
  * @param graphics The graphics context for this update.
  */
public void
update(Graphics graphics)
{
  Enumeration e = components.elements();
  while (e.hasMoreElements())
    {
      Component c = (Component)e.nextElement();
      ComponentPeer p = getPeer();
      if (p instanceof LightweightPeer)
        c.update(graphics);
    }
}

/*************************************************************************/

/**
  * Prints this container.  The implementation of this method in this
  * class forwards to any lightweight components in this container.  If
  * this method is subclassed, this method should still be invoked as
  * a superclass method so that lightweight components are properly
  * drawn.
  *
  * @param graphics The graphics context for this print job.
  */
public void
print(Graphics graphics)
{
  Enumeration e = components.elements();
  while (e.hasMoreElements())
    {
      Component c = (Component)e.nextElement();
      ComponentPeer p = getPeer();
      if (p instanceof LightweightPeer)
        c.print(graphics);
    }
}

/*************************************************************************/

/**
  * Paints all of the components in this container.
  *
  * @param graphics The graphics context for this paint job.
  */
public void
paintComponents(Graphics graphics)
{
  Enumeration e = components.elements();
  while (e.hasMoreElements())
    ((Component)e.nextElement()).paint(graphics);
}
  
/*************************************************************************/

/**
  * Prints all of the components in this container.
  *
  * @param graphics The graphics context for this print job.
  */
public void
printComponents(Graphics graphics)
{
  Enumeration e = components.elements();
  while (e.hasMoreElements())
    ((Component)e.nextElement()).print(graphics);
}
 
/*************************************************************************/

/**
  * Returns a string representing the state of this container for
  * debugging purposes.
  *
  * @return A string representing the state of this container.
  */
protected String
paramString()
{
  return(getClass().getName());
}

/*************************************************************************/

/**
  * Writes a listing of this container to the specified stream starting
  * at the specified indentation point.
  *
  * @param stream The <code>PrintStream</code> to write to.
  * @param indent The indentation point.
  */
public void
list(PrintStream stream, int indent)
{
  list(new PrintWriter(stream), indent);
}

/*************************************************************************/

/**
  * Writes a listing of this container to the specified stream starting
  * at the specified indentation point.
  *
  * @param stream The <code>PrintWriter</code> to write to.
  * @param indent The indentation point.
  */
public void
list(PrintWriter stream, int indent)
{
  // FIXME: Implement
  stream.println("haven't done this one yet!");
}

} // class Container

