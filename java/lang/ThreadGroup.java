/* java.lang.ThreadGroup
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

import java.util.Vector;

/**
 ** ThreadGroup allows you to group Threads together.  There is a
 ** hierarchy of ThreadGroups, and only the initial ThreadGroup has
 ** no parent.  A Thread may access information about its own
 ** ThreadGroup, but not its parents or others outside the tree.
 **
 ** @specnote it is not clear whether a Thread may access Threads
 **           in ThreadGroups beneath it in the tree, but I
 **           presume it to be so.
 ** @specnote it is also very unclear when, if ever, a Thread should
 **           be removed from the ThreadGroup.  I assume it is when
 **           the Thread is stopped or destroyed.
 ** @specnote Finally, it is unclear whether a ThreadGroup inherits
 **           the daemon status of its parent.  I assume it to be so.
 ** @author John Keiser
 ** @version 1.1.0, Oct 5 1998
 ** @since JDK1.0
 **/

public class ThreadGroup {
	static ThreadGroup root = new ThreadGroup();

	ThreadGroup parent;
	String name;
	Vector children = new Vector();
	Vector threads = new Vector();
	int maxPriority;
	boolean daemon;
	boolean suspendOnLowMem;

	private ThreadGroup() {
		name = "Root Thread Group";
	}

	/** Create a new ThreadGroup using the given name and the
	 ** current thread's ThreadGroup as a parent.
	 ** @param name the name to use for the ThreadGroup.
	 **/
	public ThreadGroup(String name) {
		parent = Thread.currentThread().getThreadGroup();
		this.name = name;
	}

	/** Create a new ThreadGroup using the given name and
	 ** parent group.
	 ** @param name the name to use for the ThreadGroup.
	 ** @param parent the ThreadGroup to use as a parent.
	 ** @exception NullPointerException if parent is null.
	 ** @exception SecurityException if you cannot change
	 **            the intended parent group.
	 **/
	public ThreadGroup(ThreadGroup parent, String name) {
		this.parent = parent;
		parent.checkAccess();
		parent.addChild(this);
	}

	/** Get the name of this ThreadGroup.
	 ** @return the name of this ThreadGroup.
	 **/
	public final String getName() {
		return name;
	}

	/** Get the parent of this ThreadGroup.
	 ** @return the parent of this ThreadGroup.
	 **/
	public final ThreadGroup getParent() {
		return parent;
	}

	/** Set the maximum priority for Threads in this ThreadGroup.
	 ** @param maxPriority the new maximum priority for this ThreadGroup.
	 ** @exception SecurityException if you cannoy modify this ThreadGroup.
	 **/
	public final void setMaxPriority(int maxPriority) {
		checkAccess();
		this.maxPriority = maxPriority;
	}

	/** Get the maximum priority of Threads in this ThreadGroup.
	 ** @return the maximum priority of Threads in this ThreadGroup.
	 **/
	public final int getMaxPriority() {
		return maxPriority;
	}

	/** Set whether this ThreadGroup is a daemon group.  A daemon
	 ** group will be destroyed when its last thread is stopped and
	 ** its last thread group is destroyed.
	 ** @specnote The Java API docs indicate that the group is destroyed
	 **           when either of those happen, but that doesn't make
	 **           sense.
	 ** @param daemon whether this ThreadGroup should be a daemon group.
	 ** @exception SecurityException if you cannoy modify this ThreadGroup.
	 **/
	public final void setDaemon(boolean daemon) {
		checkAccess();
		this.daemon = daemon;
	}

	/** Tell whether this ThreadGroup is a daemon group.  A daemon
	 ** group will be destroyed when its last thread is stopped and
	 ** its last thread group is destroyed.
	 ** @specnote The Java API docs indicate that the group is destroyed
	 **           when either of those happen, but that doesn't make
	 **           sense.
	 ** @return whether this ThreadGroup is a daemon group.
	 **/
	public final boolean isDaemon() {
		return daemon;
	}

	/** Tell whether this ThreadGroup has been destroyed or not.
	 ** @return whether this ThreadGroup has been destroyed or not.
	 **/
	public boolean isDestroyed() {
		return this != root && parent == null;
	}

	/** Check whether this ThreadGroup is an ancestor of the
	 ** specified ThreadGroup, or if they are the same.
	 **
	 ** @param g the group to test on.
	 ** @return whether this ThreadGroup is a parent of the
	 **         specified group.
	 **/
	public final boolean parentOf(ThreadGroup g) {
		while(g != null) {
			if(g == this)
				return true;
			g = g.parent;
		}
		return false;
	}

	/** Return the total number of active threads in this ThreadGroup
	 ** and all its descendants.<P>
	 **
	 ** This cannot return an exact number, since the status of threads
	 ** may change after they were counted.  But it should be pretty
	 ** close.<P>
	 **
	 ** @return the number of active threads in this ThreadGroup and
	 **         its descendants.
	 ** @XXX I assume that isAlive() is not sufficient to test
	 **      whether a thread is active ... but then what is?
	 **/
	public synchronized int activeCount() {
		int total = 0;
		for(int i=0;i<threads.size();i++) {
			if(((Thread)threads.elementAt(i)).isAlive())
				total++;
		}
		for(int i=0;i<children.size();i++) {
			total+=((Thread)children.elementAt(i)).activeCount();
		}
		return total;
	}

	/** Copy all of the active Threads from this ThreadGroup and
	 ** its descendants into the specified array.  If the array is
	 ** not big enough to hold all the Threads, extra Threads will
	 ** simply not be copied.
	 **
	 ** @param threads the array to put the threads into.
	 ** @return the number of threads put into the array.
	 **/
	public int enumerate(Thread[] list) {
		return enumerate(list,true);
	}

	/** Copy all of the active Threads from this ThreadGroup and,
	 ** if desired, from its descendants, into the specified array.
	 ** If the array is not big enough to hold all the Threads,
	 ** extra Threads will simply not be copied.
	 **
	 ** @param threads the array to put the threads into.
	 ** @param useDescendants whether to count Threads in this
	 **        ThreadGroup's descendants or not.
	 ** @return the number of threads put into the array.
	 **/
	public int enumerate(Thread[] list, boolean useDescendants) {
		return enumerate(list,0,useDescendants);
	}

	/** Get the number of active groups in this ThreadGroup.  If
	 ** this group is active, it is still not included in the count.
	 ** @specnote it is terribly unclear what exactly constitutes
	 **           an active ThreadGroup.  I am taking it to mean
	 **           that a ThreadGroup is active if any of the
	 **           Threads directly in it are active.
	 ** @specnote it is also unclear whether the current group is
	 **           to be included in the count.  I assume not from
	 **           looking at the docs.
	 ** @return the number of active groups in this ThreadGroup.
	 **/
	public synchronized int activeGroupCount() {
		int total = 0;
		for(int i=0;i<children.size();i++) {
			ThreadGroup g = ((ThreadGroup)children.elementAt(i));
			synchronized(g) {
				for(int t=0;t<g.threads.size();t++) {
					if(((Thread)g.threads.elementAt(t)).isAlive()) {
						total++;
					}
				}
			}
			total += g.activeGroupCount();
		}
		return total;
	}

	/** Copy all active ThreadGroups that are descendants of this
	 ** ThreadGroup into the specified array.  If the array is not
	 ** large enough to hold all active ThreadGroups, extra
	 ** ThreadGroups simply will not be copied.
	 **
	 ** @param groups the array to put the ThreadGroups into.
	 ** @return the number of ThreadGroups copied into the array.
	 **/
	public int enumerate(ThreadGroup[] groups) {
		return enumerate(groups,false);
	}

	/** Copy all active ThreadGroups that are children of this
	 ** ThreadGroup into the specified array, and if desired, also
	 ** copy all active descendants into the array.  If the array
	 ** is not large enough to hold all active ThreadGroups, extra
	 ** ThreadGroups simply will not be copied.
	 **
	 ** @param groups the array to put the ThreadGroups into.
	 ** @param useDescendants whether to include all descendants
	 **        of this ThreadGroup's children in determining
	 **        activeness.
	 ** @return the number of ThreadGroups copied into the array.
	 **/
	public int enumerate(ThreadGroup[] groups, boolean useDescendants) {
		return enumerate(groups,0,useDescendants);
	}

	/** Stop all Threads in this ThreadGroup and its descendants.
	 ** @exception SecurityException if you cannot modify this
	 **            ThreadGroup or any of its Threads or children
	 **            ThreadGroups.
	 **/
	public final void stop() {
		checkAccess();
		for(int i=0;i<threads.size();i++) {
			((Thread)threads.elementAt(i)).stop();
		}
		for(int i=0;i<children.size();i++) {
			((ThreadGroup)children.elementAt(i)).stop();
		}
	}

	/** Suspend all Threads in this ThreadGroup and its descendants.
	 ** @exception SecurityException if you cannot modify this
	 **            ThreadGroup or any of its Threads or children
	 **            ThreadGroups.
	 **/
	public final void suspend() {
		checkAccess();
		for(int i=0;i<threads.size();i++) {
			((Thread)threads.elementAt(i)).suspend();
		}
		for(int i=0;i<children.size();i++) {
			((ThreadGroup)children.elementAt(i)).suspend();
		}
	}

	/** Resume all Threads in this ThreadGroup and its descendants.
	 ** @exception SecurityException if you cannot modify this
	 **            ThreadGroup or any of its Threads or children
	 **            ThreadGroups.
	 **/
	public final void resume() {
		checkAccess();
		for(int i=0;i<threads.size();i++) {
			((Thread)threads.elementAt(i)).resume();
		}
		for(int i=0;i<children.size();i++) {
			((ThreadGroup)children.elementAt(i)).resume();
		}
	}

	/** Destroy this ThreadGroup.  There can be no Threads in it,
	 ** and none of its descendants may have Threads in them.  All its
	 ** descendants will be destroyed as well.
	 ** @exception IllegalThreadStateException if the ThreadGroup or
	 **            its descendants have Threads remaining in them, or
	 **            if the ThreadGroup in question is already destroyed.
	 ** @exception SecurityException if you cannot modify this
	 **            ThreadGroup or any of its descendants.
	 ** @impnote If any of the descendants cannot be destroyed, some
	 **          of the ThreadGroups may be destroyed and some may not.
	 ** @specnote It is unclear what the hell destruction is supposed
	 **           to do to a ThreadGroup.  As far as I can tell, you
	 **           cannot even stop new Threads and ThreadGroups from
	 **           being added to it.
	 ** @specnote what happens if you try to destroy the root
	 **           ThreadGroup?  I assume it throws the same exception
	 **           that is thrown if you try to destroy a ThreadGroup
	 **           that is already destroyed.
	 **/
	public final void destroy() {
		if(threads.size() != 0) {
			throw new IllegalThreadStateException("Tried to destroy a ThreadGroup with threads in it.");
		}
		for(int i=0;i<children.size();i++) {
			((ThreadGroup)children.elementAt(i)).destroy();
		}
		if(parent == null) {
			throw new IllegalThreadStateException("Tried to destroy a ThreadGroup that was already destroyed.");
		}
		parent.removeChild(this);
		parent = null;
	}

	/** Print out information about this ThreadGroup to System.out.
	 ** @XXX What the hell?  There is no specification whatsoever
	 **      as to what information, let alone its format.
	 **/
	public void list() {
	}

	/** When a Thread in this ThreadGroup does not catch an exception,
	 ** this method of the ThreadGroup is called.<P>
	 **
	 ** ThreadGroup's implementation does the following:<BR>
	 ** <OL>
	 ** <LI>If there is a parent ThreadGroup, call uncaughtException()
	 **     in the parent.</LI>
	 ** <LI>If the Throwable passed in is ThreadDeath, don't do
	 **     anything.</LI>
	 ** <LI>Otherwise, call <CODE>exception.printStackTrace().</CODE></LI>
	 ** </OL>
	 **
	 ** @param thread the thread that exited.
	 ** @param exception the uncaught exception.
	 **/
	public void uncaughtException(Thread thread, Throwable exception) {
		if(parent != null) {
			parent.uncaughtException(thread,exception);
		} else {
			if(!(exception instanceof ThreadDeath)) {
				exception.printStackTrace();
			}
		}
	}

	/** Tell the VM whether it may suspend Threads in low memory
	 ** situations.  I am making it default to false, though
	 ** the spec is unclear on that point.
	 ** @param allow whether to allow suspension of Threads in
	 **        low memory situations.
	 ** @return the previous value of allowThreadSuspension.
	 ** @specnote the default for this value is unclear.
	 ** @specnote it is unclear what this method returns.  I
	 **           assume it's the previous value of
	 **           allowThreadSuspension.
	 **/
	public boolean allowThreadSuspension(boolean allow) {
		boolean retval = suspendOnLowMem;
		suspendOnLowMem = allow;
		return retval;
	}

	/** Get a human-readable representation of this ThreadGroup.
	 ** @return a String representing this ThreadGroup.
	 ** @XXX find out what the format of this is.
	 **/
	public String toString() {
		return "";
	}


	/** Find out if the current Thread can modify this ThreadGroup.
	 ** Calls the current SecurityManager's checkAccess() method to
	 ** find out.  If there is none, it assumes everything's OK.
	 ** @exception SecurityException if the current Thread cannot
	 **            modify this ThreadGroup.
	 **/
	public final void checkAccess() {
		SecurityManager sm = System.getSecurityManager();
		if(sm != null) {
			sm.checkAccess(this);
		}
	}

	/* @specnote it is unclear what one should do when trying to
	 *           add a Thread to a ThreadGroup when its priority
	 *           is too high.  I am taking the tack of lowering
	 *           its priority.
	 * @specnote it is also unclear as to the behavior when one
	 *           tries to add a thread or a child threadgroup to
	 *           a destroyed thread group.  Unfortunately, I believe
	 *           we must comply; there is no appropriate exception to
	 *           throw otherwise.
	 */
	synchronized void addThread(Thread t) {
		if(t.getPriority() > getMaxPriority()) {
			t.setPriority(getMaxPriority());
		}
		threads.addElement(t);
	}

	synchronized void removeThread(Thread t) {
		threads.removeElement(t);
		if(daemon == true && children.size() == 0 && threads.size() == 0) {
			destroy();
		}
	}

	synchronized void addChild(ThreadGroup g) {
		children.addElement(g);
	}

	synchronized void removeChild(ThreadGroup g) {
		children.removeElement(g);
		if(daemon == true && children.size() == 0 && threads.size() == 0) {
			destroy();
		}
	}

	synchronized int enumerate(Thread[] threads, int startPos, boolean useDescendants) {
		int total = 0;
		for(int i=0;i<this.threads.size() && startPos+i<threads.length;i++) {
			if(((Thread)this.threads.elementAt(i)).isAlive()) {
				threads[startPos+total] = (Thread)this.threads.elementAt(i);
				total++;
			}
		}
		if(useDescendants) {
			for(int i=0;
			    i<children.size() && startPos+total<threads.length;
			    i++) {
				total+=((ThreadGroup)children.elementAt(i)).enumerate(threads,startPos+total,true);
			}
		}
		return total;
	}

	synchronized int enumerate(ThreadGroup[] groups, int startPos, boolean useDescendants) {
		int total = 0;
		for(int i=0;i<children.size() && startPos+i<groups.length;i++) {
			ThreadGroup g = (ThreadGroup)children.elementAt(i);
			boolean useThisOne = false;
			for(int t=0; t<g.threads.size(); t++) {
				if(((Thread)this.threads.elementAt(t)).isAlive()) {
					groups[startPos+total] = g;
					total++;
				}
			}
		}
		if(useDescendants) {
			for(int i=0;i<children.size() && startPos+i<groups.length;i++) {
				total += ((ThreadGroup)children.elementAt(i)).enumerate(groups,startPos+total,useDescendants);
			}
		}
		return total;
	}
}
