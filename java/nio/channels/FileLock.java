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
 
    public abstract  boolean isValid();
    public abstract  void release();


    public FileChannel channel()
    {
	return channel;
    }

    public boolean isShared()
    {
	return shared;
    }    

    public boolean overlaps(long position, long size)
    {
	if (position > this.position+this.size)
	    return false;

	if (position+size < this.position)
	    return false;

	return true;
    }

    public long position()
    {
	return position;
    }
    
    public long size()
    {
	return size;
    }

    public String toString()
    {
	return "file-lock:pos="+position+"size="+size;
    }
}
