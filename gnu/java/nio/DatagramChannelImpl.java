package manta.runtime;


import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.io.*;
import java.net.*;
import java.nio.*;

class DatagramChannelImpl extends DatagramChannel
{
    
    protected DatagramChannelImpl(SelectorProvider provider)
    {
	super(provider);
    }
    

    protected  void implCloseSelectableChannel()
    {
    }
    
    protected  void implConfigureBlocking(boolean  block)
    {
    }

    public int write(java.nio.ByteBuffer  src)
    {
	return 0;
    }

    public long write(java.nio.ByteBuffer[]  srcs, int  offset, int  length)
    {
	return 0;
    }

    public int read(java.nio.ByteBuffer  dst)
    {
	return 0;
    }
    
    DatagramChannel connect(java.net.SocketAddress  remote)
    {
	return null;
    }
    
    DatagramChannel disconnect()
    {
        return null;
    }
    
    boolean isConnected()
    {
	return false;
    }
    
    long read(ByteBuffer[]  dsts, int  offset, int  length)
    {
	return 0;
    }
    
    SocketAddress receive(java.nio.ByteBuffer  dst)
    {
	return null;
    }
    
    int send(java.nio.ByteBuffer  src, java.net.SocketAddress  target)
    {
	return 0;
    }
    
    DatagramSocket socket()
    {
	return null;
    }
}
