package java.nio.channels.spi;

import java.io.*;
import java.util.*;
import java.nio.channels.*;

public abstract class AbstractSelectableChannel extends SelectableChannel
{
    int registered;
    boolean blocking = true;
    Object LOCK = new Object();
    SelectorProvider sprovider;

    List keys;

    protected abstract  void implCloseSelectableChannel();
    protected abstract  void implConfigureBlocking(boolean block);


    Object blockingLock()
    {
	return LOCK;
	//Retrieves the object upon which the configureBlocking and register methods synchronize. 
    }
    
 SelectableChannel configureBlocking(boolean block)
    {
	synchronized(LOCK)
	    {
		blocking = true;
		implConfigureBlocking(block);
	    }
	//	Adjusts this channel's blocking mode. 
	return this;
    }

protected  void implCloseChannel()
    {
	//     Closes this channel. 
	implCloseSelectableChannel();
    }


boolean isBlocking()
    {
	return blocking;
	//Tells whether or not every I/O operation on this channel will block until it completes.  
    }


boolean isRegistered()
    {
	//Tells whether or not this channel is currently registered with any selectors. 
	return registered > 0;
    }

    public final SelectionKey keyFor(Selector sel)
    {
	//Retrieves the key representing the channel's registration with the given selector.  
	try {
	    return register(sel, 0, null);
	} catch (Exception e) {
	    return null;
	}
    }

    SelectorProvider provider()
    {
	//     Returns the provider that created this channel.  
	return sprovider;
    }

    private SelectionKey locate(Selector sel)
    {
	if (keys == null)
	    return null;

	SelectionKey k = null;
	ListIterator it = keys.listIterator();
	while (it.hasNext())
	    {
		k = (SelectionKey) it.next();
		if (k.selector() == sel)
		    {
			return k;
		    }
	    }
	return k;
    }

    private void add(SelectionKey k)
    {
	if (keys == null)
	    keys = new LinkedList();
	keys.add(k);
    }

    SelectionKey register(Selector selin, 
			  int ops,
			  Object att) throws ClosedChannelException


    {
        if (!isOpen())
	    {
		System.out.println("not open, throwing exception");
		throw new ClosedChannelException();
	    }

	System.out.println("Registers this channel with the given selector, returning a selection key.");

	SelectionKey k = null;

	AbstractSelector sel = (AbstractSelector) selin;

	synchronized (LOCK)
	    {
		k = locate(sel);

		if (k != null)
		    {
			k.attach(att);
		    }
		else
		    {
			k = sel.register(this, ops, att);
			
			if (k != null)
			    {
				add(k);
			    }
		    }
	    }

	return k;
    }
}

