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
 
 float averageBytesPerChar()
    {
	return averageBytesPerChar;
    }

    boolean canEncode(char c)
    {
	return true;
    }
    
 boolean canEncode(CharSequence cs)
    {
	return true;
    }
    
    Charset charset()
    {
	return cs;
    }
    
    ByteBuffer encode(CharBuffer in)
    {
	ByteBuffer x = ByteBuffer.allocate(in.remaining());
	encode(in, x, false);

	return x;
    }
    
    CoderResult encode(CharBuffer in, ByteBuffer out, boolean endOfInput)
    {   
	return encodeLoop(in, out);
    }
    
    
    protected abstract  CoderResult encodeLoop(CharBuffer in, ByteBuffer out);
    
    

    CoderResult flush(ByteBuffer out)
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

 CodingErrorAction malformedInputAction()
    {
	return null;
    }

 float maxBytesPerChar()
    {
	return  maxBytesPerChar;
    }    

    CharsetEncoder onMalformedInput(CodingErrorAction newAction)
    {
	return null;
    }

    CharsetEncoder onUnmappableCharacter(CodingErrorAction newAction)
    {
	return null;
    }
    
    byte[] replacement()
    {
	return repl;
    }

    CharsetEncoder replaceWith(byte[] newReplacement)
    {
	repl = newReplacement;
	return null;
    }

 CharsetEncoder reset()
    {
	return null;
    }

 CodingErrorAction unmappableCharacterAction()
    {
	return null;
    } 
}
