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
 

    abstract  SocketChannel accept();
    abstract  ServerSocket socket();
    
    static ServerSocketChannel open() throws IOException
    {
	return SelectorProvider.provider().openServerSocketChannel();
    }

    int validOps()
    {
	return SelectionKey.OP_ACCEPT;
    } 
}
