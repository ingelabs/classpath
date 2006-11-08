/* TableView.java -- A table view for HTML tables
   Copyright (C) 2006 Free Software Foundation, Inc.

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


package javax.swing.text.html;

import gnu.javax.swing.text.html.css.Length;

import javax.swing.SizeRequirements;
import javax.swing.text.AttributeSet;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * A view implementation that renders HTML tables.
 *
 * This is basically a vertical BoxView that contains the rows of the table
 * and the rows are horizontal BoxViews that contain the actual columns.
 */
class TableView
  extends BoxView
  implements ViewFactory
{

  /**
   * Represents a single table row.
   */
  class RowView
    extends BoxView
  {
    RowView(Element el)
    {
      super(el, X_AXIS);
    }

    /**
     * Calculates the overall size requirements for the row along the
     * major axis. This will be the sum of the column requirements.
     */
    protected SizeRequirements calculateMajorAxisRequirements(int axis,
                                                            SizeRequirements r)
    {
      if (r == null)
        r = new SizeRequirements();
      r.minimum = totalColumnRequirements.minimum;
      r.preferred = totalColumnRequirements.preferred;
      r.maximum = totalColumnRequirements.maximum;
      r.alignment = 0.0F;
      return r;
    }

    /**
     * Lays out the columns in this row.
     */
    protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets,
                                   int spans[])
    {
      int numCols = offsets.length;
      int realColumn = 0;
      for (int i = 0; i < numCols; i++)
        {
          View v = getView(i);
          if (v instanceof CellView)
            {
              CellView cv = (CellView) v;
              for (int j = 0; j < cv.colSpan; j++, realColumn++)
                {
                  offsets[i] = columnOffsets[realColumn];
                  spans[i] = columnSpans[realColumn];
                }
            }
        }
    }
  }

  /**
   * A view that renders HTML table cells (TD and TH tags).
   */
  class CellView
    extends BoxView
  {

    /**
     * The number of columns that this view spans.
     */
    int colSpan;

    /**
     * Creates a new CellView for the specified element.
     *
     * @param el the element for which to create the colspan
     */
    CellView(Element el)
    {
      super(el, Y_AXIS);
      colSpan = 1;
      AttributeSet atts = getAttributes();
      Object o = atts.getAttribute(HTML.Attribute.COLSPAN);
      if (o != null)
        {
          try
            {
              colSpan = Integer.parseInt(o.toString());
            }
          catch (NumberFormatException ex)
            {
              // Couldn't parse the colspan, assume 1.
              colSpan = 1;
            }
        }
    }
  }

  /**
   * The attributes of this view.
   */
  private AttributeSet attributes;

  /**
   * The column requirements.
   */
  private SizeRequirements[] columnRequirements;

  /**
   * The overall requirements across all columns.
   *
   * Package private to avoid accessor methods.
   */
  SizeRequirements totalColumnRequirements;

  /**
   * The column layout, offsets.
   *
   * Package private to avoid accessor methods.
   */
  int[] columnOffsets;

  /**
   * The column layout, spans.
   *
   * Package private to avoid accessor methods.
   */
  int[] columnSpans;

  /**
   * Indicates if the grid setup is ok.
   */
  private boolean gridValid;

  /**
   * Creates a new HTML table view for the specified element.
   *
   * @param el the element for the table view
   */
  public TableView(Element el)
  {
    super(el, Y_AXIS);
    totalColumnRequirements = new SizeRequirements();
  }

  /**
   * Implementation of the ViewFactory interface for creating the
   * child views correctly.
   */
  public View create(Element elem)
  {
    View view = null;
    AttributeSet atts = elem.getAttributes();
    Object name = atts.getAttribute(StyleConstants.NameAttribute);
    if (name instanceof HTML.Tag)
      {
        HTML.Tag tag = (HTML.Tag) name;
        if (tag == HTML.Tag.TR)
          view = new RowView(elem);
        else if (tag == HTML.Tag.TD || tag == HTML.Tag.TH)
          view = new CellView(elem);
        else if (tag == HTML.Tag.CAPTION)
          view = new ParagraphView(elem);
      }

    // If we haven't mapped the element, then fall back to the standard
    // view factory.
    if (view == null)
      {
        View parent = getParent();
        if (parent != null)
          {
            ViewFactory vf = parent.getViewFactory();
            if (vf != null)
              view = vf.create(elem);
          }
      }
    return view;
  }

  /**
   * Returns this object as view factory so that we get our TR, TD, TH
   * and CAPTION subelements created correctly.
   */
  public ViewFactory getViewFactory()
  {
    return this;
  }

  /**
   * Returns the attributes of this view. This is overridden to provide
   * the attributes merged with the CSS stuff.
   */
  public AttributeSet getAttributes()
  {
    if (attributes == null)
      attributes = getStyleSheet().getViewAttributes(this);
    return attributes;
  }

  /**
   * Returns the stylesheet associated with this view.
   *
   * @return the stylesheet associated with this view
   */
  private StyleSheet getStyleSheet()
  {
    HTMLDocument doc = (HTMLDocument) getDocument();
    return doc.getStyleSheet();
  }

  /**
   * Overridden to calculate the size requirements according to the
   * columns distribution.
   */
  protected SizeRequirements calculateMinorAxisRequirements(int axis,
                                                            SizeRequirements r)
  {
    updateGrid();
    calculateColumnRequirements();
    if (r == null)
      r = new SizeRequirements();
    // The overall minor axis requirements is the sum of all the columns.
    int numCols = columnRequirements.length;
    long min = 0;
    long pref = 0;
    for (int i = 0; i < numCols; i++)
      {
        SizeRequirements col = columnRequirements[i];
        min += col.minimum;
        pref += col.preferred;
      }

    r.minimum = (int) min;
    r.preferred = (int) pref;
    r.maximum = r.preferred;

    // Try to set the CSS width if it fits.
    AttributeSet atts = getAttributes();
    Length l = (Length) atts.getAttribute(CSS.Attribute.WIDTH);
    if (l != null)
      {
        int width = (int) l.getValue();
        if (r.minimum < width)
          r.minimum = width;
      }

    // Apply the alignment.
    Object o = atts.getAttribute(CSS.Attribute.TEXT_ALIGN);
    r.alignment = 0.0F;
    if (o != null)
      {
        String al = o.toString();
        if (al.equals("left"))
          r.alignment = 0.0F;
        else if (al.equals("center"))
          r.alignment = 0.5F;
        else if (al.equals("right"))
          r.alignment = 1.0F;
      }

    totalColumnRequirements.minimum = r.minimum;
    totalColumnRequirements.preferred = r.preferred;
    totalColumnRequirements.maximum = r.maximum;

    return r;
  }

  protected void layoutMinorAxis(int targetSpan, int axis, int[] offsets, 
                                 int[] spans)
  {
    updateGrid();

    // Mark all the rows invalid.
    int count = getViewCount();
    for (int i = 0; i < count; i++)
      ((RowView) getView(i)).layoutChanged(axis);
    // Layout columns.
    layoutColumns(targetSpan);
 
    super.layoutMinorAxis(targetSpan, axis, offsets, spans);
  }

  private void calculateColumnRequirements()
  {
    int numRows = getViewCount();
    for (int r = 0; r < numRows; r++)
      {
        RowView rowView = (RowView) getView(r);
        int numCols = rowView.getViewCount();
        for (int c = 0; c < numCols; )
          {
            // TODO: Handle column span > 0.
            View v = rowView.getView(c);
            if (v instanceof CellView)
              {
                CellView cellView = (CellView) v;
                int colSpan = cellView.colSpan;
                if (colSpan > 1)
                  {
                    int cellMin = (int) cellView.getMinimumSpan(X_AXIS);
                    int cellPref = (int) cellView.getPreferredSpan(X_AXIS);
                    int cellMax = (int) cellView.getMaximumSpan(X_AXIS);
                    int currentMin = 0;
                    int currentPref = 0;
                    long currentMax = 0;
                    for (int i = 0; i < colSpan; i++)
                      {
                        SizeRequirements req = columnRequirements[c + i];
                        currentMin += req.minimum;
                        currentPref += req.preferred;
                        currentMax += req.maximum;
                      }
                    int deltaMin = cellMin - currentMin;
                    int deltaPref = cellPref - currentPref;
                    int deltaMax = (int) (cellMax - currentMax);
                    // Distribute delta.
                    for (int i = 0; i < colSpan; i++)
                      {
                        SizeRequirements req = columnRequirements[c + i];
                        if (deltaMin > 0)
                          req.minimum += deltaMin / colSpan;
                        if (deltaPref > 0)
                          req.preferred += deltaPref / colSpan;
                        if (deltaMax > 0)
                          req.maximum += deltaMax / colSpan;
                      }
                  }
                else
                  {
                    // Shortcut for colSpan == 1.
                    SizeRequirements req = columnRequirements[c];
                    req.minimum = Math.max(req.minimum,
                                        (int) cellView.getMinimumSpan(X_AXIS));
                    req.preferred = Math.max(req.preferred,
                                      (int) cellView.getPreferredSpan(X_AXIS));
                    req.maximum = Math.max(req.maximum,
                                        (int) cellView.getMaximumSpan(X_AXIS));
                  }
                c += colSpan;
              }
          }
      }
  }

  private void layoutColumns(int targetSpan)
  {
    // TODO: Could be done better I suppose.
    SizeRequirements.calculateTiledPositions(targetSpan,
                                             totalColumnRequirements,
                                             columnRequirements, columnOffsets,
                                             columnSpans);
  }

  private void updateGrid()
  {
    if (! gridValid)
      {
        int maxColumns = 0;
        int numRows = getViewCount();
        for (int r = 0; r < numRows; r++)
          {
            RowView rowView = (RowView) getView(r);
            int numCols = rowView.getViewCount();
            maxColumns = Math.max(numCols, maxColumns);
          }
        columnRequirements = new SizeRequirements[maxColumns];
        for (int i = 0; i < maxColumns; i++)
          columnRequirements[i] = new SizeRequirements();
        columnOffsets = new int[maxColumns];
        columnSpans = new int[maxColumns];

        gridValid = true;
      }
  }

  protected SizeRequirements calculateMajorAxisRequirements(int axis,
                                                            SizeRequirements r)
  {
    r = super.calculateMajorAxisRequirements(axis, r);
    r.maximum = r.preferred;
    return r;
  }
}
