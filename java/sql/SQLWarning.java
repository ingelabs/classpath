/* SQLWarning.java -- database access warnings
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
 * This exception is thrown when a database warning occurs. Warnings are
 * silently chained to the object which caused them.
 *
 * <p>Warnings may be retrieved from <code>Connection</code>,
 * <code>Statement</code>, and <code>ResultSet</code> objects. Trying to
 * retrieve a warning one of these after it has been closed will cause an
 * exception to be thrown. Note that closing a statement also closes a result
 * set that it might have produced.
 *
 * @author Aaron M. Renn (arenn@urbanophile.com)
 * @see Connection#getWarnings()
 * @see Statement#getWarnings()
 * @see ResultSet#getWarnings()
 */
public class SQLWarning extends SQLException
{
  /**
   * Compatible with JDK 1.1+.
   */
  private static final long serialVersionUID = 3917336774604784856L;

  /**
   * Create a new instance that does not have a descriptive messages or SQL
   * state, and which has a vendor error code of 0.
   */
  public SQLWarning()
  {
  }

  /**
   * Create a new instance with the specified descriptive error message.
   * The SQL state of this instance will be <code>null</code> and the vendor
   * error code will be 0.
   *
   * @param message a string describing the nature of the error
   */
  public SQLWarning(String message)
  {
    super(message);
  }

  /**
   * Create a new instance with the specified descriptive error message
   * and SQL state string. The vendor error code of this instance will be 0.
   *
   * @param message a string describing the nature of the error
   * @param SQLState a string containing the SQL state of the error
   */
  public SQLWarning(String message, String SQLState)
  {
    super(message, SQLState);
  }

  /**
   * Create a new instance with the specified descriptive error message,
   * SQL state string, and vendor code.
   *
   * @param message a string describing the nature of the error
   * @param SQLState a string containing the SQL state of the error
   * @param vendorCode the vendor error code associated with this error
   */
  public SQLWarning(String message, String SQLState, int vendorCode)
  {
    super(message, SQLState, vendorCode);
  }

  /**
   * Get the warning that is chained to this object.
   *
   * @return the exception chained to this object, or null
   * @throws Error if the next element is not an SQLWarning
   */
  public SQLWarning getNextWarning()
  {
    if (next == null)
      return null;
    if (! (next instanceof SQLWarning))
      throw new Error("SQLWarning chain holds value that is not a SQLWarning");
    return (SQLWarning) next;
  }

  /**
   * Set the new end of the warning chain.
   *
   * @param e the exception to add to the end of the chain
   */
  public void setNextWarning(SQLWarning e)
  {
    setNextException(e);
  }
} // class SQLWarning
