/* BorderUIResource.java
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


package javax.swing.plaf;

import javax.swing.border.*;
import javax.swing.Icon;
import java.io.Serializable;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;

/**
 * A wrapper for {@link javax.swing.border.Border} that also
 * implements the {@link UIResource} marker interface.  This is useful
 * for implementing pluggable look-and-feels: When switching the
 * current LookAndFeel, only those borders are replaced that are
 * marked as {@link UIResource}.  For this reason, a look-and-feel
 * should always install borders that implement
 * <code>UIResource</code>, such as the borders provided by this
 * class.
 *
 * @serial
 * @serialField delegate Border the <code>Border</code> wrapped
 *
 * @author Brian Jones (cbj@gnu.org)
 * @author Sascha Brawer (brawer@dandelis.ch)
 */
public class BorderUIResource 
  extends Object 
  implements Border, UIResource, Serializable
{
  /**
   * Verified using the <code>serialver</code> tool
   * of Apple/Sun JDK 1.3.1 on MacOS X 10.1.5.
   */
  static final long serialVersionUID = -3440553684010079691L;


  /**
   * A shared instance of an {@link EtchedBorderUIResource}, or
   * <code>null</code> if the {@link #getEtchedBorderUIResource()}
   * method has not yet been called.
   */
  private static Border etchedBorderUIResource;


  /**
   * A shared instance of a {@link BevelBorderUIResource} whose
   * <code>bevelType</code> is {@link
   * javax.swing.border.BevelBorder#LOWERED}, or <code>null</code> if
   * the {@link #getLoweredBevelBorderUIResource()} has not yet been
   * called.
   */
  private static Border loweredBevelBorderUIResource;
  
  
  /**
   * A shared instance of a {@link BevelBorderUIResource} whose
   * <code>bevelType</code> is {@link
   * javax.swing.border.BevelBorder#RAISED}, or <code>null</code> if
   * the {@link #getRaisedBevelBorderUIResource()} has not yet been
   * called.
   */
  private static Border raisedBevelBorderUIResource;
  
  
  /**
   * A shared instance of a {@link LineBorderUIResource} for
   * a one-pixel thick black line, or <code>null</code> if
   * the {@link #getBlackLineBorderUIResource()} has not yet been
   * called.
   */
  private static Border blackLineBorderUIResource;


  /**
   * Returns a shared instance of an etched border which also
   * is marked as an {@link UIResource}.
   *
   * @see javax.swing.border.EtchedBorder
   */
  public static Border getEtchedBorderUIResource()
  {
    /* Swing is not designed to be thread-safe, so there is no
     * need to synchronize the access to the global variable.
     */
    if (etchedBorderUIResource == null)
      etchedBorderUIResource = new EtchedBorderUIResource();
    return etchedBorderUIResource;
  }
  

  /**
   * Returns a shared instance of {@link BevelBorderUIResource} whose
   * <code>bevelType</code> is {@link
   * javax.swing.border.BevelBorder#LOWERED}.
   *
   * @see javax.swing.border.BevelBorder
   */
  public static Border getLoweredBevelBorderUIResource()
  {
    /* Swing is not designed to be thread-safe, so there is no
     * need to synchronize the access to the global variable.
     */
    if (loweredBevelBorderUIResource == null)
      loweredBevelBorderUIResource = new BevelBorderUIResource(
        BevelBorder.LOWERED);
    return loweredBevelBorderUIResource;
  }


  /**
   * Returns a shared instance of {@link BevelBorderUIResource} whose
   * <code>bevelType</code> is {@link
   * javax.swing.border.BevelBorder#RAISED}.
   *
   * @see javax.swing.border.BevelBorder
   */
  public static Border getRaisedBevelBorderUIResource()
  {
    /* Swing is not designed to be thread-safe, so there is no
     * need to synchronize the access to the global variable.
     */
    if (raisedBevelBorderUIResource == null)
      raisedBevelBorderUIResource = new BevelBorderUIResource(
        BevelBorder.RAISED);
    return raisedBevelBorderUIResource;
  }
  
  
  /**
   * Returns a shared instance of {@link LineBorderUIResource} for
   * a black, one-pixel width border.
   *
   * @see javax.swing.border.LineBorder
   */
  public static Border getBlackLineBorderUIResource()
  {
    /* Swing is not designed to be thread-safe, so there is no
     * need to synchronize the access to the global variable.
     */
    if (blackLineBorderUIResource == null)
      blackLineBorderUIResource = new LineBorderUIResource(Color.black);
    return blackLineBorderUIResource;
  }


  /**
   * The wrapped border.
   */
  private Border delegate;
  
  
  /**
   * Constructs a <code>BorderUIResource</code> for wrapping
   * a <code>Border</code> object.
   * 
   * @param delegate the border to be wrapped.
   */
  public BorderUIResource(Border delegate)
  {
    if (delegate == null)
      throw new IllegalArgumentException();
    
    this.delegate = delegate;
  }

  
  /**
   * Paints the border around an enclosed component by calling
   * the <code>paintBorder</code> method of the wrapped delegate.
   *
   * @param c the component whose border is to be painted.
   * @param g the graphics for painting.
   * @param x the horizontal position for painting the border.
   * @param y the vertical position for painting the border.
   * @param width the width of the available area for painting the border.
   * @param height the height of the available area for painting the border.
   */
  public void paintBorder(Component c, Graphics g,
                          int x, int y, int width, int height)
  {
    delegate.paintBorder(c, g, x, y, width, height);
  }
  
  
  /**
   * Measures the width of this border by calling the
   * <code>getBorderInsets</code> method of the wrapped
   * delegate.
   *
   * @param c the component whose border is to be measured.
   *
   * @return an Insets object whose <code>left</code>, <code>right</code>,
   *         <code>top</code> and <code>bottom</code> fields indicate the
   *         width of the border at the respective edge.
   */
  public Insets getBorderInsets(Component c)
  { 
    return delegate.getBorderInsets(c);
  }
  
  
  /**
   * Determines whether this border fills every pixel in its area
   * when painting by calling the <code>isBorderOpaque</code>
   * method of the wrapped delegate.
   *
   * @return <code>true</code> if the border is fully opaque, or
   *         <code>false</code> if some pixels of the background
   *         can shine through the border.
   */
  public boolean isBorderOpaque()
  { 
    return delegate.isBorderOpaque();
  }


  /**
   * A {@link javax.swing.border.BevelBorder} that also implements the
   * {@link UIResource} marker interface.  This is useful for
   * implementing pluggable look-and-feels: When switching the current
   * LookAndFeel, only those borders are replaced that are marked as
   * {@link UIResource}.  For this reason, a look-and-feel should
   * always install borders that implement <code>UIResource</code>,
   * such as the borders provided by this class.
   *
   * @author Brian Jones (cbj@gnu.org)
   * @author Sascha Brawer (brawer@dandelis.ch)
   */
  public static class BevelBorderUIResource 
    extends BevelBorder
    implements UIResource, Serializable
  {
    /**
     * Constructs a BevelBorderUIResource whose colors will be derived
     * from the background of the enclosed component. The background
     * color is retrieved each time the border is painted, so a border
     * constructed by this method will automatically reflect a change
     * to the component&#x2019;s background color.
     *
     * <p><img src="../border/BevelBorder-1.png" width="500" height="150"
     * alt="[An illustration showing raised and lowered BevelBorders]" />
     *
     * @param bevelType the desired appearance of the border. The value
     *        must be either {@link javax.swing.border.BevelBorder#RAISED}
     *        or {@link javax.swing.border.BevelBorder#LOWERED}.
     *
     * @throws IllegalArgumentException if <code>bevelType</code> has
     *         an unsupported value.
     */
    public BevelBorderUIResource(int bevelType) 
    { 
      super(bevelType);
    }
    
    
    /**
     * Constructs a BevelBorderUIResource given its appearance type
     * and two colors for its highlight and shadow.
     *
     * <p><img src="../border/BevelBorder-2.png" width="500" height="150"
     * alt="[An illustration showing BevelBorders that were constructed
     * with this method]" />
     *
     * @param bevelType the desired appearance of the border. The value
     *        must be either {@link javax.swing.border.BevelBorder#RAISED}
     *        or {@link javax.swing.border.BevelBorder#LOWERED}.
     *
     * @param highlight the color that will be used for the inner side
     *        of the highlighted edges (top and left if if
     *        <code>bevelType</code> is {@link
     *        javax.swing.border.BevelBorder#RAISED}; bottom and right
     *        otherwise). The color for the outer side is a brightened
     *        version of this color.
     *
     * @param shadow the color that will be used for the outer side of
     *        the shadowed edges (bottom and right if
     *        <code>bevelType</code> is {@link
     *        javax.swing.border.BevelBorder#RAISED}; top and left
     *        otherwise). The color for the inner side is a brightened
     *        version of this color.
     *
     * @throws IllegalArgumentException if <code>bevelType</code> has
     *         an unsupported value.
     *
     * @throws NullPointerException if <code>highlight</code> or
     *         <code>shadow</code> is <code>null</code>.
     */
    public BevelBorderUIResource(int bevelType, 
                                 Color highlight, 
                                 Color shadow) 
    {
      super(bevelType, highlight, shadow);
    }


    /**
     * Constructs a BevelBorderUIResource given its appearance type
     * and all its colors.
     *
     * <p><img src="../border/BevelBorder-3.png" width="500" height="150"
     * alt="[An illustration showing BevelBorders that were constructed
     * with this method]" />
     *
     * @param bevelType the desired appearance of the border. The value
     *        must be either {@link javax.swing.border.BevelBorder#RAISED}
     *        or {@link javax.swing.border.BevelBorder#LOWERED}.
     *
     * @param highlightOuter the color that will be used for the outer
     *        side of the highlighted edges (top and left if
     *        <code>bevelType</code> is {@link
     *        javax.swing.border.BevelBorder#RAISED}; bottom and right
     *        otherwise).
     *
     * @param highlightInner the color that will be used for the inner
     *        side of the highlighted edges.
     *
     * @param shadowOuter the color that will be used for the outer
     *        side of the shadowed edges (bottom and right if
     *        <code>bevelType</code> is {@link
     *        javax.swing.border.BevelBorder#RAISED}; top and left
     *        otherwise).
     *
     * @param shadowInner the color that will be used for the inner
     *        side of the shadowed edges.
     *
     * @throws IllegalArgumentException if <code>bevelType</code> has
     *         an unsupported value.
     *
     * @throws NullPointerException if one of the passed colors
     *         is <code>null</code>.
     */
    public BevelBorderUIResource(int bevelType,
                                 Color highlightOuter,
                                 Color highlightInner,
                                 Color shadowOuter,
                                 Color shadowInner) 
    {
      super(bevelType,
            highlightOuter, highlightInner,
            shadowOuter, shadowInner);
    }
  }

    /**
     * @serial */
    public static class CompoundBorderUIResource
	extends CompoundBorder
	implements UIResource, Serializable
    {
	public CompoundBorderUIResource(Border outsideBorder,
					Border insideBorder)
	{
	  super (outsideBorder, insideBorder);
	}
    }

    /**
     * @serial
     */
    public static class EmptyBorderUIResource 
	extends EmptyBorder
	implements UIResource, Serializable
    {
	public EmptyBorderUIResource(int top, int left, int bottom, int right)
	{
	    this(new Insets(top,left,bottom,right));
	}
	
	public EmptyBorderUIResource(Insets insets)
	{
	  super (insets);
	}
    }

    /**
     * @serial
     */
    public static class EtchedBorderUIResource
	extends EtchedBorder
	implements UIResource, Serializable
    {
	public EtchedBorderUIResource() { }
	public EtchedBorderUIResource(int etchType) 
	{
	  super (etchType);
	}
	public EtchedBorderUIResource(Color highlight, Color shadow)
	{
	  super (highlight, shadow);
	}
	public EtchedBorderUIResource(int etchType, Color highlight, 
				      Color shadow)
	{
          super (etchType, highlight, shadow);
	}

    }

    /**
     * @serial
     */
    public static class LineBorderUIResource
	extends LineBorder
	implements UIResource, Serializable
    {
	public LineBorderUIResource(Color color)
	{
	   super (color); 
	}
	public LineBorderUIResource(Color color,
				    int thickness)
	{
	   super (color, thickness);
	}
    }

    /**
     * @serial
     */
    public static class MatteBorderUIResource
	extends MatteBorder
	implements UIResource, Serializable
    {
	public MatteBorderUIResource(int top, int left, int bottom, 
				     int right, Color color)
	{
          super (new Insets (top, left, bottom, right), color);
	}
	public MatteBorderUIResource(int top, int left, int bottom,
				     int right, Icon tileIcon)
	{
          super (new Insets (top, left, bottom, right), tileIcon);

	}
	public MatteBorderUIResource(Icon tileIcon)
	{
	  super (tileIcon);
	}
    }

    /**
     * @serial
     */
    public static class TitledBorderUIResource
	extends TitledBorder
	implements UIResource, Serializable
    {
	TitledBorderUIResource(String title)
	{
	  super (title);
	}
	TitledBorderUIResource(Border border)
	{
          super (border);
	}
	TitledBorderUIResource(Border border, String title)
	{
          super (border, title);
	}
	TitledBorderUIResource(Border border, String title,
			       int titleJustification, int titlePosition)
	{
          super (border, title, titleJustification, titlePosition);
	}
	TitledBorderUIResource(Border border, String title,
			       int titleJustification, int titlePosition,
			       Font titleFont)
	{
          super (border, title, titleJustification, titlePosition, titleFont);
	}
	TitledBorderUIResource(Border border, String title,
			       int titleJustification, int titlePosition,
			       Font titleFont, Color titleColor)
	{
          super (border, title, titleJustification, titlePosition, titleFont, titleColor);
	}
    }
}

