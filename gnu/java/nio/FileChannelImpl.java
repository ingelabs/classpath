package gnu.java.nio;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;


public class FileChannelImpl  extends FileChannel
{
    public long address, length;
    public int fd;
    public MappedByteBuffer buf;
    public Object file_obj; // just to keep it live...
    
    public FileChannelImpl(int fd,
			   Object obj)
    {
	this.fd       = fd;
	this.file_obj = obj;
    }

    public FileChannelImpl(FileDescriptor fd,
			   Object obj)
    {
	//this(fd.getNativeFD(), obj);
	this(0, obj);
	
	System.err.println("we need to get the native file-des here !\n");
    }
    
    
    public boolean isOpen()
    {
	// FIXME
	return fd != 0;
    }    

    public void close()
    {
    }

    public int read(java.nio.ByteBuffer  dst) throws IOException
    {
	System.out.println("in here-1");
	return 0;
    }

    public int write(java.nio.ByteBuffer  src) throws IOException
    {
	System.out.println("in here-2");
	return 0;
    }
    
    public long write(java.nio.ByteBuffer[]  srcs, 
		     int  offset,
		     int  length) throws IOException
    {
	System.out.println("in here-3");
	return 0;
    }

    static native long nio_mmap_file(int fd,
					  long pos,
					  int size,
					  int mode);

    static native void nio_unmmap_file(int fd,
					    long pos,
					    int size);
        
    public MappedByteBuffer map(java.nio.channels.FileChannel.MapMode mode,
				long  position,
				int  size) throws IOException
    {
	int cmode = 0;

	address = nio_mmap_file(fd, position, size, cmode);

	buf = new MappedByteFileBuffer(this);
	return buf;
    }

    static MappedByteBuffer create_direct_mapped_buffer(long address,
							long length)
    {
	FileChannelImpl ch = new FileChannelImpl(-1, null);
		
	ch.address = address;
	ch.length  = length;

	ch.buf = new MappedByteFileBuffer(ch);
	return ch.buf;			 
    }
}

