/* java.util.zip.Checksum
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

/**
 * This is an interface for calculating checksums.
 *
 * @author Jochen Hoenicke
 * @since JDK 1.1
 */
public interface Checksum {

  /**
   * Updates the checksum with the byte b.
   * @param b the byte, only the lower 8 bits are used.
   */
  public void update(int b); 

  /**
   * Calculates the checksum of the given part of the byte array, updating
   * its current value.  
   * @param b an array of bytes
   * @param off the offset into the array.
   * @param len the length.
   */
  public void update(byte[] b, int off, int len);

  /**
   * Resets the checksum to the initial value.
   */
  public void reset();

  /**
   * Gets the current checksum value.
   */
  public long getValue(); 
}

