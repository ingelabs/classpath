package java.nio.channels;

import java.io.IOException;

public interface Channel
{
    public boolean isOpen();
    public void close() throws IOException;
}
