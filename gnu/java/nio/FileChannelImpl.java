package manta.runtime;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;


public class FileChannelImpl  extends FileChannel
{
    long address;
    int fd;
    MappedByteBuffer buf;
    Object file_obj; // just to keep it live...
    
    FileChannelImpl(int fd,
		    Object obj)
    {
	this.fd       = fd;
	this.file_obj = obj;
    }

    FileChannelImpl(FileDescriptor fd,
		    Object obj)
    {
	this(fd.file_des, obj);
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
    
    public int write(java.nio.ByteBuffer[]  srcs, 
		     int  offset,
		     int  length) throws IOException
    {
	System.out.println("in here-3");
	return 0;
    }

    static MantaNative long nio_mmap_file(int fd,
					  long pos,
					  int size,
					  int mode);

    static MantaNative void nio_unmmap_file(int fd,
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
}

