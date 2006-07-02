/* ThreadInfo.java - Information on a thread
   Copyright (C) 2006 Free Software Foundation

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
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

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

package java.lang.management;

/**
 * <p>
 * A class which maintains information about a particular
 * thread.  This information includes:
 * </p>
 * <ul>
 * <li><strong>General Thread Information:</strong>
 * <ul>
 * <li>The identifier of the thread.</li>
 * <li>The name of the thread.</li>
 * </ul>
 * </li>
 * <li><strong>Execution Information:</strong>
 * <ul>
 * <li>The current state of the thread (e.g. blocked, runnable)</li>
 * <li>The object upon which the thread is blocked, either because
 * the thread is waiting to obtain the monitor of that object to enter
 * one of its synchronized monitor, or because
 * {@link java.lang.Object#wait()} has been called while the thread
 * was within a method of that object.</li>
 * <li>The thread identifier of the current thread holding an object's
 * monitor, upon which the thread described here is blocked.</li>
 * <li>The stack trace of the thread (if requested on creation
 * of this object</li>
 * </ul>
 * <li><strong>Synchronization Statistics</strong>
 * <ul>
 * <li>The number of times the thread has been blocked waiting for
 * an object's monitor or in a {@link java.lang.Object#wait()} call.</li>
 * <li>The accumulated time the thread has been blocked waiting for
 * an object's monitor on in a {@link java.lang.Object#wait()} call.
 * The availability of these statistics depends on the virtual machine's
 * support for thread contention monitoring (see
 * {@link ThreadMXBean#isThreadContentionMonitoringSupported()}.</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 * @since 1.5
 * @see ThreadMXBean#isThreadContentionMonitoringSupported()
 */
public class ThreadInfo
{

  /**
   * The thread which this instance concerns.
   */
  private Thread thread;

  /**
   * The number of times the thread has been blocked.
   */
  private long blockedCount;

  /**
   * The accumulated number of milliseconds the thread has
   * been blocked (used only with thread contention monitoring
   * support).
   */
  private long blockedTime;

  /**
   * The monitor lock on which this thread is blocked
   * (if any).
   */
  private Object lock;

  /**
   * The thread which owns the monitor lock on which this
   * thread is blocked, or <code>null</code> if there is
   * no owner.
   */
  private Thread lockOwner;

  /**
   * The number of times the thread has been in a waiting
   * state.
   */
  private long waitedCount;

  /**
   * The accumulated number of milliseconds the thread has
   * been waiting (used only with thread contention monitoring
   * support).
   */
  private long waitedTime;

  /**
   * True if the thread is in a native method.
   */
  private boolean isInNative;

  /**
   * True if the thread is suspended.
   */
  private boolean isSuspended;

  /**
   * The stack trace of the thread.
   */
  private StackTraceElement[] trace;

  /**
   * Cache a local reference to the thread management bean.
   */
  private static ThreadMXBean bean = null;

  /**
   * Constructs a new {@link ThreadInfo} corresponding
   * to the thread specified.
   *
   * @param thread the thread on which the new instance
   *               will be based.
   * @param blockedCount the number of times the thread
   *                     has been blocked.
   * @param blockedTime the accumulated number of milliseconds
   *                    the specified thread has been blocked
   *                    (only used with contention monitoring enabled)
   * @param lock the monitor lock the thread is waiting for
   *             (only used if blocked)
   * @param lockOwner the thread which owns the monitor lock, or
   *                  <code>null</code> if it doesn't have an owner
   *                  (only used if blocked)
   * @param waitedCount the number of times the thread has been in a
   *                    waiting state.
   * @param waitedTime the accumulated number of milliseconds the
   *                   specified thread has been waiting
   *                   (only used with contention monitoring enabled)
   * @param isInNative true if the thread is in a native method.
   * @param isSuspended true if the thread is suspended.
   * @param trace the stack trace of the thread to a pre-determined
   *              depth (see VMThreadMXBeanImpl)
   */
  private ThreadInfo(Thread thread, long blockedCount, long blockedTime,
		     Object lock, Thread lockOwner, long waitedCount,
		     long waitedTime, boolean isInNative, boolean isSuspended,
		     StackTraceElement[] trace)
  {
    this.thread = thread;
    this.blockedCount = blockedCount;
    this.blockedTime = blockedTime;
    this.lock = lock;
    this.lockOwner = lockOwner;
    this.waitedCount = waitedCount;
    this.waitedTime = waitedTime;
    this.isInNative = isInNative;
    this.isSuspended = isSuspended;
    this.trace = trace;
  }

  /**
   * Returns the number of times this thread has been
   * in the {@link java.lang.Thread.State#BLOCKED} state.
   * A thread enters this state when it is waiting to
   * obtain an object's monitor.  This may occur either
   * on entering a synchronized method for the first time,
   * or on re-entering it following a call to
   * {@link java.lang.Object#wait()}.
   *
   * @return the number of times this thread has been blocked.
   */
  public long getBlockedCount()
  {
    return blockedCount;
  }

  /**
   * <p>
   * Returns the accumulated number of milliseconds this
   * thread has been in the
   * {@link java.lang.Thread.State#BLOCKED} state
   * since thread contention monitoring was last enabled.
   * A thread enters this state when it is waiting to
   * obtain an object's monitor.  This may occur either
   * on entering a synchronized method for the first time,
   * or on re-entering it following a call to
   * {@link java.lang.Object#wait()}.
   * </p>
   * <p>
   * Use of this method requires virtual machine support
   * for thread contention monitoring and for this support
   * to be enabled.
   * </p>
   * 
   * @return the accumulated time (in milliseconds) that this
   *         thread has spent in the blocked state, since
   *         thread contention monitoring was enabled, or -1
   *         if thread contention monitoring is disabled.
   * @throws UnsupportedOperationException if the virtual
   *                                       machine does not
   *                                       support contention
   *                                       monitoring.
   * @see ThreadMXBean#isThreadContentionMonitoringEnabled()
   * @see ThreadMXBean#isThreadContentionMonitoringSupported()
   */
  public long getBlockedTime()
  {
    if (bean == null)
      bean = ManagementFactory.getThreadMXBean();
    // Will throw UnsupportedOperationException for us
    if (bean.isThreadContentionMonitoringEnabled())
      return blockedTime;
    else
      return -1;
  }

  /**
   * <p>
   * Returns a {@link java.lang.String} representation of
   * the monitor lock on which this thread is blocked.  If
   * the thread is not blocked, this method returns
   * <code>null</code>.
   * </p>
   * <p>
   * The returned {@link java.lang.String} is constructed
   * using the class name and identity hashcode (usually
   * the memory address of the object) of the lock.  The
   * two are separated by the '@' character, and the identity
   * hashcode is represented in hexadecimal.  Thus, for a
   * lock, <code>l</code>, the returned value is
   * the result of concatenating
   * <code>l.getClass().getName()</code>, <code>"@"</code>
   * and
   * <code>Integer.toHexString(System.identityHashCode(l))</code>.
   * The value is only unique to the extent that the identity
   * hash code is also unique.
   * </p>
   *
   * @return a string representing the lock on which this
   *         thread is blocked, or <code>null</code> if
   *         the thread is not blocked.
   */
  public String getLockName()
  {
    if (thread.getState().equals("BLOCKED"))
      return null;
    return lock.getClass().getName() + "@" +
      Integer.toHexString(System.identityHashCode(lock));
  }

  /**
   * Returns the identifier of the thread which owns the
   * monitor lock this thread is waiting for.  -1 is returned
   * if either this thread is not blocked, or the lock is
   * not held by any other thread.
   * 
   * @return the thread identifier of thread holding the lock
   *         this thread is waiting for, or -1 if the thread
   *         is not blocked or the lock is not held by another
   *         thread.
   */
  public long getLockOwnerId()
  {
    if (thread.getState().equals("BLOCKED"))
      return -1;
    if (lockOwner == null)
      return -1;
    return lockOwner.getId();
  }

  /**
   * Returns the name of the thread which owns the
   * monitor lock this thread is waiting for.  <code>null</code>
   * is returned if either this thread is not blocked,
   * or the lock is not held by any other thread.
   * 
   * @return the thread identifier of thread holding the lock
   *         this thread is waiting for, or <code>null</code>
   *         if the thread is not blocked or the lock is not
   *         held by another thread.
   */
  public String getLockOwnerName()
  {
    if (thread.getState().equals("BLOCKED"))
      return null;
    if (lockOwner == null)
      return null;
    return lockOwner.getName();
  }

  /**
   * <p>
   * Returns the stack trace of this thread to the depth
   * specified on creation of this {@link ThreadInfo}
   * object.  If the depth is zero, an empty array will
   * be returned.  For non-zero arrays, the elements
   * start with the most recent trace at position zero.
   * The bottom of the stack represents the oldest method
   * invocation which meets the depth requirements.
   * </p>
   * <p>
   * Some virtual machines may not be able to return
   * stack trace information for a thread.  In these
   * cases, an empty array will also be returned.
   * </p>
   * 
   * @return an array of {@link java.lang.StackTraceElement}s
   *         representing the trace of this thread.
   */
  public StackTraceElement[] getStackTrace()
  {
    return trace;
  }

  /**
   * Returns the identifier of the thread associated with
   * this instance of {@link ThreadInfo}.
   *
   * @return the thread's identifier.
   */
  public long getThreadId()
  {
    return thread.getId();
  }

  /**
   * Returns the name of the thread associated with
   * this instance of {@link ThreadInfo}.
   *
   * @return the thread's name.
   */
  public String getThreadName()
  {
    return thread.getName();
  }

  /**
   * Returns the state of the thread associated with
   * this instance of {@link ThreadInfo}.
   *
   * @return the thread's state.
   */
  public String getThreadState()
  {
    return thread.getState();
  }
    
  /**
   * Returns the number of times this thread has been
   * in the {@link java.lang.Thread.State#WAITING} 
   * or {@link java.lang.Thread.State#TIMED_WAITING} state.
   * A thread enters one of these states when it is waiting
   * due to a call to {@link java.lang.Object.wait()},
   * {@link java.lang.Object.join()} or
   * {@link java.lang.concurrent.locks.LockSupport.park()},
   * either with an infinite or timed delay, respectively. 
   *
   * @return the number of times this thread has been waiting.
   */
  public long getWaitedCount()
  {
    return waitedCount;
  }

  /**
   * <p>
   * Returns the accumulated number of milliseconds this
   * thread has been in the
   * {@link java.lang.Thread.State#WAITING} or
   * {@link java.lang.Thread.State#TIMED_WAITING} state,
   * since thread contention monitoring was last enabled.
   * A thread enters one of these states when it is waiting
   * due to a call to {@link java.lang.Object.wait()},
   * {@link java.lang.Object.join()} or
   * {@link java.lang.concurrent.locks.LockSupport.park()},
   * either with an infinite or timed delay, respectively. 
   * </p>
   * <p>
   * Use of this method requires virtual machine support
   * for thread contention monitoring and for this support
   * to be enabled.
   * </p>
   * 
   * @return the accumulated time (in milliseconds) that this
   *         thread has spent in one of the waiting states, since
   *         thread contention monitoring was enabled, or -1
   *         if thread contention monitoring is disabled.
   * @throws UnsupportedOperationException if the virtual
   *                                       machine does not
   *                                       support contention
   *                                       monitoring.
   * @see ThreadMXBean#isThreadContentionMonitoringEnabled()
   * @see ThreadMXBean#isThreadContentionMonitoringSupported()
   */
  public long getWaitedTime()
  {
    if (bean == null)
      bean = ManagementFactory.getThreadMXBean();
    // Will throw UnsupportedOperationException for us
    if (bean.isThreadContentionMonitoringEnabled())
      return waitedTime;
    else
      return -1;
  }

  /**
   * Returns true if the thread is in a native method.  This
   * excludes native code which forms part of the virtual
   * machine itself, or which results from Just-In-Time
   * compilation.
   *
   * @return true if the thread is in a native method, false
   *         otherwise.
   */
  public boolean isInNative()
  {
    return isInNative;
  }

  /**
   * Returns true if the thread has been suspended using
   * {@link java.lang.Thread#suspend()}.
   *
   * @return true if the thread is suspended, false otherwise.
   */
  public boolean isSuspended()
  {
    return isSuspended;
  }

  /**
   * Returns a {@link java.lang.String} representation of
   * this {@link ThreadInfo} object.  This takes the form
   * <code>java.lang.management.ThreadInfo[id=tid, name=n,
   * state=s, blockedCount=bc, waitedCount=wc, isInNative=iin,
   * isSuspended=is]</code>, where <code>tid</code> is
   * the thread identifier, <code>n</code> is the
   * thread name, <code>s</code> is the thread state,
   * <code>bc</code> is the blocked state count,
   * <code>wc</code> is the waiting state count and
   * <code>iin</code> and <code>is</code> are boolean
   * flags to indicate the thread is in native code or
   * suspended respectively.  If the thread is blocked,
   * <code>lock=l, lockOwner=lo</code> is also included,
   * where <code>l</code> is the lock waited for, and
   * <code>lo</code> is the thread which owns the lock
   * (or null if there is no owner).
   *
   * @return the string specified above.
   */
  public String toString()
  {
    String state = thread.getState();
    return getClass().getName() +
      "[id=" + thread.getId() + 
      ", name=" + thread.getName() +
      ", state=" + state +
      ", blockedCount=" + blockedCount +
      ", waitedCount=" + waitedCount +
      ", isInNative=" + isInNative + 
      ", isSuspended=" + isSuspended +
      (state.equals("BLOCKED") ? ", lock=" + lock +
       ", lockOwner=" + lockOwner : "") +
      "]";
  }

}
