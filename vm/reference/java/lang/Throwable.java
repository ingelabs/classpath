/* java.lang.Throwable
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


package java.lang;

import java.io.Serializable;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * Throwable is the superclass of all exceptions that can be raised.
 *
 * @version 1.1.0, Oct 5 1998
 * @author Brian Jones
 * @author John Keiser
 * @since JDK1.0
 */
public class Throwable extends Object implements Serializable 
{
  static final long serialVersionUID = -3042686055658047285L;

  private String message = null;

  /** 
   * Instantiate this Throwable with an empty message. 
   */
  public Throwable() {
    this(null);
  }
  
  /**
   * Instantiate this Throwable with the given message.
   * @param message the message to associate with the Throwable.
   */
  public Throwable(String message) {
    fillInStackTrace();
    this.message = message;
  }
  
  /**
   * Get the message associated with this Throwable.
   * @return the error message associated with this Throwable.
   */
  public String getMessage() {
    return message;
  }

  /** 
   * Get a localized version of this Throwable's error message.
   * This method must be overridden in a subclass of Throwable
   * to actually produce locale-specific methods.  The Throwable
   * implementation just returns getMessage().
   *
   * @return a localized version of this error message.
   */
  public String getLocalizedMessage() {
    return getMessage();
  }
  
  /**
   * Get a human-readable representation of this Throwable.
   * @return a human-readable String represting this Throwable.
   * @XXX find out what exactly this should look like.
   */
  public String toString() {
    return getClass().getName() + (message != null ? ": " + message : "");
  }
  
  /** 
   * Print a stack trace to the standard error stream.
   */
  public void printStackTrace() {
    printStackTrace(System.err);
  }
  
  /** 
   * Print a stack trace to the specified PrintStream.
   * @param s the PrintStream to write the trace to.
   */
  public void printStackTrace(PrintStream s) {
    s.println(toString());
    printStackTrace0 (s);
  }
  
  /** 
   * Print a stack trace to the specified PrintWriter.
   * @param w the PrintWriter to write the trace to.
   */
  public void printStackTrace(PrintWriter w) {
    w.println(toString());
    printStackTrace0 (w);
  }

  private native void printStackTrace0 (Object stream);

  /** 
   * Fill in the stack trace with the current execution stack.
   * Normally used when rethrowing an exception, to strip
   * off unnecessary extra stack frames.
   * @return this same throwable.
   */
  public native Throwable fillInStackTrace();

  /**
   * Serialize the object in a manner binary compatible with the JDK 1.2
   */
  private void writeObject(java.io.ObjectOutputStream s) 
    throws IOException
    {
      ObjectOutputStream.PutField oFields;
      oFields = s.putFields();
      oFields.put("detailMessage", message);
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
      message = (String)oFields.get("detailMessage", (String)null);
    }
}
