/*************************************************************************
/* LocaleInformation_de.java -- German locale data
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Jochen Hoenicke (jochen@gnu.org)
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
import java.util.Calendar;

/**
  * This class contains locale data for the German locale
  * @author Jochen Hoenicke
  */

public class LocaleInformation_de extends ListResourceBundle
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
  "-<0,1<2<3<4<5<6<7<8<9<A,a<b,B<c,C<d,D<e,E<f,F<g,G<h,H<i,I<j,J<j,K" +
  "<l,L<m,M<n,N<o,O<p,P<q,Q<r,R<s,S<t,T<u,U<v,V<w,W<x,X<y,Y,z<Z" + 
  "&ae,ä&Ae,Ä&oe,ö&Oe,Ö&ue,ü&Ue,Ü&ss,ß";

/**
  * This is the list of months, fully spelled out
  */
private static final String[] months = { "Januar", "Februar", "März", 
  "April", "Mai", "Juni", "Juli", "August", "September", "Oktober",
  "November", "Dezember", null };

/**
  * This is the list of abbreviated month names
  */
private static final String[] shortMonths = { 
  "Jan", "Feb", "Mär", "Apr", "Mai",
  "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez", null 
};

/**
  * This is the list of weekdays, fully spelled out
  */
private static final String[] weekdays = { 
  null, "Sonntag", "Montag", "Dienstag",
  "Mittwoch", "Donnerstag", "Freitag", "Samstag" 
};

/**
  * This is the list of abbreviated weekdays
  */
private static final String[] shortWeekdays = { 
  null, "So", "Mo", "Di", "Mi", "Do", "Fr", "Sa" 
};

/**
  * This is the list of era identifiers
  */
private static final String[] eras = { "v. Chr.", "n. Chr." };

/**
  * This is the list of timezone strings.  The JDK appears to include a
  * city name as the sixth element.
  */
private static final String[][] zoneStrings =
{
  // European time zones.  The city names are a little bit random.
  { "WET", "Westeuropäische Zeit", "WEZ", "Westeuropäische Sommerzeit", "WESZ", "London" },
  { "CET", "Mitteleuropäische Zeit", "MEZ", "Mitteleuropäische Sommerzeit", "MESZ", "Berlin" },
  { "EET", "Osteuropäische Zeit", "OEZ", "Mitteleuropäische Sommerzeit", "OESZ", "Istanbul" },
};

/**
  * This is the DateFormat.SHORT date format
  */
private static final String shortDateFormat = "dd.MM.yy";

/**
  * This is the DateFormat.MEDIUM format
  */
private static final String mediumDateFormat = "d. MMM yy";

/**
  * This is the DateFormat.LONG format
  */
private static final String longDateFormat = "d. MMMM yyyy";

/**
  * This is the DateFormat.FULL format
  */
private static final String fullDateFormat = "EEEE, d. MMMM yyyy";

/**
  * This is the DateFormat.DEFAULT format
  */
private static final String defaultDateFormat = "dd.MM.yy";

/**
  * This is the DateFormat.SHORT format
  */
private static final String shortTimeFormat = "H:mm";

/**
  * This is the DateFormat.MEDIUM format
  */
private static final String mediumTimeFormat = "H:mm:ss";

/**
  * This is the DateFormat.LONG format
  */
private static final String longTimeFormat = "H:mm:ss z";

/**
  * This is the DateFormat.FULL format
  */
private static final String fullTimeFormat = "H:mm:ss 'Uhr' z";

/**
  * This is the DateFormat.DEFAULT format
  */
private static final String defaultTimeFormat = "H:mm:ss";

/**
  * This is the currency symbol
  */
private static final String currencySymbol = "DM";

/**
  * This is the international currency symbol. 
  */
private static final String intlCurrencySymbol = "DEM";

/**
  * This is the decimal point.
  */
private static final String decimalSeparator = ",";

/**
  * This is the decimal separator in monetary values.
  */
private static final String monetarySeparator = ",";

/**
 * This is used by Calendar.
 * @see Calendar#getFirstDayOfWeek
 */
private static final Integer firstDayOfWeek = new Integer(Calendar.MONDAY);
/**
 * This is used by Calendar.
 * @see Calendar#getMinimalDaysInFirstWeek
 */
private static final Integer minimalDaysInFirstWeek = new Integer(4);

/*************************************************************************/

/**
  * This is the object array used to hold the keys and values
  * for this bundle
  */

private static final Object[][] contents =
{
  // For RuleBasedCollator
  { "collation_rules", collation_rules },
  // For SimpleDateFormat/DateFormatSymbols
  { "months", months },
  { "shortMonths", shortMonths },
  { "weekdays", weekdays },
  { "shortWeekdays", shortWeekdays },
  { "eras", eras },
  { "zoneStrings", zoneStrings },
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
  // For DecimalFormat/DecimalFormatSymbols
  { "currencySymbol", currencySymbol },
  { "intlCurrencySymbol", intlCurrencySymbol },
  { "decimalSeparator", decimalSeparator },
  { "monetarySeparator", monetarySeparator },
  // For Calendar/GregorianCalendar
  { "firstDayOfWeek", firstDayOfWeek },
  { "minimalDaysInFirstWeek", minimalDaysInFirstWeek },
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

} // class LocaleInformation_de
