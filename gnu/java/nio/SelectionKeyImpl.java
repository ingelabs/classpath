package gnu.java.nio;

import java.nio.channels.*;
import java.nio.channels.spi.*;

public class SelectionKeyImpl extends  AbstractSelectionKey
{
    int fd, ops;
    SelectorImpl impl;
    SelectableChannel ch;

    public SelectionKeyImpl(SelectableChannel ch,
			    SelectorImpl impl,
			    int fd)
    {
	this.ch   = ch;
	this.impl = impl;
	this.fd   = fd;
    }


    public SelectableChannel channel()
    {
	return ch;
    }


    public int readyOps()
    {
	return 0;
    }

    public int interestOps()
    {
	return ops;    
    }

    public SelectionKey interestOps(int  ops)
    {
	this.ops = ops;
	return this;
    }
    
    public Selector selector()
    {
	return impl;
    }
}
