package java.nio.channels;

import java.net.*;
import java.nio.*;
import java.nio.channels.spi.*;
import java.io.*;



public abstract class DatagramChannel
    extends AbstractSelectableChannel
    implements ByteChannel, ScatteringByteChannel, GatheringByteChannel
{
    protected DatagramChannel(SelectorProvider provider)
    {
    }
 
    public static DatagramChannel open() throws IOException
    {
	return SelectorProvider.provider().openDatagramChannel();
    }
    
    public long read(ByteBuffer[] dsts)
    {
	long b = 0;
	for (int i=0;i<dsts.length;i++)
	    b += read(dsts[i]);
	return b;
    }
    
    public abstract  DatagramChannel connect(SocketAddress remote);
    public abstract  DatagramChannel disconnect();
    public abstract  boolean isConnected();
    public abstract  int read(ByteBuffer dst);
    public abstract  long read(ByteBuffer[] dsts, int offset, int length);
    public abstract  SocketAddress receive(ByteBuffer dst);
    public abstract  int send(ByteBuffer src, SocketAddress target);
    public abstract  DatagramSocket socket();
    public abstract  int write(ByteBuffer src);
    public abstract  long write(ByteBuffer[] srcs, int offset, int length);

    public int validOps()
    {
	return SelectionKey.OP_READ | SelectionKey.OP_WRITE;
    }    
    
    public int write(ByteBuffer[] dsts)
    {
	long b = 0;
	for (int i=0;i<dsts.length;i++)
	    b += write(dsts[i]);
	return (int)b;	
    }
}

	       
