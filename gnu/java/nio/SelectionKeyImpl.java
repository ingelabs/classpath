package manta.runtime;

import java.nio.channels.*;
import java.nio.channels.spi.*;

class SelectionKeyImpl extends  AbstractSelectionKey
{
    int fd, ops;
    SelectorImpl impl;
    SelectableChannel ch;

    SelectionKeyImpl(SelectableChannel ch,
		     SelectorImpl impl,
		     int fd)
    {
	this.ch   = ch;
	this.impl = impl;
	this.fd   = fd;
    }


    SelectableChannel channel()
    {
	return ch;
    }


    int readyOps()
    {
	return 0;
    }

    int interestOps()
    {
	return ops;    
    }
    SelectionKey interestOps(int  ops)
    {
	this.ops = ops;
	return this;
    }

    
    Selector selector()
    {
	return impl;
    }
}
