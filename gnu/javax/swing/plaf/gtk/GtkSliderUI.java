/*************************************************************************
 * GtkSliderUI.java
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

package gnu.javax.swing.plaf.gtk;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/**
 * This is probably the first UI class I'll work on and hopefully finish.
 * It has major problems right now with anything it is trying to draw.
 *
 * @author Brian Jones
 * @see javax.swing.LookAndFeel
 */
public class GtkSliderUI extends BasicSliderUI
{
    public GtkSliderUI() 
    {
	super(null);
    }

    public static ComponentUI createUI(JComponent c)
    {
	return new GtkSliderUI();
    }

    // methods not overridden here, using Basic defaults
    // installUI()
    // uninstall()

    private static final Dimension PREF_HORIZ = new Dimension(250, 15);
    private static final Dimension PREF_VERT = new Dimension(15, 250);
    private static final Dimension MIN_HORIZ = new Dimension(25, 15);
    private static final Dimension MIN_VERT = new Dimension(15, 25);

    public Dimension getPreferredHorizontalSize()
    {
	/*
	Dimension thumbSize = getThumbSize();
	Dimenstion labelSize = getLabelSize();
	// getTickLength()
	int width = thumbSize.width + 
	getWidthOfWidestLabel
	*/
	return PREF_HORIZ;
    }

    public Dimension getPreferredVerticalSize()
    {
	return PREF_VERT;
    }

    public Dimension getMinimumHorizontalSize()
    {
	return MIN_HORIZ;
    }

    public Dimension getMinimumVerticalSize()
    {
	return MIN_VERT;
    }

    /** 
     * Returns thumb size based on slider orientation
     */
    protected Dimension getThumbSize()
    {
	Dimension size = new Dimension();

	if (slider.getOrientation() == JSlider.VERTICAL) {
	    size.width = 15;
	    size.height = 33;
	}
	else {
	    size.width = 33;
	    size.height = 15;
	}
	return size;
    }

    /**
     * Reserved width or height for ticks, as appropriate to the slider
     * orientation.
     */
    protected int getTickLength()
    {
	return 10;
    }

    public void paintFocus(Graphics g)
    {
	super.paintFocus(g);
	System.err.println("focus " + focusRect);
    }

    public void paintLabels(Graphics g)
    {
	super.paintLabels(g);
	System.err.println("label " + labelRect);
    }

    public void paintThumb(Graphics g)
    {
	int x = thumbRect.x;
	int y = thumbRect.y;
	int h = thumbRect.height;
	int w = thumbRect.width;

	g.setColor(Color.blue);
	if (slider.getOrientation() == JSlider.HORIZONTAL)
	    g.drawRect(x, y, w, h);
	else 
	    g.drawRect(x, y, w, h);

	System.err.println("thumb " + thumbRect);
    }

    // public void paintTicks(Graphics g)
    
    public void paintTrack(Graphics g)
    {
	super.paintTrack(g);
//  	int x = trackRect.x;
//  	int y = trackRect.y;
//  	int h = trackRect.height;
//  	int w = trackRect.width;

//  	g.setColor(slider.getForeground());

//  	if (slider.getOrientation() == JSlider.HORIZONTAL)
//  	    g.drawLine(x, y+h-1, x+w-1, y+h-1);
//  	else
//  	    g.drawLine(x+w-1, y, x+w-1, y+h-1);

//  	System.err.println("track " + trackRect);
//  	System.err.println("content " + contentRect);
    }

    // the four methods below allow you to control tick painting without 
    // worrying about what paintTicks does, look for in other UI delegates
    // protected void paintMajorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x)
    // protected void paintMajorTickForVertSlider(Graphics g, Rectangle tickBounds, int y)
    // protected void paintMinorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x)
    // protected void paintMinorTickForVertSlider(Graphics g, Rectangle tickBounds, int y)
}
