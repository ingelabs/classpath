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
 
static DatagramChannel open() throws IOException
    {
	return SelectorProvider.provider().openDatagramChannel();
    }

 long read(ByteBuffer[] dsts)
    {
	long b = 0;
	for (int i=0;i<dsts.length;i++)
	    b += read(dsts[i]);
	return b;
    }
    
    abstract  DatagramChannel connect(SocketAddress remote);
    abstract  DatagramChannel disconnect();
    abstract  boolean isConnected();
    abstract  public int read(ByteBuffer dst);
    abstract  long read(ByteBuffer[] dsts, int offset, int length);
    abstract  SocketAddress receive(ByteBuffer dst);
    abstract  int send(ByteBuffer src, SocketAddress target);
    abstract  DatagramSocket socket();
    abstract  public int write(ByteBuffer src);
    abstract  public long write(ByteBuffer[] srcs, int offset, int length);

    int validOps()
    {
	return SelectionKey.OP_READ | SelectionKey.OP_WRITE;
    }    
    
    public long write(ByteBuffer[] dsts)
    {
	long b = 0;
	for (int i=0;i<dsts.length;i++)
	    b += write(dsts[i]);
	return b;	
    }
}

	       
