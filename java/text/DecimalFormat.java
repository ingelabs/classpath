/* DecimalFormat.java -- Formats and parses numbers
   Copyright (C) 2001 Free Software Foundation, Inc.

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

/** This is a skeleton (ie incomplete and non-working) implementation of DecimalFormat
  * to allow MessageFormat to compile.
  * 
  *
  * @version 0.0
  *
  * @author John Leuner (jewel@debian.org)
  */

public class DecimalFormat extends NumberFormat
{
  public StringBuffer format(long number, StringBuffer sb, FieldPosition pos)
  {
    System.err.println("Warning, DecimalFormat is broken");
    return null;
  }

  public StringBuffer format(double number, StringBuffer sb, FieldPosition pos)
  {
    System.err.println("Warning, DecimalFormat is broken");
    return null;
  }

  public  Number parse(String str, ParsePosition pp)
  {
    System.err.println("Warning, DecimalFormat is broken");
    return null;
  }

  public  StringBuffer format(Object obj, StringBuffer sb, FieldPosition pos)  throws IllegalArgumentException
  {
    throw new IllegalArgumentException("Warning, DecimalFormat is broken");
  }

  public void applyPattern(String style)
  {
    System.err.println("Warning, DecimalFormat is broken");
  }
}



