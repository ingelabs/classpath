/* java.util.zip.ZipInputStream
   Copyright (C) 2001 Free Software Foundation, Inc.

This file is part of Jazzlib.

Jazzlib is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

Jazzlib is distributed in the hope that it will be useful, but
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

package java.util.zip;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;

/**
 * This is a FilterInputStream that reads the files in an zip archive
 * one after another.  It has a special method to get the zip entry of
 * the next file.  The zip entry contains information about the file name
 * size, compressed size, CRC, etc.
 *
 * It includes support for STORED and DEFLATED entries.
 *
 * @author Jochen Hoenicke
 */
public class ZipInputStream extends InflaterInputStream implements ZipConstants
{
  private CRC32 crc = new CRC32();
  private ZipEntry entry = null;

  private int csize;
  private int size;
  private int method;
  private int flags;
  private int avail;

  /**
   * Creates a new Zip input stream, reading a zip archive.
   */
  public ZipInputStream(InputStream in)
  {
    super(in, new Inflater(true));
  }

  private void fillBuf() throws IOException
  {
    avail = len = in.read(buf, 0, buf.length);
  }

  private int readBuf(byte[] out, int offset, int length) throws IOException
  {
    if (avail <= 0)
      {
	fillBuf();
	if (avail <= 0)
	  return -1;
      }
    if (length > avail)
      length = avail;
    System.arraycopy(buf, len - avail, out, offset, length);
    avail -= length;
    return length;
  }
  
  private void readFully(byte[] out) throws IOException
  {
    int off = 0;
    int len = out.length;
    while (len > 0)
      {
	int count = readBuf(out, off, len);
	if (count == -1)
	  throw new EOFException();
	off += count;
	len -= count;
      }
  }
  
  private final int readLeByte() throws IOException
  {
    if (avail <= 0)
      {
	fillBuf();
	if (avail <= 0)
	  throw new ZipException("EOF in header");
      }
    return buf[len - avail--] & 0xff;
  }

  /**
   * Read an unsigned short in little endian byte order.
   */
  private final int readLeShort() throws IOException 
  {
    return readLeByte() | (readLeByte() << 8);
  }

  /**
   * Read an int in little endian byte order.
   */
  private final int readLeInt() throws IOException 
  {
    return readLeShort() | (readLeShort() << 16);
  }

  /**
   * Open the next entry from the zip archive, and return its description.
   * If the previous entry wasn't closed, this method will close it.
   */
  public ZipEntry getNextEntry() throws IOException
  {
    if (crc == null)
      throw new IllegalStateException("Closed.");
    if (entry != null)
      closeEntry();

    int header = readLeInt();
    if (header == C_HDR_SIG)
      {
	/* Central Header reached. */
	close();
	return null;
      }
    if (header != L_HDR_SIG)
      throw new ZipException("Wrong Local header signature" + Integer.toHexString(header));
    /* skip version */
    readLeShort();
    flags = readLeShort();
    method = readLeShort();
    int dostime = readLeInt();
    int crc = readLeInt();
    csize = readLeInt();
    size = readLeInt();
    int nameLen = readLeShort();
    int extraLen = readLeShort();

    if (method == ZipOutputStream.STORED && csize != size)
      throw new ZipException("Stored, but compressed != uncompressed");


    byte[] buffer = new byte[nameLen];
    readFully(buffer);
    String name = new String(buffer, "UTF8");
    
    entry = new ZipEntry(name);
    entry.setMethod(method);
    if ((flags & 8) == 0)
      {
	entry.setCrc(crc & 0xffffffffL);
	entry.setSize(size & 0xffffffffL);
	entry.setCompressedSize(csize & 0xffffffffL);
      }
    entry.setDOSTime(dostime);
    if (extraLen > 0)
      {
	byte[] extra = new byte[extraLen];
	readFully(extra);
	entry.setExtra(extra);
      }

    if (method == ZipOutputStream.DEFLATED && avail > 0)
      {
	System.arraycopy(buf, len - avail, buf, 0, avail);
	len = avail;
	avail = 0;
	inf.setInput(buf, 0, len);
      }
    return entry;
  }

  private void readDataDescr() throws IOException
  {
    if (readLeInt() != D_HDR_SIG)
      throw new ZipException("Data descriptor signature not found");
    entry.setCrc(readLeInt() & 0xffffffffL);
    csize = readLeInt();
    size = readLeInt();
    entry.setSize(size & 0xffffffffL);
    entry.setCompressedSize(csize & 0xffffffffL);
  }

  /**
   * Closes the current zip entry and moves to the next one.
   */
  public void closeEntry() throws IOException
  {
    if (crc == null)
      throw new IllegalStateException("Closed.");
    if (entry == null)
      return;

    if (method == ZipOutputStream.DEFLATED)
      {
	if ((flags & 8) != 0)
	  {
	    /* We don't know how much we must skip, read until end. */
	    byte[] tmp = new byte[2048];
	    while (read(tmp) > 0)
	      ;
	    /* read will close this entry */
	    return;
	  }
	csize -= inf.getTotalIn();
	avail = inf.getRemaining();
      }

    if (avail > csize && csize >= 0)
      avail -= csize;
    else
      {
	csize -= avail;
	avail = 0;
	while (csize != 0)
	  {
	    long skipped = in.skip(csize & 0xffffffffL);
	    if (skipped <= 0)
	      throw new ZipException("zip archive ends early.");
	    csize -= skipped;
	  }
      }

    size = 0;
    crc.reset();
    if (method == ZipOutputStream.DEFLATED)
      inf.reset();
    entry = null;
  }

  public int available() throws IOException
  {
    return entry != null ? 1 : 0;
  }

  /**
   * Reads a byte from the current zip entry.
   * @return the byte or -1 on EOF.
   * @exception IOException if a i/o error occured.
   * @exception ZipException if the deflated stream is corrupted.
   */
  public int read() throws IOException
  {
    byte[] b = new byte[1];
    if (read(b, 0, 1) <= 0)
      return -1;
    return b[0] & 0xff;
  }

  /**
   * Reads a block of bytes from the current zip entry.
   * @return the number of bytes read (may be smaller, even before
   * EOF), or -1 on EOF.
   * @exception IOException if a i/o error occured.
   * @exception ZipException if the deflated stream is corrupted.
   */
  public int read(byte[] b, int off, int len) throws IOException
  {
    if (crc == null)
      throw new IllegalStateException("Closed.");
    if (entry == null)
      return -1;
    boolean finished = false;
    switch (method)
      {
      case ZipOutputStream.DEFLATED:
	len = super.read(b, off, len);
	if (len < 0)
	  {
	    if (!inf.finished())
	      throw new ZipException("Inflater not finished!?");
	    avail = inf.getRemaining();
	    if ((flags & 8) != 0)
	      readDataDescr();

	    if (inf.getTotalIn() != csize
		|| inf.getTotalOut() != size)
	      throw new ZipException("size mismatch: "+csize+";"+size+" <-> "+inf.getTotalIn()+";"+inf.getTotalOut());
	    inf.reset();
	    finished = true;
	  }
	break;
	
      case ZipOutputStream.STORED:

	if (len > csize && csize >= 0)
	  len = csize;
	
	len = readBuf(b, off, len);
	if (len > 0)
	  {
	    csize -= len;
	    size -= len;
	  }

	if (csize == 0)
	  finished = true;
	else if (len < 0)
	  throw new ZipException("EOF in stored block");
	break;
      }

    if (len > 0)
      crc.update(b, off, len);

    if (finished)
      {
	if ((crc.getValue() & 0xffffffffL) != entry.getCrc())
	  throw new ZipException("CRC mismatch");
	crc.reset();
	entry = null;
      }
    return len;
  }

  /**
   * Closes the zip file.
   * @exception IOException if a i/o error occured.
   */
  public void close() throws IOException
  {
    super.close();
    crc = null;
    entry = null;
  }

  /**
   * Creates a new zip entry for the given name.  This is equivalent
   * to new ZipEntry(name).
   * @param name the name of the zip entry.
   */
  protected ZipEntry createZipEntry(String name) 
  {
    return new ZipEntry(name);
  }
}
