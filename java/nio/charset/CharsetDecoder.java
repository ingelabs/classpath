package java.nio.charset;

import java.nio.*;


public abstract class CharsetDecoder
{
    Charset cs;
    float averageCharsPerByte;
    float maxCharsPerByte;
    String repl;

    protected CharsetDecoder(Charset cs,
			     float averageCharsPerByte, 
			     float maxCharsPerByte)
    {
	this.cs = cs;
	this.averageCharsPerByte = averageCharsPerByte;
	this.maxCharsPerByte     = maxCharsPerByte;
    }
    
    public float averageCharsPerByte()
    {
	return averageCharsPerByte;
    }

    public Charset charset()
    {
	return cs;
    }

    public CharBuffer decode(ByteBuffer in)
    {
	CharBuffer x = CharBuffer.allocate(in.remaining());
	decode(in, x, false);	
	x.rewind();
	return x;
    }
     
    public CoderResult decode(ByteBuffer in, CharBuffer out, boolean endOfInput)
    {
	return decodeLoop(in,out);
    }

    protected abstract  CoderResult decodeLoop(ByteBuffer in, CharBuffer out);

    public Charset detectedCharset()
    {
	return charset();
    }
    
    public CoderResult flush(CharBuffer out)
    {
	return implFlush(out);
    }
    protected  CoderResult implFlush(CharBuffer out)
    {
	return null;
    }
    protected  void implOnMalformedInput(CodingErrorAction newAction)
    {
    }
    protected  void implOnUnmappableCharacter(CodingErrorAction newAction)
    {
    }
    protected  void implReplaceWith(String newReplacement)
    {
    }
    protected  void implReset()
    {
    }

    public boolean isAutoDetecting()
    {
	return true;
    }
    
    public boolean isCharsetDetected()
    {
	return true;
    }
    public CodingErrorAction malformedInputAction()
    {
	return null;
    }
    public float maxCharsPerByte()
    {
	return maxCharsPerByte;
    }
    public CharsetDecoder onMalformedInput(CodingErrorAction newAction)
    {
	return null;
    }
    public CharsetDecoder onUnmappableCharacter(CodingErrorAction newAction)
    {
	return null;
    }
    public String replacement()
    {
	return repl;
    }

    public CharsetDecoder replaceWith(String newReplacement)
    {
	return null;
    }

    public CharsetDecoder reset()
    {
	return null;
    }
    
    public CodingErrorAction unmappableCharacterAction()
    {
	return null;
    }
}
