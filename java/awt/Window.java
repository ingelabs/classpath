/* Window.java -- Toplevel window object
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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.peer.WindowPeer;
import java.awt.peer.ContainerPeer;
import java.awt.peer.ComponentPeer;
import java.util.Locale;

/**
  * This class represents a top-level window with no decorations.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Window extends Container implements java.io.Serializable
{

// FIXME: Serialization

/*
 * Static Variables
 */

private static final String defaultWarningString = "Warning: Applet Window";

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * @serial A string that is displayed if this window is being popped up
  * by an unsecure applet or application.
  */
private String warningString = defaultWarningString;

// List of listeners for window events.
private WindowListener window_listeners;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Window</code> with the specified
  * parent.  The window will initially be invisible.
  *
  * @param parent The owning <code>Frame</code> of this window.
  */
public
Window(Frame parent)
{
  setParent(parent);

  // FIXME: SecurityManager check for toplevel window.
  // FIXME: How should we add to parent/create peer?  This is not really
  // a child component of the parent container.
  if (parent != null)
    {
      parent.ownedWindows.addElement(this);
    }
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the warning string that will be displayed if this window is
  * popped up by an unsecure applet or application.
  *
  * @return The unsecure window warning message.
  */
public final String
getWarningString()
{
  return(warningString);
}

/*************************************************************************/

/**
  * Adds the specified listener to the list of <code>WindowListeners</code>
  * that will receive events for this window.
  *
  * @param listener The <code>WindowListener</code> to add.
  */
public synchronized void
addWindowListener(WindowListener listener)
{
  window_listeners = AWTEventMulticaster.add(window_listeners, listener);
}

/*************************************************************************/

/**
  * Removes the specified listener from the list of
  * <code>WindowListeners</code> that will receive events for this window.
  *
  * @param listener The <code>WindowListener</code> to remove.
  */
public synchronized void
removeWindowListener(WindowListener listener)
{
  window_listeners = AWTEventMulticaster.remove(window_listeners, listener);
}

/*************************************************************************/

/**
  * Processes the specified event for this window.  If the event is an
  * instance of <code>WindowEvent</code>, then <code>processWindowEvent()</code>
  * is called to process the event, otherwise the superclass version of
  * this method is invoked.
  *
  * @param event The event to process.
  */
protected void
processEvent(AWTEvent event)
{
  if (event instanceof WindowEvent)
    processWindowEvent((WindowEvent)event);
  else
    super.processEvent(event);
}

/*************************************************************************/

/**
  * Dispatches this event to any listeners that are listening for
  * <code>WindowEvents</code> on this window.  This method only gets
  * invoked if it is enabled via <code>enableEvents()</code> or if
  * a listener has been added.
  *
  * @param event The event to process.
  */
protected void
processWindowEvent(WindowEvent event)
{
  if (window_listeners != null)
    switch(event.getID())
      {
        case WindowEvent.WINDOW_ACTIVATED:
          window_listeners.windowActivated(event);
          break;

        case WindowEvent.WINDOW_DEACTIVATED:
          window_listeners.windowDeactivated(event);
          break;

        case WindowEvent.WINDOW_ICONIFIED:
          window_listeners.windowIconified(event);
          break;

        case WindowEvent.WINDOW_DEICONIFIED:
          window_listeners.windowDeiconified(event);
          break;

        case WindowEvent.WINDOW_OPENED:
          window_listeners.windowOpened(event);
          break;

        case WindowEvent.WINDOW_CLOSING:
          window_listeners.windowClosing(event);
          break;

        case WindowEvent.WINDOW_CLOSED:
          window_listeners.windowClosed(event);
          break;

        default:
          break;
      }
}

/*************************************************************************/

/**
  * Post a Java 1.0 event to the event queue.
  *
  * @param event The event to post.
  */
public boolean
postEvent(Event event)
{
  return(false);
}

/*************************************************************************/

/**
  * Sets the cursor for this window to the specifiec cursor.
  *
  * @param cursor The new cursor for this window.
  */
public synchronized void
setCursor(Cursor cursor)
{
  super.setCursor(cursor);
}

/*************************************************************************/

/**
  * Makes this window visible and brings it to the front.
  */
public void
show()
{
  if (getPeer() == null)
    addNotify();

  super.show();
  toFront();
}

/*************************************************************************/

/**
  * Tests whether or not this window is visible on the screen.
  *
  * @return <code>true</code> if this window is visible, <code>false</code>
  * otherwise.
  */
public boolean
isShowing()
{
  return(super.isShowing());
}

/*************************************************************************/

/**
  * Brings this window to the front so that it displays in front of
  * any other windows.
  */
public void
toFront()
{
  ((WindowPeer)getPeer()).toFront();
}

/*************************************************************************/

/**
  * Sends this window to the back so that all other windows display in
  * front of it.
  */
public void
toBack()
{
  ((WindowPeer)getPeer()).toBack();
}

/*************************************************************************/

/**
  * Returns the toolkit used to create this window.
  *
  * @return The toolkit used to create this window.
  */
public Toolkit
getToolkit()
{
  return(super.getToolkit());
}

/*************************************************************************/

/**
  * Returns the locale that this window is configured for.
  *
  * @return The locale this window is configured for.
  */
public Locale
getLocale()
{
  return(super.getLocale());
}

/*************************************************************************/

/**
  * Returns the child window that has focus if this window is active.
  * This method returns <code>null</code> if this window is not active
  * or no children have focus.
  *
  * @return The component that has focus, or <code>null</code> if no
  * component has focus.
  */
public Component
getFocusOwner()
{
  // FIXME: Implement
  return(null);
}

/*************************************************************************/

/**
  * Relays out this window's child components at their preferred size.
  */
public void
pack()
{
  Dimension dim = getPreferredSize();
  setSize(dim);
  doLayout();
}

/*************************************************************************/

/**
  * Creates the native peer for this window.
  */
public void
addNotify()
{
  setPeer((ComponentPeer)getToolkit().createWindow(this));
}

/*************************************************************************/

/**
  * Called to free any resource associated with this window.
  */
public void
dispose()
{
  // Destroy all component peers.
  Component[] c = getComponents();
  if (c.length > 0)
    for (int i = 0; i < c.length; i++)
      c[i].removeNotify();

  removeNotify();
}

} // class Window 

