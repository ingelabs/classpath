package java.nio.channels;


import java.nio.*;
import java.io.*;

public interface GatheringByteChannel extends WritableByteChannel
{
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException;
    public int write(ByteBuffer[] srcs) throws IOException;
}
