/* URLDecoder.java -- Class to decode URL's from encoded form.
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


package java.net;

/**
  * This utility class contains one static method that converts a 
  * string into a decoded string from a string encoded in 
  * in x-www-form-urlencoded  format.  The x-www-form-urlencoded format 
  * replaces certain disallowed characters with
  * encoded equivalents.  All upper case and lower case letters in the
  * US alphabet remain as is, the space character (' ') is replaced with
  * '+' sign, and all other characters are converted to a "%XX" format
  * where XX is the hexadecimal representation of that character.  Note
  * that since unicode characters are 16 bits, and this method encodes only
  * 8 bits of information, the lower 8 bits of the character are used.
  * All encoded information is converted back to its original form.
  * <p>
  * This method is very useful for decoding strings sent to CGI scripts
  *
  * @version 1.2
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class URLDecoder
{

/*************************************************************************/

/*
 * Class Methods
 */

/**
  * Unhex-ify a number
  */
private static final int
unhex(char c)
{
  c = Character.toUpperCase(c);
  int i;

  if ((c >= '0') && (c <= '9'))
    i = c - '0';
  else if ((c >= 'A') && (c <= 'F'))
    i = 10 + (c - 'A');
  else
    i = 0;
   
  return(i);
}

/**
  * This method translates the passed in string into x-www-form-urlencoded
  * format and returns it.
  *
  * @param source The String to convert
  *
  * @return The converted String
  */
public static String
decode(String source) throws Exception
{
  StringBuffer result = new StringBuffer("");

  for (int i = 0; i < source.length(); i++)
    {
      // Get the low 8 bits of the next char
      char c = source.charAt(i);

      // Handle regular characters
      if ((c >= 'A') && (c <= 'Z'))
        {
          result.append(c);
          continue;
        }

      if ((c >= 'a') && (c <= 'z'))
        {
          result.append(c);
          continue;
        }

      // Handle spaces
      if (c == '+')
        {
          result.append(' ');
          continue;
        }

      // Handle everything else
      if (c == '%')
        {
          if ((i + 2) > (source.length() - 1))
            throw new Exception("Invalid encoded URL: " + source);

          ++i;
          char c1 = source.charAt(i);
          ++i;
          char c2 = source.charAt(i);

          char r = (char)(unhex(c1)*16 + unhex(c2));
          result.append(r);
        }

      // Still here
      throw new Exception("Unexpected character in encoded URL");
    }

  return(result.toString());
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Private constructor that does nothing. Included to avoid a default
  * public constructor being created by the compiler.
  */
private
URLDecoder()
{
  ;
}

} // class URLDecoder

