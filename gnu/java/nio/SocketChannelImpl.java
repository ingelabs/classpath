package manta.runtime;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;

public class SocketChannelImpl extends SocketChannel
{
    Socket sock_object;
    int fd;
    int local_port;
    boolean blocking = true;
    boolean connected = false;
    InetSocketAddress sa;

    static native int SocketCreate();
    static native int SocketConnect(int fd, InetAddress a, int port);
    static native int SocketBind(int fd, InetAddress host, int port);
    static native int SocketListen(int fd, int backlog);
    static native int SocketAvailable(int fd);
    static native int SocketClose(int fd);
    static native int SocketRead(int fd, byte b[], int off, int len);
    static native int SocketWrite(int fd, byte b[], int off, int len);


    SocketChannelImpl(SelectorProvider provider)		      
    {
	super(provider);

	fd = SocketCreate();

	//System.out.println("socket-channel:"+fd);
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


    int validOps()
    {
    	return SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT;
    }

    protected void implCloseSelectableChannel()
    {
	connected = false;
	SocketClose(fd);
	fd = SocketCreate();
    }

    protected void implConfigureBlocking(boolean  block)
    {
	if (blocking == block)
	    return;
    }   

    boolean connect(SocketAddress  remote)
	throws IOException
    {
	if (connected)
	    {
		throw new AlreadyConnectedException();
	    }

	// ok, lets connect !
	
	sa = (InetSocketAddress) remote;
	
	InetAddress addr = sa.getAddress();
	int         port = sa.getPort();

	//	System.out.println("CONNECT: " + addr + ","+port);
	
	int err = SocketConnect(fd, addr, port);
	
	if (err < 0) 
	    {
		throw new IOException("Connection refused:"+err + ", connect="+err);
	    }

	local_port = err;
	
	connected = true;
	
	return blocking;
    }
    
    boolean finishConnect()
    {
	return false;
    }

    boolean isConnected()
    {
	return connected;
    }
    
    boolean isConnectionPending()
    {
	if (blocking)
	    return false;
	return false;
    }
    Socket socket()
    {
	if (sock_object != null)
	    {
		sock_object.ch = this;
	    }
	return sock_object;
    }


    int read(ByteBuffer  dst)
    {
	int bytes = 0;
	
	int len = 1024;
	byte[]b = new byte[len];
	
	bytes = SocketRead(fd, b, 0, len);
	//System.out.println("readbytes:"+bytes +",len" +len);
	
	dst.put(b, 0, bytes);

	if (bytes == 0)
	    {
		// we've hit eof ?
		return -1;
	    }

	return bytes;
    }
    
    
    long read(ByteBuffer[]  dsts, int  offset, int  length)
    {
	long bytes = 0;
	for (int i=offset; i<length; i++)
	    {
		bytes += read(dsts[i]);
	    }
	return bytes;
    }
     
	
    int write(ByteBuffer  src)
    {
	int bytes = 0;
	
	int len = src.position();

	if (src instanceof ByteBufferImpl)
	    {
		ByteBufferImpl bi = (ByteBufferImpl) src;
		byte[]b = bi.backing_buffer;
		bytes = SocketWrite(fd, b, 0, len);

		//System.out.println("reused memory buffer....");
	    }
	else
	    {
		byte[]b = new byte[len];
		src.get(b, 0, len);
		bytes = SocketWrite(fd, b, 0, len);
	    }
		
	
	
	//System.out.println("WRITEN #bytes="+bytes +",fd=" +fd+","+(char)b[0]+(char)b[1]+(char)b[2]);

	return bytes;
    }

    long write(ByteBuffer[]  srcs, int  offset, int  length)
    {
	long bytes = 0;
	for (int i=offset; i<length; i++)
	    {
		bytes += write(srcs[i]);
	    }
	return bytes;
    }
}
