/*
 * GtkMainThread.java -- Runs gtk_main()
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by James E. Blair <corvus@gnu.org>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later verion.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */
package gnu.java.awt.peer.gtk;

public class GtkMainThread extends GtkGenericPeer implements Runnable
{
  private static Thread mainThread = null;
  private static Object mainThreadLock = new Object();

  static native void gtkInit();
  native void gtkMain();
  
  public GtkMainThread() {
    super (null);
    synchronized (mainThreadLock) {
      if (mainThread != null)
	throw new IllegalStateException();
      mainThread = new Thread(this, "GtkMain");
    }

    synchronized (this) {
      mainThread.start();
      try {
	wait();
      } catch (InterruptedException e) { }
    }
  }
  
  public void run() {
    synchronized (this) {
      gtkInit();
      notify();
    }
//      try {
//      Thread.sleep (5000);
//      } catch (InterruptedException e) {}
    gtkMain();
  }
}



