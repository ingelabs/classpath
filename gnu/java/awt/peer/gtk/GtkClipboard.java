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
