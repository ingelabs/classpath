package java.nio.charset;


import java.nio.*;

public class Charset
{
    static Charset forName(String name)
    {
	return new Charset();
    }

    CharsetDecoder newDecoder()
    {	
	return new CharsetDecoder(this,2,2)
	    {
		protected CoderResult decodeLoop(ByteBuffer  in,
						 CharBuffer  out)
		{
		    while (in.hasRemaining())
			{
			    char a = (char) in.get();
			    out.put(a);
			}
		    return null;
		}
	    };
    }

    CharsetEncoder newEncoder()
    {		
	return new CharsetEncoder(this,2,2)
	    {
		protected CoderResult encodeLoop(CharBuffer  in,
						 ByteBuffer  out)
		{
		    while (in.hasRemaining())
			{
			    char a = in.get();
			    out.put(a);

			    //int len = out.position();
			    //System.out.println("pos="+len);
			}
		    return null;
		}
	    };
    }
}
