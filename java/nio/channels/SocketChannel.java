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
 
    public static SocketChannel open() throws IOException
    {
	return SelectorProvider.provider().openSocketChannel();
    }
    
    public static SocketChannel open(SocketAddress remote) throws IOException
    {
	SocketChannel ch = open();
	
	if (ch.connect(remote))
	    {
	    }
	return ch;
    }
    
    
    public long read(ByteBuffer[] dsts)
    {
	long b = 0;
	for (int i=0;i<dsts.length;i++)
	    {
		b+=read(dsts[i]);
	    }
	return b;
    }
    
    public long write(ByteBuffer[] dsts)
    {
	long b = 0;
	for (int i=0;i<dsts.length;i++)
	    {
		b+=write(dsts[i]);
	    }
	return b;
    }    
    
    public int validOps()
    {
        return SelectionKey.OP_CONNECT  | SelectionKey.OP_READ | SelectionKey.OP_WRITE;
    }


    public abstract  int read(ByteBuffer dst);
    public abstract  boolean connect(SocketAddress remote) throws IOException;
    public abstract  boolean finishConnect();
    public abstract  boolean isConnected();
    public abstract  boolean isConnectionPending();
    public abstract  long read(ByteBuffer[] dsts, int offset, int length);
    public abstract  Socket socket();
    public abstract  int write(ByteBuffer src);
    public abstract  long write(ByteBuffer[] srcs, int offset, int length);
}
