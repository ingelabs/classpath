package java.nio.channels.spi;

import java.io.*;
import java.nio.channels.*;

public abstract class AbstractChannel implements Channel
{
    boolean opened;

    public boolean isOpen()
    {
	return opened;
    }

    public void close() throws IOException
    {
	if (! isOpen())
	    {
		return;
	    }
    }
}

