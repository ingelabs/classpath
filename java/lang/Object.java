/*
 * java.lang.Object: part of the Java Class Libraries project.
 * Copyright (C) 1998 Free Software Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

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
 ** @since JDK1.0
 **/

public class Object {
	/** Determine whether this Object is semantically equal
	 ** to another Object.<P>
	 **
	 ** There are some fairly strict requirements on this
	 ** method which subclasses must follow:<P>
	 **
	 ** <UL>
	 ** <LI>It must be transitive.  If a.equals(b) and
	 **     b.equals(c) then a.equals(c) should be true
	 **     as well.</LI>
	 ** <LI>It must be symmetric.  If a.equals(b) then
	 **     b.equals(a) must be true as well.  If !a.equals(b)
	 **     then b.equals(a) must be false.</LI>
	 ** <LI>It must be reflexive.  a.equals(a) must always be
	 **     true.</LI>
	 ** <LI>a.equals(null) must be false.</LI>
	 ** </UL>
	 **
	 ** The Object implementation of equals returns this == o.
	 **
	 ** @param o the Object to compare to.
	 ** @return whether this Object is semantically equal to
	 **         another.
	 ** @since JDK1.0
	 **/
	public boolean equals(Object o) {
		return this == o;
	}

	/** Get a value that represents this Object, as uniquely as possible within
	 ** the confines of an int.  This method is called on Objects.<P>
	 **
	 ** The Object implementation returns System.identityHashCode(this);
	 ** @return the hash code for this Object.
	 ** @since JDK1.0
	 **/
	public int hashCode() {
		return System.identityHashCode(this);
	}

	/** Convert this Object to a human-readable String.
	 ** There are no limits placed on how long this String
	 ** should be or what it should contain.  We suggest you
	 ** make it as intuitive as possible to be able to place
	 ** it into System.out.println().<P>
	 **
	 ** The Object implementation of toString() returns
	 ** <CODE>getClass().getName() + "@" + Integer.toHexString(hashCode())</CODE>.
	 **
	 ** @return the String representing this Object.
	 ** @since JDK1.0
	 **/
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode());
	}

	/** Called on every object at some point after the Object
	 ** is determined unreachable and before it is destroyed.
	 ** You would think that this means it eventually is
	 ** called on every Object, but this is not necessarily
	 ** the case.  If execution terminates abnormally, garbage
	 ** collection does not always happen.  Thus you cannot
	 ** rely on this method to always work.<P>
	 **
	 ** finalize() will be called by a Thread that has no
	 ** locks on any Objects.  Why this is important, I have
	 ** no idea, but Sun says it's so, so it's so.<P>
	 **
	 ** If an Exception is thrown from finalize(), it will be
	 ** patently ignored and the Object will still be
	 ** destroyed.<P>
	 **
	 ** The Object implementation of finalize() does nothing.
	 ** @since JDK1.0
	 **/
	protected void finalize() throws Throwable {
	}

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
	 ** @since JDK1.0
	 **/
	protected Object clone() throws CloneNotSupportedException {
		if(this instanceof Cloneable) {
			return VMObject.clone(this);
		} else {
			throw new CloneNotSupportedException();
		}
	}

	/** Returns the class of this Object as a Class object.
	 ** @return the class of this Object.
	 ** @see java.lang.Class
	 ** @since JDK1.0
	 **/
	public final native Class getClass();

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
	 ** @since JDK1.0
	 **/
	public final void notify() throws IllegalMonitorStateException {
		VMObject.notify(this);
	}

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
	 ** @since JDK1.0
	 **/
	public final void notifyAll() throws IllegalMonitorStateException {
		VMObject.notifyAll(this);
	}

	/** Waits indefinitely for notify() or notifyAll() to be
	 ** called on the Object in question.  Implementation is
	 ** identical to wait(0).  Most sane implementations just
	 ** call wait(0).
	 **
	 ** @exception IllegalMonitorStateException if this Thread
	 **            does not own a lock on this Object.
	 ** @exception InterruptedException if some other Thread
	 **            interrupts this Thread.
	 ** @since JDK1.0
	 **/
	public final void wait() throws IllegalMonitorStateException, InterruptedException {
		VMObject.wait(this,0,0);
	}

	/** Waits a specified amount of time (or indefinitely if
	 ** the time specified is 0) for someone to call notify()
	 ** or notifyAll() on this Object, waking up this Thread.<P>
	 **
	 ** The Thread that calls wait() loses all locks it has
	 ** when this method is called.  They are restored when
	 ** the method completes (even if it completes
	 ** abnormally).<P>
	 **
	 ** If another Thread interrupts this Thread, the method
	 ** will terminate with an InterruptedException.<P>
	 **
	 ** The Thread that calls wait() must have a lock on this
	 ** Object.<P>
	 **
	 ** The waiting period is actually only *roughly* the
	 ** amount of time you requested.  It cannot be exact
	 ** because of the overhead of the call itself.
	 **
	 ** @param ms the number of milliseconds to wait (1000
	 **        milliseconds = 1 second).
	 ** @exception IllegalMonitorStateException if this Thread
	 **            does not own a lock on this Object.
	 ** @exception InterruptedException if some other Thread
	 **            interrupts this Thread.
	 ** @since JDK1.0
	 **/
	public final void wait(long ms) throws IllegalMonitorStateException, InterruptedException {
		VMObject.wait(this,ms,0);
	}

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
	 ** @since JDK1.0
	 **/
	public final void wait(long ms, int ns) throws IllegalMonitorStateException, InterruptedException {
		VMObject.wait(this,ms,ns);
	}
}
