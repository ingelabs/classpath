package java.nio.channels.spi;

import java.nio.channels.*;
import java.io.*;

abstract public class AbstractInterruptibleChannel implements Channel, InterruptibleChannel
{
    boolean opened = false;

    protected AbstractInterruptibleChannel()
    {
    }
 

    protected  void begin()
    {
	//	Marks the beginning of an I/O operation that might block indefinitely.
    }
    
    public void close() throws IOException
    {
	//Closes this channel.
	implCloseChannel();
    }

    protected  void end(boolean completed)
    {
	//Marks the end of an I/O operation that might block indefinitely.
    }

    protected abstract  void implCloseChannel()  throws IOException;

    public boolean isOpen()
    {
	//Tells whether or not this channel is open.
	return opened;
    }
}

