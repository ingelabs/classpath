/* OutputStreamWriter.java -- Writer that converts chars to bytes
   Copyright (C) 1998, 1999, 2000, 2001, 2003, 2005  Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

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


package java.io;

import gnu.java.nio.charset.EncodingHelper;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * This class writes characters to an output stream that is byte oriented
 * It converts the chars that are written to bytes using an encoding layer,
 * which is specific to a particular encoding standard.  The desired
 * encoding can either be specified by name, or if no encoding is specified,
 * the system default encoding will be used.  The system default encoding
 * name is determined from the system property <code>file.encoding</code>.
 * The only encodings that are guaranteed to be available are "8859_1"
 * (the Latin-1 character set) and "UTF8".  Unfortunately, Java does not
 * provide a mechanism for listing the encodings that are supported in
 * a given implementation.
 * <p>
 * Here is a list of standard encoding names that may be available:
 * <p>
 * <ul>
 * <li>8859_1 (ISO-8859-1/Latin-1)
 * <li>8859_2 (ISO-8859-2/Latin-2)
 * <li>8859_3 (ISO-8859-3/Latin-3)
 * <li>8859_4 (ISO-8859-4/Latin-4)
 * <li>8859_5 (ISO-8859-5/Latin-5)
 * <li>8859_6 (ISO-8859-6/Latin-6)
 * <li>8859_7 (ISO-8859-7/Latin-7)
 * <li>8859_8 (ISO-8859-8/Latin-8)
 * <li>8859_9 (ISO-8859-9/Latin-9)
 * <li>ASCII (7-bit ASCII)
 * <li>UTF8 (UCS Transformation Format-8)
 * <li>More Later
 * </ul>
 *
 * @author Aaron M. Renn (arenn@urbanophile.com)
 * @author Per Bothner (bothner@cygnus.com)
 * @date April 17, 1998.  
 */
public class OutputStreamWriter extends Writer
{
  /**
   * The default buffer size.
   */
  private final static int BUFFER_SIZE = 1024;

  /**
   * The output stream.
   */
  private OutputStream out;

  /**
   * The charset encoder.
   */
  private CharsetEncoder encoder;

  /**
   * java.io canonical name of the encoding.
   */
  private String encodingName;

  /**
   * This buffer receives the encoded data and is flushed to the underlying
   * stream when it gets too full.
   */
  private ByteBuffer outputBuffer;

  /**
   * A one-char array to be reused in read().
   */
  private char[] oneChar = new char[1];

  /**
   * The last char array that has been passed to write(char[],int,int). This
   * is used to cache the associated CharBuffer because write(char[],int,int)
   * is usually called with the same array repeatedly and we don't want to
   * allocate a new CharBuffer object on each call.
   */
  private Object lastArray;

  /**
   * The cached char buffer.
   */
  private CharBuffer lastBuffer;

  /**
   * This method initializes a new instance of <code>OutputStreamWriter</code>
   * to write to the specified stream using a caller supplied character
   * encoding scheme.  Note that due to a deficiency in the Java language
   * design, there is no way to determine which encodings are supported.
   *
   * @param out The <code>OutputStream</code> to write to
   * @param encoding_scheme The name of the encoding scheme to use for 
   * character to byte translation
   *
   * @exception UnsupportedEncodingException If the named encoding is 
   * not available.
   */
  public OutputStreamWriter (OutputStream out, String encoding_scheme) 
    throws UnsupportedEncodingException
  {
    if (out == null || encoding_scheme == null)
      throw new NullPointerException();

    this.out = out;
    try 
      {
        /*
         * Workraround for encodings with a byte-order-mark.
         * We only want to write it once per stream.
         */
        try 
          {
            if(encoding_scheme.equalsIgnoreCase("UnicodeBig")
                ||encoding_scheme.equalsIgnoreCase("UTF-16") ||
                encoding_scheme.equalsIgnoreCase("UTF16"))
              {
                encoding_scheme = "UTF-16BE";	  
                out.write((byte)0xFE);
                out.write((byte)0xFF);
              } 
            else if(encoding_scheme.equalsIgnoreCase("UnicodeLittle")){
              encoding_scheme = "UTF-16LE";
              out.write((byte)0xFF);
              out.write((byte)0xFE);
            }
          }
        catch(IOException ioe)
          {
          }

        Charset cs = EncodingHelper.getCharset(encoding_scheme);
        encoder = cs.newEncoder();
        encodingName = EncodingHelper.getOldCanonical(cs.name());

      } 
    catch(RuntimeException e) 
      {
        // Default to ISO Latin-1, will happen if this is called, for instance,
        // before the NIO provider is loadable.
        encoder = null; 
        encodingName = "ISO8859_1";
      }
    initEncoderAndBuffer();
  }

  /**
   * This method initializes a new instance of <code>OutputStreamWriter</code>
   * to write to the specified stream using the default encoding.
   *
   * @param out The <code>OutputStream</code> to write to
   */
  public OutputStreamWriter (OutputStream out)
  {
    this.out = out;
    try 
      {
        String encoding = System.getProperty("file.encoding");
        Charset cs = Charset.forName(encoding);
        encoder = cs.newEncoder();
        encodingName =  EncodingHelper.getOldCanonical(cs.name());
      } 
    catch(RuntimeException e) 
      {
        // For bootstrap problems.
        encoder = null;
        encodingName = "ISO8859_1";
      }

    initEncoderAndBuffer();
  }

  /**
   * This method initializes a new instance of <code>OutputStreamWriter</code>
   * to write to the specified stream using a given <code>Charset</code>.
   *
   * @param out The <code>OutputStream</code> to write to
   * @param cs The <code>Charset</code> of the encoding to use
   * 
   * @since 1.5
   */
  public OutputStreamWriter(OutputStream out, Charset cs)
  {
    this.out = out;
    encoder = cs.newEncoder();
    encodingName = EncodingHelper.getOldCanonical(cs.name());
    initEncoderAndBuffer();
  }
  
  /**
   * This method initializes a new instance of <code>OutputStreamWriter</code>
   * to write to the specified stream using a given
   * <code>CharsetEncoder</code>.
   *
   * @param out The <code>OutputStream</code> to write to
   * @param enc The <code>CharsetEncoder</code> to encode the output with
   * 
   * @since 1.5
   */
  public OutputStreamWriter(OutputStream out, CharsetEncoder enc)
  {
    this.out = out;
    encoder = enc;
    Charset cs = enc.charset();
    if (cs == null)
      encodingName = "US-ASCII";
    else
      encodingName = EncodingHelper.getOldCanonical(cs.name());
    initEncoderAndBuffer();
  }

  /**
   * Initializes the encoder and the output buffer.
   */
  private void initEncoderAndBuffer()
  {
    if (encoder != null)
      {
        encoder.onMalformedInput(CodingErrorAction.REPLACE);
        encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
      }
    outputBuffer = ByteBuffer.allocate(BUFFER_SIZE);
  }

  /**
   * This method closes this stream, and the underlying 
   * <code>OutputStream</code>
   *
   * @exception IOException If an error occurs
   */
  public void close () throws IOException
  {
    if(out == null)
      return;
    flush();
    out.close ();
    out = null;
  }

  /**
   * This method returns the name of the character encoding scheme currently
   * in use by this stream.  If the stream has been closed, then this method
   * may return <code>null</code>.
   *
   * @return The encoding scheme name
   */
  public String getEncoding ()
  {
    return out != null ? encodingName : null;
  }

  /**
   * This method flushes any buffered bytes to the underlying output sink.
   *
   * @exception IOException If an error occurs
   */
  public void flush () throws IOException
  {
    int len = outputBuffer.position();
    if (len > 0)
      {
        outputBuffer.flip();
        if (outputBuffer.hasArray())
          {
            byte[] bytes = outputBuffer.array();
            int p = outputBuffer.arrayOffset();
            out.write(bytes, p, len);
          }
        else
          {
            // Shouldn't happen for normal (non-direct) ByteBuffers.
            byte[] bytes = new byte[len];
            outputBuffer.get(bytes);
            out.write(bytes, 0, len);
          }
        outputBuffer.clear();
      }
    out.flush ();
  }

  /**
   * This method writes <code>count</code> characters from the specified
   * array to the output stream starting at position <code>offset</code>
   * into the array.
   *
   * @param buf The array of character to write from
   * @param offset The offset into the array to start writing chars from
   * @param count The number of chars to write.
   *
   * @exception IOException If an error occurs
   */
  public void write (char[] buf, int offset, int count) throws IOException
  {
    if(out == null)
      throw new IOException("Stream is closed.");
    if(buf == null)
      throw new IOException("Buffer is null.");

    CharBuffer charBuffer = getCharBuffer(buf, offset, count);
    encodeChars(charBuffer);
    flush();
  }

  /**
   * This method writes <code>count</code> bytes from the specified 
   * <code>String</code> starting at position <code>offset</code> into the
   * <code>String</code>.
   *
   * @param str The <code>String</code> to write chars from
   * @param offset The position in the <code>String</code> to start 
   * writing chars from
   * @param count The number of chars to write
   *
   * @exception IOException If an error occurs
   */
  public void write (String str, int offset, int count) throws IOException
  {
    if (out == null)
      throw new IOException("Stream is closed.");
    if (str == null)
      throw new IOException("Buffer is null.");

    // Don't call str.toCharArray() here to avoid allocation.
    // TODO: CharBuffer.wrap(String) should not allocate a char array either.
    CharBuffer charBuffer = getCharBuffer(str, offset, count);
    encodeChars(charBuffer);
    flush();
  }

  /**
   * This method writes a single character to the output stream.
   *
   * @param ch The char to write, passed as an int.
   *
   * @exception IOException If an error occurs
   */
  public void write (int ch) throws IOException
  {
    oneChar[0] = (char) ch;
    write(oneChar, 0, 1);
  }

  /**
   * Encodes the specified buffer of characters. The encoded data is stored
   * in an intermediate buffer and only flushed when this buffer gets full.
   *
   * @param chars the characters to encode
   *
   * @throws IOException if something goes wrong on the underlying stream
   */
  private void encodeChars(CharBuffer chars)
    throws IOException
  {
    assert out != null;
    assert encoder != null;
    int remaining = chars.remaining();
    while (remaining > 0)
      {
        CoderResult cr = encode(chars);
        remaining = chars.remaining();
        // Flush when the output buffer has no more space or when the
        // space is not enough to hold more encoded data (that when the
        // input buffer does not change).
        if (cr.isOverflow())
          flush();
      }
  }

  /**
   * Encodes the specified CharBuffer into the output buffer. This takes
   * care for the seldom case when we have no decoder, i.e. bootstrapping
   * problems.
   *
   * @param chars the char buffer to encode
   */
  private CoderResult encode(CharBuffer chars)
  {
    CoderResult cr;
    if (encoder != null)
      {
        cr = encoder.encode(chars, outputBuffer, false);
      }
    else
      {
        // For bootstrapping weirdness.
        // Perform primitive Latin1 decoding.
        while (chars.hasRemaining() && outputBuffer.hasRemaining())
          {
            outputBuffer.put((byte) (chars.get()));
          }
        // One of the buffers must be drained.
        if (! outputBuffer.hasRemaining())
          cr = CoderResult.OVERFLOW;
        else
          cr = CoderResult.UNDERFLOW;
      }
    return cr;
  }

  /**
   * Returns a CharBuffer that wraps the specified char array. This tries
   * to return a cached instance because usually the read() method is called
   * repeatedly with the same char array instance, or the no-arg read
   * method is called repeatedly which uses the oneChar field of this class
   * over and over again.
   *
   * @param buf the array to wrap
   * @param offset the offset
   * @param length the length
   *
   * @return a prepared CharBuffer to write to
   */
  private final CharBuffer getCharBuffer(Object buf, int offset, int length)
  {
    CharBuffer outBuffer;
    if (lastArray == buf)
      {
        outBuffer = lastBuffer;
        outBuffer.position(offset);
        outBuffer.limit(offset + length);
      }
    else
      {
        lastArray = buf;
        if (buf instanceof String)
          lastBuffer = CharBuffer.wrap((String) buf, offset, length);
        else
          lastBuffer = CharBuffer.wrap((char[]) buf, offset, length);
        outBuffer = lastBuffer;
      }
    return outBuffer;
  }
} // class OutputStreamWriter

