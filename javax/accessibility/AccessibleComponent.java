/* AccessibleComponent.java -- Java interface for aiding in accessibly rendering 
   other Java components
   Copyright (C) 2000 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.
 
GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */

package javax.accessibility;

import java.awt.event.FocusListener;
import java.awt.Point;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Cursor;

/**
 * Objects which are to be rendered to a screen as part of a graphical
 * user interface should implement this interface.  Accessibility 
 * software can use the implementations of this interface to determine 
 * and set the screen representation for an object.
 * <p>
 * The <code>AccessibleContext.getAccessibleComponent()</code> method
 * should return <code>null</code> if an object does not implement this
 * interface.
 *
 * @see AccessibleContext#getAccessibleComponent() 
 */
public abstract interface AccessibleComponent {

    /**
     * Adds the specified listener to this component.
     *
     * @param listener the listener to add to this component
     */
    public abstract void addFocusListener(FocusListener listener);

    /**
     * Tests whether or not the specified point is contained within
     * this component.  The coordinates are specified relative to this
     * component's coordinate system.
     *
     * @param point the <code>Point</code> specifying the location within 
     * this component
     *
     * @return <code>true</code> if the point is within this component, 
     * <code>false</code> otherwise
     */
    public abstract boolean contains(Point point);

    /**
     * If a object exists which is a child of this parent component at
     * the specified point which implements the Accessible interface then
     * that object is returned.
     *
     * @param point the <code>Point</code> specifying the location within
     * this component
     *
     * @return <code>null</code> if no <code>Accessible</code> child exists
     * at the given point 
     */
    public abstract Accessible getAccessibleAt(Point);

    /**
     * Get the background color of this component.
     * 
     * @return the background color of this component if supported, 
     * else <code>null</code>
     */
    public abstract Color getBackground();

    /**
     * Get the bounds of this component relative to its parent.
     * 
     * @return the <code>Rectangle</code> representing the bounds of this component
     * if it is visible on the screen, else <code>null</code>
     */
    public abstract Rectangle getBounds();

    /**
     * Get the cursor of this component.
     * 
     * @return the <code>Cursor</code> representing the mouse cursor if supported,
     * else <code>null</code>
     */
    public abstract Cursor getCursor();

    /**
     * Get the font of this component
     * 
     * @return the <code>Font</code> of the component if supported, 
     * else <code>null</code>
     */
    public abstract Font getFont();

    /**
     * Get the <code>FontMetrics</code> of the specified font in this component.
     *
     * @param font the specified font
     * @return the <code>FontMetrics</code> for the specified font if supported,
     * else <code>null</code>
     */
    public abstract FontMetrics getFontMetrics(Font font);

    /**
     * Get the foreground color of this component
     * 
     * @return the foreground color of this component if supported, 
     * else <code>null</code>
     */
    public abstract Color getForeground();

    /**
     * Get the location of this component in the screen's coordinate system.
     * The point specified is the top-left corner of this component.
     * <p>
     * <i>FIXME - Sun indicates this location is relative to the parent, but in the
     * screen's coordinate space I am not sure how this is possible yet.  What
     * makes the most sense is that this location is truly relative to the
     * parent and the coordinates are in the parent's coordinate system.</i>
     */
    public abstract Point getLocation();

    /**
     * Get the location of this component in the screen's coordinate space.
     * The point specified is the top-left corner of this component.
     */
    public abstract Point getLocationOnScreen();

    /**
     * Get the size of this component.
     *
     * @return the <code>Dimension</code> giving the height and width of this
     * component, <code>null</code> if the component is not on the screen 
     */
    public abstract Dimension getSize();

    /**
     * Indicates whether or not this component is enabled.
     * 
     * @return <code>true</code> if the component is enabled, 
     * else <code>false</code>
     *
     * @see AccessibleContext#getAccessibleStateSet()
     * @see AccessibleState#ENABLED
     */
    public abstract boolean isEnabled();

    /**
     * Indicates whether or not this component can accept focus.
     * 
     * @return <code>true</code> if the component accepts focus,
     * else <code>false</code>
     *
     * @see AccessibleContext#getAccessibleStateSet()
     * @see AccessibleState#FOCUSABLE
     * @see AccessibleState#FOCUSED
     */
    public abstract boolean isFocusTraversable();

    /**
     * Indicates whether or not this component is visible by checking 
     * the visibility of this component and its ancestors.  
     * <p>
     * The component may be hidden on screen by another component like
     * pop-up help.
     * 
     * @return <code>true</code> if component and ancestors are visible, 
     * else <code>false</code>
     *
     * @see AccessibleContext#getAccessibleStateSet()
     * @see AccessibleState#SHOWING
     */
    public abstract boolean isShowing();

    /**
     * Indicates whether or not this component is visible or intends to be
     * visible although one of its ancestors may not be.
     * 
     * @return <code>true</code> if the component is visible, 
     * else <code>false</code>
     *
     * @see AccessibleContext#getAccessibleStateSet()
     * @see AccessibleState#VISIBLE
     */
    public abstract boolean isVisible();

    /**
     * Removes the specified listener from this component.
     *
     * @param listener the listener to remove
     */
    public abstract void removeFocusListener(FocusListener listener);

    /**
     * If this method is called this component will attempt to gain focus,
     * but if it cannot accept focus nothing happens.
     */
    public abstract void requestFocus();

    /**
     * Set the background color of this component to the specified color.
     * 
     * @param color the color to set the background to
     */
    public abstract void setBackground(Color color);

    /**
     * Set the bounds of this component to the specified height and width.
     * 
     * @param rectangle the object representing the height, width, and location
     * of this component relative to its parent.
     */
    public abstract void setBounds(Rectangle rectangle);

    /**
     * Set the cursor of the component.
     * 
     * @param cursor the graphical representation of the cursor to use
     */
    public abstract void setCursor(Cursor cursor);

    /**
     * Set this component to an enabled or disabled state.
     *
     * @param b if <code>true</code> enable the component, else disable it
     * 
     * @see #isEnabled()
     */
    public abstract void setEnabled(boolean b);

    /**
     * Set the font of this component.
     * 
     * @param font the font to use
     */
    public abstract void setFont(Font font);

    /**
     * Set the foreground color of this component.
     *
     * @param color the color to set the foreground to
     */
    public abstract void setForeground(Color color);

    /**
     * Set the location of this component relative to its parent.  The point
     * specified represents the top-left corner of this component.
     *
     * @param point the top-left corner of this component relative to the parent
     */
    public abstract void setLocation(Point point);

    /**
     * Set the size of this component to the given dimensions.
     *
     * @param dimension the new size of the component
     * 
     * @see #getSize()
     */
    public abstract void setSize(Dimension dimension);

    /**
     * Set the visible state of this component.  
     *
     * @param b if <code>true</code> make the component visible, else make
     * it invisible 
     * 
     * @see #isVisible()
     */
    public abstract void setVisible(boolean b);
}
