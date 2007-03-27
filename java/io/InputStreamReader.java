/* InputStreamReader.java -- Reader than transforms bytes to chars
   Copyright (C) 1998, 1999, 2001, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.

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

import gnu.classpath.SystemProperties;
import gnu.java.nio.charset.EncodingHelper;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * This class reads characters from a byte input stream.   The characters
 * read are converted from bytes in the underlying stream by a 
 * decoding layer.  The decoding layer transforms bytes to chars according
 * to an encoding standard.  There are many available encodings to choose 
 * from.  The desired encoding can either be specified by name, or if no
 * encoding is selected, the system default encoding will be used.  The
 * system default encoding name is determined from the system property
 * <code>file.encoding</code>.  The only encodings that are guaranteed to 
 * be availalbe are "8859_1" (the Latin-1 character set) and "UTF8".
 * Unforunately, Java does not provide a mechanism for listing the
 * ecodings that are supported in a given implementation.
 * <p>
 * Here is a list of standard encoding names that may be available:
 * <p>
 * <ul>
 * <li>8859_1 (ISO-8859-1/Latin-1)</li>
 * <li>8859_2 (ISO-8859-2/Latin-2)</li>
 * <li>8859_3 (ISO-8859-3/Latin-3)</li>
 * <li>8859_4 (ISO-8859-4/Latin-4)</li>
 * <li>8859_5 (ISO-8859-5/Latin-5)</li>
 * <li>8859_6 (ISO-8859-6/Latin-6)</li>
 * <li>8859_7 (ISO-8859-7/Latin-7)</li>
 * <li>8859_8 (ISO-8859-8/Latin-8)</li>
 * <li>8859_9 (ISO-8859-9/Latin-9)</li>
 * <li>ASCII (7-bit ASCII)</li>
 * <li>UTF8 (UCS Transformation Format-8)</li>
 * <li>More later</li>
 * </ul>
 * <p>
 * It is recommended that applications do not use 
 * <code>InputStreamReader</code>'s
 * directly.  Rather, for efficiency purposes, an object of this class
 * should be wrapped by a <code>BufferedReader</code>.
 * <p>
 * Due to a deficiency the Java class library design, there is no standard
 * way for an application to install its own byte-character encoding.
 *
 * @see BufferedReader
 * @see InputStream
 *
 * @author Robert Schuster
 * @author Aaron M. Renn (arenn@urbanophile.com)
 * @author Per Bothner (bothner@cygnus.com)
 * @date April 22, 1998.  
 */
public class InputStreamReader extends Reader
{
  /**
   * The default buffer size.
   */
  private final static int BUFFER_SIZE = 1024;

  /**
   * The input stream.
   */
  private InputStream in;

  /**
   * The charset decoder.
   */
  private CharsetDecoder decoder;

  /**
   * End of stream reached.
   */
  private boolean isDone = false;

  /**
   * Buffer holding surplus loaded bytes (if any)
   */
  private ByteBuffer byteBuffer;

  /**
   * java.io canonical name of the encoding.
   */
  private String encoding;

  /**
   * One char as array to be used in {@link #read()}.
   */
  private char[] oneChar = new char[1];

  /**
   * The last char array that has been passed to read(char[],int,int). This
   * is used to cache the associated CharBuffer because read(char[],int,int)
   * is usually called with the same array repeatedly and we don't want to
   * allocate a new CharBuffer object on each call.
   */
  private char[] lastArray;

  /**
   * The cached CharBuffer associated with the above array.
   */
  private CharBuffer lastBuffer;

  /**
   * This method initializes a new instance of <code>InputStreamReader</code>
   * to read from the specified stream using the default encoding.
   *
   * @param in The <code>InputStream</code> to read from 
   */
  public InputStreamReader(InputStream in)
  {
    if (in == null)
      throw new NullPointerException();

    this.in = in;

    String encodingName = SystemProperties.getProperty("file.encoding");
    try
      {
        Charset cs = EncodingHelper.getCharset(encodingName);
        decoder = cs.newDecoder();
        // The encoding should be the old name, if such exists.
        encoding = EncodingHelper.getOldCanonical(cs.name());
      }
    catch(RuntimeException e)
      {
        // For bootstrapping problems only.
        decoder = null;
        encoding = "ISO8859_1";
      }
    catch (UnsupportedEncodingException ex)
      {
        Charset cs = EncodingHelper.getDefaultCharset();
        decoder = cs.newDecoder();
        // The encoding should be the old name, if such exists.
        encoding = EncodingHelper.getOldCanonical(cs.name());
      }    
    initDecoderAndBuffer();
  }

  /**
   * This method initializes a new instance of <code>InputStreamReader</code>
   * to read from the specified stream using a caller supplied character
   * encoding scheme.  Note that due to a deficiency in the Java language
   * design, there is no way to determine which encodings are supported.
   * 
   * @param in The <code>InputStream</code> to read from
   * @param encoding_name The name of the encoding scheme to use
   *
   * @exception UnsupportedEncodingException If the encoding scheme 
   * requested is not available.
   */
  public InputStreamReader(InputStream in, String encoding_name)
    throws UnsupportedEncodingException
  {
    if (in == null || encoding_name == null)
      throw new NullPointerException();

    this.in = in;

    try
      {
        Charset cs = EncodingHelper.getCharset(encoding_name);
        decoder = cs.newDecoder();
        // The encoding should be the old name, if such exists.
        encoding = EncodingHelper.getOldCanonical(cs.name());
      }
    catch(RuntimeException e)
      {
        // For bootstrapping problems only.
        decoder = null;
        encoding = "ISO8859_1";
      }

    initDecoderAndBuffer();
  }

  /**
   * Creates an InputStreamReader that uses a decoder of the given
   * charset to decode the bytes in the InputStream into
   * characters.
   * 
   * @since 1.4
   */
  public InputStreamReader(InputStream in, Charset charset)
  {
    if (in == null || charset == null)
      throw new NullPointerException();

    this.in = in;
    decoder = charset.newDecoder();
    encoding = EncodingHelper.getOldCanonical(charset.name());
    initDecoderAndBuffer();
  }

  /**
   * Creates an InputStreamReader that uses the given charset decoder
   * to decode the bytes in the InputStream into characters.
   * 
   * @since 1.4
   */
  public InputStreamReader(InputStream in, CharsetDecoder decoder)
  {
    if (in == null || decoder == null)
      throw new NullPointerException();

    this.in = in;
    this.decoder = decoder;
    encoding = EncodingHelper.getOldCanonical(decoder.charset().name());
    initDecoderAndBuffer();
  }

  /**
   * Initializes the decoder and the input buffer.
   */
  private void initDecoderAndBuffer()
  {
    if (decoder != null)
      {
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
        decoder.reset();
      }

    byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    // No bytes available initially.
    byteBuffer.position(byteBuffer.limit());
  }

  /**
   * This method closes this stream, as well as the underlying 
   * <code>InputStream</code>.
   *
   * @exception IOException If an error occurs
   */
  public void close() throws IOException
  {
    synchronized (lock)
      {
	// Makes sure all intermediate data is released by the decoder.
	if (decoder != null)
	   decoder.reset();
	if (in != null)
	   in.close();
	in = null;
	isDone = true;
	decoder = null;
      }
  }

  /**
   * This method returns the name of the encoding that is currently in use
   * by this object.  If the stream has been closed, this method is allowed
   * to return <code>null</code>.
   *
   * @return The current encoding name
   */
  public String getEncoding()
  {
    return in != null ? encoding : null;
  }

  /**
   * This method checks to see if the stream is ready to be read.  It
   * will return <code>true</code> if is, or <code>false</code> if it is not.
   * If the stream is not ready to be read, it could (although is not required
   * to) block on the next read attempt.
   *
   * @return <code>true</code> if the stream is ready to be read, 
   * <code>false</code> otherwise
   *
   * @exception IOException If an error occurs
   */
  public boolean ready() throws IOException
  {
    if (in == null)
      throw new IOException("Reader has been closed");
    return byteBuffer.hasRemaining() || in.available() != 0;
  }

  /**
   * This method reads up to <code>length</code> characters from the stream into
   * the specified array starting at index <code>offset</code> into the
   * array.
   *
   * @param buf The character array to recieve the data read
   * @param offset The offset into the array to start storing characters
   * @param length The requested number of characters to read.
   *
   * @return The actual number of characters read, or -1 if end of stream.
   *
   * @exception IOException If an error occurs
   */
  public int read(char[] buf, int offset, int length) throws IOException
  {
    if (in == null)
      throw new IOException("Reader has been closed");
    if (isDone)
      return -1;

    CharBuffer outBuffer = getCharBuffer(buf, offset, length);
    int startPos = outBuffer.position();
    int remaining = outBuffer.remaining();
    int start = remaining;
    CoderResult cr = null;
    // Try to decode as long as the output buffer can hold more data.
    // Decode at least one character (block when necessary).
    boolean moreAvailable = true;
    while (remaining > 0 && moreAvailable)
      {
        if (byteBuffer.remaining() == 0
            || (cr != null && (cr.isUnderflow())))
          {
            // Block when we have not yet decoded at least one character.
            boolean block = remaining == start;
            moreAvailable = refillInputBuffer(block);
          }
        cr = decode(outBuffer);
        remaining = outBuffer.remaining();
      }
    return outBuffer.position() - startPos;
  }

  /**
   * Reads an char from the input stream and returns it
   * as an int in the range of 0-65535.  This method also will return -1 if
   * the end of the stream has been reached.
   * <p>
   * This method will block until the char can be read.
   *
   * @return The char read or -1 if end of stream
   *
   * @exception IOException If an error occurs
   */
  public int read() throws IOException
  {
    int count = read(oneChar, 0, 1);
    return count > 0 ? oneChar[0] : -1;
  }

  /**
   * Skips the specified number of chars in the stream.  It
   * returns the actual number of chars skipped, which may be less than the
   * requested amount.
   *
   * @param count The requested number of chars to skip
   *
   * @return The actual number of chars skipped.
   *
   * @exception IOException If an error occurs
   */
   public long skip(long count) throws IOException
   {
     if (in == null)
       throw new IOException("Reader has been closed");

     return super.skip(count);
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
  private final CharBuffer getCharBuffer(char[] buf, int offset, int length)
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
        lastBuffer = CharBuffer.wrap(buf, offset, length);
        outBuffer = lastBuffer;
      }
    return outBuffer;
  }

  /**
   * Refills the input buffer by reading a chunk of bytes from the underlying
   * input stream
   *
   * @param block true when this method is allowed to block when necessary,
   *        false otherwise
   *
   * @return true when data has been read, false when no data has been
   *         available without blocking
   *
   * @throws IOException from the underlying stream
   */
  private final boolean refillInputBuffer(boolean block)
    throws IOException
  {
    boolean refilled = false;

    // Refill input buffer.
    byteBuffer.compact();
    if (byteBuffer.hasArray())
      {
        byte[] buffer = byteBuffer.array();
        int offs = byteBuffer.arrayOffset();
        int pos = byteBuffer.position();
        int rem = byteBuffer.remaining();
        int avail = in.available();
        int readBytes = 0;
        int len;
        // Try to not block.
        if (block)
          len = avail != 0 ? Math.min(avail, rem) : rem;
        else
          len = Math.min(avail, rem);
        readBytes = in.read(buffer, offs + pos, len);

        if (readBytes > 0)
          {
            byteBuffer.position(pos + readBytes);
            byteBuffer.limit(pos + readBytes);
            refilled = true;
          }
        isDone = readBytes == -1;
      }
    else
      {
        assert false;
        // Shouldn't happen, but anyway...
        byte[] buffer = new byte[byteBuffer.limit()
                                 - byteBuffer.position()];
        int readBytes = in.read(buffer);
        isDone = readBytes == -1;
        byteBuffer.put(buffer);
      }
    byteBuffer.flip();
    return refilled;
  }

  /**
   * Decodes the current byteBuffer into the specified outBuffer. This takes
   * care of the corner case when we have no decoder (i.e. bootstrap problems)
   * and performs a primitive Latin1 decoding in this case.
   *
   * @param outBuffer the buffer to decode to
   *
   * @return the coder result
   */
  private CoderResult decode(CharBuffer outBuffer)
  {
    CoderResult cr;
    if (decoder != null)
      {
        cr = decoder.decode(byteBuffer, outBuffer, false);
      }
    else
      {
        // Perform primitive Latin1 decoding.
        while (outBuffer.hasRemaining() && byteBuffer.hasRemaining())
          {
            outBuffer.put((char) (0xff & byteBuffer.get()));
          }
        // One of the buffers must be drained.
        if (! outBuffer.hasRemaining())
          cr = CoderResult.OVERFLOW;
        else
          cr = CoderResult.UNDERFLOW;
      }
    return cr;
  }
}
