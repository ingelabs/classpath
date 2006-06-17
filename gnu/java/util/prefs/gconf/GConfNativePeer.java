/* GConfNativePeer.java -- GConf based preference peer for native methods
 Copyright (C) 2006 Free Software Foundation, Inc.

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


package gnu.java.util.prefs.gconf;

import java.util.List;
import java.util.prefs.BackingStoreException;

/**
 * Native peer for GConf based preference backend.
 * 
 * @author Mario Torre <neugens@limasoftware.net>
 * @version 1.0
 */
public final class GConfNativePeer
{
  /**
   * Object to achieve locks for methods that need to be synchronized.
   */
  private static final Object[] semaphore = new Object[0];

  /**
   * Creates a new instance of GConfNativePeer
   */
  public GConfNativePeer()
  {
    synchronized (semaphore)
      {
        init_class();
      }
  }

  /**
   * Queries whether the node <code>node</code> exists in theGConf database.
   * Returns <code>true</code> or <code>false</code>.
   * 
   * @param node the node to check.
   */
  public boolean nodeExist(String node)
  {
    if (node.endsWith("/"))
      {
        node = node.substring(0, node.length() - 1);
      }
    return gconf_client_dir_exists(node);
  }

  /**
   * Add the node <code>node</code> to the list of nodes the GConf will watch.
   * An event is raised everytime this node is changed. You can add a node
   * multiple times.
   * 
   * @param node the node to track.
   */
  public void startWatchingNode(String node)
  {
    if (node.endsWith("/"))
      {
        node = node.substring(0, node.length() - 1);
      }
    gconf_client_add_dir(node);
  }

  /**
   * Remove the node <code>node</code> to the list of nodes the GConf is
   * watching. Note that if a node has been added multiple times, you must
   * remove it the same number of times before the remove takes effect.
   * 
   * @param node the node you don't want to track anymore.
   */
  public void stopWatchingNode(String node)
  {
    if (node.endsWith("/"))
      {
        node = node.substring(0, node.length() - 1);
      }
    gconf_client_remove_dir(node);
  }

  /**
   * Change the value of key to val. Automatically creates the key if it didn't
   * exist before (ie it was unset or it only had a default value).
   * 
   * @param key the key to alter (or add).
   * @param value the new value for this key.
   * @return true if the key was updated, false otherwise.
   */
  public boolean setString(String key, String value)
  {
    if (key.endsWith("/"))
      {
        key = key.substring(0, key.length() - 1);
      }
    return gconf_client_set_string(key, value);
  }

  /**
   * Unsets the value of key; if key is already unset, has no effect. Depending
   * on the GConf daemon, unsetting a key may have the side effect to remove it
   * completely form the database.
   * 
   * @param key the key to unset.
   * @return true on success, false if the key was not updated.
   */
  public boolean unset(String key)
  {
    if (key.endsWith("/"))
      {
        key = key.substring(0, key.length() - 1);
      }
    return gconf_client_unset(key);
  }

  /**
   * Gets the value of a configuration key.
   * 
   * @param key the configuration key.
   * @return the values of this key, null if the key is not valid.
   */
  public String getKey(String key)
  {
    if (key.endsWith("/"))
      {
        key = key.substring(0, key.length() - 1);
      }
    return gconf_client_get_string(key);
  }

  /**
   * Lists the key in the given node. Does not list subnodes. Keys names are the
   * stripped names (name relative to the current node) of the kyes stored in
   * this node.
   * 
   * @param node the node where keys are stored.
   * @return a java.util.List of keys. If there are no keys in the given node, a
   *         list of size 0 is returned.
   */
  public List getKeys(String node) throws BackingStoreException
  {
    if (node.endsWith("/"))
      {
        node = node.substring(0, node.length() - 1);
      }
    return gconf_client_gconf_client_all_keys(node);
  }

  /**
   * Lists the subnodes in <code>node</code>. The returned list contains
   * allocated strings. Each string is the name relative tho the given node.
   * 
   * @param node the node to get subnodes from. If there are no subnodes in the
   *          given node, a list of size 0 is returned.
   */
  public List getChildrenNodes(String node) throws BackingStoreException
  {
    if (node.endsWith("/"))
      {
        node = node.substring(0, node.length() - 1);
      }
    return gconf_client_gconf_client_all_nodes(node);
  }

  /**
   * Suggest to the backend GConf daemon to synch with the database.
   */
  public void suggestSync() throws BackingStoreException
  {
    gconf_client_suggest_sync();
  }
  
  protected void finalize() throws Throwable
  {
    try
      {
        synchronized (semaphore)
          {
            finalize_class();
          }
      }
    finally
      {
        super.finalize();
      }
  }

  /* ***** native methods ***** */

  /*
   * Basicly, these are one to one mappings to GConfClient functions.
   * GConfClient instances are handled by the native layer, and are hidden from
   * the main java class.
   */

  /** */
  native static final private void init_id_cache();
  
  native static final private void init_class();

  native static final private void finalize_class();

  native static final protected boolean gconf_client_dir_exists(String node);

  native static final protected void gconf_client_add_dir(String node);

  native static final protected void gconf_client_remove_dir(String node);

  native static final protected boolean gconf_client_set_string(String key,
                                                                String value);

  native static final protected String gconf_client_get_string(String key);

  native static final protected boolean gconf_client_unset(String key);

  native static final protected void gconf_client_suggest_sync();

  native static final protected List gconf_client_gconf_client_all_nodes(
                                                                         String node);

  native static final protected List gconf_client_gconf_client_all_keys(
                                                                        String node);

  static
    {
      System.loadLibrary("gconfpeer");
      init_id_cache();
    }
}
