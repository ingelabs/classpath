package gnu.java.nio;

import java.nio.channels.spi.*;
import java.nio.channels.*;


public class SelectorProviderImpl extends SelectorProvider
{
    public SelectorProviderImpl()
    {
    }

    public DatagramChannel openDatagramChannel()
    {
	return new DatagramChannelImpl(this);
    }

    public Pipe openPipe()
    {
	return new PipeImpl();
    }
    
    public AbstractSelector openSelector()
    {
	return new SelectorImpl(this);
    }

    public ServerSocketChannel openServerSocketChannel()
    {
	return new ServerSocketChannelImpl(this);
    }

    public SocketChannel openSocketChannel()
    {
	return new SocketChannelImpl(this);
    }	      
}
