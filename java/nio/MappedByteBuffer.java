package java.nio;


public abstract class MappedByteBuffer extends ByteBuffer
{
    private static native void sync();

    public MappedByteBuffer force()
    {
	sync();
	return this;
    }
    
    public boolean isLoaded()
    {
	return true;
    }
    
    public MappedByteBuffer load()
    {
	sync();
	return this;
    }
 
}
