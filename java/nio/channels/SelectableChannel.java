package java.nio.channels;

import java.nio.channels.spi.*;

public abstract class SelectableChannel extends AbstractInterruptibleChannel
{
    protected SelectableChannel()
    {
    }
    abstract  Object blockingLock();
    abstract  SelectableChannel configureBlocking(boolean block);
    abstract  boolean isBlocking();
    abstract  boolean isRegistered();
    abstract  SelectionKey keyFor(Selector sel);
    abstract  SelectorProvider provider();
    SelectionKey register(Selector sel, int ops)
    {
	return register(sel, ops, null);
    }
    abstract  SelectionKey register(Selector sel, int ops, Object att);
    abstract  int validOps();  
}
