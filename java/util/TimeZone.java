/*
 * java.util.TimeZone: part of the Java Class Libraries project.
 * Copyright (C) 1998 Jochen Hoenicke
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.util;

/**
 * This class represents a time zone offset and handles daylight savings.
 * 
 * You can get the default time zone with <code>getDefault</code>.
 * This represents the time zone where program is running.
 *
 * Another way to create a time zone is <code>getTimeZone</code>, where
 * you can give an identifier as parameter.  For instance, the identifier
 * of the Central European Time zone is "CET".
 *
 * With the <code>getAvailableIDs</code> method, you can get all the
 * supported time zone identifiers.
 *
 * @see Calendar
 * @see SimpleTimeZone
 * @author Jochen Hoenicke
 */
public abstract class TimeZone implements java.io.Serializable, Cloneable {

    /**
     * Constant used to indicate that a short timezone abbreviation should
     * be returned, such as "EST"
     */
    public static final int SHORT = 0;

    /**
     * Constant used to indicate that a long timezone name should be
     * returned, such as "Eastern Standard Time".
     */
    public static final int LONG = 1;

    /**
     * The time zone identifier, e.g. PST.
     */
    private String ID;

    /**
     * The default time zone, as returned by getDefault.
     */
    private static TimeZone defaultZone;

    private static final long serialVersionUID = 3581463369166924961L;

    /**
     * Aliases for timezones.  The format is "alias name", "canonical name"
     */
    private static final String[][] zone_aliases =
    {
      { "UTC", "GMT" },
      /* Common US Abbreviations */
      { "EST", "EST5EDT" },
      { "EST5EWT", "EST5EDT" },
      { "CST", "CST6CDT" },
      { "CST6CWT", "CST6CDT" },
      { "MST", "MST7MDT" },
      { "MST7MWT", "MST7MDT" },
      { "PST", "PST8PDT" },
      /* Middle vs. Central European Time */
      { "MET", "CET" },
      /* Major US Cities */
      { "New York", "EST5EDT" },
      { "Indianapolis", "EST5" },
      { "Chicago", "CST6CDT" },
      { "Denver", "MST7MDT" },
      { "Phoenix", "MST7" },
      { "San Francisco", "PST8PDT" },
      /* Newfangled Style */
      { "America/Chicago", "CST6CDT" },
      { "America/Denver", "MST7MDT" },
      { "America/Detroit", "EST5EDT" },
      { "America/Indianapolis", "EST5" },
      { "America/Indiana/Indianapolis", "EST5" },
      { "America/Los_Angelese", "PST8PDT" },
      { "America/New_York", "EST5EDT" },
      { "America/Phoenix", "MST7" }
    };

    private final static TimeZone[] timezones = 
    {
        new SimpleTimeZone(-1000*3600, "CVT"),
        new SimpleTimeZone(+0000*3600, "GMT"),
        new SimpleTimeZone(+1000*3600, "WAT"),
        new SimpleTimeZone(+2000*3600, "CAT"),
        new SimpleTimeZone(+3000*3600, "EAT"),
        new SimpleTimeZone(+2000*3600, "Egypt(EET)" , 
                           Calendar.APRIL    , -1, Calendar.FRIDAY, 1000*3600,
                           Calendar.SEPTEMBER, -1, Calendar.FRIDAY, 3000*3600),
        new SimpleTimeZone(+1000*3600, "Libya(CET)" , 
                           Calendar.MARCH  , -1, Calendar.THURSDAY, 1000*3600,
                           Calendar.OCTOBER, -1, Calendar.THURSDAY, 0000*3600),
        new SimpleTimeZone(+0000*3600, "WET" , 
                           Calendar.MARCH    , -1, Calendar.SUNDAY, 1000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 1000*3600),
        new SimpleTimeZone(+1000*3600, "CET" , 
                           Calendar.MARCH    , -1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
        new SimpleTimeZone(+2000*3600, "EET" , 
                           Calendar.MARCH    , -1, Calendar.SUNDAY, 3000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 3000*3600),
        new SimpleTimeZone(-5000*3600, "EST5"),
        new SimpleTimeZone(-5000*3600, "EST5EDT" ,
                           Calendar.APRIL    ,  1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
        new SimpleTimeZone(-6000*3600, "CST6"),
        new SimpleTimeZone(-6000*3600, "CST6CDT" ,
                           Calendar.APRIL    ,  1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
        new SimpleTimeZone(-7000*3600, "MST7"),
        new SimpleTimeZone(-7000*3600, "MST7MDT" ,
                           Calendar.APRIL    ,  1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
        new SimpleTimeZone(-8000*3600, "PST8PDT" ,
                           Calendar.APRIL    ,  1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
        new SimpleTimeZone(-10000*3600, "HST"),
        /* Add your favourite time zone here! */
    };

    /* Look up default timezone */
    static
    {
      System.loadLibrary("javautil");

      String tzid = getDefaultTimeZoneId();
      if (tzid == null)
        tzid = "GMT";

      defaultZone = getTimeZone(tzid);
      if (defaultZone == null)
        defaultZone = getTimeZone("GMT");
    }

    /* This method returns us a time zone id string which is in the
       form <standard zone name><GMT offset><daylight time zone name>.
       The GMT offset is in seconds, except where it is evenly divisible
       by 3600, then it is in hours.  If the zone does not observe
       daylight time, then the daylight zone name is omitted.  Examples:
       in Chicago, the timezone would be CST6CDT.  In Indianapolis 
       (which does not have Daylight Savings Time) the string would
       be EST5
    */
    private static native String
    getDefaultTimeZoneId();

    /**
     * Gets the time zone offset, for current date, modified in case of 
     * daylight savings.  This is the offset to add to UTC to get the local
     * time.
     * @param era the era of the given date
     * @param year the year of the given date
     * @param month the month of the given date, 0 for January.
     * @param day the day of month
     * @param dayOfWeek the day of week
     * @param milliseconds the millis in the day (in local standard time)
     * @return the time zone offset in milliseconds.
     */
    public abstract int getOffset(int era, int year, int month,
                                  int day, int dayOfWeek, int milliseconds);

    /**
     * Gets the time zone offset, ignoring daylight savings.  This is
     * the offset to add to UTC to get the local time.
     * @return the time zone offset in milliseconds.  
     */
    public abstract int getRawOffset();

    /**
     * Gets the identifier of this time zone. For instance, PST for
     * Pacific Standard Time.
     * @returns the ID of this time zone.  
     */
    public String getID() {
        return ID;
    }

    /**
     * Sets the identifier of this time zone. For instance, PST for
     * Pacific Standard Time.
     * @param id the new time zone ID.
     */
    public void setID(String id) {
        this.ID = id;
    }

    /**
     * This method returns a string name of the time zone suitable
     * for displaying to the user.  The string returned will be the long
     * description of the timezone in the current locale.  The name
     * displayed will assume daylight savings time is not in effect.
     *
     * @return The name of the time zone.
     */
    public final String getDisplayName() {
      return(getDisplayName(false, LONG, Locale.getDefault()));
    }
    
    /**
     * This method returns a string name of the time zone suitable
     * for displaying to the user.  The string returned will be the long
     * description of the timezone in the specified locale. The name
     * displayed will assume daylight savings time is not in effect.
     *
     * @param locale The locale for this timezone name.
     *
     * @return The name of the time zone.
     */
    public final String getDisplayName(Locale locale) {
      return(getDisplayName(false, LONG, locale));
    }

    /**
     * This method returns a string name of the time zone suitable
     * for displaying to the user.  The string returned will be of the
     * specified type in the current locale. 
     *
     * @param dst Whether or not daylight savings time is in effect.
     * @param style <code>LONG</code> for a long name, <code>SHORT</code> for
     * a short abbreviation.
     *
     * @return The name of the time zone.
     */
    public final String getDisplayName(boolean dst, int style) {
      return(getDisplayName(dst, style, Locale.getDefault()));
    }


    /**
     * This method returns a string name of the time zone suitable
     * for displaying to the user.  The string returned will be of the
     * specified type in the specified locale. 
     *
     * @param dst Whether or not daylight savings time is in effect.
     * @param style <code>LONG</code> for a long name, <code>SHORT</code> for
     * a short abbreviation.
     * @param locale The locale for this timezone name.
     *
     * @return The name of the time zone.
     */
    public String getDisplayName(boolean dst, int style, Locale locale) {
      int offset = getRawOffset();

      int hours = offset / (1000*60*60);
      int minutes = (offset / (1000 * 60)) % 60;

      return("GMT" + ((hours > 9) ? "" + hours : "0" + hours) + 
             ((minutes > 9) ?  "" + minutes : "0" + minutes));
    }

    /** 
     * Returns true, if this time zone uses Daylight Savings Time.
     */
    public abstract boolean useDaylightTime();

    /**
     * Returns true, if the given date is in Daylight Savings Time in this
     * time zone.
     * @param date the given Date.
     */
    public abstract boolean inDaylightTime(Date date);

    /**
     * Gets the TimeZone for the given ID.
     * @param ID the time zone identifier.
     * @return The time zone for the identifier or null, if no such time
     * zone exists.
     */
    public static TimeZone getTimeZone(String ID) 
    {
        // First check aliases
        for (int i = 0; i < zone_aliases.length; i++)
          if (zone_aliases[i][0].equals(ID))
            {
              ID = zone_aliases[i][1];
              break;
            } 

        // Now look for the timezone in our table
        for (int i=0; i<timezones.length; i++)
            if (timezones[i].getID().equals(ID))
                return timezones[i];

        // See if the ID is really a GMT offset form.
        if (ID.startsWith("GMT"))
          {
            int offset_direction = 0;

            if (ID.charAt(4) == '-')
              offset_direction = -1; 
            else if (ID.charAt(4) == '+')
              offset_direction = 1;

            if (offset_direction != 0)
              {
                String offset_time_str = ID.substring(5);
                if (offset_time_str.indexOf(":") != -1)
                  {
                    int idx = offset_time_str.indexOf(":");
                    String hour_str = offset_time_str.substring(0, idx);
                    String min_str = offset_time_str.substring(idx + 1);

                    try
                      {
                        int hour = Integer.parseInt(hour_str);
                        int min = Integer.parseInt(min_str);
                        return(new SimpleTimeZone(((hour * 60 * 60 * 1000) +
                                (min * 60 * 1000))*offset_direction, ID));
                      }
                    catch(NumberFormatException e) { ; }
                  }
                else
                  {
                    try
                      {
                        int offset_time = Integer.parseInt(offset_time_str);
                        return(new SimpleTimeZone(
                            (offset_time * 60 * 60 * 1000) * offset_direction,
                            ID));
                      }
                    catch(NumberFormatException e) { ; }
                  }
              }
          }

        // Finally, return GMT per spec
        return getTimeZone("GMT");
    }

    /**
     * Gets the available IDs according to the given time zone
     * offset.  
     * @param rawOffset the given time zone GMT offset.
     * @return An array of IDs, where the time zone has the specified GMT
     * offset. For example <code>{"Phoenix", "Denver"}</code>, since both have
     * GMT-07:00, but differ in daylight savings behaviour.
     */
    public static String[] getAvailableIDs(int rawOffset) {
        int count = 0;
        for (int i=0; i<timezones.length; i++) {
            if (timezones[i].getRawOffset() == rawOffset)
                count++;
        }

        String[] ids = new String[count];
        for (int i=0; i<timezones.length; i++) {
            if (timezones[i].getRawOffset() == rawOffset)
                ids[i] = timezones[i].getID();
        }
        return ids;
    }

    /**
     * Gets all avaialable IDs.
     * @return An array of all supported IDs.
     */
    public static String[] getAvailableIDs() {
        String[] ids = new String[timezones.length];
        for (int i=0; i<timezones.length; i++)
            ids[i] = timezones[i].getID();
        return ids;
    }

    /**
     * Returns the time zone under which the host is running.  This
     * can be changed with setDefault.
     * @return the time zone for this host.
     * @see #setDefault
     */
    public static TimeZone getDefault() {
        return defaultZone;
    }

    public static void setDefault(TimeZone zone) {
        defaultZone = zone;
    }

    /**
     * Returns a clone of this object.  I can't imagine, why this is
     * useful for a time zone.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
