/*
 * java.util.Date: part of the Java Class Libraries project.
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
 * This class represents a specific time in milliseconds since the epoch.
 * The epoch is 1970, January 1 00:00:00.0000 UTC.  
 *
 * Date is intended to reflect universal time coordinate (UTC), but doesn't
 * handle the leap seconds.
 *
 * Prior to jdk 1.1 this class was the sole Time class and had also 
 * calendar functionality.  But this can't be localized, so a new Calendar
 * class was created, that you should use instead.  The functions which
 * get or return a year, month, day etc. are all deprecated and shouldn't be
 * used.  Use Calendar instead.
 * 
 * @see Calendar
 * @see GregorianCalendar
 * @see java.text.DateFormat
 * @author Jochen Hoenicke
 */
public class Date implements Cloneable, Comparable, java.io.Serializable {

    /**
     * The time in milliseconds since the epoch.
     */
    private transient long time;
    private static final long serialVersionUID = 7523967970034938905L;

    /**
     * Creates a new Date Object representing the current time.
     */
    public Date() {
        time = System.currentTimeMillis();
    }

    /**
     * Creates a new Date Object representing the given time.
     * @param time the time in milliseconds since the epoch.
     */
    public Date(long time) {
        this.time = time;
    }

    /**
     * Creates a new Date Object representing the given time.
     * @deprecated use <code>new GregorianCalendar(year+1900, month,
     * day)</code> instead.  
     */
    public Date(int year, int month, int day) {
        time = new GregorianCalendar(year+1900,month,day).getTimeInMillis();
    }

    /**
     * Creates a new Date Object representing the given time.
     * @deprecated use <code>new GregorianCalendar(year+1900, month,
     * day, hour, min)</code> instead.  
     */
    public Date(int year, int month, int day, int hour, int min) {
        time = new GregorianCalendar(year+1900,month,day,hour,min)
            .getTimeInMillis();
    }

    /*
     * Creates a new Date Object representing the given time.
     * @deprecated use <code>new GregorianCalendar(year+1900, month,
     * day)</code> instead.  
     */
    public Date(int year, int month, int day, int hour, int min, int sec) {
        time = new GregorianCalendar(year+1900,month,day,hour,min,sec)
            .getTimeInMillis();
    }

    /**
     * Creates a new Date from the given string representation.  This
     * does the same as <code>new Date(Date.parse(s))</code>
     * @see parse
     * @deprecated use <code>java.text.DateFormat.parse(s)</code> instead.  
     */
    public Date(String s) {
        time = parse(s);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    /**
     * Gets the time represented by this Object
     * @return the time in milliseconds since the epoch.
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the time which this Object should represented.
     * @param time the time in milliseconds since the epoch.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Tests if this date is after the specified date.
     * @param when the other date
     * @return true, if the date represented by this Object is
     * strictly later than the time represented by when.  
     */
    public boolean after(Date when) {
        return time > when.time;
    }

    /**
     * Tests if this date is before the specified date.
     * @param when the other date
     * @return true, if the date represented by when is strictly later
     * than the time represented by this object.
     */
    public boolean before(Date when) {
        return time < when.time;
    }

    /**
     * Compares two dates for equality.
     * @param obj the object to compare.
     * @return true, if obj is a Date object and the date represented
     * by obj is exactly the same as the time represented by this
     * object.  
     */
    public boolean equals(Object obj) {
        return (obj instanceof Date
                && time == ((Date)obj).time);
    }

    /**
     * Compares two dates.
     * @param when the other date.
     * @return 0, if the date represented
     * by obj is exactly the same as the time represented by this
     * object, a negative if this Date is before the other Date, and
     * a positive value otherwise.  
     */
    public int compareTo(Date when) {
        return  (time < when.time) ? -1 : (time == when.time) ? 0 : 1;
    }

    /**
     * Compares this Date to another.  This behaves like
     * <code>compareTo(Date)</code>, but it may throw a
     * <code>ClassCastException</code>
     * @param obj the other date.
     * @return 0, if the date represented
     * by obj is exactly the same as the time represented by this
     * object, a negative if this Date is before the other Date, and
     * a positive value otherwise.  
     * @throws ClassCastException if obj is not of type Date.
     */
    public int compareTo(Object obj) {
        return  compareTo((Date)obj);
    }

    public int hashCode() {
        return (int) time ^ (int) (time >>> 32);
    }

    public String toString() {
        return java.text.DateFormat.getInstance().format(this);
    }

    public static long parse(String s) {
        try {
            return java.text.DateFormat.getInstance().parse(s).time;
        } catch (java.text.ParseException ex) {
            return 0;
        }
    }

    /**
     * Reads an Object from the stream.
     */
    private void readObject(java.io.ObjectInputStream input) 
        throws java.io.IOException, ClassNotFoundException {
        input.defaultReadObject();
        time = input.readLong();
    }

    /**
     * Writes an Object to the stream.
     * @serialdata A long value representing the offset from the epoch
     * in milliseconds.  This is the same value that is returned by the
     * method getTime().
     */
    private void readObject(java.io.ObjectOutputStream output) 
        throws java.io.IOException {
        output.defaultWriteObject();
        output.writeLong(time);
    }
}
