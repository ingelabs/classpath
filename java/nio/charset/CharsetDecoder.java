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
 
    float averageCharsPerByte()
    {
	return averageCharsPerByte;
    }

    Charset charset()
    {
	return cs;
    }

    CharBuffer decode(ByteBuffer in)
    {
	CharBuffer x = CharBuffer.allocate(in.remaining());
	decode(in, x, false);
	return x;
    }
     
    CoderResult decode(ByteBuffer in, CharBuffer out, boolean endOfInput)
    {
	return decodeLoop(in,out);
    }
    protected abstract  CoderResult decodeLoop(ByteBuffer in, CharBuffer out);

    Charset detectedCharset()
    {
	return charset();
    }
    CoderResult flush(CharBuffer out)
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
 boolean isAutoDetecting()
    {
	return true;
    }
 boolean isCharsetDetected()
    {
	return true;
    }
 CodingErrorAction malformedInputAction()
    {
	return null;
    }
 float maxCharsPerByte()
    {
	return maxCharsPerByte;
    }
 CharsetDecoder onMalformedInput(CodingErrorAction newAction)
    {
	return null;
    }
 CharsetDecoder onUnmappableCharacter(CodingErrorAction newAction)
    {
	return null;
    }
 String replacement()
    {
	return repl;
    }

 CharsetDecoder replaceWith(String newReplacement)
    {
	return null;
    }

 CharsetDecoder reset()
    {
	return null;
    }
 CodingErrorAction unmappableCharacterAction()
    {
	return null;
    }
}
