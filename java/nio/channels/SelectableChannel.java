package java.nio.channels;

import java.nio.channels.spi.*;

public abstract class SelectableChannel extends AbstractInterruptibleChannel
{
    protected SelectableChannel()
    {
    }
    public abstract  Object blockingLock();
    public abstract  SelectableChannel configureBlocking(boolean block);
    public abstract  boolean isBlocking();
    public abstract  boolean isRegistered();
    public abstract  SelectionKey keyFor(Selector sel);
    public abstract  SelectorProvider provider();
    public SelectionKey register(Selector sel, int ops) throws java.nio.channels.ClosedChannelException
    {
	return register(sel, ops, null);
    }
    public abstract  SelectionKey register(Selector sel, int ops, Object att) throws java.nio.channels.ClosedChannelException;
    public abstract  int validOps();  
}
