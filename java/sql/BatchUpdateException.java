/* BatchUpdateException.java -- exception for batch oriented SQL errors
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
 * This class extends <code>SQLException</code> to count the successful
 * updates in each statement in a batch that was successfully updated prior
 * to the error. The driver may continue to process batch commands, in which
 * case those commands also have an entry in the array. So, to tell which
 * command failed, search for <code>Statement.EXECUTE_FAILED</code>.
 *
 * @author Aaron M. Renn (arenn@urbanophile.com)
 * @since 1.2
 * @status updated to 1.4
 */
public class BatchUpdateException extends SQLException
{
  /**
   * Compatible with JDK 1.2+.
   */
  private static final long serialVersionUID = 5977529877145521757L;

  /**
   * This is the array of update counts for the commands which completed
   * successfully prior to the error.
   *
   * @serial the outcome of the batch execution
   */
  private final int[] updateCounts;

  /**
   * Create a new instance with no descriptive error message.  The SQL state
   * and update count will be initialized to <code>null</code> and the
   * vendor specific error code will initialized to 0.
   */
  public BatchUpdateException()
  {
    this(null, null, 0, null);
  }

  /**
   * Create a new instance with the specified update count information and
   * no descriptive error message.  This SQL state will be initialized to
   * <code>null</code> and the vendor specific error code will be initialized
   * to 0.
   *
   * @param updateCounts the update count array
   */
  public BatchUpdateException(int[] updateCounts)
  {
    this(null, null, 0, updateCounts);
  }

  /**
   * Create a new instance with the specified descriptive error message and
   * update count information. The SQL state will be initialized to
   * <code>null</code> and the vendor specific error code will be
   * initialized to 0.
   *
   * @param message the descriptive error message
   * @param updateCounts the update count information for this error
   */
  public BatchUpdateException(String message, int[] updateCounts)
  {
    this(message, null, 0, updateCounts);
  }

  /**
   * Create a new instance with the specified descriptive error message,
   * SQL state, and update count information.  The vendor specific error
   * code will be initialized to 0.
   *
   * @param message the descriptive error message
   * @param state the SQL state information for this error
   * @param updateCounts the update count information for this error
   */
  public BatchUpdateException(String message, String state,
                              int[] updateCounts)
  {
    this(message, state, 0, updateCounts);
  }

  /**
   * Create a new instance with the specified descriptive error message,
   * SQL state, vendor specific error code and update count information.
   *
   * @param message the descriptive error message
   * @param state the SQL state information for this error
   * @param vendorCode the vendor specific error code for this error
   * @param updateCounts the update count information for this error
   */
  public BatchUpdateException(String message, String state, int vendorCode,
                              int[] updateCounts)
  {
    super(message, state, vendorCode);
    this.updateCounts = updateCounts;
  }

  /**
   * Get the update count information for this error.  If not
   * <code>null</code>, this is an array of <code>int</code>'s that are
   * the update accounts for each command that was successfully executed.
   * The array elements are in the order that the commands were executed.
   *
   * @return the update count information, which may be <code>null</code>
   */
  public int[] getUpdateCounts()
  {
    return updateCounts;
  }
} // class BatchUpdateException
