/* SQLException.java -- General SQL exception
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
 * This exception is thrown when a database error occurs. This exception
 * keeps track of additional information:<br><ul>
 * <li>The error message string (part of all Throwables)</li>
 * <li>An "SQLstate" string, following either the XOPEN SQLstate or the SQL 99
 *   conventions.</li>
 * <li>A vendor-specific integer error code.</li>
 * <li>A chain to the next exception.</li>
 * <ul>
 *
 * @author Aaron M. Renn (arenn@urbanophile.com)
 * @status updated to 1.4
 */
public class SQLException extends Exception
{
  /**
   * Compatible with JDK 1.1+.
   */
  private static final long serialVersionUID = 2135244094396331484L;

  /**
   * This is the next exception in the chain.
   *
   * @serial the next exception in the chain
   */
  // Package visible for use by SQLWarning.
  SQLException next;

  /**
   * This is the state of the SQL statement at the time of the error.
   *
   * @serial the SQLstate
   */
  private final String SQLState;

  /**
   * The vendor error code for this error.
   *
   * @serial the vendor code
   */
  private final int vendorCode;

  /**
   * Create a new instance that does not have a descriptive messages or SQL
   * state, and which has a vendor error code of 0.
   */
  public SQLException()
  {
    this(null, null, 0);
  }

  /**
   * Create a new instance with the specified descriptive error message.  The
   * SQL state of this instance will be <code>null</code> and the vendor
   * error code will be 0.
   *
   * @param message a string describing the nature of the error
   */
  public SQLException(String message)
  {
    this(message, null, 0);
  }

  /**
   * Create a new instance with the specified descriptive error message
   * and SQL state string. The vendor error code of this instance will be 0.
   *
   * @param message a string describing the nature of the error
   * @param SQLState a string containing the SQL state of the error
   */
  public SQLException(String message, String SQLState)
  {
    this(message, SQLState, 0);
  }

  /**
   * Create a new instance with the specified descriptive error message,
   * SQL state string, and vendor code.
   *
   * @param message a string describing the nature of the error
   * @param SQLState a string containing the SQL state of the error
   * @param vendorCode the vendor error code associated with this error
   */
  public SQLException(String message, String SQLState, int vendorCode)
  {
    super(message);
    this.SQLState = SQLState;
    this.vendorCode = vendorCode;
  }

  /**
   * Get the SQLState information associated with this error.  This
   * implementation formats the value using the XOPEN SQL state conventions.
   *
   * @return The SQL state, which may be <code>null</code>
   */
  public String getSQLState()
  {
    return SQLState;
  }

  /**
   * Get the vendor specific error code associated with this error.
   *
   * @return the vendor specific error code associated with this error
   */
  public int getErrorCode()
  {
    return vendorCode;
  }

  /**
   * Get any exception that is chained to this object.
   *
   * @return the exception chained to this object, or null
   */
  public SQLException getNextException()
  {
    return next;
  }

  /**
   * Add a new exception to the end of the chain of exceptions that are
   * chained to this object.
   *
   * @param e the exception to add to the end of the chain
   */
  public void setNextException(SQLException e)
  {
    if (e == null)
      return;
    SQLException entry = this;
    while (entry.next != null)
      entry = entry.next;
    entry.next = e;
  }
} // class SQLException
