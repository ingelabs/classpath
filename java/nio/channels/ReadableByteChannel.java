package java.nio.channels;


import java.io.*;
import java.nio.*;


public interface ReadableByteChannel extends Channel
{
    public int read(ByteBuffer dst) throws IOException;
}
