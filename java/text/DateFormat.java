/*************************************************************************
/* DateFormat.java -- Class for formatting/parsing date/times
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Wes Biggs (wbiggs@la.usweb.net)
/*        and Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.text;

import java.util.TimeZone;
import java.util.Locale;
import java.util.Date;

public abstract class DateFormat extends Format implements java.io.Serializable
{
  protected java.util.Calendar calendar;
  protected NumberFormat numberFormat;
  
  private boolean m_lenient = true;

  /* These constants need to have these exact values.  They
   * correspond to index positions within the localPatternChars
   * string for a given locale.  For example, the US locale uses
   * the string "GyMdkHmsSEDFwWahKz", where 'G' is the character
   * for era, 'y' for year, and so on down to 'z' for time zone.
   */
  public static final int ERA_FIELD = 0;
  public static final int YEAR_FIELD = 1;
  public static final int MONTH_FIELD = 2;
  public static final int DATE_FIELD = 3;  // sic, should be "DAY_OF_MONTH"...
  public static final int HOUR_OF_DAY1_FIELD = 4;
  public static final int HOUR_OF_DAY0_FIELD = 5;
  public static final int MINUTE_FIELD = 6;
  public static final int SECOND_FIELD = 7;
  public static final int MILLISECOND_FIELD = 8;
  public static final int DAY_OF_WEEK_FIELD = 9;
  public static final int DAY_OF_YEAR_FIELD = 10;
  public static final int DAY_OF_WEEK_IN_MONTH_FIELD = 11;
  public static final int WEEK_OF_YEAR_FIELD = 12;
  public static final int WEEK_OF_MONTH_FIELD = 13;
  public static final int AM_PM_FIELD = 14;
  public static final int HOUR1_FIELD = 15;
  public static final int HOUR0_FIELD = 16;
  public static final int TIMEZONE_FIELD = 17;

  public static final int FULL = 0;
  public static final int LONG = 1;
  public static final int MEDIUM = 2;
  public static final int SHORT = 3;
  public static final int DEFAULT = 4;

  /**
   * This method initializes a new instance of <code>DateFormat</code>.
   */
  protected DateFormat() {
  }

  /**
   * This method formats the specified <code>Object</code> into a date string
   * and appends it to the specified <code>StringBuffer</code>.
   * The specified object must be an instance of <code>Number</code> or
   * <code>Date</code> or an <code>IllegalArgumentException</code> will be
   * thrown.
   *
   * @param obj The <code>Object</code> to format.
   * @param toAppendTo The <code>StringBuffer</code> to append the resultant
   * <code>String</code> to.
   * @param fieldPosition Is updated to the start and end index of the
   * specified field.
   *
   * @return The <code>StringBuffer</code> supplied on input, with the
   * formatted date/time appended.
   */
  public final StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition fieldPosition)
  {
    if (obj instanceof Number)
      obj = new Date(((Number)obj).longValue());

    if (!(obj instanceof Date))
      throw new IllegalArgumentException("Invalid object type: " + obj);

    return(format((Date)obj, toAppendTo, fieldPosition));
  }

  /**
   * This method formats a <code>Date</code> into a string and appends it
   * to the specified <code>StringBuffer</code>.
   *
   * @param date The <code>Date</code> value to format.
   * @param toAppendTo The <code>StringBuffer</code> to append the resultant
   * <code>String</code> to.
   * @param fieldPosition Is updated to the start and end index of the
   * specified field.
   *
   * @return The <code>StringBuffer</code> supplied on input, with the
   * formatted date/time appended.
   */
  public abstract StringBuffer format(Date date, StringBuffer toAppendTo, 
                                      FieldPosition fieldPosition);

  /**  
    * Formats the date argument according to the pattern specified. 
    *
    * @param The formatted date.
    */
  public final String format(Date date)
  {
    // call the StringBuffer abstract method
    StringBuffer buffer = new StringBuffer();
    return format(date,buffer,new FieldPosition(0)).toString();
  }

  /**
   * This method parses the specified date/time string.
   *
   * @return The resultant date.
   *
   * @exception ParseException If the specified string cannot be parsed.
   */
  public Date parse(String text) throws ParseException
  {
    return parse(text,new ParsePosition(0));
  }

  /** 
   * This method parses the specified <code>String</code> into a 
   * <code>Date</code>.  The <code>pos</code> argument contains the
   * starting parse position on method entry and the ending parse
   * position on method exit.
   *
   * @param text The string to parse.
   * @param pos The starting parse position in entry, the ending parse
   * position on exit.
   *
   * @return The parsed date, or <code>null</code> if the string cannot
   * be parsed.
   */
  public abstract Date parse(String text, ParsePosition pos);

  /**
   * This method is identical to <code>parse(String, ParsePosition)</code>,
   * but returns its result as an <code>Object</code> instead of a
   * <code>Date</code>.
   * 
   * @param source The string to parse.
   * @param pos The starting parse position in entry, the ending parse
   * position on exit.
   *
   * @return The parsed date, or <code>null</code> if the string cannot
   * be parsed.
   */
  public Object parseObject(String source, ParsePosition pos) 
  {
    return parse(source,pos);
  }

 /**
   * This method returns an instance of <code>DateFormat</code> that will
   * format using the default formatting style for times.
   *
   * @return A new <code>DateFormat</code> instance.
   */
  public static final DateFormat getTimeInstance()
  {
    return getTimeInstance(DEFAULT, Locale.getDefault());
  }

  /**
   * This method returns an instance of <code>DateFormat</code> that will
   * format using the specified formatting style for times.
   *
   * @param style The type of formatting to perform. 
   * 
   * @return A new <code>DateFormat</code> instance.
   */
  public static final DateFormat getTimeInstance(int style)
  {
    return getTimeInstance(style, Locale.getDefault());
  }

  /**
   * This method returns an instance of <code>DateFormat</code> that will
   * format using the specified formatting style for times.  The specified
   * localed will be used in place of the default.
   *
   * @param style The type of formatting to perform. 
   * @param aLocale The desired locale.
   * 
   * @return A new <code>DateFormat</code> instance.
   */
  public static final DateFormat getTimeInstance(int style, Locale aLocale)
  {
    switch(style)
     {
       case SHORT:
       case MEDIUM:
       case LONG:
       case FULL:
       case DEFAULT:
         break;

       default:
         throw new IllegalArgumentException("Bad style: " + style);
     }

    DateFormatSymbols dfs = new DateFormatSymbols(aLocale);

    return new SimpleDateFormat(dfs.timeFormats[style],dfs);
  }

 /**
   * This method returns an instance of <code>DateFormat</code> that will
   * format using the default formatting style for dates.
   *
   * @return A new <code>DateFormat</code> instance.
   */
  public static final DateFormat getDateInstance()
  {
    return getDateInstance(DEFAULT, Locale.getDefault());
  }

  /**
   * This method returns an instance of <code>DateFormat</code> that will
   * format using the specified formatting style for dates.
   *
   * @param style The type of formatting to perform. 
   * 
   * @return A new <code>DateFormat</code> instance.
   */
  public static final DateFormat getDateInstance(int style)
  {
    return getDateInstance(style, Locale.getDefault());
  }

  /**
   * This method returns an instance of <code>DateFormat</code> that will
   * format using the specified formatting style for dates.  The specified
   * localed will be used in place of the default.
   *
   * @param style The type of formatting to perform. 
   * @param aLocale The desired locale.
   * 
   * @return A new <code>DateFormat</code> instance.
   */
  public static final DateFormat getDateInstance(int style, Locale aLocale)
  {
    switch(style)
     {
       case SHORT:
       case MEDIUM:
       case LONG:
       case FULL:
       case DEFAULT:
         break;

       default:
         throw new IllegalArgumentException("Bad style: " + style);
     }

    DateFormatSymbols dfs = new DateFormatSymbols(aLocale);
    return new SimpleDateFormat(dfs.dateFormats[style],dfs);
  }

  /**
   * This method returns a new instance of <code>DateFormat</code> that
   * formats both dates and times using the <code>SHORT</code> style.
   *
   * @return A new <code>DateFormat</code>instance.
   */
  public static final DateFormat getInstance()
  {
    return getDateTimeInstance(SHORT, SHORT, Locale.getDefault());
  }

  /**
   * This method returns a new instance of <code>DateFormat</code> that
   * formats both dates and times using the <code>DEFAULT</code> style.
   *
   * @return A new <code>DateFormat</code>instance.
   */
  public static final DateFormat getDateTimeInstance()
  {
    return getDateTimeInstance(DEFAULT, DEFAULT, Locale.getDefault());
  }

  /**
   * This method returns a new instance of <code>DateFormat</code> that
   * formats both dates and times using the specified styles.
   * 
   * @param dateStyle The desired style for date formatting.
   * @param timeStyle The desired style for time formatting
   *
   * @return A new <code>DateFormat</code>instance.
   */
  public static final DateFormat getDateTimeInstance(int dateStyle, 
                                                     int timeStyle)
  {
    return getDateTimeInstance(dateStyle, timeStyle, Locale.getDefault());
  }

  public static final DateFormat getDateTimeInstance(int dateStyle, 
                                                     int timeStyle, 
                                                     Locale aLocale)
  {
    switch(dateStyle)
     {
       case SHORT:
       case MEDIUM:
       case LONG:
       case FULL:
       case DEFAULT:
         break;

       default:
         throw new IllegalArgumentException("Bad style: " + dateStyle);
     }

    switch(dateStyle)
     {
       case SHORT:
       case MEDIUM:
       case LONG:
       case FULL:
       case DEFAULT:
         break;

       default:
         throw new IllegalArgumentException("Bad style: " + timeStyle);
     }

    DateFormatSymbols dfs = new DateFormatSymbols(aLocale);
    return new SimpleDateFormat(dfs.dateFormats[dateStyle] + " "
                               +dfs.timeFormats[timeStyle], dfs);
  }

  /**
   * This method returns a list of available locales supported by this
   * class.
   */
  public static java.util.Locale[] getAvailableLocales()
  {
    //****** Just hardcode for now
    Locale[] l = new Locale[1];
    l[0] = Locale.getDefault();

    return(l);
  }

  /**
   * This method specified the <code>Calendar</code> that should be used 
   * by this object to parse/format datetimes.
   *
   * @param The new <code>Calendar</code> for this object.
   *
   * @see java.util.Calendar
   */
  public void setCalendar(java.util.Calendar newCalendar)
  {
    calendar = newCalendar;
  }

  /**
    * This method returns the <code>Calendar</code> object being used by
    * this object to parse/format datetimes.
    *
    * @return The <code>Calendar</code> being used by this object.
    *
    * @see java.util.Calendar
    */
  public java.util.Calendar getCalendar()
  {
    return calendar;
  }

  /**
   * This method specifies the <code>NumberFormat</code> object that should
   * be used by this object to parse/format times.
   *
   * @param The <code>NumberFormat</code> in use by this object.
   */
  public void setNumberFormat(NumberFormat newNumberFormat)
  {
    numberFormat = newNumberFormat;
  }

  /**
   * This method returns the <code>NumberFormat</code> object being used
   * by this object to parse/format time values.
   *
   * @return The <code>NumberFormat</code> in use by this object.
   */
  public NumberFormat getNumberFormat()
  {
    return numberFormat;
  }

  /**
   * This method sets the time zone that should be used by this object.
   *
   * @param The new time zone.
   */
  public void setTimeZone(TimeZone zone)
  {
    calendar.setTimeZone(zone);
  }

  /**
   * This method returns the <code>TimeZone</code> object being used by
   * this instance.
   *
   * @return The time zone in use.
   */
  public TimeZone getTimeZone()
  {
    return calendar.getTimeZone();
  }

  /**
   * This method specifies whether or not this object should be lenient in 
   * the syntax it accepts while parsing date/time values.
   *
   * @param lenient <code>true</code> if parsing should be lenient,
   * <code>false</code> otherwise.
   */
  public void setLenient(boolean lenient)
  {
    m_lenient = lenient;
  }

  /**
   * This method indicates whether or not the parsing of date and time
   * values should be done in a lenient value.
   *
   * @return <code>true</code> if date/time parsing is lenient,
   * <code>false</code> otherwise.
   */
  public boolean isLenient()
  {
    return m_lenient;
  }

  /**
   * This method returns a hash value for this object.
   * 
   * @return A hash value for this object.
   */
  public int hashCode()
  {
    return(System.identityHashCode(this));
  }

  /**
   * This method tests this object for equality against the specified object.
   * The two objects will be considered equal if an only if the specified
   * object:
   * <P>
   * <ul>
   * <li>Is not <code>null</code>.
   * <li>Is an instance of <code>DateFormat</code>.
   * <li>Has the same calendar field value as this object.
   * </ul>
   *
   * @param obj The object to test for equality against.
   * 
   * @return <code>true</code> if the specified object is equal to this object,
   * <code>false</code> otherwise.
  public boolean equals(Object obj)
  {
    if (obj == null)
      return(false);

    if (!(obj instanceof DateFormat))
      return(false);

    DateFormat df = (DateFormat)obj;
    if (!df.calendar.equals(calendar))
      return(false);

    return(true);
  }

  /**
   * This method returns a copy of this object.
   *
   * @return A copy of this object.
   */
  public Object clone()
  {
    return(super.clone());
  }

} // class DateFormat

