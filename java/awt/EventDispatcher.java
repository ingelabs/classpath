/*************************************************************************
/* EventDispatcher.java -- Event queue dispatch thread
/*
/* Copyright (c) 1999 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.awt;

/**
  * This class processes a particular event queue.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
class EventDispatcher implements Runnable
{

/*
 * Instance Variables
 */

// Event queue to process
private EventQueue queue;

// Variable to stop thread
private boolean stopped = false;

/*
 * Constructors
 */

public
EventDispatcher(EventQueue queue)
{
  System.err.println("Dispatcher created");
  this.queue = queue;
}

/*
 * Instance Variables
 */
public void
stop()
{
  this.stopped = true;
}

public void
run()
{
  System.err.println("Starting dispatchher run loop");
  while(!stopped)
    {
      System.err.println("Looping....");
      try 
        {
          AWTEvent event = queue.getNextEvent();
          ((Component)event.getSource()).dispatchEvent(event);
        }
      catch(Exception e) { ; }
    }
}

} // class EventDispather 

