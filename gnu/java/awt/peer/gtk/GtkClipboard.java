/* GtkClipboard.java
   Copyright (C) 1999 Free Software Foundation, Inc.

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

package gnu.java.awt.peer.gtk;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.datatransfer.*;
import java.awt.image.*;
import java.awt.peer.*;

public class GtkClipboard extends Clipboard
{
  /* the number of milliseconds that we'll wait around for the
     owner of the GDK_SELECTION_PRIMARY selection to convert 
     the requested data */
  final static int SELECTION_RECEIVED_TIMEOUT = 5000;

  /* We currently only support transferring of text between applications */
  static String selection;
  static Object selectionLock = new Object ();

  static boolean hasSelection = false;

  protected 
  GtkClipboard ()
  {
    super ("System Clipboard");
    initNativeState ();
  }

  public Transferable 
  getContents (Object requestor)
  {
    synchronized (this)
      {
	if (hasSelection)
	  return contents;
      }

    /* Java doesn't own the selection, so we need to ask X11 */
    synchronized (selectionLock)
      {
	requestStringConversion ();
	try 
	  {
	    selectionLock.wait (SELECTION_RECEIVED_TIMEOUT);
	  } 
	catch (InterruptedException e)
	  {
	    return null;
	  }
	
	return (selection == null) ? null : new StringSelection (selection);
      }
  }

  void 
  stringSelectionReceived (String newSelection)
  {
    synchronized (selectionLock)
      {
	selection = newSelection;
	selectionLock.notify ();
      }
  }

  /* convert Java clipboard data into a String suitable for sending
     to another application */
  synchronized String
  stringSelectionHandler () throws IOException
  {
    String selection = null;

    try {
      if (contents.isDataFlavorSupported (DataFlavor.stringFlavor))
	selection = (String)contents.getTransferData (DataFlavor.stringFlavor);
      else if (contents.isDataFlavorSupported (DataFlavor.plainTextFlavor))
	{
	  StringBuffer sbuf = new StringBuffer ();
	  InputStreamReader reader;
	  char readBuf[] = new char[512];
	  int numChars;
	  
	  reader = new InputStreamReader 
	    ((InputStream) 
	     contents.getTransferData (DataFlavor.plainTextFlavor), "UNICODE");
	  
	  while (true)
	    {
	      numChars = reader.read (readBuf);
	      if (numChars == -1)
		break;
	      sbuf.append (readBuf, 0, numChars);
	    }
	  
	  selection = new String (sbuf);
	}
    } catch (Exception e) { }
    
    return selection;
  }

  public synchronized void
  setContents (Transferable contents, ClipboardOwner owner)
  {
    selectionGet ();

    this.contents = contents;
    this.owner = owner;

    hasSelection = true;
  }

  synchronized
  void selectionClear ()
  {
    hasSelection = false;

    if (owner != null)
      {
	owner.lostOwnership (this, contents);
	owner = null;
	contents = null;
      }
  }

  native void initNativeState ();
  native static void requestStringConversion ();
  native static void selectionGet ();
}
