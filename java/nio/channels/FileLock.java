package java.nio.channels;


public abstract class FileLock
{
    FileChannel channel;
    long position;
    long size;
    boolean shared;
    
    protected FileLock(FileChannel channel, 
		       long position, 
		       long size,
		       boolean shared)
    {
	this.channel = channel;
	this.position = position;
	this.size = size;
	this.shared = shared;
    }
 
    abstract  boolean isValid();
    abstract  void release();

 FileChannel channel()
    {
	return channel;
    }
 boolean isShared()
    {
	return shared;
    }    

 boolean overlaps(long position, long size)
    {
	if (position > this.position+this.size)
	    return false;

	if (position+size < this.position)
	    return false;

	return true;
    }
 long position()
    {
	return position;
    }
    
 long size()
    {
	return size;
    }

    public String toString()
    {
	return "file-lock:pos="+position+"size="+size;
    }
}
