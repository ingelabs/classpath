/*************************************************************************
/* DataInput.java -- Interface for reading data from a stream
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

package java.io;

/**
  * This interface is implemented by classes that can data from streams 
  * into Java primitive types. 
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface DataInput
{

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
public abstract boolean
readBoolean() throws EOFException, IOException;

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
public abstract byte
readByte() throws EOFException, IOException;

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
public abstract int
readUnsignedByte() throws EOFException, IOException;

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
  * @code{(char)((byte1 << 8) + byte2)}
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
public abstract char
readChar() throws EOFException, IOException;

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
  * @code{(short)((byte1 << 8) + byte2)}
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
public abstract short
readShort() throws EOFException, IOException;

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
  * @code{(int)((byte1 << 8) + byte2)}
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
public abstract int
readUnsignedShort() throws EOFException, IOException;

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
  * @code{(int)((byte1 << 24) + (byte2 << 16) + (byte3 << 8) + byte4))}
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
public abstract int
readInt() throws EOFException, IOException;

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
  * @code{(long)((byte1 << 56) + (byte2 << 48) + (byte3 << 40) + 
  * (byte4 << 32) + (byte5 << 24) + (byte6 << 16) + (byte7 << 8) + byte9))}
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
public abstract long
readLong() throws EOFException, IOException;

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
public abstract float
readFloat() throws EOFException, IOException;

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
public abstract double
readDouble() throws EOFException, IOException;

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
  */
public abstract String
readLine() throws IOException;

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
  * @code{(char)(((byte1 & 0x1F) << 6) + (byte2 & 0x3F))}
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
  * @code{(char)(((byte1 & 0x0F) << 12) + ((byte2 & 0x3F) + (byte3 & 0x3F))}
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
public abstract String
readUTF() throws EOFException, UTFDataFormatException, IOException;

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
public abstract void
readFully(byte[] buf) throws EOFException, IOException;

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
public abstract void
readFully(byte[] buf, int offset, int len) throws EOFException, IOException;

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
public abstract int
skipBytes(int n) throws EOFException, IOException;

} // interface DataInput

