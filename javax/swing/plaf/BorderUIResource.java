/*************************************************************************
 * BorderUIResource.java
 *
 * Copyright (c) 1999 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package javax.swing.plaf;
import javax.swing.border.*;
import javax.swing.Icon;
import java.io.Serializable;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Font;

/**
 * @serial
 * @serialField delegate Border the <code>Border</code> wrapped
 * @author Brian Jones
 */
public class BorderUIResource 
    extends Object 
    implements Border, UIResource, Serializable
{
    private Border delegate;

    /**
     * Creates a <code>UIResource</code> wrapper for a <code>Border</code>
     * object.
     * 
     * @param delegate the border to be wrapped
     */
    public BorderUIResource(Border delegate)
    {
	this.delegate = delegate;
    }

    /**
     */
    public static Border getEtchedBorderUIResource() { }

    /**
     */
    public static Border getLoweredBevelBorderUIResource() { }

    /**
     */
    public static Border getRaisedBevelBorderUIResource() { }

    /**
     */
    public static Border getBlackLineBorderUIResource() { }

    /**
     */
    public void paintBorder(Component c, Graphics g, int x, int y, 
			    int width, int height) { }

    /**
     */
    public Insets getBorderInsets(Component c) { }

    /**
     */
    public boolean isBorderOpaque() { }

    /**
     * @serial
     */
    public static class BevelBorderUIResource 
	extends BevelBorder
	implements UIResource, Serializable
    {
	public BevelBorderUIResource(int bevelType) 
	{ 

	}

	public BevelBorderUIResource(int bevelType, 
				     Color highlight, 
				     Color shadow) 
	{
	    this();
	}
	public BevelBorderUIResource(int bevelType,
				     Color highlightOuter,
				     Color highlightInner,
				     Color shadowOuter,
				     Color shadowInner) 
	{
	    this(bevelType);
	}
    }

    /**
     * @serial
     */
    public static class CompoundBorderUIResource
	extends CompoundBorder
	implements UIResource, Serializable
    {
	public CompoundBorderUIResource(Border outsideBorder,
					Border insideBorder)
	{

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

	}
	public EtchedBorderUIResource(Color highlight, Color shadow)
	{

	}
	public EtchedBorderUIResource(int etchType, Color highlight, 
				      Color shadow)
	{

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
	    
	}
	public LineBorderUIResource(Color color,
				    int thickness)
	{

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

	}
	public MatteBorderUIResource(int top, int left, int bottom,
				     int right, Icon tileIcon)
	{

	}
	public MatteBorderUIResource(Icon tileIcon)
	{

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

	}
	TitledBorderUIResource(Border border)
	{

	}
	TitledBorderUIResource(Border border, String title)
	{

	}
	TitledBorderUIResource(Border border, String title,
			       int titleJustification, int titlePosition)
	{

	}
	TitledBorderUIResource(Border border, String title,
			       int titleJustification, int titlePosition,
			       Font titleFont)
	{

	}
	TitledBorderUIResource(Border border, String title,
			       int titleJustification, int titlePosition,
			       Font titleFont, Color titleColor)
	{

	}
    }
}

