/* DataInputStream.java -- Class for reading Java data from a stream
   Copyright (C) 1998 Free Software Foundation, Inc.

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


package java.io;

/**
  * This subclass of <code>FilteredInputStream</code> implements the
  * <code>DataInput</code> interface that provides method for reading primitive
  * Java data types from a stream.
  *
  * @see DataInput
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class DataInputStream extends FilterInputStream implements DataInput
{

/*************************************************************************/

/*
 * Class Methods
 */

/**
  * This method reads a String encoded in UTF-8 format from the 
  * specified <code>DataInput</code> source.
  *
  * @param in The <code>DataInput</code> source to read from
  *
  * @return The String read from the source
  *
  * @exception IOException If an error occurs
  */
public static final String
readUTF(DataInput in) throws IOException
{
  return(in.readUTF());
}

/*
 * Ok, here we have a bunch of static methods that are used to convert
 * byte arrays to the appropriate data type.  The instance methods in 
 * this class then read the required number of bytes into a buffer and
 * call these methods to do the conversion.  This is done so that the
 * actual conversion does not need to be duplicated in RandomAccessFile.
 * That class simply calls these methods to convert buffers it reads
 * as well.  Please see the javadoc comments for the corresponding
 * instance method for a full description of the conversion process
 */

/*************************************************************************/

static final boolean
convertToBoolean(int b) 
{
  if (b == 0)
    return(false);
  else
    return(true);
}

/*************************************************************************/

static final byte
convertToByte(int b)
{
  return((byte)b); 
}

/*************************************************************************/

static final int
convertToUnsignedByte(int b)
{
  return(b);
}

/*************************************************************************/

static final char
convertToChar(byte[] buf)
{
  char retval = (char)(((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF));

  return(retval);
}

/*************************************************************************/

static final short
convertToShort(byte[] buf)
{
  short retval = (short)(((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF));

  return(retval);
}

/*************************************************************************/

static final int
convertToUnsignedShort(byte[] buf)
{
  int retval = ((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF);

  return(retval);
}

/*************************************************************************/

static final int
convertToInt(byte[] buf)
{
  int retval = ((buf[0] & 0xFF) << 24) | ((buf[1] & 0xFF) << 16) | 
               ((buf[2] & 0xFF) << 8) | (buf[3] & 0xFF);

  return(retval);
}

/*************************************************************************/

static final long
convertToLong(byte[] buf)
{
  long retval = (((long)buf[0] & 0xFF) << 56) | (((long)buf[1] & 0xFF) << 48) | 
                (((long)buf[2] & 0xFF) << 40) | (((long)buf[3] & 0xFF) << 32) | 
                (((long)buf[4] & 0xFF) << 24) | (((long)buf[5] & 0xFF) << 16) |
                (((long)buf[6] & 0xFF) << 8) | ((long)buf[7] & 0xFF);

  return(retval);
}

/*************************************************************************/

static final String
convertFromUTF(byte[] buf) throws EOFException, UTFDataFormatException
{
  StringBuffer sb = new StringBuffer("");

  for (int i = 0; i < buf.length; i++)
    {
      int byte_read = buf[i];

      // Three byte encoding case
      if ((byte_read & 0xE0) == 0xE0) // 224
        {
          int val = (byte_read & 0x0F) << 12;

          ++i;
          if (i == buf.length)
            throw new EOFException("Unexpected end of stream");

          byte_read = buf[i];

          if ((byte_read & 0x80) != 0x80)
            throw new UTFDataFormatException("Bad byte in input: " + byte_read);

          val |= (byte_read & 0x3F) << 6;

          ++i;
          if (i == buf.length)
            throw new EOFException("Unexpected end of stream");

          byte_read = buf[i];

          if ((byte_read & 0x80) != 0x80)
            throw new UTFDataFormatException("Bad byte in input: " + byte_read);

          val |= (byte_read & 0x3F);

          sb.append((char)val);
        }
      // Two byte encoding case
      else if ((byte_read & 0xC0) == 0xC0) // 192
        {
          int val = (byte_read & 0x1F) << 6;

          ++i;
          if (i == buf.length)
            throw new EOFException("Unexpected end of stream");

          byte_read = buf[i];

          if ((byte_read & 0x80) != 0x80)
            throw new UTFDataFormatException("Bad byte in input: " + byte_read);

          val |= (byte_read & 0x3F);

          sb.append((char)val);
        }
      // One byte encoding case
      else if (byte_read < 128)
        {
          sb.append((char)byte_read);
        }
      else
        {
          throw new UTFDataFormatException("Bad byte in input: " + byte_read);
        }
    }

  return(sb.toString());
}

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new <code>DataInputStream</code> to read from
  * the specified subordinate stream.
  *
  * @param in The subordinate <code>InputStream</code> to read from
  */
public
DataInputStream(InputStream in)
{
  super(in);

  this.in = new PushbackInputStream(in);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method reads a Java boolean value from an input stream.  It does
  * so by reading a single byte of data.  If that byte is zero, then the
  * value returned is <code>false</code>.  If the byte is non-zero, then
  * the value returned is <code>true</code>.
  * <p>
  * This method can read a <code>boolean</code> written by an object implementing the
  * <code>writeBoolean()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The <code>boolean</code> value read
  *
  * @exception EOFException If end of file is reached before reading the boolean
  * @exception IOException If any other error occurs
  */
public final boolean
readBoolean() throws EOFException, IOException
{
  int byte_read = in.read();

  if (byte_read == -1)
    throw new EOFException("Unexpected end of stream");

  return(convertToBoolean(byte_read));
}

/*************************************************************************/

/**
  * This method reads a Java byte value from an input stream.  The value
  * is in the range of -128 to 127.
  * <p>
  * This method can read a <code>byte</code> written by an object implementing the 
  * <code>writeByte()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The <code>byte</code> value read
  *
  * @exception EOFException If end of file is reached before reading the byte
  * @exception IOException If any other error occurs
  *
  * @see DataOutput
  */
public final byte
readByte() throws EOFException, IOException
{
  int byte_read = in.read();

  if (byte_read == -1)
    throw new EOFException("Unexpected end of stream");

  return(convertToByte(byte_read));
}

/*************************************************************************/

/**
  * This method reads 8 unsigned bits into a Java <code>int</code> value from the 
  * stream. The value returned is in the range of 0 to 255.
  * <p>
  * This method can read an unsigned byte written by an object implementing the
  * <code>writeUnsignedByte()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The unsigned bytes value read as a Java <code>int</code>.
  *
  * @exception EOFException If end of file is reached before reading the value
  * @exception IOException If any other error occurs
  *
  * @see DataOutput
  */
public final int
readUnsignedByte() throws EOFException, IOException
{
  int byte_read = in.read();

  if (byte_read == -1)
    throw new EOFException("Unexpected end of stream");

  return(convertToUnsignedByte(byte_read));
}

/*************************************************************************/

/**
  * This method reads a Java <code>char</code> value from an input stream.  
  * It operates by reading two bytes from the stream and converting them to 
  * a single 16-bit Java <code>char</code>.  The two bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  * <p>
  * As an example, if <code>byte1</code> and <code>byte2</code> represent the first
  * and second byte read from the stream respectively, they will be
  * transformed to a <code>char</code> in the following manner:
  * <p>
  * <code>(char)(((byte1 & 0xFF) << 8) | (byte2 & 0xFF)</code>
  * <p>
  * This method can read a <code>char</code> written by an object implementing the
  * <code>writeChar()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The <code>char</code> value read 
  *
  * @exception EOFException If end of file is reached before reading the char
  * @exception IOException If any other error occurs
  *
  * @see DataOutput
  */
public final char
readChar() throws EOFException, IOException
{
  byte[] buf = new byte[2];

  readFully(buf);

  return(convertToChar(buf));
}

/*************************************************************************/

/**
  * This method reads a signed 16-bit value into a Java in from the stream.
  * It operates by reading two bytes from the stream and converting them to 
  * a single 16-bit Java <code>short</code>.  The two bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  * <p>
  * As an example, if <code>byte1</code> and <code>byte2</code> represent the first
  * and second byte read from the stream respectively, they will be
  * transformed to a <code>short</code>. in the following manner:
  * <p>
  * <code>(short)(((byte1 & 0xFF) << 8) | (byte2 & 0xFF)</code>
  * <p>
  * The value returned is in the range of -32768 to 32767.
  * <p>
  * This method can read a <code>short</code> written by an object implementing the
  * <code>writeShort()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The <code>short</code> value read
  *
  * @exception EOFException If end of file is reached before reading the value
  * @exception IOException If any other error occurs
  *
  * @see DataOutput
  */
public final short
readShort() throws EOFException, IOException
{
  byte[] buf = new byte[2];

  readFully(buf);

  return(convertToShort(buf));
}

/*************************************************************************/

/**
  * This method reads 16 unsigned bits into a Java int value from the stream.
  * It operates by reading two bytes from the stream and converting them to 
  * a single Java <code>int</code>  The two bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  * <p>
  * As an example, if <code>byte1</code> and code{byte2</code> represent the first
  * and second byte read from the stream respectively, they will be
  * transformed to an <code>int</code> in the following manner:
  * <p>
  * <code>(int)(((byte1 & 0xFF) << 8) + (byte2 & 0xFF))</code>
  * <p>
  * The value returned is in the range of 0 to 65535.
  * <p>
  * This method can read an unsigned short written by an object implementing
  * the <code>writeUnsignedShort()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The unsigned short value read as a Java <code>int</code>
  *
  * @exception EOFException If end of file is reached before reading the value
  * @exception IOException If any other error occurs
  */
public final int
readUnsignedShort() throws EOFException, IOException
{
  byte[] buf = new byte[2];

  readFully(buf);

  return(convertToUnsignedShort(buf));
}

/*************************************************************************/

/**
  * This method reads a Java <code>int</code> value from an input stream
  * It operates by reading four bytes from the stream and converting them to 
  * a single Java <code>int</code>  The bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  * <p>
  * As an example, if <code>byte1</code> through <code>byte4</code> represent the first
  * four bytes read from the stream, they will be
  * transformed to an <code>int</code> in the following manner:
  * <p>
  * <code>(int)(((byte1 & 0xFF) << 24) + ((byte2 & 0xFF) << 16) + 
  * ((byte3 & 0xFF) << 8) + (byte4 & 0xFF)))</code>
  * <p>
  * The value returned is in the range of 0 to 65535.
  * <p>
  * This method can read an <code>int</code> written by an object implementing the
  * <code>writeInt()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The <code>int</code> value read
  *
  * @exception EOFException If end of file is reached before reading the int
  * @exception IOException If any other error occurs
  *
  * @see DataOutput
  */
public final int
readInt() throws EOFException, IOException
{
  byte[] buf = new byte[4];

  readFully(buf);

  return(convertToInt(buf));
}

/*************************************************************************/

/**
  * This method reads a Java long value from an input stream
  * It operates by reading eight bytes from the stream and converting them to 
  * a single Java <code>long</code>  The bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  * <p>
  * As an example, if <code>byte1</code> through <code>byte8</code> represent the first
  * eight bytes read from the stream, they will be
  * transformed to an <code>long</code> in the following manner:
  * <p>
  * <code>(long)((((long)byte1 & 0xFF) << 56) + (((long)byte2 & 0xFF) << 48) + 
  * (((long)byte3 & 0xFF) << 40) + (((long)byte4 & 0xFF) << 32) + 
  * (((long)byte5 & 0xFF) << 24) + (((long)byte6 & 0xFF) << 16) + 
  * (((long)byte7 & 0xFF) << 8) + ((long)byte9 & 0xFF)))</code>
  * <p>
  * The value returned is in the range of 0 to 65535.
  * <p>
  * This method can read an <code>long</code> written by an object implementing the
  * <code>writeLong()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The <code>long</code> value read
  *
  * @exception EOFException If end of file is reached before reading the long
  * @exception IOException If any other error occurs
  *
  * @see DataOutput
  */
public final long
readLong() throws EOFException, IOException
{
  byte[] buf = new byte[8];

  readFully(buf);

  return(convertToLong(buf));
}

/*************************************************************************/

/**
  * This method reads a Java float value from an input stream.  It operates
  * by first reading an <code>int</code> value from the stream by calling the
  * <code>readInt()</code> method in this interface, then converts that <code>int</code>
  * to a <code>float</code> using the <code>intBitsToFloat</code> method in 
  * the class <code>java.lang.Float</code>
  * <p>
  * This method can read a <code>float</code> written by an object implementing the
  * <code>writeFloat()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The <code>float</code> value read
  *
  * @exception EOFException If end of file is reached before reading the float
  * @exception IOException If any other error occurs
  *
  * @see java.lang.Float
  * @see DataOutput
  */
public final float
readFloat() throws EOFException, IOException
{
  int val = readInt();

  return(Float.intBitsToFloat(val));
}

/*************************************************************************/

/**
  * This method reads a Java double value from an input stream.  It operates
  * by first reading a <code>long</code> value from the stream by calling the
  * <code>readLong()</code> method in this interface, then converts that <code>long</code>
  * to a <code>double</code> using the <code>longBitsToDouble</code> method in 
  * the class <code>java.lang.Double</code>
  * <p>
  * This method can read a <code>double</code> written by an object implementing the
  * <code>writeDouble()</code> method in the <code>DataOutput</code> interface.
  *
  * @return The <code>double</code> value read
  *
  * @exception EOFException If end of file is reached before reading the double
  * @exception IOException If any other error occurs
  *
  * @see java.lang.Double
  * @see DataOutput
  */
public final double
readDouble() throws EOFException, IOException
{
  long val = readLong();

  return(Double.longBitsToDouble(val));
}

/*************************************************************************/

/**
  * This method reads the next line of text data from an input stream.
  * It operates by reading bytes and converting those bytes to <code>char</code>
  * values by treating the byte read as the low eight bits of the <code>char</code>
  * and using 0 as the high eight bits.  Because of this, it does
  * not support the full 16-bit Unicode character set.
  * <p>
  * The reading of bytes ends when either the end of file or a line terminator
  * is encountered.  The bytes read are then returned as a <code>String</code>
  * A line terminator is a byte sequence consisting of either 
  * <code>\r</code>, <code>\n</code> or <code>\r\n</code>.  These termination charaters are
  * discarded and are not returned as part of the string.
  * <p>
  * This method can read data that was written by an object implementing the
  * <code>writeLine()</code> method in <code>DataOutput</code>.
  *
  * @return The line read as a <code>String</code>
  *
  * @exception IOException If an error occurs
  *
  * @see DataOutput
  *
  * @deprecated
  */
public synchronized final String
readLine() throws IOException
{
  StringBuffer sb = new StringBuffer("");

  for (;;)
    {
      int byte_read = in.read();
 
      if (byte_read == -1)
        return(sb.toString());

      char c = (char)byte_read;

      if (c == '\r')
        {
          byte_read = in.read();
          if (((char)byte_read) != '\n')
            ((PushbackInputStream)in).unread(byte_read);

          return(sb.toString());
        }

      if (c == '\n')
        return(sb.toString());

      sb.append(c);
    }
}

/*************************************************************************/

/**
  * This method reads a <code>String</code> from an input stream that is encoded in
  * a modified UTF-8 format.  This format has a leading two byte sequence
  * that contains the remaining number of bytes to read.  This two byte
  * sequence is read using the <code>readUnsignedShort()</code> method of this
  * interface.
  * <p>
  * After the number of remaining bytes have been determined, these bytes
  * are read an transformed into <code>char</code> values.  These <code>char</code> values
  * are encoded in the stream using either a one, two, or three byte format.
  * The particular format in use can be determined by examining the first
  * byte read.  
  * <p>
  * If the first byte has a high order bit of 0, then
  * that character consists on only one byte.  This character value consists
  * of seven bits that are at positions 0 through 6 of the byte.  As an
  * example, if <code>byte1</code> is the byte read from the stream, it would
  * be converted to a <code>char</code> like so:
  * <p>
  * <code>(char)byte1</code>
  * <p>
  * If the first byte has 110 as its high order bits, then the 
  * character consists of two bytes.  The bits that make up the character
  * value are in positions 0 through 4 of the first byte and bit positions
  * 0 through 5 of the second byte.  (The second byte should have 
  * 10 as its high order bits).  These values are in most significant
  * byte first (i.e., "big endian") order.
  * <p>
  * As an example, if <code>byte1</code> and <code>byte2</code> are the first two bytes
  * read respectively, and the high order bits of them match the patterns
  * which indicate a two byte character encoding, then they would be
  * converted to a Java <code>char</code> like so:
  * <p>
  * <code>(char)(((byte1 & 0x1F) << 6) | (byte2 & 0x3F))</code>
  * <p>
  * If the first byte has a 1110 as its high order bits, then the
  * character consists of three bytes.  The bits that make up the character
  * value are in positions 0 through 3 of the first byte and bit positions
  * 0 through 5 of the other two bytes.  (The second and third bytes should
  * have 10 as their high order bits).  These values are in most
  * significant byte first (i.e., "big endian") order.
  * <p>
  * As an example, if <code>byte1</code> <code>byte2</code> and <code>byte3</code> are the
  * three bytes read, and the high order bits of them match the patterns
  * which indicate a three byte character encoding, then they would be
  * converted to a Java <code>char</code> like so:
  * <p>
  * <code>(char)(((byte1 & 0x0F) << 12) | ((byte2 & 0x3F) << 6) | (byte3 & 0x3F))</code>
  * <p>
  * Note that all characters are encoded in the method that requires the
  * fewest number of bytes with the exception of the character with the
  * value of <code>&#92;u0000</code> which is encoded as two bytes.  This is a 
  * modification of the UTF standard used to prevent C language style
  * <code>NUL</code> values from appearing in the byte stream.
  * <p>
  * This method can read data that was written by an object implementing the
  * <code>writeUTF()</code> method in <code>DataOutput</code>
  * 
  * @returns The <code>String</code> read
  *
  * @exception EOFException If end of file is reached before reading the String
  * @exception UTFDataFormatException If the data is not in UTF-8 format
  * @exception IOException If any other error occurs
  *
  * @see DataOutput
  */
public synchronized final String
readUTF() throws EOFException, UTFDataFormatException, IOException
{
  StringBuffer sb = new StringBuffer("");

  int num_bytes = readUnsignedShort();
  byte[] buf = new byte[num_bytes];
  readFully(buf);

  return(convertFromUTF(buf));
}

/*************************************************************************/

/**
  * This method reads raw bytes into the passed array until the array is
  * full.  Note that this method blocks until the data is available and
  * throws an exception if there is not enough data left in the stream to
  * fill the buffer
  *
  * @param buf The buffer into which to read the data
  *
  * @exception EOFException If end of file is reached before filling the buffer
  * @exception IOException If any other error occurs
  */
public final void
readFully(byte[] buf) throws EOFException, IOException
{
  readFully(buf, 0, buf.length);
}

/*************************************************************************/

/**
  * This method reads raw bytes into the passed array <code>buf</code> starting
  * <code>offset</code> bytes into the buffer.  The number of bytes read will be
  * exactly <code>len</code>  Note that this method blocks until the data is 
  * available and * throws an exception if there is not enough data left in 
  * the stream to read <code>len</code> bytes.
  *
  * @param buf The buffer into which to read the data
  * @param offset The offset into the buffer to start storing data
  * @param len The number of bytes to read into the buffer
  *
  * @exception EOFException If end of file is reached before filling the buffer
  * @exception IOException If any other error occurs
  */
public synchronized final void
readFully(byte[] buf, int offset, int len) throws EOFException, IOException
{
  int total_read = 0;

  while (total_read < len)
   {
     int bytes_read = in.read(buf, offset + total_read, len - total_read);
     if (bytes_read == -1)
       throw new EOFException("Unexpected end of stream");

     total_read += bytes_read;
   }
}

/*************************************************************************/

/**
  * This method reads bytes from the underlying stream into the specified
  * byte array buffer.  It will attempt to fill the buffer completely, but
  * may return a short count if there is insufficient data remaining to be
  * read to fill the buffer.
  *
  * @param buf The buffer into which bytes will be read.
  * 
  * @return The actual number of bytes read, or -1 if end of stream reached 
  * before reading any bytes.
  *
  * @exception IOException If an error occurs.
  */
public final int
read(byte[] buf) throws IOException
{
  return(read(buf, 0, buf.length));
}

/*************************************************************************/

/**
  * This method reads bytes from the underlying stream into the specified
  * byte array buffer.  It will attempt to read <code>len</code> bytes and
  * will start storing them at position <code>offset</code> into the buffer.
  * This method can return a short count if there is insufficient data
  * remaining to be read to complete the desired read length.
  *
  * @param buf The buffer into which bytes will be read.
  * @param offset The offset into the buffer to start storing bytes.
  * @param len The requested number of bytes to read.
  *
  * @return The actual number of bytes read, or -1 if end of stream reached
  * before reading any bytes.
  *
  * @exception IOException If an error occurs.
  */
public final int
read(byte[] buf, int offset, int len) throws IOException
{
  return(in.read(buf, offset, len));
}

/*************************************************************************/

/**
  * This method attempts to skip and discard the specified number of bytes 
  * in the input stream.  It may actually skip fewer bytes than requested. 
  * The actual number of bytes skipped is returned.  This method will not
  * skip any bytes if passed a negative number of bytes to skip.
  *
  * @param num_bytes The requested number of bytes to skip.
  *
  * @return The number of bytes actually skipped.
  *
  * @exception IOException If an error occurs.
  */
public final int
skipBytes(int n) throws IOException
{
  if (n <= 0)
    return(0);

  long total_skipped = in.skip(n);

  return((int)n);
}

} // class DataInputStream

