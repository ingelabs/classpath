/* TabSet.java --
   Copyright (C) 2004 Free Software Foundation, Inc.

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

package javax.swing.text;

import java.io.Serializable;

/**
 * A set of tab stops.  Instances of this class are immutable.
 */
public class TabSet implements Serializable
{
  /** The serialization UID (compatible with JDK1.5). */
  private static final long serialVersionUID = 2367703481999080593L;

  /** Storage for the tab stops. */
  TabStop[] tabs;

  /**
   * Creates a new <code>TabSet</code> containing the specified tab stops.
   * 
   * @param t  the tab stops (<code>null</code> permitted).
   */
  public TabSet(TabStop[] t) 
  {
    if (t != null)
      tabs = (TabStop[]) t.clone();
    else 
      tabs = new TabStop[0];
  }
 
  /**
   * Returns the tab stop with the specified index.
   * 
   * @param i  the index.
   * 
   * @return The tab stop.
   * 
   * @throws IllegalArgumentException if <code>i</code> is not in the range 
   *     <code>0</code> to <code>getTabCount() - 1</code>.
   */
  public TabStop getTab(int i) 
  {
    if (i < 0 || i >= tabs.length)
      throw new IllegalArgumentException("Index out of bounds.");
    return tabs[i];
  }

  public TabStop getTabAfter(float location) 
  {
    int idx = getTabIndexAfter(location);
    if (idx == -1)
      return null;
    else
      return tabs[idx];        
  }

  /**
   * Returns the number of tab stops in this tab set.
   * 
   * @return The number of tab stops in this tab set.
   */
  public int getTabCount() 
  {
    return tabs.length;
  }

  /**
   * Returns the index of the specified tab, or -1 if the tab is not found.
   * 
   * @param tab  the tab (<code>null</code> permitted).
   * 
   * @return The index of the specified tab, or -1.
   */
  public int getTabIndex(TabStop tab) 
  {
    for (int i = 0; i < tabs.length; ++i)
      if (tabs[i] == tab)
        return i;
    return -1;
  }

  /**
   * Returns the index of the tab at or after the specified location.
   * 
   * @param location  the tab location.
   * 
   * @return The index of the tab stop, or -1.
   */
  public int getTabIndexAfter(float location) 
  {
    for (int i = 0; i < tabs.length; i++)
      {
        if (location <= tabs[i].getPosition())
          return i;
      }
    return -1;
  }

  /**
   * Returns a string representation of this <code>TabSet</code>.
   * 
   * @return A string representation of this <code>TabSet</code>.
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    for (int i = 0; i < tabs.length; ++i)
      {
        if (i != 0)
          sb.append(" - ");
        sb.append(tabs[i].toString());
      }
    sb.append("]");
    return sb.toString();
  }
}
