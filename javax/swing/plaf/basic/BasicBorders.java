/* BasicBorders.java
   Copyright (C) 2003 Free Software Foundation, Inc.

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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;

import java.io.Serializable;

import javax.swing.AbstractButton;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;


/**
 * STUBBED (except MarginBorder)
 */
public class BasicBorders
{
  public static class ButtonBorder
  {
  } // class ButtonBorder
  public static class FieldBorder
  {
    public FieldBorder(Color shadow, Color darkShadow,
                       Color highlight, Color lightHighlight)
    {
    }
  } // class FieldBorder


  /**
   * An invisible, but spacing border whose margin is determined
   * by calling the <code>getMargin()</code> method of the enclosed
   * component.  If the enclosed component has no such method,
   * this border will not occupy any space.
   *
   * <p><img src="BasicBorders.MarginBorder-1.png" width="325"
   * height="200" alt="[An illustration that shows how MarginBorder
   * determines its borders]" />
   *
   * @author Sascha Brawer (brawer@dandelis.ch)
   */
  public static class MarginBorder
    extends AbstractBorder
    implements Serializable, UIResource
  {
    /**
     * Determined using the <code>serialver</code> tool
     * of Apple/Sun JDK 1.3.1 on MacOS X 10.1.5.
     */
    static final long serialVersionUID = -3035848353448896090L;
    
    
    /**
     * Constructs a new MarginBorder.
     */
    public MarginBorder()
    {
    }
    
    
    /**
     * Measures the width of this border.
     *
     * @param c the component whose border is to be measured.
     *
     * @return an Insets object whose <code>left</code>, <code>right</code>,
     *         <code>top</code> and <code>bottom</code> fields indicate the
     *         width of the border at the respective edge.
     *
     * @see #getBorderInsets(java.awt.Component, java.awt.Insets)
     */
    public Insets getBorderInsets(Component c)
    {
      return getBorderInsets(c, new Insets(0, 0, 0, 0));
    }
    
    
    /**
     * Determines the insets of this border by calling the
     * <code>getMargin()</code> method of the enclosed component.  The
     * resulting margin will be stored into the the <code>left</code>,
     * <code>right</code>, <code>top</code> and <code>bottom</code>
     * fields of the passed <code>insets</code> parameter.
     *
     * <p>Unfortunately, <code>getMargin()</code> is not a method of
     * {@link javax.swing.JComponent} or some other common superclass
     * of things with margins. While reflection could be used to
     * determine the existence of this method, this would be slow on
     * many virtual machines. Therefore, the current implementation
     * knows about {@link javax.swing.AbstractButton#getMargin()},
     * {@link javax.swing.JPopupMenu#getMargin()}, and {@link
     * javax.swing.JToolBar#getMargin()}. If <code>c</code> is an
     * instance of a known class, the respective
     * <code>getMargin()</code> method is called to determine the
     * correct margin. Otherwise, a zero-width margin is returned.
     *
     * @param c the component whose border is to be measured.
     *
     * @return the same object that was passed for <code>insets</code>,
     *         but with changed fields.
     */
    public Insets getBorderInsets(Component c, Insets insets)
    {
      Insets margin = null;

      /* This is terrible object-oriented design. See the above Javadoc
       * for an excuse.
       */
      if (c instanceof AbstractButton)
        margin = ((AbstractButton) c).getMargin();
      else if (c instanceof JPopupMenu)
        margin = ((JPopupMenu) c).getMargin();
      else if (c instanceof JToolBar)
        margin = ((JToolBar) c).getMargin();

      if (margin == null)
        insets.top = insets.left = insets.bottom = insets.right = 0;
      else
      {
        insets.top = margin.top;
        insets.left = margin.left;
        insets.bottom = margin.bottom;
        insets.right = margin.right;
      }

      return insets;
    }
  }
  
  
  public static class MenuBarBorder
  {
    public MenuBarBorder(Color shadow, Color highlight)
    {
    }
  } // class MenuBarBorder
  public static class RadioButtonBorder
  {
  } // class RadioButtonBorder
  public static class RolloverButtonBorder
  {
  } // class RolloverButtonBorder
  public static class SplitPaneBorder
  {
    public SplitPaneBorder(Color highlight, Color shadow)
    {
    }
  } // class SplitPaneBorder
  public static class ToggleButtonBorder
  {
  } // class ToggleButtonBorder
} // class BasicBorders
