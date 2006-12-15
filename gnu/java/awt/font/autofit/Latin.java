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
import gnu.java.awt.font.opentype.truetype.Fixed;
import gnu.java.awt.font.opentype.truetype.Point;
import gnu.java.awt.font.opentype.truetype.Zone;

/**
 * Implements Latin specific glyph handling.
 */
class Latin
  implements Script, Constants
{

  static final int MAX_WIDTHS = 16;

  private final static int MAX_TEST_CHARS = 12;

  /**
   * The types of the 6 blue zones.
   */
  private static final int CAPITAL_TOP = 0;
  private static final int CAPITAL_BOTTOM = 1;
  private static final int SMALL_F_TOP = 2;
  private static final int SMALL_TOP = 3;
  private static final int SMALL_BOTTOM = 4;
  private static final int SMALL_MINOR = 5;
  static final int BLUE_MAX = 6;

  /**
   * The test chars for the blue zones.
   *
   * @see #initBlues(LatinMetrics, OpenTypeFont)
   */
  private static final String[] TEST_CHARS =
    new String[]{"THEZOCQS", "HEZLOCUS", "fijkdbh",
                 "xzroesc", "xzroesc", "pqgjy"};

  public void applyHints(GlyphHints hints, Zone outline, ScriptMetrics metrics)
  {
    hints.reload(outline);
    hints.rescale(metrics);
    if (hints.doHorizontal())
      {
        detectFeatures(hints, DIMENSION_HORZ);
      }
    if (hints.doVertical())
      {
        detectFeatures(hints, DIMENSION_VERT);
        computeBlueEdges(hints, (LatinMetrics) metrics);
      }
    // Grid-fit the outline.
    for (int dim = 0; dim < DIMENSION_MAX; dim++)
      {
        if (dim == DIMENSION_HORZ && hints.doHorizontal()
            || dim == DIMENSION_VERT && hints.doVertical())
          {
            hintEdges(hints, dim);
            alignEdgePoints(hints, dim);
            alignStrongPoints(hints, dim);
            alignWeakPoints(hints, dim);
            
         }
      }
    // FreeType does a save call here. I guess that's not needed as we operate
    // on the live glyph data anyway.
  }

  private void alignWeakPoints(GlyphHints hints, int dim)
  {
    // TODO Auto-generated method stub
    
  }

  private void alignStrongPoints(GlyphHints hints, int dim)
  {
    // TODO Auto-generated method stub
    
  }

  private void alignEdgePoints(GlyphHints hints, int dim)
  {
    // TODO Auto-generated method stub
    
  }

  private void hintEdges(GlyphHints hints, int dim)
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

  public void scaleMetrics(ScriptMetrics metrics, HintScaler scaler)
  {
    LatinMetrics lm = (LatinMetrics) metrics;
    lm.scaler.renderMode = scaler.renderMode;
    lm.scaler.face = scaler.face;
    scaleMetricsDim(lm, scaler, DIMENSION_HORZ);
    scaleMetricsDim(lm, scaler, DIMENSION_VERT);
  }

  private void scaleMetricsDim(LatinMetrics lm, HintScaler scaler, int dim)
  {
    int scale;
    int delta;
    if (dim == DIMENSION_HORZ)
      {
        scale = scaler.xScale;
        delta = scaler.xDelta;
      }
    else
      {
        scale = scaler.yScale;
        delta = scaler.yDelta;
      }
    LatinAxis axis = lm.axis[dim];
    if (axis.orgScale == scale && axis.orgDelta == delta)
      // No change, no need to adjust.
      return;
    axis.orgScale = scale;
    axis.orgDelta = delta;

    // Correct X and Y scale to optimize the alignment of the top small
    // letters to the pixel grid.
    LatinAxis axis2 = lm.axis[DIMENSION_VERT];
    LatinBlue blue = null;
    for (int nn = 0; nn < axis2.blueCount; nn++)
      {
        if ((axis2.blues[nn].flags & LatinBlue.FLAG_ADJUSTMENT) != 0)
          {
            blue = axis2.blues[nn];
            break;
          }
      }
    if (blue != null)
      {
        int scaled = Fixed.mul16(blue.shoot.org, scaler.yScale);
        int fitted = Utils.pixRound(scaled);
        if (scaled != fitted)
          {
            if (dim == DIMENSION_HORZ)
              {
                if (fitted < scaled)
                  {
                    scale -= scale / 50;
                  }
              }
            else
              {
                scale = Utils.mulDiv(scale, fitted, scaled);
              }
          }
      }
    axis.scale = scale;
    axis.delta = delta;
    if (dim == DIMENSION_HORZ)
      {
        lm.scaler.xScale = scale;
        lm.scaler.xDelta = delta;
      }
    else
      {
        lm.scaler.yScale = scale;
        lm.scaler.yDelta = delta;
      }
    // Scale the standard widths.
    for (int nn = 0; nn < axis.widthCount; nn++)
      {
        Width w = axis.widths[nn];
        w.cur = Fixed.mul16(w.org, scale);
        w.fit = w.cur;
      }
    // Scale blue zones.
    if (dim == DIMENSION_VERT)
      {
        for (int nn = 0; nn < axis.blueCount; nn++)
          {
            blue = axis.blues[nn];
            blue.ref.cur = Fixed.mul16(blue.ref.org, scale) + delta;
            blue.ref.fit = blue.ref.cur;
            blue.shoot.cur = Fixed.mul16(blue.ref.org, scale) + delta;
            blue.flags &= ~LatinBlue.FLAG_BLUE_ACTIVE;
            // A blue zone is only active if it is less than 3/4 pixels tall.
            int dist = Fixed.mul16(blue.ref.org - blue.shoot.org, scale);
            if (dist <= 48 && dist >= -48)
              {
                int delta1 = blue.shoot.org - blue.ref.org;
                int delta2 = delta1;
                if (delta1 < 0)
                  delta2 = -delta2;
                delta2 = Fixed.mul16(delta2, scale);
                if (delta2 < 32)
                  delta2 = 0;
                else if (delta2 < 64)
                  delta2 = 32 + (((delta2 - 32) + 16) & ~31);
                else
                  delta2 = Utils.pixRound(delta2);
                if (delta1 < 0)
                  delta2 = -delta2;
                blue.ref.fit = Utils.pixRound(blue.ref.cur);
                blue.shoot.fit = blue.ref.fit+ delta2;
                blue.flags |= LatinBlue.FLAG_BLUE_ACTIVE;
              }
          }
      }
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
    Zone outline = face.getRawGlyphOutline(glyphIndex, IDENTITY);
    LatinMetrics dummy = new LatinMetrics();
    HintScaler scaler = dummy.scaler;
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
        Utils.sort(numWidths, axis.widths);
        axis.widthCount = numWidths;
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
    int[] flats = new int[MAX_TEST_CHARS];
    int[] rounds = new int[MAX_TEST_CHARS];
    int numFlats;
    int numRounds;
    LatinBlue blue;
    LatinAxis axis = metrics.axis[DIMENSION_VERT];
    // We compute the blues simply by loading each character in the test
    // strings, then compute its topmost or bottommost points.
    for (int bb = 0; bb < BLUE_MAX; bb++)
      {
        String p = TEST_CHARS[bb];
        int blueRef;
        int blueShoot;
        numFlats = 0;
        numRounds = 0;
        for (int i = 0; i < p.length(); i++)
          {
            // Load the character.
            int glyphIndex = face.getGlyph(p.charAt(i));
            Zone glyph =
              face.getRawGlyphOutline(glyphIndex, IDENTITY);

            // Now compute the min and max points.
            int numPoints = glyph.getSize() - 4; // 4 phantom points.
            Point[] points = glyph.getPoints();
            Point point = points[0];
            int extremum = 0;
            int index = 1;
            if (isTopBlue(bb))
              {
                for (; index < numPoints; index++)
                  {
                    point = points[index];
                    // We have the vertical direction swapped. The higher
                    // points have smaller (negative) Y.
                    if (point.getOrigY() < points[extremum].getOrigY())
                      extremum = index;
                  }
              }
            else
              {
                for (; index < numPoints; index++)
                  {
                    point = points[index];
                    // We have the vertical direction swapped. The higher
                    // points have smaller (negative) Y.
                    if (point.getOrigY() > points[extremum].getOrigY())
                      extremum = index;
                  }
              }
            // Debug, prints out the maxima.
            // System.err.println("extremum for " + bb + " / "+ p.charAt(i)
            //                    + ": " + points[extremum]);

            // Now determine if the point is part of a straight or round
            // segment.
            boolean round;
            int idx = extremum;
            int first, last, prev, next, end;
            int dist;
            last = -1;
            first = 0;
            for (int n = 0; n < glyph.getNumContours(); n++)
              {
                end = glyph.getContourEnd(n);
                // System.err.println("contour end for " + n + ": " + end);
                if (end >= idx)
                  {
                    last = end;
                    break;
                  }
                first = end + 1;
              }
            // Should never happen.
            assert last >= 0;

            // Now look for the previous and next points that are not on the
            // same Y coordinate. Threshold the 'closeness'.
            prev = idx;
            next = prev;
            do
              {
                if (prev > first)
                  prev--;
                else
                  prev = last;
                dist = points[prev].getOrigY() - points[extremum].getOrigY();
                if (dist < -5 || dist > 5)
                  break;
              } while (prev != idx);
            do
              {
                if (next < last)
                  next++;
                else
                  next = first;
                dist = points[next].getOrigY() - points[extremum].getOrigY();
                if (dist < -5 || dist > 5)
                  break;
              } while (next != idx);
            round = points[prev].isControlPoint()
                    || points[next].isControlPoint();

            if (round)
              {
                rounds[numRounds++] = points[extremum].getOrigY();
                // System.err.println("new round extremum: " + bb + ": "
                //                   + points[extremum].getOrigY());
              }
            else
              {
                flats[numFlats++] = points[extremum].getOrigY();
                // System.err.println("new flat extremum: " + bb + ": "
                //                    + points[extremum].getOrigY());
              }
          }
        // We have computed the contents of the rounds and flats tables.
        // Now determine the reference and overshoot position of the blues --
        // we simply take the median after a simple sort.
        Utils.sort(numRounds, rounds);
        Utils.sort(numFlats, flats);
        blue = axis.blues[axis.blueCount] = new LatinBlue();
        axis.blueCount++;
        if (numFlats == 0)
          {
            blue.ref = blue.shoot = new Width(rounds[numRounds / 2]);
          }
        else if (numRounds == 0)
          {
            blue.ref = blue.shoot = new Width(flats[numFlats / 2]);
          }
        else
          {
            blue.ref = new Width(flats[numFlats / 2]);
            blue.shoot = new Width(rounds[numRounds / 2]);
          }
        // There are sometimes problems:  if the overshoot position of top
        // zones is under its reference position, or the opposite for bottom
        // zones. We must check everything there and correct problems.
        if (blue.shoot != blue.ref)
          {
            int ref = blue.ref.org;
            int shoot = blue.shoot.org;
            // Inversed vertical coordinates!
            boolean overRef = shoot < ref;
            if (isTopBlue(bb) ^ overRef)
              {
                blue.shoot = blue.ref = new Width((shoot + ref) / 2);
              }
          }
        blue.flags = 0;
        if (isTopBlue(bb))
          blue.flags |= LatinBlue.FLAG_TOP;
        // The following flag is used later to adjust y and x scales in 
        // order to optimize the pixel grid alignment of the top small
        // letters.
        if (bb == SMALL_TOP)
          {
            blue.flags |= LatinBlue.FLAG_ADJUSTMENT;
          }
        // Debug: print out the blue zones.
        // System.err.println("blue zone #" + bb + ": " + blue);
      }
  }

  private static final AffineTransform IDENTITY = new AffineTransform();

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

  private boolean isTopBlue(int b)
  {
    return b == CAPITAL_TOP || b == SMALL_F_TOP || b == SMALL_TOP;
  }

  private void detectFeatures(GlyphHints hints, int dim)
  {
    computeSegments(hints, dim);
    linkSegments(hints, dim);
    computeEdges(hints, dim);
  }

  private void computeEdges(GlyphHints hints, int dim)
  {
    AxisHints axis = hints.axis[dim];
    LatinAxis laxis = ((LatinMetrics) hints.metrics).axis[dim];
    Segment[] segments = axis.segments;
    int numSegments = axis.numSegments;
    Segment seg;
    int upDir;
    int scale;
    int edgeDistanceThreshold;
    axis.numEdges = 0;
    scale = dim == DIMENSION_HORZ ? hints.xScale : hints.yScale;
    upDir = dim == DIMENSION_HORZ ? DIR_UP : DIR_RIGHT;

    // We will begin by generating a sorted table of edges for the
    // current direction. To do so, we simply scan each segment and try
    // to find an edge in our table that corresponds to its position.
    //
    // If no edge is found, we create one and insert a new edge in the
    // sorted table. Otherwise, we simply add the segment to the egde's
    // list which will be processed in the second step to compute the
    // edge's properties.
    //
    // Note that the edge table is sorted along the segment/edge
    // position.

    edgeDistanceThreshold = Fixed.mul16(laxis.edgeDistanceTreshold, scale);
    if (edgeDistanceThreshold > 64 / 4)
      edgeDistanceThreshold = 64 / 4;
    edgeDistanceThreshold = Fixed.div16(edgeDistanceThreshold, scale);
    for (int i = 0; i < numSegments; i++)
      {
        seg = segments[i];
        Edge found = null;
        for (int ee = 0; ee < axis.numEdges; ee++)
          {
            Edge edge = axis.edges[ee];
            int dist = seg.pos - edge.fpos;
            if (dist < 0)
              dist = -dist;
            if (dist < edgeDistanceThreshold)
              {
                found = edge;
                break;
              }
          }
        if (found == null)
          {
            // Insert new edge in the list and sort according to
            // the position.
            Edge edge = axis.newEdge(seg.pos);
            edge.first = seg;
            edge.last = seg;
            edge.fpos = seg.pos;
            edge.opos = Fixed.mul16(seg.pos, scale);
            seg.edgeNext = seg;
            seg.edge = edge;
          }
        else
          {
            seg.edgeNext = found.first;
            found.last.edgeNext = seg;
            found.last = seg;
            seg.edge = found;
          }
      }
    // Good. We will now compute each edge's properties according to
    // segments found on its position. Basically these are:
    // - Edge's main direction.
    // - Stem edge, serif edge, or both (which defaults to stem edge).
    // - Rounded edge, straight or both (which defaults to straight).
    // - Link for edge.

    // Now, compute each edge properties.
    for (int e = 0; e < axis.numEdges; e++)
      {
        Edge edge = axis.edges[e];
        // Does it contain round segments?
        int isRound = 0;
        // Does it contain straight segments?
        int isStraight = 0;
        // Number of upward segments.
        int ups = 0;
        // Number of downward segments.
        int downs = 0;

        seg = edge.first;
        do
          {
            // Check for roundness of segment.
            if ((seg.flags & Segment.FLAG_EDGE_ROUND) != 0)
              isRound++;
            else
              isStraight++;

            // Check for segment direction.
            if (seg.dir == upDir)
              ups += seg.maxPos - seg.minPos;
            else
              downs += seg.maxPos - seg.minPos;

            // Check for links. If seg.serif is set, then seg.link must
            // be ignored.
            boolean isSerif = seg.serif != null && seg.serif.edge != edge;
            if (seg.link != null || isSerif)
              {
                Edge edge2 = edge.link;
                Segment seg2 = seg.link;
                if (isSerif)
                  {
                    seg2 = seg.serif;
                    edge2 = edge.serif;
                  }
                if (edge2 != null)
                  {
                    int edgeDelta = edge.fpos - edge2.fpos;
                    if (edgeDelta < 0)
                      edgeDelta = -edgeDelta;
                    int segDelta = seg.pos - seg2.pos;
                    if (segDelta < 0)
                      segDelta = -segDelta;
                    if (segDelta < edgeDelta)
                      edge2 = seg2.edge;
                  }
                else
                  {
                    edge2 = seg2.edge;
                  }
                if (isSerif)
                  {
                    edge.serif = edge2;
                    edge2.flags |= Segment.FLAG_EDGE_SERIF;
                  }
                else
                  {
                    edge.link = edge2;
                  }
              }
            seg = seg.edgeNext;
          } while (seg != edge.first);
        edge.flags = Segment.FLAG_EDGE_NORMAL;
        if (isRound > 0 && isRound > isStraight)
          edge.flags |= Segment.FLAG_EDGE_ROUND;

        // Set the edge's main direction.
        edge.dir = DIR_NONE;
        if (ups > downs)
          edge.dir = upDir;
        else if (ups < downs)
          edge.dir = -upDir;
        else if (ups == downs)
          edge.dir = 0;

        // Gets rid of serif if link is set. This gets rid of many
        // unpleasant artifacts.
        if (edge.serif != null && edge.link != null)
          {
            edge.serif = null;
          }

        // Debug: Print out all edges.
        // System.err.println("edge# " + e + ": " + edge);
      }        
  }

  private void computeBlueEdges(GlyphHints hints, LatinMetrics metrics)
  {
    AxisHints axis = hints.axis[DIMENSION_VERT];
    Edge[] edges = axis.edges;
    int numEdges = axis.numEdges;
    LatinAxis latin = metrics.axis[DIMENSION_VERT];
    int scale = latin.scale;

    // Compute which blue zones are active. I.e. have their scaled
    // size < 3/4 pixels.

    // For each horizontal edge search the blue zone that is closest.
    for (int e = 0; e < numEdges; e++)
      {
        Edge edge = edges[e];
        // System.err.println("checking edge: " + edge);
        Width bestBlue = null;
        int bestDist = Fixed.mul16(metrics.unitsPerEm / 40, scale);
        
        if (bestDist > 64 / 2)
          bestDist = 64 / 2;
        for (int bb = 0; bb < BLUE_MAX; bb++)
          {
            LatinBlue blue = latin.blues[bb];
            // System.err.println("checking blue: " + blue);
            // Skip inactive blue zones, i.e. those that are too small.
            if ((blue.flags & LatinBlue.FLAG_BLUE_ACTIVE) == 0)
              continue;
            // If it is a top zone, check for right edges. If it is a bottom
            // zone, check for left edges.
            boolean isTopBlue = (blue.flags & LatinBlue.FLAG_TOP) != 0;
            boolean isMajorDir = edge.dir == axis.majorDir;

            // If it is a top zone, the edge must be against the major
            // direction. If it is a bottom zone it must be in the major
            // direction.
            if (isTopBlue ^ isMajorDir)
              {
                int dist = edge.fpos - blue.ref.org;
                if (dist < 0)
                  dist = -dist;
                dist = Fixed.mul16(dist, scale);
                if (dist < bestDist)
                  {
                    bestDist = dist;
                    bestBlue = blue.ref;
                  }

                // Now, compare it to the overshoot position if the edge is
                // rounded, and if the edge is over the reference position of
                // a top zone, or under the reference position of a bottom
                // zone.
                if ((edge.flags & Segment.FLAG_EDGE_ROUND) != 0 && dist != 0)
                  {
                    // Inversed vertical coordinates!
                    boolean isUnderRef = edge.fpos > blue.ref.org;
                    if (isTopBlue ^ isUnderRef)
                      {
                        blue = latin.blues[bb]; // Needed?
                        dist = edge.fpos - blue.shoot.org;
                        if (dist < 0)
                          dist = -dist;
                        dist = Fixed.mul16(dist, scale);
                        if (dist < bestDist)
                          {
                            bestDist = dist;
                            bestBlue = blue.shoot;
                          }
                      }
                  }

              }
          }
        if (bestBlue != null)
          {
            edge.blueEdge = bestBlue;
            // Debug: Print out the blue edges.
            // System.err.println("blue edge for: " + edge + ": " + bestBlue);
          }
      }
  }
}
