/* VMObject.java
   Copyright (C) 1998 Free Software Foundation

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

/**
 ** Object is the ultimate superclass of every class
 ** (excepting interfaces).  When you define a class that
 ** does not extend any other class, it implicitly extends
 ** java.lang.Object.
 **
 ** It provides general-purpose methods that every single
 ** Object, regardless of race, sex or creed, implements.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 6 1998
 **/

class VMObject {
	/** This method may be called to create a new copy of the
	 ** Object.  However, there are *no* requirements at all
	 ** placed on this method, just suggestions.  The ==,
	 ** equals() and instanceof comparisons may even return
	 ** false when comparing the original with the clone!<P>
	 **
	 ** If the Object you call clone() on does not implement
	 ** Cloneable (which is a placeholder interface), then
	 ** a CloneNotSupportedException is thrown.<P>
	 **
	 ** Object's implementation of clone allocates space for
	 ** the new Object using the correct class, and then fills
	 ** in all of the new field values with the old field
	 ** values.  Thus, it is a shallow copy.
	 **
	 ** @exception CloneNotSupportedException
	 ** @return a copy of the Object.
	 **/
	static native Object clone(Object o) throws CloneNotSupportedException;

	/** Returns the class of this Object as a Class object.
	 ** @return the class of this Object.
	 ** @see java.lang.Class
	 **/
	static native Class getClass(Object o);

	/** Wakes up one of the threads that is waiting on this
	 ** Object's monitor.  Only the owner of a lock on the
	 ** Object may call this method.<P>
	 **
	 ** The Thread to wake up is chosen arbitrarily.<P>
	 **
	 ** If the Thread waiting on this Object is waiting
	 ** because it wants to obtain the lock, then the notify()
	 ** call will in essence do nothing, since the lock will
	 ** still be owned by the Thread that called notify().
	 **
	 ** @exception IllegalMonitorStateException if this Thread
	 **            does not own the lock on the Object.
	 **/
	static native void notify(Object o) throws IllegalMonitorStateException;

	/** Wakes up all of the threads waiting on this Object's
	 ** monitor.  Only the owner of the lock on this Object
	 ** may call this method.<P>
	 **
	 ** If the Threads waiting on this Object are waiting
	 ** because they want to obtain the lock, then the
	 ** notifyAll() call will in essence do nothing, since the
	 ** lock will still be owned by the Thread that called
	 ** notifyAll().
	 **
	 ** @exception IllegalMonitorStateException if this Thread
	 **            does not own the lock on the Object.
	 **/
	static native void notifyAll(Object o) throws IllegalMonitorStateException;

	/** Waits a specified amount of time for notify() or
	 ** notifyAll() to be called on this Object.  This call
	 ** behaves almost identically to wait(int ms), except it
	 ** throws nanoseconds into the pot.  It's fairly useless,
	 ** though; if we can only roughly estimate the number of
	 ** milliseconds to wait, how do you think we can exactly
	 ** deal with nanoseconds?
	 ** @param ms the number of milliseconds to wait (1,000
	 **        milliseconds = 1 second).
	 ** @param ns the number of nanoseconds to wait over and
	 **        above ms (1,000,000 nanoseconds = 1 second).
	 ** @exception IllegalMonitorStateException if this Thread
	 **            does not own a lock on this Object.
	 ** @exception InterruptedException if some other Thread
	 **            interrupts this Thread.
	 **/
	static native void wait(Object o,long ms, int ns) throws IllegalMonitorStateException, InterruptedException;
}
