/* DataTruncation.java -- warning when data has been truncated
   Copyright (C) 1999, 2000, 2002 Free Software Foundation, Inc.

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


package java.sql;

/**
 * This exception is thrown when a piece of data is unexpectedly
 * truncated in JDBC. This has an SQLstate of <code>01004</code>.
 *
 * @author Aaron M. Renn (arenn@urbanophile.com)
 * @status updated to 1.4
 */
public class DataTruncation extends SQLWarning
{
  /**
   * Compatible with JDK 1.1+.
   */
  private static final long serialVersionUID = 6464298989504059473L;

  /**
   * The original size of the data.
   *
   * @serial the original data size
   */
  private final int dataSize;

  /**
   * The index of the parameter or column whose value was truncated.
   *
   * @serial the index that was truncated
   */
  private final int index;

  /**
   * Indicates whether or not a parameter value was truncated.
   *
   * @serial true if a parameter was truncated
   */
  private final boolean parameter;

  /**
   * Indicates whether or not a data column value was truncated.
   *
   * @serial true if a data column was read before truncation
   */
  private final boolean read;

  /**
   * This is the size of the data after truncation.
   *
   * @serial the size actually transferred
   */
  private final int transferSize;

  /**
   * Create a new instance with the specified values.  The descriptive error
   * message for this exception will be "Data truncation", the SQL state
   * will be "01004", and the vendor specific error code will be set to 0.
   *
   * @param index the index of the parameter or column that was truncated
   * @param parameter <code>true</code> if a parameter was truncated
   * @param read <code>true</code> if a data column was truncated
   * @param dataSize the original size of the data
   * @param transferSize the size of the data after truncation
   */
  public DataTruncation(int index, boolean parameter, boolean read,
                        int dataSize, int transferSize)
  {
    super("Data truncation", "01004");
    this.index = index;
    this.parameter = parameter;
    this.read = read;
    this.dataSize = dataSize;
    this.transferSize = transferSize;
  }

  /**
   * Get the index of the column or parameter that was truncated.
   *
   * @return the index of the column or parameter that was truncated
   */
  public int getIndex()
  {
    return index;
  }

  /**
   * Return true if it was a parameter that was truncated.
   *
   * @return <code>true</code> if a parameter was truncated
   */
  public boolean getParameter()
  {
    return parameter;
  }

  /**
   * Return true if it was a column that was truncated.
   *
   * @return <code>true</code> if a column was truncated
   */
  public boolean getRead()
  {
    return read;
  }

  /**
   * This method returns the original size of the parameter or column that
   * was truncated.
   *
   * @return the original size
   */
  public int getDataSize()
  {
    return dataSize;
  }

  /**
   * This method returns the size of the parameter or column after it was
   * truncated.
   *
   * @return the truncated size
   */
  public int getTransferSize()
  {
    return transferSize;
  }
} // class DataTruncation
