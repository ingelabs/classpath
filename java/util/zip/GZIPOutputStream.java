/* java.util.zip.GZIPOutputStream
   Copyright (C) 2001 Free Software Foundation, Inc.

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

package java.util.zip;

import java.io.OutputStream;
import java.io.IOException;


/**
 * This filter stream is used to compress a stream into a "GZIP" stream. 
 * The "GZIP" format is described in RFC 1952.
 *
 * @author John Leuner
 * @since JDK 1.1
 */


public class GZIPOutputStream extends DeflaterOutputStream implements GZIPConstants {

  //Variables

  /* CRC-32 value for uncompressed data
   */
  
  protected CRC32 crc = new CRC32(); 

  // Constructors

  /* Creates a GZIPOutputStream with the default buffer size
   *
   *
   * @param out The stream to read data (to be compressed) from 
   * 
   */

  public GZIPOutputStream(OutputStream out)  throws IOException
  {
    this(out, 4096);
  }

  /* Creates a GZIPOutputStream with the specified buffer size
   *
   *
   * @param out The stream to read compressed data from 
   * 
   * @param size Size of the buffer to use 
   */

  public GZIPOutputStream(OutputStream out, int size)  throws IOException
  {
    super(out, new Deflater(Deflater.DEFAULT_COMPRESSION, true), size);
    
    int mod_time = (int) (System.currentTimeMillis() / 1000L);
    byte[] gzipHeader =
      {
	/* The two magic bytes */
	(byte) (GZIP_MAGIC >> 8), (byte) GZIP_MAGIC,
	  
	/* The compression type */
	(byte) Deflater.DEFLATED,
	
        /* The flags (not set) */
	0,
	
	/* The modification time */
	(byte) mod_time, (byte) (mod_time >> 8), 
	(byte) (mod_time >> 16), (byte) (mod_time >> 24), 

	/* The extra flags */
	0,
    
	/* The OS type (unknown) */
	(byte) 255
      };

    out.write(gzipHeader);
    //    System.err.println("wrote GZIP header (" + gzipHeader.length + " bytes )");
  }
  
  public void write(byte[] buf, int off, int len) throws IOException
  {
    crc.update(buf, off, len);
    super.write(buf, off, len);
  }
  
  /** Writes remaining compressed output data to the output stream
   * and closes it.
   */
  public void close() throws IOException
  {
    finish();
    out.close();
  }

  public void finish() throws IOException
  {

    super.finish();

    int totalin = def.getTotalIn();
    int crcval = (int) (crc.getValue() & 0xffffffff);

    //    System.err.println("CRC val is " + Integer.toHexString( crcval ) 		       + " and length " + Integer.toHexString(totalin));
    
    byte[] gzipFooter = 
      {
	(byte) crcval, (byte) (crcval >> 8),
	(byte) (crcval >> 16), (byte) (crcval >> 24),

	(byte) totalin, (byte) (totalin >> 8),
	(byte) (totalin >> 16), (byte) (totalin >> 24)
      };

    out.write(gzipFooter);
  //    System.err.println("wrote GZIP trailer (" + gzipFooter.length + " bytes )");    
  }
}
