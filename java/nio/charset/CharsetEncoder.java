package java.nio.charset;

import java.nio.*;


public abstract class CharsetEncoder
{
    Charset cs;
    float averageBytesPerChar;
    float maxBytesPerChar;
    byte[] repl;
    
    protected CharsetEncoder(Charset cs,
			     float averageBytesPerChar,
			     float maxBytesPerChar)
    {
	this(cs, averageBytesPerChar, maxBytesPerChar,  null);
    }

    protected CharsetEncoder(Charset cs, 
			     float averageBytesPerChar, 
			     float maxBytesPerChar, 
			     byte[] replacement)
    {
	this.cs = cs;
	this.maxBytesPerChar = maxBytesPerChar;
	this.averageBytesPerChar = averageBytesPerChar;
	this.repl = replacement;
    }
 
    public float averageBytesPerChar()
    {
	return averageBytesPerChar;
    }

    public boolean canEncode(char c)
    {
	return true;
    }
    
    public boolean canEncode(CharSequence cs)
    {
	return true;
    }
    
    public Charset charset()
    {
	return cs;
    }
    
    public ByteBuffer encode(CharBuffer in)
    {
	ByteBuffer x = ByteBuffer.allocate(in.remaining());
	encode(in, x, false);
	return x;
    }
    
    public CoderResult encode(CharBuffer in, ByteBuffer out, boolean endOfInput)
    {   
	return encodeLoop(in, out);
    }
    
    
    protected abstract  CoderResult encodeLoop(CharBuffer in, ByteBuffer out);
    
    public CoderResult flush(ByteBuffer out)
    {
	return implFlush(out);
    }

    protected  CoderResult implFlush(ByteBuffer out)
    {
	return null;
    }

    protected  void implOnMalformedInput(CodingErrorAction newAction)
    {
    }

    protected  void implOnUnmappableCharacter(CodingErrorAction newAction)
    {
    }

    protected  void implReplaceWith(byte[] newReplacement)
    {
    }
    
    protected  void implReset()
    {
    }

 boolean isLegalReplacement(byte[] repl)
    {
	return true;
    }


    public CodingErrorAction malformedInputAction()
    {
	return null;
    }

    public float maxBytesPerChar()
    {
	return  maxBytesPerChar;
    }    

    public CharsetEncoder onMalformedInput(CodingErrorAction newAction)
    {
	return null;
    }

    public CharsetEncoder onUnmappableCharacter(CodingErrorAction newAction)
    {
	return null;
    }
    
    public byte[] replacement()
    {
	return repl;
    }

    public CharsetEncoder replaceWith(byte[] newReplacement)
    {
	repl = newReplacement;
	return null;
    }

    public CharsetEncoder reset()
    {
	return null;
    }

    public CodingErrorAction unmappableCharacterAction()
    {
	return null;
    } 
}
