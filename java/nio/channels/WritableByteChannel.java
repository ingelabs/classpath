package java.nio.channels;

import java.io.*;
import java.nio.*;


public interface WritableByteChannel extends Channel
{
    public int write(ByteBuffer src) throws IOException;
}
