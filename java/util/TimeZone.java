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
     * The time zone identifier, e.g. PST.
     */
    private String ID;

    /**
     * The default time zone, as returned by getDefault.
     */
    private static TimeZone defaultZone;

    private final static TimeZone[] timezones = 
    {
        new SimpleTimeZone(-1000*3600, "CVT"),
        new SimpleTimeZone(+0000*3600, "GMT"),
        new SimpleTimeZone(+0000*3600, "UTC"),
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
        new SimpleTimeZone(-5000*3600, "EST"),
        new SimpleTimeZone(-5000*3600, "EST5EDT" ,
                           Calendar.APRIL    ,  1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
        new SimpleTimeZone(-6000*3600, "CST6CDT" ,
                           Calendar.APRIL    ,  1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
        new SimpleTimeZone(-7000*3600, "MST"),
        new SimpleTimeZone(-7000*3600, "MST7MDT" ,
                           Calendar.APRIL    ,  1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
        new SimpleTimeZone(-8000*3600, "PST8PDT" ,
                           Calendar.APRIL    ,  1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
        new SimpleTimeZone(-10000*3600, "HST"),
        /* Add your favourite time zone here! */
        new SimpleTimeZone(+1000*3600, "MET" , //MET is a synonym for CET
                           Calendar.MARCH    , -1, Calendar.SUNDAY, 2000*3600,
                           Calendar.OCTOBER  , -1, Calendar.SUNDAY, 2000*3600),
    };

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
    public static TimeZone getTimeZone(String ID) {
        for (int i=0; i<timezones.length; i++)
            if (timezones[i].getID().equals(ID))
                return timezones[i];
        return null;
    }

    /**
     * Gets the avaialable IDs according to the given time zone
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
     * @see setDefault
     */
    public static TimeZone getDefault() {
        if (defaultZone == null) {
            String zone = System.getProperty("user.timezone", "UTC");
            defaultZone = getTimeZone(zone);
            if (defaultZone == null)
                defaultZone = getTimeZone("UTC");
        }
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
