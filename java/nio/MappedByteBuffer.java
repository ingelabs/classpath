package java.nio;


public abstract class MappedByteBuffer extends ByteBuffer
{
    private static MantaNative void sync();

    MappedByteBuffer force()
    {
	sync();
	return this;
    }
    
    boolean isLoaded()
    {
	return true;
    }
    
    MappedByteBuffer load()
    {
	sync();
	return this;
    }
 
}
