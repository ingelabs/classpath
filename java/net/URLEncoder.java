/*************************************************************************
/* URLEncoder.java -- Class to convert strings to a properly encoded URL
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

package java.net;

/**
  * This utility class contains one static method that converts a 
  * string into a fully encoded URL string in x-www-form-urlencoded
  * format.  This format replaces certain disallowed characters with
  * encoded equivalents.  All upper case and lower case letters in the
  * US alphabet remain as is, the space character (' ') is replaced with
  * '+' sign, and all other characters are converted to a "%XX" format
  * where XX is the hexadecimal representation of that character.  Note
  * that since unicode characters are 16 bits, and this metho encodes only
  * 8 bits of information, the lower 8 bits of the character are used.
  * <p>
  * This method is very useful for encoding strings to be sent to CGI scripts
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class URLEncoder
{

/*************************************************************************/

/*
 * Class Variables
 */

/**
  * A lookup table array of hexadecimal character equivalents
  */
protected static final char[] hexchars = { '0', '1', '2', '3', '4', '5', '6', 
                                           '7', '8', '9', 'A', 'B', 'C', 'D', 
                                           'E', 'F' };

/*************************************************************************/

/*
 * Class Methods
 */

/**
  * This method translates the passed in string into x-www-form-urlencoded
  * format and returns it.
  *
  * @param source The String to convert
  *
  * @return The converted String
  */
public static String
encode(String source)
{
  StringBuffer result = new StringBuffer("");

  for (int i = 0; i < source.length(); i++)
    {
      // Get the low 8 bits of the next char
      char c = source.charAt(i);
      c &= 0xFF;

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
      if (c == ' ')
        {
          result.append('+');
          continue;
        }

      // Handle everything else
      result.append('%');
      result.append(hexchars[ c / 16 ]);
      result.append(hexchars[ c % 16 ]);
    }

  return(result.toString());
}

} // class URLEncoder

