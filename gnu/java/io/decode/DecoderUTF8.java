/* DecoderUTF8.java -- Decoder for the UTF-8 character encoding.
   Copyright (C) 1998, 2003, 2004 Free Software Foundation, Inc.

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

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package gnu.java.io.decode;

import java.io.InputStream;
import java.io.IOException;

/**
  * This class implements character decoding in the UCS Transformation
  * Format 8 (UTF-8) encoding scheme.  This is defined in RFC-2279.
  * We only handle the 1-3 byte encodings for characters in the Unicode
  * set.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class DecoderUTF8 extends Decoder
{
private static final char REPLACEMENT_CHARACTER = '\uFFFD';

// If we're operating in stream mode and we encounter a surrogate pair that
// we can't fit in the output buffer, we use this field to store the
// second half of the surrogate pair.
private int pendingChar = -1;

/*************************************************************************/

/*
 * Constructors
 */

public
DecoderUTF8(InputStream in)
{
  super(in, "UTF8");
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Counts the number of characters in a given byte array
  */
public int
charsInByteArray(byte[] buf, int offset, int len)
{
    int more = 0;
    int val = 0;
    int num_chars = 0;

    for (int i = offset; i < offset + len; i++)
      {
        byte b = buf[i];

        if ((b & 0x80) == 0)
          {
            if (more != 0)
                num_chars++; // ?
            more = 0;
            num_chars++;
          }
        else if ((b & 0xC0) == 0x80)
          {
            if (more == 0)
                num_chars++; // ?
            else
              {
                val <<= 6;
                val |= b & 0x3F;
                more--;
                if (more == 0)
                  {
                    num_chars++;
                    if (val >= 0x10000)
                        num_chars++;
                  }
              }
          }
        else
          {
            if (more != 0)
                num_chars++; // ?

            if ((b & 0xF8) == 0xF0)
              {
                val = b & 0x07;
                more = 3;
              }
            else if ((b & 0xF0) == 0xE0)
              {
                val = b & 0x0F;
                more = 2;
              }
            else if ((b & 0xE0) == 0xC0)
              {
                val = b & 0x1F;
                more = 1;
              }
            else
                num_chars++; // ?
          }
      }

    return num_chars;
}

/*************************************************************************/

/**
  * Transform the specified UTF8 encoded buffer to Unicode characters
  */
public char[]
convertToChars(byte[] buf, int buf_offset, int len, char cbuf[],
               int cbuf_offset)
{
    int more = 0;
    int val = 0;

    for (int i = buf_offset; i < buf_offset + len; i++)
      {
        byte b = buf[i];

        if ((b & 0x80) == 0)
          {
            if (more != 0)
                cbuf[cbuf_offset++] = REPLACEMENT_CHARACTER;
            more = 0;
            cbuf[cbuf_offset++] = (char)b;
          }
        else if ((b & 0xC0) == 0x80)
          {
            if (more == 0)
                cbuf[cbuf_offset++] = REPLACEMENT_CHARACTER;
            else
              {
                val <<= 6;
                val |= b & 0x3F;
                more--;
                if (more == 0)
                  {
                    if (val < 0x10000)
                        cbuf[cbuf_offset++] = (char)val;
                    else
                      {
                        val -= 0x10000;
                        cbuf[cbuf_offset++] = (char)(0xD800 + (val >> 10));
                        cbuf[cbuf_offset++] = (char)(0xDC00 + (val & 0x3FF));
                      }
                  }
              }
          }
        else
          {
            if (more != 0)
                cbuf[cbuf_offset++] = REPLACEMENT_CHARACTER;

            if ((b & 0xF8) == 0xF0)
              {
                val = b & 0x07;
                more = 3;
              }
            else if ((b & 0xF0) == 0xE0)
              {
                val = b & 0x0F;
                more = 2;
              }
            else if ((b & 0xE0) == 0xC0)
              {
                val = b & 0x1F;
                more = 1;
              }
            else
                cbuf[cbuf_offset++] = REPLACEMENT_CHARACTER;
          }
      }

    return cbuf;
}

/*************************************************************************/

/**
  * Reads chars from a UTF8 encoded byte stream
  */
public int
read(char[] cbuf, int offset, int len) throws IOException
{
    int start_offset = offset;
    int more = 0;
    int val = 0;

    if (pendingChar != -1 && len > 0)
      {
        cbuf[offset++] = (char)pendingChar;
        pendingChar = -1;
      }

    while (offset < start_offset + len)
      {
        int b = in.read();
        if (b == -1)
          {
            if (more != 0)
                cbuf[offset++] = REPLACEMENT_CHARACTER;
            if (offset - start_offset == 0)
                return -1;
            return offset - start_offset;
          }

        if ((b & 0x80) == 0)
          {
            if (more != 0)
                cbuf[offset++] = REPLACEMENT_CHARACTER;
            more = 0;
            cbuf[offset++] = (char)b;
          }
        else if ((b & 0xC0) == 0x80)
          {
            if (more == 0)
                cbuf[offset++] = REPLACEMENT_CHARACTER;
            else
              {
                val <<= 6;
                val |= b & 0x3F;
                more--;
                if (more == 0)
                  {
                    if (val < 0x10000)
                        cbuf[offset++] = (char)val;
                    else
                      {
                        val -= 0x10000;
                        cbuf[offset++] = (char)(0xD800 + (val >> 10));
                        if (offset < start_offset + len)
                            cbuf[offset++] = (char)(0xDC00 + (val & 0x3FF));
                        else
                            pendingChar = (char)(0xDC00 + (val & 0x3FF));
                      }
                  }
              }
          }
        else
          {
            if (more != 0)
                cbuf[offset++] = REPLACEMENT_CHARACTER;

            if ((b & 0xF8) == 0xF0)
              {
                val = b & 0x07;
                more = 3;
              }
            else if ((b & 0xF0) == 0xE0)
              {
                val = b & 0x0F;
                more = 2;
              }
            else if ((b & 0xE0) == 0xC0)
              {
                val = b & 0x1F;
                more = 1;
              }
            else
                cbuf[offset++] = REPLACEMENT_CHARACTER;
          }
        
        // if no more bytes available, terminate loop early, instead of
        // blocking in in.read().
        // Do not test this in the for condition: it must call in.read() at
        // least once (and thus block if "in" is empty).
        if (more == 0 && in.available() <= 0)
            break;
      }

    return offset - start_offset;
}

} // class DecoderUTF8

