/* InvocationTargetException.java - Wrapper exception for reflection
   Copyright (C) 1998, 1999, 2000, 2001 Free Software Foundation, Inc.

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


package java.lang.reflect;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * InvocationTargetException is sort of a way to "wrap" whatever exception 
 * comes up when a method or constructor is called via Reflection. As of
 * JDK 1.4, it was retrofitted to match the exception chaining of all other
 * exceptions, but <code>getTargetException()</code> still works.
 *
 * @author John Keiser
 * @author Tom Tromey <tromey@cygnus.com>
 * @author Eric Blake <ebb9@email.byu.edu>
 * @see Method#invoke(Object,Object[])
 * @see Constructor#newInstance(Object[])
 * @since 1.1
 * @status stuck at 1.3; awaiting exception chaining in Throwable
 */

public class InvocationTargetException
  extends Exception 
{
  /**
   * Compatible with JDK 1.1.
   */
  private static final long serialVersionUID = 4085088731926701167L;

  /**
   * The chained exception. This field is only around for serial compatibility.
   * @serial the chained exception
   */
  private final Throwable target;

  /**
   * Construct an exception with null as the cause.
   */
  protected InvocationTargetException() 
  {
    // XXX Use this implementation when Throwable is ready.
    // this(null);

    super();
    target = null;
  }
  
  /**
   * Create an <code>InvocationTargetException</code> using another 
   * exception.
   *
   * @param targetException the exception to wrap
   */
  public InvocationTargetException(Throwable targetException) 
  {
    // XXX Use this implementation when Throwable is ready.
    // super(targetException);
    // target = targetException;

    super(targetException.toString());
    target = targetException;
  }
  
  /** 
   * Create an <code>InvocationTargetException</code> using another 
   * exception and an error message.
   *
   * @param targetException the exception to wrap
   * @param err an extra reason for the exception-throwing
   */
  public InvocationTargetException(Throwable targetException, String err) 
  {
    // XXX Use this implementation when Throwable is ready.
    // super(err, targetException);
    // target = targetException;

    super(err);
    target = targetException;
  }

  /**
   * Get the wrapped (targeted) exception.
   *
   * @return the targeted exception.
   * @see #getCause()
   */
  public Throwable getTargetException() 
  {
    return target;
  }

  /**
   * Returns the cause of this exception (which may be null).
   *
   * @return the cause.
   * @since 1.4
   */
  public Throwable getCause()
  {
    return target;
  }
}
