/*************************************************************************
/* AWTEventMulticaster.java -- Event handler chaining class
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

import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.EventObject;
import java.util.EventListener;

/**
  * This class is used to implement a chain of event handlers.  Dispatching
  * using this class is thread safe.  Here is a quick example of how to
  * add and delete listeners using this class.  For this example, we will
  * assume are firing <code>AdjustableEvent</code>'s.  However, this 
  * same approach is useful for all events in the <code>java.awt.event</code>
  * package, and more if this class is subclassed.
  * <p>
  * <code> 
  * AdjustmentListener al;
  * 
  * public void 
  * addAdjustmentListener(AdjustmentListener listener)
  * {
  *   al = AWTEventMulticaster.add(al, listener);
  * }
  *
  * public void
  * removeAdjustmentListener(AdjustmentListener listener)
  * {
  *   al = AWTEventMulticaster.remove(al, listener);
  * }
  * </code>
  * <p>
  * When it come time to process an event, simply call <code>al</code>,
  * assuming it is not <code>null</code>.
  * <p>
  * The first time <code>add</code> is called it is passed
  * <code>null</code> and <code>listener</code> as its arguments.  This
  * starts building the chain.  This class returns <code>listener</code>
  * which becomes the new <code>al</code>.  The next time, <code>add</code>
  * is called with <code>al</code> and <code>listener</code> and the
  * new listener is then chained to the old.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class AWTEventMulticaster implements ComponentListener,
       ContainerListener, FocusListener, KeyListener, MouseListener,
       MouseMotionListener, WindowListener, ActionListener, ItemListener,
       AdjustmentListener, TextListener //, InputMethodListener
{

/*
 * Instance Variables
 */

/**
  * A variable in the event chain.
  */
protected final EventListener a;

/**
  * A variable in the event chain
  */
protected final EventListener b;

/*************************************************************************/

/*
 * Static Methods
 */

/**
  * Chain <code>ComponentListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static ComponentListener
add(ComponentListener a, ComponentListener b)
{
  return((ComponentListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>ContainerListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static ContainerListener
add(ContainerListener a, ContainerListener b)
{
  return((ContainerListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>FocusListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static FocusListener
add(FocusListener a, FocusListener b)
{
  return((FocusListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>KeyListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static KeyListener
add(KeyListener a, KeyListener b)
{
  return((KeyListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>MouseListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static MouseListener
add(MouseListener a, MouseListener b)
{
  return((MouseListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>MouseMotionListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static MouseMotionListener
add(MouseMotionListener a, MouseMotionListener b)
{
  return((MouseMotionListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>WindowListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static WindowListener
add(WindowListener a, WindowListener b)
{
  return((WindowListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>ActionListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static ActionListener
add(ActionListener a, ActionListener b)
{
  return((ActionListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>ItemListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static ItemListener
add(ItemListener a, ItemListener b)
{
  return((ItemListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>AdjustmentListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static AdjustmentListener
add(AdjustmentListener a, AdjustmentListener b)
{
  return((AdjustmentListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>AdjustmentListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static TextListener
add(TextListener a, TextListener b)
{
  return((TextListener)addInternal(a, b));
}

/*************************************************************************/

/**
  * Chain <code>EventListener</code> b to a.
  *
  * @param a - Listener to chain to.
  * @param b - Listener to chain.
  *
  * @return Latest entry in the chain.
  */
public static EventListener
addInternal(EventListener a, EventListener b)
{
  if (a == null)
    return(b);
  if (b == null)
    return(a);
  
  return(new AWTEventMulticaster(a, b)); 
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static ComponentListener
remove(ComponentListener lis, ComponentListener old)
{
  return((ComponentListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static ContainerListener
remove(ContainerListener lis, ContainerListener old)
{
  return((ContainerListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static FocusListener
remove(FocusListener lis, FocusListener old)
{
  return((FocusListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static KeyListener
remove(KeyListener lis, KeyListener old)
{
  return((KeyListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static MouseListener
remove(MouseListener lis, MouseListener old)
{
  return((MouseListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static MouseMotionListener
remove(MouseMotionListener lis, MouseMotionListener old)
{
  return((MouseMotionListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static WindowListener
remove(WindowListener lis, WindowListener old)
{
  return((WindowListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static ActionListener
remove(ActionListener lis, ActionListener old)
{
  return((ActionListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static ItemListener
remove(ItemListener lis, ItemListener old)
{
  return((ItemListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static AdjustmentListener
remove(AdjustmentListener lis, AdjustmentListener old)
{
  return((AdjustmentListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static TextListener
remove(AdjustmentListener lis, TextListener old)
{
  return((TextListener)removeInternal(lis, old));
}

/*************************************************************************/

/**
  * Removes the listener <code>old</code> from the listener <code>lis</code>.
  *
  * @param lis The listener to remove <code>old</code> from.
  * @param old The listener to remove.
  *
  * @return The resulting listener after the remove operation.
  */
public static EventListener
removeInternal(EventListener lis, EventListener old)
{
  if (lis == null)
    return(null);
  else if (lis == old)
    return(null);
  else if (lis instanceof AWTEventMulticaster)
    return(((AWTEventMulticaster)lis).remove(old));
  else
    return(lis);
}

/*************************************************************************/

/**
  * // FIXME: What does this method do?
  */
protected static void
save(ObjectOutputStream stream, String str, EventListener lis)
     throws IOException
{
  throw new RuntimeException("Not Implemented");
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>AWTEventMulticaster</code> with
  * the specified event listener parameters.
  *
  * @param a The "a" listener object.
  * @param b The "b" listener object.
  */
protected
AWTEventMulticaster(EventListener a, EventListener b)
{
  this.a = a;
  this.b = b;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Removes the specified object from this multicaster object.  If the
  * object to remove is not part of this multicaster, then the remove
  * method on the parent multicaster (if it exists) is called and a 
  * new multicaster object is returned based on that object and this
  * multicaster's non-parent object.
  *
  * @param old The object to remove from this multicaster.
  *
  * @return The resulting multicaster with the specified listener removed.
  */
protected EventListener
remove(EventListener old)
{
  if (this.b == old)
    {
      return(a);
    }
  else if (a instanceof AWTEventMulticaster)
    {
      EventListener l = ((AWTEventMulticaster)a).remove(old);
      return(new AWTEventMulticaster(l, b));
    }
  else if (this.a == old)
    {
      return(b);
    }
  else
    {
      return(this);
    }
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
componentResized(ComponentEvent event)
{
  ((ComponentListener)a).componentResized(event);
  ((ComponentListener)b).componentResized(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
componentMoved(ComponentEvent event)
{
  ((ComponentListener)a).componentMoved(event);
  ((ComponentListener)b).componentMoved(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
componentShown(ComponentEvent event)
{
  ((ComponentListener)a).componentShown(event);
  ((ComponentListener)b).componentShown(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
componentHidden(ComponentEvent event)
{
  ((ComponentListener)a).componentHidden(event);
  ((ComponentListener)b).componentHidden(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
componentAdded(ContainerEvent event)
{
  ((ContainerListener)a).componentAdded(event);
  ((ContainerListener)b).componentAdded(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
componentRemoved(ContainerEvent event)
{
  ((ContainerListener)a).componentRemoved(event);
  ((ContainerListener)b).componentRemoved(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
focusGained(FocusEvent event)
{
  ((FocusListener)a).focusGained(event);
  ((FocusListener)b).focusGained(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
focusLost(FocusEvent event)
{
  ((FocusListener)a).focusLost(event);
  ((FocusListener)b).focusLost(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
keyTyped(KeyEvent event)
{
  ((KeyListener)a).keyTyped(event);
  ((KeyListener)b).keyTyped(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
keyPressed(KeyEvent event)
{
  ((KeyListener)a).keyPressed(event);
  ((KeyListener)b).keyPressed(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
keyReleased(KeyEvent event)
{
  ((KeyListener)a).keyReleased(event);
  ((KeyListener)b).keyReleased(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
mouseClicked(MouseEvent event)
{
  ((MouseListener)a).mouseClicked(event);
  ((MouseListener)b).mouseClicked(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
mousePressed(MouseEvent event)
{
  ((MouseListener)a).mousePressed(event);
  ((MouseListener)b).mousePressed(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
mouseReleased(MouseEvent event)
{
  ((MouseListener)a).mouseReleased(event);
  ((MouseListener)b).mouseReleased(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
mouseEntered(MouseEvent event)
{
  ((MouseListener)a).mouseEntered(event);
  ((MouseListener)b).mouseEntered(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
mouseExited(MouseEvent event)
{
  ((MouseListener)a).mouseExited(event);
  ((MouseListener)b).mouseExited(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
mouseDragged(MouseEvent event)
{
  ((MouseMotionListener)a).mouseDragged(event);
  ((MouseMotionListener)b).mouseDragged(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
mouseMoved(MouseEvent event)
{
  ((MouseMotionListener)a).mouseMoved(event);
  ((MouseMotionListener)b).mouseMoved(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
windowOpened(WindowEvent event)
{
  ((WindowListener)a).windowOpened(event);
  ((WindowListener)b).windowOpened(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
windowClosed(WindowEvent event)
{
  ((WindowListener)a).windowClosed(event);
  ((WindowListener)b).windowClosed(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
windowClosing(WindowEvent event)
{
  ((WindowListener)a).windowClosing(event);
  ((WindowListener)b).windowClosing(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
windowIconified(WindowEvent event)
{
  ((WindowListener)a).windowIconified(event);
  ((WindowListener)b).windowIconified(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
windowDeiconified(WindowEvent event)
{
  ((WindowListener)a).windowDeiconified(event);
  ((WindowListener)b).windowDeiconified(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
windowActivated(WindowEvent event)
{
  ((WindowListener)a).windowActivated(event);
  ((WindowListener)b).windowActivated(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
windowDeactivated(WindowEvent event)
{
  ((WindowListener)a).windowDeactivated(event);
  ((WindowListener)b).windowDeactivated(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
actionPerformed(ActionEvent event)
{
  ((ActionListener)a).actionPerformed(event);
  ((ActionListener)b).actionPerformed(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
itemStateChanged(ItemEvent event)
{
  ((ItemListener)a).itemStateChanged(event);
  ((ItemListener)b).itemStateChanged(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
adjustmentValueChanged(AdjustmentEvent event)
{
  ((AdjustmentListener)a).adjustmentValueChanged(event);
  ((AdjustmentListener)b).adjustmentValueChanged(event);
}

/*************************************************************************/

/**
  * Handles this event by dispatching it to the "a" and "b" listener
  * instances.
  *
  * @param event The event to handle.
  */
public void
textValueChanged(TextEvent event)
{
  ((TextListener)a).textValueChanged(event);
  ((TextListener)b).textValueChanged(event);
}

/*************************************************************************/

/**
  * // FIXME: What does this method do?
  */
protected void
saveInternal(ObjectOutputStream stream, String str) throws IOException
{
  throw new RuntimeException("Not Implemented");
}

} // class AWTEventMulticaster

