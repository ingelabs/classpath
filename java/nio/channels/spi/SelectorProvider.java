package java.nio.channels.spi;

import java.nio.channels.*;


public abstract class SelectorProvider
{
    protected SelectorProvider()
    {
    }
    
    public abstract  DatagramChannel openDatagramChannel();
    public abstract  Pipe openPipe();
    public abstract  AbstractSelector openSelector();
    public abstract  ServerSocketChannel openServerSocketChannel();
    public abstract  SocketChannel openSocketChannel();
    
    
    static SelectorProvider pr;
    
    public static SelectorProvider provider()
    {
	if (pr == null)
	    {
		pr = new gnu.java.nio.SelectorProviderImpl();
	    }
	return pr;
    }
}
