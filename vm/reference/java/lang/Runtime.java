/* Runtime.java -- access to the VM process
   Copyright (C) 1998, 2002 Free Software Foundation

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

import java.util.*;
import java.io.*;

/**
 * Runtime represents the Virtual Machine.
 *
 * @author John Keiser
 * @author Eric Blake <ebb9@email.byu.edu>
 * @status still missing 1.4 functionality
 */
public class Runtime
{
  /**
   * The one and only runtime instance.
   */
  private static final Runtime current = new Runtime();

  /**
   * The library path, to search when loading libraries.
   */
  private String[] libpath;

  /**
   * The current security manager. This is located here instead of in
   * Runtime, to avoid security problems, as well as bootstrap issues.
   */
  private static SecurityManager securityManager;

  /**
   * Not instantiable by a user, this should only create one instance.
   */
  private Runtime()
  {
    if (current != null)
      throw new InternalError("Attempt to recreate Runtime");
    String path = getLibraryPath();
    if (path == null)
      libpath = new String[0];
    else
      {
        // XXX Use StringTokenizer to make this nicer.
        int numColons = 0;
        int pathLength = path.length();
        for (int i = 0; i < pathLength; i++)
          // XXX Use path.separator property.
          if (path.charAt(i) == ':')
            numColons++;
        libpath = new String[numColons + 1];
        int current = 0;
        int libpathIndex = 0;
        while (true)
          {
            int next = path.indexOf(File.pathSeparatorChar, current);
            if (next == -1)
              {
                libpath[libpathIndex] = path.substring(current);
                break;
              }
            libpath[libpathIndex] = path.substring(current, next);
            libpathIndex++;
            current = next + 1;
          }
      }
  }

  /**
   * Get the current Runtime object for this JVM. This is necessary to access
   * the many instance methods of this class.
   *
   * @return the current Runtime object
   */
  public static Runtime getRuntime()
  {
    return current;
  }

  /**
   * Exit the Java runtime. This method will either throw a SecurityException
   * or it will never return. The status code is returned to the system; often
   * a non-zero status code indicates an abnormal exit. Of course, there is a
   * security check, <code>checkExit(status)</code>.
   *
   * <p>First, all shutdown hooks are run, in unspecified order, and possibly
   * concurrently. Next, if finalization on exit has been enabled, all pending
   * finalizers are run. Finally, the system calls <code>halt</code>.
   *
   * <p>If this is run a second time after shutdown has already started, there
   * are two actions. If shutdown hooks are still executing, it blocks
   * indefinitely. Otherwise, if the status is nonzero it halts immediately;
   * if it is zero, it blocks indefinitely. This is typically called by
   * <code>System.exit</code>.
   *
   * @param status the status to exit with
   * @throws SecurityException if permission is denied
   * @see #addShutdownHook(Thread)
   * @see #runFinalizersOnExit(boolean)
   * @see #runFinalization()
   * @see #halt(int)
   */
  public void exit(int status)
  {
    SecurityManager sm = securityManager; // Be thread-safe!
    if (sm != null)
      sm.checkExit(status);
    //XXX Check if we are already finalizing.
    //XXX Don't use exitInternal. Instead, run shutdown hooks, then call halt.
    exitInternal(status);
  }

  /**
   * Register a new shutdown hook. This is invoked when the program exits
   * normally (because all non-daemon threads ended, or because
   * <code>System.exit</code> was invoked), or when the user terminates
   * the virtual machine (such as by typing ^C, or logging off). There is
   * a security check to add hooks,
   * <code>RuntimePermission("shutdownHooks")<code>.
   *
   * <p>The hook must be an initialized, but unstarted Thread. The threads
   * are run concurrently, and started in an arbitrary order; and user
   * threads or daemons may still be running. Once shutdown hooks have
   * started, they must all complete, or else you must use <code>halt</code>,
   * to actually finish the shutdown sequence. Attempts to modify hooks
   * after shutdown has started result in IllegalStateExceptions.
   *
   * <p>It is imperative that you code shutdown hooks defensively, as you
   * do not want to deadlock, and have no idea what other hooks will be
   * running concurrently. It is also a good idea to finish quickly, as the
   * virtual machine really wants to shut down!
   *
   * <p>There are no guarantees that such hooks will run, as there are ways
   * to forcibly kill a process. But in such a drastic case, shutdown hooks
   * would do little for you in the first place.
   *
   * @param hook an initialized, unstarted Thread
   * @throws IllegalArgumentException if the hook is already registered or run
   * @throws IllegalStateException if the virtual machine is already in
   *         the shutdown sequence
   * @throws SecurityException if permission is denied
   * @since 1.3
   * @see #removeShutdownHook(Thread)
   * @see #exit(int)
   * @see #halt(int)
   * @XXX Add this method.
  public void addShutdownHook(Thread hook)
  {
    //XXX Implement me!
  }
   */

  /**
   * De-register a shutdown hook. As when you registered it, there is a
   * security check to remove hooks,
   * <code>RuntimePermission("shutdownHooks")<code>.
   *
   * @param hook the hook to remove
   * @return true if the hook was successfully removed, false if it was not
   *         registered in the first place
   * @throws IllegalStateException if the virtual machine is already in
   *         the shutdown sequence
   * @throws SecurityException if permission is denied
   * @since 1.3
   * @see #addShutdownHook(Thread)
   * @see #exit(int)
   * @see #halt(int)
   * @XXX Add this method.
  public boolean removeShutdownHook(Thread hook)
  {
    // Implement me!
    return false;
  }
   */

  /**
   * Forcibly terminate the virtual machine. This call never returns. It is
   * much more severe than <code>exit</code>, as it bypasses all shutdown
   * hooks and initializers. Use caution in calling this! Of course, there is
   * a security check, <code>checkExit(status)</code>.
   *
   * @param status the status to exit with
   * @throws SecurityException if permission is denied
   * @since 1.3
   * @see #exit(int)
   * @see #addShutdownHook(Thread)
   * XXX Add this method.
  public void halt(int status)
  {
    SecurityManager sm = securityManager; // Be thread-safe!
    if (sm != null)
      sm.checkExit(status);
    exitInternal(status);
  }
   */

  /**
   * Native method that actually shuts down the virtual machine.
   *
   * @param status the status to end the process with
   */
  native void exitInternal(int status);

  /**
   * Tell the VM to run the finalize() method on every single Object before
   * it exits.  Note that the JVM may still exit abnormally and not perform
   * this, so you still don't have a guarantee. And besides that, this is
   * inherently unsafe in multi-threaded code, as it may result in deadlock
   * as multiple threads compete to manipulate objects. This value defaults to
   * <code>false</code>. There is a security check, <code>checkExit(0)</code>.
   *
   * @param finalizeOnExit whether to finalize all Objects on exit
   * @throws SecurityException if permission is denied
   * @see #exit(int)
   * @see #gc()
   * @since 1.1
   * @deprecated never rely on finalizers to do a clean, thread-safe,
   *             mop-up from your code
   */
  public static void runFinalizersOnExit(boolean finalizeOnExit)
  {
    SecurityManager sm = securityManager; // Be thread-safe!
    if (sm != null)
      sm.checkExit(0);
    runFinalizersOnExitInternal(finalizeOnExit);
  }

  /**
   * Create a new subprocess with the specified command line. Calls
   * <code>exec(cmdline, null, null)<code>. A security check is performed,
   * <code>checkExec</code>.
   *
   * @param cmdline the command to call
   * @return the Process object
   * @throws SecurityException if permission is denied
   * @throws IOException if an I/O error occurs
   * @throws NullPointerException if cmdline is null
   * @throws IndexOutOfBoundsException if cmdline is ""
   */
  public Process exec(String cmdline) throws IOException
  {
    //XXX Use this:    return exec(cmdline, null, null);
    return exec(cmdline, null);
  }

  /**
   * Create a new subprocess with the specified command line and environment.
   * If the environment is null, the process inherits the environment of
   * this process. Calls <code>exec(cmdline, env, null)</code>. A security
   * check is performed, <code>checkExec</code>.
   *
   * @param cmdline the command to call
   * @param env the environment to use, in the format name=value
   * @return the Process object
   * @throws SecurityException if permission is denied
   * @throws IOException if an I/O error occurs
   * @throws NullPointerException if cmdline is null, or env has null entries
   * @throws IndexOutOfBoundsException if cmdline is ""
   */
  public Process exec(String cmdline, String[] env) throws IOException
  {
    //XXX Use this:    return exec(cmdline, env, null);
    StringTokenizer t = new StringTokenizer(cmdline);
    Vector v = new Vector();
    while (t.hasMoreTokens())
      v.addElement(t.nextElement());
    String[] cmd = new String[v.size()];
    v.copyInto(cmd);
    return exec(cmd, env);
  }

  /**
   * Create a new subprocess with the specified command line, environment, and
   * working directory. If the environment is null, the process inherits the
   * environment of this process. If the directory is null, the process uses
   * the current working directory. This splits cmdline into an array, using
   * the default StringTokenizer, then calls
   * <code>exec(cmdArray, env, dir)</code>. A security check is performed,
   * <code>checkExec</code>.
   *
   * @param cmdline the command to call
   * @param env the environment to use, in the format name=value
   * @param dir the working directory to use
   * @return the Process object
   * @throws SecurityException if permission is denied
   * @throws IOException if an I/O error occurs
   * @throws NullPointerException if cmdline is null, or env has null entries
   * @throws IndexOutOfBoundsException if cmdline is ""
   * @since 1.3
   * @XXX Add this method.
  public Process exec(String cmdline, String[] env, File dir)
    throws IOException
  {
    StringTokenizer t = new StringTokenizer(cmdline);
    String[] cmd = new String[t.countTokens()];
    for (int i = 0; i < cmd.length; i++)
      cmd[i] = t.nextToken();
    return exec(cmd, env, dir);
  }
   */

  /**
   * Create a new subprocess with the specified command line, already
   * tokenized. Calls <code>exec(cmd, null, null)</code>. A security check
   * is performed, <code>checkExec</code>.
   *
   * @param cmd the command to call
   * @return the Process object
   * @throws SecurityException if permission is denied
   * @throws IOException if an I/O error occurs
   * @throws NullPointerException if cmd is null, or has null entries
   * @throws IndexOutOfBoundsException if cmd is length 0
   */
  public Process exec(String[] cmd) throws IOException
  {
    //XXX Use this:    return exec(cmd, null, null);
    return exec(cmd, null);
  }

  /**
   * Create a new subprocess with the specified command line, already
   * tokenized, and specified environment. If the environment is null, the
   * process inherits the environment of this process. Calls
   * <code>exec(cmd, env, null)</code>. A security check is performed,
   * <code>checkExec</code>.
   *
   * @param cmd the command to call
   * @param env the environment to use, in the format name=value
   * @return the Process object
   * @throws SecurityException if permission is denied
   * @throws IOException if an I/O error occurs
   * @throws NullPointerException if cmd is null, or cmd or env has null
   *         entries
   * @throws IndexOutOfBoundsException if cmd is length 0
   */
  public Process exec(String[] cmd, String[] env) throws IOException
  {
    //XXX Use this:    return exec(cmd, env, null);
    SecurityManager sm = securityManager; // Be thread-safe!
    if (sm != null)
      sm.checkExec(cmd[0]);
    return execInternal(cmd, env);
  }

  /**
   * Create a new subprocess with the specified command line, already
   * tokenized, and the specified environment and working directory. If the
   * environment is null, the process inherits the environment of this
   * process. If the directory is null, the process uses the current working
   * directory. A security check is performed, <code>checkExec</code>.
   *
   * @param cmd the command to call
   * @param env the environment to use, in the format name=value
   * @param dir the working directory to use
   * @return the Process object
   * @throws SecurityException if permission is denied
   * @throws IOException if an I/O error occurs
   * @throws NullPointerException if cmd is null, or cmd or env has null
   *         entries
   * @throws IndexOutOfBoundsException if cmd is length 0
   * @since 1.3
   * @XXX Add this method.
  public Process exec(String[] cmd, String[] env, File dir)
    throws IOException
  {
    SecurityManager sm = securityManager; // Be thread-safe!
    if (sm != null)
      sm.checkExec(cmd[0]);
    if (env == null)
      env = new String[0];
    return execInternal(cmd, env, dir);
  }
   */

  /**
   * Returns the number of available processors currently available to the
   * virtual machine. This number may change over time; so a multi-processor
   * program want to poll this to determine maximal resource usage.
   *
   * @return the number of processors available, at least 1
   * @XXX Add this method
  public native int availableProcessors();
   */

  /**
   * Find out how much memory is still free for allocating Objects on the heap.
   *
   * @return the number of bytes of free memory for more Objects
   */
  public native long freeMemory();

  /**
   * Find out how much memory total is available on the heap for allocating
   * Objects.
   *
   * @return the total number of bytes of memory for Objects
   */
  public native long totalMemory();

  /**
   * Returns the maximum amount of memory the virtual machine can attempt to
   * use. This may be <code>Long.MAX_VALUE</code> if there is no inherent
   * limit (or if you really do have a 8 exabyte memory!).
   *
   * @return the maximum number of bytes the virtual machine will attempt
   *         to allocate
   * @XXX Add this method.
  public native long maxMemory();
   */

  /**
   * Run the garbage collector. This method is more of a suggestion than
   * anything. All this method guarantees is that the garbage collector will
   * have "done its best" by the time it returns. Notice that garbage
   * collection takes place even without calling this method.
   */
  public native void gc();

  /**
   * Run finalization on all Objects that are waiting to be finalized. Again,
   * a suggestion, though a stronger one than {@link #gc()}. This calls the
   * <code>finalize</code> method of all objects waiting to be collected.
   *
   * @see #finalize()
   */
  public native void runFinalization();

  /**
   * Tell the VM to trace every bytecode instruction that executes (print out
   * a trace of it).  No guarantees are made as to where it will be printed,
   * and the VM is allowed to ignore this request.
   *
   * @param on whether to turn instruction tracing on
   */
  public native void traceInstructions(boolean on);

  /**
   * Tell the VM to trace every method call that executes (print out a trace
   * of it).  No guarantees are made as to where it will be printed, and the
   * VM is allowed to ignore this request.
   *
   * @param on whether to turn method tracing on
   */
  public native void traceMethodCalls(boolean on);

  /**
   * Load a native library using the system-dependent filename. This is similar
   * to loadLibrary, except the only name mangling done is inserting "_g"
   * before the final ".so" if the VM was invoked by the name "java_g". There
   * may be a security check, of <code>checkLink</code>.
   *
   * @param filename the file to load
   * @throws SecurityException if permission is denied
   * @throws UnsatisfiedLinkError if the library is not found
   */
  public void load(String filename)
  {
    SecurityManager sm = securityManager; // Be thread-safe!
    if (sm != null)
      sm.checkLink(filename);
    if (nativeLoad(filename) == 0)
      throw new UnsatisfiedLinkError("Could not load library " + filename);
  }

  /**
   * Load a native library using a system-independent "short name" for the
   * library.  It will be transformed to a correct filename in a
   * system-dependent manner, via <code>System.mapLibraryName</code> (for
   * example, in Windows, "mylib" will be turned into "mylib.dll"), and then
   * passed to load(filename). There may be a security check, of
   * <code>checkLink</code>.
   *
   * @param filename the file to load
   * @throws SecurityException if permission is denied
   * @throws UnsatisfiedLinkError if the library is not found
   */
  public void loadLibrary(String libname)
  {
    // XXX First, check ClassLoader.findLibrary(libname)
    for (int i = 0; i < libpath.length; i++)
      {
        try
          {
            // XXX use this:
            // load(libpath[i] + System.mapLibraryName(libname));
            String filename = nativeGetLibname(libpath[i],libname);
            load(filename);
            return;
          }
        catch(UnsatisfiedLinkError e)
          {
            // Try next path element.
          }
      }
    throw new UnsatisfiedLinkError("Could not find library " + libname + ".");
  }

  /**
   * Return a localized version of this InputStream, meaning all characters
   * are localized before they come out the other end.
   *
   * @param in the stream to localize
   * @return the localized stream
   * @deprecated <code>InputStreamReader</code> is the preferred way to read
   *             local encodings
   * @XXX This implementation does not localize, yet.
   */
  public InputStream getLocalizedInputStream(InputStream in)
  {
    return in;
  }

  /**
   * Return a localized version of this OutputStream, meaning all characters
   * are localized before they are sent to the other end.
   *
   * @param out the stream to localize
   * @return the localized stream
   * @deprecated <code>OutputStreamWriter</code> is the preferred way to write
   *             local encodings
   * @XXX This implementation does not localize, yet.
   */
  public OutputStream getLocalizedOutputStream(OutputStream out)
  {
    return out;
  }

  /**
   * Native method that actually sets the finalizer setting.
   *
   * @param value whether to run finalizers on exit
   */
  private static native void runFinalizersOnExitInternal(boolean value);

  /**
   * This was moved to Runtime so that Runtime would no longer trigger
   * System's class initializer.  Runtime does native library loading, and
   * the System class initializer requires native libraries to have been
   * loaded.
   *
   * @param manager the new security manager
   * @throws SecurityException if permission is denied
   */
  static void setSecurityManager(SecurityManager manager)
  {
    // XXX Synchronize, for thread safety
    if (securityManager != null)
      // XXX Check RuntimePermission("setSecurityManager")
      throw new SecurityException("Security Manager already set");
    securityManager = manager;
  }

  /**
   * Get the security manager. This is here instead of system because of
   * bootstrap issues.
   *
   * @return the security manager
   */
  static SecurityManager getSecurityManager()
  {
    return securityManager;
  }

  /**
   * Load a file. If it has already been loaded, do nothing. The name has
   * already been mapped to a true filename.
   *
   * @param filename the file to load
   * @return 0 on success, nonzero on failure
   */
  native int nativeLoad(String filename);

  /**
   * Map a system-independent "short name" to the full file name, and append
   * it to the path.
   * XXX This method is being replaced by System.mapLibraryName.
   *
   * @param pathname the path
   * @param libname the short version of the library name
   * @return the full filename
   */
  native String nativeGetLibname(String pathname, String libname);

  /**
   * Execute a process. The command line has already been tokenized, and
   * the environment should contain name=value mappings. If directory is null,
   * use the current working directory; otherwise start the process in that
   * directory.
   * XXX Add directory support.
   *
   * @param cmd the non-null command tokens
   * @param env the non-null environment setup
   * @param dir the directory to use, may be null
   * @return the newly created process
   */
  //  native Process execInternal(String[] cmd, String[] env, File dir);
  native Process execInternal(String[] cmd, String[] env);

  /**
   * Get the library path.
   *
   * @return the library path
   * XXX Use the java.library.path property
   */
  static native String getLibraryPath();
}
