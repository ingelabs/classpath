package java.nio;


public class ByteOrder
{
    final static ByteOrder BIG_ENDIAN     = new ByteOrder();
    final static ByteOrder LITTLE_ENDIAN  = new ByteOrder();

    static ByteOrder nativeOrder()
    {
	return BIG_ENDIAN;
    }

    public String toString()
    {
	if (this == BIG_ENDIAN)
	    {
		return "big endian";
	    }
	else
	    {
		return "little endian";
	    }
    }
 
}
