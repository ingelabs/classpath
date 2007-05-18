/* ScanlineCoverage.java -- Manages coverage information for a scanline
   Copyright (C) 2007 Free Software Foundation, Inc.

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

package gnu.java.awt.java2d;

/**
 * Stores and handles the pixel converage for a scanline. The pixel coverage
 * is stored as sorted list of {@linke Covergage} entries, each of which holds
 * information about the coverage for the X and Y axis. This is utilized to
 * compute the actual coverage for each pixel on the scanline and finding
 * chunks of pixels with equal coverage quickly.
 */
public final class ScanlineCoverage
{

  /**
   * One bucket in the list.
   */
  public static final class Coverage
  {
    /**
     * The X coordinate on the scanline to which this bucket belongs.
     */
    private int xPos;

    /**
     * The X coverage delta.
     */
    private int covDelta;

    /**
     * Implements a linked list. This points to the next element of the list.
     */
    Coverage next;

    /**
     * Returns the X coordinate for this entry.
     *
     * @return the X coordinate for this entry
     */
    public int getXPos()
    {
      return xPos;
    }

    /**
     * Returns the coverage delta for this entry.
     *
     * @return the coverage delta for this entry
     */
    public int getCoverageDelta()
    {
      return covDelta;
    }

    /**
     * Returns a string representation.
     *
     * @return a string representation
     */
    public String toString()
    {
      return "Coverage: xPos: " + xPos + ", covDelta: " + covDelta;
    }

    /**
     * Returns a string representation of this entry and all the following
     * in the linked list.
     *
     * @return a string representation of this entry and all the following
     *         in the linked list
     */
    public String list()
    {
      String str = toString();
      if (next != null)
        str = str + " --> " + next.list();
      return str;
    }
  }

  /**
   * The head of the sorted list of buckets.
   */
  private Coverage head;

  /**
   * The current bucket. We make use of the fact that the scanline converter
   * always scans the scanline (and thus this list) from left to right to
   * quickly find buckets or insertion points.
   */
  private Coverage current;

  /**
   * The item that is before current in the list.
   */
  private Coverage currentPrev;

  /**
   * The bucket after the last valid bucket. Unused buckets are not thrown
   * away and garbage collected. Instead, we keep them at the tail of the list
   * and reuse them when necessary.
   */
  private Coverage last;

  /**
   * The last valid entry.
   */
  private Coverage lastPrev;

  /**
   * The minimum X coordinate of this scanline.
   */
  private int minX;

  /**
   * The maximum X coordinate of this scanline.
   */
  private int maxX;

  /**
   * The maximum coverage value.
   */
  private int maxCoverage;

  /**
   * Indicates the the next scan of the scanline begins and that the next
   * request will be at the beginning of this list. This makes searching and
   * sorting of this list very quick.
   */
  public void rewind()
  {
    current = head;
    currentPrev = null;
  }

  /**
   * Clears the list. This does not throw away the old buckets but only
   * resets the end-pointer of the list to the first element. All buckets are
   * then unused and are reused when the list is filled again.
   */
  public void clear()
  {
    last = head;
    lastPrev = null;
    current = head;
    currentPrev = null;
    minX = Integer.MAX_VALUE;
    maxX = Integer.MIN_VALUE;
  }

  /**
   * This adds the specified coverage to the pixel at the specified
   * X position.
   *
   * @param x the X position
   * @param xc the x coverage
   * @param yc the y coverage
   */
  public void add(int x, int xc)
  {
    Coverage bucket = findOrInsert(x);
    bucket.covDelta += xc;
    minX = Math.min(minX, x);
    maxX = Math.max(maxX, x);
  }

  /**
   * Returns the maximum coverage value for the scanline.
   *
   * @return the maximum coverage value for the scanline
   */  
  public int getMaxCoverage()
  {
    return maxCoverage;
  }

  /**
   * Sets the maximum coverage value for the scanline.
   *
   * @param maxCov the maximum coverage value for the scanline
   */
  void setMaxCoverage(int maxCov)
  {
    maxCoverage = maxCov;
  }

  /**
   * Returns the maximum X coordinate of the current scanline.
   *
   * @return the maximum X coordinate of the current scanline
   */
  public int getMaxX()
  {
    return maxX;
  }

  /**
   * Returns the minimum X coordinate of the current scanline.
   *
   * @return the minimum X coordinate of the current scanline
   */
  public int getMinX()
  {
    return minX;
  }

  /**
   * Finds the bucket in the list with the specified X coordinate.
   * If no such bucket is found, then a new one is fetched (either a cached
   * bucket from the end of the list or a newly allocated one) inserted at the
   * correct position and returned.
   *
   * @param x the X coordinate
   *
   * @return a bucket to hold the coverage data
   */
  private Coverage findOrInsert(int x)
  {
    // First search for a matching bucket.
    if (head == null)
      {
        // Special case: the list is still empty.
        // Testpoint 1.
        head = new Coverage();
        head.xPos = x;
        current = head;
        currentPrev = null;
        return head;
      }

    // This performs a linear search, starting from the current bucket.
    // This is reasonably efficient because access to this list is always done
    // in a linear fashion and we are usually not more then 1 or 2 buckets away
    // from the one we're looking for.
    Coverage match = current;
    Coverage prev = currentPrev;
    while (match != last && match.xPos < x)
      {
        prev = match;
        match = match.next;
      }

    // At this point we have either found an entry with xPos >= x, or reached
    // the end of the list (match == last || match == null).
    if (match == null)
      {
        // End of the list. No cached items to reuse.
        // Testpoint 2.
        match = new Coverage();
        match.xPos = x;
        if (prev != null)
          prev.next = match;
        current = match;
        currentPrev = prev;
        return match;
      }
    else if (match == last)
      {
        // End of the list. Reuse this item. Expand list.
        // Testpoint 3.
        last = match.next;
        lastPrev = match;
        match.xPos = x;
        match.covDelta = 0;
        // Keep link to last element or null, indicating the end of the list.
        current = match;
        currentPrev = prev;
        return match;
      }

    if (x == match.xPos)
      {
        // Special case: We have another coverage entry at the same location
        // as an already existing entry. Return this.
        // Testpoint 4.
        current = match;
        currentPrev = prev;
        return match;
      }
    else // x <= match.xPos
      {
        assert (x <= match.xPos);
        assert (prev == null ||x > prev.xPos);

        // Create new entry, or reuse existing one.
        Coverage cov;
        if (last != null)
          {
            // Testpoint 5.
            cov = last;
            last = cov.next;
            lastPrev.next = last;
          }
        else
          {
            // Testpoint 6.
            cov = new Coverage();
          }
        
        cov.xPos = x;
        cov.covDelta = 0;

        // Insert this item in the list.
        if (prev != null)
          {
            // Testpoint 5 & 6.
            prev.next = cov;
            cov.next = match;
            current = cov;
            currentPrev = prev;
          }
        else
          {
            // Testpoint 7.
            assert (match == head);
            // Insert at head.
            head = cov;
            head.next = match;
            current = head;
            currentPrev = null;
          }
        return cov;
      }
  }

  /**
   * (Re-)Starts iterating the coverage entries by returning the first Coverage
   * item in the list. Pixels left to that entry have a zero coverage.
   *
   * @return the first coverage item
   */
  public Coverage iterate()
  {
    if (head == last)
      current = null;
    else
      current = head;
    currentPrev = null;
    return current;
  }

  /**
   * Returns the next coverage item in the list, according to the current
   * iteration state.
   *
   * @return the next coverage item in the list or {@ null} if the end has
   *         been reached
   */
  public Coverage next()
  {
    currentPrev = current;
    if (current == null || current.next == last)
      current = null;
    else
      current = current.next;
    return current;
  }

  /**
   * Returns {@ true} if this object has no entries for the current scanline,
   * {@ false} otherwise.
   *
   * @return {@ true} if this object has no entries for the current scanline,
   *         {@ false} otherwise
   */
  public boolean isEmpty()
  {
    return head == null || head == last;
  }

  /**
   * Performs some tests to check if this class is working correctly.
   * There are comments about a bunch of test points in this method, which
   * correspond to other points in this class as commented.
   */
  static void test()
  {
    System.out.println("ScanlineCoverage test 1");
    // Test testpoint 1 & 2.
    ScanlineCoverage c = new ScanlineCoverage();
    c.add(2, 3);
    c.add(3, 4);
    c.add(4, 5);

    Coverage cov = c.iterate();
    assertion(cov.xPos == 2);
    assertion(cov.covDelta == 3);
    cov = c.next();
    assertion(cov.xPos == 3);
    assertion(cov.covDelta == 4);
    cov = c.next();
    assertion(cov.xPos == 4);
    assertion(cov.covDelta == 5);
    assertion(c.next() == null);
    
    System.out.println("ScanlineCoverage test 2");
    // Test testpoint 3 and 4.
    c.clear();
    c.add(5, 4);
    c.add(7, 5);
    c.add(7, 10);
    cov = c.iterate();
    assertion(cov.xPos == 5);
    assertion(cov.covDelta == 4);
    cov = c.next();
    assertion(cov.xPos == 7);
    assertion(cov.covDelta == 15);
    assertion(c.next() == null);

    System.out.println("ScanlineCoverage test 3");
    // Test testpoint 5.
    c.rewind();
    c.add(6, 20);
    cov = c.iterate();
    assertion(cov.xPos == 5);
    assertion(cov.covDelta == 4);
    cov = c.next();
    assertion(cov.xPos == 6);
    assertion(cov.covDelta == 20);
    cov = c.next();
    assertion(cov.xPos == 7);
    assertion(cov.covDelta == 15);
    assertion(c.next() == null);
    
    System.out.println("ScanlineCoverage test 4");
    // Test testpoint 6.
    c.rewind();
    c.add(8, 21);
    cov = c.iterate();
    assertion(cov.xPos == 5);
    assertion(cov.covDelta == 4);
    cov = c.next();
    assertion(cov.xPos == 6);
    assertion(cov.covDelta == 20);
    cov = c.next();
    assertion(cov.xPos == 7);
    assertion(cov.covDelta == 15);
    cov = c.next();
    assertion(cov.xPos == 8);
    assertion(cov.covDelta == 21);
    assertion(c.next() == null);
    
    System.out.println("ScanlineCoverage test 5");
    // Test testpoint 6.
    c.rewind();
    c.add(2, 100);
    cov = c.iterate();
    assertion(cov.xPos == 2);
    assertion(cov.covDelta == 100);
    cov = c.next();
    assertion(cov.xPos == 5);
    assertion(cov.covDelta == 4);
    cov = c.next();
    assertion(cov.xPos == 6);
    assertion(cov.covDelta == 20);
    cov = c.next();
    assertion(cov.xPos == 7);
    assertion(cov.covDelta == 15);
    cov = c.next();
    assertion(cov.xPos == 8);
    assertion(cov.covDelta == 21);
    assertion(c.next() == null);
    
  }

  /**
   * Checks a condition and throws and AssertionError if this condition
   * fails.
   *
   * @param b the condition
   */
  private static void assertion(boolean b)
  {
    if (! b)
      {
        throw new AssertionError();
      }
  }
}
