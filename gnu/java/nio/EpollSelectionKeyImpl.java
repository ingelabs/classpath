/* EpollSelectionKeyImpl.java -- selection key for the epoll selector.
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


package gnu.java.nio;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author Casey Marshall (csm@gnu.org)
 */
public class EpollSelectionKeyImpl extends SelectionKey
{
  final int fd;
  private final EpollSelectorImpl selector;
  private final SelectableChannel channel;
  int interestOps;
  int selectedOps;
  int key;
  boolean valid;
  boolean cancelled;
  
  EpollSelectionKeyImpl(EpollSelectorImpl selector,
                        SelectableChannel channel, int fd)
  {
    this.selector = selector;
    this.channel = channel;
    this.fd = fd;
  }

  /* (non-Javadoc)
   * @see java.nio.channels.SelectionKey#cancel()
   */
  public void cancel()
  {
    cancelled = true;
    selector.cancel(this);
  }

  /* (non-Javadoc)
   * @see java.nio.channels.SelectionKey#channel()
   */
  public SelectableChannel channel()
  {
    return channel;
  }

  /* (non-Javadoc)
   * @see java.nio.channels.SelectionKey#interestOps()
   */
  public int interestOps()
  {
    return interestOps;
  }

  /* (non-Javadoc)
   * @see java.nio.channels.SelectionKey#interestOps(int)
   */
  public SelectionKey interestOps(int ops)
  {
    if (cancelled)
      throw new CancelledKeyException();
    if ((ops & ~(channel.validOps())) != 0)
      throw new IllegalArgumentException("unsupported channel ops");
    try
      {
        selector.epoll_modify(this, ops);
        interestOps = ops;
      }
    catch (IOException ioe)
      {
        throw new IllegalArgumentException(ioe);
      }
    return this;
  }

  /* (non-Javadoc)
   * @see java.nio.channels.SelectionKey#isValid()
   */
  public boolean isValid()
  {
    return valid;
  }

  /* (non-Javadoc)
   * @see java.nio.channels.SelectionKey#readyOps()
   */
  public int readyOps()
  {
    return selectedOps;
  }

  /* (non-Javadoc)
   * @see java.nio.channels.SelectionKey#selector()
   */
  public Selector selector()
  {
    return selector;
  }
}