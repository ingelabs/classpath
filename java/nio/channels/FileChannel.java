package java.nio.channels;

import java.io.*;
import java.nio.*;
import java.nio.channels.spi.*;

public abstract class FileChannel  extends AbstractChannel
    implements ByteChannel, GatheringByteChannel, ScatteringByteChannel
{

    public int write(ByteBuffer[]  srcs) throws IOException
    {
	for (int i=0;i<srcs.length;i++)
	    {
		write(srcs[i]);
	    }
	return 0;
    }

    public static class MapMode
    {
	int m;

	public static MapMode READ_ONLY  = new MapMode(0);
	public static MapMode READ_WRITE = new MapMode(1);
	public static MapMode PRIVATE    = new MapMode(2);

	MapMode(int a)
	{
	    m = a;
	}

        public String toString() 
	{
            return ""+m;
        }
    }

    public abstract MappedByteBuffer map(MapMode mode,
					 long position,
					 int size)
        throws IOException;
}

