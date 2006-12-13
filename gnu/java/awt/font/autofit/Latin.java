/* Latin.java -- Latin specific glyph handling
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


package gnu.java.awt.font.autofit;

import java.awt.geom.AffineTransform;
import java.util.HashSet;

import gnu.java.awt.font.opentype.OpenTypeFont;
import gnu.java.awt.font.opentype.truetype.Point;
import gnu.java.awt.font.opentype.truetype.Zone;

/**
 * Implements Latin specific glyph handling.
 */
class Latin
  implements Script, Constants
{

  static final int MAX_WIDTHS = 16;

  public void applyHints(GlyphHints hints, ScriptMetrics metrics)
  {
    // TODO Auto-generated method stub

  }

  public void doneMetrics(ScriptMetrics metrics)
  {
    // TODO Auto-generated method stub

  }

  /**
   * Initializes the <code>hints</code> object.
   *
   * @param hints the hints to initialize
   * @param metrics the metrics to use
   */
  public void initHints(GlyphHints hints, ScriptMetrics metrics)
  {
    hints.rescale(metrics);
    LatinMetrics lm = (LatinMetrics) metrics;
    hints.xScale = lm.axis[DIMENSION_HORZ].scale;
    hints.xDelta = lm.axis[DIMENSION_HORZ].delta;
    hints.yScale = lm.axis[DIMENSION_VERT].scale;
    hints.yDelta = lm.axis[DIMENSION_VERT].delta;
    // TODO: Set the scaler and other flags.
  }

  /**
   * Initializes the script metrics.
   *
   * @param metrics the script metrics to initialize
   * @param face the font
   */
  public void initMetrics(ScriptMetrics metrics, OpenTypeFont face)
  {
    assert metrics instanceof LatinMetrics;
    LatinMetrics lm = (LatinMetrics) metrics;
    lm.unitsPerEm = face.unitsPerEm;

    // TODO: Check for latin charmap.

    initWidths(lm, face, 'o');
    initBlues(lm, face);
  }

  public void scaleMetrics(ScriptMetrics metrics)
  {
    // TODO Auto-generated method stub

  }

  /**
   * Determines the standard stem widths.
   *
   * @param metrics the metrics to use
   * @param face the font face
   * @param ch the character that is used for getting the widths
   */
  private void initWidths(LatinMetrics metrics, OpenTypeFont face, char ch)
  {
    GlyphHints hints = new GlyphHints();
    metrics.axis[DIMENSION_HORZ].widthCount = 0;
    metrics.axis[DIMENSION_VERT].widthCount = 0;
    int glyphIndex = face.getGlyph(ch);
    // TODO: Avoid that AffineTransform constructor and change
    // getRawGlyphOutline() to accept null or remove that parameter altogether.
    // Consider this when the thing is done and we know what we need that for.
    Zone outline = face.getRawGlyphOutline(glyphIndex, new AffineTransform());
    LatinMetrics dummy = new LatinMetrics();
    Scaler scaler = dummy.scaler;
    dummy.unitsPerEm = metrics.unitsPerEm;
    scaler.xScale = scaler.yScale = 10000;
    scaler.xDelta = scaler.yDelta = 0;
    scaler.face = face;
    hints.rescale(dummy);
    hints.reload(outline);
    for (int dim = 0; dim < DIMENSION_MAX; dim++)
      {
        LatinAxis axis = metrics.axis[dim];
        AxisHints axHints = hints.axis[dim];
        int numWidths = 0;
        computeSegments(hints, dim);
        linkSegments(hints, dim);
        Segment[] segs = axHints.segments;
        HashSet<Segment> touched = new HashSet<Segment>();
        for (int i = 0; i < segs.length; i++)
          {
            Segment seg = segs[i];
            Segment link = seg.link;
            if (link != null && link.link == seg && ! touched.contains(link))
              {
                int dist = Math.abs(seg.pos - link.pos);
                if (numWidths < MAX_WIDTHS)
                  axis.widths[numWidths++] = new Width(dist);
              }
            touched.add(seg);
          }
      }
    for (int dim = 0; dim < DIMENSION_MAX; dim++)
      {
        LatinAxis axis = metrics.axis[dim];
        int stdw = axis.widthCount > 0 ? axis.widths[0].org
                                       : constant(metrics, 50);
        axis.edgeDistanceTreshold= stdw / 5;
      }
  }

  void linkSegments(GlyphHints hints, int dim)
  {
    AxisHints axis = hints.axis[dim];
    Segment[] segments = axis.segments;
    int numSegs = axis.numSegments;
    int majorDir = axis.majorDir;
    int lenThreshold = constant((LatinMetrics) hints.metrics, 8);
    lenThreshold = Math.min(1, lenThreshold);
    int lenScore = constant((LatinMetrics) hints.metrics, 3000);
    for (int i1 = 0; i1 < numSegs; i1++)
      {
        Segment seg1 = segments[i1];
        // The fake segments are introduced to hint the metrics.
        // Never link them to anything.
        if (seg1.first == seg1.last || seg1.dir != majorDir)
          continue;
        for (int i2 = 0; i2 < numSegs; i2++)
          {
            Segment seg2 = segments[i2];
            if (seg2 != seg1 && seg1.dir + seg2.dir == 0)
              {
                int pos1 = seg1.pos;
                int pos2 = seg2.pos;
                // The vertical coords are swapped compared to how FT handles
                // this.
                int dist = dim == DIMENSION_VERT ? pos1 - pos2 : pos2 - pos1;
                if (dist >= 0)
                  {
                    int min = seg1.minPos;
                    int max = seg1.maxPos;
                    int len, score;
                    if (min < seg2.minPos)
                      min = seg2.minPos;
                    if (max > seg2.maxPos)
                      max = seg2.maxPos;
                    len = max - min;
                    if (len > lenThreshold)
                      {
                        score = dist + lenScore / len;
                        if (score < seg1.score)
                          {
                            seg1.score = score;
                            seg1.link = seg2;
                          }
                        if (score < seg2.score)
                          {
                            seg2.score = score;
                            seg2.link = seg1;
                          }
                      }
                  }
              }
          }
      }
    for (int i1 = 0; i1 < numSegs; i1++)
      {
        Segment seg1 = segments[i1];
        Segment seg2 = seg1.link;
        if (seg2 != null)
          {
            seg2.numLinked++;
            if (seg2.link != seg1)
              {
                seg1.link = null;
                seg1.serif = seg2.link;
              }
          }
        // Uncomment to show all segments.
        // System.err.println("segment#" + i1 + ": " + seg1);
      }
  }

  /**
   * Initializes the blue zones of the font.
   *
   * @param metrics the metrics to use
   * @param face the font face to analyze
   */
  private void initBlues(LatinMetrics metrics, OpenTypeFont face)
  {
    // TODO: Implement.
  }

  private int constant(LatinMetrics metrics, int c)
  {
    return c * (metrics.unitsPerEm / 2048);
  }

  private void computeSegments(GlyphHints hints, int dim)
  {
    Point[] points = hints.points;
    if (dim == DIMENSION_HORZ)
      {
        for (int i = 0; i < hints.numPoints; i++)
          {
            points[i].setU(points[i].getOrigX());
            points[i].setV(points[i].getOrigY());
          }
      }
    else
      {
        for (int i = 0; i < hints.numPoints; i++)
          {
            points[i].setU(points[i].getOrigY());
            points[i].setV(points[i].getOrigX());
          }
      }
    // Now look at each contour.
    AxisHints axis = hints.axis[dim];
    int majorDir = Math.abs(axis.majorDir);
    int segmentDir = majorDir;
    Point[] contours = hints.contours;
    int numContours = hints.numContours;
    Segment segment = null;
    for (int i = 0; i < numContours; i++)
      {
        int minPos = 32000;
        int maxPos = -32000;

        Point point = contours[i];
        Point last = point.getPrev();
        if (point == last) // Skip singletons.
          continue;
        if (Math.abs(last.getOutDir()) == majorDir
            && Math.abs(point.getOutDir()) == majorDir)
          {
            // We are already on an edge. Locate its start.
            last = point;
            while (true)
              {
                point = point.getPrev();
                if (Math.abs(point.getOutDir()) != majorDir)
                  {
                    point = point.getNext();
                    break;
                  }
                if (point == last)
                  break;
              }
          }
        last = point;
        boolean passed = false;
        boolean onEdge = false;
        while (true)    
          {
            int u, v;
            if (onEdge)
              {
                u = point.getU();
                if (u < minPos)
                  minPos = u;
                if (u > maxPos)
                  maxPos = u;
                if (point.getOutDir() != segmentDir || point == last)
                  {
                    // Leaving an edge. Record new segment.
                    segment.last = point;
                    // (minPos + maxPos) / 2.
                    segment.pos = (minPos + maxPos) >> 1;
                    if (segment.first.isControlPoint()
                        || point.isControlPoint())
                      segment.flags |= Segment.FLAG_EDGE_ROUND;
                    minPos = maxPos = point.getV();
                    v = segment.first.getV();
                    if (v < minPos)
                      minPos = v;
                    if (v > maxPos)
                      maxPos = v;
                    segment.minPos = minPos;
                    segment.maxPos = maxPos;
                    onEdge = false;
                    segment = null;
                  }
              }
            if (point == last)
              {
                if (passed)
                  break;
                passed = true;
              }
            if (! onEdge && Math.abs(point.getOutDir()) == majorDir)
              {
                // This is the start of a new segment.
                segmentDir = point.getOutDir();
                segment = axis.newSegment();
                segment.dir = segmentDir;
                segment.flags = Segment.FLAG_EDGE_NORMAL;
                minPos = maxPos = point.getU();
                segment.first = point;
                segment.last = point;
                segment.contour = contours[i];
                segment.score = 32000;
                segment.len = 0;
                segment.link = null;
                onEdge = true;
              }
            point = point.getNext();
          }
      }
    
  }
}
