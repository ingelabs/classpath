/*************************************************************************
/* Component.java -- Superclass of all AWT components
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
import java.awt.image.*;
import java.awt.peer.ComponentPeer;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Locale;

// FIXME: Java 1.0 event model unimplemented

// FIXME: This isn't even close to serialization compatible with the JDK.

/**
  * This is the superclass of all non-menu AWT widgets. 
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class Component implements ImageObserver, MenuContainer,
                                           java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * Constant returned by the <code>getAlignmentY</code> method to indicate
  * that the component wishes to be aligned to the bottom relative to
  * other components.
  */
public static final float BOTTOM_ALIGNMENT = (float)1.0;

/**
  * Constant returned by the <code>getAlignmentY</code> and 
  * <code>getAlignmentX</code> methods to indicate
  * that the component wishes to be aligned to the center relative to
  * other components.
  */
public static final float CENTER_ALIGNMENT = (float)0.5;

/**
  * Constant returned by the <code>getAlignmentY</code> method to indicate
  * that the component wishes to be aligned to the top relative to
  * other components.
  */
public static final float TOP_ALIGNMENT = (float)0.0;

/**
  * Constant returned by the <code>getAlignmentX</code> method to indicate
  * that the component wishes to be aligned to the right relative to
  * other components.
  */
public static final float RIGHT_ALIGNMENT = (float)1.0;

/**
  * Constant returned by the <code>getAlignmentX</code> method to indicate
  * that the component wishes to be aligned to the left relative to
  * other components.
  */
public static final float LEFT_ALIGNMENT = (float)0.0;

/*************************************************************************/

/*
 * Instance Variables
 */

// The background color of this component.
private Color background_color;

// The foreground color of this component.
private Color foreground_color;

// The bounding rectangle of this component.
private Rectangle bounding_rectangle;

// The cursor for this component
private Cursor cursor;

// The font for this component
private Font font;

// The locale for this component
private Locale locale;

// The name of the component
private String name;

// The parent of this component
private Container parent;

// The native peer for this componet
private ComponentPeer peer;

// Indicates whether or not this component is valid.
private boolean valid;

// Indicates whether or not this component is visible
private boolean visible = true;

// Indicates whether or not this component is enabled.
private boolean enabled = true;

// The toolkit for this componet
private Toolkit toolkit = Toolkit.getDefaultToolkit();

// The synchronization locking object for this component
private Object tree_lock = this;

// The mask for all events that are enabled for this component
private long enabled_events = 0L;

// Listeners for dispatching events
private ComponentListener component_listener;
private FocusListener focus_listener;
private KeyListener key_listener;
private MouseListener mouse_listener;
private MouseMotionListener mouse_motion_listener;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Default constructor for subclasses.
  */
protected
Component()
{
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the preferred horizontal alignment of this component.  The
  * value returned will be one of the constants defined in this class.
  *
  * @return The preferred horizontal alignment of this component.
  */
public float
getAlignmentX()
{
  return(CENTER_ALIGNMENT);
}

/*************************************************************************/

/**
  * Returns the preferred vertical alignment of this component.  The
  * value returned will be one of the constants defined in this class.
  *
  * @return The preferred vertical alignment of this component.
  */
public float
getAlignmentY()
{
  return(CENTER_ALIGNMENT);
}

/*************************************************************************/

/**
  * Returns this component's background color.
  *
  * @return The component's background color.
  */
public Color
getBackground()
{
  return(background_color);
}

/*************************************************************************/

/**
  * Sets this component's background color to the specified color.
  *
  * @param background_color The new background color
  */
public void
setBackground(Color background_color)
{
  this.background_color = background_color;
  getPeer().setBackground(background_color);
}

/*************************************************************************/

/**
  * Returns this component's foreground color.
  *
  * @return This component's foreground color.
  */
public Color
getForeground()
{
  return(foreground_color);
}

/*************************************************************************/

/**
  * Sets this component's foreground color to the specified color.
  *
  * @param foreground_color The new foreground color.
  */
public void
setForeground(Color foreground_color)
{
  this.foreground_color = foreground_color;
  getPeer().setForeground(foreground_color);
}

/*************************************************************************/

/**
  * Returns a bounding rectangle for this component.  Note that the
  * returned rectange is relative to this component's parent, not to
  * the screen.
  *
  * @return The bounding rectangle for this component.
  */
public Rectangle
getBounds()
{
  return(bounding_rectangle);
}

/*************************************************************************/

/**
  * Returns a bounding rectangle for this component.  Note that the
  * returned rectange is relative to this component's parent, not to
  * the screen.
  *
  * @return The bounding rectangle for this component.
  *
  * @deprecated Deprecated in favor of <code>getBounds()</code>.
  */
public Rectangle
bounds()
{
  return(bounding_rectangle);
}

/*************************************************************************/

/**
  * Sets the bounding rectangle for this component to the specified
  * rectangle.  Note that these coordinates are relative to the parent,
  * not to the screen.
  *
  * @param bounding_rectangle The new bounding rectangle.
  */
public void
setBounds(Rectangle bounding_rectangle)
{
  setBounds(bounding_rectangle.x, bounding_rectangle.y,
            bounding_rectangle.width, bounding_rectangle.height);
}

/*************************************************************************/

/**
  * Sets the bounding rectangle for this component to the specified
  * values.  Note that these coordinates are relative to the parent,
  * not to the screen.
  *
  * @param x The X coordinate of the upper left corner of the rectangle.
  * @param y The Y coordinate of the upper left corner of the rectangle.
  * @param width The width of the rectangle.
  * @param height The height of the rectangle.
  */
public void
setBounds(int x, int y, int width, int height)
{
  bounding_rectangle = new Rectangle(x, y, width, height);
  getPeer().setBounds(x, y, width, height);
}

/*************************************************************************/

/**
  * Sets the bounding rectangle for this component to the specified
  * values.  Note that these coordinates are relative to the parent,
  * not to the screen.
  *
  * @param x The X coordinate of the upper left corner of the rectangle.
  * @param y The Y coordinate of the upper left corner of the rectangle.
  * @param width The width of the rectangle.
  * @param height The height of the rectangle.
  *
  * @deprecated This method is deprecated in favor of
  * <code>setBounds(int, int, int, int)</code>.
  */
public void
reshape(int x, int y, int width, int height)
{
  setBounds(new Rectangle(x, y, width, height));
}

/*************************************************************************/

/**
  * Returns the color model of the device this componet is displayed on.
  *
  * @return This object's color model.
  */
public ColorModel
getColorModel()
{
  return(getPeer().getColorModel());
}

/*************************************************************************/

/**
  * Returns the component occupying the position (x,y).  This will either
  * be this component, an immediate child component, or <code>null</code>
  * if neither of the first two occupies the specified location.
  *
  * @param x The X coordinate to search for components at.
  * @param y The Y coordinate to search for components at.
  *
  * @return The component at the specified location, for <code>null</code>
  * if there is none.
  */
public Component
getComponentAt(int x, int y)
{
  if (getBounds().contains(x,y))
    return(this);
  else
    return(null);
}

/*************************************************************************/

/**
  * Returns the component occupying the specified point  This will either
  * be this component, an immediate child component, or <code>null</code>
  * if neither of the first two occupies the specified location.
  *
  * @param point The point to search for components at.
  *
  * @return The component at the specified location, for <code>null</code>
  * if there is none.
  */
public Component
getComponentAt(Point point)
{
  return(getComponentAt(point.x, point.y));
}

/*************************************************************************/

/**
  * Returns the component occupying the position (x,y).  This will either
  * be this component, an immediate child component, or <code>null</code>
  * if neither of the first two occupies the specified location.
  *
  * @param x The X coordinate to search for components at.
  * @param y The Y coordinate to search for components at.
  *
  * @return The component at the specified location, for <code>null</code>
  * if there is none.
  *
  * @deprecated The method is deprecated in favor of 
  * <code>getComponentAt()</code>.
  */
public Component
locate(int x, int y)
{
  return(getComponentAt(x, y));
}

/*************************************************************************/

/**
  * Returns the cursor for this component.
  *
  * @return The cursor for this component.
  */
public Cursor
getCursor()
{
  return(cursor);
}

/*************************************************************************/

/**
  * Sets the cursor for this component to the specified cursor.
  *
  * @param cursor The new cursor for this component.
  */
public void
setCursor(Cursor cursor)
{
  this.cursor = cursor;
  getPeer().setCursor(cursor);
}

/*************************************************************************/

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
  getPeer().setFont(font);
}

/*************************************************************************/

/**
  * Returns the font metrics for the specified font in this component.
  *
  * @param font The font to retrieve metrics for.
  *
  * @return The font metrics for the specified font.
  */
public FontMetrics
getFontMetrics(Font font)
{
  return(getPeer().getFontMetrics(font));
}

/*************************************************************************/

/**
  * Returns a graphics object for this component.  Returns <code>null</code>
  * if this component is not currently displayed on the screen.
  *
  * @return A graphics object for this component.
  */
public Graphics
getGraphics()
{
  return(getPeer().getGraphics());
}

/*************************************************************************/

/**
  * Returns the locale for this component.  If this component does not
  * have a locale, the locale of the parent component is returned.  If the
  * component has no parent, the system default locale is returned.
  *
  * @return The locale for this component.
  */
public Locale
getLocale()
{
  if (locale != null)
    return(locale);

  Component p = getParent();
  if (p != null)
    return(p.getLocale());
  else
    return(Locale.getDefault());
}

/*************************************************************************/

/**
  * Sets the locale for this component to the specified locale.
  *
  * @param locale The new locale for this component.
  */
public void
setLocale(Locale locale)
{
  this.locale = locale;
}

/*************************************************************************/

/**
  * Returns the location of this component's top left corner relative to
  * its parent component.
  *
  * @return The location of this component.
  */
public Point
getLocation()
{
  return(getBounds().getLocation());
}

/*************************************************************************/

/**
  * Returns the location of this component's top left corner relative to
  * its parent component.
  *
  * @return The location of this component.
  *
  * @deprecated This method is deprecated in favor of 
  * <code>getLocation()</code>.
  */
public Point
location()
{
  return(getLocation());
}

/*************************************************************************/

/**
  * Moves this component to the specified location.  The coordinates are
  * the new upper left corner of this component.
  *
  * @param x The new X coordinate of this component.
  * @param y The new Y coordinate of this component.
  */
public void
setLocation(int x, int y)
{
  setBounds(x, y, getBounds().width, getBounds().y);
}

/*************************************************************************/

/**
  * Moves this component to the specified location.  The coordinates are
  * the new upper left corner of this component.
  *
  * @param x The new X coordinate of this component.
  * @param y The new Y coordinate of this component.
  *
  * @deprecated Deprecated in favor for <code>setLocation</code>.
  */
public void
move(int x, int y)
{
  setLocation(x, y);
}

/*************************************************************************/

/**
  * Returns the location of this component's top left corner in screen
  * coordinates.
  *
  * @return The location of this component in screen coordinates.
  */
public Point
getLocationOnScreen()
{
  return(getPeer().getLocationOnScreen());
}

/*************************************************************************/

/**
  * Returns the component's maximum size.
  *
  * @return The component's maximum size.
  */
public Dimension
getMaximumSize()
{
  // FIXME: Is this right?
  return(getPeer().getPreferredSize()); 
}

/*************************************************************************/

/**
  * Returns the component's minimum size.
  *
  * @return The component's minimum size.
  */
public Dimension
getMinimumSize()
{
  return(getPeer().getMinimumSize());
}

/*************************************************************************/

/**
  * Returns the component's minimum size.
  *
  * @return The component's minimum size.
  *
  * @deprecated Deprecated in favor of <code>getMinimumSize()</code>
  */
public Dimension
minimumSize()
{
  return(getPreferredSize());
}

/*************************************************************************/

/**
  * Returns the component's preferred size.
  *
  * @return The component's preferred size.
  */
public Dimension
getPreferredSize()
{
  return(getPeer().getPreferredSize());
}

/*************************************************************************/

/**
  * Returns the component's preferred size.
  *
  * @return The component's preferred size.
  *
  * @deprecated Deprecated in favor of <code>getPreferredSize()</code>.
  */
public Dimension
preferredSize()
{
  return(getPreferredSize());
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
public Container
getParent()
{
  return(parent);
} 

/*************************************************************************/

// Sets the parent of this component.
final void
setParent(Container parent)
{
  this.parent = parent;
}

/*************************************************************************/

/**
  * Returns the native windowing system peer for this component.
  *
  * @return The peer for this component.
  */
public ComponentPeer
getPeer()
{
  return(peer);
}

/*************************************************************************/

// Sets the peer for this component.
final void
setPeer(ComponentPeer peer)
{
  this.peer = peer;
}

/*************************************************************************/

/**
  * Returns the size of this object.
  *
  * @return The size of this object.
  */
public Dimension
getSize()
{
  return(getBounds().getSize());
}

/*************************************************************************/

/**
  * Returns the size of this object.
  *
  * @return The size of this object.
  *
  * @deprecated This method is deprecated in favor of <code>getSize</code>.
  */
public Dimension
size()
{
  return(getSize());
}

/*************************************************************************/

/**
  * Sets the size of this component to the specified value.
  * 
  * @param dim The new size of this component.
  */
public void
setSize(Dimension dim)
{
  Rectangle rect = getBounds();
  setBounds(rect.x, rect.y, dim.width, dim.height);
}

/*************************************************************************/

/**
  * Sets the size of this component to the specified value.
  * 
  * @param dim The new size of this component.
  *
  * @deprecated This method is deprecated in favor of <code>setSize</code>.
  */
public void
resize(Dimension dim)
{
  setSize(dim);
}

/*************************************************************************/

/**
  * Sets the size of this component to the specified value.
  * 
  * @param width The new width of the component.
  * @param height The new height of the component.
  *
  * @deprecated This method is deprecated in favor of <code>setSize</code>.
  */
public void
resize(int width, int height)
{
  setSize(new Dimension(width, height));
}

/*************************************************************************/

/**
  * Returns the toolkit in use for this component.
  *
  * @return The toolkit for this component.
  */
public Toolkit
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
  * Tests whether or not this component is visible.
  *
  * @return <code>true</code> if the component is visible,
  * <code>false</code> otherwise.
  */
public boolean
isVisible()
{
  return(visible);
}

/*************************************************************************/

/**
  * Tests whether or not this component is actually being shown on
  * the screen.  This will be true if and only if it this component is
  * visible and its parent components are all visible.
  *
  * @return <code>true</code> if the component is showing on the screen,
  * <code>false</code> otherwise.
  */
public boolean
isShowing()
{
  if (!visible)
    return(false);

  Component c = getParent();
  if (c != null)
    return(c.isShowing());
  else
    return(true);
}

/*************************************************************************/

/**
  * Makes this component visible or invisible.
  *
  * @param visible <code>true</code> to make this component visible,
  * </code>false</code> to make it invisible.
  */
public void
setVisible(boolean visible)
{
  this.visible = visible;
  getPeer().setVisible(visible);
}

/*************************************************************************/

/**
  * Makes this component visible on the screen.
  *
  * @deprecated Deprecated in favor of <code>setVisible()</code>.
  */
public void
show()
{
  setVisible(true);
}

/*************************************************************************/

/**
  * Makes this component visible or invisible.
  *
  * @param visible <code>true</code> to make this component visible,
  * </code>false</code> to make it invisible.
  *
  * @deprecated Deprecated in favor of <code>setVisible()</code>.
  */
public void
show(boolean visible)
{
  setVisible(visible);
}

/*************************************************************************/

/**
  * Hides this component so that it is no longer shown on the screen.
  *
  * @deprecated Deprecated in favor of <code>setVisible()</code>.
  */
public void
hide()
{
  setVisible(false);
}

/*************************************************************************/

/**
  * Tests whether or not this component is enabled.
  *
  * @return <code>true</code> if the component is enabled,
  * <code>false</code> otherwise.
  */
public boolean
isEnabled()
{
  return(enabled);
}

/*************************************************************************/

/**
  * Enables or disables this component.
  *
  * @param enabled <code>true</code> to enable this component, 
  * <code>false</code> to disable it.
  *
  * @deprecated Deprecated in favor of <code>setEnabled()</code>.
  */
public void
setEnabled(boolean enabled)
{
  this.enabled = enabled;
  getPeer().setEnabled(enabled);
}


/*************************************************************************/

/**
  * Disables this component.
  *
  * @deprecated Deprecated in favor of <code>setEnabled()</code>.
  */
public void
disable()
{
  setEnabled(false);
}

/*************************************************************************/

/**
  * Enables this component.
  *
  * @deprecated Deprecated in favor of <code>setEnabled()</code>.
  */
public void
enable()
{
  setEnabled(true);
}

/*************************************************************************/

/**
  * Enables or disables this component.
  *
  * @param enabled <code>true</code> to enable this component, 
  * <code>false</code> to disable it.
  *
  * @deprecated Deprecated in favor of <code>setEnabled()</code>.
  */
public void
enable(boolean enabled)
{
  setEnabled(enabled);
}

/*************************************************************************/

/**
  * Tests whether or not the specified point is contained within this
  * component.  Coordinates are relative to this component.
  *
  * @param x The X coordinate of the point to test.
  * @param y The Y coordinate of the point to test.
  *
  * @return <code>true</code> if the point is within this component,
  * <code>false</code> otherwise.
  */
public boolean
contains(int x, int y)
{
  // FIXME: Is the origin the upper left of this component, or the upper
  // left of the parent component?
  return(getBounds().contains(x, y));
}

/*************************************************************************/

/**
  * Tests whether or not the specified point is contained within this
  * component.  Coordinates are relative to this component.
  *
  * @param point The point to test.
  *
  * @return <code>true</code> if the point is within this component,
  * <code>false</code> otherwise.
  */
public boolean
contains(Point point)
{
  // FIXME: Is the origin the upper left of this component, or the upper
  // left of the parent component?
  return(getBounds().contains(point.x, point.y));
}

/*************************************************************************/

/**
  * Tests whether or not the specified point is contained within this
  * component.  Coordinates are relative to this component.
  *
  * @param x The X coordinate of the point to test.
  * @param y The Y coordinate of the point to test.
  *
  * @return <code>true</code> if the point is within this component,
  * <code>false</code> otherwise.
  *
  * @deprecated Deprecated in favor of <code>contains(int, int)</code>.
  */
public boolean
inside(int x, int y)
{
  return(contains(x, y));
}

/*************************************************************************/

/**
  * AWT 1.0 focus event processor.
  *
  * @deprecated Deprecated in favor of <code>processFocusEvent</code>.
  */
public boolean
gotFocus(Event event, Object what)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 focus event processor.
  *
  * @deprecated Deprecated in favor of <code>processFocusEvent</code>.
  */
public boolean
lostFocus(Event event, Object what)
{
  return(true);
}

/*************************************************************************/

/**
  * Tests whether or not this component is in the group that can
  * be traversed using the keyboard traversal mechanism (such as the TAB
  * key).
  *
  * @return <code>true</code> if the component is traversed via the TAB
  * key, <code>false</code> otherwise.
  */
public boolean
isFocusTraversable()
{
  return(getPeer().isFocusTraversable());
}

/*************************************************************************/

/**
  * Requests that this component be given focus.  The <code>gotFocus()</code>
  * method on this event will be called when and if this request was
  * successful.
  */
public void
requestFocus()
{
  getPeer().requestFocus();
}

/*************************************************************************/

/**
  * Transfers focus to the next component in the focus traversal order.
  */
public void
transferFocus()
{
}

/*************************************************************************/

/**
  * AWT 1.0 focus event processor.
  *
  * @deprecated Deprecated in favor of <code>transferFocus()</code>.
  */
public void
nextFocus()
{
  transferFocus();
}

/*************************************************************************/

/**
  * AWT 1.0 event processor.
  *
  * @deprecated Deprecated in favor of <code>processEvent</code>.
  */
public boolean 
handleEvent(Event event)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 action event processor.
  *
  * @deprecated Deprecated in favor of the <code>ActionListener</code>
  * interface.
  */
public boolean
action(Event event, Object what)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 event dispatcher.
  *
  * @deprecated Deprecated in favor of <code>dispatchEvent()</code>.
  */
public void
deliverEvent(Event event)
{
}

/*************************************************************************/

/**
  * AWT 1.0 event dispatcher.
  *
  * @deprecated Deprecated in favor of <code>dispatchEvent()</code>.
  */
public boolean
postEvent(Event event)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 key press event.
  *
  * @deprecated Deprecated in favor of <code>processKeyEvent</code>.
  */
public boolean
keyDown(Event event, int key)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 key press event.
  *
  * @deprecated Deprecated in favor of <code>processKeyEvent</code>.
  */
public boolean
keyUp(Event event, int key)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 mouse event.
  *
  * @deprecated Deprecated in favor of <code>processMouseEvent()</code>.
  */
public boolean
mouseDown(Event event, int x, int y)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 mouse event.
  *
  * @deprecated Deprecated in favor of <code>processMouseEvent()</code>.
  */
public boolean
mouseUp(Event event, int x, int y)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 mouse event.
  *
  * @deprecated Deprecated in favor of <code>processMouseEvent()</code>.
  */
public boolean
mouseEnter(Event event, int x, int y)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 mouse event.
  *
  * @deprecated Deprecated in favor of <code>processMouseEvent()</code>.
  */
public boolean
mouseExit(Event event, int x, int y)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 mouse event.
  *
  * @deprecated Deprecated in favor of <code>processMouseMotionEvent()</code>.
  */
public boolean
mouseDrag(Event event, int x, int y)
{
  return(true);
}

/*************************************************************************/

/**
  * AWT 1.0 mouse event.
  *
  * @deprecated Deprecated in favor of <code>processMouseMotionEvent()</code>.
  */
public boolean
mouseMove(Event event, int x, int y)
{
  return(true);
}

/*************************************************************************/

/**
  * Enables the specified events.  The events to enable are specified
  * by OR-ing together the desired masks from <code>AWTEvent</code>.
  * <p>
  * Events are enabled by default when a listener is attached to the
  * component for that event type.  This method can be used by subclasses
  * to ensure the delivery of a specified event regardless of whether
  * or not a listener is attached.
  *
  * @param enable_events The desired events to enable.
  */
protected final void
enableEvents(long enable_events)
{
  enabled_events |= enable_events;
}

/*************************************************************************/

/**
  * Disables the specified events.  The events to disable are specified
  * by OR-ing together the desired masks from <code>AWTEvent</code>.
  *
  * @param disable_events The desired events to disable.
  */
protected final void
disableEvents(long disable_events)
{
  enabled_events &= ~disable_events;
}

/*************************************************************************/

/**
  * Adds the specified listener to this component.
  *
  * @param listener The new listener to add.
  */
public synchronized void
addComponentListener(ComponentListener listener)
{
  component_listener = AWTEventMulticaster.add(component_listener, listener);
  enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
}

/*************************************************************************/

/**
  * Removes the specified listener from the component.
  *
  * @param listener The listener to remove.
  */
public synchronized void
removeComponentListener(ComponentListener listener)
{
  component_listener = AWTEventMulticaster.remove(component_listener, listener);
}

/*************************************************************************/

/**
  * Adds the specified listener to this component.
  *
  * @param listener The new listener to add.
  */
public synchronized void
addFocusListener(FocusListener listener)
{
  focus_listener = AWTEventMulticaster.add(focus_listener, listener);
  enableEvents(AWTEvent.FOCUS_EVENT_MASK);
}

/*************************************************************************/

/**
  * Removes the specified listener from the component.
  *
  * @param listener The listener to remove.
  */
public synchronized void
removeFocusListener(FocusListener listener)
{
  focus_listener = AWTEventMulticaster.remove(focus_listener, listener);
}

/*************************************************************************/

/**
  * Adds the specified listener to this component.
  *
  * @param listener The new listener to add.
  */
public synchronized void
addKeyListener(KeyListener listener)
{
  key_listener = AWTEventMulticaster.add(key_listener, listener);
  enableEvents(AWTEvent.KEY_EVENT_MASK);
}

/*************************************************************************/

/**
  * Removes the specified listener from the component.
  *
  * @param listener The listener to remove.
  */
public synchronized void
removeKeyListener(KeyListener listener)
{
  key_listener = AWTEventMulticaster.remove(key_listener, listener);
}

/*************************************************************************/

/**
  * Adds the specified listener to this component.
  *
  * @param listener The new listener to add.
  */
public synchronized void
addMouseListener(MouseListener listener)
{
  mouse_listener = AWTEventMulticaster.add(mouse_listener, listener);
  enableEvents(AWTEvent.MOUSE_EVENT_MASK);
}

/*************************************************************************/

/**
  * Removes the specified listener from the component.
  *
  * @param listener The listener to remove.
  */
public synchronized void
removeMouseListener(MouseListener listener)
{
  mouse_listener = AWTEventMulticaster.remove(mouse_listener, listener);
}

/*************************************************************************/

/**
  * Adds the specified listener to this component.
  *
  * @param listener The new listener to add.
  */
public synchronized void
addMouseMotionListener(MouseMotionListener listener)
{
  mouse_motion_listener = AWTEventMulticaster.add(mouse_motion_listener, 
                                                  listener);
  enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
}

/*************************************************************************/

/**
  * Removes the specified listener from the component.
  *
  * @param listener The listener to remove.
  */
public synchronized void
removeMouseMotionListener(MouseMotionListener listener)
{
  mouse_motion_listener = AWTEventMulticaster.remove(mouse_motion_listener, 
                                                     listener);
}

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

  // Only dispatch enabled events
  if (!isEventEnabled(event))
    return;

  ((Component)event.getSource()).processEvent(event);
}

private boolean isEventEnabled(ActionEvent e) 
  { return((enabled_events & AWTEvent.ACTION_EVENT_MASK) != 0); }
private boolean isEventEnabled(AdjustmentEvent e) 
  { return((enabled_events & AWTEvent.ADJUSTMENT_EVENT_MASK) != 0); }
private boolean isEventEnabled(ComponentEvent e) 
  { return((enabled_events & AWTEvent.COMPONENT_EVENT_MASK) != 0); }
private boolean isEventEnabled(ContainerEvent e) 
  { return((enabled_events & AWTEvent.CONTAINER_EVENT_MASK) != 0); }
private boolean isEventEnabled(FocusEvent e) 
  { return((enabled_events & AWTEvent.FOCUS_EVENT_MASK) != 0); }
private boolean isEventEnabled(ItemEvent e) 
  { return((enabled_events & AWTEvent.ITEM_EVENT_MASK) != 0); }
private boolean isEventEnabled(KeyEvent e) 
  { return((enabled_events & AWTEvent.KEY_EVENT_MASK) != 0); }
private boolean isEventEnabled(MouseEvent e) 
  { 
    if ((e.getID() == MouseEvent.MOUSE_MOVED) ||
        (e.getID() == MouseEvent.MOUSE_DRAGGED))
      return((enabled_events & AWTEvent.MOUSE_MOTION_EVENT_MASK) != 0); 
    else
      return((enabled_events & AWTEvent.MOUSE_EVENT_MASK) != 0); 
  }
private boolean isEventEnabled(TextEvent e) 
  { return((enabled_events & AWTEvent.TEXT_EVENT_MASK) != 0); }
private boolean isEventEnabled(WindowEvent e) 
  { return((enabled_events & AWTEvent.WINDOW_EVENT_MASK) != 0); }
private boolean isEventEnabled(Object e)
  {  return(false); }

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
  processEventInternal(event);
}

private void processEventInternal(ComponentEvent e) 
  { processComponentEvent(e); }
private void processEventInternal(FocusEvent e) 
  { processFocusEvent(e); }
private void processEventInternal(KeyEvent e) 
  { processKeyEvent(e); }
private void processEventInternal(MouseEvent e) 
  { 
    if ((e.getID() == MouseEvent.MOUSE_MOVED) ||
        (e.getID() == MouseEvent.MOUSE_DRAGGED))
      processMouseMotionEvent(e); 
    else
      processMouseEvent(e); 
  }
private void processEventInternal(Object e) {  }

/*************************************************************************/

/**
  * Called when a component event is dispatched and component events are
  * enabled.  This method passes the event along to any listeners
  * that are attached.
  *
  * @param event The <code>ComponentEvent</code> to process.
  */
protected void
processComponentEvent(ComponentEvent event)
{
  if (component_listener != null)
    {
      switch(event.getID())
        {
          case ComponentEvent.COMPONENT_HIDDEN:
            component_listener.componentHidden(event);
            break;

          case ComponentEvent.COMPONENT_SHOWN:
            component_listener.componentShown(event);
            break;

          case ComponentEvent.COMPONENT_MOVED:
            component_listener.componentMoved(event);
            break;

          case ComponentEvent.COMPONENT_RESIZED:
            component_listener.componentResized(event);
            break;

          default:
            break;
        }
    }
}

/*************************************************************************/

/**
  * Called when a focus event is dispatched and component events are
  * enabled.  This method passes the event along to any listeners
  * that are attached.
  *
  * @param event The <code>FocusEvent</code> to process.
  */
protected void
processFocusEvent(FocusEvent event)
{
  if (focus_listener != null)
    {
      switch(event.getID())
        {
          case FocusEvent.FOCUS_GAINED:
            focus_listener.focusGained(event);
            break;

          case FocusEvent.FOCUS_LOST:
            focus_listener.focusLost(event);
            break;

          default:
            break;
        }
    }
}

/*************************************************************************/

/**
  * Called when a key event is dispatched and component events are
  * enabled.  This method passes the event along to any listeners
  * that are attached.
  *
  * @param event The <code>KeyEvent</code> to process.
  */
protected void
processKeyEvent(KeyEvent event)
{
  if (key_listener != null)
    {
      switch(event.getID())
        {
          case KeyEvent.KEY_PRESSED:
            key_listener.keyPressed(event);
            break;

          case KeyEvent.KEY_RELEASED:
            key_listener.keyReleased(event);
            break;

          case KeyEvent.KEY_TYPED:
            key_listener.keyTyped(event);
            break;

          default:
            break;
        }
    }
}

/*************************************************************************/

/**
  * Called when a regular mouse event is dispatched and component events are
  * enabled.  This method passes the event along to any listeners
  * that are attached.
  *
  * @param event The <code>MouseEvent</code> to process.
  */
protected void
processMouseEvent(MouseEvent event)
{
  if (mouse_listener != null)
    {
      switch(event.getID())
        {
          case MouseEvent.MOUSE_PRESSED:
            mouse_listener.mousePressed(event);
            break;

          case MouseEvent.MOUSE_RELEASED:
            mouse_listener.mouseReleased(event);

          case MouseEvent.MOUSE_CLICKED:
            mouse_listener.mouseClicked(event);
            break;

          case MouseEvent.MOUSE_ENTERED:
            mouse_listener.mouseEntered(event);
            break;

          case MouseEvent.MOUSE_EXITED:
            mouse_listener.mouseExited(event);
            break;

          default:
            break;
        }
    }
}

/*************************************************************************/

/**
  * Called when a mouse motion event is dispatched and component events are
  * enabled.  This method passes the event along to any listeners
  * that are attached.
  *
  * @param event The <code>MouseMotionEvent</code> to process.
  */
protected void
processMouseMotionEvent(MouseEvent event)
{
  if (mouse_motion_listener != null)
    {
      switch(event.getID())
        {
          case MouseEvent.MOUSE_DRAGGED:
            mouse_motion_listener.mouseDragged(event);
            break;

          case MouseEvent.MOUSE_MOVED:
            mouse_motion_listener.mouseMoved(event);
            break;

          default:
            break;
        }
    }
}

/*************************************************************************/

/**
  * Called to inform this component it has been added to a container.
  * A native peer - if any - is created at this time.  This method is
  * called automatically by the AWT system and should not be called by
  * user level code.
  */
public void
addNotify()
{
}

/*************************************************************************/

/**
  * Called to inform this component is has been removed from its
  * container.  Its native peer - if any - is destroyed at this time.
  * This method is called automatically by the AWT system and should
  * not be called by user level code.
  */
public void
removeNotify()
{
}

/*************************************************************************/

/**
  * Tests whether or not this component is valid.  A invalid component needs
  * to have its layout redone.
  *
  * @return <code>true</code> if this component is valid, <code>false</code>
  * otherwise.
  */
public boolean
isValid()
{
  return(valid);
}

/*************************************************************************/

/**
  * Invalidates this component and all of its parent components.  This will
  * cause them to have their layout redone.
  */
public void
invalidate()
{
  valid = false;
  Component c = getParent();
  if (c != null)
    c.invalidate(); 
}

/*************************************************************************/

/**
  * Called to ensure that the layout for this component is valid.
  */
public void
validate()
{
  valid = true;
} 

/*************************************************************************/

/**
  * Calls the layout manager to re-layout the component.  This is called
  * during validation of a container in most cases.
  */
public void
doLayout()
{
}

/*************************************************************************/

/**
  * Calls the layout manager to re-layout the component.  This is called
  * during validation of a container in most cases.
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
  * Adds the specified popup menu to this component.
  *
  * @param menu The popup menu to be added.
  */
public synchronized void
add(PopupMenu menu)
{
  // FIXME: Implement
}

/*************************************************************************/

/**
  * Removes the specified popup menu from this component.
  *
  * @param menu The popup menu to remove.
  */
public synchronized void
remove(MenuComponent menu)
{
  // FIXME: Implement
}

/*************************************************************************/

/**
  * Returns the status of the loading of the specified image. The value
  * returned will be those flags defined in <code>ImageObserver</code>.
  *
  * @param image The image to check on.
  * @param observer The observer to be notified as the image loading
  * progresses.
  *
  * @return The image observer flags indicating the status of the load.
  */
public int
checkImage(Image image, ImageObserver observer)
{
  return(checkImage(image, image.getWidth(observer), 
         image.getHeight(observer), observer));
}

/*************************************************************************/

/**
  * Returns the status of the loading of the specified image. The value
  * returned will be those flags defined in <code>ImageObserver</code>.
  *
  * @param image The image to check on.
  * @param width The scaled image width.
  * @param height The scaled image height.
  * @param observer The observer to be notified as the image loading
  * progresses.
  *
  * @return The image observer flags indicating the status of the load.
  */
public int
checkImage(Image image, int width, int height, ImageObserver observer)
{
  return(getPeer().checkImage(image, width, height, observer));
}

/*************************************************************************/

/**
  * Creates an image from the specified producer.
  *
  * @param producer The image procedure to create the image from.
  *
  * @return The resulting image.
  */
public Image
createImage(ImageProducer producer)
{
  return(getPeer().createImage(producer));
}

/*************************************************************************/

/**
  * Creates an image with the specified width and height for use in
  * double buffering.
  *
  * @param width The width of the image.
  * @param height The height of the image.
  *
  * @return The requested image.
  */
public Image
createImage(int width, int height)
{
  return(getPeer().createImage(width, height));
}

/*************************************************************************/

/**
  * Prepares the specified image for rendering on this component.
  *
  * @param image The image to prepare for rendering.
  * @param observer The image observer to notify of the status of the
  * image preparation.
  *
  * @return <code>true</code> if the image is already fully prepared
  * for rendering, <code>false</code> otherwise.
  */
public boolean
prepareImage(Image image, ImageObserver observer)
{
  return(prepareImage(image, image.getWidth(observer), 
         image.getHeight(observer), observer));
}

/*************************************************************************/

/**
  * Prepares the specified image for rendering on this component at the
  * specified scaled width and height
  *
  * @param image The image to prepare for rendering.
  * @param width The scaled width of the image.
  * @param height The scaled height of the image.
  * @param observer The image observer to notify of the status of the
  * image preparation.
  *
  * @return <code>true</code> if the image is already fully prepared
  * for rendering, <code>false</code> otherwise.
  */
public boolean
prepareImage(Image image, int width, int height, ImageObserver observer)
{
  return(getPeer().prepareImage(image, width, height, observer));
}

/*************************************************************************/

/**
  * Called when an image has changed so that this component is
  * repainted.
  *
  * @param image The image that has been updated.
  * @param flags Flags as specified in <code>ImageObserver</code>.
  * @param x The X coordinate 
  * @param y The Y coordinate
  * @param width The width
  * @param height The height
  *
  * @return <code>true</code> if the image has been fully loaded,
  * <code>false</code> otherwise.
  */
public boolean
imageUpdate(Image image, int flags, int x, int y, int width, int height)
{
  // FIXME: Implement
  return(false);
}

/*************************************************************************/

/**
  * Paints this component on the screen.  The clipping region in the
  * graphics context will indicate the region that requires painting.
  *
  * @param graphics The graphics context for this paint job.
  */
public void
paint(Graphics graphics)
{
}

/*************************************************************************/

/**
  * Paints this entire component, including any sub-components.
  *
  * @param graphics The graphics context for this paint job.
  */
public void
paintAll(Graphics graphics)
{
}

/*************************************************************************/

/**
  * Repaint this entire component.  The <code>update()</code> method
  * on this component will be called as soon as possible.
  * // FIXME: What are the coords relative to?
  */
public void
repaint()
{
  repaint(0, 0, 0, bounding_rectangle.width, bounding_rectangle.height);
}

/*************************************************************************/

/**
  * Repaint this entire component.  The <code>update()</code> method
  * on this component will be called in approximate the specified number
  * of milliseconds.
  * // FIXME: What are the coords relative to?
  *
  * @param tm The number of milliseconds before this component should
  * be repainted.
  */
public void
repaint(long tm)
{
  repaint(tm, 0, 0, bounding_rectangle.width, bounding_rectangle.height);
}

/*************************************************************************/

/**
  * Repaints the specified rectangular region within this component.
  * This <code>update</code> method on this component will be called as
  * soon as possible.
  * // FIXME: What are the coords relative to?
  *
  * @param x The X coordinate of the upper left of the region to repaint
  * @param y The Y coordinate of the upper left of the region to repaint
  * @param width The width of the region to repaint.
  * @param height The height of the region to repaint.
  */
public void
repaint(int x, int y, int width, int height)
{
  repaint(0, x, y, width, height);
}

/*************************************************************************/

/**
  * Repaints the specified rectangular region within this component.
  * This <code>update</code> method on this component will be called in
  * approximately the specified number of milliseconds.
  * // FIXME: What are the coords relative to?
  *
  * @param tm The number of milliseconds before this component should
  * be repainted.
  * @param x The X coordinate of the upper left of the region to repaint
  * @param y The Y coordinate of the upper left of the region to repaint
  * @param width The width of the region to repaint.
  * @param height The height of the region to repaint.
  */
public void
repaint(long tm, int x, int y, int width, int height)
{
  Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new PaintEvent(
    this, PaintEvent.UPDATE, new Rectangle(x, y, width, height)));
}

/*************************************************************************/

/**
  * Updates this component.  This method fills the component
  * with the background color, then sets the foreground color of the
  * specified graphics context to the foreground color of this component
  * and calls the <code>paint()</code> method.
  * // FIXME: What are the coords relative to?
  *
  * @param graphics The graphics context for this update.
  */
public void
update(Graphics graphics)
{
  graphics.clearRect(0, 0, bounding_rectangle.width, 
                     bounding_rectangle.height);
  graphics.setColor(getForeground());
  paint(graphics);
}

/*************************************************************************/

/**
  * Prints this component.  This method is
  * provided so that printing can be done in a different manner from
  * painting.  However, the implementation in this class simply calls
  * the <code>paint()</code> method.
  *
  * @param graphics The graphics context of the print device.
  */
public void
print(Graphics graphics)
{
  paint(graphics);
}

/*************************************************************************/

/**
  * Prints this component, including all sub-components.  This method is
  * provided so that printing can be done in a different manner from
  * painting.  However, the implementation in this class simply calls
  * the <code>paintAll()</code> method.
  *
  * @param graphics The graphics context of the print device.
  */
public void
printAll(Graphics graphics)
{
  paintAll(graphics);
}

/*************************************************************************/

/**
  * Prints a listing of this component to the standard output.
  */
public void
list()
{
  System.out.println(toString());
}

/*************************************************************************/

/**
  * Prints a listing of this component to the specified print stream.
  *
  * @param stream The <code>PrintStream</code> to print to.
  */
public void
list(PrintStream stream)
{
  stream.println(toString());
}

/*************************************************************************/

/**
  * Prints a listing of this component to the specified print stream,
  * starting at the specified indentation point.
  *
  * @param stream The <code>PrintStream</code> to print to.
  * @param indent The indentation point.
  */
public void
list(PrintStream stream, int indent)
{
  for(int i = 0; i < indent; i++)
    stream.print(" ");

  stream.println(toString());
}

/*************************************************************************/

/**
  * Prints a listing of this component to the specified print writer.
  *
  * @param writer The <code>PrintWrinter</code> to print to.
  */
public void
list(PrintWriter writer)
{
  writer.println(toString());
}

/*************************************************************************/

/**
  * Prints a listing of this component to the specified print writer,
  * starting at the specified indentation point.
  *
  * @param writer The <code>PrintWriter</code> to print to.
  * @param indent The indentation point.
  */
public void
list(PrintWriter writer, int indent)
{
  for(int i = 0; i < indent; i++)
    writer.print(" ");

  writer.println(toString());
}

/*************************************************************************/

/**
  * Returns a debugging string representing this component.
  *
  * @return A string representing this component.
  */
protected String
paramString()
{
  return(toString());
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

} // class Component

