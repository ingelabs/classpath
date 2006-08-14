/* MetalSplitPaneDivider.java
Copyright (C) 2005, 2006, Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

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

package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

/**
 * The divider that is used by the {@link MetalSplitPaneUI}.
 *
 * @author Roman Kennke (roman@kennke.org)
 */
class MetalSplitPaneDivider extends BasicSplitPaneDivider
{
  /**
   * The button pixel data, as indices into the colors array below.
   */
  static final byte[][] BUTTON_SPRITE = {{ 0, 0, 0, 2, 2, 0, 0, 0, 0 },
                                         { 0, 0, 2, 1, 1, 1, 0, 0, 0 },
                                         { 0, 2, 1, 1, 1, 1, 1, 0, 0 },
                                         { 2, 1, 1, 1, 1, 1, 1, 1, 0 },
                                         { 0, 3, 3, 3, 3, 3, 3, 3, 3 }};

  private class MetalOneTouchButton
    extends JButton
  {
    /**
     * Denotes a left button.
     */
    static final int LEFT = 0;

    /**
     * Denotes a right button.
     */
    static final int RIGHT = 1;

    /**
     * The colors for the button sprite.
     */
    private Color[] colors;

    /**
     * Either LEFT or RIGHT.
     */
    private int direction;

    /**
     * Creates a new instance.
     *
     * @param dir either LEFT or RIGHT
     */
    MetalOneTouchButton(int dir)
    {
      direction = dir;
      colors = new Color[4];
    }

    /**
     * Never allow borders.
     */
    public void setBorder(Border b)
    {
    }

    /**
     * Never allow focus traversal.
     */
    public boolean isFocusTraversable()
    {
      return false;
    }

    /**
     * Paints the one touch button.
     */
    public void paint(Graphics g)
    {
      if (splitPane != null)
        {
          // Update colors here to reflect dynamic changes to the theme.
          colors[0] = getBackground();
          colors[1] = MetalLookAndFeel.getPrimaryControlDarkShadow();
          colors[2] = MetalLookAndFeel.getPrimaryControlInfo();
          colors[3] = MetalLookAndFeel.getPrimaryControlHighlight();

          // Fill background.
          g.setColor(getBackground());
          g.fillRect(0, 0, getWidth(), getHeight());

          // Pressed buttons have slightly different color mapping.
          if (getModel().isPressed())
            colors[1] = colors[2];

          if (direction == LEFT)
            {
              if (orientation == JSplitPane.VERTICAL_SPLIT)
                {
                  // Draw the sprite as it is.
                  for (int y = 0; y < BUTTON_SPRITE.length; y++)
                    {
                      byte[] line = BUTTON_SPRITE[y];
                      for (int x = 0; x < line.length; x++)
                        {
                          int c = line[x];
                          if (c != 0)
                            {
                              g.setColor(colors[c]);
                              g.fillRect(x, y, 1, 1);
                            }
                        }
                    }
                }
              else
                {
                  // Draw the sprite with swapped X and Y axis.
                  for (int y = 0; y < BUTTON_SPRITE.length; y++)
                    {
                      byte[] line = BUTTON_SPRITE[y];
                      for (int x = 0; x < line.length; x++)
                        {
                          int c = line[x];
                          if (c != 0)
                            {
                              g.setColor(colors[c]);
                              g.fillRect(y, x, 1, 1);
                            }
                        }
                    }
                }
            }
          else
            {
              if (orientation == JSplitPane.VERTICAL_SPLIT)
                {
                  // Draw sprite mirrored.
                  int ySize = BUTTON_SPRITE.length;
                  for (int y = 0; y < ySize; y++)
                    {
                      byte[] line = BUTTON_SPRITE[y];
                      int xSize = line.length;
                      for (int x = 0; x < xSize; x++)
                        {
                          int c = line[x];
                          if (c != 0)
                            {
                              g.setColor(colors[c]);
                              g.fillRect(xSize - x - 1, ySize - y - 1, 1, 1);
                            }
                        }
                    }
                }
              else
                {
                  // Draw sprite mirrored and X-Y-swapped.
                  int ySize = BUTTON_SPRITE.length;
                  for (int y = 0; y < ySize; y++)
                    {
                      byte[] line = BUTTON_SPRITE[y];
                      int xSize = line.length;
                      for (int x = 0; x < xSize; x++)
                        {
                          int c = line[x];
                          if (c != 0)
                            {
                              g.setColor(colors[c]);
                              g.fillRect(ySize - y - 1, xSize - x - 1, 1, 1);
                            }
                        }
                    }
                }
            }
        }
    }
  }

  /** The dark color in the pattern. */
  Color dark;

  /** The light color in the pattern. */
  Color light;
  
  /** The JSplitPane the divider is on. */
  JSplitPane splitPane;

  /** The split pane orientation. */
  int orientation;
  
  /**
   * Creates a new instance of <code>MetalSplitPaneDivider</code>.
   *
   * @param ui the <code>MetalSplitPaneUI</code> that uses this divider
   */
  public MetalSplitPaneDivider(MetalSplitPaneUI ui, Color light, Color dark)
  {
    super(ui);
    this.splitPane = super.splitPane;
    this.orientation = super.orientation;
    this.light = light;
    this.dark = dark;
  }

  /**
   * Paints the divider.
   *
   * @param g the <code>Graphics</code> context to use for painting
   */
  public void paint(Graphics g)
  {
    Dimension s = getSize();

    if (splitPane.hasFocus())
      {
        g.setColor(UIManager.getColor("SplitPane.dividerFocusColor"));
        g.fillRect(0, 0, s.width, s.height);
      }
    
    // Paint border if one exists.
    Border border = getBorder();
    if (border != null)
      border.paintBorder(this, g, 0, 0, s.width, s.height);

    MetalUtils.fillMetalPattern(splitPane, g, 2, 2, s.width - 4, s.height - 4,
                                light, dark);
    super.paint(g);
  }

  protected JButton createLeftOneTouchButton()
  {
    JButton b = new MetalOneTouchButton(MetalOneTouchButton.LEFT);
    b.setMinimumSize(new Dimension(ONE_TOUCH_SIZE, ONE_TOUCH_SIZE));
    b.setRequestFocusEnabled(false);
    return b;
  }

  protected JButton createRightOneTouchButton()
  {
    JButton b = new MetalOneTouchButton(MetalOneTouchButton.RIGHT);
    b.setMinimumSize(new Dimension(ONE_TOUCH_SIZE, ONE_TOUCH_SIZE));
    b.setRequestFocusEnabled(false);
    return b;
  }
}
