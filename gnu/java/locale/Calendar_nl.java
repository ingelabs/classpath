/*************************************************************************
 * Calendar_nl.java -- Dutch calendar locale data
 *
 * Copyright (c) 1999 Free Software Foundation, Inc.
 * Written by Jochen Hoenicke <jochen@gnu.org>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later verion.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/

package gnu.java.locale;

import java.util.ListResourceBundle;
import java.util.Calendar;

/**
 * This class contains locale data for java.util.Calendar specific for 
 * dutch language.
 * @author Mark Wielaard
 */
public class Calendar_nl extends ListResourceBundle
{
  /**
   * This is the object array used to hold the keys and values
   * for this bundle
   */
  private static final Object[][] contents =
  {
    { "firstDayOfWeek", new Integer(Calendar.MONDAY) },

    /* XXX - I guess the default for gregorianCutover 
     * is also true for the Netherlands. But is it?
     */
  };

  /**
   * This method returns the object array of key, value pairs containing
   * the data for this bundle.
   *
   * @return The key, value information.
   */
  public Object[][] getContents()
  {
    return contents;
  }
}
