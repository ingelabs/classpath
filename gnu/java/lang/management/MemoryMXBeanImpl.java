/* MemoryMXBeanImpl.java - Implementation of a memory bean
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

package gnu.java.lang.management;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryUsage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

/**
 * Provides access to information about the memory 
 * management of the current invocation of the virtual
 * machine.  Instances of this bean are obtained by calling
 * {@link ManagementFactory#getMemoryMXBean()}.
 *
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 * @since 1.5
 */
public final class MemoryMXBeanImpl
  extends BeanImpl
  implements MemoryMXBean, NotificationEmitter
{

  private List listeners;

  public MemoryMXBeanImpl()
  {
    listeners = new ArrayList();
  }

  public void gc()
  {
    System.gc();
  }

  public MemoryUsage getHeapMemoryUsage()
  {
    return VMMemoryMXBeanImpl.getHeapMemoryUsage();
  }

  public MemoryUsage getNonHeapMemoryUsage()
  {
    return VMMemoryMXBeanImpl.getNonHeapMemoryUsage();
  }

  public int getObjectPendingFinalizationCount()
  {
    return VMMemoryMXBeanImpl.getObjectPendingFinalizationCount();
  }

  public boolean isVerbose()
  {
    return VMMemoryMXBeanImpl.isVerbose();
  }

  public void setVerbose(boolean verbose)
  {
    checkControlPermissions();
    VMMemoryMXBeanImpl.setVerbose(verbose);
  }

  private class ListenerData
  {
    private NotificationListener listener;
    private NotificationFilter filter;
    private Object passback;

    public ListenerData(NotificationListener listener,
			NotificationFilter filter, Object passback)
    {
      this.listener = listener;
      this.filter = filter;
      this.passback = passback;
    }
    
    public NotificationListener getListener()
    {
      return listener;
    }

    public NotificationFilter getFilter()
    {
      return filter;
    }

    public Object getPassback()
    {
      return passback;
    }

    public boolean equals(Object obj)
    {
      if (obj instanceof ListenerData)
	{
	  ListenerData data = (ListenerData) obj;
	  return (data.getListener() == listener &&
		  data.getFilter() == filter &&
		  data.getPassback() == passback);
	}
      return false;
    }

  }

  public void addNotificationListener(NotificationListener listener,
				      NotificationFilter filter,
				      Object passback)
  {
    if (listener == null)
      throw new IllegalArgumentException("Null listener added to bean.");
    listeners.add(new ListenerData(listener, filter, passback));
  }

  public MBeanNotificationInfo[] getNotificationInfo()
  {
    return new MBeanNotificationInfo[]
      {
	new MBeanNotificationInfo(new String[]
	  {
	    MemoryNotificationInfo.MEMORY_COLLECTION_THRESHOLD_EXCEEDED,
	    MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED
	  },
				  Notification.class.getName(), 
				  "Memory Usage Notifications")
      };
  }

  public void removeNotificationListener(NotificationListener listener)
    throws ListenerNotFoundException
  {
    Iterator it = listeners.iterator();
    boolean foundOne = false;
    while (it.hasNext())
      {
	ListenerData data = (ListenerData) it.next();
	if (data.getListener() == listener)
	  {
	    it.remove();
	    foundOne = true;
	  }
      }
    if (!foundOne)
      throw new ListenerNotFoundException("The specified listener, " + listener +
					  "is not registered with this bean.");
  }

  public void removeNotificationListener(NotificationListener listener,
					 NotificationFilter filter,
					 Object passback)
    throws ListenerNotFoundException
  {
    if (!(listeners.remove(new ListenerData(listener, filter, passback))))
      {
	throw new ListenerNotFoundException("The specified listener, " + listener +
					    " with filter " + filter + 
					    "and passback " + passback + 
					    ", is not registered with this bean.");
      }
  }

}
