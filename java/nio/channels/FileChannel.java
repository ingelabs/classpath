package java.nio.channels;

import java.io.*;
import java.nio.*;
import java.nio.channels.spi.*;

public abstract class FileChannel  extends AbstractInterruptibleChannel
    implements ByteChannel, GatheringByteChannel, ScatteringByteChannel
{

    public int write(ByteBuffer[]  srcs) throws IOException
    {
	long p = write(srcs, 0, srcs.length);
    	return (int) p;
    }

    public static class MapMode
    {
	public int m;

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

    /**
     * Return the size of the file thus far
     */
    public abstract long size() throws IOException;
    public abstract long write(ByteBuffer[] srcs, int offset, int length) throws IOException;
    public abstract int read(ByteBuffer dst) throws IOException;
    public abstract int write(ByteBuffer src) throws IOException;
    protected abstract  void implCloseChannel()  throws IOException;

    /* msync with the disk */
    public abstract  void force(boolean metaData);    
}

