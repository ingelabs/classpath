/* GlyphHints.java -- Data and methods for actual hinting
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

import gnu.java.awt.font.opentype.truetype.Fixed;
import gnu.java.awt.font.opentype.truetype.Point;
import gnu.java.awt.font.opentype.truetype.Zone;

/**
 * The data and methods used for the actual hinting process.
 */
class GlyphHints
  implements Constants
{

  int xScale;
  int xDelta;
  int yScale;
  int yDelta;

  AxisHints[] axis;

  Point[] points;
  int numPoints;
  int maxPoints;

  Point[] contours;
  int numContours;
  int maxContours;

  ScriptMetrics metrics;

  
  GlyphHints()
  {
    axis = new AxisHints[Constants.DIMENSION_MAX];
    axis[Constants.DIMENSION_VERT] = new AxisHints();
    axis[Constants.DIMENSION_HORZ] = new AxisHints();

    xScale = Fixed.ONE;
    yScale = Fixed.ONE;
  }

  void rescale(ScriptMetrics m)
  {
    metrics = m;
    // TODO: Copy scalerFlags.
  }
  
  void reload(Zone outline)
  {
    numPoints = 0;
    numContours = 0;
    axis[0].numSegments = 0;
    axis[0].numEdges = 0;
    axis[1].numSegments = 0;
    axis[1].numEdges = 0;

    // Create/reallocate the contours array.
    int newMax = outline.getNumContours();
    if (newMax > maxContours || contours == null)
      {
        newMax = (newMax + 3) & ~3; // Taken from afhints.c .
        Point[] newContours = new Point[newMax];
        if (contours != null)
          {
            System.arraycopy(contours, 0, newContours, 0, maxContours);
          }
        contours = newContours;
        maxContours = newMax;
      }

    // Create/reallocate the points array.
    newMax = outline.getSize() + 2;
    if (newMax > maxPoints || points == null)
      {
        newMax = (newMax + 2 + 7) & ~7; // Taken from afhints.c .
        Point[] newPoints = new Point[newMax];
        if (points != null)
          {
            System.arraycopy(points, 0, newPoints, 0, maxPoints);
          }
        points = newPoints;
        maxPoints = newMax;
      }

    numPoints = outline.getSize() - 4; // 4 phantom points.
    numContours = outline.getNumContours();

    // Set major direction. We don't handle Type 1 fonts yet.
    axis[DIMENSION_HORZ].majorDir = DIR_UP;
    axis[DIMENSION_VERT].majorDir = DIR_LEFT;

    // TODO: Freetype seems to scale and translate the glyph at that point.
    // I suppose that this is not really needed.
    System.arraycopy(outline.getPoints(), 0, points, 0, numPoints);

    // Setup prev and next and contours array.
    // TODO: Probably cache this.
    contours = new Point[numContours];
    Point currentContour = points[0];
    for (int i = 0, cIndex = 0; i < numPoints; i++)
      {
        // Start new contour when the last point has been a contour end.
        if (outline.isContourEnd(i))
          {
            // Connect the contour end point to the start point.
            points[i].setNext(currentContour);
            currentContour.setPrev(points[i]);
            contours[cIndex] = currentContour;
            cIndex++;
            currentContour = i < numPoints - 1 ? points[i + 1] : null;
          }
        else
          {
            // Connect the current and the previous point.
            points[i].setNext(points[i + 1]);
            points[i + 1].setPrev(points[i]);
          }
      }
    // Compute directions of in and out vectors of all points as well
    // as the weak point flag.
    for (int i = 0; i < numPoints; i++)
      {
        // Compute in and out dir.
        Point p = points[i];
        Point prev = p.getPrev();
        int inX = p.getOrigX() - prev.getOrigX();
        int inY = p.getOrigY() - prev.getOrigY();
        p.setInDir(Utils.computeDirection(inX, inY));
        Point next = p.getNext();
        int outX = next.getOrigX() - p.getOrigX();
        int outY = next.getOrigY() - p.getOrigY();
        p.setOutDir(Utils.computeDirection(outX, outY));

        if (p.isControlPoint())
          {
            setWeakPoint(p);
          }
        else if (p.getOutDir() == p.getInDir())
          {
            if (p.getOutDir() != DIR_NONE)
              setWeakPoint(p);
            else
              {
                int angleIn = Utils.atan(inY, inX);
                int angleOut = Utils.atan(outY, outX);
                int delta = Utils.angleDiff(angleIn, angleOut);
                if (delta < 2 && delta > -2)
                  setWeakPoint(p);
              }
          }
        else if (p.getInDir() == - p.getOutDir())
          {
            setWeakPoint(p);
          }
      }
    computeInflectionPoints();
  }

  private void setWeakPoint(Point p)
  {
    p.setFlags((byte) (p.getFlags() | Point.FLAG_WEAK_INTERPOLATION));
  }

  /**
   * Computes the inflection points for a glyph.
   */
  private void computeInflectionPoints()
  {
    // Do each contour separately.
    for (int c = 0; c < contours.length; c++)
      {
        Point point = contours[c];
        Point first = point;
        Point start = point;
        Point end = point;
        do
          {
            end = end.getNext();
            if (end == first)
              return;
          } while (end.getOrigX() == first.getOrigX()
                   && end.getOrigY() == first.getOrigY());
        
        // Extend segment whenever possible.
        Point before = start;
        int angleIn;
        int angleSeg = Utils.atan(end.getOrigX() - start.getOrigX(),
                                  end.getOrigY() - start.getOrigY());
        do
          {
            do
              {
                start = before;
                before = before.getPrev();
                if (before == first)
                  return;
              } while (before.getOrigX() == start.getOrigX()
                       && before.getOrigY() == start.getOrigY());
            angleIn = Utils.atan(start.getOrigX() - before.getOrigX(),
                                 start.getOrigY() - before.getOrigY());
          } while (angleIn == angleSeg);
        first = start;
        int diffIn = Utils.angleDiff(angleIn, angleSeg);
        // Now, process all segments in the contour.
        Point after;
        boolean finished = false;
        int angleOut, diffOut;
        do
          {
            // First, extend the current segment's end whenever possible.
            after = end;
            do
              {
                do
                  {
                    end = after;
                    after = after.getNext();
                    if (after == first)
                      finished = true;
                  } while (end.getOrigX() == after.getOrigX()
                           && end.getOrigY() == after.getOrigY());
                angleOut = Utils.atan(after.getOrigX() - end.getOrigX(),
                                      after.getOrigY() - end.getOrigY());
              } while (angleOut == angleSeg);
            diffOut = Utils.angleDiff(angleSeg, angleOut);
            if ((diffIn ^ diffOut) < 0)
              {
                // diffIn and diffOut have different signs, we have
                // inflection points here.
                do
                  {
                    start.addFlags(Point.FLAG_INFLECTION);
                    start = start.getNext();
                  } while (start != end);
                start.addFlags(Point.FLAG_INFLECTION);
              }
            start = end;
            end = after;
            angleSeg = angleOut;
            diffIn = diffOut;
          } while (! finished);
      }
  }

  boolean doHorizontal()
  {
    return true; // Check scaler flags here.
  }

  boolean doVertical()
  {
    return true; // Check scaler flags here.
  }
}
