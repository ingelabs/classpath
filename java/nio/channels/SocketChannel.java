package java.nio.channels;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.spi.*;

abstract public class SocketChannel extends AbstractSelectableChannel
{
    static SelectorProvider sys_provider;

    protected SocketChannel(SelectorProvider provider)
    {
    }
 
    static SocketChannel open() throws IOException
    {
	return SelectorProvider.provider().openSocketChannel();
    }
    
    static SocketChannel open(SocketAddress remote) throws IOException
    {
	SocketChannel ch = open();
	
	if (ch.connect(remote))
	    {
	    }
	return ch;
    }
    
    
    long read(ByteBuffer[] dsts)
    {
	long b = 0;
	for (int i=0;i<dsts.length;i++)
	    {
		b+=read(dsts[i]);
	    }
	return b;
    }
    
    long write(ByteBuffer[] dsts)
    {
	long b = 0;
	for (int i=0;i<dsts.length;i++)
	    {
		b+=write(dsts[i]);
	    }
	return b;
    }    
    
    int validOps()
    {
        return SelectionKey.OP_CONNECT  | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
    }


    abstract  int read(ByteBuffer dst);
    abstract  boolean connect(SocketAddress remote);
    abstract  boolean finishConnect();
    abstract  boolean isConnected();
    abstract  boolean isConnectionPending();
    abstract  long read(ByteBuffer[] dsts, int offset, int length);
    abstract  Socket socket();
    abstract  int write(ByteBuffer src);
    abstract  long write(ByteBuffer[] srcs, int offset, int length);
}
