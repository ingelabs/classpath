/* ExceptionInInitializerError.java 
   Copyright (C) 1998 Free Software Foundation, Inc.

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


package java.lang;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An <code>ExceptionInInitializerError</code> is thrown when an 
 * unexpected exception has occurred in a static initializer or the
 * initializer for a static variable.
 *
 * @since JDK 1.1
 * 
 * @author Brian Jones
 */
public class ExceptionInInitializerError extends LinkageError
{
  static final long serialVersionUID = 1521711792217232256L;

  private Throwable t = null;

  /**
   * Create an error without a message.
   */
  public ExceptionInInitializerError()
    {
      super();
    }

  /**
   * Create an error with a message.
   */
  public ExceptionInInitializerError(String s)
    {
      super(s);
    }

  /**
   * Creates an error an saves a reference to the <code>Throwable</code>
   * object.
   *
   * @param t the exception thrown
   */
  public ExceptionInInitializerError(Throwable t)
    {
      super();
      this.t = t;
    }

  /** 
   * Return the exception that caused this error to be created.
   * @return the stored <code>Throwable</code> object or <code>null</code>
   * if this <code>ExceptionInInitializerError</code> has no stored
   * <code>Throwable</code> object.
   */
  public Throwable getException()
    {
      return t;
    }

  /**
   * Print a stack trace of the exception that occurred.
   */
  public void printStackTrace()
    {
      if (t == null)
	{
	  super.printStackTrace();
	}
      else
	{
	  t.printStackTrace();
	}
    }

  /**
   * Print a stack trace of the exception that occurred to 
   * the specified <code>PrintStream</code>.
   */
  public void printStackTrace(PrintStream ps)
    {
      if (t == null)
	{
	  super.printStackTrace(ps);
	}
      else
	{
	  t.printStackTrace(ps);
	}
    }

  /**
   * Print a stack trace of the exception that occurred to 
   * the specified <code>PrintWriter</code>.
   */
  public void printStackTrace(PrintWriter pw)
    {
      if (t == null)
	{
	  super.printStackTrace(pw);
	}
      else
	{
	  t.printStackTrace(pw);
	}
    }

  /**
   * Serialize the object in a manner binary compatible with the JDK 1.2
   */
  private void writeObject(java.io.ObjectOutputStream s) 
    throws IOException
    {
      ObjectOutputStream.PutField oFields;
      oFields = s.putFields();
      oFields.put("exception", this.t);
      s.writeFields();
    }

  /**
   * Deserialize the object in a manner binary compatible with the JDK 1.2
   */    
  private void readObject(java.io.ObjectInputStream s)
    throws IOException, ClassNotFoundException
    {
      ObjectInputStream.GetField oFields;
      oFields = s.readFields();
      this.t = (Throwable)oFields.get("exception", (Throwable)null);
    }
}
