package java.nio.channels;

import java.nio.channels.spi.*;
import java.net.*;
import java.nio.*;
import java.io.*;

public abstract class ServerSocketChannel
    extends AbstractSelectableChannel
{
    protected ServerSocketChannel(SelectorProvider provider)
    {
    }
 

    public abstract  SocketChannel accept();
    public abstract  ServerSocket socket();
    
    public static ServerSocketChannel open() throws IOException
    {
	return SelectorProvider.provider().openServerSocketChannel();
    }

    public int validOps()
    {
	return SelectionKey.OP_ACCEPT;
    } 
}
