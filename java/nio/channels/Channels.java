package java.nio.channels;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;


public final class Channels 
{
    public static InputStream newInputStream(ReadableByteChannel ch)
    {
	return null;
    }
    public static OutputStream newOutputStream(final WritableByteChannel ch) 
    {
	return null;
    }
    public static ReadableByteChannel newChannel(final InputStream in)
    {
	return null;
    }
    public static WritableByteChannel newChannel(final OutputStream out)
    {

	return null;	
    }
    public static Reader newReader(ReadableByteChannel ch,
                                   CharsetDecoder dec,
                                   int minBufferCap)
    {
	return null;
    }
    public static Reader newReader(ReadableByteChannel ch,
                                   String csName)
    {
	return newReader(ch, null, 0);
    }
    public static Writer newWriter(final WritableByteChannel ch,
                                   final CharsetEncoder enc,
                                   final int minBufferCap)
    {
	return null;
    }
    public static Writer newWriter(WritableByteChannel ch,
                                   String csName)
    {
	return newWriter(ch, null, 0);
    }
}
