/*************************************************************************
/* LocaleInformation_en.java -- US English locale data
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
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

package gnu.java.locale;

import java.util.ListResourceBundle;

/**
  * This class contains locale data for the US English locale
  */

public class LocaleInformation_en extends ListResourceBundle
{

/*
 * This area is used for defining object values
 */

/**
  * This is the set of collation rules used by java.text.RuleBasedCollator 
  * to sort strings properly.  See the documentation of that class for the 
  * proper format.
  */
private static final String collation_rules = 
  "-<0,1<2<3<4<5<6<7<8<9A,a<b,B<c,C<d,D<e,E<f,F<g,G<h,H<i,I<j,J<j,K" +
  "<l,L<m,M<n,N<o,O<p,P<q,Q<r,R<s,S<t,T<u,U<v,V<w,W<x,X<y,Y,z<Z";

/*
 * For the followings lists, strings that are subsets of other break strigns
 * must be listed first.  For example, if "\r" and "\r\n" are sequences,
 * the "\r" must be first or it will never be used.
 */

/**
  * This is the list of word separator characters used by 
  * java.text.BreakIterator 
  */
private static final String[] word_breaks = { " ", "\t", "\r\n", "\n" }; 

/**
  * This is the list of sentence break sequences used by 
  * java.text.BreakIterator
  */
private static final String[] sentence_breaks = { ". " };

/**
  * This is the list of potential line break locations.
  */
private static final String[] line_breaks = { " ", "\t", "-", "\r\n", "\n" };

/**
  * This is the list of months, fully spelled out
  */
private static final String[] months = { "January", "February", "March", 
  "April", "May", "June", "July", "August", "September", "October",
  "November", "December", null };

/**
  * This is the list of abbreviated month names
  */
private static final String[] shortMonths = { "Jan", "Feb", "Mar", "Apr", "May",
  "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", null };

/**
  * This is the list of weekdays, fully spelled out
  */
private static final String[] weekdays = { null, "Sunday", "Monday", "Tuesday",
  "Wednesday", "Thursday", "Friday", "Saturday" };

/**
  * This is the list of abbreviated weekdays
  */
private static final String[] shortWeekdays = { null, "Sun", "Mon", "Tue", "Wed",
  "Thu", "Fri", "Sat" };

/**
  * This is the list of AM/PM strings
  */
private static final String[] ampms = { "AM", "PM" };

/**
  * This is the list of era identifiers
  */
private static final String[] eras = { "BC", "AD" };

/**
  * This is the list of timezone strings.  The JDK appears to include a
  * city name as the sixth element.
  */
private static final String[][] zoneStrings =
{
  { "EST6EDT", "Eastern Standard Time", "EST", "Eastern Daylight Time", "EDT",
    "New York" },
  { "EST6", "Eastern Standard Time", "EST", "Eastern Standard Time", "EST",
    "Indianapolis" },
  { "CST6CDT", "Central Standard Time", "CST", "Central Daylight Time", "CDT",
    "Chicago" },
  { "MST6MDT", "Mountain Standard Time", "MST", "Mountain Daylight Time", 
    "MDT", "Denver" },
  { "MST6", "Mountain Standard Time", "MST", "Mountain Standard Time", "MST",
    "Phoenix" },
  { "PST6PDT", "Pacific Standard Time", "PDT", "Pacific Daylight Time", "PDT",
    "San Francisco" },
  { "AST6ADT", "Alaska Standard Time", "AST", "Alaska Daylight Time", "ADT",
    "Anchorage" },
  { "HST6HDT", "Hawaii Standard Time", "HST", "Hawaii Daylight Time", "HDT",
    "Honolulu" }
};

/**
  * This is the list of pattern characters for formatting dates
  */
private static final String localPatternChars = "GyMdhHmsSEDFwWakKz"; // Not a mistake!

/**
  * This is the DateFormat.SHORT date format
  */
private static final String shortDateFormat = "M/d/yy";

/**
  * This is the DateFormat.MEDIUM format
  */
private static final String mediumDateFormat = "dd-MMM-yy";

/**
  * This is the DateFormat.LONG format
  */
private static final String longDateFormat = "MMMM d, yyyy";

/**
  * This is the DateFormat.FULL format
  */
private static final String fullDateFormat = "EEEE, MMMM, e, yyyy";

/**
  * This is the DateFormat.DEFAULT format
  */
private static final String defaultDateFormat = "dd-MMM-yy";

/**
  * This is the DateFormat.SHORT format
  */
private static final String shortTimeFormat = "h:mm a";

/**
  * This is the DateFormat.MEDIUM format
  */
private static final String mediumTimeFormat = "h:mm:ss a";

/**
  * This is the DateFormat.LONG format
  */
private static final String longTimeFormat = "h:mm:ss a z";

/**
  * This is the DateFormat.FULL format
  */
private static final String fullTimeFormat = "h:mm:ss 'o''clock' a z";

/**
  * This is the DateFormat.DEFAULT format
  */
private static final String defaultTimeFormat = "h:mm:ss a";

/*************************************************************************/

/**
  * This is the object array used to hold the keys and values
  * for this bundle
  */

private static final Object[][] contents =
{
  { "collation_rules", collation_rules },
  { "word_breaks", word_breaks },
  { "sentence_breaks", sentence_breaks },
  { "line_breaks", line_breaks },
  { "months", months },
  { "shortMonths", shortMonths },
  { "weekdays", weekdays },
  { "shortWeekdays", shortWeekdays },
  { "ampms", ampms },
  { "eras", eras },
  { "zoneStrings", zoneStrings },
  { "localPatternChars", localPatternChars },
  { "shortDateFormat", shortDateFormat },
  { "mediumDateFormat", mediumDateFormat },
  { "longDateFormat", longDateFormat },
  { "fullDateFormat", fullDateFormat },
  { "defaultDateFormat", defaultDateFormat },
  { "shortTimeFormat", shortTimeFormat },
  { "mediumTimeFormat", mediumTimeFormat },
  { "longTimeFormat", longTimeFormat },
  { "fullTimeFormat", fullTimeFormat },
  { "defaultTimeFormat", defaultTimeFormat },
};

/*************************************************************************/

/**
  * This method returns the object array of key, value pairs containing
  * the data for this bundle.
  *
  * @return The key, value information.
  */
public Object[][]
getContents()
{
  return(contents);
}

} // class LocaleInformation_en

