/*************************************************************************
/* DecoderEightBitLookup.java -- Decodes eight-bit encodings
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

package gnu.java.io.decode;

import java.io.IOException;
import java.io.InputStream;

/**
  * Numerous character encodings utilize only eight bits.  These can
  * be easily and efficiently be converted to characters using lookup tables.
  * This class is the common superclass of all <code>Decoder</code> classes
  * that use eight bit lookup tables.  All a subclass implementor has to
  * do is define an encoding name and create a class consisting of a 
  * static lookup table overriding the default. 
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract class DecoderEightBitLookup extends Decoder
{

/*************************************************************************/

/*
 * Class Variables
 */
 
/**
  * This is the lookup table.  Subclasses must allocate a 255 byte char
  * array and put each Unicode character corresponding to the eight bit
  * byte in the appropriate index slot.  For example, to convert 0xE3 to
  * \u3768, put \u3768 at index 227 (0xE3) in the lookup table.
  */
protected static char[] lookup_table;

/*************************************************************************/

/*
 * Class Methods
 */

/**
  * This method returns the number of chars that can be converted out of
  * the <code>len</code> bytes in the specified array starting at
  * <code>offset</code>.  This will be identical to the number of bytes
  * in that range, i.e., <code>len</code>.
  */
public int
charsInByteArray(byte[] buf, int offset, int len)
{
  return(len);
}

/*************************************************************************/

/**
  * Convert the requested bytes to chars
  */
public char[]
convertToChars(byte[] buf, int buf_offset, int len, char[] cbuf, 
               int cbuf_offset)
{
  for (int i = 0; i < len; i++)
    cbuf[cbuf_offset + i] = lookup_table[buf[buf_offset + i]];

  return(cbuf);
}

/*************************************************************************/

/*
 * Constructors
 */
public
DecoderEightBitLookup(InputStream in)
{
  super(in);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Read the requested number of chars from the underlying stream
  */
public int
read(char[] cbuf, int offset, int len) throws IOException
{
  byte[] buf = new byte[len];
  
  int bytes_read = in.read(buf);
  if (bytes_read == -1)
    return(-1);

  convertToChars(buf, 0, bytes_read, cbuf, offset);
  return(bytes_read); 
}

} // class DecoderEightBitLookup

