package java.nio.channels.spi;

import java.nio.channels.*;


public abstract class SelectorProvider
{
    protected SelectorProvider()
    {
    }
    
    abstract  DatagramChannel openDatagramChannel();
    abstract  Pipe openPipe();
    abstract  AbstractSelector openSelector();
    abstract  ServerSocketChannel openServerSocketChannel();
    abstract  SocketChannel openSocketChannel();
    
    
    static SelectorProvider pr;
    
    static SelectorProvider provider()
    {
	if (pr == null)
	    {
		pr = new manta.runtime.SelectorProviderImpl();
	    }
	return pr;
    }
}
