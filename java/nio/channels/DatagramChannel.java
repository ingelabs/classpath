/* DatagramChannel.java -- 
   Copyright (C) 2002 Free Software Foundation, Inc.

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

package java.nio.channels;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractSelectableChannel;

public abstract class DatagramChannel
  extends AbstractSelectableChannel
  implements ByteChannel, ScatteringByteChannel, GatheringByteChannel
{
  protected DatagramChannel (SelectorProvider provider)
  {
  }
 
  public static DatagramChannel open () throws IOException
  {
    return SelectorProvider.provider ().openDatagramChannel ();
  }
    
  public long read (ByteBuffer[] dsts)
  {
    long b = 0;
    for (int i=0;i<dsts.length;i++)
      b += read(dsts[i]);
    return b;
  }
    
  public abstract DatagramChannel connect (SocketAddress remote);
  public abstract DatagramChannel disconnect ();
  public abstract boolean isConnected ();
  public abstract int read (ByteBuffer dst);
  public abstract long read (ByteBuffer[] dsts, int offset, int length);
  public abstract SocketAddress receive (ByteBuffer dst);
  public abstract int send (ByteBuffer src, SocketAddress target);
  public abstract DatagramSocket socket ();
  public abstract int write (ByteBuffer src);
  public abstract long write (ByteBuffer[] srcs, int offset, int length);

  public int validOps()
  {
    return SelectionKey.OP_READ | SelectionKey.OP_WRITE;
  }    
    
}
