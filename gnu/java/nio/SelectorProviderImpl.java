package manta.runtime;

import java.nio.channels.spi.*;
import java.nio.channels.*;


public class SelectorProviderImpl extends SelectorProvider
{
    DatagramChannel openDatagramChannel()
    {
	return new DatagramChannelImpl(this);
    }

    Pipe openPipe()
    {
	return new PipeImpl();
    }
    
    AbstractSelector openSelector()
    {
	return new SelectorImpl(this);
    }

    ServerSocketChannel openServerSocketChannel()
    {
	return new ServerSocketChannelImpl(this);
    }

    SocketChannel openSocketChannel()
    {
	return new SocketChannelImpl(this);
    }	      
}
