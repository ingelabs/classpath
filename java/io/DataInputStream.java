/*************************************************************************
/* DataInputStream.java -- Class for reading Java data from a stream
/*
/* Copyright (c) 1998 by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.io;

/**
  * This subclass of @code{FilteredInputStream} implements the
  * @code{DataInput} interface that provides method for reading primitive
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

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new @code{DataInputStream} to read from
  * the specified subordinate stream.
  *
  * @param in The subordinate @code{InputStream} to read from
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
  * value returned is @code{false}.  If the byte is non-zero, then
  * the value returned is @code{true}.
  *
  * This method can read a @code{boolean} written by an object implementing the
  * @code{writeBoolean()} method in the @code{DataOutput} interface.
  *
  * @return The @code{boolean} value read
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

  if (byte_read == 0)
    return(false);
  else
    return(true);
}

/*************************************************************************/

/**
  * This method reads a Java byte value from an input stream.  The value
  * is in the range of -128 to 127.
  *
  * This method can read a @code{byte} written by an object implementing the 
  * @code{writeByte()} method in the @code{DataOutput} interface.
  *
  * @return The @code{byte} value read
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

  return((byte)byte_read); 
}

/*************************************************************************/

/**
  * This method reads 8 unsigned bits into a Java @code{int} value from the 
  * stream. The value returned is in the range of 0 to 255.
  *
  * This method can read an unsigned byte written by an object implementing the
  * @code{writeUnsignedByte()} method in the @code{DataOutput} interface.
  *
  * @return The unsigned bytes value read as a Java @code{int}.
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

  return(byte_read);
}

/*************************************************************************/

/**
  * This method reads a Java @code{char} value from an input stream.  
  * It operates by reading two bytes from the stream and converting them to 
  * a single 16-bit Java @code{char}.  The two bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  *
  * As an example, if @code{byte1} and code{byte2} represent the first
  * and second byte read from the stream respectively, they will be
  * transformed to a @code{char} in the following manner:
  *
  * @code{(char)(((byte1 & 0xFF) << 8) | (byte2 & 0xFF)}
  *
  * This method can read a @code{char} written by an object implementing the
  * @code{writeChar()} method in the @code{DataOutput} interface.
  *
  * @return The @code{char} value read 
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

  char retval = (char)(((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF));

  return(retval);
}

/*************************************************************************/

/**
  * This method reads a signed 16-bit value into a Java in from the stream.
  * It operates by reading two bytes from the stream and converting them to 
  * a single 16-bit Java @code{short}.  The two bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  *
  * As an example, if @code{byte1} and code{byte2} represent the first
  * and second byte read from the stream respectively, they will be
  * transformed to a @code{short} in the following manner:
  *
  * @code{(short)(((byte1 & 0xFF) << 8) | (byte2 & 0xFF)}
  *
  * The value returned is in the range of -32768 to 32767.
  *
  * This method can read a @code{short} written by an object implementing the
  * @code{writeShort()} method in the @code{DataOutput} interface.
  *
  * @return The @code{short} value read
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

  short retval = (short)(((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF));

  return(retval);
}

/*************************************************************************/

/**
  * This method reads 16 unsigned bits into a Java int value from the stream.
  * It operates by reading two bytes from the stream and converting them to 
  * a single Java @code{int}.  The two bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  *
  * As an example, if @code{byte1} and code{byte2} represent the first
  * and second byte read from the stream respectively, they will be
  * transformed to an @code{int} in the following manner:
  *
  * @code{(int)(((byte1 & 0xFF) << 8) + (byte2 & 0xFF))}
  *
  * The value returned is in the range of 0 to 65535.
  *
  * This method can read an unsigned short written by an object implementing
  * the @code{writeUnsignedShort()} method in the @code{DataOutput} interface.
  *
  * @return The unsigned short value read as a Java @code{int}.
  *
  * @exception EOFException If end of file is reached before reading the value
  * @exception IOException If any other error occurs
  */
public final int
readUnsignedShort() throws EOFException, IOException
{
  byte[] buf = new byte[2];

  readFully(buf);

  int retval = ((buf[0] & 0xFF) << 8) | (buf[1] & 0xFF);

  return(retval);
}

/*************************************************************************/

/**
  * This method reads a Java @code{int} value from an input stream
  * It operates by reading four bytes from the stream and converting them to 
  * a single Java @code{int}.  The bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  *
  * As an example, if @code{byte1} through @code{byte4} represent the first
  * four bytes read from the stream, they will be
  * transformed to an @code{int} in the following manner:
  *
  * @code{(int)(((byte1 & 0xFF) << 24) + ((byte2 & 0xFF) << 16) + 
  * ((byte3 & 0xFF) << 8) + (byte4 & 0xFF)))}
  *
  * The value returned is in the range of 0 to 65535.
  *
  * This method can read an @code{int} written by an object implementing the
  * @code{writeInt()} method in the @code{DataOutput} interface.
  *
  * @return The @code{int} value read
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

  int retval = ((buf[0] & 0xFF) << 24) | ((buf[1] & 0xFF) << 16) + 
               ((buf[2] & 0xFF) << 8) | (buf[3] & 0xFF);

  return(retval);
}

/*************************************************************************/

/**
  * This method reads a Java long value from an input stream
  * It operates by reading eight bytes from the stream and converting them to 
  * a single Java @code{long}.  The bytes are stored most
  * significant byte first (i.e., "big endian") regardless of the native
  * host byte ordering. 
  *
  * As an example, if @code{byte1} through @code{byte8} represent the first
  * eight bytes read from the stream, they will be
  * transformed to an @code{long} in the following manner:
  *
  * @code{(long)((((long)byte1 & 0xFF) << 56) + (((long)byte2 & 0xFF) << 48) + 
  * (((long)byte3 & 0xFF) << 40) + (((long)byte4 & 0xFF) << 32) + 
  * (((long)byte5 & 0xFF) << 24) + (((long)byte6 & 0xFF) << 16) + 
  * (((long)byte7 & 0xFF) << 8) + ((long)byte9 & 0xFF)))}
  *
  * The value returned is in the range of 0 to 65535.
  *
  * This method can read an @code{long} written by an object implementing the
  * @code{writeLong()} method in the @code{DataOutput} interface.
  *
  * @return The @code{long} value read
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

  long retval = (((long)buf[0] & 0xFF) << 56) | (((long)buf[1] & 0xFF) << 48) | 
                (((long)buf[2] & 0xFF) << 40) | (((long)buf[3] & 0xFF) << 32) | 
                (((long)buf[4] & 0xFF) << 24) | (((long)buf[5] & 0xFF) << 16) |
                (((long)buf[6] & 0xFF) << 8) | ((long)buf[7] & 0xFF);

  return(retval);
}

/*************************************************************************/

/**
  * This method reads a Java float value from an input stream.  It operates
  * by first reading an @code{int} value from the stream by calling the
  * @code{readInt()} method in this interface, then converts that @code{int}
  * to a @code{float} using the @code{intBitsToFloat} method in 
  * the class @{java.lang.Float}.
  *
  * This method can read a @code{float} written by an object implementing the
  * @code{writeFloat()} method in the @code{DataOutput} interface.
  *
  * @return The @code{float} value read
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
  * by first reading a @code{logn} value from the stream by calling the
  * @code{readLong()} method in this interface, then converts that @code{long}
  * to a @code{double} using the @code{longBitsToDouble} method in 
  * the class @{java.lang.Double}.
  *
  * This method can read a @code{double} written by an object implementing the
  * @code{writeDouble()} method in the @code{DataOutput} interface.
  *
  * @return The @code{double} value read
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
  * It operates by reading bytes and converting those bytes to @{char}
  * values by treating the byte read as the low eight bits of the @{char}
  * and using @code{0} as the high eight bits.  Because of this, it does
  * not support the full 16-bit Unicode character set.
  *
  * The reading of bytes ends when either the end of file or a line terminator
  * is encountered.  The bytes read are then returned as a @code{String}.
  * A line terminator is a byte sequence consisting of either 
  * @samp{\r}, @samp{\n} or @samp{\r\n}.  These termination charaters are
  * discarded and are not returned as part of the string.
  *
  * This method can read data that was written by an object implementing the
  * @code{writeLine()} method in @code{DataOutput}.
  *
  * @return The line read as a @code{String}
  *
  * @exception IOException If an error occurs
  *
  * @see DataOutput
  *
  * @deprecated
  */
public final String
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
  * This method reads a @code{String} from an input stream that is encoded in
  * a modified UTF-8 format.  This format has a leading two byte sequence
  * that contains the remaining number of bytes to read.  This two byte
  * sequence is read using the @code{readUnsignedShort()} method of this
  * interface.
  *
  * After the number of remaining bytes have been determined, these bytes
  * are read an transformed into @{char} values.  These @code{char} values
  * are encoded in the stream using either a one, two, or three byte format.
  * The particular format in use can be determined by examining the first
  * byte read.  
  *
  * If the first byte has a high order bit of @samp{0}, then
  * that character consists on only one byte.  This character value consists
  * of seven bits that are at positions 0 through 6 of the byte.  As an
  * example, if @code{byte1} is the byte read from the stream, it would
  * be converted to a @code{char} like so:
  *
  * @code{(char)byte1}
  *
  * If the first byte has @code{110} as its high order bits, then the 
  * character consists of two bytes.  The bits that make up the character
  * value are in positions 0 through 4 of the first byte and bit positions
  * 0 through 5 of the second byte.  (The second byte should have 
  * @samp{10} as its high order bits).  These values are in most significant
  * byte first (i.e., "big endian") order.
  *
  * As an example, if @code{byte1} and @code{byte2} are the first two bytes
  * read respectively, and the high order bits of them match the patterns
  * which indicate a two byte character encoding, then they would be
  * converted to a Java @code{char} like so:
  *
  * @code{(char)(((byte1 & 0x1F) << 6) | (byte2 & 0x3F))}
  *
  * If the first byte has a @code{1110} as its high order bits, then the
  * character consists of three bytes.  The bits that make up the character
  * value are in positions 0 through 3 of the first byte and bit positions
  * 0 through 5 of the other two bytes.  (The second and third bytes should
  * have @code{10} as their high order bits).  These values are in most
  * significant byte first (i.e., "big endian") order.
  *
  * As an example, if @code{byte1}, @code{byte2}, and @code{byte3} are the
  * three bytes read, and the high order bits of them match the patterns
  * which indicate a three byte character encoding, then they would be
  * converted to a Java @code{char} like so:
  *
  * @code{(char)(((byte1 & 0x0F) << 12) | ((byte2 & 0x3F) << 6) | (byte3 & 0x3F))}
  *
  * Note that all characters are encoded in the method that requires the
  * fewest number of bytes with the exception of the character with the
  * value of @samp{\u0000} which is encoded as two bytes.  This is a 
  * modification of the UTF standard used to prevent C language style
  * @samp{NUL} values from appearing in the byte stream.
  *
  * This method can read data that was written by an object implementing the
  * @code{writeUTF()} method in @code{DataOutput}.
  * 
  * @returns The @code{String} read
  *
  * @exception EOFException If end of file is reached before reading the String
  * @exception UTFDataFormatException If the data is not in UTF-8 format
  * @exception IOException If any other error occurs
  *
  * @see DataOutput
  */
public final String
readUTF() throws EOFException, UTFDataFormatException, IOException
{
  StringBuffer sb = new StringBuffer("");

  int num_bytes = readUnsignedShort();

  for (int i = 0; i < num_bytes; i++)
    {
      int byte_read = in.read();

      if (byte_read == -1)
        throw new EOFException("Unexpected end of stream");

      // Three byte encoding case
      if ((byte_read & 0xE0) == 0xE0) // 224
        {
          int val = (byte_read & 0x0F) << 12;

          byte_read = in.read();
          if (byte_read == -1)
            throw new EOFException("Unexpected end of stream");

          if ((byte_read & 0x80) != 0x80)
            throw new UTFDataFormatException("Bad byte in input: " + byte_read);

          val |= (byte_read & 0x3F) << 6;

          byte_read = in.read();
          if (byte_read == -1)
            throw new EOFException("Unexpected end of stream");

          if ((byte_read & 0x80) != 0x80)
            throw new UTFDataFormatException("Bad byte in input: " + byte_read);

          val |= (byte_read & 0x3F);

          sb.append((char)val);

          i += 2;
        }
      // Two byte encoding case
      else if ((byte_read & 0xC0) == 0xC0) // 192
        {
          int val = (byte_read & 0x1F) << 6;

          byte_read = in.read();
          if (byte_read == -1)
            throw new EOFException("Unexpected end of stream");

          if ((byte_read & 0x80) != 0x80)
            throw new UTFDataFormatException("Bad byte in input: " + byte_read);

          val |= (byte_read & 0x3F);

          sb.append((char)val);
          
          ++i;
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
  * This method reads raw bytes into the passed array @code{buf} starting
  * @code{offset} bytes into the buffer.  The number of bytes read will be
  * exactly @code{len}.  Note that this method blocks until the data is 
  * available and * throws an exception if there is not enough data left in 
  * the stream to read @code{len} bytes.
  *
  * @param buf The buffer into which to read the data
  * @param offset The offset into the buffer to start storing data
  * @param len The number of bytes to read into the buffer
  *
  * @exception EOFException If end of file is reached before filling the buffer
  * @exception IOException If any other error occurs
  */
public final void
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
  * This method skips and discards the specified number of bytes in an
  * input stream
  *
  * @param num_bytes The number of bytes to skip
  *
  * @return The number of bytes actually skipped, which will always be @code{num_bytes}
  *
  * @exception EOFException If end of file is reached before all bytes can be skipped
  * @exception IOException If any other error occurs
  */
public final int
skipBytes(int n) throws EOFException, IOException
{
  long total_skipped = 0;

  while (total_skipped < n)
    {
      long bytes_skipped = in.skip(total_skipped - n);

      // If we can't skip anymore, try once more, then bomb out
      if (bytes_skipped == 0)
        {
          bytes_skipped = in.skip(total_skipped - n);
          if (bytes_skipped == 0)
            throw new EOFException("Unexpected end of stream");
        }

      total_skipped += bytes_skipped;
    }

  return(n);
}

} // interface DataInput

