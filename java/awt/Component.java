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
import java.util.Locale;

// FIXME: Java 1.0 event model unimplemented

// FIXME: Image Update

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
public static final float BOTTOM_ALIGNMENT = 1.0;

/**
  * Constant returned by the <code>getAlignmentY</code> and 
  * <code>getAlignmentX</code> methods to indicate
  * that the component wishes to be aligned to the center relative to
  * other components.
  */
public static final float CENTER_ALIGNMENT = 0.5;

/**
  * Constant returned by the <code>getAlignmentY</code> method to indicate
  * that the component wishes to be aligned to the top relative to
  * other components.
  */
public static final float TOP_ALIGNMENT = 0.0;

/**
  * Constant returned by the <code>getAlignmentX</code> method to indicate
  * that the component wishes to be aligned to the right relative to
  * other components.
  */
public static final float RIGHT_ALIGNMENT = 1.0;

/**
  * Constant returned by the <code>getAlignmentX</code> method to indicate
  * that the component wishes to be aligned to the left relative to
  * other components.
  */
public static final float LEFT_ALIGNMENT = 0.0;

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

// The toolkit for this componet
private Toolkit toolkit = Toolkit.getDefaultToolkit();

// The synchronization locking object for this component
private Object tree_lock = this;

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
  this.foreground_color = foreground_color);
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
  this.bounding_rectangle = bounding_rectangle;
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
  setBounds(new Rectangle(x, y, width, height));
  getPeer().getBounds(x, y, width, height);
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
  return(getPeer.()getColorModel);
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
  return(getBounds(point.x, point.y));
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
getComponentAt(int x, int y)
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
  getPeer().getCursor(cursor);
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
  getPeer().getFont(font);
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
  getPeer().getFontMetrics(font);
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
  return(getBounds.getLocation());
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
  getPeer().getLocationOnScreen();
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
  return(getBounds.getSize());
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
setSize(Dimensino dim)
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
resize(Dimensino dim)
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
{
  setSize(dim);
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
  * Makes this component visible or invisible.
  *
  * @param visible <code>true</code> to make this component visible,
  * </code>false</code> to make it invisible.
  */
public void
setVisible(boolean visible)
{
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
inside(int x, int y);
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
public deliverEvent(Event event)
{
  dispatchEvent(new AWTEvent(event));
}

/*************************************************************************/

/**
  * AWT 1.0 event dispatcher.
  *
  * @deprecated Deprecated in favor of <code>dispatchEvent()</code>.
  */
public PostEvent(Event event)
{
  dispatchEvent(new AWTEvent(event));
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

} // class Component

