/*************************************************************************
/* SimpleDateFormat.java -- A class for parsing/formating simple date constructs
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

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

// Note that readObject still needs to be written for this class.

/**
 * SimpleDateFormat provides convenient methods for parsing and formatting
 * dates using Gregorian calendars (see java.util.GregorianCalendar). 
 */
public class SimpleDateFormat extends DateFormat 
{
  /** A pair class used by SimpleDateFormat as a compiled representation
   *  of a format string.
   */
  private class FieldSizePair 
  {
    public int field;
    public int size;

    /** Constructs a pair with the given field and size values */
    public FieldSizePair(int f, int s) {
      field = f;
      size = s;
    }
  }

  private Vector tokens;
  private DateFormatSymbols formatData;  // formatData
  private Date defaultCenturyStart = new Date(-1608614014805L);
  private String pattern;
  private int serialVersionOnStream = 1; // 0 indicates JDK1.1.3 or earlier

  private void compileFormat(String pattern) 
  {
    // Any alphabetical characters are treated as pattern characters
    // unless enclosed in single quotes.

    char thisChar;
    int pos;
    int field;
    FieldSizePair current = null;

    for (int i=0; i<pattern.length(); i++) {
      thisChar = pattern.charAt(i);
      field = formatData.getLocalPatternChars().indexOf(thisChar);
      if (field == -1) {
	current = null;
	if (Character.isLetter(thisChar)) {
	  // Not a valid letter
	  tokens.addElement(new FieldSizePair(-1,0));
	} else if (thisChar == '\'') {
	  // Quoted text section; skip to next single quote
	  pos = pattern.indexOf('\'',i+1);
	  if (pos == -1) {
	    // This ought to be an exception, but spec does not
	    // let us throw one.
	    tokens.addElement(new FieldSizePair(-1,0));
	  }
	  if ((pos+1 < pattern.length()) && (pattern.charAt(pos+1) == '\'')) {
	    tokens.addElement(pattern.substring(i+1,pos+1));
	  } else {
	    tokens.addElement(pattern.substring(i+1,pos));
	  }
	  i = pos;
	} else {
	  // A special character
	  tokens.addElement(new Character(thisChar));
	}
      } else {
	// A valid field
	if ((current != null) && (field == current.field)) {
	  current.size++;
	} else {
	  current = new FieldSizePair(field,1);
	  tokens.addElement(current);
	}
      }
    }
  }
    
  public String toString() 
  {
    StringBuffer output = new StringBuffer();
    Enumeration e = tokens.elements();
    while (e.hasMoreElements()) {
      output.append(e.nextElement().toString());
    }
    return output.toString();
  }
      
  /**
   * Constructs a SimpleDateFormat using the default pattern for
   * the default locale.
   */
  public SimpleDateFormat() 
  {
    /*
     * There does not appear to be a standard API for determining 
     * what the default pattern for a locale is, so use package-scope
     * variables in DateFormatSymbols to encapsulate this.
     */
    super();
    calendar = new GregorianCalendar();
    tokens = new Vector();
    formatData = new DateFormatSymbols();
    compileFormat(formatData.dateFormats[DEFAULT]+' '+formatData.timeFormats[DEFAULT]);
    this.pattern = pattern;
  }
  
  /**
   * Creates a date formatter using the specified pattern, with the default
   * DateFormatSymbols for the default locale.
   */
  public SimpleDateFormat(String pattern) 
  {
    this(pattern,new DateFormatSymbols());
  }

  /**
   * Creates a date formatter using the specified pattern, with the default
   * DateFormatSymbols for the given locale.
   */
  public SimpleDateFormat(String pattern, Locale locale) 
  {
    this(pattern,new DateFormatSymbols(locale));
  }

  /**
   * Creates a date formatter using the specified pattern. The
   * specified DateFormatSymbols will be used when formatting.
   */
  public SimpleDateFormat(String pattern, DateFormatSymbols formatData) {
    super();
    calendar = new GregorianCalendar();
    tokens = new Vector();
    this.formatData = formatData;
    compileFormat(pattern);
    this.pattern = pattern;
  }

  // What is the difference between localized and unlocalized?  The
  // docs don't say.

  /**
   * This method returns a string with the formatting pattern being used
   * by this object.  This string is unlocalized.
   *
   * @return The format string.
   */
  public String
  toPattern()
  {
    return(pattern);
  }

  /**
   * This method returns a string with the formatting pattern being used
   * by this object.  This string is localized.
   *
   * @return The format string.
   */
  public String
  toLocalizedPattern()
  {
    return(pattern);
  }

  /**
   * This method sets the formatting pattern that should be used by this
   * object.  This string is not localized.
   *
   * @param pattern The new format pattern.
   */
  public void
  applyPattern(String pattern)
  {
    tokens = new Vector();
    compileFormat(pattern);
    this.pattern = pattern;
  }

  /**
   * This method sets the formatting pattern that should be used by this
   * object.  This string is localized.
   *
   * @param pattern The new format pattern.
   */
  public void
  applyLocalizedPattern(String pattern)
  {
    tokens = new Vector();
    compileFormat(pattern);
    this.pattern = pattern;
  }

  /** 
   * Returns the start of the century used for two digit years.
   *
   * @return A <code>Date</code> representing the start of the century
   * for two digit years.
   */
  public Date
  get2DigitYearStart()
  {
    return(defaultCenturyStart);
  }

  /**
   * Sets the start of the century used for two digit years.
   *
   * @param date A <code>Date</code> representing the start of the century for
   * two digit years.
   */
  public void
  set2DigitYearStart(Date date)
  {
    defaultCenturyStart = date;
  }

  /**
   * This method returns the format symbol information used for parsing
   * and formatting dates.
   *
   * @return The date format symbols.
   */
  public DateFormatSymbols
  getDateFormatSymbols()
  {
    return(formatData);
  }

  /**
   * This method sets the format symbols information used for parsing
   * and formatting dates.
   *
   * @param formatData The date format symbols.
   */
   public void
   setDateFormatSymbols(DateFormatSymbols formatData)
   {
     this.formatData = formatData;
   }

  /**
   * This methods tests whether the specified object is equal to this
   * object.  This will be true if and only if the specified object:
   * <p>
   * <ul>
   * <li>Is not <code>null</code>.
   * <li>Is an instance of <code>SimpleDateFormat</code>.
   * <li>Is equal to this object at the superclass (i.e., <code>DateFormat</code>)
   *     level.
   * <li>Has the same formatting pattern.
   * <li>Is using the same formatting symbols.
   * <li>Is using the same century for two digit years.
   * </ul>
   *
   * @param obj The object to compare for equality against.
   *
   * @return <code>true</code> if the specified object is equal to this object,
   * <code>false</code> otherwise.
   */
  public boolean
  equals(Object o)
  {
    if (o == null)
      return(false);

    if (!super.equals(o))
      return(false);

    if (!(o instanceof SimpleDateFormat))
      return(false);

    SimpleDateFormat sdf = (SimpleDateFormat)o;

    if (!toPattern().equals(sdf.toPattern()))
      return(false);

    if (!get2DigitYearStart().equals(sdf.get2DigitYearStart()))
      return(false);

    if (!getDateFormatSymbols().equals(sdf.getDateFormatSymbols()))
      return(false);

    return(true);
  }


  /**
   * Formats the date input according to the format string in use,
   * appending to the specified StringBuffer.  The input StringBuffer
   * is returned as output for convenience.
   */
  public StringBuffer format(Date date, StringBuffer buffer, FieldPosition z) {
    String temp;
    Calendar theCalendar = (Calendar) calendar.clone();
    theCalendar.setTime(date);
    
    // go through vector, filling in fields where applicable, else toString
    Enumeration e = tokens.elements();
    while (e.hasMoreElements()) {
      Object o = e.nextElement();
      if (o instanceof FieldSizePair) {
	FieldSizePair p = (FieldSizePair) o;
	switch (p.field) {
	case ERA_FIELD:
	  buffer.append(formatData.eras[theCalendar.get(Calendar.ERA)]);
	  break;
	case YEAR_FIELD:
	  temp = String.valueOf(theCalendar.get(Calendar.YEAR));
	  if (p.size < 4)
	    buffer.append(temp.substring(temp.length()-2));
	  else
	    buffer.append(temp);
	  break;
	case MONTH_FIELD:
	  if (p.size < 3)
	    withLeadingZeros(theCalendar.get(Calendar.MONTH)+1,p.size,buffer);
	  else if (p.size < 4)
	    buffer.append(formatData.shortMonths[theCalendar.get(Calendar.MONTH)]);
	  else
	    buffer.append(formatData.months[theCalendar.get(Calendar.MONTH)]);
	  break;
	case DATE_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.DATE),p.size,buffer);
	  break;
	case HOUR_OF_DAY1_FIELD:  // 1-12
	  withLeadingZeros(theCalendar.get(Calendar.HOUR),p.size,buffer);
	  break;
	case HOUR_OF_DAY0_FIELD: // 0-23
	  withLeadingZeros(theCalendar.get(Calendar.HOUR_OF_DAY),p.size,buffer);
	  break;
	case MINUTE_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.MINUTE),p.size,buffer);
	  break;
	case SECOND_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.SECOND),p.size,buffer);
	  break;
	case MILLISECOND_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.MILLISECOND),p.size,buffer);
	  break;
	case DAY_OF_WEEK_FIELD:
	  if (p.size < 4)
	    buffer.append(formatData.shortWeekdays[theCalendar.get(Calendar.DAY_OF_WEEK)]);
	  else
	    buffer.append(formatData.weekdays[theCalendar.get(Calendar.DAY_OF_WEEK)]);
	  break;
	case DAY_OF_YEAR_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.DAY_OF_YEAR),p.size,buffer);
	  break;
	case DAY_OF_WEEK_IN_MONTH_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH),p.size,buffer);
	  break;
	case WEEK_OF_YEAR_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.WEEK_OF_YEAR),p.size,buffer);
	  break;
	case WEEK_OF_MONTH_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.WEEK_OF_MONTH),p.size,buffer);
	  break;
	case AM_PM_FIELD:
	  buffer.append(formatData.ampms[theCalendar.get(Calendar.AM_PM)]);
	  break;
	case HOUR1_FIELD: // 1-24
	  withLeadingZeros(theCalendar.get(Calendar.HOUR_OF_DAY)+1,p.size,buffer);
	  break;
	case HOUR0_FIELD: // 0-11
	  withLeadingZeros(theCalendar.get(Calendar.HOUR)-1,p.size,buffer);
	  break;
	case TIMEZONE_FIELD:
	  // TODO
	  break;
	default:
	  throw new IllegalArgumentException("Illegal pattern character");
	}
      } else {
	buffer.append(o.toString());
      }
    }
    return buffer;
  }

  private void withLeadingZeros(int value, int length, StringBuffer buffer) {
    String valStr = String.valueOf(value);
    for (length -= valStr.length(); length > 0; length--)
      buffer.append('0');
    buffer.append(valStr);
  }

  private int indexInArray(String dateStr, int index, String[] values) {
    int l1 = dateStr.length()-index;
    int l2;

    for (int i=0; i<values.length; i++) {
      if (values[i] == null)
        continue;

      l2 = values[i].length();
      if ((l1 <= l2) && (dateStr.substring(index,index+l2).equals(values[i])))
	return i;
    }
    return -1;
  }

  // IN PROGRESS
  public Date parse(String dateStr, ParsePosition pos) {
    // start looking at position pos.index
    Enumeration e = tokens.elements();
    Calendar theCalendar = (Calendar) calendar.clone();
    theCalendar.clear();
    theCalendar.setTime(new Date(0));
    int value;
    while (pos.getIndex() < dateStr.length()) {
      Object o = e.nextElement();
      if (o instanceof FieldSizePair) {
	FieldSizePair p = (FieldSizePair) o;
	switch (p.field) {
	case ERA_FIELD:
	  value = indexInArray(dateStr,pos.getIndex(),formatData.eras);
	  if (value == -1) {
	    pos.setErrorIndex(pos.getIndex());
	    return null;
	  }
	  pos.setIndex(pos.getIndex() + formatData.eras[value].length());
	  theCalendar.set(Calendar.ERA,value);
	  break;
	  /*
	case YEAR_FIELD:
	  temp = String.valueOf(theCalendar.get(Calendar.YEAR));
	  if (p.size < 4)
	    buffer.append(temp.substring(temp.length()-2));
	  else
	    buffer.append(temp);
	  break;
	case MONTH_FIELD:
	  if (p.size < 3)
	    withLeadingZeros(theCalendar.get(Calendar.MONTH),p.size,buffer);
	  else if (p.size < 4)
	    buffer.append(formatData.shortMonths[theCalendar.get(Calendar.MONTH)]);
	  else
	    buffer.append(formatData.months[theCalendar.get(Calendar.MONTH)]);
	  break;
	case DATE_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.DATE),p.size,buffer);
	  break;
	case HOUR_OF_DAY1_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.HOUR_OF_DAY)+1,p.size,buffer);
	  break;
	case HOUR_OF_DAY0_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.HOUR_OF_DAY),p.size,buffer);
	  break;
	case MINUTE_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.MINUTE),p.size,buffer);
	  break;
	case SECOND_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.SECOND),p.size,buffer);
	  break;
	case MILLISECOND_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.MILLISECOND),p.size,buffer);
	  break;
	  */
	case DAY_OF_WEEK_FIELD:
	  value = indexInArray(dateStr,pos.getIndex(),(p.size < 4) ? formatData.shortWeekdays : formatData.weekdays);
	  if (value == -1) {
	    pos.setErrorIndex(pos.getIndex());
	    return null;
	  }
	  pos.setIndex(pos.getIndex() + ((p.size < 4) ? formatData.shortWeekdays[value].length()
	    : formatData.weekdays[value].length()));
	  // Note: Calendar.set(Calendar.DAY_OF_WEEK,value) does not work
	  // as implemented in jdk1.1.5 (possibly DAY_OF_WEEK is meant to
	  // be read-only). Instead, calculate number of days offset.
	  theCalendar.add(Calendar.DATE,value 
			  - theCalendar.get(Calendar.DAY_OF_WEEK));
	  // in JDK, this seems to clear the hours, so we'll do the same.
	  theCalendar.set(Calendar.HOUR_OF_DAY,0);
	  break;
	  /*
	case DAY_OF_YEAR_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.DAY_OF_YEAR),p.size,buffer);
	  break;
	case DAY_OF_WEEK_IN_MONTH_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH),p.size,buffer);
	  break;
	case WEEK_OF_YEAR_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.WEEK_OF_YEAR),p.size,buffer);
	  break;
	case WEEK_OF_MONTH_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.WEEK_OF_MONTH),p.size,buffer);
	  break;
	  */
	case AM_PM_FIELD:
	  value = indexInArray(dateStr,pos.getIndex(),formatData.ampms);
	  if (value == -1) {
	    pos.setErrorIndex(pos.getIndex());
	    return null;
	  }
	  pos.setIndex(pos.getIndex() + formatData.ampms[value].length());
	  theCalendar.set(Calendar.AM_PM,value);
	  break;
	  /*
	case HOUR1_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.HOUR)+1,p.size,buffer);
	  break;
	case HOUR0_FIELD:
	  withLeadingZeros(theCalendar.get(Calendar.HOUR),p.size,buffer);
	  break;
	case TIMEZONE_FIELD:
	  // TODO
	  break;
	  */
	default:
	  throw new IllegalArgumentException("Illegal pattern character");
	} // end switch
      } else if (o instanceof String) {
	String ostr = (String) o;
	if (dateStr.substring(pos.getIndex(),pos.getIndex()+ostr.length()).equals(ostr)) {
	  pos.setIndex(pos.getIndex() + ostr.length());
	} else {
	  pos.setErrorIndex(pos.getIndex());
	  return null;
	}
      } else if (o instanceof Character) {
	Character ochar = (Character) o;
	if (dateStr.charAt(pos.getIndex()) == ochar.charValue()) {
	  pos.setIndex(pos.getIndex() + 1);
	} else {
	  pos.setErrorIndex(pos.getIndex());
	  return null;
	}
      }
    }
    return theCalendar.getTime();
  }
}




