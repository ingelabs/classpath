package java.nio.channels.spi;

import java.nio.channels.*;
import java.util.*;


public abstract class AbstractSelector extends Selector
{
    boolean closed = true;
    SelectorProvider provider;

    protected AbstractSelector(SelectorProvider provider)
    {
	this.provider = provider;
    }
 

    protected  void begin()
    {
    }

    public void close()
    {
	if (closed)
	    return;
	closed = true;
	implCloseSelector();
    }

    protected  void deregister(AbstractSelectionKey key)
    {
	cancelledKeys().remove(key);
    }
    
    protected  void end()
    {
    }
    
    
    public final boolean isOpen()
    {
	return ! closed;
    }
    
    public SelectorProvider provider()
    {
	return provider;
    }

    protected abstract  void implCloseSelector();	
    protected abstract  SelectionKey register(AbstractSelectableChannel ch, int ops, Object att);   
}
