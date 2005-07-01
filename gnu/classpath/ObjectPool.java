/* ObjectPool.java -- A generic object pool.
   Copyright (C) 2005  Free Software Foundation, Inc.

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

package gnu.classpath;

import java.util.HashMap;
import java.util.Stack;

/**
 * A generic object pool that can be used to cache frequently used
 * objects. Typical examples are 'throwaway' objects like java.awt.Dimension
 * java.awt.Rectangle and java.awt.Point of which are created plenty in
 * Swing but that usually have a very short lifecycle. For such objects
 * it is much more efficient to hold them in an object pool, because the
 * overhead of allocating heap and garbage collecting is avoided.
 * <p>
 * To use this pool you have to do 3 steps:
 * <ol>
 * <li>Get a reference to the (singleton) ObjectPool instance.</li>
 * <li>Get a reference to an object of the correct type; Do something with it
 * </li>
 * <li>After use, return the object back to the pool.</li>
 * </ol>
 * Note that you should never make any assumptions about the state of such
 * an object, you are responsible to take care of this before you use the
 * object.
 * </p><p>
 * If a requested object is not available in the pool, then a new instance
 * is created. It is therefore only possible to pool objects that have
 * a zero-argument default constructor.
 * </p><p>
 * Example (using a java.awt.Point object):
 * <pre>
 * ObjectPool pool = ObjectPool.getInstance();
 * Point point = (Point) pool.borrowObject(Point.class);
 * doSomething(point);
 * pool.returnObject(point);
 * </pre>
 *
 *
 * @author Roman Kennke (roman@kennke.org)
 */
public final class ObjectPool
{

  /** The maximum number of instances that we keep for each type. */
  private static final int MAX_POOL_SIZE = 128;

  /** This flag turns on/off caching (for benchmarking purposes). */
  private static final boolean IS_CACHING = true;

  /** The only instance of ObjectPool. */
  private static ObjectPool instance;

  /**
   * The object pool. This maps Class objects (the type of the pooled objects)
   * to Collections that contain the pooled instances. If there is no such
   * mapping or the mapped COllection is empty, then no instance of the
   * requested type is in the pool.
   */
  private HashMap pool;
  
  /**
   * Collect some stats in this fields. TODO: Can be removed later.
   */
  int created = 0;
  int requested = 0;
  int returned = 0;
  int pooled = 0;

  /**
   * Creates a new instance of ObjectPool. This constructor is made private
   * because it is an Singleton class. Use {@link #getInstance()} to
   * get a reference to an ObjectPool.
   */
  private ObjectPool() {
    pool = new HashMap();
  }

  /**
   * Returns the ObjectPool that is used in this VM.
   *
   * @return an ObjectPool instance ready for use
   */
  public static synchronized ObjectPool getInstance()
  {
    if (instance == null)
      instance = new ObjectPool();
    return instance;
  }

  /**
   * Return an instance of the specified type. If no such instance is available
   * in the pool, then one is created using the zero argument default
   * constructor.
   *
   * @param type the type of the requested object
   *
   * @return an object of the specified type, or <code>null</code> if there
   *     is no such object in the pool and an object of this type cannot
   *     be instantiated, like when the class does not provide a default
   *     constructor or it is not accessible
   */
  public Object borrowObject(Class type)
  {
    // This is only here for benchmarking purposes.
    if (!IS_CACHING)
      return createObject(type);
    // Counts the requested objects. This is only here for benchmarking
    // purposes.
    requested++;
    if (requested % 10000 == 0)
      printStats();


    Object object = null;
    Stack pooledInstances = null;
    synchronized (this)
      {
	pooledInstances = (Stack) pool.get(type);
      }
    if (pooledInstances == null)
      object = createObject(type);
    else
      if (pooledInstances.size() == 0)
	object = createObject(type);
      else
	synchronized (this)
	  {
	    object = pooledInstances.pop();
	  }
    return object;
  }

  /**
   * Returns an instance back into the pool.
   *
   * @param object the object that is returned into the pool
   */
  public void returnObject(Object object)
  {
    // This is only here for benchmarking purposes.
    if (!IS_CACHING)
      return;
    // Count the returned objects. This is only here for benchmarking purposes.
    returned++;

    Class type = object.getClass();
    Stack pooledInstances = null;
    synchronized (this)
      {
	pooledInstances = (Stack) pool.get(type);
      }
    if (pooledInstances == null)
      {
	pooledInstances = new Stack();
      }
    if (pooledInstances.size() < MAX_POOL_SIZE)
      synchronized (this)
	{
	  pool.put(type, pooledInstances);

	  // Count the objects that are actually pooled. This is only
	  // here for benchmarking purposes.
	  pooled++;
	}

    synchronized (this)
      {
	pooledInstances.push(object);
      }
  }

  /**
   * Creates a new instance of the specified type.
   *
   * @param type the type of the requested object
   *
   * @return an instance of that type or <code>null</code> if the object
   *     cannot be instantiated for some reason
   */
  private Object createObject(Class type)
  {
    // Counts the objects that are created here. This is only here for
    // benchmarking purposes.
    created++;


    Object object = null;
    try
      {
	object = type.newInstance();
      }
    catch (InstantiationException ex)
      {
	// We return null if the object cannot be instantiated.
      }
    catch (IllegalAccessException ex)
      {
	// We return null if the object cannot be instantiated.
      }
    return object;
  }

  /**
   * This method prints out some stats about the object pool. This gives
   * an indication on how efficiently the pool is used.
   */
  void printStats()
  {
    System.err.println("Requested Objects: " + requested);
    System.err.println("Returned Objects: " + returned);
    System.err.println("Created Objects: " + created);
    System.err.println("Pooled Objects: " + pooled);
  }
}
