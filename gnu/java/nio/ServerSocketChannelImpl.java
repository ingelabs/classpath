package manta.runtime;

import java.io.*;
import java.nio.channels.spi.*;
import java.nio.channels.*;
import java.net.*;

class ServerSocketChannelImpl extends ServerSocketChannel
{
    ServerSocket sock_object;
    int fd;
    int local_port;
    boolean blocking = true;
    boolean connected = false;
    InetSocketAddress sa;

    private static native int NioSocketAccept(ServerSocketChannelImpl server, 
					      SocketChannelImpl s);

    protected ServerSocketChannelImpl(SelectorProvider provider)
    {
	super(provider);
	fd = SocketChannelImpl.SocketCreate();
    }
 
    public void finalizer()
    {
	if (connected)
	    {
		try {
		    close();
		} catch (Exception e) {
		}
	    }
    }

    protected void implCloseSelectableChannel()
    {
	connected = false;
	SocketChannelImpl.SocketClose(fd);
	fd = SocketChannelImpl.SocketCreate();
    }

    protected void implConfigureBlocking(boolean  block)
    {
    }

    SocketChannel accept()
    {
	SocketChannelImpl result = new SocketChannelImpl(provider());
	    
	result.sa = new InetSocketAddress(0);
	
	int res = NioSocketAccept(this, result);
	
	return result;
    }

    ServerSocket socket()
    {
	return null;
    }
}
