/* InvocationEvent.java -- Call a runnable when dispatched.
   Copyright (C) 1999 Free Software Foundation, Inc.

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


package java.awt.event;

import java.awt.ActiveEvent;
import java.awt.AWTEvent;

/**
  * This event executes the <code>run()</code> method of a <code>Runnable</code>
  * when it is dispatched.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class InvocationEvent extends AWTEvent implements ActiveEvent,
             java.io.Serializable
{

/*
 * Static Variables
 */

/**
  * This is the first id in the range of event ids used by this class.
  */
public static final int INVOCATION_FIRST = 3838;

/**
  * This is the last id in the range of event ids used by this class.
  */
public static final int INVOCATION_LAST = 3838;

/**
  * This is the default id for this event type.
  */
public static final int INVOCATION_DEFAULT = 3838;

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * @serial This is the <code>Runnable</code> object to call when dispatched.
  */
protected Runnable runnable;

/**
  * @serial This is the object to call <code>notifyAll()</code> on when 
  * the call to <code>run()</code> returns, or <code>null</code> if no 
  * object is to be notified.
  */
protected Object notifier;

/**
  * @serial This variable is set to <code>true</code> if exceptions are caught 
  * and stored in a variable during the call to <code>run()</code>, otherwise
  * exceptions are ignored and propagate up.
  */
protected boolean catchExceptions;

/**
  * @serial This is the caught exception thrown in the <code>run()</code> 
  * method.
  */
private Exception exception;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>InvocationEvent</code> that has
  * the specified source, id, runnable object, and notification object.
  * It will also catch exceptions if that behavior is specified.
  *
  * @param source The source of the event.
  * @param id The parameter id.
  * @param runnable The <code>Runnable</code> object to invoke.
  * @param notifier The object to call <code>notifyAll</code> on when
  * the invocation of the runnable object is complete.
  * @param catchExceptions <code>true</code> if exceptions are caught when 
  * the runnable is running, <code>false</code> otherwise.
  */
public
InvocationEvent(Object source, int id, Runnable runnable, Object notifier,
                boolean catchExceptions)
{
  super(source, id);
  this.runnable = runnable;
  this.notifier = notifier;
  this.catchExceptions = catchExceptions;
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>InvocationEvent</code> with the
  * specified source, runnable, and notifier.  It will also catch
  * exceptions if specified.
  *
  * @param source The source of the event.
  * @param runnable The <code>Runnable</code> object to invoke.
  * @param notifier The object to call <code>notifyAll</code> on when
  * the invocation of the runnable object is complete.
  * @param catchExceptions <code>true</code> if exceptions are caught when 
  * the runnable is running, <code>false</code> otherwise.
  */
public
InvocationEvent(Object source, Runnable runnable, Object notifier,
                boolean catchExceptions)
{
  this(source, INVOCATION_DEFAULT, runnable, notifier, catchExceptions);
}

/*************************************************************************/

/**
  * Initializes a new instance of <code>InvocationEvent</code> with the
  * specified source and runnable.
  *
  * @param source The source of the event.
  * @param runnable The <code>Runnable</code> object to invoke.
  */
public
InvocationEvent(Object source, Runnable runnable)
{
  this(source, INVOCATION_DEFAULT, runnable, null, false);
}
  
/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This method calls the <code>run()</code> method of the runnable and
  * performs any other functions specified in the constructor.
  */
public void
dispatch()
{
  if (catchExceptions)
    {
      try
        {
          runnable.run();
        }
      catch(Exception e)
        {
          this.exception = e;
        }
    }
  else
    {
      runnable.run();
    }

  if (notifier != null)
    notifier.notifyAll();
}

/*************************************************************************/

/**
  * This method returns the exception that occurred during the execution of
  * the runnable, or <code>null</code> if not exception was thrown or
  * exceptions were not caught.
  *
  * @return The exception thrown by the runnable.
  */
public Exception
getException()
{
  return(exception);
}

/*************************************************************************/

/**
  * This method returns a string identifying this event.
  *
  * @return A string identifying this event.
  */
public String
paramString()
{
  return(getClass().getName());
}

} // class InvocationEvent

