/* EventQueue.java -- Event queuing mechanism
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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.awt;

/**
  * This class manages a queue of <code>AWTEvent</code> objects that
  * are posted to it.  The AWT system uses only one event queue for all
  * events.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class EventQueue
{

/*
 * Instance Methods
 */

// The queue.  We added a package protected field to AWTEvent to make it
// a convenient linked list.
private AWTEvent queue;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>EventQueue</code>.
  */
public
EventQueue()
{
  EventDispatcher ed = new EventDispatcher(this);
  Thread t = new Thread(ed);
  t.start();
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * Posts a new event to the queue.
  *
  * @param event The event to post to the queue.
  */
public synchronized void
postEvent(AWTEvent event)
{
  if (queue == null)
    queue = event;
  else
    {
      AWTEvent end = queue;
      while (end.next != null)
        end = end.next;

      end.next = queue;
    }
  notify();
}

/*************************************************************************/

/**
  * Returns the next event in the queue.  This method will block until
  * an event is available or until the thread is interrupted.
  *
  * @return The next event in the queue.
  *
  * @exception InterruptedException If this thread is interrupted while
  * waiting for an event to be posted to the queue.
  */
public synchronized AWTEvent
getNextEvent() throws InterruptedException
{
  for (;;)
    {
      if (queue != null)
        {
          AWTEvent evt = queue;
          queue = queue.next;
          evt.next = null;
          return(evt);
        }
      wait();
    }
}
 
/*************************************************************************/

/**
  * Returns the next event in the queue without removing it from the queue.
  * This method will block until an event is available or until the thread
  * is interrupted.
  *
  * @return The next event in the queue.
  */
public synchronized AWTEvent
peekEvent()
{
  for (;;)
    {
      if (queue != null)
        {
          // We are taking a chance that the user doesn't repost the same
          // event back to the queue, but AWTEvent isn't cloneable so we
          // don't have much choice.  If this becomes a problem we'll have
          // to write a wrapper class for enqueuing.
          return(queue);
        }

      try
        {
          wait();
        }
      catch(InterruptedException e) {  }
    }
}

/*************************************************************************/

/**
  * Returns the next event in the queue that has the specified id
  * without removing it from the queue.
  * This method will block until an event is available or until the thread
  * is interrupted.
  *
  * @param id The event id to return.
  *
  * @return The next event in the queue.
  */
public synchronized AWTEvent
peekEvent(int id)
{
  for(;;)
    {
      if (queue != null)
        {
          AWTEvent evt = queue;
          while (evt != null)
            {
              if (evt.getID() == id)
                 return(evt);
              evt = evt.next;
            }
        }

      try
        {
          wait();
        }
      catch(InterruptedException e) {  }
    }
}

} // class EventQueue

