/* java.util.Date
   Copyright (C) 1998, 1999, 2000 Free Software Foundation, Inc.

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
public class Date implements Cloneable, Comparable, java.io.Serializable
{
  /**
   * This is the serialization UID for this class
   */
  private static final long serialVersionUID = 7523967970034938905L;

  /**
   * The time in milliseconds since the epoch.
   */
  private transient long time;

  /**
   * Creates a new Date Object representing the current time.
   */
  public Date()
  {
    time = System.currentTimeMillis();
  }

  /**
   * Creates a new Date Object representing the given time.
   * @param time the time in milliseconds since the epoch.
   */
  public Date(long time)
  {
    this.time = time;
  }

  /**
   * Creates a new Date Object representing the given time.
   * @deprecated use <code>new GregorianCalendar(year+1900, month,
   * day)</code> instead.  
   */
  public Date(int year, int month, int day)
  {
    time = new GregorianCalendar(year + 1900, month, day).getTimeInMillis();
  }

  /**
   * Creates a new Date Object representing the given time.
   * @deprecated use <code>new GregorianCalendar(year+1900, month,
   * day, hour, min)</code> instead.  
   */
  public Date(int year, int month, int day, int hour, int min)
  {
    time =
      new GregorianCalendar(year + 1900, month, day, hour,
			    min).getTimeInMillis();
  }

  /*
   * Creates a new Date Object representing the given time.
   * @deprecated use <code>new GregorianCalendar(year+1900, month,
   * day)</code> instead.  
   */
  public Date(int year, int month, int day, int hour, int min, int sec)
  {
    time =
      new GregorianCalendar(year + 1900, month, day, hour, min,
			    sec).getTimeInMillis();
  }

  /**
   * Creates a new Date from the given string representation.  This
   * does the same as <code>new Date(Date.parse(s))</code>
   * @see #parse
   * @deprecated use <code>java.text.DateFormat.parse(s)</code> instead.  
   */
  public Date(String s)
  {
    time = parse(s);
  }

  public Object clone()
  {
    try
      {
	return super.clone();
      }
    catch (CloneNotSupportedException ex)
      {
	return null;
      }
  }

  /**
   * @deprecated Use Calendar with a UTC TimeZone instead.
   * @return the time in millis since the epoch.
   */
  public static long UTC(int year, int month, int date,
			 int hrs, int min, int sec)
  {
    GregorianCalendar cal =
      new GregorianCalendar(year + 1900, month, date, hrs, min, sec);
    cal.set(Calendar.ZONE_OFFSET, 0);
    cal.set(Calendar.DST_OFFSET, 0);
    return cal.getTimeInMillis();
  }

  /**
   * Gets the time represented by this Object
   * @return the time in milliseconds since the epoch.
   */
  public long getTime()
  {
    return time;
  }

  /**
   * @deprecated use
   * Calendar.get(Calendar.ZONE_OFFSET)+Calendar.get(Calendar.DST_OFFSET)
   * instead.
   * @return The time zone offset in minutes of the local time zone
   * relative to UTC.  The time represented by this object is used to
   * determine if we should use daylight savings.
   */
  public int getTimezoneOffset()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return (cal.get(Calendar.ZONE_OFFSET)
	    + cal.get(Calendar.DST_OFFSET)) / (60 * 1000);
  }

  /**
   * Sets the time which this Object should represented.
   * @param time the time in milliseconds since the epoch.  */
  public void setTime(long time)
  {
    this.time = time;
  }

  /**
   * Tests if this date is after the specified date.
   * @param when the other date
   * @return true, if the date represented by this Object is
   * strictly later than the time represented by when.  
   */
  public boolean after(Date when)
  {
    return time > when.time;
  }

  /**
   * Tests if this date is before the specified date.
   * @param when the other date
   * @return true, if the date represented by when is strictly later
   * than the time represented by this object.
   */
  public boolean before(Date when)
  {
    return time < when.time;
  }

  /**
   * Compares two dates for equality.
   * @param obj the object to compare.
   * @return true, if obj is a Date object and the date represented
   * by obj is exactly the same as the time represented by this
   * object.  
   */
  public boolean equals(Object obj)
  {
    return (obj instanceof Date && time == ((Date) obj).time);
  }

  /**
   * Compares two dates.
   * @param when the other date.
   * @return 0, if the date represented
   * by obj is exactly the same as the time represented by this
   * object, a negative if this Date is before the other Date, and
   * a positive value otherwise.  
   */
  public int compareTo(Date when)
  {
    return (time < when.time) ? -1 : (time == when.time) ? 0 : 1;
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
   * @exception ClassCastException if obj is not of type Date.
   */
  public int compareTo(Object obj)
  {
    return compareTo((Date) obj);
  }

  public int hashCode()
  {
    return (int) time ^ (int) (time >>> 32);
  }

  private String[] weekNames = { "Sun", "Mon", "Tue", "Wed",
				 "Thu", "Fri", "Sat" };
  
  private String[] monthNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
				  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
  
  public String toString()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    String day = "0" + cal.get(Calendar.DATE);
    String hour = "0" + cal.get(Calendar.HOUR_OF_DAY);
    String min = "0" + cal.get(Calendar.MINUTE);
    String sec = "0" + cal.get(Calendar.SECOND);
    String year = "000" + cal.get(Calendar.YEAR);
    return weekNames[cal.get(Calendar.DAY_OF_WEEK) - 1] + " "
      + monthNames[cal.get(Calendar.MONTH)] + " "
      + day.substring(day.length() - 2) + " "
      + hour.substring(hour.length() - 2) + ":"
      + min.substring(min.length() - 2) + ":"
      + sec.substring(sec.length() - 2) + " "
      +
      cal.getTimeZone().getDisplayName(cal.getTimeZone().inDaylightTime(this),
				       TimeZone.SHORT) + " " +
      year.substring(year.length() - 4);
  }

  public String toLocaleString()
  {
    return java.text.DateFormat.getInstance().format(this);
  }

  public String toGMTString()
  {
    java.text.DateFormat format = java.text.DateFormat.getInstance();
    format.setTimeZone(TimeZone.getTimeZone("GMT"));
    return format.format(this);
  }

  public static long parse(String s)
  {
    try
      {
	return java.text.DateFormat.getInstance().parse(s).time;
      }
    catch (java.text.ParseException ex)
      {
	return 0;
      }
  }

  /**
   * @return the year minus 1900 represented by this date object.
   * @deprecated Use Calendar instead of Date, and use get(Calendar.YEAR)
   * instead.  Note about the 1900 difference in year.
   */
  public int getYear()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return cal.get(Calendar.YEAR) - 1900;
  }

  /**
   * Sets the year to year minus 1900, not changing the other fields.
   * @param year the year minus 1900.
   * @deprecated Use Calendar instead of Date, and use
   * set(Calendar.YEAR, year) instead.  Note about the 1900
   * difference in year.  
   */
  public void setYear(int year)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    cal.set(Calendar.YEAR, 1900 + year);
    time = cal.getTimeInMillis();
  }

  /**
   * @return the month represented by this date object (zero based).
   * @deprecated Use Calendar instead of Date, and use get(Calendar.MONTH)
   * instead.
   */
  public int getMonth()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return cal.get(Calendar.MONTH);
  }

  /**
   * Sets the month to the given value, not changing the other fields.
   * @param month the month, zero based.
   * @deprecated Use Calendar instead of Date, and use
   * set(Calendar.MONTH, month) instead. 
   */
  public void setMonth(int month)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    cal.set(Calendar.MONTH, month);
    time = cal.getTimeInMillis();
  }

  /**
   * @return the day of month represented by this date object.
   * @deprecated Use Calendar instead of Date, and use get(Calendar.DATE)
   * instead.
   */
  public int getDate()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return cal.get(Calendar.DATE);
  }

  /**
   * Sets the date to the given value, not changing the other fields.
   * @param date the date.
   * @deprecated Use Calendar instead of Date, and use
   * set(Calendar.DATE, date) instead. 
   */
  public void setDate(int date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    cal.set(Calendar.DATE, date);
    time = cal.getTimeInMillis();
  }

  /**
   * @return the day represented by this date object.
   * @deprecated Use Calendar instead of Date, and use get(Calendar.DAY_OF_WEEK)
   * instead.
   */
  public int getDay()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return cal.get(Calendar.DAY_OF_WEEK);
  }

  /**
   * @return the hours represented by this date object.
   * @deprecated Use Calendar instead of Date, and use get(Calendar.HOUR_OF_DAY)
   * instead.
   */
  public int getHours()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return cal.get(Calendar.HOUR_OF_DAY);
  }

  /**
   * Sets the hours to the given value, not changing the other fields.
   * @param hours the hours.
   * @deprecated Use Calendar instead of Date, and use
   * set(Calendar.HOUR_OF_DAY, hours) instead. 
   */
  public void setHours(int hours)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    cal.set(Calendar.HOUR_OF_DAY, hours);
    time = cal.getTimeInMillis();
  }

  /**
   * @return the minutes represented by this date object.
   * @deprecated Use Calendar instead of Date, and use get(Calendar.MINUTE)
   * instead.
   */
  public int getMinutes()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return cal.get(Calendar.MINUTE);
  }

  /**
   * Sets the minutes to the given value, not changing the other fields.
   * @param minutes the minutes.
   * @deprecated Use Calendar instead of Date, and use
   * set(Calendar.MINUTE, minutes) instead. 
   */
  public void setMinutes(int minutes)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    cal.set(Calendar.MINUTE, minutes);
    time = cal.getTimeInMillis();
  }

  /**
   * @return the seconds represented by this date object.
   * @deprecated Use Calendar instead of Date, and use get(Calendar.SECOND)
   * instead.
   */
  public int getSeconds()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    return cal.get(Calendar.SECOND);
  }

  /**
   * Sets the seconds to the given value, not changing the other fields.
   * @param seconds the seconds.
   * @deprecated Use Calendar instead of Date, and use
   * set(Calendar.SECOND, seconds) instead. 
   */
  public void setSeconds(int seconds)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(time);
    cal.set(Calendar.SECOND, seconds);
    time = cal.getTimeInMillis();
  }

  /**
   * Reads an Object from the stream.
   */
  private void readObject(java.io.ObjectInputStream input)
    throws java.io.IOException, ClassNotFoundException
  {
    input.defaultReadObject();
    time = input.readLong();
  }

  /**
   * Writes an Object to the stream.
   * @serialdata A long value representing the offset from the epoch
   * in milliseconds.  This is the same value that is returned by the
   * method getTime().
   */
  private void writeObject(java.io.ObjectOutputStream output)
    throws java.io.IOException
  {
    output.defaultWriteObject();
    output.writeLong(time);
  }
}
