package java.nio.channels;



public interface Channel
{
    public boolean isOpen();
    public void close() throws IOException;
}
