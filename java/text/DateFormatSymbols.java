/* DateFormatSymbols.java -- Manage local specific date formatting information
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.text;

import java.util.ResourceBundle;
import java.util.Locale;

/**
 * This class acts as container for locale specific date/time formatting
 * information such as the days of the week and the months of the year.
 */
public class DateFormatSymbols 
{
  private ResourceBundle rb;
  String[] weekdays; // Serialized
  String[] shortWeekdays; // Serialized
  String[] months; // Serialized
  String[] shortMonths; // Serialized
  String[] eras; // Serialized
  String[] ampms; // Serialized
  String[][] zoneStrings; // Serialized
  String localPatternChars; // Serialized

  // The order of these prefixes must be the same as in DateFormat
  private static final String[] formatPrefixes = { "full", "long", "medium", "short", "default" };

  private static boolean
  arrayEquals(Object[] o1, Object[] o2)
  {
    if (o1 == null)
      {
        if (o2 == null)
          return(true);
        else
          return(false);
      }
    else
      if (o2 == null)
        return(false);
  
    // We assume ordering is important.
    for (int i = 0; i < o1.length; i++)
      if (o1[i] instanceof Object[])
        {
          if (o2[i] instanceof Object[]) 
            {
              if (!arrayEquals((Object[])o1[i], (Object[])o2[i]))
                return(false);
            }
          else
            return(false);
        }
      else
        {
          if (o1[i] == null)
            {
              if (o2[i] == null)
                return(true);
              else
                return(false);
            }
          else
            if (o2[1] == null)
              return(false);

          if (!o1[i].equals(o2[i]))
            return(false);
        }
  
    return(true);
  }

  // These are each arrays with a value for SHORT, MEDIUM, LONG, FULL,
  // and DEFAULT (constants defined in java.text.DateFormat).  While
  // not part of the official spec, we need a way to get at locale-specific
  // default formatting patterns.  They are declared package scope so
  // as to be easily accessible where needed (DateFormat, SimpleDateFormat).
  String[] dateFormats;
  String[] timeFormats;

  private String[] formatsForKey(String key) 
  {
    String[] values = new String [formatPrefixes.length];
    for (int i = 0; i < formatPrefixes.length; i++) {
      values[i] = rb.getString(formatPrefixes[i]+key);
    }
    return values;
  }

  /**
   * This method loads the format symbol information for the default
   * locale.
   */
  public DateFormatSymbols() 
  {
    this(Locale.getDefault());
  }

  /**
   * This method initializes a new instance of <code>DateFormatSymbols</code>
   * by loading the date format information for the specified locale.
   *
   * @param locale The locale for which date formatting symbols should be loaded.
   */
  public DateFormatSymbols(Locale locale) 
  {
    rb = ResourceBundle.getBundle("gnu/java/locale/LocaleInformation",locale);

    months = (String [])rb.getObject("months");
    shortMonths = (String [])rb.getObject("shortMonths");
    weekdays = (String [])rb.getObject("weekdays");
    shortWeekdays = (String [])rb.getObject("shortWeekdays");
    ampms = (String [])rb.getObject("ampms");
    eras = (String [])rb.getObject("eras");
    zoneStrings = (String [][])rb.getObject("zoneStrings");
    localPatternChars = rb.getString("localPatternChars");

    dateFormats = formatsForKey("DateFormat");
    timeFormats = formatsForKey("TimeFormat");
  }

  /**
   * Returns a new copy of this object.
   *
   * @param A copy of this object
   */
  public Object clone() 
  {
    try
      {
        return super.clone();
      } 
    catch (CloneNotSupportedException e) 
      {
        return null;
      }
  }

  /**
   * This method tests a specified object for equality against this object.
   * This will be true if and only if the specified object:
   * <p>
   * <ul>
   * <li> Is not <code>null</code>.
   * <li> Is an instance of <code>DateFormatSymbols</code>.
   * <li> Contains identical formatting symbols to this object.
   * </ul>
   * 
   * @param obj The <code>Object</code> to test for equality against.
   *
   * @return <code>true</code> if the specified object is equal to this one,
   * </code>false</code> otherwise.
   */
  public boolean equals(Object obj) 
  {
    if (obj == null)
      return(false);

    if (!(obj instanceof DateFormatSymbols))
      return(false);

    DateFormatSymbols dfs = (DateFormatSymbols)obj;

    if (!arrayEquals(getAmPmStrings(), dfs.getAmPmStrings()))
      return(false);
    if (!arrayEquals(getEras(), dfs.getEras()))
      return(false);
    if (!arrayEquals(getMonths(), dfs.getMonths()))
      return(false);
    if (!arrayEquals(getShortMonths(), dfs.getShortMonths()))
      return(false);
    if (!arrayEquals(getWeekdays(), dfs.getWeekdays()))
      return(false);
    if (!arrayEquals(getShortWeekdays(), dfs.getShortWeekdays()))
      return(false);
    if (!arrayEquals(getZoneStrings(), dfs.getZoneStrings()))
      return(false);
    if (!getLocalPatternChars().equals(dfs.getLocalPatternChars()))
      return(false);

    // Don't check non-spec variables
    return(true);
  }

  /**
   * This method returns a hash value for this object.
   *
   * @return A hash value for this object.
  public int hashCode() 
  {
    return(System.identityHashCode(this));
  }

  /**
   * This method returns the list of strings used for displaying AM or PM.
   * This is a two element <code>String</code> array indexed by
   * <code>Calendar.AM</code> and <code>Calendar.PM</code>
   *
   * @return The list of AM/PM display strings.
   */
  public String[] getAmPmStrings() 
  {
    return(ampms);
  }

  /**
    * This method returns the list of strings used for displaying eras
    * (e.g., "BC" and "AD").  This is a two element <code>String</code>
    * array indexed by <code>Calendar.BC</code> and <code>Calendar.AD</code>.
    *
    * @return The list of era disply strings.
    */
  public String[] getEras() 
  {
    return(eras);
  }

  /**
    * This method returns the pattern character information for this
    * object.  This is an 18 character string that contains the characters
    * that are used in creating the date formatting strings in 
    * <code>SimpleDateFormat</code>.   The following are the character
    * positions in the string and which format character they correspond
    * to (the character in parentheses is the default value in the US English
    * locale):
    * <p>
    * <ul>
    * <li>0 - era (G)
    * <li>1 - year (y)
    * <li>2 - month (M)
    * <li 3 - day of month (d)
    * <li>4 - hour out of 12, from 1-12 (h)
    * <li>5 - hour out of 24, from 0-23 (H)
    * <li>6 - minute (m)
    * <li>7 - second (s)
    * <li>8 - millisecond (S)
    * <li>9 - date of week (E)
    * <li>10 - date of year (D)
    * <li>11 - day of week in month, eg. "4th Thur in Nov" (F)
    * <li>12 - week in year (w)
    * <li>13 - week in month (W)
    * <li>14 - am/pm (a)
    * <li>15 - hour out of 24, from 1-24 (k)
    * <li>16 - hour out of 12, from 0-11 (K)
    * <li>17 - time zone (z)
    * </ul>
    *
    * @return The format patter characters
    */
  public String getLocalPatternChars() 
  {
    return(localPatternChars);
  }

  /**
   * This method returns the list of strings used for displaying month
   * names (e.g., "January" and "February").  This is a thirteen element
   * string array indexed by <code>Calendar.JANUARY</code> through
   * <code>Calendar.UNDECEMBER</code>.  Note that there are thirteen
   * elements because some calendars have thriteen months.
   *
   * @return The list of month display strings.
   */
  public String[] getMonths() 
  {
    return(months);
  }

  /**
   * This method returns the list of strings used for displaying abbreviated
   * month names (e.g., "Jan" and "Feb").  This is a thirteen element
   * <code>String</code> array indexed by <code>Calendar.JANUARY</code>
   * through <code>Calendar.UNDECEMBER</code>.  Note that there are thirteen
   * elements because some calendars have thirteen months.
   *
   * @return The list of abbreviated month display strings.
   */
  public String[] getShortMonths() 
  {
    return(shortMonths);
  }

  /**
   * This method returns the list of strings used for displaying weekday
   * names (e.g., "Sunday" and "Monday").  This is an eight element
   * <code>String</code> array indexed by <code>Calendar.SUNDAY</code>
   * through <code>Calendar.SATURDAY</code>.  Note that the first element
   * of this array is ignored.
   *
   * @return This list of weekday display strings.
   */
  public String[] getWeekdays() 
  {
    return(weekdays);
  }

  /**
   * This method returns the list of strings used for displaying abbreviated 
   * weekday names (e.g., "Sun" and "Mon").  This is an eight element
   * <code>String</code> array indexed by <code>Calendar.SUNDAY</code>
   * through <code>Calendar.SATURDAY</code>.  Note that the first element
   * of this array is ignored.
   *
   * @return This list of abbreviated weekday display strings.
   */
  public String[] getShortWeekdays() 
  {
    return(shortWeekdays);
  }

  /**
   * This method returns this list of localized timezone display strings.
   * This is a two dimensional <code>String</code> array where each row in
   * the array contains five values:
   * <P>
   * <ul>
   * <li>0 - The non-localized time zone id string.
   * <li>1 - The long name of the time zone (standard time).
   * <li>2 - The short name of the time zone (standard time).
   * <li>3 - The long name of the time zone (daylight savings time).
   * <li>4 - the short name of the time zone (daylight savings time).
   *
   * @return The list of time zone display strings.
   */
  public String[][] getZoneStrings() 
  {
    return(zoneStrings);
  }

  /**
   * This method sets the list of strings used to display AM/PM values to
   * the specified list.
   * This is a two element <code>String</code> array indexed by
   * <code>Calendar.AM</code> and <code>Calendar.PM</code>
   *
   * @param ampms The new list of AM/PM display strings.
   */
  public void setAmPmStrings(String[] ampms) 
  {
    this.ampms = ampms;
  }

  /**
   * This method sets the list of strings used to display time eras to
   * to the specified list.
   * This is a two element <code>String</code>
   * array indexed by <code>Calendar.BC</code> and <code>Calendar.AD</code>.
   *
   * @param eras The new list of era disply strings.
   */
  public void setEras(String[] eras) 
  {
    this.eras = eras;
  }

  /**
    * This method sets the list of characters used to specific date/time
    * formatting strings.
    * This is an 18 character string that contains the characters
    * that are used in creating the date formatting strings in 
    * <code>SimpleDateFormat</code>.   The following are the character
    * positions in the string and which format character they correspond
    * to (the character in parentheses is the default value in the US English
    * locale):
    * <p>
    * <ul>
    * <li>0 - era (G)
    * <li>1 - year (y)
    * <li>2 - month (M)
    * <li 3 - day of month (d)
    * <li>4 - hour out of 12, from 1-12 (h)
    * <li>5 - hour out of 24, from 0-23 (H)
    * <li>6 - minute (m)
    * <li>7 - second (s)
    * <li>8 - millisecond (S)
    * <li>9 - date of week (E)
    * <li>10 - date of year (D)
    * <li>11 - day of week in month, eg. "4th Thur in Nov" (F)
    * <li>12 - week in year (w)
    * <li>13 - week in month (W)
    * <li>14 - am/pm (a)
    * <li>15 - hour out of 24, from 1-24 (k)
    * <li>16 - hour out of 12, from 0-11 (K)
    * <li>17 - time zone (z)
    * </ul>
    *
    * @param localPatternChars The new format patter characters
    */
  public void setLocalPatternChars(String localPatternChars) 
  {
    this.localPatternChars = localPatternChars;
  }

  /**
    * This method sets the list of strings used to display month names.
    * This is a thirteen element
    * string array indexed by <code>Calendar.JANUARY</code> through
    * <code>Calendar.UNDECEMBER</code>.  Note that there are thirteen
    * elements because some calendars have thriteen months.
    *
    * @param months The list of month display strings.
    */
  public void setMonths(String[] months) 
  {
    this.months = months;
  }

  /**
   * This method sets the list of strings used to display abbreviated month
   * names.
   * This is a thirteen element
   * <code>String</code> array indexed by <code>Calendar.JANUARY</code>
   * through <code>Calendar.UNDECEMBER</code>.  Note that there are thirteen
   * elements because some calendars have thirteen months.
   *
   * @param shortMonths The new list of abbreviated month display strings.
   */
  public void setShortMonths(String[] shortMonths) 
  {
    this.shortMonths = shortMonths;
  }

  /**
   * This method sets the list of strings used to display weekday names.
   * This is an eight element
   * <code>String</code> array indexed by <code>Calendar.SUNDAY</code>
   * through <code>Calendar.SATURDAY</code>.  Note that the first element
   * of this array is ignored.
   *
   * @param weekdays This list of weekday display strings.
   */
  public void setWeekdays(String[] weekdays) 
  {
    this.weekdays = weekdays;
  }

  /**
   * This method sets the list of strings used to display abbreviated
   * weekday names.
   * This is an eight element
   * <code>String</code> array indexed by <code>Calendar.SUNDAY</code>
   * through <code>Calendar.SATURDAY</code>.  Note that the first element
   * of this array is ignored.
   *
   * @param shortWeekdays This list of abbreviated weekday display strings.
   */
  public void setShortWeekdays(String[] shortWeekdays) 
  {
    this.shortWeekdays = shortWeekdays;
  }

  /**
   * This method sets the list of display strings for time zones.
   * This is a two dimensional <code>String</code> array where each row in
   * the array contains five values:
   * <P>
   * <ul>
   * <li>0 - The non-localized time zone id string.
   * <li>1 - The long name of the time zone (standard time).
   * <li>2 - The short name of the time zone (standard time).
   * <li>3 - The long name of the time zone (daylight savings time).
   * <li>4 - the short name of the time zone (daylight savings time).
   *
   * @return The list of time zone display strings.
   */
  public void setZoneStrings(String[][] zoneStrings) 
  {
    this.zoneStrings = zoneStrings;
  }

} // DateFormatSymbols

