package java.nio.charset;


import java.nio.*;

public class Charset
{
    public static Charset forName(String name)
    {
	return new Charset();
    }

    public CharsetDecoder newDecoder()
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

    public CharsetEncoder newEncoder()
    {		
	return new CharsetEncoder(this,2,2)
	    {
		protected CoderResult encodeLoop(CharBuffer  in,
						 ByteBuffer  out)
		{
		    //System.out.println("in encode loop:"+in.hasRemaining());

		    while (in.hasRemaining())
			{
			    char a = in.get();
			    out.put((byte)a);

			    //int len = out.position();
			    //System.out.println("pos="+len + ","+a);
			}
		    return null;
		}
	    };
    }
}
