package gnu.java.nio;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

/** This file is not user visible !
 * But alas, Java does not have a concept of frieldly packages
 * so this class is public. 
 * Instances of this class are created by invoking getChannel
 * Upon a Input/Output/RandomAccessFile object.
 */

public class FileChannelImpl  extends FileChannel
{
    public long address;
    public int length;
    public int fd;
    public MappedByteBuffer buf;
    public Object file_obj; // just to keep it live...
    
    /**
     * This method came from java.io.RandomAccessFile
     * It is private there so we will repeat it here.
     */
    private native long	lengthInternal(int native_fd) throws IOException;

    public FileChannelImpl(int fd,
			   Object obj)
    {
	this.fd       = fd;
	this.file_obj = obj;

	//	System.out.println("file channel: " + fd);
    }

    public long size() throws IOException
    {
	return lengthInternal(fd);

    }
    
    public boolean isOpen()
    {
	// FIXME
	return fd != 0;
    }    

    
    protected void implCloseChannel()  throws IOException
    {
	//System.out.println("length in Java ="+length);
	
	if (address != 0)
	    {
		nio_unmmap_file(fd,
				address,
				(int)length);
	    }

	// FIXME
	fd = 0;

	if (file_obj instanceof RandomAccessFile)
	    {
		RandomAccessFile o = (RandomAccessFile) file_obj;
		o.close();
		//System.out.println("closing stream too");
	    }
	else if (file_obj instanceof FileInputStream)
	    {
		FileInputStream o = (FileInputStream) file_obj;
		o.close();
	    }
	else if (file_obj instanceof FileOutputStream)
	    {
		FileOutputStream o = (FileOutputStream) file_obj;
		o.close();
	    }
    }

    public int read(java.nio.ByteBuffer  dst) throws IOException
    {
	//System.out.println("unimplemented: in here-1");
	
	int w = 0;
	
	int s = (int)size();

	if (buf == null)
	    {
		throw new EOFException("file not mapped");
	    }

	for (int i=0; i<s; i++)
	    {
		dst.put( buf.get() );
	    }

	return s;
    }

    public int write(java.nio.ByteBuffer  src) throws IOException
    {
	int w = 0;

	if (buf == null)
	    {
		throw new EOFException("file not mapped");
	    }

	while (src.hasRemaining())
	    {
		buf.put(src.get());
		w++;
	    }
	return w;
    }
    
    public long write(java.nio.ByteBuffer[]  srcs, 
		      int  offset,
		      int  length) throws IOException
    {
	long res = 0;

	for (int i=offset;i<offset+length;i++)
	    {
		res += write(srcs[i]);
	    }
	return res;
    }
				   

        
    public MappedByteBuffer map(java.nio.channels.FileChannel.MapMode mode,
				long  position,
				int  size) throws IOException
    {
	int cmode = mode.m;

	address = nio_mmap_file(fd, position, size, cmode);

	length = size;

	//	System.out.println("file mapped" + address + "length="+length);

	buf = new MappedByteFileBuffer(this);
	return buf;
    }

    static MappedByteBuffer create_direct_mapped_buffer(long address,
							long length)
    {
	FileChannelImpl ch = new FileChannelImpl(-1, null);
		
	ch.address = address;
	ch.length  = (int)length;

	ch.buf = new MappedByteFileBuffer(ch);
	return ch.buf;			 
    }

    /* msync with the disk */
    public void force(boolean metaData)
    {
	nio_msync(fd, address, length);
    }

    static native long nio_mmap_file(int fd,
					  long pos,
					  int size,
					  int mode);

    static native void nio_unmmap_file(int fd,
				       long address,
				       int size);
    static native void nio_msync(int fd, 
				 long address,
				 int length);
}

